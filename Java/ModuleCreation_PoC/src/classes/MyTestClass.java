package classes;

import java.lang.reflect.InvocationTargetException;

public class MyTestClass {

	public MyTestClass()
	{}
	
	public void generateRadomBackgroundColor()
	{
		
		System.out.println("Invoke Jar Class");
		
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = MethodInvoker.getInstance().obtainClassByName("netInt.visualElements.gui.UserSettingsProxy");
			MethodInvoker.getInstance().invokeWithoutParameters(c, "setColorBackground");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
