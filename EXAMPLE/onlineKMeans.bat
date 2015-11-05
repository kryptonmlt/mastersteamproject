set "k=5 50 100"

 for %%i in (%k%) do (
  	java -jar OnlineKMeans.jar AVGDATA_0.01_1000.txt %%i 0.05
)