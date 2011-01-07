<?php
include_once("db.inc.php");

$puffer="";

$result=db_query("SELECT h.MAC,h.IP, h.\"hostname\", h.\"Speed\", h.\"lastuser\", h.VHOST, P.\"SwitchIP\", h.\"stamptime\", p.MAC AS PMAC, s.\"alias\" AS SName, s.\"alevel\" AS A FROM \"st_hosts\" h, \"st_ports\" p, \"st_switchs\" s WHERE h.\"PortMAC\"=p.MAC AND p.\"SwitchIP\"=s.\"IP\" AND h.MAC IN (SELECT MAC FROM \"USRTRACK\".\"st_hosts\" GROUP BY MAC HAVING COUNT(*)>1) ORDER BY h.MAC DESC");


$puffer=$puffer."<div id=\"content\" style=\"height: auto\">";


$puffer=$puffer."<table border='1'>\n";

$puffer=$puffer."<th>MAC</th>
<th>PMAC</th>
<th>SwitchIP</th>
<th>IP</th>
<th>DNS</th>
<th>Speed [MBit/s]</th>
<th>User</th>
<th>Last seen</th>
<th>VHOST</th>";

while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	
	$puffer=$puffer."<tr>\n";

	$puffer=$puffer."<td><a href='show.php?pmac=".$row['PMAC']."&hmac=".$row['MAC']."&details=true'>".$row['MAC']."</a></td>";
	$puffer=$puffer."<td><a href='show.php?pmac=".$row['PMAC']."'>".$row['PMAC']."</a></td>";
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

$additional=40*$row_counter;
$pagewidth=1400;

include("header.php");
include("loginbox.php");

echo $puffer;




echo "</div>";

include ("footer.html");

?>