
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
