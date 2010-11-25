package org.dh.usertrack;

import java.util.ArrayList;

public class DNSHelperClass {

	public static final String getHostname(String IP){
		ArrayList<String> DNSServer=new ArrayList<String>();
		String sPuffer="NODNS";
		int i=0;
		
		DNSServer.add("151.10.136.202");
		DNSServer.add("151.10.136.70");
		//long time1=System.currentTimeMillis();
		
		while(sPuffer.contains("NODNS")&&i<DNSServer.size())
		{
			
			sPuffer=DNSReserve.lookupDNS(DNSServer.get(i),IP);
			i++;
		}
		
		long time2=System.currentTimeMillis();
		
//		if((time2-time1)>90)
//		{
//		System.out.println("DNS resovle took "+(time2-time1)+"ms for H["+IP+"]");
//		}

		if(sPuffer.contains("NODNS"))
		{
			sPuffer="";
		}
		
		return sPuffer;
	}
	
}
