<?php
//Header und loginbox as usaual
session_start(); 
include("header.php");
include("loginbox.php");
?>

  

  <div id="content">

<h2>Sie haben folgende Möglichkeiten: </h2>

		<a href="ticket.php" class="uebersichtneuesticket" onmouseover="Tip('Erstellen Sie ein neues Ticket');" onmouseout="tt_Hide();"></a> 

		<a href="profil.php" class="uebersichtprofil" onmouseover="Tip('Aktualisieren Sie ihr Profil');" onmouseout="tt_Hide();"></a> 

		<a href="ticket.php?tid=-1" class="uebersichtalletickets" onmouseover="Tip('Alle Tickets im Blick ');" onmouseout="tt_Hide();"></a> 



</div>

<?php
include //footer wie üblich
            'footer.html';
?>
