var nsList = Object.create(nsContainerBase);

nsList.initializeComponent = function() 
{
	this.base.initializeComponent();
	this.ITEM_SELECTED = "itemSelected";
	this.ITEM_UNSELECTED = "itemUnselected";
	
	this.__resuableRenderRequired = false;
	this.__dataProvider = null;
	this.__labelField = "label";
	this.__labelFunction = null;
	this.__templateID = null;
	this.__itemRenderer = null;
	this.__setDataCallBack = null;
	this.__clearDataCallBack = null;
	this.__currentIndex = -1;
	this.__selectedIndex = -1;
	this.__selectedItem = null;
	this.__allowMultipleSelection = false;
	this.__selectedItems = new Array();	
	
	this.__outerContainer = null;
	this.__childContainer = null;
	this.__listContainer = null;
	
	
	this.__availableHeight = 0;
	this.__scrollHeight = 0;
	this.__listItemHeight = 0;
	this.__rowCount = 0;
	this.__maxRows = 0;
	this.__visibleRows = 0;
	this.__hiddenRows = 0;
	this.__availableRows = 0;
	this.__topHiddenRows = 0;
	this.__bottomHiddenRows = 0;
	this.__maxCount = 0;
	this.__startArrayElement = -1;
	this.__scrollOffset = 0;
	
	this.__positionX = 0;
	this.__positionY = 0;
	this.__changeX = 0;
	this.__changeY = 0;
};

nsList.setComponentProperties = function() 
{
	if(this.hasAttribute("resuableRenderRequired"))
	{
		this.__resuableRenderRequired =  Boolean.parse(this.getAttribute("resuableRenderRequired"));
	}
	if(this.hasAttribute("labelField"))
	{
		this.__labelField = this.getAttribute("labelField");
	}
	if(this.hasAttribute("labelFunction"))
	{
		this.__labelFunction = this.getAttribute("labelFunction");
	}
	if(this.hasAttribute("template"))
	{
		this.__templateID = this.getAttribute("template");
	}
	if(this.hasAttribute("setDataCallBack"))
	{
		this.__setDataCallBack = this.getAttribute("setDataCallBack");
	}
	if(this.hasAttribute("clearDataCallBack"))
	{
		this.__clearDataCallBack  = this.getAttribute("clearDataCallBack");
	}
	if(this.hasAttribute("allowMultipleSelection"))
	{
		this.__allowMultipleSelection =  Boolean.parse(this.getAttribute("allowMultipleSelection"));
	}
	this.__setTemplate();
	this.base.setComponentProperties();
};

nsList.setDataProvider = function(dataProvider)
{
	this.__dataProvider = dataProvider;
	if(this.__dataProvider && this.__dataProvider.length > 0)
	{
		this.__calculateComponentParameters();
		this.__createComponents();
		if(this.__resuableRenderRequired)
		{
			this.__createReusableRendererComponents();
			this.__bindRenderers();
			this.__calculateDimensions();
			this.__renderList(0);
			this.__setPosition(0,0);
		}
	}
};

nsList.setSelectedIndex = function(selectedIndex,animationRequired)
{
	if(selectedIndex > -1 && this.__dataProvider && selectedIndex < this.__dataProvider.length)
	{
		if(this.__resuableRenderRequired)
		{
			var targetDimension = parseInt(selectedIndex) * this.__listItemHeight;
			if(animationRequired)
			{
				var animation = new this.util.animation(this.__outerContainer,[
	       	  	    {
	       	  	      time: 1,
	       	  	      property:"scrollTop",
	       	  	      target: targetDimension,
	       	  	    }
	       	  	]);
       	  	  	animation.animate();
			}
			else
			{
				this.__outerContainer.scrollTop = targetDimension;
			}
		}
	}
};

nsList.getSelectedIndex = function()
{
	return this.__selectedIndex;
};

nsList.getSelectedItem = function()
{
	return this.__selectedItem;
};



nsList.deselectAll = function()
{
	if(this.__listContainer && this.__listContainer.children && this.__listContainer.children.length > 0)
	{
		for(var count = 0; count < this.__listContainer.children.length; count++) 
		{
			var item = this.__listContainer.children[count];
			this.util.removeStyleClass(item,"itemHover");
			if(this.util.hasStyleClass(item,"selected"))
			{
				this.util.removeStyleClass(item,"selected");
				this.util.dispatchEvent(this,this.ITEM_UNSELECTED,item.data);
			}
			
		}
	}
};

nsList.__setTemplate = function()
{
	if(this.__templateID)
	{
		this.__itemRenderer = this.util.getTemplate(this.__templateID);
	}
	else
	{
		var renderer = new this.util.defaultRenderer();
		this.__itemRenderer = renderer.getRenderer();
		this.__setDataCallBack = renderer.setData;
		this.__clearDataCallBack = renderer.clearData;
	}
};

nsList.__createComponents = function()
{
	if(!this.__outerContainer)
	{
		this.__outerContainer = this.util.createDiv(this.getID() + "#container","nsListParentContainer");
		this.__outerContainer.style.height = this.__availableHeight + "px";
		this.addChild(this.__outerContainer);
		this.__childContainer = this.util.createDiv(this.getID() + "#childContainer","nsListChildContainer");
		this.__outerContainer.appendChild(this.__childContainer);
		this.__listContainer = document.createElement("ul");
		this.util.addStyleClass(this.__listContainer,"nsListContainer");
		this.__childContainer.appendChild(this.__listContainer);
	}
};

nsList.__createReusableRendererComponents = function()
{
	if(this.__outerContainer)
	{
		var divHeight = this.util.createDiv(null,"nsListScrollerCause");
		divHeight.style.maxHeight = this.__scrollHeight + "px";
		divHeight.style.height = this.__scrollHeight + "px";
		this.__outerContainer.appendChild(divHeight);
		for(var count = 0; count <= this.__rowCount; count++) 
		{
			 var listItem = document.createElement("li");
			 this.util.addStyleClass(listItem,"nsListItem");
			 listItem.style.height = this.__listItemHeight + "px";
			 this.util.addEvent(listItem,"click",this.__itemClickHandler.reference(this));
			 this.util.addEvent(listItem,"mouseover",this.__itemMouseOverHandler.reference(this));
		     this.util.addEvent(listItem,"mouseout",this.__itemMouseOutHandler.reference(this));
			 this.__listContainer.appendChild(listItem);
		}
		this.util.addEvent(this.__outerContainer,"scroll",this.__scrollHandler.reference(this));
	}
};

nsList.__itemClickHandler = function(event)
{
	 this.deselectAll();
	 var target = this.util.getTarget(event);
     target = this.util.findParent(target,"li");
     if(target)
     {
    	 this.util.removeStyleClass(target,"itemHover");
    	 if(target.index > -1)
    	 {
    		 this.__selectedIndex = target.index;
    		 this.__selectedItem = target.data;
    		 this.__markRowSelected(target);
    	 }
     }
};

nsList.__itemMouseOverHandler = function(event)
{
	 var target = this.util.getTarget(event);
     target = this.util.findParent(target,"li");
     if(target && target.index > -1)
     {
    	 this.util.addStyleClass(target,"itemHover");
     }
};

nsList.__itemMouseOutHandler = function(event)
{
	 var target = this.util.getTarget(event);
     target = this.util.findParent(target,"li");
     if(target)
     {
    	 this.util.removeStyleClass(target,"itemHover");
     }
};

nsList.__isRowSelected= function(row)
{
    if(row)
    {
        return this.util.hasStyleClass(row,"selected");
    }   
    return false;
};

nsList.__markRowSelected= function(row)
{
    if(row)
    {
        if(!this.__isRowSelected(row))
        {
        	this.util.addStyleClass(row,"selected");   
            this.__selectedItems.push(row);
            this.util.dispatchEvent(this,this.ITEM_SELECTED,row.data);
            console.log("Selected Index is ::" + row.index);
        }
    }
};

nsList.__markRowUnselected= function(row)
{
    if(this.__isRowSelected(row))
    {
    	this.util.removeStyleClass(row,"selected");
    	var isUnselected = false;
        for (var count=0; count < this.__selectedItems.length ; count++)
        {
            if (this.__selectedItems[count].index === row.index)
            {
                this.__selectedItems.splice(count,1);
                isUnselected = true;
                break;
            }
        }
        if(isUnselected)
        {
        	 this.util.dispatchEvent(this,this.ITEM_UNSELECTED,row.data);
        }
    }
};

nsList.__clearAllRowSelection= function()
{
    for (var count=0; count < this.__selectedItems.length ; count++)
    {
        if (this.__selectedItems[count])
        {
        	this.util.removeStyleClass(this.__selectedItems[count],"selected");
        }
    }
    this.__selectedItems = new Array();
};

nsList.__multiSectionHandler= function(lastRow)
{
	 if(!lastRow)
	 {
		 return;
	 }
	 if (this.__selectedItems.length === 0)
	 {
		 this.__isRowSelected(lastRow);
	     return;
	 }
	 var firstRow = this.__selectedItems[this.__selectedItems.length - 1];
	 if(lastRow.index === firstRow.index)
	 {
		 this.__markRowUnselected(lastRow);
		 return;
	 }
	 var isDown = lastRow.index > firstRow.index;
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


nsList.__calculateComponentParameters = function()
{
	if(this.hasAttribute("nsHeight"))
	{
		this.__availableHeight = this.util.getDimensionAsNumber(this,this.getAttribute("nsHeight"));
	}
	else if(this.style.height != "")
	{
		this.__availableHeight  = this.util.getDimensionAsNumber(this,this.style.height);
	}
	else
	{
		this.__availableHeight  = this.offsetHeight;
	}
	var tempRenderer = this.__itemRenderer.cloneNode(true);
	tempRenderer.removeAttribute("id");
	this.addChild(tempRenderer);
	this.__listItemHeight = tempRenderer.offsetHeight;
	this.__rowCount = Math.round(this.__availableHeight/this.__listItemHeight) * 2;
	this.__scrollHeight = ((this.__dataProvider.length) * this.__listItemHeight);
	this.deleteChild(tempRenderer);
};

nsList.__calculateDimensions = function()
{
	if(this.__listContainer.children)
	{
		this.__maxRows = (this.__listContainer.offsetHeight - this.__availableHeight) / this.__listItemHeight;
		this.__availableRows = this.__listContainer.children.length;
		this.__visibleRows = (this.__availableHeight / this.__listItemHeight);
		this.__hiddenRows = this.__availableRows - this.__visibleRows;
		this.__topHiddenRows = Math.floor(this.__hiddenRows / 2);
		this.__bottomHiddenRows = this.__topHiddenRows + (this.__hiddenRows % 2);
		this.__maxCount = Math.max(0,this.__dataProvider.length - this.__visibleRows);
		for(var count = 0; count < this.__listContainer.children.length; count++) 
		{
			this.__listContainer.children[count].originalOrder = count;
		}
	}
};

nsList.__renderList = function(value)
{
	var currentIndex = this.__currentIndex;
	if(value < 0)
	{
		value = 0;
	}
	else if(value > this.__maxCount)
	{
		value = this.__maxCount;
	}
	
	if(this.__currentIndex != value)
	{
		this.__currentIndex = value;
		currentIndex = value;
		var topOffset = 0;
		var minBottomRows = Math.min(this.__bottomHiddenRows,this.__maxCount - this.__currentIndex);
		var minTopRows = Math.min(this.__currentIndex,this.__topHiddenRows);
		topOffset = this.__listContainer.children[0].originalOrder;
		var rowsOnTop = (this.__currentIndex - topOffset % this.__availableRows) % this.__availableRows;
		var rowsOnBottom = this.__availableRows - this.__visibleRows - rowsOnTop;
		var toMove = 0;
		if(this.__currentIndex > currentIndex)
		{
			minTopRows--;
		}
		else if(this.__currentIndex < currentIndex)
		{
			minBottomRows--;
		}
		while(rowsOnBottom < minBottomRows)
		{
			toMove = this.__listContainer.children[0];
			this.__listContainer.removeChild(toMove);
			this.__listContainer.appendChild(toMove);
			rowsOnBottom++;
			rowsOnTop--;
		}
		
		while(rowsOnTop < minTopRows) 
		{
			toMove = this.__listContainer.children[this.__listContainer.children.length - 1];
			this.__listContainer.removeChild(toMove);
			this.__listContainer.insertBefore(toMove,this.__listContainer.children[0]);
			rowsOnTop++;
			rowsOnBottom--;
		}
		rowsOnTop = this.__availableRows - this.__visibleRows - rowsOnBottom; 
		if(rowsOnTop < 0)
		{
			rowsOnTop = 0;
		}
		topOffset = Math.max(0,Math.floor(this.__currentIndex - rowsOnTop));
       	var start = Math.ceil(this.__currentIndex) - Math.ceil(rowsOnTop);
		if(start != this.__startArrayElement)
		{
			var end = start + this.__availableRows;
			if(start < 0)
			{
				start = 0;
			}
			
			if(end > this.__dataProvider.length)
			{
				end = this.__dataProvider.length;
			}
			var visibleData = this.__dataProvider.slice(start, end);
			var domElement = null;
			var dataItem = null;
			for(var count = 0; count < visibleData.length; count++) 
			{
				if(this.__listContainer.children[count].data != visibleData[count]) 
				{
					dataItem = visibleData[count];
					domElement = this.__listContainer.children[count];
					domElement.index = start + count;
					this.__setRendererInData(domElement,dataItem);
					//IE bug
					domElement.data = dataItem;
					if(this.util.isFunction(this.__setDataCallBack))
		            {
						var list = this;
		            	if(this.util.isString(this.__setDataCallBack))
		            	{
		            		this.util.callFunctionFromString(this.__setDataCallBack + "(domElement,dataItem,labelField)",function(paramValue){
		        				if(paramValue === "domElement")
		        				{
		        					return domElement;
		        				}
		        				if(paramValue === "dataItem")
		        				{
		        					return dataItem;
		        				}
		        				if(paramValue === "labelField")
		        				{
		        					return list.__labelField;
		        				}
		        				return paramValue;
		        			});
		            	}
		            	else
		            	{
		            		this.__setDataCallBack(domElement,dataItem,this.__labelField);
		            	}
		            }
				}
			}
			if(this.__listContainer.children.length > visibleData.length)
			{
				var childCount = this.__listContainer.children.length;
				var visibleCount = visibleData.length;
				for(var count = childCount - 1;count > visibleCount - 1;count--)
				{
					domElement = this.__listContainer.children[count];
					domElement.index = -1;
					if(this.util.isFunction(this.__clearDataCallBack))
		            {
		            	if(this.util.isString(this.__clearDataCallBack))
		            	{
		            		this.util.callFunctionFromString(this.__clearDataCallBack + "(domElement)",function(paramValue){
		        				if(paramValue === "domElement")
		        				{
		        					return domElement;
		        				}
		        				return paramValue;
		        			});
		            	}
		            	else
		            	{
		            		this.__clearDataCallBack(domElement);
		            	}
		            }
				}
			}
			this.__startArrayElement = start;
		}
		var listTop = this.__scrollOffset - ((this.__currentIndex - topOffset) * this.__listItemHeight);
		this.__listContainer.style.top = Math.max(0,listTop) + "px";
		
	}
};

nsList.__setPosition= function(posX,posY)
{
	this.__positionX = posX;
	this.__positionY = posY;
};

nsList.__setChange= function(changeX,changeY)
{
	this.__changeX = this.__positionX - changeX;
	this.__changeY = this.__positionY - changeY;
};

nsList.__scrollHandler = function(event)
{
	if(this.__outerContainer.scrollTop == this.__scrollOffset) 
	{
		return;
	}
	if(!this.__dataProvider || !this.__dataProvider.length) 
	{
		return;
	}
	this.__scrollOffset = Math.max(0,this.__outerContainer.scrollTop);
	this.__scrollOffset = Math.min(this.__scrollOffset, this.__scrollHeight);
	
	
	this.__setChange(this.__outerContainer.scrollLeft, this.__scrollOffset);
	this.__renderList(this.__currentIndex - this.__changeY / this.__listItemHeight, false);
	this.__setPosition(this.__outerContainer.scrollLeft, this.__scrollOffset);
};

nsList.__bindRenderers = function() 
{
	var listItem = null;
	for(var count = 0; count < this.__listContainer.children.length; count++) 
	{
		listItem = this.__listContainer.children[count];
		this.util.removeAllChildren(listItem);
		listItem.appendChild(this.__itemRenderer.cloneNode(true));
		this.__setRendererProperties(listItem);
	}
};

nsList.__setRendererProperties = function(listItem)
{
	if(listItem)
	{
		var compChild = null;
		for(var count = 0; count < listItem.children.length; count++) 
		{
			compChild = listItem.children[count];
			var list = this;
			Array.prototype.slice.call(compChild.attributes).forEach(function(attribute) 
			{
		        if(list.util.isFunction(attribute.value))
		        {
		        	var newValue = attribute.value + "(this)";
		        	compChild.removeAttribute(attribute.name);
					compChild.setAttribute(attribute.name,newValue);
		        }
			});
			if(compChild)
			{
				if(compChild.hasAttribute("accessor-name"))
				{
					listItem[compChild.getAttribute("accessor-name")] = compChild;
				}
			}
			this.__setRendererProperties(compChild);
		}
	}
};

nsList.__setRendererInData = function(listItem,item)
{
	if(listItem)
	{
		var compChild = null;
		for(var count = 0; count < listItem.children.length; count++) 
		{
			compChild = listItem.children[count];
			if(compChild)
			{
				compChild.data = item;
			}
			//IE 9 Bug,you got to assign it back
			//listItem.children[count] = compChild;
			this.__setRendererInData(compChild,item);
		}
	}
};

nsList.propertyChange = function(attrName, oldVal, newVal, setProperty) 
{
	this.base.propertyChange(attrName, oldVal, newVal, setProperty);
};


document.registerElement("ns-list", {prototype: nsList});
