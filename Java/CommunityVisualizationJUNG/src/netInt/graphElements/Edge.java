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

/**
 * @author jsalam
 *
 */
public class Edge extends GraphElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node source;
	private Node target;
	//private HashMap<String, Object> attributes;
	private boolean directed;
	private boolean loop;

	public Edge(Node source, Node target, boolean directed) {
		super();
		this.source = source;
		this.target = target;
		this.directed = directed;
		if (source.equals(target))
			loop = true;
	}

	public Edge() {
		super();

	}

	// **** Getters and Setters

	public Node getSource() {
		return source;
	}

	public Node getTarget() {
		return target;
	}

	public boolean isDirected() {
		return directed;
	}

	// *** Getters

	public Object getAttribute(String key) {
		return absoluteAttributes.get(key);
	}

	public String getAttribute(String key, String rtn) {
		try {
			rtn = (String) absoluteAttributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public float getAttribute(String key, Float rtn) {
		try {
			rtn = (Float) absoluteAttributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public int getAttribute(String key, Integer rtn) {
		try {
			rtn = (Integer) absoluteAttributes.get(key);
		} catch (Exception e) {
//			System.out.println("Node Attribute couldn't be casted att");
		}
		return rtn;
	}

	public String getName() {
		return (String) absoluteAttributes.get("label");
	}
	
	public boolean isLoop() {
		return loop;
	}

	// *** Setters
	public void setSource(Node source) {
		this.source = source;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
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
		netInt.utilities.mapping.Mapper.getInstance().setMaxMinEdgeAttributes(key, value);
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
	public void addRelativeAttributes(int tier, HashMap<String,Float> attributeSet) {
		relativeAttributes.put(tier, attributeSet);
	//	netInt.utilities.mapping.Mapper.getInstance().setMaxMinEdgeAttributes(key, value);
	}



	public boolean equals(Object obj) {
		Edge edge = (Edge) obj;
		boolean sourceIsEqual = edge.getSource().equals(this.getSource());
		boolean targetIsEqual = edge.getTarget().equals(this.getTarget());
		return sourceIsEqual && targetIsEqual;
	}

}
