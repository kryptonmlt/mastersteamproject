::java -jar KMEANS.jar AVGData\AVGDATAa\0.01\AVGDATA_50000.txt 100
::java -jar ClusterVisualizer.jar AVGData\AVGDATAa\0.01\AVGDATA_50000.txt pointsclusters_100.csv
java -jar KMEANS.jar DATAa.txt 10
java -jar ClusterVisualizer.jar DATAa.txt pointsclusters_10.csv
::java -jar KMEANS.jar AVGData\AVGDATAb\0.01\AVGDATA_1000.txt 10
::java -jar ClusterVisualizer.jar AVGData\AVGDATAb\0.01\AVGDATA_1000.txt pointsclusters_10.csv