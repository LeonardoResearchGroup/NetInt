/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import jViridis.ColorMap;
import netInt.canvas.Canvas;
import netInt.utilities.Assembler;
import netInt.utilities.TestPerformance;
import netInt.visualElements.gui.UserSettings;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

/**
 * <p>
 * This is the central class of NetInt library. An instance of this class always
 * needs a <tt>processing.PApplet</tt> to operate. The enclosed instance of
 * <tt>Assembler</tt> is in charge of putting retrieving the graph from the
 * source file, generate the <tt>VCommunity</tt>'s (visual communities) and all
 * the visual elements that represent each vertex and edge of the source graph.
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
 * 
 * @author Juan Salamanca
 * @version alpha
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
	 * initialization. Instantiate the classes and initialize attributes
	 * declared in this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	private void init() {
		// Set PApplet global properties
		parent.textSize(10);
		//Original: parent.getSurface().setLocation(200, -300);
		parent.getSurface().setLocation(0,0);
		parent.smooth();

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
	}

	/**
	 * Required method from parent class. It draws visualElements and other
	 * PApplet elements on the visualization pad. It constantly iterates over
	 * its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void show() {
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
			performance.displayValues(new PVector(parent.width - 20, parent.height - 60));

			// export a frame as png
			if (UserSettings.getInstance().getFileExportName() != null) {
				exportFrameAsPNG();
			}
			
			ColorMap.getInstance("plasma").show(parent,20,20);
			
		} else {
			if (netIntLogo != null)
				parent.image(netIntLogo, 100, 50);
			else
				parent.text("Missing NetInt Logo", 100, 50);
		}

		// Signature Message :)
		parent.textAlign(PConstants.LEFT);
		parent.fill(186, 216, 231);
		parent.text("NetInt® | Built with Processing 3 | Leonardo & I2T Research Groups, U. Icesi. 2017", 20,
				parent.height - 10);

		// Sets any event on the canvas to false. MUST be at the end of draw()
		Canvas.setEventOnCanvas(false);
		UserSettings.getInstance().setEventOnVSettings(false);
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
}
