<?php

#putenv("NLS_LANG=German_Germany");
#putenv("NLS_DATE_FORMAT=HH24:MI:SS DD.MM.YYYY");
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
/*
        while ($row = oci_fetch_array($stid, OCI_ASSOC+OCI_RETURN_NULLS))
        {
                $i++;
                echo "<tr>\n";
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
        echo "<br/><a href='javascript:history.back()' style='font-family:Verdana; font-size:10pt; color:black'>zur&uuml;ck</a>";
        }

*/
?>