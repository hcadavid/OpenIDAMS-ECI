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
package script.openidams.interpreter;

/**
 * This is the class that defines a procedure argument
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Argument {

    /**
     * Argument type
     */
    private int type;

    /**
     * Argument keyword
     */
    private String keyword;

    /**
     * Argument value
     */
    private String value;

    /**
     * Creates an argument given its value
     * 
     * @param value
     *            Value of the argument
     */
    public Argument(String value) {
        type = 1;
        keyword = null;
        this.value = value;
    }

    /**
     * Creates an argument given its keyword and value
     * 
     * @param keyword
     *            Keyword of the argument
     * @param value
     *            Value of the argument
     */
    public Argument(String keyword, String value) {
        type = 2;
        this.keyword = keyword;
        this.value = value;
    }

    /**
     * Returns the type of the argument
     * 
     * @return Type of the argument
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the keyword of the argument
     * 
     * @return Keyword of the argument
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Returns the value of the argument
     * 
     * @return Value of the argument
     */
    public String getValue() {
        return value;
    }

    /**
     * Overrides the object's toString method
     * @return toString definition of the instance
     */
    public String toString() {
        String rc = null;

        if (type == 1) {
            rc = "[" + type + "] " + value;
        } else if (type == 2) {
            rc = "[" + type + "] " + keyword + "=" + value;
        }
        return rc;
    }
}
