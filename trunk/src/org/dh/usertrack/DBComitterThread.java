package org.dh.usertrack;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBComitterThread implements Runnable{

	ArrayList<String> sSQL_List=new ArrayList<String>();
	Thread runner;
	String sIP="";
	
	public DBComitterThread(String sIP, ArrayList<String> ArrayListSQL) {

		this.sSQL_List=ArrayListSQL;
		this.sIP=sIP;
		
	    runner = new Thread(this, "DB Thread: "+(int)Math.random()*1000);
	    runner.start(); 
	}
	
	@Override
	public void run() {

	DataManagerOracleMulti.execute(this.sSQL_List);
	
	//HelperClass.msgLog("[DBT][FIN]["+sIP+"]");
	
	}

}
