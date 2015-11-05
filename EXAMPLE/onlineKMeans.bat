set "k=20 100"

 for %%i in (%k%) do (
  	java -jar OnlineKMeans.jar RealData/nethylene_CO_min.txt %%i 0.05
)