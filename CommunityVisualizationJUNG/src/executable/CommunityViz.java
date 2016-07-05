package executable;

import java.util.Collection;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

import graphElements.Node;
import utilities.GraphReader;

public class CommunityViz {

	public DirectedSparseMultigraph<Node, graphElements.Edge> jungGraph;
	GraphReader reader;

	public CommunityViz(String file) {
		reader = new GraphReader(file);
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

	public static void main(String[] args) {
		new CommunityViz("./data/graphs/Risk.graphml");
	}
}
