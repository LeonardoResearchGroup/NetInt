package utilities.visualArrangements;

import java.util.ArrayList;

import visualElements.interactive.VisualAtom;

public abstract class Arrangement {

	String name;

	public Arrangement() {

	}
	
	protected void clearLayout(ArrayList<VisualAtom> visualElements){
		for(VisualAtom e: visualElements){
			e.setX(0);
			e.setY(0);
		}
	}

	public boolean hasName(String name) {
		if (this.name.equals(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
