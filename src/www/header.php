<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
	
<html lang="de">
    <head>
        <meta name="generator" content="Bluefish 2.0.0" >
        <meta http-equiv="Content-Type" content=
        "text/html; charset=utf-8">
        <meta name="keywords" content="Ticket, Support, Bug">
        <meta name="description" content=
        "TINTS is no ticket system; Plattform um Supportstickets zu erstellen und Bugs zu loesen">
        <meta name="author" content="storminator" >

        <title>SNMP-Track</title>
        <link rel="stylesheet" type="text/css" href="styles/style.php?height=<?php echo 850+$additional; ?>">
        <link rel="stylesheet" type="text/css" href="styles/print.css" media="print">

						

<link rel="alternate" type="application/rss+xml" title="RSS" href="<?php echo $rssurl; ?>" />
        

<script language="javascript">AC_FL_RunContent = 0;</script> 
<script src="scripts/AC_RunActiveContent.js" language="javascript"></script>
    </head>
 
    <body>
<script type="text/javascript" src="scripts/config.js"></script>
         <script type="text/javascript" src="scripts/wz_tooltip.js"></script>
	<div id="seite">

        <div id="header">
            <img id="logo" src="images/logo.png" alt="Logo"> <a id=
            "banner" href="index.php"><?php

if(!strpos($_SERVER['PHP_SELF'],"index.php")===false){

?>
	<img src="images/tints_header.png"   alt="Banner">
<?php

}else{
?><img src="images/tints_header.png"   alt="Banner"><?php
}

 ?>
    		</a>
        </div>


		

        <div id="menucontainer">
			<a  href="show.php" class="neuesticket" onmouseover="Tip('Legen Sie ein neues Ticket an. Hierfür müssen sie eingeloggt sein.');" onmouseout="tt_Hide();"  > Neues Ticket </a> 
					<a href="show.php" class="neuesprofil" onmouseover="Tip('Ändern Sie Ihre Profileigenschaften. Hierfür müssen sie eingeloggt sein.');" onmouseout="tt_Hide();"> Profil</a>	

<a  href="show.php"class="news" onmouseover="Tip('Alle Switchs auf einen Blick.');" onmouseout="tt_Hide();"> Übersicht</a>

					<a  href="help.php"class="hilfe" onmouseover="Tip('Dokumentation für Tints');" onmouseout="tt_Hide();"> Hilfe</a>


					<img class="suchbild" src="images/navi/Searchbar.png" alt="Menupic">
						<div id="suchcontainer">
							<form method="get" action="<?php echo $_SERVER['PHP_SELF']; ?>">
								<input id="suchfeld" type="text" onfocus="this.value=''" onblur="checkInputBlur(this)" maxlength="30" name="q" id="suche" value="Suche...">
								<input id="sip" name="sip" type="hidden" value="<?php echo $_GET['sip']; ?>">
								<input id="pmac" name="pmac" type="hidden" value="<?php echo $_GET['pmac']; ?>">
								<input id="hmac" name="hmac" type="hidden" value="<?php echo $_GET['hmac']; ?>">
								<input id="suchbutton" type="image" name="submit" value="">
							</form>
						</div>
				</div>
				
<script type="text/javascript" >
function checkInputBlur(obj)
{
  if (obj.value=="") // Keine Änderung am Inhalt? Dann setze wieder Standardtext.
    obj.value = "Suche...";
}

</script>
