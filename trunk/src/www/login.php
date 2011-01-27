<?php 
session_start(); 

if(strlen($_GET['logout'])>0){
	handleLogout();
}

include_once("db.inc.php");

include "header.php";

if(strlen($_POST['password'])>0){
handleLogin();
}

?>


	<div id="login">

            <form id="loginst" method="post" action="<?php echo $selfurlsave; ?>">
               
	
                <label><b>Benutzername:</b></label> <input id="idusername" class="text" type="text" maxlength="20" size="20" name="username" />
                <label><b>Passwort:</b></label> <input class="text" type="password" maxlength="20" size="20" name="password" />
  
            	<br>
                <input type="submit" value="Anmelden" class="buttons"name="login" /> <br>
				 <br>

            </form>
        </div>

<?php 

echo "</div>";

include ("footer.html");

function handleLogin(){
include_once 'db.inc.php';	
	
$username = $_POST['username'];
$userpwd = sha1($_POST['password']);

$dbusrid="";
$dbusrname="";
$dbusrpwd="";
$dbusrlvl="";

$sSQL="SELECT * FROM USRS WHERE USRNAME LIKE '".$username."' AND USRPWD LIKE '".$userpwd."'";
$result=db_query($sSQL);

while ($row = oci_fetch_array($result, OCI_ASSOC+OCI_RETURN_NULLS)) {
	
	$dbusrid=$row['USRID'];
	$dbusrname=$row['USRNAME'];
	$dbusrpwd=$row['USRPWD'];
	$dbusrlvl=$row['USRLVL'];
	
}

if($username===$dbusrname&&$userpwd===$dbusrpwd){
$_SESSION['id'] = $dbusrid;
$_SESSION['username'] = $dbusrname;
$_SESSION['userlevel'] = $dbusrlvl;

echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=index.php\">");
die;
}

}

function handleLogout(){

//clean session
$_SESSION[]=array();
if(isset($_COOKIE[session_name()])){
setcookie(session_name(),'',time()-42000,'/');
}
session_destroy();

echo ("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=index.php\">");
die;
	
}

?>