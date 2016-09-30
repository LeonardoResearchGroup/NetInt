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

	public void gui() {
		cp5 = new ControlP5(this);

		// Archivo
		Group g1 = cp5.addGroup("Archivo").setBackgroundColor(color(0, 64)).setBackgroundHeight(150);
		cp5.addButton("Abrir").plugTo(parent).setPosition(5, 7).setSize(170, 18).moveTo(g1);
		cp5.addButton("Guardar").plugTo(parent).setPosition(5, 27).setSize(170, 18).moveTo(g1);
		cp5.addButton("Importar").plugTo(parent).setPosition(5, 47).setSize(170, 18).moveTo(g1);
		String[] formatos = { "PNG", "PDF", "JPEG" };
		cp5.addScrollableList("Exportar").setPosition(5, 67).setSize(170, 40).setBarHeight(13).setItemHeight(13)
				.addItems(formatos).setType(ScrollableList.LIST).moveTo(g1).close();

		cp5.addButton("Salir").plugTo(parent)
				.setPosition(5, 67 + cp5.getGroup("Archivo").getController("Exportar").getHeight()).setSize(170, 18)
				.moveTo(g1);

		// Nodos / Clientes
		Group g2 = cp5.addGroup("Nodos / Clientes").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		cp5.addSlider("Umbral OutDegree").setPosition(5, 7).setSize(100, 10).setRange(0, 35).moveTo(g2);
		cp5.addButton("Mostrar Nombre").setPosition(5, 20).setSize(100, 10).moveTo(g2);
		String[] mappers = { "Lineal", "Logartimico", "Sinusoidal", "Radial", "Sigmoideo" };
		cp5.addScrollableList("Filtros Nodo").setPosition(5, 33).setSize(100, 100).setBarHeight(13).setItemHeight(13)
				.addItems(mappers).setType(ScrollableList.DROPDOWN).moveTo(g2).close();

		// Vinculos / Transacciones
		Group g3 = cp5.addGroup("Vinculos / Transacciones").setBackgroundColor(color(0, 64)).setBackgroundHeight(150)
				.setBackgroundColor(parent.color(39, 67, 110));
		cp5.addSlider("Vol. Transaccion").setPosition(5, 7).setSize(100, 10)
				.setRange(0,
						Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[1])
				.moveTo(g3);

		cp5.addSlider("Propagacion").setPosition(5, 20).setSize(100, 10).setRange(1, 10).moveTo(g3);
		cp5.addScrollableList("Filtros Vinculo").setPosition(5, 33).setSize(100, 100).setBarHeight(13).setItemHeight(13)
				.addItems(mappers).setType(ScrollableList.DROPDOWN).moveTo(g3).close();

		// Riesgo y Rentabilidad
		Group g4 = cp5.addGroup("Riesgo / Rentabilidad").setBackgroundColor(color(0, 64)).setBackgroundHeight(150);

		// create a new accordion. Add g1, g2, and g3 to the accordion.
		accordion = cp5.addAccordion("acc").setPosition(10, 15).setWidth(180).addItem(g1).addItem(g2).addItem(g3)
				.addItem(g4);

		// open close sections
		accordion.open(0, 1, 2);

		// use Accordion.MULTI to allow multiple group to be open at a time.
		accordion.setCollapseMode(Accordion.MULTI);
	}

	public void draw() {
		background(70);
		// This line updates the controller position. It can be controlled by
		// the event controller for performance improvement.
		cp5.getGroup("Archivo").getController("Salir").setPosition(5,
				69 + cp5.getGroup("Archivo").getController("Exportar").getHeight());

	}

	public void controlEvent(ControlEvent theEvent) {
		System.out.println("ControlPanel> Event at: " + theEvent.getController().getName());
		if (theEvent.isGroup()) {

		} else {
			switch (theEvent.getController().getName()) {
			case "Importar":
				ChooseHelper.getInstance().showFileChooser(false, "graphml", parent);
				break;

			// **** NODES ****
			case "Umbral OutDegree":
				VisibilitySettings.getInstance().setUmbralGrados(theEvent.getValue());
				break;
			case "Mostrar Nombre":
				Button b = (Button) theEvent.getController();
				VisibilitySettings.getInstance().setMostrarNombre(b.getBooleanValue());
				break;
			case "Filtros Nodo":
				VisibilitySettings.getInstance().setFiltrosNodo(theEvent.getController().getLabel());
				break;

			// **** EDGES ****
			case "Vol. Transaccion":
				VisibilitySettings.getInstance().setVolTransaccion(theEvent.getValue());
				break;
			case "Propagacion":
				VisibilitySettings.getInstance().setPropagacion(theEvent.getValue());
				break;
			case "Filtros Vinculo":
				VisibilitySettings.getInstance().setFiltrosVinculo(theEvent.getController().getLabel());
				break;

			default:
				// Executable.retrieveControlPanelEvent(theEvent);
				break;
			}
		}
	}
}