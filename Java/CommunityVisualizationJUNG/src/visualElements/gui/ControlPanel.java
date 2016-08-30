package visualElements.gui;

import processing.core.*;
import controlP5.*;
import executable.Executable;

/**
 * @author jsalam Example copied from
 *         https://github.com/sojamo/controlp5/issues/17
 *
 */
public class ControlPanel extends PApplet {
	int w, h;
	PApplet parent;
	ControlP5 cp5;

	public ControlPanel(PApplet _parent, int _w, int _h, String _name) {
		super();
		parent = _parent;
		w = _w;
		h = _h;
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}

	public void setup() {
		this.surface.setSize(w, h);
		this.surface.setLocation(3, 45);
		this.surface.setAlwaysOnTop(true);
		cp5 = new ControlP5(this);
		cp5.addButton("Import Graph").plugTo(parent).setPosition(10, 10).setSize(180,20);
		cp5.addSlider("Edge degree threshold").setPosition(10,60).setSize(180,10).setRange(0,35);
	}

	public void draw() {
		background(70);
	}

	public void controlEvent(ControlEvent theEvent) {
		//System.out.println(theEvent.getController().getName());
		switch(theEvent.getController().getName()){
		case "Import Graph":
			ChooseHelper.getInstance().showFileChooser(false, "graphml", parent);
		break;
		case "Edge degree threshold":
			Executable.retrieveControlPanelEvent("vCommunity", theEvent.getValue());
			break;
		}
	}
}
