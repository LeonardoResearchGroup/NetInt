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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import netInt.utilities.entities.JsonModuleFile;

/**
 * 
 * This class allows to read and load in memory json files. This class implements singleton pattern.
 * @author lfrivera
 *
 */
public class JsonReader {

	/**
	 * Unique instance of the class.
	 */
	private static JsonReader instance;
	
	/**
	 * Private constructor of the class.
	 */
	private JsonReader ()
	{}
	
	/**
	 * Allows to obtain the unique instance of the class.
	 * @return Unique instance of the class.
	 */
	public static JsonReader getInstance()
	{
		if(instance == null)
		{
			instance = new JsonReader();
		}
		
		return instance;
	}
	
	/**
	 * This method allows to read a json file from a module. 
	 * Based on https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
	 * 
	 * @param jsonFile - The stream of the json file.
	 * @return Wrapper entity.
	 * @throws FileNotFoundException Throwable exception.
	 * @throws JsonIOException Throwable exception.
	 * @throws JsonSyntaxException Throwable exception.
	 */
	public JsonModuleFile readJson(InputStream jsonFile) throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		Gson gson = new Gson();
		// 1. JSON to Java object, read it from a file.
		//Based on: https://stackoverflow.com/questions/5200187/convert-inputstream-to-bufferedreader
		Reader reader = new InputStreamReader(jsonFile);	
		JsonModuleFile file = gson.fromJson(reader, JsonModuleFile.class);

		return file;
	}
	
}
