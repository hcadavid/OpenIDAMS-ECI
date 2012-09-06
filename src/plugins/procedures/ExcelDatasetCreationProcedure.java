package plugins.procedures;

import org.openidams.procedure.Procedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.output.ProceduresOutputHandler;
import org.openidams.supervisor.gui.output.TextOutputStream;

public class ExcelDatasetCreationProcedure extends Procedure {

	String name,file,dict,firstrw;
	
	public ExcelDatasetCreationProcedure(StatementStep pStatement) throws ProcedureInstantiationException {
		super(pStatement);
		name=pStatement.getMetadata().getProperty("Name");
		file=pStatement.getMetadata().getProperty("File");
		dict=pStatement.getMetadata().getProperty("Dict");
		firstrw=pStatement.getMetadata().getProperty("FirstRow");
		
	}

	@Override
	public void execute() throws ProcedureExecutionException {
		TextOutputStream tout=ProceduresOutputHandler.getInstance().addTextOutput("Procedure test");
		tout.writeString(name);
		tout.writeString(file);
		tout.writeString(dict);
		tout.writeString(firstrw);
		
	}

	@Override
	public String getDescription() {
		return "dictionary creation procedure";
	}

	@Override
	public String[] getRequiredParameters() {
		return new String[]{};
	}


}
