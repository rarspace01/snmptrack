<?php session_start(); 
include_once("./config/config.php");
include("ticketfunctions.php"); //alle wichtigen funktionen rund um Tickets, schaft etwas übersichtlichkeit

if(strlen($_GET['delcomment'])>0&&strlen($_GET['tid'])>0) //Kommentar löschen
{
delComment($_GET['tid'],$_GET['delcomment'],$_SESSION['userlevel']);
}else if(strlen($_GET['tid'])<1&&strlen($_GET['q'])>0&&strlen($_POST['adddesc'])<1&&!$_POST['submit']=="Update"&&!$_POST['submit']=="Erstellen") //ticket suchen
{
echo "searchT";
searchTicket($_GET['q'],$_SESSION['userlevel']);
}else if(strlen($_GET['del'])>0&&strlen($_GET['tid'])>0) //ticket löschen
{
delTicket($_GET['tid'],$_SESSION['userlevel']);
}else if(strlen($_GET['tid'])>0&&strlen($_POST['adddesc'])<1&&!$_POST['submit']=="Update"&&!$_POST['submit']=="Erstellen") //ticket anzeigen
{
showTicket($_GET['tid'],$_SESSION['userlevel']);
}else if(strlen($_POST['longdesc'])>0&&strlen($_POST['title'])>0&&strlen($_SESSION['id'])>0&&$_POST['submit']=="Erstellen") //ticket erstellen
{
createTicket($_POST['projectid'],$_POST['catid'],$_POST['osid'],$_POST['longdesc'],$_POST['title'],$_SESSION['id'],$_SESSION['userlevel']);
}else if(strlen($_POST['longdesc'])>0&&strlen($_POST['title'])>0&&strlen($_SESSION['id'])>0&&$_POST['submit']=="Aktualisieren") //ticket akutalisieren
{
updateTicket($_POST['projectid'],$_POST['catid'],$_POST['osid'],$_POST['longdesc'],$_POST['title'],$_SESSION['id'],$_POST['ticketid'],$_SESSION['userlevel'],$_POST['ticketstatus']);
}else if(strlen($_SESSION['id'])>0&&strlen($_POST['adddesc'])>0) //kommentar hinzufügen
{
addcomment($_POST['adddesc'],$_SESSION['id'],$_POST['ticketid'],$_SESSION['userlevel']);
}
else //wenn garnichts zugetroffen hat, ticket erstellen anzeigen
{
showTicketcreation();
}

?>
