<!doctype html>
<html lang="en-US">
<head>
  <meta charset="utf-8">
  <meta http-equiv="Content-Type" content="text/html">
  <title>Full CSS3 Tooltips - Design Shack Demo</title>
  <meta name="author" content="Jake Rocheleau">
  <link rel="shortcut icon" href="http://designshack.net/favicon.ico">
  <link rel="icon" href="http://designshack.net/favicon.ico">
  <link rel="stylesheet" type="text/css" media="all" href="css/styles.css">
</head>

<body>
  <div id="topbar">
  <a href="http://designshack.net">Back to Design Shack</a>
  </div>
  
  <div id="w">
    <div id="content">
      <h1>HTML5 &amp; CSS3 Link Tooltips</h1>
      <p>Hover onto a page link to display various tooltips.</p>
      
      <p>Think of it like <a href="#" data-tool="Sample tooltip" class="tooltip animate">testing stuff</a> for various purposes.</p>
      
      <p>And how about <a href="#" data-tool="screwy eh?" class="tooltip animate right">off to the side</a> of the text?</p>


      <p>Why not use a sleek <a href="#" class="tooltip animate blue" data-tool="banana bana fo filly">animation</a> for the tips? If you don't like the CSS3 transitions just remove the class to <a href="#" class="tooltip" data-tool="simple!">turn them off</a>.</p>
      
      <p>Let's also take a peek at <a href="#" class="tooltip bottom animate blue" data-tool="Get ready because this tooltip is loooong. I mean seriously...">bottom-styled tips</a>. Do you think sideways tips could work again?</p>
      
      <p>I dunno but let's <a href="#" class="tooltip right animate blue" data-tool="just hangin'">find out</a>.</p>
     
     <p>And perhaps they can appear <a href="#" class="tooltip left animate blue" data-tool="tooltips are clickable">on the left</a> as well...</p>
      
      <p>Tooltips can also be applied to form inputs using extra HTML:</p>
      
      <input type="text" class="basictxt" tabindex="1" data-tool="Enter some text"><span class="fieldtip">Enter some text!</span>
	  <input type="checkbox" class="fieldtip" tabindex="1" data-tool="Enter some text"><span class="fieldtip">Enter some text!</span>
    </div><!-- @end #content -->
  </div><!-- @end #w -->
</body>
</html>
