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

	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String communityKey, String nameKey) {
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

			if (rtnGraph.containsVertex(sourceNode)){
				sourceNode = getEqualNode(rtnGraph,sourceNode);
			}
			
			if (rtnGraph.containsVertex(targetNode)){
				targetNode = getEqualNode(rtnGraph,targetNode);
			}
			
			// Check if exist a property matching communityKey
			if (source.getProperty(communityKey) != null && target.getProperty(communityKey) != null) {
				// Get and store communities
				sourceNode.setCommunity("Root", 0);
				targetNode.setCommunity("Root", 0);
				sourceNode.setCommunity(source.getProperty(communityKey).toString(), 1);
				targetNode.setCommunity(target.getProperty(communityKey).toString(), 1);

				addCommunity(sourceNode.getCommunity(1));
				addCommunity(targetNode.getCommunity(1));

			} else {
				System.out.println("GraphmlReader> No filter matches!!! Check the key String of the community filter");
			}

			// Check if exist a property matching nameKey
			if (source.getProperty(nameKey) != null && target.getProperty(nameKey) != null) {
				sourceNode.setName(source.getProperty(nameKey).toString());
				targetNode.setName(target.getProperty(nameKey).toString());
			} else {
				System.out.println("GraphmlReader> No filter matches!!! Check the key String of the community filter");
			}

			// Add graphElements to collection
			graphElements.Edge e = new graphElements.Edge(sourceNode, targetNode, true);
			if ((Double) edge.getProperty("weight") != null) {
				String val = String.valueOf((Double) edge.getProperty("weight"));
				e.setWeight(Float.valueOf(val));
			}
//			if (rtnGraph.containsVertex(targetNode)) {
//				System.out.println("Este nodo target " + targetNode.getName() + " ya esta en el grafo");
//				System.out.println("Hay " + rtnGraph.getVertexCount() + " nodos en el grafo");
//			}
//
//			if (rtnGraph.containsVertex(sourceNode)) {
//				System.out.println("Este nodo source " + sourceNode.getId() + " ya esta en el grafo");
//				System.out.println("Hay " + rtnGraph.getVertexCount() + " nodos en el grafo");
//			}
			rtnGraph.addEdge(e, sourceNode, targetNode, EdgeType.DIRECTED);

		}

		Edge B_P = null;
		Edge B_V = null;
		for (Edge edgeJung : rtnGraph.getEdges()) {
			if (edgeJung.getSource().getName().equals("Brazil")) {
			
				if (edgeJung.getTarget().getName().equals("Peru")) {
					B_P = edgeJung;
				} else if (edgeJung.getTarget().getName().equals("Venezuela")) {
					B_V = edgeJung;
				}
			}
		}
		
		System.out.println("Probando el metodo equals en Node: "+B_P.getSource().equals(B_V.getSource()));
		System.out.println("Probando el  == en Node: "+ (B_P.getSource() == B_V.getSource())); 
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
	protected Node getEqualNode(edu.uci.ics.jung.graph.Graph<Node, Edge> graph, Node lookingForNode){
		Node nodo =null;
		for(Node node : graph.getVertices()){
			if(lookingForNode.equals(node)){
				nodo = node;
				return nodo;
			}
		}
		return nodo;
	}
}