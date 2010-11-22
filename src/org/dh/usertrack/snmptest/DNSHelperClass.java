package org.dh.usertrack.snmptest;

public class DNSHelperClass {

	public static final String getHostname(String IP){
		String sHostname="";
		sHostname=DNSReserve.lookupDNS(IP);
		if(sHostname.length()>0&&!sHostname.contains("fritz."))
		{
		sHostname=sHostname.substring(0,sHostname.indexOf("."));
		}
		sHostname=sHostname.substring(0, sHostname.lastIndexOf("."));
		return sHostname;
	}
	
	public static final String getDomainname(String IP){
		String sDomainname="";
		sDomainname=DNSReserve.lookupDNS(IP);
		if(sDomainname.length()>0&&!sDomainname.contains("fritz."))
		{
		sDomainname=sDomainname.substring(sDomainname.indexOf("."));
		}
		sDomainname=sDomainname.substring(0, sDomainname.lastIndexOf(","));
		return sDomainname;
	}
	
	
}
