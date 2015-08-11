.nsListParentContainer
{
    position: relative;   
    overflow-y: auto;
}

.nsListChildContainer
{
	position: absolute; 
	height: 100%; 
	width: 100%; 
	margin: 0px; 
	padding: 0px;
}

.nsListScrollerCause
{
	width: 1px; 
	position: absolute; 
	margin: 0; 
	padding: 0;
}

.nsListContainer
{
	position: relative;
	margin: 0px;
	padding: 0px;
	text-align: center;
}

-------------------------------------------------------------------------------------
ListDemo.jsp
<ns-List id="lstDemo" labelField="hierarchy" style="height:200px;">
	</ns-List>
	
	function loadHandler()
				{
					dataSource = [];
					for (var count = 0 ;count < 100; count++)
					{
						var item = {id:count, hierarchy:"Hierarchy " + count};
						dataSource.push(item);
						
					}
					ns.onload(function(){
						var lstDemo = document.getElementById("lstDemo");
						lstDemo.setDataProvider(dataSource);
					});	
				}
