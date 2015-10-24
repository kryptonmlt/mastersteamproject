package generateDataSet;


import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.math.BigDecimal;



public class ExportArff {

    public void exportArff() throws Exception {
		// TODO Auto-generated method stub
		DataSource source;
		try {
			source = new DataSource("eb.arff");
			Instances data = source.getDataSet();

			// setting class attribute if the data format does not provide this
			//information
			// E.g., the XRFF format saves the class attribute information as well
			if (data.classIndex() == -1) 
			data.setClassIndex(data.numAttributes() - 1);
		    String[] options = new String[2];
		    options[0] = "-R";                             // "range"
		    options[1] = "3-last";                              // first attribute
		    Remove remove = new Remove();                 // new instance of filter
		    remove.setOptions(options);                  // set options
		    // inform filter about dataset //**AFTER** setting options
		    remove.setInputFormat(data); 
		    Instances newData = Filter.useFilter(data, remove);   // apply filter
		    for( int i = 0; i < newData.numInstances(); i++ ) {
		    	Instance ins = newData.instance(i);
		    	double[] dou = ins.toDoubleArray();
		    	BigDecimal a1 = new BigDecimal(dou[0]);
		    	BigDecimal b1 = new BigDecimal(dou[1]);
		    	BigDecimal c1 = new BigDecimal("0.5");
		    	BigDecimal d1 = new BigDecimal("1");
		    	a1 = a1.subtract(c1).divide(d1, 15, BigDecimal.ROUND_HALF_UP);
		    	b1 = b1.subtract(c1).divide(d1, 15, BigDecimal.ROUND_HALF_UP);
		    	dou[0] = a1.doubleValue();
		    	dou[1] = b1.doubleValue();
		    	System.out.println(dou[0]+","+dou[1]+"\r");
		    	WriteFile.writeFile("DATAa.txt",dou);
		    }
		    System.out.println(newData.toSummaryString()); 
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
