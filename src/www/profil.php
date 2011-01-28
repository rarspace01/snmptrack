<?php
session_start(); 

include_once("db.inc.php");
include_once("snmptrack_functions.php");



	$passwd = $_POST['password'];
	$passwd2 = $_POST['password2'];

//check if pwd change
if(isset($passwd)&&strlen($passwd)>0){
	
	if($passwd==$passwd2){
		
	//update in DB
	
		$sSQL="UPDATE USRS SET USRPWD='".sha1($passwd)."' WHERE USRID='".$_SESSION['id']."'";
		$result=db_query($sSQL);
		echo "Ihr Passwort wurde geändert";
	}else{
		echo "Passwörter stimmen nicht überein.";
	}
	
}
	

	
//Header+Loginbox
include("header.php");
include("loginbox.php");


echo "<div id=\"content\">";
        
echo "<h1>Profil bearbeiten</h1>";

if(strlen($email)>0) //wenn aktualisieren geklickt wurde, zeige an, dass aktualisiert wurde + udpate das Profil
	{
	updateUserInfo($_SESSION['id'],$nickname,$passwd,$email,$img_url,$timezone_utc,$user_language);
	echo "Ihr Profil wurde aktualisiert";
	}           
           
	$sSQL="SELECT USRID, USRNAME FROM USRS WHERE USRID='".$_SESSION['id']."'";

$result=db_query($sSQL);
						
      if ($result) {
   	    while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)){ //lese die Werte aus der Datenbank aus
		  $profil_name = $row['USRNAME'];
       	  $profil_id = $row['USRID'];
		  
echo "<form id=\"formularprofil\" action=\"profil.php\" name=\"form1\" method=\"post\" \">";
?>
                <div id="profilcontent">
<?php
echo "Benutzer: ".$profil_name;
echo "<br/><br/>";
echo "Passwort ändern:<br/><br/>";
echo "<label>Passwort: <input type=\"password\" name=\"password\" id=\"password\" value=\"\" class=\"textfeld\"></label><br><br>";
echo "<label>Passwort Wdh.: <input type=\"password\" name=\"password2\" id=\"password2\" value=\"\" class=\"textfeld\"></label><br><br>";
   	    }


      }	 
 
	 
	  
echo "<input type=\"submit\" class=\"buttons\"value=\"Aktualisieren\"  />"; 

echo "</div>";
echo "</form>";


echo "</div>";

include 'footer.html';

//funktion für das updaten des users, wir unterscheiden ob ein passwort gesetzt wurde, damit dieses auch nru geändert wird sofern ein neues eingetragen wurde
function updateUserInfo($user_id,$nickname,$passwd,$email,$img_url,$timezone_utc,$user_language)
{
include("./config/config.php");
if(strlen($passwd)>0) //prüfen ob PWD sich geändert hat
{
$passwd=sha1("$passwd");
$query="UPDATE ".$config['dbPrefix']."_users SET nickname='$nickname', passwd='$passwd', email='$email', imgurl='$img_url', timezone_utc='$timezone_utc', language='$user_language' WHERE userid='$user_id';";
}else
{
$query="UPDATE ".$config['dbPrefix']."_users SET nickname='$nickname', email='$email', imgurl='$img_url', timezone_utc='$timezone_utc', language='$user_language' WHERE userid='$user_id';";
}
  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);
} 
  
//funktion für das kündigen des users, der Login wird ungültig gemacht (zufälliges password welches der user nicht kennt  + zusätzlich wird das userlevel auf -1 gesetzt
function delUser($userid)
{
include("./config/config.php");
$query="UPDATE ".$config['dbPrefix']."_users SET nickname='Deaktivierter User', passwd='".sha1(time()+rand(1,999))."', imgurl='', userlevel='-1', timezone_utc='', language='' WHERE userid='$userid';";
 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);
	 mysql_close($connection);
}

?>

 <script type="text/javascript">
   function check() {
      if (!confirm("Wollen Sie wirklich Ihr Konto entfernen. Dieser Schritt ist unwiderruflich!")) {
         return false;
      }
   }
  </script>

