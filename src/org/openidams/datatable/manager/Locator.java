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
package org.openidams.datatable.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.openidams.utilities.ClassPathUpdater;
import org.openidams.utilities.Properties;

import com.thoughtworks.xstream.XStream;

/**
 * Locator of datasources definition
 * @author ECITeam
 * @version 0.0.6
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
    private static LinkedList<Descriptor> plugins;

    /**
     * Loads the available plugins
     * @return List of available plugins
     * @throws PlugInSourceHandlerException An error ocurred during the instation of
     * at least one plugin
     */
    public static LinkedList<Descriptor> loadPlugInSourceHandlersDescriptors()
            throws PlugInSourceHandlerException {
        // Is the first time?
        if (plugins == null) {
            File f = new File(Properties.getInstance().getProperty(
                    Properties.DATAPLUGINSROOT));
            if (!f.exists()){
            	throw new PlugInSourceHandlerException("The directory:"+f.getAbsolutePath()+" defined as the data plugins directory, doesn't exists.");
            }
            File[] files = f.listFiles();

            plugins = new LinkedList<Descriptor>();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()
                        && new File(files[i].toString() + DESCRIPTOR_XML)
                                .exists()
                        && new File(files[i].toString() + CLASSES_JAR).exists()) {
                    try {
                        Descriptor pd = loadDataTableDescriptor(new File(
                                files[i].toString() + DESCRIPTOR_XML));
                        ClassPathUpdater.addFile(files[i].toString()
                                + CLASSES_JAR);
                        plugins.add(pd);
                    } catch (FileNotFoundException ex) {
                        throw new PlugInSourceHandlerException(
                                "Plugged procedure located on "
                                        + files[i].toString()
                                        + " could not be loaded", ex);
                    } catch (IOException ex) {
                        throw new PlugInSourceHandlerException(
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
     * Loads the descriptor located in the given xmlfile
     * @param xmlfile File containing a descriptor
     * @return Descriptor contained in the given file
     * @throws FileNotFoundException File not found
     * @throws PlugInSourceHandlerException Malformed plugin
     */
    private static Descriptor loadDataTableDescriptor(File xmlfile)
            throws FileNotFoundException, PlugInSourceHandlerException {
        XStream xs = new XStream();
        setXMLAliases(xs);
        try {
            return (Descriptor) xs.fromXML(new FileReader(xmlfile));
        } catch (ClassCastException ex) {
            throw new PlugInSourceHandlerException(
                    "The XML file does not have a valid format", ex);
        }
    }

    /**
     * Sets the aliases for the xml loading
     * @param xs XStream metadata descriptor
     */
    private static void setXMLAliases(XStream xs) {
        xs.alias("Descriptor", Descriptor.class);
    }
}
