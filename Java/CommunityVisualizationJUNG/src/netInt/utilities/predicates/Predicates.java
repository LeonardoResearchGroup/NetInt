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
package netInt.utilities.predicates;

import org.apache.commons.collections15.Predicate;

import netInt.graphElements.Edge;
import netInt.graphElements.Node;

/**
 * A series of predicates used in boolean operations performed over community's
 * graphElements
 * 
 * In mathematics, a predicate is commonly understood to be a Boolean-valued
 * function P: X? {true, false}, called the predicate on X. Informally, a
 * predicate is a statement that may be true or false depending on the values of
 * its variables. It can be thought of as an operator or function that returns a
 * value that is either true or false. From:
 * http://howtodoinjava.com/java-8/how-to-use-predicate-in-java-8/
 * 
 * @author jsalam
 *
 */
public abstract class Predicates {

	/**
	 * Evaluates if a given node belongs to the named community
	 * 
	 * @param communityName
	 *            the community
	 * 
	 * @return predicate with the boolean for a node
	 */
	public static Predicate<Node> nodeInCommunity(final String communityName, final String communityTag) {
		Predicate<Node> nodeIncludedInSubgraph = new Predicate<Node>() {
			public boolean evaluate(Node nodo) {
				return nodo.belongsTo(communityName, communityTag);
			}
		};
		return nodeIncludedInSubgraph;
	}
	
	/**
	 * Evaluates if a given node belongs to the named community * @param
	 * communityName
	 * 
	 * @return
	 */
	public static Predicate<Node> nodeInCommunity(final String communityName) {
		Predicate<Node> nodeIncludedInSubgraph = new Predicate<Node>() {
			public boolean evaluate(Node nodo) {
				return nodo.belongsTo(communityName);
			}
		};
		return nodeIncludedInSubgraph;
	}

	/**
	 * Evaluates if the source and target of a given edge belong to the same
	 * community
	 * 
	 * @param communityName communityName
	 * @return predicate with the boolean for an edge
	 */
	public static Predicate<Edge> edgeInCommunity(final String communityName) {
		Predicate<Edge> rtnPredicate = new Predicate<Edge>() {
			public boolean evaluate(Edge edge) {
				Node source = edge.getSource();
				Node target = edge.getTarget();
				if (source.belongsTo(communityName) && target.belongsTo(communityName)) {
					return true;
				} else {
					return false;
				}
			}
		};
		return rtnPredicate;
	}

	/**
	 * Evaluates if a given edge links nodes from two communities. It checks if
	 * either source and target belong to one of the two communities but both
	 * cannot belong to the same community
	 * 
	 * @param communityNameA communityName
	 * @param communityNameB communityName
	 * @return predicate with the boolean for an edge
	 */
	public static Predicate<Edge> edgeLinkingCommunities(final String communityNameA, final String communityNameB) {
		Predicate<Edge> rtnPredicate = new Predicate<Edge>() {
			public boolean evaluate(Edge edge) {
				Node source = edge.getSource();
				Node target = edge.getTarget();
				if (source.belongsTo(communityNameA) && target.belongsTo(communityNameB)) {
					return true;
				} else if (source.belongsTo(communityNameB) && target.belongsTo(communityNameA)) {
					return true;
				} else {
					return false;
				}
			}
		};
		return rtnPredicate;
	}
	
	/**
	 * Evaluates if a given edge links nodes from two communities made of a communityTag. It checks if
	 * either source and target belong to one of the two communities but both
	 * cannot belong to the same community
	 * 
	 * @param communityNameA
	 * @param communityNameB
	 * @param communityTag
	 * @return
	 */
	public static Predicate<Edge> edgeLinkingCommunities(final String communityNameA, final String communityNameB,  final String communityTag) {
		Predicate<Edge> rtnPredicate = new Predicate<Edge>() {
			public boolean evaluate(Edge edge) {
				Node source = edge.getSource();
				Node target = edge.getTarget();
			
				if (source.belongsTo(communityNameA, communityTag) && target.belongsTo(communityNameB, communityTag)) {
					return true;
				} else if (source.belongsTo(communityNameB, communityTag) && target.belongsTo(communityNameA, communityTag)) {
					return true;
				} else {
					return false;
				}
			}
		};
		return rtnPredicate;
	}
}
