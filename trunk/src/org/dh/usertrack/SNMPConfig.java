package org.dh.usertrack;

import java.util.ArrayList;

public class SNMPConfig {

	public static final int getDebuglevel(){
		SNMPTrackConfig.getDebuglevel();
		return 1;
	}
	
	public static final int getThreadmaxcount(){
		
		//return 24;
		return SNMPTrackConfig.getThreadcount();
		//return HelperClass.getCPUCount()*2;
		
	}
	
	public static final int getISleeper(){
		
		return 100;
		
	}
	
	public static final String getReadCommunity(){
		
		return "pdhoechst";
		
	}
	
	public static final ArrayList<String> getRouters(){
		ArrayList<String> lRouter=new ArrayList<String>();

		lRouter.add("151.10.132.2!pdhoechst");
		//lRouter.add("151.10.97.65!pirvienna");
		lRouter.add("151.10.143.1!pdhoechst");
		lRouter.add("10.100.1.1!pdhoechst");
		lRouter.add("151.10.231.1!dcsmerzig");
		
		return lRouter;
	}
	
}
