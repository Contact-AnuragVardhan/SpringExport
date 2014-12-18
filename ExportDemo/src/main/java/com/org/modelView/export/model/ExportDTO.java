package com.org.modelView.export.model;

import java.util.List;

import com.org.modelView.export.interfaces.IExport;

public class ExportDTO 
{
	private List<IExport> lstExports;
	private String fileName;
	private String excelSheetName;
	private String headerText;
	
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public List<IExport> getLstExports() {
		return lstExports;
	}
	public void setLstExports(List<IExport> lstExports) {
		this.lstExports = lstExports;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getExcelSheetName() {
		return excelSheetName;
	}
	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}
}
