package org.openidams.script.standardprocedures;

import java.util.Hashtable;
import java.util.LinkedList;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.Repository;
import org.openidams.dataset.dictionary.DataType;
import org.openidams.dataset.dictionary.Dictionary;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.datatable.Catalog;
import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableInstantiationException;
import org.openidams.procedure.InputOnlyProcedure;
import org.openidams.procedure.Procedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;

/**
 * This procedure creates an empty dataset (without dictionary and mapping). Default datatable is provided.
 * @author ECI Team
 *
 */
public class EmptyDataSetCreationProcedure extends Procedure {

	String dataSetName=null;
	String dataFileName=null;
	
	public EmptyDataSetCreationProcedure(StatementStep pStatement)
			throws ProcedureInstantiationException {
		super(pStatement);
		dataSetName=pStatement.getMetadata().getProperty("dataSetName");		
		dataFileName=pStatement.getMetadata().getProperty("dataFileName");
        if (dataSetName == null ) {
            throw new ProcedureInstantiationException(
                    "Procedure instantiation failed. The property dataSetName is required.");
        }		
        if (dataFileName == null ) {
            throw new ProcedureInstantiationException(
                    "Procedure instantiation failed. The property dataFileName is required.");
        }		
        
	}

	@Override
	public void execute() throws ProcedureExecutionException {

		DataTable dt = null;
		try {
			Hashtable<String, String> metadata=new Hashtable<String, String>();
			metadata.put("dataFileName",dataFileName);
			dt = Catalog.getInstance().buildDataTable(Catalog.OODB_DATA_FILE,
					metadata);
		} catch (DataTableInstantiationException e) {
			e.printStackTrace();
			throw new ProcedureExecutionException("Standard datatable <OODBDataFile> instantation failed:",e);
		}
		Repository.getInstance().addDataSet(dataSetName, new DataSet(dt,null,null));
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[]{"dataSetName"};
	}

	@Override
	public String getDescription() {
		return "Empty dataset creation(no dictionary or mapping defined yet.):"+dataSetName;
	}

}
