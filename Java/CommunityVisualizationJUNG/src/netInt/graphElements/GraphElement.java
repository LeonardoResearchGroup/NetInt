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
package netInt.graphElements;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Abstract class that handles two collections for storage of graphElements'
 * attributes (either nodes or edges). The first collection contains absolute
 * attributes, i.e., raw attributes independent of any graph processing, e.g,
 * the amount sent by one payer to one beneficiary is the weight of an edge
 * linking their nodes. The second set are relative attributes, i.e., attributes
 * relative to the community to which the element belongs. An example of the
 * latter is node degree after clustering. A node has a relative degree inside
 * each cluster to which it belongs.
 * 
 * For convenience, node and edge graph metrics (inDegree, outDegree, degree)
 * are stored in the collection of relative attributes in numbered sequence,
 * reserving 0 for the root graph.
 * 
 * @author jsalam
 *
 */
public abstract class GraphElement implements Serializable {

	private static final long serialVersionUID = 1L;

	// For key use the attribute name. For value use the attribute value (either
	// Numerical or categorical)
	private HashMap<String, Object> absoluteAttributes;

	// A set of attributes to be initiated by instances of classes that
	// inherit from this class. The integer parameter identifies the
	// community to which a set of relative attributes are related. (0 reserved
	// for root graph)
	private HashMap<Integer, HashMap<String, Float>> relativeAttributes;

	public GraphElement() {
		absoluteAttributes = new HashMap<String, Object>();
		relativeAttributes = new HashMap<Integer, HashMap<String, Float>>();
	}

	// *** Getters
	public HashMap<String, Object> getAttributes() {
		return absoluteAttributes;
	}

	/**
	 * Gets the object attribute associated to the key from the map of absolute
	 * attributes
	 * 
	 * @param key
	 *            key
	 * @return the object attribute associated to the key from the map of absolute
	 *         attributes
	 */
	public Object getAttribute(String key) {
		Object rtn = null;
		try {
			rtn = absoluteAttributes.get(key);
		} catch (NullPointerException np) {
			System.out.println(this.getClass().getName() + " " + np.getMessage());
		}
		return rtn;
	}

	/**
	 * Gets the String representation of the object attribute associated to the key.
	 * The object is retrieved from the map of absolute attributes
	 * 
	 * @param key
	 *            key
	 * @return String representation of attribute object
	 */
	public String getStringAttribute(String key) {
		String rtn = null;
		try {
			rtn = absoluteAttributes.get(key).toString();
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				/*
				 * IMPORTANT: edges belonging to tiers above tier 0 might not have the same
				 * attributes as the root edges, that is why the NullPointerException is
				 * ignored.
				 */
			} else {
				System.out.println(this.getClass().getName() + " Value of attribute named: " + key
						+ " couldn't be casted as String");
			}
		}
		return rtn;
	}

	/**
	 * Gets the float value of the attribute associated to the key. The value is
	 * retrieved from the map of absolute attributes
	 * 
	 * @param key
	 *            key
	 * @return the float value of the attribute associated to the key
	 */
	public float getFloatAttribute(String key) throws NullPointerException {
		Float rtn = null;
		Object value = absoluteAttributes.get(key);
		String type = "No defined.";
		try {
			// If Double
			if (value instanceof Double) {
				type = "Double";
				Double rtnObj = (Double) value;
				rtn = rtnObj.floatValue();
				// If Integer
			} else if (value instanceof Integer) {
				type = "Integer";
				Integer rtnObj = (Integer) value;
				rtn = rtnObj.floatValue();
				// If float
			} else if (value instanceof Float){
				type = "Float";
				rtn = (float) value;
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				/*
				 * IMPORTANT: edges belonging to tiers above tier 0 might not have the same
				 * attributes as the root edges, that is why the NullPointerException is
				 * ignored.
				 */
				throw (e);
			} else {
				System.out.println(this.getClass().getName() + " Value: " + value + " of Attribute key: " + key
						+ " couldn't be casted as " + type + ". " + e.getClass().getName());
			}
		}
		return rtn;
	}

	/**
	 * Gets the int value of the attribute associated to the key. The value is
	 * retrieved from the map of absolute attributes
	 * 
	 * @param key
	 *            key
	 * @return the float value of the attribute associated to the key
	 */
	public int getIntegerAttribute(String key) {
		Integer rtn = null;
		try {
			// If Double
			if (absoluteAttributes.get(key) instanceof Double) {
				Double rtnObj = (Double) absoluteAttributes.get(key);
				rtn = rtnObj.intValue();
				// If Float
			} else if (absoluteAttributes.get(key) instanceof Float) {
				Float rtnObj = (Float) absoluteAttributes.get(key);
				rtn = rtnObj.intValue();
				// If Integer
			} else {
				rtn = (Integer) absoluteAttributes.get(key);
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				/*
				 * IMPORTANT: edges belonging to tiers above tier 0 might not have the same
				 * attributes as the root edges, that is why the NullPointerException is
				 * ignored.
				 */
			} else {
				System.out.println(this.getClass().getName() + "Value of attribute named: " + key
						+ " - couldn't be casted as int");
			}
		}
		return rtn;
	}

	/**
	 * Gets the total number of attributes of this graph element
	 * 
	 * @return the total number of attributes
	 */
	public int getAttributeSize() {
		return absoluteAttributes.size();
	}

	/**
	 * The vector of keys associated to the attributes of this graph element
	 * 
	 * @return The vector of keys associated to the attributes of this graph element
	 */
	public Object[] getAttributeKeys() {
		Set<String> keys = absoluteAttributes.keySet();
		return keys.toArray();
	}

	/**
	 * Prints the pairs of key-value associated to the AbsoluteAttributes of this
	 * graph element
	 */
	public void printAbsoluteAttributes() {
		System.out.println(this.getClass().getName() + " absolute atts of: " + getId());
		Set<String> s = absoluteAttributes.keySet();
		for (String keyName : s) {
			System.out.println("   Key: " + keyName + ", Value: " + absoluteAttributes.get(keyName));
		}
	}

	public void printRelativeAttributes() {
		System.out.println(this.getClass().getName() + " relative atts of: " + getId());

		// Get the set of keys. This means the level in the nested structure of
		// communities
		Set<Integer> keys = relativeAttributes.keySet();

		// For each key / level
		for (Integer i : keys) {
			System.out.println("   Nested Level: " + i);

			// Get the associated HashMap
			HashMap<String, Float> map = relativeAttributes.get(i);

			// Get the set of keys for this HashMap
			Set<String> mapKeys = map.keySet();

			// Iterate over the keys
			for (String keyName : mapKeys) {
				System.out.println("   Key: " + keyName + ", Value: " + map.get(keyName));
			}

		}
	}

	// *** Setters
	/**
	 * @param community
	 *            the community name to which the node belongs
	 * @param key
	 *            the metadata level associated to the community. This means that
	 *            some statistics of the node such as betweenness or degree are
	 *            relative to the community to which it belongs. If the node belongs
	 *            to more than one community, then the key identifies the community
	 *            to which the attributes are associated. 0 is reserved for the root
	 *            community
	 */
	public void setCommunity(String community, int key) {
		// create meta-datum
		ElementAttributeMap metaData = new ElementAttributeMap();
		// assign name
		metaData.setCommunityName(community);
		// add meta-datum to collection of meta-data
		relativeAttributes.put(key, metaData);
	}

	/**
	 * @param metaData
	 *            the Metadata that contains the community attributes
	 * @param key
	 *            the metadata level associated to the community. This means that
	 *            some statistics of the node such as betweenness or degree are
	 *            relative to the community to which it belongs. If the node belongs
	 *            to more than one community, then the key identifies the community
	 *            to which the attributes are associated. 0 is reserved for the root
	 *            community
	 */
	public void setCommunity(ElementAttributeMap metaData, int key) {
		relativeAttributes.put(key, metaData);
	}

	public String getId() {
		String id = (String) absoluteAttributes.get("id");
		return id;
	}

	public abstract void setAbsoluteAttribute(String key, Object value);

	public abstract void setRelativeAttributes(int tier, HashMap<String, Float> attributeSet);

	public void addAbsoluteAtt(String key, Object value) {
		// System.out.println(this.getClass().getName() + " absolute att added.
		// Key: " + key + ", value: "+value.toString());
		absoluteAttributes.put(key, value);
	}

	public void addRelativeAtt(int key, HashMap<String, Float> attributeSet) {
		relativeAttributes.put(key, attributeSet);
	}

	public HashMap<String, Float> getRelativeAttributes(int key) {
		return relativeAttributes.get(key);
	}

	public Collection<HashMap<String, Float>> getRelativeValues() {
		return relativeAttributes.values();
	}

	public Set<Integer> getRelativeKeySet() {
		return relativeAttributes.keySet();
	}

}
