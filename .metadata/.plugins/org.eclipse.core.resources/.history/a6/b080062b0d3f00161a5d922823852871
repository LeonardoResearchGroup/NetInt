package utilities;

import processing.core.PApplet;

public class Filter {

	float minIn;
	float maxIn;
	float alpha = 1;
	float beta = 1;

	public Filter(float minIn, float maxIn) {
		this.minIn = minIn;
		this.maxIn = maxIn;
	}

	// Filtro lineal
	public float linear(float val) {
		float yp = PApplet.map(val, minIn, maxIn, 0, 1);
		return yp;
	}

	// Filtro sinusoidal
	/**
	 * The radians are the limits if the circumference quarter to be used in the
	 * filter. PI to HALF_PI is the third quarter counter clockwise
	 * 
	 * @param val
	 * @return
	 */
	public float sinusoidal(float val) {
		float xp = PApplet.map(val, minIn, maxIn, PApplet.PI, PApplet.HALF_PI);
		float y = PApplet.sin(xp);
		return y;
	}

	// Filtro radial
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

	public float convertToUnits(float val, float minOut, float maxOut,
			boolean sigmoid) {
		float p;
		if (sigmoid)
			p = (maxOut - minOut) * val + minOut;
		else
			p = PApplet.map(val, 0, 1, minOut, maxOut);
		return p;
	}

	public void drawFilter(PApplet app, int kind, int origX, int origY) {
		float p = 0;
		float x = 0;
		float y = 0;
		app.strokeWeight(2);
		for (int i = (int) minIn; i < (int) maxIn; i++) {
			switch (kind) {
			case 1:
				p = linear(i);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			case 2:
				p = sinusoidal(i);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			case 3:
				p = sigmoid(i, alpha, beta);
				x = origX + convertToUnits(p, 0, 100, true);
				break;
			case 4:
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
		case 1:
			app.text("Linear", origX - 130, origY - 87);
			break;
		case 2:
			app.text("Sinusoidal", origX - 130, origY - 87);
			break;
		case 3:
			app.text("Sigmoid", origX - 130, origY - 87);
			app.text(("áAlpha= " + alpha), origX - 130, origY - 74);
			app.text(("áBeta = " + beta), origX - 130, origY - 61);
			break;
		case 4:
			app.text("Radial", origX - 130, origY - 87);
		}
	}
}
