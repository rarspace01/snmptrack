package org.dh.usertrack.snmptest;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class JMain {

	private int ifinishedThreads=0;
	private int activeThreads=0;
	
	TransportMapping transport;
	
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

	public static void main(String[] args) {
	
			
			JMain jm1=new JMain();
		
		    
		      
			
			
			Address targetAddress=GenericAddress.parse("udp:151.10.132.226/161");


			long lstart=System.currentTimeMillis();
			   
			int iMax=50, yMax=120;
			
			for(int y=0;y<yMax;y++)
			{
			
				
				if (jm1.getActiveThreads()>=HelperClass.getCPUCount()) {
				
					while(jm1.getActiveThreads()>=HelperClass.getCPUCount()){
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				
					
				jm1.setActiveThreads(jm1.getActiveThreads()+1);		
					
				JMAINSwitchWorkerThread t=new JMAINSwitchWorkerThread("Thread Nr. "+y, jm1.snmp,jm1 ,targetAddress, iMax);
				
				
				
			}
			
			while(jm1.getIfinishedThreads()<yMax){
				try {
					Thread.sleep(1000);
					System.out.println(jm1.getIfinishedThreads());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			long lstop=System.currentTimeMillis();

			long ldiff=lstop-lstart;
			
			System.out.println("Took: "+ldiff+"ms ["+((yMax*iMax)*1000/ldiff)+"] req/s ");
		
	}
	
}


class JMAINSwitchWorkerThread implements Runnable {
	Thread runner;
	Address targetAddress;
	JMain jm;
	Snmp snmp;
	int iMax;
	
	  public JMAINSwitchWorkerThread(String threadName, Snmp snmp, JMain jm, Address adr, int iMax) {
	    this.targetAddress = adr;
	    this.jm = jm;
	    this.iMax = iMax;
	    this.snmp = snmp;
	    runner = new Thread(this, threadName);
	    runner.start(); 
	  }

	  public void run() {

//		  System.out.print("[T]");
		  
		  System.out.println("[T] Start: "+this.runner.getName());

		  
		  
		    OID targetOID         = new OID("1.3.6.1.2.1.2.1.0");
		  
		  //run

		  //int iMax=10;
		  
			for(int i=0;i<iMax;i++)
			{
			
				
		    PDU requestPDU = new PDU();
		    requestPDU.add(new VariableBinding(targetOID));
		    requestPDU.setType(PDU.GET);

		    
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString("pdhoechst"));
		    target.setAddress(targetAddress);
		    target.setVersion(SnmpConstants.version2c);


		
			try {
				
				
				TransportMapping transport = new DefaultUdpTransportMapping();
			      Snmp snmp = new Snmp(transport);
			      transport.listen();
				
				//PDU responsePDU = snmp.sendPDU(requestPDU, target);			      
				
				if(snmp == null)
				{
					System.out.println("ERR0RR");
				}
				
				snmp.send(requestPDU, target);
				
				ResponseEvent response = snmp.send(requestPDU, target);
				
				
//				System.out.println(response.getResponse().get(0));
				
				

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        
			}
		  
			jm.setIfinishedThreads(jm.getIfinishedThreads()+1);
			jm.setActiveThreads(jm.getActiveThreads()-1);
			
			
	  }
	} 
