package utilities;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import utilities.mapping.Mapper;

public class GraphLoader {

	public DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph;
	public GraphmlReader reader;
	public static final int PAJEK = 1;
	public static final int GRAPHML = 0;

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
		if (format == GraphLoader.PAJEK) {
			reader = new GraphmlReader();
			jungGraph = reader.readFromPajek(file);
		} else if (format == GraphLoader.GRAPHML) {
			reader = new GraphmlReader(file);
			jungGraph = reader.getJungDirectedGraph(nodeImportAttributes, edgeImportAttributes);
		}
		System.out.println(this.getClass().getName() + " Jung Graph Created from file:" + file);
		System.out.println("   Total Nodes in the graph: " + jungGraph.getVertexCount());
		System.out.println("   Total Edges in the graph: " + jungGraph.getEdgeCount());
		System.out.println(this.getClass().getName() + " " + reader.getCommunities().size() + " communities loaded");
		// Iterate over elements to set attributes of nodes in the
		// GraphElements and Mapping
		for (Node n : jungGraph.getVertices()) {
			setNodesDegrees(jungGraph, n);
			Mapper.getInstance().setMaxMinGraphElementAttributes(n);
		}
		System.out.println(this.getClass().getName() + " Degrees assigned to nodes and attributes to Mapper Class");
		// ***** EDGE ATRIBUTES ADDED IN METHOD getJungDirectedGraph AROUND LINE
		// 183 OF GRAPHMLREADER CLASS
		System.out.println(this.getClass().getName() + " Edge attributes assigned to edges and to Mapper Class");

	}

	public ArrayList<String> getCommunityNames() {
		return reader.getCommunities();
	}

	/**
	 * Uses 0 as index because this method is only used for root Graphs
	 * 
	 * @param graph
	 */
	public static void setNodesDegrees(Graph<Node, Edge> graph, Node n) {
		// Degree. 0 because it is the root community
		// Degree
		n.setDegree(0, graph.degree(n));
		// Out degree
		n.setOutDegree(0, graph.getSuccessorCount(n));
		// In Degree
		n.setInDegree(0, graph.getPredecessorCount(n));
	}

	public void printJungGraph(boolean printDetails) {
		System.out.println("Nodes: " + jungGraph.getVertexCount());
		System.out.println("Edges: " + jungGraph.getEdgeCount());
		if (printDetails) {
			Collection<graphElements.Edge> edges = jungGraph.getEdges();
			for (graphElements.Edge e : edges) {
				System.out.println("from: " + e.getSource().getName() + " " + e.getSource().getId() + " to: "
						+ e.getTarget().getName() + " " + e.getTarget().getId());
			}

			Collection<Node> nodes = jungGraph.getVertices();
			for (Node n : nodes) {
				System.out.print(n.getName() + " has ID: " + n.getId());
				System.out.println("  Predecessors count: " + jungGraph.getPredecessorCount(n));
			}

			for (Object ob : jungGraph.getVertices()) {
				Node n = (Node) ob;
				System.out.println(n.getName() + " has ID: " + n.getId());
			}
		}
	}

	/**
	 * Returns a subgraph of jungGraph whose nodes belong to the specified
	 * community
	 * 
	 * @param jungGraph
	 * @param comunidad
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterByCommunity(
			DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph, final String community) {

		Predicate<Node> inSubgraph = new Predicate<Node>() {
			public boolean evaluate(Node nodo) {
				return nodo.belongsTo(community);
			}
		};
		VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<Node, Edge>(inSubgraph);
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
				.transform(jungGraph);
		// Set In and Out Degree
		for (Node n : problemGraph.getVertices()) {
			n.setOutDegree(n.getMetadataSize() - 1, problemGraph.getSuccessorCount(n));
			n.setInDegree(n.getMetadataSize() - 1, problemGraph.getPredecessorCount(n));
		}
		return problemGraph;

	}

	/**
	 * Returns a subgraph of jungGraph whose edges connect the specified
	 * communities in either direction.
	 * 
	 * @param jungGraph
	 * @param comumnity1
	 * @param comumnity2
	 * @return
	 */
	public static DirectedSparseMultigraph<Node, Edge> filterByInterCommunities( DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph, final String communityNameA,final String communityNameB) {

		Predicate<Edge> inSubgraph = new Predicate<Edge>() {
			public boolean evaluate(Edge edge) {
				boolean connectsOneWay = edge.getSource().belongsTo(communityNameA)
						&& edge.getTarget().belongsTo(communityNameB);
				boolean connectsOtherWay = edge.getSource().belongsTo(communityNameB)
						&& edge.getTarget().belongsTo(communityNameA);
				boolean connectsCommunities = connectsOneWay || connectsOtherWay;
				return connectsCommunities;
			}
		};
		EdgePredicateFilter<Node, Edge> filter = new EdgePredicateFilter<Node, Edge>(inSubgraph);
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter.transform(jungGraph);
		return problemGraph;

	}

}
