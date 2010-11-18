package org.dh.usertrack.snmptest;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;


public class HelperClass {

	public static int getCPUCount(){
		
		OperatingSystemMXBean osBean = ManagementFactory
		.getOperatingSystemMXBean();

		int numOfProcessors = osBean.getAvailableProcessors();
		
		return numOfProcessors;
		
	}
	
	public static void sleeping(int i)
	{
		try {
			Thread.sleep(i*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
