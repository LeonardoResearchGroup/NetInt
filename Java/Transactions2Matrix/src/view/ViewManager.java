package view;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

public final class ViewManager {

	public static final int INPUT_TYPE_STRING = 0;
	public static final int INPUT_TYPE_INT = 1;
	public static final int INPUT_TYPE_DOUBLE = 2;
	public static final int INPUT_TYPE_FLOAT = 3;
	
	public static Object getInput(int inputType, String questionMessage) throws NumberFormatException
	{
		Object response = null;
		
		String input = javax.swing.JOptionPane.showInputDialog(questionMessage);
		
		if(inputType == INPUT_TYPE_STRING)
		{
			response = input;
		}
		
		if(inputType == INPUT_TYPE_INT)
		{
			response = Integer.parseInt(input);
		}
		
		if(inputType == INPUT_TYPE_DOUBLE)
		{
			response = Double.parseDouble(input);
		}
		
		if(inputType == INPUT_TYPE_FLOAT)
		{
			response = Float.parseFloat(input);
		}
		
		return response;
	}
	
	public static void showErrorMessage(String message, String title)
	{
		javax.swing.JOptionPane.showMessageDialog(null, message, title, javax.swing.JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showInformationMessage(String message, String title)
	{
		javax.swing.JOptionPane.showMessageDialog(null, message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static File getFile()
	{
		javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CVS FILES", "csv", "text");
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(null);
		return chooser.getSelectedFile();
	}
	
}
