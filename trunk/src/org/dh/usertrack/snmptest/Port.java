package org.dh.usertrack.snmptest;

public class Port {
	public String sMAC;
	public String SwitchIP;
	public int PortID;
	public int VPortID;
	public String alias;
	public int vlan;
	public long LastUpdate;
	public boolean astatus;
	public boolean cstatus;
	public String Speed;
	public String Duplex;
	public boolean isUplink;
	public String UplinkIP;
	
	public Port() {
		// TODO Auto-generated constructor stub
	}

	public String printAll() {
		// TODO Auto-generated method stub
		return sMAC+" "+
		SwitchIP+" "+
		PortID+" "+
		VPortID+" "+
		alias+" "+
		vlan+" "+
		LastUpdate+" "+
		astatus+" "+
		cstatus+" "+
		Speed+" "+
		Duplex+" "+
		isUplink+" "+
		UplinkIP+" ";
	}
	
	
	
}
