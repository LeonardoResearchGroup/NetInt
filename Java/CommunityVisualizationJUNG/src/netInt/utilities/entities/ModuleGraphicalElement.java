package netInt.utilities.entities;

/**
 * The class  used to represent a graphical element in the module.
 * @author lfrivera
 *
 */
public class ModuleGraphicalElement
{
	
	/**
	 * The new button name.
	 */
	private String buttonName;
	
	/**
	 * The type of the new button.
	 */
	private String buttonType;
	
	/**
	 * The name of the method invoked by the button.
	 */
	private String methodName;
	
	/**
	 * Indicates whether the method have parameters.
	 */
	private boolean withParameters;
	
	/**
	 * A string list that contains the methods and classes for obtaining the parameters for the method to be invoked.
	 * e.g. getType:netInt.utilities.GraphmlKey;getName:netInt.utilities.GraphmlKey
	 */
	private String methodParameters;
	
	/**
	 * The name of the method's class invoked by the button.
	 */
	private String className;
	
	/**
	 * Indicates whether the invocation is realized in a external jar.
	 */
	private boolean externalClass;
	
	/**
	 * The horizontal position of the element.
	 */
	private int posx;
	
	/**
	 * The vertical position of the element.
	 */
	private int posy;
	
	/**
	 * The height of the element.
	 */
	private int height;
	
	/**
	 * The width of the element.
	 */
	private int width;

	/**
	 * Blank constructor of the class.
	 */
	public ModuleGraphicalElement() {
		super();
	}

	/**
	 * The full constructor of the class.
	 * @param buttonName The label to be used in the element.
	 * @param buttonType The type of the element.
	 * @param methodName The method to be invoked by the element.
	 * @param withParameters Indicates whether the method have parameters.
	 * @param methodParameters A string list that contains the methods and classes for obtaining the parameters for the method to be invoked.
	 * e.g. getType:netInt.utilities.GraphmlKey;getName:netInt.utilities.GraphmlKey
	 * @param className The class of the method to be invoked.
	 * @param external Indicates whether the invocation is realized in a external jar.
	 * @param posx The horizontal position of the element.
	 * @param posy The vertical position of the element.
	 * @param height The height of the element.
	 * @param width The width of the element.
	 */
	public ModuleGraphicalElement(String buttonName, String buttonType, String methodName, boolean withParameters, String methodParameters, String className, boolean externalClass, int posx,
			int posy, int height, int width) {
		super();
		this.buttonName = buttonName;
		this.buttonType = buttonType;
		this.methodName = methodName;
		this.withParameters = withParameters;
		this.methodParameters = methodParameters;
		this.className = className;
		this.externalClass = externalClass;
		this.posx = posx;
		this.posy = posy;
		this.height = height;
		this.width = width;
	}



	// Getters & Setters section.

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public boolean isWithParameters() {
		return withParameters;
	}

	public void setWithParameters(boolean withParameters) {
		this.withParameters = withParameters;
	}
	
	public String getMethodParameters() {
		return methodParameters;
	}

	public void setMethodParameters(String methodParameters) {
		this.methodParameters = methodParameters;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}

	
	public boolean isExternalClass() {
		return externalClass;
	}

	public void setExternalClass(boolean externalClass) {
		this.externalClass = externalClass;
	}

	public int getPosx() {
		return posx;
	}

	public void setPosx(int posx) {
		this.posx = posx;
	}

	public int getPosy() {
		return posy;
	}

	public void setPosy(int posy) {
		this.posy = posy;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	
	
}
