package gui;

import java.awt.Color;
import java.util.ArrayList;

import graphElements.Graph;
import processing.core.*;

public class InfoBox {

	PApplet app;
	PVector pos, end;
	PVector margin;
	Color background, textColor;
	ArrayList<String> text;

	public InfoBox(PApplet app) {
		this.app = app;
		margin = new PVector(15, 15);
		pos = new PVector(0, 0);
		end = new PVector(app.width, 200);
		background = new Color(10, 0, 0, 80);
		textColor = Color.WHITE;
		text = new ArrayList<String>();
	}

	public void setBox(int x, int y, int w, int h) {
		pos.set(x, y);
		end.set(w, h);
	}

	public void setMargins(int left, int top) {
		margin.set(left, top);
	}

	public void addContent(String obj) {
			text.add((String) obj);
	}

	public void show() {
		app.stroke(Color.WHITE.darker().getRGB(), 50);
		app.fill(background.getRGB());
		app.rect(pos.x, pos.y, end.x, end.y);
		app.fill(textColor.darker().getRGB());
		int count = 0;
		for (String txt : text) {
			app.text(txt, pos.x + margin.x, pos.y + margin.y + (count * 13));
			app.text("Frame rate: "+app.frameRate, pos.x + margin.x, pos.y + margin.y + ((count+1) * 13));
			count++;
		}
	}
}
