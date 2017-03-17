package examples;

import java.io.FileNotFoundException;

import netInt.GraphPad;

public class ControlPanelExample {

	public ControlPanelExample() {
		try {
			// Initialize GraphPad with no parameters
			new GraphPad();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ControlPanelExample();
	}

}
