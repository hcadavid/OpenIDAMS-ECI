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
package org.openidams.script.interpreter;

import org.openidams.procedure.Metadata;


/**
 * Statement's abstract definition
 * The idea behind this design is to avoid high coupling between
 * the procedures implementation and the compilation process. In this way
 * procedures are only instantiated in the moment they are needed, not before. And,
 * if we plan on transporting script scripts over the net, this could be a good choice.
 * @author ECITeam
 * @version 0.0.6
 */
public abstract class StatementStep {

    /**
     * Statement name
     */
    private String name;

    /**
     * Statement metadata
     */
    private Metadata metadata;

    /**
     * Type of StatementStep
     */
    protected String type;

    /**
     * Template method for the constructor
     * @param pName Name of the statement
     * @param pMetadata Metadata, if needed
     */
    protected StatementStep(String pName, Metadata pMetadata) {
        name = pName;
        metadata = pMetadata;
        type = null;
    }

    /**
     * Returns statement's metadata
     * @return Statement's metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Returns statement's name
     * @return Statement's name
     */
    public String getName() {
        return name;
    }
}
