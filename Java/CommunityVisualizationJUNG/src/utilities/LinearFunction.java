package utilities;

public class LinearFunction {
	
	private double b;
	private double m;
	
	/**
	 * Construct a linear function from two bi-dimensional points
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public LinearFunction(double x1, double x2, int y1, int y2){
		m = (y2 - y1) / (x2 - x1);
		// y = mx * b
		b = y1 - m*x1;
	}
	
	
	public double f(double x){
		double y = m*x + b;
		return y;
	}

}
