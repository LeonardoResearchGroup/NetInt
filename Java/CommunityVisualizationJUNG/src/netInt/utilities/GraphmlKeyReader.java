package netInt.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

/**
 * Reads the keys from a graphml file
 * 
 * @author jsalam
 *
 */
public class GraphmlKeyReader {

	private ArrayList<GraphmlKey> graphKeys;

	public GraphmlKeyReader(String fileURL) {
		graphKeys = new ArrayList<GraphmlKey>();
		try {
			String currentLine;
			BufferedReader br = new BufferedReader(new FileReader(fileURL));
			System.out.println(this.getClass().getName() + " Reading graphml Keys... ");
			while ((currentLine = br.readLine()) != null) {
				currentLine = currentLine.trim();
				if (currentLine.startsWith("<key")) {
					splitKeyAttributes(currentLine);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.getClass().getName() + " Reading graphml keys completed");
	}

	public GraphmlKeyReader(File file) {
		graphKeys = new ArrayList<GraphmlKey>();
		try {
			String currentLine;
			BufferedReader br = new BufferedReader(new FileReader(file));
			System.out.println(this.getClass().getName() + " Reading graphml Keys... ");
			while ((currentLine = br.readLine()) != null) {
				currentLine = currentLine.trim();
				if (currentLine.startsWith("<key")) {
					splitKeyAttributes(currentLine);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.getClass().getName() + " Reading graphml keys completed");
	}

	/**
	 * Splits a Key String in tokens. And stores each of the following key values: 'name', 'type', 'for' and 'id' 
	 * 
	 * 
	 * @param key
	 *            the string to be split
	 */
	public void splitKeyAttributes(String key) {
		// System.out.println(this.getClass().getName() + " " + key);
		try {
			String[] tokens = key.split("\"");
			GraphmlKey tmp = new GraphmlKey();

			for (int i = 0; i < tokens.length - 1; i++) {
				if (tokens[i].endsWith("name=")) {
					tmp.setName(tokens[i + 1]);
				}
				if (tokens[i].endsWith("type=")) {
					tmp.setType(tokens[i + 1]);
				}
				if (tokens[i].endsWith("for=")) {
					tmp.setElement(tokens[i + 1]);
				}
				if (tokens[i].endsWith("id=")) {
					tmp.setId(tokens[i + 1]);
				}
			}
			graphKeys.add(tmp);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(
					" **** WARNING **** Check the graphml format");
		}
	}

	/**
	 * Returns the GraphmlKey object with all its attribute names
	 * 
	 * @return
	 */
	public ArrayList<GraphmlKey> getKeys() {
		return graphKeys;
	}

	/**
	 * Returns all key names from the graphml
	 * 
	 * @return
	 */
	public ArrayList<String> getKeyNames() {
		ArrayList<String> tmp = new ArrayList<String>();
		for (GraphmlKey k : graphKeys) {
			tmp.add(k.getName());
		}
		return tmp;
	}

	/**
	 * Returns all key names of edge attributes from the graphml
	 * 
	 * @return
	 */
	public ArrayList<String> getKeyNamesForEdges() {
		ArrayList<String> tmp = new ArrayList<String>();
		for (GraphmlKey k : graphKeys) {
			if (k.isEdgeKey())
				tmp.add(k.getName());
		}
		return tmp;
	}

	/**
	 * Returns all key names of node attributes from the graphml
	 * 
	 * @return
	 */
	public ArrayList<String> getKeyNamesForNodes() {
		ArrayList<String> tmp = new ArrayList<String>();
		for (GraphmlKey k : graphKeys) {
			if (k.isNodeKey())
				tmp.add(k.getName());
		}
		return tmp;
	}
}
