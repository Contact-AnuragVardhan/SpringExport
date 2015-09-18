function NSUtil()
{
	this.__sheetId = "nsStyle";
	this.KEYCODE = {TAB:9, ENTER:13, SHIFT:16, CTRL:17, ESC:27, LEFT:37, UP:38, RIGHT:39, DOWN:40};
};
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
var nsContainerBase = Object.create(HTMLDivElement.prototype);

nsContainerBase.INITIALIZE = "initialize";
nsContainerBase.CREATION_COMPLETE = "creationComplete";
nsContainerBase.PROPERTY_CHANGE = "propertyChange";
nsContainerBase.REMOVE = "remove";

/*start of private variables */
nsContainerBase.base = null;
nsContainerBase.__setProperty = true; 
nsContainerBase.__id = null;
nsContainerBase.__shadow = null;
nsContainerBase.__focused = false;
/*end of private variables */

/*start of functions */
nsContainerBase.__setBase = function() 
{
	if(this.__proto__ && this.__proto__.__proto__)
	{
		this.base = this.__proto__.__proto__;
	}
};

nsContainerBase.__setID = function()
{
	if(this.hasAttribute("id"))
	{
		this.__id = this.getAttribute("id");
	}
	else if(this.hasAttribute("name"))
	{
		this.__id = this.getAttribute("name");
	}
	else
	{
		this.__id = "comp" + this.util.getUniqueId();
	}
};

nsContainerBase.createdCallback = function() 
{
	console.log("In Parent createdCallback");
	this.util = new NSUtil();
	this.__setBase();
	this.__setID();
	this.initializeComponent();
	this.util.addEvent(this,"focus",this.focusHandler.reference(this));
	this.util.addEvent(this,"blur",this.blurHandler.reference(this));
	this.dispatchCustomEvent(this.INITIALIZE);
};

nsContainerBase.attachedCallback = function()
{
	console.log("In attachedCallback");
	this.setComponentProperties();
	this.dispatchCustomEvent(this.CREATION_COMPLETE);
};

nsContainerBase.attributeChangedCallback = function(attrName, oldVal, newVal)
{
	console.log("attrName::" + attrName + " oldVal::" + oldVal + " newVal::" + newVal);
	var data = {};
	data.propertyName = attrName;
	data.oldValue = oldVal;
	data.newValue = newVal;
	this.dispatchCustomEvent(this.PROPERTY_CHANGE,data);
	this.propertyChange(attrName, oldVal, newVal, this.__setProperty);
	this.__setProperty = true;
};

nsContainerBase.detachedCallback = function()
{
	console.log("In detachedCallback");
	this.dispatchCustomEvent(this.REMOVE);
};

nsContainerBase.initializeComponent = function() 
{
	console.log("In Parent initializeComponent");
};

nsContainerBase.setComponentProperties = function() 
{
	console.log("In Parent setComponentProperties");
};

nsContainerBase.propertyChange = function(attrName, oldVal, newVal, setProperty) 
{
	console.log("In Parent setComponentProperties");
};

nsContainerBase.initializeDOM = function(requireStyleClass)
{
	if(document.head.createShadowRoot) 
	{
	    if(!this.__shadow)
	    {
	    	this.__shadow = this.createShadowRoot();
	    }
	    if(requireStyleClass)
	    {
	    	var shadow = this.__shadow;
	    	new this.util.ajax(ns.__dicPath["component.css"], function (response) {
	    		if(response)
	    		{
	    			var sheet = document.createElement("style");
			    	sheet.innerHTML = response;
			    	shadow.appendChild(sheet);
	    		}
		    });
	    	new this.util.ajax(requireStyleClass, function (response) {
	    		if(response)
	    		{
	    			var sheet = document.createElement("style");
			    	sheet.innerHTML = response;
			    	shadow.appendChild(sheet);
	    		}
		    });
	    	
	    }
	}
};

nsContainerBase.getID = function() 
{
	return this.__id;
};

nsContainerBase.focusHandler = function(event)
{
	this.__focused = true;
};

nsContainerBase.blurHandler = function(event)
{
	this.__focused = false;
};

nsContainerBase.addChild = function(element)
{
	if(element)
	{
	    if(this.__shadow)
	    {
	    	 this.__shadow.appendChild(element);
	    }
		else 
		{
		    this.appendChild(element);
		}
	}
};

nsContainerBase.deleteChild = function(element)
{
	if(element)
	{
	    if(this.__shadow)
	    {
	    	 this.__shadow.removeChild(element);
	    }
		else 
		{
		    this.removeChild(element);
		}
	}
};

nsContainerBase.getElement = function(elementId)
{
	if(this.__shadow) 
	{
		if(elementId && elementId.length > 0)
		{
			return this.__shadow.getElementById(elementId);
		}
	} 
    return this.util.getElement(elementId);
};

nsContainerBase.hasFocus = function()
{
    return this.__focused;
};

nsContainerBase.dispatchCustomEvent = function(eventType,data,bubbles,cancelable) 
{
	console.log("In Parent dispatchCustomEvent");
	if(this.util.isUndefined(data))
	{
		data = null;
	}
	if(typeof bubbles == "undefined")
	{
		bubbles = true;
	}
	if(typeof cancelable == "undefined")
	{
		cancelable = true;
	}
	var event = new CustomEvent(eventType, 
	{
		detail: data,
		bubbles: bubbles,
		cancelable: cancelable
	});
	if (this.hasAttribute(eventType)) 
	{
		var attributeValue = this.getAttribute(eventType);
		if(attributeValue)
		{
			this.util.callFunctionFromString(attributeValue,function(paramValue){
				if(paramValue === 'true' || paramValue === 'false')
				{
					return Boolean.parse(paramValue);
				}
				else if(paramValue === 'this')
				{
					return this;
				}
				else if(paramValue === 'event')
				{
					return event;
				}
				return paramValue;
			});
		}
	}
	this.dispatchEvent(event);
};
/*end of functions */

document.registerElement('ns-containerbase', {prototype: nsContainerBase});
