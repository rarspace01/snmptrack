package org.dh.usertrack;

import java.util.ArrayList;

public class Nagios {

	public static final ArrayList<String> getHostsOfGroup(String sGroup){
		
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
	
}
