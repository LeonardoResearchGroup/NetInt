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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.jar.JarFile;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import netInt.utilities.entities.JarFilter;
import netInt.utilities.entities.JsonModuleFile;
import netInt.utilities.entities.exceptions.ModuleLoadingException;
import netInt.utilities.entities.exceptions.NotFolderModuleException;


/**
 * This class allows to load modules in the main application. Implements the
 * Singleton pattern.
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
	private ModuleLoader() {
	}

	/**
	 * Allows to obtain the unique instance of the class.
	 * 
	 * @return Unique instance of the class.
	 */
	public static ModuleLoader getInstance() {
		if (instance == null) {
			instance = new ModuleLoader();
		}

		return instance;
	}

	/**
	 * Allows to load modules in the main application.
	 * 
	 * @param folderPath
	 *            The path of the folder that contains all modules to be loaded.
	 * @return boolean value that indicates whether all modules were loaded.
	 * @throws ModuleLoadingException
	 *             A throwable exception that can occur in module loading.
	 * @throws URISyntaxException
	 *             Throwable exception.
	 * @throws JsonIOException
	 *             Throwable exception.
	 * @throws JsonSyntaxException
	 *             Throwable exception.
	 * @throws IOException
	 *             Throwable exception.
	 * @throws ClassNotFoundException
	 *             Throwable exception.
	 * 
	 */
	public boolean loadModules(String folderPath) throws ModuleLoadingException, JsonSyntaxException, JsonIOException,
			URISyntaxException, ClassNotFoundException, IOException {
		boolean correct = true;

		File folder = new File(folderPath);

		if (!folder.isDirectory()) {
			throw new NotFolderModuleException();
		} else {
			File[] modules = folder.listFiles(new JarFilter());

			loadExternalClasses(modules);

			loadModuleGraphicalElements(modules);

		}

		return correct;
	}

	/**
	 * Allows to read a json within a jar file. Based on:
	 * https://stackoverflow.com/questions/2271926/how-to-read-a-file-from-a-jar-file
	 * 
	 * @param json
	 *            Json file.
	 * @return Wrapper class that contins information about the jar.
	 * @throws URISyntaxException
	 *             Throwable exception.
	 * @throws JsonIOException
	 *             Throwable exception.
	 * @throws JsonSyntaxException
	 *             Throwable exception.
	 * @throws IOException
	 *             Throwable exception.
	 */
	private JsonModuleFile readJson(File json)
			throws JsonSyntaxException, JsonIOException, URISyntaxException, IOException {
		JsonModuleFile element = null;

		JarFile jar = new JarFile(json);

		element = JsonReader.getInstance().readJson(jar.getInputStream(jar.getEntry("GraphicalComponents.json")));

		jar.close();

		return element;
	}

	/**
	 * Allows to allocate external classes into a class repository.
	 * 
	 * @param modules
	 *            Modules loaded into main app.
	 * @throws ClassNotFoundException
	 *             Throwable exception.
	 * @throws IOException
	 *             Throwable exception.
	 */
	private void loadExternalClasses(File[] modules) throws ClassNotFoundException, IOException {

		for (File f : modules) {
			@SuppressWarnings("rawtypes")
			ArrayList<Class> moduleClasses = ClassLoader.getInstance().loadClasses(f.getAbsolutePath());

			// Adding the classes to the repository.
			ExternalClasses.getInstance().getClasses().addAll(moduleClasses);
		}

	}

	/**
	 * Allows to load the graphical elements of the module.
	 * 
	 * @param modules Modules to be inspected.
	 * @throws JsonSyntaxException Throwable exception.
	 * @throws JsonIOException Throwable exception.
	 * @throws URISyntaxException Throwable exception.
	 * @throws IOException Throwable exception.
	 */
	private void loadModuleGraphicalElements(File[] modules)
			throws JsonSyntaxException, JsonIOException, URISyntaxException, IOException {
		
		ArrayList<JsonModuleFile> loadedModules = new ArrayList<JsonModuleFile>();
		
		for (File f : modules) {
			// Reading the graphical elements of the module.
			JsonModuleFile moduleFile = readJson(f);

			loadedModules.add(moduleFile);

		}
		
		ActiveModules.getInstance().setModules(loadedModules);

	}

}
