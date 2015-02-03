package com.org.export.model;

import java.util.List;

import com.org.export.enums.ExportType;
import com.org.export.interfaces.IExport;

public class ExportDTO 
{
	private ExportType exportType;
	private List<? extends IExport> lstExports;
	private String fileName;
	private String excelSheetName;
	private String headerText;
	private boolean isHeirarchicalData;
	private GridColumnInfo parentColumn;
	private String[] columnsToBeRemoved;//Array of dataField for the columns which needs to removed from the Annotation List
	private LogoDetails logoDetails;
	private String[] fileImportPaths;

	
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public List<? extends IExport> getLstExports() {
		return lstExports;
	}
	public void setLstExports(List<? extends IExport> lstExports) {
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
	public LogoDetails getLogoDetails() {
		return logoDetails;
	}
	public void setLogoDetails(LogoDetails logoDetails) {
		this.logoDetails = logoDetails;
	}
	public String[] getFileImportPaths() {
		return fileImportPaths;
	}
	public void setFileImportPaths(String[] fileImportPaths) {
		this.fileImportPaths = fileImportPaths;
	}
	public String[] getColumnsToBeRemoved() {
		return columnsToBeRemoved;
	}
	public void setColumnsToBeRemoved(String[] columnsToBeRemoved) {
		this.columnsToBeRemoved = columnsToBeRemoved;
	}
}
