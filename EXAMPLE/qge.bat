set "k=1000 20000 100000"
set "t=0.1 0.01"

 for %%i in (%k%) do (
 	for %%j in (%t%) do (
  		java -jar QGE.jar RealData/neth.txt %%j %%i
	)
)