<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Banner Demo</title>
	<script src="lib/com/org/util/nsImport.js"></script>
</head>
<body  style="margin: 0px;" onscroll="scrollHere();">
  <nsimport file="nsBanner.js">
  <div id="divWrite">
  </div>
  <div id="content">
	  <a href="javascript:void(0)" onclick="showInfo();">Success</a> |
	  <a href="javascript:void(0)" onclick="showWarning();">Warning</a> |
	  <a href="javascript:void(0)" onclick="showError();">Error</a>
  </div>
  <script>
  	var objBanner = null;
  	function showInfo()
  	{
  		if(!objBanner)
  		{
  			objBanner = document.getElementById("temp");//document.createElement("nsbanner");
  		}
  		objBanner.showInfo("Your request has been successfully received.");
  	}
  	function showWarning()
  	{
  		if(!objBanner)
  		{
  			objBanner = document.getElementById("temp");//document.createElement("nsbanner");
  		}
  		objBanner.showWarning("You must enter all required information.");
  	}
  	function showError()
  	{
  		if(!objBanner)
  		{
  			objBanner = document.getElementById("temp");//document.createElement("nsbanner");
  		}
  		objBanner.showError("You have encountered a critical error.");
  	}
  </script>
  		<ns-banner id="temp"/>
        <p><b>Note:</b> Utility will work in IE7 and IE8 if a !DOCTYPE is specified(&lt;!DOCTYPE html&gt;) at the start of the Page(before &lt;html&gt; tag).</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
		<p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p><p>Some text</p>
</body>
</html>
