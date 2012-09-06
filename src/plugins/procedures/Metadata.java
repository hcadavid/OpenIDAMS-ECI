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
package plugins.procedures;

import java.util.Hashtable;
import java.util.Map;

/**
 * Metadata implantation. In this case, the implantation corresponds to a map (function)
 * from strings to strings
 * @author ECITeam
 * @version 0.0.6
 */
public class Metadata {

    /**
     * Map of <key,value>
     */
	private Hashtable<String,String> properties;
	
    /**
     * Creates a metadata instance with an empty map
     */
	public Metadata(){
		properties=new Hashtable<String,String>();
	}

	/**
     * Creates a metadata instance with the given map
	 * @param pProperties Map of given properties
	 */
	public Metadata(Map<String,String> pProperties){
		properties=new Hashtable<String,String>(pProperties);
	}
	
    /**
     * Adds a property to the metadata
     * @param pPropName Key or name of the property
     * @param pValue Value of the property
     */
	public void addProperty(String pPropName,String pValue){
		properties.put(pPropName.toLowerCase(),pValue);
	}
	
    /**
     * Returns the value of property corresponding to the given name
     * @param pPropName Name of the given property
     * @return Value of property corresponding to the given name
     */
	public String getProperty(String pPropName){
		return properties.get(pPropName.toLowerCase());
	}
}
