package executable;


import processing.core.*;

public class Executable extends PApplet {
	Logica app;
	boolean clockwise = true;

	public void setup() {
		size(1200,600);
		//size(1500, 600);
		app = new Logica(this);
		textSize(10);
		textAlign(CENTER);
		// noLoop();
		ellipseMode(CENTER);
	}

	public void draw() {
		background(0);
		 app.show(this);
		 fill(255,100);
		 text(frameRate,20,10);
	}

	
	public void keyPressed(){
		if (key=='r'){
			clockwise = !clockwise;
		}
	}
	
}
