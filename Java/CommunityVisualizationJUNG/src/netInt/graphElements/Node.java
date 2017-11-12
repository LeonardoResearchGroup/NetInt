/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package netInt.graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import netInt.utilities.mapping.Mapper;

/**
 * This class has two predefined attributes stored in the hashMap that use these
 * keys: id (String) and (label (String). Any other attribute is stored with the
 * key retrieved from the graph source file, usually a graphml. *** IMPORTANT***
 * the attribute "id" defines the node identity therefore it must be a unique
 * identifier
 * 
 * @author juan salamanca Nov 2016
 */
public class Node extends GraphElement implements Serializable, Comparable<Node> {

	private static final long serialVersionUID = 1L;
	// True if the node searcher query matches any of this node's attributes
	private boolean isFound = false;

	public Node() {
		super();
	}

	public Node(String id) {
		super();
		ElementAttributeMap metaData = new ElementAttributeMap();
		// Initialize basic attributes
		absoluteAttributes.put("id", id);
		absoluteAttributes.put("label", "no name");
		relativeAttributes.put(0, metaData);
	}

	// Comparators

	public int compareTo(Node node) {
		return this.getId().compareTo(node.getId());
	}

	// Methods community related

	public boolean belongsTo(String community) {
		boolean rtn = false;
		for (Object rA : relativeAttributes.values()) {
			ElementAttributeMap tmp = (ElementAttributeMap) rA;
			if (tmp.getCommunityName().equals(community)) {
				rtn = true;
				break;
			} else
				rtn = false;
		}
		return rtn;
	}

	public String getCommunityNames() {
		String communities = "";
		int cont = 0;
		for (Object rA : relativeAttributes.values()) {
			ElementAttributeMap tmp = (ElementAttributeMap) rA;
			communities = communities + tmp.getCommunityName();
			if (cont < relativeAttributes.size() - 1) {
				communities = communities + ",";
			}
			cont++;
		}
		return communities;
	}

	// *** equals
	public boolean equals(Object obj) {
		Node n = (Node) obj;
		boolean rtn = n.getId().equals(this.getId());
		return rtn;
	}

	public int hashCode() {
		return this.getId().hashCode();
	}

	// *** Getters and setters

	public String getName() {
		return (String) absoluteAttributes.get("label");
	}

	public float getSize() {
		return (Float) absoluteAttributes.get("size");
	}

	public String getCommunity(int key) {
		ElementAttributeMap tmp = (ElementAttributeMap) relativeAttributes.get(key);
		return tmp.getCommunityName();
	}

	public int getMetadataSize() {
		return relativeAttributes.size();
	}

	public Set<Integer> getMetadataKeys() {
		return relativeAttributes.keySet();
	}

	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

	// Setters
	public void setId(String id) {
		absoluteAttributes.put("id", id);
	}

	public void setName(String object) {
		absoluteAttributes.put("label", object);
	}

	public void setSize(float size) {
		absoluteAttributes.put("size", size);
	}

	/**
	 * Sets the attribute to this graph element. It is stored in the
	 * absoluteAttributes HashMap
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 */
	public void setAbsoluteAttribute(String key, Object value) {
		absoluteAttributes.put(key, value);
		netInt.utilities.mapping.Mapper.getInstance().setMaxMinNodeAttributes(key, value);
	}

	/**
	 * Sets an object containing attributes to this graph element. It is stored
	 * in the relativeAttributes HashMap
	 * 
	 * @param tier
	 *            the tier to which the set of attributes belongs
	 * @param attributeSet
	 *            An object containing a set of attributes
	 */
	public void addRelativeAttributes(int tier, HashMap<String, Float> attributeSet) {
		relativeAttributes.put(tier, attributeSet);
		// Update Mapper.minmax
		for (String key : attributeSet.keySet()) {
			netInt.utilities.mapping.Mapper.getInstance().setMaxMinNodeAttributes(key, attributeSet.get(key));
		}

	}

	// *****Get & set metrics
	// Getters metric
	/**
	 * @param key
	 *            the tier from which the procedure is invoked
	 * @return in degree of the node
	 */
	public int getInDegree(int key) {
		ElementAttributeMap tmp = (ElementAttributeMap) relativeAttributes.get(key);
		return tmp.getCommunityInDegree();
	}

	/**
	 * @param key
	 *            the tier from which the procedure is invoked
	 * @return out degree of the node
	 */
	public int getOutDegree(int key) {
		ElementAttributeMap tmp = (ElementAttributeMap) relativeAttributes.get(key);
		return tmp.getCommunityOutDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return degree of the node
	 */
	public int getDegree(int key) {
		ElementAttributeMap tmp = (ElementAttributeMap) relativeAttributes.get(key);
		return tmp.getCommunityDegree();
	}

	// Setters metrics
	public void setInDegree(int key, int inDegree) {
		if (key == 0)
			this.absoluteAttributes.put("inDegree", inDegree);
		ElementAttributeMap tmp = (ElementAttributeMap) this.relativeAttributes.get(key);
		tmp.setCommunityInDegree(inDegree);
		Mapper.getInstance().setMaxMinNodeAttributes("inDegree", inDegree);
	}

	public void setOutDegree(int key, int outDegree) {
		if (key == 0)
			this.absoluteAttributes.put("outDegree", outDegree);
		ElementAttributeMap tmp = (ElementAttributeMap) this.relativeAttributes.get(key);
		tmp.setCommunityOutDegree(outDegree);
		Mapper.getInstance().setMaxMinNodeAttributes("outDegree", outDegree);
	}

	public void setDegree(int key, int degree) {
		if (key == 0)
			this.absoluteAttributes.put("degree", degree);
		ElementAttributeMap tmp = (ElementAttributeMap) this.relativeAttributes.get(key);
		tmp.setCommunityDegree(degree);
		Mapper.getInstance().setMaxMinNodeAttributes("degree", degree);
	}

}
