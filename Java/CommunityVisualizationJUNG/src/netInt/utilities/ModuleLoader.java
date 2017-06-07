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

import java.io.File;

import netInt.utilities.entities.JarFilter;
import netInt.utilities.entities.exceptions.ModuleLoadingException;
import netInt.utilities.entities.exceptions.NotFolderModuleException;

/**
 * This class allows to load modules in the main application. Implements the Singleton pattern.
 * 
 * @author lfrivera
 *
 */
public class ModuleLoader {

	/**
	 * Unique instance of the class.
	 */
	private static ModuleLoader instance;
	
	/**
	 * Private constructor of the class.
	 */
	private ModuleLoader()
	{}
	
	/**
	 * Allows to obtain the unique instance of the class.
	 * @return Unique instance of the class.
	 */
	public static ModuleLoader getInstance()
	{
		if(instance == null)
		{
			instance = new ModuleLoader();
		}
		
		return instance;
	}
	
	/**
	 * Allows to load modules in the main application.
	 * 
	 * @param folderPath The path of the folder that contains all modules to be loaded.
	 * @return boolean value that indicates whether all modules were loaded.
	 * @throws ModuleLoadingException A throwable exception that can occur in module loading.
	 * 
	 */
	public boolean loadModules(String folderPath) throws ModuleLoadingException
	{
		boolean correct = true;
		
		File folder = new File(folderPath);
		
		if(!folder.isDirectory())
		{
			throw new NotFolderModuleException();
		}
		else
		{
			File[] modules =  folder.listFiles(new JarFilter());
		}
		
		
		return correct;
	}
	
}
