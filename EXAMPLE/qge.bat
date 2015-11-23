set "M=100000"
set "t=0.1"

 for %%i in (%M%) do (
 	for %%j in (%t%) do (
  		::java -jar QGE.jar RealData/nethylene_CO_2sensor.txt %%j %%i 10 0.05 L.txt
  		java -jar QGE.jar AVGData/QueryClustering/nethylene_CO_2sensor.txt %%j %%i
	)
)