package org.dh.usertrack.snmptest;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class DNSReserve {

	public static final String lookupDNS(String IP){
		
		String sTMP="";
		String sDNS="";
		
		
		try{
		
		InetAddress adr = InetAddress.getByName(IP) ;
	      byte[] addr = adr.getAddress() ;

	      // build reverse IP address
	      String addrString = "";
	      for (int i=addr.length-1 ; i>=0; i--)
	      { addrString += (addr[i] & 0xFF) + "."    ;
	      }	
		
	      //System.out.println("DEBUG: "+IP+"->"+addrString);
	      
		Hashtable env = new Hashtable();

		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		env.put("java.naming.provider.url",    "dns://151.10.136.202/. dns://151.10.136.70/.");
		
		
		DirContext ctx = new InitialDirContext(env);

		Attributes attrs = ctx.getAttributes(addrString+"in-addr.arpa",new String[] {"PTR"});

		for (NamingEnumeration ae = attrs.getAll();ae.hasMoreElements();) {

			Attribute attr = (Attribute)ae.next();

			String attrId = attr.getID();

			for (Enumeration vals = attr.getAll();vals.hasMoreElements(); System.out.println(attrId + ": " + vals.nextElement()));
			
		}

		ctx.close();

 	}	

	catch(Exception e) {

		System.out.println("ERROR: NO REVERSE DNS");

	}
		
		return "";
		
	}
	
}
