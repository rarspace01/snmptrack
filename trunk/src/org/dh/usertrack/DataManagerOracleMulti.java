package org.dh.usertrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


/**Die Klasse DataManager dient zum Verwalten der Verbindungen mit der Datenbank*/
public class DataManagerOracleMulti {

		public static final void execute(ArrayList<String> sSQLList){
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@octopus:1521:USRTRACK","USRTRACK","TrackIt");
	            conn.setAutoCommit(false);
	        	
	        	ArrayList<PreparedStatement> psL=new ArrayList<PreparedStatement>();
	        	
	        	for(int i=0;i<sSQLList.size();i++)
	        	{
	        		psL.add(conn.prepareStatement(sSQLList.get(i)));
	        	}
	        	
	        	for(int i=0;i<psL.size();i++){
	        		psL.get(i).executeUpdate();
	        	}
				
                conn.commit();

                for(int i=0;i<psL.size();i++){
            		psL.get(i).close();
            	}
                
                conn.setAutoCommit(true);
	        	
                conn.close();
                
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
        
}
