package utilities.predicates;

import org.apache.commons.collections15.Predicate;

import graphElements.Edge;
import graphElements.Node;

/**
 * A series of predicates used in boolean operations performed over community's
 * graphElements
 * 
 * @author jsalam
 *
 */
public abstract class Predicates {

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
	 * @param communityName
	 * @return
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
	 * @param communityNameA
	 * @param communityNameB
	 * @return
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
}