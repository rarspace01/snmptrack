package org.dh.usertrack.snmptest;

public class SNMPConfig {

	public static final int getThreadmaxcount(){
		
		return 2;
		
		//return HelperClass.getCPUCount()*2;
		
	}
	
	public static final String getReadCommunity(){
		
		return "pdhoechst";
		
	}
	
}
