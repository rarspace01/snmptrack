package org.dh.usertrack;

import java.io.IOException;
import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPTrack {

	private int ifinishedThreads=0;
	private int activeThreads=0;

	TransportMapping transport;
	Snmp snmp;

	ArrayList<String> swHostMacIps=new ArrayList<String>();
	
	
	public int getActiveThreads() {
		return activeThreads;
	}

	public void setActiveThreads(int activeThreads) {
		this.activeThreads = activeThreads;
	}

	public int getIfinishedThreads() {
		return ifinishedThreads;
	}

	public void setIfinishedThreads(int ifinishedThreads) {
		this.ifinishedThreads = ifinishedThreads;
	}
	
	public SNMPTrack() {
		
		ArrayList<String> sPufferList=new ArrayList<String>();
		
		HelperClass.msgLog("Starte SNMPTrack");
		
		HelperClass.msgLog("Initialisiere SNMP");
		
		try {
			DefaultUdpTransportMapping dutm=new DefaultUdpTransportMapping();
			
			//System.out.println(dutm.getReceiveBufferSize());
			
			dutm.setReceiveBufferSize(2^24);
			
			transport = dutm;
			
			snmp = new Snmp(transport);
			transport.listen();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HelperClass.msgLog("Lade aktuelle Switchliste");
		
		ArrayList<Switch> swList=new ArrayList<Switch>();
		
		SwitchListe sws=new SwitchListe();
		
		swList=sws.getSwitchList();
		
		SNMPTrackHelper.switchListe=swList;
		
		HelperClass.msgLog("Lade ARP Cache");
		
		swHostMacIps = SNMPHandler.getOIDWalknonBluk(snmp, OID.ipNetToMediaPhysAddress, SNMPConfig.getRouter(), SNMPConfig.getReadCommunity());
//		for(int i=0; i<SNMPConfig.getRouters().size();i++){
//			
//			sPufferList=SNMPHandler.getOIDWalknonBluk(snmp, OID.ipNetToMediaPhysAddress, SNMPConfig.getRouters().get(i), SNMPConfig.getReadCommunity());
//			
//			for (int j=0; j<sPufferList.size();j++){
//				
//				if(!swHostMacIps.contains(sPufferList.get(j))){
//					
//					swHostMacIps.add(sPufferList.get(j));
//					
//				}
//				
//			}
//			
//		}
		
		
		HelperClass.msgLog("Gefundene ARP Einträge: "+swHostMacIps.size());
		
		HelperClass.msgLog("Beginne mit Auslese Prozess.");
		
		for(int i=0; i<swList.size();i++)
		{
			if(getActiveThreads()>=SNMPConfig.getThreadmaxcount())
			{
				while(getActiveThreads()>=SNMPConfig.getThreadmaxcount()){
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			setActiveThreads(getActiveThreads()+1);	
			SwitchWorkerThread t=new SwitchWorkerThread("Thread Switch Nr. "+i, this.snmp,this, swList.get(i).getsIP(), swList.get(i).readCommunity());
		}
		
		while(getActiveThreads()>0){
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		HelperClass.msgLog("Starte Duplikat Erkennungs + Entfernungsmodus");
		
		SNMPTrackHelper.updateALevels();
		
		SNMPTrackHelper.cleanupDuplicates();
		
	}
	


	public static void main(String[] args) {
	
		long time1=(long)System.currentTimeMillis()/1000;
		System.out.println("SNMP:SART:"+time1);
		
		new SNMPTrack();
		
		long time2=(long)System.currentTimeMillis()/1000;
		System.out.println("SNMP:STP:"+(long)System.currentTimeMillis()/1000);
		System.out.println("SNMP:DIF:"+(time2-time1));
	}
	
	class SwitchWorkerThread implements Runnable {
		Thread runner;
		Address targetAddress;
		String sIP;
		String sReadc;
		SNMPTrack jm;
		Snmp snmp;
		TransportMapping transport;
		int iMax;
		
		  public SwitchWorkerThread(String threadName, Snmp snmp, SNMPTrack jm, String SwitchAdresse, String ReadCommunity) {
		    
			sIP=SwitchAdresse;  
			sReadc=ReadCommunity; 
			
			this.jm = jm;
		    this.snmp = snmp;
		    runner = new Thread(this, threadName);
		    runner.start(); 
		  }

		  public void run() {

			Switch workerSwitch=new Switch(sIP, sReadc, this.snmp, jm.swHostMacIps);
			
			workerSwitch.refresh();
				
			threadMGTfinish();	
		  }
		  
		  private void threadMGTfinish(){
				jm.setIfinishedThreads(jm.getIfinishedThreads()+1);
				jm.setActiveThreads(jm.getActiveThreads()-1);  
		  }
	}
}