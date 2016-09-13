package utilities.mapping;

import processing.core.PApplet;

/**
 * @author juansalamanca
 *
 */
public class MappingTool {

	float minIn;
	float maxIn;
	float alpha = 1;
	float beta = 1;

	public MappingTool(float minIn, float maxIn) {
		this.minIn = minIn;
		this.maxIn = maxIn;
	}

	// Linear mapping
	public float linear(float val) {
		float yp = PApplet.map(val, minIn, maxIn, 0, 1);
		return yp;
	}

	// Sinusoidal mapping
	/**
	 * This method converts the input value to a value between PI and HALF_PI
	 * getting its radian value and then returns the sin value of such radian
	 * 
	 * @param val
	 * @return
	 */
	public float sinusoidal(float val) {
		// The radians are the limits if the circumference quarter to
		// be used in the filter. PI to HALF_PI is the third quarter counter
		// clockwise
		float xp = PApplet.map(val, minIn, maxIn, PApplet.PI, PApplet.HALF_PI);
		float y = PApplet.sin(xp);
		return y;
	}

	// Radial mapping
	/**
	 * This method converts the input value to a number between 0 and 1. Such
	 * value is assumed as the intersection on x axis of a circle radius. Then
	 * the angle of the radius is computed using the aCos() function and finally
	 * the sin value of such angle is returned.
	 * 
	 * @param val
	 * @return
	 */
	public float radial(float val) {
		float xp = PApplet.map(val, minIn, maxIn, 1, 0);
		float angulo = PApplet.acos(xp);
		float y = PApplet.sin(angulo);
		return y;
	}

	/**
	 * The interval [minOut,maxOut] determines the target range of the new
	 * values. \alpha defines the width of the input intensity range, and \beta
	 * defines the intensity around which the range is centered.
	 * https://en.wikipedia.org/wiki/Normalization_(image_processing)
	 * 
	 * @param val
	 *            : the value to be filtered
	 * @param minOut
	 *            : the output lower bound
	 * @param maxOut
	 *            : the output upper bound
	 * @param alpha
	 *            : the width of the input intensity range
	 * @param beta
	 *            : the intensity around which the range is centered
	 * @return
	 */
	public float sigmoid(float val, float alpha, float beta) {
		val = PApplet.map(val, minIn, maxIn, 255, 0);
		this.alpha = alpha;
		this.beta = beta;
		float t = (float) Math.pow(Math.E, ((val - beta) / alpha));
		float p = (1 / (1 + t));
		return p;
	}

	/**
	 * Use this method ONLY to visualize the filter
	 * 
	 * @param val
	 * @return
	 */
	public float sigmoid(float val) {
		val = PApplet.map(val, minIn, maxIn, 255, 0);
		float t = (float) Math.pow(Math.E, ((val - beta) / alpha));
		float p = (1 / (1 + t));
		return p;
	}

	/**
	 * This method serves to convert a number between 0 and 1 to a given scale
	 * defined by it lower and higher boundaries. It is mainly used to draw
	 * widgets of the mapping filter
	 * 
	 * @param val
	 *            the value to be converted between 0 and 1
	 * @param minOut
	 * @param maxOut
	 * @param sigmoid
	 *            True if sigmoid, else false
	 * @return
	 */
	private float convertToUnits(float val, float minOut, float maxOut, boolean sigmoid) {
		float p;
		if (sigmoid)
			p = (maxOut - minOut) * val + minOut;
		else
			p = PApplet.map(val, 0, 1, minOut, maxOut);
		return p;
	}

	/**
	 * This method just shows a widget of the mapping filter
	 * 
	 * @param app
	 *            The PApplet where to draw
	 * @param kind
	 *            The name of the mapping filter
	 * @param origX
	 *            The x coordinate of the widget
	 * @param origY
	 *            The y coordinate of the widget
	 * 
	 */
	public void drawFilter(PApplet app, String kind, int origX, int origY) {
		float p = 0;
		float x = 0;
		float y = 0;
		app.strokeWeight(2);
		for (int i = (int) minIn; i < (int) maxIn; i++) {
			switch (kind) {
			case "linear":
				p = linear(i);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			case "sinusoidal":
				p = sinusoidal(i);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			case "sigmoid":
				p = sigmoid(i, alpha, beta);
				x = origX + convertToUnits(p, 0, 100, true);
				break;
			case "radial":
				p = radial(i);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			}
			y = origY - PApplet.map(i, minIn, maxIn, 0, 100);
			app.point(x, y);
		}
		app.strokeWeight(0);
		app.noFill();
		app.rect(origX, origY, 100, -100);
		// legend
		app.text("in", origX + 90, origY + 10);
		app.text("out", origX - 20, origY - 90);
		app.fill(184, 255, 255);
		app.text("Current Filter:", origX - 130, origY - 100);
		switch (kind) {
		case "linear":
			app.text("Linear", origX - 130, origY - 87);
			break;
		case "sinusoidal":
			app.text("Sinusoidal", origX - 130, origY - 87);
			break;
		case "sigmoid":
			app.text("Sigmoid", origX - 130, origY - 87);
			app.text(("·Alpha= " + alpha), origX - 130, origY - 74);
			app.text(("·Beta = " + beta), origX - 130, origY - 61);
			break;
		case "radial":
			app.text("Radial", origX - 130, origY - 87);
		}
	}
}
