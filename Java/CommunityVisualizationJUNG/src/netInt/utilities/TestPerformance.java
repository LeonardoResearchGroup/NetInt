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
package netInt.utilities;

import netInt.canvas.Canvas;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class TestPerformance {
	Runtime runtime;
	int mb = 1024 * 1024;

	public TestPerformance() {
	}

	public void displayValues(PVector pos) {
		runtime = Runtime.getRuntime();
		// **** Legends
		Canvas.app.fill(255, 90);
		Canvas.app.textAlign(PConstants.RIGHT);
		Canvas.app.text("Heap Memory Ussage in Mb: ", pos.x, pos.y);
		Canvas.app.text("Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / mb, pos.x, pos.y + 10);
		Canvas.app.text("Free Memory:" + runtime.freeMemory() / mb, pos.x, pos.y + 20);
		Canvas.app.text("Total Memory " + runtime.totalMemory() / mb, pos.x, pos.y + 30);
		Canvas.app.text("Max Memory:" + runtime.maxMemory() / mb, pos.x, pos.y + 40);
		Canvas.app.text("Frame Rate:" + PApplet.round(Canvas.app.frameRate), pos.x, pos.y + 50);
		Canvas.app.textAlign(PConstants.CENTER);
	}
}