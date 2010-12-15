<?php

include_once("db.inc.php");

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



?>