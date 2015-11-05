// To run: SPARK_CLASSPATH=./mysql-connector-java-5.1.37/mysql-connector-java-5.1.37-bin.jar ./spark-1.5.1-bin-hadoop2.4/bin/spark-shell -i Spark/mysql.scala
val df = sqlContext.read.format("jdbc").options(
        Map("url" -> "jdbc:mysql://localhost:3306/EventsRecommendation?user=root&password=",
        "dbtable" -> "user")).load()
df.printSchema()
df.filter(df("timezone") === 480).show()
