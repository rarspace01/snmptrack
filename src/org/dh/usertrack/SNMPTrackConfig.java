package org.dh.usertrack;

import java.io.File;

public class SNMPTrackConfig {

	private String SNMPtrackDB_IP="";
	private String SNMPtrackDB_DB="";
	private String SNMPtrackDB_USR="";
	private String SNMPtrackDB_PWD="";
	private String SNMPtrackDB_TBL="";
	
	private String NagiosDB_IP="";
	private String NagiosDB_DB="";
	private String NagiosDB_USR="";
	private String NagiosDB_PWD="";
	private String NagiosDB_TBL="";	
	
	public SNMPTrackConfig() {
		// TODO Auto-generated constructor stub
		
		File file = new File("config.xml");
		
		if(file.exists()) //load config
		{
		}
		
	}
	
}
