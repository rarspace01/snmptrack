<?php
		
        include 'header.php'; //header und keine lgoinbox genauso wie bei register.php, auch aus dem selben grund
	     

?>
	
        <div id="content">
        
 <h1>Passwort vergessen?</h1>     
 <h2>Wenn Du Dein Passwort vergessen hast …</h2>  

<p>
… kannst Du Dir hier ein neues erstellen.<br>

Gib bitte Deine E-Mail-Adresse an, Du erhältst dann umgehend eine E-Mail von uns zugesendet.</p>       

<form id="formularregistrierung" action="register.php" method="get">
                <label>Deine E-Mail-Adresse: <input type="text" maxlength="50" size="30" name="reset" id="name" class="textfeld" style="left:200px;"><br><br>
               
                </label> <input type="submit" value="Abschicken" class="buttons" >
            </form>     
</div>        
    
<?php
include 'footer.html';
?>
