/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
			System.out.println(
					"\n************************\n*** Path to graph file broken ***\n************************\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.getClass().getName() + " Reading graphml keys completed");
	}

	/**
	 * Splits a Key String in tokens. And stores each of the following key
	 * values: 'name', 'type', 'for' and 'id'
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
			System.out.println(" **** WARNING **** Check the graphml format");
		}
	}

	/**
	 * Returns the GraphmlKey objects with all its attribute names
	 * 
	 * @return the list of GraphmlKeys
	 */
	public ArrayList<GraphmlKey> getKeys() {
		return graphKeys;
	}

	/**
	 * Returns all key names from the graphml
	 * 
	 * @return all key names from the graphml
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
	 * @return all key names of edge attributes from the graphml
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
	 * @return all key names of node attributes from the graphml
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
