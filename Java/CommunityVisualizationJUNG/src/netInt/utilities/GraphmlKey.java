/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package netInt.utilities;

/**
 * Contains the attributes of a graphmlKey as Strings
 * 
 * @author jsalam
 *
 */
public class GraphmlKey {
	String name;
	String type;
	String element;
	String id;

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

	public String toString(){
		String rtn = name + "," + type + "," + element+","+id;
		return rtn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
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
