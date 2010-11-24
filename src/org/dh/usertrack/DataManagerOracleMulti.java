package org.dh.usertrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;


/**Die Klasse DataManager dient zum Verwalten der Verbindungen mit der Datenbank*/
public class DataManagerOracleMulti {
        private static DataManagerOracleMulti uniqueInstance = null;
        
        private static long lLastAccessed=0;
        
        private java.sql.Connection conn;
        private Statement stmt;
        private PreparedStatement pstmt;
        
        private DataManagerOracleMulti() {
                try {
                        Class.forName("oracle.jdbc.driver.OracleDriver"); 
                        conn = DriverManager.getConnection("jdbc:oracle:thin:@octopus:1521:USRTRACK","USRTRACK","TrackIt");
                        conn.setAutoCommit(false);
                        //stmt = conn.createStatement();
                        
                        
                } catch (SQLException e) {
                        
                        e.printStackTrace();
                        HelperClass.err(e);
                } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                uniqueInstance = this;
        }

        public static DataManagerOracleMulti getInstance()
        {
                        if(uniqueInstance== null||Calendar.getInstance().getTimeInMillis()>(lLastAccessed+(999*1000)))
                        {
                    	uniqueInstance=null;
                        new DataManagerOracleMulti();
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

        //Methode für alles auÃŸer select Operationen
        public int execute(String SQLString) throws SQLException
        {
        	
        		//getlist
        	
        		//pstmt = conn.prepareStatement();
        	//http://download.oracle.com/javase/tutorial/jdbc/basics/transactions.html
        	
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
        
    public Connection getConnection() {
        return conn;
    }

        
}
