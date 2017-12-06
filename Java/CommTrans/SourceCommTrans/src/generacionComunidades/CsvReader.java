package generacionComunidades;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;



public class CsvReader {
	

	private HashSet <String> nits;

	private String[] headers;
	

	private String sep;
	private File file;
	
	public CsvReader(File file, String sep){
		this.sep = sep;
		this.file = file;
		nits =  new HashSet<String>();
		headers = new String[0];
	}
	
	/**
	 * Read and validate the csvEdfeFile
	 * @param csvEdfeFile
	 * @param sep
	 * @return
	 */
	public boolean validateEdgeFile(){
		 
		boolean typeValidation = true; 
		boolean formatValidation = true; 
		int lines = 0;
		boolean contentValidation = true;
		
		try {
			 BufferedReader br = new BufferedReader(new FileReader(file));
			 String line;
			 line = br.readLine();
			 lines++;
			 System.out.println(sep);
			 System.out.println(line);
			 headers = line.split(sep);
	
		     while ((line = br.readLine()) != null) {
		    	 lines++;
		    	 //System.out.println(line);
		         String[] transaction = line.split(sep);
	        	 nits.add(transaction[0]);		         
	        	 nits.add(transaction[1]);
		         
		         for(int i = 2; i < transaction.length; i++){
//		        	 if(!transaction[i].matches("\\d+(?:[\\.\\,]\\d+)?")){
//		        		 System.out.println(transaction[i]);
//		        		 return false;
//		        	 }

		         }
		
		     }
		     br.close();
		}
		 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			typeValidation = false;
		}
		catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			formatValidation=false;
		}
		
		if(lines < 2){
			contentValidation = false;
		}
		
		return contentValidation && formatValidation && typeValidation;
		
//		 BufferedReader br;
//		try {
//			br = new BufferedReader(new FileReader(file));
//		
//		CsvParserSettings settings = new CsvParserSettings();
//		//the file used in the example uses '\n' as the line separator sequence.
//		//the line separator sequence is defined here to ensure systems such as MacOS and Windows
//		//are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
//		settings.getFormat().setLineSeparator("\n");
//		settings.getFormat().setDelimiter('|');
//		
//		// creates a CSV parser
//		CsvParser parser = new CsvParser(settings);
//		
//		// parses all rows in one go.
//		List<String[]> allRows = parser.parseAll(br);
//		headers = allRows.get(0);
//		allRows.remove(0);
//		for(String[] row : allRows){
//	        	 nits.add(row[0]);
//	        	 nits.add(row[1]);
//	         
//		}
//		
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(nits.size());
//		return true;
	}
	
	public boolean validateNodeFile(){
		 
		boolean formatValidation = true; 
		int lines = 0;
		boolean contentValidation = true;
		
		try {
			 BufferedReader br = new BufferedReader(new FileReader(file));
			 String line;
			 line = br.readLine();
			 lines++;
			 headers = line.split(sep);
	
		     while ((line = br.readLine()) != null) {
		    	 lines++;
		         String[] transaction = line.split(sep);
		        	 nits.add(transaction[0]);
		     }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			formatValidation=false;
		}
		
		if(lines < 2){
			contentValidation = false;
		}
		
		return contentValidation && formatValidation;
		
//		 BufferedReader br;
//			try {
//				br = new BufferedReader(new FileReader(file));
//			
//			CsvParserSettings settings = new CsvParserSettings();
//			//the file used in the example uses '\n' as the line separator sequence.
//			//the line separator sequence is defined here to ensure systems such as MacOS and Windows
//			//are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
//			settings.getFormat().setLineSeparator("\n");
//			settings.getFormat().setDelimiter('|');
//			
//			// creates a CSV parser
//			CsvParser parser = new CsvParser(settings);
//			
//			// parses all rows in one go.
//			List<String[]> allRows = parser.parseAll(br);
//			headers = allRows.get(0);
//			allRows.remove(0);
//			for(String[] row : allRows){
//				nits.add(row[0]);
//			}
//			
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return true;
	}
	
	/**
	 * Check if b is containded by a
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean checkContainability(HashSet<String> a, HashSet<String> b){
		for(String e : b){
			//System.out.println( b.get(i) );
			if(!a.contains(e)){
				return false;
			}
		}
		return true;
	}
	
	public HashSet<String> getNits() {
		return nits;
	}
	public String[] getHeaders() {
		return headers;
	}
	
	

}
