package generateDataSet;


import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Workbook; 
import org.apache.poi.ss.usermodel.WorkbookFactory; 

public class ExportXls {

    //use POI.jar to read excel file
    public void exportXls() throws Exception { 
        //create input stream
        InputStream inp = new FileInputStream("Data_User_Modeling_Dataset_Hamdi Tolga KAHRAMAN.xls"); 
         
        //create workbook object
        Workbook wb = WorkbookFactory.create(inp); 
        //get Training_Data sheet 
        Sheet sheet = wb.getSheetAt(1); 
        Cell cellSTG = null;
        Cell cellPEG = null;
        int ignoreRows = 1;
        double[] dou = {-1,-1};
        for (int rowIndex = ignoreRows; rowIndex <= sheet.getLastRowNum(); rowIndex++) { 
			Row row = sheet.getRow(rowIndex);
			int columnIndexSTG = 0;
			int columnIndexPEG = 4;
			cellSTG = row.getCell(columnIndexSTG);
			cellPEG = row.getCell(columnIndexPEG);
			dou[0] = cellSTG.getNumericCellValue();
			dou[1] = cellPEG.getNumericCellValue();
	    	BigDecimal a1 = new BigDecimal(dou[0]);
	    	BigDecimal b1 = new BigDecimal(dou[1]);
	    	BigDecimal c1 = new BigDecimal("0.5");
	    	BigDecimal d1 = new BigDecimal("1");
	    	a1 = a1.subtract(c1).divide(d1, 15, BigDecimal.ROUND_HALF_UP);
	    	b1 = b1.subtract(c1).divide(d1, 15, BigDecimal.ROUND_HALF_UP);
	    	dou[0] = a1.doubleValue();
	    	dou[1] = b1.doubleValue();
	    	System.out.println(dou[0]+","+dou[1]+"\r");
	    	WriteFile.writeFile("DATA.txt",dou);
        } 
        inp.close(); 
    } 
}
