package plugins.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;

import org.openidams.datatable.DataTable;
import org.openidams.datatable.DataTableException;
import org.openidams.datatable.DataTableRow;

public class MySQLDataTable extends DataTable {
	
	private String connectionString;
	private String username;
	private String password;
	private String selectClause;

	//attribute to be used by inner classes
	private Connection con;
	
	
	public MySQLDataTable(Hashtable<String, String> metadata) throws DataTableException{
		super(metadata);
        if (this.getMetadata().get("connectionstring") == null) {
            throw new DataTableException("Missing metadata: connectionString.");            
        }
        if (this.getMetadata().get("username") == null) {
            throw new DataTableException("Missing metadata: username.");
        }
        if (this.getMetadata().get("password") == null) {
            throw new DataTableException("Missing metadata: password.");
        }
        if (this.getMetadata().get("selectclause") == null) {
            throw new DataTableException("Missing metadata: selectclause.");
        }
        connectionString=this.getMetadata().get("connectionstring");
        username=this.getMetadata().get("username");
        password=this.getMetadata().get("password");
        selectClause=this.getMetadata().get("selectclause");
        
				
	}
	
	private Connection getConnection() throws DataTableException{
		Connection con=null;
        try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(connectionString,username,password);
        } catch (ClassNotFoundException e) {
			throw new DataTableException("The required MySQL JDBC driver was not found on classpath: com.mysql.jdbc.Driver .",e);
		} catch (SQLException e){
			throw new DataTableException("There was an error opening the database connection:",e);
		}
		return con;
		
	}

	@Override
	public String getSourceDescription() {
		return "MySQL(c) (4.1-compatible) DataTable";
	}

	@Override
	public Iterator<DataTableRow> getRecords() {
		try {
			con=getConnection();
		} catch (DataTableException e1) {
			throw new RuntimeException("DataTable iteration failed -connection failed:",e1);						
		}
		return new Iterator<DataTableRow>(){
			boolean started=false;
			ResultSet rs=null;
			public boolean lastHasNextValue=false;
			
			//verify if next() was invoked after hasNext(). If not, lastHasNextValue will be returned for better 
			//performance and interation consistency.
			public boolean nextPerformed=true;
						
			
			public boolean hasNext() {
				
				if (nextPerformed){
					if (!started){
						try {
							Statement st=con.createStatement();
							rs=st.executeQuery("SELECT "+selectClause);
						} catch (SQLException e) {
							throw new RuntimeException("DataTable data iteration failed:",e);
						}
						started=true;
					}
					try {
						boolean hasMore=rs.next();
						//ensure connection closing when there is no more records
						if (!hasMore){
							con.close();
						}
						
						lastHasNextValue=hasMore;

						//set to false until next() method is called  
						nextPerformed=false;
						
						return hasMore;
						
					} catch (SQLException e) {
						throw new RuntimeException("DataTable data iteration failed:",e);
					}					
				}
				else{
					return lastHasNextValue;
				}
			}
			
			public DataTableRow next() {
				try{
					String rowdata[]=new String[rs.getMetaData().getColumnCount()];
					for (int i=0;i<rowdata.length;i++){
						rowdata[i]=rs.getString(i+1);
					}
					nextPerformed=true;
					return new DataTableRow(rowdata);
				}
				catch (SQLException e){
					throw new RuntimeException("DataTable data iteration failed:",e);
				}
			}

			public void remove() {}
			
		};
		

	}

	@Override
	public void addRecord(DataTableRow dsr) {
		throw new RuntimeException("MySQL DataTable doesn't support record-writing.");

	}

}
