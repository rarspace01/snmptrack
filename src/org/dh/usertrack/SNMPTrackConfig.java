package org.dh.usertrack;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SNMPTrackConfig {

	private static int iThreadcount=-1;
	private static int iDebuglevel=-1;
	
	private static int iSNMPintervall=-1;
	
	public static String SNMPtrackDB_IP="";
	public static String SNMPtrackDB_DB="";
	public static String SNMPtrackDB_USR="";
	public static String SNMPtrackDB_PWD="";
	
	public static String NagiosDB_IP="";
	public static String NagiosDB_DB="";
	public static String NagiosDB_USR="";
	public static String NagiosDB_PWD="";
	
	public static final int getThreadcount(){
		
		if(iThreadcount==-1){
			loadSNMPTrackConfig();
		}
		
		return iThreadcount;
		
	}
	
	public static final int getDebuglevel() {
		if(iDebuglevel==-1){
			loadSNMPTrackConfig();
		}
		
		return iDebuglevel;
	}
	
	public static final int getSNMPIntervall() {
		if(iSNMPintervall==-1){
			loadSNMPTrackConfig();
		}
		
		return iSNMPintervall;
	}
	
	public static void loadSNMPTrackConfig() {
		// TODO Auto-generated constructor stub
		
		File file = new File(HelperClass.sWorkpath+"config.xml");
		
		if(file.exists()) //load config
		{
			
			HelperClass.msgLog("Found config.xml");	
			
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			BufferedReader br= null;
			
			try {
				fis = new FileInputStream(file);
				
				// Here BufferedInputStream is added for fast reading.
				bis = new BufferedInputStream(fis);
				br = new BufferedReader(new InputStreamReader(bis));
				
				String sLine="";
				
				// dis.available() returns 0 if the file does not have more lines.
				while ((sLine = br.readLine()) != null) {
					
					// this statement reads the line from the file and print it to
					// the console.
					
					String sTStart="<threads count=\"";
					String sTEnde1="\"/>";
					
					String sDStart="<debug level=\"";
					String sDEnde1="\"/>";
					
					String sIStart="<snmp intervall=\"";
					String sIEnde1="\"/>";
					
					String sNStart="<nagios";
					String sNEnde="\"/>";
					
					String sSTStart="<snmptrack";
					String sSTEnde="\"/>";
					
					if(sLine.contains(sNStart)&&sLine.contains(sNEnde)){
						String sXMLelement="ip";
						NagiosDB_IP=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
						sXMLelement="db";
						NagiosDB_DB=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
						sXMLelement="usr";
						NagiosDB_USR=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
						sXMLelement="pwd";
						NagiosDB_PWD=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
					}
					
					if(sLine.contains(sSTStart)&&sLine.contains(sSTEnde)){
						String sXMLelement="ip";
						SNMPtrackDB_IP=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
						sXMLelement="db";
						SNMPtrackDB_DB=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
						sXMLelement="usr";
						SNMPtrackDB_USR=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
						sXMLelement="pwd";
						SNMPtrackDB_PWD=sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length(), sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()+sLine.substring(sLine.indexOf(sXMLelement+"=\"")+(sXMLelement+"=\"").length()).indexOf("\""));
					}
					
					if(sLine.contains(sTStart)&&sLine.contains(sTEnde1)){ 
						iThreadcount=Integer.parseInt(sLine.substring(sTStart.length(), sLine.indexOf(sTEnde1)));
					}
					
					if(sLine.contains(sDStart)&&sLine.contains(sDEnde1)){ 
						iDebuglevel=Integer.parseInt(sLine.substring(sDStart.length(), sLine.indexOf(sDEnde1)));
					}
					
					if(sLine.contains(sIStart)&&sLine.contains(sIEnde1)){ 
						iSNMPintervall=Integer.parseInt(sLine.substring(sIStart.length(), sLine.indexOf(sIEnde1)));
					}
					
					
				}
				
				// dispose all the resources after using them.
				fis.close();
				bis.close();
				br.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}


	
}
