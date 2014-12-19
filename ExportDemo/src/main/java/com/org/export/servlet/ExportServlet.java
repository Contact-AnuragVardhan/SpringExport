package com.org.export.servlet;

import com.org.export.base.AbstractExport;
import com.org.export.base.ExportBase;
import com.org.export.impl.ExcelExportImpl;
import com.org.export.impl.PDFExportImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExportServlet extends AbstractExport 
{
	private static final long serialVersionUID = 1L;
	private static final String PDF_CONTENT_TYPE = "application/pdf";
	protected static final String EXCEL_CONTENT_TYPE = "application/vnd.ms-excel";
	
	protected ExportBase objExport = null;
	
    public ExportServlet() 
    {
        super();
    }
    
    @Override
    protected void renderDocument(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	OutputStream outputStream = null;
    	String contentType = null; 
    	switch(this.getExportInfo().getExportType())
    	{
    		case PDF:
    			contentType = PDF_CONTENT_TYPE;
    			outputStream = createTemporaryOutputStream();
    			objExport = new PDFExportImpl();
    		break;
    		case XLS:
    			contentType = EXCEL_CONTENT_TYPE;
    			//outputStream = response.getOutputStream();
    			objExport = new ExcelExportImpl();
        	break;
    		case XLSX:
    			contentType = EXCEL_CONTENT_TYPE;
    			//outputStream = response.getOutputStream();
    			objExport = new ExcelExportImpl();
        	break;
    	}
    	setExportBase(objExport,contentType,outputStream);
    	objExport.renderDocument();
    }
    
    @Override
    protected void writeResponse(HttpServletResponse response) throws IOException
    {
    	ServletOutputStream servletOutputStream = response.getOutputStream();
    	switch(this.getExportInfo().getExportType())
    	{
    		case PDF:
    			ByteArrayOutputStream baos = (ByteArrayOutputStream) objExport.getExportInfo().getOutputStream();
    			response.setContentLength(baos.size());
    			baos.writeTo(servletOutputStream);
    		break;
    		case XLS:
    			((ExcelExportImpl)objExport).getWorkbook().write(servletOutputStream);
        	break;
    		case XLSX:
    			((ExcelExportImpl)objExport).getWorkbook().write(servletOutputStream);
        	break;
    	}
    	servletOutputStream.flush();
    }
    
    protected void setExportBase(ExportBase objExport,String contentType,OutputStream outputStream) throws Exception
    {
    	if(objExport!=null)
    	{
    		objExport.setExportInfo(this.getExportInfo());
    		objExport.getExportInfo().setContentType(contentType);
    		objExport.getExportInfo().setOutputStream(outputStream);
    	}
    }
}
