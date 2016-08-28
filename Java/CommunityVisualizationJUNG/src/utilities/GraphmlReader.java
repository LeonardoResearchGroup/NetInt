package utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

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

public class GraphmlReader {

	private Graph graph;
	ArrayList<String> communities;

	public GraphmlReader(String xmlFile) {
		graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);
		communities = new ArrayList<String>();

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

	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String communityKey, String nameKey, String sectorKey) {
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();

		System.out.println("GraphmlReader> Building Nodes and Edges");
		System.out.println("GraphmlReader> Working on it ...");
		
		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.IN);
			Vertex target = edge.getVertex(Direction.OUT);
			// Get their ID
			int idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));
			// Instantiate Nodes
			Node sourceNode = new Node(String.valueOf(idSource));
			Node targetNode = new Node(String.valueOf(idTarget));
			
			// Stores the sourceNode retrieved from the collection of nodes if it
			// exists, else stores null
			Node tmp = getEqualNode(rtnGraph, sourceNode);
			if (tmp != null) {
				// If the node does exist assign it to source 
				sourceNode = tmp;
			}
			// Stores the targetNode retrieved from the collection of nodes if it
			// exists, else stores null
			tmp = getEqualNode(rtnGraph, targetNode);
			if (tmp != null) {
				// If the node does exist assign it to target
				targetNode = tmp;
			}

			// Check if it exist a property matching communityKey
			if (source.getProperty(communityKey) != null && target.getProperty(communityKey) != null) {
				// Get and store communities
				sourceNode.setCommunity("Root", 0);
				targetNode.setCommunity("Root", 0);
				sourceNode.setCommunity(source.getProperty(communityKey).toString(), 1);
				targetNode.setCommunity(target.getProperty(communityKey).toString(), 1);

				addCommunity(sourceNode.getCommunity(1));
				addCommunity(targetNode.getCommunity(1));

			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph(): No filter matches!!! Check the key String of the community filter");
			}

			// Check if exist a property matching nameKey
			if (source.getProperty(nameKey) != null && target.getProperty(nameKey) != null) {
				sourceNode.setName(source.getProperty(nameKey).toString());
				targetNode.setName(target.getProperty(nameKey).toString());
			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches!!! Check the key String of the graphML label");
			}
			
			// Check if exist a property matching sectorKey
			if (source.getProperty(sectorKey) != null && target.getProperty(sectorKey) != null) {
				sourceNode.setSector(source.getProperty(sectorKey).toString());
				targetNode.setSector(target.getProperty(sectorKey).toString());
			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches!!! Check the key String of the sector");
			}

			// Add graphElements to collection
			graphElements.Edge e = new graphElements.Edge(sourceNode, targetNode, true);
			if ((Double) edge.getProperty("weight") != null) {
				String val = String.valueOf((Double) edge.getProperty("weight"));
				e.setWeight(Float.valueOf(val));
			}
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
			Node sourceNode = new Node(String.valueOf(idSource));
			Node targetNode = new Node(String.valueOf(idTarget));
			// Add Attributes
			sourceNode.setName((String) source.getProperty("label"));
			sourceNode.setCommunity((String) source.getProperty("Continent"));
			targetNode.setName((String) target.getProperty("label"));
			targetNode.setCommunity((String) target.getProperty("Continent"));

			// Add graphElements to collection
			graphElements.Edge e = new graphElements.Edge(sourceNode, targetNode, true);
			String val = String.valueOf((Double) edge.getProperty("weight"));
			e.setWeight(Float.valueOf(val));
			rtnGraph.addEdge(e, source, target, "directed");
		}
		return rtnGraph;
	}

	/**
	 * ArrayList of community values obtained from the graphML file
	 * 
	 * @return
	 */
	public ArrayList<String> getCommunities() {
		return communities;
	}

	private void addCommunity(String string) {
		// If community not in the list yet
		if (!communities.contains(string)) {
			communities.add(string);
		}
	}

	public Set<String> getKeys() {
		return graph.getVertices().iterator().next().getPropertyKeys();
	}

	/**
	 * 
	 * @param graph
	 * @param lookingForNode
	 * @return
	 */
	protected Node getEqualNode(edu.uci.ics.jung.graph.Graph<Node, Edge> graph, Node lookingForNode) {
		Node nodo = null;
		for (Node node : graph.getVertices()) {
			if (lookingForNode.equals(node)) {
				nodo = node;
				return nodo;
			}
		}
		return nodo;
	}
}