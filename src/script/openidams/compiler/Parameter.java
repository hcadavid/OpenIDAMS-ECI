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

import java.lang.reflect.Array;

/**
 * This is the class that defines a procedure parameter
 * @author ECITeam
 * @version 0.0.8
 */
public class Parameter {

    /**
     * Parameter name
     */
    private String name;

    /**
     * Paramter type
     */
    private int type;

    /**
     * Is parameter mandatory?
     */
    private boolean mandatory;

    /**
     * is a multiple parameter?
     */
    @SuppressWarnings("unused")
    private boolean multiple;

    /**
     * Parameter keywords
     */
    private String[] keywords;

    /**
     * Parameter values
     */
    @SuppressWarnings("unused")
    private String[] values;

    /**
     * Parameter defaultvalue
     */
    private String defaultValue;

    /**
     * Parameter values count
     */
    private int count;

    /**
     * Creates a simple parameter
     */
    public Parameter() {
        type = -1;
        mandatory = false;
        multiple = false;
        keywords = null;
        values = null;
        count = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lexeme DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public boolean match(String lexeme) throws Exception {
        boolean rc;
        int id;
        int length;

        switch (type) {
        case 1:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:

            if (keywords != null) {
                rc = keywords[0].toLowerCase().compareTo(lexeme.toLowerCase()) == 0;

                //System.out.println("\t\t\t\tParameter.match(" + lexeme + ")=[" + keywords[0] +"]? " + rc);			
            } else {
                keywords = new String[1];
                keywords[0] = lexeme;
                rc = true;
            }

            break;

        case 2:

            if (keywords == null) {
                rc = false;
            } else {
                length = Array.getLength(keywords);
                id = 0;

                while ((id < length) &&
                        (keywords[id].toLowerCase().compareTo(lexeme.toLowerCase()) != 0)) {
                    id++;
                }

                rc = id < length;
            }

            break;

        default:
            rc = false;
        }

        return rc;
    }

    /**
     * Return the paramer name
     * @return Parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the parameter type
     * @return Parameter type
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the mandatority of the parameter
     * @return Mandatority of the parameter
     */
    public boolean getMandatory() {
        return mandatory;
    }

    /**
     * Returns the values' count of the parameter
     * @return Values' count of the parameter
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments the values's count in one unit
     */
    public void incCount() {
        count++;
    }

    /**
     * Returns the default value
     * @return Default value
     */
    public String getDefault() {
        return defaultValue;
    }

    /**
     * Modifies the type
     * @param type Type of the param
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Modifies the default value
     * @param defaultValue Default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Modifies the keywords list
     * @param keywords Keywords list
     */
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    /**
     * Modifies the mandatority
     * @param mandatory Mandatority
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * Modifies if the parameter is multple or not
     * @param multiple The parameter is multple or not
     */
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    /**
     * Modifies the values
     * @param values Values of the parameter
     */
    public void setValues(String[] values) {
        this.values = values;
    }
    
    public String toString(){
    	return "("+this.count+","+this.defaultValue+","+this.name+","+this.type+")"; 
    }
}
