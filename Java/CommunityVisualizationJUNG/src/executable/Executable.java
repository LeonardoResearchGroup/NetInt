package executable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import processing.core.*;
import utilities.Assembler;
import utilities.GraphmlKeyReader;
import utilities.TestPerformance;
import utilities.console.ConsoleCatcher;
import visualElements.Canvas;
import visualElements.gui.ControlPanel;
import visualElements.gui.ImportMenu;
import visualElements.gui.UserSettings;

public class Executable extends PApplet {
	public static Assembler app;
	private Canvas canvas;
	private TestPerformance performance;
	private static boolean activeGraph;
	private ControlPanel controlPanel;
	protected ConsoleCatcher consoleCatcher;
	public static File file;
	protected static ImportMenu importMenu;

	public void setup() {
		textSize(10);
		surface.setLocation(200, -300);
		smooth();
		/*
		 * Output Console Catcher. Uncomment the line below to enable a console Catcher.
		 * CAUTION, it might trigger conflicts with Menu's File Open.
		 */
	   // consoleCatcher = new ConsoleCatcher(initSystemOutToConsole());
		// Canvas
		System.out.println("Building Canvas");
		canvas = new Canvas(this);
		// Control Panel Frame
		System.out.println("Building Control Panel");
		controlPanel = new ControlPanel(this, 200, this.height - 25);
		surface.setTitle("Java Networked Interaction Visualization. NetInt");
		// Import Menu
		System.out.println("Instantiating Import Menu");
		importMenu = new ImportMenu(this);
		// Assembling network
		System.out.println("Instantiating Graph Assembler");
		app = new Assembler(Assembler.HD720);
		performance = new TestPerformance();
		setActiveGraph(false);
	}

	public void draw() {
		background(UserSettings.getInstance().getColorBackground());
		if (activeGraph) {
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
		}
		
		// Signature Message :)
		textAlign(PConstants.LEFT);
		text("Built with Processing 3 | Leonardo, I2T & CIENFI Research Groups, U. Icesi. 2017", 20, height - 10);
		
		// Sets any event on the canvas to false. MUST be at the end of draw()
		Canvas.setEventOnCanvas(false);
		UserSettings.getInstance().setEventOnVSettings(false);
	}

	public Assembler getApp() {
		return app;
	}

	public boolean isActiveGraphA() {
		return activeGraph;
	}

	public static void setActiveGraph(boolean activeGraph) {
		Executable.activeGraph = activeGraph;
	}
	
	/**
	 * This method receives the graph file path and triggers an import process.
	 * The import process follows the parameters chosen by the user from the
	 * import menu.
	 * 
	 * The method is invoked by showFileChooser() at the ChooseHelper class
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
	protected ByteArrayOutputStream initSystemOutToConsole() {
		// Create a stream to hold the output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
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