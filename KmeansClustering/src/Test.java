import java.util.LinkedList;


public class Test {

	public static void main(String[] args) {
		
		Kmeans km=new Kmeans("data.txt",5);
		LinkedList<double[]> centroids2=km.update();
		for(int i=0; i<centroids2.size();i++){
			System.out.print(centroids2.get(i)[0]+",");
			System.out.print(centroids2.get(i)[1]+"\n");
		}

	}

}
