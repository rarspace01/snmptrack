package org.dh.usertrack;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Calendar;


public class HelperClass {
	
	public static boolean isVerbose=false;

	public static int getCPUCount(){
		
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

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
	
    // Fehlermeldung ausgeben, falls Logdatei schreiben fehlerhaft war
    public static void err (Throwable e) {
            
            String retValue = null;
            StringWriter sw = null;
            PrintWriter pw = null;
            try {
             sw = new StringWriter();
             pw = new PrintWriter(sw);
             e.printStackTrace(pw);
             retValue = sw.toString();
            } finally {
             try {
               if(pw != null)  pw.close();
               if(sw != null)  sw.close();
             } catch (IOException ignore) {}
            }
            msgLog(retValue);
            
    }
	
    // Schreibt eine Log-Datei f�r Revisionszwecke in Datei agent.log
    public static void msgLog(String sLog)
    {
    		if(isVerbose)
            System.out.println(printClock()+sLog);
            BufferedWriter out;
            try {
                    out = new BufferedWriter(new FileWriter("usertrack.log", true));
                    out.write(printClock()+sLog+"\r\n");
                    out.close();
            } catch (IOException e) {
                    e.printStackTrace();
                    err(e);
            }
    }
    
    public static void msgLog(String sFilename,String sLog)
    {
    		if(isVerbose)
            System.out.println(printClock()+sLog);
            BufferedWriter out;
            try {
                    out = new BufferedWriter(new FileWriter(sFilename, true));
                    out.write(printClock()+sLog+"\r\n");
                    out.close();
            } catch (IOException e) {
                    e.printStackTrace();
                    err(e);
            }
    }

  //liefert aktuelle Uhrzeit als String zur�ck
    public static String printClock(){
    	String sDay="",sMonth="",sYear="",sHour="",sMinute="",sSecond="";
    	
    		sDay=""+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    		sMonth=""+(Calendar.getInstance().get(Calendar.MONTH)+1);
    		sYear=""+Calendar.getInstance().get(Calendar.YEAR);    		
    		sHour=""+Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    		sMinute=""+Calendar.getInstance().get(Calendar.MINUTE);
    		sSecond=""+Calendar.getInstance().get(Calendar.SECOND);
    		
    		while(sDay.length()<2){
    			sDay="0"+sDay;
    		}
    		
    		while(sMonth.length()<2){
    			sMonth="0"+sMonth;
    		}
    		
    		while(sHour.length()<2){
    			sHour="0"+sHour;
    		}
    	
    		while(sMinute.length()<2){
    			sMinute="0"+sMinute;
    		}
    	
    		while(sSecond.length()<2){
    			sSecond="0"+sSecond;
    		}
    		
            return "["
            +sDay
            +"."+sMonth
            +"."+sYear
            +"]["+sHour
            +":"+sMinute
            +":"+sSecond+"]";
    }

    //checks if a given String is a IP
    public static boolean isValidIP(String sIP){
        String[] parts = sIP.split( "\\." );

        if ( parts.length != 4 )
        {
            return false;
        }

        for ( String s : parts )
        {
        	try{
        		
        		int i = Integer.parseInt( s );

	            if ( (i < 0) || (i > 255) )
	            {
	                return false;
	            }
        	}catch(NumberFormatException e){
        		return false;
        	}
        }

        return true;
    }
    
}
