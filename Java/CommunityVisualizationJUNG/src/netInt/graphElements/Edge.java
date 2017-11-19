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
import java.util.HashMap;

import netInt.utilities.mapping.Mapper;

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
	// private HashMap<String, Object> attributes;
	private boolean directed;
	private boolean loop;

	public Edge(Node source, Node target, boolean directed) {
		super();
		this.source = source;
		this.target = target;
		setAbsoluteAttribute("id", source.getId() + "&" + target.getId());
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


	public String getAttribute(String key, String rtn) {
		try {
			rtn = (String) getAttribute(key);
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " Edge Attribute " + key + " couldn't be casted");
		}
		return rtn;
	}

	public float getAttribute(String key, Float rtn) {
		try {
			rtn = (Float) getAttribute(key);
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " Edge Attribute " + key + " couldn't be casted");
		}
		return rtn;
	}

	public int getAttribute(String key, Integer rtn) {
		try {
			rtn = (Integer) getAttribute(key);
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " Edge Attribute " + key + " couldn't be casted");
		}
		return rtn;
	}

	public String getName() {
		return (String) getAttribute("label");
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
	public void setAbsoluteAttribute(String key, Object value) {
		addAbsoluteAtt(key, value);
		Mapper.getInstance().setMaxMinEdgeAttributes(key, value);
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
	public void setRelativeAttributes(int tier, HashMap<String, Float> attributeSet) {
		addRelativeAtt(tier, attributeSet);
		// Update Mapper.minmax
		for (String key : attributeSet.keySet()) {
			Mapper.getInstance().setMaxMinEdgeAttributes(key, attributeSet.get(key));
		}
	}

	public boolean equals(Object obj) {
		Edge edge = (Edge) obj;
		return getId().equals(edge);
	}
}
