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
package script.openidams.interpreter;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.Repository;
import org.openidams.dataset.exceptions.DataSetInstantationException;

import org.openidams.procedure.Catalog;
import org.openidams.procedure.Procedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;

import org.openidams.script.interpreter.IInterpreter;
import org.openidams.script.interpreter.InterpreterRuntimeException;
import org.openidams.script.interpreter.StatementScript;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.ProcessLogger;

import java.util.Iterator;


/**
 * An implementation of a possible LanguageInterpreter. In a production environment
 * this 'bundle' could be uploaded dinamically as a plugin interpreter component.
 * @author ECITeam
 * @version 0.0.8
 */
public class OpenIDAMSInterpreter implements IInterpreter {
	
	/**
	 * Executes the given script
	 * @param pScript Given script
	 * @throws InterpreterRuntimeException Problems executing the given script
	 */
	public void executeScript(StatementScript pScript)
	throws InterpreterRuntimeException {
		new ScriptExecutionThread(pScript).start();
	}
	
}

/**
 * A private class to create a new thread for script's execution.
 * Its objective is to independize setup execution process from GUI's execution thread
 * @author ECITeam
 *
 */
class ScriptExecutionThread extends Thread{
	Iterator _statements = null;
	StatementStep _statement = null;
	Procedure _procedure = null;
	DataSet _dsinput;
	DataSet _dsoutput;


	public ScriptExecutionThread(StatementScript ss){
		_statements=ss.statementsIterator();
	}
	
	
	public void run(){
		ProcessLogger.getInstance().reportEvent("Starting setup execution...");
		try {
			while (_statements.hasNext()) {
				_statement = (StatementStep) _statements.next();
							
				_procedure = Catalog.getInstance().buildProcedure(_statement);
				
				ProcessLogger.getInstance().reportEvent("Running:"+_procedure.getDescription());                
				
				_procedure.execute();

				ProcessLogger.getInstance().reportEvent("Done: "+_procedure.getClass());                
			}
			
			ProcessLogger.getInstance().reportEvent("Execution finished successfully.");
			
		} catch (ProcedureInstantiationException ex) {
			Throwable rootCause=ex;
			while (rootCause.getCause()!=null){
				rootCause=rootCause.getCause();
			}			
			ProcessLogger.getInstance().reportEvent("An error has ocurred on script execution:"+ex.getMessage()+","+rootCause);
			ex.printStackTrace();
		} catch (ProcedureExecutionException ex){
			Throwable rootCause=ex;
			while (rootCause.getCause()!=null){
				rootCause=rootCause.getCause();
			}			
			ProcessLogger.getInstance().reportEvent("An error has ocurred on script execution:"+ex.getMessage()+","+rootCause);
			ex.printStackTrace();			
		} catch (Exception ex){
			Throwable rootCause=ex;
			while (rootCause.getCause()!=null){
				rootCause=rootCause.getCause();
			}			
			ProcessLogger.getInstance().reportEvent("An error has ocurred on script execution:"+ex.getMessage()+","+rootCause);
			ex.printStackTrace();			
		}

	}
	
}
