set "k=50000"

 for %%i in (%k%) do (
  java -jar QGE.jar nethylene_CO.txt 0.01 %%i
)