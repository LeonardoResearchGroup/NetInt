package utilities;

import java.io.Serializable;
import java.util.ArrayList;

import visualElements.VCommunity;

@SuppressWarnings("serial")
public class SerializeWrapper implements Serializable{

	private VCommunity vSubSubCommunity;
	private ArrayList<VCommunity> vSubCommunities;
	

	public SerializeWrapper(VCommunity vSubSubCommunity, ArrayList<VCommunity> vSubCommunities) {
		super();
		this.vSubSubCommunity = vSubSubCommunity;
		this.vSubCommunities = vSubCommunities;
	}


	public VCommunity getvSubSubCommunity() {
		return vSubSubCommunity;
	}


	public void setvSubSubCommunity(VCommunity vSubSubCommunity) {
		this.vSubSubCommunity = vSubSubCommunity;
	}


	public ArrayList<VCommunity> getvSubCommunities() {
		return vSubCommunities;
	}


	public void setvSubCommunities(ArrayList<VCommunity> vSubCommunities) {
		this.vSubCommunities = vSubCommunities;
	}

	
	
}
