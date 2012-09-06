package org.openidams.script.standardprocedures;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.Repository;
import org.openidams.dataset.dictionary.DataType;
import org.openidams.dataset.dictionary.Dictionary;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.datatable.Catalog;
import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableInstantiationException;
import org.openidams.procedure.Procedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.utilities.Properties;
import org.openidams.utilities.winidamscompatibility.InvalidWinIDAMSDictionaryException;
import org.openidams.utilities.winidamscompatibility.WinIDAMSDictionaryEntry;
import org.openidams.utilities.winidamscompatibility.WinIDAMSDictionaryInterpreter;

public class ImportWinIDAMSDataSetProcedure extends Procedure {

	String datafile;
	String dictfile;
	String newdsname;
	LinkedList<WinIDAMSDictionaryEntry> oldDict;
	
	public ImportWinIDAMSDataSetProcedure(StatementStep pStatement)
			throws ProcedureInstantiationException {
		super(pStatement);
		datafile=pStatement.getMetadata().getProperty("datafile");
		dictfile=pStatement.getMetadata().getProperty("dictfile");
		newdsname=pStatement.getMetadata().getProperty("dsname");
        if (datafile == null ) {
            throw new ProcedureInstantiationException(
                    "Procedure instantiation failed. The property datafile is required.");
        }		
        if (dictfile == null ) {
            throw new ProcedureInstantiationException(
                    "Procedure instantiation failed. The property dictfile is required.");
        }		
        if (newdsname == null ) {
            throw new ProcedureInstantiationException(
                    "Procedure instantiation failed. The property dsname is required.");
        }
        
        //if no extension was provided
        if (datafile.indexOf(".")==-1) datafile+=".dat";
        if (dictfile.indexOf(".")==-1) dictfile+=".dic";
        
        try {
			oldDict=WinIDAMSDictionaryInterpreter.getDictionaryEntries(new File(Properties.getInstance().getProperty(Properties.WINIDAMSFILES)+"/"+dictfile));
		} catch (InvalidWinIDAMSDictionaryException e) {
			throw new ProcedureInstantiationException(
            "Procedure instantiation failed. The given WinIDAMS dictionary isn't valid:",e);

		}
	}

	@Override
	public void execute() throws ProcedureExecutionException {

		DataTable dt = null;
		try {
			Hashtable<String, String> metadata=new Hashtable<String, String>();
			metadata.put("datafile",datafile);
			metadata.put("dictfile",dictfile);
			dt = Catalog.getInstance().buildDataTable(Catalog.WINIDAMS_DATA_FILE,metadata);
		} catch (DataTableInstantiationException e) {
			e.printStackTrace();
			throw new ProcedureExecutionException("Standard datatable <"+Catalog.WINIDAMS_DATA_FILE+"> instantation failed:",e);
		}

		
		LinkedList<DictionaryVariable> newDictData=new LinkedList<DictionaryVariable>();
		Hashtable<String,Integer> mapping=new Hashtable<String,Integer>();
		
		int varNum=0;
		for (WinIDAMSDictionaryEntry olddi:oldDict){
			newDictData.add(new DictionaryVariable(olddi.getName(),"V"+varNum,"Imported old-WinIDAMS datafile field:"+olddi.getName(),olddi.isAlphanumeric()?DataType.A:DataType.N));
			mapping.put("V"+varNum,varNum);
			varNum++;
		}
		
		Dictionary dict=new Dictionary(newDictData);
		DataSet ds=new DataSet(dt,dict,mapping);
		ds.setDataSetName("Imported WinIDAMS data:"+datafile);
		Repository.getInstance().addDataSet(newdsname,ds );

	}

	@Override
	public String[] getRequiredParameters() {
		return new String[]{"dsname","datafile","dictionary"};
	}

	@Override
	public String getDescription() {
		return "OpenIDAMS DataSet creation based on old-WinIDAMS dictionary and datafile";
	}
	
}
