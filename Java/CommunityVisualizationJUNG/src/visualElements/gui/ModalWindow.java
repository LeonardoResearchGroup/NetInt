package visualElements.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import processing.core.PApplet;

public class ModalWindow extends JDialog{

	private static final long serialVersionUID = 1L;

	private JTextField[] fields;
	private JComboBox<String> combo;
	private JButton[] buttons;
	
	private boolean cancel;
	
	private HashMap<String, Integer> layouts;
	
	
	public ModalWindow(String title, Dimension size, PApplet applet)
	{
		setModal(true);
		setTitle(title);
		setSize(size.width, size.height);
		setUndecorated(true);
		
		layouts = new HashMap<String, Integer>();
		layouts.put("CIRCULAR", 0);
		layouts.put("SPRING", 1);
		layouts.put("FRUCHTERMAN_REINGOLD", 2);
		
		setLocationRelativeTo(applet.frame);
		
		cancel = false;

		setContentPane(init());
		
	
	}
	
	private JPanel init()
	{
		JPanel contentPane = new JPanel();
		contentPane.setSize(getMaximumSize());	
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel label1 = new JLabel("Community filter:");
		JLabel label2 = new JLabel("Node label:");
		JLabel label3 = new JLabel("Visual Layout:");
		
		fields = new JTextField[2];
		for(int i = 0; i< fields.length; i++)
		{
			fields[i] = new JTextField(10);
		}
		
		combo = new JComboBox<String>();
		combo.addItem("CIRCULAR");
		combo.addItem("SPRING");
		combo.addItem("FRUCHTERMAN_REINGOLD");
		
		buttons = new JButton[2];
		buttons[0] = new JButton("Continue");
		buttons[1] = new JButton("Cancel");
		
		
		contentPane.add(label1);
		contentPane.add(fields[0]);
		contentPane.add(label2);
		contentPane.add(fields[1]);
		contentPane.add(label3);
		contentPane.add(combo);
		
		for(JButton button:buttons)
		{
			button.addActionListener(buttonsAction());
			contentPane.add(button);
		}
		
		contentPane.setBorder(BorderFactory.createLineBorder(Color.blue.darker(), 5, false));
		
		return contentPane;
	}
	
	public void open()
	{
		setVisible(true);
	}
	
	private boolean validateFields()
	{
		boolean ok = true;
		
		for(JTextField field:fields)
		{
			if(field.getText().equals(""))
			{
				ok = false;
			}
		}
		
		return ok;
	}
	
	private void close()
	{
		setVisible(false);
	}
	
	private ActionListener buttonsAction() 
	{
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String sourceName = ((JButton)e.getSource()).getText();
				if(sourceName.equals("Continue"))
				{
					if(!validateFields())
					{
						JOptionPane.showMessageDialog(null, "You most complete all fields!","Error",JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						close();
					}
				}
				else
				{
					cancel = true;
					close();
				}
					
			}
		};
	}
	
	public Object[] getData()
	{
		Object[] response = null;
		
		if(!cancel)
		{
			response = new Object[3];
			response[0] = fields[0].getText();
			response[1] = fields[1].getText();
			response[2] = layouts.get((String) combo.getSelectedItem());
		}
		
		return response;
	}
	
}
