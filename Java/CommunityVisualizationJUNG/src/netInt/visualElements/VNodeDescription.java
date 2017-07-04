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
package netInt.visualElements;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import netInt.canvas.Canvas;
import netInt.visualElements.VNode;
import netInt.visualElements.gui.UserSettings;
import processing.core.PConstants;

public class VNodeDescription implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
			Canvas.app.rect(vNode.getPos().x - 5, vNode.getPos().y - 3, width, height - heightComplement);
			// canvas.app.fill(setColor(200, 170));
			Canvas.app.fill(new Color(200, 200, 200, 170).getRGB());

			// Identification Data
			Canvas.app.text("Name: " + vNode.getNode().getName(), vNode.getPos().x + 5, vNode.getPos().y - 5);
			Canvas.app.text("ID: " + vNode.getNode().getId(), vNode.getPos().x + 5, vNode.getPos().y - 15);
			Canvas.app.stroke(180, 50);
			Canvas.app.strokeWeight(1);
			Canvas.app.line(vNode.getPos().x + 5, vNode.getPos().y - 30, vNode.getPos().x + 150, vNode.getPos().y - 30);

			// Communities data
			Iterator<Integer> itr = vNode.getNode().getMetadataKeys().iterator();
			int count = 0;
			int shift = 0;
			while (itr.hasNext()) {
				int tier = itr.next();
				shift = count * 35;
				Canvas.app.text("Com: " + vNode.getNode().getCommunity(tier), vNode.getPos().x + 5,
						vNode.getPos().y - 60 - shift);
				Canvas.app.text("in: " + vNode.getNode().getInDegree(tier), vNode.getPos().x + 5,
						vNode.getPos().y - 50 - shift);
				Canvas.app.text("out: " + vNode.getNode().getOutDegree(tier), vNode.getPos().x + 5,
						vNode.getPos().y - 40 - shift);
				count++;
			}

			Canvas.app.strokeWeight(1);
			Canvas.app.line(vNode.getPos().x + 5, vNode.getPos().y - 80 - shift, vNode.getPos().x + 150,
					vNode.getPos().y - 80 - shift);

			// Descriptive Statistics
			HashMap<String, Boolean> descriptiveStats = UserSettings.getInstance().getDescriptiveStatistics();
			count = 0;
			for (String key : descriptiveStats.keySet()) {
				// If the map of descriptive stats has any true boolean
				if (descriptiveStats.get(key)) {
					shift = count * 12;
					Canvas.app.text(key + ": " + vNode.getNode().getStringAttribute(key), vNode.getPos().x + 5,
							vNode.getPos().y - 125 - shift);
					count++;
				}
			}
			heightComplement = shift + 20;
		}
		Canvas.app.textAlign(PConstants.CENTER, PConstants.CENTER);
	}

	public void show(VCommunity vComm) {
		Canvas.app.textAlign(PConstants.LEFT);
		if (vComm.isMouseOver) {
			Canvas.app.noStroke();
			// canvas.app.fill(setColor(50, 150));
			Canvas.app.fill(new Color(50, 50, 50, 150).getRGB());
			Canvas.app.rect(vComm.getPos().x - 5, vComm.getPos().y - 3, width, -38 - heightComplement);
			// canvas.app.fill(setColor(200, 170));
			Canvas.app.fill(new Color(200, 200, 200, 170).getRGB());

			// Identification Data
			Canvas.app.text("Name: " + vComm.getNode().getName(), vComm.getPos().x + 5, vComm.getPos().y - 5);
			Canvas.app.text("ID: " + vComm.getNode().getId(), vComm.getPos().x + 5, vComm.getPos().y - 15);
			Canvas.app.stroke(180, 50);
			Canvas.app.strokeWeight(1);
			Canvas.app.line(vComm.getPos().x + 5, vComm.getPos().y - 30, vComm.getPos().x + 150, vComm.getPos().y - 30);

			// Communities data
			int shift = 0;
			if (vComm.getNode().getRelativeAttributes(1) != null) {
				for (String key : vComm.getNode().getRelativeAttributes(1).keySet()) {
					float tempValue = vComm.getNode().getRelativeAttributes(1).get(key);
					Canvas.app.text(key + " " + tempValue, vComm.getPos().x + 5, vComm.getPos().y - 45 - shift);
					shift += 13;
				}
			}else{
				Canvas.app.text("--", vComm.getPos().x + 5, vComm.getPos().y - 45 );
				shift = 13;
			}
			Canvas.app.strokeWeight(1);
			Canvas.app.line(vComm.getPos().x + 5, vComm.getPos().y - 50 - shift, vComm.getPos().x + 150, vComm.getPos().y - 50 - shift);

			heightComplement = shift + 20;
		}
		Canvas.app.textAlign(PConstants.CENTER, PConstants.CENTER);
	}
}
