set "V=0.1 0.2 0.3 0.4"

 for %%i in (%V%) do (
	java -jar ART.jar queries_0.1_200000.txt %%i 0.05
)