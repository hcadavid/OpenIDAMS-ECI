package org.openidams.supervisor.gui;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URLDecoder;

public class AboutDialog extends JFrame {

    
	private static final long serialVersionUID = 1L;
	ImageIcon ic=new ImageIcon(AboutDialog.class.getResource("openidamseci.jpg").getPath().replaceAll("%20"," "));
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    private JFrame self=this;
	
    public AboutDialog(String title) {
        super(title);
        try {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        jButton1.setBounds(new Rectangle(25, 22, 375, 227));
        jButton1.setIcon(ic);
        
        jButton1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try{					
					Runtime.getRuntime().exec("explorer.exe mailto:gospina@escuelaing.edu.co");				
				}
				catch(IOException ex){
					JOptionPane.showMessageDialog(self,"Your system doesn't allow automatic e-mail client launch. Please contact us at: gospina@escuelaing.edu.co","Contact us",JOptionPane.INFORMATION_MESSAGE);
					ex.printStackTrace();
				}
				
			}
        	
        });
        
        jButton2.setBounds(new Rectangle(168, 279, 96, 46));
        jButton2.setText("OK");
        jButton2.addActionListener(new AboutDialog_jButton2_actionAdapter(this));
        this.getContentPane().add(jButton2);
        this.getContentPane().add(jButton1);
    }

    public void jButton2_actionPerformed(ActionEvent e) {
        this.dispose();
    }
}


class AboutDialog_jButton2_actionAdapter implements ActionListener {
    private AboutDialog adaptee;
    AboutDialog_jButton2_actionAdapter(AboutDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton2_actionPerformed(e);
    }
}
