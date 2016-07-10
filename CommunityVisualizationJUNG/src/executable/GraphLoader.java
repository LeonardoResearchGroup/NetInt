package executable;

import java.util.Collection;

import org.apache.commons.collections15.Predicate;

import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import graphElements.Edge;
import graphElements.Node;
import utilities.GraphmlReader;

public class GraphLoader {

	public DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph;
	GraphmlReader reader;

	public GraphLoader(String file) {
		reader = new GraphmlReader(file);
		jungGraph = reader.getJungDirectedGraph();
		System.out.println("CommunityViz: Graph Created from file:" + file);
	}
	
	public void setNodesDegree(){
		// Degree
		for (Node n: jungGraph.getVertices()){
			n.setDegree(jungGraph.degree(n));
		}	
	}
	public void setNodesOutDegree(){
		// Degree
		for (Node n: jungGraph.getVertices()){
			n.setOutDegree(jungGraph.getSuccessorCount(n));
		}	
	}
	public void setNodesInDegree(){
		// Degree
		for (Node n: jungGraph.getVertices()){
			n.setInDegree(jungGraph.getPredecessorCount(n));
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
	 * Returns a subgraph of jungGraph whose nodes belong to the specified community
	 * @param jungGraph
	 * @param comunidad
	 * @return
	 */
	public DirectedSparseMultigraph<Node, Edge> filterByCommunity(DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph,
			final String community){
		
		 Predicate<Node> inSubgraph = new Predicate<Node>(){
				@Override
				public boolean evaluate(Node nodo) {
					//System.out.println(nodo.getCommunity());
					return nodo.getCommunity().equals(community);
				}};
		VertexPredicateFilter<Node,Edge> filter = new VertexPredicateFilter<Node,Edge>(inSubgraph);
		DirectedSparseMultigraph<Node, Edge> problemGraph = (DirectedSparseMultigraph<Node, Edge> )filter.transform(jungGraph);
		return problemGraph;
		
	}

	public static void main(String[] args) {
		new GraphLoader("./data/graphs/Risk.graphml");
	}
}
