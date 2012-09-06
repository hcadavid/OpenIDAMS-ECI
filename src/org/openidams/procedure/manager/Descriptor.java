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
package org.openidams.procedure.manager;

import script.openidams.compiler.ProcedureProperties;

/**
 * Relation between a procedure name and a procedure implantation
 * @author ECITeam
 * @version 0.0.6
 */
public class Descriptor {
    

    /**
     * Procedure implantation name
     */
    private String procedureClassName;

    /**
     * Procedure properties required by language's parser
     */
    private ProcedureProperties procProperties=null;
    
    /**
     * Creates a descriptor with the given implantation name and procedure name
     * @param classname Implantation name
     * @param pname Procedure name
     */
    public Descriptor(String classname,  ProcedureProperties properties) {
        procedureClassName = classname;
        procProperties=properties;
    }

    /**
     * Returns the procedure implantation name
     * @return Procedure implantation name
     */
    public String getProcedureClassName() {
        return procedureClassName;
    }


    /**
     * Overrides the Object's toString method
     * @return string representation of the instsance
     */
    public String toString() {
        return super.toString() + "[" + this.getProcedureProperties().getProcedureName() + "," +
        procedureClassName + "]";
    }

	/**
	 * @return Returns the procProperties.
	 */
	public ProcedureProperties getProcedureProperties() {
		return procProperties;
	}
}
