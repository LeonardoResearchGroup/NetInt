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
import java.util.ArrayList;

import netInt.GraphPad;
import netInt.containers.Container;
import netInt.utilities.GraphLoader;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.gui.ControlPanel;
import processing.core.PApplet;

/**
 * 
 * @author juan salamanca
 *
 */
public class ControlPanel_Example extends PApplet {

	// The visualization pad
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
		background(70);
		pad.show();
	}

	/**
	 * Receives the file with the path pointing to the graph file and triggers
	 * an import process. The import process follows the parameters chosen by
	 * the user from the import menu.
	 * 
	 * The method is invoked by showFileChooser() at the ChooseHelper class
	 * 
	 * @param selection
	 *            The file to be imported
	 */
	public void selectImport(File selection) {
		if (selection != null) {

			// The first one defines the communities, the second the node names
			String[] nodeAtts = { "Continent", "label" };

			// In Graphml file format. Edge attributes copied from the graphml
			// file.
			// The first one defines edge thickness
			String[] edgeAtts = { "weight" };

			// The node distribution layout
			int layout = Container.FRUCHTERMAN_REINGOLD;

			// Graphml. Pajek not ready yet
			int graphFormat = GraphLoader.GRAPHML;

			// Load the graph
			if (GraphPad.app.loadGraph(selection, nodeAtts, edgeAtts, layout, graphFormat)) {
				// Activate graph in the visualization pad
				GraphPad.setActiveGraph(true);
			}

			// Set control panel variables
			if (ControlPanel.getInstance() != null) {
				ArrayList<String> nodeAttributesKeys = Mapper.getInstance().getNodeAttributesMax().getAttributeKeys();
				ArrayList<String> edgeAttributeKeys = Mapper.getInstance().getEdgeAttributesMax().getAttributeKeys();
				ControlPanel.getInstance().initGroups(nodeAttributesKeys, edgeAttributeKeys);

			}
		}
	}

	public static void main(String[] args) {
		PApplet.main("examples.ControlPanel_Example");
	}
}