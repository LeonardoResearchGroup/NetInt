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
package netInt.utilities;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * Singleton pattern.
 * 
 * @author lfrivera
 *
 */
public class ClassLoader {

	private static ClassLoader instance;

	/**
	 * Private constructor of the class.
	 */
	private ClassLoader() {
	}

	/**
	 * Allows to obtain the unique instance of the class.
	 * 
	 * @return Unique instance.
	 */
	public static ClassLoader getInstance() {
		if (instance == null) {
			instance = new ClassLoader();
		}

		return instance;
	}

	/**
	 * Based on:
	 * https://stackoverflow.com/questions/11016092/how-to-load-classes-at-
	 * runtime-from-a-folder-or-jar
	 * https://stackoverflow.com/questions/5266532/can-i-get-all-methods-of-a-
	 * class
	 * 
	 * Allows to load classes from a jar.
	 * 
	 * @param jarPath
	 *            Path of the jar file.
	 * @return List of classes.
	 * @throws IOException
	 *             Throwable exception
	 * @throws ClassNotFoundException
	 *             Throwable exception
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<Class> loadClasses(String jarPath) throws IOException, ClassNotFoundException {
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + jarPath + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		ArrayList<Class> classes = new ArrayList<Class>();

		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			String className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace('/', '.');
			Class c = cl.loadClass(className);
			classes.add(c);

		}

		jarFile.close();

		return classes;
	}

	/**
	 * Allows to invoke an external method from a module.
	 * 
	 * @param className
	 *            Class of the method.
	 * @param methodName
	 *            Method to invoke.
	 * @param parameters
	 *            Parameters of the method. Must be an Object array.
	 * @return Response value of the method.
	 * @throws IllegalAccessException
	 *             Throwable exception
	 * @throws InstantiationException
	 *             Throwable exception
	 * @throws SecurityException
	 *             Throwable exception
	 * @throws NoSuchMethodException
	 *             Throwable exception
	 * @throws InvocationTargetException
	 *             Throwable exception
	 * @throws IllegalArgumentException
	 *             Throwable exception
	 */
	public Object invokeWithParametersExternal(String className, String methodName, Object[] parameters)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {

		return performInvocationWithParameters(ExternalClasses.getInstance().getClasses().get(className), methodName,
				parameters);
	}

	/**
	 * Allows to invoke an internal method from a module.
	 * 
	 * @param className
	 *            Class of the method.
	 * @param methodName
	 *            Method to invoke.
	 * @param parameters
	 *            Parameters of the method. Must be an Object array.
	 * @return Response value of the method.
	 * @throws IllegalAccessException
	 *             Throwable exception
	 * @throws InstantiationException
	 *             Throwable exception
	 * @throws SecurityException
	 *             Throwable exception
	 * @throws NoSuchMethodException
	 *             Throwable exception
	 * @throws InvocationTargetException
	 *             Throwable exception
	 * @throws IllegalArgumentException
	 *             Throwable exception
	 * @throws ClassNotFoundException
	 *             Throwable exception
	 */
	public Object invokeWithParametersInternal(String className, String methodName, Object[] parameters)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException {

		return performInvocationWithParameters(Class.forName(className), methodName, parameters);
	}

	/**
	 * Allows to invoke an external method from a module.
	 * 
	 * @param className
	 *            Class of the method.
	 * @param methodName
	 *            Method to invoke.
	 * @return Response value of the method.
	 * @throws IllegalAccessException
	 *             Throwable exception
	 * @throws InstantiationException
	 *             Throwable exception
	 * @throws SecurityException
	 *             Throwable exception
	 * @throws NoSuchMethodException
	 *             Throwable exception
	 * @throws InvocationTargetException
	 *             Throwable exception
	 * @throws IllegalArgumentException
	 *             Throwable exception
	 */
	public Object invokeWithoutParametersExternal(String className, String methodName)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {

		return performInvocationNoParameters(ExternalClasses.getInstance().getClasses().get(className), methodName);
	}

	/**
	 * Allows to invoke an internal method.
	 * 
	 * @param className
	 *            Class of the method.
	 * @param methodName
	 *            Method to invoke.
	 * @return Response value of the method.
	 * @throws IllegalAccessException
	 *             Throwable exception
	 * @throws InstantiationException
	 *             Throwable exception
	 * @throws SecurityException
	 *             Throwable exception
	 * @throws NoSuchMethodException
	 *             Throwable exception
	 * @throws InvocationTargetException
	 *             Throwable exception
	 * @throws IllegalArgumentException
	 *             Throwable exception
	 * @throws ClassNotFoundException
	 *             Throwable exception
	 */
	public Object invokeWithoutParametersInternal(String className, String methodName)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException {

		return performInvocationNoParameters(Class.forName(className), methodName);
	}

	/**
	 * Performs the execution of a method withoud parameters.
	 * 
	 * @param theClass
	 *            Class of the method.
	 * @param methodName
	 *            Method to invoke.
	 * @return Response value of the method.
	 * @throws IllegalAccessException
	 *             Throwable exception
	 * @throws IllegalArgumentException
	 *             Throwable exception
	 * @throws InvocationTargetException
	 *             Throwable exception
	 * @throws NoSuchMethodException
	 *             Throwable exception
	 * @throws SecurityException
	 *             Throwable exception
	 * @throws InstantiationException
	 *             Throwable exception
	 */
	private Object performInvocationNoParameters(Class<?> theClass, String methodName)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException {

		// Based on:
		// http://www.mkyong.com/java/how-to-use-reflection-to-call-java-method-at-runtime/

		// No parameters
		Class<?> noparams[] = {};

		Object instance = theClass.newInstance();

		Method method = theClass.getDeclaredMethod(methodName, noparams);

		Object[] param = null;

		return method.invoke(instance, param);
	}

	/**
	 * 
	 * @param theClass
	 *            Class of the method.
	 * @param methodName
	 *            Method to invoke.
	 * @param parameters
	 *            The parameters of the method.
	 * @return Response value of the method.
	 * @throws IllegalAccessException
	 *             Throwable exception
	 * @throws InstantiationException
	 *             Throwable exception
	 * @throws SecurityException
	 *             Throwable exception
	 * @throws NoSuchMethodException
	 *             Throwable exception
	 * @throws InvocationTargetException
	 *             Throwable exception
	 * @throws IllegalArgumentException
	 *             Throwable exception
	 */
	private Object performInvocationWithParameters(Class<?> theClass, String methodName, Object[] parameters)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {

		// Based on:
		// https://stackoverflow.com/questions/16207283/how-to-pass-multiple-parameters-to-a-method-in-java-reflections

		Class<?> params[] = new Class[parameters.length];

		for (int i = 0; i < parameters.length; i++) {

			if (parameters[i].getClass() == Integer.class) {

				params[i] = Integer.TYPE;

			}

			if (parameters[i].getClass() == String.class) {

				params[i] = String.class;

			}

			if (parameters[i].getClass() == Boolean.class) {

				params[i] = Boolean.class;

			}

			if (parameters[i].getClass() == Random.class) {

				params[i] = Random.class;

			}

			if (parameters[i].getClass() == Float.class) {

				params[i] = Float.class;

			}

			if (parameters[i].getClass() == Object.class) {

				params[i] = Object.class;

			}

		}

		Object _instance = theClass.newInstance();

		Method myMethod = theClass.getDeclaredMethod(methodName, params);

		return myMethod.invoke(_instance, parameters);
	}

}
