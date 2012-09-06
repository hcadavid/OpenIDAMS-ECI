/**
 * Copyright (C) 2006  OpenIDAMS ECITeam 
 * ECITeam: Gerardo Ospina, HŽctor Cadavid, Camilo Rocha
 * <gospina,hcadavid,hrocha@escuelaing.edu.co>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
*/
package org.openidams.dataset;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.dataset.dictionary.DataType;
import org.openidams.dataset.dictionary.Dictionary;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.dataset.exceptions.DataSetOperationException;
import org.openidams.dataset.exceptions.InvalidFilterException;
import org.openidams.dataset.exceptions.InvalidRecodeException;
import org.openidams.dataset.exceptions.VariableNotFoundException;
import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableRow;

/**
 * Dataset implantation. In this case, a dataset has
 * an instance of a list of variables and an instance of 
 * a datatable (variables values). Each row in the datatable
 * corresponds to a record of the dataset
 * @author ECITeam
 * @version 0.0.6
 */
public class DataSet {
	
	
	private String dataSetName;
	
    /**
     * DataSet's table with data
     */
	private DataTable dataTable;
    
    /**
     * List of dataset's variables
     */
	private Dictionary dictionary;	
	
	
	
	/**
	 * Attributes used by an Iterator inner class
	 */
	private Iterator<DataTableRow> sourceRows=null;
	/**
	 * Variable name / data table column number mapping
	 */
	private Hashtable<String,Integer> variableNameToColumnNumberMapping;
	
	
	
	//variables required by inner classes
	private Iterator<DataSetRow> rows;
	LinkedList<Integer> indexes;
	private LinkedList<DictionaryVariable> vars=null;

	
	/**
     * Creates a new data set with a given datatable and variables
	 * @param source DataTable to be used by the DataSet
	 * @param variables Variables of the DataSet
	 */
	public DataSet(DataTable source, Dictionary dict, Hashtable<String,Integer> mapping) {
		dataTable = source;
		this.dictionary = dict;
		this.variableNameToColumnNumberMapping=mapping;
	}

	/**
	 * Dataset alternate constructor. Use to create output datasets.
	 * @param source DataTable to be used
	 */
	/*public DataSet(DataTable source) {
		dataTable = source;
	}*/
	
	/**
     * Adds the given DataSetRow to the datatable
     * comment
     * @param dsr Row to be added to the datatable
     */
	public void addRecord(DataSetRow dsr) throws DataSetOperationException{
		//this method requieres the reconstruction of a datatable row based on the datasetrow, 
		//the dictionary and the variables mapping.

		//get dictionary variables
		LinkedList<DictionaryVariable> vars=getDictionary().getBasicVariables();
		
		//create space for datatablerow data
		String[] values=new String[getDictionary().getBasicVariables().size()];
		
		//for each dictionary variable
		for (int i=0;i<vars.size();i++){
			//get the position of the variable on the datatable
			Integer col=getVariableNameToColumnNumberMapping().get(vars.get(i).getName());
			if (col==null) throw new DataSetOperationException("There is an error with a dataset variable-datatable column mapping. Variable:"+vars.get(i).getName()+" hasn't an assigned column.");
			if (col>=values.length) throw new DataSetOperationException("There is an error with a dataset variable-datatable column mapping. Variable:"+vars.get(i).getName()+" hasn't a valid column number:"+col);
			//set the value on the respective column, of the ieth variable
			values[col]=dsr.getStringValue(i);			
		}
		
		getDataTable().addRecord(new DataTableRow(values));
	}
	


	
	/**
	 * Evaluate filter expression at given datatablerow
	 * It uses mapping entries to determine concrete values to be used.
	 * @param filterExp
	 * @param varToValMapping
	 * @return
	 * @throws  
	 */
	private boolean evaluateFilterExpression(String filterExp,Hashtable<String,String> varToValMapping){
		try {
			return FilterEvaluator.evaluate(varToValMapping,filterExp);
		} catch (InvalidFilterException e) {
			throw new RuntimeException("Filter couldn't be intepretated:",e);
		}		
	}
	
	/**
	 * @return Returns the dataSetName.
	 */
	public String getDataSetName() {
		return dataSetName;
	}

	/**
	 * @return Returns the dataTable.
	 */
	public DataTable getDataTable() {
		if (dataTable==null) throw new RuntimeException("Data set hasn't defined a data table");
		return dataTable;
	}
	
	
	
    /**
	 * @return Returns the dictionary.
	 */
	public Dictionary getDictionary() {
		if (dataTable==null) throw new RuntimeException("Data set hasn't defined a dictionary");
		return dictionary;
	}

    /**
	 * Returns an interator over the rows of the datatable for all variables
	 * @return Interator over the rows of the datatable for all variables
	 */
	public Iterator<DataSetRow> getRecords(){
		vars=getDictionary().getBasicVariables();

		sourceRows=getDataTable().getRecords();
		
		return new Iterator<DataSetRow>(){

			DataSetRow nextDsr=null;
						
			public boolean hasNext() {				
				return sourceRows.hasNext();
			}

			public DataSetRow next() {
				
				DataTableRow sourceRow=sourceRows.next();
				DataSetRow dsr=new DataSetRow();
				
				for (DictionaryVariable v:vars){
					Integer dataCol=getVariableNameToColumnNumberMapping().get(v.getName());									
					if (dataCol==null) throw new RuntimeException("DataSet data loading failed: Dictionary-variable '"+v.getName()+"' hasn't a defined mapping on the DataSet");
					String value=sourceRow.getStringValue(dataCol);
					dsr.addValue(value);
				}
				return dsr;						
			}

			public void remove() {}
			
		};

		
	}

	
	
	
	/**
	 * Returns an iterator over the rows of the datatable for the given varibles names<br>
     * <b>NOT IMPLEMENTED YET</b>
	 * @param variables variable names
	 * @return Iterator over the rows of the datatable for the given varibles names
	 */
	public Iterator<DataSetRow> getRecords(String variables[]) throws VariableNotFoundException{
		rows=getRecords();
		
		//list of indexes (columns) where are located the requested variables, in the same order
		//as the parameter's variables array
		indexes=new LinkedList<Integer>();
		
		//get each variable's position (column) of each variable given on 'variables' array
		for (int i=0;i<variables.length;i++){
			Iterator<String> vars=dictionary.getAllVariableNames();
			boolean colFound=false;
			int index=-1;
			while (vars.hasNext() && !colFound){
				index++;
				if (vars.next().equals(variables[i])){
					colFound=true;
					indexes.add(index);
				}
			}
			if (!colFound){
				throw new VariableNotFoundException("Variable "+variables[i] +" wasn't defined on the dictionary");
			}
			
		}
		
		return new Iterator<DataSetRow>(){

			public boolean hasNext() {
				return rows.hasNext();
			}

			public DataSetRow next() {
				DataSetRow dsr=rows.next();
				DataSetRow filtereddsr=new DataSetRow();
				for (Integer i:indexes){
					filtereddsr.addValue(dsr.getStringValue(i));					
				}
				return filtereddsr;
			}

			public void remove() {}
			
		};
	}

	/**
	 * @return Returns the variableNameToColumnNumberMapping.
	 */
	public Hashtable<String, Integer> getVariableNameToColumnNumberMapping() {
		if (dataTable==null) throw new RuntimeException("Data set hasn't defined a name-to-column mapping.");
		return variableNameToColumnNumberMapping;
	}

	/**
	 * Returns an iterator over the list of variables (basic variables and recode variables). The iterator
     * respects the order of the list
	 * @return Iterator over the list of variables
	 */
	public Iterator<DictionaryVariable> getVariables(){
		LinkedList<DictionaryVariable> vars=new LinkedList<DictionaryVariable>();
		vars.addAll(getDictionary().getBasicVariables());

		return vars.iterator();
	}

	/**
	 * @param dataSetName The dataSetName to set.
	 */
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	/**
     * Sets the list of variables of the dataset
     * @param variables New list of variables
     */
	public void setDictionary(Dictionary dict){
		this.dictionary=dict;
	}

	/**
	 * @param variableNameToColumnNumberMapping The variableNameToColumnNumberMapping to set.
	 */
	public void setVariableNameToColumnNumberMapping(
			Hashtable<String, Integer> variableNameToColumnNumberMapping) {
		this.variableNameToColumnNumberMapping = variableNameToColumnNumberMapping;
	}
}
