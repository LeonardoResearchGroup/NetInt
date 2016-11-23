package visualElements.gui;

import java.awt.Color;
import java.util.ArrayList;

import containers.Container;
import controlP5.Accordion;
import controlP5.Bang;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.ScrollableList;
import executable.Executable;
import processing.core.PApplet;
import utilities.GraphLoader;

public class ImportMenu implements ControlListener {
	public ControlP5 menu;
	public int barHeight = 13;
	public int itemHeight = 13;
	public int gap = 2;
	public String[] nodeAttributes = { "Community", "Label", "Size", "Node color" };
	public String[] edgeAttributes = { "Source thickness", "Target thickness", "Body thickness", "Body color" };
	public String[] importParameters = new String[nodeAttributes.length + edgeAttributes.length];
	public boolean loadGraphEnabled = false;
	private Accordion accordion;

	public ImportMenu(PApplet app) {
		menu = new ControlP5(app);
	}

	/**
	 * Creates two ContorlP5.Group, one for nodes and one for edges. Both groups
	 * are attached to an instance of ControlP5 that draws them in the PApplet
	 * using an internal ControlP5.autoDraw() method.
	 * 
	 * @param nodeAttributeKeys
	 * @param edgeAttributeKeys
	 */
	public void makeLists(ArrayList<String> nodeAttributeKeys, ArrayList<String> edgeAttributeKeys) {
		Group g1 = menu.addGroup("Node Attributes").setBackgroundColor(new Color(0, 0, 0, 64).getRGB());
		Group g2 = menu.addGroup("Edge Attributes").setBackgroundColor(new Color(0, 0, 0, 64).getRGB());
		menu.addBang("bang").setPosition(195, 55).setSize(100, 20).setTriggerEvent(Bang.RELEASE).setLabel("Load graph");
		menu.getController("bang").addListener(this);
		// Node attributes
		addElementAttributes(g1, nodeAttributes, nodeAttributeKeys);
		// Edge Attributes
		addElementAttributes(g2, edgeAttributes, edgeAttributeKeys);
		// create a new accordion. Add g1, g2, and g3 to the accordion.
		accordion = menu.addAccordion("acc").setPosition(10, 55).setWidth(180).addItem(g1).addItem(g2);

		// open close sections
		accordion.open(0, 1);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordion.setCollapseMode(Accordion.MULTI);
	}

	/**
	 * Inserts the values obtained from the list of keys retrieved from the
	 * graphml file into each element attribute. Each attribute is assigned to a
	 * group of controllers
	 * 
	 * @param group
	 *            The Group of controllers
	 */
	private void addElementAttributes(Group group, String[] controllerNames, ArrayList<String> attributeKeys) {
		for (int i = 0; i < controllerNames.length; i++) {
			menu.addScrollableList(controllerNames[i]).setPosition(5, 5 + (barHeight + gap) * i).setSize(170, 150)
					.setBarHeight(barHeight).setItemHeight(itemHeight).addItems(attributeKeys)
					.setType(ScrollableList.LIST).moveTo(group).close();
			// *** Important *** each controller must add a listener of events
			// caught by the interface ControlListener implemented by this
			// class
			menu.getController(controllerNames[i]).addListener(this);
		}
	}

	public void controlEvent(ControlEvent theEvent) {
		choiceCatcher(theEvent);

	}

	/**
	 * This method initiates the importing process based on the user defined
	 * parameters from the import menu
	 * 
	 * @param theEvent
	 */
	private void choiceCatcher(ControlEvent theEvent) {
		System.out.println("ImportDisplay> at switchCaseMenu(): " + theEvent.getController().getName());
		String controllerName = theEvent.getController().getName();

		// First it goes over the node attributes
		for (int i = 0; i < nodeAttributes.length; i++) {
			if (nodeAttributes[i].equals(controllerName)) {

				// deploy menu
				menu.getGroup("Node Attributes").getController(controllerName).setHeight(100);
				menu.getGroup("Node Attributes").getController(nodeAttributes[i - 1]).setPosition(5,
						69 + menu.getGroup("Node Attributes").getController(controllerName).getHeight());
				// Get the index
				int value = (int) menu.get(ScrollableList.class, controllerName).getValue();
				// The value extracted from the map at that index item
				importParameters[i] = (String) menu.get(ScrollableList.class, controllerName).getItem(value)
						.get("name");
				System.out.println(controllerName + ": " + importParameters[i]);
			}
		}
		// Then it goes over the edge attributes
		for (int i = 0; i < edgeAttributes.length; i++) {
			if (edgeAttributes[i].equals(controllerName)) {
				// Get the index
				int value = (int) menu.get(ScrollableList.class, controllerName).getValue();
				// The value extracted from the map at that index item
				importParameters[i + nodeAttributes.length] = (String) menu.get(ScrollableList.class, controllerName)
						.getItem(value).get("name");
				System.out.println(controllerName + ": " + importParameters[i + nodeAttributes.length]);
			}
		}
		if (controllerName.equals("bang")) {
			loadGraphEnabled = true;
			for (int i = 0; i < importParameters.length; i++) {
				System.out.print(importParameters[i] + ", ");
			}
			System.out.println("---");
			Executable.app.loadGraph(Executable.file, importParameters[0], importParameters[1], importParameters[2],
					importParameters[3], Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
			Executable.activeGraph = true;
			menu.setVisible(false);
		}
	}

}
