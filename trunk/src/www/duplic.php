<?php

session_start(); 

include_once("db.inc.php");
include_once("snmptrack_functions.php");

if($_SESSION['userlevel']>=2){

if(strpos($_SERVER['REQUEST_URI'],'?')!==true){
$REQURI="duplic.php?p=";
}

$puffer="";

if(strlen($_GET['sort'])>0){
$orderstring=" ORDER BY \"".getSortname($_GET['sort'])."\" ".getSortorder($_GET['sort']);	
}else{
$orderstring=" ORDER BY h.MAC ASC";	
}	

$result=db_query("SELECT h.MAC,h.IP, h.\"hostname\", h.\"Speed\", h.\"lastuser\", h.VHOST, P.\"SwitchIP\", h.\"stamptime\", p.\"name\",p.MAC AS PMAC, s.\"alias\" AS SName, s.\"alevel\" AS A FROM \"st_hosts\" h, \"st_ports\" p, \"st_switchs\" s WHERE h.\"PortMAC\"=p.MAC AND p.\"SwitchIP\"=s.\"IP\" AND h.MAC IN (SELECT MAC FROM \"USRTRACK\".\"HOSTS_LIVE\" GROUP BY MAC HAVING COUNT(*)>1)".$orderstring);


$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";


$puffer=$puffer."<table border='1'>\n";

$puffer=$puffer."<th>MAC <a href='".$REQURI."&sort=MAC_A'>▲</a><a href='".$REQURI."&sort=MAC_D'>▼</a></th>
<th>PMAC <a href='".$REQURI."&sort=PortMAC_A'>▲</a><a href='".$REQURI."&sort=PortMAC_D'>▼</a></th>
<th>Port <a href='".$REQURI."&sort=PortID_A'>▲</a><a href='".$REQURI."&sort=PortID_D'>▼</a></th>
<th>SwitchIP <a href='".$REQURI."&sort=SwitchIP_A'>▲</a><a href='".$REQURI."&sort=SwitchIP_D'>▼</a></th>
<th>IP <a href='".$REQURI."&sort=IP_A'>▲</a><a href='".$REQURI."&sort=IP_D'>▼</a></th>
<th>DNS <a href='".$REQURI."&sort=hostname_A'>▲</a><a href='".$REQURI."&sort=hostname_D'>▼</a></th>
<th>Speed [MBit/s] <a href='".$REQURI."&sort=Speed_A'>▲</a><a href='".$REQURI."&sort=Speed_D'>▼</a></th>
<th>User <a href='".$REQURI."&sort=lastuser_A'>▲</a><a href='".$REQURI."&sort=lastuser_D'>▼</a></th>
<th>Last seen <a href='".$REQURI."&sort=stamptime_A'>▲</a><a href='".$REQURI."&sort=stamptime_D'>▼</a></th>
<th>VHOST <a href='".$REQURI."&sort=VHOST_A'>▲</a><a href='".$REQURI."&sort=VHOST_D'>▼</a></th>";

while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	
	$puffer=$puffer."<tr>\n";

	$puffer=$puffer."<td><a href='show.php?pmac=".$row['PMAC']."&hmac=".$row['MAC']."&details=true'>".$row['MAC']."</a></td>";
	$puffer=$puffer."<td><a href='show.php?pmac=".$row['PMAC']."'>".$row['PMAC']."</a></td>";
	$puffer=$puffer."<td>".$row['name']."</td>";
	$puffer=$puffer."<td><a href='show.php?sip=".$row['SwitchIP']."'>".$row['SwitchIP']."</a></td>";
	$puffer=$puffer."<td>".$row['IP']."</td>";
	$puffer=$puffer."<td>".$row['hostname']."</td>";
	$puffer=$puffer."<td>".$row['Speed']."</td>";
	$puffer=$puffer."<td>".$row['lastuser']."</td>";
	$puffer=$puffer."<td>".date('H:i.s\U\h\r d.m.Y',$row['stamptime'])."</td>";
	$puffer=$puffer."<td>".$row['VHOST']."</td>";
	
	$puffer=$puffer."</tr>\n";
}
$puffer=$puffer."</table>\n";

$row_counter=oci_num_rows($result);

$additional=45*$row_counter;
$pagewidth=1400;

}else{
	$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";
	$puffer=$puffer."Keine Rechte";
}

include("header.php");
include("loginbox.php");

echo $puffer;




echo "</div>";

include ("footer.html");

?>