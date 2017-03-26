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
package netInt.utilities;

/**
 * Contains the attributes of a graphmlKey as Strings
 * 
 * @author jsalam
 *
 */
public class GraphmlKey {
	String name;
	String type;
	String element;
	String id;

	public GraphmlKey() {

	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getElement() {
		return element;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNodeKey() {
		if (element.equals("node")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEdgeKey() {
		if (element.equals("edge")) {
			return true;
		} else {
			return false;
		}
	}

	public void printKey() {
		System.out
				.println(" name: " + getName() + ", type: " + getType() + ", for:" + getElement() + ", id: " + getId());
	}
}
