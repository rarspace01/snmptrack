<?php

include_once("db.inc.php");

if(strlen($_GET[hmac])>0){
$result=db_query("SELECT * FROM USRTRACK.HOSTS_LIVE WHERE \"PortMAC\"='".$_GET[pmac]."' AND MAC='".$_GET[hmac]."' ORDER BY \"hostname\" ASC");

echo "<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {

    echo "<tr>\n";

	echo "<td><a href='show.php?pmac=".$row['PortMAC']."&hmac=".$row['MAC']."&details=true'>".$row['MAC']."</a></td>";
	echo "<td>".$row['PortMAC']."</td>";
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
$result=db_query("SELECT * FROM USRTRACK.HOSTS_LIVE WHERE \"PortMAC\"='".$_GET[pmac]."' ORDER BY \"hostname\" ASC");

echo "<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {

    echo "<tr>\n";

	echo "<td><a href='show.php?pmac=".$row['PortMAC']."&hmac=".$row['MAC']."'>".$row['MAC']."</a></td>";
	echo "<td>".$row['PortMAC']."</td>";
	echo "<td>".$row['IP']."</td>";
	echo "<td>".$row['hostname']."</td>";
	echo "<td>".$row['Speed']."</td>";

    echo "</tr>\n";
}
echo "</table>\n";

}else if(strlen($_GET[sip])>0){

$result=db_query("SELECT * FROM USRTRACK.PORTS_LIVE WHERE \"SwitchIP\"='".$_GET[sip]."' ORDER BY \"name\" ASC");

echo "<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	//var_dump($row);
	
	
    echo "<tr>\n";
	
	
	echo "<td><a href='show.php?pmac=".$row['MAC']."'>".$row['MAC']."</a></td>";
	echo "<td>".$row['SwitchIP']."</td>";
	echo "<td>".$row['name']."</td>";
	echo "<td>".$row['Speed']."</td>";

	echo "<td>".$row['location']."</td>";
	
    // foreach ($row as $item) {
        // echo "    <td>" . ($item !== null ? htmlentities($item, ENT_QUOTES) : "&nbsp;") . "</td>\n";
    // }
	
	
    echo "</tr>\n";
}
echo "</table>\n";

}else{

$result=db_query("SELECT * FROM USRTRACK.SWITCHS_LIVE ORDER BY IP ASC");

echo "<table border='1'>\n";
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	//var_dump($row);
	
	
    echo "<tr>\n";
	
	echo "<td><a href='show.php?sip=".$row['IP']."'>".$row['IP']."</a></td>";
	echo "<td>".$row['model']."</td>";
	echo "<td>".$row['alias']."</td>";
	echo "<td>".$row['location']."</td>";
	
    // foreach ($row as $item) {
        // echo "    <td>" . ($item !== null ? htmlentities($item, ENT_QUOTES) : "&nbsp;") . "</td>\n";
    // }
    echo "</tr>\n";
}
echo "</table>\n";
}

/*

if($_GET[action]==='switchs'){

$sql = "SELECT * FROM USRTRACK.\"st_switchs\" ORDER BY IP DESC";

}else if($_GET[action]==='ports'){
$sql = "SELECT * FROM USRTRACK.\"st_ports\" p, USRTRACK.\"st_switchs\" s WHERE p.\"SwitchIP\"=s.IP ORDER BY p.MAC ASC";
}else if($_GET[action]==='hosts'){
$sql = "SELECT h.MAC, h.IP, h.\"hostname\", p.\"name\", s.\"alias\" AS SName FROM \"st_hosts\" h, \"st_ports\" p, \"st_switchs\" s WHERE h.\"PortMAC\"=p.MAC AND p.\"SwitchIP\"=s.IP ORDER BY h.\"hostname\" ASC, h.IP ASC, h.MAC ASC";
}else if($_GET[action]==='dub'){
$sql = "SELECT h.MAC, h.ip, h.\"hostname\", p.MAC AS PMAC, s.\"alias\" AS SName, s.\"alevel\" AS A FROM \"st_hosts\" h, \"st_ports\" p, \"st_switchs\" s WHERE h.\"PortMAC\"=p.MAC AND p.\"SwitchIP\"=s.\"IP\" AND h.MAC IN (SELECT MAC FROM \"USRTRACK\".\"st_hosts\" GROUP BY MAC HAVING COUNT(*)>1) ORDER BY h.MAC";
}else{
print "Kein Parameter</br>
Sort by:<br>
<a href='".$_SELF."?action=switchs'>Switchs</a></br>
<a href='".$_SELF."?action=ports'>Ports</a></br>
<a href='".$_SELF."?action=hosts'>Hosts</a></br>
<a href='".$_SELF."?action=dub'>Duplikate</a></br>
";
}

if(isset($sql))
{
	if(strlen($sql)>0)
	{
	print ($sql);
	query($sql);
	}
}
*/

/*
if (!isset($_SERVER['PHP_AUTH_USER'])) {
    header('WWW-Authenticate: Basic realm="SNMPTrack Site"');
    header('HTTP/1.0 401 Unauthorized');
    echo 'You must enter a valid login and password';
    exit;
} else if(PHP_AUTH_PW=='geheim'){
    echo "<p>Hello {$_SERVER['PHP_AUTH_USER']}.</p>";
    echo "<p>You entered {$_SERVER['PHP_AUTH_PW']} as your password.</p>";
}
*/


?>