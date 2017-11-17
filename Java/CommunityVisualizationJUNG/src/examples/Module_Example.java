/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package examples;

import java.io.File;

import netInt.GraphPad;
import netInt.gui.ControlPanel;
import netInt.gui.UserSettings;
import processing.core.PApplet;

/**
 * 
 * @author jsalam
 * @version alpha
 * @date June 2017
 *
 */
public class Module_Example extends PApplet {
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
		PApplet.main("examples.Module_Example");
	}
}
