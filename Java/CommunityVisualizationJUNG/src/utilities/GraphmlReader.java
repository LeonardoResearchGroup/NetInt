package utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import utilities.mapping.Mapper;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graphElements.Node;
import graphElements.Edge;

/**
 * 
 * @author jsalam
 *
 */
public class GraphmlReader {
	private Graph graph;
	// Hash map <Name of community, Node object of a community>
	private HashMap<String, Node> vCommunityNodes;
	private ArrayList<String> communities;
	// Edges between communities
	private ArrayList<Edge> edgesBetweenCommunities;

	/**
	 * Reader usually used to load pajek format files
	 */
	public GraphmlReader() {
		communities = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
	}

	/**
	 * Reader used to read graphml file formats. It receives the path to the
	 * file, reads its contents and generates a Jung Graph
	 * 
	 * @param file
	 *            The url to the source file
	 */
	public GraphmlReader(String file) {
		graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);
		communities = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
		InputStream input;
		try {
			input = new BufferedInputStream(new FileInputStream(file));
			reader.inputGraph(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This class was initially designed to return an array of nodes but it
	 * needed to be changed to a TreeMap because it was not possible to
	 * determine the final size of the array in advance.
	 * 
	 * @param nodeImportAttributes
	 * @param saveCategoricalAttributes
	 *            true if you want to save the categorical attributes in the
	 *            general collection of attributes stored in Mapper Class. The
	 *            collection does not accept duplicates. Enabling this might
	 *            occupy a big chunk of memory because for instance all the node
	 *            names will be stored in the collection
	 * @return The collection of nodes ordered by the Integer version of their
	 *         Id
	 */
	private TreeMap<Integer, Node> makeNodes(String[] nodeImportAttributes, boolean saveCategoricalAttributes) {
		System.out.println(this.getClass().getName() + " Instantiating Nodes...");

		TreeMap<Integer, Node> theNodes = new TreeMap<Integer, Node>();

		// *** Go over graph vertex and set all nodes
		for (Vertex vertex : graph.getVertices()) {
			// Get vertex ID
			int id = Integer.parseInt(vertex.getId().toString().replace("n", ""));
			// Make a node with the retrieved ID
			Node nodeTmp = new Node(String.valueOf(id));

			try {
				// For the first two attributes: node community and node
				// name
				if (vertex.getProperty(nodeImportAttributes[0]) != null) {
					// set root community
					nodeTmp.setCommunity("Root", 0);
					// set next community
					nodeTmp.setCommunity(vertex.getProperty(nodeImportAttributes[0]).toString(), 1);
					addCommunity(vertex.getProperty(nodeImportAttributes[0]).toString());
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
			// Load all the node attributes from the graphml file
			for (String key : vertex.getPropertyKeys()) {
				nodeTmp.setAttribute(key, vertex.getProperty(key));
			}
			// Setting max min boundaries in Mapper class
			Mapper.getInstance().setMaxMinNodeAttributes(nodeTmp);
			if (saveCategoricalAttributes) {
				Mapper.getInstance().setCategoricalNodeAttributes(nodeTmp);
			}
			theNodes.put(id, nodeTmp);
		}
		return theNodes;
	}

	/**
	 * GENERAL METHOD TO GET THE JUNG GRAPH
	 * 
	 * @param nodeImportAttributes
	 *            the vector of node attributes
	 * @param edgeImportAttributes
	 *            the vector of edge attributes
	 * @param saveCategoricalAttributes
	 *            true if you want to save the categorical attributes in the
	 *            general collection of attributes stored in Mapper Class. The
	 *            collection does not accept duplicates. Enabling this might
	 *            occupy a big chunk of memory because for instance all the node
	 *            names will be stored in the collection
	 * @return
	 */
	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String[] nodeImportAttributes,
			String[] edgeImportAttributes, boolean saveCategoricalAttributes) {
		// Create the graph to be returned
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();
		// Notify progress on console
		System.out.println(this.getClass().getName() + " Getting Jung Directed Graph...");
		// Hash map <Name of community, Node object of a community>
		vCommunityNodes = new HashMap<String, Node>();

		// **** MAKE NODES ****
		TreeMap<Integer, Node> nodes = makeNodes(nodeImportAttributes, saveCategoricalAttributes);

		// **** CREATE EDGES ****
		System.out.println(this.getClass().getName() + " Instantiating Edges...");
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
			// Setting max min boundaries in Mapper class
			Mapper.getInstance().setMaxMinEdgeAttributes(e);
			if (saveCategoricalAttributes) {
				Mapper.getInstance().setCategoricalEdgeAttributes(e);
			}

			// For the first order community graph
			Edge metaE = new Edge(vCommunityNodes.get(nodes.get(idSource).getCommunity(1)),
					vCommunityNodes.get(nodes.get(idTarget).getCommunity(1)), true);
			// if no attributes selected set the weight to 1
			metaE.setAttribute("weight", 1);

			if (!edgesBetweenCommunities.contains(metaE)) {
				edgesBetweenCommunities.add(metaE);
			}

			// Create the edge with source and target nodes
			rtnGraph.addEdge(e, nodes.get(idSource), nodes.get(idTarget), EdgeType.DIRECTED);
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
	public void assignNodeAttributes(Vertex vertex, Node node, String[] nodeImportAttributes) {
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
			System.out.println(this.getClass().getName() + ": No vertex label match, Check the attribute key");
		}
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
			vCommunityNodes.put(string, new Node(string));
		}
	}

	/**
	 * Looks for the equal node in the graph
	 * 
	 * @param graph
	 * @param searchedNode
	 * @return
	 */
	protected Node getEqualNode(edu.uci.ics.jung.graph.Graph<Node, Edge> graph, Node searchedNode) {
		Node rtn = null;
		for (Node node : graph.getVertices()) {
			if (searchedNode.equals(node)) {
				rtn = node;
				return rtn;
			}
		}
		return rtn;
	}

	/**
	 * Return the list of all keys associated to vertex read from the graphml
	 * file
	 * 
	 * @return
	 */
	public String[] getNodeGraphmlKeys() {
		String[] rtn = null;
		for (Vertex v : graph.getVertices()) {
			v.getPropertyKeys().toArray(rtn);
			return rtn;
		}
		return rtn;
	}

	/**
	 * Return the list of all keys associated to edges read from the graphml
	 * file
	 * 
	 * @return
	 */
	public String[] getEdgeGraphmlKeys() {
		String[] rtn = null;
		for (com.tinkerpop.blueprints.Edge e : graph.getEdges()) {
			e.getPropertyKeys().toArray(rtn);
			return rtn;
		}
		return rtn;
	}

	public ArrayList<Edge> getEdgesBetweenCommunities() {
		return edgesBetweenCommunities;
	}
}