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
package netInt.visualElements.gui;

import processing.core.PApplet;

/**
 * 
 * Singleton pattern.
 * 
 * @author Felipe Rivera
 *
 */
public class ChooseHelper {

	private static final ChooseHelper INSTANCE = new ChooseHelper();

	// Private constructor.
	private ChooseHelper() {
	}

	public static ChooseHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * Shows the Open File window of the OS
	 * 
	 * @param applet
	 *            The PApplet calling the Input Menu. It must have a method
	 *            named as the second parameter passed to PApplet.selectInput()
	 */
	public void showFileChooser(PApplet applet) {
		applet.selectInput("Select a graphml file to process:", "selectImport");
	}

	/**
	 * Calls the System File Browser. This is used to load or save files
	 * 
	 * @param save
	 *            either or not show the save dialog
	 * @param extension
	 *            the file extension of the file to be loaded
	 * @return The path to the chosen file
	 */
	public String showJFileChooser(boolean save, String extension) {
		String path = null;
		javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
		chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
				extension, extension);
		chooser.setFileFilter(filter);
		int action;

		if (save) {
			action = chooser.showSaveDialog(null);
		} else {
			action = chooser.showOpenDialog(null);
		}

		if (action == javax.swing.JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getAbsolutePath();
		}
		return path;
	}
}
