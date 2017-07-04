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
	 * The name of the method's class invoked by the button.
	 */
	private String className;
	
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
	 * @param className The class of the method to be invoked.
	 * @param posx The horizontal position of the element.
	 * @param posy The vertical position of the element.
	 * @param height The height of the element.
	 * @param width The width of the element.
	 */
	public ModuleGraphicalElement(String buttonName, String buttonType, String methodName, String className, int posx,
			int posy, int height, int width) {
		super();
		this.buttonName = buttonName;
		this.buttonType = buttonType;
		this.methodName = methodName;
		this.className = className;
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

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
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
