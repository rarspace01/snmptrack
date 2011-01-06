<?php

function showMostactivity() // ist für die Anzeige auf der Frontseite, Meiste Aktivität zustädnig
{
include("./config/config.php");
include("ticketfunctions.php"); //wir brauchen Hierovn die Funktion um aus den ticket status zahlen Texte zu erzeugen showstatus();


//Diese Abfrage ist zeigt alle Tickets bei denen Kommentare existieren, sortiert nach der Anzahl an, jedoch nur die 5 "aktivsten" Tickets 
$query="SELECT COUNT(*) AS ticketcount,tints_comment.ticketid,tints_tickets.title,tints_projects.projectid,catid,osid,tints_tickets.userid,tints_users.username,tints_tickets.status,tints_projects.projecttitle FROM tints_comment,tints_tickets,tints_users,tints_projects WHERE tints_tickets.projectid=tints_projects.projectid AND tints_tickets.userid=tints_users.userid AND tints_comment.ticketid=tints_tickets.ticketid GROUP BY tints_comment.ticketid ORDER BY COUNT(*) DESC LIMIT 5;";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {
//Table Header
echo "<table>
		<th style=\"text-align:center;\">Projekt</th>
		<th style=\"text-align:center;\">Ticketbeschreibung</th>
		<th style=\"text-align:center;\">Status</th>
		<th style=\"text-align:center;\">Username</th>
		<th style=\"text-align:center;\">Beiträge</th>
		";
//Alle Tickets anzeigen
   	    while ($row = mysql_fetch_row($result)) {
		$ticketcount=$row[0];
		$ticketid=$row[1];
		$ticketdesc=$row[2];
		$projectid=$row[3];
		$username=$row[7];
		$ticketstatus=$row[8];
		$projecttitle=$row[9];

echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$projecttitle</td>
		<td style=\"text-align:center;width:15px;\"><a href='ticket.php?tid=$ticketid'>$ticketdesc</a></td>
		<td style=\"text-align:center;width:10px;\">"; 

printStatus($ticketstatus);  //Funktion aus: "ticketfunctions.php", brauchen diese Funktion um aus den ticket status zahlen Texte zu erzeugen

		echo "</td>
		<td style=\"text-align:center;width:10px;\">$username</td>
		<td style=\"text-align:center;width:10px;\">$ticketcount</td>		
		</tr>";
		}	

		echo "</table>";

		}

}


?>
