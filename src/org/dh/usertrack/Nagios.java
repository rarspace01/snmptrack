package org.dh.usertrack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Nagios {

	public static final ArrayList<String> getHostsOfGroupOverHTTP(String sGroup){
		
		ArrayList<String> hostList=new ArrayList<String>();
		
		String sPuffer="";
		String[] saPuffer;
		
		sPuffer=HttpHelperClass.getBasicAuthPage("http://151.10.136.251/nagios/cgi-bin/status.cgi?hostgroup="+sGroup+"&style=overview", "nagiostest", "Snow.fall");
		
		saPuffer=sPuffer.split("\n");

		for(int i=0;i<saPuffer.length;i++){
			
			if(saPuffer[i].contains("title='")){
				sPuffer=saPuffer[i].substring(saPuffer[i].indexOf("title='")+"title='".length());
				sPuffer=sPuffer.substring(0,sPuffer.indexOf("'"));
				
				if(HelperClass.isValidIP(sPuffer)){
					hostList.add(sPuffer);
				}
				
			}
			
		}
		
		return hostList;
	}
	
	public static final ArrayList<String> getHostsOfGroupOverDB(String sGroup){
		
		ArrayList<String> hostList=new ArrayList<String>();
		
		String sSQL="SELECT h.address AS address FROM `tbl_hostgroup` hg, `tbl_lnkHostgroupToHost` hg2h, `tbl_host` h WHERE hg.id=hg2h.idMaster AND hg2h.idSlave=h.id AND hg.hostgroup_name LIKE '"+sGroup+"' ORDER BY h.address;";
		
		try {
			ResultSet rset=DataManagerMySQL.getInstance().select(sSQL);
			
			while(rset.next()){
				
				
				if(rset.getString("address").length()>0&&rset.getString("address").contains(".")){
				hostList.add(rset.getString("address"));
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hostList;
	}
	
	public static final ArrayList<String> getSwitchs(){
		ArrayList<String> hostList=new ArrayList<String>();
		
		String sSQL="SELECT h.address AS address FROM `tbl_hostgroup` hg, `tbl_lnkHostgroupToHost` hg2h, `tbl_host` h WHERE hg.id=hg2h.idMaster AND hg2h.idSlave=h.id";
		
		return null;
	}
	
}
