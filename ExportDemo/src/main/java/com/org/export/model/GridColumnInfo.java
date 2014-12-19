package com.org.export.model;

public class GridColumnInfo 
{
	private String dataField;
	private String headerText;
	private int order;
	private float width;
	private boolean wordWrap;
	
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
}
