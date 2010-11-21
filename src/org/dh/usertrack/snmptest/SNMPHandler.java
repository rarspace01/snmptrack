package org.dh.usertrack.snmptest;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPHandler {

	public static String getOID(Snmp snmp, String OID, String Target, String Community){
		String sOID="";
		
		System.out.println("DEBUG: GET OID FROM: "+Target);
		
		Address targetAdress=GenericAddress.parse("udp:"+Target+"/161");
		
		OID targetOID = new OID(OID);
		
		 PDU requestPDU = new PDU();
		    requestPDU.add(new VariableBinding(targetOID));
		    requestPDU.setType(PDU.GET);

		    
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(Community));
		    target.setAddress(targetAdress);
		    target.setVersion(SnmpConstants.version2c);


		
			try {
				
				TransportMapping transport = new DefaultUdpTransportMapping();
				snmp = new Snmp(transport);
				transport.listen();
			      
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
		
			System.out.println("DEBUG: GOT OID FROM: "+Target);
		
		return sOID;
	}
	
}
