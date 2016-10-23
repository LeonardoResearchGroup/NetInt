package utilities.mapping;


import processing.core.PApplet;


/**
 * @author jsalam
 *
 */
public class Mapper {

	// ATTRIBUTE
	public static final int COMUNITY_SIZE = 1;
	public static final int EDGE_WEIGHT = 2;
	public static final int EDGE_DENSITY = 3;
	public static final int IN_DEGREE = 4;
	public static final int OUT_DEGREE = 5;
	public static final int BETWENESS = 6;
	// FILTER
	public static final String LINEAR = "linear";
	public static final String SINUSOIDAL = "sinusoidal";
	public static final String LOGARITMIC = "logarithmic";
	public static final String RADIAL = "radial";
	public static final String SIGMOID = "sigmoid";
	// MAXs & MINs
	private int minCommunitySize;
	private int maxCommunitySize;
	private float minEdgeWeight;
	private float maxEdgeWeight;
	private int minEdgeDensity;
	private int maxEdgeDensity;
	private int minInDegree;
	private int maxInDEgree;
	private int minOutDegree;
	private int maxOutDegree;
	private float minBetweeness;
	private float maxBetweeness;
	// Other attributes
	private float alpha = 1;
	private float beta = 1;

	private static Mapper mapperInstance = null;

	public static Mapper getInstance() {
		if (mapperInstance == null) {
			mapperInstance = new Mapper();
		}
		return mapperInstance;
	}

	protected Mapper() {
	}

	/**
	 * @param filter
	 *            filter name
	 * @param val
	 *            the number to be mapped
	 * @param factor
	 *            the desired max output value
	 * @param graphAttribute
	 * @return the value mapped equals to a number between 0 and 1 X factor
	 */
	public float convert(String filter, float val, float factor, int graphAttribute) {
		float rtn = -1;
		switch (filter) {
		case "linear":
			rtn = linear(val, getMaxMin(graphAttribute));
			break;
		case "sinusoidal":
			rtn = sinusoidal(val, getMaxMin(graphAttribute));
			break;
		case "logarithmic":
			rtn = log(val);
			break;
		case "radial":
			rtn = radial(val, getMaxMin(graphAttribute));
			break;
		case "sigmoid":
			rtn = sigmoid(val, getMaxMin(graphAttribute));
			break;
		}
		rtn = rtn * factor;

		if (rtn < 0) {
			System.out.println(this.getClass().getName() + "   *** Error in " + filter + " filter trying to map : " + val
					+ ", ussing the graph atttribute: " + graphAttribute + ". Value mapped to 0");
			rtn = 0;
		}
		return rtn;
	}

	/**
	 * @param graphAttribute
	 *            Static attribute of the class
	 * @return Array of floats [0] min, [1] max
	 */
	public float[] getMaxMin(int graphAttribute) {
		float[] rtn = new float[2];
		switch (graphAttribute) {
		// COMUNITY_SIZE = 1;
		case 1:
			rtn[0] = minCommunitySize;
			rtn[1] = maxCommunitySize;
			break;
		// EDGE_WEIGHT = 2;
		case 2:
			rtn[0] = minEdgeWeight;
			rtn[1] = maxEdgeWeight;
			break;
		// EDGE_DENSITY = 3;
		case 3:
			rtn[0] = minEdgeDensity;
			rtn[1] = maxEdgeDensity;
			break;
		// IN_DEGREE = 4;
		case 4:
			rtn[0] = minInDegree;
			rtn[1] = maxInDEgree;
			break;
		// OUT_DEGREE = 5;
		case 5:
			rtn[0] = minOutDegree;
			rtn[1] = maxOutDegree;
			break;
		// BETWENESS = 6;
		case 6:
			rtn[0] = minBetweeness;
			rtn[1] = maxBetweeness;
			break;
		}

		if (rtn[0] == rtn[1]) {
			System.out.println(this.getClass().getName() + "> getMaxMin: graphAttribute '" + graphAttribute
					+ "' not implemented. MinMax set to [0,5]");
			rtn[0] = 0;
			rtn[1] = 5;
		}
		return rtn;
	}

	// Linear mapping
	private float linear(float val, float[] minMax) {
		float yp = PApplet.map(val, minMax[0], minMax[1], 0, 1);
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
	private float sinusoidal(float val, float[] minMax) {
		// The radians are the limits if the circumference quarter to
		// be used in the filter. PI to HALF_PI is the third quarter counter
		// clockwise
		float xp = PApplet.map(val, minMax[0], minMax[1], PApplet.PI, PApplet.HALF_PI);
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
	private float radial(float val, float[] minMax) {
		float xp = PApplet.map(val, minMax[0], minMax[1], 1, 0);
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
	private float sigmoid(float val, float[] minMax, float alpha, float beta) {
		val = PApplet.map(val, minMax[0], minMax[1], 255, 0);
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
	private float sigmoid(float val, float[] minMax) {
		val = PApplet.map(val, minMax[0], minMax[1], 255, 0);
		float t = (float) Math.pow(Math.E, ((val - beta) / alpha));
		float p = (1 / (1 + t));
		return p;
	}

	/**
	 * Logarithm base 10
	 * 
	 * @param weight
	 * @return
	 */
	private float log(float weight) {
		float rtn = (float) Math.log(weight);
		return rtn;
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
		float[] mM = { 0, 100 };
		for (int i = 0; i < mM[1]; i++) {
			switch (kind) {
			case "linear":
				p = linear(i, mM);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			case "sinusoidal":
				p = sinusoidal(i, mM);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			case "sigmoid":
				p = sigmoid(i, mM, alpha, beta);
				x = origX + convertToUnits(p, 0, 100, true);
				break;
			case "radial":
				p = radial(i, mM);
				x = origX + convertToUnits(p, 0, 100, false);
				break;
			}
			y = origY - PApplet.map(i, mM[0], mM[1], 0, 100);
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

	// ***** Setters
	public void setMinWeight(float minIn) {
		minEdgeWeight = minIn;
	}

	public void setMaxWeight(float maxIn) {
		maxEdgeWeight = maxIn;
	}

	public void setMaxMinWeight(float minIn, float maxIn) {
		this.minEdgeWeight = minIn;
		this.maxEdgeWeight = maxIn;
	}

	public void setSigmoidAlphaBeta(float alpha, float beta) {
		this.alpha = alpha;
		this.beta = beta;
	}

	public void setMinCommunitySize(int val) {
		this.minCommunitySize = val;

	}

	public void setMaxCommunitySize(int val) {
		this.maxCommunitySize = val;

	}

}
