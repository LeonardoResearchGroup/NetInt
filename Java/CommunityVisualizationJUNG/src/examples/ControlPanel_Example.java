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

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import netInt.GraphPad;
import netInt.utilities.ModuleLoader;
import netInt.utilities.entities.exceptions.ModuleLoadingException;

/**
 * This example makes use of the GraphPad constructor that receives a boolean
 * parameter that enables or disables the control panel.
 * 
 * @author jsalam
 *
 */
public class ControlPanel_Example {

	public ControlPanel_Example() {
		// Initialize GraphPad with no parameters
		new GraphPad(true);
	}

	public static void main(String[] args) {
		
	try {
		ModuleLoader.getInstance().loadModules("C:\\Users\\DiegoFernando\\Desktop\\Nueva_carpeta");
	} catch (JsonSyntaxException | JsonIOException | ClassNotFoundException | ModuleLoadingException
			| URISyntaxException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		//new ControlPanel_Example();

	}
}