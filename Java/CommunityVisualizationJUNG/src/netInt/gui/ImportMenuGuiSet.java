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
package netInt.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import guiSet.GuiElement;
import guiSet.GuiSet;
import guiSet.Item;
import guiSet.ItemList;
import guiSet.SelectableList;
import netInt.GraphPad;
import netInt.containers.Container;
import netInt.utilities.GraphLoader;
import netInt.utilities.mapping.Mapper;
import netInt.utilities.mapping.MapperViewer;
import processing.core.PVector;

/**
 * The menu displayed to assign attributes from the graph file to the
 * visualization elements
 * 
 * Updated June 2017
 * 
 * @author jsalam
 *
 */
public class ImportMenuGuiSet implements Observer {
	private GuiSet menu;
	private SelectableList communities;
	private ItemList nodeNameList;
	private ItemList edgeThicknessList;
	private ItemList layoutList;
	private Item loadButton;
	private boolean graphLoaded = false;

	public GraphPad graphPad;

	public ImportMenuGuiSet(GraphPad app) {
		this.graphPad = app;
		menu = new GuiSet(graphPad.parent);
	}

	/**
	 * This show simple draws the SelectableList on the graphPad. It is not the
	 * best solution but it works.
	 */
	public void show() {
		if (menu != null) {
			menu.show();
		}
	}

	public void init() {

		// Selectable List for nested communities
		communities = new SelectableList(100f, 110f, "Node Attributes", "Nesting order");
		communities.setItemSize(100, 12);
		communities.setName("Select nesting order");

		// ItemList for node names
		nodeNameList = new ItemList(400f, 145f, "Node name");
		nodeNameList.setItemSize(new PVector(120, 12));

		// for edges
		edgeThicknessList = new ItemList(550f, 145f, "Edge weight");
		edgeThicknessList.setItemSize(new PVector(120, 12));

		// for layout
		layoutList = new ItemList(700f, 145f, "Visualization Layout");
		layoutList.setItemSize(new PVector(120, 12));

		// for load button
		loadButton = new Item();
		loadButton.setDimension(new PVector(100, 25));
		loadButton.setPosition(new PVector(850f, 145f));
		loadButton.setLabel("Load Graph");
		loadButton.setName("Finalize import");

		// add elements to the gui set
		menu.addGuiElement(communities);
		menu.addGuiElement(nodeNameList);
		menu.addGuiElement(edgeThicknessList);
		menu.addGuiElement(layoutList);
		menu.addGuiElement(loadButton);

		// Register GUI events
		menu.registerGuiSetEvents(graphPad.parent);

		// Add observers to menu items
		menu.addObserverToGuiElement(this, "Finalize import");
	}

	/**
	 * Creates two ContorlP5.Group, one for nodes and one for edges. Both groups
	 * are attached to an instance of ControlP5 that draws them in the PApplet
	 * using an internal ControlP5.autoDraw() method. It initializes the
	 * ControlP5 variables every time it is invoked. Albeit this is done in the
	 * constructor, it is necessary to do it here in order to have fresh
	 * variables every time the user loads new files without restarting the
	 * application
	 * 
	 * @param nodeAttributeKeys
	 *            The list of node attributes retrieved from the graphml header
	 * @param edgeAttributeKeys
	 *            The list of edge attributes retrieved from the graphml header
	 * @param layoutAttributeKeys
	 *            The list of available layout names
	 */
	public void makeLists(ArrayList<String> nodeAttributeKeys, ArrayList<String> edgeAttributeKeys,
			ArrayList<String> layoutAttributeKeys) {
		// Initialize variables
		init();

		// populate the selectable list
		String[] nodeAttributeNames = nodeAttributeKeys.toArray(new String[nodeAttributeKeys.size()]);
		communities.setSourceItems(nodeAttributeNames);

		// populate the nodeList
		nodeNameList.setItems(nodeAttributeNames, true);

		// Populate edge list
		String[] edgeAttributeNames = edgeAttributeKeys.toArray(new String[edgeAttributeKeys.size()]);
		edgeThicknessList.setItems(edgeAttributeNames, true);

		// Populate layout list
		String[] layoutNames = layoutAttributeKeys.toArray(new String[layoutAttributeKeys.size()]);
		layoutList.setItems(layoutNames, true);
	}

	/**
	 * This method initiates the importing process based on the user defined
	 * parameters from the import menu.
	 * 
	 * NOTE: It is set to import graphml files only. Jan 15 2017
	 * 
	 * NOTE: Updated with guiSet library. It now works with observer-observable
	 * pattern. This object observes buttons from the menu instance.
	 * 
	 */
	private void choiceCatcher(GuiElement button) {

		// If the load graph button is pressed
		if (button.getName().equalsIgnoreCase("Finalize import")) {

			// Get arrays to simplify verbose
			String[] communityStructure = communities.getOrderedTargetList();

			String[] nodeName = nodeNameList.getOrderedLabelList();

			String[] edgeThickness = edgeThicknessList.getSelectedLabels();

			// Don't know what this does
			ArrayList<String> temp = new ArrayList<String>(Arrays.asList(nodeNameList.getItemLabels()));
			UserSettings.getInstance().setDescriptiveStatisticKeys(temp);

			// get the user selection for nodes
			System.out.println("Node import selection:");

			for (int i = 0; i < nodeName.length; i++) {
				System.out.println("..." + nodeNameList.getName() + ": " + nodeName[i] + ", ");
			}

			// get the user selection for edges

			System.out.println("Edge import selection:");

			for (int i = 0; i < edgeThickness.length; i++) {
				System.out.println("..." + edgeThicknessList.getName() + ": " + edgeThickness[i] + ", ");
			}

			System.out.println("_ _ _");

			// Verify if the user selected at least the first two node
			// attributes from the menu: one for community and one for label
			if (communityStructure.length >= 1 && nodeName.length >= 1) {

				// Active the graph in the graphPad
				GraphPad.setActiveGraph(true);

				// If the user does not select a layout, Concentric is
				// assigned by default

				int layoutSelection;

				try {

					layoutSelection = layoutSelection(layoutList.getSelectedLabels()[0]);

				} catch (ArrayIndexOutOfBoundsException e) {

					layoutSelection = Container.CONCENTRIC;

				}

				// Ask the assembler to load the graph
				if (communityStructure[0] != null && nodeName != null) {

					// set graphLoaded to true
					graphLoaded = graphPad.getAssembler().loadGraph(GraphPad.getFile(), communityStructure, nodeName,
							edgeThickness, layoutSelection, GraphLoader.GRAPHML);

					// Enable some loop operations in GraphPad
					GraphPad.setActiveGraph(graphLoaded);

				} else {

					JOptionPane.showMessageDialog(graphPad.parent.frame,
							"Missing either \"community\" or \"label\" attributes", "Import warning",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {

				JOptionPane.showMessageDialog(graphPad.parent.frame,
						"Must select at least \"community\" and \"label\" attributes", "Import warning",
						JOptionPane.WARNING_MESSAGE);
			}

			if (graphLoaded) {

				// Populate Control panel with attributes retrieved from the
				// graph if the control panel exists
				if (ControlPanel.getInstance() != null) {

					ArrayList<String> nodeAttributesKeys = Mapper.getInstance().getNodeAttributesMax()
							.getAttributeKeys();

					ArrayList<String> edgeAttributeKeys = Mapper.getInstance().getEdgeAttributesMax()
							.getAttributeKeys();

					ControlPanel.getInstance().initGroups(nodeAttributesKeys, edgeAttributeKeys);

				}

				// Feed min & max values to mapper viewer
				MapperViewer.getInstance().initMinMaxValues();

				// Hide Import Menu from main panel
				menu.setVisible(false);
			}
		}

	}

	public boolean getGraphLoaded() {
		return graphLoaded;
	}

	private int layoutSelection(String value) {
		// Default selection fruchterman_reingold
		int selection = Container.CONCENTRIC;
		switch (value) {
		case "Spring":
			selection = Container.SPRING;
			break;
		case "Fruchterman-Reingold":
			selection = Container.FRUCHTERMAN_REINGOLD;
			break;
		case "Linear":
			selection = Container.LINEAR;
			break;
		case "Concentric":
			selection = Container.CONCENTRIC;
			break;
//		case "Circular":
//			selection = Container.CIRCULAR;
//			break;

		}
		return selection;
	}

	/// ***** Observer methods *****
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		choiceCatcher((GuiElement) arg);
	}
}
