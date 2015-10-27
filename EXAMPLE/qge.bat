set "k=1000 5000 10000 50000 100000"

 for %%i in (%k%) do (
  java -jar QGE.jar DATAb.txt 0.01 %%i
)