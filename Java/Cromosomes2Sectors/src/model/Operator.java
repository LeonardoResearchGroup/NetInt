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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Operator {

	
	public Operator()
	{
		
	}
	
	public void transform(ArrayList<String> clusters, File sectorsFile) throws IOException
	{
		
		ArrayList<String> sectors = readLines(sectorsFile);
	
		ArrayList<String> communities = new ArrayList<String>();
		
		for(int i = 0 ; i < clusters.size() ; i++)
		{
			
			String cluster = clusters.get(i);
			
			String transformedCluster = "Community " + (i+1) + ": ";
			
			String[] elements = cluster.split(",");
			
			boolean firstTime = true;
			
			for(String element : elements)
			{
				
				int intElement = Integer.parseInt(element);
				String convertedElement = sectors.get(intElement);
				
				if(firstTime)
				{
					transformedCluster += convertedElement;
					firstTime = false;
				}
				else
				{
					transformedCluster += ", " + convertedElement;
				}
				
			}
			
			communities.add(transformedCluster);
			
		}
		
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		
		String fileName = "Communities_"+dateFormat.format(date)+".txt";
		
		File out = new File(desktopPath + "\\" + fileName);
		
		out.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(out);
		 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
		boolean firstTime = true;
		
		for (String community:communities) {
			
			if(firstTime)
			{
				bw.write(community);
				firstTime = false;
			}
			else
			{
				bw.newLine();
				bw.write(community);
			}
		}
		
		bw.flush();
		bw.close();
		
	}
	
	private ArrayList<String> readLines(File file) throws IOException
	{
		ArrayList<String> list = new ArrayList<>();
		
		InputStream fis=new FileInputStream(file);
	    BufferedReader br=new BufferedReader(new InputStreamReader(fis));
	   
	    
	    for (String line = br.readLine(); line != null; line = br.readLine()) {
	      
	    		 list.add(line);
	     }

	     br.close();
		
		if(list.isEmpty())
		{
			list = null;
		}
		
		return list;
	}
	
}
