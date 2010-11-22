package org.dh.usertrack.snmptest;

public class DNSHelperClass {

	public static final String getHostname(String IP){
		String sHostname="";
		sHostname=DNSReserve.lookupDNS(IP);
		sHostname=sHostname.substring(0,sHostname.indexOf("."));
		return sHostname;
	}
	
	public static final String getDomainname(String IP){
		String sDomainname="";
		sDomainname=DNSReserve.lookupDNS(IP);
		sDomainname=sDomainname.substring(sDomainname.indexOf("."));
		return sDomainname;
	}
	
	
}
