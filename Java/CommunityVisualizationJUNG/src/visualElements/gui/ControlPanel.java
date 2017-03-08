package visualElements.gui;

import processing.core.*;
import utilities.Assembler;
import utilities.GraphLoader;
import utilities.SerializeHelper;
import utilities.SerializeWrapper;
import utilities.mapping.Mapper;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

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
	static PApplet parent;
	private static ControlP5 main;
	private static ControlP5 secondary;
	private static CheckBox cBox;
	private static Accordion accordion;
	private PFont font;
	private PImage logo;
	// From NetInt: Java Network Interaction Visualization Library.
	private final String EXTENSION = "nti";
	// List of graphElements attribute names
	private static ArrayList<String> keyNamesForNodes = new ArrayList<String>();
	private static ArrayList<String> keyNamesForEdges = new ArrayList<String>();
	// Groups
	// private static Group nodeKeys;

	public ControlPanel(PApplet _parent, int _w, int _h, String _name) {
		super();
		parent = _parent;
		w = _w;
		h = _h;
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}

	public void setup() {
		this.surface.setSize(w, h);
		this.surface.setLocation(0, 45);
		this.surface.setAlwaysOnTop(false);
		logo = loadImage("../data/images/Logo_Bancolombia.png");
		keyNamesForNodes.add("empty list");
		keyNamesForEdges.add("empty list");
		init();
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
	public void init() {
		main = new ControlP5(this);
		secondary = new ControlP5(this);
		secondary.hide();

		String[] fileFunctions = { "Open", "Save", "Import", "Export", "Quit" };
		main.addScrollableList("File").setPosition(10, 55).setSize(180, 110).setBarHeight(18).setItemHeight(18)
				.addItems(fileFunctions).setType(ScrollableList.LIST).open();

		// create a new accordion. Add g1, g2, and g3 to the accordion.
		// accordion = main.addAccordion("acc").setPosition(10,
		// 55).setWidth(180).addItem(g1);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		// accordion.setCollapseMode(Accordion.MULTI);

		// open close sections
		// accordion.open(0);

	}

	public static void initGroups(ArrayList<String> nodeKeyNames, ArrayList<String> edgeKeyNames) {

		setKeyNamesForNodes(nodeKeyNames);
		setKeyNamesForEdges(edgeKeyNames);

		// Group instantiation
		ControllerGroup<Group> backgGroup = new Group(secondary, "Background");
		ControllerGroup<Group> nodesGroup = new Group(secondary, "Node");
		ControllerGroup<Group> edgesGroup = new Group(secondary, "Edge");
		ControllerGroup<Group> statsGroup = new Group(secondary, "Stats");

		// Group visual attributes

		// nodesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		// edgesGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);
		// statsGroup.setBackgroundColor(color.getRGB()).setBackgroundHeight(150);

		// Add Components to each group
		//synchronized(secondary){
		setBackgroundComponents(backgGroup);
		setNodeComponents(nodesGroup);
		//}

		// cBox = new CheckBox(secondary, "Stats nodes");
		// cBox.setPosition(5, 7).moveTo(statsGroup);

		
		// setVinculosComponent(g3);
		// setEstadisticasDescriptivasComponent();

		// accordion = secondary.addAccordion("acc").setPosition(10,
		// 175).setWidth(180);
		//
		// // create a new accordion. Add g1, g2, and g3 to the accordion.
		// accordion.addItem(g1).addItem(g2).addItem(g3).addItem(nodeKeys);
		//
		// // use Accordion.MULTI to allow multiple group to be open at a time.
		// accordion.setCollapseMode(Accordion.MULTI);
		//
		// // open close sections
		// accordion.open(0, 1, 2, 3);
		secondary.show();

	}

	/**
	 * GUI component related to Color background
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private static void setBackgroundComponents(ControllerGroup<Group> group) {
		// Group visual attributes
		Color color = new Color(0, 0, 0, 64);
		group.setHeight(30);
		group.setColorBackground(color.getRGB());

		// Add gui elements
		Slider luminosity = new Slider(secondary, group, "Luminosity", 0f, 255f, 70f, 5, 10, 165, 18);
	}

	/**
	 * GUI component related to Node Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private static void setNodeComponents(ControllerGroup<Group> group) {
		// Group visual attributes
		Color color = new Color(0, 0, 0, 64);
		group.setHeight(30);
		group.setColorBackground(color.getRGB());

//		// Visibility control
		//Tab tab= new Tab(secondary,secondary.getWindow() ) // , ControlWindow theControlWindow, java.lang.String theName
//		Toggle nodes = new Toggle(secondary, secondary.getTab("secondary"), "On/Off",0f,5f,5f,45,10);
//		
		secondary.addToggle("Nodos").setPosition(5, 5).setSize(45, 10).setValue(true).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);
//
//		// Name Visibility control
//		secondary.addToggle("Nombre").setPosition(60, 5).setSize(45, 10).setValue(true).moveTo(group).getCaptionLabel()
			//	.align(ControlP5.CENTER, ControlP5.CENTER);
//
//		// Node search
//		secondary.addTextfield("Buscar ID Nodo").setPosition(5, 20).setSize(68, 15).setAutoClear(false).moveTo(group)
//				.getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER).setPaddingX(35);
//
//		// Clear node search
//		secondary.addBang("Clear").setPosition(77, 20).setSize(28, 15).moveTo(group).getCaptionLabel()
//				.align(ControlP5.CENTER, ControlP5.CENTER);
//
//		secondary.addSlider("Min OutDegree").setPosition(5, 40).setSize(100, 10).setRange(0, 35)
//				.setNumberOfTickMarks(36).snapToTickMarks(true).moveTo(group);
//		// Diameter
//		Object[] mappers = Mapper.getInstance().getAttributesMax().getAttributeKeys("Node").toArray();
//		String[] items = new String[mappers.length];
//		for (int i = 0; i < mappers.length; i++) {
//			items[i] = (String) mappers[i];
//		}
//
//		secondary.addScrollableList("Diametro").addItems(items).setPosition(5, 53).setSize(100, 100).setBarHeight(13).setItemHeight(13).setType(ScrollableList.DROPDOWN).moveTo(group).close();
	}

	/**
	 * GUI component related to Edge Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private static void setVinculosComponent(Group group) {

		// Control de visibilidad
		secondary.addToggle("Internos").setPosition(5, 7).setSize(45, 10).setValue(true).moveTo(group);
		secondary.getController("Internos").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		secondary.addToggle("Externos").setPosition(60, 7).setSize(45, 10).setValue(true).moveTo(group);
		secondary.getController("Externos").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// Vol. Transaccion
		secondary.addSlider("Vol. Transaccion").setPosition(5, 20).setSize(100, 10).setRange(0, 1).moveTo(group);

		// Propagacion
		secondary.addSlider("Propagacion").setPosition(5, 33).setSize(68, 10).setRange(1, 10).setNumberOfTickMarks(10)
				.snapToTickMarks(true).moveTo(group);
		secondary.getController("Propagacion").getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
				.setPaddingX(35);
		secondary.addToggle("Solo").setPosition(77, 33).setSize(28, 10).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);

		// Espesor Vinculo
		Object[] mappers = Mapper.getInstance().getAttributesMax().getAttributeKeys("Edge").toArray();
		String[] items = new String[mappers.length];
		for (int i = 0; i < mappers.length; i++) {
			items[i] = (String) mappers[i];
		}
		secondary.addScrollableList("Espesor").setPosition(5, 46).setSize(100, 100).setBarHeight(13).setItemHeight(13)
				.addItems(items).setType(ScrollableList.DROPDOWN).moveTo(group).close();
	}

	/**
	 * GUI component related to descriptive statistics of clients
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private static void setEstadisticasDescriptivasComponent() {
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
		case "Lumiosidad_Fondo":
			UserSettings.getInstance().setColorBackground((int) theEvent.getController().getValue());
			// System.out.println(theEvent.getController().getValue());
			break;

		// **** NODES ****
		case "Buscar ID Nodo":
			UserSettings.getInstance().setIdBuscador(theEvent.getStringValue());
			break;
		case "Clear":
			secondary.get(Textfield.class, "Buscar ID Nodo").clear();
			UserSettings.getInstance().resetIdBuscador();
			break;
		case "Min OutDegree":
			UserSettings.getInstance().setUmbralGrados(theEvent.getValue());
			break;
		case "Nodos":
			Toggle nodo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarNodos(nodo.getBooleanValue());
			break;
		case "Nombre":
			Toggle nombre = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarNombre(nombre.getBooleanValue());
			break;
		case "Diametro":
			int valueD = (int) secondary.get(ScrollableList.class, "Diametro").getValue();
			UserSettings.getInstance().setFiltrosNodo(
					secondary.get(ScrollableList.class, "Diametro").getItem(valueD).get("name").toString());
			break;

		// **** EDGES ****
		case "Internos":
			Toggle vinculoInt = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarVinculosInt(vinculoInt.getBooleanValue());
			break;
		case "Externos":
			Toggle vinculoExt = (Toggle) theEvent.getController();
			UserSettings.getInstance().setMostrarVinculosExt(vinculoExt.getBooleanValue());
			break;
		case "Vol. Transaccion":
			UserSettings.getInstance().setVolTransaccion(theEvent.getValue());
			break;
		case "Propagacion":
			UserSettings.getInstance().setPropagacion(theEvent.getValue());
			break;
		case "Solo":
			Toggle solo = (Toggle) theEvent.getController();
			UserSettings.getInstance().setSoloPropagacion(solo.getBooleanValue());
			break;
		case "Espesor":
			int valueE = (int) secondary.get(ScrollableList.class, "Espesor").getValue();
			UserSettings.getInstance().setFiltrosNodo(
					secondary.get(ScrollableList.class, "Espesor").getItem(valueE).get("name").toString());
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

	private static void setKeyNamesForNodes(ArrayList<String> keyNames) {
		keyNamesForNodes = keyNames;
	}

	private static void setKeyNamesForEdges(ArrayList<String> keyNames) {
		keyNamesForEdges = keyNames;
	}
}
