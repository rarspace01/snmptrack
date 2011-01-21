package org.dh.usertrack;

import java.io.IOException;
import java.util.ArrayList;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Benchmark {
	ArrayList<Switch> swList=new ArrayList<Switch>();
	String sLastSwitch="";
	
	public Benchmark() {
		// TODO Auto-generated constructor stub
		
		TransportMapping transport;
		Snmp snmp = null;
		
		DefaultUdpTransportMapping dutm;
		try {
			dutm = new DefaultUdpTransportMapping();
		
		//System.out.println(dutm.getReceiveBufferSize());
		
		dutm.setReceiveBufferSize(2^24);
		
		transport = dutm;
		
		snmp = new Snmp(transport);
		transport.listen();	
		
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		SwitchListe sws=new SwitchListe();
		
		swList=sws.getSwitchList();
		
		int iTestcounter=10;
		
		int iSNMPres[] = new int[iTestcounter];
		
		Switch swc=randSwitch();
		
		for(int i=0;i<iTestcounter;i++)
		{
			iSNMPres[i]=runSNMP(swc, snmp);
			System.out.println("SNMP["+i+"]["+iSNMPres[i]+"]");
			benchpause();
		}
		
		
		
	}
	
	public static void main(String[] args) {
		
	new Benchmark();	
		
	}

	private void benchpause(){
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Switch randSwitch(){
		
		Switch swr;
		
		swr=randomswitch();
		
		sLastSwitch=swr.getsIP();
		
		return swr;
	}
	
	private Switch randomswitch(){
		Switch sw=null;
		
		int iC=0;
		double dC=0.0;
		dC=Math.random()*swList.size();	
		iC=(int)dC;		

		if(iC==swList.size()){
			iC=iC-1;
		}
		
		sw=swList.get(iC);
		
		System.out.println(sw.getsIP());
		
		if(sLastSwitch.contains(sw.getsIP())){
			sw=randomswitch();
		}
		
		return sw;
	}
	
	private int runSNMP(Switch swc, Snmp snmp) {
		// TODO Auto-generated method stub
		
		int iResult=0;
		
		int iTcount=500;
		
		long lStart=0, lStop=0;
		
		String sPuffer="";
		
		lStart=System.currentTimeMillis();

		System.out.println("Bench on: "+swc.getsIP());
		
		for(int i=0; i<iTcount; i++){
		
		sPuffer=SNMPHandler.getOID(snmp,2, OIDL.sysDescr0, swc.getsIP(), swc.readCommunity());
		}
		lStop=System.currentTimeMillis();
		
		

		
		//System.out.println("Req/s: "+);
		
		//System.out.println("GOT: ["+sPuffer+"]");
		
		iResult=(int)((iTcount*1000)/(lStop-lStart));
		return iResult;
	}
	
}
