package utilities;

import java.util.ArrayList;
import java.util.Collection;


import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import utilities.mapping.Mapper;


/**
 * PAJEK import changes made on feb 3. It has not ben tested.
 * 
 * @author jsalam
 *
 */
public class GraphLoader {
	public static Graph<Node, Edge> theGraph;
	public GraphmlReader GMLreader;
	public PajekReader PJKreader;
	public static final int PAJEK = 1;
	public static final int GRAPHML = 0;
	private int totalCommunities = 0;
	private int fileFormat;

	/**
	 * @param file
	 *            The path to the source file
	 * @param nodeImportAttributes
	 *            List of node attributes to be retrieved from the source file
	 * @param edgeImportAttributes
	 *            List of edge attributes to be retrieved from the source file
	 * @param format
	 */
	public GraphLoader(String file, String[] nodeImportAttributes, String[] edgeImportAttributes, int format) {
		System.out.println(this.getClass().getName() + "GraphLoader");
		fileFormat = format;
		if (format == GraphLoader.PAJEK) {
			PJKreader = new PajekReader();
			theGraph = PJKreader.getGraph();
			//jungGraph = PJKreader.getGraph();
			totalCommunities = PJKreader.getCommunities().size();
		} else if (format == GraphLoader.GRAPHML) {
			GMLreader = new GraphmlReader(file);
			theGraph = GMLreader.getJungDirectedGraph(nodeImportAttributes, edgeImportAttributes);
			totalCommunities = GMLreader.getCommunities().size();
		}
		System.out.println("     Jung Graph Created from file:" + file);
		System.out.println("        Total Nodes in the graph: " + theGraph.getVertexCount());
		System.out.println("        Total Edges in the graph: " + theGraph.getEdgeCount());
		System.out.println("     " + totalCommunities + " communities loaded");
		// Iterate over elements to set attributes of nodes in the
		// GraphElements and Mapping
		for (Node n : theGraph.getVertices()) {
			setNodesDegrees(theGraph, n);
			Mapper.getInstance().setMaxMinGraphElementAttributes(n);
		}
		System.out.println("     Degrees assigned to nodes and attributes to Mapper Class");
		// ***** EDGE ATRIBUTES ADDED IN METHOD getJungDirectedGraph AROUND LINE
		// 183 OF GRAPHMLREADER CLASS
		System.out.println("     Edge attributes assigned to edges and to Mapper Class");
	}

	public ArrayList<String> getCommunityNames() {
		if (fileFormat == GraphLoader.PAJEK) {
			return PJKreader.getCommunities();
		} else if (fileFormat == GraphLoader.GRAPHML) {
			return GMLreader.getCommunities();
		} else {
			return null;
		}
	}

	/**
	 * Uses 0 as index because this method is only used for root Graphs
	 * 
	 * @param graph
	 */
	private static void setNodesDegrees(Graph<Node, Edge> graph, Node n) {
		// Degree. 0 because it is the root community
		// Degree
		n.setDegree(0, graph.degree(n));
		// Out degree
		n.setOutDegree(0, graph.getSuccessorCount(n));
		// In Degree
		n.setInDegree(0, graph.getPredecessorCount(n));
	}

	public void printJungGraph(boolean printDetails) {
		System.out.println("Nodes: " + theGraph.getVertexCount());
		System.out.println("Edges: " + theGraph.getEdgeCount());
		if (printDetails) {
			Collection<graphElements.Edge> edges = theGraph.getEdges();
			for (graphElements.Edge e : edges) {
				System.out.println("from: " + e.getSource().getName() + " " + e.getSource().getId() + " to: "
						+ e.getTarget().getName() + " " + e.getTarget().getId());
			}

			Collection<Node> nodes = theGraph.getVertices();
			for (Node n : nodes) {
				System.out.print(n.getName() + " has ID: " + n.getId());
				System.out.println("  Predecessors count: " + theGraph.getPredecessorCount(n));
			}

			for (Object ob : theGraph.getVertices()) {
				Node n = (Node) ob;
				System.out.println(n.getName() + " has ID: " + n.getId());
			}
		}
	}

	
}
