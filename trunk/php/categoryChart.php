<?php
	/**
	* categoryChart.php connects to mySQL database and shows expense records in 7 categories using google chart tool
	*/
	mysql_connect("sfsuswe.com","wwei","wwei");

	mysql_select_db("student_wwei");
	
	//retrieve expense records in 7 categories from the database
	$q=mysql_query("SELECT sum(amount) as total , type FROM record WHERE type <> 'Earning' and account = '".$_REQUEST['account']."' and date >='".$_REQUEST['start']."' and date <='".$_REQUEST['end']."'  group by type");


	while($e=mysql_fetch_assoc($q))
		$output[]=$e;

	$amount;
	if (empty($output)) {
		echo "No expense record found from ".$_REQUEST['start']." to ".$_REQUEST['end']."\n";
	}
	else{
		foreach ($output as &$value) {
			if($value['type'] == "Grocery")
				$amount[0] = $value['total'];
			elseif($value['type'] == "Housing")
				$amount[1] = $value['total'];
			elseif($value['type'] == "Clothes")
				$amount[2] = $value['total'];
			elseif($value['type'] == "Transportation")
				$amount[3] = $value['total'];
			elseif($value['type'] == "Entertainment")
				$amount[4] = $value['total'];
			elseif($value['type'] == "Other")
				$amount[5] = $value['total'];
			else
				$amount[6] = $value['total'];
		}	
	}		
	mysql_close();
?>


<html>
  <head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">

      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);
	 

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'category');
        data.addColumn('number', 'amount');
		var grocery = parseInt("<?php echo $amount[0] ?>");
		var housing = parseInt("<?php echo $amount[1] ?>");
		var clothes = parseInt("<?php echo $amount[2]?>");
		var transportation = parseInt("<?php echo $amount[3] ?>");
		var entertainment = parseInt("<?php echo $amount[4] ?>");
		var other = parseInt("<?php echo $amount[5] ?>");
		var health = parseInt("<?php echo $amount[6] ?>");
		var type = "<?php echo $_GET['mode'] ?>";
        data.addRows([
          ['Grocery', grocery],
          ['Housing', housing],
          ['Clothes', clothes],
          ['Transportation', transportation],
          ['Entertainment', entertainment],
		  ['Health Care', health],
		  ['Other', other]
        ]);
	   
	  

        // Set chart options
        var options = {'title':'Spending Categories',
                       'width':400,
                       'height':300};

        // Instantiate and draw our chart, passing in some options.
		var chart;
		if(type == 'PieChart'){
			chart = new google.visualization.PieChart(document.getElementById('chart_div'));
		}
        else if(type == 'BarChart'){
			chart = new google.visualization.BarChart(document.getElementById('chart_div'));
			options = {'title':'Spending Categories',
                       'width':400,
                       'height':300,
					   'legend': 'none'};
		}
		else{
			chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
			options = {'title':'Spending Categories',
                       'width':400,
                       'height':300,
					   'legend': 'none'};
		}
        chart.draw(data, options);
      }
    </script>
  </head>

  <body>
   <? if (!empty($output)) {?>
    <div id="chart_div"></div>
	<? } ?>
  </body>
</html>
  