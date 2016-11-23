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
	public DropDownList nodeList, edgeList;

	public ImportMenu(PApplet app) {
		menu = new ControlP5(app);
		// for nodes
		nodeList = new DropDownList (app, "Node Attributes");
		nodeList.setPos(100, 300);
		String [] nodeAttributeNames = {"Community", "Label", "Size", "Node", "color"};
		nodeList.setAttributes(nodeAttributeNames);
		// for edges
		edgeList = new DropDownList (app, "Node Attributes");
		edgeList.setPos(100, 450);
		String [] edgeAttributeNames = {"Source thickness", "Target","thickness", "Body thickness", "Body color"};
		edgeList.setAttributes(edgeAttributeNames);
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
		nodeList.addElementAttributes(nodeAttributeKeys);
		edgeList.addElementAttributes(edgeAttributeKeys);
		menu.addBang("bang").setPosition(195, 55).setSize(100, 280).setTriggerEvent(Bang.RELEASE).setLabel("Load graph");
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
			for (int i = 0; i < nodeList.selection.length; i++) {
				System.out.print(nodeList.selection[i] + ", ");
			}
			// ***** stitch selection lists and do the retrieval
			
			System.out.println("---");
			Executable.app.loadGraph(Executable.file, nodeList.selection[0], nodeList.selection[1], nodeList.selection[2],
					nodeList.selection[3], Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
			Executable.activeGraph = true;
			// *** Ideally this object must be deleted.
			menu.setVisible(false);
			nodeList.dropMenu.setVisible(false);
			edgeList.dropMenu.setVisible(false);
		}
	}

}
