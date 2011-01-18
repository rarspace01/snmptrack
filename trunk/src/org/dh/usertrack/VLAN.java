package org.dh.usertrack;

public class VLAN {

	public String sID, sStatus, sType, sName;
	
	public String getDBString(){
	
		String sSQL="";
		
		int iTimestamp=(int)(System.currentTimeMillis()/1000);
		
//		sSQL="MERGE INTO USRTRACK.\"st_ports\" dest "+
//		"USING dual ON (dual.dummy is not null and dest.MAC='"+sMAC+"' and dest.\"vlan\"="+svlan+") "+
//		"WHEN MATCHED "+
//		" THEN UPDATE SET "+
//		"\"SwitchIP\"='"+SwitchIP+"',"+
//		"\"name\"='"+name+"',"+
//		"\"alias\"='"+alias+"',"+
//		"\"stamptime\"='"+iTimestamp+"',"+
//		"\"cstatus\"='"+cstatus+"',"+
//		"\"Speed\"='"+Speed+"',"+
//		"\"Duplex\"='"+Duplex+"',"+
//		"\"isUplink\"='"+isUplink+"',"+
//		"\"UplinkIP\"='"+UplinkIP+"',"+
//		"\"PortID\"='"+PortID+"',"+
//		"\"VPortID\"='"+VPortID+"' "+
//		"WHEN NOT MATCHED "+
//		 "THEN INSERT (\"MAC\",\"vlan\",\"SwitchIP\",\"name\",\"alias\",\"stamptime\",\"cstatus\",\"Speed\",\"Duplex\",\"isUplink\",\"UplinkIP\",\"PortID\",\"VPortID\") VALUES ('"+sMAC+"',"+svlan+",'"+SwitchIP+"','"+name+"','"+alias+"','"+iTimestamp+"','"+cstatus+"','"+Speed+"','"+Duplex+"','"+isUplink+"','"+UplinkIP+"','"+PortID+"',"+VPortID+")";

		return sSQL;
	}
	
}
