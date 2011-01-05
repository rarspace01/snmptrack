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
	
	public static final int getThreadcount(){
		
		if(iThreadcount==-1){
			loadSNMPTrackConfig();
		}
		
		return iThreadcount;
		
	}
	
	public static void loadSNMPTrackConfig() {
		// TODO Auto-generated constructor stub
		
		File file = new File("config.xml");
		
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
					
					if(sLine.contains(sTStart)&&sLine.contains(sTEnde1)){ 

						iThreadcount=Integer.parseInt(sLine.substring(sTStart.length(), sLine.indexOf(sTEnde1)));
						
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
