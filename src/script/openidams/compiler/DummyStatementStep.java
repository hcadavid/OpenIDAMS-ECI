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
package script.openidams.compiler;

import org.openidams.procedure.Metadata;

import org.openidams.script.interpreter.ExecutionStep;


/**
 * Dummy statement step
 * @author ECITeam
 * @version 0.0.6
 */
public class DummyStatementStep extends ExecutionStep {

    /**
     * Dummy's constructor
     * @param pName Name of the dummy statement step
     * @param pMetadata Metadata for the dummy statement step
     */
    public DummyStatementStep(String pName, Metadata pMetadata) {
        super(pName, pMetadata);
    }
}
