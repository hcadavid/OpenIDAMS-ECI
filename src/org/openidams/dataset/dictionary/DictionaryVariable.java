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

package org.openidams.dataset.dictionary;

/**
 * Class for representing a single value in a dataset
 * @author ECITeam
 * @version 0.0.6
 */
public class DictionaryVariable {

	private DataType dataType;
	
    /**
     * Name of the varible
     */
    private String name;

    /**
     * Label of the variable
     */
    private String label;

    
    /**
     * Variable's description 
     */
    private String description;
    
    /**
     * Creates a new datasetvarible with the given column, label and name
     * @param column Column of the datasetvariable
     * @param label Label of the datasetvariable
     * @param name Name of the datasetvariable
     * @param desc Variable's description
     */
    public DictionaryVariable(String label, String name, String desc,DataType dt) {
        this.label = label;
        this.name = name;
        this.description=desc;
        this.dataType=dt;
    }


    /**
     * Returns the label of the variable
     * @return Returns the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the variable
     * @param label The label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the name of the variable
     * @return name Name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the variable
     * @param name The label to set.
     */
    public void setName(String name) {
        this.name = name;
    }


	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	public DataType getDataType() {
		return dataType;
	}
}
