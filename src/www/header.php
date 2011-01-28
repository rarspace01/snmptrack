<?php

if(strlen($_SESSION['userlevel'])>0){
}else{

if(strpos($_SERVER['PHP_SELF'], "login.php")===false){
echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=login.php\">");
die;
}


}

?><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
	
<html lang="de">
    <head>
    	<meta http-equiv="refresh" content="900" />
        <meta name="generator" content="Bluefish 2.0.0" >
        <meta http-equiv="Content-Type" content=
        "text/html; charset=utf-8">
        <meta name="keywords" content="Ticket, Support, Bug">
        <meta name="description" content=
        "TINTS is no ticket system; Plattform um Supportstickets zu erstellen und Bugs zu loesen">
        <meta name="author" content="Denis and tints" >

        <title>SNMP-Track</title>
        <link rel="stylesheet" type="text/css" href="styles/style.php?height=<?php echo 850+$additional; ?>&width=<?php echo $pagewidth; ?>">
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
            <img id="logo" src="images/icons/snmptrack_slow.gif" alt="Logo"> <a id=
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
			<a  href="index.php" class="neuesticket" onmouseover="Tip('Legen Sie ein neues Ticket an. HierfÃ¼r mÃ¼ssen sie eingeloggt sein.');" onmouseout="tt_Hide();"  > &Uuml;bersicht </a> 
					<a  href="duplic.php" class="news" onmouseover="Tip('Alle Duplikate auf einen Blick.');" onmouseout="tt_Hide();"> Duplikate</a>
					<a href="profil.php" class="neuesprofil" onmouseover="Tip('Ã„ndern Sie Ihre Profileigenschaften. HierfÃ¼r mÃ¼ssen sie eingeloggt sein.');" onmouseout="tt_Hide();">Profil</a>	


					<a  href="help.php"class="hilfe" onmouseover="Tip('Dokumentation fÃ¼r Tints');" onmouseout="tt_Hide();"> Hilfe</a>


					<img class="suchbild" src="images/navi/Searchbar.png" alt="Menupic">
						<div id="suchcontainer">
							<form method="get" action="<?php 
							if(strpos($_SERVER['PHP_SELF'],"show.php")===false)
							{
							echo "show.php";
							}else{
							echo $_SERVER['PHP_SELF'];	
							}
							 ?>">
							 	
							 	<?php 
							 	if(strpos($_SERVER['PHP_SELF'],"login.php")===false){
							 		
							 	
							 	?>
							 	
							 	<script type="text/javascript">
								   function formfocus() {
								      document.getElementById('suchfeld').focus();
								   }
								   window.onload = formfocus;
								</script>
							 
							 	<?php 
							 	
							 	}else if(!strpos($_SERVER['PHP_SELF'],"login.php")===false){
							 		
							 									 	?>
							 	
							 	<script type="text/javascript">
								   function formfocus() {
								      document.getElementById('idusername').focus();
								   }
								   window.onload = formfocus;
								</script>
							 
							 	<?php
							 		
							 	}
							 	
							 	?>
							 
								<input id="suchfeld" type="text" maxlength="30" name="q" id="suche" value="<?php 
								
								if(strlen($_GET['q'])>0){
									echo $_GET['q'];
								}
								
								?>">
								<input id="sip" name="sip" type="hidden" value="<?php echo $_GET['sip']; ?>">
								<input id="pmac" name="pmac" type="hidden" value="<?php
								if(strpos($_SERVER['PHP_SELF'],"show.php")===false){	
									echo "?";							
								}else{
									
									if(isset($_GET['pmac'])){	
									echo $_GET['pmac'];	
									}else{
									echo "?";	
									}									
								}
								?>">
								<input id="hmac" name="hmac" type="hidden" value="<?php echo $_GET['hmac']; ?>">
								<input id="suchbutton" type="image" name="submit" value="">
							</form>
						</div>
				</div>
				
<script type="text/javascript" >

</script>