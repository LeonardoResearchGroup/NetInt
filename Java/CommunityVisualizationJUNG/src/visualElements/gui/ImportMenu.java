package visualElements.gui;

import java.util.ArrayList;
import java.util.Arrays;

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
	public int gap = 2;
	public DropDownList nodeList, edgeList, layoutList, graphImportFormat;
	public PApplet app;

	public ImportMenu(PApplet app) {
		this.app = app;
		init();
	}

	public void init() {
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
		// for layout
		layoutList = new DropDownList(app, "Visualization Layout");
		layoutList.setPos(100, 400);
		String[] layoutAttributeNames = { "Choose one" };
		layoutList.setAttributes(layoutAttributeNames);
	}

	/**
	 * Creates two ContorlP5.Group, one for nodes and one for edges. Both groups
	 * are attached to an instance of ControlP5 that draws them in the PApplet
	 * using an internal ControlP5.autoDraw() method. It initializes the
	 * ControlP5 variables every time it is invoked. Albeit is is done in the
	 * constructor, it is necessary to do here in order to have fresh variables
	 * every time the user loads new files without restarting the application
	 * 
	 * @param nodeAttributeKeys
	 * @param edgeAttributeKeys
	 */
	public void makeLists(ArrayList<String> nodeAttributeKeys, ArrayList<String> edgeAttributeKeys,
			ArrayList<String> layoutAttributeKeys) {
		// Initialize variables
		init();
		menu.setVisible(true);
		nodeList.dropMenu.setVisible(true);
		edgeList.dropMenu.setVisible(true);
		layoutList.dropMenu.setVisible(true);
		//
		nodeList.addElementAttributes(nodeAttributeKeys);
		edgeList.addElementAttributes(edgeAttributeKeys);
		layoutList.addElementAttributes(layoutAttributeKeys);
		menu.addBang("loadGraph").setPosition(100, 500).setSize(100, 20).setTriggerEvent(Bang.RELEASE)
				.setLabel("Load graph");
		menu.getController("loadGraph").addListener(this);
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
		if (controllerName.equals("loadGraph")) {
			ArrayList<String> temp = new ArrayList<String>(Arrays.asList(nodeList.attributes));
			ControlPanel.initGroups(temp);
			UserSettings.getInstance().setDescriptiveStatisticKeys(temp);
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
			if (nodeList.getSelection().length >= 2) {
				Executable.setActiveGraph(true);
				// If the user does not select a layout, Fruchterman_Reingold is
				// assigned by default
				int layoutSelection;
				try {
					layoutSelection = layoutSelection(layoutList.getSelection()[0]);
				} catch (ArrayIndexOutOfBoundsException e) {
					layoutSelection = Container.FRUCHTERMAN_REINGOLD;
				}
				// Ask the assembler to load the graph
				if (nodeList.getSelection()[0] != null && nodeList.getSelection()[1] != null) {
					Executable.app.loadGraph(Executable.file, nodeList.getSelection(), edgeList.getSelection(),layoutSelection, GraphLoader.GRAPHML);
					Executable.setActiveGraph(true);

				} else {
					JOptionPane.showMessageDialog(app.frame, "Missing either \"community\" or \"label\" attributes",
							"Import warning", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(app.frame, "Must select at least \"community\" and \"label\" attributes",
						"Import warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		menu.setVisible(false);
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
