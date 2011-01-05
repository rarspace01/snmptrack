package org.dh.usertrack;

import java.util.ArrayList;

public class SNMPConfig {

	public static final int getDebuglevel(){
		return SNMPTrackConfig.getDebuglevel();
	}
	
	public static final int getThreadmaxcount(){
		return SNMPTrackConfig.getThreadcount();
	}
	
	public static final int getISleeper(){
		return SNMPTrackConfig.getSNMPIntervall();
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
