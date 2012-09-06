package plugins.procedures;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.dictionary.DataType;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.dataset.exceptions.VariableNotFoundException;
import org.openidams.procedure.InputOnlyProcedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.output.ProceduresOutputHandler;
import org.openidams.utilities.DataSetRowsPersistenceHandler;

public class DataCheck extends InputOnlyProcedure{

	public DataCheck(StatementStep pStatement) throws ProcedureInstantiationException {
		super(pStatement);		
	}

	public void execute() throws ProcedureExecutionException {
		LinkedList<DictionaryVariable> variables = this.getInputDataSet().getDictionary().getBasicVariables();
		
    	Calendar calendar = new GregorianCalendar();
		boolean entered = false;
    	String res = "";
		res += "________________________________________________________________\n\n";
		res += "OpenIDAMS ECITeam";
		res += "\n\n*** Procedure: DataSet Check";
		res += "\n*** Description: Checking Results for <" + this.getInputDataSet().getDataSetName() + "> Dataset";
		res += "\n*** Date: " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
		res += "\n________________________________________________________________";

		Iterator<DataSetRow> it; 
		it = this.getInputDataSet().getRecords();
    	int cont=0;		
		for (int pos=0; pos<variables.size(); pos++){
			if (DataType.N == variables.get(pos).getDataType()){
		    	int numErrors=0;
		    	if (cont==variables.size()){
		    		cont=0;
		    	}
		    	int tam = res.length();
		    	String resPar = this.checking(it,cont,variables.get(pos).getName(),variables.get(pos).getDataType().name());
		    	it = this.getInputDataSet().getRecords();
		    	if (!(resPar.equals(""))){
		    		entered=true;
		    		res += resPar;
		    	}		   		    	
			}	
			cont++;
		}
    	if (!entered){
    		res += "\n\nNo wrong data found on <" + this.getInputDataSet().getDataSetName() + "> Dataset";
    	}
		ProceduresOutputHandler.getInstance().addTextOutput("Check " + "<" + this.getInputDataSet().getDataSetName() + ">",res);
		
	}

	public String checking(Iterator<DataSetRow> it, int pos, String var, String type){
		int numErrors=0;
		String res="";
		int cont=0;
		while (it.hasNext()){
			String val=it.next().getStringValue(pos);
    		try {
    			float fval = Float.parseFloat(val);
    		}
    		catch (NumberFormatException e1){
				if (numErrors==0){
					res += "\n\n> Variable '" + var + "' Type: " + type + "\n";
					res += "   \n   - Row: " + (cont+1) + "  :::  Value: " + val;
				} else {
					res += "   \n   - Row: " + (cont+1) + "  :::  Value: " + val;
				}
				numErrors ++;		
    		}
    		cont++;
		}
    	if (numErrors>0){
    		res += "\n\n     Wrong values found: " + numErrors;
    		res += "\n________________________________________________________________";
    	}
		return res;
	}
	
}
