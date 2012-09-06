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
package plugins.data;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.CompoundFile;

import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableException;
import org.openidams.datatable.DataTableRow;

import common.Logger;

/**
 * Datatable implantation to read/write from a excel file
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class ExcelDataTable extends DataTable {

    /**
     * Excel file location
     */
    private File fileLocation;

    /**
     * Excel file sheet name
     */
    private String sheetName;

    /**
     * Index of first row
     */
    private int firstRowIndex;

    /**
     * Excel file sheet
     */
    private Sheet sheet;

    /**
     * Creates an excel datatable
     * 
     * @param metadata
     *            Metadata of the datatable
     * @throws DataTableException
     *             Problems creating the datatable
     */
    public ExcelDataTable(Hashtable<String, String> metadata)
            throws DataTableException {
        super(metadata);

        if (this.getMetadata().get("fileLocation") == null) {
            throw new DataTableException("Missing metadata: fileLocation.");
        }
        if (this.getMetadata().get("sheetName") == null) {
            throw new DataTableException("Missing metadata: sheetName.");
        }
        try {
            fileLocation = new File(this.getMetadata().get("fileLocation"));
            sheetName = this.getMetadata().get("sheetName");
            firstRowIndex = Integer.parseInt(this.getMetadata().get(
                    "firstRowIndex"));
        } catch (NumberFormatException e) {
            throw new DataTableException(
                    "Wrong metadata format: firstRowIndex must be an integer.",
                    e);
        } catch (NullPointerException e) {
            throw new DataTableException("Missing metadata: firstRowIndex.", e);
        }
        if (!fileLocation.exists()) {
            throw new DataTableException(
                    "Wrong metadata value: fileLocation. File " + fileLocation
                            + " doesn't exists.");
        }
        

        try {
            Workbook wb = Workbook.getWorkbook(fileLocation);
            
            sheet = wb.getSheet(sheetName);                        
        
            if (sheet==null){
            	throw new DataTableException("Excel sheet opening failed: the file ["+fileLocation+"] doesn't exists or hasn't the '"+sheetName+"' sheet defined");
            }
            
        } catch (BiffException e) {
            throw new DataTableException("Excel sheet read failed.", e);
        } catch (IOException e) {
            throw new DataTableException("Excel sheet read failed.", e);
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openidams.DataSource#getRecords()
     */
    @Override
    public Iterator<DataTableRow> getRecords() {

        return new Iterator<DataTableRow>() {
        	
            int rcount = firstRowIndex;

            public DataTableRow next() {
                Cell cells[] = sheet.getRow(rcount);
                String rowdata[] = new String[cells.length];
                for (int i = 0; i < cells.length; i++) {
                    rowdata[i] = cells[i].getContents();
                }
                rcount++;
                return new DataTableRow(rowdata);
            }

            public void remove() {
            }

            public boolean hasNext() {
                return rcount < sheet.getRows();
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openidams.DataSource#getSourceDescription()
     */
    @Override
    public String getSourceDescription() {
        return "Excel dataIterator source";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.openidams.datahandlers.DataSource#addRecord(org.openidams.DataSetRow)
     */
    @Override
    public void addRecord(DataTableRow dsr) {
    	throw new RuntimeException("Excel DataTable doesn't suppor record-writing.");
    }
}
