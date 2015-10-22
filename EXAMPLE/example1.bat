java -jar QGE.jar sampleData.txt 0.01 10000 true
java -jar KMEANS.jar AVGDATA.txt 5
java -jar ClusterVisualizer.jar sampleData.txt pointsclusters.csv
#silhoute.exe AVGDATA.txt pointsclusters.csv > output1.txt