package org.openidams.supervisor.gui;

import javax.swing.JTextArea;



@SuppressWarnings("serial")
public class StandardOutputTextArea extends JTextArea implements OutputWriter{

	public static StandardOutputTextArea instance=new StandardOutputTextArea();
	
	private StandardOutputTextArea(){};
	
	public static StandardOutputTextArea getInstance(){
		return instance;
	}

	public void writeLine(String line) {
		this.setText(this.getText()+line+"\n");
		this.setCaretPosition(this.getDocument().getLength());
	}

}
