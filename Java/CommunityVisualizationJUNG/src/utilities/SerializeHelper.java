package utilities;

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
