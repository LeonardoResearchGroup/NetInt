package graphElements;
import java.util.ArrayList;


public class Node implements Comparable <Node>{
	
	private int inDegree, outDegree, Degree, id;
	private String name;
	private float Excentricity, Betweeness;
	private ArrayList<Integer> communities;
	
	public Node(){
	}
	
	public Node(int id){
		this.id = id;
	}
	
	public int compareTo(Node vertex) {
		return id - vertex.id;
	}

	
	// *** Getters and setters

		public int getInDegree() {
			return inDegree;
		}

		public int getOutDegree() {
			return outDegree;
		}

		public int getDegree() {
			return Degree;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public float getExcentricity() {
			return Excentricity;
		}

		public float getBetweeness() {
			return Betweeness;
		}

		public ArrayList<Integer> getCommunities() {
			return communities;
		}

		public void setInDegree(int inDegree) {
			this.inDegree = inDegree;
		}

		public void setOutDegree(int outDegree) {
			this.outDegree = outDegree;
		}

		public void setDegree(int degree) {
			Degree = degree;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setExcentricity(float excentricity) {
			Excentricity = excentricity;
		}

		public void setBetweeness(float betweeness) {
			Betweeness = betweeness;
		}

		public void setCommunities(ArrayList<Integer> communities) {
			this.communities = communities;
		}


	

}
