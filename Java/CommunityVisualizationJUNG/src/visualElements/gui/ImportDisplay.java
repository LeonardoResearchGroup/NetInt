package visualElements.gui;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import controlP5.Accordion;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.ScrollableList;
import controlP5.Textfield;
import controlP5.Toggle;
import executable.Executable;
import executable.Logica;
import processing.core.PApplet;
import processing.event.MouseEvent;
import utilities.SerializeHelper;
import utilities.SerializeWrapper;

public class ImportDisplay implements ControlListener {
	ControlP5 menu;
	int barHeight = 13;
	int itemHeight = 13;
	int gap = 2;

	public ImportDisplay(PApplet app) {
		menu = new ControlP5(app);
		
	}

	public void makeLists(ArrayList<String> nodeAttributes, ArrayList<String> edgeAttributes) {
		Group g1 = menu.addGroup("Node Attributes").setPosition(300, 55).setWidth(180)
				.setBackgroundColor(new Color(0, 0, 0, 64).getRGB()).setBackgroundHeight(180);
		Group g2 = menu.addGroup("Edge Attributes").setPosition(490, 55).setWidth(180)
				.setBackgroundColor(new Color(0, 0, 0, 64).getRGB()).setBackgroundHeight(180);
		String [] nodeItems = {"Community", "Label", "Size", "Color"};
		String [] edgeItems = {"Source color", "Target color", "Body color", "Thickness"};
		nodeAttributes(g1, nodeItems,nodeAttributes);
		edgeAttributes(g2, edgeItems, edgeAttributes);
	}

	/**
	 * Menu component related to Node attributes
	 * 
	 * @param group
	 *            The Group of node attributes
	 */
	private void nodeAttributes(Group group, String[] controllerNames,ArrayList<String> attributes) {
		for (int i = 0; i < controllerNames.length; i++) {
			menu.addScrollableList(controllerNames[i]).setPosition(5, 5 + (barHeight + gap)*i).setSize(170, 150).setBarHeight(barHeight).setItemHeight(itemHeight)
			.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
			menu.getController(controllerNames[i]).addListener(this);
		}
	}

	/**
	 * Menu component related to edge attributes
	 * 
	 * @param group
	 *            The Group of edge attributes
	 */
	private void edgeAttributes(Group group, String[] controllerNames,ArrayList<String> attributes) {
		for (int i = 0; i < controllerNames.length; i++) {
			menu.addScrollableList(controllerNames[i]).setPosition(5, 5 + (barHeight + gap)*i ).setSize(170, 150).setBarHeight(barHeight).setItemHeight(itemHeight)
			.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
			menu.getController(controllerNames[i]).addListener(this);
		}
	}

	public void controlEvent(ControlEvent theEvent) {
		switchCaseMenu(theEvent);
	}

	private void switchCaseMenu(ControlEvent theEvent) {
		System.out.println("ImportDisplay> at switchCaseMenu(): " + theEvent.getController().getName());
		switch (theEvent.getController().getName()) {
		case "Abrir":
			break;
		case "Guardar":
			break;
		default:
			break;
		}
	}


}
