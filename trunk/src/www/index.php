<?php 
session_start(); 
include_once("db.inc.php");
include("header.php"); //Header einbinden
include("loginbox.php"); //Loginbox
include("overviewfunctions.php"); //Funktionen für die Hauptseite

?>
  <!-- HTML Content -->
	
        <div id="content">
            <h1>Was ist SNMP Track?</h1>

				SNMP Track ist informativ.<br/>
				<br/><br/>

            <h1>Übersicht: </h1>
            
            <?php 
            
            $sSQLSUM="SELECT COUNT(*) PGES FROM \"USRTRACK\".\"PORTS_LIVE\"";
            $resultSUM=db_query($sSQLSUM);
            $row = oci_fetch_array($resultSUM, OCI_ASSOC+OCI_RETURN_NULLS);
            $PSUM=$row['PGES'];
                        
            $sSQLON="SELECT COUNT(*) PON FROM \"USRTRACK\".\"PORTS_LIVE\" WHERE \"cstatus\"='true'";
            $resultON=db_query($sSQLON);
            $row = oci_fetch_array($resultON, OCI_ASSOC+OCI_RETURN_NULLS);
            $PON=$row['PON'];
            
            
            $sSQLHC="SELECT COUNT(*) HC FROM \"USRTRACK\".\"HOSTS_LIVE\"";
            $resultHC=db_query($sSQLHC);
            $row = oci_fetch_array($resultHC, OCI_ASSOC+OCI_RETURN_NULLS);
            $HC=$row['HC'];
            
            $sSQLST="SELECT \"stamptime\" ST FROM (SELECT \"stamptime\" FROM \"USRTRACK\".\"HOSTS_LIVE\") ORDER BY \"stamptime\" DESC";
            $resultST=db_query($sSQLST);
            $row = oci_fetch_array($resultST, OCI_ASSOC+OCI_RETURN_NULLS);
            $ST=$row['ST'];
            
            // UTC+1 +DST if active
//            $ST=$ST+60*60;
            date_default_timezone_set("Europe/Berlin");
            
//            if($ST>mktime(2, 0, 0, 3, 31, date('Y', $ST))&&$ST<mktime(2, 0, 0, 10, 31, date('Y', $ST))){
//            	$ST=$ST+60*60;
//            }
            
            
            echo "Ports online: [".$PON."/".$PSUM."]<br/>";
            echo "Hosts: [".$HC."]<br/>";
            echo "Last Check: ".date('H:i.s\U\h\r d.m.Y', $ST)."<br/>";
            
            ?>
		
       </div>
    
<?php
include 'footer.html';
?>
