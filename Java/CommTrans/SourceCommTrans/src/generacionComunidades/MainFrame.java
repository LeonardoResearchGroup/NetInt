package generacionComunidades;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.SystemUtils;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Dimension;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private File csvEdgeFile;
	private File csvNodeFile;
	private JTextArea textAreaResults;
	private JLabel labelVinculos;
	private JLabel labelNodos;
	private JPanel panel;
	private JButton btnArchivoNodos;
	private JButton btnValidar;
	private JComboBox <String> comboBoxWeights;
	private CsvReader edgesValidator;
	private CsvReader nodesValidator;
	private boolean validated;
	private boolean readyForValidation;
	private JButton btnGenerarLouvainIcesi;
	private JButton btnGenerarLouvainEafit;
	private JButton btnGenerarInfomapEafit;
	private JLabel lblValido;
	private JLabel lblDeteccinDeComunidades;
	Executable exec = new Executable();
	private JLabel lblCargando;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 317, 542);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panel = new JPanel();
		
		
		
		JButton btnArchivoVinculos = new JButton("Archivo de V\u00EDnculos");
		
		labelVinculos = new JLabel("");
		
		labelNodos = new JLabel("");
		
		btnGenerarLouvainIcesi = new JButton("Louvain Icesi");
		btnGenerarLouvainIcesi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGenerarLouvainIcesiAction(e);
			}
		});
		btnGenerarLouvainIcesi.setEnabled(false);
		
		btnGenerarLouvainEafit = new JButton("Louvain Eafit");
		btnGenerarLouvainEafit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGenerarLouvainEafitAction(e);
			}
		});
		btnGenerarLouvainEafit.setEnabled(false);
		
		btnGenerarInfomapEafit = new JButton("Infomap Eafit");
		btnGenerarInfomapEafit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGenerarInfomapEafitAction(e);
			}
		});
		btnGenerarInfomapEafit.setEnabled(false);
		
		btnArchivoNodos = new JButton("Archivo de Nodos");
		btnArchivoNodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnArchivoNodosAction(e);
			}
		});
		
		btnValidar = new JButton("Validar Archivos");
		btnValidar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnValidarAction(e);
			}
		});
		
		comboBoxWeights = new  JComboBox<String>();
		comboBoxWeights.setMaximumSize(new Dimension(28, 20));
		comboBoxWeights.setToolTipText("");
		comboBoxWeights.addItem("Binario");
		comboBoxWeights.setEnabled(false);
		comboBoxWeights.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
		
		lblValido = new JLabel("No Válido");
		
		lblDeteccinDeComunidades = new JLabel("Detecci\u00F3n de Comunidades");
		lblDeteccinDeComunidades.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		lblCargando = new JLabel("Cargando...");
		lblCargando.setVisible(false);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(56)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblValido)
								.addComponent(btnGenerarLouvainIcesi, 0, 0, Short.MAX_VALUE)
								.addComponent(btnGenerarLouvainEafit, 0, 0, Short.MAX_VALUE)
								.addComponent(btnGenerarInfomapEafit, 0, 0, Short.MAX_VALUE)
								.addComponent(comboBoxWeights, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnValidar, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
								.addComponent(btnArchivoVinculos, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelVinculos, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
								.addComponent(btnArchivoNodos, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelNodos, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(100)
							.addComponent(lblCargando))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(43)
							.addComponent(lblDeteccinDeComunidades)))
					.addContainerGap(49, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDeteccinDeComunidades)
					.addGap(43)
					.addComponent(btnArchivoVinculos)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelVinculos, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnArchivoNodos)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelNodos, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addGap(62)
					.addComponent(btnValidar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblValido)
					.addGap(41)
					.addComponent(comboBoxWeights, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnGenerarLouvainIcesi)
					.addComponent(btnGenerarLouvainEafit)
					.addComponent(btnGenerarInfomapEafit)
					.addGap(18)
					.addComponent(lblCargando)
					.addContainerGap(81, Short.MAX_VALUE))
		);
		lblValido.setVisible(false);
		panel.setLayout(gl_panel);
		btnArchivoVinculos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnArchivoVinculosAction(arg0);
			}
		});
		contentPane.setLayout(new CardLayout(0, 0));
		contentPane.add(panel, "name_22168491457713");
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, "name_112771585507934");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnVolverAction(e);
			}
		});
		
		JLabel lblResultados = new JLabel("Resultados");
		lblResultados.setFont(new Font("Tahoma", Font.BOLD, 15));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(23)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblResultados))
					.addContainerGap(25, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(119, Short.MAX_VALUE)
					.addComponent(btnVolver)
					.addGap(109))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(21)
					.addComponent(lblResultados)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 381, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnVolver)
					.addContainerGap(20, Short.MAX_VALUE))
		);
		
		textAreaResults = new JTextArea();
		scrollPane.setViewportView(textAreaResults);
		panel_1.setLayout(gl_panel_1);
	}
	
	private void btnArchivoVinculosAction(ActionEvent ae){
		JFileChooser jfc = new JFileChooser();
        int returnVal = jfc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            csvEdgeFile = jfc.getSelectedFile();
            validated = false;
            enableDisableDetection();
            edgesValidator = new CsvReader(csvEdgeFile,"\\|");
            labelVinculos.setText(csvEdgeFile.getName());
            //readyForValidation = false;
            
            lblValido.setText("");
            System.out.println(csvEdgeFile.getPath());
        }
        
	}
	private void btnArchivoNodosAction(ActionEvent ae){
		JFileChooser jfc = new JFileChooser();
        int returnVal = jfc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            csvNodeFile = jfc.getSelectedFile();
            validated = false;
            enableDisableDetection();
            nodesValidator = new CsvReader(csvNodeFile,"\\|");
            labelNodos.setText(csvNodeFile.getName());
            //readyForValidation = false;
            
            lblValido.setText("");
            System.out.println(csvNodeFile.getPath());
        }
        
	}
	private void btnValidarAction(ActionEvent ae){
		validated = false;
		enableDisableDetection();
		if( edgesValidator != null){
			validated = edgesValidator.validateEdgeFile() ;
			if(nodesValidator != null){
				validated = validated && nodesValidator.validateNodeFile();
				validated = validated && edgesValidator.checkContainability(nodesValidator.getNits(), edgesValidator.getNits());
			}
			
		}else{
			validated = false;
		}
		System.out.println(validated);
		enableDisableDetection();
	}
	private void btnGenerarLouvainIcesiAction(ActionEvent ae){
			lblCargando.setVisible(true);
			JFileChooser jfc = new JFileChooser();
			//jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        
	        //set it to be a save dialog
	        jfc.setDialogType(JFileChooser.SAVE_DIALOG);
	        //set a default filename (this is where you default extension first comes in)
	        jfc.setSelectedFile(new File("comunidades.graphml"));
	        //Set an extension filter, so the user sees other XML files
	        jfc.setFileFilter(new FileNameExtensionFilter("graphml file","graphml"));
	        int returnVal = jfc.showSaveDialog(this);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	        	
	           //labelNodos.setText(jfc.getSelectedFile().getName());
	        	System.out.println(jfc.getSelectedFile());
	        	if(!exec.checkRscript()){
		        	JOptionPane.showMessageDialog(null, "Indique por favor en dónde tiene instalado Rscript");
		        	JFileChooser jfcR = new JFileChooser();
		            int returnValR = jfcR.showOpenDialog(this);
		            if(returnValR == JFileChooser.APPROVE_OPTION) {
		            	exec.setMainCommand("\""+ jfcR.getSelectedFile().getPath() +"\"");
		            }else{
		            	return;
		            }
		        }
		        String nodeFilePath = "";
		        if(csvNodeFile != null){
		        	nodeFilePath = csvNodeFile.getPath();
		        }
		        String script = "generarComunidadesYGraphml.R";
		        String result = exec.Execute(script, csvEdgeFile.getPath(), nodeFilePath, (String)comboBoxWeights.getSelectedItem(),
		        		jfc.getSelectedFile().getPath());
		        textAreaResults.setText(result);
		        
				CardLayout cl = (CardLayout)(contentPane.getLayout());
			    cl.show(contentPane, "name_112771585507934");
	        }
	        lblCargando.setVisible(false);
	        
	        
		        
        
	
		
		
	}
	
	private void btnGenerarLouvainEafitAction(ActionEvent ae){
		lblCargando.setVisible(true);
		JFileChooser jfc = new JFileChooser();
		//jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        //set it to be a save dialog
        jfc.setDialogType(JFileChooser.SAVE_DIALOG);
        //set a default filename (this is where you default extension first comes in)
        jfc.setSelectedFile(new File("comunidades.graphml"));
        //Set an extension filter, so the user sees other XML files
        jfc.setFileFilter(new FileNameExtensionFilter("graphml file","graphml"));
        int returnVal = jfc.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	
           //labelNodos.setText(jfc.getSelectedFile().getName());
        	System.out.println(jfc.getSelectedFile());
        	if(!exec.checkRscript()){
	        	JOptionPane.showMessageDialog(null, "Indique por favor en dónde tiene instalado Rscript");
	        	JFileChooser jfcR = new JFileChooser();
	            int returnValR = jfcR.showOpenDialog(this);
	            if(returnValR == JFileChooser.APPROVE_OPTION) {
	            	exec.setMainCommand("\""+ jfcR.getSelectedFile().getPath() +"\"");
	            }else{
	            	return;
	            }
	        }
	        String nodeFilePath = "";
	        if(csvNodeFile != null){
	        	nodeFilePath = csvNodeFile.getPath();
	        }
	        
	        String script = "InfomapComms.R";
	        String result = exec.Execute(script, csvEdgeFile.getPath(), nodeFilePath, (String)comboBoxWeights.getSelectedItem(),
	        		jfc.getSelectedFile().getPath());
	        
	        textAreaResults.setText(result);
	        
			CardLayout cl = (CardLayout)(contentPane.getLayout());
		    cl.show(contentPane, "name_112771585507934");
        }
        lblCargando.setVisible(false);
        
        
	        
    

	
	
	}
	
	private void btnGenerarInfomapEafitAction(ActionEvent ae){
		lblCargando.setVisible(true);
		JFileChooser jfc = new JFileChooser();
		//jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        //set it to be a save dialog
        jfc.setDialogType(JFileChooser.SAVE_DIALOG);
        //set a default filename (this is where you default extension first comes in)
        jfc.setSelectedFile(new File("comunidades.graphml"));
        //Set an extension filter, so the user sees other XML files
        jfc.setFileFilter(new FileNameExtensionFilter("graphml file","graphml"));
        int returnVal = jfc.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	
           //labelNodos.setText(jfc.getSelectedFile().getName());
        	System.out.println(jfc.getSelectedFile());
        	if(!exec.checkRscript()){
	        	JOptionPane.showMessageDialog(null, "Indique por favor en dónde tiene instalado Rscript");
	        	JFileChooser jfcR = new JFileChooser();
	            int returnValR = jfcR.showOpenDialog(this);
	            if(returnValR == JFileChooser.APPROVE_OPTION) {
	            	exec.setMainCommand("\""+ jfcR.getSelectedFile().getPath() +"\"");
	            }else{
	            	return;
	            }
	        }
	        String nodeFilePath = "";
	        if(csvNodeFile != null){
	        	nodeFilePath = csvNodeFile.getPath();
	        }
	        
	        String script = "LouvainComms.R";
	        String result = exec.Execute(script, csvEdgeFile.getPath(), nodeFilePath, (String)comboBoxWeights.getSelectedItem(),
	        		jfc.getSelectedFile().getPath());
	        textAreaResults.setText(result);
	        
			CardLayout cl = (CardLayout)(contentPane.getLayout());
		    cl.show(contentPane, "name_112771585507934");
        }
        lblCargando.setVisible(false);
        
        
	        
    

	
	
	}
	
	private void enableDisableDetection(){
		comboBoxWeights.setEnabled(validated);
		btnGenerarLouvainIcesi.setEnabled(validated);
		btnGenerarLouvainEafit.setEnabled(validated);
		btnGenerarInfomapEafit.setEnabled(validated);
//		if(readyForValidation){
			lblValido.setVisible(true);
			if(validated && edgesValidator!= null){
				for(int i=2; i < edgesValidator.getHeaders().length; i++){
					comboBoxWeights.addItem(edgesValidator.getHeaders()[i]);
				}
				lblValido.setText("Válido");
				lblValido.setForeground(Color.green);
			}else{
				if(edgesValidator != null){
					for(int i=2; i < edgesValidator.getHeaders().length; i++){
						comboBoxWeights.removeItem(edgesValidator.getHeaders()[i]);
					}
				}
				lblValido.setText("No válido");
				lblValido.setForeground(Color.red);
			}
//		}else{
//			lblValido.setText("");
//		}
		
	}
	private void btnVolverAction(ActionEvent e){
		CardLayout cl = (CardLayout)(contentPane.getLayout());
	    cl.show(contentPane, "name_22168491457713");
	}
}
