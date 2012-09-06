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
package org.openidams.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



/**
 * OpenIDAMS properties object (singleton)
 * @author ECITeam
 * @version 0.0.6
 */
public class Properties {
    
	public static String HOME_PATH;
	
	static{
		//calculate application local path
		String pkgName=Properties.class.getName();
		int pkgNum=0;
		String absPath=Properties.class.getResource(".").getFile().replaceAll("%20"," ");
		for (int i=0;i<pkgName.length();i++){
			if (pkgName.charAt(i)=='.') pkgNum++;
		}
		for (int i=0;i<pkgNum+1;i++){
			absPath=new File(absPath).getParent();
		}
		
		HOME_PATH=absPath;		
	}
	
	/**
     * Class' only instance
     */
    private static final Properties instance = new Properties();

    /**
     * Properties collection
     */
    private java.util.Properties properties;

    
    /**
     * Properties Id for the root of the data plugins
     */
    public static final String DATAPLUGINSROOT = "datapluginsroot";

    /**
     * Properties Id for the root of the procedure plugins
     */
    public static final String PROCEDUREPLUGINSROOT = "procedurespluginsroot";

    /**
     * Properties Id for the root of the languages plugins
     */
    public static final String LANGUAGESPLUGINROOT = "languagespluginsroot";
    
    
    /**
     * Properties Id for the location of the dictionaries directory 
     */
    public static final String DICTIONARIESPATH = "dictionariespath";


    /**
     * Properties Id for the location of the data sets descriptor files
     */
    public static final String DATASETSPATH = "datasetspath";

    /**
     * Properties Id for the location of the data files
     */
    public static final String DATAFILESPATH = "datafilespath";
    

    /**
     * Properties Id for the location of the WinIDAMS dictionaries and data files
     */
    public static final String WINIDAMSFILES = "winidamsfiles";

    
    /**
     * Properties Id for the location of temporary files
     */
    public static final String TEMP = "temporaryfilespath";
    
    
    
    
    
    /**
     * Private constructor for the singleton
     */
    private Properties() {
        properties = new java.util.Properties();
    }

    /**
     * Returns this class's only instance
     * @return This class's only instance
     */
    public static Properties getInstance() {
        return instance;
    }

    /**
     * Loads properties from file
     *
     */
    public void load(String filename) {
    	properties.clear();
        try {
            properties.load(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Supervisor properties file not found (" +
                filename + ") ", e);
        } catch (IOException e) {
            throw new RuntimeException(
                "Error reading supervisor properties file (" + filename + ") ",
                e);
        }
    }

    /**
     * Loads the supervisor properties from the supervisor.properties file
     */
    protected void defaultLoad() {
        load(Properties.class.getResource("supervisor.properties").getFile()
                             .replaceAll("%20", " "));
    }

    /**
     * Returns the property value associated to the pPropName name
     * @param pPropName Name of the desired property
     * @return Value associated to pPropName
     */
    public String getProperty(String pPropName) {
        String property = null;

        property = properties.getProperty(pPropName);        
        
        return setPropertyVariablesValues(property);
    }
    
    
    /*
     * Replace all the supported variables defined inside variables (%VAR%)
     */
    private String setPropertyVariablesValues(String prop){
    	if (prop.indexOf("%HOME_PATH%")==0){
    		prop=prop.replaceAll("%HOME_PATH%","");
    		prop=HOME_PATH.concat(prop);   		
    	}
   		return prop;	   	
    }
    
    
}
