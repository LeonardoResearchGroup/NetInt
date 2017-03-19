package examples;

import java.io.File;

import netInt.GraphPad;
import netInt.containers.Container;
import netInt.utilities.GraphLoader;
import processing.core.PApplet;

/**
 * 
 * @author jsalam
 *
 */
public class GraphPad_Example extends PApplet {
	GraphPad pad;

	/**
	 * Required method from parent class. Define here the size of the
	 * visualization pad
	 * 
	 * @see processing.core.PApplet#settings()
	 */
	public void settings() {
		size(displayWidth - 201, displayHeight - 100, P2D);
	}

	/**
	 * Required method from parent class. It runs only once at the PApplet
	 * initialization. Instantiate the classes and initialize attributes
	 * declared in this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {
		textSize(10);
		surface.setLocation(200, -300);
		smooth();

		pad = new GraphPad(this);

		// The path to the source file
		File file = new File("./data/graphs/samples/Risk.graphml");

		// In Graphml format, node attributes copied from the graphml file. The
		// first one defines the communities, the second the node names
		String[] nodeAtts = { "Continent", "label" };

		// In Graphml format, edge attributes copied from the graphml file. The
		// first one defines edge thickness
		String[] edgeAtts = { "weight" };

		// The node distribution layout
		int layout = Container.FRUCHTERMAN_REINGOLD;

		// Graphml. Pajek not ready yet
		int graphFormat = GraphLoader.GRAPHML;

		// Load the graph
		if (GraphPad.app.loadGraph(file, nodeAtts, edgeAtts, layout, graphFormat)) {
			// Activate graph in the visualization pad
			pad.setActiveGraph(true);
		}
	}

	/**
	 * Required method from parent class. It draws visualElements and other
	 * PApplet elements on the visualization pad. It constantly iterates over
	 * its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		background(70);
		pad.show();
	}

	public static void main(String[] args) {
		PApplet.main("examples.GraphPad_Example");
	}
}