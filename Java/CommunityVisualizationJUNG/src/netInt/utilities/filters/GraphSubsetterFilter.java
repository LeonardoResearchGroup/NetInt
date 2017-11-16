/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 *  
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
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package netInt.utilities.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.GraphLoader;
import netInt.utilities.customCollections.NestedNodeMap;

public class GraphSubsetterFilter {

	// The collection for subGraphs
	private HashMap<String, DirectedSparseMultigraph<Node, Edge>> subGraphs;

	// The collection of betweens. Used as buffer
	private ArrayList<Edge> betweens;

	public GraphSubsetterFilter() {
		subGraphs = new HashMap<String, DirectedSparseMultigraph<Node, Edge>>();
		betweens = new ArrayList<Edge>();

		// Method A
		// getSubCommunities(subGraphs, betweens);

		// Method B
		// classifyEdges(subGraphs);
	}

	public DirectedSparseMultigraph<Node, Edge> getSubGraphForCommunity(String community) {
		return subGraphs.get(community);
	}

	private void getSubCommunities(NestedNodeMap<String, Character> nMap, HashMap<String, DirectedSparseMultigraph<Node, Edge>> subGraphs,
			ArrayList<Edge> betweens) {

		System.out.println(this.getClass().getName() + "     Building subgraphs for:");

		// **** A collection of Between Edge Lists
		// HashMap<String, HashMap<String, ArrayList<Edge>>> betweenEdgeLists =
		// new HashMap<String, HashMap<String, ArrayList<Edge>>>();
		float progress = 0f;
		try {
			// Go over all community names / TIER_0 keys
			for (String community : nMap.getTIER_0Keys()) {
				progress++;
				double percentage = Math.floor((progress / nMap.getTIER_0Keys().size()) * 100);
				System.out.println("       Retrieving edges for community: " + percentage + "%  " + community);

				// Go over all node TIER_1 keys
				for (Character initial : nMap.getTIER_1Keys(community)) {

					// Go over all nodes in the community
					for (Node n : nMap.getValueOfK1_K2(community, initial)) {

						// Get all out-edges for that node
						Collection<Edge> edges = GraphLoader.theGraph.getIncidentEdges(n);

						/*
						 * Pida al nested map la colección de vínculos que
						 * entran al nodo
						 * 
						 * Pida los nodos de origen
						 * 
						 * Preguntele a la comunidad del nested map si contiene
						 * cada uno de ellos
						 * 
						 * si si, adicione el vínculo
						 * 
						 * si no, adicione el vinculo a la comunidad de origen
						 * del nodo de origen (borre el nodo de origen del
						 * nested map para que no sean vueltos a clasificar)
						 * 
						 * Repita lo anterior pero esta vez pidiendo la
						 * colección de nodos de destino, siendo este nodo el de
						 * origen
						 */

						// go over all edges
						for (Edge e : edges) {

							// If the target belongs to this community
							// if
							// (nMap.communityOfNode(e.getTarget()).equals(community))
							// {

							addEdgeToSubGraph(community, e, subGraphs);
							// }

							// If the target does not belong to this community
							// } else {
							//
							// addEdgeToBetweenEdgeList(community,
							// communityOfTarget, e, betweenEdgeLists);
							// }
						}
					}
				}
			}
			/*
			 * ALTERNATIVE B Then apply a retailALL() between the collection of
			 * vertices of the problemGraph and the set of nodes in the
			 * nestedMap belonging to this community
			 */
			// Prints
			// printSubGraphs(subGraphs);
			// printBetweenEdgeLists(betweenEdgeLists);

			System.out.println("Total Communities: " + subGraphs.keySet().size() + " = " + nMap.getTIER_0Keys().size());
		} catch (NullPointerException np) {
			System.out.println(this.getClass().getName()
					+ ". NestedStructure not Initialized. Invoke init() method on instance of NestedNodeMap before passing it as parameter. "
					+ np.toString());
		}
	}

	private void classifyEdges(NestedNodeMap<String, Character> nMap, HashMap<String, DirectedSparseMultigraph<Node, Edge>> subGraphs) {

		for (String s : nMap.getTIER_0Keys()) {
			DirectedSparseMultigraph<Node, Edge> problemGraph = new DirectedSparseMultigraph<Node, Edge>();
			subGraphs.put(s, problemGraph);
		}

		float progress = 0f;
		for (Edge e : GraphLoader.theGraph.getEdges()) {
			
			// Calculating progress
			progress++;
			double percentage = Math.floor((progress / GraphLoader.theGraph.getEdges().size()) * 100);
			System.out.println("       Setting edges to communities: " + percentage + "%");

			// Getting community names
			String sourceCommunityName = nMap.communityOfNode(e.getSource());
			String targetCommunityName = nMap.communityOfNode(e.getTarget());

			// Put edge in subgraph
			if (sourceCommunityName.equals(targetCommunityName)) {
				subGraphs.get(sourceCommunityName).addEdge(e, e.getSource(), e.getTarget());
			} else {

				// Put nodes in subgraphs
				subGraphs.get(targetCommunityName).addVertex(e.getTarget());
				subGraphs.get(sourceCommunityName).addVertex(e.getSource());
			}
		}

		System.out.println(subGraphs.size());
	}

	private void addEdgeToSubGraph(String com, Edge e,
			HashMap<String, DirectedSparseMultigraph<Node, Edge>> subGraphs) {

		// If the collection of subGraphs contains this
		// community
		if (subGraphs.containsKey(com)) {

			// get the associated graph and add the edge
			subGraphs.get(com).addEdge(e, e.getSource(), e.getTarget());

		} else {

			// Create a temporal graph
			DirectedSparseMultigraph<Node, Edge> temp = new DirectedSparseMultigraph<Node, Edge>();

			// Add edge
			temp.addEdge(e, e.getSource(), e.getTarget());

			// Put it in subGraph Collection
			subGraphs.put(com, temp);
		}
	}

	private void addEdgeToBetweenEdgeList(String sourceCom, String targetCom, Edge e,
			HashMap<String, HashMap<String, ArrayList<Edge>>> betweenEdgeLists) {

		// If the collection of between edge lists contains a key for community
		if (betweenEdgeLists.containsKey(sourceCom)) {

			// If the collection of between edge lists contains a key for the
			// target's community
			if (betweenEdgeLists.get(sourceCom).containsKey(targetCom)) {

				// add a node to the list
				betweenEdgeLists.get(sourceCom).get(targetCom).add(e);

			} else {

				// if there is no key for that target community
				// Make the list
				ArrayList<Edge> tempEdgeList = new ArrayList<Edge>();

				// add the edge
				tempEdgeList.add(e);

				// Put the list in the collection
				betweenEdgeLists.get(sourceCom).put(targetCom, tempEdgeList);
			}

		} else {
			// If the collection of between edge lists does
			// not contain a key for community

			// Make the list
			ArrayList<Edge> tempEdgeList = new ArrayList<Edge>();

			// add the edge
			tempEdgeList.add(e);

			// Initialize the hashmap
			HashMap<String, ArrayList<Edge>> mapTemp = new HashMap<String, ArrayList<Edge>>();

			// Put elements in hashMap
			mapTemp.put(targetCom, tempEdgeList);

			// Put the list in the collection
			betweenEdgeLists.put(sourceCom, mapTemp);
		}
	}

	private void printSubGraphs(HashMap<String, DirectedSparseMultigraph<Node, Edge>> subGraphs) {

		for (String s : subGraphs.keySet()) {
			System.out.println("\n Community: " + s);
			for (Edge e : subGraphs.get(s).getEdges()) {
				System.out.println("  s: " + e.getSource().getName() + " t:" + e.getTarget().getName());
			}
		}
	}

	private void printBetweenEdgeLists(HashMap<String, HashMap<String, ArrayList<Edge>>> betweenEdgeLists) {

		for (String s : betweenEdgeLists.keySet()) {
			System.out.println("\n Community source: " + s);
			for (String t : betweenEdgeLists.get(s).keySet()) {
				System.out.println("    Community target: " + t);
				ArrayList<Edge> tmp = betweenEdgeLists.get(s).get(t);
				for (Edge e : tmp) {
					System.out.println(
							"      source: " + e.getSource().getName() + ". Target:" + e.getTarget().getName());
				}
			}
		}
	}

}
