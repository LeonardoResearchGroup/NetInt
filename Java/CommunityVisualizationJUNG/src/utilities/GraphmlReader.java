package utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import utilities.mapping.Mapper;
import visualElements.VCommunity;

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
	private ArrayList<Edge> edgesBetweenCommunuties;
	HashMap<String, Node> communityNodes;

	

	ArrayList<String> communities;
	
	public GraphmlReader() {
		communities = new ArrayList<String>();
	}
	
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

	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String communityKey, String nameKey,
			String sectorKey, String weightKey, String frequencyKey) {
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();
		System.out.println("GraphmlReader> Building Nodes and Edges");
		System.out.println("GraphmlReader> Working on it ...");
		
		edgesBetweenCommunuties = new ArrayList<Edge>();
		// <Name of community, Node object of a community>
		communityNodes = new HashMap<String, Node>();

		Node[] nodes = new Node[30000];

		for (Vertex vertex : graph.getVertices()) {

			int id = Integer.parseInt(vertex.getId().toString().replace("n", ""));

			Node node = new Node(String.valueOf(id));

			if (vertex.getProperty(communityKey) != null) {
				node.setCommunity("Root", 0);
				node.setCommunity(vertex.getProperty(communityKey).toString(), 1);
				addCommunity(node.getCommunity(1));
			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph(): No filter matches " + communityKey
						+ "!!! Check the key String of the community filter");
			}

			// Check if exist a property matching nameKey
			if (vertex.getProperty(nameKey) != null) {
				node.setName(vertex.getProperty(nameKey).toString());
			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches " + nameKey
						+ "!!! Check the key String of the graphML label");
			}

			// Check if exist a property matching sectorKey
			if (vertex.getProperty(sectorKey) != null) {
				node.setSector(vertex.getProperty(sectorKey).toString());
			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches " + sectorKey
						+ "!!! Check the key String of the sector");
			}

			nodes[id] = node;

//			if (node.getOutDegree(1) > maxOutDegree) {
//				maxOutDegree = node.getOutDegree(1);
//			}
//			if (node.getOutDegree(1) < minOutDegree) {
//				minOutDegree = node.getOutDegree(1);
//			}
		}

		float maxWeight = 0;
		float minWeight = Float.POSITIVE_INFINITY;

		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.IN);
			Vertex target = edge.getVertex(Direction.OUT);
			// Get their ID
			int idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));
			/*
			 * // Instantiate Nodes Node sourceNode = new
			 * Node(String.valueOf(idSource)); Node targetNode = new
			 * Node(String.valueOf(idTarget));
			 * 
			 * // Stores the sourceNode retrieved from the collection of nodes
			 * if it // exists, else stores null Node tmp =
			 * getEqualNode(rtnGraph, sourceNode); if (tmp != null) { // If the
			 * node does exist assign it to source sourceNode = tmp; } // Stores
			 * the targetNode retrieved from the collection of nodes if it //
			 * exists, else stores null tmp = getEqualNode(rtnGraph,
			 * targetNode); if (tmp != null) { // If the node does exist assign
			 * it to target targetNode = tmp; }
			 * 
			 * // Check if it exist a property matching communityKey if
			 * (source.getProperty(communityKey) != null &&
			 * target.getProperty(communityKey) != null) { // Get and store
			 * communities sourceNode.setCommunity("Root", 0);
			 * targetNode.setCommunity("Root", 0);
			 * sourceNode.setCommunity(source.getProperty(communityKey).toString
			 * (), 1);
			 * targetNode.setCommunity(target.getProperty(communityKey).toString
			 * (), 1);
			 * 
			 * addCommunity(sourceNode.getCommunity(1));
			 * addCommunity(targetNode.getCommunity(1));
			 * 
			 * } else { System.out.println(
			 * "GraphmlReader> getJungDirectedGraph(): No filter matches!!! Check the key String of the community filter"
			 * ); }
			 * 
			 * // Check if exist a property matching nameKey if
			 * (source.getProperty(nameKey) != null &&
			 * target.getProperty(nameKey) != null) {
			 * sourceNode.setName(source.getProperty(nameKey).toString());
			 * targetNode.setName(target.getProperty(nameKey).toString()); }
			 * else { System.out.println(
			 * "GraphmlReader> getJungDirectedGraph (): No label matches!!! Check the key String of the graphML label"
			 * ); }
			 * 
			 * // Check if exist a property matching sectorKey if
			 * (source.getProperty(sectorKey) != null &&
			 * target.getProperty(sectorKey) != null) {
			 * sourceNode.setSector(source.getProperty(sectorKey).toString());
			 * targetNode.setSector(target.getProperty(sectorKey).toString()); }
			 * else { System.out.println(
			 * "GraphmlReader> getJungDirectedGraph (): No label matches!!! Check the key String of the sector"
			 * ); }
			 */
			// Add graphElements to collection
			graphElements.Edge e = new graphElements.Edge(nodes[idSource], nodes[idTarget], true);
			if (edge.getProperty(weightKey) != null) {
				String val = edge.getProperty(weightKey).toString();
				float weight = Float.valueOf(val);
				if (weight > maxWeight) {
					maxWeight = weight;
				}
				if (weight < minWeight) {
					minWeight = weight;
				}
				e.setWeight(weight);
			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches " + weightKey
						+ "!!! Check the key String of the weight");
			}
			if (edge.getProperty(frequencyKey) != null) {
				String freq = edge.getProperty(frequencyKey).toString();
				e.setFrequency(Double.valueOf(freq).intValue());
			} else {
				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches " + frequencyKey
						+ "!!!!!! Check the key String of the frequency");
			}
			rtnGraph.addEdge(e, nodes[idSource], nodes[idTarget], EdgeType.DIRECTED);
			//For the metagraph
            Edge metaE = new Edge(communityNodes.get(nodes[idSource].getCommunity(1)),
            		communityNodes.get(nodes[idTarget].getCommunity(1)), true);
            if(!edgesBetweenCommunuties.contains(metaE)){
            	edgesBetweenCommunuties.add(metaE);
            }

		}
		// Setting limits in static class (Singleton pattern)
		Mapper.getInstance().setMaxWeight(maxWeight);
		Mapper.getInstance().setMinWeight(minWeight);
		return rtnGraph;
	}
	
	/**
	 * Build a JungGraph from a pajek file. 
	 * @param filename
	 * @return
	 */
	public DirectedSparseMultigraph<Node, Edge> readFromPajek(String filename){
		
		
		edgesBetweenCommunuties = new ArrayList<Edge>();
		// <Name of community, Node object of a community>
		communityNodes = new HashMap<String, Node>();
		
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();
		try {
		    File file = new File(filename);
		    FileReader fr = new FileReader(file);
		    Scanner br = new Scanner(fr);
		    
		    String token;
		    br.next();
		    token = br.next();
		    int numberOfNodes = Integer.parseInt(token);
		    Node[] nodes = new Node[numberOfNodes];
		    
	    	
		    
		    
		    for(int i=0; i<numberOfNodes; i++){
		    	token = br.next();
		    	int idS = Integer.parseInt(token) - 1;
		    	Node node = new Node(token);
		    	
		    	node.setCommunity("Root", 0);
				node.setCommunity(br.next(), 1);
				addCommunity(node.getCommunity(1));
				nodes[idS] = node;
		    }
		    System.out.println(br.next());
		    while(br.hasNext()) {
		    	token = br.next();
//		    	System.out.println(token);
		    	int idS  = Integer.parseInt(token) - 1;
		    	token = br.next();
		    	int idT = Integer.parseInt(token) - 1;
	            graphElements.Edge e = new graphElements.Edge(nodes[idS], nodes[idT], true);
	            rtnGraph.addEdge(e, nodes[idS], nodes[idT], EdgeType.DIRECTED);
	            //For the metagraph
	            Edge metaE = new Edge(communityNodes.get(nodes[idS].getCommunity(1)),
	            		communityNodes.get(nodes[idT].getCommunity(1)), true);
	            if(!edgesBetweenCommunuties.contains(metaE)){
	            	edgesBetweenCommunuties.add(metaE);
	            }
	            
	            
	        }

		    br.close();
		    System.out.println("Cantidad de V�rtices");
		    System.out.println(rtnGraph.getVertexCount());


		} catch (Exception e) {
		   e.printStackTrace();
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
			communityNodes.put(string, new Node(string));
		} 
	}

	public Set<String> getKeys() {
		return graph.getVertices().iterator().next().getPropertyKeys();
	}
	
	public ArrayList<Edge> getEdgesBetweenCommunuties() {
		return edgesBetweenCommunuties;
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