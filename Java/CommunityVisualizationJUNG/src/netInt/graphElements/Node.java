/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * This class has two predefined attributes stored in the hashMap that use these
 * keys: id (String) and (label (String). Any other attribute is stored with the
 * key retrieved from the graph source file, usually a graphml. *** IMPORTANT***
 * the attribute "id" defines the node identity therefore it must be a unique
 * identifier
 * 
 * @author juan salamanca Nov 2016
 */
public class Node extends GraphElement implements Serializable {

	private static final long serialVersionUID = 1L;
	// True if the node searcher query matches any of this node's attributes
	private boolean isFound = false;

	public Node() {
		super();
	}

	public Node(String id) {
		super();
		AttributesMap metaData = new AttributesMap();
		// Initialize basic attributes
		absoluteAttributes.put("id", id);
		absoluteAttributes.put("label", "no name");
		relativeAttributes.put(0, metaData);
	}

	public int compareTo(Node node) {
		return this.getId().compareTo(node.getId());
	}

	// Methods community related

	public boolean belongsTo(String community) {
		boolean rtn = false;
		for (Object rA : relativeAttributes.values()) {
			AttributesMap tmp = (AttributesMap) rA;
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
			AttributesMap tmp = (AttributesMap) rA;
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
		AttributesMap tmp = (AttributesMap) relativeAttributes.get(key);
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
	public void setAttribute(String key, Object value) {
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
	 *            the community from which the procedure is invoked
	 * @return in degree of the node
	 */
	public int getInDegree(int key) {
		AttributesMap tmp = (AttributesMap) relativeAttributes.get(key);
		return tmp.getCommunityInDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return out degree of the node
	 */
	public int getOutDegree(int key) {
		AttributesMap tmp = (AttributesMap) relativeAttributes.get(key);
		return tmp.getCommunityOutDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return degree of the node
	 */
	public int getDegree(int key) {
		AttributesMap tmp = (AttributesMap) relativeAttributes.get(key);
		return tmp.getCommunityDegree();
	}

	// Setters metrics
	public void setInDegree(int key, int inDegree) {
		if (key == 0)
			this.absoluteAttributes.put("inDegree", inDegree);
		AttributesMap tmp = (AttributesMap) this.relativeAttributes.get(key);
		tmp.setCommunityInDegree(inDegree);
	}

	public void setOutDegree(int key, int outDegree) {
		if (key == 0)
			this.absoluteAttributes.put("outDegree", outDegree);
		AttributesMap tmp = (AttributesMap) this.relativeAttributes.get(key);
		tmp.setCommunityOutDegree(outDegree);
	}

	public void setDegree(int key, int degree) {
		if (key == 0)
			this.absoluteAttributes.put("degree", degree);
		AttributesMap tmp = (AttributesMap) this.relativeAttributes.get(key);
		tmp.setCommunityDegree(degree);
	}

}
