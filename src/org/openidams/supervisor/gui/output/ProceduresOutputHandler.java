package org.openidams.supervisor.gui.output;

import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextArea;

import org.openidams.utilities.GUIUtilities;

public class ProceduresOutputHandler {

		
	private static ProceduresOutputHandler instance=new ProceduresOutputHandler();
	
	private ProceduresOutputContainer container=null;
	
	private ProceduresOutputHandler() {
		container=new ProceduresOutputContainer();
		container.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);
		container.setVisible(false);
		GUIUtilities.centerComponent(container);
	}
	
	public static ProceduresOutputHandler getInstance(){
		return instance;
	}
	
	public void addTextOutput(String name,String text){
		container.addTextOutput(name,text);
		container.setVisible(true);		
	}
	
	public TextOutputStream addTextOutput(String name){
		JTextArea ta=container.addUpdateableTextOutput(name);		
		container.setVisible(true);		
		return new TextAreaOutputStream(ta);
	}
		
	public void addGraphicalOutput(String name,JComponent graphic){
		container.addGraphicalOutput(name,graphic);
		container.setVisible(true);
	}
	
	public void showOutput(){
		container.setVisible(true);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		container.dispose();
	}
	
	

}

class TextAreaOutputStream implements TextOutputStream{
	JTextArea ta;
	
	/**
	 * @param ta
	 */
	protected TextAreaOutputStream(JTextArea ta) {
		this.ta = ta;
	}

	public void writeString(String s) {
		ta.append(s+"\n");
	}
	
	
}
