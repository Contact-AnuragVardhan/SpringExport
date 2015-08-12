<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List Demo</title>
<script src="lib/com/org/util/nsImport.js"></script>

<style>
   label {
  display: block;
  padding: 5px 5px 5px;
  
  background-color: #2175bc;
  color: #fff;
  text-decoration: none;
  } 
  label:hover {
  border-left: 10px solid #1c64d1;
  border-right: 10px solid #5ba3e0;
  background-color: #2586d7;
  color: #fff;
  }
</style>

</head>
<body onload="loadHandler();" style="overflow:hidden;">

 	<nsimport file="nsList.js">
 	</nsimport>
	
	<ns-List id="lstDemo" labelField="hierarchy" style="height:200px;">
	</ns-List>
	<form>
	  <input type="text" id="txtSelectedIndex" placeholder="Enter Selected Index" required><br>
	  <input type="submit" name="Submit" value="Submit" onclick="return setSelectedIndex();"/>
	</form>
	<script>	
				var dataSource = [];
				document.getElementById('txtSelectedIndex').onkeydown = function(e) 
				{
				    var key = e.keyCode ? e.keyCode : e.which;
				    if ( isNaN( String.fromCharCode(key) ) ) return false;
				}
		
				function loadHandler()
				{
					dataSource = [];
					for (var count = 0 ;count < 400; count++)
					{
						var item = {id:count, hierarchy:"Hierarchy " + count};
						dataSource.push(item);
					}
					ns.onload(function(){
						var lstDemo = document.getElementById("lstDemo");
						lstDemo.setDataProvider(dataSource);
					});	
				}
				function setSelectedIndex()
				{
					var txtSelectedIndex = document.getElementById('txtSelectedIndex'); 
					var lstDemo = document.getElementById('lstDemo'); 
					lstDemo.setSelectedIndex(txtSelectedIndex.value);
					return false;
				}
	</script>
</body>
</html>
