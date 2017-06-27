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

public class RelativeAttributes extends HashMap<String, Float> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The name of the community to which this set of attributes is associated with
	private String communityName;
	// Degrees
	private int inDegree, outDegree, degree;
	
	public RelativeAttributes(){
		super();
	}
	
	public void put(String key, float value){
		this.put(key, value);
	}
	
	public int getCommunityInDegree() {
		return inDegree;
	}

	public void setCommunityInDegree(int inDegree) {
		this.inDegree = inDegree;
	}

	public int getCommunityOutDegree() {
		return outDegree;
	}

	public void setCommunityOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}

	public int getCommunityDegree() {
		return degree;
	}

	public void setCommunityDegree(int degree) {
		this.degree = degree;
	}


	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String community) {
		this.communityName = community;
	}

}
