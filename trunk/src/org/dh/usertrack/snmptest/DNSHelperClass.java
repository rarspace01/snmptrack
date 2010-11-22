package org.dh.usertrack.snmptest;

public class DNSHelperClass {

	public static final String getHostname(String IP){
		String sHostname="";
		sHostname=DNSReserve.lookupDNS(IP);
		if(sHostname.length()>0)
		{
		sHostname=sHostname.substring(0,sHostname.indexOf("."));
		}
		return sHostname;
	}
	
	public static final String getDomainname(String IP){
		String sDomainname="";
		sDomainname=DNSReserve.lookupDNS(IP);
		if(sDomainname.length()>0)
		{
		sDomainname=sDomainname.substring(sDomainname.indexOf("."));
		}
		return sDomainname;
	}
	
	
}
