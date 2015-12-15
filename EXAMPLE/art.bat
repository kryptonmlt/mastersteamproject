set "V=0.9 1.0"

 for %%i in (%V%) do (
	java -jar ART.jar AVGDATA_0.2_200000.txt %%i 0.05
)