package executable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import containers.Container;
import processing.core.*;
import utilities.Assembler;
import utilities.GraphmlKeyReader;
import utilities.TestPerformance;
import utilities.console.ConsoleCatcher;
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
	private ConsoleCatcher consoleCatcher;
	public static File file;
	public static ImportMenu importMenu;

	public void setup() {
		textSize(10);
		surface.setLocation(200, -300);
		smooth();
		/*
		 * Output Console. Uncomment this line to enable a console Catcher.
		 * CAUTION, it has conflicts with Menu's File Open.
		 */
		consoleCatcher = new ConsoleCatcher(initSystemOutToConsole());
		// Canvas
		System.out.println("Building Canvas");
		canvas = new Canvas(this);
		// Import Menu
		System.out.println("Instantiating Import Menu");
		importMenu = new ImportMenu(this);
		// Assembling network
		System.out.println("Instantiating Graph Assembler");
		app = new Assembler(Assembler.HD1080);
		performance = new TestPerformance();
		this.setActiveGraph(false);
		// Control Panel Frame
		System.out.println("Building Control Panel");
		cFrame = new ControlPanel(this, 200, this.height - 25, "Controls");
		surface.setTitle("Java Networked Interaction Visualization. NetInt");
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
			String[] layoutKeys = { "Fruchterman_Reingold", "Spring", "Circular" };
			ArrayList<String> layoutAttributes = new ArrayList<String>(Arrays.asList(layoutKeys));
			importMenu.makeLists(reader.getKeyNamesForNodes(), reader.getKeyNamesForEdges(), layoutAttributes);
		}
	}

	public void settings() {
		size(displayWidth - 201, displayHeight - 100, P2D);
	}

	/**
	 * Initialized a console visible on runtime. It disables the printing of
	 * messages on the IDE console.
	 * 
	 * @return A "siphon" that collects all the output messages from the System
	 */
	private ByteArrayOutputStream initSystemOutToConsole() {
		// Create a stream to hold the output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		// // IMPORTANT: Save the old System.out!
		// PrintStream old = System.out;
		// Tell java.System to use the special stream
		System.setOut(ps);
		return baos;
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