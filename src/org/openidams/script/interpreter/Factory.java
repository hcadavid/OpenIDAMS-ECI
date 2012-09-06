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
package org.openidams.script.interpreter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openidams.datatable.DataTableInstantiationException;
import org.openidams.languages.LanguagePluginException;
import org.openidams.languages.Locator;
import org.openidams.languages.PluggableLanguageDescriptor;
import org.openidams.script.compiler.CompilerInstantationException;
import org.openidams.script.compiler.ICompiler;

import script.openidams.interpreter.OpenIDAMSInterpreter;

/**
 * Creates an interpreter according to the system configuration
 * @author ECITeam
 * @version 0.0.6
 */
public class Factory {

    /**
     * Classe's only instance
     */
    private static final Factory instance = new Factory();

    /**
     * Singleton constructor
     */
    private Factory() {
    }

    /**
     * Returns the only instance
     * @return Only instance
     */
    public static Factory getInstance() {
        return instance;
    }

    /**
     * Builds an instance of an interpreter
     * @return An instance of an interpreter
     * @throws LanguagePluginException 
     * @throws InterpreterInstantationException 
     */
    public IInterpreter getInterpreter(String filename) throws LanguagePluginException, InterpreterInstantationException {
    	String extension=getExtension(filename);
    	PluggableLanguageDescriptor langDesc=Locator.getLanguageDescriptorByExtension(extension);
    	IInterpreter interp=instantiateInterpreter(langDesc.getInterpreterClass());
    	return interp;
    }
    
    
    /**
     * Instantiates a interpreter from a registed plugin
     * @param className Name of the class that implements the interpreter
     * @return IInterpreter that corresponds to the given classname
     * @throws InterpreterInstantiationException An error ocurred in the process
     */
    @SuppressWarnings("unchecked")
	private static IInterpreter instantiateInterpreter(String className)
            throws InterpreterInstantationException {
    	IInterpreter _interp = null;
        try {
            Class _c = Class.forName(className);
            Constructor _cons = _c
                    .getConstructor(new Class[] {});
            _interp = (IInterpreter) _cons.newInstance();
        } catch (ClassNotFoundException e) {
            throw new InterpreterInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (NoSuchMethodException e) {
            throw new InterpreterInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalArgumentException e) {
            throw new InterpreterInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (InstantiationException e) {
            throw new InterpreterInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new InterpreterInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (InvocationTargetException e) {
            throw new InterpreterInstantationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (ClassCastException e) {
            throw new InterpreterInstantationException(className
                    + " isn't a IInterpreter subclass.", e);
        }

        return _interp;
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
