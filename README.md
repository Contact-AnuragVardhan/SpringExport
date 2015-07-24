var nsGrid = Object.create(nsContainerBase);

nsGrid.initializeComponent = function() 
{
	this.base.initializeComponent();
	
	this.__OUTER_CONTAINER_ID = "divDataSet";
	this.__TITLE_CONTAINER_ID = "divTitleBar";
	this.__TABLE_HEADER_CONTAINER_ID = "divHeaderContainer";
	this.__TABLE_HEADER_ID = "tblHeader";
	this.__TABLE_BODY_CONTAINER_ID = "divBodyContainer";
	this.__TABLE_BODY_ID = "tblBody";
	
	this.__CLASS_TITLEBAR = "dataGridTitleBar";
	this.__CLASS_TABLE = "dataGrid";
	this.__CLASS_TABLE_HEADER_CELL = "dataGridHeader";
	this.__CLASS_ODD_ROW = "dataGridOddRow";
	this.__CLASS_EVEN_ROW = "dataGridEvenRow";
	this.__CLASS_GROUP_CELL = "hbox";
	this.__CLASS_ARROW_COLLAPSED = "arrow-right";
	this.__CLASS_ARROW_EXPANDED = "arrow-down";
	this.__CLASS_SORTING_ASC = "sorted_asc";
	this.__CLASS_SORTING_DESC = "sorted_desc";
	
	this.__DEFAULT_COLUMN_WIDTH = 100;
	
	this.columns = new Array();
	this.selectedItems = new Array();
	
	this.__title = null;
	this.__dataSource = new Array();
	this.__enableMouseHover = false;
	this.__enableMultiSelection = false;
	this.__childField = "children";
	this.__columnMinWidth = 10;
	this.__rowSelectionHandler = null;
	this.__rowUnSelectionHandler = null;
	//stores initial Column Count 
	this.__initialColumnCount = 0;
	
	this.__tblHeader = null;
	this.__tblBody = null;
	//stores whether mouse is on GridLine
	this.__onGridLine = false;
	//cell which is being resized
	this.__resizingHeader = null;
	this.__resizingHeaderCell = null;
	this.__resizingBodyCell = null;
	this.__resizeHandler = null;
};

nsGrid.setComponentProperties = function() 
{
	this.base.setComponentProperties();
	if(this.hasAttribute("title"))
	{
		this.__title = this.getAttribute("title");
	}
	if(this.hasAttribute("enableMouseHover"))
	{
		this.__enableMouseHover = this.getAttribute("enableMouseHover");
	}
	if(this.hasAttribute("enableMultiSelection"))
	{
		this.__enableMultiSelection = this.getAttribute("enableMultiSelection");
	}
	if(this.hasAttribute("childField"))
	{
		this.__childField = this.getAttribute("childField");
	}
	if(this.hasAttribute("columnMinWidth"))
	{
		this.__columnMinWidth = this.getAttribute("columnMinWidth");
	}
	if(this.hasAttribute("rowSelectionHandler"))
	{
		this.__rowSelectionHandler = this.getAttribute("rowSelectionHandler");
	}
	if(this.hasAttribute("rowUnSelectionHandler"))
	{
		this.__rowUnSelectionHandler = this.getAttribute("rowUnSelectionHandler");
	}
	this.__setInitialColumnsCount();
	this.columns = this.__getAllColumns();
	this.__createStructure();
};

nsGrid.propertyChange = function(attrName, oldVal, newVal, setProperty) 
{
	this.base.propertyChange(attrName, oldVal, newVal, setProperty);
};

nsGrid.dataSource = function(source)
{
	this.__dataSource = source;
	this.__setHeaderSortFunction();
	this.__renderBody(false);
};

nsGrid.columnInitialized = function(column)
{
	if(column)
	{
		this.columns[this.columns.length] = column;
		this.__initialColumnCount = this.__initialColumnCount - 1;
		if(this.__initialColumnCount === 0)
		{
			this.__createStructure();
		}
	}
};

nsGrid.__setInitialColumnsCount = function()
{
	var gridColumns = this.getElementsByTagName("ns-gridcolumn");
	if(gridColumns && gridColumns.length > 0)
	{
		this.__initialColumnCount = gridColumns.length;
	}
};

nsGrid.__getAllColumns = function()
{
	var gridColumns = this.getElementsByTagName("ns-gridcolumn");
	return gridColumns;
};

nsGrid.__createStructure = function()
{
	this.util.removeAllChildren(this);
	var divOuterContainer = this.__createOuterContainer();
	this.__createTitleBar(divOuterContainer);
	this.__createTableStructure(divOuterContainer);
};

nsGrid.__createOuterContainer = function()
{
	var divOuterContainer = this.util.createDiv(this.getID() + this.__OUTER_CONTAINER_ID);
	if(this.style.width != "")
	{
		divOuterContainer.style.width = this.style.width;
	}
	else if(this.offsetWidth > 0)
	{
		divOuterContainer.style.width = this.offsetWidth + "px";
	}
	else
	{
		divOuterContainer.style.width = "100%";
	}
	if(this.style.height != "")
	{
		divOuterContainer.style.height = this.style.height;
	}
	else if(this.offsetHeight > 0)
	{
		divOuterContainer.style.height = this.offsetHeight + "px";
	}
	else
	{
		divOuterContainer.style.height = "100%";
	}
	this.addChild(divOuterContainer);
	return divOuterContainer;
};

nsGrid.__createTitleBar= function(parentElement)
{
	if(parentElement && this.__title && this.__title.length > 0)
	{
		var divTitleBar = this.util.createDiv(this.getID() + this.__TITLE_CONTAINER_ID,this.__CLASS_TITLEBAR);
		var titleText = document.createTextNode(this.__title);
		divTitleBar.appendChild(titleText);
		parentElement.appendChild(divTitleBar);
		
		return divTitleBar;
	}
	return null;
};

nsGrid.__createTableStructure= function(parentElement)
{
	if(parentElement)
	{
		this.__createHeader(parentElement);
		this.__createBody(parentElement);
	}
};

/******************************Header Creation *************************************/
nsGrid.__createHeader= function(parentElement)
{	
	if(parentElement)
	{
		var divHeaderContainer = this.__createTableAndParent(this.getID() + this.__TABLE_HEADER_CONTAINER_ID,this.getID() + this.__TABLE_HEADER_ID);
		divHeaderContainer.style.overflow = "hidden";
		parentElement.appendChild(divHeaderContainer);
		this.__tblHeader = divHeaderContainer.firstChild;
		this.__createHeaderRows();
	}
};

nsGrid.__createHeaderRows= function()
{
	if(this.__tblHeader && this.columns && this.columns.length > 0)
	{
		var header = this.__tblHeader.createTHead();
		var headerRow = header.insertRow(-1);
	    headerRow.style.height = "auto";
	    var body = document.createElement("tbody");
		this.__tblHeader.appendChild(body);
	    var bodyRow = body.insertRow(-1);
	    for (var colIndex = 0; colIndex < this.columns.length; colIndex++)
	    {
	    	var colItem = this.columns[colIndex];
	        this.__createHeaderHeaderCell(colItem,headerRow);
	        this.__createHeaderBodyCell(colItem,bodyRow,colIndex);
	    }
	}
};

nsGrid.__createHeaderHeaderCell = function(colItem,headerRow)
{
	if(colItem && headerRow)
	{
		var headerCell = headerRow.insertCell(-1);
		if(colItem.hasOwnProperty("width") && !isNaN(colItem["width"]))
        {
        	headerCell.style.width = Number(colItem["width"]) + "px";
        }
        else
        {
        	headerCell.style.width = this.__DEFAULT_COLUMN_WIDTH + "px";
        }
	}
};

nsGrid.__createHeaderBodyCell = function(colItem,bodyRow,index)
{
	if(colItem && bodyRow)
	{
		var headerText = " ";
		if(colItem.hasOwnProperty("headerText") && colItem["headerText"])
        {
        	headerText = colItem["headerText"];
        }
		var bodyCell = bodyRow.insertCell(-1);
		bodyCell.setAttribute("id","col" + colItem["headerText"]);
		this.util.addStyleClass(bodyCell , this.__CLASS_TABLE_HEADER_CELL);
        
        var divCell = this.util.createDiv(this.getID() + "div" + headerText); 
        divCell.style.fontWeight = "bold";
        var headerTextNode = document.createTextNode(headerText);
        divCell.appendChild(headerTextNode);
        bodyCell.appendChild(divCell);
        
        bodyCell.setAttribute("orignalColumnIndex",index);
        bodyCell.setAttribute("currentColumnIndex",index);
        
        this.util.addEvent(bodyCell,"click",this.__headerClickHandler.reference(this));
        this.util.addEvent(bodyCell,"mouseover",this.__headerMouseOverHandler.reference(this));
        this.util.addEvent(bodyCell,"mouseout",this.__headerMouseOutHandler.reference(this));
	}
};

nsGrid.__setHeaderSortFunction = function()
{
	if(this.__dataSource && this.__dataSource.length > 0)
    {
		if(this.__tblHeader.tBodies.length > 0 && this.__tblHeader.tBodies[0].rows.length > 0 && this.__tblHeader.tBodies[0].rows[0].cells.length > 0)
		{
			var headerCells = this.__tblHeader.tBodies[0].rows[0].cells
	        for(var colIndex = 0; colIndex < headerCells.length; colIndex++)
	        {
	        	var headerCell = headerCells[colIndex];
	        	var colItem = this.columns[colIndex];
	        	if(colItem.hasOwnProperty("dataField") && colItem["dataField"] && colItem.hasOwnProperty("sortable") && colItem["sortable"] === true)
	        	{
	        		var item = null;
	        		for(var count = 0; count < this.__dataSource.length; count++)
	                {
	                     item = this.__dataSource[count][colItem["dataField"]];
	                     if(item && item != "")
	                     {
	                           break;
	                     }
	                }
	                if(item && item != "")
	                {
	                	headerCell.sortFunction = this.__determineSortFunction(item);
	                }
	                else
	                {
	                	headerCell.sortFunction = "sortCaseInsensitive";
	                }
	        	}
	        }
		}
    } 
};

/******************************End of Header Creation *************************************/

/******************************Body Creation **********************************************/

nsGrid.__createBody= function(parentElement)
{	
	if(parentElement)
	{
		var divBodyContainer = this.__createTableAndParent(this.getID() + this.__TABLE_BODY_CONTAINER_ID,this.getID() + this.__TABLE_BODY_ID);
		divBodyContainer.style.overflow = "auto";
		parentElement.appendChild(divBodyContainer);
		this.__tblBody = divBodyContainer.firstChild;
	}
};

nsGrid.__renderBody = function(fromSort)
{
	this.__tblBody.deleteTHead();
    if(this.__tblBody.tBodies && this.__tblBody.tBodies.length > 0)
    {
    	this.__tblBody.removeChild(this.__tblBody.tBodies[0]);
    }
    var body = document.createElement("tbody");
	this.__tblBody.appendChild(body);
	this.__createBodyHeader();
	this.__createBodyBody(this.__dataSource,0,0);
	this.__alignTables(fromSort);
	var divBodyContainer = this.getElement(this.getID() + this.__TABLE_BODY_CONTAINER_ID);
	this.util.makeElementUnselectable(divBodyContainer,true);
};

nsGrid.__createBodyHeader= function()
{
	var header = this.__tblBody.createTHead();
	var headerRow = header.insertRow(-1);
    headerRow.style.height = "auto";
    for (var colIndex = 0; colIndex < this.columns.length; colIndex++)
    {
        var colItem = this.columns[colIndex];
        var headerCell = headerRow.insertCell(-1);
        if(colItem.hasOwnProperty("width") && !isNaN(colItem["width"]))
        {
        	headerCell.style.width = Number(colItem["width"]) + "px";
        }
        else
        {
        	headerCell.style.width = this.__DEFAULT_COLUMN_WIDTH + "px";
        }
        headerCell.style.height = "0px";
    }
};

nsGrid.__createBodyBody= function(dataSet,parentIndex,level)
{
    if(dataSet)
    {
    	 var body = this.__tblBody.tBodies[0];
    	 for (var rowIndex = 0; rowIndex < dataSet.length; rowIndex++)
	     {
    		var item = dataSet[rowIndex];
	        var row = body.insertRow(-1);
	        if(parentIndex > 0)
	        {
	        	row.setAttribute("parent-row-count",parentIndex);
	        }
	        var totalRowCount = this.__getTotalRows() - 1;
	        var className = ((totalRowCount % 2) === 0) ? this.__CLASS_EVEN_ROW : this.__CLASS_ODD_ROW;
	        this.__createBodyRow(row,item,className,totalRowCount,level);
	        this.util.addStyleClass(row , className);
	        this.util.addEvent(row,"mouseover",this.__rowMouseHover.reference(this));
	        this.util.addEvent(row,"mouseout",this.__rowMouseHover.reference(this));
	        this.util.addEvent(row,"click",this.__rowClickHandler.reference(this));
	        if(item.hasOwnProperty(this.__childField) && item[this.__childField]  && item[this.__childField].length > 0)
            {
	        	this.__createBodyBody(item[this.__childField],totalRowCount,level + 1);
            }
	     }
     }
};

nsGrid.__createBodyRow= function(row,item,className,parentIndex,level)
{
    if(item)
    {
        for (var colIndex = 0; colIndex < this.columns.length; colIndex++)
        {
        	var colItem = this.columns[colIndex];
        	if((colItem.hasOwnProperty("dataField") && colItem["dataField"]) || (colItem.hasOwnProperty("templateID") && colItem["templateID"])
        			|| (colItem.hasOwnProperty("itemRenderer") && colItem["itemRenderer"]))
        	{
        		var dataField = colItem["dataField"];
        		var templateID = colItem["templateID"];
	            var cell = row.insertCell(-1);
	            if(className && className.length > 0)
	            {
	                this.util.addStyleClass(cell , "dataGridCell");
	            }
	            var cellDiv = this.util.createDiv(null);
	            cell.appendChild(cellDiv);
	            if(colIndex == 0)
	            {
	            	if(level == 0)
	            	{
	            		cell.style.paddingLeft = "5px";
	            	}
	            	else
	            	{
	            		cell.style.paddingLeft = (20 * level) + "px";
	            	}
	            	
	            }
	            if(colIndex == 0 && item.hasOwnProperty(this.__childField) && item[this.__childField]  && item[this.__childField].length > 0)
	            {
	            	this.util.addStyleClass(cellDiv,this.__CLASS_GROUP_CELL);
	            	this.__createArrow(parentIndex,cellDiv);
	            	var cellText = this.util.createDiv(null);
	            	this.__addCellText(item,cellText,colItem,colIndex);
	            	cellDiv.appendChild(cellText);
	            }
	            else
	            {
	            	this.__addCellText(item,cellDiv,colItem,colIndex);
	            }
        	}
        }
    }
};

nsGrid.__createArrow = function(parentRowCount,parentElement)
{
	 if(parentElement)
	 {
		 var cellArrowParent = this.util.createDiv(null);
		 var compArrow = this.util.createDiv(this.getID() + "compArrow" + parentRowCount,this.__CLASS_ARROW_EXPANDED);
		 compArrow.setAttribute("parent-row-count",parentRowCount);
		 this.util.addEvent(compArrow,"click",this.__arrowClickHandler.reference(this));
		 cellArrowParent.appendChild(compArrow);
		 parentElement.appendChild(cellArrowParent);
		 
		 return compArrow;
	 }
	 return null;
};

/******************************Create Sort Components*******************************/
nsGrid.__addAscendingIndicator = function(target)
{
     if(target)
     {
          var indicator_Asc = document.createElement("span");
          indicator_Asc.setAttribute("id",this.getID() + "indicator_Asc");
          indicator_Asc.innerHTML = this.util.isBrowserIE() ? "&nbsp<font face='webdings'>5</font>" : "&nbsp;&#x25B4;";
          target.firstChild.appendChild(indicator_Asc);
          this.util.addStyleClass(target,this.__CLASS_SORTING_ASC);
     }
};

nsGrid.__removeAscendingIndicator= function(target)
{
     if(target)
     {
          this.util.removeStyleClass(target,this.__CLASS_SORTING_ASC);
          var indicator_Asc = this.getElement(this.getID() + "indicator_Asc");
          if(indicator_Asc && indicator_Asc.parentNode)
          {
        	  indicator_Asc.parentNode.removeChild(indicator_Asc);
          }
     }
};

nsGrid.__addDescendingIndicator= function(target)
{
    if(target)
    {
         var indicatorDesc = document.createElement("span");
         indicatorDesc.setAttribute("id",this.getID() + "indicator_Desc");
         indicatorDesc.innerHTML = this.util.isBrowserIE() ? "&nbsp<font face='webdings'>6</font>" : "&nbsp;&#x25BE;";
         target.firstChild.appendChild(indicatorDesc);
         this.util.addStyleClass(target,this.__CLASS_SORTING_DESC);
    }
};

nsGrid.__removeDescendingIndicators= function(target)
{
     if(target)
     {
    	 this.util.removeStyleClass(target,this.__CLASS_SORTING_DESC);
          var indicator_Desc = this.getElement(this.getID() + "indicator_Desc");
          if(indicator_Desc && indicator_Desc.parentNode)
          {
        	  indicator_Desc.parentNode.removeChild(indicator_Desc);
          }
     }
};

nsGrid.__resetIndicators= function(target)
{
     if(target)
     {
          this.__removeAscendingIndicator(target);
          this.__removeDescendingIndicators(target);
     }
};

nsGrid.__resetColumnHeaders= function()
{
     var tblHeaderBody = null;
     //safari doesnot support table.tHead
     if (this.__tblHeader.tBodies && this.__tblHeader.tBodies.length > 0)
     {
    	 tblHeaderBody = this.__tblHeader.tBodies[0];
     }
     //two header not allowed
     if (tblHeaderBody.rows.length != 1)
     {
          return;
     }
     var headers = tblHeaderBody.rows[0].cells;
     for (var colCount = 0; colCount < headers.length; colCount++)
     {
          this.__resetIndicators(headers[colCount]);
     }
};

/******************************End of Create Sort Components*******************************/

/******************************Event Listeners *************************************/

nsGrid.__arrowClickHandler = function(event)
{
	var target = event.target;
	if(target && target.hasAttribute("parent-row-count"))
	{
		var rowNum = target.getAttribute("parent-row-count");
		var isCollapse = true;
		if(target.className == this.__CLASS_ARROW_COLLAPSED)
		{
			isCollapse = false;
		}
		this.__hideShowRow(rowNum,target,isCollapse);
	}
};

nsGrid.__hideShowRow = function(rowCount,compArrow,isCollapse)
{
	if(rowCount > 0)
	{
		var arrChildRows = [];
		this.__getChildRows(arrChildRows,rowCount,isCollapse);
		if(arrChildRows && arrChildRows.length > 0)
		{
			for(var count = 0;count < arrChildRows.length;count++)
			{
				var row = arrChildRows[count];
				if(isCollapse)
				{
					row.style.display = "none";
					if(row && row.hasAttribute("parent-row-count"))
					{
						var rowParentCount = row.getAttribute("parent-row-count");
						if(rowParentCount)
						{
							var divArrow = this.__getArrows(rowParentCount);
							if(divArrow)
							{
								divArrow.className = this.__CLASS_ARROW_COLLAPSED;
							}
						}
					}
				}
				else
				{
					row.style.display = "";
				}
			}
			if(compArrow)
			{
				if(isCollapse)
				{
					compArrow.className = this.__CLASS_ARROW_COLLAPSED;
				}
				else
				{
					compArrow.className = this.__CLASS_ARROW_EXPANDED;
				}
			}
		}
	}
};

nsGrid.__headerClickHandler= function(event)
{
	if(this.__onGridLine)
	{
		 return;
	}
     var target = this.util.getTarget(event);
     //adding the below condition so that if we add a span or a font and click on it then we should navigate till we find the header object
     target = this.util.findParent(target,"TD");
     var columnDetail = this.columns[target.getAttribute("currentColumnIndex")];
     if(columnDetail && columnDetail.sortable)
     {
    	  var sortAscending = false;
    	  if (this.util.hasStyleClass(target,this.__CLASS_SORTING_ASC) || this.util.hasStyleClass(target,this.__CLASS_SORTING_DESC))
          {
               if(this.util.hasStyleClass(target,this.__CLASS_SORTING_ASC))
               {
                    this.__removeAscendingIndicator(target);
                    sortAscending = false;
               }
               else if(this.util.hasStyleClass(target,this.__CLASS_SORTING_DESC))
               {
            	   this.__removeDescendingIndicators(target);
            	   sortAscending = true;
               }
          }
    	  else
    	  {
    		  this.__resetColumnHeaders();
    		  sortAscending = !columnDetail.sortDescending;
    	  }
    	  if(sortAscending)
          {
    		  this.__addAscendingIndicator(target);
          }
          else
          {
        	  this.__addDescendingIndicator(target);
          }
          var dataSorted = this.__dataSource.slice(0);
          this.__sortArrOfObjectsByParam(this.__dataSource,target.sortFunction,this.columns[target.getAttribute("currentColumnIndex")].dataField,sortAscending);
          this.__renderBody(true);
     }
};

nsGrid.__headerMouseOverHandler = function(event)
{
	var target = this.util.getTarget(event);
	target = this.util.findParent(target,"TD");
	this.util.addEvent(target,"mousemove",this.__headerMouseMoveHandler.reference(this));
	this.util.addEvent(target,"mousedown",this.__headerMouseDownHandler.reference(this));
};

nsGrid.__headerMouseOutHandler = function(event)
{
	var target =  this.util.getTarget(event);
	target =  this.util.findParent(target,"TD");
	this.__onGridLine = false;
	
	this.util.removeEvent(target,"mousemove",this.__headerMouseMoveHandler);
	this.util.removeEvent(target,"mousedown",this.__headerMouseDownHandler);
	
	this.util.removeStyleClass(target,"resize-handle-active");
};

nsGrid.__headerMouseMoveHandler = function(event)
{
	event = this.util.getEvent(event);
	var target = this.util.getTarget(event);
	target = this.util.findParent(target,"TD");
	if(this.__isMouseOnElement(target,event.clientX,event.clientY))
	{
		this.util.addStyleClass(target,"resize-handle-active");
		this.__onGridLine = true;
	}
	else
	{
		this.util.removeStyleClass(target,"resize-handle-active");
		this.__onGridLine = false;
	}
};

nsGrid.__headerMouseDownHandler = function(event)
{
	event = this.util.getEvent(event);
    var target = this.util.getTarget(event);
    target = this.util.findParent(target,"TD");
    this.__startResize(event,target);
};

nsGrid.__rowMouseHover= function(event)
{
	 if(this.__enableMouseHover)
	 {
		 event = this.util.getEvent(event);
	     var target = this.util.getTarget(event);
	     target = this.util.findParent(target,"TR");
	     if (event.type == "mouseover")
	     {
	    	 this.util.addStyleClass(target,"dataGridHover");
	     }
	     else if (event.type == "mouseout")
	     {
	    	 this.util.removeStyleClass(target,"dataGridHover");
	     }
	 }
	 return false;
};

nsGrid.__rowClickHandler= function(event)
{
	event = this.util.getEvent(event);
    var target = this.util.getTarget(event);
    target = this.util.findParent(target,"TR");
    //Multiselection Check
    if (event.shiftKey && this.__enableMultiSelection)
    {
    	this.__multiSectionHandler(target);
    }
    else if(event.ctrlKey && this.__enableMultiSelection)
    {
      if(this.__isRowSelected(target))
      {
    	  this.__markRowUnselected(target);
      }
      else
      {
    	  this.__markRowSelected(target);
      }
    }
    else
    {
    	this.__clearAllRowSelection();
    	this.__markRowSelected(target);
    } 
};

/******************************End of Event Listeners*************************************/

/******************************Column Resize Logic*************************************/

nsGrid.__resize = function(table,cell, desirableWidth)
{
	var cellIndex = cell.cellIndex;
	var cellPaddingLeft = this.util.getDimensionAsNumber(cell,cell.style.paddingLeft);
	var cellPaddingRight = this.util.getDimensionAsNumber(cell,cell.style.paddingRight);
	var pad = parseInt(cellPaddingLeft,10) + parseInt(cellPaddingRight,10);
	var setWidth = Math.max(desirableWidth - pad, this.__columnMinWidth);
	cell.style.width = setWidth + "px";
};

nsGrid.__startResize = function(event,target)
{
	if(!this.__onGridLine)
	{
		return;
	}
	
	 this.util.removeEvent(target,"click",this.__headerClickHandler);
     this.util.removeEvent(target,"mouseover",this.__headerMouseOverHandler);
     this.util.removeEvent(target,"mouseout",this.__headerMouseOutHandler);
     this.util.removeEvent(target,"mousedown",this.__headerMouseDownHandler);
	
	this.__resizingHeader = target;
	this.__resizingHeaderCell = this.__getHeaderTopCell(target);
	this.__resizingBodyCell = this.__getBodyTopCell(target);
	
	this.__createResizeHandler(event);
	
	this.util.addEvent(document,"mousemove",this.__doResize.reference(this));
	this.util.addEvent(document,"mouseup",this.__endResize.reference(this));
	
	event.stopImmediatePropagation();
};

nsGrid.__doResize = function(event)
{
	//putting this check as somehow the document evenlistener is not getting detached
	if(!this.__resizingHeader)
	{
		return;
	}
	event = this.util.getEvent(event);
	this.__resizeHandler.style.left = event.clientX + "px";
	
	return false;
};

nsGrid.__endResize = function(event)
{
	//putting this check as somehow the document evenlistener is not getting detached
	if(!this.__resizingHeader)
	{
		return;
	}
	event = this.util.getEvent(event);
	var selectedHeader = this.__resizingHeader;
	var tableHeader = this.__tblHeader;
	var tableBody = this.__tblBody;
	var desiredWidth = event.clientX - this.util.getCumulativeOffset(selectedHeader).x;
	//this.__resize(tableHeader,this.__resizingHeaderCell,desiredWidth);
	this.__resize(tableBody,this.__resizingBodyCell,desiredWidth);
	
	this.util.removeEvent(document,"mousemove",this.__doResize);
	this.util.removeEvent(document,"mouseup",this.__endResize);
	
	if(this.__resizeHandler)
	{
		document.body.removeChild(this.__resizeHandler);
	}
	
	this.__resizingHeader = null;
	this.__resizingHeaderCell = null;
	this.__resizingBodyCell = null;
	this.__resizeHandler = null;
	event.stopImmediatePropagation();
	this.__alignTables(false);
	this.util.removeEvent(selectedHeader,"mouseout",this.__headerMouseOutHandler);
	this.util.removeEvent(selectedHeader,"click",this.__headerClickHandler);
};

nsGrid.__createResizeHandler = function(event)
{
	this.__resizeHandler = this.util.createDiv(null,"resize-handle");
	this.__resizeHandler.style.top = this.util.getCumulativeOffset(this.__resizingHeader).y + "px";
	this.__resizeHandler.style.left = event.clientX + "px";
	var divHeader = this.getElement(this.getID() + this.__TABLE_HEADER_CONTAINER_ID);
	var divBody = this.getElement(this.getID() + this.__TABLE_BODY_CONTAINER_ID);
	var scrollBarWidth = this.util.getScrollBarWidth(divBody);
	this.__resizeHandler.style.height = (divHeader.offsetHeight + divBody.offsetHeight - scrollBarWidth) + "px";
	
	document.body.appendChild(this.__resizeHandler);
}

nsGrid.__getHeaderTopCell = function(cell)
{
	if(cell)
	{
		var index = cell.cellIndex;
		if(this.__tblHeader && this.__tblHeader.tHead && this.__tblHeader.tHead.rows.length > 0 && this.__tblHeader.tHead.rows[0].cells.length > 0)
		{
			var headerCells = this.__tblHeader.tHead.rows[0].cells;
			return headerCells[index];
		}
	}
	return null;
};

nsGrid.__getBodyTopCell = function(cell)
{
	if(cell)
	{
		var index = cell.cellIndex;
		if(this.__tblBody && this.__tblBody.tHead && this.__tblBody.tHead.rows.length > 0 && this.__tblBody.tHead.rows[0].cells.length > 0)
		{
			var bodyCells = this.__tblBody.tHead.rows[0].cells;
			return bodyCells[index];
		}
	}
	return null;
};

/******************************End of Column Resize Logic*************************************/

/******************************************************Start of Selection Functions*************************************************************/

nsGrid.__isRowSelected= function(row)
{
    if(row)
    {
        return this.util.hasStyleClass(row,"dataGridSelection");
    }   
    return false;
};

nsGrid.__markRowSelected= function(row)
{
    if(row)
    {
        if(!this.__isRowSelected(row))
        {
        	this.util.addStyleClass(row,"dataGridSelection");   
            this.selectedItems.push(row);
            if(this.util.isFunction(this.__rowSelectionHandler))
            {
                this.__rowSelectionHandler(row.source);
            }
        }
    }
};

nsGrid.__markRowUnselected= function(row)
{
    if(this.__isRowSelected(row))
    {
    	this.util.removeStyleClass(row,"dataGridSelection");
        for (var count=0; count < this.selectedItems.length ; count++)
        {
            if (this.selectedItems[count].index === row.index)
            {
                this.selectedItems.splice(count,1);
                break;
            }
        }
        if(this.util.isFunction(this.__rowUnSelectionHandler))
        {
            this.__rowUnSelectionHandler(row.source);
        }
    }
};

nsGrid.__clearAllRowSelection= function()
{
    for (var count=0; count < this.selectedItems.length ; count++)
    {
        if (this.selectedItems[count])
        {
        	this.util.removeStyleClass(this.selectedItems[count],"dataGridSelection");
        }
    }
    this.selectedItems = new Array();
};

nsGrid.__multiSectionHandler= function(lastRow)
{
	 if(!lastRow)
	 {
		 return;
	 }
	 if (this.selectedItems.length === 0)
	 {
		 this.__isRowSelected(lastRow);
	     return;
	 }
	 var firstRow = this.selectedItems[this.selectedItems.length - 1];
	 if(lastRow.index === firstRow.index)
	 {
		 this.__markRowUnselected(lastRow);
		 return;
	 }
	 var isDown = (lastRow.index > firstRow.index);
	 var isSelection = !this.__isRowSelected(lastRow);
	 var navigateRow = firstRow;
	 do
	 {
		  navigateRow = isDown ? navigateRow.nextSibling : navigateRow.previousSibling;
		  if (isSelection)
		  {
			  this.__markRowSelected(navigateRow);
		  }
		  else
		  {
			  this.__markRowUnselected(navigateRow);
		  }
	 }
	 while(navigateRow.index != lastRow.index);
};

nsGrid.__setVisibilityOfColumn = function(index,isVisible)
{
	var style;
    if (isVisible) 
    {
    	style = "";
    }
    else
    {
    	style = "none";
    }
	var rows = this.__tblHeader.getElementsByTagName("tr");
	for (var count = 0; count < rows.length;count++) 
	{
	     var cells = rows[count].getElementsByTagName("td");
	     if(cells.length > index)
	     {
	    	 cells[index].style.display = style;
	     }
	     else
	     {
	    	 return;
	     }
	}
	rows = this.__tblBody.getElementsByTagName("tr");
	for (var count = 0; count < rows.length;count++) 
	{
	     var cells = rows[count].getElementsByTagName("td");
	     cells[index].style.display = style;
	}
	
};

/******************************************************End of Selection Functions*************************************************************/
/******************************************************Start of Sorting Logic*************************************************************/
//This method is based on Stuart Langridge's "sorttable" code
nsGrid.__determineSortFunction= function(item)
{ 
      var sortFunction = "sortCaseInsensitive";
     
      if (item.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\dgetElement/))
      {
          sortFunction = "sortDate";
      }
      if (item.match(/^\d\d[\/-]\d\d[\/-]\d\dgetElement/))
      {
          sortFunction = "sortDate";
      }
      if (item.match(/^[Â£$]/))
      {
          sortFunction = "sortCurrency";
      }
      if (item.match(/^\d?\.?\d+getElement/))
      {
             sortFunction = "sortNumeric";
      }
      if (item.match(/^[+-]?\d*\.?\d+([eE]-?\d+)?getElement/))
      {
             sortFunction = "sortNumeric";
      }
     
      return sortFunction;
};

nsGrid.__sortArrOfObjectsByParam= function(arrToSort,sortFunctionName,dataField,sortAscending)
{
     if(sortAscending == null || sortAscending == undefined)
     {
         sortAscending = true;  // default to true
     }
     arrToSort.sort(function (item1, item2)
     {
         var retValue = 0;
         if (typeof this[sortFunctionName] === "function")
         {
             retValue = this[sortFunctionName](item1, item2 , dataField, sortAscending);
         }
         return retValue;
     }.reference(this));
};
 
nsGrid.__sortCaseInsensitive= function(item1, item2 , dataField, sortAscending)
{
	if(!item1[dataField] && !item2[dataField])
	{
		return 0;
	}
	var retValue = -1;
	if(!item1[dataField])
	{
		retValue = -1;
	}
	else if(!item2[dataField])
	{
		retValue = 1;
	}
	else
	{
		var firstString = item1[dataField].toLowerCase();
	    var secondString = item2[dataField].toLowerCase();
	      
	    if(firstString == secondString)
	    {
	    	return 0;
	    }
	    if (firstString < secondString)
	    {
	        retValue = -1;
	    }
	    else
	    {
	        retValue = 1;
	    }
	}
	
    if(sortAscending)
    {
        return retValue; 
    }
    return (retValue * -1);
};
 
nsGrid.__sortDate= function(item1, item2 , dataField, sortAscending)
{
      // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
      var firstDateString = item1[dataField];
      var secondDateString = item2[dataField];
      var firstDate, secondDate, year = -1;
     
      if (firstDateString.length == 10)
      {
           firstDate = firstDateString.substr(6,4) + firstDateString.substr(3,2) + firstDateString.substr(0,2);
      }
      else
      {
           year = firstDateString.substr(6,2);
           if (parseInt(year) < 50)
           {
                year = "20" + year;
           }
           else
           {
                year = "19" + year;
           }
           firstDate = year + firstDateString.substr(3,2) + firstDateString.substr(0,2);
      }
     
      if (secondDateString.length == 10)
      {
           secondDate = secondDateString.substr(6,4)+secondDateString.substr(3,2)+secondDateString.substr(0,2);
      }
      else
      {
           year = secondDateString.substr(6,2);
           if (parseInt(year) < 50)
           {
                year = "20" + year;
           }
           else
           {
                year = "19" + year;
           }
           secondDate = year + secondDateString.substr(3,2) + secondDateString.substr(0,2);
      }
     
      if (firstDate == secondDate)
      {
          return 0;
      }
      var retValue = -1;
      if (firstDate < secondDate)
      {
          retValue = -1;
      }
      else
      {
          retValue = 1;
      }
      if(sortAscending)
      {
          return retValue; 
      }
      return (retValue * -1);
};

nsGrid.__sortCurrency= function(item1, item2 , dataField, sortAscending)
{
      var firstCurrency = item1[dataField].replace(/[^0-9.]/g,"");
      var secondCurrency = item2[dataField].replace(/[^0-9.]/g,"");
      if(sortAscending)
      {
          return parseFloat(firstCurrency) - parseFloat(secondCurrency); 
      }
      return parseFloat(secondCurrency) - parseFloat(firstCurrency);
};

nsGrid.__sortNumeric= function(item1, item2 , dataField, sortAscending)
{
      var firstNumber = parseFloat(item1[dataField]);
      if (isNaN(firstNumber))
      {
          firstNumber = 0;
      }
      var secondNumber = parseFloat(item2[dataField]);
      if (isNaN(secondNumber))
      {
          secondNumber = 0;
      }
      if(sortAscending)
      {
          return (firstNumber - secondNumber);
      }
      return (secondNumber - firstNumber);
};

nsGrid.__reverseTable= function()
{
    // reverse the rows in a tbody
    var tbody = this.tblBody.tBodies[0];
    var indexCount = 0;
    newrows = new Array();
    for (var rowCount=0; rowCount < tbody.rows.length; rowCount++)
    {
        newrows[newrows.length] = tbody.rows[rowCount];
    }
    for (var rowCount=newrows.length-1; rowCount >= 0; rowCount--)
    {
    	var row = newrows[rowCount];
    	row.index = indexCount;
    	indexCount ++;
        tbody.appendChild(row);
    }
    delete newrows;
};
/******************************************************End of Sorting Logic*************************************************************/

/******************************Custom Function *************************************/

nsGrid.__createTableAndParent = function(containerID,tableID)
{
	var divTableContainer = this.util.createDiv(containerID); 
	var table = document.createElement("TABLE");
	table.setAttribute("id",tableID);
	this.util.addStyleClass(table , this.__CLASS_TABLE);
	divTableContainer.appendChild(table);
	return divTableContainer;
};

nsGrid.__alignTables = function(fromSort)
{
	var divOuterContainer = this.getElement(this.getID() + this.__OUTER_CONTAINER_ID);
	var divHeader = this.getElement(this.getID() + this.__TABLE_HEADER_CONTAINER_ID);
	var divBody = this.getElement(this.getID() + this.__TABLE_BODY_CONTAINER_ID);
	var divTitleBar = this.getElement(this.getID() + this.__TITLE_CONTAINER_ID);
	var topHeight = divHeader.offsetHeight;
	if(divTitleBar)
	{
		topHeight += divTitleBar.offsetHeight;
	}
	divBody.style.height = (divOuterContainer.offsetHeight - topHeight) + "px";
	var tableHeader = this.__tblHeader;
	var tableBody = this.__tblBody;
	var scrollBarWidth = this.util.getScrollBarWidth(divBody);
	divHeader.style.width = (divBody.offsetWidth - scrollBarWidth) + "px";
	this.util.addEvent(divBody,"scroll",this.__synchronizeTables.reference(this));
	
	if(tableHeader.tHead && tableHeader.tHead.rows.length > 0 && tableBody.tHead && tableBody.tHead.rows.length)
	{
		var headerCells = tableHeader.tHead.rows[0].cells;
		var bodyCells = tableBody.tHead.rows[0].cells;
		if(headerCells && headerCells.length > 0 && bodyCells && bodyCells.length > 0)
		{
			var totalWidth = 0;
			var count = 0;
			var widthToBeSet = 0;
			if(fromSort)
			{
				for(count = 0;count < headerCells.length;count++)
				{
					widthToBeSet = this.util.getDimensionAsNumber(headerCells[count],headerCells[count].style.width);
					if(widthToBeSet === 0)
					{
						widthToBeSet = headerCells[count].offsetWidth;
					}
					totalWidth += widthToBeSet;
					bodyCells[count].style.width = widthToBeSet + "px";
					headerCells[count].style.width = widthToBeSet + "px";
				}
			}
			else
			{
				for(count = 0;count < headerCells.length;count++)
				{
					widthToBeSet = this.util.getDimensionAsNumber(bodyCells[count],bodyCells[count].style.width);
					if(widthToBeSet === 0)
					{
						widthToBeSet = bodyCells[count].offsetWidth;
					}
					totalWidth += widthToBeSet;
					headerCells[count].style.width = widthToBeSet + "px";
					bodyCells[count].style.width = widthToBeSet + "px";
				}
			}
			tableBody.style.width = totalWidth + "px";
			tableHeader.style.width = totalWidth + "px";
		}
	}
};

nsGrid.__synchronizeTables = function(event) 
{
	var divHeader = this.getElement(this.getID() + this.__TABLE_HEADER_CONTAINER_ID);
	divHeader.scrollLeft = event.srcElement.scrollLeft;
};


nsGrid.__addCellText = function(item,div,colItem,colIndex)
{
	var text = "";
	var dataField = colItem["dataField"];
	var templateID = colItem["templateID"];
	var itemRenderer = colItem["itemRenderer"];
	if(itemRenderer)
	{
		var strRenderer = itemRenderer(item,dataField,this.__getTotalRows() - 1,colIndex);
		if(strRenderer)
		{
			var compBodySpan = document.createElement("span");
			compBodySpan.innerHTML = strRenderer;
			div.appendChild(compBodySpan);
			return ;
		}
	}
	if(templateID)
	{
		this.util.addTemplateInContainer(div,templateID);
		return;
	}
	if(item && item.hasOwnProperty(dataField) && item[dataField])
    {
		text = item[dataField];
    }
	div.appendChild(document.createTextNode(text));
};

nsGrid.__getChildRows = function(sendRows,rowCount,includeAllChildren)
{
	var arrRows = this.__tblBody.querySelectorAll("tr");
	if(arrRows && arrRows.length > 0)
	{
		for(var count = 0;count < arrRows.length;count++)
		{
			var row = arrRows[count];
			if(row && row.hasAttribute("parent-row-count"))
			{
				var rowParentCount = row.getAttribute("parent-row-count");
				if(rowParentCount)
				{
					if(includeAllChildren)
					{
						if(rowParentCount == rowCount)
						{
							sendRows[sendRows.length] = row;
							this.__getChildRows(sendRows,count,includeAllChildren);
						}
					}
					else if(rowParentCount == rowCount)
					{
						sendRows[sendRows.length] = row;
					}
				}
			}
		}
	}
};

nsGrid.__getArrows = function(rowCount)
{
	var arrDivs = this.__tblBody.querySelectorAll("div");
	if(arrDivs && arrDivs.length > 0)
	{
		for(var count = 0;count < arrDivs.length;count++)
		{
			var div = arrDivs[count];
			if(div && div.hasAttribute("parent-row-count"))
			{
				var rowParentCount = div.getAttribute("parent-row-count");
				if(rowParentCount && rowParentCount == rowCount)
				{
					return div;
				}
			}
		}
	}
};

nsGrid.__getExpandCollapseComponent = function(row)
{
	var compArrow  = row;
	while(!this.util.hasStyleClass(compArrow,this.__CLASS_ARROW_EXPANDED) && !this.util.hasStyleClass(compArrow,this.__CLASS_ARROW_COLLAPSED))
	{
		compArrow = getFirstChild(compArrow);
	}
	if(compArrow && (this.util.hasStyleClass(compArrow,this.__CLASS_ARROW_EXPANDED) || this.util.hasStyleClass(this.__CLASS_ARROW_COLLAPSED)))
	{
		return compArrow;
	}
	return null;
};

nsGrid.__getTotalRows = function()
{
	if(this.__tblBody)
	{
		return this.__tblBody.rows.length;
	}
	return 0;
};


nsGrid.__isMouseOnElement = function(element, currentX, currentY)
{
	var offset = this.util.getCumulativeOffset(element);
	return (currentY >= offset.y &&
			currentY <  offset.y + element.offsetHeight &&
            currentX >= offset.x + element.offsetWidth - 5 &&
            currentX <  offset.x + element.offsetWidth);
};

/******************************End of Custom Function *************************************/



document.registerElement("ns-grid", {prototype: nsGrid});

---------------------------------------------------------------------
in jsp file:
<body onload="loadHandler();">
function loadHandler()
		{
			ns.onload(function(){
				//alert('here');
				var dgDemo = document.getElementById("dgDemo");
				dgDemo.dataSource(dataSource);
			});	
		}
