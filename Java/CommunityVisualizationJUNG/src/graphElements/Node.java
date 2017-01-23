package graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * This class has two predefined attributes stored in the hashMap that use these
 * keys: id (String) and (label (String). Any other attribute is stored with the
 * key retrieved from the graph source file, usually a graphml. *** IMPORTANT***
 * the attribute "id" defines the node identity therefore it must be a unique
 * identifier
 * 
 * @author jsalam
 * @date Nov 2016
 */
public class Node extends GraphElement implements Comparable<Node>, Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * Collection for community meta-data. The integer parameter identifies the
	 * community (0 reserved for root graph)
	 */
	private HashMap<Integer, NodeMetadata> communityMetaData;
	// Collection of special statistics retrieved from the source file.
	private HashMap<String, Double> descriptiveStatistics = new HashMap<String, Double>();
	// True if the node searcher query matches any of this node's attributes
	private boolean isFound = false;

	public Node() {
		super();
	}

	public Node(String id) {
		super();
		communityMetaData = new HashMap<Integer, NodeMetadata>();
		NodeMetadata metaData = new NodeMetadata();
		// Initialize basic attributes
		attributes.put("id", id);
		attributes.put("label", "no name");
		communityMetaData.put(0, metaData);
	}

	public int compareTo(Node node) {
		return this.getId().compareTo(node.getId());
	}

	// Methods community related

	public boolean belongsTo(String community) {
		boolean rtn = false;
		for (NodeMetadata mD : communityMetaData.values()) {
			if (mD.getCommunityName().equals(community)) {
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
		for (NodeMetadata mD : communityMetaData.values()) {
			communities = communities + mD.getCommunityName();
			if (cont < communityMetaData.size() - 1) {
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

	public String getCommunity(int key) {
		return communityMetaData.get(key).getCommunityName();
	}

	public int getMetadataSize() {
		return communityMetaData.size();
	}

	public Set<Integer> getMetadataKeys() {
		return communityMetaData.keySet();
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
		attributes.put("size", size);
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
		// create meta-datum
		NodeMetadata metaData = new NodeMetadata();
		// assign name
		metaData.setCommunityName(community);
		// add meta-datum to collection of meta-data
		communityMetaData.put(key, metaData);
	}

	// *****Get & set metrics
	// Getters metric
	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getInDegree(int key) {
		return communityMetaData.get(key).getCommunityInDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getOutDegree(int key) {
		return communityMetaData.get(key).getCommunityOutDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getDegree(int key) {
		return communityMetaData.get(key).getCommunityDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public float getBetweeness(int key) {
		return communityMetaData.get(key).getBetweeness();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public float getExcentricity(int key) {
		return communityMetaData.get(key).getExcentricity();
	}

	// Setters metrics
	public void setInDegree(int key, int inDegree) {
		this.communityMetaData.get(key).setCommunityInDegree(inDegree);
	}

	public void setOutDegree(int key, int outDegree) {
		this.communityMetaData.get(key).setCommunityOutDegree(outDegree);
	}

	public void setDegree(int key, int degree) {
		this.communityMetaData.get(key).setCommunityDegree(degree);
	}

	public void setBetweeness(int key, float betweeness) {
		this.communityMetaData.get(key).setBetweeness(betweeness);
	}

	public void setExcentricity(int key, float excentricity) {
		this.communityMetaData.get(key).setExcentricity(excentricity);
	}

	public HashMap<String, Double> getDescriptiveStatistics() {
		return descriptiveStatistics;
	}

}
