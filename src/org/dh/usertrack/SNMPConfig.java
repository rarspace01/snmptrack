package org.dh.usertrack;

public class SNMPConfig {

	public static final int getThreadmaxcount(){
		
		return 32;
		
		//return HelperClass.getCPUCount()*2;
		
	}
	
	public static final String getReadCommunity(){
		
		return "pdhoechst";
		
	}
	
	public static final String getRouter(){
		return "151.10.132.2";
	}
	
}
