package utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.Direction;
//import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graphElements.Node;
import graphElements.Edge;

public class GraphReader {

	private Graph graph;

	public GraphReader(String xmlFile) {
		graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);

		InputStream input;
		try {
			input = new BufferedInputStream(new FileInputStream(xmlFile));
			reader.inputGraph(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph() {
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();

		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.IN);
			Vertex target = edge.getVertex(Direction.OUT);
			// Get their ID
			int idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));
			// Instantiate Nodes
			Node sourceNode = new Node(idSource);
			Node targetNode = new Node(idTarget);
			// Add Attributes
			sourceNode.setName(source.getProperty("label"));
			sourceNode.setCommunity(source.getProperty("Continent"));
			targetNode.setName(target.getProperty("label"));
			targetNode.setCommunity(target.getProperty("Continent"));

			// Add graphElements to collection
			graphElements.Edge e = new graphElements.Edge(sourceNode, targetNode, true);
			String val = String.valueOf((Double) edge.getProperty("weight"));
			e.setWeight(Float.valueOf(val));
			rtnGraph.addEdge(e, sourceNode, targetNode, EdgeType.DIRECTED);
		}	
		return rtnGraph;
	}
	
	public TinkerGraph getTinkerGraph() {
		TinkerGraph rtnGraph = new TinkerGraph();

		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.IN);
			Vertex target = edge.getVertex(Direction.OUT);
			// Get their ID
			int idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));
			// Instantiate Nodes
			Node sourceNode = new Node(idSource);
			Node targetNode = new Node(idTarget);
			// Add Attributes
			sourceNode.setName(source.getProperty("label"));
			sourceNode.setCommunity(source.getProperty("Continent"));
			targetNode.setName(target.getProperty("label"));
			targetNode.setCommunity(target.getProperty("Continent"));

			// Add graphElements to collection
			graphElements.Edge e = new graphElements.Edge(sourceNode, targetNode, true);
			String val = String.valueOf((Double) edge.getProperty("weight"));
			e.setWeight(Float.valueOf(val));
			rtnGraph.addEdge(e, source, target, "directed");
		}
		return rtnGraph;
	}

}