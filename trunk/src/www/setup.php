<?php
session_start();
include 'header.php';

//um nicht alles neu zu coden nehemen wir einfach das register layout
echo "<div id=\"registrieren\">";

//check ob config.php existiert
if(!file_exists("./config/config.php"))
{
die("Keine config.php vorhanden.<br>");
}

//include config.php
include("./config/config.php");
	
//Setup starten
echo "Setup Script gestartet.<br>";

//Wurde reset ausgewählt?
if(strlen($_GET['reset'])>0)//when reset
{
	//Prüfe ob Admin
	if($_SESSION['userlevel']>1)
	{
	//Alle tables droppen
	$query="DROP TABLE tints_categories; DROP TABLE tints_comment; DROP TABLE tints_os; DROP TABLE tints_projects; DROP TABLE tints_tickets; DROP TABLE tints_users;";

	$connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
		  if (!$connection) {
		    die("Fehler! " . mysql_error());
		}
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);

	//import db
	parse_mysql_dump("install.sql",$config['dbHost'],"tints", $config['dbUser'], $config['dbPassword']);
	echo "Datenbank zur&uuml;ckgesetzt.</br>";
	}
}else{ //Wenn Install ausgewählt
	//Gibt es einen Benutzer?
	$query="SELECT COUNT(*) FROM tints_users;";

	$connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);

	$row = mysql_fetch_row($result);
	if($row[0]==0)//wenn es keinen Benutzer gibt starte import
	{
	parse_mysql_dump("install.sql",$config['dbHost'],"tints", $config['dbUser'], $config['dbPassword']);
	echo "Tints wurde installiert.<br>";
	}else{
	echo "Tints bereits installiert.<br>";
	};

}

echo "</div>";
include 'footer.html';


//funktion um SQL Befehle aus datei auszuführen
function parse_mysql_dump($url,$nowhost,$nowdatabase,$nowuser,$nowpass){
	$link = mysql_connect($nowhost, $nowuser, $nowpass);
		if (!$link) {
		   die('Not connected : ' . mysql_error());
		}
		

		$db_selected = mysql_select_db($nowdatabase, $link);
		if (!$db_selected) {
		   die ('Can\'t use Database: ' . mysql_error());
		}
   $file_content = file($url);
	$query="";
   foreach($file_content as $sql_line){
     if(trim($sql_line) != "" && strpos($sql_line, "--") === false){
	$query=$query.$sql_line;
	if(strlen(strstr($query,";"))>0)
	{
       	$result=mysql_query($query);
	$query="";
	}

     }
   }
  }

?>
