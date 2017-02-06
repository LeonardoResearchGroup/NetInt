package visualElements.gui;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSettings {

	// BACKGROUND Visibility Settings
	private int colorBackground = 70;

	// NODE Visibility Settings
	private String idBuscador;
	private float umbralOutDegree;
	private boolean mostrarNodos;
	private boolean mostrarNombre;
	private String filtrosNodo;

	// EDGE Visibility Settings
	private boolean mostrarVinculosInt;
	private boolean mostrarVinculosExt;
	private float volTransaccion;
	private float propagacion;
	private boolean soloPropagacion;
	private String filtrosVinculo;

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
	}

	// ******* GETTERS *******
	public float getUmbralGrados() {
		return umbralOutDegree;
	}

	public boolean mostrarNombre() {
		return mostrarNombre;
	}

	public boolean mostrarNodos() {
		return mostrarNodos;
	}

	public boolean mostrarVinculosInt() {
		return mostrarVinculosInt;
	}

	public boolean mostrarVinculosExt() {
		return mostrarVinculosExt;
	}

	public String getFiltrosNodo() {
		return filtrosNodo;
	}

	public float getVolTransaccion() {
		return volTransaccion;
	}

	public float getPropagacion() {
		return propagacion;
	}

	public String getFiltrosVinculo() {
		return filtrosVinculo;
	}

	public String getIdBuscador() {
		return idBuscador;
	}

	public int getColorBackground() {
		return colorBackground;
	}

	public boolean getOnlyPropagation() {
		return soloPropagacion;
	}

	public ArrayList<String> getDescriptiveStatisticKeys() {
		return descriptiveStatisticKeys;
	}
	
	public HashMap<String, Boolean> getDescriptiveStatistics() {
		return descriptiveStatistics;
	}

	public boolean isStatisticVisible(String key){
		return descriptiveStatistics.get(key);
	}


	// ***** SETTERS ******

	public void setDescriptiveStatisticKeys(ArrayList<String> descriptiveStatisticKeys) {
		this.descriptiveStatisticKeys = descriptiveStatisticKeys;
	}

	public void setUmbralGrados(float umbralGrados) {
		this.umbralOutDegree = umbralGrados;
	}

	public void setMostrarNombre(boolean mostrarNombre) {
		this.mostrarNombre = mostrarNombre;
	}

	public void setFiltrosNodo(String filtrosNodo) {
		this.filtrosNodo = filtrosNodo;
	}

	public void setVolTransaccion(float volTransaccion) {
		this.volTransaccion = volTransaccion;
	}

	public void setPropagacion(float propagacion) {
		this.propagacion = propagacion;
	}

	public void setFiltrosVinculo(String filtrosVinculo) {
		this.filtrosVinculo = filtrosVinculo;
	}

	public void setIdBuscador(String stringValue) {
		idBuscador = stringValue;
	}

	public void resetIdBuscador() {
		idBuscador = null;
	}

	public void setColorBackground(int colorValue) {
		colorBackground = colorValue;

	}

	public void setSoloPropagacion(boolean booleanValue) {
		soloPropagacion = booleanValue;

	}
	
	public void setStatisticVisibility(String key, boolean state){
		descriptiveStatistics.put(key, state);
	}


	public void setMostrarNodos(boolean booleanValue) {
		mostrarNodos = booleanValue;

	}

	public void setMostrarVinculosInt(boolean booleanValue) {
		mostrarVinculosInt = booleanValue;
	}

	public void setMostrarVinculosExt(boolean booleanValue) {
		mostrarVinculosExt = booleanValue;
	}

	public void setEventOnVSettings(boolean eventTriggered) {
		eventOnVSettings = eventTriggered;
	}

	public HashMap<String, String> getDescriptiveKeys() {
		return descriptiveKeys;
	}

}
