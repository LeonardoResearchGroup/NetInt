
/*
 * Created on May 19, 2008
 *
 * Copyright (c) 2008, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 * 
 * Complemented on February 8, 2017
 * Juan Salamanca
 */
package utilities.filters;

import edu.uci.ics.jung.algorithms.filters.Filter;

import java.util.ArrayList;

import org.apache.commons.collections15.Predicate;
import edu.uci.ics.jung.graph.Graph;

/**
 * Adaptation of original JUNG's EdgePredicateFilter. This version REMOVES the
 * edges from the original collection. As a result it is likely that vertex with
 * no edges remain in the returned collection of method transform().
 * 
 * The original JUNG class documentation says: Transforms the input graph into
 * one which contains only those edges that pass the specified
 * <code>Predicate</code>. The filtered graph is a copy of the original graph
 * (same type, uses the same vertex and edge objects). All vertices from the
 * original graph are copied into the new graph (even if they are not incident
 * to any edges in the new graph).
 * 
 * @author Joshua O'Madadhain
 * @author Juan Salamanca (2017)
 */
public class FilterAndExtractorEdges<V, E> implements Filter<V, E> {
	protected Predicate<E> betweenCommunity_pred;
	protected Predicate<E> withinCommunity_pred;

	/**
	 * Creates an instance based on the specified edge <code>Predicate</code>.
	 * 
	 * @param edge_pred
	 *            the predicate that specifies which edges to add to the
	 *            filtered graph
	 */
	public FilterAndExtractorEdges(Predicate<E> betweenCommunity_pred, Predicate<E> withinCommunity_pred) {
		this.betweenCommunity_pred = betweenCommunity_pred;
		this.withinCommunity_pred = withinCommunity_pred;
	}

	public Graph<V, E> transform(Graph<V, E> g) {
		Graph<V, E> filtered;
		Graph<V, E> community;
		try {
			filtered = g.getClass().newInstance();
			community = g.getClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Unable to create copy of existing graph: ", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to create copy of existing graph: ", e);
		}

		for (V v : g.getVertices())
			filtered.addVertex(v);

		for (E e : g.getEdges()) {
			if (betweenCommunity_pred.evaluate(e))
				filtered.addEdge(e, g.getIncidentVertices(e));
		}
		for (E e : g.getEdges()){
			if(withinCommunity_pred.evaluate(e)){
				community.addEdge(e, g.getIncidentVertices(e));
			}
		}
		for (E e : filtered.getEdges()){
			g.removeEdge(e);
		}
		for (E e : community.getEdges()){
			g.removeEdge(e);
		}
		
		//System.out.println("    Remaining edges in Filters: " + Filters.getInstance().getRemainingGraph().getEdgeCount() + ", rtnGraph : "+ filtered.getEdgeCount());
		return filtered;
	}

}