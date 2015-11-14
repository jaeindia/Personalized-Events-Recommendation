from pyspark.ml import Pipeline
from pyspark.ml.classification import DecisionTreeClassifier
from pyspark.ml.feature import StringIndexer, VectorIndexer
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.mllib.linalg import Vectors
from pyspark.sql import Row


# Needed for toDF to work
def build_decision_tree(sqlContext, features, interested):
	print '-----------------------------------------'
	data = sqlContext.createDataFrame(
			[Row(label=interested[i],features=Vectors.dense(features[i])) for i in xrange(len(features))])
	data.printSchema()
	data.show(5)
	print 'created data frame'

	# Index the label column & adding metadata.
	labelIndexer = StringIndexer(inputCol="label", outputCol="indexedLabel").fit(data)
	print 'created label indexer'

	# Mark the features with < 4 distinct values as categorical
	featureIndexer = VectorIndexer(inputCol="features", outputCol="indexedFeatures", maxCategories=4).fit(data)

	# Split the data into training and test sets
	(trainingData, testData) = data.randomSplit([0.8, 0.2])

	# Train a DecisionTree model
	dt = DecisionTreeClassifier(labelCol="indexedLabel", featuresCol="indexedFeatures")

	# Chain the indexers together with DecisionTree
	pipeline = Pipeline(stages=[labelIndexer, featureIndexer, dt])

	# Train the model
	model = pipeline.fit(trainingData)

	# Make predictions
	predictions = model.transform(testData)

	predictions.select("prediction", "indexedLabel", "features").show(5)

	# Select (prediction, true label) & compute test error
	evaluator = MulticlassClassificationEvaluator(
			labelCol="indexedLabel", predictionCol="prediction", metricName="precision")
	precision = evaluator.evaluate(predictions)
	print "Error = {}".format(1 - precision)

	treeModel = model.stages[2]
	return treeModel
