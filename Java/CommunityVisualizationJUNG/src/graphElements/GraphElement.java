package graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Abstract class that handles two attribute collections of nodes and edges. The
 * first set contains absolute attributes, i.e., attributes independent of any
 * graph processing result. The second set are relative attributes, i.e.,
 * attributes that get values depending of graph processing. An example of the
 * latter is node degree after clustering. A node has a relative degree inside
 * each cluster to which it belongs.
 * 
 * For convenience, node and edge graph metrics (inDegree, outDegree, degree)
 * are stored in the collection of relative attributes in numbered sequence,
 * reserving 0 for the root graph.
 * 
 * @author jsalam
 *
 */
public abstract class GraphElement implements Serializable {

	private static final long serialVersionUID = 1L;
	// For String use the attribute name (Edge_Weight, Out_Degree). For Object
	// use the attribute value
	// (either Numerical or categorical)
	protected HashMap<String, Object> absoluetAttributes;
	// A set of attributes to be initiated by instances of classes that
	// inherit from this class. The integer parameter identifies the
	// community to which a set of relative attributes are related. (0 reserved
	// for root graph)
	protected HashMap<Integer, RelativeAttributes> relativeAttributes;

	public GraphElement() {
		absoluetAttributes = new HashMap<String, Object>();
		relativeAttributes = new HashMap<Integer, RelativeAttributes>();
	}

	// *** Getters
	public HashMap<String, Object> getAttributes() {
		return absoluetAttributes;
	}

	public Object getAttribute(String key) {
		return absoluetAttributes.get(key);
	}

	public String getStringAttribute(String key) {
		String rtn = "void";
		try {
			rtn = (String) absoluetAttributes.get(key);
		} catch (Exception e) {
			System.out
					.println(this.getClass().getName() + " Attribute named: " + key + " couldn't be casted as String");
		}
		return rtn;
	}

	public float getFloatAttribute(String key) {
		Float rtn = Float.NEGATIVE_INFINITY;
		try {
			// If Double
			if (absoluetAttributes.get(key) instanceof Double) {
				Double rtnObj = (Double) absoluetAttributes.get(key);
				rtn = rtnObj.floatValue();
				// If Integer
			} else if (absoluetAttributes.get(key) instanceof Integer) {
				Integer rtnObj = (Integer) absoluetAttributes.get(key);
				rtn = rtnObj.floatValue();
				// If float
			} else {
				rtn = (float) absoluetAttributes.get(key);
			}
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " Attribute key: " + key + " value: "
					+ absoluetAttributes.get(key).toString() + " couldn't be casted as Float");
		}
		return rtn;
	}

	public int getIntegerAttribute(String key) {
		Integer rtn = Integer.MIN_VALUE;
		try {
			// If Double
			if (absoluetAttributes.get(key) instanceof Double) {
				Double rtnObj = (Double) absoluetAttributes.get(key);
				rtn = rtnObj.intValue();
				// If Float
			} else if (absoluetAttributes.get(key) instanceof Float) {
				Float rtnObj = (Float) absoluetAttributes.get(key);
				rtn = rtnObj.intValue();
				// If Integer
			} else {
				rtn = (Integer) absoluetAttributes.get(key);
			}
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + " Attribute named: " + key + " couldn't be casted as int");
		}
		return rtn;
	}

	public int getAttributeSize() {
		return absoluetAttributes.size();
	}

	public Object[] getAttributeKeys() {
		Set<String> keys = absoluetAttributes.keySet();
		return keys.toArray();
	}

	public void printAttributes() {
		System.out.println("GRAPH ELEMENT> printAttributes():");
		Set<String> s = absoluetAttributes.keySet();
		for (String keyName : s) {
			System.out.println("   Key: " + keyName + ", Value: " + absoluetAttributes.get(keyName));
		}
	}

	// *** Setters
	/**
	 * @param community
	 *            the community name to which the node belongs
	 * @param key
	 *            the metadata level associated to the community. This means
	 *            that some statistics of the node such as betweenness or degree
	 *            are relative to the community to which it belongs. If the node
	 *            belongs to more than one community, then the key identifies
	 *            the community to which the attributes are associated. 0 is
	 *            reserved for the root community
	 */
	public void setCommunity(String community, int key) {
		// create meta-datum
		RelativeAttributes metaData = new RelativeAttributes();
		// assign name
		metaData.setCommunityName(community);
		// add meta-datum to collection of meta-data
		relativeAttributes.put(key, metaData);
	}

	/**
	 * @param metaData
	 *            the Metadata that contains the community attributes
	 * @param key
	 *            the metadata level associated to the community. This means
	 *            that some statistics of the node such as betweenness or degree
	 *            are relative to the community to which it belongs. If the node
	 *            belongs to more than one community, then the key identifies
	 *            the community to which the attributes are associated. 0 is
	 *            reserved for the root community
	 */
	public void setCommunity(RelativeAttributes metaData, int key) {
		relativeAttributes.put(key, metaData);
	}

	public void setAttribute(String key, Object value) {
		absoluetAttributes.put(key, value);
	}

}
