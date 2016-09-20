package executable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import processing.core.*;
import utilities.TestPerformance;
import utilities.mapping.Mapper;
import visualElements.Canvas;
import visualElements.VCommunity;
import visualElements.gui.ChooseHelper;
import visualElements.gui.GUIHelper;
import visualElements.gui.ControlPanel;
import controlP5.ControlEvent;

public class Executable extends PApplet {
	static Logica app;
	Canvas canvas;
	TestPerformance performance;
	// GUIHelper guiHelper;
	boolean activeGraph;
	ControlPanel cFrame;

	public void setup() {
		textSize(10);
		// guiHelper = new GUIHelper(this);
		// guiHelper.loadGUI();
		smooth();
		canvas = new Canvas(this);
		app = new Logica();
		performance = new TestPerformance();
		// public void loadGraph(String file, String communityFilter, String
		// nodeName, String sector, String edgeWeight)
		app.loadGraph("./data/graphs/Risk.graphml", "Continent", "label", "sector", "weight");
		this.setActiveGraph(true);
		// Control Frame
		cFrame = new ControlPanel(this, 200, this.height - 25, "Controls");
		surface.setLocation(0, 0);
		System.out.println("MAX: " + Mapper.getInstance().getMaxIn() + " MIN: " + Mapper.getInstance().getMinIn());
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
		if (selection != null) {
			ChooseHelper.getInstance().processImport(selection, this);
		}
	}

	public void settings() {
		// size(995, 600, P2D);
		size(displayWidth - 201, displayHeight - 100, P2D);
	}

	public static void retrieveControlPanelEvent(ControlEvent event) {
		try {
			for (VCommunity temp : app.getVisualCommunities()) {
				if (event.isFrom("Umbral grados")) {
					temp.setNodeVisibilityThreshold(event.getValue());
				} else if (event.isFrom("Vol. Transaccion")) {
					temp.setEdgeVisibilityThreshold(event.getValue());
				} else if (event.isFrom("Propagacion")) {
					temp.setPropagationSteps((int) event.getValue());
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
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