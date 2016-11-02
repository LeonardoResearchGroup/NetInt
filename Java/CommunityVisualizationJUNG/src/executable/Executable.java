package executable;

import java.io.File;

import containers.Container;
import processing.core.*;
import utilities.GraphLoader;
import utilities.TestPerformance;
import utilities.mapping.Mapper;
import visualElements.Canvas;
import visualElements.VCommunity;
import visualElements.gui.ChooseHelper;

import visualElements.gui.ControlPanel;
import visualElements.gui.VisibilitySettings;
import controlP5.ControlEvent;

public class Executable extends PApplet {
	public static Logica app;
	private Canvas canvas;
	private TestPerformance performance;
	// GUIHelper guiHelper;
	public static boolean activeGraph;
	private ControlPanel cFrame;
	
	public static final int CURSOR_ARROW = ARROW;
	public static final int CURSOR_WAIT = WAIT;
	public static int activeCursor = CURSOR_ARROW;
	

	public void setup() {
		textSize(10);
		// guiHelper = new GUIHelper(this);
		// guiHelper.loadGUI();
		smooth();
		canvas = new Canvas(this);
		app = new Logica();
		performance = new TestPerformance();
		 app.loadGraph(new File("./data/graphs/Risk.graphml"), "Continent", "label", "sector", "weight",
		 Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
//		app.loadGraph(new File("./data/graphs/comunidadesNodosEstadosFinancieros.graphml"), "comunidad", "name", "void sector",
//				"VALORES_MOVILIZADOS", Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
//		app.loadGraph(new File("./data/graphs/comunidadesNodosEstadosFinancieros.net"), "comunidad", "name", "void sector",
//				"VALORES_MOVILIZADOS", Container.FRUCHTERMAN_REINGOLD, GraphLoader.PAJEK);
		// app.loadGraph(new File("./data/graphs/comunidadesEafit.graphml"),
		// "comunidad", "name", "void sector", "VALORES_MOVILIZADOS",
		// Container.FRUCHTERMAN_REINGOLD);

		this.setActiveGraph(true);
		// Control Frame
		cFrame = new ControlPanel(this, 200, this.height - 25, "Controls");
//		surface.setLocation(0, 0);

		System.out.println(
				"Executable > setup Mapper weight: MAX: " + Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[1]
						+ " MIN: " + Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[0]);
		System.out.println(
				"Executable > setup Mapper outDegree: MAX: " + Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[1]
						+ " MIN: " + Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[0]);

//		this.surface.setLocation(150, 0);
//		this.surface.setTitle("PropaGraph");
	}

	public void draw() {
		
		//Set the current cursor.
		cursor(activeCursor);
		
		if (activeGraph) {
			background(VisibilitySettings.getInstance().getColorBackground());
			pushMatrix();
			canvas.translateCenter(width / 2, height / 2);
			canvas.transform();
			// canvas.originCrossHair();
			app.show();
			popMatrix();
			//
			canvas.showLegend(new PVector(width - 20, 20));
			canvas.displayValues(new PVector(width - 20, 40));
			canvas.showControlPanelMessages(new PVector(20, 20));
			performance.displayValues(canvas, new PVector(width - 20, height - 60));
			// this.noLoop();
		}
		// Signature Message :)
		textAlign(PConstants.LEFT);
		text("Built with Processing 3 | I2T & Leonardo Research Group, U. Icesi", 20, height - 10);
		// Sets any event on the canvas to false. MUST be at the end of draw()
		Canvas.setEventOnCanvas(false);
		VisibilitySettings.getInstance().setEventOnVSettings(false);
	}

	public Logica getApp() {
		return app;
	}

	public boolean isActiveGraph() {
		return activeGraph;
	}

	public void setActiveGraph(boolean activeGraph) {
		this.activeGraph = activeGraph;
	}

	public void selectImport(File selection) {
		if (selection != null) {
			ChooseHelper.getInstance().processImport(selection, this);
		}
	}

	public void settings() {
		// size(995, 600, P2D);
		size(displayWidth - 201, displayHeight - 150, P2D);
	}

	public static void main(String[] args) {
		String[] appletArgs = new String[] { "executable.Executable" };
		if (args != null) {
			PApplet.main(concat(appletArgs, args));
		} else {
			PApplet.main(appletArgs);
		}
	}
}