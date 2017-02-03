package graphElements;

import java.io.Serializable;

public class RelativeAttributes implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The name of the community to which this set of attributes is associated with
	private String communityName;
	// Degrees
	private int inDegree, outDegree, degree;
	
	public RelativeAttributes(){
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


	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String community) {
		this.communityName = community;
	}

}
