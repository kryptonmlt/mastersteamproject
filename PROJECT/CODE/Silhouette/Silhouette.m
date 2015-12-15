% the path to the data file
data = csvread('C:\Users\Public\Desktop\AVGDATA_0.1_100000.txt'); 

% the file path to the clusters 
kmeans = csvread('C:\Users\Public\Desktop\pointsclusters_10_0.05.txt');

% silhouette function
s=silhouette(data,kmeans,'Euclidean');

% average silhouette coefficient
mean_silhouette=mean(s);
disp(mean_silhouette)
