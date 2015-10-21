package org.masters.qge.storage;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {

	private String[] columns;
	private List<Data> dataSet = new ArrayList<Data>();
	private static DataStorage instance = null;

	private DataStorage() {

	}

	public static DataStorage getInstance() {
		if (instance == null) {
			instance = new DataStorage();
		}
		return instance;
	}

	public void setColumns(String[] columns) {
		this.columns = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			this.columns[i] = columns[i].trim();
		}
	}

	public List<Data> getDataSet() {
		return dataSet;
	}

	public void setDataSet(List<Data> dataSet) {
		this.dataSet = dataSet;
	}

	public String[] getColumns() {
		return columns;
	}

}
