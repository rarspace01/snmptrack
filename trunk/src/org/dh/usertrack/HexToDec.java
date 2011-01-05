package org.dh.usertrack;

/*
 * hextodec.java
 *
 * Created 23 Feb 2003 17:00
 */

/*
 * Copyright (C) s0gno
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

/**
 * Title:		HEX IP to DEC IP Converter
 * Description:	This simple proggy helps you figure out the
 *				DEC correspondant of an IP represented in
 *				HEX format. This is useful when trying to
 *				figure out the ip of a user connected to a
 *				Java IRC Chat such as the one on www.ircnet.com.
 *				Code is OpenSource under the terms of the GNU
 *				General Public Licence. I will not be held
 *				responsable for any appropriate use you make
 *				with this code (ie: you get an ip and then start
 *				a DDoS attack towards it etc.)
 * Copyright:	Copyright (c) 2003 s0gno - Copyright (c) 2010 denis
 * @author		s0gno - modified by denis
 * @version		0.2
 */
public class HexToDec{

	public static String getDec(String sHex){
		
		sHex=sHex.replace(":", "");
		
		String ip = "";
		
		if(sHex.length()>0){
		
		try{
			if(sHex.length() == 8){
				
				for(int i = 0;i < sHex.length();i+=2){
					ip += Integer.parseInt(sHex.substring(i, i+2),16);
					if(i!=6)
						ip += ".";
					
				}//for
			}//if
			
			
		}catch(IndexOutOfBoundsException e){
		}
		catch(NumberFormatException e){
			System.out.println("String HEX errata");	
		}
		}
		
		return ip;
	}
	
	public static String getSimpleHex(String sHex){
		
		sHex=sHex.replace("00", "0");
		sHex=sHex.replace("01", "1");
		sHex=sHex.replace("02", "2");
		sHex=sHex.replace("03", "3");
		sHex=sHex.replace("04", "4");
		sHex=sHex.replace("05", "5");
		sHex=sHex.replace("06", "6");
		sHex=sHex.replace("07", "7");
		sHex=sHex.replace("08", "8");
		sHex=sHex.replace("09", "9");
		sHex=sHex.replace("0a", "a");
		sHex=sHex.replace("0b", "b");
		sHex=sHex.replace("0c", "c");
		sHex=sHex.replace("0d", "d");
		sHex=sHex.replace("0e", "e");
		sHex=sHex.replace("0f", "f");
		
		return sHex;
	}
	
	public static String getADVfromSimple(String sHex){
		String sPuffer="", sHexPuffer="";
		
		if(sHex.length()>0){
		
		while(sHex.length()>0)
		{
			if(sHex.contains(":")){
			sHexPuffer=sHex.substring(0, sHex.indexOf(":"));
			}else{
			sHexPuffer=sHex;
			sHex="";
			}
			
			if(sHexPuffer.length()<2)
			{
				sHexPuffer="0"+sHexPuffer;
			}
			sPuffer+=sHexPuffer+":";
			sHex=sHex.substring(sHex.indexOf(":")+1);
		}
		
		sPuffer=sPuffer.substring(0,sPuffer.lastIndexOf(":"));
		}
		
		return sPuffer;
		
	}
	
	public static String getHex(String sDec)
	{
		String sPuffer="", sHexPuffer="";

		if(sDec.length()>0){
		
		sDec+=":";
		
		sDec=sDec.replace(".", ":");
		
		while(sDec.length()>0){

			
			sHexPuffer=Integer.toHexString(Integer.parseInt(sDec.substring(0, sDec.indexOf(":"))));
			
			sPuffer+=""+sHexPuffer+":";
			
			
			sDec=sDec.substring(sDec.indexOf(":")+1);
		}
		
		sPuffer=sPuffer.substring(0,sPuffer.lastIndexOf(":"));
		
		}
		
		return sPuffer;
	}
	
	public static long ipToInt (String addr) {
		long num = 0;
		
		if(addr.length()>0&&addr.contains("."))
		{
		String[] addrArray = addr.split("\\.");

	        for (int i = 0; i <addrArray.length; i++) {
	            int power=3-i;

	            num += ((Integer.parseInt (addrArray [i])%256*Math.pow (256, power)));
	        } 
		}
		
		return num;
	    }
}
