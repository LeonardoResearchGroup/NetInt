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

import java.util.ArrayList;

import netInt.utilities.entities.JsonModuleFile;

/**
 * This class contains the active modules of the application.
 * @author lfrivera
 *
 */
public class ActiveModules {

	private ArrayList<JsonModuleFile> modules;
	
	/**
	 * Unique instance of the class.
	 */
	private static ActiveModules instance;

	/**
	 * Private constructor of the class.
	 */
	private ActiveModules() {
		modules = new ArrayList<JsonModuleFile>();
	}

	/**
	 * Allows to obtain the unique instance of the class.
	 * 
	 * @return Unique instance of the class.
	 */
	public static ActiveModules getInstance() {
		if (instance == null) {
			instance = new ActiveModules();
		}

		return instance;
	}

	public ArrayList<JsonModuleFile> getModules() {
		return modules;
	}

	public void setModules(ArrayList<JsonModuleFile> modules) {
		this.modules = modules;
	}
	
	

	
}
