package org.dh.usertrack;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LDAP {

	public static void main(String[] args) {
		System.out.println(getUser("wk258-864de.emea.group.pirelli.com."));
	}
	
	public static String getDomainDNS(String DNS){

		if(DNS.contains(".emea.group.pirelli.com.")){
			DNS=DNS.replace(".emea.group.pirelli.com.", "");
		}else{
			DNS="";
		}
		
		return DNS;
	}
	
	public static String getUser(String DNS){
		String sWorkDNS="";
		String sPuffer="";
		
		//System.out.println("Davor: "+DNS);
		
		sWorkDNS=getDomainDNS(DNS);
		
		//System.out.println("Danach: "+sWorkDNS);
		
		if(sWorkDNS.length()>0)
		{
		
		DirContext ctx =null;
		
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
		    "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://dmc258de/");
		
		// Authenticate as S. User and password "mysecret"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "emea\\hamande001");
		env.put(Context.SECURITY_CREDENTIALS, getpwd());


		try {
			ctx= new InitialDirContext(env);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

			
				try {
					Attributes attrs;
					attrs = ctx.getAttributes("CN="+sWorkDNS+",OU=DESKTOPS,OU=Hoechst,OU=DE,OU=APIS XP,DC=emea,DC=group,DC=pirelli,DC=com");
					
					if(attrs.get("pirelliPSILikelyRealOwner") != null){
						sPuffer=""+attrs.get("pirelliPSILikelyRealOwner");
						sPuffer=sPuffer.replace("pirelliPSILikelyRealOwner: ", "");
					}
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
				
				
		}
		//System.out.println("GOT ["+sPuffer+"]");	
		return sPuffer;
	}

	private static String getpwd() {
		String sPuffer="";
		

		
		return sPuffer;
	}
	
}
