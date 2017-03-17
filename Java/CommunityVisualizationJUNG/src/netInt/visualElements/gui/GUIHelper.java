package netInt.visualElements.gui;

import controlP5.*;
import netInt.GraphPad;

/**
 * @author juansalamanca
 *@deprecated
 */
public class GUIHelper {

	private String[] buttonLabels = { "Import graph" };
	private GraphPad applet;

	public GUIHelper(GraphPad applet) {
		this.applet = applet;
	}

	private CallbackListener barEvents() {
		return new CallbackListener() {
			public void controlEvent(CallbackEvent ev) {
				ButtonBar bar = (ButtonBar) ev.getController();
				int clicked = bar.hover();
				// Graph import.
				if (clicked == 0) {
					ChooseHelper.getInstance().showFileChooser(applet);
				}
			}
		};
	}

	public void loadGUI() {

		ControlP5 cp5 = new ControlP5(applet);
		ButtonBar b = cp5.addButtonBar("bar");
		b.setPosition(0, 0);
		b.setSize(applet.width, 20).addItems(buttonLabels);
		b.onClick(barEvents());
	}

}
