<?php
	/**
	* login.php connects to mySQL database and verifies the username and password stored in the user table
	*/
	mysql_connect("sfsuswe.com","wwei","wwei");
	mysql_select_db("student_wwei");

	$q=mysql_query("SELECT count(*) as count FROM user WHERE username ='".$_REQUEST['username']."' and password ='".$_REQUEST['password']."'");

	while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	print($output[0]['count']);

	mysql_close();
?>

  