<?php
	/**
	* detail.php connects to mySQL database and retrieves the expense records information associated with
	* a specific user within some date range
	*/
	mysql_connect("sfsuswe.com","wwei","wwei");
	mysql_select_db("student_wwei");

	$q=mysql_query("SELECT * FROM record WHERE type <> 'Earning' and account = '".$_POST['account']."' and date >='".$_POST['start']."' and date <='".$_POST['end']."'");
	
	while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	print(json_encode($output));

	mysql_close();
?>

  