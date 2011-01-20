package org.dh.usertrack;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import org.snmp4j.Snmp;

public class Switch {

	private ArrayList<String> swHostMacIps=new ArrayList<String>();

	private String sIP="";
	private long lIP=0;
	private String sReadcommunity="";
	private Snmp snmp;
	private String svendor="";
	private String smodel="";
	private String sversion="";
	private String sSerial="";
	private String sLocation=""; 
	private String iUptime="";
	private String sAlias="";
	private String sDNS="";
	private int iTimestamp=0;
	
	
	public Switch(String sAdr, String sReadc){
		try {
			InetAddress iaSwitch;
			iaSwitch=InetAddress.getByName(sAdr);
			sIP=iaSwitch.getHostAddress();
			sReadcommunity=sReadc;
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Switch(String sAdr,String sReadc, Snmp snmp, ArrayList<String> alARP) {
		// TODO Auto-generated constructor stub
		
		this.sIP=sAdr;
		
		this.snmp=snmp;
		
		swHostMacIps=alARP;
		
		sReadcommunity=sReadc;
	}
	
	public String getsIP() {
		return sIP;
	}

	public void refresh(){
	
	HelperClass.msgLog("Start: "+sIP);
	lIP=HexToDec.ipToInt(sIP);
	//HelperClass.msgLog("LIP: "+lIP);
	
	String sSQL="";	
	ArrayList<String> sSQLList=new ArrayList<String>();	
	
	String sPuffer="";
	String sCDPPuffer="";
	
	boolean supportsCiscoDuplex=false;
	
	//Get switch info	
		
		
	//Parse SystemDesc && Test if SNMP is working
	String 	sOID="";
		
	sOID=SNMPHandler.getOID(snmp, OIDL.sysDescr0, sIP, sReadcommunity);	
		
	if(sOID.contains("ERROR AT ")){
		
		if(SNMPConfig.getDebuglevel()>=1)
		{
		HelperClass.msgLog("No valid SNMP response from: ["+sIP+"]["+sOID+"]. Aborting Tracking.");
		}
	}else{
	
	//get DNS
 	sDNS=DNSHelperClass.getHostname(sIP);
		
	//is Cisco? -> Pr�fe IOS/Modell
	if(sOID.toLowerCase().contains("cisco")){
		svendor="Cisco";
	
		smodel=Cisco.getModelfromDescr(sOID);
		
		sversion=Cisco.getIOSfromDescr(sOID);
		
		sSerial=SNMPHandler.getOID(snmp, OIDL.chassisId+".0", sIP, sReadcommunity);
		sSerial=sSerial.replace(OIDL.chassisId+".0 = ", "");
		if(sSerial.contains("noSuchObject")){
			sSerial="";
		}
		
		sLocation=Cisco.getLocation(SNMPHandler.getOID(snmp, OIDL.sysLocation0, sIP, sReadcommunity));

		iUptime=Cisco.getUptime(SNMPHandler.getOID(snmp, OIDL.sysUpTimeInstance, sIP, sReadcommunity));
		//HelperClass.msgLog("SOID:["+sOID+"]");
		
		sAlias=Cisco.getAlias(SNMPHandler.getOID(snmp, OIDL.sysName0, sIP, sReadcommunity));
		
	}else{
		
		svendor="Unknown"+"["+sOID+"]";
	}
	
	//HelperClass.msgLog("["+sIP+"]DNS:["+sDNS+"] Vendor: ["+svendor+"] Model: ["+smodel+"] IOS: ["+sversion+"] LOC: ["+sLocation+"] Uptime: ["+iUptime+"] Alias: ["+sAlias+"]");

	
	
	iTimestamp=(int)(System.currentTimeMillis()/1000);
	
	sSQL="MERGE INTO USRTRACK.\"st_switchs\" dest "+
	"USING dual ON (dual.dummy is not null and dest.IP='"+sIP+"') "+
	"WHEN MATCHED "+
	" THEN UPDATE SET "+
	"\"LIP\"='"+lIP+"',"+
	"\"hostname\"='"+sDNS+"',"+
	"\"vendor\"='"+svendor+"',"+
	"\"model\"='"+smodel+"',"+
	"\"osversion\"='"+sversion+"',"+
	"\"location\"='"+sLocation+"',"+
	"\"uptime\"='"+iUptime+"',"+
	"\"alias\"='"+sAlias+"',"+
	"SERIAL='"+sSerial+"',"+
	"\"stamptime\"='"+iTimestamp+"' "+
	"WHEN NOT MATCHED "+
	 "THEN INSERT (\"IP\",\"LIP\",\"hostname\",\"vendor\",\"model\",\"osversion\",\"location\",\"alias\",\"uptime\",SERIAL,\"stamptime\") VALUES ('"+sIP+"','"+lIP+"','"+sDNS+"','"+svendor+"','"+smodel+"','"+sversion+"','"+sLocation+"','"+sAlias+"','"+iUptime+"','"+sSerial+"',"+iTimestamp+")";

	sSQLList.add(sSQL);
	
	ArrayList<String> swPuffer=new ArrayList<String>();
	
	ArrayList<String> swMACs=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swStatus=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swVLANs=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swVLANPorts=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swPortname=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swPortalias=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swVPort=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swHostMAC=new ArrayList<String>(); //Liste der Mac Adressen
	ArrayList<String> swCDP=new ArrayList<String>();
	ArrayList<String> swCDPC=new ArrayList<String>();
	ArrayList<String> swCDPDI=new ArrayList<String>();
	ArrayList<String> swCDPPort=new ArrayList<String>();
	ArrayList<String> swVLANListe=new ArrayList<String>();
	ArrayList<String> swSpeed=new ArrayList<String>();
	ArrayList<String> swType=new ArrayList<String>();
//	ArrayList<String> swTypeCisco=new ArrayList<String>();
	ArrayList<String> swSTP=new ArrayList<String>();
	
	//only for selected cisco switchs
	ArrayList<String> swCiscoPort=new ArrayList<String>();
	ArrayList<String> swCiscoDuplex=new ArrayList<String>();	
	ArrayList<String> swDuplex=new ArrayList<String>();
	
	swMACs=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.ifPhysAddress, sIP, sReadcommunity);
	swStatus=SNMPHandler.getOIDWalk(snmp, OIDL.ifOperStatus, sIP, sReadcommunity);
	swVLANs=SNMPHandler.getOIDWalk(snmp, OIDL.vtpVlanState, sIP, sReadcommunity);
	swVLANPorts=SNMPHandler.getOIDWalk(snmp, OIDL.vmVlan, sIP, sReadcommunity);
	swPortname=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.ifName, sIP, sReadcommunity);
	swPortalias=SNMPHandler.getOIDWalk(snmp, OIDL.ifAlias, sIP, sReadcommunity);
	
	swCDP=SNMPHandler.getOIDWalk(snmp, OIDL.cdpCacheAddress, sIP, sReadcommunity);
	swCDPC=SNMPHandler.getOIDWalk(snmp, OIDL.cdpCacheCapabilities, sIP, sReadcommunity);
	swCDPDI=SNMPHandler.getOIDWalk(snmp, OIDL.cdpCacheDeviceId, sIP, sReadcommunity);
	swCDPPort=SNMPHandler.getOIDWalk(snmp, OIDL.cdpCacheDevicePort, sIP, sReadcommunity);
	swSpeed=SNMPHandler.getOIDWalk(snmp, OIDL.ifSpeed, sIP, sReadcommunity);
	swType=SNMPHandler.getOIDWalk(snmp, OIDL.ifType, sIP, sReadcommunity);
//	swTypeCisco=SNMPHandler.getOIDWalk(snmp, OID.portType, sIP, sReadcommunity);
	swSTP=SNMPHandler.getOIDWalk(snmp, OIDL.locIfspanInPkts, sIP, sReadcommunity);
	
	swCiscoPort = SNMPHandler.getOIDWalk(snmp, OIDL.c2900PortIfIndex, sIP, sReadcommunity);
	
	if(swCiscoPort.size()>0){
	swCiscoDuplex = SNMPHandler.getOIDWalk(snmp, OIDL.c2900PortDuplexStatus, sIP, sReadcommunity);
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
		if(SNMPConfig.getDebuglevel()>=2)
		{
		HelperClass.msgLog("["+sIP+"]["+sversion+"]["+smodel+"] ERROR Keine Duplexerkennung moeglich.");
		}
	}
	
	//get all the macs of the coresponding vlans
	
	swVLANListe=getVLanList(swVLANs);
	
	//add All Macs to the "Global" Maccache-List
	
	//normal
	swPuffer=SNMPHandler.getOIDWalk(snmp, OIDL.dot1dTpFdbPort, sIP, sReadcommunity);
	for(int j=0;j<swPuffer.size();j++){
		//System.out.println("R["+sIP+"]["+swVLANListe.get(i)+"]:"+swPuffer.get(j));
		swHostMAC.add(swPuffer.get(j));
	}
	swPuffer.clear();
	
	//vor den VLAN spezfischen Sachen
	
	
	//for vlans
	for(int i=0;i<swVLANListe.size();i++)
	{
		
		//for hosts
		swPuffer=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.dot1dTpFdbPort, sIP, sReadcommunity+"@"+swVLANListe.get(i));
		for(int j=0;j<swPuffer.size();j++){
			//System.out.println("R["+sIP+"]["+swVLANListe.get(i)+"]:"+swPuffer.get(j));
			swHostMAC.add(swPuffer.get(j));
		}
		swPuffer.clear();
		//for vportids
		swPuffer=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.dot1dBasePortIfIndex, sIP, sReadcommunity+"@"+swVLANListe.get(i));
		for(int j=0;j<swPuffer.size();j++){
			//System.out.println("R["+sIP+"]["+swVLANListe.get(i)+"]:"+swPuffer.get(j));
			swVPort.add(swPuffer.get(j));
		}
		swPuffer.clear();
		
	}
	
	if(swHostMAC.size()<1){
		if(SNMPConfig.getDebuglevel()>=2)
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
				p.PortID=Integer.parseInt(swMACs.get(i).substring(0,swMACs.get(i).indexOf("!")).replace(OIDL.ifPhysAddress+".", ""));
				p.VPortID=getVPortID(swVPort, p.PortID);
				
				p.name=getOIDListEntry(swPortname,p.PortID).substring(getOIDListEntry(swPortname,p.PortID).indexOf("!")+1);
				
				//Pr�fen ob der Typ der Verbindung korrekt ist, Serielle Konsole etc ausschlie�en
				if(getType(swType,p.PortID)==6&&!p.name.toLowerCase().contains("vl"))
				{
					
				p.alias=getOIDListEntry(swPortalias,p.PortID).substring(getOIDListEntry(swPortalias,p.PortID).indexOf("!")+1);
				p.vlan=getOIDListEntry(swVLANPorts,p.PortID).substring(getOIDListEntry(swVLANPorts,p.PortID).indexOf("!")+1);
				
				//get cstatus of port
				
				sPuffer=getOIDListEntry(swStatus, p.PortID).substring(getOIDListEntry(swStatus, p.PortID).indexOf("!")+1);
				
				if(sPuffer.contains("1"))
				{
					p.cstatus=true;
					
					p.Speed=getOIDListEntry(swSpeed, p.PortID).substring(getOIDListEntry(swSpeed, p.PortID).indexOf("!")+1);
					
					if(p.Speed.length()>0){
						if(Long.parseLong(p.Speed)>1000000){
							p.Speed=""+(Long.parseLong(p.Speed)/1000000);
						}
					}
					
					
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
					p.hasCDPN=false;
					p.UplinkIP="";
										
					//Pr�fe zuerst per STP
					if(getSTPCount(swSTP, p.PortID)>0)
					{
						p.isUplink=true;	
					}else if(isUplinkportCDP(swCDP,swCDPC,p.PortID)){
						p.isUplink=true;
					}
				
					if(p.isUplink)
					{
						p.UplinkIP=getUplinkCDPIP(swCDP, p.PortID);
						if(p.UplinkIP.length()<1){
						p.isUplink=false;	
						}
					}
					
					if(isCDP(swCDP, p.PortID)){
						p.hasCDPN=true;
					}
					

					//
					
					if(p.VPortID<0)
					{
						//pr�fe ob PortID bereits in VPortID existent
						if(!isVportID(swVPort, p.PortID)){
							p.VPortID=p.PortID;
						}
					}
					
					//save Port
					//System.out.println(p.getDBString());
					sSQLList.add(p.getDBString());

					if(p.VPortID<0)
					{
						HelperClass.msgLog("["+sIP+"]["+sversion+"]["+smodel+"]["+p.PortID+"] ["+getCDPIP(swCDP, p.PortID)+"] no vportid");
					}else{
					
					//wenn Uplink + CDP==Switch
					if(p.isUplink&&p.cstatus)
					{
					
						if(p.UplinkIP.length()>0){
						
						Host h=new Host();
						h.MAC=getMACfromIP(swHostMacIps, p.UplinkIP);
						h.IP=p.UplinkIP;
						
						h.PortMAC=p.sMAC;
						h.MAC=HexToDec.getADVfromSimple(h.MAC);
						h.Duplex=p.Duplex;
						h.Speed=p.Speed;
						
						sCDPPuffer=getCDPLport(swCDP, h.IP);
						h.CDPDeviceIP=h.IP;
						h.CDPDeviceID=getCDPID(swCDPDI, sCDPPuffer);
						h.CDPDevicePort=getCDPPort(swCDPPort, sCDPPuffer);
						h.CDPDeviceTyp=getCDPTyp(swCDPC, sCDPPuffer);
						sCDPPuffer="";	
					
						h.hostname=DNSHelperClass.getHostname(h.IP);
					
						if(h.MAC.length()>0){
						sSQLList.add(h.getDBString());
						}else{
							HelperClass.msgLog("CDP ERROR ON SW: "+h.getDBString());
						}
						
						
						}else{
							HelperClass.msgLog("Uplink but no IP via CDP"+p.PortID);
						}
						
						
						}
						
					//wenn kein Uplink und Interface up und kein Switch, dann erfasse Host
					if(!p.isUplink&&p.cstatus==true&&!SNMPTrackHelper.isinList(p.UplinkIP)){
//					if(p.cstatus==true&&!SNMPTrackHelper.isinList(p.UplinkIP)){

						//get Hosts on this port
						ArrayList<String> alHosts=null;
							
						
						
							alHosts=getHostsOfPort(swHostMAC,p.VPortID);	
							
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
											h.MAC=HexToDec.getADVfromSimple(h.MAC);
											
											if(isVMWareMAC(h.MAC)){
												h.sVHOST="1";
											}else if(isVBoxMAC(h.MAC)){
												h.sVHOST="2";
											}else if(isVPCMAC(h.MAC)){
												h.sVHOST="3";
											}else if(isVMMAC(h.MAC)){
												h.sVHOST="4";
											}
											
											h.IP=getIPfromMAC(swHostMacIps,h.MAC);
											h.lIP=HexToDec.ipToInt(h.IP);
											h.Duplex=p.Duplex;
											h.Speed=p.Speed;
		
											//sofern IP erhalten, l�se DNS auf
											if(h.IP.length()>0){
											
											if(p.hasCDPN){
												
												sCDPPuffer=getCDPLport(swCDP, h.IP);
												
												
												//Pr�fe ob HostIP in CDP IP(s)
												if(sCDPPuffer.length()>0){
													//wenn ja hole ID
												h.CDPDeviceIP=h.IP;
												
												h.CDPDeviceID=getCDPID(swCDPDI, sCDPPuffer);
												
												h.CDPDevicePort=getCDPPort(swCDPPort, sCDPPuffer);
												
												h.CDPDeviceTyp=getCDPTyp(swCDPC, sCDPPuffer);

												//ENDE
												sCDPPuffer="";	
													
												}
											
											}	
												
											h.hostname=DNSHelperClass.getHostname(h.IP);
											
											if(h.hostname.length()>0){
												h.lastuser=LDAP.getUser(h.hostname);
											}
											
											}
											
											//Frage CDP Sachen ab
											
											//save Host
											sSQLList.add(h.getDBString());
										}
									}
								}
							}
						}else if(!p.isUplink&&p.cstatus==true&&SNMPTrackHelper.isinList(p.UplinkIP)){
							
							HelperClass.msgLog("Bekannter Switch auf ["+sIP+"]["+sAlias+"]["+p.name+"]");
							//wenn Port bekannter Switch
//							Host h=new Host();
//							h.MAC=alHosts.get(j);
//							h.PortMAC=p.sMAC;
//							h.MAC=HexToDec.getADVfromSimple(h.MAC);
//							h.IP=p.UplinkIP;
//							h.Duplex=p.Duplex;
//							h.Speed=p.Speed;
//
//							//sofern IP erhalten, l�se DNS auf
//							if(h.IP.length()>0){
//							h.hostname=DNSHelperClass.getHostname(h.IP);
//							}
//							
//							//Frage CDP Sachen ab
//							
//							//save Host
//							sSQLList.add(h.getDBString());
							
						}
					}
	
			} //wenn kein virtueller Port
				
			} //wenn Mac Adresse vorhanden
			
		} //port
		
		//save in DB
		DBComitterThread dbc=new DBComitterThread(sIP, sSQLList);
		
//		if(SNMPConfig.getDebuglevel()>=2)
//		{
		HelperClass.msgLog("[DEBUG] FIN:"+sIP);
//		}
		
	} //wenn macliste leer
	
	}//IF abfrage ob SNMP geht
	
	}//Funkstionsende 
	
	private boolean isVMMAC(String sMAC) {
		boolean isVhost=false;
		ArrayList<String> alVMMACprefix=new ArrayList<String>();

		alVMMACprefix.add("00:1C:42");
		alVMMACprefix.add("00:0F:4B");
		alVMMACprefix.add("00:16:3E");

		for(int i=0;i<alVMMACprefix.size();i++){
			if(sMAC.startsWith(alVMMACprefix.get(i))){
				isVhost=true;
			}
		}
		
		return isVhost;
	}
	
	private boolean isVPCMAC(String sMAC) {
		boolean isVhost=false;
		ArrayList<String> alVPCMACprefix=new ArrayList<String>();

		alVPCMACprefix.add("00:03:FF");

		for(int i=0;i<alVPCMACprefix.size();i++){
			if(sMAC.startsWith(alVPCMACprefix.get(i))){
				isVhost=true;
			}
		}
		
		return isVhost;
	}

	private boolean isVBoxMAC(String sMAC) {
		boolean isVhost=false;
		ArrayList<String> alVBoxMACprefix=new ArrayList<String>();

		alVBoxMACprefix.add("08:00:27");

		for(int i=0;i<alVBoxMACprefix.size();i++){
			if(sMAC.startsWith(alVBoxMACprefix.get(i))){
				isVhost=true;
			}
		}
		
		return isVhost;
	}

	private boolean isVMWareMAC(String sMAC) {
		boolean isVhost=false;
		ArrayList<String> alVMwareMACprefix=new ArrayList<String>();

		alVMwareMACprefix.add("00:05:69");
		alVMwareMACprefix.add("00:0C:29");
		alVMwareMACprefix.add("00:1C:14");
		alVMwareMACprefix.add("00:50:56");

		for(int i=0;i<alVMwareMACprefix.size();i++){
			if(sMAC.startsWith(alVMwareMACprefix.get(i))){
				isVhost=true;
			}
		}
		
		return isVhost;
	}

	private String getMACfromIP(ArrayList<String> swHostMacIP, String uplinkIP) {
		String sPuffer="";
		for(int i=0;i<swHostMacIP.size();i++)
		{
			if(swHostMacIP.get(i).contains(uplinkIP)){
				sPuffer=swHostMacIP.get(i).substring(swHostMacIP.get(i).indexOf("!")+1);
			}
		}
		return sPuffer;
	}

	private String getCDPTyp(ArrayList<String> swCDPC, String sCDPLport) {
		String sPuffer="";
		for(int i=0;i<swCDPC.size();i++){
			if(swCDPC.get(i).contains(sCDPLport)){
				sPuffer=swCDPC.get(i).substring(swCDPC.get(i).indexOf("!")+1);
			}
		}
		return sPuffer;
	}

	private String getCDPPort(ArrayList<String> swCDPPort, String sCDPLport) {
		String sPuffer="";
		for(int i=0;i<swCDPPort.size();i++){
			if(swCDPPort.get(i).contains(sCDPLport)){
				sPuffer=swCDPPort.get(i).substring(swCDPPort.get(i).indexOf("!")+1);
			}
		}
		return sPuffer;
	}

	private String getCDPID(ArrayList<String> swCDPDI, String sCDPLport) {
		String sPuffer="";
		String[] sHexpuffer;
		
		for(int i=0;i<swCDPDI.size();i++){
			if(swCDPDI.get(i).contains(sCDPLport)){
				sPuffer=swCDPDI.get(i).substring(swCDPDI.get(i).indexOf("!")+1);
			}
		}
		
		if(sPuffer.contains(":")){
			sHexpuffer=sPuffer.split(":");
			sPuffer="";
			for(String a:sHexpuffer){
				int c = Integer.parseInt(a,16);
				if(c!=0){
				  char chr = (char)c;
				  sPuffer+=(chr);
				  }

			}
			
		}
		
		return sPuffer;
	}

	private String getCDPLport(ArrayList<String> swCDP, String IP) {
		String sPuffer="";
		for (int i=0;i<swCDP.size();i++){
			if(swCDP.get(i).contains(HexToDec.getADVfromSimple(HexToDec.getHex(IP)))){
				sPuffer=swCDP.get(i);
				sPuffer=sPuffer.replace(OIDL.cdpCacheAddress, "");
				sPuffer=sPuffer.substring(1,sPuffer.indexOf("!"));
			}
		}
		return sPuffer;
	}

	private boolean isVportID(ArrayList<String> swVPort, int portID) {
		
		boolean isVportID=false;
		
		for(int i=0;i<swVPort.size();i++){

		if(swVPort.contains("."+portID+"!")){	
			isVportID=true;
		}
		
		}
		return isVportID;
	}

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
		String sOID=OIDL.ipNetToMediaPhysAddress;
		
		if(sMAC.length()>0){
		
		for(int i=0; i<swHostMacIps.size();i++)
		{
	
			if(swHostMacIps.get(i).substring(swHostMacIps.get(i).indexOf("!")).contains(sMAC)){
				sPuffer=swHostMacIps.get(i).substring(sOID.length(),swHostMacIps.get(i).indexOf("!")+1);
			}
		
		}
		if(sPuffer.length()>0){
			sPuffer=sPuffer.substring(sPuffer.indexOf(".")+1,sPuffer.lastIndexOf("!"));
			sPuffer=sPuffer.substring(sPuffer.indexOf(".")+1);
		}
		
		}
		
		return sPuffer;
	}

	private ArrayList<String> getHostsOfPort(ArrayList<String> swHostMAC, int vPortID) {
		ArrayList<String> alportHost=new ArrayList<String>();
		String sOID=OIDL.dot1dTpFdbPort+".";
		
//		if(vPortID>100){
//		System.out.println("VPORT >100: "+vPortID);
//		}
		
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
		String sOID=OIDL.dot1dBasePortIfIndex+".";
		
		for(int i=0;i<swVPort.size();i++){
			
			if(Integer.parseInt(swVPort.get(i).substring(swVPort.get(i).indexOf("!")+1))==PortID){
				
				iVPort=Integer.parseInt(swVPort.get(i).substring(swVPort.get(i).indexOf(sOID)+sOID.length(),swVPort.get(i).indexOf("!")));
				
			}
			
		}
		return iVPort;
	}
	
	private String getUplinkCDPIP(ArrayList<String> swCDP, int portID) {
		String sPuffer="";
		
		for(int i=0;i<swCDP.size();i++){
			
				if(swCDP.get(i).contains(OIDL.cdpCacheAddress+"."+portID+"."))
				{
				sPuffer=swCDP.get(i).substring(swCDP.get(i).indexOf("!")+1);
				}
				
		}
		
		sPuffer=HexToDec.getDec(sPuffer);
		
		return sPuffer;
	}

	private String getCDPIP(ArrayList<String> swCDP, int portID) {
		String sPuffer="";
		
		for(int i=0;i<swCDP.size();i++){

			if(swCDP.get(i).contains(OIDL.cdpCacheAddress+"."+portID+"."))
			{
			sPuffer=swCDP.get(i).substring(swCDP.get(i).indexOf("!")+1);
			}
		}
		
		sPuffer=HexToDec.getDec(sPuffer);
		
		return sPuffer;
	}
	
	private boolean isCDP(ArrayList<String> swCDP, int portID) {
		boolean isCDP=false;
		for(int i=0;i<swCDP.size();i++){

			if(swCDP.get(i).contains(OIDL.cdpCacheAddress+"."+portID+"."))
			{
			isCDP=true;
			}
		}
		return isCDP;
	}
	
	private boolean isUplinkportCDP(ArrayList<String> swCDP, ArrayList<String> swCDPC, int portID) {
		boolean isUplink=false;
		boolean isCDP=false;
		
		for(int i=0;i<swCDP.size();i++){

			if(swCDP.get(i).contains(OIDL.cdpCacheAddress+"."+portID+"."))
			{
			isCDP=true;
			
			if(isCDPswitch(swCDPC.get(i))){
				isUplink=true;
			}
			
			}
		}
		
		return isUplink;
	}

	private boolean isCDPswitch(String CDPC) {
		// TODO Auto-generated method stub
		/*
		 *
		 *	sw10305de-132.122# sh cdp neigh
		Capability Codes: R - Router, T - Trans Bridge, B - Source Route Bridge
		                  S - Switch, H - Host, I - IGMP, r - Repeater, P - Phone
		
		Device ID        Local Intrfce     Holdtme    Capability  Platform  Port ID
		AP10302DE-132.17 Fas 0/24           151           T       AP1020    enet		00 00 00 02 -> 000010 
		ro00066de        Fas 0/12           125           R       3725      Fas 0/0		00 00 00 01 -> 000001
		ro00012de        Fas 0/23           136           R       2610      Eth 0/0		00 00 00 01 -> 000001
		sw10304de-132.121														   010000
		                 Gig 0/1            176           S       WS-C3550-2Gig 0/2		00 00 00 08 -> 001000
		TBM05453534      Gig 0/2            142         T S I     WS-C6509  3/8			00 00 00 2A	-> 101010
		sw10305de-132.122#													   						   IHSBTR
		 * 
		 */
		
		boolean isSwitch=false;
		
		int iPuffer;
		
		if(CDPC.length()>0)
		{
			CDPC=CDPC.substring(CDPC.indexOf("!")+1);
			CDPC=CDPC.substring(CDPC.lastIndexOf(":")+1);
			iPuffer=Integer.parseInt(CDPC,16);
			CDPC=Integer.toBinaryString(iPuffer);
			
			while(CDPC.length()<8){
				CDPC="0"+CDPC;
			}
			
			if(CDPC.charAt(2)=='1'){
				isSwitch=true;
			}
		}
		
		return isSwitch;
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
			sTMP=Liste.get(i).substring(0,Liste.get(i).indexOf("!"));
			sTMP=sTMP.substring(sTMP.lastIndexOf(".")+1);
			//pruefe ob in liste, bei bedarf hinzuf�gen
			if(!vll.contains(sTMP))
			{
				//ignoriere Cisco spezifische VLANs
				if(Integer.parseInt(sTMP)<1002||Integer.parseInt(sTMP)>1005){
				vll.add(sTMP);
				}
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

	public String readCommunity() {
		// TODO Auto-generated method stub
		return sReadcommunity;
	}
	
}
