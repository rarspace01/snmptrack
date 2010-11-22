package org.dh.usertrack.snmptest;

import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;

public class SNMPTrack {

	private int ifinishedThreads=0;
	private int activeThreads=0;
	

	
	Snmp snmp;
	
	
	
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
		// TODO Auto-generated constructor stub
		
		
		HelperClass.msgLog("Starte SNMPTrack");
		
		HelperClass.msgLog("Lade aktuelle Switchliste");
		
		ArrayList<Switch> swList=new ArrayList<Switch>();
		
		SwitchListe sws=new SwitchListe();
		
		swList=sws.getSwitchList();
		
		HelperClass.msgLog("Beginne mit Auslese Prozess.");
		
		for(int i=0; i<swList.size();i++)
		{
			if(getActiveThreads()>=SNMPConfig.getThreadmaxcount())
			{
				while(getActiveThreads()>=SNMPConfig.getThreadmaxcount()){
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			setActiveThreads(getActiveThreads()+1);	
			
			//HelperClass.msgLog("[Scan][Switch]["+i+"][Start]");
			SwitchWorkerThread t=new SwitchWorkerThread("Thread Switch Nr. "+i, this.snmp,this, swList.get(i).getsIP());
		}
		
	}
	
	public static void main(String[] args) {
	
		new SNMPTrack();
		
		
	}
	
	class SwitchWorkerThread implements Runnable {
		Thread runner;
		Address targetAddress;
		String sIP;
		SNMPTrack jm;
		Snmp snmp;
		TransportMapping transport;
		int iMax;
		
		  public SwitchWorkerThread(String threadName, Snmp snmp, SNMPTrack jm, String SwitchAdresse) {
		    //this.targetAddress = GenericAddress.parse("udp:"+SwitchAdresse+"/161");
		    
			sIP=SwitchAdresse;  
			  
			this.jm = jm;
		    this.snmp = snmp;
		    runner = new Thread(this, threadName);
		    runner.start(); 
		  }

		  public void run() {

			Switch workerSwitch=new Switch(sIP, this.snmp);
			
			workerSwitch.refresh();
				
			threadMGTfinish();	
		  }
		  
		  private void threadMGTfinish(){
				jm.setIfinishedThreads(jm.getIfinishedThreads()+1);
				jm.setActiveThreads(jm.getActiveThreads()-1);  
		  }
	}
}
