package examples;

import java.io.FileNotFoundException;
import netInt.GraphPad;

public class GraphPadInstance {

	GraphPadInstance() {
		try {
			String filePath = "./data/graphs/samples/Risk.graphml";
			new GraphPad(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GraphPadInstance();
	}
}