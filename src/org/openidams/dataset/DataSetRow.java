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

package org.openidams.dataset;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Dummy implantation of a row of a datatable. Each instance of thi class
 * corresponds to a record of a dataset.
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class DataSetRow {

    /**
     * Values of the datasetrow
     */
    private LinkedList<String> values;

    /**
     * Creates a datasetrow with the given values' names
     * 
     * @param values
     *            Values of the datasetrows
     */
    public DataSetRow(String[] values) {
        this.values = new LinkedList<String>();
        for (String v : values) {
            this.values.add(v);
        }
    }

    
    /**
     * Creates a datasetrow with empty values set
     * 
     */
    public DataSetRow(){
    	this.values = new LinkedList<String>();
    }
    
    /**
     * Returns an iterator over the values of the datasetrow
     * 
     * @return Iterator over the values of the datasetrow
     */
    public Iterator<String> valuesIterator() {
        return values.iterator();
    }

    /**
     * Returns the long value corresponding to the given column
     * 
     * @param column
     *            Column for which the value is required
     * @return Value corresponding to the given column
     */
    public Long getIntValue(int column) {
        return Long.parseLong(values.get(column));
    }

    /**
     * Returns the String value corresponding to the given column
     * 
     * @param column
     *            Column for which the value is required
     * @return Value corresponding to the given column
     */
    public String getStringValue(int column) {
        return values.get(column);
    }

    /**
     * Returns the float value corresponding to the given column
     * 
     * @param column
     *            Column for which the value is required
     * @return Value corresponding to the given column
     */
    public Float getFloatValue(int column) {
        return Float.parseFloat(values.get(column));
    }

    /**
     * add a value at the end of the values list
     * @param v value
     */
    public void addValue(String v){
    	values.add(v);
    }
    
    
    /**
     * return the number of values associated to this row
     * @return values number
     */
    public int getValuesNumber(){
    	return values.size();
    	
    }
    
}
