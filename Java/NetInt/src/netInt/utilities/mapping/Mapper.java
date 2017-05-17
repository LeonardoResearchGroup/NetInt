package netInt.utilities.mapping;

import java.util.ArrayList;

import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import processing.core.PApplet;

/**
 * Singleton pattern
 * 
 * @author jsalam
 *
 */

public class Mapper {

	// ATTRIBUTE
	public static final String NODE = "Node";
	public static final String EDGE = "Edge";
	// CONVERTER
	public static final String LINEAR = "linear";
	public static final String SINUSOIDAL = "sinusoidal";
	public static final String LOGARITMIC = "logarithmic";
	public static final String RADIAL = "radial";
	public static final String SIGMOID = "sigmoid";

	private String[] converters = {"linear","sinusoidal","logarithmic","radial","sigmoid"};
	// MAXs & MINs nodes
	private NumericalCollection nodeAttributesMin, nodeAttributesMax;
	private CategoricalCollection nodeCategoricalAttributes;
	// MAXs & MINs edges
	private NumericalCollection edgeAttributesMin, edgeAttributesMax;
	private CategoricalCollection edgeCategoricalAttributes;
	// MAXs & MINs communities
	// private NumericalCollection commAttributesMin, commAttributesMax;
	// private CategoricalCollection commCategoricalAttributes;

	// Other attributes for sigmoid filter
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
	 * @param converter
	 *            filter name
	 * @param val
	 *            the number to be mapped
	 * @param factor
	 *            the desired max output value
	 * @param graphElementClassName
	 * @param graphAttribute
	 * @return the value mapped equals to a number between 0 and 1 X factor
	 */
	public float convert(String converter, float val, float factor, String graphElementClassName, String graphAttribute) {
		float rtn = Float.NEGATIVE_INFINITY;
		switch (converter) {
		case "linear":
			if (graphElementClassName.equals(Mapper.NODE))
				rtn = linear(val, getMinMaxForNodes(graphAttribute));
			if (graphElementClassName.equals(Mapper.EDGE))
				rtn = linear(val, getMinMaxForEdges(graphAttribute));
			break;
		case "sinusoidal":
			if (graphElementClassName.equals(Mapper.NODE))
				rtn = sinusoidal(val, getMinMaxForNodes(graphAttribute));
			if (graphElementClassName.equals(Mapper.EDGE))
				rtn = sinusoidal(val, getMinMaxForEdges(graphAttribute));
			break;
		case "logarithmic":
			rtn = log(val);
			break;
		case "radial":
			if (graphElementClassName.equals(Mapper.NODE))
				rtn = radial(val, getMinMaxForNodes(graphAttribute));
			if (graphElementClassName.equals(Mapper.EDGE))
				rtn = radial(val, getMinMaxForEdges(graphAttribute));
			break;
		case "sigmoid":
			if (graphElementClassName.equals(Mapper.NODE))
				rtn = sigmoid(val, getMinMaxForNodes(graphAttribute));
			if (graphElementClassName.equals(Mapper.EDGE))
				rtn = sigmoid(val, getMinMaxForEdges(graphAttribute));
			break;
		}
		
		rtn = rtn * factor;
		
		if (graphElementClassName.equals(Mapper.NODE)){
			// Maximal node diameter
			if (rtn > factor)
				rtn = factor;
			// Minimal diameter or thickness
			if (rtn < 3)
				rtn = 3;
		}

		if (graphElementClassName.equals(Mapper.EDGE)){
			// Maximal node visibility
			if (rtn > factor)
				rtn = factor;
			// Minimal diameter or thickness
			if (rtn < 1)
				rtn = 1;
		}

		if (rtn < 0) {
			System.out.println(this.getClass().getName() + "   *** Error in " + converter + " filter trying to map : "
					+ val + ", using the graph atttribute: " + graphAttribute + ". Value mapped to 0");
			rtn = 0;
		}

		return rtn;
	}

	/**
	 * Returns the min and max value of a graph element. The attribute is
	 * retrieved from a NumericalCollection that stores attributes in a TreeMap.
	 * The keys of the TreeMap are the concatenation of the following Strings:
	 * graphElementClassName + "_" + AttributeName. For example the weight of an
	 * edge is stored with the key Edge_Weight
	 * 
	 * @param graphElementClassName
	 * 
	 * @param attributeName
	 *            The attribute of either a node or edge. Example "weight",
	 *            "degree", "inDegree"
	 * @return Array of floats [0] min, [1] max
	 */
	public float[] getMinMaxForNodes(String attributeName) {
		float[] rtn = new float[2];
		try {
			rtn[0] = nodeAttributesMin.getValueofAttribute(attributeName);
			rtn[1] = nodeAttributesMax.getValueofAttribute(attributeName);
		} catch (NullPointerException e) {
			System.out.println(
					this.getClass().getName() + "> wrong attribute name: " + attributeName + " at getMaxMin()");
		}
		if (rtn[0] == rtn[1]) {
			rtn[0] = 0;
			rtn[1] = 5;
		}
		return rtn;
	}

	public float[] getMinMaxForEdges(String attributeName) {
		float[] rtn = new float[2];
		try {
			rtn[0] = edgeAttributesMin.getValueofAttribute(attributeName);
			rtn[1] = edgeAttributesMax.getValueofAttribute(attributeName);
		} catch (NullPointerException e) {
			System.out.println(
					this.getClass().getName() + "> wrong attribute name: " + attributeName + " at getMaxMin()");
		}
		if (rtn[0] == rtn[1]) {
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

	// Sigmoid mapping
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

	// Logarithmic mapping
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
	 * This method serves to normalize a number to a given scale defined by its
	 * lower and higher boundaries. It is mainly used to draw widgets of the
	 * mapping filter
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

	/**
	 * Sets the min and max value stored in a collection of attributes for
	 * nodes. It initializes the collection of attributes in case it is equal to
	 * null
	 * 
	 * @param gElem
	 */
	public void setMaxMinNodeAttributes(Node gElem) {
		// if the min and max collections are not initialized
		if (nodeAttributesMin == null) {
			// min values
			nodeAttributesMin = new NumericalCollection();
		}
		if (nodeAttributesMax == null) {
			// max values
			nodeAttributesMax = new NumericalCollection();
		}
		// If all collections are initialized
		if (nodeAttributesMin.getSize() >= 0 && nodeAttributesMax.getSize() >= 0) {
			// Go over all the attributes of this GraphElement
			for (int i = 0; i < gElem.getAttributeKeys().length; i++) {
				// For each attribute key get its value
				String key = (String) gElem.getAttributeKeys()[i];
				Object value = gElem.getAttribute(key);
				if (NumericalCollection.isNumerical(value)) {
					try {
						if (value instanceof Double) {
							// If Double convert to float
							Double rtnObj = (Double) value;
							Float attrFloat = rtnObj.floatValue();
							// add to collection of min attributes else to
							// collection of max Attributes
							if (!nodeAttributesMin.addLowerValue(key, attrFloat)) {
								nodeAttributesMax.addHigherValue(key, attrFloat);
							}

						} else if (value instanceof Integer) {
							// If Integer
							Integer attrInteger = (Integer) value;
							Float attrFloat = attrInteger.floatValue();
							// add to collection of min attributes else to
							// collection of max Attributes
							if (!nodeAttributesMin.addLowerValue(key, attrFloat)) {
								nodeAttributesMax.addHigherValue(key, attrFloat);
							}

						} else if (value instanceof Float) {
							// If Float
							Float attrFloat = (Float) value;
							// add to collection of min attributes else to
							// collection of max Attributes
							if (!nodeAttributesMin.addLowerValue(key, attrFloat)) {
								nodeAttributesMax.addHigherValue(key, attrFloat);
							}

						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.println(this.getClass().getName() + " Node Attribute named: " + key
								+ " does not match the available Mapper data type: Double,Float,Integer");
					}
				}
			}
		}
	}

	/**
	 * Sets the min and max value stored in a collection of attributes for
	 * edges. It initializes the collection of attributes in case it is equal to
	 * null
	 * 
	 * @param gElem
	 */
	public void setMaxMinEdgeAttributes(Edge gElem) {
		// if the min and max collections are not initialized
		if (edgeAttributesMin == null) {
			// min values
			edgeAttributesMin = new NumericalCollection();
		}
		if (edgeAttributesMax == null) {
			// max values
			edgeAttributesMax = new NumericalCollection();
		}
		// If all collections are initialized
		if (edgeAttributesMin.getSize() >= 0 && edgeAttributesMax.getSize() >= 0) {
			// Go over all the attributes of this GraphElement
			for (int i = 0; i < gElem.getAttributeKeys().length; i++) {
				// For each attribute key get its value
				String key = (String) gElem.getAttributeKeys()[i];
				Object value = gElem.getAttribute(key);
				if (NumericalCollection.isNumerical(value)) {
					try {
						if (value instanceof Double) {
							// If Double convert to float
							Double rtnObj = (Double) value;
							Float attrFloat = rtnObj.floatValue();
							// add to collection of min attributes else to
							// collection of max Attributes
							if (!edgeAttributesMin.addLowerValue(key, attrFloat)) {
								edgeAttributesMax.addHigherValue(key, attrFloat);
							}

						} else if (value instanceof Integer) {
							// If Integer
							Integer attrInteger = (Integer) value;
							Float attrFloat = attrInteger.floatValue();
							// add to collection of min attributes else to
							// collection of max Attributes
							if (!edgeAttributesMin.addLowerValue(key, attrFloat)) {
								edgeAttributesMax.addHigherValue(key, attrFloat);
							}

						} else if (value instanceof Float) {
							// If Float
							Float attrFloat = (Float) value;
							// add to collection of min attributes else to
							// collection of max Attributes
							if (!edgeAttributesMin.addLowerValue(key, attrFloat)) {
								edgeAttributesMax.addHigherValue(key, attrFloat);
							}

						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.println(this.getClass().getName() + " Edge Attribute named: " + key
								+ " does not match the available Mapper data type: Double,Float,Integer");
					}
				}
			}
		}
	}
	
	/**
	 * Sets the min and max value stored in a collection of node attributes. It
	 * initializes the collection if attributes in case it is equal to null
	 * 
	 * @param gElem
	 */
	public void setCategoricalNodeAttributes(Node gElem) {
		// if the categorical collection is not initialized
		if (nodeCategoricalAttributes == null) {
			// categorical values
			nodeCategoricalAttributes = new CategoricalCollection();
		}
		// If all collections are initialized
		if (nodeCategoricalAttributes.getSize() >= 0) {
			// Go over all the attributes of this GraphElement
			for (int i = 0; i < gElem.getAttributeKeys().length; i++) {
				// For each attribute key get its value
				String key = (String) gElem.getAttributeKeys()[i];
				Object value = gElem.getAttribute(key);
				if (CategoricalCollection.isCategorical(value)) {
					// If String store the value in a TreeSet of categorical
					// values for that attribute/key
					nodeCategoricalAttributes.addValue(key, value.toString());
				}
			}
		}
	}

	/**
	 * Sets the min and max value stored in a collection of edge attributes. It
	 * initializes the collection if attributes in case it is equal to null
	 * 
	 * @param gElem
	 */
	public void setCategoricalEdgeAttributes(Edge gElem) {
		// if the categorical collection is not initialized
		if (edgeCategoricalAttributes == null) {
			// categorical values
			edgeCategoricalAttributes = new CategoricalCollection();
		}
		// If all collections are initialized
		if (edgeCategoricalAttributes.getSize() >= 0) {
			// Go over all the attributes of this GraphElement
			for (int i = 0; i < gElem.getAttributeKeys().length; i++) {
				// For each attribute key get its value
				String key = (String) gElem.getAttributeKeys()[i];
				Object value = gElem.getAttribute(key);
				if (CategoricalCollection.isCategorical(value)) {
					// If String store the value in a TreeSet of categorical
					// values for that attribute/key
					edgeCategoricalAttributes.addValue(key, value.toString());
				}
			}
		}
	}

	public NumericalCollection getNodeAttributesMin() throws NullPointerException {
		return nodeAttributesMin;
	}

	public NumericalCollection getNodeAttributesMax() throws NullPointerException {
		return nodeAttributesMax;
	}

	public CategoricalCollection getNodeCategoricalAttributes() throws NullPointerException {
		return nodeCategoricalAttributes;
	}

	public NumericalCollection getEdgeAttributesMin() throws NullPointerException {
		return edgeAttributesMin;
	}

	public NumericalCollection getEdgeAttributesMax() throws NullPointerException {
		return edgeAttributesMax;
	}

	public CategoricalCollection getEdgeCategoricalAttributes() throws NullPointerException {
		return edgeCategoricalAttributes;
	}

	public String[] getConvertersList() {
		return converters;
	}

	public ArrayList <String> getNodeNumericalAttributeKeys(){
		return nodeAttributesMin.getAttributeKeys();
	}
	
	public ArrayList <String> getEdgeNumericalAttributeKeys(){
		return edgeAttributesMin.getAttributeKeys();
	}
	
	/**
	 * Get the list of graph element attributes stores in a categorical collection
	 * 
	 * @param GraphElementClassName
	 *            The name of the graph element class. It must be either "Node"
	 *            or "Edge"
	 * @return
	 */
	public ArrayList <String> getCategorialAttributesKeys(String GraphElementClassName){
		return nodeCategoricalAttributes.getAttributeKeys(GraphElementClassName);
	}

}