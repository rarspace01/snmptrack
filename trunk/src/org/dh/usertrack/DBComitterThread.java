package org.dh.usertrack;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBComitterThread implements Runnable{

	ArrayList<String> sSQL_List=new ArrayList<String>();
	Thread runner;
	String sName="";
	
	public DBComitterThread(String sName,ArrayList<String> ArrayListSQL) {

		this.sSQL_List=ArrayListSQL;
		
		this.sName=sName;
		
	    runner = new Thread(this, "DB Thread: "+sName);
	    runner.start(); 
	}
	
	@Override
	public void run() {

	DataManagerOracleMulti.execute(this.sName,this.sSQL_List);
	
	//HelperClass.msgLog("[DBT][FIN]["+sIP+"]");
	
	}

}
