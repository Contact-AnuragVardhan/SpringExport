package com.org.export.base;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

public abstract class AbstractExcelView extends AbstractView 
{
	private static final String CONTENT_TYPE = "application/vnd.ms-excel";
	private static final String DEFAULT_FILE_NAME = "excel";
	
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
		if(!downloadFileName.endsWith(".xls"))
		{
			downloadFileName += ".xls";
		}
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFileName);
        response.setHeader(headerKey, headerValue);
        response.setContentType(CONTENT_TYPE);
    }
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		HSSFWorkbook workbook;
		workbook = new HSSFWorkbook();
		buildExcelDocument(model, workbook, request, response);
		prepareResponse(request,response);
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}
	
	//from Spring AbstractExcelView
	protected HSSFCell getCell(HSSFSheet sheet, int row, int col)
	{
		HSSFRow sheetRow = sheet.getRow(row);
		if (sheetRow == null) 
		{
			sheetRow = sheet.createRow(row);
		}
		HSSFCell cell = sheetRow.getCell((short) col);
		if (cell == null) 
		{
			cell = sheetRow.createCell((short) col);
		}
		return cell;
	}

	//from Spring AbstractExcelView
	protected void setText(HSSFCell cell, String text) 
	{
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(text);
	}
	
	protected abstract void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

}
