package org.dh.usertrack;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPTrack {

	private int iRev=3;
	
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
		
		HelperClass.msgLog("Rev: "+iRev);
		
		ArrayList<String> sPufferList=new ArrayList<String>();
		
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		
		HelperClass.msgLog("Start Request on:\nPlatform: ["+osBean.getArch()+"] - "+osBean.getAvailableProcessors()+" CPUs\nOS: ["+osBean.getName()+"]\nVersion: ["+osBean.getVersion()+"]");
		HelperClass.msgLog("CONF MAXT:["+SNMPConfig.getThreadmaxcount()+"] DEBUGLVL: ["+SNMPConfig.getDebuglevel()+"] SNMPINT: ["+SNMPConfig.getISleeper()+"]");
		
		HelperClass.msgLog("Starte SNMPTrack");
		
		HelperClass.msgLog("Lade Config");
		
		SNMPTrackConfig.loadSNMPTrackConfig();
		
		HelperClass.msgLog("Initialisiere SNMP");
		
		try {
			DefaultUdpTransportMapping dutm=new DefaultUdpTransportMapping();
			
			//System.out.println(dutm.getReceiveBufferSize());
			
			dutm.setReceiveBufferSize(2^28);
			
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
		
		HelperClass.msgLog("Switchs zu Auslesen: "+swList.size());
		
		//if args Switch was set remove all who dont match:
		if(HelperClass.sWorkIP.length()>1){
			while(swList.size()>1){
				
				if(!swList.get(0).getsIP().contains(HelperClass.sWorkIP)){
					swList.remove(0);
				}else if(swList.size()>1){
					swList.remove(1);
				}
				
			}
			if(!swList.get(0).getsIP().contains(HelperClass.sWorkIP)){
				swList.remove(0);	
			}
		}
		//
		
		if(SNMPConfig.getRouters().size()>0){
		HelperClass.msgLog("Lade VLAN Liste");
		
		VLANWorkerThread vt=new VLANWorkerThread("Thread VLAN Nr. 1", this.snmp, SNMPConfig.getRouters().get(0).substring(0,SNMPConfig.getRouters().get(0).indexOf("!")), SNMPConfig.getRouters().get(0).substring(SNMPConfig.getRouters().get(0).indexOf("!")+1));
		
		}		
		
		HelperClass.msgLog("Lade ARP Cache");

		for(int i=0; i<SNMPConfig.getRouters().size();i++){
			
			HelperClass.msgLog("Lade ARP Cache von "+SNMPConfig.getRouters().get(i).substring(0,SNMPConfig.getRouters().get(i).indexOf("!")));
			
			sPufferList=SNMPHandler.getOIDWalknonBulk(snmp, 2, OIDL.ipNetToMediaPhysAddress, SNMPConfig.getRouters().get(i).substring(0,SNMPConfig.getRouters().get(i).indexOf("!")), SNMPConfig.getRouters().get(i).substring(SNMPConfig.getRouters().get(i).indexOf("!")+1));
			
			for (int j=0; j<sPufferList.size();j++){
				
				if(!swHostMacIps.contains(sPufferList.get(j))){
					
					swHostMacIps.add(sPufferList.get(j));
					
				}
				
			}
			
		}
		
		
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
		
		HelperClass.msgLog("Level geupdated");
		
		SNMPTrackHelper.cleanupDuplicates();
		
		HelperClass.msgLog("Duplikate entfernt");
		
		SNMPTrackHelper.transferData();
		
		HelperClass.msgLog("Datenbank fertig kopiert.");
		
		HelperClass.msgLog("SNMPTrack fertig.");
		
	}
	


	public static void main(String[] args) {
	
//		System.out.println("LOC: ["+HelperClass.class.getProtectionDomain().getCodeSource().getLocation().getFile()+"]");
		
//		System.out.println("ARGS:");
		
		for(int i=0;i<args.length;i++){
		
		if(args[i].contains("-v")){
			System.out.println("Verbose Mode");
			HelperClass.isVerbose=true;
		}else if(args[i].contains("-help")){
			System.out.println("use -v for Verbose mode");
			System.exit(0);
		}else if(HexToDec.validateIPAddress(args[i])) {
			System.out.println("Scan only on Switch: ["+args[i]+"]");
			HelperClass.sWorkIP=args[i];
		}else if(args[i].length()>0) {
			System.out.println("Working dir: ["+args[i]+"]");
			HelperClass.sWorkpath=args[i];
			//check if last char is a "/"
			if(HelperClass.sWorkpath.charAt(HelperClass.sWorkpath.length()-1)!='/'){
				HelperClass.sWorkpath+="/";
			}
			
		}
		
		
		}
		
		long time1=(long)System.currentTimeMillis()/1000;
		HelperClass.msgLog("SNMP:START:"+time1);
		
		new SNMPTrack();
		
		long time2=(long)System.currentTimeMillis()/1000;
		HelperClass.msgLog("SNMP:STP:"+(long)System.currentTimeMillis()/1000);
		HelperClass.msgLog("SNMP:DIF:"+(time2-time1));
		
		System.exit(0);
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