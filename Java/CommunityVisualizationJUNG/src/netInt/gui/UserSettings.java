/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class UserSettings {

	// FILE
	private String fileExportName;

	// BACKGROUND Visibility Settings
	private int colorBackground = 70;

	// NODE Visibility Settings
	private String idSearch;
	private float thresholdOutDegree;
	private boolean showNodes = true;
	private boolean showName = false;
	private String nodeColorAttribute;
	private String nodeSizeAttribute;
	private String converterNode;

	// EDGE Visibility Settings
	private boolean showInternalEdges = true;
	private boolean showExternalEdges = true;
	private String edgeWeightAttribute;
	private float thresholdWeight;
	private float thresholdPropagation = 0;
	private boolean onlyPropagation = false;
	private String edgeThicknessAttribute;
	private String edgeColorAttribute;
	private String converterEdge;
	private TreeMap<Integer, TreeMap<String, Boolean>> edgeTierVisibility;
	private float clearPropagation;

	// DESCRIPTIVE STATISTICS Visibility Settings
	private ArrayList<String> descriptiveStatisticKeys;

	private HashMap<String, String> descriptiveKeys;
	// It has the financial statements keys and their titles to show into
	// VNodeDescriptions
	private HashMap<String, Boolean> descriptiveStatistics;

	private static UserSettings vSettingsInstance = null;

	// An Event to inform if there was an event on the canvas
	public static boolean eventOnVSettings = false;

	public static UserSettings getInstance() {
		if (vSettingsInstance == null) {
			vSettingsInstance = new UserSettings();
		}
		return vSettingsInstance;
	}

	public static void reloadInstance(UserSettings instance) {
		vSettingsInstance = instance;
	}

	protected UserSettings() {
		descriptiveKeys = new HashMap<String, String>();
		descriptiveStatistics = new HashMap<String, Boolean>();
		edgeTierVisibility = new TreeMap<Integer, TreeMap<String, Boolean>>();
		initTierVisibility();
	}

	private void initTierVisibility() {
		// Two tiers so far. Internal
		for (int i = 0; i < 2; i++) {
			TreeMap<String, Boolean> tmp = new TreeMap<String, Boolean>();
			tmp.put("Internal", true);
			tmp.put("External", true);
			edgeTierVisibility.put(i, tmp);
		}

	}

	// **************************** GETTERS ****************************
	// ***** FILE GETTERS ******
	
	public String getFileExportName() {
		return fileExportName;
	}
	
	// ***** BACKGROUND GETTERS ******
	
	public int getColorBackground() {
		return colorBackground;
	}
	
	// ***** NODE GETTERS ******
	
	public float getDegreeThreshold() {
		return thresholdOutDegree;
	}
	
	public boolean showName() {
		return showName;
	}

	public boolean showNodes() {
		return showNodes;
	}
	
	public String getNodeColor() {
		return nodeColorAttribute;
	}
	
	public String getNodeSize() {
		return nodeSizeAttribute;
	}
	
	public String getIdSearch() {
		return idSearch;
	}

	public String getConverterNode() {
		return converterNode;
	}
	
	// ***** EDGE GETTERS ******
	
	public boolean showInternalEdges() {
		return showInternalEdges;
	}

	public boolean showExternalEdges() {
		return showExternalEdges;
	}
	
	public float getWeight() {
		return thresholdWeight;
	}
	
	public float getPropagationThreshold() {
		return thresholdPropagation;
	}

	public String getEdgeColor() {
		return edgeColorAttribute;
	}
	
	public String getEdgeThickness() {
		return edgeThicknessAttribute;
	}
	
	public boolean filterPropagation() {
		return onlyPropagation;
	}
	
	public String getConverterEdge() {
		return converterEdge;
	}
	
	public String getEdgeWeightAttribute() {
		return edgeWeightAttribute;
	}
	
	public boolean internalEdgeVisibilityForTier(int tier) {
		return edgeTierVisibility.get(tier).get("Internal");
	}

	public boolean externalEdgeVisibilityForTier(int tier) {
		return edgeTierVisibility.get(tier).get("External");
	}

	public boolean getClearPropagation() {
		boolean rtn = clearPropagation == 1.0;
		clearPropagation = 0f;
		return rtn;
	}
	
	// ***** STATISTICS GETTERS ******
	
	public ArrayList<String> getDescriptiveStatisticKeys() {
		return descriptiveStatisticKeys;
	}
	
	public HashMap<String, Boolean> getDescriptiveStatistics() {
		return descriptiveStatistics;
	}
	
	public boolean isStatisticVisible(String key) {
		return descriptiveStatistics.get(key);
	}
	
	public HashMap<String, String> getDescriptiveKeys() {
		return descriptiveKeys;
	}


	// **************************** SETTERS ***************************
	// ***** FILE SETTERS ******

	public void setFileExportName(String val) {
		this.fileExportName = val;
	}

	// ***** BACKGROUND SETTERS ******

	public void setColorBackground(int colorValue) {
		colorBackground = colorValue;
	}

	// ***** NODE SETTERS ******

	public void setShowNodes(boolean booleanValue) {
		showNodes = booleanValue;
	}

	public void setDegreeThreshold(float umbralGrados) {
		this.thresholdOutDegree = umbralGrados;
	}

	public void setShowName(boolean mostrarNombre) {
		this.showName = mostrarNombre;
	}

	public void setNodeColorAtt(String val) {
		this.nodeColorAttribute = val;
	}
	
	public void setNodeSizeAtt(String val) {
		this.nodeSizeAttribute = val;
	}

	public void setIDSearch(String stringValue) {
		idSearch = stringValue;
	}

	public void resetSearchId() {
		idSearch = null;
	}

	public void setConverterNode(String val) {
		this.converterNode = val;
	}

	// ***** EDGE SETTERS ******

	public void setEdgeWeightAttribute(String edgeWeightAttribute) {
		this.edgeWeightAttribute = edgeWeightAttribute;
	}

	public void setWeight(float weight) {
		this.thresholdWeight = weight;
	}

	public void setConverterEdge(String converterEdge) {
		this.converterEdge = converterEdge;
	}

	public void setEdgeColorAtt(String val) {
		this.edgeColorAttribute = val;
	}
	
	public void setEdgeThicknessAtt(String val) {
		this.edgeThicknessAttribute = val;
	}

	public void setPropagation(float propagacion) {
		this.thresholdPropagation = propagacion;
	}

	public void setPropagationFilter(boolean booleanValue) {
		onlyPropagation = booleanValue;
	}

	public void setClearPropagation(float val) {
		clearPropagation = val;
	}

	public void setShowInternalEdges(int tier, boolean booleanValue) {
		edgeTierVisibility.get(tier).put("Internal", booleanValue);
	}

	public void setShowExternalEdges(int tier, boolean booleanValue) {
		edgeTierVisibility.get(tier).put("External", booleanValue);
	}

	// ***** STATISTICS SETTERS ******

	public void setDescriptiveStatisticKeys(ArrayList<String> descriptiveStatisticKeys) {
		this.descriptiveStatisticKeys = descriptiveStatisticKeys;
	}

	public void setStatisticVisibility(String key, boolean state) {
		descriptiveStatistics.put(key, state);
	}

	// ****** EVENT SETTERS ******

	public void setEventOnVSettings(boolean eventTriggered) {
		eventOnVSettings = eventTriggered;
	}

}
