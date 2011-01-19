<?php

function handleLogin() //Funktion um User anzumelden, wir holen uns die userid und userlevel, sowie den usernamen. 
{

include("./config/config.php");

   $username = $_POST['username'];
	
	$passwd = $_POST['password'];
	$passwd = sha1("$passwd");
	
	$isPostBack = $_POST['login'];

	$loginquery="SELECT userid, username, passwd, userlevel FROM ".$config['dbPrefix']."_users WHERE username='$username' AND passwd='$passwd' AND userlevel>0;";
	
	if ($isPostBack) {
	  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);
	  $result = mysql_query($loginquery, $connection);		

		
						
      if ($result && $row = mysql_fetch_row($result)) {
		$_SESSION['id'] = $row[0];
		$_SESSION['name'] = $row[1];
		$_SESSION['userlevel'] = $row[3];

		echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=".$selfurl."\">");
	  }else
	{ //wenn der user nicht existiert/passwort falsch fehler anzeigen, optionale messagebox per JS
	echo "<script type=\"text/javascript\"> alert(\"Benutzername/Passwort falsch bzw. Account nicht aktiviert\");</script>
			<noscript><br> Anmeldung fehlgeschlagen!</noscript>";
	}
	  
	  mysql_close($connection);
	}

}

?>
