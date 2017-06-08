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
package examples;

import netInt.GraphPad;
import java.io.FileNotFoundException;

/**
 * Example for Graphml file formats
 * 
 * @author jsalam
 *
 */
public class GraphPad_Example {

	public GraphPad_Example() {
		try {
			String filePath = "./data/graphs/samples/Risk.graphml";
			new GraphPad(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GraphPad_Example();
	}
}
