/**
 * Copyright (C) 2006  OpenIDAMS ECITeam 
 * ECITeam: Gerardo Ospina, H�ctor Cadavid, Camilo Rocha
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
package org.openidams.script.compiler;

import org.openidams.script.interpreter.StatementScript;

/**
 * Language compiler functional interface
 * @author ECITeam
 * @version 0.0.6
 */
public interface ICompiler {
    /**
     * Parses a given script file and creates an statement script object
     * @param filename File containing the script
     * @return Statement script object that represents the script statements
     */
    public StatementScript parse(String filename) throws Exception;

    /**
     * Compiles a given script file and creates an statement script object
     * @param filename File containing the script
     * @return Statement script object that represents the script statements
     */
    public StatementScript compile(String filename) throws Exception;
}
