------------------------------------------------------------
Note:
All these projects can be opened using ECLIPSE
All are able to work with multidimensional data.
------------------------------------------------------------
QGE:
contains the Prediction, Learning, and TrainingSet Generation Applications.

Inside it there is also the ART and Online K-means and both implement the same interface to simplify their usage.
To use any of these Clustering techniques just import this project/jar and use:
Clustering art = new ART(row,alpha);
Clustering online = new OnlineKmeans(K,alpha);
------------------------------------------------------------
DataSetTask2
The aim of this is to read from a data file and then produce a normalized output.
It takes for inputs:
Data File = file containt data in format: x,y,z,...\n,x,y,z,...\n,etc...
start point = example from column 2
end point = example to column
rowLimit = example first 100000 rows
------------------------------------------------------------
KmeansClustering:
contains the batch kmeans.
It takes 2 parameters: data file and number of clusters.
------------------------------------------------------------
ClusterVisualizer
Takes a file with data points and another file with their associated clusters. Then visualizes them on a graph.
Data File:
x,y
x,y
x,y
etc..
cluster File:
0,
5,
5,
etc...
------------------------------------------------------------