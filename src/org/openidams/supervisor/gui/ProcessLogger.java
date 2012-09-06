package org.openidams.supervisor.gui;

import java.util.LinkedList;

public class ProcessLogger {

	private static ProcessLogger instance=new ProcessLogger();
	private LinkedList<EventFilter> eventfilters;
	
	private ProcessLogger(){
		eventfilters=new LinkedList<EventFilter>();
	}
	
	public static ProcessLogger getInstance(){
		return instance;
	}
	
	public void addFilter(EventFilter ef){
		eventfilters.add(ef);
	}
	
	public void reportEvent(String event){
		for (EventFilter ef:eventfilters){
			new EventManager(ef,event).start();
			//ef.addEvent(event);
		}
	}
		
}

class EventManager extends Thread{
	private EventFilter ef;
	private String ev;
	public EventManager(EventFilter ef,String event){
		this.ef=ef;
		this.ev=event;
		
	}
	public void run(){
		ef.addEvent(ev);		
	}
}