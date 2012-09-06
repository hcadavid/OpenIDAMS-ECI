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
package org.openidams.datatable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.LinkedList;

import org.openidams.datatable.manager.Descriptor;
import org.openidams.datatable.manager.Locator;
import org.openidams.datatable.manager.PlugInSourceHandlerException;

/**
 * DataSet catalog implantation. The datasources are loaded under the
 * DEFAULT_INTERMEDIATE_DATA_TABLE_CLASS class type
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Catalog {

    /**
     * Name of the class for loading the catalog of datasources
     */
    private static final String DEFAULT_INTERMEDIATE_DATA_TABLE_CLASS = "plugins.data.DefaultIntermediateDataTable";
    private static final String DEFAULT_INTERMEDIATE_MEMORY_SAFE_DATA_TABLE_CLASS = "plugins.data.DefaultMemorySafeDataTable";
    private static final String WINIDAMS_DATA_TABLE_CLASS = "plugins.data.WinIdamsDataFileDataTable";

    /**
     * Default available datatable names 
     */
    public static final String INTERMEDIATE="Intermediate";
    public static final String OODB_DATA_FILE="OODBDataFile";
    public static final String WINIDAMS_DATA_FILE="WinIDAMSDataFile";
    
    
    /**
     * Hashtable of names of datables over the location of datatables
     */
    private Hashtable<String, String> datatables;

    /**
     * Singleton attribute
     */
    private static Catalog instance = new Catalog();

    /**
     * Private constructor of the catalog (singleton)
     */
    private Catalog() {
        datatables = new Hashtable<String, String>();
    }

    /**
     * Returns the only instance of the class
     * 
     * @return Singleton of the class
     */
    public static Catalog getInstance() {
        return instance;
    }

    /**
     * Initializes the catalog by loading the datasources
     * 
     * @throws PlugInSourceHandlerException
     *             An error ocurred during the process
     */
    public void init() throws PlugInSourceHandlerException {
        loadDataTables();
    }

    /**
     * Loads the available data sources, the standard ones (wired) and the
     * dynamic ones (plugins)
     * @throws PlugInSourceHandlerException An error ocurred during the process
     */
    private void loadDataTables() throws PlugInSourceHandlerException {
        LinkedList<Descriptor> sources = Locator
                .loadPlugInSourceHandlersDescriptors();

        // All the standard datasources are created
        datatables.put(INTERMEDIATE, DEFAULT_INTERMEDIATE_DATA_TABLE_CLASS);
        datatables.put(OODB_DATA_FILE, DEFAULT_INTERMEDIATE_MEMORY_SAFE_DATA_TABLE_CLASS);
        datatables.put(WINIDAMS_DATA_FILE, WINIDAMS_DATA_TABLE_CLASS);


        // Dinamic lodaded datasources
        for (Descriptor ds : sources) {
            datatables.put(ds.getTableName(), ds.getTableClassName());
        }
    }

    /**
     * Builds a datasource according to the given logical name and metadata
     * @param logicalName Logical name of the datasource
     * @param metadata Metadata of the datasource. In this case the metadata is a pair <key,value>
     * @return Datatable associated to the given logicalName
     * @throws DataTableInstantiationException An error ocurred when loading the datasource
     */
    public DataTable buildDataTable(String logicalName,
            Hashtable<String, String> metadata)
            throws DataTableInstantiationException {
        DataTable _dataTable = null;
        String _name = logicalName;

        if (datatables.keySet().contains(_name)) {
            _dataTable = instantiateDataTable(datatables.get(_name), metadata);
        } else {
            throw new DataTableInstantiationException("Datatable <" + _name
                    + "> is unknown");
        }

        return _dataTable;
    }

    /**
     * Instantiatesa datasource from a registed plugin
     * @param className Name of the class that implements the datasource
     * @param metadata Metadata of teh datasource
     * @return Datasource that corresponds to the given classname
     * @throws DataTableInstantiationException An error ocurred in the process
     */
    private static DataTable instantiateDataTable(String className,
            Hashtable<String, String> metadata)
            throws DataTableInstantiationException {
        DataTable _ds = null;
        try {
            Class _c = Class.forName(className);
            Constructor _cons = _c
                    .getConstructor(new Class[] { Hashtable.class });
            _ds = (DataTable) _cons.newInstance(metadata);
        } catch (ClassNotFoundException e) {
            throw new DataTableInstantiationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (NoSuchMethodException e) {
            throw new DataTableInstantiationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalArgumentException e) {
            throw new DataTableInstantiationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (InstantiationException e) {
            throw new DataTableInstantiationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new DataTableInstantiationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (InvocationTargetException e) {
            throw new DataTableInstantiationException("DataTable class "
                    + className + " couldn't be instantiated.", e);
        } catch (ClassCastException e) {
            throw new DataTableInstantiationException(className
                    + " isn't a DataTable subclass.", e);
        }

        return _ds;
    }
}
