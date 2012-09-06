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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.dataset.DataSetRow;
import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableRow;

/**
 * This class will be the default dataIterator source to write intermediate results
 * between procedures. This default data source uses heap space to store data.
 * @author ECITeam
 * @version 0.0.6
 */
public class DefaultIntermediateDataTable extends DataTable {

    /**
     * Dataset iterator among rows
     */
	Iterator<DataTableRow> dataIterator;
    
    /**
     * Data implantation with list of rows
     */
	LinkedList<DataTableRow> data;
		

    /**
     * Creates a simple datatable with the given metadata
     * @param metadata Metadata for construting the datatable
     */
	public DefaultIntermediateDataTable(Hashtable<String,String> metadata) {
		super(metadata); 
		data=new LinkedList<DataTableRow>();
	}
	

	/* (non-Javadoc)
	 * @see org.openidams.dataset.DataTable#getRecords()
	 */
	@Override
	public Iterator<DataTableRow> getRecords() {
		return data!=null?data.iterator():dataIterator;
	}

	/* (non-Javadoc)
	 * @see org.openidams.dataset.DataTable#getSourceDescription()
	 */
	@Override
	public String getSourceDescription() {
		return "Intermediate (procedure to procedure) dataIterator source.";
	}

	/* (non-Javadoc)
	 * @see org.openidams.dataset.table.DataTable#addRecord(org.openidams.dataset.DataSetRow)
	 */
	@Override
	public void addRecord(DataTableRow dsr) {
		data.add(dsr);		
	}
}
