package graphElements;

import java.util.TreeSet;

public class Node implements Comparable<Node> {

	private int inDegree, outDegree, Degree;
	private int id;// Must have a unique identifier. See equals(Object obj)
	private TreeSet<Integer> insideSubGraphs;
	private String label;
	private float Excentricity, Betweeness, size;
	private String community;
	
	
	public Node(){
		
	}

	public Node(int id) {
		this.id = id;
		insideSubGraphs = new TreeSet<Integer>();
		insideSubGraphs.add(0);
	}

	public int compareTo(Node vertex) {
		return id - vertex.id;
	}

	// Methods community related
	public void includeInSubGraph(int arg) {
		insideSubGraphs.add(arg);
	}

	public boolean belongsTo(int communityID) {
		boolean rtn = false;
		for (Integer i : insideSubGraphs) {
			if (i.equals(communityID)) {
				rtn = true;
			} else
				rtn = false;
		}
		return rtn;
	}

	public String getSubGraphIDs() {
		String indexes = "";
		int cont = 0;
		for (Integer val : insideSubGraphs) {
			indexes = indexes + val.toString();
			if (cont < insideSubGraphs.size() - 1) {
				indexes = indexes + ",";
			}
			cont++;
		}
		return indexes;
	}

	// *** equals
	public boolean equals(Object obj) {
		Node n = (Node) obj;
		boolean rtn = n.getId() == this.getId();
		return rtn;
	}
	
	public int hashCode(){
		return id;
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
		return label;
	}

	public float getExcentricity() {
		return Excentricity;
	}

	public float getBetweeness() {
		return Betweeness;
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

	public void setName(Object object) {
		this.label =(String) object;
	}

	public void setExcentricity(float excentricity) {
		Excentricity = excentricity;
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

	public String getCommunity() {
		return community;
	}

	public void setCommunity(Object object) {
		this.community = (String) object;
	}

	public TreeSet<Integer> getMySubGraphs() {
		return insideSubGraphs;
	}

}
