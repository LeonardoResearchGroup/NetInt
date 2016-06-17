package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.entity.Matrix;

public final class Operator {

	public static void TransformFile(File file, int transactionsColumn, int payerColumn, int receiverColumn, boolean removeQuotes, boolean geneticAlgorithm) throws IOException, URISyntaxException
	{
		
		ArrayList<String> lines = readLines(file);
		
		if(removeQuotes)
		{
			lines = removeQuotes(lines);
		}
		
		Matrix matrix = new Matrix(geneticAlgorithm);
		
		for(String s:lines)	
		{
			String[] split = s.split(";");
			
			int numTransactions = Integer.parseInt(split[transactionsColumn]);
			
			matrix.appendRow(split[payerColumn], split[receiverColumn], numTransactions);
		}
		
		saveMatrix(matrix, geneticAlgorithm);
	}
	
	private static ArrayList<String> readLines(File file) throws IOException
	{
		ArrayList<String> list = new ArrayList<>();
		
		InputStream fis=new FileInputStream(file);
	    BufferedReader br=new BufferedReader(new InputStreamReader(fis));
	    
	    boolean firstTime = true;
	    
	    for (String line = br.readLine(); line != null; line = br.readLine()) {
	      
	    	if(firstTime)
	    	{
	    		firstTime = false;
	    	}
	    	else
	    	{
	    		 list.add(line);
	    	}
	    	
	     }

	     br.close();
		
		if(list.isEmpty())
		{
			list = null;
		}
		
		return list;
	}
	
	private static ArrayList<String> removeQuotes(ArrayList<String> lines)
	{
		ArrayList<String> list = new ArrayList<>();
		
		for(int i=0; i<lines.size();i++)
		{
			
			String line = lines.get(i);
			
			char[] chars = line.toCharArray();
			
			String temp = "";
			
			for(char c : chars)
			{
				if((int)c != 34)
				{
					temp += c;
				}
			}
			
			if(temp.length() > 0)
			{
				list.add(temp);
			}
			
		}
		
		
		if(list.isEmpty())
		{
			list = null;
		}
		
		return list;
	}
	
	private static  void saveMatrix(Matrix myMatrix, boolean geneticAlgorithm) throws IOException, URISyntaxException
	{
		
		int[][] matrix = myMatrix.toIntMatrix(false,false);
		
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		
		String folderName = "Matrix_"+dateFormat.format(date);
		
		File folder = new File((desktopPath + "\\" + folderName));
		folder.mkdirs();
		
		String fileName = "Matrix_"+dateFormat.format(date)+".txt";
		
		File out = new File(folder.getAbsolutePath() + "\\" + fileName);
		
		out.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(out);
		 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
		boolean firstTime = true;
		
		for (int i = 0; i < matrix.length; i++) {
			
			String row = formatRow(matrix, i);
			
			if(firstTime)
			{
				bw.write(row);
				firstTime = false;
			}
			else
			{
				bw.newLine();
				bw.write(row);
				
			}
			
		}
	 
		bw.close();
		
		//------------------------------------------------------------------------------------------
		
		String metadataName = "Metadata_"+dateFormat.format(date)+".txt";
		
		File out2 = new File(folder.getAbsolutePath() + "\\" + metadataName);
		
		out2.createNewFile();
		
		FileOutputStream fos2 = new FileOutputStream(out2);
		 
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));

		bw2.write("Number of rows: "+matrix.length);
		bw2.newLine();
		
		int numColumns = geneticAlgorithm ? matrix[0].length-1 : matrix[0].length;
		
		bw2.write("Number of columns (Without acc column in GA): "+numColumns);
		
		bw2.newLine();
		
		bw2.write("Genetic Algorithm: "+geneticAlgorithm);
	 
		bw2.close();
		
		//------------------------------------------------------------------------------------------
		
		String columnsName = "Columns_"+dateFormat.format(date)+".txt";
		
		File out3 = new File(folder.getAbsolutePath() + "\\" + columnsName);
		
		out3.createNewFile();
		
		FileOutputStream fos3 = new FileOutputStream(out3);
		 
		BufferedWriter bw3 = new BufferedWriter(new OutputStreamWriter(fos3));
	 
		firstTime = true;
		
		for (String column: myMatrix.getColumns()) {
			
			if(firstTime)
			{
				bw3.write(column);
				firstTime = false;
			}
			else
			{
				bw3.newLine();
				bw3.write(column);
				
			}
			
		}
	 
		bw3.close();
		
	}
	
	private static String formatRow(int[][] matrix, int row)
	{
		String response = "";
		
		boolean firstTime = true;
		
		for(int i = 0; i < matrix[0].length; i++)
		{
			
			if(firstTime)
			{
				response += matrix[row][i];
				firstTime = false;
			}
			else
			{
				response += " " +  matrix[row][i];
			}
			
		}
		
		return response;
	}
	
	
}
