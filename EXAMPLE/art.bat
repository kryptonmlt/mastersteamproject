set "V=0.5 0.7 1.0"

 for %%i in (%V%) do (
	java -jar ART.jar AVGData/ethME/500kDataPoints/AVGDATA_0.05_200000.txt %%i 0.05
)