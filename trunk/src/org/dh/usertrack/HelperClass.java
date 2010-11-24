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
	
    // Schreibt eine Log-Datei für Revisionszwecke in Datei agent.log
    public static void msgLog(String sLog)
    {
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

  //liefert aktuelle Uhrzeit als String zurück
    public static String printClock(){
            return "["+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"."+(Calendar.getInstance().get(Calendar.MONTH)+1)+"."+Calendar.getInstance().get(Calendar.YEAR)+"]["+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":"+Calendar.getInstance().get(Calendar.MINUTE)+":"+Calendar.getInstance().get(Calendar.SECOND)+"]";
    }

}
