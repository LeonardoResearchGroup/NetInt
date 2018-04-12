/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import netInt.canvas.Canvas;
import netInt.gui.ControlPanel;
import netInt.gui.ImportMenuGuiSet;
import netInt.gui.UserSettings;
import netInt.utilities.Assembler;
import netInt.utilities.GraphmlKeyReader;
import netInt.utilities.TestPerformance;
import netInt.utilities.console.ConsoleCatcher;
import netInt.utilities.mapping.MapperViewer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.pdf.*;

/**
 * <p>
 * This is the central class of NetInt library. An instance of this class always
 * needs a <tt>processing.PApplet</tt> to operate. The enclosed instance of
 * <tt>Assembler</tt> is in charge of retrieving the graph from the source file,
 * generate the <tt>VCommunity</tt>'s (visual communities) and all the visual
 * elements that represent each vertex and edge of the source graph.
 * </p>
 * <p>
 * It also encloses and instance of <tt>Canvas</tt> that handles the canvas on
 * top of which visual elements are drawn, mouseEvents are detected, and spatial
 * transformations such as zoom and pan are controlled.
 * </p>
 * <p>
 * An option to monitor the usage of JVM resources is to instantiate
 * <tt>TestPerformence</tt> to display on the canvas the status of heap memory
 * and other features of the virtual machine.
 * </p>
 * <p>
 * LOADING THE IMPORT MENU: Inside <tt>selectImport()</tt>, an instance of
 * <tt>GraphmlKeyReader</tt> reads the header of the graphml source file,
 * instantiate the ImportMenuGuiSet class and populate its dropdown menus using
 * the <tt>makeList()</tt> method with both the data retrieved from the header
 * and an array of layout names. The import menu is displayed on the
 * <tt>PApplet</tt> for the user to choose the nested structure of communities,
 * the node label and edge weight from the lists of node attributes of each
 * dropdown menu. Each user selection is stored in arrays or strings that are
 * passed across classes up to the <tt>GraphmlReader</tt> class, where the
 * <tt>makeNodes()</tt> method uses them to import nodes and their attributes
 * from the source graphml file. Such loading attributes operation happens at
 * loading time.
 * </p>
 * 
 * @author jsalam. June 2017
 *
 */

public class GraphPad {

	public PApplet parent;

	// Class that assembles the graph with the visual elements
	public static Assembler app;

	// The canvas on top of which visual elements and mouse events occur
	public Canvas canvas;

	// True if the graph is successfully retrieved and loaded from the source
	private static boolean activeGraph;

	// The menu displayed once a graph file is selected
	protected static ImportMenuGuiSet importMenu;

	// The floating window displaying system messages
	protected ConsoleCatcher consoleCatcher;

	// The Mapper viewer window
	protected MapperViewer mapperViewer;

	private TestPerformance performance;

	// Instance attributes
	private PImage netIntLogo;
	private static File file;

	/**
	 * Launches a visualization pad together with a Console Catcher
	 * 
	 * @param parent
	 *            The PApplet serving as sketch pad
	 * @param args
	 *            The path to the grap file
	 */
	public GraphPad(PApplet parent, String args) {
		this.parent = parent;
		GraphPad.file = new File(args);
		init();
	}

	/**
	 * Launches a visualization pad together with a Console Catcher
	 * 
	 * @param parent
	 *            The PApplet serving as sketch pad
	 * @param args
	 *            The graph file
	 */
	public GraphPad(PApplet parent, File args) {
		this.parent = parent;
		GraphPad.file = args;
		init();
	}

	/**
	 * Launches a visualization pad
	 * 
	 * @param parent
	 *            The PApplet serving as sketch pad
	 */
	public GraphPad(PApplet parent) {
		this.parent = parent;
		init();
	}

	/**
	 * Required method from parent class. It runs only once at the PApplet
	 * initialization. Instantiate the classes and initialize attributes declared in
	 * this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	private void init() {
		// Set PApplet global properties
		parent.textSize(10);
		parent.getSurface().setLocation(226, 0);
		parent.smooth();

		/*
		 * Output Console Catcher. The line below enables a console Catcher. WARNING, it
		 * might trigger conflicts with Menu's File Open.
		 */

		// Visualization of system.out messages in external panel
		// consoleCatcher = new ConsoleCatcher(initSystemOutToConsole());

		// Visualization of node and edge attributes in external panel
		mapperViewer = new MapperViewer();

		// Canvas
		System.out.println("Building Canvas");
		canvas = new Canvas(parent);
		parent.getSurface().setTitle("NetInt. Networked Interaction Visualization in Java ");

		// Assembling network
		System.out.println("Instantiating Graph Assembler");
		app = new Assembler(Assembler.HD720);
		setActiveGraph(false);

		// The path to the icon
		netIntLogo = parent.loadImage("./images/netInt.png");

		// Performance
		performance = new TestPerformance();

		System.out.println("** GraphPad Init() completed **");

	}

	/**
	 * Required method from parent class. It draws visualElements and other PApplet
	 * elements on the visualization pad. It constantly iterates over its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void show() {
		
		// start export a frame as pdf
		if (UserSettings.getInstance().getFileExportName() != null) {
			exportFrameAsPDF(true);
		}

		// User defines import parameters choosing from import menu
		if (importMenu != null) {
			importMenu.show();
		}

		if (activeGraph) {



			parent.pushMatrix();
			canvas.translateCenter((parent.width - app.getRootDimension().width) / 2,
					(parent.height - app.getRootDimension().height) / 2);
			canvas.transform();
			// canvas.originCrossHair();
			app.show();
			parent.popMatrix();
			canvas.showLegend(new PVector(parent.width - 20, 20));
			canvas.displayValues(new PVector(parent.width - 20, 40));
			canvas.showControlPanelMessages(new PVector(20, 20));

			if (UserSettings.getInstance().getAdaptivePerformance()) {
				canvas.adjustThresholdAdaptivePerformance();
			}

			performance.displayValues(new PVector(parent.width - 20, parent.height - 60));

		} else {
			if (netIntLogo != null)
				parent.image(netIntLogo, 100, 50);
			else
				parent.text("Missing NetInt Logo", 100, 50);
		}
		
		// end export a frame as pdf
		if (UserSettings.getInstance().getFileExportName() != null) {
			exportFrameAsPDF(false);
			UserSettings.getInstance().setFileExportName(null);
		}
		

		// Signature Message :)
		parent.textAlign(PConstants.LEFT);
		parent.fill(186, 216, 231);
		parent.text("NetInt | Built with Processing 3 | 2017", 20, parent.height - 10);

		// Sets any event on the canvas to false. MUST be at the end of draw()
		Canvas.resetMouseEventsOnCanvas();
		UserSettings.getInstance().resetEvents();
	}

	public Assembler getAssembler() {
		return app;
	}

	public void setAssembler(Assembler val) {
		app = val;
	}

	public boolean isActiveGraph() {
		return activeGraph;
	}

	public static void setActiveGraph(boolean activeGraph) {
		GraphPad.activeGraph = activeGraph;
	}

	public static File getFile() {
		return file;
	}

	public static void setFile(File file) {
		GraphPad.file = file;
	}

	/**
	 * Receives the file with the path pointing to the graph file and triggers an
	 * import process. The import process follows the parameters chosen by the user
	 * from the import menu.
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
			String[] layoutKeys = { "Fruchterman_Reingold", "Spring", "Circular", "Circular_Concentric", "Linear" };
			ArrayList<String> layoutAttributes = new ArrayList<String>(Arrays.asList(layoutKeys));
			// Import Menu
			System.out.println("Instantiating Import Menu");
			importMenu = new ImportMenuGuiSet(this);
			importMenu.makeLists(reader.getKeyNamesForNodes(), reader.getNumericKeyNamesForEdges(), layoutAttributes);
		}
	}

	/**
	 * Receives the file with the path pointing to the graph file and triggers an
	 * import process. The import process follows the parameters chosen by the user
	 * from the import menu.
	 * 
	 * The method is invoked by showFileChooser() at the ChooseHelper class
	 * 
	 * @param selection
	 *            The file to be imported
	 * @param strings
	 *            The second parameter is the array of complementary module names
	 */
	public void selectImport(File selection, String[] strings) {

		if (selection != null) {
			file = selection;
			GraphmlKeyReader reader = new GraphmlKeyReader(selection);
			// this creates and displays the menu
			String[] layoutKeys = { "Concentric", "Fruchterman-Reingold", "Spring" };
			ArrayList<String> layoutAttributes = new ArrayList<String>(Arrays.asList(layoutKeys));
			// Import Menu
			System.out.println("Instantiating Import Menu");
			importMenu = new ImportMenuGuiSet(this);
			importMenu.makeLists(reader.getKeyNamesForNodes(), reader.getNumericKeyNamesForEdges(), layoutAttributes);
		}
	}

	/**
	 * Initialized a console visible on runtime. It disables the printing of
	 * messages on the IDE console.
	 * 
	 * @return A "siphon" that collects all the output messages from the System
	 */
	public static ByteArrayOutputStream initSystemOutToConsole() {
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
		parent.cursor(PApplet.WAIT);
		parent.saveFrame(UserSettings.getInstance().getFileExportName());
		javax.swing.JOptionPane.showMessageDialog(null,
				"File exported to " + UserSettings.getInstance().getFileExportName() + "." + "png", "",
				javax.swing.JOptionPane.INFORMATION_MESSAGE);
		UserSettings.getInstance().setFileExportName(null);
		parent.cursor(PApplet.ARROW);
	}

	/**
	 * Exports the last frame of the draw loop in format "pdf"
	 */
	private void exportFrameAsPDF(boolean condition) {
		parent.cursor(PApplet.WAIT);
		if (condition) {
			parent.beginRecord(PApplet.PDF, UserSettings.getInstance().getFileExportName() + ".pdf");
		} else {
			parent.endRecord();
			javax.swing.JOptionPane.showMessageDialog(null,
					"File exported to " + UserSettings.getInstance().getFileExportName() + "." + "pdf", "",
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
			UserSettings.getInstance().setFileExportName(null);
			parent.cursor(PApplet.ARROW);
		}
	}

}
