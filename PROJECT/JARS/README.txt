JARs Description:
TrainingSet.jar generates the training set from the Real Data.
Learning.jar learns the training set and generates the query/data clusters.
Prediction.jar uses these clusters to predict the results using an evaluation set and then calculate the error.
------------------------------------------------
How To Run (from terminal with java8 installed):
java -jar TrainingSet.jar DATAa.txt 0.1 10000 L.txt
java -jar Learning.jar trainingSet_0.1_20000.txt 10 0.05
java -jar Prediction.jar queryDataMap_10_0.05.txt trainingSet_0.1_10000.txt
------------------------------------------------
Input/Output
------------------
TrainingSet:
Input: Data File, theta, queryLimit, L File
Output: TrainingSet File
------------------
Learning:
Input: TrainingSet File, K, alpha
Output: QueryDataMap File
------------------
Prediction:
Input: QueryDataMap File, EvaluationSet File
Output: results.txt
------------------------------------------------
