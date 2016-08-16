package executable;

import java.io.File;

import processing.core.*;
import utilities.TestPerformance;
import visualElements.Canvas;
import visualElements.gui.ChooseHelper;
import visualElements.gui.GUIHelper;

public class Executable extends PApplet {
	Logica app;
	Canvas canvas;
	TestPerformance performance;
	GUIHelper guiHelper;
	boolean activeGraph;

	public void setup() {
		textSize(10);
		guiHelper = new GUIHelper(this);
		guiHelper.loadGUI();
		smooth();
		canvas = new Canvas(this);
		app = new Logica();
		performance = new TestPerformance();
		
	}

	public void draw() {
		
		if (activeGraph) {

			background(30);

			pushMatrix();
			canvas.translateCenter(width / 2, height / 2);
			canvas.transform();
			// canvas.originCrossHair();
			app.show(canvas);
			popMatrix();
			//
			canvas.showLegend(new PVector(width - 20, 20));
			canvas.displayValues(new PVector(width - 20, 40));
			performance.displayValues(canvas, new PVector(width - 20, height - 60));
		}
	}

	public Logica getApp() {
		return app;
	}

	public boolean isActiveGraph() {
		return activeGraph;
	}

	public void setActiveGraph(boolean activeGraph) {
		this.activeGraph = activeGraph;
	}
	
	public void selectImport(File selection) {
		
		if(selection != null)
		{
			ChooseHelper.getInstance().processImport(selection, this);
		}
		
	}

	public void settings() {
		size(1200, 600, P2D);
		// size(displayWidth, displayHeight - 100, P2D);
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