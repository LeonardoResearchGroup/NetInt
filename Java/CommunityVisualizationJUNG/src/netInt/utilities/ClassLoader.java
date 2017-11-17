/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.utilities;

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
public class ClassLoader {

	private static ClassLoader instance;
	
	/**
	 * Private constructor of the class.
	 */
	private ClassLoader()
	{
	}
	
	/**
	 * Allows to obtain the unique instance of the class.
	 * @return Unique instance.
	 */
	public static ClassLoader getInstance()
	{
		if(instance == null)
		{
			instance = new ClassLoader();
		}
		
		return instance;
	}
	
	/**
	 * Based on: 
	 * https://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
	 * https://stackoverflow.com/questions/5266532/can-i-get-all-methods-of-a-class
	 * 
	 * Allows to load classes from a jar.
	 * @param jarPath Path of the jar file.
	 * @return List of classes.
	 * @throws IOException Throwable exception
	 * @throws ClassNotFoundException Throwable exception
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<Class> loadClasses(String jarPath) throws IOException, ClassNotFoundException
	{
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + jarPath +"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		ArrayList<Class> classes = new ArrayList<Class>();
		
		while (e.hasMoreElements()) {
		    JarEntry je = e.nextElement();
		    if(je.isDirectory() || !je.getName().endsWith(".class")){
		        continue;
		    }
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    Class c = cl.loadClass(className);
		   classes.add(c);
		    
		}
		
		jarFile.close();
		
		return classes;
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
