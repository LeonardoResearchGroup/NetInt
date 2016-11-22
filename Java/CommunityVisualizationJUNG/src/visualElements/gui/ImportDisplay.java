package visualElements.gui;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import controlP5.Accordion;
import controlP5.ControlEvent;
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

public class ImportDisplay {
	ControlP5 menu;

	public ImportDisplay(PApplet app) {
		menu = new ControlP5(app);
		//app.registerMethod("controlEvent", this);
	}

	public void makeLists(ArrayList<String> nodeAttributes, ArrayList<String> edgeAttributes) {
		Group g1 = menu.addGroup("Node Attributes").setPosition(300, 55).setWidth(180).setBackgroundColor(new Color(0, 0, 0, 64).getRGB())
				.setBackgroundHeight(180);
		Group g2 = menu.addGroup("Edge Attributes").setPosition(490, 55).setWidth(180).setBackgroundColor(new Color(0, 0, 0, 64).getRGB())
				.setBackgroundHeight(180);
		nodeAttributes(g1, nodeAttributes);
		edgeAttributes(g2, edgeAttributes);		
		// TEST
		ArrayList<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		menu.addScrollableList("communitys").setPosition(5, 5).setSize(170, 150).setBarHeight(13).setItemHeight(13)
		.addItems(list);
	}

	/**
	 * Menu component related to Node attributes
	 * 
	 * @param group
	 *            The Group of node attributes
	 */
	private void nodeAttributes(Group group, ArrayList<String> attributes) {
		menu.addScrollableList("community").setPosition(5, 5).setSize(170, 150).setBarHeight(13).setItemHeight(13)
				.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
		menu.addScrollableList("Size").setPosition(5, 20).setSize(170, 150).setBarHeight(13).setItemHeight(13)
		.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
		menu.addScrollableList("Color").setPosition(5, 35).setSize(170, 150).setBarHeight(13).setItemHeight(13)
		.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
		menu.addScrollableList("Label").setPosition(5, 50).setSize(170, 150).setBarHeight(13).setItemHeight(13)
		.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
	}

	/**
	 * Menu component related to edge attributes
	 * 
	 * @param group
	 *            The Group of edge attributes
	 */
	private void edgeAttributes(Group group, ArrayList<String> attributes) {
		menu.addScrollableList("Source_Color").setPosition(5, 5).setSize(170, 150).setBarHeight(13).setItemHeight(13)
				.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
		menu.addScrollableList("Target_Color").setPosition(5, 20).setSize(170, 150).setBarHeight(13).setItemHeight(13)
		.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
		menu.addScrollableList("Body_Color").setPosition(5, 35).setSize(170, 150).setBarHeight(13).setItemHeight(13)
		.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
		menu.addScrollableList("Thickness").setPosition(5, 50).setSize(170, 150).setBarHeight(13).setItemHeight(13)
		.addItems(attributes).setType(ScrollableList.LIST).moveTo(group).close();
	}

	public void controlEvent(ControlEvent theEvent) {
		 System.out.println("ImportDisplay> Event at: " + theEvent.getController().getName());
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
	
	public void communitys(int n){
		System.out.println (menu.get(ScrollableList.class, "communitys").getItem(n).toString());
	}
	
}
