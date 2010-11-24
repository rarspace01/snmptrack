package org.dh.usertrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;


/**Die Klasse DataManager dient zum Verwalten der Verbindungen mit der Datenbank*/
public class DataManagerMySQL {
        private static DataManagerMySQL uniqueInstance = null;
        
        private static long lLastAccessed=0;
        
        private java.sql.Connection conn;
        private Statement stmt;
        
        private DataManagerMySQL() {
                try {
                        Class.forName("com.mysql.jdbc.Driver"); 
                        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/boerse?" +
                                   "user=wwi08b&password=ant0n&useDynamicCharsetInfo=false&autoReconnect=true&holdResultsOpenOverStatementClose=true");
                        stmt = conn.createStatement();
                        
                } catch (SQLException e) {
                        
                        e.printStackTrace();
                        HelperClass.err(e);
                } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                uniqueInstance = this;
        }

        public static DataManagerMySQL getInstance()
        {
                        if(uniqueInstance== null||Calendar.getInstance().getTimeInMillis()>(lLastAccessed+(999*1000)))
                        {
                    	uniqueInstance=null;
                        new DataManagerMySQL();
                        }
                        lLastAccessed=Calendar.getInstance().getTimeInMillis();
                return uniqueInstance;  
        }
        
        public void dispose()
        {
                uniqueInstance=null;
        }
        
        //Methode für normale Select Operationen
        public ResultSet select(String SQLString) throws SQLException
        {
                ResultSet rs=null;
                
                try {
                        
                        rs=stmt.executeQuery(SQLString);

                } catch (SQLException e) {
                	e.printStackTrace();
                	HelperClass.err(e);
                }
                return rs;
        }

        //Methode für alles außer select Operationen
        public int execute(String SQLString) throws SQLException
        {
                int i=-1;
                
                try {
                        
                        i=stmt.executeUpdate(SQLString);

                } catch (SQLException e) {
                	e.printStackTrace();
                	HelperClass.err(e);
                }
                return i;
        }
        
    public Connection getConnection() {
        return conn;
    }

        
}
