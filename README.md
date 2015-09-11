function NSUtil()
{
	this.__sheetId = "nsStyle";
};

NSUtil.prototype.getElement = function (elementId)
{
	if(elementId && elementId.length > 0)
	{
		return document.getElementById(elementId);
	}
	return null;
};

NSUtil.prototype.createDiv = function (id,styleName)
{
    var div = document.createElement("div");
    if(id)
    {
    	div.setAttribute("id",id);
    }
    if(styleName)
    {
        div.className = styleName;   
    }
    return div;
};

NSUtil.prototype.setDivVisibility = function (id,isVisible)
{
    var div = document.getElementById(id);
    if(div)
    {
        if(isVisible)
        {
            div.style.display = "block";
        }
        else
        {
            div.style.display = "none";
        }
    }
};

NSUtil.prototype.removeDiv = function (id)
{
    var div = document.getElementById(id);
    if (div)
    {
        var parent = div.parentNode;
        if(parent)
        {
            parent.removeChild(div);
        }
    }
};

/*Utility Methods */
NSUtil.prototype.getKeys = function (object) 
{
    var keys = [];
    for (var property in object)
    {
    	keys.push(property);
    }
    return keys;
};

NSUtil.prototype.getValues = function (object)
{
	var values = [];
	for (var property in object)
	{
		values.push(object[property]);
	}
	return values;
};
   
NSUtil.prototype.clone = function (object) 
{
    return Object.extend({ }, object);
}
;
NSUtil.prototype.isElement = function (refElement)
{
    return !!(refElement && refElement.nodeType == 1);
};

NSUtil.prototype.isArray = function (refArr) 
{
    return refArr != null && typeof refArr == "object" &&
      "splice" in refArr && "join" in refArr;
};

NSUtil.prototype.isVariable = function (variableName) 
{
	return !this.isUndefined(window[variableName]); 
};

NSUtil.prototype.callFunction = function (functionName) 
{
	var returnValue = null;
	if (typeof window[functionName] === "function") 
	{
		returnValue = window[functionName]();
	}
	return returnValue;
};

NSUtil.prototype.callFunctionFromString = function (functionString,replaceParameterFunction)
{
	var returnValue = null;
	if(functionString)
	{
		var functionName = null;
		var arrParameter = null;
		if(functionString.indexOf("(") > -1)
		{
			var param = functionString.split("(");
			if(param && param.length > 0)
			{
				functionName = param[0];
				var parameter = param[1];
				if(parameter && parameter.charAt(parameter.length - 1) === ")")
				{
					parameter = parameter.substring(0,parameter.length - 1);
					var arrParam = parameter.split(",");
					if(arrParam && arrParam.length > 0)
					{
						arrParameter = arrParam;
					}
				}
			}
		}
		else
		{
			functionName = functionString;
		}
		if(this.isFunction(functionName))
		{
			if(!arrParameter || arrParameter.length === 0)
			{
				returnValue = this.callFunction(functionName);
			}
			else
			{
				if(replaceParameterFunction)
				{
					for(var count = 0;count < arrParameter.length;count++)
					{
						 var newValue = replaceParameterFunction(arrParameter[count]);
						 if(!this.isUndefined(newValue))
						 {
							 arrParameter[count] = newValue;
						 }
					}	
				}
				switch (arrParameter.length) 
				{
				    case 1:
				    	returnValue = window[functionName](arrParameter[0]);
				        break;
				    case 2:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1]);
				        break;
				    case 3:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2]);
				        break;
				    case 4:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2],arrParameter[3]);
				        break;
				    case 5:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2],arrParameter[3],arrParameter[4]);
				        break;
				    case 6:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2],arrParameter[3],arrParameter[4],arrParameter[5]);
				        break;
				    case 7:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2],arrParameter[3],arrParameter[4],arrParameter[5],arrParameter[6]);
				        break;
				    case 8:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2],arrParameter[3],arrParameter[4],arrParameter[5],arrParameter[6],arrParameter[7]);
				        break;
				    case 9:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2],arrParameter[3],arrParameter[4],arrParameter[5],arrParameter[6],arrParameter[7],arrParameter[8]);
				        break;
				    case 10:
				    	returnValue = window[functionName](arrParameter[0],arrParameter[1],arrParameter[2],arrParameter[3],arrParameter[4],arrParameter[5],arrParameter[6],arrParameter[7],arrParameter[8],arrParameter[9]);
				        break;
				}
			}
		}
	}
	return returnValue;
};

NSUtil.prototype.isFunction = function (refFunction) 
{
    if(refFunction && ((typeof refFunction == "function") || (typeof window[refFunction] === "function")))
    {
    	return true;
    }
    return false;
};

NSUtil.prototype.isString = function (object) 
{
    return typeof object == "string";
};

NSUtil.prototype.isNumber = function (object) 
{
    return typeof object == "number";
};
  
NSUtil.prototype.isUndefined = function (object) 
{
    return typeof object == "undefined";
};

/*
 * 1001 - function validation failure
 */
NSUtil.prototype.throwException = function (exceptionCode,exceptionName,exceptionDetails)
{
    //alert("Error Occured with details code: " + exceptionCode + ",name: " + exceptionName + ",details: " + exceptionDetails);
    throw{code: exceptionCode,name: exceptionName,details: exceptionDetails};
};

//other values of navigator.appName is "Netscape","Opera"
//From IE 11 navigator.appName returns "Netscape" instead of "Microsoft Internet Explorer"
//and From IE 11 it follows standard API.
NSUtil.prototype.isBrowserIE = function ()
{
    if(navigator && navigator.appName && navigator.appName == "Microsoft Internet Explorer")
    {
        return true;
    }
    return false;
};

//navigates to the DOM Heirarchy to find the nearest parent of the specified HTML tag 
NSUtil.prototype.findParent = function (element,parentTag)
{
    if(element && parentTag)
    {
        while (element && element.tagName && element.tagName.toLowerCase()!=parentTag.toLowerCase())
        {
            element = element.parentNode;
        }
    }  
    return element;
};
//returns the top y coordinate of the given div used while listening to scrolling events(vertical scrolling)
NSUtil.prototype.scrollTop = function (div)
{
	 if(div)
	 {
	    return (div.scrollHeight - div.clientHeight);
	 }
};

//returns the top x coordinate of the given div used while listening to scrolling events(horizontal scrolling)
NSUtil.prototype.scrollLeft = function (div)
{
	 if(div)
	 {
		 return (div.scrollWidth - div.clientWidth);
	 }
};

//adds Style class to an element's styles list 
NSUtil.prototype.addStyleClass = function (divAlert,styleClass)
{
    if(divAlert && styleClass && styleClass.length > 0)
    {
        if(document.body.classList)
        {
            if(!this.hasStyleClass(divAlert,styleClass))
            {
                divAlert.className += " " + styleClass;
            }
        }
        else
        {
            if(!this.hasStyleClass(divAlert,styleClass))
            {
                divAlert.classList.add(styleClass);
            }
        }
    }
};

//returns true if a Style class is present in element's styles list
NSUtil.prototype.hasStyleClass = function (divAlert,styleClass)
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
};

//removes Style class from an element's styles list
NSUtil.prototype.removeStyleClass = function (divAlert,styleClass)
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
};

//change Style class from an element's styles list
NSUtil.prototype.changeStyleClass = function (divAlert,oldStyleClass,newStyleClass)
{
    if(divAlert && oldStyleClass && oldStyleClass.length > 0 && newStyleClass && newStyleClass.length > 0)
    {
        if(divAlert.classList)
        {
            if(divAlert.className)
            {
                divAlert.className = divAlert.className.replace(oldStyleClass,newStyleClass);
            }
        }
        else
        {
            divAlert.classList.remove(oldStyleClass);
            if(!divAlert.classList.contains(newStyleClass))
            {
                divAlert.classList.add(newStyleClass);
            }
        }
    }
};

NSUtil.prototype.addEvent = function(element, eventType, listener)
{
	 var retValue = false;
	 if (element.addEventListener)
	 {
		 element.addEventListener(eventType, listener);
		 retValue = true;
	 } 
	 else if (element.attachEvent)
	 {
	    retValue = element.attachEvent("on" + eventType, listener);
	 } 
	 else 
	 {
	    retValue = false;
	 }
};

NSUtil.prototype.removeEvent = function(element, eventType, listener)
{
	 var retValue = false;
	 var bindedFunction = listener;
	 if(listener && listener.bindedFunction)
	 {
		 bindedFunction = listener.bindedFunction;
		 listener.bindedFunction = null;
	 }
	 if (element.removeEventListener)
	 {
		 element.removeEventListener(eventType, bindedFunction);
		 retValue = true;
	 } 
	 else if (element.detachEvent)
	 {
	    retValue = element.detachEvent("on" + eventType, bindedFunction);
	 } 
	 else 
	 {
	    retValue = false;
	 }
	 
	 return retValue;
};

NSUtil.prototype.dispatchEvent = function(element, eventType, data)
{
	 var event = null;
	 if (document.createEvent) 
	 {
	    event = document.createEvent("HTMLEvents",{ "detail": data});
	    event.initEvent(eventType, true, true);
	 } 
	 else 
	 {
	    event = document.createEventObject();
	    event.eventType = eventType;
	 }
	 event.eventName = eventType;
	 event.detail = data;
	 if (document.createEvent) 
	 {
	    element.dispatchEvent(event);
	 } 
	 else 
	 {
	    element.fireEvent("on" + event.eventType, event);
	 }
};

//returns event for all kind of browser
NSUtil.prototype.getEvent = function (event)
{
	// IE is evil and doesn't pass the event object
	if (!event)
	{
		event = window.event;
	}
	return event;
};

//returns target for all kind of browser
NSUtil.prototype.getTarget = function (event)
{
	// we assume we have a standards compliant browser, but check if we have IE
	event = this.getEvent(event);
	var target = event.target ? event.target : event.srcElement;
	return target;
};


//copied from http://www.broofa.com/Tools/Math.uuid.js
NSUtil.prototype.getUniqueId = function () 
{
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");
    var uuid = new Array(36);
    var rnd=0;
    var r;
    
    for (var count = 0; count < 36; count++) 
    {
      if (count==8 || count==13 ||  count==18 || count==23) 
      {
        uuid[count] = '-';
      } 
      else if (count==14) 
      {
        uuid[count] = '4';
      } 
      else 
      {
        if (rnd <= 0x02) 
        {
        	rnd = 0x2000000 + (Math.random()*0x1000000) | 0;
        }
        r = rnd & 0xf;
        rnd = rnd >> 4;
        uuid[count] = chars[(count == 19) ? (r & 0x3) | 0x8 : r];
      }
    }
    return uuid.join('');
 };
 
 NSUtil.prototype.addShadowStyle = function (control,className,property,cssClassName,checkClassCallBack)
 {
 	var styleProperties = this.getCSSClass(cssClassName);
 	if(checkClassCallBack)
 	{
 		styleProperties = checkClassCallBack(styleProperties);
 	}
 	var _head = document.head || document.getElementsByTagName('head')[0];
 	var _sheet = document.getElementById(this.__sheetId) || document.createElement('style');
 	_sheet.id = this.__sheetId;
 	control.className +=  " " + className; 
 	_sheet.innerHTML += "\n." + className + ":" + property + "{" +styleProperties + "}";
 	_head.appendChild(_sheet);
 };

 NSUtil.prototype.getCSSClass = function (cssClassName)
 {
     if(!document.styleSheets) 
 	{
 		return "";
 	}
 	var regEx = null;
 	var styleSheets = document.styleSheets;
 	var counter = styleSheets.length;
 	var result = [];
     if(typeof cssClassName == "string") 
 	{
 		regEx = RegExp('\\b' + cssClassName + '\\b','i');
 	}
     while(counter)
 	{
     	var currentSheet = styleSheets[--counter];
     	var cssRules = (currentSheet.rules) ? currentSheet.rules: currentSheet.cssRules;
 		var cssRulesLength = cssRules.length;
     	for(var count = 0 ; count < cssRulesLength; count++)
 		{
     		var tempClassDetails = cssRules[count].selectorText ? [cssRules[count].selectorText, cssRules[count].style.cssText]: [cssRules[count]+''];
     		if(regEx.test(tempClassDetails[0])) 
 			{
 				result[result.length]= tempClassDetails;
 			}
     	}
     }
 	if(result && result.length === 1 && result[0] && result[0].length > 1)
 	{
 		return result[0][1];
 	}
     return result.join('\n\n');
 };
 
 NSUtil.prototype.throwNSError = function(componentName,message)
 {
 	throw new Error("Error in " + componentName + " with details::" + message);
 };
 
 //gets the offset of the element 
 NSUtil.prototype.getOffSet = function(element, offset) 
 {
	if(element)
    {
		offset.left += element.offsetLeft;
	    offset.top += element.offsetTop;
	    this.getOffSet(element.offsetParent, offset);
    }
 };
 
 NSUtil.prototype.addTemplateInContainer = function(container,templateID) 
 {
	 if(container && templateID)
	 {
		 var templateClone = this.getTemplate(templateID);
		 if(templateClone)
		 {
			 container.appendChild(templateClone);
		 }
	 }
 };
 
 NSUtil.prototype.getTemplate = function(templateID) 
 {
	 var templateClone = null;
	 if(templateID)
	 {
		 var template = document.getElementById(templateID);
		 if(template)
		 {
			 if(this.supportsTemplate())
			 {
				 templateClone = template.content.children[0]; //document.importNode(template.content, true); 
			 }
			 else
			 {
				 templateClone = template.cloneNode(true);
				 template.style.display = "none";
			 }
		 }
	 }
	 return templateClone;
 };
 
 NSUtil.prototype.supportsTemplate = function(element, offset) 
 {
	 return ("content" in document.createElement('template'));
 };
 
 NSUtil.prototype.removeAllChildren = function(element) 
 {
	if(element)
	{
		var node = element;
	    while (element.hasChildNodes()) 
	    {              

	        if (node.hasChildNodes()) 
	        {                
	            node = node.lastChild;                 
	        }
	        else 
	        {                                     
	            node = node.parentNode;                
	            node.removeChild(node.lastChild);      
	        }
	    }
	}
 };
 
 NSUtil.prototype.getScrollBarWidth = function(element)
 {
 	var scrollBarWidth = 0;
 	if(element)
 	{
 		scrollBarWidth = element.offsetWidth - element.clientWidth;
 	}
 	else
 	{
 		var divScroll = this.createDiv(null,"nsGetScrollBar");
        document.body.appendChild(divScroll);
        scrollBarWidth = divScroll.offsetWidth - divScroll.clientWidth;
        document.body.removeChild(divScroll);
 	}
 	
 	return scrollBarWidth;
 };
 
 NSUtil.prototype.getKeyUnicode = function(event) 
 {
	 event = this.getEvent(event);
     var unicode=event.keyCode ? event.keyCode : event.charCode;
     return unicode;
 };

 NSUtil.prototype.getDimensionAsNumber = function(element,dimension)
 {
 	var retValue = 0;
 	if(element && dimension)
 	{
 		if(dimension.substring(dimension.length - 1) == "%")
 		{
 			dimension = dimension.substring(0,dimension.length - 1);
 			retValue = (dimension / 100) * element.parent.offsetHeight;
 		}
 		else if(dimension.substring(dimension.length - 2) == "px")
 		{
 			retValue = dimension.substring(0,dimension.length - 2);
 		}
 		else
 		{
 			retValue = dimension;
 		}
 	}
 	if(isNaN(retValue))
 	{
 		retValue = 0;
 	}
 	return parseInt(retValue);
 };
 
 NSUtil.prototype.getCumulativeOffset = function(element)
 {
     var x = 0;
     var y = 0;
     var currentElement = (element) ? element : this;
     do
     {
         if (currentElement.nodeName.toLowerCase != 'td')
         {
             x += currentElement.offsetLeft;
             y += currentElement.offsetTop;
         }
     }
     while ((currentElement = currentElement.offsetParent) && currentElement.nodeName.toLowerCase() != 'body');   
  
     return { x: x, y: y };
 };
 
 NSUtil.prototype.makeElementUnselectable = function(element,makeChildrenUnselectable)
 {
	 if(element)
	 {
		 if (element.nodeType == 1) 
	     {
			 element.setAttribute("unselectable", "on");
	     }
		 if(makeChildrenUnselectable)
		 {
			 var childElement = element.firstChild;
		     while (childElement) 
		     {
		    	 this.makeElementUnselectable(childElement,makeChildrenUnselectable);
		         childElement = childElement.nextSibling;
		     }
		 }
	 }
 };

 var falseExpression = /^(?:f(?:alse)?|no?|0+)$/i;
 Boolean.parse = function(value) 
 { 
    return !falseExpression.test(value) && !!value;
 };

//Defining the indexOf function (before you call it!) if it doesnï¿½t exist ï¿½ taken from MDN (for Internet Explorer 8 and below)
if (!Array.prototype.indexOf)
{
   Array.prototype.indexOf = function (searchElement /*, fromIndex */ )
   {
     'use strict';
     if (this == null)
     {
       throw new TypeError();
     }
     var n, k, t = Object(this),
         len = t.length >>> 0;
     if (len === 0)
     {
       return -1;
     }
     n = 0;
     if (arguments.length > 1) 
     {
       n = Number(arguments[1]);
       if (n != n)
       { // shortcut for verifying if it's NaN
         n = 0;
       }
       else if (n != 0 && n != Infinity && n != -Infinity)
       {
         n = (n > 0 || -1) * Math.floor(Math.abs(n));
       }
     }
     if (n >= len)
     {
       return -1;
     }
     for (k = n >= 0 ? n : Math.max(len - Math.abs(n), 0); k < len; k++)
     {
       if (k in t && t[k] === searchElement)
       {
         return k;
       }
     }
     return -1;
   };
}

//Added to get over the limitation of removeEventListener not working when we bind the listener
if (!Function.prototype.reference) 
{
	  Function.prototype.reference = function(ref) 
	  {
		  this.bindedFunction = this.bind(ref);
	      return this.bindedFunction;
	  };
}

NSUtil.prototype.defaultRenderer = function()
{
	this.util = new NSUtil();
	this.divItemRenderer = null;
	
	this.getRenderer = function()
	{
		if(!this.divItemRenderer)
		{
			this.__createComponents();
		}
		return this.divItemRenderer;
	};
	
	this.setData = function(renderer,item,labelField)
	{
		if(renderer)
		{
			if(item && item[labelField])
			{
				renderer.rendererBody.rendererLabel.innerHTML = item[labelField];
				//renderer.rendererBody.rendererLabel.appendChild(document.createTextNode(item[labelField]));
			}
			else
			{
				this.clearData(renderer);
			}
		}
		
	};
	
	this.clearData = function(renderer)
	{
		if(renderer)
		{
			renderer.rendererBody.rendererLabel.innerHTML = "";
		}
	};
	
	this.__createComponents = function()
	{
		this.divItemRenderer = this.util.createDiv(null,"imageHolder"); 
		this.divItemRenderer.style.height = 20 + "px";
		this.divItemRenderer.style.padding = 4 + "px";
		this.divItemRenderer.setAttribute("accessor-name","rendererBody"); 
		var lblItemRenderer = document.createElement("LABEL");
		lblItemRenderer.setAttribute("accessor-name","rendererLabel");
		this.divItemRenderer.appendChild(lblItemRenderer);
	};
};

/** Expalination http://javascript.info/tutorial/animation of animation .. not the code **/
NSUtil.prototype.animation = function(element,arrOptions)
{
	this.__element = element;
	this.__arrOptions = arrOptions;
	this.__util = new NSUtil();
	
	this.DELAY = 10;
	
	this.__duration = 0;
	this.__currentOption = null;
	this.__currentStart = 0;
	this.__currentEnd = 0;
	
	this.animate = function()
	{
		this.__run();
	};
	
	this.__requestAnimationFrame = (function()
	{
		  return  window.requestAnimationFrame       ||
		          window.webkitRequestAnimationFrame ||
		          window.mozRequestAnimationFrame    ||
		          window.oRequestAnimationFrame      ||
		          function(callback)
		          {
		              window.setTimeout(callback,this.DELAY);
		          };
	})();
	
	this.__run = function() 
	{
	    var current = +new Date();
	    var remaining = this.__currentEnd - current;
	    if(remaining < this.DELAY) 
	    {
		      if(this.__currentOption) 
		      {
		    	  this.__step(1);  //1 = progress is at 100%
		    	  if(this.__currentOption.animationCompleteHandler)
		    	  {
		    		  this.__currentOption.animationCompleteHandler(this.__currentOption);
		    	  }
		      }
		      this.__currentOption = this.__arrOptions.shift();  //get the next item
		      if(this.__currentOption) 
		      {
		    	  if(this.__currentOption["style"])
		    	  {
		    		  this.__currentStart = this.__util.getDimensionAsNumber(this.__element,this.__element.style[this.__currentOption.style]);
		    	  }
		    	  else
		    	  {
		    		  this.__currentStart = this.__element[this.__currentOption.property];
		    	  }
		    	  this.__duration = this.__currentOption.time * 1000;
		    	  this.__currentEnd = current + this.__duration;
		    	  this.__step(0);  //0 = progress is at 0%
		      }
		      else 
		      {
		        return;
		      }
	    }
	    else 
	    {
		      var progress = remaining / this.__duration;
		      var delta = this.__delta(progress);
		      this.__step(delta);
	    }
	    this.__requestAnimationFrame.bind(window)(this.__run.bind(this));
	};
	
	this.__delta = function(progress)
	{
		return (1 - Math.pow(progress, 3));  //easing formula
	};
	
	this.__step = function(progress) 
	{
		var newValue = progress * (this.__currentOption.target - this.__currentStart) + this.__currentStart;
		if(this.__currentOption["style"])
  	  	{
			this.__element.style[this.__currentOption.style] = newValue + "px";
			//this.__element.style[this.__currentOption.style] = (progress * this.__currentOption.target) + "px";
  	  	}
		else
		{
			this.__element[this.__currentOption.property] = newValue;
		}
	};
	
};

NSUtil.prototype.loader = function(parentContainer)
{
	this.parentContainer = parentContainer;
	this.util = new NSUtil();
	this.divLoaderContainerParent = null;
	
	this.createComponents = function()
	{
		this.divLoaderContainerParent = this.util.createDiv("divLoaderContainerParent","loaderContainerParent"); 
		this.parentContainer.appendChild(this.divLoaderContainerParent);
		var divLoaderContainer = this.util.createDiv("divLoaderContainer","loaderContainer"); 
		this.divLoaderContainerParent.appendChild(divLoaderContainer);
		var divLoadingIcon = this.util.createDiv("divLoadingIcon","progress"); 
		divLoaderContainer.appendChild(divLoadingIcon);
		var divLoadingIconText = this.util.createDiv(null); 
		divLoadingIconText.appendChild(document.createTextNode("Loadingâ€¦"));
		divLoadingIcon.appendChild(divLoadingIconText);
		var compBreak = document.createElement("br");
		divLoaderContainer.appendChild(compBreak);
		compBreak = document.createElement("br");
		divLoaderContainer.appendChild(compBreak);
		var divLoadingLabel = this.util.createDiv("divLoadingLabel","loaderText"); 
		divLoadingLabel.appendChild(document.createTextNode("Loading"));
		divLoaderContainer.appendChild(divLoadingLabel);
		
		this.centerElement(this.divLoaderContainerParent);
		this.centerElement(divLoadingLabel);
		divLoadingLabel.style.top = (this.util.getDimensionAsNumber(divLoadingLabel.style.top) + 40) + "px";
	};
	
	this.centerElement = function(component)
	{
		if(component)
		{
			var parent = component.parentNode;
			component.style.left = ((parent.offsetWidth - component.offsetWidth) / 2) + "px";
			console.log("parent.id::" + parent.id + " component.id::" + component.id + " parent.offsetWidth::" + parent.offsetWidth + " component.offsetWidth::" + component.offsetWidth);
			component.style.top = ((parent.offsetHeight - component.offsetHeight) / 2) + "px";
		}
	};
	
	this.show = function()
	{
		if(!this.divLoaderContainerParent)
		{
			this.createComponents();
		}
		this.divLoaderContainerParent.style.display = "inline-block";
	};
	
	this.hide = function()
	{
		if(this.divLoaderContainerParent)
		{
			this.divLoaderContainerParent.style.display = "none";
		}
	};
	
};

NSUtil.prototype.ajax = function(url, callbackFunction)
{
	this.callbackFunction = callbackFunction;
	this.url = url;
	this.postBody = (arguments[2] || "");
	
	this.bindFunction = function (caller, object) 
	{
		return function() 
		{
			return caller.apply(object, [object]);
		};
	};

	this.stateChange = function (object) 
	{
		if (this.request.readyState == 4)
		{
			this.callbackFunction(this.request.responseText);
		}
	};

	this.getRequest = function() 
	{
		if (window.ActiveXObject)
		{
			return new ActiveXObject('Microsoft.XMLHTTP');
		}
		else if (window.XMLHttpRequest)
		{
			return new XMLHttpRequest();
		}
		return false;
	};
	
	this.request = this.getRequest();
	
	if(this.request) 
	{
		var req = this.request;
		req.onreadystatechange = this.bindFunction(this.stateChange, this);

		if (this.postBody!=="") 
		{
			req.open("POST", url, true);
			req.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
			req.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
			req.setRequestHeader('Connection', 'close');
		} 
		else 
		{
			req.open("GET", url, true);
		}

		req.send(this.postBody);
	}
};
