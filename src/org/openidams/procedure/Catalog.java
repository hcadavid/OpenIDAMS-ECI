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
package org.openidams.procedure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import org.openidams.procedure.manager.Descriptor;
import org.openidams.procedure.manager.Locator;
import org.openidams.procedure.manager.PlugInException;
import org.openidams.script.interpreter.StatementStep;

import script.openidams.compiler.ProcedureProperties;

/**
 * Procedure catalog collection
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Catalog {

    /**
     * Class's only instance
     */
    private static Catalog instance = null;

    /**
     * Procedures collection indexed by procedure name
     */
    private Hashtable<String,Descriptor> procedures;

    /**
     * Class's private constructor (singleton)
     * @throws PlugInException 
     */
    private Catalog() throws PlugInException {
        procedures = new Hashtable<String,Descriptor>();
        loadProcedures();
    }

    /**
     * Returns the class's only instance
     * 
     * @return Class's only instance
     * @throws PlugInException 
     */
    public static Catalog getInstance() throws PlugInException {
        if (instance==null){
        	instance=new Catalog();
        }
    	return instance;
    }

    /**
     * Loads the available procedures
     * 
     * @throws PlugInException
     *             An error ocurred during the plugin loading
     */
    //TODO remove:
    public void init() throws PlugInException {
        loadProcedures();
    }
    
    /**
     * Get all the available procedures names
     * 
     */
    public Enumeration<String> getAvailableProcedures(){
    	return procedures.keys();
    }

    /**
     * Loads the available procedures
     * 
     * @throws PlugInException
     *             An error ocurred during the plugin loading
     */
    private void loadProcedures() throws PlugInException {
        procedures=new Hashtable<String,Descriptor>();
    	LinkedList<Descriptor> pdesc = Locator.loadPlugInsDescriptors();
        for (Descriptor pd : pdesc) {
            procedures.put(pd.getProcedureProperties().getProcedureName(), pd);
        }
        //TODO remove: addStandardProcedures();
    }
    

    public String getProcedurePath(String name) throws PlugInException{
    	return Locator.getProcedurePath(name);
    }
    
    /**
     * Check if a procedure exists on the catalog given its name
     * @param name parameter's  name
     * @return if exists a procedure with the given name
     */
    public boolean existsProcedure(String name){
    	return procedures.get(name)!=null;
    }
    
    /**
     * 
     * @param name procedure name
     * @return procedure's properties requierd by language's compiler
     */
    public ProcedureProperties getProcedureProperties(String name) throws PlugInException{
    	loadProcedures();
    	Descriptor desc=procedures.get(name);
    	if (desc==null){
    		throw new PlugInException("Missing properties required by "+name+" procedure.");
    	}
    	return desc.getProcedureProperties();
    	
    }
    
    /*  TODO remove:   
    private void addStandardProcedures(){
    	procedures.put("stdout","org.openidams.script.standardprocedures.StandardOutputProcedure");
    	procedures.put("createdataset","org.openidams.script.standardprocedures.EmptyDataSetCreationProcedure");
    	procedures.put("list","org.openidams.script.standardprocedures.ListProcedure");
    	procedures.put("importwinidamsdataset","org.openidams.script.standardprocedures.ImportWinIDAMSDataSetProcedure");    	
    }*/

    /**
     * Builds a procedure according to the given statement and to the procedures
     * previously loaded
     * 
     * @param pStatement
     *            Given statement
     * @return Procedure associated to the given statement
     * @throws ProcedureInstantiationException
     *             An error ocurred when instantiating the procedure
     */
    public Procedure buildProcedure(StatementStep pStatement)
            throws ProcedureInstantiationException {
        Procedure _procedure = null;
        String _name = pStatement.getName();

        if (procedures.keySet().contains(_name)) {
            _procedure = instantiateProcedure(procedures.get(_name).getProcedureClassName(),
                    pStatement);
        } else {
            throw new ProcedureInstantiationException("Procedure <" + _name
                    + "> is unknown");
        }

        return _procedure;
    }

    /**
     * Instantiates the corresponding procedure for the given classname and
     * statement
     * 
     * @param className
     *            Class implementing the procedure
     * @param pStatement
     *            Statement for instantiating a procedure
     * @return Procedure for the given classname and statement
     * @throws ProcedureInstantiationException
     *             An error ocurred instantiating the procedure
     */
    private static Procedure instantiateProcedure(String className,
            StatementStep pStatement) throws ProcedureInstantiationException {
        Procedure _proc = null;
        try {
            Class _c = Class.forName(className);
            Constructor _cons = _c
                    .getConstructor(new Class[] { StatementStep.class });
            _proc = (Procedure) _cons.newInstance(pStatement);
        } catch (ClassNotFoundException e) {
            throw new ProcedureInstantiationException("Procedure class "
                    + className + " couldn't be instantiated.", e);
        } catch (NoSuchMethodException e) {
            throw new ProcedureInstantiationException("Procedure class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalArgumentException e) {
            throw new ProcedureInstantiationException("Procedure class "
                    + className + " couldn't be instantiated.", e);
        } catch (InstantiationException e) {
            throw new ProcedureInstantiationException("Procedure class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new ProcedureInstantiationException("Procedure class "
                    + className + " couldn't be instantiated.", e);
        } catch (InvocationTargetException e) {
            throw new ProcedureInstantiationException("Procedure class "
                    + className + " couldn't be instantiated.", e);
        } catch (ClassCastException e) {
            throw new ProcedureInstantiationException(className
                    + " isn't a Procedure subclass.", e);
        }

        return _proc;
    }
}
