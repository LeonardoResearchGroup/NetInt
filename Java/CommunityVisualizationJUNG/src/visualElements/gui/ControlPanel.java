package visualElements.gui;

import processing.core.*;
import utilities.Assembler;
import utilities.GraphLoader;
import utilities.SerializeHelper;
import utilities.SerializeWrapper;
import utilities.filters.Filters;
import utilities.mapping.Mapper;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import controlP5.*;
import executable.Executable;
import visualElements.gui.UserSettings;

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
	private CheckBox cBox;
	private Accordion accordion;
	private PFont font;
	private PImage logo;
	// From NetInt: Java Network Interaction Visualization Library.
	private final String EXTENSION = "nti";
	// List of graphElements attribute names
	private ArrayList<String> keyNamesForNodes = new ArrayList<String>();
	private ArrayList<String> keyNamesForEdges = new ArrayList<String>();
	// Groups
	// private static Group nodeKeys;

	private static ControlPanel CPInstance = null;

	public static ControlPanel getInstance() throws NullPointerException {
		if (CPInstance == null) {
			return null;
		}
		return CPInstance;
	}

	/**
	 * This constructor is used to create a control panel for user selection of visualization parameters
	 * @param _parent
	 * @param _w
	 * @param _h
	 */
	public ControlPanel(PApplet _parent, int _w, int _h) {
		super();
		parent = _parent;
		w = _w;
		h = _h;
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
		CPInstance = this;
	}



	public void setup() {
		this.surface.setSize(w, h);
		this.surface.setLocation(0, 45);
		this.surface.setAlwaysOnTop(false);
		logo = loadImage("../data/images/Logo_Bancolombia.png");
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

		String[] fileFunctions = { "Open", "Save", "Import", "Export", "Quit" };
		main.addScrollableList("File").setPosition(10, 55).setSize(180, 85).setBarHeight(18).setItemHeight(13)
				.addItems(fileFunctions).setType(ScrollableList.LIST).open();
	}

	public void initGroups(ArrayList<String> nodeKeyNames, ArrayList<String> edgeKeyNames) {

		setKeyNamesForNodes(nodeKeyNames);
		setKeyNamesForEdges(edgeKeyNames);

		// Group instantiation
		Group backgGroup = new Group(secondary, "Background");
		Group nodesGroup = new Group(secondary, "Node");
		Group edgesGroup = new Group(secondary, "Edge");
		Group statsGroup = new Group(secondary, "Stats");

		// Group visual attributes
		Color color = new Color(0, 35, 80);
		backgGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(50);
		nodesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		edgesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		statsGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		cBox = new CheckBox(secondary, "Stats nodes");
		cBox.setPosition(5, 7).moveTo(statsGroup);

		// Add Components to each group
		setBackgroundComponents(backgGroup);
		setNodeComponents(nodesGroup);
		setEdgeComponents(edgesGroup);
		setEstadisticasDescriptivasComponent();

		// Accordion GUI
		accordion = secondary.addAccordion("acc").setPosition(10, 145).setWidth(180);

		// create a new accordion. Add g1, g2, and g3 to the accordion.
		accordion.addItem(backgGroup).addItem(nodesGroup).addItem(edgesGroup).addItem(statsGroup);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordion.setCollapseMode(Accordion.MULTI);

		// open close sections
		accordion.open(1, 2, 3);

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

		// Visibility control
		secondary.addToggle("On/Off").setPosition(5, 5).setSize(45, 10).setValue(true).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Name Visibility control
		secondary.addToggle("Name").setPosition(60, 5).setSize(45, 10).setValue(true).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Node search
		secondary.addTextfield("Search ID").setPosition(5, 20).setSize(68, 15).setAutoClear(false).moveTo(group)
				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setPaddingX(35);

		// Clear node search
		secondary.addBang("Clear").setPosition(77, 20).setSize(28, 15).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		secondary.addSlider("Min OutDegree").setPosition(5, 40).setSize(100, 10).setRange(0, 35)
				.setNumberOfTickMarks(36).snapToTickMarks(true).moveTo(group);

		// Diameter
		Object[] mappers = Mapper.getInstance().getNodeAttributesMax().getAttributeKeys().toArray();
		String[] items = new String[mappers.length];
		for (int i = 0; i < mappers.length; i++) {
			items[i] = (String) mappers[i];
		}

		secondary.addScrollableList("Diameter").addItems(items).setPosition(5, 53).setSize(100, 100).setBarHeight(13)
				.setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group).close();
	}

	/**
	 * GUI component related to Edge Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void setEdgeComponents(Group group) {

		// Visibility control
		secondary.addToggle("Internal").setPosition(5, 7).setSize(45, 10).setValue(true).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		secondary.addToggle("External").setPosition(60, 7).setSize(45, 10).setValue(true).moveTo(group)
				.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// Transaction volume
		secondary.addSlider("Volume").setPosition(5, 20).setSize(100, 10).setRange(0, 1).moveTo(group);

		// Propagation
		secondary.addSlider("Succesors").setPosition(5, 33).setSize(68, 10).setRange(1, 10).setNumberOfTickMarks(10)
				.snapToTickMarks(true).moveTo(group).getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
				.setPaddingX(35);

		// Visualize only propagation
		secondary.addToggle("Only").setPosition(77, 33).setSize(28, 10).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Thickness
		Object[] mappers = Mapper.getInstance().getEdgeAttributesMax().getAttributeKeys().toArray();
		String[] items = new String[mappers.length];
		for (int i = 0; i < mappers.length; i++) {
			items[i] = (String) mappers[i];
		}
		secondary.addScrollableList("Thickness").setPosition(5, 46).setSize(100, 100).setBarHeight(13).setItemHeight(13)
				.addItems(items).setType(ScrollableList.DROPDOWN).moveTo(group).close();
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

	public void draw() {
		background(70);
		// logo
		fill(170);
		rect(10, 2, 180, 48);
		image(logo, 15, 5);
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
		System.out.println("ControlPanel> Event at: " + controllerName);
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
			UserSettings.getInstance().setIdBuscador(theEvent.getStringValue());
			break;
		case "Clear":
			secondary.get(Textfield.class, "Search ID").clear();
			UserSettings.getInstance().resetIdBuscador();
			break;
		case "Min OutDegree":
			UserSettings.getInstance().setUmbralGrados(theEvent.getValue());
			break;
		case "On/Off":
			Toggle nodo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarNodos(nodo.getBooleanValue());
			break;
		case "Name":
			Toggle nombre = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarNombre(nombre.getBooleanValue());
			break;
		case "Diameter":
			int valueD = (int) secondary.get(ScrollableList.class, "Diameter").getValue();
			UserSettings.getInstance().setFiltrosNodo(
					secondary.get(ScrollableList.class, "Diameter").getItem(valueD).get("name").toString());
			break;

		// **** EDGES ****
		case "Internal":
			Toggle vinculoInt = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarVinculosInt(vinculoInt.getBooleanValue());
			break;
		case "External":
			Toggle vinculoExt = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarVinculosExt(vinculoExt.getBooleanValue());
			break;
		case "Volume":
			UserSettings.getInstance().setVolTransaccion(theEvent.getValue());
			break;
		case "Succesors":
			UserSettings.getInstance().setPropagacion(theEvent.getValue());
			break;
		case "Only":
			Toggle solo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setSoloPropagacion(solo.getBooleanValue());
			break;
		case "Thickness":
			int valueE = (int) secondary.get(ScrollableList.class, "Thickness").getValue();
			
			UserSettings.getInstance().setFiltrosVinculo(
					secondary.get(ScrollableList.class, "Thickness").getItem(valueE).get("name").toString());
			break;
		default:
			// Executable.retrieveControlPanelEvent(theEvent);
			break;

		}
	}

	private void manageFileSelection(String choice) {
		switch (choice) {
		case "Open":
			String selectedFile = ChooseHelper.getInstance().showJFileChooser(false, EXTENSION);

			try {
				parent.cursor(WAIT);
				// Executable.activeCursor = Executable.CURSOR_WAIT;

				SerializeWrapper deserializedWrapper = SerializeHelper.getInstance().deserialize(selectedFile);

				Executable.setActiveGraph(false);
				Assembler.secondOrderVComm = deserializedWrapper.getvSubCommunities();
				for (visualElements.VCommunity com : Assembler.secondOrderVComm) {
					com.eventRegister(parent);
				}
				Assembler.firstOrderVComm = deserializedWrapper.getvSubSubCommunity();
				Assembler.firstOrderVComm.eventRegister(parent);
				Assembler.firstOrderVComm.container.runVEdgeFactory();
				UserSettings.reloadInstance(deserializedWrapper.getvSettings());
				GraphLoader.theGraph = deserializedWrapper.getTheGraph();
				Executable.setActiveGraph(true);
				javax.swing.JOptionPane.showMessageDialog(null, "Finalizado.", "",
						javax.swing.JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException e1) {

				javax.swing.JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			} finally {
				parent.cursor(ARROW);
				// Executable.activeCursor = Executable.CURSOR_ARROW;
			}

			break;

		case "Save":

			String selectedPath = ChooseHelper.getInstance().showJFileChooser(true, EXTENSION);

			if (selectedPath != null) {

				// Executable.activeCursor = Executable.CURSOR_WAIT;
				parent.cursor(WAIT);

				SerializeWrapper wrapper = new SerializeWrapper(Assembler.firstOrderVComm, Assembler.secondOrderVComm,
						UserSettings.getInstance(), GraphLoader.theGraph);

				try {
					SerializeHelper.getInstance().serialize(wrapper, selectedPath, EXTENSION);
					javax.swing.JOptionPane.showMessageDialog(null,
							"Archivo guardado en " + selectedPath + "." + EXTENSION, "",
							javax.swing.JOptionPane.INFORMATION_MESSAGE);
				}

				catch (FileNotFoundException e) {
					javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
							javax.swing.JOptionPane.ERROR_MESSAGE);
				}

				finally {
					// Executable.activeCursor = Executable.CURSOR_ARROW;
					parent.cursor(ARROW);
				}

			}
			break;

		case "Import":
			ChooseHelper.getInstance().showFileChooser(parent);
			break;

		case "Export":
			String selectedPathExport = ChooseHelper.getInstance().showJFileChooser(true, EXTENSION);
			if (selectedPathExport != null) {
				// Executable.activeCursor = Executable.CURSOR_WAIT;
				// parent.cursor(WAIT);
				// SerializeWrapper wrapper = new
				// SerializeWrapper(Assembler.firstOrderVComm,
				// Assembler.secondOrderVComm,
				// UserSettings.getInstance(), GraphLoader.theGraph);
				// try {
				// SerializeHelper.getInstance().serialize(wrapper,
				// selectedPath, EXTENSION);
				// javax.swing.JOptionPane.showMessageDialog(null, "File
				// exported to " + "path" + "." + EXTENSION, "",
				// javax.swing.JOptionPane.INFORMATION_MESSAGE);
				// }
				// catch (FileNotFoundException e) {
				// javax.swing.JOptionPane.showMessageDialog(null,
				// e.getMessage(), "Error",
				// javax.swing.JOptionPane.ERROR_MESSAGE);
				// }
				// finally {
				// Executable.activeCursor = Executable.CURSOR_ARROW;
				// parent.cursor(ARROW);
				// }
			}
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
}
