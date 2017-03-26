/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 *
 * It makes extensive use of free libraries such as Processing, Jung, ControlP5, JOGL, 
 * Tinkerpop and many others. For details see the copyrights folder. 
 *
 * Contributors:
 * 	Juan Salamanca, Cesar Loaiza, Luis Felipe Rivera, Javier Diaz
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *
 * version alpha
 *******************************************************************************/
package netInt.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;

/**
 * Class that allows to serialize and deserialize an object.
 * 
 * @author lfrivera
 *
 */
public class SerializeHelper {

	private static final SerializeHelper INSTANCE = new SerializeHelper();

	private SerializeHelper() {
	}

	public static SerializeHelper getInstance() {
		return INSTANCE;
	}

	public void serialize(SerializeWrapper object, String fullPath, String extension) throws FileNotFoundException {
		XStream xstream = new XStream();
		xstream.alias("wrapper", SerializeWrapper.class);
		xstream.toXML(object, new FileOutputStream(new File(fullPath + "." + extension)));
	}
	
	public SerializeWrapper deserialize(String fullPath) throws FileNotFoundException
	{
		SerializeWrapper response = null;
		XStream xstream = new XStream();
		xstream.alias("wrapper", SerializeWrapper.class);
		response = (SerializeWrapper) xstream.fromXML(new FileInputStream(new File(fullPath)));
		return response;
	}

}
