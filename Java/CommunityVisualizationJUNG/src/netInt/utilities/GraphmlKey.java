/*******************************************************************************
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.utilities;

/**
 * Contains the attributes of a graphmlKey as Strings
 * 
 * @author jsalam
 *
 */
public class GraphmlKey {
	private String name;
	private String type;
	private String element;
	private String id;

	private static final String DOUBLE = "double";
	private static final String BOOLEAN = "boolean";
	private static final String FLOAT = "float";
	private static final String INT = "int";
	private static final String LONG = "long";
	private static final String STRING = "string";

	/*
	 * GRAPHML SPECS FROM http://graphml.graphdrawing.org/specification.html
	 */

	public GraphmlKey() {

	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getElement() {
		return element;
	}

	public String getId() {
		return id;
	}

	public String toString() {
		String rtn = getName() + "," + getType() + "," + getElement() + "," + getId();
		return rtn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {

		switch (type) {

		case "string":
			this.type = STRING;
			break;
			
		case "boolean":
			this.type = BOOLEAN;
			break;
			
		case "int":
			this.type = INT;
			break;
			
		case "float":
			this.type = FLOAT;
			break;
			
		case "long":
			this.type = LONG;
			break;
			
		case "double":
			this.type = DOUBLE;
			break;

		default:
			System.out.println(
					this.getClass().getName() + " WARNING. Attribute type of Graphml file does not match standards");
		}

	}

	public void setElement(String element) {
		this.element = element;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNodeKey() {
		if (element.equals("node")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEdgeKey() {
		if (element.equals("edge")) {
			return true;
		} else {
			return false;
		}
	}

	public void printKey() {
		System.out
				.println(" name: " + getName() + ", type: " + getType() + ", for:" + getElement() + ", id: " + getId());
	}
}
