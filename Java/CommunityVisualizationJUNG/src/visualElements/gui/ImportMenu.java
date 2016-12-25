package visualElements.gui;

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

/**
 * The menu displayed to assign attributes from the graph file to the
 * visualization elements
 * 
 * @author jsalam
 *
 */
public class ImportMenu implements ControlListener {
	public ControlP5 menu;
	public int barHeight = 13;
	public int barWidth = 170;
	public int itemHeight = 13;
	public int gap = 2;
	public DropDownList nodeList, edgeList;
	public PApplet app;

	public ImportMenu(PApplet app) {
		menu = new ControlP5(app);
		// for nodes
		nodeList = new DropDownList(app, "Node Attributes");
		nodeList.setPos(100, 100);
		String[] nodeAttributeNames = { "Community", "Label", "Size", "Color" };
		nodeList.setAttributes(nodeAttributeNames);
		// for edges
		edgeList = new DropDownList(app, "Edge Attributes");
		edgeList.setPos(100, 250);
		String[] edgeAttributeNames = { "Body thickness", "Target thickness", "Body color", "Target Color" };
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
		menu.addBang("loadGraph").setPosition(100, 400).setSize(100, 20).setTriggerEvent(Bang.RELEASE)
				.setLabel("Load graph");
		menu.getController("loadGraph").addListener(this);
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
		String controllerName = theEvent.getController().getName();
		if (controllerName.equals("loadGraph")) {
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
			if (nodeList.getSelection().length >=2) {
				if (nodeList.getSelection()[0] != null && nodeList.getSelection()[1] != null) {
					Executable.app.loadGraph(Executable.file, nodeList.getSelection(), edgeList.getSelection(),
							Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
					Executable.activeGraph = true;
					// *** Ideally this object must be deleted instead of
					// turning it
					// invisible. I should be created again if the user wants to
					// import a new file
					menu.setVisible(false);
					nodeList.dropMenu.setVisible(false);
					edgeList.dropMenu.setVisible(false);

				} else {
					JOptionPane.showMessageDialog(app.frame, "Missing either \"community\" or \"label\" attributes",
							"Import warning", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(app.frame, "Must select at least \"community\" and \"label\" attributes",
						"Import warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

}
