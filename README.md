if(this.hasAttribute("class") && this.__coreElement) 
	{
		var className = this.getAttribute("class");
		this.util.removeStyleClass(this,className);
		this.util.addStyleClass(this.__coreElement,className);
	}
	
	
	
	.nsTextBox 
	{
	    outline: 0;
	    border-color: #ECECEC;
	    border-style: solid;
	    border-width: 1px;
	    background-color: #ffffff;
	    padding: 6px;
	    border-radius: 2px;
	    margin-bottom: 5px;
	}	
