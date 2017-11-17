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
package netInt.comparators;

import java.util.Comparator;

import netInt.graphElements.Node;

public class InDegreeComparator implements Comparator<Node> {

	int key;
	
	public void setKey(int key){
		this.key = key;
	}
	@Override
	public int compare(Node a, Node b) {
		return a.getInDegree(key) - b.getInDegree(key);
	}

}
