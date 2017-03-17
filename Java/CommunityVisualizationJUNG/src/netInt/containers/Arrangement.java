package netInt.containers;

import java.util.ArrayList;

import netInt.visualElements.primitives.VisualAtom;

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
