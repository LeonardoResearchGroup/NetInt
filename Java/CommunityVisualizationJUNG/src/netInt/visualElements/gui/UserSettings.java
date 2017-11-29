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
	private String filtersNode;
	private String converterNode;

	// EDGE Visibility Settings
	private boolean showInternalEdges = true;
	private boolean showExternalEdges = true;
	private float transactionVolume;
	private float propagation = 0;
	private boolean onlyPorpagation= false;
	private String filtersEdge;
	private String converterEdge;

	// DESCRIPTIVE STATISTICS Visibility Settings
	private ArrayList<String> descriptiveStatisticKeys;

	private HashMap<String, String> descriptiveKeys;
	// It has the financial statements keys and their titles to show into
	// VNodeDescriptions
	private HashMap<String, Boolean> descriptiveStatistics;

	//Adaptative performance
	private boolean isAdapting;
	private double degreeThresholdPercentage = 100;

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
	}

	// ******* GETTERS *******
	public float getDegreeThreshold() {
		return thresholdOutDegree;
	}

	public boolean showName() {
		return showName;
	}

	public boolean showNodes() {
		return showNodes;
	}

	public boolean showInternalEdges() {
		return showInternalEdges;
	}

	public boolean showExternalEdges() {
		return showExternalEdges;
	}

	public String getNodeFilters() {
		return filtersNode;
	}

	public float getTransactionVolume() {
		return transactionVolume;
	}

	public float getPropagation() {
		return propagation;
	}

	public String getEdgeFilters() {
		return filtersEdge;
	}

	public String getIdSearch() {
		return idSearch;
	}

	public int getColorBackground() {
		return colorBackground;
	}

	public boolean getOnlyPropagation() {
		return onlyPorpagation;
	}

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
	
	public String getConverterNode() {
		return converterNode;
	}
	
	public String getConverterEdge() {
		return converterEdge;
	}
	
	public String getFileExportName(){
		return fileExportName ;
	}

	// ***** SETTERS ******
	
	public void setFileExportName(String val){
		this.fileExportName = val;
	}

	public void setConverterEdge(String converterEdge) {
		this.converterEdge = converterEdge;
	}

	public void setDescriptiveStatisticKeys(ArrayList<String> descriptiveStatisticKeys) {
		this.descriptiveStatisticKeys = descriptiveStatisticKeys;
	}

	public void setDegreeThreshold(float umbralGrados) {
		this.thresholdOutDegree = umbralGrados;
	}

	public void setShowName(boolean mostrarNombre) {
		this.showName = mostrarNombre;
	}

	public void setNodeFilters(String filtrosNodo) {
		this.filtersNode = filtrosNodo;
	}

	public void setTransactionVolume(float volTransaccion) {
		this.transactionVolume = volTransaccion;
	}

	public void setPropagation(float propagacion) {
		this.propagation = propagacion;
	}

	public void setEdgeFilters(String filtrosVinculo) {
		this.filtersEdge = filtrosVinculo;
	}

	public void setIDSerch(String stringValue) {
		idSearch = stringValue;
	}

	public void resetSearchId() {
		idSearch = null;
	}

	public void setColorBackground(int colorValue) {
		colorBackground = colorValue;
	}

	public void setPropagationOnly(boolean booleanValue) {
		onlyPorpagation = booleanValue;
	}

	public void setStatisticVisibility(String key, boolean state) {
		descriptiveStatistics.put(key, state);
	}

	public void setShowNodes(boolean booleanValue) {
		showNodes = booleanValue;
	}

	public void setShowInternalEdges(boolean booleanValue) {
		showInternalEdges = booleanValue;
	}

	public void setShowExternalEdges(boolean booleanValue) {
		showExternalEdges = booleanValue;
	}

	public void setEventOnVSettings(boolean eventTriggered) {
		eventOnVSettings = eventTriggered;
	}
	
	public void setConverterNode (String val){
		this.converterNode = val;
	}

	public boolean isAdapting() {
		return isAdapting;
	}

	public void setAdapting(boolean isAdapting) {
		this.isAdapting = isAdapting;
	}

	public double getDegreeThresholdPercentage() {
		return degreeThresholdPercentage;
	}
	
	public void setDegreeThresholdPercentage(double degreeThresholdPercentage) {
		this.degreeThresholdPercentage = degreeThresholdPercentage;
	}

	public void reduceDegreeThresholdPercentage(double r) {
		this.degreeThresholdPercentage -= r;
	}
	
	public void incrementDegreeThresholdPercentage(double i) {
		this.degreeThresholdPercentage += i;
	}

}
