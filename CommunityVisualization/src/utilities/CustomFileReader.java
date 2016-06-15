package utilities;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;

public class CustomFileReader {

	String fileContent;
	File file;

	public CustomFileReader(String filename) {
		file = new File(filename); // for ex foo.txt
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			fileContent = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getContent(){
		return fileContent;
	}
}