package plugins.procedures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.openidams.dataset.dictionary.DataType;
import org.openidams.dataset.dictionary.Dictionary;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.dataset.exceptions.InvalidExpressionException;
import org.openidams.procedure.Procedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.output.ProceduresOutputHandler;
import org.openidams.supervisor.gui.output.TextOutputStream;
import org.openidams.utilities.Properties;

import com.thoughtworks.xstream.XStream;

public class DictionaryCreationProcedure extends Procedure {

	String name,variable;
	
	public DictionaryCreationProcedure(StatementStep pStatement) throws ProcedureInstantiationException {
		super(pStatement);
		name=pStatement.getMetadata().getProperty("Name");
		variable=pStatement.getMetadata().getProperty("Variable");
		
	}


	@Override
	public void execute() throws ProcedureExecutionException {		
		/*tout.writeString(name);
		tout.writeString(variable);
		tout.writeString(filter);
		tout.writeString(recode);*/
		
		try {			
			String vars=variable;
			
			//load variables definitions
			StringTokenizer varsTok=new StringTokenizer(vars,";");
			
			LinkedList<DictionaryVariable> dictVars=new LinkedList<DictionaryVariable>();
						
			while (varsTok.hasMoreTokens()){
				String label,name,desc;
				DataType dt;				
				StringTokenizer varsElTok=new StringTokenizer(varsTok.nextToken(),",");
				label=name=varsElTok.nextToken();
				dt=varsElTok.nextToken().equals("n")?DataType.N:DataType.A;
				desc=varsElTok.hasMoreTokens()?varsElTok.nextToken():name+" variable";				
				dictVars.add(new DictionaryVariable(label,name,desc,dt)); 				
			}
			
			Dictionary dict=new Dictionary(dictVars);
			
			//load recodes definitions
				
			XStream xs=new XStream();
			setXMLAliases(xs);
			
			FileWriter fw=new FileWriter(new File(Properties.getInstance().getProperty(Properties.DICTIONARIESPATH)+"/"+name+".dict.xml"));
			fw.write(xs.toXML(dict));
			fw.close();
			//FileOutputStream fos=new FileOutputStream();
		} catch (FileNotFoundException e) {
			throw new ProcedureExecutionException("Dictionary creation failed:"+name,e);
		} catch (NoSuchElementException e){
			throw new ProcedureExecutionException("Dictionary creation failed because the given data is malformed:"+name,e);
		} catch (IOException e){
			throw new ProcedureExecutionException("Dictionary file creation failed:",e);
		}
		
	}
	

	@Override
	public String getDescription() {
		return "dictionary creation procedure";
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[]{};
	}

	private void setXMLAliases(XStream xs){
		xs.alias("DataSetDefinition",org.openidams.dataset.persistence.DataSetDefinition.class);
		xs.alias("DictionaryVariable",org.openidams.dataset.dictionary.DictionaryVariable.class);
		xs.alias("Dictionary",org.openidams.dataset.dictionary.Dictionary.class);
	}


}
