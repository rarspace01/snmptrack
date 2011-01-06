<?php
include_once("db.inc.php");

$puffer="";

if(strlen($_GET[hmac])>0){
	
$sSQL="SELECT H.\"PortMAC\", H.MAC, H.IP, H.\"hostname\", H.\"Speed\", H.\"lastuser\", H.VHOST, P.\"SwitchIP\" FROM USRTRACK.HOSTS_LIVE H, USRTRACK.PORTS_LIVE P WHERE H.\"PortMAC\"=P.MAC AND \"PortMAC\"='".$_GET[pmac]."' AND H.MAC='".$_GET[hmac]."' ORDER BY H.\"hostname\" ASC";
	
$result=db_query($sSQL);

$row_counter=oci_num_rows($result);

include("header.php");
include("loginbox.php");
?>
<div id="content" style="height: auto">
<?php

echo "<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {

    echo "<tr>\n";

	echo "<td><a href='show.php?pmac=".$row['PortMAC']."&hmac=".$row['MAC']."&details=true'>".$row['MAC']."</a></td>";
	echo "<td><a href='show.php?pmac=".$row['PortMAC']."'>".$row['PortMAC']."</a></td>";
	echo "<td><a href='show.php?sip=".$row['SwitchIP']."'>".$row['SwitchIP']."</a></td>";
	echo "<td>".$row['IP']."</td>";
	echo "<td>".$row['hostname']."</td>";
	echo "<td>".$row['Speed']."</td>";
	echo "<td>".$row['lastuser']."</td>";
	echo "<td>".$row['VHOST']."</td>";

	if(strlen($_GET[details])>0){
	echo "<td>EXTENDED</td>";
	}
	
    echo "</tr>\n";
}
echo "</table>\n";

}else if(strlen($_GET[pmac])>0){

$sSQL="SELECT H.\"PortMAC\", H.MAC, H.IP, H.\"hostname\", H.\"Speed\", H.\"lastuser\", H.VHOST, P.\"SwitchIP\" FROM USRTRACK.HOSTS_LIVE H, USRTRACK.PORTS_LIVE P WHERE H.\"PortMAC\"=P.MAC AND \"PortMAC\"='".$_GET[pmac]."' ORDER BY H.\"hostname\" ASC";
	
	
	
if(strlen($_GET[q])>0){
$sSQL="SELECT H.\"PortMAC\", H.MAC, H.IP, H.\"hostname\", H.\"Speed\", H.\"lastuser\", H.VHOST, P.\"SwitchIP\" FROM USRTRACK.HOSTS_LIVE H, USRTRACK.PORTS_LIVE P WHERE H.\"PortMAC\"=P.MAC AND (H.\"IP\" LIKE '%".$_GET[q]."%' OR LOWER(H.MAC) LIKE LOWER('%".$_GET[q]."%') OR LOWER(H.\"hostname\") LIKE LOWER('%".$_GET[q]."%') OR LOWER(H.\"lastuser\") LIKE LOWER('%".$_GET[q]."%')) ORDER BY H.\"hostname\" ASC";
}else{
$sSQL="SELECT H.\"PortMAC\", H.MAC, H.IP, H.\"hostname\", H.\"Speed\", H.\"lastuser\", H.VHOST, P.\"SwitchIP\" FROM USRTRACK.HOSTS_LIVE H, USRTRACK.PORTS_LIVE P WHERE H.\"PortMAC\"=P.MAC AND \"PortMAC\"='".$_GET[pmac]."' ORDER BY H.\"hostname\" ASC";
}

$result=db_query($sSQL);

$row_counter=oci_num_rows($result);

include("header.php");
include("loginbox.php");
?>
<div id="content" style="height: auto">
<?php

echo "<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {

    echo "<tr>\n";

	echo "<td><a href='show.php?pmac=".$row['PortMAC']."&hmac=".$row['MAC']."&details=true'>".$row['MAC']."</a></td>";
	echo "<td><a href='show.php?pmac=".$row['PortMAC']."'>".$row['PortMAC']."</a></td>";
	echo "<td><a href='show.php?sip=".$row['SwitchIP']."'>".$row['SwitchIP']."</a></td>";
	echo "<td>".$row['IP']."</td>";
	echo "<td>".$row['hostname']."</td>";
	echo "<td>".$row['Speed']."</td>";
	echo "<td>".$row['lastuser']."</td>";
	echo "<td>".$row['VHOST']."</td>";

    echo "</tr>\n";
}
echo "</table>\n";

}else if(strlen($_GET[sip])>0){

$result=db_query("SELECT * FROM USRTRACK.PORTS_LIVE WHERE \"SwitchIP\" LIKE '".$_GET[sip]."' ORDER BY \"PortID\" ASC");

$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";


$puffer=$puffer."<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	//var_dump($row);
	
	
    $puffer=$puffer."<tr>\n";
	
	
	$puffer=$puffer."<td><a href='show.php?pmac=".$row['MAC']."'>".$row['MAC']."</a></td>";
	$puffer=$puffer."<td>".$row['SwitchIP']."</td>";
	$puffer=$puffer."<td>".$row['name']."</td>";
	$puffer=$puffer."<td>".$row['Speed']."</td>";

	$puffer=$puffer."<td>".$row['location']."</td>";
	
    // foreach ($row as $item) {
        // echo "    <td>" . ($item !== null ? htmlentities($item, ENT_QUOTES) : "&nbsp;") . "</td>\n";
    // }
	
	
    $puffer=$puffer."</tr>\n";
}
$puffer=$puffer."</table>\n";

$row_counter=oci_num_rows($result);

$additional=20*$row_counter-350;

include("header.php");
include("loginbox.php");

echo $puffer;

}else{

if(strlen($_GET[q])>0){
$result=db_query("SELECT * FROM USRTRACK.SWITCHS_LIVE WHERE IP LIKE '%".$_GET[q]."%' OR \"hostname\" LIKE '%".$_GET[q]."%' ORDER BY LIP ASC");
}else{
$result=db_query("SELECT * FROM USRTRACK.SWITCHS_LIVE ORDER BY LIP ASC");
}




$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";


$puffer=$puffer."<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	//var_dump($row);
	
	$puffer=$puffer."<tr>\n"."<td><a href='show.php?sip=".$row['IP']."'>".
	$row['IP']."</a></td>"."<td>".$row['model']."</td>"."<td>".$row['alias']."</td>".
	"<td>".$row['location']."</td>"."<td>".$row['SERIAL']."</td>";
	
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
}



echo "</div>";

include ("footer.html");

?>