package netInt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import jViridis.ColorMap;
import netInt.utilities.Assembler;
import netInt.utilities.GraphmlKeyReader;
import netInt.utilities.console.ConsoleCatcher;
import netInt.canvas.Canvas;
import netInt.visualElements.gui.ControlPanel;
import netInt.visualElements.gui.ImportMenu;
import netInt.visualElements.gui.UserSettings;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

/**
 * This is the central class of NetInt. It inherits from Processing.PApplet thus
 * it encloses all the functionalities of a Processing sketch.
 * 
 * It could be instantiated in three ways. The simplest way is to invoke
 * GraphPad(boolean) to launch a visualization pad and a Console Catcher. If the
 * parameter is true, it opens a Control Panel to choose the graphml file. The
 * second way is to invoke GraphPad("filePath") parameterizing the file path. It
 * launches the visualization pad but omits launching the Control Panel. The
 * third way is to run the included main class. The latter needs to set the graph
 * path inside the body of the main method
 * 
 * @author jsalam
 * @version alpha
 * @date June 2017
 *
 */
public class GraphPad extends PApplet {
	// Class that assembles the graph with the visual elements
	private Assembler app;

	// The canvas on top of which visual elements and mouse events occur
	private Canvas canvas;

	// The floating window displaying system messages
	protected ConsoleCatcher consoleCatcher;

	// The menu displayed once a graph file is selected
	protected static ImportMenu importMenu;

	// True if the graph is successfully retrieved and loaded from the source
	private static boolean activeGraph;

	// Instance attributes
	private PImage netIntLogo;
	private static File file;

	/**
	 * Launches a visualization pad together with a Console Catcher
	 * 
	 * @param args
	 *            The path to the grap file
	 * @throws FileNotFoundException
	 */
	public GraphPad(String args) throws FileNotFoundException {
		super();
		GraphPad.file = new File(args);
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}

	/**
	 * Launches a visualization pad together with a Console Catcher
	 * 
	 * @param args
	 *            The graph file
	 * @throws FileNotFoundException
	 */
	public GraphPad(File args) throws FileNotFoundException {
		super();
		GraphPad.file = args;
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}

	/**
	 * Launches a visualization pad together with Console Catcher
	 * 
	 * @param enableControlPanel
	 *            if true it launches a Control Panel
	 */
	public GraphPad(boolean enableControlPanel) {
		super();
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
		if (enableControlPanel) {
			if (GraphPad.file == null) {
				new ControlPanel(this, 200, this.height - 25);
			}
		}
	}

	/**
	 * Required method from parent class. It runs only once at the PApplet
	 * initialization. Instantiate the classes and initialize attributes
	 * declared in this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {
		textSize(10);
		surface.setLocation(200, -300);
		smooth();
		/*
		 * Output Console Catcher. The line below enables a console Catcher.
		 * WARNING, it might trigger conflicts with Menu's File Open.
		 */
		 consoleCatcher = new ConsoleCatcher(initSystemOutToConsole());

		// Canvas
		System.out.println("Building Canvas");
		canvas = new Canvas(this);
		surface.setTitle("NetInt. Java Networked Interaction Visualization ");

		// Assembling network
		System.out.println("Instantiating Graph Assembler");
		app = new Assembler(Assembler.HD720);
		setActiveGraph(false);
		netIntLogo = loadImage("./data/images/netInt.png");
		selectImport(file);
		System.out.println("** SETUP completed **");
	}

	/**
	 * Required method from parent class. It draws visualElements and other
	 * PApplet elements on the visualization pad. It constantly iterates over
	 * its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
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
			// export a frame as png
			if (UserSettings.getInstance().getFileExportName() != null) {
				exportFrameAsPNG();
			}
			ColorMap.getInstance("plasma").show(this, 20, 20);
		} else {
			image(netIntLogo, 100, 50);
		}

		// Signature Message :)
		textAlign(PConstants.LEFT);
		fill(186, 216, 231);
		text("NetInt® | Built with Processing 3 | Leonardo & I2T Research Groups, U. Icesi. 2017", 20, height - 10);

		// Sets any event on the canvas to false. MUST be at the end of draw()
		Canvas.setEventOnCanvas(false);
		UserSettings.getInstance().setEventOnVSettings(false);
	}

	/**
	 * Required method from parent class. Define here the size of the
	 * visualization pad
	 * 
	 * @see processing.core.PApplet#settings()
	 */
	public void settings() {
		size(displayWidth - 201, displayHeight - 100, P2D);
	}

	public Assembler getAssembler() {
		return app;
	}

	public void setAssembler(Assembler val) {
		app = val;
	}

	public boolean isActiveGraphA() {
		return activeGraph;
	}

	public static void setActiveGraph(boolean activeGraph) {
		GraphPad.activeGraph = activeGraph;
	}

	public static File getFile() {
		return file;
	}

	/**
	 * Receives the file with the path pointing to the graph file and triggers
	 * an import process. The import process follows the parameters chosen by
	 * the user from the import menu.
	 * 
	 * The method is invoked by showFileChooser() at the ChooseHelper class
	 * 
	 * @param selection
	 *            The file to be imported
	 */
	public void selectImport(File selection) {
		if (selection != null) {
			file = selection;
			GraphmlKeyReader reader = new GraphmlKeyReader(selection);
			// this creates and displays the menu
			String[] layoutKeys = { "Fruchterman_Reingold", "Spring", "Circular" };
			ArrayList<String> layoutAttributes = new ArrayList<String>(Arrays.asList(layoutKeys));
			// Import Menu
			System.out.println("Instantiating Import Menu");
			importMenu = new ImportMenu(this);
			importMenu.makeLists(reader.getKeyNamesForNodes(), reader.getKeyNamesForEdges(), layoutAttributes);
		}
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

	/**
	 * Exports the last frame of the draw loop in format "png"
	 */
	private void exportFrameAsPNG() {
		this.cursor(WAIT);
		saveFrame(UserSettings.getInstance().getFileExportName());
		javax.swing.JOptionPane.showMessageDialog(null,
				"File exported to " + UserSettings.getInstance().getFileExportName() + "." + "png", "",
				javax.swing.JOptionPane.INFORMATION_MESSAGE);
		UserSettings.getInstance().setFileExportName(null);
		this.cursor(ARROW);
	}

	public static void main(String[] args) {
		if (args != null) {
			try {
				new GraphPad("insert your file path here");
			} catch (FileNotFoundException fnfe) {
				System.out.println("File not found. Check your main parameters in GraphPad Class");
			}
		}
	}
}