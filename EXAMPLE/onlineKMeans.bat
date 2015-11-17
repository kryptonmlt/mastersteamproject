set "k=10"

 for %%i in (%k%) do (
  	java -jar OnlineKMeans.jar AVGDATA_0.1_100000.txt %%i 0.05
)