package netInt.utilities.customCollections;

import java.util.ArrayList;

import netInt.graphElements.Edge;
import netInt.graphElements.Node;

/**
 * * A collection of node arranged in a nested structure: hashMap<K1,
 * hashMap<K2, hashSet<Edge>>>. It is extremely efficient to look for an object
 * of type Edge if K1 and K2 are attributes of Edge
 * 
 * @author juan salamanca 
 * 
 * October 2017
 *
 * @param <K1>
 *            Key type of hashmap at tier 0
 * @param <K2>
 *            Key type of hashMap at tier 1
 */
public class NestedEdgeMap<K1, K2> extends NestedMap<K1, K2, Edge> {

	public NestedEdgeMap() {
		super();
	}

	public boolean internal(Edge e) {
		// Look for the community of the source
		String sCom = e.getSource().getCommunity(1);
		// Look for the community of the target
		String tCom = e.getTarget().getCommunity(1);
		// Compare communities
		return (sCom.equals(tCom));
	}

	@SuppressWarnings("unchecked")
	protected K1 getAttributeValue(Edge e, K1 attributeName) {
		K1 k1 = null;
		// String: the first two chars of the edge source's ID
		if (e.getSource().getId().length() >= 2)
			k1 = (K1) e.getSource().getId().substring(0, 2);
		else
			k1 = (K1) e.getSource().getId().substring(0, 1);
		return k1;
	}

	@SuppressWarnings("unchecked")
	protected K2 getElementK2(Edge e) {
		K2 k2 = null;
		// String: the first two chars of the edge target's ID
		if (e.getTarget().getId().length() >= 2)
			k2 = (K2) e.getTarget().getId().substring(0, 2);
		else
			k2 = (K2) e.getTarget().getId().substring(0, 1);
		return k2;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Edge> getIncidentEdges(Node n) {
		K1 k = null;
		ArrayList<Edge> rtn = new ArrayList<Edge>();
		// Get the K1 value
		if (n.getId().length() >= 2)
			k = (K1) n.getId().substring(0, 2);
		else
			k = (K1) n.getId().substring(0, 1);

		// Look first as Source
		// Iterate over all K2 values
		for (K2 k2 : nestedStructure.get(k).keySet()) {
			// Look for each edge
			for (Edge e : nestedStructure.get(k).get(k2)) {
				if (e.getSource().equals(n)) {
					rtn.add(e);
				}
			}
		}

		// Then look as target
		for (K1 k1 : nestedStructure.keySet()) {
			if (nestedStructure.get(k1).containsKey(k)) {
				for (Edge e : nestedStructure.get(k1).get(k)) {
					if (e.getTarget().equals(n)) {
						rtn.add(e);
					}
				}
			}
		}

		return rtn;
	}

}
