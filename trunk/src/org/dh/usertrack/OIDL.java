package org.dh.usertrack;

public class OIDL {

	//IP-MIB RFC1213
	public static final String ipNetToMediaPhysAddress="1.3.6.1.2.1.4.22.1.2";
	public static final String sysDescr0="1.3.6.1.2.1.1.1.0";
	public static final String sysLocation0="1.3.6.1.2.1.1.6.0";
	public static final String sysUpTimeInstance="1.3.6.1.2.1.1.3.0";
	public static final String sysName0="1.3.6.1.2.1.1.5.0";
	public static final String ifPhysAddress="1.3.6.1.2.1.2.2.1.6";
	public static final String ifOperStatus="1.3.6.1.2.1.2.2.1.8";
	public static final String ifName="1.3.6.1.2.1.31.1.1.1.1";
	public static final String ifAlias="1.3.6.1.2.1.31.1.1.1.18";
	public static final String ifSpeed="1.3.6.1.2.1.2.2.1.5";
	public static final String ifType="1.3.6.1.2.1.2.2.1.3";
	
	
	//BRIDGE-MIB RFC1286
	public static final String dot1dBasePortIfIndex="1.3.6.1.2.1.17.1.4.1.2";
	public static final String dot1dTpFdbPort="1.3.6.1.2.1.17.4.3.1.2";
	
	
	//CISCO-VLAN-MEMBERSHIP-MIB - http://www.oidview.com/mibs/9/CISCO-VLAN-MEMBERSHIP-MIB.html
	public static final String vmVlan="1.3.6.1.4.1.9.9.68.1.2.2.1.2";
	
	//CISCO-VTP-MIB - http://www.oidview.com/mibs/9/CISCO-VTP-MIB.html
	public static final String vtpVlanState="1.3.6.1.4.1.9.9.46.1.3.1.1.2"; 
	public static final String vtpVlanType="1.3.6.1.4.1.9.9.46.1.3.1.1.3"; 
	public static final String vtpVlanName="1.3.6.1.4.1.9.9.46.1.3.1.1.4"; 

	//CISCO-CDP-MIB - http://www.oidview.com/mibs/9/CISCO-CDP-MIB.html
	public static final String cdpCacheAddress="1.3.6.1.4.1.9.9.23.1.2.1.1.4";
	public static final String cdpCacheDeviceId="1.3.6.1.4.1.9.9.23.1.2.1.1.6";
	public static final String cdpCacheDevicePort="1.3.6.1.4.1.9.9.23.1.2.1.1.7";
	public static final String cdpCacheCapabilities="1.3.6.1.4.1.9.9.23.1.2.1.1.9";
	
	//CISCO-STACK-MIB - http://www.oidview.com/mibs/9/CISCO-STACK-MIB.html
	public static final String portType="1.3.6.1.4.1.9.5.1.4.1.1.5";
	
	//OLD-CISCO-INTERFACES-MIB - http://www.oidview.com/mibs/9/OLD-CISCO-INTERFACES-MIB.html
	public static final String locIfspanInPkts="1.3.6.1.4.1.9.2.2.1.1.102";
	
	//OLD-CISCO-CHASSIS-MIB
	public static final String chassisId="1.3.6.1.4.1.9.3.6.3";
	
	//CISCO-C2900-MIB - http://www.oidview.com/mibs/9/CISCO-C2900-MIB.html
	public static final String c2900PortIfIndex="1.3.6.1.4.1.9.9.87.1.4.1.1.25";
	public static final String c2900PortDuplexStatus="1.3.6.1.4.1.9.9.87.1.4.1.1.32";
}
