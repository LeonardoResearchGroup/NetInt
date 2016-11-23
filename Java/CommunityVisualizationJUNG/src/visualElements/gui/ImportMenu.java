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
import processing.event.MouseEvent;
import utilities.GraphLoader;
import visualElements.Canvas;

public class ImportMenu implements ControlListener {
	public ControlP5 menu;
	public int barHeight = 13;
	public int barWidth = 170;
	public int itemHeight = 13;
	public int gap = 2;
	public String[] nodeAttributes = { "n1", "n2", "n3" };
	public String[] edgeAttributes = { "e1", "e2", "e3" };
	// public String[] nodeAttributes = { "Community", "Label", "Size", "Node
	// color" };
	// public String[] edgeAttributes = { "Source thickness", "Target
	// thickness", "Body thickness", "Body color" };
	public boolean loadGraphEnabled = false;
	private Accordion accordion;
	public DropDownList nodeAtt;

	public ImportMenu(PApplet app) {
		menu = new ControlP5(app);
		nodeAtt = new DropDownList (app, "Node Attributes");
		nodeAtt.setPos(100, 300);
		String [] attributeNames = {"Community", "Label", "Size", "Node", "color"};
		nodeAtt.setAttributes(attributeNames);
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
		nodeAtt.addElementAttributes(nodeAttributeKeys);
		menu.addBang("bang").setPosition(195, 55).setSize(100, 20).setTriggerEvent(Bang.RELEASE).setLabel("Load graph");
		menu.getController("bang").addListener(this);
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
		if (controllerName.equals("bang")) {
			loadGraphEnabled = true;
			for (int i = 0; i < nodeAtt.selection.length; i++) {
				System.out.print(nodeAtt.selection[i] + ", ");
			}
			System.out.println("---");
			Executable.app.loadGraph(Executable.file, nodeAtt.selection[0], nodeAtt.selection[1], nodeAtt.selection[2],
					nodeAtt.selection[3], Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
			Executable.activeGraph = true;
			// *** Ideally this object must be deleted.
			menu.setVisible(false);
			nodeAtt.dropMenu.setVisible(false);
		}
	}

}
