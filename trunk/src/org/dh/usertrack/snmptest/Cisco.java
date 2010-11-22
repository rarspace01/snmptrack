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
			sModell=sModell.trim();
		}else if(sOID.contains(sPattern2)){
			sModell=sOID.substring(sOID.indexOf(sPatternS)+sPatternS.length(),sOID.indexOf(sPatternE));
			sModell=sModell.replace(sIOSTM, "");
			sModell=sModell.trim();
		
		}else if(sOID.contains(sPattern3))
		{	
			sModell=sOID.substring(sOID.indexOf(sPattern3)+sPattern3.length(), sOID.indexOf(sPattern4));
		}else{
			System.out.println(sOID);
		}
		
		if(sModell.contains("C6MSFC2-JSV-M"))
		{
			sModell="C6500-MSFC2-JSV-M";
		}
		
		return sModell;
	}
	
	public static final String getIOSfromDescr(String sOID){
		String sIOS="";
				
		return sIOS;
	}
	
}
