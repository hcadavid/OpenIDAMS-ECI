package org.openidams.dataset.dictionary;

import java.util.Iterator;
import java.util.LinkedList;


public class Dictionary {

	private LinkedList<DictionaryVariable> basicVariables;

	/**
	 * @param basicVariables
	 * @param equivalences
	 */
	public Dictionary(LinkedList<DictionaryVariable> variables) {
		this.basicVariables = variables;
	}

	
	
	/**
	 * 
	 * @return all variable names includig basic and recode variables
	 */
	public Iterator<String> getAllVariableNames(){

		return new Iterator<String>(){
			Iterator<DictionaryVariable> basicVarsIt=basicVariables.iterator();
			
			public boolean hasNext() {
				return (basicVarsIt.hasNext());
			}

			public String next() {
				return basicVarsIt.next().getName();				
			}

			public void remove() {}
			
		};
		
	}

	
	/**
	 * 
	 * @return all variable names includig basic and recode variables
	 */
	public Iterator<String> getAllVariableLabels(){

		return new Iterator<String>(){
			Iterator<DictionaryVariable> basicVarsIt=basicVariables.iterator();
			
			public boolean hasNext() {
				return (basicVarsIt.hasNext());
			}

			public String next() {
				return basicVarsIt.next().getLabel();

			}

			public void remove() {}
			
		};
		
	}

	
	
	/**
	 * @return Returns the basicVariables.
	 */
	public LinkedList<DictionaryVariable> getBasicVariables() {
		return basicVariables;
	}


	/**
	 * @param basicVariables The basicVariables to set.
	 */
	public void setBasicVariables(LinkedList<DictionaryVariable> basicVariables) {
		this.basicVariables = basicVariables;
	}
	

	
}
