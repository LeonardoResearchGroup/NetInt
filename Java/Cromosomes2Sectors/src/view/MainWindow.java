package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Operator;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	private DefaultListModel<String> model;
	private JButton addButton;
	private JButton clearButton;
	private JButton transformButton;
	private Operator operator;
	
	public MainWindow(Operator operator)
	{
		super("Cromosomes-Sectors");
		setSize(500,200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		model = new DefaultListModel<String>();
		setContentPane(loadPanel());
		this.operator = operator;
		
	}
	
	private JPanel loadPanel()
	{
		JPanel response = new JPanel();
		response.setSize(getMaximumSize());
		response.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JList<String> jList= new JList<String>();
		jList.setSize(getMaximumSize());
		jList.setModel(model);
		
		JScrollPane scroll = new JScrollPane(jList);
		scroll.setPreferredSize(new Dimension(450,120));
		response.add(scroll);
		
		
		addButton = new JButton("Add Cluster");
		addButton.addActionListener(addEvent());
		response.add(addButton);
		
		clearButton = new JButton("Clear All");
		clearButton.addActionListener(clearEvent());
		response.add(clearButton);
		
		transformButton = new JButton("Transform");
		transformButton.addActionListener(transformEvent());
		response.add(transformButton);
		
		return response;
	}
	
	private ActionListener addEvent()
	{
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String input = JOptionPane.showInputDialog("Cromosome (comma-separated):");
				
				boolean correct = true;
				
				if(input != null)
				{
					if(!input.equals(""))
					{
						model.addElement(input);	
						
					}
					else
					{
						correct = false;
					}
				}
				else
				{
					correct = false;
				}
				
				if(!correct)
				{
					showMessage(0, "Write something!");
				}
				
			}
		};
	}
	
	private ActionListener clearEvent()
	{
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int actualSize =  model.getSize();
				
				for(int i = 0 ; i < actualSize ; i++)
				{
					model.removeElementAt(0);
				}
				
			}
		};
	}
	
	private ActionListener transformEvent()
	{
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(model.size()<1)
				{
					showMessage(0, "Empty list!");
				}
				else
				{
					try
					{
						ArrayList<String> list = new ArrayList<String>();
						
						for(int i = 0; i < model.size() ; i++)
						{
							list.add(model.getElementAt(i));
						}
						
						JFileChooser chooser = new JFileChooser();
						FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
						chooser.setFileFilter(filter);
						chooser.showOpenDialog(null);
						File selected = chooser.getSelectedFile();
						
						if(selected == null)
						{
							showMessage(0, "Select a .csv file with the names of sectors!");
						}
						else
						{
							operator.transform(list, selected);
							showMessage(1, "Done!");
						}
						
						return ;
						
						
						
					}catch(Exception ex)
					{
						showMessage(0, ex.getMessage());
					}
				}
				
			}
		};
	}
	
	private void showMessage(int type, String message)
	{
		int messageType = type == 0 ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(null, message, "Message",messageType);
	}
	
}
