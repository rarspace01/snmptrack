<?php 
//Header Typ muss gesetzt werden, da sonst Browser CSS Code nicht laden kann
header("Content-type: text/css"); ?>

/**************************************************************************************************
Diese Notiz muss in allen veränderten Quellcodedateien enthalten leiben

style.php aus TiNts v. 1.0.0

Die neueste Version ist immer verfügbar unter
http://sourceforge.net/projects/tints-system/

Erstellt am 22.04.2010 von Andre Lannert, Denis Hamann  und Patrick&Ouml; Mey&Ouml;fer

Design für die Layoutansicht des Benutzers 
LIZENZ: EUPL

TiNts und alle dazugehörigen Dateien sind freie Software; Sie können diese 
anpassen und modifizieren unter den Bedingungen der European Public Licence (EUPL).
***************************************************************************************************/

/*=============================Beginn Dokumentation======================================*/


/*Grundsaetzliche Body-Angaben*/
body {
    font-family:verdana,arial,sans-serif;
    background-color:#F9F9F9;
 	 background:url("../images/background.png") repeat-x scroll 0 0 #3E3E3E;
 	 line-height:20px;
}

/*Inhalt mittig ausrichten*/
#seite{
			position: relative;
    		width: <?php 
    	if(isset($_GET['width'])) //Wurde von remote eine Höhe gesetzt? Wenn ja gebe sie aus, ansonsten nehme Konstante
   		{
   			echo $_GET['width'];
   			}else {
   		echo "1200";
  			}
    		?>px;
    		margin: 0 auto;
   		height: <?php
   		if(isset($_GET['height'])) //Wurde von remote eine Höhe gesetzt? Wenn ja gebe sie aus, ansonsten nehme Konstante
   		{
   			echo $_GET['height'];
   			}else {
   		echo "850";
  			}
   		?>px;
   		background-color:white;	
	}

/*Layout fuer Impressum*/
	#seiteimpressum {
		position: absolute;
		text-align: left;
		width: 800px;
		top: 145px;
		left: 201px;
		margin: 0 auto;	
		height: 1100px;	
		background-color: white;				
		}

/*Layout fuer Hilfe*/
	#seitehilfe {
		position: absolute;
		text-align: left;
		width: 800px;
		top: 145px;
		left: 201px;
		margin: 0 auto;	
		height: 1069px	
		background-color: white;				
		}

/* allgemeine Headerangaben    */
#header {
    background-color: white;
    width: 100%;
    height: 103px;
    padding: 0px;
    margin: 0px;
}
/*   Footer der Webseite    */
#footer {
    
		background:none repeat scroll 0 0 #FFFFFF;
		border-top:1px solid #e3e3e3;
		color:black;
		line-height:2em;
		margin:1em;
		padding:1em 0 0;
		font-family: verdana, arial, helvetica, geneva, sans-serif;
    	font-size: 12px;  
    	position: absolute;
   	 width: 890px;
   	 top: <?php
   		if(isset($_GET['height'])) //Wurde von remote eine Höhe gesetzt? Wenn ja gebe sie aus, ansonsten nehme Konstante
   		{
   			echo $_GET['height']-50;
   			}else {
   		echo "850";
  			}
   		?>px;
   	 padding-left: 190px;
} 

/*  Gesamter Menuecontainer     */
#menucontainer {
    position: absolute;
	 top: 104px;
    height: 38px;       
    width: auto;
    left: 200px;
    font-family: verdana, arial, helvetica, geneva, sans-serif;
}
/*  Hintergrundbilder fuer Navigation, Zentriert(horizontal und vertikal)    
	 Menuepunkt Neues Ticket     */
.neuesticket, .neuesticket a:visited, .neuesticket a:link {
		background-image:url(../images/navi/NeuesTicket.png);
		display:block;	
		position:relative;
		width: 133px;
		height: 36px;
		line-height: 36px;
		font-weight: bold;
		margin-left: 0;
		margin-right: 0;
		float: left;
		text-align: center;
		color: black;
	}
	
/*  hover Effekt fuer Mouseover     
    Menuepunkt Neues Ticket     */
.neuesticket:hover, .neuesticket:active, .neuesticket:focus {
		background-image:url(../images/navi/NeuesTicket2.png);
		color: white;
	}
/*  Hintergrundbilder fuer Navigation, Zentriert(horizontal und vertikal)     */	
.neuesprofil, .neuesprofil a:visited, .neuesprofil a:link {
		background-image:url(../images/navi/Profil.png);
		display:block;	
		position:relative;
		width: 133px;
		height: 36px;
		line-height: 36px;
		font-weight: bold;
		margin-left: 0;
		margin-right: 0;
		float: left;
		text-align: center;
		color: black;
	}

/*  Hover-Effekt fuer Menuepunkt Neues Profil     */	
.neuesprofil:hover, .neuesprofil:active, .neuesprofil:focus {
		background-image:url(../images/navi/Profil2.png);
		color: white
	}	

/*  Vorgaben fuer Menuepunkt Uebersicht    */
.news, .news a:visited, .news a:link {
		background-image:url(../images/navi/NewsTOPs.png);
		display:block;	
		position:relative;
		width: 133px;
		height: 36px;
		line-height: 36px;
		font-weight: bold;
		margin-left: 0;
		margin-right: 0;
		float: left;
		text-align: center;
		color: black;
	}
	
/*  Hover-Effekt fuer Menuepunkt Uebersicht    */	
.news:hover, .news:active, .news:focus {
		background-image:url(../images/navi/NewsTOPs2.png);
		color: white;
	}	
	
/*  Vorgaben fuer Menuepunkt Hilfe    */	
.hilfe, .hilfe a:visited, .hilfe a:link {
		background-image:url(../images/navi/Hilfe.png);
		display:block;	
		position:relative;
		width: 133px;
		height: 36px;
		line-height: 36px;
		font-weight: bold;
		margin-left: 0;
		margin-right: 0;
		float: left;
		text-align: center;
		color: black;
	}
/*  Hover-Effekt fuer Menuepunkt Hilfe    */		
.hilfe:hover, .hilfe:active, .hilfe:focus {
		background-image:url(../images/navi/Hilfe2.png);
		color: white;
	}	

/*  Icons fuer overview.php Ticket erstellen    */	
.uebersichtneuesticket{
		background-image:url(../images/uebersicht/ticket_erstellen.png);
		margin-left: 50px;
		height: 100px;
		width: 70px;
		float: left;
}

/*  Icons fuer overview.php Ticket erstellen hover-Effekt  */	
.uebersichtneuesticket:hover, .uebersichtneuesticket:active, .uebersichtneuesticket:focus {
		background-image:url(../images/uebersicht/ticket_erstellen2.png);
	}

/* Icons fuer overview.php Profil bearbeiten   */	
.uebersichtprofil{
		background-image:url(../images/uebersicht/profil_bearbeiten.png);
		margin-left: 50px;
		height: 100px;
		width: 70px;
		float: left;
}

/*  Icons fuer overview.php Profil bearbeiten hover-Effekt    */	
.uebersichtprofil:hover, .uebersichtprofil:active, .uebersichtprofil:focus {
		background-image:url(../images/uebersicht/profil_bearbeiten2.png);
	}
	
/*   Icons fuer overview.php Alle Tickets bearbeiten   */	
.uebersichtalletickets{
		background-image:url(../images/uebersicht/ticket_alle.png);
		margin-left: 50px;
		height: 100px;
		width: 70px;
		float: left;
}

/*   Icons fuer overview.php Alle Tickets bearbeiten hover-Effekt   */	
.uebersichtalletickets:hover, .uebersichtalletickets:active, .uebersichtalletickets:focus {
		background-image:url(../images/uebersicht/ticket_alle2.png);
	}
	
/*  Bild fuer die Suche (Lupe)    */	
.suchbild {
	background-image:url(../images/navi/search.png);
	position:relative;
	width: 255px;;
	height: 36px;
	line-height: 36px;
	margin-left: 0;
	margin-right: 0;
	float: left;
	}	

/*   Suchbild hover-Effekt   */		
.suchbild:hover, .suchbild:active, .suchbild:focus {
		background-image:url(../images/navi/search2.png);	
	}



/*   Loginbox am linken Bildrand   */
#loginbox {
    position: absolute;
    border: solid 1px ;
    background-color: #EBEBEB;
    top: 142px;
    left: 0px;
    width: 120px;
    height: 200px;
    padding-top: 20px;
    padding-left: 5px;
    margin-left: 2px;    
    -moz-border-radius: 20px;
    -webkit-border-radius: 10px;
}

/*   Inhalt auf der Startseite   */
#content {
    position: absolute;
    top: 145px;
    left: 135px;
    width: 1050px;
    text-align: left;  
   }   

/*   Inhalt des Impressums   */	
#contentimpressum {
	position: absolute;
    top: 136px;
    left: 205px;
    width: 800px;
	text-align: left;	
	}
	
/*  Logo von tiNts    */
#logo {
    position: absolute;
    top: 0px;
    width: auto;
    height: 110px;
    margin-left: 25px;
    margin-right: 25px;
}

/*   Bannerangaben   */
#banner {
    position: absolute;
    left: 200px;
    top:0px;
    height: 103px;
    width: 800px;
}

/*   Bild vom Banner   */
#banner_pic {
    border: none;
    width: auto;
    height: 103px;
    width: 800px;
}

/*  1. Ueberschrift (regelt insbesondere die unterstrichene blaue Linie    */
h1 {
    font-weight: bold;
    font-style: normal;
    font-size: 130%;
    line-height: 1.1em;
    padding-left: 4px;
    color: rgb(90, 115, 135);
    border-left: rgb(190, 215, 235) solid 20px;
    border-bottom: rgb(190, 215, 235) solid 4px;
    text-shadow: 1px 1px 0px #ccf,  2px 2px 2px rgb(90, 115, 135);
    
}

h2 {
    font-style: normal;
    font-size: 100%;
    line-height: 1.1em;
    padding-left: 4px;
    color: rgb(90, 115, 135);    
}	
	
/*   Textbreite der Textfelder  */
.text {
    width: 180px;
}

/*      */	
#suchcontainer {
	 position: absolute;
	 left:540px;
    width: 300px;
    height: 38px; 
    float: left;	
	}

/*   Suchfeld   */
#suchfeld {
	  width: 190px;
	  height: 15px;
	  margin-top: 7px;
    font-size: 12px;    
    font-weight: bold;
    color: black;
}

/*      */	
#suchbutton {
		background-image:url(../images/search.png) ;
		position: absolute;
		left: 210px;
		top: 3px;		
		width: 30px;
		height: 30px;					
	}

/*      */	
#suchbutton:hover{
		background-image:url(../images/search2.png);
	}

/*  Profilangaben in profil.php    */
#profil {
    height: 130px; 
    width: 950px;
    height: 700px;
}

/*   Avatar-Bild   */
#avatar_pic {
    margin-left: 10px;
    margin-top: 10px;
    margin-bottom: 2px;
    border: none;
    width: 200px;
    height: 150px;
}

/*   Textfelder und Labels in profil.php   */
#profilcontent {
    position: absolute;
    top: 220px;    
    padding-top: 1px;    
    height: 250px;
    font-family: verdana, arial, helvetica, geneva, sans-serif;
    width:460px;
}

/*  Hoehe von neues Ticket erstellen    */	
#ticketformular {
		height: 400px;
	}
	
/*  Positionsangaben der Ticketformularelemente     */		
.ticketelement {
	 position: absolute;
    width: 500px;
    left: 250px; 
	}

/*  Eigenschaften der Drop-Down-Felder    */		
.dropdownfelder {
	position:absolute;
	left:150px;
	padding:2px;	
	width:310px;
	text-align:left; 
	}

/*      */
#forumlarregistrierung {
	position: absolute;
	top: 136px;
	left: 220px;		
	border:solid 3px;	
	}

/*   allgemeine Eigenschaften von Tabellen   */	
TABLE { 
	table-layout:fixed 
	top: 40px;
	border-collapse:collapse;	
	width: 1025px;
}

/*  Tabellenueberschriften    */	
th {
background:url("../images/th_header.jpg") repeat scroll 0 0 transparent;
border:1px solid #CCCCCC;
padding:2px;
text-align:center;
}

/*   Tabellenrahmen   */	
td {
	border:1px solid #CCCCCC;	
	}
 


/*  allgemeine Angaben fuer Bilder    */
img {
    float: left;
    border: none;
}

/*  Bild fuer Profil    */
#imgprofil{
		padding-left: 200px;
}

/*  Textfelderangaben    */
.textfeld {
    position: absolute;
    left: 150px;
    padding: 2px 2px 2px 2px;
    width: 300px;
}

/*  Aussehen der Buttons    */	
.buttons{
	background:none repeat scroll 0 0 #b7b7b7;
	height: 24px;
	font-size: 16px;
	border: solid 1px grey;
	padding:1px 10px;
	margin-top: 5px;
	-moz-border-radius:5px 5px 5px 5px;	
	}

/*  Hover-Effekt fuer Buttons    */	
.buttons:hover{
	background:none repeat scroll 0 0 #e3e3e3;
	border: solid 1px grey;
	padding:1px 10px;
	margin-top: 5px;
	-moz-border-radius:5px 5px 5px 5px;	
	}
	
/*   Links im Buttonaussehen im Profil   */	
.linksprofil,   .linksprofil a:visited{
	background:none repeat scroll 0 0 #b7b7b7;
	border: solid 1px grey;
	padding:1px 10px;
	margin-top: 5px;
	-moz-border-radius:5px 5px 5px 5px;
	color:black;
	float:right;
	padding-right: 10px;	
	}

/*      */	
.linksprofil:hover,  .linksprofil a:visited {
	background:none repeat scroll 0 0 #e3e3e3;
	border: solid 1px grey;
	padding:1px 10px;
	margin-top: 5px;
	-moz-border-radius:5px 5px 5px 5px;
	color:red;	
	}
	

/*  Angaben fuer register.php    */
#registrieren {
	position: absolute;
    top: 136px;
    left: 201px;
    width: 800px;
}

/* Alle Links auf den Webseiten sollen erst einmal
 	keine Unterstreichung erhalten     */

a:link {
    
    text-decoration: none;    
    
}

a:visited {
    text-decoration: none;     
}

a:hover {
    text-decoration: none;       
}

a:active {    
    text-decoration: none
}


