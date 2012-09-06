/**
 * Copyright (C) 2006  OpenIDAMS ECITeam 
 * ECITeam: Gerardo Ospina, Héctor Cadavid, Camilo Rocha
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
package org.openidams.datatable.manager;

/**
 * Resolves the relation between a datasource name and its implantation
 * @author ECITeam
 * @version 0.0.6
 */
public class Descriptor {
    // TODO Revisar porque no entiendo bien para qué sirve la clase
    /**
     * Datatable name associated to the descriptor 
     */
    private String datatableName;

    /**
     * Name of the class which handles the datatable access
     */
    private String datatableClassName;

    /**
     * Creates a descriptor with the given class and datatable names
     * @param classname Classname
     * @param name2 dname Datasource name
     */
    protected Descriptor(String classname, String dname) {
        datatableClassName = classname;
        datatableName = dname;
    }

    /**
     * Datatable class name
     * @return Returns the datatableClassName
     */
    public String getTableClassName() {
        return datatableClassName;
    }

    /**
     * Datatable name
     * @return Returns the datatableName
     */
    public String getTableName() {
        return datatableName;
    }

    /**
     * Overrides the Objet's toString method
     * @return toString of the instance
     */
    public String toString() {
        return super.toString() + "[" + datatableName + "," +
        datatableClassName + "]";
    }
}
