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

import processing.core.PApplet;

/**
 * This class is regularly used to store the set of Degree attributes of a
 * node in a tier, however it can store any <String, Float> using put()
 * 
 * @author jsalam
 *
 */
public class ElementAttributeMap extends HashMap<String, Float> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The name of the community to which this set of attributes is associated
	// with
	private String communityName;
	// Degrees
	// private int inDegree, outDegree, degree;

	public ElementAttributeMap() {
		super();
	}

	// *** Setters && PUT

	public void setCommunityDegree(int degree) {
		put("Degree", (float) degree);
		// this.degree = degree;
	}

	public void setCommunityOutDegree(int outDegree) {
		put("OutDegree", (float) outDegree);
		// this.outDegree = outDegree;
	}

	public void setCommunityName(String community) {
		this.communityName = community;
	}

	public void setCommunityInDegree(int inDegree) {
		put("InDegree", (float) inDegree);
		// this.inDegree = inDegree;
	}

	// *** Getters
	public int getCommunityInDegree() {
		int rtn = PApplet.floor(get("InDegree"));
		return rtn;
	}

	public String getCommunityName() {
		return communityName;
	}

	public int getCommunityOutDegree() {
		int rtn = PApplet.floor(get("OutDegree"));
		return rtn;
	}

	public int getCommunityDegree() {
		int rtn = PApplet.floor(get("Degree"));
		return rtn;
	}

}
