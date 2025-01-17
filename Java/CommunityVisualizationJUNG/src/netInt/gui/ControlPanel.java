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
import controlP5.Bang;
import controlP5.Button;
import controlP5.CheckBox;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControllerGroup;
import controlP5.Group;
import controlP5.ScrollableList;
import controlP5.Textfield;
import controlP5.Toggle;
import jViridis.ColorMap;
import netInt.GraphPad;
import netInt.canvas.Canvas;
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

	// The main controller loaded at class instantiation
	private ControlP5 main;

	// The secondary controller populated with data from the graph
	private ControlP5 secondary;

	// Groups
	private Group statisticsGroup;

	// Other controller elements
	private CheckBox cBox;
	private Accordion accordion;

	// The font
	private PFont font;

	// NetInt Logo. Generic logo loaded by default
	private static PImage logo;

	// From NetInt: Java Network Interaction Visualization Library.
	private final String netInt_EXTENSION = "nti";

	// List of graphElements attribute names
	private ArrayList<String> keyNamesForNodes = new ArrayList<String>();
	private ArrayList<String> keyNamesForEdges = new ArrayList<String>();

	// Singleton Pattern
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

		this.surface.setSize(w, h);

		this.surface.setLocation(0, 0);

		this.surface.setAlwaysOnTop(false);

		this.surface.setResizable(true);

		if (logo == null) {
			// URL url = GraphPad.class.getResource("/controlPanelImage.png");
			// logo = loadImage(url.getPath());
			logo = loadImage("./images/netInt.png");
		}

		keyNamesForNodes.add("empty list");

		keyNamesForEdges.add("empty list");

		initMain();

		System.out.println("CPanel initialized ");

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

		// Instantiate main controller
		main = new ControlP5(this);

		// Instantiate secondary controller
		secondary = new ControlP5(this);

		// Switch secondary controller invisible
		secondary.hide();

		// Accordeon
		accordion = secondary.addAccordion("acc").setPosition(10, 165).setWidth(206).setMinItemHeight(342);
		Color color = new Color(125, 125, 125);
		accordion.setColorValue(color.getRGB());

		// Set background color
		main.setColorBackground(0xff353535);
		secondary.setColorBackground(0xff353535);

		// Add list to main controller
		String[] fileFunctions = { "Open", "Save", "Import", "Export_PDF", "Quit" }; // "Load Module",
		main.addScrollableList("File").setPosition(10, 55).setSize(206, 100).setBarHeight(18).setItemHeight(13)
				.addItems(fileFunctions).setType(ScrollableList.LIST).open();
	}

	/**
	 * This method is invoked at Import Menu
	 * 
	 * @param nodeKeyNames
	 *            List of names of node attributes
	 * @param edgeKeyNames
	 *            List of names of edge attributes
	 */
	public void initGroups(ArrayList<String> nodeKeyNames, ArrayList<String> edgeKeyNames) {

		setKeyNamesForNodes(nodeKeyNames);
		setKeyNamesForEdges(edgeKeyNames);

		// Group instantiation
		Group settingsGroup = new Group(secondary, "Settings");
		Group nodesGroup = new Group(secondary, "Node");
		Group edgesGroup = new Group(secondary, "Edge");
		Group communitiesGroup = new Group(secondary, "Community");
		statisticsGroup = new Group(secondary, "Node attribute visibility");

		// Group visual attributes
		Color color = new Color(45, 45, 45);
		settingsGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		nodesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		edgesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		communitiesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		statisticsGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		cBox = new CheckBox(secondary, "Stats nodes");
		cBox.setPosition(5, 7).moveTo(statisticsGroup);

		// Add Components to each group
		setSettingsComponents(settingsGroup);
		setNodeComponents(nodesGroup);
		setEdgeComponents(edgesGroup);
		setCommunityComponents(communitiesGroup);
		setEstadisticasDescriptivasComponent();

		// create a new accordion. Add g1, g2, and g3 to the accordion.
		accordion.addItem(nodesGroup).addItem(edgesGroup).addItem(communitiesGroup).addItem(statisticsGroup)
				.addItem(settingsGroup);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordion.setCollapseMode(Accordion.MULTI);

		// open close sections
		// accordion.open(2); // ,2,3

		// Show controller
		secondary.show();

	}

	/**
	 * GUI component related to GraphPad's graphic environment settings
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setSettingsComponents(Group group) {

		// Background color
		secondary.addSlider("Luminosity").setPosition(5, 10).setSize(196, 18).setRange(0, 255).setValue(70)
				.moveTo(group).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE);

		// Adaptive performance

		secondary.addLabel("Adaptive performance").setPosition(2, 48).moveTo(group);

		secondary.addToggle("Adaptive_performance").setPosition(5, 63).setSize(196, 15).setValue(false)
				.setCaptionLabel("Adaptive performance").moveTo(group).getCaptionLabel()
				.align(ControlP5.LEFT, ControlP5.CENTER).setPaddingX(10);

		// JViridis palette names

		secondary.addLabel("Color map").setPosition(2, 83).moveTo(group);

		// String[] items = { ColorMap.VIRIDIS, ColorMap.INFERNO, ColorMap.MAGMA,
		// ColorMap.PLASMA };
		String[] items = ColorMap.getInstance().getColorPaletteNames();

		secondary.addScrollableList("Palette").setLabel("Color Palette").addItems(items).setPosition(5, 98).setValue(1f)
				.setSize(196, 66).setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER); // .close()

	}

	/**
	 * GUI component related to Node Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setNodeComponents(ControllerGroup<Group> group) {

		// Visibility control

		secondary.addLabel("Node visibility").setPosition(2, 7).moveTo(group);

		secondary.addToggle("On/Off Node").setPosition(5, 21).setSize(95, 10).setValue(true).moveTo(group)
				.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// Name Visibility control

		secondary.addToggle("On/Off Name").setPosition(106, 21).setSize(95, 10).setValue(false).moveTo(group)
				.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// Node search

		secondary.addLabel("Search by ID").setPosition(2, 41).moveTo(group);

		secondary.addTextfield("Search ID").setPosition(5, 56).setSize(94, 15).setAutoClear(false).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setPaddingX(35);

		// Clear node search
		secondary.addBang("Clear search").setPosition(106, 56).setSize(95, 15).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		secondary.addLabel("Filter by out degree").setPosition(2, 81).moveTo(group);

		secondary.addSlider("Min OutDegree").setPosition(5, 96).setSize(150, 10).setRange(0, 35)
				.setNumberOfTickMarks(10).snapToTickMarks(false).showTickMarks(false).moveTo(group).getCaptionLabel()
				.setPaddingX(10).setVisible(false); //

		// Node Appearance controllers

		secondary.addLabel("Node attributes").setPosition(2, 116).moveTo(group);

		// Diameter
		Object[] mappers = Mapper.getInstance().getNodeAttributesMax().getAttributeKeys().toArray();

		String[] items = new String[mappers.length];

		for (int i = 0; i < mappers.length; i++) {
			items[i] = (String) mappers[i];
		}

		secondary.addLabel("nSize:").setLabel("Size").setPosition(5, 131).moveTo(group);

		secondary.addScrollableList("Node_Size").setLabel("Size").addItems(items).setPosition(5, 141).setSize(95, 52)
				.setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group);// .getCaptionLabel()
		// .align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER); // .close()

		secondary.addLabel("nColor:").setLabel("Color:").setPosition(106, 131).moveTo(group);

		secondary.addScrollableList("Node_Color").setLabel("Color").addItems(items).setPosition(106, 141)
				.setSize(95, 52).setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER); // .close()

		// Converters
		items = Mapper.getInstance().getConvertersList();

		secondary.addLabel("nConverter:").setLabel("Converter:").setPosition(5, 203).moveTo(group);

		secondary.addScrollableList("Converter_Node").addItems(items).setPosition(5, 213).setSize(95, 52)
				.setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group); // .close();

	}

	/**
	 * GUI component related to Edge Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setEdgeComponents(Group group) {

		// Visibility control tier 1
		secondary.addLabel("Edge visibility tier 1").setPosition(2, 7).moveTo(group);

		secondary.addToggle("Tier_1_Internal").setPosition(5, 21).setSize(95, 10).setValue(true).moveTo(group)
				.setCaptionLabel("Internal").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// secondary.addToggle("Tier_1_External").setPosition(106, 21).setSize(95,
		// 10).setValue(true).moveTo(group)
		// .setCaptionLabel("External").getCaptionLabel().align(ControlP5.CENTER,
		// ControlP5.CENTER);

		// Visibility control tier 2
		secondary.addLabel("Edge visibility tier 2").setPosition(2, 41).moveTo(group);

		secondary.addToggle("Tier_2_Internal").setPosition(5, 56).setSize(95, 10).setValue(true).moveTo(group)
				.setCaptionLabel("Internal").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		secondary.addToggle("Tier_2_External").setPosition(106, 56).setSize(95, 10).setValue(true).moveTo(group)
				.setCaptionLabel("External").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// Weight

		secondary.addLabel("Weight filter: " + UserSettings.getInstance().getEdgeWeightAttribute()).setPosition(2, 76)
				.moveTo(group); //

		float[] minMaxWeightValue = Mapper.getInstance().getMinMaxForEdges(keyNamesForEdges.get(0));

		secondary.addSlider("Edge_Weight").setPosition(5, 90).setSize(150, 10)
				.setRange(minMaxWeightValue[0], minMaxWeightValue[1]).moveTo(group).getCaptionLabel().setVisible(false);

		// Propagation

		secondary.addLabel("Propagation").setPosition(2, 110).moveTo(group);

		secondary.addSlider("Succesors").setPosition(5, 124).setSize(95, 10).setRange(1, 10).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setPaddingX(10).setVisible(false);

		// Visualize and clear propagation
		secondary.addToggle("Filter").setPosition(106, 124).setSize(45, 10).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		secondary.addBang("Clear").setPosition(155, 124).setSize(45, 10).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Appearance controllers

		secondary.addLabel("Edge attributes").setPosition(2, 144).moveTo(group);

		// Thickness
		Object[] mappers = Mapper.getInstance().getEdgeAttributesMax().getAttributeKeys().toArray();

		String[] items = new String[mappers.length];

		for (int i = 0; i < mappers.length; i++) {
			items[i] = (String) mappers[i];
		}

		secondary.addLabel("eThickness:").setLabel("Thickness:").setPosition(5, 159).moveTo(group);

		secondary.addScrollableList("Edge_Thickness").setPosition(5, 169).setSize(95, 52).setBarHeight(13)
				.setItemHeight(13).addItems(items).setType(ScrollableList.DROPDOWN).setCaptionLabel("Thickness")
				.moveTo(group);

		secondary.addLabel("eColor:").setLabel("Color:").setPosition(106, 159).moveTo(group);

		secondary.addScrollableList("Edge_Color").setPosition(106, 169).setSize(95, 52).setBarHeight(13)
				.setItemHeight(13).addItems(items).setType(ScrollableList.DROPDOWN).setCaptionLabel("Color")
				.moveTo(group);

		// Converters
		items = Mapper.getInstance().getConvertersList();

		secondary.addLabel("eConverter:").setLabel("Converter:").setPosition(5, 231).moveTo(group);

		secondary.addScrollableList("Converter_Edge").addItems(items).setPosition(5, 241).setSize(95, 52)
				.setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group);
	}

	/**
	 * GUI component related to Community Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setCommunityComponents(ControllerGroup<Group> group) {

		// Visibility control

		secondary.addLabel("Filter by community size").setPosition(2, 7).moveTo(group);

		secondary.addSlider("Min_Community_Size").setPosition(5, 22).setSize(150, 10).setRange(0, 1000)
				.setNumberOfTickMarks(10).snapToTickMarks(false).showTickMarks(false).moveTo(group).getCaptionLabel()
				.setPaddingX(10).setVisible(false); //

		// Node Appearance controllers

		secondary.addLabel("Community attributes").setPosition(2, 42).moveTo(group);

		// Diameter
		Object[] mappers = Mapper.getInstance().getNodeAttributesMax().getAttributeKeys().toArray();

		// String[] items = new String[mappers.length];
		//
		// for (int i = 0; i < mappers.length; i++) {
		// items[i] = (String) mappers[i];
		// }

		String[] items = { "Community size" };

		secondary.addLabel("cSize:").setLabel("Size").setPosition(5, 57).moveTo(group);

		secondary.addScrollableList("Community_Size").setLabel("Size").addItems(items).setPosition(5, 67)
				.setSize(95, 52).setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group);

		secondary.addLabel("cColor:").setLabel("Color:").setPosition(106, 57).moveTo(group);

		secondary.addScrollableList("Community_Color").setLabel("Color").addItems(items).setPosition(106, 67)
				.setSize(95, 52).setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER);

		// Converters
		items = Mapper.getInstance().getConvertersList();

		secondary.addLabel("cConverter:").setLabel("Converter:").setPosition(5, 129).moveTo(group);

		secondary.addScrollableList("Converter_Community").addItems(items).setPosition(5, 139).setSize(95, 52)
				.setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group); // .close();

	}

	/**
	 * GUI component related to descriptive statistics of clients
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	public void setEstadisticasDescriptivasComponent() {
		// Set new names
		for (int i = 0; i < keyNamesForNodes.size(); i++) {
			cBox.addItem(keyNamesForNodes.get(i), 1);
		}
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
		// At any event notify VisibilitySettings class
		UserSettings.getInstance().setEventOnVSettings();

		if (theEvent.isGroup()) {
			// **** DESCRIPTIVE STATISTICS ****
			if (theEvent.isFrom(cBox)) {
				switchBooleans();
			}
		} else {
			// **** All OTHER CONTROLLERS****
			switchCaseCP5(theEvent);

			// Calls observable method and passes the event
			UserSettings.getInstance().setLatestEvent(theEvent);
		}
	}

	private void switchBooleans() {
		for (int i = 0; i < cBox.getArrayValue().length; i++) {
			// get its Caption label
			String itemLabel = cBox.getItem(i).getCaptionLabel().getText();
			boolean value = cBox.getItem(i).getState();
			UserSettings.getInstance().setStatisticVisibility(itemLabel, value);
		}
	}

	/**
	 * Handles events of group "secondary"
	 * 
	 * @param theEvent
	 */
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

		// **** GraphPad Background ****
		case "Luminosity":
			UserSettings.getInstance().setColorBackground((int) theEvent.getController().getValue());
			break;

		case "Adaptive_performance":
			Toggle performance = (Toggle) theEvent.getController();
			UserSettings.getInstance().setAdaptivePerformance(performance.getBooleanValue());
			if (performance.getBooleanValue()) {
				Canvas.setAdaptiveDegreeThresholdPercentage(100);
			} else {
				Canvas.setAdaptiveDegreeThresholdPercentage(0);
			}
			break;

		case "Palette":
			int valuePalette = (int) secondary.get(ScrollableList.class, "Palette").getValue();
			String colorMapName = secondary.get(ScrollableList.class, "Palette").getItem(valuePalette).get("name")
					.toString();
			ColorMap.getInstance().setColorMap(colorMapName);
			break;

		// **** NODES ****
		case "Search ID":
			UserSettings.getInstance().setIDSearch(theEvent.getStringValue());
			break;
		case "Clear search":
			secondary.get(Textfield.class, "Search ID").clear();
			UserSettings.getInstance().resetSearchId();
			break;
		case "Min OutDegree":
			UserSettings.getInstance().setDegreeThreshold(theEvent.getValue());
			break;
		case "On/Off Node":
			Toggle nodo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowNodes(nodo.getBooleanValue());
			break;
		case "On/Off Name":
			Toggle nombre = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowName(nombre.getBooleanValue());
			break;
		case "Node_Size":
			int nodeSize = (int) secondary.get(ScrollableList.class, "Node_Size").getValue();
			UserSettings.getInstance().setNodeSizeAtt(
					secondary.get(ScrollableList.class, "Node_Size").getItem(nodeSize).get("name").toString());
			break;
		case "Node_Color":
			int nodeColor = (int) secondary.get(ScrollableList.class, "Node_Color").getValue();
			UserSettings.getInstance().setNodeColorAtt(
					secondary.get(ScrollableList.class, "Node_Color").getItem(nodeColor).get("name").toString());
			break;
		case "Converter_Node":
			int valueCN = (int) secondary.get(ScrollableList.class, "Converter_Node").getValue();
			UserSettings.getInstance().setConverterNode(
					secondary.get(ScrollableList.class, "Converter_Node").getItem(valueCN).get("name").toString());
			break;

		// **** EDGES ****
		case "Tier_1_Internal":
			Toggle vinculoIntT1 = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowInternalEdges(0, vinculoIntT1.getBooleanValue());
			if (vinculoIntT1.getBooleanValue()) {
				Canvas.setAdaptiveDegreeThresholdPercentage(100);
			}
			break;
		// case "Tier_1_External":
		// Toggle vinculoExtT1 = (Toggle) theEvent.getController();
		// UserSettings.getInstance().setShowExternalEdges(0,
		// vinculoExtT1.getBooleanValue());
		// if (vinculoExtT1.getBooleanValue()) {
		// Canvas.setAdaptiveDegreeThresholdPercentage(100);
		// }
		// break;
		case "Tier_2_Internal":
			Toggle vinculoIntT2 = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowInternalEdges(1, vinculoIntT2.getBooleanValue());
			if (vinculoIntT2.getBooleanValue()) {
				Canvas.setAdaptiveDegreeThresholdPercentage(100);
			}
			break;
		case "Tier_2_External":
			Toggle vinculoExtT2 = (Toggle) theEvent.getController();
			UserSettings.getInstance().setShowExternalEdges(1, vinculoExtT2.getBooleanValue());
			if (vinculoExtT2.getBooleanValue()) {
				Canvas.setAdaptiveDegreeThresholdPercentage(100);
			}
			break;
		case "Edge_Weight":
			UserSettings.getInstance().setWeight(theEvent.getValue());
			break;
		case "Succesors":
			UserSettings.getInstance().setPropagation(theEvent.getValue());
			break;
		case "Filter":
			Toggle solo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setPropagationFilter(solo.getBooleanValue());
			break;
		case "Clear":
			Bang clearPropagation = (Bang) theEvent.getController();
			clearPropagation.setTriggerEvent(Bang.RELEASE);
			UserSettings.getInstance().setClearPropagation(true);
			break;
		case "Converter_Edge":
			int valueCE = (int) secondary.get(ScrollableList.class, "Converter_Edge").getValue();
			UserSettings.getInstance().setConverterEdge(
					secondary.get(ScrollableList.class, "Converter_Edge").getItem(valueCE).get("name").toString());
			break;
		case "Edge_Thickness":
			int edgeThickness = (int) secondary.get(ScrollableList.class, "Edge_Thickness").getValue();

			UserSettings.getInstance().setEdgeThicknessAtt(secondary.get(ScrollableList.class, "Edge_Thickness")
					.getItem(edgeThickness).get("name").toString());
			break;
		case "Edge_Color":
			int valueE = (int) secondary.get(ScrollableList.class, "Edge_Color").getValue();

			UserSettings.getInstance().setEdgeColorAtt(
					secondary.get(ScrollableList.class, "Edge_Color").getItem(valueE).get("name").toString());
			break;

		// **** COMMUNITIES ****
		case "Min_Community_Size": // slider
			UserSettings.getInstance().setCommunitySizeThreshold(theEvent.getValue());
			break;

		case "Community_Size":
			int communitySize = (int) secondary.get(ScrollableList.class, "Community_Size").getValue();
			UserSettings.getInstance().setCommunitySizeAtt(secondary.get(ScrollableList.class, "Community_Size")
					.getItem(communitySize).get("name").toString());
			break;

		case "Community_Color":
			int communityColor = (int) secondary.get(ScrollableList.class, "Community_Color").getValue();
			UserSettings.getInstance().setCommunityColorAtt(secondary.get(ScrollableList.class, "Community_Color")
					.getItem(communityColor).get("name").toString());
			break;

		case "Converter_Community":
			int valueCC = (int) secondary.get(ScrollableList.class, "Converter_Community").getValue();
			UserSettings.getInstance().setCommunityConverter(
					secondary.get(ScrollableList.class, "Converter_Community").getItem(valueCC).get("name").toString());
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

		case "Export_PDF":
			if (accordion != null) {
				String selectedPathExport = ChooseHelper.getInstance().showJFileChooser(true, "pdf");
				if (selectedPathExport != null) {
					UserSettings.getInstance().setFileExportName(selectedPathExport);
				} else {
					javax.swing.JOptionPane.showMessageDialog(null, "No file name entered", "",
							javax.swing.JOptionPane.INFORMATION_MESSAGE);
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

	public void setKeyNamesForNodes(ArrayList<String> keyNames) {
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

	public Accordion getAccordion() {
		return accordion;
	}
}
