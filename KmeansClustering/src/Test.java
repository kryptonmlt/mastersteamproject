import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;


public class Test {

	public static void main(String[] args) {
		
		Kmeans km=new Kmeans("data.txt",5);
		LinkedList<double[]> centroids=km.update();
		for(int i=0; i<centroids.size();i++){
			System.out.print(centroids.get(i)[0]+",");
			System.out.print(centroids.get(i)[1]+"\n");
		}
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("centroids.csv")));
			for (int i = 0; i < centroids.size(); i++) {
				out.write(centroids.get(i)[0]+","+centroids.get(i)[1]+"\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
