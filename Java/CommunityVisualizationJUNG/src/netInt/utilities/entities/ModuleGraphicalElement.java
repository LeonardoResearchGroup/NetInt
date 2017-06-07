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
	 * Blank constructor of the class.
	 */
	public ModuleGraphicalElement() {
		super();
	}

	/**
	 * The full constuctor of the class.
	 * @param buttonName The new button name.
	 * @param buttonType The type of the new button.
	 * @param methodName The name of the method invoked by the button.
	 * @param className The name of the method's class invoked by the button.
	 */
	public ModuleGraphicalElement(String buttonName, String buttonType, String methodName, String className) {
		super();
		this.buttonName = buttonName;
		this.buttonType = buttonType;
		this.methodName = methodName;
		this.className = className;
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
	
}
