package org.dh.usertrack;

import java.sql.SQLException;
import java.util.ArrayList;

public class BenchmarkDBMulti {

	public BenchmarkDBMulti() {
		// TODO Auto-generated constructor stub
	
		ArrayList<String> alSQL=new ArrayList<String>();
		
		int iRowcounter=1000, iMesswerte=10;
		
		long lStart=0, lStop=0;
		
		String sSQL="";
		String MAC="00:00:00:00:00:00";
		String PortMAC=MAC;
		int iTimestamp=0;
		iTimestamp=(int)(System.currentTimeMillis()/1000);
		
		sSQL="MERGE INTO USRTRACK.HOSTS_LIVE dest "+
		"USING dual ON (dual.dummy is not null and dest.MAC='"+MAC+"' and dest.\"PortMAC\"='"+PortMAC+"') "+
		"WHEN MATCHED "+
		" THEN UPDATE SET "+
		"\"stamptime\"='"+iTimestamp+"' "+
		"WHEN NOT MATCHED "+
		 "THEN INSERT (\"MAC\",\"PortMAC\",\"stamptime\") VALUES ('"+MAC+"','"+PortMAC+"','"+iTimestamp+"')";

		for(int j=0;j<iMesswerte;j++){
		
		lStart=System.currentTimeMillis();
		
		for(int i=0;i<iRowcounter;i++){
			alSQL.add(sSQL);
		}

		DataManagerOracleMulti.execute("BENCHDB"+j, alSQL);
		
		lStop=System.currentTimeMillis();
		
		alSQL.clear();
		System.out.println("Took: "+(int)(lStop-lStart));
		
		}
	}
	
	public static void main(String[] args) {
		
		new BenchmarkDBMulti();
		
	}
	
}
