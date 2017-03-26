/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 *
 * It makes extensive use of free libraries such as Processing, Jung, ControlP5, JOGL, 
 * Tinkerpop and many others. For details see the copyrights folder. 
 *
 * Contributors:
 * 	Juan Salamanca, Cesar Loaiza, Luis Felipe Rivera, Javier Diaz
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *
 * version alpha
 *******************************************************************************/
package netInt.visualElements.gui;

import java.util.ArrayList;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ScrollableList;
import processing.core.PApplet;

public class DropDownList implements ControlListener {
	public ControlP5 dropMenu;
	public int buttonHeight = 13;
	private int posX = 10;
	private int posY = 10;
	private int width = 170;
	// private int height = buttonHeight * 7;
	private int buttonWidth = (int) width - 10;
	private int gap = 2;
	public String menuName;
	public String[] attributes = { "1", "2", "3", "4" };
	// The user selection
	private String[] selection = new String[attributes.length];
	// The list of element names of this DropDownList
	private ArrayList<String> listNames;

	public DropDownList(PApplet app, String name) {
		dropMenu = new ControlP5(app);
		menuName = name;

	}

	/**
	 * Inserts the values obtained from the list of keys retrieved from the
	 * graphml file into each element attribute.
	 * 
	 * @param attributeKeys
	 */
	public void initializeList(ArrayList<String> attributeKeys) {
		listNames = attributeKeys;
		// name
		dropMenu.addLabel(menuName, posX, posY);

		// Attributes
		for (int i = 0; i < attributes.length; i++) {
			dropMenu.addScrollableList(attributes[i]).setPosition(posX + ((buttonWidth + gap) * i), posY + buttonHeight)
					.setSize(buttonWidth, 140).setBarHeight(buttonHeight).setItemHeight(buttonHeight)
					.addItems(attributeKeys).setType(ScrollableList.LIST).close();
			// *** Important *** each controller must add a listener of events
			// caught by the interface ControlListener implemented by this
			// class
			dropMenu.getController(attributes[i]).addListener(this);
		}

	}

	public void controlEvent(ControlEvent arg0) {
		choiceCatcher(arg0);
	}

	/**
	 * This method initiates the importing process based on the user defined
	 * parameters from the import menu
	 * 
	 * @param theEvent
	 */
	private void choiceCatcher(ControlEvent theEvent) {
		String controllerName = theEvent.getController().getName();
		System.out.println("DropDownList " +controllerName);
		// First it goes over the node attributes
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].equals(controllerName)) {
				// Get the index
				int value = (int) dropMenu.get(ScrollableList.class, controllerName).getValue();
				// The value extracted from the map at that index item
				selection[i] = (String) dropMenu.get(ScrollableList.class, controllerName).getItem(value).get("name");
				//System.out.println(controllerName + ": " + selection[i]);
			}
		}
	}

	// *** Setters
	public void setPos(int x, int y) {
		posX = x;
		posY = y;
	}

	public void setAttributes(String[] attributes) {
		this.attributes = attributes;
		// re-initilize selection
		selection = new String[attributes.length];
	}

	public String[] getSelection() {
		ArrayList<String> tmp = new ArrayList<String>();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] != null)
				tmp.add(selection[i]);
		}
		String[] rtn = new String[tmp.size()];
		for (int i = 0; i < rtn.length; i++) {
			rtn[i] = tmp.get(i);
		}
		return rtn;
	}

	public ArrayList<String> getListNames() {
		return listNames;
	}
}
