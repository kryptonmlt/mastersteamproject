set "V=0.1"

 for %%i in (%V%) do (
	java -jar ART.jar AVGDATA_0.1_100000.txt %%i 0.05
)