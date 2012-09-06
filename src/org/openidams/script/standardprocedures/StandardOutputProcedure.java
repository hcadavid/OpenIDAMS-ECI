package org.openidams.script.standardprocedures;

import java.util.Iterator;

import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.procedure.InputOnlyProcedure;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.OutputWriter;
import org.openidams.supervisor.gui.StandardOutputTextArea;

public class StandardOutputProcedure extends InputOnlyProcedure {

	private OutputWriter out=StandardOutputTextArea.getInstance();
	
	/**
	 * @param pStatement
	 */
	public StandardOutputProcedure(StatementStep pStatement) throws ProcedureInstantiationException {
		super(pStatement);	
	}


	
	@Override
	public void execute() {			
			Iterator<DataSetRow> it = this.getInputDataSet().getRecords();
            Iterator<DictionaryVariable> vars = this.getInputDataSet().getVariables();

            String header = "";
            while (vars.hasNext()) {
                header = header + "\t" + vars.next().getLabel();
            }

            out.writeLine("DATASET PRINTOUT");
            out.writeLine(header);

            

            while (it.hasNext()) {
            	String row = "";
                vars = this.getInputDataSet().getVariables();
                DataSetRow drow = it.next();
                
                Iterator valIt=drow.valuesIterator();
                
                while (valIt.hasNext()) {
                    row = row + "\t"
                            + valIt.next();
                }
                out.writeLine(row);
            }

	}

	/**
	 * @param out The out to set.
	 */
	public void setOut(OutputWriter out) {
		this.out = out;
	}

}
