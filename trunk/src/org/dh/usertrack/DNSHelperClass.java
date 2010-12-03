package org.dh.usertrack;

import java.util.ArrayList;

public class DNSHelperClass {

	public static final String getHostname(String IP){
		ArrayList<String> DNSServer=new ArrayList<String>();
		String sPuffer="NODNS";
		int i=0;
		
		DNSServer.add("151.10.136.70");
		DNSServer.add("151.10.136.202");
//		DNSServer.add("151.10.142.35");
//		DNSServer.add("151.10.17.8");
//		DNSServer.add("151.10.17.14");
		
		
		while(sPuffer.contains("NODNS")&&i<DNSServer.size())
		{
			
			sPuffer=DNSReserve.lookupDNS(DNSServer.get(i),IP);
			i++;
		}
		
		if(sPuffer.contains("NODNS"))
		{
			sPuffer="";
		}
		
		return sPuffer;
	}
	
}
