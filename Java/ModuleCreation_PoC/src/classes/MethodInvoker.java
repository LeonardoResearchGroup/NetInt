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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
	 * Based on: http://www.mkyong.com/java/how-to-use-reflection-to-call-java-method-at-runtime/
	 * 
	 * Allows to invoke a method from a module.
	 * 
	 * @param c Class of the method.
	 * @param methodName Method to invoke.
	 * @param parameters Parameters of the method. Must be an Object array.
	 * @return Response value of the method.
	 * @throws IllegalAccessException Throwable exception
	 * @throws InstantiationException Throwable exception
	 * @throws SecurityException Throwable exception
	 * @throws NoSuchMethodException Throwable exception
	 * @throws InvocationTargetException Throwable exception
	 * @throws IllegalArgumentException Throwable exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object invokeWithParameters(Class c, String methodName, Object[] parameters) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		//Object parameter
		Class[] paramObject = new Class[1];
		paramObject[0] = Object.class;
		
		Object instance = c.newInstance();
		
		Method method = c.getDeclaredMethod(methodName, paramObject);
		
		Object param = parameters;
		
		return method.invoke(instance, param);
	}
	
	/**
	 * Based on: http://www.mkyong.com/java/how-to-use-reflection-to-call-java-method-at-runtime/
	 * 
	 * Allows to invoke a method from a module.
	 * 
	 * @param c Class of the method.
	 * @param methodName Method to invoke.
	 * @return Response value of the method.
	 * @throws IllegalAccessException Throwable exception
	 * @throws InstantiationException Throwable exception
	 * @throws SecurityException Throwable exception
	 * @throws NoSuchMethodException Throwable exception
	 * @throws InvocationTargetException Throwable exception
	 * @throws IllegalArgumentException Throwable exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object invokeWithoutParameters(Class c, String methodName) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
	{
		//No parameters
		Class noparams[] = {};
		
		Object instance = c.newInstance();
		
		Method method = c.getDeclaredMethod(methodName, noparams);
		
		Object[] param = null;
		
		return method.invoke(instance, param);
	}
	
	
}
