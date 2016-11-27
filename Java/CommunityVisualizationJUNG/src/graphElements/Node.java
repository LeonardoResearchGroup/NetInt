package graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * This class has three predefined attributes stored in the hashMap that use
 * these keys: id (String), label (String) and size(float). Any other attribute
 * is stored with the key retrieved from the graph source file, usually a
 * graphml. *** IMPORTANT*** the attribute id defines the node identity
 * therefore it must be a unique identifier
 * 
 * @author jsalam
 * @date Nov 2016
 */
public class Node implements Comparable<Node>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> attributes;
	private HashMap<Integer, NodeMetadata> metadata;
	private boolean isFound = false;
	private HashMap<String, Double> descriptiveStatistics = new HashMap<String, Double>();

	public Node() {

	}

	public Node(String id) {
		// this.id = id;
		attributes = new HashMap<String, Object>();
		metadata = new HashMap<Integer, NodeMetadata>();
		NodeMetadata comData = new NodeMetadata();
		// Initialize basic attributes
		attributes.put("id", id);
		attributes.put("size", null);
		attributes.put("label", null);
		metadata.put(0, comData);
	}

	public int compareTo(Node node) {
		return this.getId().compareTo(node.getId());
	}

	// Methods community related

	public boolean belongsTo(String community) {
		boolean rtn = false;
		for (NodeMetadata mD : metadata.values()) {
			if (mD.getCommunity().equals(community)) {
				rtn = true;
				break;
			} else
				rtn = false;
		}
		return rtn;
	}

	public String getCommunityNames() {
		String communities = "";
		int cont = 0;
		for (NodeMetadata mD : metadata.values()) {
			communities = communities + mD.getCommunity();
			if (cont < metadata.size() - 1) {
				communities = communities + ",";
			}
			cont++;
		}
		return communities;
	}

	// *** equals
	public boolean equals(Object obj) {
		Node n = (Node) obj;
		boolean rtn = n.getId().equals(this.getId());
		return rtn;
	}

	public int hashCode() {
		return this.getId().hashCode();
	}

	// *** Getters and setters

	public String getId() {
		String id = (String) attributes.get("id");
		return id;
	}

	public String getName() {
		return (String) attributes.get("label");
	}

	public float getSize() {
		return (Float) attributes.get("size");
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public String getAttribute(String key, String rtn) {
		try {
			rtn = (String) attributes.get(key);
		} catch (Exception e) {
			System.out.println("Node Attribute couldn't be casted as String");
		}
		return rtn;
	}

	public float getAttribute(String key, Float rtn) {
		try {
			rtn = (Float) attributes.get(key);
		} catch (Exception e) {
			System.out.println("Node Attribute couldn't be casted aa float");
		}
		return rtn;
	}

	public int getAttribute(String key, Integer rtn) {
		try {
			rtn = (Integer) attributes.get(key);
		} catch (Exception e) {
			System.out.println("Node Attribute couldn't be casted as integer");
		}
		return rtn;
	}

	public String getCommunity(int key) {
		return metadata.get(key).getCommunity();
	}

	public int getMetadataSize() {
		return metadata.size();
	}

	public Set<Integer> getMetadataKeys() {
		return metadata.keySet();
	}

	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

	// Setters
	public void setId(String id) {
		attributes.put("id", id);
	}

	public void setName(String object) {
		attributes.put("label", object);
	}

	public void setSize(float size) {
		attributes.put("label", size);
	}
	
	public void setAttribute(String key, Object value){
		attributes.put(key, value);
	}
	
	public void setCommunity(String community) {
		NodeMetadata comData = metadata.get(0);
		comData.setCommunity(community);
		metadata.put(0, comData);
	}

	/**
	 * @param community
	 *            the community to which the node belongs
	 * @param key
	 *            the metadata level associated to the community. This means
	 *            that some statistics of the node such as betweenness or degree
	 *            are relative to the community to which it belongs. If the node
	 *            belongs to more than one community, then the key identifies
	 *            the community to which the attributes are associated. 0 is
	 *            reserved for the root community
	 */
	public void setCommunity(String community, int key) {
		NodeMetadata comData = new NodeMetadata();
		comData.setCommunity(community);
		metadata.put(key, comData);
	}

	// *****Get & set metrics
	// Getters metric
	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getInDegree(int key) {
		return metadata.get(key).getInDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getOutDegree(int key) {
		return metadata.get(key).getOutDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getDegree(int key) {
		return metadata.get(key).getDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public float getBetweeness(int key) {
		return metadata.get(key).getBetweeness();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public float getExcentricity(int key) {
		return metadata.get(key).getExcentricity();
	}

	// Setters metrics
	public void setInDegree(int key, int inDegree) {
		this.metadata.get(key).setInDegree(inDegree);
	}

	public void setOutDegree(int key, int outDegree) {
		this.metadata.get(key).setOutDegree(outDegree);
	}

	public void setDegree(int key, int degree) {
		this.metadata.get(key).setDegree(degree);
	}

	public void setBetweeness(int key, float betweeness) {
		this.metadata.get(key).setBetweeness(betweeness);
	}

	public void setExcentricity(int key, float excentricity) {
		this.metadata.get(key).setExcentricity(excentricity);
	}
	
	public HashMap<String, Double> getDescriptiveStatistics() {
		return descriptiveStatistics;
	}

}
