package com.org.export.enums;

public enum ExportType {
	XLS("xls"),
	XLSX("xlsx"),
	PDF("pdf")
	;
	
	private final String exportType;
	ExportType(String exportType) 
	{
        this.exportType = exportType;
    }
	
	public String getExportType() 
	{
        return this.exportType;
    }
}
