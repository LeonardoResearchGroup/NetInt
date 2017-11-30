/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package classes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;


/**
 * 
 * Singleton pattern.
 * 
 * @author lfrivera
 *
 */
public class MethodInvoker {

	private static MethodInvoker instance;
	
	/**
	 * Private constructor of the class.
	 */
	private MethodInvoker()
	{
	}
	
	/**
	 * Allows to obtain the unique instance of the class.
	 * @return Unique instance.
	 */
	public static MethodInvoker getInstance()
	{
		if(instance == null)
		{
			instance = new MethodInvoker();
		}
		
		return instance;
	}
	
	/**
	 * Allows to obtain a java class object by its name.
	 * @param name The name of the class.
	 * @return Class object.
	 * @throws ClassNotFoundException Throwable exception.
	 */
	@SuppressWarnings("rawtypes")
	public Class obtainClassByName(String name) throws ClassNotFoundException
	{
		return Class.forName(name);
	}
	
	
	/**
	 * 
	 * Allows to invoke a method from a module.
	 * 
	 * @param theClass Class of the method.
	 * @param methodName Method to invoke.
	 * @param parameters Parameters of the method. Must be an Object array.
	 * @return Response value of the method.
	 * @throws IllegalAccessException Throwable exception
	 * @throws InstantiationException Throwable exception
	 * @throws SecurityException Throwable exception
	 * @throws NoSuchMethodException Throwable exception
	 * @throws InvocationTargetException Throwable exception
	 * @throws IllegalArgumentException Throwable exception
	 * @throws ClassNotFoundException myMethod
	 */ 
	public Object invokeWithParameters(String theClass, String methodName, Object[] parameters) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException
	{
		
		//Based on: https://stackoverflow.com/questions/16207283/how-to-pass-multiple-parameters-to-a-method-in-java-reflections
		
        Class<?> params[] = new Class[parameters.length];
        
        System.out.println(">External Jar: Matching parameters...");
        for (int i = 0; i < parameters.length; i++) {
           
        	if (parameters[i].getClass() == Integer.class) {
                
        		System.out.println(">External Jar: Integer parameter detected.");
        		params[i] = Integer.TYPE;
           
        	} 
        	
        	if (parameters[i].getClass() == String.class) {
                
        		System.out.println(">External Jar: String parameter detected.");
        		params[i] = String.class;
            
        	}
        	
        	
        	if (parameters[i].getClass() == Boolean.class) {
                
        		System.out.println(">External Jar: Boolean parameter detected.");
        		params[i] = Boolean.class;
            
        	}
        	
        	
        	if (parameters[i].getClass() == Random.class) {
        		System.out.println(">External Jar: Random parameter detected.");
        		params[i] = Random.class;
            
        	}
        	
        	
        	if (parameters[i].getClass() == Float.class) {
        		System.out.println(">External Jar: Float parameter detected.");
        		params[i] = Float.class;
            
        	}
        	
        	
        	if (parameters[i].getClass() == Object.class) {
        		System.out.println(">External Jar: Object parameter detected.");
        		params[i] = Object.class;
            
        	}
          
        }
        
        System.out.println(">External Jar: Detecting class...");
        Class<?> cls = Class.forName(theClass);
        System.out.println(">External Jar: Class detected.");
        
        System.out.println(">External Jar: Obtaining class instance...");
        Object _instance = cls.newInstance();
        System.out.println(">External Jar: Instance obtained.");
        
        System.out.println(">External Jar: Obtaining declared method...");
        Method myMethod = cls.getDeclaredMethod(methodName, params);
        System.out.println(">External Jar: Method obtained.");
        
       return  myMethod.invoke(_instance, parameters);
       
	}
	
	/**
	 * Based on: http://www.mkyong.com/java/how-to-use-reflection-to-call-java-method-at-runtime/
	 * 
	 * Allows to invoke a method from a module.
	 * 
	 * @param className Class of the method.
	 * @param methodName Method to invoke.
	 * @return Response value of the method.
	 * @throws IllegalAccessException Throwable exception
	 * @throws InstantiationException Throwable exception
	 * @throws SecurityException Throwable exception
	 * @throws NoSuchMethodException Throwable exception
	 * @throws InvocationTargetException Throwable exception
	 * @throws IllegalArgumentException Throwable exception
	 * @throws ClassNotFoundException Throwable exception
	 */
	@SuppressWarnings({ "rawtypes"})
	public Object invokeWithoutParameters(String className, String methodName) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException
	{
		
		 Class<?> c = Class.forName(className);
		
		//No parameters
		Class noparams[] = {};
		
		Object instance = c.newInstance();
		
		Method method = c.getDeclaredMethod(methodName, noparams);
		
		Object[] param = null;
		
		return method.invoke(instance, param);
	}
	
	
}
