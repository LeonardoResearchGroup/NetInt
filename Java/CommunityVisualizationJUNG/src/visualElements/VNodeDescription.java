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

	private VNode vNode;

	public VNodeDescription(VNode vNode) {
		this.vNode = vNode;
	}

	int heightComplement = 0;

	public void show(Canvas canvas) {
		canvas.app.textAlign(PConstants.LEFT);
		if (vNode.isMouseOver) {
			canvas.app.noStroke();
			// canvas.app.fill(setColor(50, 150));
			canvas.app.fill(new Color(50, 50, 50, 150).getRGB());
			canvas.app.rect(vNode.pos.x - 5, vNode.pos.y - 3, width, height - heightComplement);
			// canvas.app.fill(setColor(200, 170));
			canvas.app.fill(new Color(200, 200, 200, 170).getRGB());

			// Identification Data
			canvas.app.text("Name: " + vNode.getNode().getName(), vNode.pos.x + 5, vNode.pos.y - 5);
			canvas.app.text("ID: " + vNode.getNode().getId(), vNode.pos.x + 5, vNode.pos.y - 15);
			canvas.app.text("Sector: " + vNode.getNode().getSector(), vNode.pos.x + 5, vNode.pos.y - 25);

			// Communities data
			Iterator<Integer> itr = vNode.getNode().getMetadataKeys().iterator();
			int count = 0;
			int shift = 0;
			while (itr.hasNext()) {
				int key = itr.next();
				shift = count * 35;
				canvas.app.text("Com: " + vNode.getNode().getCommunity(key), vNode.pos.x + 5, vNode.pos.y - 65 - shift);
				canvas.app.text("in: " + vNode.getNode().getInDegree(key), vNode.pos.x + 5, vNode.pos.y - 55 - shift);
				canvas.app.text("out: " + vNode.getNode().getOutDegree(key), vNode.pos.x + 5, vNode.pos.y - 45 - shift);
				count++;
			}

			// Descriptive Statistics
			HashMap<String, Boolean> descriptiveStats = VisibilitySettings.getInstance().getDescriptiveStatistics();
			count = 0;
			for (String key : descriptiveStats.keySet()) {
				// If the map of descriptive stats has any true boolean
				if (descriptiveStats.get(key)) {
					shift = count * 12;
					canvas.app.text(key + ": ", vNode.pos.x + 5, vNode.pos.y - 125 - shift);
					count++;
				}
			}
			heightComplement = shift +20;
		}
		canvas.app.textAlign(PConstants.CENTER, PConstants.TOP);
	}
}
