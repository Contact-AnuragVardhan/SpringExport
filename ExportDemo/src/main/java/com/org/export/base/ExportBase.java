package com.org.export.base;

import com.org.export.model.ExportInfo;

public abstract class ExportBase 
{
	private ExportInfo exportInfo;
	public void setExportInfo(ExportInfo exportInfo) {
		this.exportInfo = exportInfo;
	}
	public ExportInfo getExportInfo() {
		return exportInfo;
	}
	
	public abstract void renderDocument() throws Exception;
}
