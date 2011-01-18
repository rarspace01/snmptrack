package org.dh.usertrack;

import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;

class VLANWorkerThread implements Runnable {
	Thread runner;
	Address targetAddress;
	String sIP;
	String sReadc;
	SNMPTrack jm;
	Snmp snmp;
	TransportMapping transport;
	int iMax;
	
	  public VLANWorkerThread(String threadName, Snmp snmp, String SwitchAdresse, String ReadCommunity) {
	    
		sIP=SwitchAdresse;  
		sReadc=ReadCommunity; 
		
		this.jm = jm;
	    this.snmp = snmp;
	    runner = new Thread(this, threadName);
	    runner.start(); 
	  }

	  public void run() {

		  ArrayList<String> svStatus=new ArrayList<String>();
		  ArrayList<String> svTyp=new ArrayList<String>();
		  ArrayList<String> svName=new ArrayList<String>();
		  
		//Switch workerVLAN=new Switch(sIP, sReadc, this.snmp);
		
		  //get The VLAN Lists
		  
		  svStatus=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.vtpVlanState, SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));

		  svTyp=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.vtpVlanType, SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));
		  
		  svName=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.vtpVlanName, SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));

		 //erstelle VLANs 
		  
	  }
	  
}
