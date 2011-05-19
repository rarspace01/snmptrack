<?php 
session_start(); 

//Einbindungen
include("header.php");
include("loginbox.php");



echo "<div id=\"content\">";
if($_SESSION['userlevel']==3)//Prüfe ob User == Admin
{

	if(isset($_GET['msg'])){
	echo "Started Agent on Switch:".$_GET['msg'];
	}

	if(isset($_GET['cleandb'])){
	echo "Cleaned DB";
	delOldData();
	}
	
	if(isset($_GET['startagent'])){
	
	if($_GET['startagent']==='all'){
	$sEXEC="/usr/bin/java -jar /usr/local/usrtrack/snmptrack.jar /usr/local/usrtrack/";	
	}else{
	$sEXEC="/usr/bin/java -jar /usr/local/usrtrack/snmptrack.jar ".$_GET['startagent']." /usr/local/usrtrack/";	
	}
	
	$sEXEC="bash -c \"exec nohup setsid ".$sEXEC."> /dev/null 2>&1 &\"";
	
	exec($sEXEC)." <br/> ";
	
	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=admin.php?msg=".$_GET['startagent']."\">");
	die;
	}
	
	//prüfe jeweils ob das "Objekt" gelöscht werden soll
	if(isset($_GET['deluser']))
	{
	delUser($_GET['deluser']);
	}

	//prüfe jeweils ob ein "Objekt" erstellt werden soll
	if($_GET['action']==='addusr')
	{
	if($_POST['usrpwd']===$_POST['usrpwd2']){	
	echo "User wurde erstellt.<br/>";
	addUser($_POST['usrname'],$_POST['usrpwd'],$_POST['usrlvl']);
	}else{
	echo "Passwort stimmt nicht überein<br/>";	
	}
	
	}

	//prüfe jeweils ob das "Objekt" angezeigt werden soll
	if(isset($_GET['showuser'])){

	showUseradd();
	showUsers();
	
	}//Wenn rein garnichts selektiert wurde, zeige Auswahl an
	else{
	
	echo "<h1>Admin-Panel</h1>";
	echo "<u><b>User</b></u><br/>";
	echo "User <a href='admin.php?showuser=-1'>verwalten</a><br/>";
	
	echo "<br>";
	echo "<u><b>DB</b></u><br/>";
	echo "DB <a href='admin.php?cleandb=true'>aufr&auml;umen</a> Alte Eintr&auml;ge (>90 Tage) l&ouml;schen<br/>";

	
	echo "<br>";
	echo "<u><b>Agent</b></u><br/>";
	echo "Agent <a href='admin.php?startagent=all'>starten</a><br/>";
	?>
	<form method="get" action="admin.php">
               
                <label>Agent für IP:</label>
                <select name="startagent">
                <?php 
				include_once 'db.inc.php';
                
				$sSQL="SELECT IP FROM SWITCHS_LIVE ORDER BY LIP";
				$result=db_query($sSQL);
				
				while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
				
					echo "<option value='".$row['IP']."'>".$row['IP']."</option>";
				
				}
				
                /*
                 * <option value="1">Helpdesk</option>
                <option value="2">Netzwerk</option>
                <option value="3">Admin</option>
                 */
                
                
                
                
                ?>
                </select>
                <input type="submit" value="starten" class="buttons"/> <br/>
            </form><br>
	
	<?php 
	}

}else
{//output wenn user kein Admin
echo "Kein Admin";
}
echo"</div>  ";

?>
<!-- JS Code fuer Datenbank reset -->
 <script type="text/javascript">
   function check() {
      if (!confirm("Wollen Sie wirklich die Datenbank in den initialen Zustand setzen?. Dieser Schritt ist unwiderruflich!")) {
         return false;
      }
   }
  </script>
<?php

include 'footer.html';

//Funktion um Userliste anzuzeigen
function showUsers()
{
include_once 'db.inc.php';

	$sSQL="SELECT USRID, USRNAME, USRPWD, USRLVL FROM USRS ORDER BY USRNAME ASC";

	$result=db_query($sSQL);

  //zuerst Tabellen header ausgeben
echo "<table>
		<th style=\"background-color:#e3e3e3;padding-left:5px;background-color:#e3e3e3;\">Benutzername</th>
		<th style=\"background-color:#e3e3e3;padding-left:5px;background-color:#e3e3e3;\">Benutzerlevel</th>
		<th style=\"background-color:#e3e3e3;\">Löschen</th>
		";
//so viele Tabellen Einträge ausgeben wie wir finden
while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
		$userid=$row['USRID'];
		$username=$row['USRNAME'];
		$usrlvl=$row['USRLVL'];
echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$username</td>
		<td style=\"text-align:center;width:10px;\">$usrlvl</td>
		<td style=\"text-align:center;width:10px;\"><a href='admin.php?deluser=$userid'>X</a></td>		
		</tr>";
		}	

		
echo "</table>";

}





//Formular anzeigen für Projekt hinzufügen
function showUseradd()
{
?>
            <form method="post" action="admin.php?action=addusr">
               
                <label><b>Benutzername:</b></label> <input class="text" type="text" maxlength="255" size="20" name="usrname" /><br/>
                <label><b>Passwort:</b></label> <input class="text" type="password" maxlength="255" size="20" name="usrpwd" /><br/>
                <label><b>Passwort wdh.:</b></label> <input class="text" type="password" maxlength="255" size="20" name="usrpwd2" /><br/>
                <label><b>Level:</b></label> <select name="usrlvl">
                <option value="1">Helpdesk - 1</option>
                <option value="2">Netzwerk - 2</option>
                <option value="3">Admin - 3</option>
                </select><br/>
                <input type="submit" value="Hinzufügen" class="buttons"/> <br/>
            </form><br>
<?php
}


//Funktion um user zu löschen
//Parameter: Userid
function delUser($userid)
{
include_once 'db.inc.php';
$sSQL="DELETE FROM USRS WHERE USRID='$userid'";
$result=db_query($sSQL);
}

//Funktion um User hinzuzufügen
function addUser($usrname,$usrpwd,$usrlvl)
{
include_once 'db.inc.php';

$usrid="";	
$usrid=sha1(time());

$usrpwd=sha1($usrpwd);

$sSQL="INSERT INTO USRS (USRID,USRNAME,USRPWD,USRLVL) VALUES ('$usrid','".$usrname."','".$usrpwd."','".$usrlvl."')";

$result=db_query($sSQL);

}

function delOldData()
{
//del old entries
$tempdays=90;
include_once 'db.inc.php';
$ctime=time()-($tempdays*24*60*60);

//work tables

$sSQL="DELETE FROM \"st_hosts\" WHERE \"stamptime\"<'".$ctime."'";
$result=db_query($sSQL);
$sSQL="DELETE FROM \"st_ports\" WHERE \"stamptime\"<'".$ctime."'";
$result=db_query($sSQL);
$sSQL="DELETE FROM \"st_switchs\" WHERE \"stamptime\"<'".$ctime."'";
$result=db_query($sSQL);
$sSQL="DELETE FROM \"st_vlans\" WHERE stamptime<'".$ctime."'";
$result=db_query($sSQL);

//live tables
$sSQL="DELETE FROM HOSTS_LIVE WHERE \"stamptime\"<'".$ctime."'";
$result=db_query($sSQL);
$sSQL="DELETE FROM PORTS_LIVE WHERE \"stamptime\"<'".$ctime."'";
$result=db_query($sSQL);
$sSQL="DELETE FROM SWITCHS_LIVE WHERE \"stamptime\"<'".$ctime."'";
$result=db_query($sSQL);
$sSQL="DELETE FROM VLANS_LIVE WHERE stamptime<'".$ctime."'";
$result=db_query($sSQL);

}

?>
