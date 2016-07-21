package utilities;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import visualElements.Canvas;

public class TestPerformance {
	Runtime runtime;
	int mb = 1024 * 1024;

	public TestPerformance() {
	}

	public void displayValues(Canvas canvas, PVector pos) {
		runtime = Runtime.getRuntime();
		// **** Legends
		canvas.app.fill(255, 90);
		canvas.app.textAlign(PConstants.RIGHT);
		canvas.app.text("Heap Memory Ussage in Mb: ", pos.x, pos.y);
		canvas.app.text("Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / mb, pos.x, pos.y + 10);
		canvas.app.text("Free Memory:" + runtime.freeMemory() / mb, pos.x, pos.y + 20);
		canvas.app.text("Total Memory " + runtime.totalMemory() / mb, pos.x, pos.y + 30);
		canvas.app.text("Max Memory:" + runtime.maxMemory() / mb, pos.x, pos.y + 40);
		canvas.app.text("Frame Rate:" + PApplet.round(canvas.app.frameRate), pos.x, pos.y + 50);
		canvas.app.textAlign(PConstants.CENTER);
	}
}