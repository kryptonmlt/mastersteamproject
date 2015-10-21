package generateDataSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class WriteFile {
	
	public static void writeFile(String filename, double[] dou) {
		
		File file = new File(filename);
		StringBuffer str = new StringBuffer();
		try {
			str.append(dou[0]+","+dou[1]+"\r\n");
			//get output writer
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(   
	                  new FileOutputStream(file, true)));
			output.write(str.toString());
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
