<?php

include_once("db.inc.php");

$sql = "SELECT * FROM USRTRACK.\"st_switchs\" ORDER BY IP DESC";
query($sql);

?>