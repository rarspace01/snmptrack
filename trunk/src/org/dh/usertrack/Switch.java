package org.dh.usertrack;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import org.snmp4j.Snmp;

public class Switch {

	private ArrayList<String> swHostMacIps=new ArrayList<String>();

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
	
	public Switch(String sAdr, Snmp snmp, ArrayList<String> alARP) {
		// TODO Auto-generated constructor stub
		
		this.sIP=sAdr;
		
		this.snmp=snmp;
		
		swHostMacIps=alARP;
	}
	
	public String getsIP() {
		return sIP;
	}

	public void refresh(){
	
	String sSQL="";	
	ArrayList<String> sSQLList=new ArrayList<String>();	
	
	String sPuffer="";
	
	boolean supportsCiscoDuplex=false;
	
	//Get switch info	
		
		
	//Parse SystemDesc && Test if SNMP is working
	String 	sOID="";
		
	sOID=SNMPHandler.getOID(snmp, OID.sysDescr0, sIP, SNMPConfig.getReadCommunity());	
		
	if(sOID.contains("ERROR AT ")){
		HelperClass.msgLog("SNMP not runnging on: ["+sIP+"]. Aborting Tracking.");
	}else{
	
	//get DNS
 	sDNS=DNSHelperClass.getHostname(sIP);
		
	//is Cisco? -> Pr�fe IOS/Modell
	if(sOID.toLowerCase().contains("cisco")){
		svendor="Cisco";
	
		smodel=Cisco.getModelfromDescr(sOID);
		
		sversion=Cisco.getIOSfromDescr(sOID);
		
		sLocation=Cisco.getLocation(SNMPHandler.getOID(snmp, OID.sysLocation0, sIP, SNMPConfig.getReadCommunity()));

		iUptime=Cisco.getUptime(SNMPHandler.getOID(snmp, OID.sysUpTimeInstance, sIP, SNMPConfig.getReadCommunity()));
		//HelperClass.msgLog("SOID:["+sOID+"]");
		
		sAlias=Cisco.getAlias(SNMPHandler.getOID(snmp, OID.sysName0, sIP, SNMPConfig.getReadCommunity()));
		
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
	ArrayList<String> swCDPC=new ArrayList<String>();
	ArrayList<String> swVLANListe=new ArrayList<String>();
	ArrayList<String> swSpeed=new ArrayList<String>();
	ArrayList<String> swType=new ArrayList<String>();
	ArrayList<String> swTypeCisco=new ArrayList<String>();
	ArrayList<String> swSTP=new ArrayList<String>();
	
	//only for selected cisco switchs
	ArrayList<String> swCiscoPort=new ArrayList<String>();
	ArrayList<String> swCiscoDuplex=new ArrayList<String>();	
	ArrayList<String> swDuplex=new ArrayList<String>();
	
	swMACs=SNMPHandler.getOIDWalk(snmp, OID.ifPhysAddress, sIP, SNMPConfig.getReadCommunity());
	swStatus=SNMPHandler.getOIDWalk(snmp, OID.ifOperStatus, sIP, SNMPConfig.getReadCommunity());
	swVLANs=SNMPHandler.getOIDWalk(snmp, OID.vmVlan, sIP, SNMPConfig.getReadCommunity());
	swPortname=SNMPHandler.getOIDWalk(snmp, OID.ifName, sIP, SNMPConfig.getReadCommunity());
	swPortalias=SNMPHandler.getOIDWalk(snmp, OID.ifAlias, sIP, SNMPConfig.getReadCommunity());
	swVPort=SNMPHandler.getOIDWalk(snmp, OID.dot1dBasePortIfIndex, sIP, SNMPConfig.getReadCommunity());
	swCDP=SNMPHandler.getOIDWalk(snmp, OID.cdpCacheAddress, sIP, SNMPConfig.getReadCommunity());
	swCDPC=SNMPHandler.getOIDWalk(snmp, OID.cdpCacheCapabilities, sIP, SNMPConfig.getReadCommunity());
	swSpeed=SNMPHandler.getOIDWalk(snmp, OID.ifSpeed, sIP, SNMPConfig.getReadCommunity());
	swType=SNMPHandler.getOIDWalk(snmp, OID.ifType, sIP, SNMPConfig.getReadCommunity());
	swTypeCisco=SNMPHandler.getOIDWalk(snmp, OID.portType, sIP, SNMPConfig.getReadCommunity());
	swSTP=SNMPHandler.getOIDWalk(snmp, OID.locIfspanInPkts, sIP, SNMPConfig.getReadCommunity());
	
	
	//System.out.println("["+sIP+"]swHostMacIps.size(): "+swHostMacIps.size());
	
	swCiscoPort = SNMPHandler.getOIDWalk(snmp, OID.c2900PortIfIndex, sIP, SNMPConfig.getReadCommunity());
	
	if(swCiscoPort.size()>0){
	swCiscoDuplex = SNMPHandler.getOIDWalk(snmp, OID.c2900PortDuplexStatus, sIP, SNMPConfig.getReadCommunity());
	supportsCiscoDuplex=true;
	
	
	//TODO hier muss unterschieden werden zwischen CiscoPort+Cisco Duplex, Port ID?
	for(int i=0;i<swCiscoPort.size()&&i<swCiscoDuplex.size();i++)
	{
		
		swDuplex.add(swCiscoPort.get(i).substring(swCiscoPort.get(i).indexOf("!")+1)
				+"!"+swCiscoDuplex.get(i).substring(swCiscoDuplex.get(i).indexOf("!")+1)
				);
		
	}
	
	
	
	}else{
		//Keine Cisco Port SNMP unterst�tzt
		HelperClass.msgLog("["+sIP+"]["+sversion+"]["+smodel+"] ERROR Keine Duplexerkennung moeglich.");
	}
	
	//get all the macs of the coresponding vlans
	
	swVLANListe=getVLanList(swVLANs);
	
	//add All Macs to the "Global" Maccache-List
	
	//normal
	swPuffer=SNMPHandler.getOIDWalk(snmp, OID.dot1dTpFdbPort, sIP, SNMPConfig.getReadCommunity());
	for(int j=0;j<swPuffer.size();j++){
		//System.out.println("R["+sIP+"]["+swVLANListe.get(i)+"]:"+swPuffer.get(j));
		swHostMAC.add(swPuffer.get(j));
	}
	swPuffer.clear();
	
	
	//for vlans
	for(int i=0;i<swVLANListe.size();i++)
	{
		swPuffer=SNMPHandler.getOIDWalk(snmp, OID.dot1dTpFdbPort, sIP, SNMPConfig.getReadCommunity()+"@"+swVLANListe.get(i));
		for(int j=0;j<swPuffer.size();j++){
			//System.out.println("R["+sIP+"]["+swVLANListe.get(i)+"]:"+swPuffer.get(j));
			swHostMAC.add(swPuffer.get(j));
		}
		swPuffer.clear();
	}
	
	if(swHostMAC.size()<1){
		HelperClass.msgLog("["+sIP+"] Keine Hosts am Ger�t gefunden.");
	}
	
	//System.out.println("["+sIP+"]: swHostMAC Size:"+swHostMAC.size());
	
	//is needed for SQL summary
	sSQL="";
	
	if(swMACs.size()<0){
		HelperClass.msgLog("["+sIP+"] ERROR MAC Liste der Ports leer. Keine Ports auf dem Ger�t.");
	}else{
		for(int i=0;i<swMACs.size();i++){
			if(swMACs.get(i).substring(swMACs.get(i).indexOf("!")+1).length()>0)
			{
				
				Port p=new Port();
				
				
				sPuffer=swMACs.get(i).substring(swMACs.get(i).indexOf("!")+1);
				
				p.sMAC=sPuffer;
				p.SwitchIP=sIP;
				p.PortID=Integer.parseInt(swMACs.get(i).substring(0,swMACs.get(i).indexOf("!")).replace(OID.ifPhysAddress+".", ""));
				p.VPortID=getVPortID(swVPort, p.PortID);
				
				p.name=getOIDListEntry(swPortname,p.PortID).substring(getOIDListEntry(swPortname,p.PortID).indexOf("!")+1);
				
				//Pr�fen ob der Typ der Verbindung korrekt ist, Serielle Konsole etc ausschlie�en
				if(getType(swType,p.PortID)==6&&!p.name.toLowerCase().contains("vl"))
				{
					
				p.alias=getOIDListEntry(swPortalias,p.PortID).substring(getOIDListEntry(swPortalias,p.PortID).indexOf("!")+1);
				p.vlan=getOIDListEntry(swVLANs,p.PortID).substring(getOIDListEntry(swVLANs,p.PortID).indexOf("!")+1);
				
				//get cstatus of port
				
				sPuffer=getOIDListEntry(swStatus, p.PortID).substring(getOIDListEntry(swStatus, p.PortID).indexOf("!")+1);
				
				if(sPuffer.contains("1"))
				{
					p.cstatus=true;
					
					p.Speed=getOIDListEntry(swSpeed, p.PortID).substring(getOIDListEntry(swSpeed, p.PortID).indexOf("!")+1);
					
				}else{
					p.cstatus=false;	
					
					p.Speed="0";
				}
				
					//Duplex Erkennung - nur fuer spezielle Cisco Switchs moeglich
					
					if(supportsCiscoDuplex)
					{
						sPuffer=getDuplexState(swDuplex,p.PortID).substring(sPuffer.indexOf("!")+1);
						
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
					}else{//hier evtl andere Abfrage verwenden
						p.Duplex="";
					}
					
					//zur�cksetzung der Erkennungsvariablen
					
					p.isUplink=false;
					p.UplinkIP="";
										
					//Pr�fe zuerst per STP
					if(getSTPCount(swSTP, p.PortID)>0)
					{
						p.isUplink=true;	
					}else if(isUplinkportCDP(swCDP, p.PortID))
					{
						p.isUplink=true;
						HelperClass.msgLog("["+sIP+"]["+sversion+"]["+smodel+"]["+p.PortID+"] Kein STP aber CDP");
					}
					
					if(p.isUplink&&isUplinkportCDP(swCDP, p.PortID))
					{
						p.UplinkIP=getUplinkIP(swCDP, p.PortID, p.vlan);
					}
					
					//save Port
					sSQLList.add(p.getDBString());
					
					//wenn kein Uplink und Interface up, dann erfasse Host
					if(!p.isUplink&&p.cstatus==true){
					
					//get Hosts on this port
					ArrayList<String> alHosts=null;
						
					//sofern vorhanden verwende VPortID	
					
					if(p.PortID>100){
						HelperClass.msgLog("["+sIP+"]["+sversion+"]["+smodel+"]["+p.PortID+"] PortID>100");
	
					}
					
					if(p.VPortID>100){
						HelperClass.msgLog("["+sIP+"]["+sversion+"]["+smodel+"]["+p.PortID+"] VPortID>100");
	
					}
					
					if(p.VPortID<0)
					{
					alHosts=getHostsOfPort(swHostMAC,p.PortID);
					}else{
					alHosts=getHostsOfPort(swHostMAC,p.VPortID);	
					}
					
					if(alHosts.size()>0)
					{
						for(int j=0; j<alHosts.size();j++)
						{
							if(alHosts.get(j).length()>0)
							{
								if(!HexToDec.getSimpleHex(p.sMAC).contains(alHosts.get(j)))
								{
									Host h=new Host();
									h.MAC=alHosts.get(j);
									h.PortMAC=p.sMAC;
									h.IP=getIPfromMAC(swHostMacIps,h.MAC);
									h.Duplex=p.Duplex;
									h.Speed=p.Speed;

									//sofern IP erhalten, l�se DNS auf
									if(h.IP.length()>0){
									h.hostname=DNSHelperClass.getHostname(h.IP);
									}
									
									//save Host
									sSQLList.add(h.getDBString());
								}
							}
						}
					}
					
					}
	
			} //wenn kein virtueller Port
				
			} //wenn Mac Adresse vorhanden
			
		} //port
		
		//save in DB
		DBComitterThread dbc=new DBComitterThread(sIP,sSQLList);
		
	} //wenn macliste leer
	
	}//IF abfrage ob SNMP geht
	
	}//Funkstionsende 
	
	private int getType(ArrayList<String> swType, int portID) {
		
		String sPuffer="";
		
		sPuffer=getOIDListEntry(swType,portID).substring(getOIDListEntry(swType,portID).indexOf("!")+1);
		
		if(sPuffer.length()>0)
		{
			return Integer.parseInt(sPuffer);
		}else{
			return 0;
		}
		
			
	}

	private long getSTPCount(ArrayList<String> swSTP, int portID){
	
		String sPuffer="";
		
		sPuffer=getOIDListEntry(swSTP,portID).substring(getOIDListEntry(swSTP,portID).indexOf("!")+1);
		
		if(sPuffer.length()>0)
		{
			return Long.parseLong(sPuffer);
		}else{
			return 0;
		}
		
	}
	
	private String getIPfromMAC(ArrayList<String> swHostMacIps, String sMAC) {
		
		sMAC=HexToDec.getADVfromSimple(sMAC);
		
		String sPuffer="";
		String sOID=OID.ipNetToMediaPhysAddress;
		
		if(sMAC.length()>0){
		
		for(int i=0; i<swHostMacIps.size();i++)
		{
	
			if(swHostMacIps.get(i).substring(swHostMacIps.get(i).indexOf("=")).contains(sMAC)){
				sPuffer=swHostMacIps.get(i).substring(sOID.length(),swHostMacIps.get(i).indexOf("=")+1);
			}
		
		}
		if(sPuffer.length()>0){
			sPuffer=sPuffer.substring(sPuffer.indexOf(".")+1,sPuffer.lastIndexOf(" "));
			sPuffer=sPuffer.substring(sPuffer.indexOf(".")+1);
		}
		
		}
		
		return sPuffer;
	}

	private ArrayList<String> getHostsOfPort(ArrayList<String> swHostMAC, int vPortID) {
		ArrayList<String> alportHost=new ArrayList<String>();
		String sOID=OID.dot1dTpFdbPort+".";
		
		//Bei speziellen Cisco Switchs wird eine ID>100 zur�ckgegeben typischerweise 10101,10102, etc
		//Dies wird hier abgefangen
		
		if(vPortID>10000)
		{
			vPortID=vPortID-10100;
		}
		
		for(int i=0;i<swHostMAC.size();i++){
			if(Integer.parseInt(swHostMAC.get(i).substring(swHostMAC.get(i).indexOf("!")+1))==vPortID)
			{

				alportHost.add(HexToDec.getHex(
					swHostMAC.get(i).substring(
												sOID.length(),
												swHostMAC.get(i).indexOf("!")
												)
											)
					);	
			}
		}
		return alportHost;
	}

	private int getVPortID(ArrayList<String> swVPort, int PortID){
		int iVPort=-1;
		String sOID=OID.dot1dBasePortIfIndex+".";
		
		for(int i=0;i<swVPort.size();i++){
			
			if(Integer.parseInt(swVPort.get(i).substring(swVPort.get(i).indexOf("!")+1))==PortID){
				
				iVPort=Integer.parseInt(swVPort.get(i).substring(swVPort.get(i).indexOf(sOID)+sOID.length(),swVPort.get(i).indexOf("!")));
				
			}
			
		}
		return iVPort;
	}
	
	private String getUplinkIP(ArrayList<String> swCDP, int portID, String vlan) {
		String sPuffer="";
		
		for(int i=0;i<swCDP.size();i++){

			if(swCDP.get(i).contains(OID.cdpCacheAddress+"."+portID)&&vlan.length()==0)
			{
			sPuffer=swCDP.get(i).substring(swCDP.get(i).indexOf("!")+1);
			}
		}
		
		sPuffer=HexToDec.getDec(sPuffer);
		
		return sPuffer;
	}

	private boolean isUplinkportCDP(ArrayList<String> swCDP, int portID) {
		boolean isUplink=false;
		
		for(int i=0;i<swCDP.size();i++){

			if(swCDP.get(i).contains(OID.cdpCacheAddress+"."+portID))
			{
			isUplink=true;
			}
		}
		
		return isUplink;
	}

	private String getDuplexState(ArrayList<String> swDuplex, int portID) {
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
			//pr�fne ob in liste, bei bedarf hinzuf�gen
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