package com.org.export.model;

public class LogoDetails 
{
	private String path;
	private float height = -1.0f;
	private float scaleWidthBy = -1.0f;
	private float scaleHeightBy = -1.0f;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getScaleWidthBy() {
		return scaleWidthBy;
	}
	public void setScaleWidthBy(float scaleWidthBy) {
		this.scaleWidthBy = scaleWidthBy;
	}
	public float getScaleHeightBy() {
		return scaleHeightBy;
	}
	public void setScaleHeightBy(float scaleHeightBy) {
		this.scaleHeightBy = scaleHeightBy;
	}
}
