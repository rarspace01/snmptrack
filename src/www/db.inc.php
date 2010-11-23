<?php
function query($sql)
        {
        $conn = @oci_connect('USRTRACK','TrackIt','USER_TRACKING') or die("Die Datenbank konnte nicht erreicht werden.");

        if (!$conn)
            {
              $error = oci_error();
              trigger_error(htmlentities($error['message'], ENT_QUOTES), E_USER_ERROR);
            }
       
        $stid = oci_parse($conn, $sql);
        oci_execute($stid);
        oci_close($conn);
	
        $i = 0;

		echo "<table cellspacing='1' style='font-family:Verdana; font-size:10pt'>\n";
		
		
		
        while ($row = oci_fetch_array($stid, OCI_ASSOC+OCI_RETURN_NULLS))
        {
                $i++;
                echo "<tr>\n";
				
				echo "<td>".$i."</td>";
				
                if (($i%2) == 0)
                {
                        foreach ($row as $item)
                        {
                                echo "<td bgcolor='#FFFFFF'>" . ($item !== null ? htmlentities($item, ENT_QUOTES) : "&nbsp;" ) . "</td>\n";
                        }
                }
                else
                {
                        foreach ($row as $item)
                        {
                                echo "<td bgcolor='#3399FF'>" . ($item !== null ? htmlentities($item, ENT_QUOTES) : "&nbsp;") . "</td>\n";
                        }
                }
                echo "</tr>\n";
        }
        echo "</table>\n";
        }
?>