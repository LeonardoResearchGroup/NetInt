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

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import controlP5.Accordion;
import controlP5.CheckBox;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControllerGroup;
import controlP5.Group;
import controlP5.ScrollableList;
import controlP5.Textfield;
import controlP5.Toggle;
import netInt.GraphPad;
import netInt.utilities.Assembler;
import netInt.utilities.ClassLoader;
import netInt.utilities.GraphLoader;
import netInt.utilities.SerializeHelper;
import netInt.utilities.SerializeWrapper;
import netInt.utilities.mapping.Mapper;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * @author jsalam Example adapted from
 *         https://github.com/sojamo/controlp5/issues/17
 *
 */
public class ControlPanel extends PApplet {
	int w, h;
	private PApplet parent;
	private ControlP5 main;
	private ControlP5 secondary;
	private Group bancaGroup;
	private Group bancaGroupEafit;
	private CheckBox cBox;
	private Accordion accordion;
	private PFont font;
	private static PImage logo;
	// From NetInt: Java Network Interaction Visualization Library.
	private final String netInt_EXTENSION = "nti";
	// List of graphElements attribute names
	private ArrayList<String> keyNamesForNodes = new ArrayList<String>();
	private ArrayList<String> keyNamesForEdges = new ArrayList<String>();
	// Modules
	private boolean bancaModuleEnabled = false;

	private static ControlPanel CPInstance = null;

	public static ControlPanel getInstance() throws NullPointerException {
		if (CPInstance == null) {
			return null;
		}
		return CPInstance;
	}

	/**
	 * This constructor is used to create a control panel for user selection of
	 * visualization parameters
	 * 
	 * @param _parent
	 *            parent PApplet
	 * @param _w
	 *            window width
	 * @param _h
	 *            window height
	 */
	public ControlPanel(PApplet _parent, int _w, int _h) {
		super();
		parent = _parent;
		w = _w;
		h = _h;
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
		CPInstance = this;
	}

	/**
	 * This constructor is used to create a control panel for user selection of
	 * visualization parameters. It sets the ControlPanel logo.
	 * 
	 * @param _parent
	 *            parent PApplet
	 * @param image
	 *            The application logo
	 * @param _w
	 *            window width
	 * @param _h
	 *            window height
	 */
	public ControlPanel(PApplet _parent, PImage image, int _w, int _h) {
		super();
		parent = _parent;
		w = _w;
		h = _h;
		logo = image;
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
		CPInstance = this;
	}

	public void setup() {
		
		System.out.println("CPanel initialized 1");
		this.surface.setSize(w, h);
		
		System.out.println("CPanel initialized 2");
		this.surface.setLocation(0, 45);
		
		System.out.println("CPanel initialized 3");
		this.surface.setAlwaysOnTop(false);
		
		System.out.println("CPanel initialized 4");
		this.surface.setResizable(true);
		
		if (logo == null) {
			// URL url = GraphPad.class.getResource("/controlPanelImage.png");
			// logo = loadImage(url.getPath());
			logo = loadImage("./images/netInt.png");
		}
		
		keyNamesForNodes.add("empty list");
		
		keyNamesForEdges.add("empty list");
		
		initMain();
		
		// Font
		font = createFont("Arial", 11, false);
		
		textFont(font);
	}

	public void exit() {
		System.out.println("Control Panel Closed");
	}

	/**
	 * Main GUI method that assembles all the GUI components
	 */
	public void initMain() {
		main = new ControlP5(this);
		secondary = new ControlP5(this);
		secondary.hide();
		main.setColorBackground(0xff353535);
		secondary.setColorBackground(0xff353535);

		String[] fileFunctions = { "Open", "Save", "Import", "Export", "Load Module", "Quit" };
		main.addScrollableList("File").setPosition(10, 55).setSize(180, 100).setBarHeight(18).setItemHeight(13)
				.addItems(fileFunctions).setType(ScrollableList.LIST).open();
	}

	/**
	 * This method is invoked at Import Menu
	 * 
	 * @param nodeKeyNames
	 *            User Selection of node names list
	 * @param edgeKeyNames
	 *            User selection of edge list
	 */
	public void initGroups(ArrayList<String> nodeKeyNames, ArrayList<String> edgeKeyNames) {

		setKeyNamesForNodes(nodeKeyNames);
		setKeyNamesForEdges(edgeKeyNames);

		// Group instantiation
		Group backgGroup = new Group(secondary, "Background");
		Group nodesGroup = new Group(secondary, "Node");
		Group edgesGroup = new Group(secondary, "Edge");
		Group communitiesGroup = new Group(secondary, "Community");
		bancaGroup = new Group(secondary, "Financial Stats");

		// Group visual attributes
		Color color = new Color(45, 45, 45);
		backgGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(50);
		nodesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(350);
		edgesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(350);
		communitiesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(350);
		bancaGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(350);
		cBox = new CheckBox(secondary, "Stats nodes");
		cBox.setPosition(5, 7).moveTo(bancaGroup);
		bancaGroup.setVisible(bancaModuleEnabled);

		// Group Banca Eafit
		bancaGroupEafit = new Group(secondary, "Financial Stats Eafit");
		bancaGroupEafit.setBackgroundColor(color.getRGB()).setBackgroundHeight(350);
		bancaGroupEafit.setVisible(bancaModuleEnabled);

		// Add Components to each group
		setBackgroundComponents(backgGroup);
		setNodeComponents(nodesGroup);
		setEdgeComponents(edgesGroup);
		setEstadisticasDescriptivasComponent();

		// Accordion GUI
		accordion = secondary.addAccordion("acc").setPosition(10, 165).setWidth(180).setMinItemHeight(160);

		// create a new accordion. Add g1, g2, and g3 to the accordion.
		accordion.addItem(backgGroup).addItem(nodesGroup).addItem(edgesGroup).addItem(communitiesGroup)
				.addItem(bancaGroup).addItem(bancaGroupEafit);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordion.setCollapseMode(Accordion.MULTI);

		// open close sections
		accordion.open(1); // ,2,3

		// Show controller
		secondary.show();

	}

	/**
	 * GUI component related to Color background
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setBackgroundComponents(Group group) {
		secondary.addSlider("Luminosity").setPosition(5, 10).setSize(165, 18).setRange(0, 255).setValue(70)
				.moveTo(group).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE);
		;
	}

	/**
	 * GUI component related to Node Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setNodeComponents(ControllerGroup<Group> group) {
		Accordion accordionNodes;

		// Visibility control
		secondary.addToggle("On/Off").setPosition(5, 5).setSize(45, 10).setValue(true).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Name Visibility control
		secondary.addToggle("Name").setPosition(60, 5).setSize(45, 10).setValue(false).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Node search
		secondary.addTextfield("Search ID").setPosition(5, 20).setSize(68, 15).setAutoClear(false).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setPaddingX(35);

		// Clear node search
		secondary.addBang("Clear").setPosition(77, 20).setSize(28, 15).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		secondary.addSlider("Min OutDegree").setPosition(5, 40).setSize(100, 10).setRange(0, 35)
				.setNumberOfTickMarks(36).snapToTickMarks(true).moveTo(group);

		// Appearance controllers
		accordionNodes = secondary.addAccordion("accNodes").setPosition(5, 53).setWidth(100).setMinItemHeight(42)
				.moveTo(group);
		Group converterNames = new Group(secondary, "Converters");
		Group nodeAttrList = new Group(secondary, "Attributes");

		// Converters
		String[] items = Mapper.getInstance().getConvertersList();
		secondary.addScrollableList("Converter Node").addItems(items).setPosition(4, 2).setSize(98, 39).setBarHeight(13)
				.setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(converterNames).close();

		// Diameter
		Object[] mappers = Mapper.getInstance().getNodeAttributesMax().getAttributeKeys().toArray();
		items = new String[mappers.length];
		for (int i = 0; i < mappers.length; i++) {
			items[i] = (String) mappers[i];
		}

		secondary.addScrollableList("Diameter").setLabel("Diameter").addItems(items).setPosition(4, 2).setSize(98, 39)
				.setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(nodeAttrList).close()
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER);

		// create a new accordion. Add groups to the accordion.
		accordionNodes.addItem(converterNames).addItem(nodeAttrList);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordionNodes.setCollapseMode(Accordion.MULTI);
	}

	/**
	 * GUI component related to Edge Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setEdgeComponents(Group group) {
		Accordion accordionEdges;

		// Visibility control
		secondary.addToggle("Internal").setPosition(5, 7).setSize(45, 10).setValue(true).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		secondary.addToggle("External").setPosition(60, 7).setSize(45, 10).setValue(true).moveTo(group)
				.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// Weight
		float[] minMaxWeightValue = Mapper.getInstance().getMinMaxForEdges(keyNamesForEdges.get(0));
		secondary.addSlider("Weight").setPosition(5, 20).setSize(100, 10)
				.setRange(minMaxWeightValue[0], minMaxWeightValue[1]).moveTo(group);

		// Propagation
		secondary.addSlider("Succesors").setPosition(5, 33).setSize(68, 10).setRange(1, 10).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setPaddingX(35);

		// Visualize only propagation
		secondary.addToggle("Only").setPosition(77, 33).setSize(28, 10).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Appearance controllers
		accordionEdges = secondary.addAccordion("accEdges").setPosition(5, 53).setWidth(120).setMinItemHeight(42)
				.moveTo(group);
		Group converterNamesEdges = new Group(secondary, "Converters Edges");
		Group edgeAttrList = new Group(secondary, "Attributes Edges");

		// Converters
		String[] items = Mapper.getInstance().getConvertersList();
		secondary.addScrollableList("Converter Edge").addItems(items).setPosition(4, 2).setSize(98, 39).setBarHeight(13)
				.setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(converterNamesEdges).close();

		// Thickness
		Object[] mappers = Mapper.getInstance().getEdgeAttributesMax().getAttributeKeys().toArray();
		items = new String[mappers.length];
		for (int i = 0; i < mappers.length; i++) {
			items[i] = (String) mappers[i];
		}
		secondary.addScrollableList("Thickness").setPosition(4, 2).setSize(98, 39).setBarHeight(13).setItemHeight(13)
				.addItems(items).setType(ScrollableList.DROPDOWN).moveTo(edgeAttrList).close();

		// create a new accordion. Add groups to the accordion.
		accordionEdges.addItem(converterNamesEdges).addItem(edgeAttrList);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordionEdges.setCollapseMode(Accordion.MULTI);
	}

	/**
	 * GUI component related to descriptive statistics of clients
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setEstadisticasDescriptivasComponent() {
		// Set new names
		for (int i = 0; i < keyNamesForNodes.size(); i++) {
			cBox.addItem(keyNamesForNodes.get(i), 1);
		}
	}

	public void enableBancaModule() {
		bancaModuleEnabled = true;
	}

	public void draw() {
		background(70);
		// logo
		if (logo != null) {
			image(logo, 10, 9);
		} else {
			text("Missing Netint logo", 10, 9);
		}
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isGroup()) {
			// **** DESCRIPTIVE STATISTICS ****
			if (theEvent.isFrom(cBox)) {
				switchBooleans();
			}
		} else {
			// **** All OTHER CONTROLLERS****
			switchCaseCP5(theEvent);
		}
		// At any event notify VisibilitySettings class
		UserSettings.getInstance().setEventOnVSettings(true);
	}

	private void switchBooleans() {
		for (int i = 0; i < cBox.getArrayValue().length; i++) {
			// get its Caption label
			String itemLabel = cBox.getItem(i).getCaptionLabel().getText();
			boolean value = cBox.getItem(i).getState();
			UserSettings.getInstance().setStatisticVisibility(itemLabel, value);
		}
	}

	private void switchCaseCP5(ControlEvent theEvent) {
		String controllerName = theEvent.getController().getName();
		// System.out.println("ControlPanel> Event at: " + controllerName);
		switch (controllerName) {

		case "File":
			// Get the index
			int valueFile = (int) main.get(ScrollableList.class, "File").getValue();
			// The value extracted from the map at that index item
			String selectionFile = (String) main.get(ScrollableList.class, "File").getItem(valueFile).get("name")
					.toString();
			// Handle selection
			manageFileSelection(selectionFile);
			break;

		// **** FONDO ****
		case "Luminosity":
			UserSettings.getInstance().setColorBackground((int) theEvent.getController().getValue());
			break;

		// **** NODES ****
		case "Search ID":
			UserSettings.getInstance().setIDSerch(theEvent.getStringValue());
			break;
		case "Clear":
			secondary.get(Textfield.class, "Search ID").clear();
			UserSettings.getInstance().resetSearchId();
			break;
		case "Min OutDegree":
			UserSettings.getInstance().setDegreeThreshold(theEvent.getValue());
			break;
		case "On/Off":
			Toggle nodo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowNodes(nodo.getBooleanValue());
			break;
		case "Name":
			Toggle nombre = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowName(nombre.getBooleanValue());
			break;
		case "Converter Node":
			int valueCN = (int) secondary.get(ScrollableList.class, "Converter Node").getValue();
			UserSettings.getInstance().setConverterNode(
					secondary.get(ScrollableList.class, "Converter Node").getItem(valueCN).get("name").toString());
			break;
		case "Diameter":
			int valueD = (int) secondary.get(ScrollableList.class, "Diameter").getValue();
			UserSettings.getInstance().setNodeFilters(
					secondary.get(ScrollableList.class, "Diameter").getItem(valueD).get("name").toString());
			break;

		// **** EDGES ****
		case "Internal":
			Toggle vinculoInt = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowInternalEdges(vinculoInt.getBooleanValue());
			break;
		case "External":
			Toggle vinculoExt = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowExternalEdges(vinculoExt.getBooleanValue());
			break;
		case "Weight":
			UserSettings.getInstance().setWeight(theEvent.getValue());
			break;
		case "Succesors":
			UserSettings.getInstance().setPropagation(theEvent.getValue());
			break;
		case "Only":
			Toggle solo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setPropagationOnly(solo.getBooleanValue());
			break;
		case "Converter Edge":
			int valueCE = (int) secondary.get(ScrollableList.class, "Converter Edge").getValue();
			UserSettings.getInstance().setConverterEdge(
					secondary.get(ScrollableList.class, "Converter Edge").getItem(valueCE).get("name").toString());
			break;
		case "Thickness":
			int valueE = (int) secondary.get(ScrollableList.class, "Thickness").getValue();

			UserSettings.getInstance().setEdgeFilters(
					secondary.get(ScrollableList.class, "Thickness").getItem(valueE).get("name").toString());
			break;
		default:
			// Executable.retrieveControlPanelEvent(theEvent);
			break;
		}

	}

	@SuppressWarnings("rawtypes")
	private void manageFileSelection(String choice) {
		switch (choice) {
		case "Open":
			String selectedFile = ChooseHelper.getInstance().showJFileChooser(false, netInt_EXTENSION);

			try {
				parent.cursor(WAIT);
				SerializeWrapper deserializedWrapper = SerializeHelper.getInstance().deserialize(selectedFile);
				GraphPad.setActiveGraph(false);
				Assembler.secondOrderVComm = deserializedWrapper.getvSubCommunities();
				for (netInt.visualElements.VCommunity com : Assembler.secondOrderVComm) {
					com.eventRegister(parent);
				}
				Assembler.firstOrderVComm = deserializedWrapper.getvSubSubCommunity();
				Assembler.firstOrderVComm.eventRegister(parent);
				Assembler.firstOrderVComm.container.runVEdgeFactory();
				UserSettings.reloadInstance(deserializedWrapper.getvSettings());
				GraphLoader.theGraph = deserializedWrapper.getTheGraph();
				GraphPad.setActiveGraph(true);
				javax.swing.JOptionPane.showMessageDialog(null, "Finalizado.", "",
						javax.swing.JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException e1) {

				javax.swing.JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			} finally {
				parent.cursor(ARROW);
			}

			break;

		case "Save":

			String selectedPath = ChooseHelper.getInstance().showJFileChooser(true, netInt_EXTENSION);
			if (selectedPath != null) {
				parent.cursor(WAIT);
				SerializeWrapper wrapper = new SerializeWrapper(Assembler.firstOrderVComm, Assembler.secondOrderVComm,
						UserSettings.getInstance(), GraphLoader.theGraph);
				try {
					SerializeHelper.getInstance().serialize(wrapper, selectedPath, netInt_EXTENSION);
					javax.swing.JOptionPane.showMessageDialog(null,
							"File saved in " + selectedPath + "." + netInt_EXTENSION, "",
							javax.swing.JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException e) {
					javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
							javax.swing.JOptionPane.ERROR_MESSAGE);
				} finally {
					parent.cursor(ARROW);
				}
			}
			break;

		case "Import":
			ChooseHelper.getInstance().showFileChooser(parent);
			break;

		case "Export":
			if (accordion != null) {
				String selectedPathExport = ChooseHelper.getInstance().showJFileChooser(true, "png");
				if (selectedPathExport != null) {
					UserSettings.getInstance().setFileExportName(selectedPathExport);
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(null, "No frames to export. Try loading a graph first", "",
						javax.swing.JOptionPane.INFORMATION_MESSAGE);
			}
			break;

		case "Load Module":

			String selectedJar = ChooseHelper.getInstance().showJFileChooser(false, "jar");
			System.out.println("-------------------------------------------------------");
			try {
				parent.cursor(WAIT);
				ArrayList<Class> classes = ClassLoader.getInstance().loadClasses(selectedJar);

				// <DELETE>----------------------------------------------------------------------

				for (Class c : classes) {
					System.out.println(c.getName());
				}

				Object[] parameters = { "S", 2.1, 2.5 };

				try {

					Object response = ClassLoader.getInstance().invokeWithParameters(classes.get(2), "operar",
							parameters);
					boolean response2 = (boolean) ClassLoader.getInstance().invokeWithoutParameters(classes.get(1),
							"showNodes");

					UserSettings.getInstance().setShowNodes(response2);

					System.out.println(response);
					System.out.println(response2);

				} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
						| IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				parent.cursor(ARROW);
			}

			// </DELETE>----------------------------------------------------------------------
			break;

		case "Quit":
			System.exit(0);
			break;
		}
	}

	private void setKeyNamesForNodes(ArrayList<String> keyNames) {
		keyNamesForNodes = keyNames;
	}

	private void setKeyNamesForEdges(ArrayList<String> keyNames) {
		keyNamesForEdges = keyNames;
	}

	public ControlP5 getSecondaryControllerSet() {
		return secondary;
	}

	public void setSecondaryControllerSet(ControlP5 secondary) {
		this.secondary = secondary;
	}

	public ControlP5 getMainControllerSet() {
		return main;
	}

	public void setMainControllerSet(ControlP5 main) {
		this.main = main;
	}

	public static void setLogo(PImage loadImage) {
		logo = loadImage;
	}
}
