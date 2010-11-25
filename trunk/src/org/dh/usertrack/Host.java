package org.dh.usertrack;

public class Host {

	public String MAC="";
	public String PortMAC="";
	public String IP="";
	public String hostname="";
	public String lastuser="";
	public String Speed="";
	public String Duplex="";
	
	public String getDBString() {
		

		
		String sSQL="";
		
		int iTimestamp=(int)(System.currentTimeMillis()/1000);
		
		sSQL="MERGE INTO USRTRACK.\"st_hosts\" dest "+
		"USING dual ON (dual.dummy is not null and dest.MAC='"+MAC+"' and dest.\"PortMAC\"='"+PortMAC+"') "+
		"WHEN MATCHED "+
		" THEN UPDATE SET "+
		"\"IP\"='"+IP+"',"+
		"\"hostname\"='"+hostname+"',"+
		"\"stamptime\"='"+iTimestamp+"',"+
		"\"lastuser\"='"+lastuser+"',"+
		"\"Speed\"='"+Speed+"',"+
		"\"Duplex\"='"+Duplex+"' "+
		"WHEN NOT MATCHED "+
		 "THEN INSERT (\"MAC\",\"PortMAC\",\"IP\",\"hostname\",\"stamptime\",\"lastuser\",\"Speed\",\"Duplex\") VALUES ('"+MAC+"','"+PortMAC+"','"+IP+"','"+hostname+"','"+iTimestamp+"','"+lastuser+"','"+Speed+"','"+Duplex+"')";

		return sSQL;
	}
	
}
