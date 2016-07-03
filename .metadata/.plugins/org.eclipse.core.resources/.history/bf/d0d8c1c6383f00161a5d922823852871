package graphElements;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is a set of references to the nodes in the rootGraph
 * 
 * @author juansalamanca
 * 
 */
public class SubGraph extends Graph{

	// Constructors
	public SubGraph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}

	public SubGraph(ArrayList<Node> nodes) {
		this.nodes = nodes;
		edges = new ArrayList<Edge>();
	}

	public SubGraph(Graph graph, int communityID) {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		setNodesFromGraph(graph, communityID);
	}

	// Methods
	/**
	 * Search in the graph for the nodes that belong to the communityID. It also
	 * gets references to edges from which the each node is source
	 * 
	 * @param graph
	 *            Usually the root graph
	 * @param communityID
	 *            Must be larger than 0
	 * @return The list of references to nodes in the root graph
	 */
	public void setNodesFromGraph(Graph graph, int communityID) {
		Iterator<Node> graphItr = graph.getNodes().iterator();
		while (graphItr.hasNext()) {
			Node nd = graphItr.next();
			if (nd.belongsTo(communityID)) {
				nodes.add(nd);
				// gets all the edges for this source
				edges.addAll(graph.getDirectedEdgesFrom(nd));
			}
		}
	}
}
