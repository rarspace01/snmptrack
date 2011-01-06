<?php header('Content-type: text/xml'); //Diese Datei erzeugt den RSS Feed
 echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
$selfurl="http://".$_SERVER['SERVER_NAME']."/tints/ticket.php";
?>
<rss version="2.0">
<channel>
<title>Tints - RSS-Feed</title>
<description>Tints - TicketSystem</description>
<link>http://sourceforge.net/projects/tints-system/</link>
<copyright>EUPL</copyright>


<?php

include("./config/config.php");


//zeigt die letzten 20 Tickets aus dem System an
$query="SELECT tints_tickets.userid,title,ticketdesc,projectid,catid,osid,username,ticketid FROM tints_tickets,tints_users WHERE tints_tickets.userid=tints_users.userid ORDER BY ticketid DESC LIMIT 20;";

 $connection = mysql_connect($config['dbHost'], $config['dbUser'], $config['dbPassword']);
	  if (!$connection) {
	    die("Fehler! " . mysql_error());
      }
	  mysql_select_db($config['dbDatabase'], $connection);

	  $result = mysql_query($query, $connection);

   if ($result) {

		   while ($row = mysql_fetch_row($result)) {
		$userid=$row[0];
		$title=$row[1];
		$ticketdesc=$row[2];
		$projectid=$row[3];
		$catid=$row[4];
		$osid=$row[5];
		$username=$row[6];
		$ticketid=$row[7];
?>

     <item>
        <title>Ticket: <?php echo $ticketid; ?>  - <?php echo $title; ?></title>
        <description><?php echo $ticketdesc; ?></description>
        <link><?php echo $selfurl."?tid=$ticketid";//der Link um direkt zum Ticket zu kommen ?></link>
        <pubDate> <?php echo strftime( "%a, %d %b %Y %T %Z" , $result['pubDate']); //timestamp, damit der user eine chronologsiche abfolge hat?></pubDate>
     </item>  

<?php	

	}

	}

?>
</channel>
</rss>

