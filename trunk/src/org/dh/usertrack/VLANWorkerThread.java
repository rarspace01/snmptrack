package org.dh.usertrack;

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

		//Switch workerVLAN=new Switch(sIP, sReadc, this.snmp);
			
	  }
	  
}
