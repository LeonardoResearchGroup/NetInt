package executable;

import java.io.File;

import containers.Container;
import processing.core.*;
import utilities.Assembler;
import utilities.GraphLoader;
import utilities.GraphmlKeyReader;
import utilities.TestPerformance;
import utilities.mapping.Mapper;
import visualElements.Canvas;

import visualElements.gui.ControlPanel;
import visualElements.gui.ImportMenu;
import visualElements.gui.VisibilitySettings;

public class Executable extends PApplet {
	public static Assembler app;
	private Canvas canvas;
	private TestPerformance performance;
	public static boolean activeGraph;
	private ControlPanel cFrame;
	public static File file;
	public static ImportMenu importMenu;

	public void setup() {
		textSize(10);
		smooth();
		System.out.println("Building Canvas");
		canvas = new Canvas(this);
		System.out.println("Instantiating Import Menu");
		importMenu = new ImportMenu(this);
		System.out.println("Instantiating Network Assembler");
		app = new Assembler(Assembler.HD1080);
		performance = new TestPerformance();
		this.setActiveGraph(false);
		// Control Frame
		System.out.println("Building Control Panel");
		cFrame = new ControlPanel(this, 200, this.height - 25, "Controls");
//
//		System.out.println(this.getClass().getName()+
//				" Weight: MAX: " + Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[1]
//						+ " MIN: " + Mapper.getInstance().getMaxMin(Mapper.EDGE_WEIGHT)[0]);
//		System.out.println(this.getClass().getName()+
//				" OutDegree: MAX: " + Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[1]
//						+ " MIN: " + Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[0]);
//
		this.surface.setLocation(200, 0);
		this.surface.setTitle("Java Networked Interaction Visualization. NetInt");
	}

	public void draw() {
		if (activeGraph) {
			background(VisibilitySettings.getInstance().getColorBackground());
			pushMatrix();
			canvas.translateCenter((width - app.rootDimension.width) / 2, (height - app.rootDimension.height) / 2);
			canvas.transform();
			// canvas.originCrossHair();
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

	public Assembler getApp() {
		return app;
	}

	public boolean isActiveGraph() {
		return activeGraph;
	}

	public void setActiveGraph(boolean activeGraph) {
		Executable.activeGraph = activeGraph;
	}

	/**
	 * This method receive the graph file path and triggers an import process.
	 * The import process consists of user selection of graph keys.
	 * 
	 * @param selection
	 */

	public void selectImport(File selection) {
		if (selection != null) {
			file = selection;
			GraphmlKeyReader reader = new GraphmlKeyReader(selection);
			// this creates and displays the menu
			importMenu.makeLists(reader.getKeyNamesForNodes(), reader.getKeyNamesForEdges());
		}
	}

	public void settings() {
		size(displayWidth - 201, displayHeight - 100, P2D);
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