package com.org.modelView.export.impl;

import com.org.modelView.export.annotation.ExportInfo;
import com.org.modelView.export.base.AbstractExcelView;
import com.org.modelView.export.interfaces.IExport;
import com.org.modelView.export.model.ExportDTO;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class ExcelExport extends AbstractExcelView
{
	protected static final String DEFAULT_SHEET_NAME = "Sheet";
	
	protected final int HEADER_ROW = 0;
	
	protected List<Field> lstAnnotatedFields = null;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		// get data model which is passed by the Spring container
		ExportDTO export = (ExportDTO) model.get("exportInfo");
		if(export != null)
		{
			this.setFileName(export.getFileName());
			List<IExport> lstExports = export.getLstExports();
			String sheetFileName = ((export.getExcelSheetName() == null || export.getExcelSheetName().length() == 0) ? DEFAULT_SHEET_NAME : export.getExcelSheetName());
	        // create a new Excel sheet
	        HSSFSheet sheet = workbook.createSheet(sheetFileName);
	        getAnnotatedField(lstExports);
	        createHeader(workbook,sheet);
	        createBody(workbook,sheet,lstExports);
	        setColumnWidth(sheet);
		}
	}
	
	protected void getAnnotatedField(List<IExport> lstExport)
	{
		lstAnnotatedFields = new ArrayList<Field>();
		if(lstExport!=null && lstExport.size() > 0)
    	{
        	IExport exportClass = lstExport.get(0);
        	if(exportClass != null)
        	{
        		Field[] fields = exportClass.getClass().getDeclaredFields();
	         	for(int count = 0; count < fields.length; count++)
	         	{
	         		Field field = fields[count];
	         		field.setAccessible(true);
	         		if(field.isAnnotationPresent(ExportInfo.class))
	         		{
	         			lstAnnotatedFields.add(field);
	         		}
	         	}
        	}
    	}
	}
	
	protected void createHeader(HSSFWorkbook workbook,HSSFSheet sheet)
	{
		if(workbook!= null && sheet != null)
    	{
    		// start of style for header cells
	        CellStyle style = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setFontName("Arial");
	        style.setFillForegroundColor(HSSFColor.BLUE.index);
	        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font.setColor(HSSFColor.WHITE.index);
	        style.setFont(font);
	        // end of style for header cells
			HSSFRow header = sheet.createRow(HEADER_ROW);
         	for(int count = 0; count < lstAnnotatedFields.size(); count++)
         	{
         		Field field = lstAnnotatedFields.get(count);
     			ExportInfo exportInfo = field.getAnnotation(ExportInfo.class);
 				HSSFCell cell = header.createCell(count);
 				cell.setCellValue(exportInfo.headerText());
 				cell.setCellStyle(style);
 				if(exportInfo.wordWrap())
 				{
 					setColumnWordWrap(workbook,sheet,count);
 				}
         	}
    	}
	}
	
	protected void createBody(HSSFWorkbook workbook,HSSFSheet sheet,List<IExport> lstExport) throws IllegalAccessException
	{
		if(lstExport != null && lstExport.size() > 0)
		{
			for(int rowCount = 0;rowCount < lstExport.size(); rowCount++)
         	{
				// (rowCount + 1) as 0 is for Header Row
        		HSSFRow row = sheet.createRow(rowCount + HEADER_ROW + 1);
        		IExport export = lstExport.get(rowCount);
             	for(int colCount = 0; colCount < lstAnnotatedFields.size(); colCount++)
             	{
             		Field field = lstAnnotatedFields.get(colCount);
         			int cellType = -1;
         			if(field.get(export) instanceof BigDecimal || field.get(export) instanceof BigInteger)
     				{
         				cellType = Cell.CELL_TYPE_NUMERIC;
     				}
     				else if(field.get(export) instanceof Boolean)
     				{
     					cellType = Cell.CELL_TYPE_BOOLEAN;
     				}
     				else
     				{
     					cellType = Cell.CELL_TYPE_STRING;
     				}
     				HSSFCell cell = row.createCell(colCount,cellType);
     				String colValue = ((field.get(export) == null) ? "" : field.get(export).toString());
     				cell.setCellValue(colValue);
             	}
             }
		}
	}
	
	protected void setColumnWidth(HSSFSheet sheet)
	{
		if(sheet != null)
    	{
    		int colCount = 0;
    		for(int count = 0; count < lstAnnotatedFields.size(); count++)
         	{
         		Field field = lstAnnotatedFields.get(colCount);
         		if(field.isAnnotationPresent(ExportInfo.class))
         		{
         			ExportInfo exportInfo = field.getAnnotation(ExportInfo.class);
         			if(exportInfo.width() > -1.0f)
         			{
         				sheet.setColumnWidth(colCount,(int)(256 * Math.round(exportInfo.width())));
         			}
         			else
         			{
         				sheet.autoSizeColumn(colCount);
         			}
     				colCount++;
         		}
         	}
    	}
	}
	
	protected void setColumnWordWrap(HSSFWorkbook workbook,HSSFSheet sheet,int columnCount)
	{
		if(workbook!=null && sheet != null)
		{
			CellStyle style = workbook.createCellStyle();
			style.setWrapText(true);
		    sheet.setDefaultColumnStyle(columnCount, style);
		}
	}
}
