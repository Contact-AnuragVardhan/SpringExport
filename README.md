<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- css from http://www.csstablegenerator.com/?table_id=3 -->
<style>

.dataGrid
{
    /*border-collapse: collapse;
    border-spacing: 0;
    margin:0px;
    padding:0px;
    border:1px solid #000000;*/
    
    width: 100%;
background-color: #EEE;
border: 0px;
margin: 0px;
padding: 0px;
background-color: #F6F6F6;
border-collapse: collapse;
}
.dataGridTitleBar
{
    background-color: #eee;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
    border-top: 1px solid #ccc;
    color: #333;
    font-weight: bold;
    text-shadow: 1px 1px 0 white;
}

.dataGridHeader
{
    /*background:-o-linear-gradient(bottom, #005fbf 5%, #003f7f 100%);  
    background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #005fbf), color-stop(1, #003f7f) );
    background:-moz-linear-gradient( center top, #005fbf 5%, #003f7f 100% );
    filter:progid:DXImageTransform.Microsoft.gradient(startColorstr="#005fbf", endColorstr="#003f7f");  
    background: -o-linear-gradient(top,#005fbf,003f7f);
    background-color:#005fbf;
    border:1px solid #000000;
    text-align:center;
    border-width:0px 0px 1px 1px;
    font-weight:bold;
    color:#ffffff;
    cursor : default;*/
    background-color: #e6e8ea;
  	color: #485563;
  	line-height: 1.8em;
  	margin: 0px;
  white-space: nowrap;
   border-left: 1px solid #000000;
  padding: 4px 6px 2px;
    border-collapse: collapse;
}
.dataGridOddRow
{
    background-color: white;
	color: black;
}
.dataGridEvenRow  
{
    background-color: #EEEEEE;
	color: black; 
}
.dataGridCell
{
    border-bottom : buttonshadow 1px solid;
    border-top : buttonshadow 1px solid;
    border-left : buttonshadow 1px solid;
    border-right : buttonshadow 1px solid;
    cursor : default;
    padding:7px;
    text-align:left;
    font-weight:normal;
     vertical-align:middle;
     color:#000000;
}
.dataGridHover
{
    background-color: #DCDCDC;
}
.dataGridSelection
{
    background-color: #b0bed9;
}

.hbox 
{
	/*display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-align: stretch;
 
	display: -moz-box;
	-moz-box-orient: horizontal;
	-moz-box-align: stretch;
 
	display: box;
	box-orient: horizontal;
	box-align: stretch;*/
	
	/*margin-right: -15px;
  	margin-left: -15px;*/
  	display: table;
	width: 100%;
}

.hbox > div 
{
    display: table-cell;
    width: 2%;
}
.hbox > div:nth-child(2) {
    width: 50%;
}

/*.hbox:before,.hbox:after
{
	display: table;
  	content: " ";
}

.hbox:after
{
	clear: both;
}
*/

.arrow-down 
{
	/*width: 0; 
	height: 0; 
	border-left: 5px solid transparent;
	border-right: 5px solid transparent;
	border-top: 5px solid #000;*/
	
	width: 0;
	height: 0;
	border-style: solid;
	border-width: 10px 5px 0 5px;
	border-color: #1f1c1c transparent transparent transparent;
}

.arrow-right 
{
	/*width: 0; 
	height: 0; 
	border-top: 5px solid transparent;
	border-bottom: 5px solid transparent;
	border-left: 5px solid #000;*/
	
	width: 0;
	height: 0;
	border-style: solid;
	border-width: 4px 0 4px 10px;
	border-color: transparent transparent transparent #1f1c1c;
}

.resize-handle-active
{
	cursor: e-resize;
}

.resize-handle 
{
	cursor: e-resize;
	width: 2px;
	border-right: 1px dashed #1E90FF;
	position:absolute;
	top:0;
	left:0;
}

*.unselectable {
   -moz-user-select: none;
   -khtml-user-select: none;
   -webkit-user-select: none;

   /*
     Introduced in IE 10.
     See http://ie.microsoft.com/testdrive/HTML5/msUserSelect/
   */
   -ms-user-select: none;
   user-select: none;
}

</style>

<script type="text/javascript" src="jsDataGrid1.js"></script>
<!--  <script type="text/javascript" src="resizable-tables.js"></script> -->
<script type="text/javascript">

var columns=[ { headerText: "Id", dataField: "id", width : 80 , sortable: true , sortDescending: false },
              { headerText: "Hierarchy", dataField: "hierarchy" ,width : 200 , sortable: true , sortDescending: true },
              { headerText: "Supervisor", dataField: "supervisor" ,width : 150 , sortable: false , sortDescending: true },
              { headerText: "Employees", dataField: "employees" ,width : 150 , sortable: true , sortDescending: true },
              //{ headerText: "", dataField: "checked" ,width : 50 , sortable: false , sortDescending: false,templateID:"#tempCheckBox"},
              { headerText: "", dataField: "checked" ,width : 50 , sortable: false , sortDescending: false,itemRenderer:itemRenderer}
           ];

                
var dataSource =[{id: '1', hierarchy: 'NDPI', supervisor: null, country: 'US', employees: null, price: '10.90', year: '1985',checked:true,children:[
					{id: '2', hierarchy: 'NGFP Head', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'Patel,Samir', price: '9.90', year: '1988',children:[
						{id: '3', hierarchy: 'NGFP Corporate Equity Derivative Sales', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
						{id: '4', hierarchy: 'NGFP Structured Products Sales', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'}  					                                                                                                                                         
					]},
                 ]},
	             {id: '5', hierarchy: 'Non Regulated Entity', supervisor: null, country: 'US', employees: null, price: '10.90', year: '1985',children:[
					{id: '6', hierarchy: 'Nomura America Services, LLC', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'Patel,Samir', price: '9.90', year: '1988',children:[
						{id: '7', hierarchy: 'OPERATIONS', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
						{id: '8', hierarchy: 'ENTERPRISE DATA MGMT', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'}  					                                                                                                                                         
					]},
                ]},
	            {id: '9', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '10',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '11', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '12', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '13', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '14', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '15', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '16', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '17', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '18', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '19', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '20',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'}
	            /* {id: '21', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '22', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '23', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '24', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '25', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '26', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '27', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '28', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '29', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '30',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '31', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '32', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '33', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '34', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '35', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '36', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '37', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '38', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '39', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '40',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '41', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '42', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '43', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '44', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '45', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '46', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '47', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '48', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '49', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '50',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '51', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '52', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '53', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '54', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '55', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '56', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '57', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '58', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '59', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '60',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '61', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '62', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '63', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '64', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '65', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '66', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '67', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '68', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '69', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '70',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '71', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '72', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '73', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '74', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '75', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '76', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '77', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '78', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '79', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '80',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '81', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '82', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '83', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '84', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '85', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '86', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '87', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '88', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '89', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '90',hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'},
	            {id: '91', hierarchy: 'Empire Burlesque', supervisor: 'Bob Dylan', country: 'US', employees: 'Columbia', price: '10.90', year: '1985'},
	            {id: '92', hierarchy: 'Hide your heart', supervisor: 'Bonnie Tyler', country: 'UK', employees: 'CBS Records', price: '9.90', year: '1988'},
	            {id: '93', hierarchy: 'One night only', supervisor: 'Bee Gees', country: 'UK', employees: 'Polydor', price: '10.90', year: '1998'},
	            {id: '94', hierarchy: 'Romanza', supervisor: 'Andrea Bocelli', country: 'US', employees: 'Polydor', price: '10.80', year: '1996'},
	            {id: '95', hierarchy: 'Pavarotti Gala Concert', supervisor: 'Luciano Pavarotti', country: 'US', employees: 'DECCA', price: '9.90', year: '1991'},
	            {id: '96', hierarchy: 'Picture book', supervisor: 'Simply Red', country: 'US', employees: 'Elektra', price: '7.90', year: '1985'},
	            {id: '97', hierarchy: 'Eros', supervisor: 'Eros Ramazzotti', country: 'US', employees: 'BMG', price: '9.90', year: '1997'},
	            {id: '98', hierarchy: 'Black angel', supervisor: 'Savage Rose', country: 'US', employees: 'Mega', price: '10.90', year: '1995'},
	            {id: '99', hierarchy: 'For the good times', supervisor: 'Kenny Rogers', country: 'UK', employees: 'Mucik Master', price: '8.70', year: '1995'},
	            {id: '100', hierarchy: 'Big Willie style', supervisor: 'Will Smith', country: 'US', employees: 'Columbia', price: '9.90', year: '1997'}*/
            ];
var dataGrid = null;
function loadHandler()
{
	/*JSDataGrid.init("divTable",columns,"Data Set Demo",true,true);
	JSDataGrid.dataSource(dataSource);
	JSDataGrid.rowSelectionHandler = onSelection;
	JSDataGrid.rowUnSelectionHandler = onUnSelection;*/
	dataGrid = new JSDataGrid("divTable",null,"700px","700px",columns,"Data Set Demo",true,true);
	dataGrid.init();
	dataGrid.dataSource(dataSource);
	dataGrid.rowSelectionHandler = onSelection;
	dataGrid.rowUnSelectionHandler = onUnSelection;
}

function onSelection(item)
{
	if(item)
	{
		//divSelected.innerHTML += "Engine:" + item.engine + ",";
	}
}

function onUnSelection(item)
{
	if(item)
	{
		//divUnSelected.innerHTML += "Engine:" + item.engine + ",";
	}
}

function itemRenderer(data,dataField,rowIndex,columnIndex)
{
	var selected = data[dataField];
	if(selected)
	{
		return '<input type="checkbox" checked>';
	}
	else
	{
		return '<input type="checkbox">';
	}
}
 
//Global Exception Handler
/*window.onerror = function(msg, url, line)
{
    // You can view the information in an alert to see things working
    // like so:
   alert("Error: " + msg + "\nurl: " + url + "\nline #: " + line);
   // TODO: Report this error via ajax so you can keep track
   //       of what pages have JS issues
   var suppressErrorAlert = true;
   // If you return true, then error alerts (like in older versions of
   // Internet Explorer) will be suppressed.
   return suppressErrorAlert;
};*/
</script>
</head>
<body onload="loadHandler();" style="margin:0px;padding:0px;width:1000px;height:1000px;">
 <template id="tempCheckBox">
 	<input type="checkbox" name="vehicle" value="Bike">
 </template>
 <div id="divTable" style="position:relative; width: 640px; height: 700px;padding-left:50px;">
  <div class="vertical-line"></div>
 </div>
 <div id="divSelected">
 </div>
  <div id="divUnSelected">
 </div>
 <br/><br/>
<form>
  Enter column no: <input type='text' name=col_no>
  <input type='button' onClick='javascript:dataGrid.setVisibilityOfColumn(col_no.value,  true);' value='show'>
  <input type='button' onClick='javascript:dataGrid.setVisibilityOfColumn(col_no.value, false);' value='hide'>

</form>
 
 
</body>
</html>
