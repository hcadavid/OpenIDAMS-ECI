package org.openidams.languages;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import org.openidams.datatable.manager.PlugInSourceHandlerException;
import org.openidams.utilities.ClassPathUpdater;
import org.openidams.utilities.Properties;

import com.thoughtworks.xstream.XStream;

/**
 * Locator of languages definition
 * @author ECITeam
 * @version 0.0.8
 * 
 */
public class Locator {

    /**
     * Name of the file which contains the plugin implantation
     */
    private static final String CLASSES_JAR = "/classes.jar";

    /**
     * Name of the descriptor of each plugin implantation
     */
    private static final String DESCRIPTOR_XML = "/descriptor.xml";

    /**
     * List of registed plugins
     */
    private static LinkedList<PluggableLanguageDescriptor> plugins;
    
    /**
     * Map of registed languages names
     */    
    private static Hashtable<String,PluggableLanguageDescriptor> languagePluginsNamesMap;
    
    /**
     * Map of registed languages extensions
     */    
    private static Hashtable<String,PluggableLanguageDescriptor> languagePluginsExtensionsMap;

    
    /**
     * Loads the available plugins
     * @return List of available plugins
     * @throws PlugInSourceHandlerException An error ocurred during the instation of
     * at least one plugin
     */
    public static LinkedList<PluggableLanguageDescriptor> loadLanguagesPlugInDescriptors()
            throws LanguagePluginException {
        // Loaded for first time?
        if (plugins == null) {
            File f = new File(Properties.getInstance().getProperty(
                    Properties.LANGUAGESPLUGINROOT));
            if (!f.exists()){
            	throw new LanguagePluginException("The directory:"+f.getAbsolutePath()+" defined as the languages plugins directory, doesn't exists.");
            }
            
            File[] files = f.listFiles();

            plugins = new LinkedList<PluggableLanguageDescriptor>();

            languagePluginsNamesMap=new Hashtable<String,PluggableLanguageDescriptor>();
            
            languagePluginsExtensionsMap=new Hashtable<String,PluggableLanguageDescriptor>();
            
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()
                        && new File(files[i].toString() + DESCRIPTOR_XML)
                                .exists()
                        && new File(files[i].toString() + CLASSES_JAR).exists()) {
                    try {
                        PluggableLanguageDescriptor pd = loadLanguagePluginDescriptor(new File(
                                files[i].toString() + DESCRIPTOR_XML));
                        ClassPathUpdater.addFile(files[i].toString()
                                + CLASSES_JAR);
                        plugins.add(pd);
                        languagePluginsNamesMap.put(pd.getLanguageName(),pd);
                        languagePluginsExtensionsMap.put(pd.getScriptFileExtension(),pd);
                        
                    } catch (FileNotFoundException ex) {
                        throw new LanguagePluginException(
                                "Plugged procedure located on "
                                        + files[i].toString()
                                        + " could not be loaded", ex);
                    } catch (IOException ex) {
                        throw new LanguagePluginException(
                                "Plugged procedure located on "
                                        + files[i].toString()
                                        + " could not be loaded", ex);
                    }

                }
            }
        }
        return plugins;
    }
    
    
    /**
     * 
     * @param langName load a descriptor by a given language name
     * @return language descriptor
     * @throws LanguagePluginException if there isn't a descriptor related with the given name
     */
    public static PluggableLanguageDescriptor getLanguageDescriptorByName(String langName) throws LanguagePluginException{
    	PluggableLanguageDescriptor langDesc=null;
    	if (languagePluginsNamesMap==null){
    		loadLanguagesPlugInDescriptors();
    	}    	
    	
    	langDesc=languagePluginsNamesMap.get(langName);
    	if (langDesc==null) throw new LanguagePluginException("Missing languange plugin required for "+langName+".");    	
    	return langDesc;    	    	
    }


    /**
     * 
     * @param langName load a descriptor by a given language script extension
     * @return language descriptor
     * @throws LanguagePluginException if there isn't a descriptor related with the given extension
     */
    public static PluggableLanguageDescriptor getLanguageDescriptorByExtension(String langFileExtension) throws LanguagePluginException{
    	PluggableLanguageDescriptor langDesc=null;
    	if (languagePluginsExtensionsMap==null){
    		loadLanguagesPlugInDescriptors();
    	}    	
    	
    	langDesc=languagePluginsExtensionsMap.get(langFileExtension);
    	if (langDesc==null) throw new LanguagePluginException("Missing languange plugin required for script with extension "+langFileExtension+".");    	
    	return langDesc;    	    	
    }

    
    /**
     * Loads the descriptor located in the given xmlfile
     * @param xmlfile File containing a descriptor
     * @return Descriptor contained in the given file
     * @throws FileNotFoundException File not found
     * @throws PlugInSourceHandlerException Malformed plugin
     */
    private static PluggableLanguageDescriptor loadLanguagePluginDescriptor(File xmlfile)
            throws FileNotFoundException, LanguagePluginException {
        XStream xs = new XStream();
        setXMLAliases(xs);
        try {
            return (PluggableLanguageDescriptor) xs.fromXML(new FileReader(xmlfile));
        } catch (ClassCastException ex) {
            throw new LanguagePluginException(
                    "The XML file: "+xmlfile+" doesn't have a valid format for a language plugin descriptor", ex);
        }
    }

    /**
     * Sets the aliases for the xml loading
     * @param xs XStream metadata descriptor
     */
    private static void setXMLAliases(XStream xs) {
		xs.alias("LanguageDescriptor",PluggableLanguageDescriptor.class);
    }
}
