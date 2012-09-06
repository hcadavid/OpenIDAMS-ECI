/**
 * Copyright (C) 2006  OpenIDAMS ECITeam 
 * ECITeam: Gerardo Ospina, HŽctor Cadavid, Camilo Rocha
 * <gospina,hcadavid,hrocha@escuelaing.edu.co>
 * Debeloped by Javier aLexander Guio Gelvez 
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
package plugins.procedures;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.dictionary.DataType;
import org.openidams.dataset.dictionary.Dictionary;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.dataset.exceptions.DataSetOperationException;
import org.openidams.datatable.DataTableRow;
import org.openidams.procedure.InputOutputDatasetProcedure;
import org.openidams.procedure.InputOutputProcedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;

public class TransProcedure extends InputOutputDatasetProcedure{

	/**
	 * Trans procedure implantation reading and writing from/to different
	 * datasources
	 * @author ECITeam
	 * @version 0.0.6
	 */
	public TransProcedure(StatementStep pStatement) throws ProcedureInstantiationException {
        super(pStatement);
        
    }
	/**
     * Executes the procedure with the given datasets
     * @param pInputDataSet Input dataset (read-only)
     * @param pOutputDataSet Output dataset
     */	
	 public void execute() throws ProcedureExecutionException{       
		 Iterator<DataSetRow> itr=this.getInputDataSet().getRecords();
		 LinkedList<DictionaryVariable>Variables=this.getInputDataSet().getDictionary().getBasicVariables();		 
		 LinkedList<DictionaryVariable> Vars=new LinkedList<DictionaryVariable>();
		 // define output dataset variables
		 int count =0;
		 int size=Variables.size();
		 while(count<size){
			 DictionaryVariable t=Variables.get(count);
			 Vars.add(t);
			 count++;
		 }
		 count =0;
		 Hashtable<String,Integer> maps=new Hashtable<String,Integer>();
		 for(int i=0;i<Vars.size();i++){
			 maps.put(Vars.get(i).getName(),i);
		 }
		 // associate output variables
		 this.getOutputDataSet().setDictionary(new Dictionary(Vars));
		 this.getOutputDataSet().setVariableNameToColumnNumberMapping(maps);
		 //add results to output dataset. for this procedure
		 try{
			while(itr.hasNext()){
				this.getOutputDataSet().addRecord(itr.next());
			}
		  }
	     catch(DataSetOperationException e){
	        	throw new ProcedureExecutionException("The procedure execution couldn't be terminated because a dataset operation exception:",e);
	     }
	        
	 }
}
