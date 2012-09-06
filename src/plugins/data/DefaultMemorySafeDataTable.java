package plugins.data;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableException;
import org.openidams.datatable.DataTableRow;
import org.openidams.utilities.Properties;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseFileLockedException;

public class DefaultMemorySafeDataTable extends DataTable {

	//private static LinkedList<ObjectContainer> filesPoll=new LinkedList<ObjectContainer>();
	
	private ObjectContainer db=null;
	
	private static Hashtable<String,ObjectContainer> openConnections;
	
	static{
		openConnections=new Hashtable<String,ObjectContainer>();
	}
	
	
	public DefaultMemorySafeDataTable(Hashtable<String, String> metadata) throws DataTableException{
		super(metadata);
        if (this.getMetadata().get("dataFileName") == null) {
            throw new DataTableException("Missing metadata: dataFileName.");
        }
        
        String dataFilePath=Properties.getInstance().getProperty(Properties.DATAFILESPATH);
        
        if (!new File(dataFilePath).exists()){
        	if (!new File(dataFilePath).mkdir()) throw new DataTableException("The directory ["+dataFilePath+"] specified on openidams.properties file wasn't found.");
        }
		String dataFile=Properties.getInstance().getProperty(Properties.DATAFILESPATH)+"/"+this.getMetadata().get("dataFileName");
		File f=new File(dataFile);
		//remove datatable if it already exists
		if (f.exists()){
			//if file is locked, close db40 connection
			if (!f.delete()){				
				ObjectContainer oc=openConnections.get(dataFile);
				if (oc!=null){
					oc.ext().close();				
					f.delete();
				}
			}
		}
		try{
			db=Db4o.openFile(dataFile);
			openConnections.put(dataFile,db);
			
		}
		catch (DatabaseFileLockedException e){
			throw new DataTableException("DataFile locking failed. Probably there is another instance of openIdams already running.",e);
		}
		
	}

	@Override
	public String getSourceDescription() {
		return "OODBMS-Based persistent data table.";
	}

	@Override
	public Iterator<DataTableRow> getRecords() {
		DataTableRow queryProt=new DataTableRow(new String[0]);
		ObjectSet<DataTableRow> ds = db.get(queryProt);
		return ds.iterator();
	}

	@Override
	public void addRecord(DataTableRow dsr) {
		db.set(dsr);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		db.close();
	}
	
	

}
