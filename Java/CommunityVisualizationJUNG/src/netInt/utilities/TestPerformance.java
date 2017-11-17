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
