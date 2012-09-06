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
package org.openidams.procedure;

import org.openidams.dataset.DataSet;
import org.openidams.script.interpreter.StatementStep;

/**
 * Abstract procedure class
 * @author ECITeam
 * @version 0.0.6
 */
public abstract class Procedure {

    /**
     * Associated StatementStep
     */
    protected StatementStep statement = null;

    /**
     * Template abstract creator method
     * @param pStatement Associated statement
     */
    protected Procedure(StatementStep pStatement) throws ProcedureInstantiationException{
        statement = pStatement;
    }

    /**
     * Executes the procedure producing a new dataset from the given one
     * @param pInputDataSet Given dataset (inmutable)
     * @param pOutputDataSet Output data set (mutable)
     */
    public abstract void execute() throws ProcedureExecutionException;

 
    /**
     * Return the list of the parameters requiered by the procedure
     * @return
     */
    public abstract String[] getRequiredParameters();

    
    /**
     * Get a procedure's instance description
     * @return procedure's description
     */
    public abstract String getDescription();
    
    
}
