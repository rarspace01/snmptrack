/*
 *	Author: Denis Hamann
 *	Name: Agent Class
 *	Description: Helper Class for HTTP gets
 */

package org.dh.usertrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class HttpHelperClass {

	public static String getPage(String surl){
		
		String sPage="";
		
		URLConnection connection = null;		
		// Zerlegt einen String und fügt jeweils ein Zeilenende ein
		try {			
			URL urlpage=new URL(surl);			
			connection = urlpage.openConnection();
			connection.setDoOutput(true);			
                        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			  
			  String line = null;
			  
			  while ((line = rd.readLine()) != null) {
				  sPage+=line+System.getProperty("line.separator");
				} 		
			  
		} catch (ConnectException e) {			
			
		} catch (UnknownHostException e) {			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return sPage;
	}	
}
