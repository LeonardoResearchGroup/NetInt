/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.visualElements.gui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class allows to change or obtain user settings from an external Jar file.
 * @author lfrivera
 *
 */
public class UserSettingsProxy {

	/**
	 * An instance of the original user settings.
	 */
	private UserSettings settings;
	
	/**
	 * Simple constructor of the class. Necessary for Java Reflection API.
	 */
	public UserSettingsProxy()
	{
		settings = UserSettings.getInstance();
	}
	
	//------------------------Method Replicas------------------------------------
	
	// ******* GETTERS *******
		public float getDegreeThreshold() {
			return settings.getDegreeThreshold();
		}

		public boolean showName() {
			return settings.showName();
		}

		public boolean showNodes() {
			return settings.showNodes();
		}

		public boolean showInternalEdges() {
			return settings.showInternalEdges();
		}

		public boolean showExternalEdges() {
			return settings.showExternalEdges();
		}

		public String getNodeFilters() {
			return settings.getNodeFilters();
		}

		public float getTransactionVolume() {
			return settings.getTransactionVolume();
		}

		public float getPropagation() {
			return settings.getPropagation();
		}

		public String getEdgeFilters() {
			return settings.getEdgeFilters();
		}

		public String getIdSearch() {
			return settings.getIdSearch();
		}

		public int getColorBackground() {
			return settings.getColorBackground();
		}

		public boolean getOnlyPropagation() {
			return settings.getOnlyPropagation();
		}

		public ArrayList<String> getDescriptiveStatisticKeys() {
			return settings.getDescriptiveStatisticKeys();
		}

		public HashMap<String, Boolean> getDescriptiveStatistics() {
			return settings.getDescriptiveStatistics();
		}

		public boolean isStatisticVisible(String key) {
			return settings.isStatisticVisible(key);
		}
		
		public HashMap<String, String> getDescriptiveKeys() {
			return settings.getDescriptiveKeys();
		}
		
		public String getConverterNode() {
			return settings.getConverterNode();
		}
		
		public String getConverterEdge() {
			return settings.getConverterEdge();
		}
		
		public String getFileExportName(){
			return settings.getFileExportName() ;
		}
		
		// ***** SETTERS ******
		
		public void setFileExportName(String val){
			settings.setFileExportName(val);
		}

		public void setConverterEdge(String converterEdge) {
			settings.setConverterEdge(converterEdge);
		}

		public void setDescriptiveStatisticKeys(ArrayList<String> descriptiveStatisticKeys) {
			settings.setDescriptiveStatisticKeys(descriptiveStatisticKeys);
		}

		public void setDegreeThreshold(float umbralGrados) {
			settings.setDegreeThreshold(umbralGrados);
		}

		public void setShowName(boolean mostrarNombre) {
			settings.setShowName(mostrarNombre);
		}

		public void setNodeFilters(String filtrosNodo) {
			settings.setNodeFilters(filtrosNodo);
		}

		public void setTransactionVolume(float volTransaccion) {
			settings.setTransactionVolume(volTransaccion);
		}

		public void setPropagation(float propagacion) {
			settings.setPropagation(propagacion);
		}

		public void setEdgeFilters(String filtrosVinculo) {
			settings.setEdgeFilters(filtrosVinculo);
		}

		public void setIDSerch(String stringValue) {
			settings.setIDSerch(stringValue);
		}

		public void resetSearchId() {
			settings.resetSearchId();
		}

		public void setColorBackground(int colorValue) {
			settings.setColorBackground(colorValue);
		}

		public void setPropagationOnly(boolean booleanValue) {
			settings.setPropagationOnly(booleanValue);
		}

		public void setStatisticVisibility(String key, boolean state) {
			settings.setStatisticVisibility(key, state);
		}

		public void setShowNodes(boolean booleanValue) {
			settings.setShowNodes(booleanValue);
		}

		public void setShowInternalEdges(boolean booleanValue) {
			settings.setShowInternalEdges(booleanValue);
		}

		public void setShowExternalEdges(boolean booleanValue) {
			settings.setShowExternalEdges(booleanValue);
		}

		public void setEventOnVSettings(boolean eventTriggered) {
			settings.setEventOnVSettings(eventTriggered);
		}
		
		public void setConverterNode (String val){
			settings.setConverterNode(val);
		}

}
