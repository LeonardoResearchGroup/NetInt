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
package netInt.visualElements.gui;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import controlP5.Bang;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import netInt.GraphPad;
import netInt.containers.Container;
import netInt.utilities.GraphLoader;
import netInt.utilities.mapping.Mapper;
import processing.core.PApplet;

/**
 * The menu displayed to assign attributes from the graph file to the
 * visualization elements
 * 
 * @author jsalam
 *
 */
public class ImportMenu implements ControlListener {
	private ControlP5 importMenu;
	private DropDownList nodeList, edgeList, layoutList;
	public PApplet parent;
	private boolean enableExtrasAtControlPanel;

	public ImportMenu(PApplet app, boolean extras) {
		this.parent = app;
		enableExtrasAtControlPanel = extras;
	}

	public void init() {
		importMenu = new ControlP5(parent);

		// for nodes
		nodeList = new DropDownList(parent, "Node Attributes");
		nodeList.setPos(100, 100);
		String[] nodeAttributeNames = { "Community", "Label" };
		// String[] nodeAttributeNames = { "Community", "Label", "Size", "Color"
		// };
		nodeList.setAttributes(nodeAttributeNames);

		// for edges
		edgeList = new DropDownList(parent, "Edge Attributes");
		edgeList.setPos(100, 250);
		// String[] edgeAttributeNames = { "Body thickness", "Target thickness",
		// "Body color", "Target Color" };
		String[] edgeAttributeNames = { "Body thickness" };
		edgeList.setAttributes(edgeAttributeNames);

		// for layout
		layoutList = new DropDownList(parent, "Visualization Layout");
		layoutList.setPos(100, 400);
		String[] layoutAttributeNames = { "Choose one" };
		layoutList.setAttributes(layoutAttributeNames);
	}

	/**
	 * Creates two ContorlP5.Group, one for nodes and one for edges. Both groups
	 * are attached to an instance of ControlP5 that draws them in the PApplet
	 * using an internal ControlP5.autoDraw() method. It initializes the
	 * ControlP5 variables every time it is invoked. Albeit is is done in the
	 * constructor, it is necessary to do it here in order to have fresh
	 * variables every time the user loads new files without restarting the
	 * application
	 * 
	 * @param nodeAttributeKeys
	 * @param edgeAttributeKeys
	 */
	public void makeLists(ArrayList<String> nodeAttributeKeys, ArrayList<String> edgeAttributeKeys,
			ArrayList<String> layoutAttributeKeys) {
		// Initialize variables
		init();
		nodeList.initializeList(nodeAttributeKeys);
		edgeList.initializeList(edgeAttributeKeys);
		layoutList.initializeList(layoutAttributeKeys);
		importMenu.addBang("loadGraph").setPosition(100, 500).setSize(100, 20).setTriggerEvent(Bang.RELEASE)
				.setLabel("Load graph");
		importMenu.getController("loadGraph").addListener(this);

		// make them visible
		importMenu.setVisible(true);
		nodeList.dropMenu.setVisible(true);
		edgeList.dropMenu.setVisible(true);
		layoutList.dropMenu.setVisible(true);
	}

	public void controlEvent(ControlEvent theEvent) {
		choiceCatcher(theEvent);
	}

	/**
	 * This method initiates the importing process based on the user defined
	 * parameters from the import menu.
	 * 
	 * NOTE: It is set to import graphml files only. Jan 15 2017
	 * 
	 * @param theEvent
	 */
	private void choiceCatcher(ControlEvent theEvent) {
		String controllerName = theEvent.getController().getName();
		System.out.println("ImportMenu " + controllerName);
		if (controllerName.equals("loadGraph")) {
			ArrayList<String> temp = new ArrayList<String>(Arrays.asList(nodeList.attributes));
			UserSettings.getInstance().setDescriptiveStatisticKeys(temp);
			System.out.println("Node import selection:");
			for (int i = 0; i < nodeList.getSelection().length; i++) {
				System.out.println("..." + nodeList.attributes[i] + ": " + nodeList.getSelection()[i] + ", ");
			}
			System.out.println("Edge import selection:");
			for (int i = 0; i < edgeList.getSelection().length; i++) {
				System.out.println("..." + edgeList.attributes[i] + ": " + edgeList.getSelection()[i] + ", ");
			}
			System.out.println("_ _ _");
			// If the user selected at least the first two attributes from the
			// menu
			if (nodeList.getSelection().length >= 2) {
				GraphPad.setActiveGraph(true);
				// If the user does not select a layout, Fruchterman_Reingold is
				// assigned by default
				int layoutSelection;
				try {
					layoutSelection = layoutSelection(layoutList.getSelection()[0]);
				} catch (ArrayIndexOutOfBoundsException e) {
					layoutSelection = Container.FRUCHTERMAN_REINGOLD;
				}
				// Ask the assembler to load the graph
				if (nodeList.getSelection()[0] != null && nodeList.getSelection()[1] != null) {
					//GraphPad.app.loadGraph(GraphPad.getFile(), nodeList.getSelection(), edgeList.getSelection(),
							//layoutSelection, GraphLoader.GRAPHML);
					GraphPad.setActiveGraph(true);
				} else {
					JOptionPane.showMessageDialog(parent.frame, "Missing either \"community\" or \"label\" attributes",
							"Import warning", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(parent.frame,
						"Must select at least \"community\" and \"label\" attributes", "Import warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		// Populate Control panel with attributes retrieved from the graph if
		// the control panel exists
		if (ControlPanel.getInstance() != null) {
			ArrayList<String> nodeAttributesKeys = Mapper.getInstance().getNodeAttributesMax().getAttributeKeys();
			ArrayList<String> edgeAttributeKeys = Mapper.getInstance().getEdgeAttributesMax().getAttributeKeys();
			ControlPanel.getInstance().initGroups(nodeAttributesKeys, edgeAttributeKeys);
			if (enableExtrasAtControlPanel)
				ControlPanel.getInstance().enableStatistics();
		}

		// Hide Import Menu from main panel
		importMenu.setVisible(false);
		nodeList.dropMenu.setVisible(false);
		edgeList.dropMenu.setVisible(false);
		layoutList.dropMenu.setVisible(false);
	}

	private int layoutSelection(String value) {
		// Default selection fruchterman_reingold
		int selection = Container.FRUCHTERMAN_REINGOLD;
		switch (value) {
		case "Spring":
			selection = Container.SPRING;
			break;
		case "Circular":
			selection = Container.CIRCULAR;
			break;
		}
		return selection;
	}
}
