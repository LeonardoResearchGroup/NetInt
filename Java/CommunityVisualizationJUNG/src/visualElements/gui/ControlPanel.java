package visualElements.gui;

import processing.core.*;
import utilities.Assembler;
import utilities.SerializeHelper;
import utilities.SerializeWrapper;
import utilities.mapping.Mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import controlP5.*;
import executable.Executable;
import graphElements.Edge;
import visualElements.gui.UserSettings;

/**
 * @author jsalam Example adapted from
 *         https://github.com/sojamo/controlp5/issues/17
 *
 */
public class ControlPanel extends PApplet {
	int w, h;
	PApplet parent;
	ControlP5 cp5;
	ColorPicker cPicker;
	CheckBox cBox;
	Accordion accordion;
	PFont font;
	PImage logo;
	// From NetInt: Java Network Interaction Visualization Library.
	private final String EXTENSION = "nit";
	// List of graphElements attribute names
	ArrayList<String> keyNamesForNodes = new ArrayList<String>();
	ArrayList<String> keyNamesForEdges = new ArrayList<String>();
	// Groups
	Group nodeKeys;
	Group edgeKeys;

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
		// logo = loadImage("../data/images/Bank.png");
		gui();
		// Font
		font = createFont("Arial", 11, false);
		textFont(font);
	}
	
	public void exit() {
	  println("not exiting");
	}

	/**
	 * Main GUI method that assembles all the GUI components
	 */
	public void gui() {
		cp5 = new ControlP5(this);

		Group g1 = cp5.addGroup("Archivo").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		Group g2 = cp5.addGroup("Fondo").setBackgroundColor(color(0, 64)).setBackgroundHeight(420)
				.setBackgroundColor(parent.color(39, 67, 110));
		Group g3 = cp5.addGroup("Nodos / Clientes").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		Group g4 = cp5.addGroup("Vinculos / Transacciones").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		nodeKeys = cp5.addGroup("Estadisticas descriptivas").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));

		guiArchivo(g1);
		guiBackground(g2);
		guiNodos(g3);
		guiVinculos(g4);
		guiEstadisticasDescriptivas(nodeKeys);

		// create a new accordion. Add g1, g2, and g3 to the accordion.
		accordion = cp5.addAccordion("acc").setPosition(10, 55).setWidth(180).addItem(g1).addItem(g2).addItem(g3)
				.addItem(g4).addItem(nodeKeys);

		// open close sections
		accordion.open(0, 2, 3, 4);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordion.setCollapseMode(Accordion.MULTI);
	}

	/**
	 * GUI component related to File Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void guiArchivo(Group group) {
		cp5.addButton("Abrir").plugTo(parent).setPosition(5, 7).setSize(170, 18).moveTo(group);
		cp5.addButton("Guardar").plugTo(parent).setPosition(5, 27).setSize(170, 18).moveTo(group);
		cp5.addButton("Importar").plugTo(parent).setPosition(5, 47).setSize(170, 18).moveTo(group);
		String[] formatos = { "PNG", "PDF", "JPEG" };
		cp5.addScrollableList("Exportar").setPosition(5, 67).setSize(170, 40).setBarHeight(13).setItemHeight(13)
				.addItems(formatos).setType(ScrollableList.LIST).moveTo(group).close();
		cp5.addButton("Salir").plugTo(parent)
				.setPosition(5, 67 + cp5.getGroup("Archivo").getController("Exportar").getHeight()).setSize(170, 18)
				.moveTo(group);
	}

	/**
	 * GUI component related to File Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void guiBackground(Group group) {
		cPicker = cp5.addColorPicker("Color Selector").plugTo(parent).setPosition(5, 10).setWidth(165)
				.setColorValue(color(200, 200, 200, 255)).moveTo(group);

	}

	/**
	 * GUI component related to Node Operations
	 * 
	 * @param g2
	 *            The Group of GUI elements
	 */
	private void guiNodos(Group group) {
		// Control de visibilidad
		cp5.addToggle("Nodos").setPosition(5, 5).setSize(45, 10).setValue(true).moveTo(group);
		cp5.getController("Nodos").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		cp5.addToggle("Nombre").setPosition(60, 5).setSize(45, 10).setValue(true).moveTo(group);
		cp5.getController("Nombre").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		// Buscador por ID de nodo
		cp5.addTextfield("Buscar ID Nodo").setPosition(5, 20).setSize(68, 15).setAutoClear(false).moveTo(group);
		cp5.getController("Buscar ID Nodo").getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
				.setPaddingX(35);
		cp5.addBang("Clear").setPosition(77, 20).setSize(28, 15).moveTo(group).getCaptionLabel().align(ControlP5.CENTER,
				ControlP5.CENTER);
		cp5.addSlider("Min OutDegree").setPosition(5, 40).setSize(100, 10).setRange(0, 35).setNumberOfTickMarks(36)
				.snapToTickMarks(true).moveTo(group);
		// Diametro Nodo
		String[] mappers = { "Lineal", "Logartimico", "Sinusoidal", "Radial", "Sigmoideo" };
		cp5.addScrollableList("Diametro Nodo").setPosition(5, 53).setSize(100, 100).setBarHeight(13).setItemHeight(13)
				.addItems(mappers).setType(ScrollableList.DROPDOWN).moveTo(group).close();
	}

	/**
	 * GUI component related to Edge Operations
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void guiVinculos(Group group) {
		// Control de visibilidad
		cp5.addToggle("Internos").setPosition(5, 7).setSize(45, 10).setValue(true).moveTo(group);
		cp5.getController("Internos").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		cp5.addToggle("Externos").setPosition(60, 7).setSize(45, 10).setValue(true).moveTo(group);
		cp5.getController("Externos").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		// Vol. Transaccion
		cp5.addSlider("Vol. Transaccion").setPosition(5, 20).setSize(100, 10).setRange(0, 1).moveTo(group);
		// Propagacion
		cp5.addSlider("Propagacion").setPosition(5, 33).setSize(68, 10).setRange(1, 10).setNumberOfTickMarks(10)
				.snapToTickMarks(true).moveTo(group);
		cp5.getController("Propagacion").getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
				.setPaddingX(35);
		cp5.addToggle("Solo").setPosition(77, 33).setSize(28, 10).moveTo(group).getCaptionLabel()
				.align(ControlP5.CENTER, ControlP5.CENTER);
		// Espesor Vinculo
		String[] mappers = { "Lineal", "Logartimico", "Sinusoidal", "Radial", "Sigmoideo" };
		cp5.addScrollableList("Espesor Vinculo").setPosition(5, 46).setSize(100, 100).setBarHeight(13).setItemHeight(13)
				.addItems(mappers).setType(ScrollableList.DROPDOWN).moveTo(group).close();
	}

	/**
	 * GUI component related to descriptive statistics of clients
	 * 
	 * @param group
	 *            The Group of GUI elements
	 */
	private void guiEstadisticasDescriptivas(Group group) {
		cBox = cp5.addCheckBox("Estadisticas Nodos").setPosition(5, 7);
		for (int i = 0; i < keyNamesForNodes.size(); i++) {
			cBox.addItem(keyNamesForNodes.get(i), 1);
		}
		cBox.moveTo(group);
	}

	public void draw() {
		background(70);
		// logo
		fill(170);
		rect(10, 2, 180, 48);
		image(logo, 15, 5);
		// This line updates the controller position. It can be controlled by
		// the event controller for performance improvement.
		cp5.getGroup("Archivo").getController("Salir").setPosition(5,
				69 + cp5.getGroup("Archivo").getController("Exportar").getHeight());
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isGroup()) {
			// **** BACKGROUND ****
			if (theEvent.isFrom(cPicker)) {
				switchCaseCPicker();
			}
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

	private void switchCaseCPicker() {
		UserSettings.getInstance().setColorBackground(cPicker.getColorValue());

	}

	private void switchCaseCP5(ControlEvent theEvent) {
		// System.out.println("ControlPanel> Event at: " +
		// theEvent.getController().getName());
		switch (theEvent.getController().getName()) {

		case "Abrir":
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

		case "Guardar":

			String selectedPath = ChooseHelper.getInstance().showJFileChooser(true, EXTENSION);

			if (selectedPath != null) {

				// Executable.activeCursor = Executable.CURSOR_WAIT;
				parent.cursor(WAIT);

				SerializeWrapper wrapper = new SerializeWrapper(Assembler.firstOrderVComm, Assembler.secondOrderVComm,
						UserSettings.getInstance());

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

		case "Importar":
			ChooseHelper.getInstance().showFileChooser(false, "graphml", parent);
			break;

		case "Salir":
			System.exit(0);
			break;

		// **** NODES ****
		case "Buscar ID Nodo":
			UserSettings.getInstance().setIdBuscador(theEvent.getStringValue());
			break;
		case "Clear":
			cp5.get(Textfield.class, "Buscar ID Nodo").clear();
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
		case "Diametro Nodo":
			UserSettings.getInstance().setFiltrosNodo(theEvent.getController().getLabel());
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
		case "Espesor Vinculo":
			UserSettings.getInstance().setFiltrosVinculo(theEvent.getController().getLabel());
			break;
		default:
			// Executable.retrieveControlPanelEvent(theEvent);
			break;

		}
	}

	public void setKeyNamesForNodes(ArrayList<String> keyNames) {
		this.keyNamesForNodes = keyNames;
		guiEstadisticasDescriptivas(nodeKeys);
	}

	public void setKeyNamesForEdges(ArrayList<String> keyNames) {
		this.keyNamesForEdges = keyNames;
	}
}
