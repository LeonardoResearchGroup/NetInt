package utilities;

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

	public GraphmlKeyReader(String file) {
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
		System.out.println(this.getClass().getName() + " Reading graphml completed");
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
		System.out.println(this.getClass().getName() + " Reading graphml completed");
	}

	/**
	 * Splits a Key String in tokens. It must have the following format:
	 * <key attr.name="nnn" attr.type="ttt" for="eee" id="nnn"/>
	 * 
	 * 
	 * @param key
	 */
	public void splitKeyAttributes(String key) {
		System.out.println(this.getClass().getName() + " " + key);
		try {
			String[] tokens = key.split(" ");
			GraphmlKey tmp = new GraphmlKey();
			tmp.setName(tokens[1].substring(11, tokens[1].length() - 1));
			tmp.setType(tokens[2].substring(11, tokens[2].length() - 1));
			tmp.setElement(tokens[3].substring(5, tokens[3].length() - 1));
			tmp.setId(tokens[4].substring(4, tokens[4].length() - 3));
			graphKeys.add(tmp);
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println(" **** WARNING **** Check key attribute in graphml file. It can't contain blank spaces");
			GraphmlKey tmp = new GraphmlKey();
			tmp.setName("missing_Value");
			tmp.setType("missing_Value");
			tmp.setElement("missing_Value");
			tmp.setId("missing_Value");
			graphKeys.add(tmp);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(
					" **** WARNING **** Check the graphml format. It must follow this structure: <key attr.name='nnn' attr.type='ttt' for='eee' id='nnn'/>");
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
