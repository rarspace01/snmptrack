package org.dh.usertrack;

import java.sql.SQLException;

public class Port {
	public String sMAC;
	public String SwitchIP;
	public int PortID;
	public int VPortID;
	public String name;
	public String alias;
	public String vlan;
	public long LastUpdate;
	public boolean astatus;
	public boolean cstatus;
	public String Speed;
	public String Duplex;
	public boolean isUplink;
	public String UplinkIP;
	public boolean hasCDPN;

	
	public String printAll() {
		// TODO Auto-generated method stub
		return "["+sMAC+"] ["+
		SwitchIP+"] ["+
		PortID+"] ["+
		VPortID+"] ["+
		name+"] ["+
		alias+"] ["+
		vlan+"] ["+
		LastUpdate+"] ["+
		astatus+"] ["+
		cstatus+"] ["+
		Speed+"] ["+
		Duplex+"] ["+
		isUplink+"] ["+
		UplinkIP+"] ";
	}
	
	public String getDBString(){
		String svlan=vlan;
		
		if(svlan.length()<1){
			svlan="-1";
		}
		
		
		
		String sSQL="";
		
		int iTimestamp=(int)(System.currentTimeMillis()/1000);
		
		sSQL="MERGE INTO USRTRACK.\"st_ports\" dest "+
		"USING dual ON (dual.dummy is not null and dest.MAC='"+sMAC+"' and dest.\"vlan\"="+svlan+") "+
		"WHEN MATCHED "+
		" THEN UPDATE SET "+
		"\"SwitchIP\"='"+SwitchIP+"',"+
		"\"name\"='"+name+"',"+
		"\"alias\"='"+alias+"',"+
		"\"stamptime\"='"+iTimestamp+"',"+
		"\"cstatus\"='"+cstatus+"',"+
		"\"Speed\"='"+Speed+"',"+
		"\"Duplex\"='"+Duplex+"',"+
		"\"isUplink\"='"+isUplink+"',"+
		"\"UplinkIP\"='"+UplinkIP+"',"+
		"\"PortID\"='"+PortID+"',"+
		"\"VPortID\"='"+VPortID+"' "+
		"WHEN NOT MATCHED "+
		 "THEN INSERT (\"MAC\",\"vlan\",\"SwitchIP\",\"name\",\"alias\",\"stamptime\",\"cstatus\",\"Speed\",\"Duplex\",\"isUplink\",\"UplinkIP\",\"PortID\",\"VPortID\") VALUES ('"+sMAC+"',"+svlan+",'"+SwitchIP+"','"+name+"','"+alias+"','"+iTimestamp+"','"+cstatus+"','"+Speed+"','"+Duplex+"','"+isUplink+"','"+UplinkIP+"','"+PortID+"',"+VPortID+")";

		return sSQL;
	}
	
}
