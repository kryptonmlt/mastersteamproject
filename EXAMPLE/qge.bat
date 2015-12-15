set "M=100000"
set "t=0.05 0.2"

 for %%i in (%M%) do (
 	for %%j in (%t%) do (
  		java -jar QGE.jar RealData/nethylene_methane_min.txt %%j %%i
	)
)