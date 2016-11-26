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
	private ArrayList<Edge> edgesBetweenCommunities;
	private HashMap<String, Node> communityNodes;
	private ArrayList<String> communities;
	private ArrayList<String> graphKeys;

	public GraphmlReader() {
		communities = new ArrayList<String>();
		graphKeys = new ArrayList<String>();
	}

	public GraphmlReader(String file) {
		graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);
		communities = new ArrayList<String>();
		graphKeys = new ArrayList<String>();
		InputStream input;
		try {
			input = new BufferedInputStream(new FileInputStream(file));
			reader.inputGraph(input);
			// *** Read keys
			String currentLine;
			BufferedReader br = new BufferedReader(new FileReader(file));
			System.out.println(this.toString().getClass() + "Reading graphml Keys... ");
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.startsWith("<key")) {
					graphKeys.add(currentLine);
				}
			}
			br.close();
			// ***

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated
	 * @param nodeImportAttributes
	 * @return
	 */
	private Node[] makeNodes(String[] nodeImportAttributes) {
		// Final Array of nodes with attributes
		Node[] nodes = new Node[30000];
		// *** Go over graph vertex and set all nodes
		for (Vertex vertex : graph.getVertices()) {
			// Get vertex ID
			int id = Integer.parseInt(vertex.getId().toString().replace("n", ""));
			// Make a node with the retrieved ID
			Node nodeTmp = new Node(String.valueOf(id));
			for (int i = 0; i < nodeImportAttributes.length; i++) {
				try {
					// For the first two attributes: node community and node
					// name
					switch (i) {
					case 0: // Community key
						if (vertex.getProperty(nodeImportAttributes[i]) != null) {
							nodeTmp.setCommunity("Root", 0);
							nodeTmp.setCommunity(vertex.getProperty(nodeImportAttributes[i]).toString(), 1);
							addCommunity(nodeTmp.getCommunity(1));
						} else
							throw new NullPointerException();
						break;
					case 1: // Label key
						if (vertex.getProperty(nodeImportAttributes[i]) != null) {
							nodeTmp.setName(vertex.getProperty(nodeImportAttributes[i]).toString());
							break;
						} else
							throw new NullPointerException();
					}
					// For the remaining attributes
					if (i > 1) {
						// Check if it does exist a property matching other
						// attributes
						if (vertex.getProperty(nodeImportAttributes[i]) != null) {
							nodeTmp.setAttribute(nodeImportAttributes[i], vertex.getProperty(nodeImportAttributes[i]));
						} else
							throw new NullPointerException();
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println(this.getClass().getName() + ": No vertex label matches: "
							+ nodeImportAttributes[i] + "!!! Check the attribute key");
				}
			}
			nodes[id] = nodeTmp;
		}
		return nodes;
	}

	/**
	 * GENERAL METHOD TO GET THE JUNG GRAPH
	 * 
	 * @param nodeImportAttributes
	 *            the vector of node attributes
	 * @param edgeImportAttributes
	 *            the vector of edge attributes
	 * @return
	 */
	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String[] nodeImportAttributes,
			String[] edgeImportAttributes) {
		// Create the graph to be returned
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();
		// Notify progress on console
		System.out.println("GraphmlReader> Building Nodes and Edges");
		System.out.println("GraphmlReader> Working on it ...");
		// The collection of synthetic edges between communities
		edgesBetweenCommunities = new ArrayList<Edge>();
		// Hash map <Name of community, Node object of a community>
		communityNodes = new HashMap<String, Node>();

		// **** CREATE EDGES ****
		float maxWeight = 0;
		float minWeight = Float.POSITIVE_INFINITY;

		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.IN);
			Vertex target = edge.getVertex(Direction.OUT);
			// Get their ID
			int idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));

			// **** MAKE SOURCE AND TARGET NODES ****
			Node nodeSource = new Node(String.valueOf(idSource));
			Node nodeTarget = new Node(String.valueOf(idTarget));
			// Assign attributes to nodes
			assignNodeAttributes(source, nodeSource, nodeImportAttributes);
			assignNodeAttributes(target, nodeTarget, nodeImportAttributes);

			// Add graphElements to collection
			graphElements.Edge e = new graphElements.Edge(nodeSource, nodeTarget, true);

			// check if the edge has any of the edge Import attributes
			for (int i = 0; i < edgeImportAttributes.length; i++) {
				try {
					Object tmpProperty = edge.getProperty(edgeImportAttributes[i]);
					if (tmpProperty != null) {
						// Add the attribute to the temporal edge;
						e.setAttribute(edgeImportAttributes[i], tmpProperty);
					} else
						throw new NullPointerException();
				} catch (NullPointerException exception) {
					//exception.printStackTrace();
					//System.out.println(this.getClass().getName() + " No label matches " + edgeImportAttributes[i]);
				}
			}
			rtnGraph.addEdge(e, nodeSource, nodeTarget, EdgeType.DIRECTED);
			// For the metagraph
			Edge metaE = new Edge(communityNodes.get(nodeSource.getCommunity(1)),
					communityNodes.get(nodeTarget.getCommunity(1)), true);
			if (!edgesBetweenCommunities.contains(metaE)) {
				edgesBetweenCommunities.add(metaE);
			}
		}
		// Setting limits in static class (Singleton pattern)
		Mapper.getInstance().setMaxWeight(maxWeight);
		Mapper.getInstance().setMinWeight(minWeight);
		return rtnGraph;
	}

	/**
	 * Assigns attributes retrieved from a vertex of a JUNG graph to a node. The
	 * attribute names come from a list obtained from the source file, usually a
	 * graphml
	 * 
	 * @param vertex
	 * @param node
	 * @param nodeImportAttributes
	 */
	private void assignNodeAttributes(Vertex vertex, Node node, String[] nodeImportAttributes) {
		// For the first two attributes: node community and node name
		try {
			// Community key
			if (vertex.getProperty(nodeImportAttributes[0]) != null) {
				node.setCommunity("Root", 0);
				node.setCommunity(vertex.getProperty(nodeImportAttributes[0]).toString(), 1);
				addCommunity(node.getCommunity(1));
			} else
				throw new NullPointerException();
			// Label key
			if (vertex.getProperty(nodeImportAttributes[1]) != null) {
				node.setName(vertex.getProperty(nodeImportAttributes[1]).toString());

			} else
				throw new NullPointerException();

			for (int i = 2; i < nodeImportAttributes.length; i++) {

				// For the remaining attributes
				if (i > 1) {
					// Check if it does exist a property matching other
					// attributes
					if (vertex.getProperty(nodeImportAttributes[i]) != null) {
						node.setAttribute(nodeImportAttributes[i], vertex.getProperty(nodeImportAttributes[i]));
					} else
						throw new NullPointerException();
				}
			}
		} catch (NullPointerException e) {
			//e.printStackTrace();
			//System.out.println(this.getClass().getName() + ": No vertex label match, Check the attribute key");
		}
	}

	/**
	 * @deprecated
	 * @param communityKey
	 * @param nameKey
	 * @param sectorKey
	 * @param weightKey
	 * @param frequencyKey
	 * @return
	 */
	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String communityKey, String nameKey,
			String sectorKey, String weightKey, String frequencyKey) {
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();
		System.out.println("GraphmlReader> Building Nodes and Edges");
		System.out.println("GraphmlReader> Working on it ...");

		edgesBetweenCommunities = new ArrayList<Edge>();
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
//				System.out.println("GraphmlReader> getJungDirectedGraph(): No filter matches " + communityKey
//						+ "!!! Check the key String of the community filter");
			}

			// Check if exist a property matching nameKey
			if (vertex.getProperty(nameKey) != null) {
				node.setName(vertex.getProperty(nameKey).toString());
			} else {
//				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches " + nameKey
//						+ "!!! Check the key String of the graphML label");
			}

			// Check if exist a property matching sectorKey
			// if (vertex.getProperty(sectorKey) != null) {
			// node.setSector(vertex.getProperty(sectorKey).toString());
			// } else {
			// System.out.println("GraphmlReader> getJungDirectedGraph (): No
			// label matches " + sectorKey
			// + "!!! Check the key String of the sector");
			// }
			nodes[id] = node;

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
//				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches " + weightKey
//						+ "!!! Check the key String of the weight");
			}
			if (edge.getProperty(frequencyKey) != null) {
				String freq = edge.getProperty(frequencyKey).toString();
				e.setFrequency(Double.valueOf(freq).intValue());
			} else {
//				System.out.println("GraphmlReader> getJungDirectedGraph (): No label matches " + frequencyKey
//						+ "!!!!!! Check the key String of the frequency");
			}
			rtnGraph.addEdge(e, nodes[idSource], nodes[idTarget], EdgeType.DIRECTED);
			// For the metagraph
			Edge metaE = new Edge(communityNodes.get(nodes[idSource].getCommunity(1)),
					communityNodes.get(nodes[idTarget].getCommunity(1)), true);
			if (!edgesBetweenCommunities.contains(metaE)) {
				edgesBetweenCommunities.add(metaE);
			}

		}
		// Setting limits in static class (Singleton pattern)
		Mapper.getInstance().setMaxWeight(maxWeight);
		Mapper.getInstance().setMinWeight(minWeight);
		return rtnGraph;
	}

	/**
	 * Build a JungGraph from a pajek file.
	 * 
	 * @param filename
	 * @return
	 */
	public DirectedSparseMultigraph<Node, Edge> readFromPajek(String filename) {

		edgesBetweenCommunities = new ArrayList<Edge>();
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

			for (int i = 0; i < numberOfNodes; i++) {
				token = br.next();
				int idS = Integer.parseInt(token) - 1;
				Node node = new Node(token);

				node.setCommunity("Root", 0);
				node.setCommunity(br.next(), 1);
				addCommunity(node.getCommunity(1));
				nodes[idS] = node;
			}
			System.out.println(br.next());
			while (br.hasNext()) {
				token = br.next();
				// System.out.println(token);
				int idS = Integer.parseInt(token) - 1;
				token = br.next();
				int idT = Integer.parseInt(token) - 1;
				graphElements.Edge e = new graphElements.Edge(nodes[idS], nodes[idT], true);
				rtnGraph.addEdge(e, nodes[idS], nodes[idT], EdgeType.DIRECTED);
				// For the metagraph
				Edge metaE = new Edge(communityNodes.get(nodes[idS].getCommunity(1)),
						communityNodes.get(nodes[idT].getCommunity(1)), true);
				if (!edgesBetweenCommunities.contains(metaE)) {
					edgesBetweenCommunities.add(metaE);
				}

			}

			br.close();
			System.out.println("Cantidad de Vertices");
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
			// e.setWeight(Float.valueOf(val));
			e.setAttribute("weight", val);
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

	/**
	 * See getGraphKeys()
	 * 
	 * @return
	 * @deprecated
	 */
	public Set<String> getKeys() {
		return graph.getVertices().iterator().next().getPropertyKeys();
	}

	/**
	 * Returns the graphml keys read at the moment of loading
	 * 
	 * @return the list of keys
	 */
	public ArrayList<String> getGraphKeys() {
		return graphKeys;
	}

	public ArrayList<Edge> getEdgesBetweenCommunuties() {
		return edgesBetweenCommunities;
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