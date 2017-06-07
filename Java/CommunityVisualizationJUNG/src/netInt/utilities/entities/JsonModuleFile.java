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
package netInt.utilities.entities;

import java.util.ArrayList;

/**
 * The entity that is mapped from json file.
 * @author lfrivera
 *
 */
public class JsonModuleFile {

	/**
	 * The list of graphical elements in the new module.
	 */
	private ArrayList<ModuleGraphicalElement> graphicalElements;
	
	/**
	 * Constructor of the class.
	 */
	public JsonModuleFile()
	{}
		
	/**
	 * Allows to get the graphical elements.
	 * @return Graphical elements.
	 */
	public ArrayList<ModuleGraphicalElement> getGraphicalElements() {
		return graphicalElements;
	}

	/**
	 * Allows to set the graphical elements-
	 * @param graphicalElements The graphical elements of the module.
	 */
	public void setGraphicalElements(ArrayList<ModuleGraphicalElement> graphicalElements) {
		this.graphicalElements = graphicalElements;
	}
}
