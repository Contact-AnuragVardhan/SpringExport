package com.org.export.model;

import java.util.List;
import java.util.Map;

public class ExportDataDTO 
{
	private Object parent;
	private List<Map<String,Object>> lstChildren;
	
	
	public List<Map<String, Object>> getLstChildren() {
		return lstChildren;
	}
	public void setLstChildren(List<Map<String, Object>> lstChildren) {
		this.lstChildren = lstChildren;
	}
	public Object getParent() {
		return parent;
	}
	public void setParent(Object parent) {
		this.parent = parent;
	}
	

}
