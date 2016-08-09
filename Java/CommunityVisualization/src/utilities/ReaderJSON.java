package utilities;

import org.json.simple.JSONObject;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

// API http://alex-public-doc.s3.amazonaws.com/json_simple-1.1/index.html

public class ReaderJSON {

	JSONParser parser;
	String JSONSource;

	public ReaderJSON(String path) {
		// JSONSource = path;
		parser = new JSONParser();
		try {
			Object source = parser.parse(path);
			JSONObject jsonObject = (JSONObject) source;
			// KeySet
			// System.out.println(jsonObject.keySet());
			Iterator<JSONObject> itr1 = jsonObject.keySet().iterator();

			while (itr1.hasNext()) {
				Object tmp1 = itr1.next();
				System.out.println("Palette name: " + tmp1);
				JSONObject palette = (JSONObject) jsonObject.get(tmp1);
				// System.out.println(palette.keySet());
				Iterator<JSONObject> itr2 = palette.keySet().iterator();
				// System.out.println(palette);
				while (itr2.hasNext()) {
					try {
						Object tmp2 = itr2.next();
						System.out.println("  palette: " + tmp2);
						JSONArray colors = (JSONArray) palette.get(tmp2);
						System.out.println("    colors: " + colors);
					} catch (Exception e) {
						System.out.println(" No Array***");
					}
				}
				System.out.println("...");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}