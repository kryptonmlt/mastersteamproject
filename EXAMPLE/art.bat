set "V=0.01 0.1 0.2"

 for %%i in (%V%) do (
	java -jar ART.jar AVGData/ethCO/AVGDATA_0.1_200000.txt %%i 0.05
)