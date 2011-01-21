package org.dh.usertrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

public class SNMPHandler {

	public static String getOID(Snmp snmp, int snmpver,  String OID, String Target, String Community){
		String sOID="";
		
		Address targetAddress=GenericAddress.parse("udp:"+Target+"/161");
		
		OID targetOID = new OID(OID);
		
		 PDU requestPDU = new PDU();
		    requestPDU.add(new VariableBinding(targetOID));
		    requestPDU.setType(PDU.GET);

		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(Community));
		    target.setAddress(targetAddress);
		    
		    if(snmpver==2){
		    target.setVersion(SnmpConstants.version2c);
		    }else if(snmpver==1){
		    target.setVersion(SnmpConstants.version1);	
		    }

		    target.setRetries(5);
		    
		    target.setTimeout(300);
		
			try {
				
				ResponseEvent response = snmp.send(requestPDU, target);
				
				sOID=response.getResponse().get(0).toString();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				sOID="ERROR AT "+Target+" [IO]";
			} catch (NullPointerException e2){
				sOID="ERROR AT "+Target+" [null]";
			}
		
		return sOID;
	}

	public static ArrayList<String> getOIDWalk(Snmp snmp, String OID, String Target, String Community){
		ArrayList<String> resultList=new ArrayList<String>();
		
		Address targetAddress=GenericAddress.parse("udp:"+Target+"/161");
		
		OID targetOID = new OID(OID);
		
		PDU requestPDU = new PDU();
	    requestPDU.add(new VariableBinding(targetOID));
	    requestPDU.setType(PDU.GETBULK);
	    
	    requestPDU.setNonRepeaters(0);
	    requestPDU.setMaxRepetitions(999);
	    
	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity(new OctetString(Community));
	    target.setAddress(targetAddress);
	    
	    target.setVersion(SnmpConstants.version2c);
	    
	    target.setRetries(5);
	    
	    target.setTimeout(300);
		
			try {
				
//				TransportMapping transport = new DefaultUdpTransportMapping();
//				snmp = new Snmp(transport);
//				
//			      transport.listen();

			      Vector vb = null;
			        
			        ResponseEvent response = snmp.getBulk(requestPDU, target);
			        if (response.getResponse() != null)
			        {
			        	//System.out.println("Received response from: "+response.getPeerAddress());
			        	//System.out.println(sresponse);
			        	
			        	PDU responsePDU=response.getResponse();
			        	
			          vb = responsePDU.getVariableBindings();
			          
			          //System.out.println("Size: "+vb.size());

			          Iterator iter = vb.iterator();

			          while(iter.hasNext()){
			       	  VariableBinding v = (VariableBinding) iter.next();
			       	  
			       	  if(v.getOid().toString().contains(OID)){
			       	  resultList.add(v.getOid().toString()+"!"+v.getVariable().toString());
			       	  }
//			       	  else{
//			       		  System.out.println("WEGWURF: "+v.getOid().toString()+"!"+v.getVariable().toString());
//			       	  }
			       	  
			       	  }
				
			        }
				
			} catch (IOException e) {
			
				HelperClass.err(e);
				
			} catch (NullPointerException e2){
				HelperClass.err(e2);
			}
		
		return resultList;
	}
	
	public static ArrayList<String> getOIDWalknonBulk(Snmp snmp, int snmpver, String OID, String Target, String Community){
		ArrayList<String> resultList=new ArrayList<String>();
		
		Address targetAddress=GenericAddress.parse("udp:"+Target+"/161");
		
		OID targetOID = new OID(OID);
		
		PDU requestPDU = new PDU();
	    requestPDU.add(new VariableBinding(targetOID));
	    requestPDU.setType(PDU.GETNEXT);
	    
	    requestPDU.setNonRepeaters(0);
	    requestPDU.setMaxRepetitions(999);
	    
	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity(new OctetString(Community));
	    target.setAddress(targetAddress);
	    
	    target.setRetries(5);
	    
	    target.setTimeout(300);
	    
	    if(snmpver==2){
		    target.setVersion(SnmpConstants.version2c);
	    }else if(snmpver==1){
		    target.setVersion(SnmpConstants.version1);	
	    }
		
			try {
				

				boolean finished=false;
				
				while(!finished)
				{
				
			      Vector vb = null;
			        
			        ResponseEvent response = snmp.getNext(requestPDU, target);
			        if (response.getResponse() != null)
			        {
			        	
			        	PDU responsePDU=response.getResponse();
			        	
			         
			       	  if(responsePDU.toString().contains(OID)){
			       		  resultList.add(responsePDU.get(0).toString().replace(" = ", "!"));
			       		  requestPDU.setRequestID(new Integer32(0));
			       		requestPDU.set(0,responsePDU.get(0));
			       	  }else{
			       		finished=true;
			       	  }
			       	  
			       	  }
				
			        }
				
			} catch (IOException e) {
			
				HelperClass.err(e);
				
			} catch (NullPointerException e2){
				
				System.out.println("Null on ["+Community+"]"+OID);
				
				HelperClass.err(e2);
			}
		
		return resultList;
	}
	
	public static ArrayList<String> getOIDWalknonBulkSlow(Snmp snmp, int snmpver, String OID, String Target, String Community){
		ArrayList<String> resultList=new ArrayList<String>();
		
		Address targetAddress=GenericAddress.parse("udp:"+Target+"/161");
		
		OID targetOID = new OID(OID);
		
		PDU requestPDU = new PDU();
	    requestPDU.add(new VariableBinding(targetOID));
	    requestPDU.setType(PDU.GETNEXT);
	    
	    requestPDU.setNonRepeaters(0);
	    requestPDU.setMaxRepetitions(999);
	    
	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity(new OctetString(Community));
	    target.setAddress(targetAddress);
	    
	    target.setRetries(5);
	    
	    target.setTimeout(300);
	    
	    if(snmpver==2){
		    target.setVersion(SnmpConstants.version2c);
	    }else if(snmpver==1){
		    target.setVersion(SnmpConstants.version1);	
	    }
		
			try {
				

				boolean finished=false;
				
				while(!finished)
				{
				
			      Vector vb = null;
			        
			        ResponseEvent response = snmp.getNext(requestPDU, target);
			        if (response.getResponse() != null)
			        {
			        	
			        	PDU responsePDU=response.getResponse();
			        	
			         
			       	  if(responsePDU.toString().contains(OID)){
			       		  resultList.add(responsePDU.get(0).toString().replace(" = ", "!"));
			       		  requestPDU.setRequestID(new Integer32(0));
			       		requestPDU.set(0,responsePDU.get(0));
			       	  }else{
			       		finished=true;
			       	  }
			       	  
			       	  }
				
			        Thread.sleep(SNMPConfig.getISleeper());
			        
			       }
				
			} catch (IOException e) {
			
				HelperClass.err(e);
				
			} catch (NullPointerException e2){
				
				System.out.println("Null on ["+Community+"]"+OID);
				
				HelperClass.err(e2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return resultList;
	}
	
}
