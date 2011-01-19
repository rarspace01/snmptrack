package org.dh.usertrack;

public class VLAN {

	public String sID, sStatus, sType, sName, sVTPD;
	
	public String getDBString(){
	
		String sSQL="";
		
		int iTimestamp=(int)(System.currentTimeMillis()/1000);
		
		sSQL="MERGE INTO USRTRACK.\"st_vlans\" dest "+
		"USING dual ON (dual.dummy is not null and dest.VLANID='"+sID+"') "+
		"WHEN MATCHED "+
		" THEN UPDATE SET "+
		"STATUS='"+sStatus+"',"+
		"TYP='"+sType+"',"+
		"VNAME='"+sName+"',"+
		"VTPD='"+sVTPD+"', "+
		"STAMPTIME='"+iTimestamp+"' "+
		"WHEN NOT MATCHED "+
		 "THEN INSERT (VLANID,STATUS,TYP,VNAME,VTPD,STAMPTIME) VALUES ('"+sID+"','"+sStatus+"','"+sType+"','"+sName+"','"+sVTPD+"','"+iTimestamp+"')";

		return sSQL;
	}
	
}
