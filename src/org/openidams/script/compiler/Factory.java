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
package org.openidams.script.compiler;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.LinkedList;

import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableInstantiationException;
import org.openidams.languages.LanguagePluginException;
import org.openidams.languages.Locator;
import org.openidams.languages.PluggableLanguageDescriptor;
import org.openidams.utilities.Properties;

import script.openidams.compiler.OpenIDAMSCompiler;

/**
 * Compiler factory, which generates a compiler according to the system
 * configuration (compilername<n> propeties)
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Factory {

    /**
     * Class's only instance
     */
    private static Factory instance = null;

    /**
     * Number of compilers registered in the system
     */
    private int compilers;

    /**
     * Names of the registered compilers
     */
    private String[] names;

    /**
     * Extensions of the registered compilers
     */
    private String[] extensions;

    private LinkedList<PluggableLanguageDescriptor> langDescs=null;
    
    /**
     * Factory private constructor
     * @throws LanguagePluginException 
     */
    private Factory() throws LanguagePluginException {
        Properties properties;
        Integer n;
        int i;

        properties = Properties.getInstance();

        if (!new File("supervisor.properties").exists()) {
            throw new RuntimeException(
                    "Configuration file supervisor.properties not found at:"
                            + new File(".").getAbsolutePath());
        } else {
            properties.load(new File("supervisor.properties").getAbsolutePath()
                    .replace("%20", " "));
        }
        
        langDescs= Locator.loadLanguagesPlugInDescriptors();
        
        
        compilers = langDescs.size();

        if (compilers > 0) {
            names = new String[compilers];
            extensions = new String[compilers];

            for (i = 0; i < compilers; i++) {
                names[i] = langDescs.get(i).getLanguageName();
                extensions[i] = langDescs.get(i).getScriptFileExtension();
            }
        }
    }

    /**
     * Returns the class's only instance
     * 
     * @return Class's only instance
     * @throws LanguagePluginException 
     */
    public static Factory getInstance() throws LanguagePluginException {
        if (instance==null){
        	instance=new Factory();
        }
    	return instance;
    }

    /**
     * Builds an instance of a compiler according to the supervisor
     * configuration
     * 
     * @return An instance of a compiler according to the supervisor
     *         configuration
     * @throws LanguagePluginException 
     */
    public ICompiler getCompiler(String file) throws LanguagePluginException {
        ICompiler compiler = null;
        String extension = getExtension(file);
        
        PluggableLanguageDescriptor langDesc=Locator.getLanguageDescriptorByExtension(extension);
        
        String compilerClassName=langDesc.getCompilerClass();
        
        
        //instantiate compiler class
        try {
			compiler=instantiateCompiler(compilerClassName);
		} catch (CompilerInstantationException e) {
			throw new LanguagePluginException("The compiler class defined for the file "+file+" couldn't be instantiated.",e);
		}
        
        return compiler;
    }

    /**
     * Retuns the number of registered compilers
     * @return Number of registered compilers
     */
    public int getCompilers() {
        return compilers;
    }

    /**
     * Returns the name of the i-th compiler (0-based index)
     * @param i number of the compiler
     * @return Name of the i-th compiler (0-based index)
     */
    public String getName(int i) {
        String name = null;

        if ((i >= 0) && (i < compilers)) {
            name = names[i];
        }

        return name;
    }

    
    /**
     * Instantiates a compiler from a registed plugin
     * @param className Name of the class that implements the compiler
     * @return Compiler that corresponds to the given classname
     * @throws CompilerInstantiationException An error ocurred in the process
     */
    @SuppressWarnings("unchecked")
	private static ICompiler instantiateCompiler(String className)
            throws CompilerInstantationException {
        ICompiler _comp = null;
        try {
            Class _c = Class.forName(className);
            Constructor _cons = _c
                    .getConstructor(new Class[] {});
            _comp = (ICompiler) _cons.newInstance();
        } catch (ClassNotFoundException e) {
            throw new CompilerInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (NoSuchMethodException e) {
            throw new CompilerInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalArgumentException e) {
            throw new CompilerInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (InstantiationException e) {
            throw new CompilerInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new CompilerInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (InvocationTargetException e) {
            throw new CompilerInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (ClassCastException e) {
            throw new CompilerInstantationException(className
                    + " isn't a ICompiler subclass.", e);
        }

        return _comp;
    }

    
    
    
    /**
     * Returns the extension of the i-th compiler (0-based index)
     * @param i number of the compiler
     * @return Extension of the i-th compiler (0-based index)
     */
    public String getExtension(int i) {
        String extension = null;

        if ((i >= 0) && (i < compilers)) {
            extension = extensions[i];
        }

        return extension;
    }

    /**
     * Gets the extension of a file name where the file name is of
     * the form {name}.{ext}
     * @param File name
     * @return Extension of the file name
     */
    private String getExtension(String s) {
        String ext = "";

        if (s != null) {
            int i = s.lastIndexOf('.');

            if ((i > 0) && (i < (s.length() - 1))) {
                ext = s.substring(i).toLowerCase();
            }
        }
        return ext;
    }
}