package org.dh.usertrack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SNMPTrackHelper {

	public static ArrayList<Switch> switchListe;
	
	public static final boolean isinList(String sIP){
		boolean isinListe=false;
		
		if(sIP.length()>0){
		
		for (int i=0; i<switchListe.size(); i++){
			
			if(switchListe.get(i).getsIP().contains(sIP)){
				isinListe=true;
			}
			
		}
		
		}
		
		return isinListe;
	}
	
	public static final void updateALevels() {

		String sSQL="";
		
		ArrayList<String> sSQLList=new ArrayList<String>();
		
		ArrayList<String> swAccess=new ArrayList<String>();
		ArrayList<String> swDistribution=new ArrayList<String>();
		ArrayList<String> swCore=new ArrayList<String>();
		ArrayList<String> swServer=new ArrayList<String>();
		ArrayList<String> swVienna=new ArrayList<String>();
		ArrayList<String> swMerzig=new ArrayList<String>();
		
		swAccess=Nagios.getHostsOfGroup("Access");		
		swDistribution=Nagios.getHostsOfGroup("Distribution");		
		swCore=Nagios.getHostsOfGroup("Core");
		swServer=Nagios.getHostsOfGroup("Server_Switches");
		swVienna=Nagios.getHostsOfGroup("SwitchesVie");
		swMerzig=Nagios.getHostsOfGroup("SwitchesDCS");
		
		for (int i = 0; i < swAccess.size(); i++) {
			
			sSQL="UPDATE USRTRACK.\"st_switchs\" SET \"alevel\"=2 WHERE IP LIKE '"+swAccess.get(i)+"'";
			sSQLList.add(sSQL);
			
		}
		
		for (int i = 0; i < swServer.size(); i++) {
			
			sSQL="UPDATE USRTRACK.\"st_switchs\" SET \"alevel\"=2 WHERE IP LIKE '"+swServer.get(i)+"'";
			sSQLList.add(sSQL);
			
		}
		
		for (int i = 0; i < swDistribution.size(); i++) {
			
			sSQL="UPDATE USRTRACK.\"st_switchs\" SET \"alevel\"=1 WHERE IP LIKE '"+swDistribution.get(i)+"'";
			sSQLList.add(sSQL);
			
		}	
		
		for (int i = 0; i < swCore.size(); i++) {
			
			sSQL="UPDATE USRTRACK.\"st_switchs\" SET \"alevel\"=0 WHERE IP LIKE '"+swCore.get(i)+"'";
			sSQLList.add(sSQL);
			
		}	
		
		for (int i = 0; i < swVienna.size(); i++) {
			
			sSQL="UPDATE USRTRACK.\"st_switchs\" SET \"alevel\"=2 WHERE IP LIKE '"+swVienna.get(i)+"'";
			sSQLList.add(sSQL);
			
		}	
		
		for (int i = 0; i < swMerzig.size(); i++) {
			
			sSQL="UPDATE USRTRACK.\"st_switchs\" SET \"alevel\"=2 WHERE IP LIKE '"+swMerzig.get(i)+"'";
			sSQLList.add(sSQL);
			
		}	
		
		DataManagerOracleMulti.execute("SNMP TRACKER HELPER",sSQLList);
		
	}
	
	public static final void cleanupDuplicates() {
		ArrayList<String> swListeComplete=new ArrayList<String>();
		
		ArrayList<String> sSQLlist=new ArrayList<String>();
		
		String msgPuffer="";
		
		String sPuffer="";
		String sSQL="", eSQL="";
		String salevel="";
		
		String sMAC="";
		String sPortMAC="";
		
		String sHMAC="";
		String sHMACPort="";
		String sHMACLevel="";
		String sLMAC="";
		String sLMACPort="";
		String sLMACLevel="";
		
		int iDcount=0;
		
		//list of duplicates
		
		eSQL="SELECT h.MAC, p.MAC AS PMAC, s.\"alias\" AS SName, s.\"alevel\" AS A FROM \"st_hosts\" h, \"st_ports\" p, \"st_switchs\" s WHERE h.\"PortMAC\"=p.MAC AND p.\"SwitchIP\"=s.\"IP\" AND h.MAC IN (SELECT MAC FROM \"USRTRACK\".\"st_hosts\" GROUP BY MAC HAVING COUNT(*)>1)";
		
		System.out.println("["+eSQL+"]");
		
		try {
			ResultSet rset= DataManagerOracle.getInstance().select(eSQL);
		
			
			//hole Datensätze, lege in Liste
			
			while(rset.next()){
				
				sMAC=rset.getString("MAC");
				sPortMAC=rset.getString("PMAC");
				salevel=rset.getString("A");
				
				try{
					
					if(salevel.contains("null")||salevel==null){
					salevel="0";	
					}
					
					}catch(NullPointerException e){
						salevel="0";	
					}
					
				msgPuffer=sMAC+"!"+sPortMAC+"!"+salevel;
				//System.out.println("ADD: ["+msgPuffer+"]");
				swListeComplete.add(msgPuffer);

				
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<swListeComplete.size();i++){

			sHMAC=swListeComplete.get(i).substring(0, swListeComplete.get(i).indexOf("!"));
			sHMACPort=swListeComplete.get(i).substring(swListeComplete.get(i).indexOf("!")+1, swListeComplete.get(i).lastIndexOf("!"));
			sHMACLevel=swListeComplete.get(i).substring(swListeComplete.get(i).lastIndexOf("!")+1);
			
			for(int j=0;j<swListeComplete.size();j++){
				
				sLMAC=swListeComplete.get(j).substring(0, swListeComplete.get(j).indexOf("!"));
				sLMACPort=swListeComplete.get(j).substring(swListeComplete.get(j).indexOf("!")+1, swListeComplete.get(i).lastIndexOf("!"));
				sLMACLevel=swListeComplete.get(j).substring(swListeComplete.get(j).lastIndexOf("!")+1);

				if(sHMAC.contains(sLMAC)&&!sHMACPort.contains(sLMACPort)){
					
						if(Integer.parseInt(sHMACLevel)>Integer.parseInt(sLMACLevel))
						{
		
						//
						sSQL="DELETE FROM \"st_hosts\" WHERE \"MAC\"='"+sLMAC+"' AND \"PortMAC\"='"+sLMACPort+"'";
						
						iDcount++;

						//System.out.println("["+sSQL+"]");
						sSQLlist.add(sSQL);

						}
				
					
					
				}
				
			}
			
		}
		
		DataManagerOracleMulti.execute("SNPM TRacker helper",sSQLlist);
		
		if (SNMPConfig.getDebuglevel()>=2) {
			HelperClass.msgLog("DELETED "+iDcount+" Duplicates");
		}
		
	}
	
}
