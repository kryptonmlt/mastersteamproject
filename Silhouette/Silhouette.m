%here you need to enter the path to the data file
data = csvread('C:\\Users\\Desktop\DATA.txt'); 
%here you need to enter the file path to the clusters 
kmeans = csvread('C:\\Users\\Desktop\pointsclusters.txt');

%nothing to change here
s=silhouette(data,kmeans,'Euclidean');
mean_silhouette=mean(s);
disp(mean_silhouette)

%to run it just press strg+enter inside the script (opend in Matlab) or go
%to tab editor and press run section

%write the output in an excel or somewhere it should be only one number 
%so if you run it for offline k means with k=5 you get the sil=0.35 for
%example and so on ....
%if sil near to 1 it is a perfect cluster if to 0 it is a weak cluster