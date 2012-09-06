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
package org.openidams.dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import org.openidams.dataset.dictionary.Dictionary;
import org.openidams.dataset.exceptions.DataSetInstantationException;
import org.openidams.dataset.exceptions.DataSetNotFoundException;
import org.openidams.dataset.persistence.DataSetDefinition;
import org.openidams.datatable.Catalog;
import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableInstantiationException;
import org.openidams.datatable.manager.PlugInSourceHandlerException;
import org.openidams.utilities.Properties;

import com.thoughtworks.xstream.XStream;

/**
 * Implementation of an interface to a repository of datasets as a singleton
 * class
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Repository {

    /**
     * Hashtable of datasets indexed by strings
     */
    private Hashtable<String, DataSet> datasets;

    /**
     * Singleton implantation
     */
    private static final Repository instance = new Repository();

    /**
     * Returns the only class' instance
     * 
     * @return Only instance of the class (the singleton)
     */
    public static Repository getInstance() {
        return instance;
    }

    /**
     * Private constructor of the only instace of this class
     */
    private Repository() {
        datasets = new Hashtable<String, DataSet>();
    }

    /**
     * Returns the dataset that correspons to the given name
     * 
     * @param name
     *            Name of the desired dataset
     * @return Dataset corresponding to the given name
     */
    public DataSet getDataSet(String name) throws DataSetNotFoundException{

    	if (name !=null && datasets.containsKey(name)) {        
        	return datasets.get(name);
        }
        else{
        	DataSet ds=null;
        	try {
				ds=loadDataSet(name);
				datasets.put(name,ds);
	        	return ds;
			} catch (DataSetInstantationException e) {			
				throw new DataSetNotFoundException("The requested DataSet:"+name+" wasn't found or couldn't be instantiated.",e);
			}

        }
    }

    
    
        
    
    /**
     * Adds a repositor under the given name. If such repository exists for
     * the given name, the dataset is updated
     * @param name Name of the dataset
     * @param ds Dataset to be added
     */
    public void addDataSet(String name, DataSet ds) {
        datasets.put(name, ds);
    }
    
    
    /**
     * Adds a dataset locating it by its logical name
     */
    public DataSet loadAndAddDataSet(String name) throws DataSetInstantationException{
        DataSet ds=loadDataSet(name);
    	datasets.put(name, ds);
    	return ds;
    }
    
    /**
     * 
     * @param name
     * @return
     * @throws DataSetInstantationException
     */
	private DataSet loadDataSet(String name) throws DataSetInstantationException{
		
		String dataSetPath=Properties.getInstance().getProperty(Properties.DATASETSPATH);				
		File dsDir=new File(dataSetPath.replaceAll("%20"," "));

		File dataSetFiles[]=dsDir.listFiles();
		DataSet ds=null;		
		for (int i=0;i<dataSetFiles.length && ds==null;i++){			
			//TODO for best performance improve the dataset file name checking
			if (dataSetFiles[i].isFile() && dataSetFiles[i].getName().equals(name+".xml") && dataSetFiles[i].getName().indexOf(name)==0){
				ds=loadDataSetFile(dataSetFiles[i]);
				if (ds.getDataSetName().equals(name)){					
					return ds;
				}
				else{
					ds=null;
				}
			}
		}
		if (ds!=null){
			return ds;
		}
		else{			
			throw new DataSetInstantationException("DataSet "+name+" wasn't found. (Missing "+name+".xml file).");			
		}
	}
	
	
	/**
	 * 
	 * @param f
	 * @return
	 */
	private DataSet loadDataSetFile(File f) throws DataSetInstantationException{
		XStream xs=new XStream();
		setXMLAliases(xs);
		DataSetDefinition dsd=null;
		DataSet ds=null;
		Dictionary dict=null;
		try {
			dsd=(DataSetDefinition)xs.fromXML(new FileInputStream(f));
			//create the datatable
            Catalog.getInstance().init();
            DataTable dt = Catalog.getInstance().buildDataTable(dsd.getDataSourceName(), dsd.getDataSourceMetadata());
            
            //create the dictionary
            //if no absolute path provided, default location will be used
            String dictFileName=dsd.getDictionaryName();
            
            if (new File(dictFileName).exists()){
            	dict=loadDictionary(new File(dictFileName));
            }
            else if(new File(Properties.getInstance().getProperty(Properties.DICTIONARIESPATH)+"/"+dictFileName).exists()){
            	dict=loadDictionary(new File(Properties.getInstance().getProperty(Properties.DICTIONARIESPATH)+"/"+dictFileName));
            }
            else{
            	throw new DataSetInstantationException("Dictionary "+dsd.getDictionaryName()+" wasn't found.");	
            }

            ds=new DataSet(dt,dict,dsd.getVariableToColumnMapping());
            
            ds.setDataSetName(dsd.getDataSetName());
	
		} catch (FileNotFoundException e) {			
			throw new DataSetInstantationException("File "+dsd.getDictionaryName()+" wasn't found.",e);
		} catch (ClassCastException e){
			throw new DataSetInstantationException("The XML file:"+f.getAbsolutePath()+" isn't a valid DataSetDefinition file",e);
		} catch (PlugInSourceHandlerException e){
			throw new DataSetInstantationException("Problem creating the dataset:"+dsd.getDataSetName()+". ",e);
		} catch (DataTableInstantiationException e){
			throw new DataSetInstantationException("Problem creating the dataset:"+dsd.getDataSetName()+". ",e);
		}

		return ds;
		
	}
	
	/**
	 * 
	 * @param f
	 * @return
	 */
	private Dictionary loadDictionary(File f){
		XStream xs=new XStream();
		setXMLAliases(xs);
		Dictionary dict=null;
		try {
			dict=(Dictionary)xs.fromXML(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException e){
			e.printStackTrace();
		}
		return dict;		
	}
	
	/**
	 * 
	 * @param xs
	 */
	private void setXMLAliases(XStream xs){
		xs.alias("DataSetDefinition",org.openidams.dataset.persistence.DataSetDefinition.class);
		xs.alias("DictionaryVariable",org.openidams.dataset.dictionary.DictionaryVariable.class);
		xs.alias("Dictionary",org.openidams.dataset.dictionary.Dictionary.class);
	}


    
    
}
