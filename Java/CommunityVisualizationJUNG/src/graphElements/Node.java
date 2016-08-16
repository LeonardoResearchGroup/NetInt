package graphElements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Node implements Comparable<Node>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;// Must have a unique identifier. See equals(Object obj)
	private float diameter;
	private String label;
	private String sector;
	private HashMap<Integer, NodeMetadata> metadata;

	public Node() {

	}

	public Node(String id) {
		this.id = id;
		metadata = new HashMap<Integer, NodeMetadata>();
		NodeMetadata comData = new NodeMetadata();
		metadata.put(0, comData);
	}

	public int compareTo(Node node) {
		return id.compareTo(node.id);
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
		return id.hashCode();
	}

	// *** Getters and setters
	
	public String getId() {
		return id;
	}

	public String getName() {
		return label;
	}
	
	public String getSector() {
		return sector;
	}

	public float getSize() {
		return diameter;
	}

	public String getCommunity(int key) {
		return metadata.get(key).getCommunity();
	}
	
	public int getMetadataSize() {
		return metadata.size();
	}

	public Set<Integer> getMetadataKeys(){
		return metadata.keySet();
	}
	

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String object) {
		this.label = object;
	}
	
	public void setSector(String sector) {
		this.sector = sector;
	}
	
	public void setSize(float size) {
		this.diameter = size;
	}
	
	public void setCommunity(String community) {
		NodeMetadata comData = metadata.get(0);
		comData.setCommunity(community);
		metadata.put(0, comData);
	}
	
	public void setCommunity(String community, int key) {
		NodeMetadata comData = new NodeMetadata();
		comData.setCommunity(community);
		metadata.put(key, comData);
	}

	// *****Get & set metrics
	// Getters metric
	/**
	 * @param key the community from which the procedure is invoked
	 * @return
	 */
	public int getInDegree(int key) {
		return metadata.get(key).getInDegree();
	}
	
	/**
	 * @param key the community from which the procedure is invoked
	 * @return
	 */
	public int getOutDegree(int key) {
		return metadata.get(key).getOutDegree();
	}

	/**
	 * @param key the community from which the procedure is invoked
	 * @return
	 */
	public int getDegree(int key) {
		return metadata.get(key).getDegree();
	}

	/**
	 * @param key the community from which the procedure is invoked
	 * @return
	 */
	public float getBetweeness(int key) {
		return metadata.get(key).getBetweeness();
	}
	
	/**
	 * @param key the community from which the procedure is invoked
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

}
