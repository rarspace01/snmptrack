package org.dh.usertrack;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import org.snmp4j.Snmp;
import org.snmp4j.smi.IpAddress;

public class Switch {

	private String sIP="";
	private Snmp snmp;
	private String svendor="";
	private String smodel="";
	private String sversion="";
	private String sLocation=""; 
	private String iUptime="";
	private String sAlias="";
	private String sDNS="";
	private int iTimestamp=0;
	
	
	public Switch(String sAdr){
		try {
			InetAddress iaSwitch;
			iaSwitch=InetAddress.getByName(sAdr);
			sIP=iaSwitch.getHostAddress();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Switch(String sAdr, Snmp snmp) {
		// TODO Auto-generated constructor stub
		
		this.sIP=sAdr;
		
		this.snmp=snmp;
		
	}
	
	public String getsIP() {
		return sIP;
	}

	public void refresh(){
	
	String sSQL="";	
	String sPuffer="";
	
	boolean supportsCiscoDuplex=false;
	
	//Get switch info	
		
		
	//Parse SystemDesc && Test if SNMP is working
	String 	sOID="";
		
	sOID=SNMPHandler.getOID(snmp, "1.3.6.1.2.1.1.1.0", sIP, SNMPConfig.getReadCommunity());	
		
	if(sOID.contains("ERROR AT ")){
		//HelperClass.msgLog("SNMP not runnging on: ["+sIP+"]. Aborting Tracking.");
	}else{
	
	//get DNS
 	sDNS=DNSHelperClass.getHostname(sIP);
		
	//is Cisco? -> Prï¿½fe IOS/Modell
	if(sOID.toLowerCase().contains("cisco")){
		svendor="Cisco";
	
		smodel=Cisco.getModelfromDescr(sOID);
		
		sversion=Cisco.getIOSfromDescr(sOID);
		
		sLocation=Cisco.getLocation(SNMPHandler.getOID(snmp, "1.3.6.1.2.1.1.6.0", sIP, SNMPConfig.getReadCommunity()));

		iUptime=Cisco.getUptime(SNMPHandler.getOID(snmp, "1.3.6.1.2.1.1.3.0", sIP, SNMPConfig.getReadCommunity()));
		//HelperClass.msgLog("SOID:["+sOID+"]");
		
		sAlias=Cisco.getAlias(SNMPHandler.getOID(snmp, "1.3.6.1.2.1.1.5.0", sIP, SNMPConfig.getReadCommunity()));
		
	}else{
		
		svendor="Unknown"+"["+sOID+"]";
	}
	
	//HelperClass.msgLog("["+sIP+"]DNS:["+sDNS+"] Vendor: ["+svendor+"] Model: ["+smodel+"] IOS: ["+sversion+"] LOC: ["+sLocation+"] Uptime: ["+iUptime+"] Alias: ["+sAlias+"]");

	iTimestamp=(int)(System.currentTimeMillis()/1000);
	
	sSQL="MERGE INTO USRTRACK.\"st_switchs\" dest "+
	"USING dual ON (dual.dummy is not null and dest.IP='"+sIP+"') "+
	"WHEN MATCHED "+
	" THEN UPDATE SET "+
	"\"hostname\"='"+sDNS+"',"+
	"\"vendor\"='"+svendor+"',"+
	"\"model\"='"+smodel+"',"+
	"\"osversion\"='"+sversion+"',"+
	"\"location\"='"+sLocation+"',"+
	"\"uptime\"='"+iUptime+"',"+
	"\"alias\"='"+sAlias+"',"+
	"\"stamptime\"='"+iTimestamp+"' "+
	"WHEN NOT MATCHED "+
	 "THEN INSERT (\"IP\",\"hostname\",\"vendor\",\"model\",\"osversion\",\"location\",\"alias\",\"uptime\",\"stamptime\") VALUES ('"+sIP+"','"+sDNS+"','"+svendor+"','"+smodel+"','"+sversion+"','"+sLocation+"','"+sAlias+"','"+iUptime+"',"+iTimestamp+")";

	try {
		
		DataManagerOracle.getInstance().execute(sSQL);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	ArrayList<String> swPuffer=new ArrayList<String>();
	
	ArrayList<String> swMACs=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swStatus=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swVLANs=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swPortname=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swPortalias=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swVPort=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swHostMAC=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swCDP=new ArrayList<String>();
	ArrayList<String> swVLANListe=new ArrayList<String>();
	ArrayList<String> swSpeed=new ArrayList<String>();
	//only for selected cisco switchs
	ArrayList<String> swCiscoPort=new ArrayList<String>();
	ArrayList<String> swCiscoDuplex=new ArrayList<String>();	
	ArrayList<String> swDuplex=new ArrayList<String>();
	
	swMACs=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.2.1.2.2.1.6", sIP, SNMPConfig.getReadCommunity());
	swStatus=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.2.1.2.2.1.8", sIP, SNMPConfig.getReadCommunity());
	swVLANs=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.4.1.9.9.68.1.2.2.1.2", sIP, SNMPConfig.getReadCommunity());
	swPortname=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.2.1.31.1.1.1.1", sIP, SNMPConfig.getReadCommunity());
	swPortalias=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.2.1.31.1.1.1.18", sIP, SNMPConfig.getReadCommunity());
	swVPort=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.2.1.17.1.4.1.2", sIP, SNMPConfig.getReadCommunity());
	swCDP=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.4.1.9.9.23.1.2.1.1.4", sIP, SNMPConfig.getReadCommunity());
	swSpeed=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.2.1.2.2.1.5", sIP, SNMPConfig.getReadCommunity());
	swCiscoPort = SNMPHandler.getOIDWalk(snmp, "1.3.6.1.4.1.9.9.87.1.4.1.1.25", sIP, SNMPConfig.getReadCommunity());
	
	if(swCiscoPort.size()>0){
	swCiscoDuplex = SNMPHandler.getOIDWalk(snmp, "1.3.6.1.4.1.9.9.87.1.4.1.1.32", sIP, SNMPConfig.getReadCommunity());
	supportsCiscoDuplex=true;
	
	for(int i=0;i<swCiscoPort.size()||i<swCiscoDuplex.size();i++)
	{
	
		swDuplex.add(swCiscoPort.get(i).substring(swCiscoPort.get(i).indexOf("!")+1)
				+"!"+swCiscoDuplex.get(i).substring(swCiscoDuplex.get(i).indexOf("!")+1)
				);
		
	}
	
	
	
	}
	
	//get all the macs of the coresponding vlans
	
	swVLANListe=getVLanList(swVLANs);
	
	//add All Macs to the "Global" Maccache-List
	
	for(int i=0;i<swVLANListe.size();i++)
	{
		swPuffer=SNMPHandler.getOIDWalk(snmp, "1.3.6.1.2.1.17.4.3.1.2", sIP, SNMPConfig.getReadCommunity()+"@"+swVLANListe.get(i));
		for(int j=0;j<swPuffer.size();j++){
			swHostMAC.add(swPuffer.get(j));
		}
		swPuffer.clear();
	}
	
	//System.out.println("DEBUG: swMAC Size:"+swHostMAC.size());
	
	if(swMACs.size()==0){
		System.out.println("ERROR swMAClist");
	}else{
		for(int i=0;i<swMACs.size();i++){
			if(swMACs.get(i).substring(swMACs.get(i).indexOf("!")+1).length()>0)
			{
				
				Port p=new Port();
				
				
				sPuffer=swMACs.get(i).substring(swMACs.get(i).indexOf("!")+1);
				
				p.sMAC=sPuffer;
				p.SwitchIP=sIP;
				p.PortID=Integer.parseInt(swMACs.get(i).substring(0,swMACs.get(i).indexOf("!")).replace("1.3.6.1.2.1.2.2.1.6.", ""));
				p.name=getOIDListEntry(swPortname,i+1).substring(getOIDListEntry(swPortname,i+1).indexOf("!")+1);
				p.alias=getOIDListEntry(swPortalias,i+1).substring(getOIDListEntry(swPortalias,i+1).indexOf("!")+1);
				p.vlan=getOIDListEntry(swVLANs,i+1).substring(getOIDListEntry(swVLANs,i+1).indexOf("!")+1);
				
				//get cstatus of port
				
				sPuffer=getOIDListEntry(swStatus, i+1).substring(getOIDListEntry(swStatus, i+1).indexOf("!")+1);
				
				if(sPuffer.contains("1"))
				{
				p.cstatus=true;
				
				p.Speed=getOIDListEntry(swSpeed, i+1).substring(getOIDListEntry(swSpeed, i+1).indexOf("!")+1);
				
				}else{
				p.cstatus=false;	
				
				p.Speed="0";
				}
			
				//Duplex Erkennung - nur für spezielle Cisco Switchs möglich
				
				if(supportsCiscoDuplex)
				{
					sPuffer=getDuplexSTate(swDuplex,p.PortID).substring(sPuffer.indexOf("!")+1);
					
					if(sPuffer.contains("2")&&p.cstatus)
					{
					p.Duplex="HalfDuplex";
					}else if(sPuffer.contains("1")&&p.cstatus)
					{
					p.Duplex="FullDuplex";
					}else{
						p.Duplex="";
					}
					sPuffer="";
				}
				
				//Prüfe ob Uplink Port + Setzte Uplink IP sofern möglich
				
				if(isUplinkport(swCDP, p.PortID, p.vlan))
				{
					p.isUplink=true;
					p.UplinkIP=getUplinkIP(swCDP, p.PortID, p.vlan);
				}
				//
				
				//System.out.println(p.printAll());
				
			
				
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Beginne mit Port Bearbeitung				
				
				
				
				//System.out.println(swMACs.get(i));
				//has mac
//				System.out.println("Hat ne Mac");
			}
		}
	}
	
	HelperClass.msgLog("FIN: "+sIP);
	
	//in Boerse-project:
	//
	//String sSQL="INSERT INTO kurs (wkn,stamptime,kurswert) VALUES ('"+swkn+"','"+timestamp+"','"+iKurs+"') ON DUPLICATE KEY UPDATE kurswert="+iKurs+";";
	
		
	//String sSQL="INSERT ";	
		
	}
	
	}
	
	private String getUplinkIP(ArrayList<String> swCDP, int portID, String vlan) {
		String sPuffer="";
		
		for(int i=0;i<swCDP.size();i++){

			if(swCDP.get(i).contains("1.3.6.1.4.1.9.9.23.1.2.1.1.4."+portID)&&vlan.length()==0)
			{
			sPuffer=swCDP.get(i).substring(swCDP.get(i).indexOf("!")+1);
			}
		}
		
		sPuffer=HexToDec.getDec(sPuffer);
		
		return sPuffer;
	}

	private boolean isUplinkport(ArrayList<String> swCDP, int portID, String vlan) {
		boolean isUplink=false;
		
		for(int i=0;i<swCDP.size();i++){

			if(swCDP.get(i).contains("1.3.6.1.4.1.9.9.23.1.2.1.1.4."+portID)&&vlan.length()==0)
			{
			isUplink=true;
			}
		}
		
		return isUplink;
	}

	private String getDuplexSTate(ArrayList<String> swDuplex, int portID) {
		String sPuffer="";
		
		for (int i=0;i<swDuplex.size(); i++)
		{
			if(swDuplex.get(i).contains(""+portID)){
				sPuffer=swDuplex.get(i);
			}
		}
		return sPuffer;
	}

	public ArrayList<String> getVLanList(ArrayList<String> Liste){
		String sTMP="";
		ArrayList<String> vll=new ArrayList<String>();
		
		for(int i=0;i<Liste.size();i++)
		{
			//vereinfachen
			sTMP=Liste.get(i).substring(Liste.get(i).indexOf("!")+1);
			//prüfne ob in liste, bei bedarf hinzufügen
			if(!vll.contains(sTMP))
			{
				vll.add(sTMP);
			}
		}
		Collections.sort(vll);
		return vll;
	}
	
	public String getOIDListEntry(ArrayList<String> swListe, int s){
		String sPuffer="";
		for(int i=0;i<swListe.size();i++)
		{
			if(swListe.get(i).contains("."+s+"!"))
			{
				sPuffer=swListe.get(i);
			}
		}
		return sPuffer;
	}
	
}
