set "k=5 10 50 100"

 for %%i in (%k%) do (
  java -jar OnlineKMeans.jar DATAa.txt %%i 0.02
)