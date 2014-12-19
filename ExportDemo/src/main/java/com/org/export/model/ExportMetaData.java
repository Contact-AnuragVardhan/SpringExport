package com.org.export.model;

import java.util.List;

import com.org.export.enums.ExportType;

public class ExportMetaData 
{
	private ExportType exportType;
	private String fileName;
	private String sheetName;
	private String headerText;
	private boolean isHeirarchicalData;
	private GridColumnInfo parentColumn;
	private List<ExportDataDTO> dataProvider;
	private List<GridColumnInfo> columns;
	
	public ExportType getExportType() {
		return exportType;
	}
	public void setExportType(ExportType exportType) {
		this.exportType = exportType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public boolean getIsHeirarchicalData() {
		return isHeirarchicalData;
	}
	public void setIsHeirarchicalData(boolean isHeirarchicalData) {
		this.isHeirarchicalData = isHeirarchicalData;
	}
	public GridColumnInfo getParentColumn() {
		return parentColumn;
	}
	public void setParentColumn(GridColumnInfo parentColumn) {
		this.parentColumn = parentColumn;
	}
	public List<GridColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(List<GridColumnInfo> columns) {
		this.columns = columns;
	}
	public List<ExportDataDTO> getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(List<ExportDataDTO> dataProvider) {
		this.dataProvider = dataProvider;
	}
	
}
