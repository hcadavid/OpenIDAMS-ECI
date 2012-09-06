/**
 * SortProcedure
 * @author Mónica Ramírez Bernal.
 * @date Noviembre 11 de 2006
 */

package plugins.procedures;

import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.dictionary.DataType;
import org.openidams.dataset.exceptions.DataSetOperationException;
import org.openidams.procedure.InputOutputDatasetProcedure;
import org.openidams.procedure.InputOutputProcedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.utilities.DataSetRowsPersistenceHandler;

public class SortProcedure extends InputOutputDatasetProcedure{
	
	private static final int RowsInFile=1000;
	
	private String criterio;
	private String mode;
	private int col;
	
	public SortProcedure(StatementStep pStatement) throws ProcedureInstantiationException {
		super(pStatement);
		String m=pStatement.getMetadata().getProperty("mode");
		
		if (pStatement.getMetadata().getProperty("var")==null || m==null){
			throw new ProcedureInstantiationException("missing 'variable' or 'mode' parameter.");
		}
		else if (!m.equalsIgnoreCase("A") && !m.equalsIgnoreCase("D")){
			throw new ProcedureInstantiationException("'mode' parameter must indicarte Ascending (A) or Descending (D) order");
		}else{
			this.criterio=pStatement.getMetadata().getProperty("var");
			col=0;
			Iterator <String> vars=this.getInputDataSet().getDictionary().getAllVariableNames();
			boolean b=true;
			while(vars.hasNext() && b){
				String s=vars.next();
				if(!s.equals(criterio))
					col++;
				else
					b=false;
			}
			if(b){
				throw new ProcedureInstantiationException("variable "+criterio+" wasn't found on the given DataSet's dictionary.");
			}
			this.mode=m;
		}
	}

	public void execute() throws ProcedureExecutionException{
		try{
			getOutputDataSet().setDictionary(getInputDataSet().getDictionary());
			getOutputDataSet().setVariableNameToColumnNumberMapping(getInputDataSet().getVariableNameToColumnNumberMapping());
			Iterator<DataSetRow> it = this.getInputDataSet().getRecords();
			LinkedList<DataSetRowsPersistenceHandler> dsp= new LinkedList<DataSetRowsPersistenceHandler>();
			boolean hn=it.hasNext();
			
			while(hn){
				DataSetRow[] data=new DataSetRow[RowsInFile];
				int cant=0;
			
				for(int i=0; i<RowsInFile && hn; i++){
					data[i]=it.next();
					cant++;
					if (!it.hasNext())
						hn=false;
				}
				
				dsp.add(order(data, cant));
			}
			joinDataSets(dsp);
			
		}catch(DataSetOperationException e){
        	throw new ProcedureExecutionException("The procedure execution couldn't be terminated because a dataset operation exception:",e);
        }
	}

	private DataSetRowsPersistenceHandler order(DataSetRow[] data, int cant){
		int ini=0;
		int m=1;
		DataSetRowsPersistenceHandler ph=new DataSetRowsPersistenceHandler();
		
		if(col>=this.getInputDataSet().getDictionary().getBasicVariables().size() || this.getInputDataSet().getDictionary().getBasicVariables().get(col).getDataType().equals(DataType.N)){
			QuickSort(data,0,cant-1,col);
		}else{
			QuickSortAlpha(data,0,cant-1,col);
		}
		
		if(mode.equalsIgnoreCase("D")){
			ini=cant-1;
			m=-1;
		}
		
		for(int i=0; i<cant; i++){
			ph.addRow(data[ini+(m*i)]);
		}
		return ph;
	}

	private void QuickSort(DataSetRow[] dataset,int start, int end, int col){
		int i =start;
		int f=end;
		
		float temp=dataset[(i+f)/2].getFloatValue(col);
		
		while(i<=f){
			while(dataset[i].getFloatValue(col)<temp)
				i++;
		
			while (dataset[f].getFloatValue(col)>temp)
				f--;
			
			if (i<=f){
				DataSetRow x=dataset[i];
				dataset[i]=dataset[f];
				dataset[f]=x;
				i++;
				f--;
			}
		}
		
		if (start<f)
			QuickSort(dataset,start,f,col);
		
		if(i<end)
			QuickSort(dataset,i,end, col);
	}
	
	private void QuickSortAlpha(DataSetRow[] dataset,int start, int end, int col){
		int i =start;
		int f=end;
		
		String temp=dataset[(i+f)/2].getStringValue(col);
		
		while(i<=f){
			while(dataset[i].getStringValue(col).compareToIgnoreCase(temp)<0)
				i++;
		
			while (dataset[f].getStringValue(col).compareToIgnoreCase(temp)>0)
				f--;
			
			if (i<=f){
				DataSetRow x=dataset[i];
				dataset[i]=dataset[f];
				dataset[f]=x;
				i++;
				f--;
			}
		}
		
		if (start<f)
			QuickSortAlpha(dataset,start,f,col);
		
		if(i<end)
			QuickSortAlpha(dataset,i,end, col);
	}
	
	private void joinDataSets(LinkedList<DataSetRowsPersistenceHandler> dsp)throws DataSetOperationException{
		LinkedList<Iterator<DataSetRow>>lir = new LinkedList<Iterator<DataSetRow>>();
		LinkedList<DataSetRow> primeros= new LinkedList<DataSetRow>();
		DataSet outds= this.getOutputDataSet();
		
		for(int i=0; i<dsp.size(); i++){
			lir.add(dsp.get(i).getSavedRows());
			primeros.add(lir.get(i).next());
		}
		
		int e=0;
		while(lir.size()!=0){
			if (mode.equalsIgnoreCase("A"))
				e=maxPosicion(primeros);
			else if (mode.equalsIgnoreCase("D"))
				e=minPosicion(primeros);
			
			outds.addRecord(primeros.get(e));
			primeros.remove(e);

			if(lir.get(e).hasNext()){
				primeros.add(e,lir.get(e).next());
			}else{
				dsp.get(e).close();
				dsp.remove(e);
				lir.remove(e);
			}
		}
	}

	private int minPosicion(LinkedList<DataSetRow> rws) {
		int minp=0;
		if(col>=this.getInputDataSet().getDictionary().getBasicVariables().size() || this.getInputDataSet().getDictionary().getBasicVariables().get(col).getDataType().equals(DataType.N)){
			float min=rws.get(0).getFloatValue(col);
			
			for (int i=1;i<rws.size(); i++){
				if(min>rws.get(i).getFloatValue(col)){
					min=rws.get(i).getFloatValue(col);
					minp=i;
				}
			}
		}else{
			String min=rws.get(0).getStringValue(col);
			
			for (int i=1;i<rws.size(); i++){
				if(rws.get(i).getStringValue(col).compareToIgnoreCase(min)<0){
					min=rws.get(i).getStringValue(col);
					minp=i;
				}
			}
		}
		
		return minp;
	}

	private int maxPosicion(LinkedList<DataSetRow> rws) {
		int maxp=0;
		if(col>=this.getInputDataSet().getDictionary().getBasicVariables().size() || this.getInputDataSet().getDictionary().getBasicVariables().get(col).getDataType().equals(DataType.N)){
			float max=rws.get(0).getFloatValue(col);
			
			for (int i=1;i<rws.size(); i++){
				if(max<rws.get(i).getFloatValue(col)){
					max=rws.get(i).getFloatValue(col);
					maxp=i;
				}
			}
		}else{
			String max=rws.get(0).getStringValue(col);
			
			for (int i=1;i<rws.size(); i++){
				if(rws.get(i).getStringValue(col).compareToIgnoreCase(max)>0){
					max=rws.get(i).getStringValue(col);
					maxp=i;
				}
			}
		}
		return maxp;
	}
}

