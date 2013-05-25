<?php
	/**
	* insertRecord.php connects to mySQL database and inserts the expense record into the database and 
	* retrieve the resulting balance for the account
	*/
	mysql_connect("sfsuswe.com","wwei","wwei");
	mysql_select_db("student_wwei");
	
	//insert expense record into the record table
	$i=mysql_query("INSERT INTO record(date, type, description, amount, account) VALUES ('".$_REQUEST['date']."','".$_REQUEST['type']."','".$_REQUEST['description']."','".$_REQUEST['amount']."','".$_REQUEST['account']."');");
	
	//retrieve the account balance
	$q= mysql_query("SELECT sum(amount) as total,account.type as category FROM account,record WHERE record.account = account.account_number and account = ".$_REQUEST['account']);

	while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	print(json_encode($output));

	mysql_close();
?>

  