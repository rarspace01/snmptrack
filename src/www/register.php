<?php
session_start(); 
//Header einbinden
include 'header.php';
//Login box nicht einbinden, da wir sonst umgeleitet werden würden
//include("loginbox.php");

//prüfen ob aktivierung
if(strlen($_GET['activate'])>0)
{
activateUser($_GET['activate'],$_GET['mail']); //aktiviere user
}else if(strlen($_GET['reset'])>0) //prüfen ob passwort zurücksetzten
{
resetPWD($_GET['reset']); //resette passwort
}else if(strlen($_POST['username'])>0) //prüfen ob Registrierung erfolgt
{
registerUser($_POST['username'],$_POST['password'],$_POST['password2'],$_POST['email']); //user anlegen
}else{ //seite normal anzeigen wenn keine parameter übergeben wurden

?>


        <div id="registrieren">
 			<h1>Registrierung</h1>
            <form id="formularregistrierung" action="register.php" method="post">
                <label>Benutzername: <input type="text" maxlength=
                "11" size="20" name="username" id="name" class=
                "textfeld" style="left:200px;"><br><br>
                </label> <label>Passwort <input type="password"
                maxlength="20" size="20" name="password" id="name"
                class="textfeld" style="left:200px;" ><br><br>
                </label> <label>Passwortwiederholung: <input type=
                "password" maxlength="20" size="20" name=
                "password2" id="name" class="textfeld" style="left:200px;"><br><br>
                </label> <label>E-Mail: <input type="text"
                maxlength="40" size="40" name="email" id="name"
                class="textfeld" style="left:200px;"><br><br>
                </label> <input type="submit" value="Registrieren" class="buttons" >
            </form>
        </div>
		
<?php

//footer einbinden
include 'footer.html';
}

//funktion zum überprüfen der Daten zum Anmeldung
function registerUser($username,$passwd,$passwd2,$email)
{
		if(strlen($username)>0 and strlen($passwd)>0 and strlen($passwd2)>0)
		{
			//check ob pwd1=pwd2 und email korrekt	
			if(strcmp($passwd,$passwd2)==0 and isValidEmail($email))
			{
				if(!isInDB($username,$email))//nur wenn user oder email nicht in DB machen wir weiter
				{
				createNewUser($username,$passwd,$email);//erstelle neuen User
			
				echo "<div id=\"registrieren\"><br>Ihr Accout wurde erstellt. Sie m&uuml;ssen den Account mit der soeben erhaltenen Email freischalten.</div>";
			
				}else
				{
				echo "<div id=\"registrieren\"><br>Sie sind bereits registriert. Passwort <a href='resetpassword.php>vergessen?</a></div>";
				}
			}else{
			echo "<div id=\"registrieren\"><br>Bitte geben sie das Passwort sowie die Emailadresse korrekt ein.</div>";
			}
			
		
		}else
		{
			echo "<div id=\"registrieren\"><br>Bitte geben sie die Daten korrekt ein.</div>";
		}
}

//funktion zum prüfen ob email oder username in DB
function isInDB($username,$email)
{
include("./config/config.php");

$isindb=false;

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	mysql_select_db($config['dbDatabase'], $connection);
	$result = mysql_query("SELECT COUNT(*) FROM ".$config['dbPrefix']."_users".
	                        " WHERE username LIKE '$username' OR email LIKE '$email'", $connection);

	$row = mysql_fetch_row($result);
	//wenn mehr als 0 dann ist einer in der DB
	if($row[0]>0)
	{
	$isindb=true;
	}

mysql_close($connection);

return $isindb;
}

//Funktion zum erstellen des Nutzers
function createNewUser($given_username,$given_pwd, $given_email)
{
include("./config/config.php");
include("sendEmail.php"); //wird verwendet um die registrierungsmail zu versenden, über diese muss sich der user aktivieren um sich einloggen zu können

$given_pwd=sha1($given_pwd);

	$regkey=generateRegisterKey();
	$regurl="http://".$_SERVER["SERVER_NAME"].$_SERVER["PHP_SELF"]."?activate=$regkey&mail=$given_email";

	$emailData[0]=$given_email; //Zieladresse
	$emailData[1]="Tints - Registration"; //Betreff
	$emailData[2]="Bitte aktiviere dein Account: $regurl"; //Email Text, kurz und knapp
	
	sendEmail($emailData);

	  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query("INSERT INTO ".$config['dbPrefix']."_users (username,passwd,email,registerkey) VALUES  ('$given_username','$given_pwd','$given_email','$regkey');", $connection);
//echo "<!-- Sofern wir Internet h&auml;tten, w&auml;re der Aktivierungslink: $regurl -->";

	 mysql_close($connection);

}

//erzeuge zufälligne registrierungscode
function generateRegisterKey()
{
return sha1(time());
}

//einfache Funktion für simple Überprüfung ob Email valide ist
function isValidEmail($givenEmail)
{

	$returnEmail=false;

	$pos = strpos($givenEmail,"@");

	if($pos === false) {
	}
	else {
	$returnEmail=true;
	}

	$pos = strpos($givenEmail,".");

	if($pos === false) {
	}
	else {
	$returnEmail=true;
	}

	 return($returnEmail);
}


//aktiviere user
function activateUser($activatekey,$email)
{
include("./config/config.php");

//Überprüfung ob user in DB obsolette, weil wir sowieso nur den status ändern bei dem user der auch wirklich existiert

//query für update, zusätzlich userlevel check, damit kein admin sich degradiert ausversehen
$query="UPDATE tints_users SET userlevel=1 WHERE registerkey='$activatekey' AND email='$email' AND userlevel='0'"; 

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);

echo "<div id=\"registrieren\"><br>Aktivierung beendet. Sie werden in 5 Sekunden weitergeleitet zum Login.</div>";
	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"5; URL=index.php\">");

}


//passwort zurücksetzten funktion
function resetPWD($emailadress)
{
include("./config/config.php");
include("sendEmail.php"); //wiederum für den Mailversand, diesmal mit dem neuen Passwort

	$neuespasswort=md5(generateRegisterKey()+rand(1,999)); //Als neues Passwort dient einfach ein Registrierungskey + etwas zufall und das ganez als md5 damit es nicht so alnge ist

	$emailData[0]=$emailadress; //Empfänger
	$emailData[1]="Tints - Passwort Reset"; //Betreff
	$emailData[2]="Dein neues Passwort lautet: $neuespasswort"; //Inhalt der nachricht

	$neuespasswort=sha1($neuespasswort); //erst hier hashen da der user sonst nicht das PWD bekäme sondern nru dne hash
	
	$query="UPDATE ".$config['dbPrefix']."_users set passwd='".$neuespasswort."' WHERE email='".$emailadress."';";

	  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($query, $connection);


	sendEmail($emailData);

	 mysql_close($connection);

echo "<div id=\"registrieren\"><br>Neues Password wurde zugesendet. Sie werden in 5 Sekunden weitergeleitet zum Login.</div>";
	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"5; URL=index.php\">");
}


?>
