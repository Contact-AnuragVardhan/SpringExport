package com.org.export.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.org.export.model.ExportInfo;
import com.org.export.model.ExportMetaData;

public abstract class AbstractExport extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;
	private static final String DEFAULT_FILE_NAME = "Export";
	private static final String META_DATA_NAME = "exportData";
	
	public AbstractExport() 
	{
        super();
    }
	
	private ExportInfo exportInfo;
	public void setExportInfo(ExportInfo exportInfo) {
		this.exportInfo = exportInfo;
	}
	public ExportInfo getExportInfo() {
		return exportInfo;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		createDocument(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		createDocument(request,response);
	}
	
	protected void createDocument(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			Object objMetaData = request.getSession().getAttribute(META_DATA_NAME);
			if(objMetaData == null || !(objMetaData instanceof ExportMetaData))
			{
				throw new Exception("Export Meta Data is missing");
			}
			setMetaDataInfo((ExportMetaData)objMetaData);
			if(this.getExportInfo().getDataProvider() != null && this.getExportInfo().getDataProvider().size() > 0)
			{
				renderDocument(request,response);
				writeToResponse(response);
			}
			else
			{
				System.out.println("Data Provider is null");
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	protected void setMetaDataInfo(ExportMetaData exportMetaData) throws SQLException
	{
		if(exportMetaData!=null)
		{
			this.setExportInfo(new ExportInfo());
			this.getExportInfo().setExportType(exportMetaData.getExportType());
			this.getExportInfo().setFileName(exportMetaData.getFileName());
			this.getExportInfo().setSheetName(exportMetaData.getSheetName());
			this.getExportInfo().setColumns(exportMetaData.getColumns());
			this.getExportInfo().setHeirarchicalData(exportMetaData.getIsHeirarchicalData());
			this.getExportInfo().setHeaderText(exportMetaData.getHeaderText());
			this.getExportInfo().setDataProvider(exportMetaData.getDataProvider());
			this.getExportInfo().setParentColumn(exportMetaData.getParentColumn());
		}
	}
	
	protected void writeToResponse(HttpServletResponse response) throws IOException 
	{
		prepareResponse(response);
		writeResponse(response);
	}
	
	protected void prepareResponse(HttpServletResponse response) 
	{
		String downloadFileName = "";
		String headerKey = "Content-Disposition";
		String fullExportType = "." + this.getExportInfo().getExportType();
		if(this.getExportInfo().getFileName() == null || this.getExportInfo().getFileName().length() == 0)
		{
			downloadFileName = DEFAULT_FILE_NAME;
		}
		else
		{
			downloadFileName = this.getExportInfo().getFileName();
		}
		if(!downloadFileName.endsWith(fullExportType))
		{
			downloadFileName += fullExportType;
		}
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFileName);
        response.setHeader(headerKey, headerValue);
        response.setContentType(this.getExportInfo().getContentType());
    }
	
	protected ByteArrayOutputStream createTemporaryOutputStream() 
	{
		return new ByteArrayOutputStream(OUTPUT_BYTE_ARRAY_INITIAL_SIZE);
	}
	
	protected abstract void renderDocument(HttpServletRequest request, HttpServletResponse response) throws Exception;
	protected abstract void writeResponse(HttpServletResponse response) throws IOException;
}
