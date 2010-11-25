package org.dh.usertrack;

public class DNSHelperClass {

	public static final String getHostname(String IP){
		String sHostname="";
		sHostname=DNSReserve.lookupDNS("151.10.136.202",IP);
		if(sHostname.contains("NODNS"))
		{
			sHostname=DNSReserve.lookupDNS("151.10.136.70",IP);	
		}
		
		if(sHostname.contains("NODNS")){
			sHostname="";
		}
		
		return sHostname;
	}
	
}
