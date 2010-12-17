package org.dh.usertrack;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LDAP {

	public static void main(String[] args) {
		System.out.println("DEBUG: "+getUser("wk262-32de.emea.group.pirelli.com."));
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
		String sPuffer="";
	
		if(DNS.length()>0){
		
		ArrayList<String> alDMCGroups=new ArrayList<String>();
		int i=0;
		
		alDMCGroups.add("Hoechst");
		alDMCGroups.add("Munich");
		alDMCGroups.add("Pneumobil");
		alDMCGroups.add("Merzig Offices");
		alDMCGroups.add("Merzig");
		
		while(sPuffer.length()<1&&i<alDMCGroups.size()){
			
			sPuffer=getUser(alDMCGroups.get(i),DNS);
			if(sPuffer.length()>0)
			{
				i=alDMCGroups.size();
			}
			i++;
		}
			
		}
		
		return sPuffer;
	}
	
	public static String getUser(String sDMGroup,String DNS){
		String sWorkDNS="";
		String sPuffer="";
		
		sWorkDNS=getDomainDNS(DNS);
		
		if(sWorkDNS.length()>0)
		{
		
		DirContext ctx =null;
		
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
		    "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://dmc258de/");
		
		// Authenticate as S. User and password "mysecret"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "emea\\sa.ldapex01de");
		env.put(Context.SECURITY_CREDENTIALS, "TrackIt10");


		try {
			ctx= new InitialDirContext(env);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

			
		Attributes attrs=null;
				try {
					attrs = ctx.getAttributes("CN="+sWorkDNS+",OU=DESKTOPS,OU="+sDMGroup+",OU=DE,OU=APIS XP,DC=emea,DC=group,DC=pirelli,DC=com");
				} catch (NamingException e) {
				} catch (NullPointerException e2){
				}
				
				if(attrs != null){
					sPuffer=""+attrs.get("pirelliPSILikelyRealOwner");
					sPuffer=sPuffer.replace("pirelliPSILikelyRealOwner: ", "");
				}else{
					//CN=WK258-690DE,OU=LAPTOPS,OU=Hoechst,OU=DE,OU=APIS XP,DC=emea,DC=group,DC=pirelli,DC=com
					try {
					attrs = ctx.getAttributes("CN="+sWorkDNS+",OU=LAPTOPS,OU="+sDMGroup+",OU=DE,OU=APIS XP,DC=emea,DC=group,DC=pirelli,DC=com");
					if(attrs.get("pirelliPSILikelyRealOwner") != null){
						sPuffer=""+attrs.get("pirelliPSILikelyRealOwner");
						sPuffer=sPuffer.replace("pirelliPSILikelyRealOwner: ", "");
					}else{
					}
					} catch (NamingException e) {
						// TODO Auto-generated catch block
					} catch (NullPointerException e2){
					}
				}
				
		}
		return sPuffer;
	}
	
}
