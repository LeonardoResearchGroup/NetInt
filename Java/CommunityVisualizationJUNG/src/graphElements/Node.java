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
	// True if the node searcher query matches any of this node's attributes
	private boolean isFound = false;

	public Node() {
		super();
	}

	public Node(String id) {
		super();
		RelativeAttributes metaData = new RelativeAttributes();
		// Initialize basic attributes
		absoluetAttributes.put("id", id);
		absoluetAttributes.put("label", "no name");
		relativeAttributes.put(0, metaData);
	}

	public int compareTo(Node node) {
		return this.getId().compareTo(node.getId());
	}

	// Methods community related

	public boolean belongsTo(String community) {
		boolean rtn = false;
		for (RelativeAttributes mD : relativeAttributes.values()) {
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
		for (RelativeAttributes mD : relativeAttributes.values()) {
			communities = communities + mD.getCommunityName();
			if (cont < relativeAttributes.size() - 1) {
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
		String id = (String) absoluetAttributes.get("id");
		return id;
	}

	public String getName() {
		return (String) absoluetAttributes.get("label");
	}

	public float getSize() {
		return (Float) absoluetAttributes.get("size");
	}

	public String getCommunity(int key) {
		return relativeAttributes.get(key).getCommunityName();
	}

	public int getMetadataSize() {
		return relativeAttributes.size();
	}

	public Set<Integer> getMetadataKeys() {
		return relativeAttributes.keySet();
	}

	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

	// Setters
	public void setId(String id) {
		absoluetAttributes.put("id", id);
	}

	public void setName(String object) {
		absoluetAttributes.put("label", object);
	}

	public void setSize(float size) {
		absoluetAttributes.put("size", size);
	}

	// *****Get & set metrics
	// Getters metric
	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getInDegree(int key) {
		return relativeAttributes.get(key).getCommunityInDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getOutDegree(int key) {
		return relativeAttributes.get(key).getCommunityOutDegree();
	}

	/**
	 * @param key
	 *            the community from which the procedure is invoked
	 * @return
	 */
	public int getDegree(int key) {
		return relativeAttributes.get(key).getCommunityDegree();
	}

	// Setters metrics
	public void setInDegree(int key, int inDegree) {
		this.relativeAttributes.get(key).setCommunityInDegree(inDegree);
	}

	public void setOutDegree(int key, int outDegree) {
		this.relativeAttributes.get(key).setCommunityOutDegree(outDegree);
	}

	public void setDegree(int key, int degree) {
		this.relativeAttributes.get(key).setCommunityDegree(degree);
	}

}
