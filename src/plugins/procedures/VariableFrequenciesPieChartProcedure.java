package plugins.procedures;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.openidams.dataset.DataSet;
import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.exceptions.VariableNotFoundException;
import org.openidams.procedure.InputOnlyProcedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.output.ProceduresOutputHandler;
import org.openidams.utilities.DataSetRowsPersistenceHandler;
/**
 * 
 * @author Héctor Fabio Cadavid Rengifo
 *
 */
public class VariableFrequenciesPieChartProcedure extends InputOnlyProcedure {

	String variable;
	int intervals;
	
	public VariableFrequenciesPieChartProcedure(StatementStep pStatement)
			throws ProcedureInstantiationException {
		super(pStatement);
		if (pStatement.getMetadata().getProperty("variables")==null){
			throw new ProcedureInstantiationException("Procedure instantation failed: missing property 'variables'.");
		}
		else{
			variable=pStatement.getMetadata().getProperty("variables");
		}
		if (pStatement.getMetadata().getProperty("intervals")==null){
			throw new ProcedureInstantiationException("Procedure instantation failed: missing property 'intervals'.");
		}
		else{
			String intval=pStatement.getMetadata().getProperty("intervals");
			try{
				intervals=Integer.parseInt(intval.trim());
			}
			catch (NumberFormatException e){
				throw new ProcedureInstantiationException("Property 'intervals' must be an integer value.("+intval+")");	
			}			
		}
	}

	@Override
	public void execute() throws ProcedureExecutionException {
		double max=0;
		double min=0;
		DataSet ds=this.getInputDataSet();
		DataSetRowsPersistenceHandler dsp=new DataSetRowsPersistenceHandler();
		Iterator<DataSetRow> data;
		try {
			data = ds.getRecords(new String[]{variable});
		} catch (VariableNotFoundException e) {
			throw new ProcedureExecutionException("Procedure execution failed:",e);
		}
		
		int itcount=0;
		while (data.hasNext()){
			DataSetRow dsr;
			dsr=data.next();
			
			if (itcount==0){
				min=dsr.getFloatValue(0);
				max=dsr.getFloatValue(0);
			}
			else{
				double val=dsr.getFloatValue(0);
				if (val>max){
					max=val;
				}
				else if (val<min){
					min=val;
				}
			}
			dsp.addRow(data.next());
			
			itcount++;
		}

		Iterator<DataSetRow> savedres=dsp.getSavedRows();
		
		int[] intervalsHits=new int[intervals];
		double intervalsRange=(max-min)/intervals;

		
		String[] intervalLabels=new String[intervals];
		for (int i=0;i<intervals;i++){
			intervalLabels[i]=""+(min+(i*intervalsRange))+"-"+(min+((i+1)*intervalsRange));
		}
		
		while (savedres.hasNext()){
			DataSetRow row=savedres.next();
			double val=row.getFloatValue(0);
			for (int i=0;i<intervals;i++){
				if (val>=(min+(i*intervalsRange)) && val <(min+((i+1)*intervalsRange))){
					intervalsHits[i]++;
				}
			}
		}
		
		JLabel but=new JLabel("");
		JFreeChart jfc=createChart(intervalsHits,intervalLabels);
		
		BufferedImage bim=jfc.createBufferedImage(800,600);
		but.setIcon(new ImageIcon(bim));
		
		ProceduresOutputHandler.getInstance().addGraphicalOutput("Frequencies",but);
		//ProceduresOutputHandler.getInstance().addTextOutput("aaa",""+intervalsHits[0]+","+intervalsHits[1]+","+intervalsHits[2]);

		dsp.close();
	}
	
	
	
	private static JFreeChart createChart(int[] vals,String[] labels)
	{
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for (int i=0;i<vals.length;i++){
			pieDataset.setValue( labels[i], vals[i] );	
		}	    
		JFreeChart pieChart = ChartFactory.createPieChart3D( "Frequencies by value intervals.", pieDataset, false, true, true );
		pieChart.setBackgroundPaint( Color.white );
		return pieChart;
	}

	
	
	

}
