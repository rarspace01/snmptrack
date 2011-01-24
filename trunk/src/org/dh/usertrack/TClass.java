package org.dh.usertrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TClass {

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
	
	public TClass(String sIP) {
		
		if(sIP.length()>0){
			
		}else{
			sIP="151.10.144.169";
		}
		
		HelperClass.msgLog("catcher.log","Search for: ["+sIP+"]");
		
		ArrayList<String> sPufferList=new ArrayList<String>();

		HelperClass.msgLog("catcher.log","Starte SNMPTrack");
		
		HelperClass.msgLog("catcher.log","Initialisiere SNMP");
		
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
		
		while(true){
		
		
			HelperClass.msgLog("catcher.log","Clear ARP Cache");
			
			swHostMacIps.clear();
			
			HelperClass.msgLog("catcher.log","Lade ARP Cache");
	
			for(int i=0; i<SNMPConfig.getRouters().size();i++){
				
				HelperClass.msgLog("catcher.log","Lade ARP Cache von "+SNMPConfig.getRouters().get(i).substring(0,SNMPConfig.getRouters().get(i).indexOf("!")));
				
				sPufferList=SNMPHandler.getOIDWalknonBulk(snmp, 2, OIDL.ipNetToMediaPhysAddress, SNMPConfig.getRouters().get(i).substring(0,SNMPConfig.getRouters().get(i).indexOf("!")), SNMPConfig.getRouters().get(i).substring(SNMPConfig.getRouters().get(i).indexOf("!")+1));
				
				for (int j=0; j<sPufferList.size();j++){
					
					if(!swHostMacIps.contains(sPufferList.get(j))){
						
						swHostMacIps.add(sPufferList.get(j));
						
					}
					
				}
				
			}
			
			
			HelperClass.msgLog("catcher.log","Gefundene ARP Einträge: "+swHostMacIps.size());
			
			for(int i=0;i<swHostMacIps.size();i++){
				
				HelperClass.msgLog("catcher.log",swHostMacIps.get(i));
				
				if(swHostMacIps.get(i).contains(sIP)){
				
					HelperClass.msgLog("catcher.log","FOUND ARP: "+swHostMacIps.get(i));
					
					
					//start
					
					
					ProcessBuilder pb = new ProcessBuilder("java", "-Xmx1024m",
							"-Xms1024m",
							"-jar",
							"snmptrack.jar");
	
					Map<String, String> env = pb.environment();
	
					pb.redirectErrorStream( true );
	
					try {
	
						Process p = pb.start();
						Process sister = pb.start();
	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				
			}
		
			
		try {
			
			Thread.sleep(3*60*1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		}
		
		
	}
	


	public static void main(String[] args) {
	
		String sPuffer="";
		
		HelperClass.msgLog("catcher.log","LOC: ["+HelperClass.class.getProtectionDomain().getCodeSource().getLocation().getFile()+"]");
		
		HelperClass.msgLog("catcher.log","ARGS:");
		
		for(int i=0;i<args.length;i++){
		HelperClass.msgLog("catcher.log",args[i]);	
		}
		
		if(args.length>0){
			sPuffer=args[0];
		}
		
		new TClass(sPuffer);

	}
}