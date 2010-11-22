package org.dh.usertrack.snmptest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;

public class Switch {

	private String sIP="";
	private Snmp snmp;
	private TransportMapping transport;
	private String svendor="";
	private String smodel="";
	private String sversion="";
	
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
	
	//Get switch info	
		
		
	//Parse SystemDesc && Test if SNMP is working
	String 	sOID="";
		
	sOID=SNMPHandler.getOID(snmp, "1.3.6.1.2.1.1.1.0", sIP, SNMPConfig.getReadCommunity());	
		
	if(sOID.contains("ERROR AT ")){
		HelperClass.msgLog("SNMP not runnging on: ["+sIP+"]");
	}else{
	
	//is Cisco? -> Prüfe IOS/Modell
	if(sOID.toLowerCase().contains("cisco")){
		svendor="Cisco";
	
		smodel=Cisco.getModelfromDescr(sOID);
		
		//HelperClass.msgLog("SOID:["+sOID+"]");
		
	}else{
		
		svendor="Unknown"+"["+sOID+"]";
	}
	
	HelperClass.msgLog("["+sIP+"]Vendor: ["+svendor+"] Model: ["+smodel+"]");
	
	//in Boerse-project:
	//
	//String sSQL="INSERT INTO kurs (wkn,stamptime,kurswert) VALUES ('"+swkn+"','"+timestamp+"','"+iKurs+"') ON DUPLICATE KEY UPDATE kurswert="+iKurs+";";
	
		
	//String sSQL="INSERT ";	
		
	}
	
	}
	
}
