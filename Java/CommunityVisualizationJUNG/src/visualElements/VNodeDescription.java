package visualElements;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import visualElements.Canvas;
import visualElements.VNode;
import visualElements.gui.VisibilitySettings;
import processing.core.PConstants;

public class VNodeDescription {
	private int width = 160;
	private int height = -113;

	public VNodeDescription() {

	}

	int heightComplement = 0;

	public void show(VNode vNode) {
		Canvas.app.textAlign(PConstants.LEFT);
		if (vNode.isMouseOver) {
			Canvas.app.noStroke();
			// canvas.app.fill(setColor(50, 150));
			Canvas.app.fill(new Color(50, 50, 50, 150).getRGB());
			Canvas.app.rect(vNode.pos.x - 5, vNode.pos.y - 3, width, height - heightComplement);
			// canvas.app.fill(setColor(200, 170));
			Canvas.app.fill(new Color(200, 200, 200, 170).getRGB());

			// Identification Data
			Canvas.app.text("Name: " + vNode.getNode().getName(), vNode.pos.x + 5, vNode.pos.y - 5);
			Canvas.app.text("ID: " + vNode.getNode().getId(), vNode.pos.x + 5, vNode.pos.y - 15);
			Canvas.app.text("Sector: " + vNode.getNode().getSector(), vNode.pos.x + 5, vNode.pos.y - 25);

			// Communities data
			Iterator<Integer> itr = vNode.getNode().getMetadataKeys().iterator();
			int count = 0;
			int shift = 0;
			while (itr.hasNext()) {
				int key = itr.next();
				shift = count * 35;
				Canvas.app.text("Com: " + vNode.getNode().getCommunity(key), vNode.pos.x + 5, vNode.pos.y - 65 - shift);
				Canvas.app.text("in: " + vNode.getNode().getInDegree(key), vNode.pos.x + 5, vNode.pos.y - 55 - shift);
				Canvas.app.text("out: " + vNode.getNode().getOutDegree(key), vNode.pos.x + 5, vNode.pos.y - 45 - shift);
				count++;
			}

			// Descriptive Statistics
			HashMap<String, Boolean> descriptiveStats = VisibilitySettings.getInstance().getDescriptiveStatistics();
			count = 0;
			for (String key : descriptiveStats.keySet()) {
				// If the map of descriptive stats has any true boolean
				if (descriptiveStats.get(key)) {
					shift = count * 12;
					Canvas.app.text(key + ": ", vNode.pos.x + 5, vNode.pos.y - 125 - shift);
					count++;
				}
			}
			heightComplement = shift +20;
		}
		Canvas.app.textAlign(PConstants.CENTER, PConstants.TOP);
	}
}
