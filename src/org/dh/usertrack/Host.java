package org.dh.usertrack;

public class Host {

	public String MAC="";
	public String PortMAC="";
	public String IP="";
	public long lIP=0;
	public String hostname="";
	public String lastuser="";
	public String Speed="";
	public String Duplex="";
	public String CDPDeviceID="";
	public String CDPDeviceIP="";
	public String CDPDevicePort="";
	public String CDPDeviceTyp="";
	public String sVHOST="0";
	
	public String getDBString() {
		

		
		String sSQL="";
		
		int iTimestamp=(int)(System.currentTimeMillis()/1000);
		
		sSQL="MERGE INTO USRTRACK.\"st_hosts\" dest "+
		"USING dual ON (dual.dummy is not null and dest.MAC='"+MAC+"' and dest.\"PortMAC\"='"+PortMAC+"') "+
		"WHEN MATCHED "+
		" THEN UPDATE SET "+
		"\"IP\"='"+IP+"',"+
		"\"LIP\"='"+lIP+"',"+
		"\"hostname\"='"+hostname+"',"+
		"\"stamptime\"='"+iTimestamp+"',"+
		"\"lastuser\"='"+lastuser+"',"+
		"\"Speed\"='"+Speed+"',"+
		"\"Duplex\"='"+Duplex+"', "+
		"\"CDPID\"='"+CDPDeviceID+"', "+
		"\"CDPIP\"='"+CDPDeviceIP+"', "+
		"\"CDPPORT\"='"+CDPDevicePort+"', "+
		"\"CDPTYP\"='"+CDPDeviceTyp+"', "+
		"\"VHOST\"='"+sVHOST+"' "+
		"WHEN NOT MATCHED "+
		 "THEN INSERT (\"MAC\",\"PortMAC\",\"IP\",\"LIP\",\"hostname\",\"stamptime\",\"lastuser\",\"Speed\",\"Duplex\",\"CDPID\",\"CDPIP\",\"CDPPORT\",\"CDPTYP\",\"VHOST\") VALUES ('"+MAC+"','"+PortMAC+"','"+IP+"','"+lIP+"','"+hostname+"','"+iTimestamp+"','"+lastuser+"','"+Speed+"','"+Duplex+"','"+CDPDeviceID+"','"+CDPDeviceIP+"','"+CDPDevicePort+"','"+CDPDeviceTyp+"','"+sVHOST+"')";

		return sSQL;
	}
	
}
