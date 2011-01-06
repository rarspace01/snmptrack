<?php //hier wird der Login abgehandelt

if($_GET['logout']=="true") //wenn logout=true als get
{
//clean session
$_SESSION[]=array();
if(isset($_COOKIE[session_name()])){
setcookie(session_name(),'',time()-42000,'/');
}
session_destroy();
echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=index.php\">"); //redirekt auf die frontseite
die(); //wir brechen heir ab um keine weiteren daten anzuzeigen
}

?>
