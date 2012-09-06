package plugins.procedures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JComponent;

import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.exceptions.DataSetOperationException;
import org.openidams.dataset.exceptions.VariableNotFoundException;
import org.openidams.procedure.InputOnlyProcedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;
import org.openidams.supervisor.gui.output.ProceduresOutputHandler;

public class LinearRegression extends InputOnlyProcedure{

	String Dependent; 
	String Independent;
	String Graphic;
	
    public LinearRegression(StatementStep pStatement) throws ProcedureInstantiationException {
        super(pStatement);
		if ((pStatement.getMetadata().getProperty("Dependent")==null)&&
		    (pStatement.getMetadata().getProperty("Independent")==null)){
			throw new ProcedureInstantiationException("missing 'name' parameter.");
		}
		else{
			Dependent=pStatement.getMetadata().getProperty("Dependent");
			Independent=pStatement.getMetadata().getProperty("Independent");
			Graphic = pStatement.getMetadata().getProperty("Graphic");
		}
    }

	public void execute() throws ProcedureExecutionException {		
		
		Iterator<DataSetRow> it;
		float SumDep=0;
		float SumIndep=0;
    	float sumDepIndep = 0;
    	float sumDepCuad = 0;
		float maxDep=Float.NEGATIVE_INFINITY; 
    	float maxIndep=Float.NEGATIVE_INFINITY;
    	float minValDep=Float.POSITIVE_INFINITY;
    	float minValIndep=Float.POSITIVE_INFINITY;
		try{
			it = this.getInputDataSet().getRecords(new String[] {Dependent,Independent});
		} catch (VariableNotFoundException e) {
			throw new ProcedureExecutionException("Procedure execution failed:",e);
		}
    	int cont=0;
    	boolean valid=true;
    	
    	while ((it.hasNext())&&(valid)){
    		float valDep = it.next().getFloatValue(0);
    		float valIndep = it.next().getFloatValue(1);
    		if (valDep>maxDep){
    			maxDep=valDep;
    		}
    		if (valDep<minValDep){
    			minValDep=valDep;
    		}
    		if (valIndep>maxIndep){
    			maxIndep=valIndep;
    		}
    		if (valIndep<minValIndep){
    			minValIndep=valIndep;
    		}
    		if (valDep<0){
    			valid=false;    			
    			throw new ProcedureExecutionException("Invalid Negative Table Values in Column: " + Dependent + " Row: " + cont);
    		} else {
    			SumDep += valDep;
        		sumDepCuad += (double) Math.pow(valDep,2);   
    		}
    		if (valIndep<0){
    			valid=false;
    			throw new ProcedureExecutionException("Invalid Negative Table Values in Column: " + Independent + " Row: " + cont);
    		} else {
    			SumIndep += valIndep;
        		sumDepIndep += (double) (valDep*valIndep);
    		}
    		cont++;
    	}
   
		if ((maxDep>20)&&(maxDep%20!=0)){		
			maxDep=(maxDep-(maxDep%20))+20;
		}
		if ((maxIndep>20)&&(maxIndep%20!=0)){
			maxIndep=(maxIndep-(maxIndep%20))+20;
		}
		
    	
    	float meanDep= SumDep/cont;    
    	float meanIndep = SumIndep/cont; 
    	

    	//AGREGAR IF VALID=TRUE

    	if (cont<=1){
    		//MENSAJE NUMERO MINIMO DE FILAS 
    	}
    	boolean menorX=false;
    	boolean menorY=false;
    	double[][] matVal=null;
    	int partitionX=0;
    	int partitionY=0;
    	int sendPartX=0;
    	int sendPartY=0;
    	if (maxDep>20){
    		if (maxIndep>20){ 
    			sendPartX=20;
    			sendPartY=20;
    			partitionX=20;
       			partitionY=20;       			
    			matVal = new double[21][21];
    			
    			int valX=(int)Math.ceil(maxDep);   	
    			int beetX=(int) (maxDep/20);
    			for (int x=20; x>0; x--){
    				matVal[x][0]=valX;
    				valX-= beetX;
    			}
    			
    			int valY=(int)Math.ceil(maxIndep);
    			int beetY=(int) (maxIndep/20);
    			for (int y=20; y>0; y--){
    				matVal[0][y]=valY;
    				valY-= beetY;
    			}
    		} else {
    			sendPartX=20;
    			sendPartY=(int)maxIndep;
    			partitionX=20;
       			partitionY=(int)maxIndep+1;        		  		
    			matVal = new double[21][(int)maxIndep+2];
    			
    			int valX=(int)Math.ceil(maxDep); 
    			int beetX=(int) (maxDep/20);
    			for (int x=20; x>0; x--){
    				matVal[x][0]=valX;
    				valX-= beetX;
    			}
    			for (int y=1; y<maxIndep+2; y++){
    				matVal[0][y]=y-1;
    			}
    			menorY=true;
    		}
    	} else {
    		if (maxIndep>20){    		    			
    			partitionX=((int) maxDep)+1;
    			partitionY=20;
    			
    			sendPartX=partitionX-1;
    			sendPartY=20;
    			
    			matVal = new double[((int) maxDep)+2][21];
    			
    			for (int x=1; x<((int) maxDep)+2; x++){
        			matVal[x][0]=x-1;
        		}
    			
    			int valY=(int)Math.ceil(maxIndep);
    			int beetY=(int) (maxIndep/20);
    			for (int y=20; y>0; y--){
    				matVal[0][y]=valY;
    				valY-= beetY;
    			}
    		} else {  	    			 
    			sendPartY=(int)maxIndep; 
    			partitionX=((int) maxDep)+1;
    			partitionY=(int)maxIndep+1;
    			
    			sendPartX= partitionX-1;
    			matVal = new double[((int) maxDep)+2][(int)maxIndep+2];
    			
    			for (int x=1; x<((int) maxDep)+2; x++){
        			matVal[x][0]=x-1;
        		}
    			for (int y=1; y<maxIndep+2; y++){
    				matVal[0][y]=y-1;
    			}
    			menorY=true;
    		}
			menorX=true;
    	}      
    	
    	double covariance=0;
    	double depCuad=0;
    	double sumIndepCuad=0;
		try{
			it = this.getInputDataSet().getRecords(new String[] {Dependent,Independent});
		} catch (VariableNotFoundException e) {
			throw new ProcedureExecutionException("Procedure execution failed:",e);
		}
    	while (it.hasNext()){
    		double valDep = it.next().getFloatValue(0);
    		double valIndep = it.next().getFloatValue(1);    		
    		depCuad += (double) Math.pow(valDep-meanDep,2);
    		sumIndepCuad += (double) Math.pow(valIndep-meanIndep,2);
    		covariance += (double)((double)valDep-(double)meanDep)*((double)valIndep-(double)meanIndep);
    		int posX = NearestX(matVal,valDep,1,partitionX);    	
    		if (!(posX==0)&&!(posX==partitionX)){
    			if (matVal[posX+1][0]-valDep<valDep-matVal[posX][0]){
        			posX=posX+1;
        		}
    		}        
    		int posY = NearestY(matVal,valIndep,1,partitionY);    		
    		if (!(posY==0)&&!(posY==partitionY)){
    			if (matVal[0][posY+1]-valIndep<valIndep-matVal[0][posY]){
        			posY=posY+1;
        		}

    		}    
    		matVal[posX][posY]=-1;
    	}
    	
    	float pearson = (float) ((double)covariance/((double)Math.sqrt(depCuad)*(double)Math.sqrt(sumIndepCuad))); 
    	
    	covariance = (double)covariance/(double)(cont-1);
    	
    	
		// THE ... LINE
		double b1 = (double)((((double)SumDep*(double)SumIndep)-((double)cont*(double)sumDepIndep))/ 
				   ((double)((double)Math.pow(SumDep,2))-((double)cont*(double)sumDepCuad)));
		double b0 = (((double)SumIndep/cont)-(((double)SumDep/(double)cont)*(double)b1));		  

    	representingComponent com = new representingComponent(maxDep,maxIndep,matVal,b0,b1,this.Dependent,this.Independent,sendPartX,sendPartY,menorX,menorY);
    	if ((Graphic!=null)&&(Graphic.equals("yes"))){
    		ProceduresOutputHandler.getInstance().addGraphicalOutput("Dispersion Diagram",com);
    	}
    	

    	Calendar calendar = new GregorianCalendar();
		String res = "";
		
		res += "_____________________________________________________________\n\n";
		res += "OpenIDAMS ECITeam";
		res += "\n\n*** Procedure: Linear Regression";
		res += "\n*** Description: Linear Regression for <" + this.getInputDataSet().getDataSetName() + "> Dataset";
		res += "\n                 Variables <" + this.Dependent + "> <" + this.Independent + ">";
		res += "\n*** Date: " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
		res += "\n_____________________________________________________________";
    	res += "\n\nCovariance: " + covariance;
    	res += "\nPearson's correlation coefficient: " + pearson;
    	res += "\nCoefficient of Determination: " + Math.pow(pearson,2);
    	res += "\n\nlinear regression equation: y= " + b0 + " + " + b1 +"x";  
    	res += "\n\nMean for '" + this.Dependent + "': "+ meanDep;
    	res += "\nMean for '" + this.Independent + "': "+ meanIndep;
    	res += "\n\nMaximum Value for '" + this.Dependent + "': " + maxDep;
    	res += "\nMaximum Value for '" + this.Independent + "': " + maxIndep;
		ProceduresOutputHandler.getInstance().addTextOutput("Linear Regression",res);
	}
	
	public int NearestX(double[][] matVal, double val, int ind1, int ind2){
		int meadle = (ind1+ind2)/2;
		
		if ((ind1==ind2)||(ind1+1==ind2)){
			return meadle;
		}
		else if (matVal[meadle][0]==val){
			return meadle;
		}
		else if (matVal[meadle][0]<val){
			return NearestX(matVal,val,meadle,ind2);
		} 
		else{
			return NearestX(matVal,val,ind1,meadle);
		}
	}
	
	public int NearestY(double[][] matVal, double val, int ind1, int ind2){
		int meadle = (ind1+ind2)/2;
		
		if ((ind1==ind2)||(ind1+1==ind2)){
			return meadle;
		}
		else if (matVal[0][meadle]==val){
			return meadle;
		}
		else if (matVal[0][meadle]<val){
			return NearestY(matVal,val,meadle,ind2);
		} 
		else{
			return NearestY(matVal,val,ind1,meadle);
		}
	}
	
}

class representingComponent extends JComponent {

	int maxX,maxY,numEspX,numEspY;
	String nomVarDep;
	String nomVarIndep;
	
	// For the ... line
	double b0,b1;
	double[][] matVal;
	boolean menorX,menorY;
	
	public representingComponent(float x, float y, double[][] matVal,double b0, double b1, String nomVarDep, 
								 String nomVarIndep, int partitionX, int partitionY, boolean menorX, boolean menorY) {
		this.maxX = (int) Math.floor(x);
		this.maxY = (int) Math.floor(y);		
		this.matVal=matVal;
		this.nomVarDep=nomVarDep;
		this.nomVarIndep=nomVarIndep;
		this.b0 = b0;
		this.b1 = b1;
		this.numEspX=partitionX;
		this.numEspY=partitionY;
		this.menorX=menorX;
		this.menorY=menorY;
	}

	public void paint (Graphics g){

		g.setColor(Color.WHITE);
		g.fillRect(10,10,600,450);
		
		int distBorderToY=80; //Distance from frame left border to Y
		int distBorderToX=400; //Distance from frame top border to X
				
		int startY=90;
		int endX=550; 		
		
		
		
		g.setColor(Color.RED);
		String[] numCharsVarIndep= ((nomVarIndep.trim()).split(""));
		g.drawString("<"+nomVarIndep+">",distBorderToY-(numCharsVarIndep.length*3),startY-15);		
		g.drawString("<"+nomVarDep+">",endX+15,distBorderToX+4);
		
		g.setColor(Color.BLUE);
		g.drawLine(250,45,380,45);
		g.setColor(Color.BLACK);
		g.drawString("DISPERSION DIAGRAM",250,40);
		g.drawLine(distBorderToY,startY,distBorderToY,distBorderToX);
		g.drawLine(distBorderToY,distBorderToX,endX,distBorderToX);
		
		DecimalFormat form = new DecimalFormat("0");
		int posX=(endX-distBorderToY)/(numEspX);
		int show=0;
		int factorValX=0;
		for (int cont=numEspX; cont>=1; cont--){
			int pos=cont;
			if (menorX){
				pos++;
			}
			if (cont==numEspX){
				factorValX=(int)matVal[pos][0];
			}
			g.drawLine((posX*(cont))+distBorderToY,distBorderToX-5,(posX*(cont))+distBorderToY,distBorderToX+5);
			String[] numDigitsX= (String.valueOf(form.format(matVal[cont][0])).trim()).split("");
			int posValX=(numDigitsX.length*2);
			if (maxX>=100){
				if ((show==4)||(cont==numEspX)){
					g.drawString(String.valueOf(form.format(matVal[pos][0])),(posX*(cont))+(distBorderToY-posValX),distBorderToX+20);
					show=0;
				}
				show++;
			}
			else {				
				g.drawString(String.valueOf(form.format(matVal[pos][0])),(posX*(cont))+(distBorderToY-posValX),distBorderToX+20);
			}	
		}
	
		String[] numDigitsY= (String.valueOf(maxY).trim()).split("");
		int posValY = (numDigitsY.length*7);
		

		int posY=(distBorderToX-startY)/numEspY;
		int contAux=numEspY;
		int factorValY=0;
		for (int cont=0; cont<numEspY; cont++){
			int pos=contAux;
			if (menorY){
				pos++;
			}
			if (cont==0){
				factorValY=(int)matVal[0][contAux];
			}			
			g.drawLine(distBorderToY-5,(posY*(cont))+(startY),distBorderToY+5,(posY*(cont))+(startY));
			g.drawString(String.valueOf(form.format(matVal[0][pos])),distBorderToY-posValY,(posY*cont)+(startY+5));
			contAux--;
		}
			
		int factorPosY=posY*(numEspY);		
		int factorPosX=posX*(numEspX);
		
		g.setColor(Color.BLUE);
		int limX=0;
		if (menorX){
			limX=numEspX+2;
		} else {
			limX=numEspX+1;
		}
		int limY=0;
		if (menorY){
			limY=numEspY+2;
		} else {
			limY=numEspY+1;
		}
		for (int x=0; x<limX; x++){			
			for (int y=0; y<limY; y++){
				if (matVal[x][y]==-1){
					double valDep= Integer.parseInt(form.format(matVal[x][0]));
					int posDep = (int) (((double)valDep*(double)factorPosX)/(double)factorValX)+distBorderToY;
					double valIndep = Integer.parseInt(form.format(matVal[0][y]));
					int posIndep = (int) ((double)distBorderToX-(((double)valIndep*(double)factorPosY)/(double)factorValY));
					g.fillOval(posDep,posIndep,4,4);
				}
			}
		}
		
		g.setColor(Color.RED);
		int x1 = (int) distBorderToY;
		int yAux = (int) (b0);
		int y1 = (int) ((double)distBorderToX-(((double)yAux*(double)factorPosY)/(double)factorValY));
		
		if ((b1!=0)&&(factorValX!=0)&&(factorValY!=0)){
			int xAux = (int) (-b0/b1);
			int x2 = (int) ((((double)xAux*(double)factorPosX)/(double)factorValX)+(double)distBorderToY);
			int y2 = (int) distBorderToX;	
		
			if (b1>0){
				int corteX=(int)((maxY-b0)/b1);
				if (corteX>=maxX){
					int corteY=(int)(b0+b1*maxX);
					if ((x1>=distBorderToY)&&(y1<=distBorderToX)){
						x2=endX;					
						y2=corteY;
						y2= (int) ((double)distBorderToX-(((double)y2*(double)factorPosY)/(double)factorValY));
					}
					else {
						x1=endX;					
						y1=corteY;
						y1= (int) ((double)distBorderToX-(((double)y1*(double)factorPosY)/(double)factorValY));
					}
				} else {				
					if ((x1>=distBorderToY)&&(y1<=distBorderToX)){
						x2=corteX;
						x2=(int)((((double)x2*(double)factorPosX)/(double)factorValX)+distBorderToY);
						y2=startY;
					}
					else {
						x1=corteX;
						x1=(int)((((double)x1*(double)factorPosX)/(double)factorValX)+(double)distBorderToY);
						y1=startY;
					}
				}
			}
			else if (b1<0){
				if (yAux>=maxY){					
					int corteX=(int)((maxY-b0)/b1);
					if (xAux>maxX){
						int corteY=(int)(b0+b1*maxX);
						x1=corteX;
						x1=(int)((((double)x1*(double)factorPosX)/(double)factorValX)+(double)distBorderToY);
						y1=startY;
						x2=endX;
						y2=corteY;
						y2= (int) ((double)distBorderToX-(((double)y2*(double)factorPosY)/(double)factorValY));
					}
					else if ((x1<=endX)&&(x1>=0)&&(y1<=distBorderToX)&&(y1>=0)){
						x2=corteX;
						x2=(int)((((double)x2*(double)factorPosX)/(double)factorValX)+(double)distBorderToY);
						y2=startY;
					}
					else {
						x1=corteX;
						x1=(int)((((double)x1*(double)factorPosX)/(double)factorValX)+(double)distBorderToY);
						y1=startY;
					}
				} else if (xAux>=maxX){
 					int corteY=(int)(b0+b1*maxX);
					if ((x1<=endX)&&(y1<=distBorderToX)&&(x1>=0)&&(y1>=0)){
						x2=endX;					
						y2=corteY;
						y2= (int) ((double)distBorderToX-(((double)y2*(double)factorPosY)/(double)factorValY));
					}
					else {
						x1=endX;					
						y1=corteY;
						y1= (int) ((double)distBorderToX-(((double)y1*(double)factorPosY)/(double)factorValY));
					}
				}
			}
			g.drawLine(x1,y1,x2,y2);	
		} 
		else if (b1==0) {
			g.drawLine(x1,y1,x1,startY);
		}

	}

}
