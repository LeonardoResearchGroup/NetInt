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
import guiSet.GuiSet;
import guiSet.SelectableList;
import netInt.GraphPad;
import netInt.containers.Container;
import netInt.utilities.GraphLoader;
import netInt.utilities.mapping.Mapper;

/**
 * The menu displayed to assign attributes from the graph file to the
 * visualization elements
 * 
 * This class is replaced by ImportMenuGuiSet June 24 2017
 * 
 * @author jsalam
 * @deprecated
 *
 */
public class ImportMenuOLD implements ControlListener {
	private ControlP5 importMenu;
	private DropDownList nodeList;
	private DropDownList edgeList, layoutList;
	// SelectableList is a custom made GUI for this project thus it is not part
	// of ControlP5
	private GuiSet guiSet;
	private SelectableList communities;
	public GraphPad graphPad;

	public ImportMenuOLD(GraphPad app) {
		this.graphPad = app;
	}

	/**
	 * This show simple draws the SelectableList on the graphPad. It is not the
	 * best solution but it works.
	 */
	public void show() {
		if (guiSet != null)
			guiSet.show();
	}

	public void init() {
		importMenu = new ControlP5(graphPad);
		guiSet = new GuiSet(graphPad);

		// for nodes
		nodeList = new DropDownList(graphPad, "Node Attributes");
		// nodeList.setPos(100, 100);
		nodeList.setPos(400, 80);
		String[] nodeAttributeNames = { "Label" };
		// String[] nodeAttributeNames = { "Community", "Label" };
		// String[] nodeAttributeNames = { "Community", "Label", "Size", "Color"
		// };
		nodeList.setAttributes(nodeAttributeNames);

		// Selectable List
		communities = new SelectableList(100f, 80f, "Node Attributes", "Nesting order");
		communities.setItemSize(100, 12);
		guiSet.addGuiElement(communities);
		// By now set the capacity to 1 nested tier
		//communities.setMaxCapacityTargetList(1);

		// for edges
		edgeList = new DropDownList(graphPad, "Edge Attributes");
		edgeList.setPos(600, 80);
		// String[] edgeAttributeNames = { "Body thickness", "Target thickness",
		// "Body color", "Target Color" };
		String[] edgeAttributeNames = { "Body thickness" };
		edgeList.setAttributes(edgeAttributeNames);

		// for layout
		layoutList = new DropDownList(graphPad, "Visualization Layout");
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
	 *            The list of node attributes retrieved from the graphml header
	 * @param edgeAttributeKeys
	 *            The list of edge attributes retrieved from the graphml header
	 */
	public void makeLists(ArrayList<String> nodeAttributeKeys, ArrayList<String> edgeAttributeKeys,
			ArrayList<String> layoutAttributeKeys) {
		// Initialize variables
		init();
		importMenu.setVisible(true);
		nodeList.dropMenu.setVisible(true);
		edgeList.dropMenu.setVisible(true);
		layoutList.dropMenu.setVisible(true);

		// populate the nodeList
		nodeList.initializeList(nodeAttributeKeys);

		// populate the selectable list
		String[] nodeAttributes = nodeAttributeKeys.toArray(new String[nodeAttributeKeys.size()]);
		communities.setSourceItems(nodeAttributes);

		// Populate edge list
		edgeList.initializeList(edgeAttributeKeys);

		// Populate layout list
		layoutList.initializeList(layoutAttributeKeys);

		// Add load button
		importMenu.addBang("loadGraph").setPosition(100, 500).setSize(100, 20).setTriggerEvent(Bang.RELEASE)
				.setLabel("Load graph");
		importMenu.getController("loadGraph").addListener(this);
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

		// If the load graph button is pressed
		if (controllerName.equals("loadGraph")) {

			ArrayList<String> temp = new ArrayList<String>(Arrays.asList(nodeList.attributes));
			UserSettings.getInstance().setDescriptiveStatisticKeys(temp);

			// get the user selection for nodes
			System.out.println("Node import selection:");
			for (int i = 0; i < nodeList.getSelection().length; i++) {
				System.out.println("..." + nodeList.attributes[i] + ": " + nodeList.getSelection()[i] + ", ");
			}

			// get the user selection for edges
			System.out.println("Edge import selection:");
			for (int i = 0; i < edgeList.getSelection().length; i++) {
				System.out.println("..." + edgeList.attributes[i] + ": " + edgeList.getSelection()[i] + ", ");
			}
			System.out.println("_ _ _");

			// Verify if the user selected at least the first two node
			// attributes from the menu: one for community and one for label
			if (communities.getOrderedTargetList().length >= 1 && nodeList.getSelection().length >= 1) {

				// Active the graph in the graphPad
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
				if (communities.getOrderedTargetList()[0] != null && nodeList.getSelection()[0] != null) {
					graphPad.getAssembler().loadGraph(GraphPad.getFile(), communities.getOrderedTargetList(),
							nodeList.getSelection(), edgeList.getSelection(), layoutSelection, GraphLoader.GRAPHML);
					GraphPad.setActiveGraph(true);
				} else {
					JOptionPane.showMessageDialog(graphPad.frame,
							"Missing either \"community\" or \"label\" attributes", "Import warning",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(graphPad.frame,
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
		}

		// Hide Import Menu from main panel
		importMenu.setVisible(false);
		communities.setVisible(false);
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
