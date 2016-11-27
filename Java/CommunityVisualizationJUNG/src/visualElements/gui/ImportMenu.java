package visualElements.gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import containers.Container;
import controlP5.Bang;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import executable.Executable;
import processing.core.PApplet;
import utilities.GraphLoader;

public class ImportMenu implements ControlListener {
	public ControlP5 menu;
	public int barHeight = 13;
	public int barWidth = 170;
	public int itemHeight = 13;
	public int gap = 2;
	public boolean loadGraphEnabled = false;
	public DropDownList nodeList, edgeList;
	public PApplet app;

	public ImportMenu(PApplet app) {
		menu = new ControlP5(app);
		// for nodes
		nodeList = new DropDownList(app, "Node Attributes");
		nodeList.setPos(100, 300);
		String[] nodeAttributeNames = { "Community", "Label", "Size", "Node", "color" };
		nodeList.setAttributes(nodeAttributeNames);
		// for edges
		edgeList = new DropDownList(app, "Edge Attributes");
		edgeList.setPos(100, 450);
		String[] edgeAttributeNames = { "Source thickness", "Target", "thickness", "Body thickness", "Body color" };
		edgeList.setAttributes(edgeAttributeNames);
		this.app = app;
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
		menu.addBang("bang").setPosition(100, 600).setSize(100, 20).setTriggerEvent(Bang.RELEASE)
				.setLabel("Load graph");
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
			for (int i = 0; i < nodeList.getSelection().length; i++) {
				System.out.print(nodeList.getSelection()[i] + ", ");
			}
			// ***** stitch selection lists and do the retrieval

			System.out.println("---");
			// If the user selected at least the first two attributes from the
			// menu
			if (nodeList.getSelection().length >= 2) {
				Executable.app.loadGraph(Executable.file, nodeList.getSelection(), edgeList.getSelection(),
						Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
				Executable.activeGraph = true;
				// *** Ideally this object must be deleted.
				menu.setVisible(false);
				nodeList.dropMenu.setVisible(false);
				edgeList.dropMenu.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(app.frame, "Must select at least \"community\" and \"label\" attributes",
						"Import warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

}
