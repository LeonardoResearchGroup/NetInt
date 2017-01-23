package graphElements;

import java.io.Serializable;

public class NodeMetadata implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int inDegree, outDegree, degree;
	private float Excentricity, Betweeness, size;
	private String communityName;
	
	public NodeMetadata(){
//		inDegree = 0;
//		outDegree = 0;
//		degree = 0;
	}
	
	public int getCommunityInDegree() {
		return inDegree;
	}

	public void setCommunityInDegree(int inDegree) {
		this.inDegree = inDegree;
	}

	public int getCommunityOutDegree() {
		return outDegree;
	}

	public void setCommunityOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}

	public int getCommunityDegree() {
		return degree;
	}

	public void setCommunityDegree(int degree) {
		this.degree = degree;
	}

	public float getExcentricity() {
		return Excentricity;
	}

	public void setExcentricity(float excentricity) {
		Excentricity = excentricity;
	}

	public float getBetweeness() {
		return Betweeness;
	}

	public void setBetweeness(float betweeness) {
		Betweeness = betweeness;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String community) {
		this.communityName = community;
	}

}
