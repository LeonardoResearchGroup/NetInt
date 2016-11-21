package visualElements.gui;

import java.util.HashMap;

public class VisibilitySettings {

	// BACKGROUND Visibility Settings
	private int colorBackground = 70;

	// NODE Visibility Settings
	private String idBuscador;
	private float umbralOutDegree;
	private boolean mostrarNodos;
	private boolean mostrarNombre;
	private String filtrosNodo;

	// EDGE Visibility Settings
	private boolean mostrarVinculos;
	private float volTransaccion;
	private float propagacion;
	private boolean soloPropagacion;
	private String filtrosVinculo;

	// DESCRIPTIVE STATISTICS Visibility Settings
	private boolean WKActivo;
	private boolean tamano;
	private boolean flujoCajaPasivo;
	private boolean razonCorriente;
	private boolean tangibilidad;
	private boolean pasivoActivo;
	private boolean EBITDAIntereses;
	private boolean EBITDAVentas;
	private boolean roa;
	private boolean roe;
	private boolean crecimientoVentas;
	private HashMap<String, String> descriptiveKeys;
	
	private HashMap<String, Boolean> descriptiveStatistics = new HashMap<String, Boolean>();

	private static VisibilitySettings vSettingsInstance = null;
	
	// An Event to inform if there was an event on the canvas
	public static boolean eventOnVSettings = false;

	public static VisibilitySettings getInstance() {
		if (vSettingsInstance == null) {
			vSettingsInstance = new VisibilitySettings();
		}
		return vSettingsInstance;
	}
	
	public static void reloadInstance(VisibilitySettings instance) {
		vSettingsInstance = instance;
	}

	protected VisibilitySettings() {
		descriptiveKeys.put("WKActivo", "1");
		descriptiveKeys.put("tamano", "1");
		descriptiveKeys.put("flujoCajaPasivo", "1");
		descriptiveKeys.put("razonCorriente", "1");
		descriptiveKeys.put("tangibilidad", "1");
		descriptiveKeys.put("pasivoActivo", "1");
		descriptiveKeys.put("EBITDAIntereses", "1");
		descriptiveKeys.put("EBITDAVentas", "1");
		descriptiveKeys.put("roa", "1");
		descriptiveKeys.put("roe", "1");
		descriptiveKeys.put("crecimientoVentas", "1");
		
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

	public boolean mostrarVinculos() {
		return mostrarVinculos;
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

	public HashMap<String, Boolean> getDescriptiveStatistics() {
		return descriptiveStatistics;
	}

	public boolean WKActivo() {
		return WKActivo;
	}

	public boolean tamano() {
		return tamano;
	}

	public boolean flujoCajaPasivo() {
		return flujoCajaPasivo;
	}

	public boolean razonCorriente() {
		return razonCorriente;
	}

	public boolean tangibilidad() {
		return tangibilidad;
	}

	public boolean pasivoActivo() {
		return pasivoActivo;
	}

	public boolean isEBITDAIntereses() {
		return EBITDAIntereses;
	}

	public boolean isEBITDAVentas() {
		return EBITDAVentas;
	}

	public boolean isRoa() {
		return roa;
	}

	public boolean isRoe() {
		return roe;
	}

	public boolean crecimientoVentas() {
		return crecimientoVentas;
	}

	// ***** SETTERS ******
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

	public void setWKActivo(boolean state) {
		descriptiveStatistics.put("WK/Activo", state);
	}

	public void setTamano(boolean state) {
		descriptiveStatistics.put("Tamano", state);
	}

	public void setFlujoCajaPasivo(boolean state) {
		descriptiveStatistics.put("Flujo de caja / Pasivo", state);
	}

	public void setRazonCorriente(boolean state) {

		descriptiveStatistics.put("Razon Corriente", state);
	}

	public void setTangibilidad(boolean state) {
		descriptiveStatistics.put("Tangibilidad", state);
	}

	public void setPasivoActivo(boolean state) {
		descriptiveStatistics.put("Pasivo / Activo", state);
	}

	public void setEBITDAIntereses(boolean state) {
		descriptiveStatistics.put("EBITDA/Intereses", state);
	}

	public void setEBITDAVentas(boolean state) {
		descriptiveStatistics.put("EBITDA/Ventas", state);
	}

	public void setROA(boolean state) {
		descriptiveStatistics.put("ROA", state);
	}

	public void setROE(boolean state) {
		descriptiveStatistics.put("ROE", state);
	}

	public void setCrecimientoVentas(boolean state) {
		descriptiveStatistics.put("Crecimiento ventas", state);
	}

	public void setMostrarNodos(boolean booleanValue) {
		mostrarNodos = booleanValue;

	}

	public void setMostrarVinculos(boolean booleanValue) {
		mostrarVinculos = booleanValue;
	}
	
	public void setEventOnVSettings(boolean eventTriggered){
		eventOnVSettings = eventTriggered;
	}
	
	public HashMap<String, String> getDescriptiveKeys() {
		return descriptiveKeys;
	}


}
