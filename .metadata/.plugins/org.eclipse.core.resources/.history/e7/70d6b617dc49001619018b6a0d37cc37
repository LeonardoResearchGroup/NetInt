package utilities.visualArrangements;

import processing.core.*;

import java.awt.Dimension;

import edu.uci.ics.jung.graph.Graph;
import graphElements.*;

public class RootContainer extends Container {

	/**
	 * Constructor to be used with instances of edu.uci.ics.jung.graph
	 * 
	 * @param app
	 * @param graph
	 *            The graph
	 * @param kindOfLayout
	 *            Integer defining the kind of layout
	 * @param dimension
	 *            The Dimension of the component that contain the visualElements
	 */
	public RootContainer(PApplet app, Graph<Node, Edge> graph, int kindOfLayout, Dimension dimension) {
		super(app, graph);
		distributeNodesInLayout(kindOfLayout, dimension);
		// Generate Visual Elements
		runNodeFactory();
		runEdgeFactory();
	}

}
