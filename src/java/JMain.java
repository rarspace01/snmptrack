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

public class JMain {

	
	public static void main(String[] args) {
	
			
			 Address targetAddress=GenericAddress.parse("udp:151.10.132.226/161");
			    OID targetOID         = new OID("1.3.6.1.2.1.2.1.0");

			long lstart=System.currentTimeMillis();
			   
			int iMax=10, yMax=120;
			
			PDU requestPDU = new PDU();
			    
			    CommunityTarget target = new CommunityTarget();
			    target.setCommunity(new OctetString("pdhoechst"));
			    target.setAddress(targetAddress);
			    target.setVersion(SnmpConstants.version1);
			    requestPDU.setType(PDU.GET);
			      TransportMapping transport = new DefaultUdpTransportMapping();
			      Snmp snmp = new Snmp(transport);
			      transport.listen();

			try {

				for(int y=0;y<yMax;y++)
				{
				
				for(int i=0;i<iMax;i++)
				{
				
			    
			    requestPDU.add(new VariableBinding(targetOID));


			
			      
			        //PDU responsePDU = snmp.sendPDU(requestPDU, target);			      
			      
			      ResponseEvent response = snmp.send(requestPDU, target);
			      
			       // System.out.println(response.getResponse().get(0));
			        
				}
				
				}
				
				long lstop=System.currentTimeMillis();
				
				System.out.println("Took: "+(lstop-lstart)+"Req/s: "+(yMax*iMax)*1000/(lstop-lstart));
				
			      
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		
	}
	
}
