package visualElements.gui;

import processing.core.*;
import utilities.mapping.Mapper;
import controlP5.*;
import executable.Executable;
import visualElements.gui.VisibilitySettings;

/**
 * @author jsalam Example copied from
 *         https://github.com/sojamo/controlp5/issues/17
 *
 */
public class ControlPanel extends PApplet {
	int w, h;
	PApplet parent;
	ControlP5 cp5;
	ColorPicker cp;
	Accordion accordion;
	PFont font;

	public ControlPanel(PApplet _parent, int _w, int _h, String _name) {
		super();
		parent = _parent;
		w = _w;
		h = _h;
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}

	public void setup() {
		this.surface.setSize(w, h);
		this.surface.setLocation(180, 45);
		this.surface.setAlwaysOnTop(true);
		gui();
		// Font
		font = createFont("Arial", 11, false);
		textFont(font);
	}

	/**
	 * Main GUI method that assembles all the GUI components
	 */
	public void gui() {
		cp5 = new ControlP5(this);
		Group g1 = cp5.addGroup("Archivo").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		Group g2 = cp5.addGroup("Fondo").setBackgroundColor(color(0, 64)).setBackgroundHeight(20)
				.setBackgroundColor(parent.color(39, 67, 110));
		Group g3 = cp5.addGroup("Nodos / Clientes").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		Group g4 = cp5.addGroup("Vinculos / Transacciones").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		Group g5 = cp5.addGroup("Riesgo / Rentabilidad").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));

		guiArchivo(g1);
		guiBackground(g2);
		guiNodos(g3);
		guiVinculos(g4);
		guiRiesgo(g5);

		// create a new accordion. Add g1, g2, and g3 to the accordion.
		accordion = cp5.addAccordion("acc").setPosition(10, 15).setWidth(180).addItem(g1).addItem(g2).addItem(g3)
				.addItem(g4).addItem(g5);

		// open close sections
		accordion.open(0, 2, 3);

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
		cp = cp5.addColorPicker("Color Selector").plugTo(parent).setPosition(5, 10).setColorValue(color(200, 200, 200, 255)).moveTo(group);
	}

	/**
	 * GUI component related to Node Operations
	 * 
	 * @param g2
	 *            The Group of GUI elements
	 */
	private void guiNodos(Group group) {
		// Control de visibilidad
		cp5.addButton("Nodo").setPosition(5, 5).setSize(45, 10).moveTo(group);
		cp5.addButton("Nombre").setPosition(60, 5).setSize(45, 10).moveTo(group);
		// Buscador por ID de nodo
		cp5.addTextfield("Buscar ID Nodo").setPosition(5, 20).setSize(68, 15).setAutoClear(false).moveTo(group);
		cp5.getController("Buscar ID Nodo").getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
				.setPaddingX(35);
		cp5.addBang("Clear").setPosition(77, 20).setSize(28, 15).moveTo(group).getCaptionLabel().align(ControlP5.CENTER,
				ControlP5.CENTER);
		cp5.addSlider("Min OutDegree").setPosition(5, 40).setSize(100, 10).setRange(0, 35).setNumberOfTickMarks(36)
				.snapToTickMarks(true).moveTo(group);
		//Diametro Nodo
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
		// Vol. Transaccion
		cp5.addSlider("Vol. Transaccion").setPosition(5, 7).setSize(100, 10)
				.setRange(0, Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[1]).moveTo(group);
		//Propagacion
		cp5.addSlider("Propagacion").setPosition(5, 20).setSize(68, 10).setRange(1, 10).setNumberOfTickMarks(10)
				.snapToTickMarks(true).moveTo(group);
		cp5.getController("Propagacion").getCaptionLabel().align(ControlP5.RIGHT_OUTSIDE, ControlP5.CENTER)
		.setPaddingX(35);
		cp5.addToggle("Solo").setPosition(77, 20).setSize(28, 10).moveTo(group).getCaptionLabel().align(ControlP5.CENTER,
				ControlP5.CENTER);
		//Espesor Vinculo
		String[] mappers = { "Lineal", "Logartimico", "Sinusoidal", "Radial", "Sigmoideo" };
		cp5.addScrollableList("Espesor Vinculo").setPosition(5, 33).setSize(100, 100).setBarHeight(13).setItemHeight(13)
				.addItems(mappers).setType(ScrollableList.DROPDOWN).moveTo(group).close();
	}

	/**
	 * GUI component related to Risk and profit Operations
	 * 
	 * @param g4
	 *            The Group of GUI elements
	 */
	private void guiRiesgo(Group group) {

	}

	public void draw() {
		background(70);
		// This line updates the controller position. It can be controlled by
		// the event controller for performance improvement.
		cp5.getGroup("Archivo").getController("Salir").setPosition(5,
				69 + cp5.getGroup("Archivo").getController("Exportar").getHeight());

	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isGroup()) {
			// **** BACKGROUND ****
			if (theEvent.isFrom(cp)) {
				VisibilitySettings.getInstance().setColorBackground(cp.getColorValue());
			}
		} else {
			System.out.println("ControlPanel> Event at: " + theEvent.getController().getName());
			switch (theEvent.getController().getName()) {
			case "Importar":
				ChooseHelper.getInstance().showFileChooser(false, "graphml", parent);
				break;

			// **** NODES ****
			case "Buscar ID Nodo":
				VisibilitySettings.getInstance().setIdBuscador(theEvent.getStringValue());
				break;
			case "Clear":
				cp5.get(Textfield.class, "Buscar ID Nodo").clear();
				VisibilitySettings.getInstance().resetIdBuscador();
				break;
			case "Min OutDegree":
				VisibilitySettings.getInstance().setUmbralGrados(theEvent.getValue());
				break;
			case "Nombre":
				Button b = (Button) theEvent.getController();
				VisibilitySettings.getInstance().setMostrarNombre(b.getBooleanValue());
				break;
			case "Diametro Nodo":
				VisibilitySettings.getInstance().setFiltrosNodo(theEvent.getController().getLabel());
				break;

			// **** EDGES ****
			case "Vol. Transaccion":
				VisibilitySettings.getInstance().setVolTransaccion(theEvent.getValue());
				break;
			case "Propagacion":
				VisibilitySettings.getInstance().setPropagacion(theEvent.getValue());
				break;
			case "Solo":
				Toggle solo = (Toggle) theEvent.getController();
				VisibilitySettings.getInstance().setSoloPropagacion(solo.getBooleanValue());
				break;
			case "Espesor Vinculo":
				VisibilitySettings.getInstance().setFiltrosVinculo(theEvent.getController().getLabel());
				break;

			default:
				// Executable.retrieveControlPanelEvent(theEvent);
				break;
			}
		}
	}
}
