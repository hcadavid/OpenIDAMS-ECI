package org.openidams.procedure;

import java.util.Hashtable;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.Repository;
import org.openidams.dataset.exceptions.DataSetNotFoundException;
import org.openidams.datatable.Catalog;
import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableInstantiationException;
import org.openidams.script.interpreter.StatementStep;

public abstract class InputOutputDatasetProcedure extends Procedure {

	private DataSet inputDataSet=null;
	private DataSet outputDataSet=null;
	private String inDataSetName=null;
	private String outDataSetName=null;
	
	public InputOutputDatasetProcedure(StatementStep pStatement) throws ProcedureInstantiationException{
		super(pStatement);
		inDataSetName=pStatement.getMetadata().getProperty("InputDataSet");
		outDataSetName=pStatement.getMetadata().getProperty("OutputDataSetName");
        if (inDataSetName == null || outDataSetName == null) {
            throw new ProcedureInstantiationException(
                    "Procedure failed. The property InputDataSet and OutputDatasetName are requiered.");
        }
		try {
			inputDataSet = Repository.getInstance().getDataSet(inDataSetName);
			
			outputDataSet=createDataSet(outDataSetName);			
		
		} catch (DataSetNotFoundException e) {
			throw new ProcedureInstantiationException("Procedure instantiation failed, because requested DataSet wasn't found:"+inDataSetName,e);
		}
		
        
	}
	
	private DataSet createDataSet(String name) throws ProcedureInstantiationException{
		
		DataTable dt = null;
		try {
			Hashtable<String, String> metadata=new Hashtable<String, String>();
			metadata.put("dataFileName",name+".dataset.dat");
			dt = Catalog.getInstance().buildDataTable(Catalog.OODB_DATA_FILE,
					metadata);
		} catch (DataTableInstantiationException e) {
			e.printStackTrace();
			throw new ProcedureInstantiationException("Standard datatable <OODBDataFile> instantation failed:",e);
		}
		DataSet ds;
		ds=new DataSet(dt,null,null);
		ds.setDataSetName(name);
		Repository.getInstance().addDataSet(name, ds);
		return ds;
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

