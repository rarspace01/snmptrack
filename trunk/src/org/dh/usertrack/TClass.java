package org.dh.usertrack;

import java.io.IOException;
import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TClass {
	
//	try {
//	ResultSet rset=DataManagerOracle.getInstance().select("select BANNER from SYS.V_$VERSION");
//	
//    while (rset.next())
//          System.out.println (rset.getString(1));   // Print col 1
//
//	
//} catch (SQLException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
	
	
	public static void main(String[] args) {
		
		
		TransportMapping transport;
		Snmp snmp = null;
		
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Switch sw1=new Switch("151.10.132.226", snmp);
		Switch sw1=new Switch("151.10.132.240", snmp);
		sw1.refresh();
		
		//System.out.println("DNS:"+DNSHelperClass.getHostname("192.168.1.1"));
		
		
//		try {
//			Thread.sleep(999999);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		for(int i=0; i<255; i++){
//			
//			try {
//
//				Hashtable env = new Hashtable();
//
//				env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
//				env.put("java.naming.provider.url",    "dns://151.10.136.202/. dns://151.10.136.70/. dns://192.168.1.1/.");
//				
//				
//				DirContext ctx = new InitialDirContext(env);
//	 
//				Attributes attrs = ctx.getAttributes(i+".1.168.192.in-addr.arpa",new String[] {"PTR"});
//
//				for (NamingEnumeration ae = attrs.getAll();ae.hasMoreElements();) {
//
//					Attribute attr = (Attribute)ae.next();
//
//					String attrId = attr.getID();
//
//					for (Enumeration vals = attr.getAll();vals.hasMoreElements(); System.out.println(attrId + ": " + vals.nextElement()));
//
//				}
//
//				ctx.close();
//
//		 	}	
//
//			catch(Exception e) {
//
//				System.out.println("NO REVERSE DNS");
//
//			}
//			
//		}

		
	}
	
}