package org.dh.usertrack;

import java.util.ArrayList;

public class SNMPConfig {

	public static final int getDebuglevel(){
		return 0;
	}
	
	public static final int getThreadmaxcount(){
		
		//return 24;
		return 10;
		
		//return HelperClass.getCPUCount()*2;
		
	}
	
	public static final String getReadCommunity(){
		
		return "pdhoechst";
		
	}
	
	public static final String getRouter(){
		return "151.10.132.2";
	}
	
	public static final ArrayList<String> getRouters(){
		ArrayList<String> lRouter=new ArrayList<String>();

		lRouter.add("151.10.132.1");
		lRouter.add("151.10.132.2");
		lRouter.add("151.10.132.3");
		lRouter.add("151.10.132.4");
		lRouter.add("151.10.132.5");
		
		
		return lRouter;
	}
	
}
