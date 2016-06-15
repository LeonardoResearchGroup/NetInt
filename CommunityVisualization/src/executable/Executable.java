package executable;

import processing.core.*;
import utilities.*;


public class Executable extends PApplet {

	Logica app;

	public void setup() {
		//size(100,100);
		size(1000, 600);
		app = new Logica(this);
		textSize(8);
		textAlign(CENTER);
		//noLoop();
	}

	public void draw() {
		background(0);
		app.show(this);
	}

}
