package org.openidams.supervisor.gui.output;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Savepoint;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import org.openidams.utilities.GUIUtilities;
import org.openidams.utilities.Properties;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class ProceduresOutputContainer extends JFrame {
    private static final int FONT_SIZE = 12;
	BorderLayout borderLayout1 = new BorderLayout();
    JPanel upperPanel = new JPanel();
    JButton hideButton = new JButton();
    JButton closeAllButton = new JButton();
    JPanel lowerPanel = new JPanel();
    JLabel infoText = new JLabel();
    JTabbedPane componentsContainer = new JTabbedPane();

    BorderLayout borderLayout2 = new BorderLayout();
    JPanel panElementUpperPanel = new JPanel();
    JButton closePanElementTool = new JButton();
    JScrollPane componentContainerSP = new JScrollPane();

    BorderLayout borderLayout3 = new BorderLayout();
    BorderLayout borderLayout4 = new BorderLayout();

    JFrame self=this;
    
    public ProceduresOutputContainer() {
    	super("OpenIDAMS Output");
    	try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Add new text output and return the text area reference
     * to allow incremental text output
     * @param name
     * @return
     */
    public JTextArea addUpdateableTextOutput(String name){
    	JPanel panel=new JPanel();
    	JPanel upperContainer=new JPanel();
    	JPanel lowerContainer=new JPanel();
    	upperContainer.setLayout(new BorderLayout());
    	lowerContainer.setLayout(new FlowLayout());
    	JTextArea ta=new JTextArea();
    	ta.setFont( new Font( "Monospaced", Font.PLAIN, FONT_SIZE ));
    	
    	ComponentRelatedButton panelCloseBut=new ComponentRelatedButton("Close",panel);
    	ComponentRelatedButton panelExportButtonPdf=new ComponentRelatedButton("Save as PDF",ta);
    	ComponentRelatedButton panelExportButtonText=new ComponentRelatedButton("Save as text file",ta);
    	
    	
    	panelCloseBut.addActionListener(    			
    			new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if (e.getSource() instanceof ComponentRelatedButton){
							componentsContainer.remove(((ComponentRelatedButton)e.getSource()).getRelatedComponent());	
						}						
					}    				
    			}
    	
    	);
    	
    	addSaveTXTActionListener(panelExportButtonText);    	
    	

    	addSavePDFActionListener(panelExportButtonPdf);
        	
    	upperContainer.setLayout(new BorderLayout());
    	upperContainer.add(panelCloseBut,BorderLayout.EAST);
    	lowerContainer.add(panelExportButtonPdf);
    	lowerContainer.add(panelExportButtonText);

    	panel.setLayout(new BorderLayout());
    	JScrollPane internalCon=new JScrollPane();    	
    	ta.setEditable(false);
    	internalCon.getViewport().add(ta);
    	
    	panel.add(upperContainer,BorderLayout.NORTH);
    	
    	panel.add(lowerContainer,BorderLayout.SOUTH);
    	
    	panel.add(internalCon,BorderLayout.CENTER);
    	
    	componentsContainer.add(name,panel);
    	componentsContainer.setSelectedComponent(panel);
    	return ta;
    }

    
    
    
    /**
     * 
     * @param name
     * @param cont
     */
    public void addTextOutput(String name,String cont){
    	JPanel panel=new JPanel();
    	JPanel upperContainer=new JPanel();
    	JPanel lowerContainer=new JPanel();
    	upperContainer.setLayout(new BorderLayout());
    	lowerContainer.setLayout(new FlowLayout());
    	JTextArea ta=new JTextArea(cont);
    	ta.setFont( new Font( "Monospaced", Font.PLAIN, FONT_SIZE ));
    	
    	ComponentRelatedButton panelCloseBut=new ComponentRelatedButton("Close",panel);
    	ComponentRelatedButton panelExportButtonPdf=new ComponentRelatedButton("Save as PDF",ta);
    	ComponentRelatedButton panelExportButtonText=new ComponentRelatedButton("Save as text file",ta);
    	    	
    	panelCloseBut.addActionListener(    			
    			new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if (e.getSource() instanceof ComponentRelatedButton){
							componentsContainer.remove(((ComponentRelatedButton)e.getSource()).getRelatedComponent());	
						}						
					}    				
    			}    	
    	);
    	
    	addSavePDFActionListener(panelExportButtonPdf);    	
    	addSaveTXTActionListener(panelExportButtonText);
    	
    	upperContainer.setLayout(new BorderLayout());
    	upperContainer.add(panelCloseBut,BorderLayout.EAST);
    	lowerContainer.add(panelExportButtonPdf);
    	lowerContainer.add(panelExportButtonText);

    	panel.setLayout(new BorderLayout());
    	JScrollPane internalCon=new JScrollPane();    	
    	ta.setEditable(false);
    	internalCon.getViewport().add(ta);
    	
    	panel.add(upperContainer,BorderLayout.NORTH);
    	
    	panel.add(lowerContainer,BorderLayout.SOUTH);
    	
    	panel.add(internalCon,BorderLayout.CENTER);
    	
    	componentsContainer.add(name,panel);
    	componentsContainer.setSelectedComponent(panel);
    }
    
    
    private void addSaveTXTActionListener(ComponentRelatedButton b){    
    	b.addActionListener(
    		new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					JFileChooser fc=new JFileChooser();
					fc.setFileFilter(
							new FileFilter(){
								@Override
								public boolean accept(File f) {
									return f.isDirectory() || f.getName().indexOf(".txt")==f.getName().length()-4;
								}
								public String getDescription() {
									return "TXT file";
								}								
							}
					);
					
					if (fc.showSaveDialog(self)==JFileChooser.APPROVE_OPTION){
						try {
							boolean granted=true;
							if (fc.getSelectedFile().exists()){
								granted=(JOptionPane.showConfirmDialog(self,"File "+fc.getSelectedFile().getName()+" already exists. Are you sure you want to overwrite it?","Warning",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION);
								
							}
							if (granted){
								FileWriter fw=new FileWriter(fc.getSelectedFile());    							    							
								BufferedWriter bw=new BufferedWriter(fw);
								
								StringReader sr = new StringReader(((JTextArea)((ComponentRelatedButton)e.getSource()).getRelatedComponent()).getText());
								BufferedReader br = new BufferedReader(sr);
								String nextLine = "";
								while ((nextLine = br.readLine()) != null){
									bw.write(nextLine+"\n");    
								}
								bw.close();
								fw.close();
							}
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(self,e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						} 
						
					}
					
				}
    			
    		}
    	);
    }

    

    private void addSavePDFActionListener(ComponentRelatedButton b){
    	b.addActionListener(
        		new ActionListener(){

    				public void actionPerformed(ActionEvent e) {
    					JFileChooser fc=new JFileChooser();
    					fc.setFileFilter(
    							new FileFilter(){
    								@Override
    								public boolean accept(File f) {
    									return f.isDirectory() || f.getName().indexOf(".pdf")==f.getName().length()-4;
    								}
    								public String getDescription() {
    									return "PDF file";
    								}								
    							}
    					);
    					
    					if (fc.showSaveDialog(self)==JFileChooser.APPROVE_OPTION){
    						try {
    							boolean granted=true;
    							if (fc.getSelectedFile().exists()){
    								granted=(JOptionPane.showConfirmDialog(self,"File "+fc.getSelectedFile().getName()+" already exists. Are you sure you want to overwrite it?","Warning",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION);
    								
    							}
    							if (granted){    							
	    							Document document = new Document();    							    							
	    							PdfWriter.getInstance(document, new FileOutputStream(fc.getSelectedFile()));
	    							
	    							JComponent comp=((ComponentRelatedButton)e.getSource()).getRelatedComponent();
	    							
	    							document.setPageSize(new Rectangle(comp.getWidth()+50,document.getPageSize().height()));
	    							document.open(); 
	    							
	    							StringReader sr = new StringReader(((JTextArea)((ComponentRelatedButton)e.getSource()).getRelatedComponent()).getText());
	    							BufferedReader br = new BufferedReader(sr);
	    							String nextLine = "";
	    							while ((nextLine = br.readLine()) != null){
	    								document.add(new Paragraph(nextLine));    
	    							}
	    							document.close();    							
    							}
    						} catch (IOException e1) {
    							JOptionPane.showMessageDialog(self,e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    							e1.printStackTrace();
    						} catch (DocumentException e1){
    							JOptionPane.showMessageDialog(self,e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    							e1.printStackTrace();
    						}
    						
    					}
    					
    				}
        			
        		}
        	);    	
    }

    
    
    
    public void addGraphicalOutput(String name,JComponent comp){
    	JPanel panel=new JPanel();
    	JPanel upperContainer=new JPanel();
    	JPanel lowerContainer=new JPanel();
    	upperContainer.setLayout(new BorderLayout());
    	lowerContainer.setLayout(new FlowLayout());
    	
    	ComponentRelatedButton panelCloseBut=new ComponentRelatedButton("Close",panel);
    	ComponentRelatedButton panelExportButtonPdf=new ComponentRelatedButton("Save as PDF",comp);
    	ComponentRelatedButton panelExportButtonJPG=new ComponentRelatedButton("Save as Image",comp);
    	
    	
    	panelCloseBut.addActionListener(    			
    			new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if (e.getSource() instanceof ComponentRelatedButton){
							componentsContainer.remove(((ComponentRelatedButton)e.getSource()).getRelatedComponent());	
						}						
					}    				
    			}
    	
    	);
    	
    	
    	panelExportButtonJPG.addActionListener(
    		new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					JFileChooser fc=new JFileChooser();
					fc.setFileFilter(
							new FileFilter(){
								@Override
								public boolean accept(File f) {
									return f.isDirectory() || f.getName().indexOf(".jpg")==f.getName().length()-4;
								}
								public String getDescription() {
									return "JPG Image format";
								}								
							}
					);
					
					if (fc.showSaveDialog(self)==JFileChooser.APPROVE_OPTION){
						try {
							boolean granted=true;
							if (fc.getSelectedFile().exists()){
								granted=(JOptionPane.showConfirmDialog(self,"File "+fc.getSelectedFile().getName()+" already exists. Are you sure you want to overwrite it?","Warning",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION);								
							}
							if (granted){							
								GUIUtilities.createImage(((ComponentRelatedButton)e.getSource()).getRelatedComponent(),fc.getSelectedFile().getAbsolutePath());
							}
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(self,e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}						
					}
					
				}
    			
    		}
    	);
    	
    	

    	panelExportButtonPdf.addActionListener(
        		new ActionListener(){

    				public void actionPerformed(ActionEvent e) {
    					JFileChooser fc=new JFileChooser();
    					fc.setFileFilter(
    							new FileFilter(){
    								@Override
    								public boolean accept(File f) {
    									return f.isDirectory() || f.getName().indexOf(".pdf")==f.getName().length()-4;
    								}
    								public String getDescription() {
    									return "PDF Post Script Data file";
    								}								
    							}
    					);
    					
    					if (fc.showSaveDialog(self)==JFileChooser.APPROVE_OPTION){
    						try {
    							boolean granted=true;
    							if (fc.getSelectedFile().exists()){
    								granted=(JOptionPane.showConfirmDialog(self,"File "+fc.getSelectedFile().getName()+" already exists. Are you sure you want to overwrite it?","Warning",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION);
    								
    							}
    							if (granted){
    							
	    							String tmpImageFile=Properties.getInstance().getProperty(Properties.TEMP)+"/"+System.currentTimeMillis()+".jpg";
	    							JComponent comp=((ComponentRelatedButton)e.getSource()).getRelatedComponent();
	    							GUIUtilities.createImage(comp,tmpImageFile);    							
	    							Document document = new Document();    							
	    							document.setPageSize(new Rectangle(comp.getWidth()+50,comp.getHeight()+50));
	    							PdfWriter.getInstance(document, new FileOutputStream(fc.getSelectedFile()));
	    							document.open();
	    							Image jpg = Image.getInstance(tmpImageFile);
	    							document.add(jpg);
	    							document.close();
	    							new File(tmpImageFile).delete();
    							}
    						} catch (IOException e1) {
    							JOptionPane.showMessageDialog(self,e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    							e1.printStackTrace();
    						} catch (DocumentException e1){
    							JOptionPane.showMessageDialog(self,e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    							e1.printStackTrace();
    						}
    						
    					}
    					
    				}
        			
        		}
        	);
        	

    	
    	
    	upperContainer.setLayout(new BorderLayout());
    	upperContainer.add(panelCloseBut,BorderLayout.EAST);
    	lowerContainer.add(panelExportButtonPdf);
    	lowerContainer.add(panelExportButtonJPG);

    	panel.setLayout(new BorderLayout());
    	JScrollPane internalCon=new JScrollPane();
    	
    	
    	internalCon.getViewport().add(comp);    	
    	
    	panel.add(upperContainer,BorderLayout.NORTH);
    	
    	panel.add(lowerContainer,BorderLayout.SOUTH);
    	
    	panel.add(internalCon,BorderLayout.CENTER);
    	
    	componentsContainer.add(name,panel);
    	componentsContainer.setSelectedComponent(panel);
    }

    
    
    private void jbInit() throws Exception {
                   	
    	getContentPane().setLayout(borderLayout1);
        upperPanel.setLayout(borderLayout3);
        infoText.setText("");
        
        closePanElementTool.setText("jButton2");
        
        panElementUpperPanel.setLayout(borderLayout4);
        lowerPanel.add(infoText);
        this.getContentPane().add(componentsContainer, java.awt.BorderLayout.CENTER);
        
        upperPanel.add(hideButton, java.awt.BorderLayout.EAST);
        upperPanel.add(closeAllButton, java.awt.BorderLayout.CENTER);
        
        panElementUpperPanel.add(closePanElementTool,
                                 java.awt.BorderLayout.EAST);
        this.getContentPane().add(upperPanel, java.awt.BorderLayout.NORTH);

        this.getContentPane().add(lowerPanel, java.awt.BorderLayout.SOUTH);
        
        hideButton.setText("Hide window");
        closeAllButton.setText("Close all");
        
        
        closeAllButton.addActionListener(
            	new ActionListener(){

    				public void actionPerformed(ActionEvent e) {
    					componentsContainer.removeAll();
    				}
            		
            	}        
            );        
        
        hideButton.addActionListener(
        	new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					self.setVisible(false);
				}
        		
        	}        
        );
    }

}

class ComponentRelatedButton extends JButton{
	
	private JComponent comp;

	/**
	 * 
	 */
	protected ComponentRelatedButton(JComponent comp) {
		super();
		this.comp=comp;
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @param text
	 * @param icon
	 */
	protected ComponentRelatedButton(String text,JComponent comp) {
		super(text);
		this.comp=comp;
	}


	/**
	 * @return Returns the comp.
	 */
	public JComponent getRelatedComponent() {
		return comp;
	}
	
	
}

