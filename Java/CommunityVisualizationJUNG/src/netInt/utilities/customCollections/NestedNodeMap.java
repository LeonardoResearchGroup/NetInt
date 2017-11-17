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
package netInt.utilities.customCollections;

import java.util.Collection;

import netInt.graphElements.Node;

/**
 * * A collection of node arranged in a nested structure: hashMap<K1,
 * hashMap<K2, hashSet<Node>>>. It is extremely efficient to look for an object
 * of type Node if K1 and K2 are attributes of Node
 * 
 * @author juan salamanca
 * 
 *         October 2017
 *
 * @param <K1>
 *            Key type of hashmap at tier 0
 * @param <K2>
 *            Key type of hashMap at tier 1
 */
public class NestedNodeMap<K1, K2> extends NestedMap<K1, K2, Node> {

	public NestedNodeMap(Collection<Node> elements, K1 filter) {
		super();
		this.elements = elements;
		this.filter = filter;
	}

	public boolean containsNode(Node n) {

		K2 k2 = getElementK2(n);

		for (K1 k1 : getTIER_0Keys()) {

			if (nestedStructure.get(k1).containsKey(k2)) {

				return nestedStructure.get(k1).get(k2).contains(n);
			}
		}

		return false;
	}

	/**
	 * Retrieves a node by its id
	 * 
	 * @param id
	 * @return
	 */
	public Node containsNode(String id) {
		Character initial = id.charAt(0);

		for (K1 k1 : getTIER_0Keys()) {

			if (nestedStructure.get(k1).containsKey(initial)) {

				for (Node n : nestedStructure.get(k1).get(initial)) {

					if (n.getId().equals(id))
						return n;
				}
			}
		}
	return null;
	}

	/**
	 * Retrieves a node by its id
	 * 
	 * @param nodeId
	 * @return
	 */
	public K1 communityOfNode(String nodeId) {
		Character initial = nodeId.charAt(0);

		for (K1 k1 : getTIER_0Keys()) {

			if (nestedStructure.get(k1).containsKey(initial)) {

				for (Node n : nestedStructure.get(k1).get(initial)) {

					if (n.getId().equals(nodeId))
						return k1;
				}
			}
		}
		return null;
	}

	public K1 communityOfNode(Node n) {
		K2 k2 = getElementK2(n);

		for (K1 s : getTIER_0Keys()) {
			if (nestedStructure.get(s).containsKey(k2)) {
				if (nestedStructure.get(s).get(k2).contains(n)) {
					return s;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected K1 getAttributeValue(Node n, K1 attributeName) {
		String tmp = n.getStringAttribute((String) attributeName);
		return (K1) tmp;
	}

	@SuppressWarnings("unchecked")
	protected K2 getElementK2(Node n) {
		Character tmp = n.getId().charAt(0);
		return (K2) tmp;
	}

}
