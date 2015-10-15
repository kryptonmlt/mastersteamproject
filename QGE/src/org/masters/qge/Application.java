package org.masters.qge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.masters.qge.storage.Data;
import org.masters.qge.storage.DataStorage;

public class Application {

	public static void main(String args[]) throws IOException {

		if (args.length < 1) {
			throw new IllegalArgumentException("path to input file needed..");
		}

		File input = new File(args[0]);
		if (!input.exists()) {
			throw new IllegalArgumentException("File does not exist");
		}
		// read file and populate Data Store
		BufferedReader br = new BufferedReader(new FileReader(input));
		String temp = br.readLine();

		DataStorage.getInstance().setColumns(temp.split(","));
		boolean completed = false;
		try {
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				if (d.length != DataStorage.getInstance().getColumns().length) {
					throw new IllegalArgumentException("Number of columns doesnt match data in input file..." + temp);
				}
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				DataStorage.getInstance().getDataSet().add(new Data(row));
			}
			completed = true;
		} finally {
			br.close();

		}
		if (completed) { //no errors in populating data

		}
	}
}
