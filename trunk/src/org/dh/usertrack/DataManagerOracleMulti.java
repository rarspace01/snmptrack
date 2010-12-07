package org.dh.usertrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


/**Die Klasse DataManager dient zum Verwalten der Verbindungen mit der Datenbank*/
public class DataManagerOracleMulti {

		public static final void execute(String sName, ArrayList<String> sSQLList){
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@octopus:1521:USRTRACK","USRTRACK","TrackIt");
	            conn.setAutoCommit(false);
	        	
	        	ArrayList<PreparedStatement> psL=new ArrayList<PreparedStatement>();
	        	
	        	int iTotalCount=0,iCount=0,iRemainCount=0, ipRemainCount=0;
	        	
	        	iTotalCount=sSQLList.size();
	        	iRemainCount=iTotalCount;
	        	
	        	while(iRemainCount>0)
	        	{
	        	
	        	if(iRemainCount>100){
	        		
	        		for(int i=0;i<100;i++)
	        		{
	        			psL.add(conn.prepareStatement(sSQLList.get(i+iCount)));
	        		}
	        		
	        		for(int i=0;i<100;i++){
	        			psL.get(i+iCount).executeUpdate();
	        		}
	        		
	        		conn.commit();
	        		
	        		for(int i=0;i<100;i++){
	        			psL.get(i+iCount).close();
	        		}

	        		for(int i=0;i<100;i++)
	        		{
	        			iRemainCount--;
	        			iCount++;
	        		}
	        		
	        	}else{
	        		
	        		ipRemainCount=iRemainCount;
	        		
	        		for(int i=0;i<ipRemainCount;i++)
	        		{
	        			psL.add(conn.prepareStatement(sSQLList.get(i+iCount)));
	        		}
	        		
	        		for(int i=0;i<ipRemainCount;i++){
	        			psL.get(i+iCount).executeUpdate();
	        		}
	        		
	        		conn.commit();
	        		
	        		for(int i=0;i<ipRemainCount;i++){
	        			psL.get(i+iCount).close();
	        		}

	        		for(int i=0;i<ipRemainCount;i++)
	        		{
	        			iRemainCount--;
	        			iCount++;
	        		}
	        		
	        	}

	        	}
	        		
                
                conn.setAutoCommit(true);
	        	
                conn.close();
                
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e) {
				System.out.println("Error in : "+sName);
				// TODO Auto-generated catch block
				
				for(int i=0;i<sSQLList.size();i++)
				{
					System.out.println("["+sName+"]["+sSQLList.get(i)+"]");
				}
				
				e.printStackTrace();
			} 
			
		}
        
}
