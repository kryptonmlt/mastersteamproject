set "k=50 150 400"

 for %%i in (%k%) do (
  	java -jar OnlineKMeans.jar RealData\nethylene_methane_min.txt %%i 0.05
)