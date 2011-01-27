<?php

function db_connect(){

        $conn = @oci_pconnect('USRTRACK','TrackIt','USER_TRACKING') or die("Die Datenbank konnte nicht erreicht werden.");

        if (!$conn)
            {
              $error = oci_error();
			  echo $error;
             trigger_error(htmlentities($error['message'], ENT_QUOTES), E_USER_ERROR);
            }
		return $conn;
}

function db_query($sqlquery){

		$conn=db_connect();

	    $stid = oci_parse($conn, $sqlquery);
        oci_execute($stid);
        oci_close($conn);

		return $stid;
}

date_default_timezone_set("Europe/Berlin");

?>