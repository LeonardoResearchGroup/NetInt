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

import java.io.File;

import netInt.GraphPad;
import netInt.visualElements.gui.ControlPanel;
import netInt.visualElements.gui.UserSettings;
import processing.core.PApplet;

/**
 * 
 * @author jsalam
 * @version alpha
 * @date June 2017
 *
 */
public class NetInt_Banca_Example extends PApplet {
	GraphPad pad;

	/**
	 * Required method from parent class. Define here the size of the
	 * visualization pad
	 * 
	 * @see processing.core.PApplet#settings()
	 */
	public void settings() {
		size(displayWidth - 201, displayHeight - 100, P2D);
	}

	/**
	 * Required method from parent class. It runs only once at the PApplet
	 * initialization. Instantiate the classes and initialize attributes
	 * declared in this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {
		pad = new GraphPad(this);

		// Initiate the Control Panel
		new ControlPanel(this, 200, this.height - 25);
	}

	/**
	 * Required method from parent class. It draws visualElements and other
	 * PApplet elements on the visualization pad. It constantly iterates over
	 * its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		background(UserSettings.getInstance().getColorBackground());
		pad.show();
	}

	/**
	 * Required method to launch graph import menu
	 * 
	 * @param selection
	 *            the file chosen by the user at the Control Panel
	 */
	public void selectImport(File selection) {

		// The second parameter is the array of complementary module names. The
		// modules must be imported in advance from the Control Panel.
		pad.selectImport(selection, new String[] { "NetInt_Banca" });
	}

	public static void main(String[] args) {
		PApplet.main("examples.NetInt_Banca_Example");
	}
}