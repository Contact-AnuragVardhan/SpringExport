package com.org.modelView.export.model;

import java.util.List;

import com.org.export.enums.ExportType;
import com.org.export.model.GridColumnInfo;
import com.org.modelView.export.interfaces.IExport;

public class ExportDTO 
{
	private ExportType exportType;
	private List<IExport> lstExports;
	private String fileName;
	private String excelSheetName;
	private String headerText;
	private boolean isHeirarchicalData;
	private GridColumnInfo parentColumn;

	
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
	public boolean isHeirarchicalData() {
		return isHeirarchicalData;
	}
	public void setHeirarchicalData(boolean isHeirarchicalData) {
		this.isHeirarchicalData = isHeirarchicalData;
	}
	public GridColumnInfo getParentColumn() {
		return parentColumn;
	}
	public void setParentColumn(GridColumnInfo parentColumn) {
		this.parentColumn = parentColumn;
	}
	public ExportType getExportType() {
		return exportType;
	}
	public void setExportType(ExportType exportType) {
		this.exportType = exportType;
	}
}
