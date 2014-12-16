package com.org.export.base;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;


public abstract class AbstractPdfView extends AbstractView 
{
	private static final String CONTENT_TYPE = "application/pdf";
	private static final String DEFAULT_FILE_NAME = "PDF";
	
	private String fileName;
	public void setFileName(String fileName) 
	{
	    this.fileName = fileName;
	}
	
	@Override
	protected boolean generatesDownloadContent() 
	{
		return true;
	}
	
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) 
	{
		String downloadFileName = "";
		String headerKey = "Content-Disposition";
		if(fileName == null || fileName.length() == 0)
		{
			downloadFileName = DEFAULT_FILE_NAME;
		}
		else
		{
			downloadFileName = fileName;
		}
		if(!downloadFileName.endsWith(".pdf"))
		{
			downloadFileName += ".pdf";
		}
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFileName);
        response.setHeader(headerKey, headerValue);
        response.setContentType(CONTENT_TYPE);
    }

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		// IE workaround: write into byte array first.
		ByteArrayOutputStream baos = createTemporaryOutputStream();

		// Apply preferences and build metadata.
		Document document = newDocument();
		PdfWriter writer = newWriter(document, baos);
		prepareWriter(model, writer, request);
		buildPdfMetadata(model, document, request);

		// Build PDF document.
		document.open();
		buildPdfDocument(model, document, writer, request, response);
		document.close();
		prepareResponse(request,response);

		// Flush to HTTP response.
		writeToResponse(response, baos);
	}
	
	protected Document newDocument() 
	{
		return new Document(PageSize.A4);
	}
	
	protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException 
	{
		return PdfWriter.getInstance(document, os);
	}
	
	protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request) throws DocumentException 
	{
		writer.setViewerPreferences(getViewerPreferences());
	}
	
	protected int getViewerPreferences() 
	{
		return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
	}
	
	protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) 
	{
		
	}
	
	protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,HttpServletRequest request, HttpServletResponse response) throws Exception;

}
