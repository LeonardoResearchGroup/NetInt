package utilities;

import java.io.Serializable;
import java.util.ArrayList;

import visualElements.VCommunity;
import visualElements.gui.UserSettings;

@SuppressWarnings("serial")
public class SerializeWrapper implements Serializable{

	private VCommunity vSubSubCommunity;
	private ArrayList<VCommunity> vSubCommunities;
	private UserSettings  vSettings;
	

	public SerializeWrapper(VCommunity vSubSubCommunity, ArrayList<VCommunity> vSubCommunities, UserSettings  vSettings) {
		super();
		this.vSubSubCommunity = vSubSubCommunity;
		this.vSubCommunities = vSubCommunities;
		this.vSettings = vSettings;
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


	public UserSettings getvSettings() {
		return vSettings;
	}


	public void setvSettings(UserSettings vSettings) {
		this.vSettings = vSettings;
	}

	
	
}
