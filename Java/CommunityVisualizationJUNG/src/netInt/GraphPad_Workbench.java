/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package netInt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import netInt.canvas.Canvas;
import netInt.utilities.TestPerformance;
import netInt.utilities.console.ConsoleCatcher;
import netInt.visualElements.gui.UserSettings;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class GraphPad_Workbench {

	public PApplet parent;

	// The canvas on top of which visual elements and mouse events occur
	public Canvas canvas;

	// The floating window displaying system messages
	protected ConsoleCatcher consoleCatcher;

	private TestPerformance performance;

	// Instance attributes

	/**
	 * Launches a visualization pad
	 * 
	 * @param parent
	 *            The PApplet serving as sketch pad
	 */
	public GraphPad_Workbench(PApplet parent) {
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
		parent.getSurface().setLocation(200, 0);
		parent.smooth();

		/*
		 * Output Console Catcher. The line below enables a console Catcher.
		 * WARNING, it might trigger conflicts with Menu's File Open.
		 */

		consoleCatcher = new ConsoleCatcher(initSystemOutToConsole());

		// Canvas
		System.out.println("Building Canvas");
		canvas = new Canvas(parent);

		// Performance
		performance = new TestPerformance();

		System.out.println("** GraphPad Init() completed **");

	}

	/**
	 * Required method from parent class. It draws visualElements and other
	 * PApplet elements on the visualization pad. It constantly iterates over
	 * its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void show(PGraphics[] graphics) {
		//parent.background(100);
		parent.pushMatrix();
		// canvas.translateCenter(parent.width / 2, parent.height / 2);
		canvas.transform();
		parent.image(graphics[0], 0, 0);
		parent.image(graphics[1], 0, 0);
		canvas.originCrossHair();
		parent.popMatrix();
		canvas.showLegend(new PVector(parent.width - 20, 20));
		canvas.displayValues(new PVector(parent.width - 20, 40));
		canvas.showControlPanelMessages(new PVector(20, 20));
		performance.displayValues(new PVector(parent.width - 20, parent.height - 60));

		// Signature Message :)
		parent.textAlign(PConstants.LEFT);
		parent.fill(186, 216, 231);
		parent.text("NetInt® | Built with Processing 3 | Leonardo & I2T Research Groups, U. Icesi. 2017", 20,
				parent.height - 10);

		// Sets any event on the canvas to false. MUST be at the end of draw()
		Canvas.setEventOnCanvas(false);
		UserSettings.getInstance().setEventOnVSettings(false);
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
}
