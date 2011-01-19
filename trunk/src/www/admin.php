<?php 
//Einbindungen
session_start(); 
include("header.php");
include("loginbox.php");



echo "<div id=\"content\">";
if($_SESSION['userlevel']>1)//Prüfe ob User == Admin
{

	//prüfe jeweils ob das "Objekt" gelöscht werden soll
	if(($_GET['deluser'])>0)
	{
	delUser($_GET['deluser']);
	}

	if(($_GET['delproject'])>0)
	{
	delProject($_GET['delproject']);
	}

	if(($_GET['delcat'])>0)
	{
	delCat($_GET['delcat']);
	}

	if(($_GET['delos'])>0)
	{
	delOS($_GET['delos']);
	}

	//prüfe jeweils ob ein "Objekt" erstellt werden soll
	if($_GET['action']==='addproject')
	{
	echo "Project wurde erstellt.<br>";
	addProject($_POST['projectname']);
	}
	if($_GET['action']==='addcat')
	{
	echo "Kategorie wurde erstellt.<br>";
	addCat($_POST['catname']);
	}
	if($_GET['action']==='addos')
	{
	echo "OS wurde erstellt.<br>";
	addOS($_POST['osname'],$_POST['osversion']);
	}

	//prüfe jeweils ob das "Objekt" angezeigt werden soll
	if(isset($_GET['showuser'])){

	showUsers();

	}
	elseif(isset($_GET['showproj']))
	{

	showProjectadd(); //Formular zum hinzufügen
	showProjects();
	
	}
	elseif(isset($_GET['showcat']))
	{

	showCatadd(); //Formular zum hinzufügen
	showCat();
	}
	elseif(isset($_GET['showos']))
	{

	showOSadd(); //Formular zum hinzufügen
	showOS();
	
	}//Wenn rein garnichts selektiert wurde, zeige Auswahl an
	else{
	
	echo "<h1>Admin-Panel</h1>";
	echo "<u><b>Datenbank</b></u><br>";
	echo "Datenbank <a href=\"setup.php?reset=true\" onClick=\"return check();\">resetten<a/><br><br>";

	echo "<u><b>User</b></u><br>";
	echo "User <a href='admin.php?showuser=-1'>anzeigen</a><br><br>";

	echo "<u><b>Ticket</b></u><br>";
	echo "Projekte <a href='admin.php?showproj=-1'>verwalten</a><br>";
	echo "Kategorie <a href='admin.php?showcat=-1'>verwalten</a><br>";
	echo "Betriebsystem <a href='admin.php?showos=-1'>verwalten</a><br>";   


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
include("./config/config.php");
	$query="SELECT userid, username,email FROM tints_users ORDER BY username ASC;";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {

//zuerst Tabellen header ausgeben
echo "<table>
		<th style=\"background-color:#e3e3e3;\">UserID</th>
		<th style=\"background-color:#e3e3e3;padding-left:5px;background-color:#e3e3e3;\">Benutzername</th>
		<th style=\"background-color:#e3e3e3;\">Email</th>
		<th style=\"background-color:#e3e3e3;\">Sperren</th>
		";
//so viele Tabellen Einträge ausgeben wie wir finden
   	    while ($row = mysql_fetch_row($result)) {
		$userid=$row[0];
		$username=$row[1];
		$email=$row[2];

echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$userid</td>
		<td style=\"text-align:center;width:10px;\">$username</td>
		<td style=\"text-align:center;width:10px;\">$email</td>
		<td style=\"text-align:center;width:10px;\"><a href='admin.php?deluser=$userid'>X</a></td>		
		</tr>";
		}	

		}
echo "</table>";

}

//zeige Projekte an
function showProjects()
{
include("./config/config.php");
$query="SELECT projectid,projecttitle FROM tints_projects ORDER BY projecttitle ASC;";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {
//zuerst Tabellen header ausgeben
echo "<table>
		<th style=\"background-color:#e3e3e3;padding-left:5px;background-color:#e3e3e3;\">Projektname</th>
		<th style=\"background-color:#e3e3e3;\">L&ouml;schen</th>
		";
//so viele Tabellen Einträge ausgeben wie wir finden
   	    while ($row = mysql_fetch_row($result)) {
		$projectid=$row[0];
		$projecttitle=$row[1];

echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$projecttitle</td>
		<td style=\"text-align:center;width:10px;\"><a href='admin.php?delproject=$projectid'>X</a></td>		
		</tr>";
		}	

		}
echo "</table>";
}

//Zeige alle Kategorien
function showCat()
{
include("./config/config.php");
$query="SELECT catid, cattitle FROM tints_categories ORDER BY cattitle ASC;";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {
//zuerst Tabellen header ausgeben
echo "<table>
		<th style=\"background-color:#e3e3e3;padding-left:5px;background-color:#e3e3e3;\">Kategoriename</th>
		<th style=\"background-color:#e3e3e3;\">L&ouml;schen</th>
		";
//so viele Tabellen Einträge ausgeben wie wir finden
   	    while ($row = mysql_fetch_row($result)) {
		$catid=$row[0];
		$cattitle=$row[1];

echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$cattitle</td>
		<td style=\"text-align:center;width:10px;\"><a href='admin.php?delcat=$catid'>X</a></td>		
		</tr>";
		}	

		}
echo "</table>";
}

//zeige alle Betriebsysteme inkl Version an
function showOS()
{
include("./config/config.php");
$query="SELECT osid, title,version FROM tints_os ORDER BY title ASC,version DESC;";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {
//zuerst Tabellen header ausgeben
echo "<table>
		<th style=\"background-color:#e3e3e3;padding-left:5px;background-color:#e3e3e3;\">Betriebsystemname</th>
		<th style=\"background-color:#e3e3e3;\">Betriebsystemversion</th>
		<th style=\"background-color:#e3e3e3;\">L&ouml;schen</th>
		";
//so viele Tabellen Einträge ausgeben wie wir finden
   	    while ($row = mysql_fetch_row($result)) {
		$osid=$row[0];
		$ostitle=$row[1];
		$osversion=$row[2];

echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$ostitle</td>
		<td style=\"text-align:center;width:10px;\">$osversion</td>
		<td style=\"text-align:center;width:10px;\"><a href='admin.php?delos=$osid'>X</a></td>		
		</tr>";
		}	

		}
echo "</table>";
}

//Formular anzeigen für Projekt hinzufügen
function showProjectadd()
{
?>
            <form method="post" action="admin.php?action=addproject">
               
                <label><b>Projektname:</b></label> <input class="text" type="text" maxlength="20" size="20" name="projectname" />
            	<br>
                <input type="submit" value="Erstellen" class="buttons"/> <br>
            </form><br>
<?php
}

//Formular anzeigen für Kategorie hinzufügen
function showCatadd()
{
?>
            <form method="post" action="admin.php?action=addcat">
               
                <label><b>Kategoriename:</b></label> <input class="text" type="text" maxlength="20" size="20" name="catname" />
            	<br>
                <input type="submit" value="Erstellen" class="buttons"/> <br>
            </form><br>
<?php
}

//Formular anzeigen für Betriebsystem hinzufügen
function showOSadd()
{
?>
            <form method="post" action="admin.php?action=addos">
               
                <label><b>Betriebsystemname:</b></label> <input class="text" type="text" maxlength="20" size="20" name="osname" />
                <label><b>Betriebsystemversion:</b></label> <input class="text" type="text" maxlength="20" size="20" name="osversion" />
            	<br>
                <input type="submit" value="Erstellen" class="buttons"/> <br>
            </form><br>
<?php
}

//Funktion um user zu löschen
//Parameter: Userid
function delUser($userid)
{
include("./config/config.php");
$query="UPDATE ".$config['dbPrefix']."_users SET nickname='Deaktivierter User', passwd='".sha1(time())."', imgurl='', userlevel='-1', timezone_utc='', language='' WHERE userid='$userid';";
 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);
}


//Funktion um Projekt zu löschen
//Parameter: Projektid
//Hinweis: Es wird rekursiv gelöscht
function delProject($projectid)
{
include("./config/config.php");
$query="DELETE FROM tints_comment WHERE tickedid=(SELECT ticketid FROM tints_tickets WHERE projectid=".$projectid.");";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);


$query="DELETE FROM tints_tickets WHERE projectid=".$projectid.";";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);


$query="DELETE FROM tints_projects WHERE projectid=".$projectid.";";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);
}


//Funktion um Kategorie zu löschen
//Parameter: Kategorieid
//Hinweis: Es wird rekursiv gelöscht
function delCat($catid)
{
include("./config/config.php");
$query="DELETE FROM tints_comment WHERE tickedid=(SELECT ticketid FROM tints_tickets WHERE catid=".$catid.");";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);


$query="DELETE FROM tints_tickets WHERE catid=".$catid.";";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);


$query="DELETE FROM tints_categories WHERE catid=".$catid.";";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);
}

//Funktion um Betriebsystem zu löschen
//Parameter: Betriebsystemid
//Hinweis: Es wird rekursiv gelöscht
function delOS($osid)
{
include("./config/config.php");
$query="DELETE FROM tints_comment WHERE tickedid=(SELECT ticketid FROM tints_tickets WHERE osid=".$osid.");";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);


$query="DELETE FROM tints_tickets WHERE osid=".$osid.";";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);


$query="DELETE FROM tints_os WHERE osid=".$osid.";";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);
}


//Funktion um Projekt hinzuzufügen
function addProject($projectname)
{
include("./config/config.php");
$query="INSERT INTO tints_projects (projecttitle) VALUES ('$projectname');";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);

}

//Funktion um Kategorie hinzuzufügen
function addCat($catname)
{
include("./config/config.php");
$query="INSERT INTO tints_categories (cattitle) VALUES ('$catname');";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);

}

//Funktion um Betriebsystem hinzuzufügen
function addOS($osname,$osversion)
{
include("./config/config.php");
$query="INSERT INTO tints_os (title,version) VALUES ('$osname','$osversion');";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);

}

?>
