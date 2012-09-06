package org.openidams.script.standardprocedures;

import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.exceptions.VariableNotFoundException;
import org.openidams.procedure.InputOnlyProcedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.output.ProceduresOutputHandler;
import org.openidams.supervisor.gui.output.TextOutputStream;

public class ListProcedure extends InputOnlyProcedure {

	private static final int COLUMN_WIDTH = 25;
	private String variable=null;
	public ListProcedure(StatementStep pStatement)
			throws ProcedureInstantiationException {
		super(pStatement);
		
		//variables are optional values, so there isn't exception throwing if value wasn't set
		variable=pStatement.getMetadata().getProperty("variables");
	}

	@Override
	public void execute() throws ProcedureExecutionException {
		DataSet ds=this.getInputDataSet();
		TextOutputStream textOut=ProceduresOutputHandler.getInstance().addTextOutput("LIST(all) - "+ds.getDataSetName());
		
		if (variable==null){			
			textOut.writeString("Listing all values");
			textOut.writeString("");
			int count=0;
			Iterator<DataSetRow> rows=ds.getRecords();
			
			Iterator<String> vars=ds.getDictionary().getAllVariableNames();
			Iterator<String> labels=ds.getDictionary().getAllVariableLabels();
			
			
			String varCol="|";
			int varsNum=0;
			
			while (vars.hasNext()){				
				varCol+=formatStringCentered("("+vars.next()+")-"+labels.next(),COLUMN_WIDTH)+"|";
				varsNum++;
			}
	
			String line="";
			for (int i=0;i<(varsNum*(COLUMN_WIDTH+1))+1;i++){
				line+="-";
			}
			textOut.writeString(line);
			textOut.writeString(varCol);
			textOut.writeString(line);
			
			while (rows.hasNext()){
				DataSetRow nextrow=rows.next();
				int rn=nextrow.getValuesNumber();
				String out="|";
				for (int i=0;i<rn;i++){
					out+=formatStringRightAlign(nextrow.getStringValue(i),COLUMN_WIDTH)+"|";
				}
				textOut.writeString(out);				
				count++;				
			}
			textOut.writeString(line);
			textOut.writeString("\n\n"+count+" records.");
			
		}
		else{
			int count=0;
			textOut.writeString("Listing values. Variable: "+variable+"\n------------------------");
			textOut.writeString("|"+formatStringCentered(variable,COLUMN_WIDTH)+"|");
			Iterator<DataSetRow> rows;
			try {
				rows = ds.getRecords(new String[]{variable});
			} catch (VariableNotFoundException e) {
				throw new ProcedureExecutionException("LIST execution failed.",e);
			}
			while (rows.hasNext()){
				textOut.writeString("|"+formatStringRightAlign(rows.next().getStringValue(0),COLUMN_WIDTH)+"|");				
				count++;
			}
			textOut.writeString("\n\n"+count+" records.");
		}
			
	}
	
	
	private String formatStringCentered(String val,int reqLenght){
		if (val.length()>reqLenght) return val.substring(0,reqLenght);
		else{
			int diff=reqLenght-val.length();
			if (diff%2==0){
				String ws=createWhiteSpaces(diff/2);
				return ws+val+ws;
			}
			else{
				String ws1=createWhiteSpaces(diff/2);
				String ws2=createWhiteSpaces(diff-(diff/2));
				return ws1+val+ws2;
			}
		}
	}

	private String formatStringRightAlign(String val,int reqLenght){
		if (val.length()>reqLenght) return val.substring(0,reqLenght);
		else{
			int diff=reqLenght-val.length();
			
				String ws=createWhiteSpaces(diff);
				return ws+val;
		}
	}
	
	private String createWhiteSpaces(int n){
		String r="";
		for (int i=0;i<n;i++){
			r+=" ";
		}
		return r;
	}

}
