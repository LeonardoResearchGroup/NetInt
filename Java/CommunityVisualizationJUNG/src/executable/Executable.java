package executable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import containers.Container;
import controlP5.ControlEvent;
import processing.core.*;
import utilities.GraphLoader;
import utilities.GraphmlKey;
import utilities.GraphmlKeyReader;
import utilities.TestPerformance;
import utilities.mapping.Mapper;
import visualElements.Canvas;
import visualElements.VCommunity;
import visualElements.gui.ChooseHelper;

import visualElements.gui.ControlPanel;
import visualElements.gui.ImportDisplay;
import visualElements.gui.VisibilitySettings;


public class Executable extends PApplet {
	public static Logica app;
	private Canvas canvas;
	private TestPerformance performance;
	public static boolean activeGraph;
	private ControlPanel cFrame;

	public void setup() {
		textSize(10);
		smooth();
		canvas = new Canvas(this);
		app = new Logica(Logica.HD1080);
		performance = new TestPerformance();
		// app.loadGraph(new File("./data/graphs/Risk.graphml"), "Continent","label", "sector", "weight", Container.FRUCHTERMAN_REINGOLD, GraphLoader.GRAPHML);
		//// app.loadGraph(new
		// File("./data/graphs/comunidadesNodosEstadosFinancieros.graphml"),
		// "comunidad", "name", "void sector",
		//// "VALORES_MOVILIZADOS", Container.FRUCHTERMAN_REINGOLD,
		// GraphLoader.GRAPHML);
		//// app.loadGraph(new
		// File("./data/graphs/comunidadesNodosEstadosFinancieros.net"),
		// "comunidad", "name", "void sector",
		//// "VALORES_MOVILIZADOS", Container.FRUCHTERMAN_REINGOLD,
		// GraphLoader.PAJEK);
		// // app.loadGraph(new File("./data/graphs/comunidadesEafit.graphml"),
		// // "comunidad", "name", "void sector", "VALORES_MOVILIZADOS",
		// // Container.FRUCHTERMAN_REINGOLD);
		this.setActiveGraph(false);
		// Control Frame
		cFrame = new ControlPanel(this, 200, this.height - 25, "Controls");
	    surface.setLocation(0, 0);

		System.out.println(
				"Executable > setup Mapper weight: MAX: " + Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[1]
						+ " MIN: " + Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[0]);
		System.out.println(
				"Executable > setup Mapper outDegree: MAX: " + Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[1]
						+ " MIN: " + Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[0]);

		this.surface.setLocation(150, 0);
		this.surface.setTitle("Visualizador de transacciones Bancolombia");
	}

	public void draw() {

		if (activeGraph) {
			background(VisibilitySettings.getInstance().getColorBackground());
			pushMatrix();
			canvas.translateCenter((width - app.rootDimension.width) / 2, (height - app.rootDimension.height) / 2);
			canvas.transform();
			canvas.originCrossHair();
			app.show();
			popMatrix();
			canvas.showLegend(new PVector(width - 20, 20));
			canvas.displayValues(new PVector(width - 20, 40));
			canvas.showControlPanelMessages(new PVector(20, 20));
			performance.displayValues(canvas, new PVector(width - 20, height - 60));
		} else {
			background(100);
		}
		// Signature Message :)
		textAlign(PConstants.LEFT);
		text("Built with Processing 3 | Leonardo, I2T & CIENFI Research Groups, U. Icesi. 2016", 20, height - 10);
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
		Executable.activeGraph = activeGraph;
	}

	/**
	 * This method received the graph file path and triggers an import process.
	 * The import process consists of user selection of graph keys.
	 * 
	 * @param selection
	 */
	public void selectImport(File selection) {
		if (selection != null) {
			GraphmlKeyReader reader = new GraphmlKeyReader(selection);
			// this creates and displays the menu
			ImportDisplay importMenu = new ImportDisplay(this);
			importMenu.makeLists(reader.getKeyNamesForNodes(), reader.getKeyNamesForEdges());
		}
	}
	
	public void controlEvent(ControlEvent theEvent) {
		 System.out.println("Executable> Event at: " + theEvent.getController().getName());
			//switchCaseMenu(theEvent);
	}

	public void settings() {
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