set "M=200000"
set "t=0.1 0.01"

 for %%i in (%M%) do (
 	for %%j in (%t%) do (
  		java -jar QGE.jar RealData/nethylene_methane.txt %%j %%i
	)
)