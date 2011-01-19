package org.dh.usertrack;

import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;

class VLANWorkerThread implements Runnable {
	
	ArrayList<VLAN> vlList=new ArrayList<VLAN>();
	
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

		  String sID="",sName="",sTyp="", sStatus="", sVTP;
		  
		  ArrayList<String> svStatus=new ArrayList<String>();
		  ArrayList<String> svTyp=new ArrayList<String>();
		  ArrayList<String> svName=new ArrayList<String>();
		  
		  ArrayList<String> sSQLlist=new ArrayList<String>();
		  
		//Switch workerVLAN=new Switch(sIP, sReadc, this.snmp);
		
		  //get The VLAN Lists
		  
		  svStatus=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.vtpVlanState, SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));

		  svTyp=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.vtpVlanType, SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));
		  
		  svName=SNMPHandler.getOIDWalknonBulkSlow(snmp, OIDL.vtpVlanName, SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));

		  for(int i=0;i<svName.size();i++){
			  
			  VLAN vl=new VLAN();
			  
			  sID=svName.get(i).substring(OIDL.vtpVlanName.length()+1, svName.get(i).indexOf("!"));	  
			  sName=svName.get(i).substring(svName.get(i).indexOf("!")+1);
			  sStatus=svStatus.get(i).substring(svStatus.get(i).indexOf("!")+1);
			  sTyp=svTyp.get(i).substring(svTyp.get(i).indexOf("!")+1);
			  
			  //System.out.println(sID);
			  
			  sVTP=sID.substring(0,sID.indexOf("."));
			  
			  sID=sID.substring(sID.indexOf(".")+1);
			  
			  vl.sID=sID;
			  vl.sVTPD=sVTP;
			  vl.sName=sName;
			  vl.sStatus=sStatus;
			  vl.sType=sTyp;
			  
			  vlList.add(vl);
			  
		  }
		  
		  for(int j=0;j<vlList.size();j++){
			 
			  sSQLlist.add(vlList.get(j).getDBString());
			  
		  }
		  
		  DBComitterThread dbcv=new DBComitterThread("VLANS", sSQLlist);
		  
		 //erstelle VLANs 
		  
			//sPufferList=SNMPHandler.getOIDWalknonBulk(snmp, , SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));
			
			//SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!"));
			
		  
	  }
	  
}
