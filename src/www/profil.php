<?php session_start(); 
include_once("./config/config.php"); //wird für die abfrage benötigt

if(strlen($_GET['delself'])>0) //wenn der user konto-kündigne aufgerufen hat
{
delUser($_SESSION['id']); //lösche user
echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=profil.php?logout=true\">"); //logge den user aus
}

	$nickname = $_POST['nickname'];
	$passwd = $_POST['password'];
	$email = $_POST['email'];
	$img_url= $_POST['img_url'];
	$timezone_utc = $_POST['timezone_utc'];
	$user_language= $_POST['user_language'];

	
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
           

	  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	$query="SELECT nickname, passwd, email, imgurl,timezone_utc,language FROM ".$config['dbPrefix']."_users WHERE userid ='".$_SESSION['id']."'";

	  $result = mysql_query($query, $connection);

						
      if ($result) {
   	    while ($row = mysql_fetch_row($result)) { //lese die Werte aus der Datenbank aus
		  $profil_name = $row[0];
		  $profil_email = $row[2];
		$imgurl = $row[3];
		$timezone_utc=$row[4];
		$user_language=$row[5];
echo "<form id=\"formularprofil\" action=\"profil.php\" name=\"form1\" method=\"post\" \">";
?>
                <div id="imgprofil">
                <div id="avatar">
                    <?php if(strlen($imgurl)>0){ echo "<img height='128' src='$imgurl'><br><br><br><br><br><br>"; } //Wenn ein Bild gesetzt ist, zeige es an
?>
                </div>
              </div>

                <div id="profilcontent">


<?php
echo "Externe Bild-URL:<input type=\"text\" name=\"img_url\" size=\"50\" maxlength=\"2083\" value=\"$imgurl\"class=\"textfeld\" /> <br><br><br>";
echo "<label>Anzeigename: <input type=\"text\" name=\"nickname\" id=\"nickname\" value=\"$profil_name\" class=\"textfeld\"></label><br><br>";
echo "<label>Passwort: <input type=\"password\" name=\"password\" id=\"password\" value=\"\" class=\"textfeld\"></label><br><br>";
echo "<label>E-Mail: <input type=\"text\" name=\"email\" id=\"email\" value=\"$profil_email\" class=\"textfeld\"></label><br><br>";
//Zeitzone Dropdown, wir erzeugen von -12 bis +12 eine Dropdownmenü und selktierne eins vorab, sofern in der Datenbank ein wert hinterlegt war
echo "<label>Zeitzone: <select name=\"timezone_utc\" size=\"1\" class=\"dropdownfelder\" ><br>";
for( $i=-12;$i<=12;$i++)
{
	echo "<option value=\"$i\"";
	if($i==1&&$timezone_utc>-13)
	{
	echo " selected";
	}
	if($timezone_utc==$i)
	{
	echo " selected";
	}
	echo ">$i</option>"; 
}
 	echo "</select>	</label><br><br>";
//Sprache, wie bei timezone wird auch hier der Wert aus der Datenbank vorselektiert sofern vorhanden
	echo "<label>Sprache: <select name=\"user_language\" size=\"1\" class=\"dropdownfelder\" ><br>";
	echo "<option value=\"0\"";
	if($user_language==0)
	echo " selected";
	echo ">Deutsch</option>"; 
 	echo "<option value=\"1\""; 
	if($user_language==1)
	echo " selected";	
	echo ">Englisch</option>"; 
	echo "<option value=\"2\""; 
	if($user_language==2)
	echo " selected";	
	echo ">Spanisch</option>"; 
	echo "<option value=\"3\""; 
	if($user_language==3)
	echo " selected";	
	echo ">Franz&ouml;sisch</option>"; 
 	echo "</select>	</label><br><br>";
		}
	  }


			 
 
	 
	  
	  mysql_close($connection);  
echo "<input type=\"submit\" class=\"buttons\"value=\"Aktualisieren\"  />"; 

echo "<a class=\"linksprofil\"  href=\"profil.php?delself=true\" onClick=\"return check();\" onmouseover=\"Tip('Achtung! Ihr Benutzerkonto wird komplett gel&ouml;scht und ist nicht wiederherstellbar!');\" onmouseout=\"tt_Hide();\" >Benutzerkonto k&uuml;ndigen</a>";
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

