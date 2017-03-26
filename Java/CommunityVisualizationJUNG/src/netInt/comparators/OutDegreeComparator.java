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
package netInt.comparators;

import java.util.Comparator;

import netInt.graphElements.Node;

public class OutDegreeComparator implements Comparator <Node> {
	
	int key;
	
	public void setKey(int key){
		this.key = key;
	}
		public int compare(Node a, Node b) {
			return a.getOutDegree(key) - b.getOutDegree(key);
		}
}
