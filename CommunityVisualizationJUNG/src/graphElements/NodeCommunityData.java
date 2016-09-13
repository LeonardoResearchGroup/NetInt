package graphElements;

public class NodeCommunityData {
	private int inDegree, outDegree, Degree;
	private float Excentricity, Betweeness, size;
	private String communityName;
	
	public NodeCommunityData(){
		
	}
	
	int getInDegree() {
		return inDegree;
	}

	void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}

	int getOutDegree() {
		return outDegree;
	}

	void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}

	int getDegree() {
		return Degree;
	}

	void setDegree(int degree) {
		Degree = degree;
	}

	float getExcentricity() {
		return Excentricity;
	}

	void setExcentricity(float excentricity) {
		Excentricity = excentricity;
	}

	float getBetweeness() {
		return Betweeness;
	}

	void setBetweeness(float betweeness) {
		Betweeness = betweeness;
	}

	float getSize() {
		return size;
	}

	void setSize(float size) {
		this.size = size;
	}

	String getCommunity() {
		return communityName;
	}

	void setCommunity(String community) {
		this.communityName = community;
	}

}
