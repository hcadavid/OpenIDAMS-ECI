package org.openidams.supervisor.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.openidams.procedure.Catalog;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.utilities.GUIUtilities;


public class ProceduresInfoFrame extends javax.swing.JFrame {
	private JScrollPane jScrollPane1;
	private JPanel jPanel1;
	private JTextArea procInfoText;
	private JButton closeButton;
	private JFrame self=this;
	
	private static String info=null;
	
	public ProceduresInfoFrame() {
		super("Available procedures.");
		init();
	}
	
	private void init() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			jScrollPane1 = new JScrollPane();
			getContentPane().add(jScrollPane1, BorderLayout.CENTER);
			
			procInfoText = new JTextArea();
			procInfoText.setEditable(false);
			Catalog.getInstance().init();
			
			
			
			procInfoText.setBackground(Color.BLUE);
			procInfoText.setForeground(Color.WHITE);
			
			
			info=""+"Available procedures:\n\n";
			
			Enumeration<String> pnames=Catalog.getInstance().getAvailableProcedures();			
			while (pnames.hasMoreElements()){
				info+="\n\t-"+pnames.nextElement();
				
			}
			
			procInfoText.setText(info);
			
			jPanel1 = new JPanel();
			getContentPane().add(jPanel1, BorderLayout.SOUTH);
			jScrollPane1.setViewportView(procInfoText);
			
			closeButton = new JButton();
			jPanel1.add(closeButton);
			closeButton.setText("OK");
			closeButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {					
					self.dispose();
				}				
			});
			pack();
			setSize(400, 300);
			GUIUtilities.centerComponent(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
