package org.openidams.utilities;

import java.io.File;
import java.util.Iterator;
import java.util.Random;

import org.openidams.dataset.DataSetRow;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;


/**
 * This class allows the creation of a transparent intermediate dataset storage
 * when a procedure needs to reprocess all records from a dataset
 * @author ECITeam
 *
 */
public class DataSetRowsPersistenceHandler {

	private ObjectContainer db;
	String filename=null;
	public DataSetRowsPersistenceHandler() {
		super();
		//generates an unique random file name
		filename=Properties.getInstance().getProperty(Properties.TEMP)+"/"+System.currentTimeMillis()+(new Random(System.currentTimeMillis()).nextLong()*100)+".tmp";
		db=Db4o.openFile(filename);	
	}
	
	public Iterator<DataSetRow> getSavedRows() {
		DataSetRow queryProt=new DataSetRow(new String[0]);
		ObjectSet<DataSetRow> ds = db.get(queryProt);
		return ds.iterator();
	}


	public void addRow(DataSetRow dsr) {
		db.set(dsr);
	}
	
	public void close(){
		db.close();
		new File(filename).delete();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {		
		db.close();
		File f=new File(filename);
		if (f.exists()) f.delete();		
	}
	
	

}
