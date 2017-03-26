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
package netInt.containers;

import java.util.ArrayList;

import netInt.visualElements.primitives.VisualAtom;

public abstract class Arrangement {

	public Arrangement() {

	}
	
	protected void clearLayout(ArrayList<VisualAtom> visualElements){
		for(VisualAtom e: visualElements){
			e.setX(0);
			e.setY(0);
		}
	}

}
