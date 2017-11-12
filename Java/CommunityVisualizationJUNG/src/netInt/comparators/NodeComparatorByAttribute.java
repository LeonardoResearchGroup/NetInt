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
package netInt.comparators;

import java.util.Comparator;

import netInt.graphElements.Node;

public class NodeComparatorByAttribute implements Comparator<Node> {
	String attributeName;

	public NodeComparatorByAttribute(String attributeName) {
		this.attributeName = attributeName;
	}

	public int compare(Node o1, Node o2) {
		try {
			float value = o1.getFloatAttribute(attributeName) - o2.getFloatAttribute(attributeName);

			if (value < 0) {
				return -1;
			} else if (value > 0) {
				return 1;
			} else if (value == 0) {
				return 0;
			} else {
				return 0;
			}
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " " + e.toString() + " \n" + "    Attribute name - "
					+ attributeName + " - not found in nodes. CHECK COMPARATOR RESULTS");

			return 0;
		}
	}

}
