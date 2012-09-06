/**
 * Copyright (C) 2006  OpenIDAMS ECITeam 
 * ECITeam: Gerardo Ospina, HŽctor Cadavid, Camilo Rocha
 * <gospina,hcadavid,hrocha@escuelaing.edu.co>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package plugins.procedures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.openidams.dataset.DataSet;
import org.openidams.dataset.DataSetRow;
import org.openidams.dataset.dictionary.DictionaryVariable;
import org.openidams.procedure.InputOnlyProcedure;
import org.openidams.procedure.Procedure;
import org.openidams.procedure.ProcedureExecutionException;
import org.openidams.procedure.ProcedureInstantiationException;
import org.openidams.script.interpreter.StatementStep;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Procedure that implements the identity method: the output data source is a
 * copy of the input one. In this case a filename is needed to create a report file
 * @author ECITeam
 * @version 0.0.6
 */
public class IdentityReportProcedure extends InputOnlyProcedure {

    /**
     * Name of the report file
     */
    private String reportFile = null;

    /**
     * Creates the procedure
     * @param pStatement Statement step that depends on the procedure
     */
    public IdentityReportProcedure(StatementStep pStatement) throws ProcedureInstantiationException {
        super(pStatement);      
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openidams.supervisor.procedures.Procedure#execute(org.openidams.IDataSet)
     */
    @Override
    public void execute() throws ProcedureExecutionException{
        createReportDocument(this.getInputDataSet(), new File(reportFile));
    }

    /**
     * Creates a report file with the given dataset
     * @param pDataSet Dataset
     * @param file File to be used to save the report
     */
    public static void createReportDocument(DataSet pDataSet, File file) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Iterator<DataSetRow> it = pDataSet.getRecords();
            Iterator<DictionaryVariable> vars = pDataSet.getVariables();

            String header = "";
            while (vars.hasNext()) {
                header = header + " " + vars.next().getLabel();
            }

            document.add(new Paragraph("DATASET PRINTOUT"));
            document.add(new Paragraph(header));

            while (it.hasNext()) {
            	String row = "";
                vars = pDataSet.getVariables();
                DataSetRow drow = it.next();
                
                Iterator valIt=drow.valuesIterator();
                
                while (valIt.hasNext()) {
                    row = row + " "
                            + valIt.next();
                }
                document.add(new Paragraph(row));
            }

        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
