package org.openidams.supervisor.gui;

import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;

public class EventsTextArea extends JTextArea implements EventFilter {

	private static final long serialVersionUID = 1L;
	private String eventDesc;
	private JTextArea ta;
	
	public EventsTextArea(){
		super();
		this.setBackground(new Color(112,176,248));
		this.setForeground(Color.BLACK);
	}
	
	public void addEvent(String eventDescription) {
		eventDesc=eventDescription;
		ta=this;
		Calendar cal=new GregorianCalendar();
		String time="["+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+"."+cal.get(Calendar.MILLISECOND)+"]\t";
		ta.setText(ta.getText()+"\n"+time+eventDesc);
		ta.setCaretPosition(ta.getDocument().getLength());
	}

}
