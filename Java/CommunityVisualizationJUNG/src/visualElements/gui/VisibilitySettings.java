package visualElements.gui;

public class VisibilitySettings {

	// NODE Visibility Settings
	private float umbralOutDegree;
	private boolean mostrarNombre;
	private String filtrosNodo;

	// EDGE Visibility Settings
	private float volTransaccion;
	private float propagacion;
	private String filtrosVinculo;
	
	// RISK & PROFIT Visibility Settings
	// *** place here the user selections from 

	private static VisibilitySettings vSettingsInstance = null;

	public static VisibilitySettings getInstance() {
		if (vSettingsInstance == null) {
			vSettingsInstance = new VisibilitySettings();
		}
		return vSettingsInstance;
	}

	protected VisibilitySettings() {
	}

	// ******* Getters & Setters *******

	public float getUmbralGrados() {
		return umbralOutDegree;
	}

	public boolean isMostrarNombre() {
		return mostrarNombre;
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

}
