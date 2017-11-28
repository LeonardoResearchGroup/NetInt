package classes;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class MyTestClass {

	public MyTestClass()
	{}
	
	public void generateRadomBackgroundColor()
	{
		
		System.out.println(">External Jar: Invoked Jar Class...");
		
		try {
			
			System.out.println(">External Jar: Generating random color...");
			Random r = new Random();
			int random = r.nextInt(1024);
			
			System.out.println(" >External Jar: Calling MethodInvoker...");
			Object[] parameters = {random};
			MethodInvoker.getInstance().invokeWithParameters("netInt.visualElements.gui.UserSettingsProxy", "setColorBackground", parameters);
			
			System.out.println(">External Jar: Method invoked.");
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			
			System.out.println(">External Jar: Could not invoke the method: " + e.getMessage());
			
		}
		
	}
	
	public void generateLuminosity(String type, String name)
	{
		System.out.println(">External Jar: getType:" + type);
		System.out.println(">External Jar: getName:" + name);
	}
	
}
