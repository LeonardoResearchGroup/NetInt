package containers;

import java.awt.Dimension;

import edu.uci.ics.jung.graph.Graph;
import graphElements.*;
import visualElements.Canvas;

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
	public RootContainer(Graph<Node, Edge> graph, int kindOfLayout, Dimension dimension) {
		super(graph);
		this.kindOfLayout = kindOfLayout;
		setDimension(dimension);
	}
}
