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
