
<?php
	/**
	* account.php connects to mySQL database and retrieves the account number and type associated with the a user
	*/
	mysql_connect("sfsuswe.com","wwei","wwei");
	mysql_select_db("student_wwei");

	$q=mysql_query("SELECT account_number,type FROM account,user WHERE user.username ='".$_REQUEST['username']."' and user.id = account.user_id");

	while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	print(json_encode($output));

	mysql_close();
?>

  