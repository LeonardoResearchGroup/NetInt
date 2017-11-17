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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import netInt.graphElements.Node;

/**
 * A collection of objects arranged in a nested structure: hashMap<K1,
 * hashMap<K2, hashSet<E>>>. It is extremely efficient to look for an object of
 * type E if K1 and K2 are attributes of E
 * 
 * This nested collection has a tiered structure as follows:
 * 
 * A HashMap at top tier (TIER_0) with instances of type K1 as keys paired with
 * values of type HashMap
 * 
 * The nested HashMaps inside TIER_0 constitute the tier (TIER_1). Each element
 * of TIER_1 HashMaps has keys of type K2 paired with a value of type HashSet.
 * 
 * The HasSet is of type E
 * 
 * @author juan salamanca
 * 
 *         October 2017
 *
 * @param <K1>
 *            Key type of tier 0 HashMap
 * @param <K2>
 *            Key type of tier 1 HashMap
 * @param <E>
 *            GraphElement type either netInt.graphElements.Node or
 *            netInt.graphElements.Edge
 */
public abstract class NestedMap<K1, K2, E> extends Thread{

	private boolean initialized = false;

	protected HashMap<K1, HashMap<K2, HashSet<E>>> nestedStructure;
	
	protected Collection<E> elements;
	
	protected K1 filter;
	

	/**
	 * initializes Nested Structure. It calls private method arrangeElements().
	 * 
	 * @param elements
	 *            collection of elements
	 * @param filter
	 *            attribute of elements. Not tried with null or N.As as of
	 *            October 2017
	 */
	
	public void run() {
			nestedStructure = arrangeElements(elements, filter);
			initialized = true;
	}

	/**
	 * Arrange elements from the given Collection in a tiered structure as
	 * follows:
	 * 
	 * A HashMap at top tier (TIER_0) with K1 instances as keys and nested
	 * HashMaps as values.
	 * 
	 * The TIER_0 nested HashMaps constitute the tier (TIER_1). Each has K2
	 * instances as keys a corresponding HashSet as value.
	 * 
	 * The internal HasSet is of type E
	 * 
	 * 
	 * @param elements
	 * @param filter
	 * @return
	 */
	private HashMap<K1, HashMap<K2, HashSet<E>>> arrangeElements(Collection<E> elements, K1 filter) {

		// Collection to be returned
		HashMap<K1, HashMap<K2, HashSet<E>>> communities = new HashMap<K1, HashMap<K2, HashSet<E>>>();

		// Go over the collection of elements
		for (E e : elements) {

			K1 k1 = getAttributeValue(e, filter);

			K2 k2 = getElementK2(e);

			// If TIER 0 hashMap DOES contain that community name key
			if (communities.containsKey(k1)) {

				// If TIER 1 hashMap has the k2 key
				if (communities.get(k1).containsKey(k2)) {

					// get TIER 0 hashMap and TIER 1 hashSet and add the
					// element
					communities.get(k1).get(k2).add(e);

					// If TIER 1 hashMap does not contains the k2 key
				} else {

					// Initialize TIER 2 hashSet
					HashSet<E> setTemp = new HashSet<E>();

					// add element to set
					setTemp.add(e);

					// put TIER 2 hashMap in TIER 1 hashMap
					communities.get(k1).put((K2) k2, setTemp);
				}

				// If TIER 0 hashMap DOES NOT contain that community name
				// key
			} else {

				// initialize TIER 1 hashMap
				HashMap<K2, HashSet<E>> mapTemp = new HashMap<K2, HashSet<E>>();

				// Initialize TIER 2 hashSet
				HashSet<E> setTemp = new HashSet<E>();

				// add node to TIER HashSet
				setTemp.add(e);

				// put TIER 2 hashSet in TIER 1 hashMap
				mapTemp.put((K2) k2, setTemp);

				// put TIER 1 hashMap in TIER 0 hashMap
				communities.put(k1, mapTemp);
			}
		}
		return communities;
	}

	// ***** Abstract methods *****

	/**
	 * Gets the value under the attribute name from the set of E's attributes
	 * 
	 * @param e
	 *            Element
	 * @param attributeName
	 *            attribute name
	 * @return the value as instance of K1
	 */
	protected abstract K1 getAttributeValue(E e, K1 attributeName);

	/**
	 * Makes the key to which this element must be associated
	 * 
	 * @param e
	 *            element
	 * @return the key as K2 instance
	 */
	protected abstract K2 getElementK2(E e);

	// ***** Getters *****

	public Set<K1> getTIER_0Keys() {
		return nestedStructure.keySet();
	}

	public Set<K2> getTIER_1Keys(String community) {
		return nestedStructure.get(community).keySet();
	}

	/**
	 * Retrieves Tier_0 values
	 * 
	 * @param key
	 *            Key1
	 * @return a hashmap <K2,HashSet<E>>
	 */
	public HashMap<K2, HashSet<E>> getValueOfK1(K1 key) {
		return nestedStructure.get(key);
	}

	/**
	 * Retrieves Tier_0 values
	 * 
	 * @param key1
	 *            Key1
	 * @param key2
	 *            Key2
	 * @return a HashSet<E>
	 */
	public HashSet<E> getValueOfK1_K2(K1 key1, K2 key2) {
		return getValueOfK1(key1).get(key2);
	}

	// ***** Reports *****

	/**
	 * Prints out the structure result
	 * 
	 * @param tier1
	 */
	public void report(boolean tier1) {

		for (K1 s : getTIER_0Keys()) {
			System.out.println(
					"... TIER 0 : " + s + " contains " + this.getTIER_1Keys((String) s).size() + " tier 1 keys");
			// TIER 1
			if (tier1) {
				System.out.println("    TIER 1 keys : ");
				for (K2 c : this.getTIER_1Keys((String) s)) {
					System.out.println(
							"     " + c + " contains " + this.nestedStructure.get(s).get(c).size() + " values");
					for (E n : this.nestedStructure.get(s).get(c)) {
						Node nd = (Node) n;
						System.out.println("       " + nd.getId() + " " + nd.getName());
					}

				}
			}
		}
		System.out.println("***** TIER 0 : contains " + this.getTIER_0Keys().size() + " total keys");
	}

	public boolean isInitialized() {
		return initialized;
	}
}
