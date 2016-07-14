package utilities.visualArrangements;

import java.util.ArrayList;

import visualElements.interactive.VisualAtom;

public abstract class Arrangement {

	public Arrangement() {

	}
	
	protected void clearLayout(ArrayList<VisualAtom> visualElements){
		for(VisualAtom e: visualElements){
			e.setX(0);
			e.setY(0);
		}
	}

}
