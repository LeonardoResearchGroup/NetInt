package gui;

import processing.core.*;
public class GUI {
	public InfoBox infoBox;
	public PApplet app;
	
	public GUI(PApplet app){
		this.app = app;
		infoBox = new InfoBox(app);
	}

	public void show(){
		app.textAlign(PConstants.LEFT, PConstants.TOP);
		infoBox.show();
		app.textAlign(PConstants.CENTER, PConstants.CENTER);		
	}
}
