package org.dh.usertrack;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.TreeUtils;

public class BulkSNMPWalk
{
  //
  // Command line format:
  //   java SNMPWalk targetAddress targetOID
  // EX:
  //   java SNMPWalk 192.168.76.15/161 1.3.6.1.4.1.517
  //
  public static void main(String[] args)
  {
    Address targetAddress = new UdpAddress("151.10.155.1/161");
    OID targetOID         = new OID("1.3.6.1.2.1.4.22.1.2");
//    OID targetOID         = new OID("1.3.6.1.4.1.517");

    //
   
    int iReqc=1;
    
    long lstart=System.currentTimeMillis();
    
    for(int j=0; j<iReqc; j++)
    {
    

    PDU requestPDU = new PDU();
    requestPDU.add(new VariableBinding(targetOID));
    //requestPDU.setType(PDU.GETNEXT);
    requestPDU.setType(PDU.GETBULK);
    
    requestPDU.setNonRepeaters(0);
    requestPDU.setMaxRepetitions(65535);
    
    CommunityTarget target = new CommunityTarget();
    target.setCommunity(new OctetString("pdhoechst@37"));
    target.setAddress(targetAddress);
    
    target.setVersion(SnmpConstants.version2c);

    //


    try
    {
      TransportMapping transport = new DefaultUdpTransportMapping();
      Snmp snmp = new Snmp(transport);
      
      transport.listen();


     
        Vector vb = null;
        String sresponse="";

        
        
        ResponseEvent response = snmp.getBulk(requestPDU, target);
        if (response.getResponse() != null)
        {
        	//System.out.println("Received response from: "+response.getPeerAddress());
        	sresponse=response.getResponse().toString();
        	System.out.println(sresponse);
        	
        	PDU responsePDU=response.getResponse();
        	
          vb = responsePDU.getVariableBindings();
          
          //System.out.println("Size: "+vb.size());

          Iterator iter = vb.iterator();

          while(iter.hasNext()){
       	  VariableBinding v = (VariableBinding) iter.next();
       	  System.out.println(v.getOid()+"\n"+v.getVariable().toString());
          }

          
        }

        if (response.getResponse() == null)
        {
          //System.out.println("responsePDU == null");
        }
        else if (response.getResponse().getErrorStatus() != 0)
        {
          //System.out.println("responsePDU.getErrorStatus() != 0");
         // System.out.println(response.getResponse().getErrorStatusText());
        }
        
      

      snmp.close();

    }
    catch (IOException e)
    {
      System.out.println("IOException: "+e);
    }

    
    }
    
    long lstop=System.currentTimeMillis();
    
    System.out.println("Req/s ["+(48*iReqc)*1000/(lstop-lstart)+"]");
    
  }

}
