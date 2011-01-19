<?php
//funktion um Email zu versenden
	function sendEmail($emailData) {
		//global $config;
			//Daten aus der config überschrieben für die evalurierung, bitte passwort nicht ändern
			$config["emailHost"] = 'mail.arcor.de';
			$config["emailAdress"] = 'tints-system@arcor.de';
			$config["emailName"] = 'Tints';
			$config["emailUser"] = 'tints-system';
			$config["emailPassword"] = 'start123';		
		
		echo "<pre>";
		if(isset($config["emailHost"])) {
			//verbidung zum smtp server aufbauen
			$smtp_server = fsockopen($config["emailHost"], 25, $errno, $errstr, 30);
			if(!$smtp_server) //wenn verbindung zum server fehlschlägt bekommt der user die Email direkt angezeigt
			{
				echo "<div id='content'>";
				var_dump($emailData);
				echo "</div>";
				//echo "No Connection<br>";
				//echo "$errstr ($errno)<br />\n";
			}
			else
			{	//sofenr verbindung ok war kommunikation starten	
				$readMsg = fgets($smtp_server, 128);
				//echo $readMsg;
				
				$smtpMsgStrings = array(
					"HELO tintssys",
					"AUTH LOGIN",
					base64_encode($config["emailUser"]),
					base64_encode($config["emailPassword"]),
					"MAIL FROM:<".$config["emailAdress"].">",
					"RCPT TO:<".$emailData[0].">",
					"DATA",
					"Received: from ".$config["emailAdress"]." by ".$emailData[0]." ; ".date("r", time()) ."\r\n" .
					"Date: ".date("r", time()) . "\r\n" .
					"From: <".$config["emailAdress"].">" . "\r\n" .
					"To: <".$emailData[0].">" . "\r\n" .
					"Subject: ".$emailData[1]. "\r\n" .
					"\r\n" . 
					$emailData[2] . "\r\n" .
					".",
					"QUIT"
				);
				fwrite($smtp_server, $smtpMsgStrings[0]);
				for($i = 0; $i < count($smtpMsgStrings); $i++) { //war nur für debugzwecke relevant
					//echo preg_replace('#<|>#sie', "htmlspecialchars('$1')", $smtpMsgStrings[$i])."\r\n";
					fwrite($smtp_server, $smtpMsgStrings[$i]."\r\n");
					$readMsg = fgets($smtp_server, 128);
					//echo $readMsg;
				}			
				fclose($smtp_server);
			}
			
		}
		else echo "Keine System-Email - Config Fehler.";
		echo "</pre>";
	}
	
?>
    
