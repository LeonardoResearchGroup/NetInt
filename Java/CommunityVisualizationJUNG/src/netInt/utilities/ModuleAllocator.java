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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * This class allows to allocate modules in a specific folder. This class implements the Singleton pattern.
 * @author lfrivera
 *
 */
public class ModuleAllocator {

	//OS constants.
	private final String WIN_OS = "Windows";
	private final String LIN_OS = "Linux";
	private final String MAC_OS = "Mac";
	
	/**
	 * The current operative system.
	 */
	private String currentOS;
	
	/**
	 * The unique instance of the class.
	 */
	private static ModuleAllocator instance;
	
	/**
	 * Constructor of the class.
	 */
	private ModuleAllocator()
	{
		detectOS();
	}
	
	/**
	 * Allows to obtain the unique instance of the class.
	 * @return Unique instance of the class.
	 */
	public static ModuleAllocator getInstance()
	{
		if(instance == null)
		{
			instance = new ModuleAllocator();
		}
		return instance;
	}
	
	/**
	 * Allows to detect the current operative system of the user.
	 */
	private void detectOS()
	{
		//Based on: https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
		
		String os = System.getProperty("os.name").toLowerCase();
		
		if(os.indexOf("win") >= 0)
		{
			currentOS = WIN_OS;
		}
		
		if(os.indexOf("mac") >= 0)
		{
			currentOS = MAC_OS;
		}
		
		if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0)
		{
			currentOS = LIN_OS;
		}
	}
	
	/**
	 * Allows to obtain the absolute path of the module folder. If the folder does not exist, the method calls a mkdir operation.
	 * @return Module folder.
	 */
	public String getModuleFolder()
	{
		
		String folder = null;
		
		switch(currentOS)
		{
		
		case WIN_OS:
			
			
			
			break;
			
		case LIN_OS:
			
			String username = System.getProperty("user.name");

			folder = "/home/"+username+"/ProgramData/"+"NetInt/"+"Modules";
			
			
			break;
			
		case MAC_OS:
		

			
			break;
		
		}
		
		File folder_file = new File(folder);
		
		if(!folder_file.exists())
		{
			folder_file.mkdirs();
		}
		
		return folder;
	}
	
	/**
	 * Allows to add a module to module folder.
	 * @return Success of operation.
	 */
	public boolean addModule(File module)
	{
		boolean success = true;
		
		try {
			
			Path from = Paths.get(module.getAbsolutePath());
			
			Path to = Paths.get(getModuleFolder()+"/"+module.getName());
			
			Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
		
		} catch (IOException e) {
			
			success = false;
			
		}
		
		return success;
	}
	
	
}

