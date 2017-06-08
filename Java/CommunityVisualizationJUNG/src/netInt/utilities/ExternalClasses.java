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

/**
 * This class contains all external classes that were loaded. This class implements the singleton pattern.
 * @author lfrivera
 *
 */
public class ExternalClasses {

	/**
	 * Unique instance of the class.
	 */
	private static ExternalClasses instance;
	
	@SuppressWarnings("rawtypes")
	private ArrayList<Class> classes;

	/**
	 * Constructor of the class.
	 */
	@SuppressWarnings("rawtypes")
	private ExternalClasses(){
		classes = new ArrayList<Class>();
	}
	
	/**
	 * Allows to obtain the unique instance of the class.
	 * @return Unique instance of the class.
	 */
	public static ExternalClasses getInstance()
	{
		if(instance == null)
		{
			instance = new ExternalClasses();
		}
		
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Class> getClasses()
	{
		return classes;
	}
	
}
