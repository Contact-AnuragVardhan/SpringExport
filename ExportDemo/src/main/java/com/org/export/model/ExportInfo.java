package com.org.export.model;

import java.io.OutputStream;
import java.util.List;

import com.org.export.enums.ExportType;

public class ExportInfo 
{
	private String contentType;
	private ExportType exportType;
	private String fileName;
	private String sheetName;
	private GridColumnInfo parentColumn;
	private List<GridColumnInfo> columns;
	private boolean isHeirarchicalData;
	private List<ExportDataDTO> dataProvider;
	private LogoDetails logoDetails;
	private String[] fileImportPaths;
	private String headerText;
	private OutputStream outputStream;
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
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
	public List<GridColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(List<GridColumnInfo> columns) {
		this.columns = columns;
	}
	public boolean isHeirarchicalData() {
		return isHeirarchicalData;
	}
	public void setHeirarchicalData(boolean isHeirarchicalData) {
		this.isHeirarchicalData = isHeirarchicalData;
	}
	public List<ExportDataDTO> getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(List<ExportDataDTO> dataProvider) {
		this.dataProvider = dataProvider;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public GridColumnInfo getParentColumn() {
		return parentColumn;
	}
	public void setParentColumn(GridColumnInfo parentColumn) {
		this.parentColumn = parentColumn;
	}
	public String[] getFileImportPaths() {
		return fileImportPaths;
	}
	public void setFileImportPaths(String[] fileImportPaths) {
		this.fileImportPaths = fileImportPaths;
	}
	public LogoDetails getLogoDetails() {
		return logoDetails;
	}
	public void setLogoDetails(LogoDetails logoDetails) {
		this.logoDetails = logoDetails;
	}
}
