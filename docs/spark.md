# Load mysql data to Spark

There are 2 methods for using Spark with other Databases:

1. JdbcRDD --> outdated: [Article](http://dev.mysql.com/downloads/connector/j/)
2. Data source, returns DataFrame & can be easily processed with Spark SQL

```
# Start spark-shell with JDBC connector in classpath:
SPARK_CLASSPATH=./mysql-connector-java-5.1.37/mysql-connector-java-5.1.37-bin.jar ./spark-1.5.1-bin-hadoop2.4/bin/spark-shell

# Now we're inside scala REPL
scala> val df = sqlContext.read.format("jdbc").options(
|   Map("url" -> "jdbc:mysql://localhost:3306/EventsRecommendation?user=root&password=",
|       "dbtable" -> "user")).load()

# Playing around
scala> df.printSchema()
scala> df.filter(df("timezone") === 480).show()
```

[More about DataFrame](http://spark.apache.org/docs/latest/sql-programming-guide.html)

# MLLib

[Article](http://spark.apache.org/docs/latest/mllib-guide.html#sparkml-high-level-apis-for-ml-pipelines)

Divided into 2 packages:

1. spark.mllib --> API built on top of RDDs
2. spark.ml --> API on top of DataFrames --> what we're using

## spark.ml

- DataFrame: ML dataset, have multiple columns storing different types
- Transformer: algo for transform 1 DataFrame --> other
- Estimator: algo which can be fit on DataFrame to produce Transformer
- Pipeline: chains multiple Transformers & Estimators together
- Parameter: common API of Transformers & Estimators