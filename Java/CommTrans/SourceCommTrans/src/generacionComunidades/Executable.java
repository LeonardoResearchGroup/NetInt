package generacionComunidades;

import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.SystemUtils;

public class Executable {
	
	String mainCommand = "\"Rscript\"";

	
	public Executable(){
		if(SystemUtils.IS_OS_WINDOWS){
			mainCommand = "\"Rscript\"";
		}else{
			mainCommand = "Rscript";
		}
		
	}
	
	public void setMainCommand(String mainCommand) {
		this.mainCommand = mainCommand;
	}

	public String readResult(Process proc) throws IOException{
		String result = "";
		String err = "";
		try{
			ReadStream s1 = new ReadStream("stdin", proc.getInputStream ());
			ReadStream s2 = new ReadStream("stderr", proc.getErrorStream ());
			s1.start ();
			s2.start ();
			proc.waitFor();  
			result = s1.getOutput();
			err = s2.getOutput();
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {
		    if(proc != null)
		    	proc.destroy();
		}
		if(result.equals("")){
			result = err;
		}
		return result;
		
		
//		
//		String result ="";
//		String err = "";
//		String line = null;

//		InputStream stderr = proc.getErrorStream();
//		
//		InputStreamReader errsr = new InputStreamReader(stderr, "UTF-8");
//		
//		BufferedReader berror = new BufferedReader(errsr);
//		  while ((line = berror.readLine()) != null) {
//			    System.out.println(line);
//			    err = err + line + "\n";
//			  }
//		//System.out.println("<OUTPUT>");
//		stderr.close();
//		errsr.close();
//		berror.close();
//		
//		try {
//			proc.waitFor();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		  
//		InputStream stdin = proc.getInputStream();
//		InputStreamReader isr = new InputStreamReader(stdin, "UTF-8");
//		BufferedReader br = new BufferedReader(isr);
//		while ( (line = br.readLine()) != null){
//		     System.out.println(line);
//		     result = result += line.replace("\"", "").replace("[1]", "") + "\n";
//		}
////		isr.close();
//		br.close();
////		stdin.close();
////		stderr.close();
////		errsr.close();
//		
//		if(result.equals("")){
//			result = err;
//		}
//		return result;
	}
	
	public String readError(Process proc) throws IOException{
		String result ="";
		String line = null;
		InputStream stderr = proc.getErrorStream();
		InputStreamReader errsr = new InputStreamReader(stderr, "UTF-8");
		BufferedReader berror = new BufferedReader(errsr);
		  while ((line = berror.readLine()) != null) {
//			    System.out.println(line);
			    result = result += line + "\n";
			  }
		stderr.close();
		errsr.close();
		berror.close();
		return result;
		
	}
	
	public boolean checkRscript(){
		boolean exist = false;
		try {
//			String command = "\"Rscrip\"";
			Process proc = Runtime.getRuntime().exec(mainCommand);
			proc.waitFor();
			//				Process proc = Runtime.getRuntime().exec("cmd /c dir");
			//readError(proc);
			if(proc.exitValue() == 1){
				exist = true;
			}
			System.out.println(proc.exitValue());
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exist;
	}
	
	public String Execute(String script, String edgeFile, String nodeFile, String selection, String outFile){
		String result = "";
		try {
			File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
			String command = "";
			String thisFilePath = "";
	    	String edgeFileString =edgeFile;
	    	String outFileString =outFile;
	    	String selectionString =selection;
	    	String nodeFileString = nodeFile;
	    	
	    	
	    	thisFilePath = URLDecoder.decode(jarDir.getPath(), "UTF-8");
	        if(SystemUtils.IS_OS_WINDOWS){
//		    	command = mainCommand +  " generarComunidadesYGraphml.R \""+edgeFile+"\" \""+ outFile +  "\" " +
//		        		selection + " \""+ nodeFile + "\" ";
//		    	thisFilePath = "\""+URLDecoder.decode(jarDir.getPath(), "UTF-8")+"\"";
		    	edgeFileString ="\""+edgeFile+"\"";
		    	outFileString ="\""+outFile+"\"";
		    	selectionString ="\""+selection+"\"";
		    	nodeFileString = "\""+nodeFile+"\"";
		    	
	        }
//		    	else{
//		        command = mainCommand + " generarComunidadesYGraphml.R "+edgeFile+" "+  outFile+
//		        		" "+ selection + " " + nodeFile ;
//	        }
	        //String decoded = URLDecoder.decode(queryString, "UTF-8");
	        
	    	String[] commands = new String[]{mainCommand, "--vanilla",script,edgeFileString,outFileString,selectionString,nodeFileString};	
	        System.out.println("Current path real "+ URLDecoder.decode(jarDir.getPath(), "UTF-8"));
	        System.out.println("Current path " + System.getProperty("user.dir"));
	        System.out.println(command);
//			Process proc = Runtime.getRuntime().exec(command
//					,null,
//					new File(thisFilePath));
			Process proc = Runtime.getRuntime().exec(commands
					,null,
					new File(thisFilePath));
	//				Process proc = Runtime.getRuntime().exec("cmd /c dir");
	
			result = readResult(proc);
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	        
			
		
	}
	private class ReadStream implements Runnable {
	    String name;
	    InputStream is;
	    Thread thread;
	    String output;
	    public String getOutput() {
			return output;
		}
		public ReadStream(String name, InputStream is) {
	        this.name = name;
	        this.is = is;
	        output = "";
	    }       
	    public void start () {
	        thread = new Thread (this);
	        thread.start ();
	    }       
	    public void run () {
	        try {
	            InputStreamReader isr = new InputStreamReader (is, "UTF-8");
	            BufferedReader br = new BufferedReader (isr);   
	            while (true) {
	                String s = br.readLine ();
	                if (s == null) break;
	                System.out.println ("[" + name + "] " + s);
	                output = output + s.replace("\"", "").replace("[1]", "") + "\n";
	            }
	            is.close ();    
	        } catch (Exception ex) {
	            System.out.println ("Problem reading stream " + name + "... :" + ex);
	            ex.printStackTrace ();
	        }
	    }
	}

}


