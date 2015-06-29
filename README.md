//menu.js

function Menu(parentID,dataSource,menuClickHandler,labelField,isFirstMenuSelected) 
{
	this.DEFAULT_LABEL_FIELD = "label";
	this.__parentID = parentID;
	this.__dataSource = dataSource;
	this.__menuClickHandler = menuClickHandler;
	this.__labelField = labelField;
	this.__isFirstMenuSelected = true;
	if(isFirstMenuSelected  !== undefined)
	{
		this.__isFirstMenuSelected = isFirstMenuSelected;
	}
	this.__parent = null;
	this.__menuListContainer = null;
	this.__objSource = [];
	this.__menuParentClass = "nomuraMenu";
	this.__selectedMenuClass = "active";
	this.__hoverMenuClass = "hover";
	
	this.initialize();
}

Menu.prototype.initialize = function () 
{
	 this.__parent = getElement(this.__parentID);
	 if(this.__parent)
	 {
		 addStyleClass(this.__parent,this.__menuParentClass);
		 this.__menuListContainer = document.createElement("ul");
		 this.__parent.appendChild(this.__menuListContainer);
		 if(!this.__labelField)
		 {
			 this.__labelField = this.DEFAULT_LABEL_FIELD;
		 }
		 this.createMenus();
	 }
	 else
	 {
		 throw "Parent for Menu is not defined or does not exist";
	 }
	
};

Menu.prototype.createMenus = function () 
{
	if(this.__dataSource)
	{
		var menuLength = this.__dataSource.length;
		for(var count = 0 ; count < menuLength ; count++)
		{
			var item = this.__dataSource[count];
			if(item)
			{
				var menuAnchor = document.createElement("a");
				menuAnchor.setAttribute("href","#");
				menuAnchor.innerHTML = item[this.__labelField];
				
	            var menuItem = document.createElement("li");
	            var id = this.getMenuID(count);
	            menuItem.setAttribute("id",id);
	            this.__objSource[id] = item;
	            menuItem.onclick = this.selectItem.bind(this);
	            menuItem.onmouseover = this.menuItemOverHandler.bind(this);
	            menuItem.onmouseout = this.menuItemOutHandler.bind(this);
	            menuItem.appendChild(menuAnchor);
	            this.__menuListContainer.appendChild(menuItem);
			}
		}
		if(this.__isFirstMenuSelected)
		{
			var firstMenuID = this.getMenuID(0);
			this.selectMenu(firstMenuID,true);
		}
	}
};

Menu.prototype.selectItem = function (event) 
{
	var menu = getTarget(event);
	menu = getParentByType(menu,"LI");
	if(menu)
	{
		this.deSelectAllMenus();
		this.selectMenu(menu.id,true);
	}
};

Menu.prototype.menuItemOverHandler = function (event,count) 
{
	var menu = null;
	if(count  === undefined || count < 0)
	{
		menu = getTarget(event);
		menu = getParentByType(menu,"LI");
	}
	else
	{
		var menuId = this.getMenuID(count);
		menu = getElement(menuId);
	}
	if(menu)
	{
		addStyleClass(menu,this.__hoverMenuClass);
	}
};

Menu.prototype.menuItemOutHandler = function (event,count) 
{
	var menu = null;
	if(count  === undefined || count < 0)
	{
		menu = getTarget(event);
		menu = getParentByType(menu,"LI");
	}
	else
	{
		var menuId = this.getMenuID(count);
		menu = getElement(menuId);
	}
	if(menu)
	{
		removeStyleClass(menu,this.__hoverMenuClass);
	}
};

Menu.prototype.selectMenu = function (menuID,callClickHandler) 
{
	var menu = getElement(menuID);
	if(menu)
	{
		addStyleClass(menu,this.__selectedMenuClass);
		var item =  this.__objSource[menuID];
		if(this.__menuClickHandler && callClickHandler)
		{
			this.__menuClickHandler(event,item);
		}
	}
};

Menu.prototype.deSelectAllMenus = function () 
{
	var arrMenus =  this.__menuListContainer.getElementsByTagName("li");
	var menuLength = arrMenus.length;
	for(var count =  0 ; count < menuLength ; count++)
	{
		var menu = arrMenus[count];
		removeStyleClass(menu,this.__selectedMenuClass);
	}
};

Menu.prototype.setSelectedMenu = function (count) 
{
	var menuID = this.getMenuID(count);
	var item = this.__objSource[menuID];
	if(item)
	{
		this.deSelectAllMenus();
		this.selectMenu(menuID,false);
	}
};

Menu.prototype.getMenuID = function (count)
{
	var id = this.__parentID + "#menu" + count;
	return id;
};


function getElement(elementId)
{
	if(elementId && elementId.length > 0)
	{
		return document.getElementById(elementId);
	}
	return null;
};

function addStyleClass(divAlert,styleClass)
{
    if(divAlert && styleClass && styleClass.length > 0)
    {
        if(document.body.classList)
        {
            if(!hasStyleClass(divAlert,styleClass))
            {
                divAlert.className += " " + styleClass;
            }
        }
        else
        {
            if(!hasStyleClass(divAlert,styleClass))
            {
                divAlert.classList.add(styleClass);
            }
        }
    }
}

function hasStyleClass(divAlert,styleClass)
{
    if(divAlert && styleClass && styleClass.length > 0)
    {
    	try
    	{
    		if(document.body.classList)
            {
                return (divAlert.className.indexOf(" " + styleClass) > -1);
            }
            else if(divAlert.classList.contains)
            {
                return divAlert.classList.contains(styleClass);
            }
    	}
    	catch(error)
    	{
    		
    	}
        
    }
    return false;
}

function removeStyleClass(divAlert,styleClass)
{
    if(divAlert && styleClass && styleClass.length > 0)
    {
        if(document.body.classList)
        {
            if(divAlert.className)
            {
                divAlert.className = divAlert.className.replace(" " + styleClass,"");
            }
        }
        else
        {
            divAlert.classList.remove(styleClass);
        }
    }
}

function getEvent(event)
{
	if (!event)
	{
		event = window.event;
	}
	return event;
}

function getTarget(event)
{
	event = getEvent(event);
	var target = event.target ? event.target : event.srcElement;
	
	return target;
}

function getParentByType(element,type)
{
	if(element && type)
	{
		var parent = element;
	    while(parent && parent.nodeName != type.toUpperCase())
	    {
	       parent = parent.parentNode;
	    }
	    return parent;
	}
	return element;
}

---------------------------------------------------------------------------------------------------------

//app.js

var menuSource = [
      {label:"Home",source:"home"},
      {label:"Biologist", source:"https://en.wikipedia.org/wiki/Ask_a_Biologist"},
      {label:"Action Bioscience", source:"https://en.wikipedia.org/wiki/Actionbioscience"},
      {label:"FishBase", source:"https://en.wikipedia.org/wiki/FishBase"},
      {label:"NeuroLex", source:"https://en.wikipedia.org/wiki/NeuroLex"},
      {label:"Proteopedia", source:"https://en.wikipedia.org/wiki/Proteopedia"},
      {label:"Transterm", source:"https://en.wikipedia.org/wiki/Transterm"}
 ];

var biologistSubNav = [
                 {menuName:"Biologist", source:"https://en.wikipedia.org/wiki/Ask_a_Biologist",beforeSeparator:false},
                 {menuName:"Action Bioscience", source:"https://en.wikipedia.org/wiki/Actionbioscience",beforeSeparator:false},
                 {menuName:"FishBase", source:"https://en.wikipedia.org/wiki/FishBase",beforeSeparator:false},
                 {menuName:"NeuroLex", source:"https://en.wikipedia.org/wiki/NeuroLex",beforeSeparator:false},
                 {menuName:"Transterm", source:"https://en.wikipedia.org/wiki/Proteopedia",beforeSeparator:false}
            ];

var actionbioscienceSubNav = [
                 {menuName:"Biologist", source:"https://en.wikipedia.org/wiki/Ask_a_Biologist",beforeSeparator:false},
                 {menuName:"Action Bioscience", source:"https://en.wikipedia.org/wiki/Actionbioscience",beforeSeparator:false},
                 {menuName:"FishBase", source:"https://en.wikipedia.org/wiki/FishBase",beforeSeparator:false},
                 {menuName:"NeuroLex", source:"https://en.wikipedia.org/wiki/NeuroLex",beforeSeparator:false},
                 {menuName:"Transterm", source:"https://en.wikipedia.org/wiki/Proteopedia",beforeSeparator:false}
            ];

var fishbaseSubNav = [
                {menuName:"Biologist", source:"https://en.wikipedia.org/wiki/Ask_a_Biologist",beforeSeparator:false},
                 {menuName:"Action Bioscience", source:"https://en.wikipedia.org/wiki/Actionbioscience",beforeSeparator:false},
                 {menuName:"FishBase", source:"https://en.wikipedia.org/wiki/FishBase",beforeSeparator:false},
                 {menuName:"NeuroLex", source:"https://en.wikipedia.org/wiki/NeuroLex",beforeSeparator:false},
                 {menuName:"Transterm", source:"https://en.wikipedia.org/wiki/Proteopedia",beforeSeparator:false}
            ];

var neurolexSubNav = [
                {menuName:"Biologist", source:"https://en.wikipedia.org/wiki/Ask_a_Biologist",beforeSeparator:false},
                 {menuName:"Action Bioscience", source:"https://en.wikipedia.org/wiki/Actionbioscience",beforeSeparator:false},
                 {menuName:"FishBase", source:"https://en.wikipedia.org/wiki/FishBase",beforeSeparator:false},
                 {menuName:"NeuroLex", source:"https://en.wikipedia.org/wiki/NeuroLex",beforeSeparator:false},
                 {menuName:"Transterm", source:"https://en.wikipedia.org/wiki/Proteopedia",beforeSeparator:false}
            ];

var proteopediaSubNav = [
                {menuName:"Biologist", source:"https://en.wikipedia.org/wiki/Ask_a_Biologist",beforeSeparator:false},
                 {menuName:"Action Bioscience", source:"https://en.wikipedia.org/wiki/Actionbioscience",beforeSeparator:false},
                 {menuName:"FishBase", source:"https://en.wikipedia.org/wiki/FishBase",beforeSeparator:false},
                 {menuName:"NeuroLex", source:"https://en.wikipedia.org/wiki/NeuroLex",beforeSeparator:false},
                 {menuName:"Transterm", source:"https://en.wikipedia.org/wiki/Proteopedia",beforeSeparator:false}
            ];

var transtermSubNav = [
               {menuName:"Biologist", source:"https://en.wikipedia.org/wiki/Ask_a_Biologist",beforeSeparator:false},
                 {menuName:"Action Bioscience", source:"https://en.wikipedia.org/wiki/Actionbioscience",beforeSeparator:false},
                 {menuName:"FishBase", source:"https://en.wikipedia.org/wiki/FishBase",beforeSeparator:false},
                 {menuName:"NeuroLex", source:"https://en.wikipedia.org/wiki/NeuroLex",beforeSeparator:false},
                 {menuName:"Transterm", source:"https://en.wikipedia.org/wiki/Proteopedia",beforeSeparator:false}
            ];



var biologistBody = "";
var actionbioscienceBody = "";
var fishbaseBody = "";
var neurolexBody = "";
var proteopediaBody = "";
var transtermBody = "";


var objMenus = null;
var objDashBoard = null;

function initializeCompliancePortal()
{
	biologistBody = "#templateBio";
	actionbioscienceBody = "#templateBio";
	fishbaseBody = "#templateBio";
	neurolexBody = "#templateBio";
	proteopediaBody = "#templateBio";
	transtermBody = "#templateBio";
	
	var dashBoardSource = [
	                       {title:"Biologist",subNav:biologistSubNav, templateID:biologistBody, menuCount:1, source:"https://en.wikipedia.org/wiki/Ask_a_Biologist"},
	                       {title:"Action Bioscience",subNav:actionbioscienceSubNav, templateID:actionbioscienceBody, menuCount:2, source:"https://en.wikipedia.org/wiki/Actionbioscience"},
	                       {title:"FishBase",subNav:fishbaseSubNav, templateID:fishbaseBody, menuCount:3, source:"https://en.wikipedia.org/wiki/FishBase"},
	                       {title:"NeuroLex",subNav:neurolexSubNav, templateID:neurolexBody, menuCount:4, source:"https://en.wikipedia.org/wiki/NeuroLex"},
	                       {title:"Proteopedia",subNav:proteopediaSubNav, templateID:proteopediaBody, menuCount:5, source:"https://en.wikipedia.org/wiki/Proteopedia"},
	                       {title:"Transterm",subNav:transtermSubNav, templateID:transtermBody, menuCount:6, source:"https://en.wikipedia.org/wiki/Transterm"}
	                  ];
	
	objMenus = new Menu("divMenus",menuSource,loadApplications);
	objDashBoard = new DashBoard("divPortlets",dashBoardSource);
	objDashBoard.portletMouseOverHandler = portletMouseOver;
	objDashBoard.portletMouseOutHandler = portletMouseOut;
	objDashBoard.navigationHandler = loadApplications;
	
}

function loadApplications(event,menuItem)
{
	if(menuItem)
	{
		var divPortlets = getElement("divPortlets");
		divPortlets.style.display = "none";
		var divContent = getElement("divContent");
		divContent.style.display = "block";
		if(menuItem.source == "home")
		{
			divPortlets.style.display = "block";
			divContent.style.display = "none";
		}
		else
		{
			getElement("compFrame").setAttribute("src", menuItem.source);
		}
		if(menuItem["menuCount"])
		{
			objMenus.setSelectedMenu(menuItem["menuCount"]);
		}
	}
}

function portletMouseOver(event,menuCount)
{
	console.log("In portletMouseOver");
	if(menuCount > -1)
	{
		objMenus.menuItemOverHandler(null,menuCount);
	}
}

function portletMouseOut(event,menuCount)
{
	console.log("In portletMouseOut");
	if(menuCount > -1)
	{
		objMenus.menuItemOutHandler(null,menuCount);
	}
}

window.onload = initializeCompliancePortal;
