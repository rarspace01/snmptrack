package org.dh.usertrack.snmptest;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

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
		
		for(int i=0; i<255; i++){
			
			try {

				Hashtable env = new Hashtable();

				env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
				env.put("java.naming.provider.url",    "dns://151.10.136.202/. dns://151.10.136.70/.");
				
				
				DirContext ctx = new InitialDirContext(env);
	 
				Attributes attrs = ctx.getAttributes(i+".132.10.151.in-addr.arpa",new String[] {"PTR"});

				for (NamingEnumeration ae = attrs.getAll();ae.hasMoreElements();) {

					Attribute attr = (Attribute)ae.next();

					String attrId = attr.getID();

					for (Enumeration vals = attr.getAll();vals.hasMoreElements(); System.out.println(attrId + ": " + vals.nextElement()));

				}

				ctx.close();

		 	}	

			catch(Exception e) {

				System.out.println("NO REVERSE DNS");

			}
			
		}

		
	}
	
}