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

import java.io.File;
import java.io.FileFilter;

/**
 * This class represents a filter for jar selection.
 * 
 * @author lfrivera
 *
 */
public class JarFilter implements FileFilter {

	private final String[] allowedFileExtensions = new String[] { "jar" };

	public boolean accept(File file) {
		
		for (String extension : allowedFileExtensions) {
			if (file.getName().toLowerCase().endsWith(extension)) {
				return true;
			}
		}
		
		return false;
	}

}