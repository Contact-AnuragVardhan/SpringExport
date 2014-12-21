package com.org.export.model;

public class GridColumnInfo 
{
	private String dataField;
	private String headerText;
	private int order;
	private float width = -1.0f;
	private float relativeWidth = -1.0f;
	private boolean wordWrap;
	
	public GridColumnInfo()
	{
		
	}
	
	public GridColumnInfo(String dataField, String headerText, int order,float width, float relativeWidth, boolean wordWrap) 
	{
		this.dataField = dataField;
		this.headerText = headerText;
		this.order = order;
		this.width = width;
		this.relativeWidth = relativeWidth;
		this.wordWrap = wordWrap;
	}
	
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public boolean isWordWrap() {
		return wordWrap;
	}
	public void setWordWrap(boolean wordWrap) {
		this.wordWrap = wordWrap;
	}
	public float getRelativeWidth() {
		return relativeWidth;
	}
	public void setRelativeWidth(float relativeWidth) {
		this.relativeWidth = relativeWidth;
	} 
}
