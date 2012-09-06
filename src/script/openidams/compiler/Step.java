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

/**
 * @author ECI Team
 */

import java.util.Vector;
import script.openidams.interpreter.Argument;

/**
 * This is the class that defines a script step
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Step {
    
    /**
     * Step's name
     */
    private String name;

    /**
     * Step's arguments
     */
    private Vector <Argument>arguments;

    /**
     * Creates a step with the given name an arguments
     * @param name Name
     * @param arguments Arguments
     */
    public Step(String name, Vector <Argument>arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    /**
     * Returns the step's name
     * @return Step's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an array containing the steps parameters
     * @return Array containing the steps parameters
     */
    public Object[] getArguments() {
        return arguments.toArray();
    }
}
