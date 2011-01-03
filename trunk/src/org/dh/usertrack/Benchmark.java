package org.dh.usertrack;

import java.io.IOException;
import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Benchmark {

	public Benchmark() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		int iTestcounter=3;
		
		int iSNMPres[] = new int[iTestcounter];
		
		Benchmark b=new Benchmark();
		
		for(int i=0;i<iTestcounter;i++)
		{
		iSNMPres[i]=b.runSNMP();
		System.out.println("SNMP["+i+"]["+iSNMPres[i]+"]");
		}
		
	}

	private int runSNMP() {
		// TODO Auto-generated method stub
		
		int iResult=0;
		
		int iTcount=1000;
		
		long lStart=0, lStop=0;
		
		String sPuffer="";
		
		TransportMapping transport;
		Snmp snmp;
		
		DefaultUdpTransportMapping dutm;
		try {
			dutm = new DefaultUdpTransportMapping();
		
		//System.out.println(dutm.getReceiveBufferSize());
		
		dutm.setReceiveBufferSize(2^24);
		
		transport = dutm;
		
		snmp = new Snmp(transport);
		transport.listen();
		
		lStart=System.currentTimeMillis();
		
		for(int i=0; i<iTcount; i++){
		
		sPuffer=SNMPHandler.getOID(snmp, OIDL.sysDescr0, "151.10.132.229", "pdhoechst");
		}
		
		lStop=System.currentTimeMillis();
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("Req/s: "+);
		
		//System.out.println("GOT: ["+sPuffer+"]");
		
		iResult=(int)((iTcount*1000)/(lStop-lStart));
		return iResult;
	}
	
}
