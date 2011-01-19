<?php
include_once("db.inc.php");
include_once("snmptrack_functions.php");


$puffer="";

if(strlen($_GET['sort'])>0){
$orderstring=" ORDER BY \"".getSortname($_GET['sort'])."\" ".getSortorder($_GET['sort']);	
}else{
$orderstring=" ORDER BY VLANID ASC";	
}		
	
$sSQL="SELECT * FROM vlans_live".$orderstring;
	
$result=db_query($sSQL);

$row_counter=oci_num_rows($result);

//$pagewidth=1450;
$additional=400;

include("header.php");
include("loginbox.php");
?>
<div id="content" style="height: auto">
<?php

echo "<table border='1'>\n";

echo "
<th>VLANID <a href='".$_SERVER['REQUEST_URI']."&sort=VLANID_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=VLANID_D'>▼</a></th>
<th>STATUS <a href='".$_SERVER['REQUEST_URI']."&sort=STATUS_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=STATUS_D'>▼</a></th>
<th>TYP <a href='".$_SERVER['REQUEST_URI']."&sort=TYP_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=TYP_D'>▼</a></th>
<th>VNAME <a href='".$_SERVER['REQUEST_URI']."&sort=VNAME_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=VNAME_D'>▼</a></th>
<th>VTPD <a href='".$_SERVER['REQUEST_URI']."&sort=VTPD_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=VTPD_D'>▼</a></th>
<th>Last seen <a href='".$_SERVER['REQUEST_URI']."&sort=STAMPTIME_A'>▲</a><a href='".$_SERVER['REQUEST_URI']."&sort=STAMPTIME_D'>▼</a></th>";



while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {

    echo "<tr>\n";

	echo "<td>".$row['VLANID']."</td>";
	echo "<td>".$row['STATUS']."</td>";
	echo "<td>".$row['TYP']."</td>";
	echo "<td>".$row['VNAME']."</td>";
	echo "<td>".$row['VTPD']."</td>";
	echo "<td>".$row['STAMPTIME']."</td>";
	
    echo "</tr>\n";
}
echo "</table>\n";


echo "</div>";

include ("footer.html");

?>