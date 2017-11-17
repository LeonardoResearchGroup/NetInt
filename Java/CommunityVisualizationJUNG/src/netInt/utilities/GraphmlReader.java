/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import netInt.comparators.LinkComparator;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.customCollections.NestedNodeMap;
import netInt.utilities.mapping.Mapper;

/**
 * 
 * @author jsalam
 *
 */
public class GraphmlReader {
	private Graph graph;

	// Hash map <Name of community, Node object of a community>
	private HashMap<String, Node> vCommunityNodes;

	// ArrayList of community values obtained from the import file
	private ArrayList<String> communities;

	// Edges between VCommunities
	private ArrayList<Edge> edgesBetweenCommunities;

	// Collection of community subgraphs
	private  HashMap<String, DirectedSparseMultigraph<Node, Edge>> subGraphs;

	/**
	 * Reader usually used to load pajek format files
	 */
	public GraphmlReader() {
		communities = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
		subGraphs = new HashMap<String, DirectedSparseMultigraph<Node, Edge>>();
	}

	/**
	 * Reader used to read graphml file formats. It receives the path to the
	 * file, reads its contents and generates a Jung Graph. It relies on
	 * GraphMLReader from Thinkerpop
	 * 
	 * @param file
	 *            The url to the source file
	 */
	public GraphmlReader(String file) {
		graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);
		InputStream input;
		try {
			input = new BufferedInputStream(new FileInputStream(file));
			reader.inputGraph(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		communities = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
		subGraphs = new HashMap<String, DirectedSparseMultigraph<Node, Edge>>();
	}

	/**
	 * This class was initially designed to return an array of nodes but it
	 * needed to be changed to a TreeMap because it was not possible to
	 * determine the final size of the array in advance.
	 * 
	 * @param nestedAttributesOrder
	 *            the vector of ordered node categorical attributes
	 * 
	 * @param nodeImportAttributes
	 *            The list of user defined attributes for node importing
	 * @param saveCategoricalAttributes
	 *            true if you want to save the categorical attributes in the
	 *            general collection of attributes stored in Mapper Class. The
	 *            collection does not accept duplicates. Enabling this might
	 *            occupy a big chunk of memory because for instance all the node
	 *            names will be stored in the collection
	 * @return The collection of nodes ordered by the Integer version of their
	 *         Id
	 */
	private TreeMap<Integer, Node> makeNodes(String[] nestedAttributesOrder, String[] nodeImportAttributes,
			boolean saveCategoricalAttributes) {
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
				if (vertex.getProperty(nestedAttributesOrder[0]) != null) {

					// set root community as absolute attribute
					nodeTmp.setCommunity("Root", 0);

					// set next community as relative attribute
					nodeTmp.setCommunity(vertex.getProperty(nestedAttributesOrder[0]).toString(), 1);
					addCommunity(vertex.getProperty(nestedAttributesOrder[0]).toString());
				} else {
					// for root importing, set root community to absolute and
					// relative attributes
					nodeTmp.setCommunity("Root", 0);
					nodeTmp.setCommunity("Root", 1);
				}

				// Label
				if (vertex.getProperty(nodeImportAttributes[0]) != null) {
					nodeTmp.setName(vertex.getProperty(nodeImportAttributes[0]).toString());
				} else {
					nodeTmp.setName("no named yet");
					// throw new NullPointerException();
				}

			} catch (NullPointerException e) {
				System.out.println(this.getClass().getName() + " NullPointerException making nodes ");
			}

			for (int i = 2; i < nodeImportAttributes.length; i++) {
				try {
					// For the remaining attributes
					// Check if it does exist a property matching other
					// attributes
					if (vertex.getProperty(nodeImportAttributes[i]) != null) {
						nodeTmp.setAbsoluteAttribute(nodeImportAttributes[i],
								vertex.getProperty(nodeImportAttributes[i]));
					} else {
						// for root importing
						throw new NullPointerException();
					}
				} catch (NullPointerException e) {
					System.out.println(this.getClass().getName()
							+ " NullPointerException making nodes with remaining attributes ");
				}
			}

			// Load all the remaining node attributes from the graphml file and
			// set the attributes from the file
			for (String key : vertex.getPropertyKeys()) {
				if (nodeTmp.getAttribute(key) == null) {
					nodeTmp.setAbsoluteAttribute(key, vertex.getProperty(key));
				}
			}
			// Setting max min boundaries in Mapper class
			// Mapper.getInstance().setMaxMinNodeAttributes(nodeTmp);
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
	 * @param nestedAttributesOrder
	 *            the ordered vector of node attributes
	 * 
	 * @param nodeLabelAttributes
	 *            the vector of node attributes
	 * @param edgeImportAttributes
	 *            the vector of edge attributes
	 * @param saveCategoricalAttributes
	 *            true if you want to save the categorical attributes in the
	 *            general collection of attributes stored in Mapper Class. The
	 *            collection does not accept duplicates. Enabling this might
	 *            occupy a big chunk of memory because for instance all the node
	 *            names will be stored in the collection
	 * @return THE JUNG GRAPH as DirectedSparseMultigraph
	 */
	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String[] nestedAttributesOrder,
			String[] nodeLabelAttributes, String[] edgeImportAttributes, boolean saveCategoricalAttributes) {

		// Create the graph to be returned
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();

		// Notify progress on console
		System.out.println(this.getClass().getName() + " Getting Jung Directed Graph...");

		// Hash map <Name of community, Node object of a community>
		vCommunityNodes = new HashMap<String, Node>();

		// **** MAKE NODES ****
		TreeMap<Integer, Node> nodes = makeNodes(nestedAttributesOrder, nodeLabelAttributes, saveCategoricalAttributes);

		// **** CREATE EDGES ****
		System.out.println(this.getClass().getName() + " Instantiating Edges...");

		// The comparator of former links between nodes
		LinkComparator linkComparator = new LinkComparator();

		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {

			// From each edge retrieve the source and target vertex
			Vertex sourceVertex = edge.getVertex(Direction.OUT);
			Vertex targetVertex = edge.getVertex(Direction.IN);

			// Get their ID
			Integer idSource = Integer.parseInt(sourceVertex.getId().toString().replace("n", ""));
			Integer idTarget = Integer.parseInt(targetVertex.getId().toString().replace("n", ""));

			// **** MAKE EDGE FROM SOURCE AND TARGET NODES ****

			Node source = nodes.get(idSource);
			Node target = nodes.get(idTarget);

			String sourceCommunityName = source.getCommunity(1);
			String targetCommunityName = target.getCommunity(1);

			netInt.graphElements.Edge e = new netInt.graphElements.Edge(source, target, true);

			// set edges and nodes to community subGraphs
			addEdgeToSubgraphs(e);

			// Check if the edge has any of the edge Import attributes
			if (edgeImportAttributes.length > 0) {
				for (int i = 0; i < edgeImportAttributes.length; i++) {
					try {

						// Retrieve the attribute value and check if it is null
						if (edge.getProperty(edgeImportAttributes[i]) != null) {
							Object tmpProperty = edge.getProperty(edgeImportAttributes[i]);

							// Add the attribute value to the temporal edge;
							e.setAbsoluteAttribute(edgeImportAttributes[i], tmpProperty);
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
				e.setAbsoluteAttribute("weight", 1);
			}

			// Load all the edge attributes from the graphml file
			for (String key : edge.getPropertyKeys()) {
				if (e.getAttribute(key) == null) {
					e.setAbsoluteAttribute(key, edge.getProperty(key));
				}
			}

			// Setting max min boundaries in Mapper class
			// Mapper.getInstance().setMaxMinEdgeAttributes(e);
			if (saveCategoricalAttributes) {
				Mapper.getInstance().setCategoricalEdgeAttributes(e);
			}

			// Create edges for communities with at least one edge connecting
			// nodes from both communities. Here we retrieve the Node object of
			// a community passing the key: Name of community

			Node vCSource = vCommunityNodes.get(sourceCommunityName);
			Node vCTarget = vCommunityNodes.get(targetCommunityName);

			// if there are no loop edges connecting a community with itself
			if (!vCSource.equals(vCTarget)) {

				// Make edges between communities
				linkComparator.buildLink(vCSource, vCTarget, edgesBetweenCommunities);

			}

			// Create the edge with source and target nodes
			rtnGraph.addEdge(e, source, target, EdgeType.DIRECTED);
		}

		// linkComparator.printCacheTable();
		linkComparator.reset();

		System.out.println(this.getClass().getName() + "  Subgraphs size: " + subGraphs.size());
		return rtnGraph;

	}

	/**
	 * Add edge to Jung DirectedSparseMultigraphs mapped to community name keys.
	 * The hashMap containing these subgraphs provides them to container at the
	 * assembly time in the assembler class
	 * 
	 * The edges added to each subgraph are only those linking nodes within the
	 * same community. If source and target nodes belong to different
	 * communities, only the nodes are added to their respective communities
	 * 
	 * @param e
	 *            the edge to be added
	 */
	private void addEdgeToSubgraphs(Edge e) {
		Node source = e.getSource();// nodes.get(idSource);
		Node target = e.getTarget();// nodes.get(idTarget);

		String sourceCommunityName = source.getCommunity(1);
		String targetCommunityName = target.getCommunity(1);

		// If source and community belong to the same community
		if (sourceCommunityName.equals(targetCommunityName)) {

			// if the source community exists in subgraphs
			if (subGraphs.containsKey(sourceCommunityName)) {
				subGraphs.get(sourceCommunityName).addEdge(e, source, target);
			} else {

				// If community does not exist in subgraphs
				// Make the subgraph
				DirectedSparseMultigraph<Node, Edge> tmp = new DirectedSparseMultigraph<Node, Edge>();

				// Add the edge
				tmp.addEdge(e, source, target);

				// Put edge in subgraph
				subGraphs.put(sourceCommunityName, tmp);
			}
		} else {
			// Put source node in subgraphs
			if (subGraphs.containsKey(sourceCommunityName)) {
				subGraphs.get(sourceCommunityName).addVertex(source);
			} else {
				// If community does not exist in subgraphs
				// Make the subgraph
				DirectedSparseMultigraph<Node, Edge> tmp = new DirectedSparseMultigraph<Node, Edge>();

				// Add the edge
				tmp.addVertex(source);

				// Put edge in subgraph
				subGraphs.put(sourceCommunityName, tmp);
			}

			// Put target node in subgraphs
			if (subGraphs.containsKey(targetCommunityName)) {
				subGraphs.get(targetCommunityName).addVertex(target);
			} else {
				// If community does not exist in subgraphs
				// Make the subgraph
				DirectedSparseMultigraph<Node, Edge> tmp = new DirectedSparseMultigraph<Node, Edge>();

				// Add the edge
				tmp.addVertex(target);

				// Put edge in subgraph
				subGraphs.put(targetCommunityName, tmp);
			}
		}
	}

	/**
	 * Assigns attributes retrieved from a vertex of a JUNG graph to a node. The
	 * attribute names come from a list obtained from the source file, usually a
	 * graphml
	 * 
	 * @param vertex
	 *            a vertex of a JUNG graph
	 * @param node
	 *            the recipient node
	 * @param nodeImportAttributes
	 *            list obtained from the source file
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
						node.setAbsoluteAttribute(nodeImportAttributes[i], vertex.getProperty(nodeImportAttributes[i]));
					} else
						throw new NullPointerException();
				}
			}
		} catch (NullPointerException e) {
			// e.printStackTrace();
			System.out.println(this.getClass().getName() + ": No vertex label match, Check the attribute key");
		}
	}

	public HashMap<String, DirectedSparseMultigraph<Node, Edge>> getSubGraphs() {
		return subGraphs;
	}

	/**
	 * ArrayList of community values obtained from the import file
	 * 
	 * @return ArrayList of community values
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
	 * @return the list of all keys associated to vertex
	 */
	public String[] getNodesGraphmlKeys() {
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
	 * @return the list of all keys associated to edges
	 */
	public String[] getEdgesGraphmlKeys() {
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
