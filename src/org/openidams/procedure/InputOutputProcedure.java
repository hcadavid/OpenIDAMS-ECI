package org.openidams.procedure;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.Repository;
import org.openidams.dataset.exceptions.DataSetNotFoundException;
import org.openidams.script.interpreter.StatementStep;

public abstract class InputOutputProcedure extends Procedure {

	private DataSet inputDataSet=null;
	private DataSet outputDataSet=null;
	private String inDataSetName=null;
	private String outDataSetName=null;
	
	public InputOutputProcedure(StatementStep pStatement) throws ProcedureInstantiationException{
		super(pStatement);
		inDataSetName=pStatement.getMetadata().getProperty("InputDataSet");
		outDataSetName=pStatement.getMetadata().getProperty("OutputDataSet");
        if (inDataSetName == null || outDataSetName == null) {
            throw new ProcedureInstantiationException(
                    "Procedure failed. The property InputDataSet and OutputDataset are requiered.");
        }
		try {
			inputDataSet = Repository.getInstance().getDataSet(inDataSetName);
			outputDataSet = Repository.getInstance().getDataSet(outDataSetName);
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

	/**
	 * @return Returns the outputDataSet.
	 */
	public DataSet getOutputDataSet() {
		return outputDataSet;
	}

	/* (non-Javadoc)
	 * @see org.openidams.procedure.Procedure#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.getClass().getName()+"("+inDataSetName+","+outDataSetName+")";
	}

	/* (non-Javadoc)
	 * @see org.openidams.procedure.Procedure#getRequiredParameters()
	 */
	@Override
	public String[] getRequiredParameters() {
		return new String[]{"InputDataSet","OutputDataSet"};
	}

	
	
}
