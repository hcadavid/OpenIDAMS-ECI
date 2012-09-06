package org.openidams.dataset.persistence;

import java.util.Hashtable;
 
import org.openidams.dataset.dictionary.Dictionary;

public class DataSetDefinition {
	
	private String dataSetName=null;
	private String dataSourceName=null;
	private Hashtable<String, String> dataSourceMetadata=null;
	private String dictionaryName=null;
	Hashtable<String,Integer> variableToColumnMapping=null;
	/**
	 * @param metadata
	 * @param name
	 * @param dictionary
	 * @param mapping
	 */
	public DataSetDefinition(String dsname,Hashtable<String, String> dsmetadata, String dataSourceName, String dictionaryName, Hashtable<String, Integer> mapping) {
		// TODO Auto-generated constructor stub
		dataSetName=dsname;
		dataSourceMetadata = dsmetadata;
		this.dataSourceName = dataSourceName;
		this.dictionaryName = dictionaryName;
		variableToColumnMapping = mapping;
	}
	/**
	 * @return Returns the dataSourceMetadata.
	 */
	public Hashtable<String, String> getDataSourceMetadata() {
		return dataSourceMetadata;
	}
	/**
	 * @return Returns the dataSourceName.
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}
	/**
	 * @return Returns the dictionary.
	 */
	public String getDictionaryName() {
		return dictionaryName;
	}
	/**
	 * @return Returns the variableToColumnMapping.
	 */
	public Hashtable<String, Integer> getVariableToColumnMapping() {
		return variableToColumnMapping;
	}
	/**
	 * @return Returns the dataSetName.
	 */
	public String getDataSetName() {
		return dataSetName;
	}
	
	
	
}
