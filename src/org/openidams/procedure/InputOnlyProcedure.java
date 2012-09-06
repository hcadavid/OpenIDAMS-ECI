package org.openidams.procedure;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.Repository;
import org.openidams.dataset.exceptions.DataSetNotFoundException;
import org.openidams.script.interpreter.StatementStep;

public abstract class InputOnlyProcedure extends Procedure{

	private DataSet inputDataSet=null;
	private String inDataSetName=null;
	
	public InputOnlyProcedure(StatementStep pStatement)throws ProcedureInstantiationException{
		super(pStatement);
		inDataSetName=pStatement.getMetadata().getProperty("InputDataSet");
        if (inDataSetName == null ) {
            throw new ProcedureInstantiationException(
                    "Procedure instantiation failed. The property InputDataSet is requiered.");
        }
		try {
			inputDataSet = Repository.getInstance().getDataSet(inDataSetName);
		} catch (DataSetNotFoundException e) {
			throw new ProcedureInstantiationException("Procedure instantiation failed, because requested DataSet wasn't found",e);
		}
	}
	
	/**
	 * @return Returns the inputDataSet.
	 */
	public DataSet getInputDataSet() {
		return inputDataSet;
	}


	/* (non-Javadoc)
	 * @see org.openidams.procedure.Procedure#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.getClass().getName()+"("+inDataSetName+")";
	}

	/* (non-Javadoc)
	 * @see org.openidams.procedure.Procedure#getRequiredParameters()
	 */
	@Override
	public String[] getRequiredParameters() {
		return new String[]{"InputDataSet"};
	}


	
}
