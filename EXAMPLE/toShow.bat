java -jar OnlineKMeans.jar DATAa.txt 100 0.05
java -jar ClusterVisualizer.jar DATAa.txt pointsclusters_100_0.05.txt
java -jar ClusterVisualizer.jar DATAa.txt pointsclusters2_100_0.05.txt
java -jar OnlineKMeans.jar AVGData\AVGDATAa\0.01\AVGDATA_50000.txt 100 0.05
java -jar ClusterVisualizer.jar AVGData\AVGDATAa\0.01\AVGDATA_50000.txt pointsclusters_100_0.05.txt
java -jar ClusterVisualizer.jar AVGData\AVGDATAa\0.01\AVGDATA_50000.txt pointsclusters2_100_0.05.txt