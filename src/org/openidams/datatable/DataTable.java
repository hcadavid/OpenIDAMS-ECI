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
package org.openidams.datatable;

import java.util.Hashtable;
import java.util.Iterator;

import org.openidams.dataset.DataSetRow;

/**
 * Abstract class that defines common functionality for any datatable. A datatable
 * is a container of variables instances organized by rows and columns
 * @author ECITeam 
 * @version 0.0.6
 */
public abstract class DataTable {

    /**
     * Data table metadata
     */
	private Hashtable<String,String> sourceMetadata;
	
	/**
     * Creates a datatable from the given metadata
	 * @param metadata Datatable metadata
	 */
	protected DataTable(Hashtable<String, String> metadata) {
		sourceMetadata = metadata;
	}

	/**
     * Sets the metadata of the datatable
	 * @param sourceMetadata The sourceMetadata to set
	 */
	public void setMetadata(Hashtable<String, String> sourceMetadata) {
		this.sourceMetadata = sourceMetadata;
	}

	/**
     * Returns the description of the datatable
	 * @return Returns the sourceDescription
	 */
	public abstract String getSourceDescription();
	
	/**
     * Returns the metadata of the datatable
	 * @return Returns the sourceMetadata.
	 */
	public Hashtable<String, String> getMetadata() {
		return sourceMetadata;
	}
	
	/**
	 * Returns an iterator over the rows of the datatable
	 * @return Returns DataSetRow Iterator Iterator over the rows of the datatable
	 */
	public abstract Iterator<DataTableRow> getRecords();
	
    /**
     * Adds a record to the end of the datatable
     * @param dsr Row to be added
     */
	public abstract void addRecord(DataTableRow dsr);
}
