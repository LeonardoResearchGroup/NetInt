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
package netInt.utilities.mapping;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import netInt.graphElements.GraphElement;

/**
 * This collection stores pairs of attribute names and associated float values.
 * The storage structure is a TreeMap with instances of String as keys, and of
 * Float as values.
 * 
 * @author jsalam
 *
 */
public class NumericalCollection {
	private TreeMap<String, Float> attributes;
	public static final String NODE = "Node";
	public static final String EDGE = "Edge";

	public NumericalCollection() {
		attributes = new TreeMap<String, Float>();
	}

	public void initialize(GraphElement graphElement) {
		// Go over all the attributes of this element
		for (int i = 0; i < graphElement.getAttributeKeys().length; i++) {
			// For each attribute key get its value
			String key = (String) graphElement.getAttributeKeys()[i];
			// set the attribute as Float in the attribute collections
			Object value = graphElement.getAttribute(key);
			if (isNumerical(value)) {
				Float valueFloat = graphElement.getFloatAttribute(key);
				if (valueFloat != Float.NEGATIVE_INFINITY) {
					attributes.put(key, graphElement.getFloatAttribute(key));
				}
			}
		}
	}

	public static boolean isNumerical(Object value) {
		if (value instanceof Double) {
			return true;
		} else if (value instanceof Integer) {
			return true;
		} else if (value instanceof Float) {
			return true;
		}
		return false;
	}

	/**
	 * Add a value to the HashMap if the key does not exists or if its current
	 * associated value is higher to the passed parameter
	 * 
	 * @param key
	 * @param value
	 * @return true if the value is added
	 */
	public boolean addLowerValue(String key, Float value) {
		boolean rtn = false;
		if (attributes.containsKey(key)) {
			if (attributes.get(key) > value && isNumerical(value)) {
				attributes.put(key, value);
				rtn = true;
			}
		} else {
			attributes.put(key, value);
			rtn = true;
		}

		return rtn;
	}

	/**
	 * Add a value to the HashMap if the key does not exists or if its current
	 * associated value is lower to the passed parameter
	 * 
	 * @param key
	 * @param value
	 * @return true if the value is added
	 */

	public boolean addHigherValue(String key, Float value) {
		boolean rtn = false;
		if (attributes.containsKey(key)) {
			if (attributes.get(key) < value && isNumerical(value)) {
				attributes.put(key, value);
				rtn = true;
			}
		} else {
			attributes.put(key, value);
			rtn = true;
		}
		return rtn;
	}

	public Float getValueofAttribute(String key) {
		return attributes.get(key);
	}

	/**
	 * Get the list of graph element attributes stores in this
	 * NumericalCollection
	 * 
	 * @return
	 */
	public ArrayList<String> getAttributeKeys() {
		ArrayList<String> classElementAttributes = new ArrayList<String>();
		for (String s: attributes.keySet()) {
			classElementAttributes.add(s);
		}
		return classElementAttributes;
	}

	public boolean collectionInitialized() {
		return attributes.size() > 0;
	}

	public int getSize() {
		return attributes.size();
	}

	public void printAttributes() {
		System.out.println(this.getClass().getName() + " printAttributes():");
		if (attributes != null) {
			Set<String> s = attributes.keySet();
			for (String keyName : s) {
				System.out.println("   Key: " + keyName + ", Value: " + attributes.get(keyName));
			}
		} else {
			System.out.println("Collection of Numerical Attributes not initialized");
		}
	}
}
