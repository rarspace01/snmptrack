<?php

//Hinweis: um schnell zu den gewünschtne funktionen zu kommen bitte suchen funktion im editor verwenden

function showTicketcreation() //Funktion zum ticket anzeigen
{

include("header.php");
include("loginbox.php");
?>

	
 <div id="content">  
 
   <h1>Neues Ticket erstellen</h1>
   
  <form action="ticket.php" method="post" id="ticketformular">
	<label> <b>Projekt:</b>  </label> <?php printProjectDropdown(-1);//Projektdropdown anzeigen ?>  <br>
	
	<label> <b>Kategorie:</b> </label> <?php printCatDropdown(-1);//Categoriedropdown anzeigen ?> <br>

   <label>	<b> Betriebsystem:</b> </label> <?php printOSDropdown(-1);//OSdropdown anzeigen ?> <br>

	<label> <b> Kurzbeschreibung:</b> </label> <input type="text" onfocus="this.value=''"   class="ticketelement" maxlength="27" size="40" name="title" value="Bitte Betreff eingeben."/> <br> <br>
	<label> <b>ausführliche Beschreibung:</b> </label>  <textarea name="longdesc" onfocus="this.value=''"  class="ticketelement" cols="50" rows="10">Bitte hier Ticketbeschreibung eingeben.</textarea> <br> <br> <br> <br> <br><br> <br> <br> <br><br> <br>
	 <input type="submit" class="buttons" name="submit" value="Erstellen" /> <br>
  </form>
 </div>
 
 

<?php
include 
            'footer.html';
}

//Funktion für Dropdown Projekt
function printProjectDropdown($id)
{
include("./config/config.php");

echo "<select name=\"projectid\" class=\"ticketelement\" size=\"1\">";

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query("SELECT projectid,projecttitle FROM tints_projects ORDER BY projectid ASC;", $connection);

   if ($result) {
   	    while ($row = mysql_fetch_row($result)) {
		  $projectid = $row[0];
		  $projecttitle = $row[1];
			if($id==$projectid)//wenn aktuelles Projekt selektiert
			{
			echo "<option value=\"$projectid\"   selected>$projecttitle</option>";	
			}else
			{
			echo "<option value=\"$projectid\"  >$projecttitle</option>";
			}
		}
	  }
	  
	  mysql_close($connection);  

echo "</select> <br>";

}

//Funktion für Dropdown kategorie
function printCatDropdown($id)
{
include("./config/config.php");

echo "<select class=\"ticketelement\" name=\"catid\" size=\"1\">";

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query("SELECT catid,cattitle FROM tints_categories ORDER BY catid ASC;", $connection);

   if ($result) {
   	    while ($row = mysql_fetch_row($result)) {
		  $cattid = $row[0];
		  $cattitle = $row[1];
			if($id==$cattid)//wenn aktuelle Kategorie selektiert
			{
			echo "<option value=\"$cattid\"  selected>$cattitle</option>";	
			}else
			{
			echo "<option value=\"$cattid\"  >$cattitle</option>";
			}
		}
	  }
	  
	  mysql_close($connection);  

echo "</select> <br>";

}

//Funktion für Dropdown Betriebsystem
function printOSDropdown($id)
{
include("./config/config.php");

echo "<select class=\"ticketelement\" name=\"osid\" size=\"1\">";

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query("SELECT osid,title,version FROM tints_os ORDER BY osid ASC;", $connection);

   if ($result) {
   	    while ($row = mysql_fetch_row($result)) {
		  $osid = $row[0];
		  $ostitle = $row[1]." ".$row[2];

			if($id==$osid)//wenn aktuelles Betriebsystem selektiert
			{
			echo "<option value=\"$osid\" selected>$ostitle</option>";	
			}else
			{
			echo "<option value=\"$osid\">$ostitle</option>";
			}
		}
	  }
	  
	  mysql_close($connection);  

echo "</select> <br>";

}

//Funktion um 1 oder viele tickets anzuzeigen
function showTicket($ticket_sid,$userlevel)
{
include("./config/config.php");


if($ticket_sid<0) //wenn ticketid negativ dann "alle" (nur 15) tickets anzeigen
{
$query="SELECT tints_tickets.ticketid,tints_tickets.title,tints_projects.projectid,catid,osid,tints_tickets.userid,tints_users.username,tints_tickets.status,tints_projects.projecttitle FROM tints_tickets,tints_users,tints_projects WHERE tints_tickets.projectid=tints_projects.projectid AND tints_tickets.userid=tints_users.userid ORDER BY tints_tickets.ticketid DESC LIMIT 15;";
}else
{
$query="SELECT tints_tickets.userid,title,ticketdesc,projectid,catid,osid,username,ticketid,status FROM tints_tickets,tints_users WHERE ticketid='".$ticket_sid."' AND tints_tickets.userid=tints_users.userid ORDER BY ticketid;";
//get commentscount - benötigen wir für die dynamische css datei, sofern sehr viele kommentare in der DB sind
$comment_counter=countcomments($ticket_sid);
}



include("header.php");
include("loginbox.php");
?>
	
 <div id="content" style="height: auto">  

<?php

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {

		if($ticket_sid<0)//fragestellung wie oben, 1 oder alle tickets
		{
			//Tabellen header
		echo "<h1>Alle Tickets im Blick</h1><table>
		<th style=\"padding: 5px;text-align:center;\">Ticket-Nr.</th>
		<th style=\"padding: 5px;text-align:center;\">Projekt</th>
		<th style=\"padding: 5px;text-align:center;\">Betreff des Tickets</th>
		<th style=\"padding: 5px;text-align:center;\">Status</th>
		<th style=\"padding: 5px;text-align:center;\">Verfasser</th>
		";	
			
	   	while ($row = mysql_fetch_row($result)) {
		$ticketid=$row[0];
		$ticketdesc=$row[1];
		$userid=$row[5];
		$username=$row[6];
		$ticketstatus=$row[7];
		$projecttitle=$row[8];
		//1 Zeile = 1 Ticket, Tabellen einträge
		echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$ticketid</td>
		<td style=\"text-align:center;width:10px;\">$projecttitle</td>
		<td style=\"padding-left:5px;\"><a href=\"ticket.php?tid=$ticketid\">$ticketdesc</a></td>
		<td style=\"text-align:center;width:10px;\">"; 
		printStatus($ticketstatus);  //wie bei overviewfunctions, wird benutzt um zahl in text umzuwandeln
		echo "</td>
		<td style=\"text-align:center;width:10px;\">$username</td>		
		</tr>";
			}

			echo "</table>	";

			echo "</div>";

			include 'footer.html';

			}else{ //nur 1 TIcket anzeigen-modus
	   	

   	    while ($row = mysql_fetch_row($result)) {
		$userid=$row[0];
		$title=$row[1];
		$ticketdesc=$row[2];
		$projectid=$row[3];
		$catid=$row[4];
		$osid=$row[5];
		$username=$row[6];
		$ticketid=$row[7];
		$ticketstatus=$row[8];

?>
   <h1>Ticket anzeigen</h1>
   <form action="<?php echo "ticket.php?tid=".$_GET['tid']; ?>" method="post" style="width:750px;">
<?php
if ($userlevel>1)
{
echo "<a href=\"ticket.php?del=true&tid=$ticketid&a=$userlevel\" class=\"linksprofil\"  href=\"profil.php?delself=true\" onmouseover=\"Tip('Achtung! Das Ticket wird komplett gel&ouml;scht und ist nicht wiederherstellbar!');\" onmouseout=\"tt_Hide();\" >L&ouml;sche Ticket</a>";;
}
?>

	<input type="hidden" name="ticketid" value="<?php echo $ticketid; ?>"/>
	<label><b> TicketID: </b></label>  <?php echo $ticketid; ?> <br><br>
	<label> <b> Ticketstatus: </b> <label> <?php printStatusDropdown($ticketstatus); ?> <br>
<label> <b>Projekt:</b> </label>	 <?php printProjectDropdown($projectid); ?> <br>
	
<label> <b>Kategorie:</b> </label>	 <?php printCatDropdown($catid);  ?> <br>

 <label><b>Betriebsystem:</b> </label>  	 <?php printOSDropdown($osid); ?> <br>

<label> <b>Kurzbeschreibung:</b> </label>	  <input type="text" class="ticketelement" maxlength="27" size="40" name="title" value="<?php echo $title; ?>" <?php if($userlevel<1){echo "readonly=true"; } ?>/> <br> <br>
<label><b>ausführliche Beschreibung:</b> </label>	 <textarea class="ticketelement" name="longdesc" cols="50" rows="7" <?php if($userlevel<1){echo "readonly=true"; } ?>><?php echo $ticketdesc; ?></textarea> <br> <br> <br> <br> <br><br> <br> 

	<?php 
	if($userlevel>1){echo "<input type=\"submit\" class=\"buttons\" name=\"submit\" value=\"Aktualisieren\" /> <br><br>"; } //wenn admin, dann ermögliche Ticketbeschriebung zu ändern
	
	//zeige kommentare
	
	echo "<div id=\"ticketdiv\" style=\"height: auto \">";
	
	showcomments($ticketid,$userlevel);

	echo "</div>";

	//kommentar hinzufügen n anzeigen wenn kein gast
	if($userlevel>0)
	{

	?>	
	
<label><b>Kommentar hinzufügen:</b>	 </label> <textarea name="adddesc" class="ticketelement" cols="50" rows="7"></textarea>  <br> <br> <br><br> <br> <br> <br>

	<input type="submit" class="buttons" name="addcomment" value="Hinzuf&uuml;gen">
	 
  </form>



<?php
	}//ende if gast
		

		} //ende der kommentare 
		
		}//ende while
}//ende if

?>

</div>

</html>
<?php

} 


//funktion um tickets zu löschen
function delTicket($ticket_sid,$userlevel)
{
include("./config/config.php");

if($userlevel>1)//prüfe ob admin
{
//Erst alle Kommentare entfernen die zum ticket gehören
$query="DELETE FROM tints_comment WHERE ticketid=$ticket_sid";

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);



	  $result = mysql_query($query, $connection);

	
	  
	  mysql_close($connection);

//Ticket selbst löschen
$query="DELETE FROM tints_tickets WHERE ticketid=$ticket_sid";

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);



	  $result = mysql_query($query, $connection);
	  
	  mysql_close($connection);

	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=ticket.php?tid=-1\">");

	echo "Ihr Ticket wurde gelöscht. Sie werden zur &Uuml;bersicht umgeleitet.";

}else
{
echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=ticket.php?tid=-1\">"); //wenn ein nicht admin versucht hatte etwas zu löschen (nur durch manipulation möglich)
}

}


//Kommentar löschen
function delComment($ticket_sid,$commentid,$userlevel)
{
include("./config/config.php");

if($userlevel>1) //prüfe ob admin
{
//lösche kommentarid X von ticketid Y
$query="DELETE FROM tints_comment WHERE ticketid=$ticket_sid AND commentid=$commentid";

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);



	  $result = mysql_query($query, $connection);

	
	  
	  mysql_close($connection);

	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=ticket.php?tid=$ticket_sid\">"); //redirct zum ticket wo gelöscht wurde

	echo "Ihr Kommentar wurde gelöscht. Sie werden zum Ticket umgeleitet.";

}

}

//Tickets suchen
function searchTicket($searchstring,$userlevel)
{
include("./config/config.php");

//zeige alle tickets deren ticket-titel oder ticket beschreibung den gewünschten suchbegriff enthält, maximal 15 stück
$query="SELECT tints_tickets.ticketid,tints_tickets.title,tints_projects.projectid,catid,osid,tints_tickets.userid,tints_users.username,tints_tickets.status,tints_projects.projecttitle FROM tints_tickets,tints_users,tints_projects WHERE tints_tickets.projectid=tints_projects.projectid AND tints_tickets.userid=tints_users.userid AND (tints_tickets.ticketdesc LIKE '%$searchstring%' OR tints_tickets.title LIKE '%$searchstring%') ORDER BY tints_tickets.ticketid DESC  LIMIT 15;";

include("header.php");
include("loginbox.php");
?>	
	
 <div id="content">  

<?php
//table header
echo "<table>
		<th style=\"\">Ticketnr</th>
		<th style=\"padding-left:5px;\">Projekt</th>
		<th style=\"text-align:center;\">Betreff des Tickets</th>
		<th style=\"text-align:center;\">Status</th>
		<th style=\"\">Benutzername</th>
		";		



 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {
   	    while ($row = mysql_fetch_row($result)) {
		$ticketid=$row[0];
		$ticketdesc=$row[1];
		$userid=$row[5];
		$username=$row[6];
		$ticketstatus=$row[7];
		$projecttitle=$row[8];
//einzelne tickets jeweils wieder als zeile ausgegeben in der tabelle
echo "
		<tr>
		<td style=\"text-align:center;width:10px;\">$ticketid</td>
		<td style=\"text-align:center;width:10px;\">$projecttitle</td>
		<td style=\"padding-left:5px;\"><a href=\"ticket.php?tid=$ticketid\">$ticketdesc</a></td>
		<td style=\"text-align:center;width:10px;\">"; 
		printStatus($ticketstatus); 
		echo "</td>
		<td style=\"text-align:center;width:10px;\">$username</td>		
		</tr>";
}


		
	  }
echo "</table>";
?>

</div>
<?php

include 'footer.html';
	  

}

//ticket erstellen
function createTicket($projectid,$catid,$osid,$longdesc,$title,$id,$userlevel)
{
include("./config/config.php");

if($userlevel>0) //user muss mindestens aktiviert sein
{


  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	$query="INSERT INTO tints_tickets (userid,title,ticketdesc,projectid,catid,osid) VALUES ('$id','$title','$longdesc','$projectid','$catid','$osid');";

	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=ticket.php?tid=-1\">");

	echo "Ihr Ticket wurde angelegt. Sie werden nun weitergeleitet zur &Uuml;bersicht.";

	  $result = mysql_query($query, $connection);
	  
	  mysql_close($connection);  
}

}


//ticket aktualisieren
function updateTicket($projectid,$catid,$osid,$longdesc,$title,$id,$ticketid,$userlevel,$ticketstatus)
{
include("./config/config.php");

if($userlevel>0)  //user muss mindestens aktiviert sein
{

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	$query="Update tints_tickets SET userid='$id',title='$title',ticketdesc='$longdesc',projectid='$projectid',catid='$catid',osid='$osid', status='$ticketstatus' WHERE ticketid='$ticketid';";
	
	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=ticket.php?tid=$ticketid\">");

	echo "Ihr Ticket wurde aktualisiert. Sie werden zum Ticket weitergeleitet...";


	  $result = mysql_query($query, $connection);
	  
	  mysql_close($connection);  
}

}

//Kommentar hinzufügen
function addcomment($addtxt,$userid,$ticketid,$userlevel)
{

if($userlevel>0) //user muss mindestens aktiviert sein
{


include("./config/config.php");

  $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	$query="INSERT INTO tints_comment (ticketid,comment,userid) VALUES ('$ticketid','$addtxt','$userid');";

	  $result = mysql_query($query, $connection);
	  
	  mysql_close($connection);
  
	echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=ticket.php?tid=$ticketid\">");

	echo "Ihr Kommentar wurde angelegt. Sie werden weitergeleitet...";

}

}

//funktion zum anzeigen der Kommentare
function showcomments($ticketid,$userlevel)
{

include("./config/config.php");

//alle kommentare chronologisch ausgeben
$query="SELECT commentid,comment,tints_comment.userid,username from tints_comment, tints_users WHERE tints_comment.userid=tints_users.userid AND ticketid=$ticketid ORDER BY commentid;";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {
   	    while ($row = mysql_fetch_row($result)) {
		$commentid=$row[0];
		$comment=$row[1];
		$commentuid=$row[2]; //userid
		$commentusername=$row[3]; //username
		
	?>	
	
 <label><b><?php echo $commentusername; ?> schrieb:</b></label>	  <textarea class="ticketelement" cols="50" rows="10" readonly=true><? echo $comment; ?></textarea>	<br> <br><?php

if($userlevel>1) //wenn admin, löschen option anzeigen
{ 
echo "Kommentar <a href=\"ticket.php?delcomment=$commentid&tid=$ticketid\">l&ouml;schen</a>";
} ?>

<br><br><br><br><br><br><br><br><br>

	<?php
		
	}//for while

	}
}

//anzahl der Kommentare berechnen bei einem ticket, wird benötigt für dynamischen style
function countcomments($ticketid)
{

include("./config/config.php");

$query="SELECT count(*) from tints_comment WHERE ticketid=$ticketid";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {
   	    $row = mysql_fetch_row($result);
		$count=$row[0];
		return $count;		
}


}

//Ticket status dropwdown ausgeben, selektion möglich
function printStatusDropdown($statusid)
{

//da Ticketstatus nicht in der Datenbank gespeichert ist müssen wir das ganze manuell anzeigen

echo "<select class=\"ticketelement\" name=\"ticketstatus\" size=\"1\">";


echo "<option value=\"0\""; 
if($statusid==0){ echo " selected"; }
echo ">Neu</option>";

echo "<option value=\"1\"";
if($statusid==1){ echo " selected"; }
echo ">Zugewiesen</option>";

echo "<option value=\"2\"";
if($statusid==2){ echo " selected"; }
echo ">In Arbeit</option>";

echo "<option value=\"3\"";
if($statusid==3){ echo " selected"; }
echo ">Gel&ouml;st</option>";

echo "<option value=\"4\"";
if($statusid==4){ echo " selected"; }
echo ">Nicht l&ouml;sbar</option>";	


echo "</select> <br>";

}

//Funktion zum einfachen umwandeln, ticketstatus zahl -> text
function printStatus($statusid)
{

//da Ticketstatus nicht in der Datenbank gespeichert ist müssen wir das ganze manuell anzeigen

switch ($statusid) {
    case 0:
        echo "Neu";
	break;
    case 1:
        echo "Zugewiesen";
	break;
    case 2:
        echo "In Arbeit";
	break;
    case 3:
        echo "Gel&ouml;st";
	break;
    case 4:
        echo "Nicht l&ouml;sbar";
	break;
}



}

?>
