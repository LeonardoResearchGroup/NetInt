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
package netInt.comparators;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import netInt.graphElements.Edge;
import netInt.graphElements.Node;

/**
 * Utility for evaluating if there is at least one edge between to nodes in a
 * graph. The data structure is optimized for quick search in large datasets.
 * 
 * 
 * The idea here is to have a table containing source nodes as keys and their
 * values are a collection of target nodes. The collection of target nodes is a
 * second table which keys are single chars and their matched value are the
 * actual target nodes. The keys of the latter Hashtable are collected from the
 * first character of the node's id
 *
 * 
 * @author juan salamanca Oct 2017
 *
 */
public class LinkComparator {

	private Hashtable<Node, Hashtable<Character, ArrayList<Node>>> cache;

	/**
	 * Constructor
	 */
	public LinkComparator() {
		cache = new Hashtable<Node, Hashtable<Character, ArrayList<Node>>>();
	}

	/**
	 * Determines if the source and target have been previously linked to each
	 * other in the scope of the cache. It observes directionality, i.e., if A
	 * is linked to B, B is not necessarily linked to A
	 * 
	 * @param source
	 *            source node
	 * @param target
	 *            target node
	 * @return true if linked
	 */
	private boolean sourceTargetLinked(Node source, Node target) {
		Character initial = target.getId().charAt(0);
		if (cache.containsKey(source)) {
			if (cache.get(source).containsKey(initial)) {
				if (cache.get(source).get(initial).contains(target))
					return true;
				else
					return false;
			}
			return false;
		}
		return false;
	}

	/**
	 * Empties cache
	 */
	public void reset() {
		cache = new Hashtable<Node, Hashtable<Character, ArrayList<Node>>>();
	}

	/**
	 * Determines if there is a link in cache between source and target. If
	 * there are none, it creates one and stores it in cache.
	 * 
	 * @param source
	 *            source node
	 * @param target
	 *            target node
	 * @return
	 *            true if it creates the first link between the pair of nodes
	 */
	public boolean link(Node source, Node target) {

		// Get target initial
		Character initial = new Character(target.getId().charAt(0));

		// if cache DOES NOT contain the source
		if (!cache.containsKey(source)) {

			// Initialize the cache's Hashtables
			Hashtable<Character, ArrayList<Node>> tmpTable = new Hashtable<Character, ArrayList<Node>>();
			ArrayList<Node> tmpTargets = new ArrayList<Node>();

			// Add both source and target nodes to cache
			tmpTargets.add(target);
			tmpTable.put(initial, tmpTargets);
			cache.put(source, tmpTable);
			return true;

			// If cache DOES contain the source
		} else {

			// If target is not in list
			if (!sourceTargetLinked(source, target)) {

				// If the Character key DOES exist in internal hashtable
				if (cache.get(source).containsKey(initial)) {
					// if the internal list contains the target
					// if (!cache.get(source).get(initial).contains(target)) {
					cache.get(source).get(initial).add(target);
					return true;
					// }else{
					// return false;
					// }

					// If the Character key DOES NOT exist in internal
					// hashtable
				} else {
					ArrayList<Node> tmpTargets = new ArrayList<Node>();
					tmpTargets.add(target);
					cache.get(source).put(initial, tmpTargets);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * makes a link in the cache between source and target and adds an Edge
	 * weight 1 to the given collection
	 * 
	 * @param source
	 *            the source node
	 * @param target
	 *            the target node
	 * @param edges
	 *            the list of edges to check if any link exists between source
	 *            and target
	 */
	public void buildLink(Node source, Node target, ArrayList<Edge> edges) {
		if (link(source, target)) {
			Edge temp = new Edge(source, target, true);
			temp.setAbsoluteAttribute("weight", 1);
			edges.add(temp);
		}
	}

	/**
	 * Makes a list of edges from stored values in cache
	 * 
	 * @return the list of edges
	 */
	public ArrayList<Edge> cacheAsList() {
		ArrayList<Edge> rtn = new ArrayList<Edge>();
		Set<Node> sourceKeys = cache.keySet();
		for (Node source : sourceKeys) {
			Set<Character> targetKeys = cache.get(source).keySet();
			for (Character c : targetKeys) {
				for (Node target : cache.get(source).get(c)) {
					rtn.add(new Edge(source, target, true));
				}
			}
		}

		return rtn;
	}

	/**
	 * Prints a list of edges from stored values in cache
	 * 
	 */
	public void printCacheTable() {
		Set<Node> sourceKeys = cache.keySet();
		for (Node source : sourceKeys) {
			System.out.println(" --- source" + source.getId());
			Set<Character> targetKeys = cache.get(source).keySet();
			for (Character c : targetKeys) {
				System.out.println("          Key:" + c);
				for (Node target : cache.get(source).get(c)) {
					System.out.println("             Target:" + target.getId());

				}
			}
		}
	}
}
