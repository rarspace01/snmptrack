package org.dh.usertrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;


/**Die Klasse DataManager dient zum Verwalten der Verbindungen mit der Datenbank*/
public class DataManagerOracle {
        private static DataManagerOracle uniqueInstance = null;
        
        private static long lLastAccessed=0;
        
        private java.sql.Connection conn;
        private Statement stmt;
        
        private DataManagerOracle() {
                try {
                        Class.forName("oracle.jdbc.driver.OracleDriver"); 
                        conn = DriverManager.getConnection("jdbc:oracle:thin:@"+SNMPTrackConfig.SNMPtrackDB_IP+":1521:"+SNMPTrackConfig.SNMPtrackDB_DB,SNMPTrackConfig.SNMPtrackDB_USR,SNMPTrackConfig.SNMPtrackDB_PWD);
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

        public static DataManagerOracle getInstance()
        {
                        if(uniqueInstance== null||Calendar.getInstance().getTimeInMillis()>(lLastAccessed+(999*1000)))
                        {
                    	uniqueInstance=null;
                        new DataManagerOracle();
                        }
                        lLastAccessed=Calendar.getInstance().getTimeInMillis();
                return uniqueInstance;  
        }
        
        public void dispose()
        {
                uniqueInstance=null;
        }
        
        //Methode f�r normale Select Operationen
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

        //Methode f�r alles außer select Operationen
        public int execute(String SQLString) throws SQLException
        {
                int i=-1;
                
                try {
                        
                        i=stmt.executeUpdate(SQLString);

                } catch (SQLException e) {
                	System.out.println("DEBUGSQL:["+SQLString+"]");
                	e.printStackTrace();
                	HelperClass.err(e);
                }
                return i;
        }
        
        public int executenoexception(String SQLString) throws SQLException
        {
                int i=-1;
                
                try {
                        
                        i=stmt.executeUpdate(SQLString);

                } catch (SQLException e) {
                }
                return i;
        }
        
    public Connection getConnection() {
        return conn;
    }

        
}
