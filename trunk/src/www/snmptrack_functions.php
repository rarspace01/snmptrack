<?php

function getSortname($sortstring){
return substr($sortstring,0,strpos($sortstring,'_'));	
}

function getSortorder($sortstring){
$sortvar=substr($sortstring,strpos($sortstring,'_')+1);
	if(strpos($sortvar,'D')!==false){
	return "DESC";	
	}else{
	return "ASC";
	}
}


?>