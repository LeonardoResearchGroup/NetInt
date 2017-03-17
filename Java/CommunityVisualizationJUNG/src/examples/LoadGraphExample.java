package examples;

import java.io.File;

import netInt.GraphPad;
import netInt.containers.Container;
import netInt.utilities.Assembler;
import netInt.utilities.GraphLoader;

/**
 * Example for Graphml file formats
 * 
 * @author jsalam
 *
 */
public class LoadGraphExample {

	public LoadGraphExample() {

		// The path to the source file
		File file = new File("./data/graphs/samples/Risk.graphml");

		// The visualization pad
		GraphPad gp = new GraphPad(false);

		//System.out.println("app A: " + GraphPad.gp.getAssembler());

		// In Graphml format, node attributes copied from the graphml file.
		String[] nodeAtts = { "Continent" };

		// In Graphml format, edge attributes copied from the graphml file
		String[] edgeAtts = { "weight" };

		// The node distribution layout
		int layout = Container.FRUCHTERMAN_REINGOLD;

		// Graphml. Pajek not ready yet
		int graphFormat = GraphLoader.GRAPHML;

		// Load the graph
		System.out.println("f" + file);
		System.out.println("n" + nodeAtts);
		System.out.println("e" + edgeAtts);
		System.out.println("l" + layout);
		System.out.println("g" + graphFormat);
//		System.out.println("app" + pad.getAssembler());
		
		Assembler assmb = new Assembler(Assembler.HD720);
		assmb.loadGraph(file, nodeAtts, edgeAtts, layout, graphFormat);
//		if (pad.getAssembler().loadGraph(file, nodeAtts, edgeAtts, layout, graphFormat)) {
//			// Activate graph in the visualization pad
//			GraphPad.setActiveGraph(true);
//		}
		gp.setAssembler(assmb);
	}

	public static void main(String[] args) {
		new LoadGraphExample();
	}
}
