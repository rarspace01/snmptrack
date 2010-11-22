package org.dh.usertrack.snmptest;

public class Cisco {

	public static final String getModelfromDescr(String sOID){
		
		String sModell="";
		String sIOSTM="IOS (tm)";
		String sPatternS="Software (";
		String sPatternE="), Version";
		String sPattern1="1.3.6.1.2.1.1.1.0 = Cisco IOS Software,";
		String sPattern2="1.3.6.1.2.1.1.1.0 = Cisco Internetwork Operating System Software";
		String sPattern3="1.3.6.1.2.1.1.1.0 = Cisco Systems ";
		String sPattern4="Cisco Catalyst Operating System Software";
		
		if(sOID.contains(sPattern1))
		{
			sModell=sOID.substring(sOID.indexOf(sPatternS)+sPatternS.length(),sOID.indexOf(sPatternE));
		}else if(sOID.contains(sPattern2)){
			sModell=sOID.substring(sOID.indexOf(sPatternS)+sPatternS.length(),sOID.indexOf(sPatternE));
			sModell=sModell.replace(sIOSTM, "");
		}else if(sOID.contains(sPattern3))
		{	
			sModell=sOID.substring(sOID.indexOf(sPattern3)+sPattern3.length(), sOID.indexOf(sPattern4));
		}else{
			//System.out.println(sOID);
		}
		sModell.trim();

		sModell=sModell.replace("\r", "");
		sModell=sModell.replace("\n", "");

		if(sModell.contains("C6MSFC2-JSV-M"))
		{
			sModell="C6500-MSFC2-JSV-M";
		}
		
		return sModell;
	}
	
	public static final String getIOSfromDescr(String sOID){
		String sIOS="";
		
		String sPattern1="Version";
		
		if(sOID.contains("Version"))
		{
			if(sOID.substring(sOID.indexOf(sPattern1)+sPattern1.length()).contains(","))
			{
				sIOS=sOID.substring(sOID.indexOf(sPattern1)+sPattern1.length(),sOID.indexOf(sPattern1)+sPattern1.length()+sOID.substring(sOID.indexOf(sPattern1)+sPattern1.length()).indexOf(","));
			}else{
				sIOS=sOID.substring(sOID.indexOf(sPattern1)+sPattern1.length(),sOID.indexOf(sPattern1)+sPattern1.length()+sOID.substring(sOID.indexOf(sPattern1)+sPattern1.length()).indexOf("\n"));
			}
		sIOS=sIOS.trim();
		}
		return sIOS;
	}

	public static String getLocation(String sOID) {
		
		String sPattern1="1.3.6.1.2.1.1.6.0 =";
		
		sOID=sOID.replace(sPattern1, "");
		
		sOID=sOID.trim();
		
		return sOID;
	}

	public static String getUptime(String oid) {
		oid=oid.replace("1.3.6.1.2.1.1.3.0 = ", "");
		
		if(oid.contains("day"))
		{
			oid=oid.substring(0, oid.indexOf("day")).trim()+":"+oid.substring(oid.indexOf(", ")+", ".length());
		}else{
			oid="0:"+oid;
		}
			
		return oid;
	}

	public static String getAlias(String oid) {
		oid=oid.replace("1.3.6.1.2.1.1.5.0 =","");
		oid=oid.trim();
		return oid;
	}
	
}
