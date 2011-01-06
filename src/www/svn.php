<?php
//simple Methode um zu Prüfne welche SVN Version auf dem Server ist, nur zu debugzwecken
$svn = File('.svn/entries');
$svnrev = $svn[3];
unset($svn);
echo "SVN: rev. ".$svnrev;
?>

