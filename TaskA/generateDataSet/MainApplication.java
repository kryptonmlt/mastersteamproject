package generateDataSet;

public class MainApplication {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			ExportArff arf = new ExportArff();
			ExportXls xls = new ExportXls();
			arf.exportArff();
			xls.exportXls();
	}

}
