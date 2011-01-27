package org.dh.usertrack;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SwitchListe {

	private ArrayList<Switch> switchList=new ArrayList<Switch>();
	
	public SwitchListe() {
		// TODO Auto-generated constructor stub
	
		//Load Switchs from any src
	
		File file = new File(HelperClass.sWorkpath+"switchs.xml");
		
		if(file.exists())
		{
		HelperClass.msgLog("Found switch.xml");	
		
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
				
				String sStart="<switch ip=\"";
				String sEnde1="\" rc=\"";
				String sEnde2="\"/>";
				
				if(sLine.contains(sStart)&&sLine.contains(sEnde1)){ 
					addsw(sLine.substring(sLine.indexOf(sStart)+sStart.length(), sLine.indexOf(sEnde1)), sLine.substring(sLine.indexOf(sEnde1)+sEnde1.length(), sLine.indexOf(sEnde2)));
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
		}else{
		String sPuffer="";	
		HelperClass.msgLog("No switch.xml found, reading from nagios");	
		
		switchList=Nagios.getSwitchs();
		
		//System.out.println(sPuffer);
		
		try {
			Thread.sleep(9999);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
		
	}
	
	private void addsw(String sAdrr, String sReadc){
		
		switchList.add(new Switch(sAdrr, sReadc));
		
	}
	
	public ArrayList<Switch> getSwitchList() {
		return switchList;
	}
	
}
