
/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 *
 * It makes extensive use of free libraries such as Processing, Jung, ControlP5, JOGL, 
 * Tinkerpop and many others. For details see the copyrights folder. 
 *
 * Contributors:
 * 	Juan Salamanca, Cesar Loaiza, Luis Felipe Rivera, Javier Diaz
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *
 * version alpha
 *******************************************************************************/
package netInt.utilities.filters;

import edu.uci.ics.jung.algorithms.filters.Filter;

import org.apache.commons.collections15.Predicate;
import edu.uci.ics.jung.graph.Graph;

/**
 * Adaptation of original JUNG's EdgePredicateFilter. This version REMOVES the
 * edges from the original collection. As a result it is likely that vertices
 * with no edges remain in the returned collection of method transform().
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
	 * @param betweenCommunity_pred
	 *            The predicate that determines if a given edge belongs t the
	 *            set of edges linking nodes from both communities
	 * @param withinCommunity_pred
	 *            The predicate that determines if a given edge belongs to the
	 *            set of edges linking nodes from a single community
	 */
	public FilterAndExtractorEdges(Predicate<E> betweenCommunity_pred, Predicate<E> withinCommunity_pred) {
		this.betweenCommunity_pred = betweenCommunity_pred;
		this.withinCommunity_pred = withinCommunity_pred;
	}

	/*
	 * (non-Javadoc) Receives a source graph from which edges from the filtered
	 * and community sets are removed. The filtered set contains edges linking
	 * nodes from two communities (defined in the predicates). The community set
	 * contains edges that belong to a given community to which we want to find
	 * the external linking edges.
	 * 
	 * The result of this operation is twofold. First, all the edges linking
	 * nodes from a given community are removed from the source graph (See
	 * Filter.remainingGraph). Second, it returns a set of edges linking nodes
	 * from a given community regardless if the nodes are edge's source or target.
	 * 
	 * @see
	 * org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 * 
	 */
	@SuppressWarnings("unchecked")
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
		for (E e : g.getEdges()) {
			if (withinCommunity_pred.evaluate(e)) {
				community.addEdge(e, g.getIncidentVertices(e));
			}
		}
		for (E e : filtered.getEdges()) {
			g.removeEdge(e);
		}
		for (E e : community.getEdges()) {
			g.removeEdge(e);
		}

		// System.out.println(" Remaining edges in Filters: " +
		// Filters.getInstance().getRemainingGraph().getEdgeCount() + ",
		// rtnGraph : "+ filtered.getEdgeCount());
		return filtered;
	}

}