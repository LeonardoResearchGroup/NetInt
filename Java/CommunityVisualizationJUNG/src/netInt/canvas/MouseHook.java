package netInt.canvas;

import netInt.visualElements.primitives.VisualAtom;

/**
 * Used to select only one instance of Visual Atom when mouse is pressed
 * Singleton pattern
 * 
 * @author juan salamanca
 *
 */
public class MouseHook {

	private static VisualAtom hooked;
	private static MouseHook instance = null;

	protected MouseHook() {
		// Exists only to defeat instantiation.
	}

	public static MouseHook getInstance() {
		if (instance == null) {
			instance = new MouseHook();
			hooked = null;
		}
		return instance;
	}

	public void hook(VisualAtom vA) {
			hooked = vA;
	}

	public boolean isHooked(VisualAtom vA) {
		if (hooked == null) {
			return false;
		} else if (hooked.equals(vA)) {
			return true;
		} else {
			return false;
		}
	}

	public void release() {
		hooked = null;
	}
}
