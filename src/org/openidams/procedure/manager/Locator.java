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
package org.openidams.procedure.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import org.openidams.utilities.ClassPathUpdater;
import org.openidams.utilities.Properties;

import script.openidams.compiler.Parameter;
import script.openidams.compiler.ProcedureProperties;

import com.thoughtworks.xstream.XStream;

/**
 * Class that facilitates the location and instantiation of procedures
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Locator {

    /**
     * Name of the file containing the procedure's implantation
     */
    private static final String CLASSES_JAR = "/classes.jar";

    /**
     * Name of the file containing the procedure's description
     */
    public static final String DESCRIPTOR_XML = "/descriptor.xml";

    /**
     * List of procedures plugins
     */
    private static LinkedList<Descriptor> plugins;

    /**
     * Map of procedures, with names as a key
     */
    private static Hashtable<String,Descriptor> procedurePluginsMap=null;

    /**
     * Map of procedures directories, with names as a key
     */
    private static Hashtable<String,String> proceduresPath=null;
    
    
    /**
     * Loads the available procedures plugins
     * 
     * @return List of plugins
     * @throws PlugInException
     *             An error ocurred when loading at least one procedure
     */
    public static LinkedList<Descriptor> loadPlugInsDescriptors()
            throws PlugInException {

        if (plugins == null) {

            File f = new File(Properties.getInstance().getProperty(
                    Properties.PROCEDUREPLUGINSROOT).replaceAll("%20", " "));

            if (!f.exists()){
            	throw new PlugInException("The directory:"+f.getAbsolutePath()+" defined as the procedure plugins directory, doesn't exists.");
            }
            
            File[] files = f.listFiles();

            if (files == null) {
                throw new PlugInException("Plugin directory not found: "
                        + Properties.getInstance().getProperty(
                                "procedurespluginsroot").replaceAll("%20", " "));
            } else {
            	
                plugins = new LinkedList<Descriptor>();
                procedurePluginsMap=new Hashtable<String,Descriptor>();
                proceduresPath=new Hashtable<String,String>();
                
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()
                            && new File(files[i].toString() + DESCRIPTOR_XML)
                                    .exists()
                            && new File(files[i].toString() + CLASSES_JAR)
                                    .exists()) {

                        try {
                            Descriptor pd = loadProcedureDescriptor(new File(
                                    files[i].toString() + DESCRIPTOR_XML));
                            ClassPathUpdater.addFile(files[i].toString()
                                    + CLASSES_JAR);
                            
                            plugins.add(pd);
                            
                            procedurePluginsMap.put(pd.getProcedureProperties().getProcedureName(),pd);
                                                        
                            proceduresPath.put(pd.getProcedureProperties().getProcedureName(),files[i].getAbsolutePath().replaceAll("%20"," "));
                        } catch (FileNotFoundException ex) {
                            throw new PlugInException(
                                    "Plugged procedure located on "
                                            + files[i].toString()
                                            + " could not be loaded", ex);
                        } catch (IOException ex) {
                            throw new PlugInException(
                                    "Plugged procedure located on "
                                            + files[i].toString()
                                            + " could not be loaded", ex);
                        }
                    }
                }
            }
        }
        return plugins;
    }

    
    public static String getProcedurePath(String name) throws PlugInException{
    	if (proceduresPath==null){
    		loadPlugInsDescriptors();
    	}
    	String path=null;
    	
    	path=proceduresPath.get(name);
    	if (path==null) throw new PlugInException("Missing required plugin for procedure "+name+".");
    	
    	return path;
    	
    }
    
    /**
     * 
     * @param name
     * @return
     * @throws PlugInException
     */
    public Descriptor getProcedureDescriptor(String name) throws PlugInException{
    	if (procedurePluginsMap==null){
    		loadPlugInsDescriptors();
    	}
    	Descriptor des=null;
    	
    	des=procedurePluginsMap.get(name);
    	if (des==null) throw new PlugInException("Missing required plugin for procedure "+name+".");
    	
    	return des;
    }
    
    
    /**
     * Loads the descriptor within the given file
     * @param xmlfile File containing the descriptor
     * @return Descriptor included in the given file
     * @throws FileNotFoundException File not found
     * @throws PlugInException Problem creating the descriptor
     */
    private static Descriptor loadProcedureDescriptor(File xmlfile)
            throws FileNotFoundException, PlugInException {
        XStream xs = new XStream();
        setXMLAliases(xs);
        try {
            return (Descriptor) xs.fromXML(new FileReader(xmlfile));
        } catch (ClassCastException ex) {
            throw new PlugInException("The XML file hasn't a valid format.", ex);
        }
    }

    /**
     * Sets the aliases for the Descriptor loading (required by XStream)
     * @param xs Metadata holder
     */
    private static void setXMLAliases(XStream xs) {
		xs.alias("procedure", ProcedureProperties.class);
        xs.alias("parameter", Parameter.class);
        xs.alias("procedureDescriptor", Descriptor.class);
    }
}
