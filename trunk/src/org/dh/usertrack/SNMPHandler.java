package org.dh.usertrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPHandler {

	public static String getOID(Snmp snmp, String OID, String Target, String Community){
		String sOID="";
		
		//System.out.println("DEBUG: GET OID FROM: "+Target);
		
		Address targetAddress=GenericAddress.parse("udp:"+Target+"/161");
		
		OID targetOID = new OID(OID);
		
		 PDU requestPDU = new PDU();
		    requestPDU.add(new VariableBinding(targetOID));
		    requestPDU.setType(PDU.GET);

		    
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(Community));
		    target.setAddress(targetAddress);
		    target.setVersion(SnmpConstants.version2c);


		
			try {
				
//				TransportMapping transport = new DefaultUdpTransportMapping();
//				snmp = new Snmp(transport);
//				transport.listen();
			      
				//PDU responsePDU = snmp.sendPDU(requestPDU, target);			      
				
				ResponseEvent response = snmp.send(requestPDU, target);
				
				
//				System.out.println(response.getResponse().get(0));
				sOID=response.getResponse().get(0).toString();
				

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				sOID="ERROR AT "+Target+" [IO]";
			} catch (NullPointerException e2){
				sOID="ERROR AT "+Target+" [null]";
			}
		
			//System.out.println("DEBUG: GOT OID FROM: "+Target);
		
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
	    requestPDU.setMaxRepetitions(65535);
	    
	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity(new OctetString(Community));
	    target.setAddress(targetAddress);
	    
	    target.setVersion(SnmpConstants.version2c);
		
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
	
	public static ArrayList<String> getOIDWalknonBluk(Snmp snmp, String OID, String Target, String Community){
		ArrayList<String> resultList=new ArrayList<String>();
		
		Address targetAddress=GenericAddress.parse("udp:"+Target+"/161");
		
		OID targetOID = new OID(OID);
		
		PDU requestPDU = new PDU();
	    requestPDU.add(new VariableBinding(targetOID));
	    requestPDU.setType(PDU.GETNEXT);
	    
	    requestPDU.setNonRepeaters(0);
	    requestPDU.setMaxRepetitions(65535);
	    
	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity(new OctetString(Community));
	    target.setAddress(targetAddress);
	    
	    target.setVersion(SnmpConstants.version2c);
		
			try {
				
//				TransportMapping transport = new DefaultUdpTransportMapping();
//				snmp = new Snmp(transport);
//				
//			      transport.listen();

				boolean finished=false;
				
				while(!finished)
				{
				
			      Vector vb = null;
			        
			        ResponseEvent response = snmp.getNext(requestPDU, target);
			        if (response.getResponse() != null)
			        {
			        	//System.out.println("Received response from: "+response.getPeerAddress());
			        	//System.out.println(sresponse);
			        	
			        	PDU responsePDU=response.getResponse();
			        	
			         
			       	  if(responsePDU.toString().contains(OID)){
			       		  //System.out.println(responsePDU.toString().contains(OID));
			       		 //System.out.println(responsePDU.get(0).toString());
			       		  resultList.add(responsePDU.get(0).toString());
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
				HelperClass.err(e2);
			}
		
		return resultList;
	}
	
}
