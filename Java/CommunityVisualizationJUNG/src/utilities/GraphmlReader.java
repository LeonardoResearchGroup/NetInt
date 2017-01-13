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
import java.util.TreeMap;

import utilities.mapping.Mapper;
import visualElements.gui.VisibilitySettings;

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
	 * This class was initially designed to return an array of nodes but it
	 * needed to be changes to a TreeMap because it was not possible to
	 * determine the final size of the array in advance.
	 * 
	 * @param nodeImportAttributes
	 * @return The collection of nodes ordered by the Integer version of their
	 *         Id
	 */
	private TreeMap<Integer, Node> makeNodes(String[] nodeImportAttributes) {
		// private Node[] makeNodes(String[] nodeImportAttributes) {
		System.out.println(this.getClass().getName() + " Making Nodes...");

		TreeMap<Integer, Node> theNodes = new TreeMap<Integer, Node>();

		// Final Array of nodes with attributes
		// Node[] nodes = new Node[vertexCount];
		// *** Go over graph vertex and set all nodes
		// int counter = 0;
		for (Vertex vertex : graph.getVertices()) {
			// Get vertex ID
			int id = Integer.parseInt(vertex.getId().toString().replace("n", ""));
			// Make a node with the retrieved ID
			Node nodeTmp = new Node(String.valueOf(id));

			try {
				// For the first two attributes: node community and node
				// name
				if (vertex.getProperty(nodeImportAttributes[0]) != null) {
					nodeTmp.setCommunity("Root", 0);
					nodeTmp.setCommunity(vertex.getProperty(nodeImportAttributes[0]).toString(), 1);
					addCommunity(nodeTmp.getCommunity(1));
				} else
					throw new NullPointerException();
				// Label
				if (vertex.getProperty(nodeImportAttributes[1]) != null) {
					nodeTmp.setName(vertex.getProperty(nodeImportAttributes[1]).toString());
				} else
					throw new NullPointerException();

			} catch (NullPointerException e) {
				System.out.println(this.getClass().getName() + " NullPointerException making nodes "
						+ nodeImportAttributes[0] + " " + nodeImportAttributes[1]);
			}

			for (int i = 2; i < nodeImportAttributes.length; i++) {
				try {
					// For the remaining attributes
					// Check if it does exist a property matching other
					// attributes
					if (vertex.getProperty(nodeImportAttributes[i]) != null) {
						nodeTmp.setAttribute(nodeImportAttributes[i], vertex.getProperty(nodeImportAttributes[i]));
					} else
						throw new NullPointerException();
				} catch (NullPointerException e) {
					System.out.println(this.getClass().getName() + " NullPointerException making nodes ");
				}
			}
			// Check if some graphml key match with some financial stament key
			for (String key : vertex.getPropertyKeys()) {
				String keyLabel = VisibilitySettings.getInstance().getDescriptiveKeys().get(key);
				if (keyLabel != null) {
					nodeTmp.getDescriptiveStatistics().put(key, (double) vertex.getProperty(key));
				}
			}
			// nodes[id] = nodeTmp;
			theNodes.put(id, nodeTmp);
		}
		// return nodes;
		return theNodes;
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

		// **** MAKE NODES ****
		// Node[] nodes = makeNodes(nodeImportAttributes);
		TreeMap<Integer, Node> nodes = makeNodes(nodeImportAttributes);
		// **** CREATE EDGES ****

		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.OUT);
			Vertex target = edge.getVertex(Direction.IN);
			// Get their ID
			Integer idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			Integer idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));

			// **** MAKE EDGE FROM SOURCE AND TARGET NODES ****
			graphElements.Edge e = new graphElements.Edge(nodes.get(idSource), nodes.get(idTarget), true);
			// Check if the edge has any of the edge Import attributes
			if (edgeImportAttributes.length > 0) {
				for (int i = 0; i < edgeImportAttributes.length; i++) {
					try {
						// Retrieve the attribute value and check if it is null
						if (edge.getProperty(edgeImportAttributes[i]) != null) {
							Object tmpProperty = edge.getProperty(edgeImportAttributes[i]);
							// Add the attribute value to the temporal edge;
							e.setAttribute(edgeImportAttributes[i], tmpProperty);
						} else
							throw new NullPointerException();
					} catch (NullPointerException exception) {
						System.out.println(this.getClass().getName()
								+ " Null Pointer Exception. Edges in the source file don't have attributes named: "
								+ edgeImportAttributes[i]);
					}
				}
			} else {
				// if no attributes selected set the weight to 1
				e.setAttribute("weight", 1);
			}
			// Setting max min limits in Mapper class (Singleton
			// pattern)
			Mapper.getInstance().setMaxMinGraphElementAttributes(e);
			rtnGraph.addEdge(e, nodes.get(idSource), nodes.get(idTarget), EdgeType.DIRECTED);
			Edge metaE = new Edge(communityNodes.get(nodes.get(idSource).getCommunity(1)),
					communityNodes.get(nodes.get(idTarget).getCommunity(1)), true);
			if (!edgesBetweenCommunities.contains(metaE)) {
				edgesBetweenCommunities.add(metaE);
			}
		}
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
			// e.printStackTrace();
			// System.out.println(this.getClass().getName() + ": No vertex label
			// match, Check the attribute key");
		}
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

	/**
	 * ArrayList of community values obtained from the import file
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