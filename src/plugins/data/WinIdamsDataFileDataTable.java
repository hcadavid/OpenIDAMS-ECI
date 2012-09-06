package plugins.data;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableException;
import org.openidams.datatable.DataTableRow;
import org.openidams.utilities.Properties;
import org.openidams.utilities.winidamscompatibility.InvalidWinIDAMSDictionaryException;
import org.openidams.utilities.winidamscompatibility.WinIDAMSDataFileReader;
import org.openidams.utilities.winidamscompatibility.WinIDAMSDictionaryEntry;
import org.openidams.utilities.winidamscompatibility.WinIDAMSDictionaryInterpreter;

public class WinIdamsDataFileDataTable extends DataTable {

	private File datafile;
	private File dictfile;	
	private Iterator<LinkedList<String>> widata;	
	
	public WinIdamsDataFileDataTable(Hashtable<String, String> metadata) throws DataTableException{
		super(metadata);
        if (this.getMetadata().get("datafile") == null) {
            throw new DataTableException("Missing metadata: datafile.");            
        }
        if (this.getMetadata().get("dictfile") == null) {
            throw new DataTableException("Missing metadata: dictfile.");
        }
        datafile=new File(Properties.getInstance().getProperty(Properties.WINIDAMSFILES)+"/"+this.getMetadata().get("datafile"));
        dictfile=new File(Properties.getInstance().getProperty(Properties.WINIDAMSFILES)+"/"+this.getMetadata().get("dictfile"));
        if (!datafile.exists() || !dictfile.exists()){
        	throw new DataTableException("WinIDAMS datafile or dictionary wasn't found:"+datafile.getAbsolutePath()+","+dictfile.getAbsolutePath());
        }
	}

	@Override
	public String getSourceDescription() {
		return "Old WinIDAMS dataFile compatible DataTable";
	}

	@Override
	public Iterator<DataTableRow> getRecords() {
		LinkedList<WinIDAMSDictionaryEntry> dictent;
		try {
			dictent=WinIDAMSDictionaryInterpreter.getDictionaryEntries(dictfile);
		} catch (InvalidWinIDAMSDictionaryException e) {
			throw new RuntimeException("WinIDAMS datafile read failed, dictionary problem:",e);
		}
		WinIDAMSDataFileReader dfr=new WinIDAMSDataFileReader(datafile,dictent);
		widata=dfr.getRows();
		
		return new Iterator<DataTableRow>(){
			
			public boolean hasNext() {
				return widata.hasNext();
			}

			public DataTableRow next() {
				LinkedList<String> data=widata.next();
				DataTableRow datarw=new DataTableRow();
				for (String val:data){
					datarw.addValue(val);	
				}
				return datarw;				
			}

			public void remove() {}
			
		};
		
	}

	@Override
	public void addRecord(DataTableRow dsr) {
		throw new RuntimeException("WinIDAMS dataFile -  DataTable doesn't support record-writing.");

	}

}
