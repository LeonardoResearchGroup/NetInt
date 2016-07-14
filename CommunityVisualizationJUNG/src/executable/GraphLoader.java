package executable;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import utilities.GraphmlReader;

public class GraphLoader {

	public DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph;
	GraphmlReader reader;
	

	public GraphLoader(String file, String coomunityFilter, String nodeLabel) {
		reader = new GraphmlReader(file);
		jungGraph = reader.getJungDirectedGraph(coomunityFilter, nodeLabel);
		for (String s : reader.getCommunities()){
			System.out.println("GRaphLoadr> community: "+ s);
		}
		setNodesOutDegree(jungGraph);
		setNodesInDegree(jungGraph);
		System.out.println("CommunityViz: Graph Created from file:" + file);
	}

	public ArrayList<String> getCommunityNames(){
		return reader.getCommunities();
	}
	
	public static void setNodesDegree(Graph<Node, Edge> graph) {
		// Degree
		for (Node n : graph.getVertices()) {
			n.setDegree(0, graph.degree(n));
		}
	}

	public static void setNodesOutDegree(Graph<Node, Edge> graph) {
		// Degree
		for (Node n : graph.getVertices()) {
			n.setOutDegree(0, graph.getSuccessorCount(n));
		}
	}

	public static void setNodesInDegree(Graph<Node, Edge> graph) {
		// Degree
		for (Node n : graph.getVertices()) {
			n.setInDegree(0, graph.getPredecessorCount(n));
		}
	}

	public void printJungGraph() {
		System.out.println("Nodes: " + jungGraph.getVertexCount());
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
			@Override
			public boolean evaluate(Node nodo) {
				return nodo.belongsTo(community);
			}
		};
		VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<Node, Edge>(inSubgraph);
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge>) filter
				.transform(jungGraph);
		// Set In and Out Degree
		for (Node n : problemGraph.getVertices()) {
			n.setOutDegree(n.getMetadataSize()-1, problemGraph.getSuccessorCount(n));
			n.setInDegree(n.getMetadataSize()-1, problemGraph.getPredecessorCount(n));
		}
		return problemGraph;

	}

	public static void main(String[] args) {
		new GraphLoader("./data/graphs/Risk.graphml", "comunidad", "name");
	}
}
