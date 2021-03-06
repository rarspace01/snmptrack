<?php
session_start(); 

include_once("db.inc.php");
include_once("snmptrack_functions.php");


$puffer="";

if(strlen($_GET[hmac])>0){

if(strlen($_GET['sort'])>0){
$orderstring=" ORDER BY \"".getSortname($_GET['sort'])."\" ".getSortorder($_GET['sort']);	
}else{
$orderstring=" ORDER BY H.\"hostname\" ASC";	
}		
	
$sSQL="SELECT H.\"PortMAC\",  S.\"alias\" AS salias, H.CDPID,H.CDPIP,H.CDPPORT,H.CDPTYP, H.MAC,H.\"Duplex\" AS \"HDuplex\", H.\"stamptime\" , H.IP, H.\"hostname\", H.\"Speed\", H.\"lastuser\", H.VHOST, P.\"name\", P.\"SwitchIP\" FROM USRTRACK.HOSTS_LIVE H, USRTRACK.PORTS_LIVE P, USRTRACK.SWITCHS_LIVE S  WHERE S.IP=P.\"SwitchIP\" AND H.\"PortMAC\"=P.MAC AND \"PortMAC\" LIKE '%".$_GET[pmac]."%' AND H.MAC='".$_GET[hmac]."'".$orderstring;
	
$result=db_query($sSQL);

$row_counter=oci_num_rows($result);

$pagewidth=1450;

include("header.php");
include("loginbox.php");
?>
<div id="content" style="height: auto">
<?php

echo "<table border='1'>\n";

echo "
<th>MAC <a href='".$_SERVER['REQUEST_URI']."&sort=MAC_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=MAC_D'>▼</a></th>
<th>PMAC <a href='".$_SERVER['REQUEST_URI']."&sort=PortMAC_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=PortMAC_D'>▼</a></th>
<th>Port <a href='".$_SERVER['REQUEST_URI']."&sort=PortID_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=PortID_D'>▼</a></th>
<th>SwitchIP <a href='".$_SERVER['REQUEST_URI']."&sort=SwitchIP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=SwitchIP_D'>▼</a></th>
<th>SwitchAlias <a href='".$_SERVER['REQUEST_URI']."&sort=SALIAS_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=SALIAS_D'>▼</a></th>
<th>IP <a href='".$_SERVER['REQUEST_URI']."&sort=LIP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=LIP_D'>▼</a></th>
<th>DNS <a href='".$_SERVER['REQUEST_URI']."&sort=hostname_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=hostname_D'>▼</a></th>
<th>[MBit/s] <a href='".$_SERVER['REQUEST_URI']."&sort=Speed_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=Speed_D'>▼</a></th>
<th>Duplex <a href='".$_SERVER['REQUEST_URI']."&sort=HDuplex_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=HDuplex_D'>▼</a></th>
<th>User <a href='".$_SERVER['REQUEST_URI']."&sort=lastuser_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=lastuser_D'>▼</a></th>
<th>Last seen <a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_D'>▼</a></th>
<th>VHOST <a href='".$_SERVER['REQUEST_URI']."&sort=VHOST_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=VHOST_D'>▼</a></th>";
if(strlen($_GET[details])>0){
echo "<th>CDPID <a href='".$_SERVER['REQUEST_URI']."&sort=CDPID_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=CDPID_D'>▼</a></th>";
echo "<th>CDPIP <a href='".$_SERVER['REQUEST_URI']."&sort=CDPIP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=CDPIP_D'>▼</a></th>";
echo "<th>CDP-Port <a href='".$_SERVER['REQUEST_URI']."&sort=CDPPORT_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=CDPPORT_D'>▼</a></th>";
echo "<th>CDP-Typ <a href='".$_SERVER['REQUEST_URI']."&sort=CDPTYP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=CDPTYP_D'>▼</a></th>";
}


while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {

    echo "<tr>\n";

	echo "<td><a href='show.php?pmac=".$row['PortMAC']."&hmac=".$row['MAC']."&details=true'>".$row['MAC']."</a></td>";
	echo "<td><a href='show.php?pmac=".$row['PortMAC']."'>".$row['PortMAC']."</a></td>";
	echo "<td>".$row['name']."</td>";
	echo "<td><a href='show.php?sip=".$row['SwitchIP']."'>".$row['SwitchIP']."</a></td>";
	echo "<td>".$row['SALIAS']."</td>";
	echo "<td>".$row['IP']."</td>";
	echo "<td>".$row['hostname']."</td>";
	echo "<td>".$row['Speed']."</td>";
	echo "<td>".$row['HDuplex']."</td>";
	echo "<td>".$row['lastuser']."</td>";
	echo "<td>".date('H:i.s\U\h\r d.m.Y',$row['stamptime'])."</td>";
	echo "<td>".$row['VHOST']."</td>";

	if(strlen($_GET[details])>0){
	echo "<td>".$row['CDPID']."</td>";
	echo "<td>".$row['CDPIP']."</td>";
	echo "<td>".$row['CDPPORT']."</td>";
	echo "<td>".$row['CDPTYP']."</td>";
	}
	
    echo "</tr>\n";
}
echo "</table>\n";

}else if(strlen($_GET[pmac])>0){

if(strlen($_GET['sort'])>0){
$orderstring=" ORDER BY \"".getSortname($_GET['sort'])."\" ".getSortorder($_GET['sort']);	
}else{
$orderstring=" ORDER BY H.\"hostname\" ASC";	
}	
	
if(strlen($_GET[q])>0){
$sSQL="SELECT H.\"PortMAC\", S.\"alias\" AS salias, H.MAC, H.IP, H.\"stamptime\", H.\"hostname\", H.\"Speed\", H.\"lastuser\", H.VHOST, P.\"name\", P.\"SwitchIP\" FROM USRTRACK.HOSTS_LIVE H, USRTRACK.PORTS_LIVE P, USRTRACK.SWITCHS_LIVE S WHERE S.IP=P.\"SwitchIP\" AND H.\"PortMAC\"=P.MAC AND (H.\"IP\" LIKE '%".$_GET[q]."' OR LOWER(H.MAC) LIKE LOWER('%".$_GET[q]."%') OR LOWER(H.\"PortMAC\") LIKE LOWER('%".$_GET[q]."%') OR P.\"SwitchIP\" LIKE LOWER('".$_GET[q]."') OR LOWER(H.\"hostname\") LIKE LOWER('%".$_GET[q]."%') OR LOWER(H.\"lastuser\") LIKE LOWER('%".$_GET[q]."%'))".$orderstring;
}else{
$sSQL="SELECT H.\"PortMAC\", S.\"alias\" AS salias, H.MAC, H.IP, H.\"stamptime\", H.\"hostname\", H.\"Speed\", H.\"lastuser\", H.VHOST, P.\"name\", P.\"SwitchIP\" FROM USRTRACK.HOSTS_LIVE H, USRTRACK.PORTS_LIVE P, USRTRACK.SWITCHS_LIVE S WHERE S.IP=P.\"SwitchIP\" AND H.\"PortMAC\"=P.MAC AND \"PortMAC\"='".$_GET[pmac]."'".$orderstring;
}

//echo $sSQL;

$result=db_query($sSQL);




$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";


$puffer=$puffer."<table border='1'>\n";
$puffer=$puffer."
<th>MAC <a href='".$_SERVER['REQUEST_URI']."&sort=MAC_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=MAC_D'>▼</a></th>
<th>PMAC <a href='".$_SERVER['REQUEST_URI']."&sort=PortMAC_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=PortMAC_D'>▼</a></th>
<th>Port <a href='".$_SERVER['REQUEST_URI']."&sort=PortID_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=PortID_D'>▼</a></th>
<th>SwitchIP <a href='".$_SERVER['REQUEST_URI']."&sort=SwitchIP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=SwitchIP_D'>▼</a></th>
<th>SwitchAlias <a href='".$_SERVER['REQUEST_URI']."&sort=SALIAS_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=SALIAS_D'>▼</a></th>
<th>IP <a href='".$_SERVER['REQUEST_URI']."&sort=LIP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=LIP_D'>▼</a></th>
<th>DNS <a href='".$_SERVER['REQUEST_URI']."&sort=hostname_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=hostname_D'>▼</a></th>
<th>[MBit/s] <a href='".$_SERVER['REQUEST_URI']."&sort=Speed_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=Speed_D'>▼</a></th>
<th>User <a href='".$_SERVER['REQUEST_URI']."&sort=lastuser_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=lastuser_D'>▼</a></th>
<th>Last seen <a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_D'>▼</a></th>
<th>VHOST <a href='".$_SERVER['REQUEST_URI']."&sort=VHOST_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=VHOST_D'>▼</a></th>";

$tstart=time();

while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {

    $puffer=$puffer."<tr>\n";

	$puffer=$puffer."<td><a href='show.php?pmac=".$row['PortMAC']."&hmac=".$row['MAC']."&details=true'>".$row['MAC']."</a></td>";
	$puffer=$puffer."<td><a href='show.php?pmac=".$row['PortMAC']."'>".$row['PortMAC']."</a></td>";
	$puffer=$puffer."<td>".$row['name']."</td>";
	$puffer=$puffer."<td><a href='show.php?sip=".$row['SwitchIP']."'>".$row['SwitchIP']."</a></td>";
	$puffer=$puffer."<td>".$row['SALIAS']."</td>";
	$puffer=$puffer."<td>".$row['IP']."</td>";
	$puffer=$puffer."<td>".$row['hostname']."</td>";
	$puffer=$puffer."<td>".$row['Speed']."</td>";
	$puffer=$puffer."<td>".$row['lastuser']."</td>";
	$puffer=$puffer."<td>".date('H:i.s\U\h\r d.m.Y',$row['stamptime'])."</td>";
	$puffer=$puffer."<td>".$row['VHOST']."</td>";

    $puffer=$puffer."</tr>\n";
}

$tende=time();

$puffer=$puffer."</table>\n";

//$puffer=$puffer."Took ".$tende-$tstart."s";

$row_counter=oci_num_rows($result);

$additional=40*$row_counter;

$pagewidth=1400;


include("header.php");
include("loginbox.php");

echo $puffer;


}else if(strlen($_GET[sip])>0&&$_SESSION['userlevel']>=2){

	
if(strlen($_GET['sort'])>0){
$orderstring=" ORDER BY \"".getSortname($_GET['sort'])."\" ".getSortorder($_GET['sort']);	
}else{
$orderstring=" ORDER BY \"PortID\" ASC";	
}		
	
	
$result=db_query("SELECT * FROM USRTRACK.PORTS_LIVE WHERE \"SwitchIP\" LIKE '".$_GET[sip]."'".$orderstring);

$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";


$puffer=$puffer."<table border='1'>\n";

$puffer=$puffer."<th>MAC <a href='".$_SERVER['REQUEST_URI']."&sort=MAC_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=MAC_D'>▼</a></th>
<th>SwitchIP <a href='".$_SERVER['REQUEST_URI']."&sort=SwitchIP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=SwitchIP_D'>▼</a></th>
<th>VLAN <a href='".$_SERVER['REQUEST_URI']."&sort=vlan_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=vlan_D'>▼</a></th>
<th>Port <a href='".$_SERVER['REQUEST_URI']."&sort=PortID_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=PortID_D'>▼</a></th>
<th>Link <a href='".$_SERVER['REQUEST_URI']."&sort=cstatus_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=cstatus_D'>▼</a></th>
<th>Uplink <a href='".$_SERVER['REQUEST_URI']."&sort=isUplink_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=isUplink_D'>▼</a></th>
<th>[MBit/s] <a href='".$_SERVER['REQUEST_URI']."&sort=Speed_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=Speed_D'>▼</a></th>
<th>Duplex <a href='".$_SERVER['REQUEST_URI']."&sort=Duplex_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=Duplex_D'>▼</a></th>
<th>Alias <a href='".$_SERVER['REQUEST_URI']."&sort=alias_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=alias_D'>▼</a></th>
<th>Last seen <a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_D'>▼</a></th>";

while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	//var_dump($row);
	
	
    $puffer=$puffer."<tr>\n";
	
	
	$puffer=$puffer."<td><a href='show.php?pmac=".$row['MAC']."'>".$row['MAC']."</a> </td>";
	$puffer=$puffer."<td>".$row['SwitchIP']."</td>";
	$puffer=$puffer."<td>".$row['vlan']."</td>";
	$puffer=$puffer."<td>".$row['name']."</td>";
	
	if($row['cstatus']==='true'){
	$puffer=$puffer."<td>"."UP"."</td>";
	}else{
	$puffer=$puffer."<td>"."DOWN"."</td>";
	}
	
	if($row['isUplink']==='true'){
	$puffer=$puffer."<td>"."J"."</td>";
	}else{
	$puffer=$puffer."<td>"."N"."</td>";
	}
	$puffer=$puffer."<td>".$row['Speed']."</td>";
	$puffer=$puffer."<td>".$row['Duplex']."</td>";
	$puffer=$puffer."<td>".$row['alias']."</td>";
	$puffer=$puffer."<td>".date('H:i.s\U\h\r d.m.Y',$row['stamptime'])."</td>";
	
    // foreach ($row as $item) {
        // echo "    <td>" . ($item !== null ? htmlentities($item, ENT_QUOTES) : "&nbsp;") . "</td>\n";
    // }
	
	
    $puffer=$puffer."</tr>\n";
}
$puffer=$puffer."</table>\n";

$row_counter=oci_num_rows($result);

$additional=20*$row_counter;

include("header.php");
include("loginbox.php");

echo $puffer;

}else if($_SESSION['userlevel']>=2){

if(strpos($_SERVER['REQUEST_URI'],'?')!==true){
$REQURI="show.php?p=";
}
	
//generate order by string
if(strlen($_GET['sort'])>0){
$orderstring=" ORDER BY \"".getSortname($_GET['sort'])."\" ".getSortorder($_GET['sort']);	
}else{
$orderstring=" ORDER BY LIP ASC";	
}	
	
if(strlen($_GET[q])>0){
$result=db_query("SELECT * FROM USRTRACK.SWITCHS_LIVE WHERE IP LIKE '%".$_GET[q]."%' OR \"hostname\" LIKE '%".$_GET[q]."%'".$orderstring);
}else{
$result=db_query("SELECT * FROM USRTRACK.SWITCHS_LIVE".$orderstring);
}




$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";


$puffer=$puffer."<table border='1'>\n";

$puffer=$puffer."<th>IP <a href='".$REQURI."&sort=LIP_A'>▲</a><a href='".$REQURI."&sort=LIP_D'>▼</a></th>
<th>DNS <a href='".$REQURI."&sort=hostname_A'>▲</a><a href='".$REQURI."&sort=hostname_D'>▼</a></th>
<th>Hosts</th>
<th>Modell <a href='".$REQURI."&sort=model_A'>▲</a><a href='".$REQURI."&sort=model_D'>▼</a></th>
<th>OS Version <a href='".$REQURI."&sort=osversion_A'>▲</a><a href='".$REQURI."&sort=osversion_D'>▼</a></th>
<th>Alias <a href='".$REQURI."&sort=alias_A'>▲</a><a href='".$REQURI."&sort=alias_D'>▼</a></th>
<th>Ort <a href='".$REQURI."&sort=location_A'>▲</a><a href='".$REQURI."&sort=location_D'>▼</a></th>
<th>Serial <a href='".$REQURI."&sort=SERIAL_A'>▲</a><a href='".$REQURI."&sort=SERIAL_D'>▼</a></th>
<th>Last seen <a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=stamptime_D'>▼</a></th>";


while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	//var_dump($row);
	
	$puffer=$puffer."<tr>\n"."<td><a href='show.php?sip=".$row['IP']."'>".$row['IP']."</a></td>".
	"<td>".$row['hostname']."</td>".
	"<td><a href='show.php?pmac=?&q=".$row['IP']."'>Show</td>".
	"<td>".$row['model']."</td>"."<td>".$row['osversion']."</td>".
	"<td>".$row['alias']."</td>".
	"<td>".$row['location']."</td>"."<td>".$row['SERIAL']."</td>".
	"<td>".date('H:i.s\U\h\r d.m.Y',$row['stamptime'])."</td>";
	
    // foreach ($row as $item) {
        // echo "    <td>" . ($item !== null ? htmlentities($item, ENT_QUOTES) : "&nbsp;") . "</td>\n";
    // }
	$puffer=$puffer."</tr>\n";
}
$puffer=$puffer."</table>\n";

$row_counter=oci_num_rows($result);

$additional=35*$row_counter;

include("header.php");
include("loginbox.php");

echo $puffer;
}else{
	
include("header.php");
include("loginbox.php");	

echo "<div id=\"content\" style=\"height: auto\">";
echo "Keine Rechte";


}



echo "</div>";

include ("footer.html");

?>