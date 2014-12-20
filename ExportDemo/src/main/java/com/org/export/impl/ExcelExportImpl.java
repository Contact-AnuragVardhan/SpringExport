package com.org.export.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.export.base.ExportBase;
import com.org.export.enums.ExportType;
import com.org.export.model.ExportDataDTO;
import com.org.export.model.GridColumnInfo;
import com.org.export.util.DataSourceUtil;

public class ExcelExportImpl extends ExportBase
{
	protected static final String DEFAULT_SHEET_NAME = "Sheet";
	protected static short HEADER_BACKGROUND_COLOR;
	protected static short HEADER_TEXT_COLOR;
	protected static short TABLE_HEADER_BACKGROUND_COLOR;
	protected static short TABLE_HEADER_TEXT_COLOR;
	protected static short[] TABLE_BODY_COLOR;
	protected static short TABLE_BODY_TEXT_COLOR;
	
	
	protected final int HEADER_TEXT_HEIGHT = 20;
	protected int headerRowIndex = 0;
	
	private Workbook workbook = null;
	public Workbook getWorkbook() {
		return workbook;
	}

	@Override
	public void renderDocument() throws Exception 
	{
		HEADER_BACKGROUND_COLOR = IndexedColors.WHITE.getIndex();
		HEADER_TEXT_COLOR = IndexedColors.BLACK.getIndex();
		TABLE_HEADER_BACKGROUND_COLOR = IndexedColors.GREY_80_PERCENT.getIndex();
		TABLE_HEADER_TEXT_COLOR = IndexedColors.WHITE.getIndex();
		TABLE_BODY_COLOR = new short[]{IndexedColors.WHITE.getIndex(),IndexedColors.GREY_25_PERCENT.getIndex()};
		TABLE_BODY_TEXT_COLOR = IndexedColors.BLACK.getIndex();
		
		buildExcelDocument();
	}
	
	protected void buildExcelDocument() throws IllegalAccessException
    {
		if(this.getExportInfo().getExportType() == ExportType.XLS)
        {
			workbook = new HSSFWorkbook();
        }
        else
        {
        	workbook = new XSSFWorkbook();
        }
		Map<String, CellStyle> styles = createStyles(workbook);
		String sheetFileName = ((this.getExportInfo().getSheetName() == null || this.getExportInfo().getSheetName().length() == 0) ? DEFAULT_SHEET_NAME : this.getExportInfo().getSheetName());
        // create a new Excel sheet
        Sheet sheet = workbook.createSheet(sheetFileName);
        setPrinterConfiguration(sheet);
        if(this.getExportInfo().getHeaderText() != null && this.getExportInfo().getHeaderText().length() > 0)
		{
        	Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(HEADER_TEXT_HEIGHT);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(this.getExportInfo().getHeaderText());
            titleCell.setCellStyle(styles.get("title"));
            String cellRangeAddress = "$A$1:$" + DataSourceUtil.getAlphabetByIndex(this.getExportInfo().getColumns().size(),true)  + "$1";
            sheet.addMergedRegion(CellRangeAddress.valueOf(cellRangeAddress));
            headerRowIndex = 1;
		}
        createTable(workbook,sheet,styles);
    }
	
	protected void createTable(Workbook workbook,Sheet sheet,Map<String, CellStyle> styles) throws IllegalAccessException
	{
		createHeader(workbook,sheet,styles.get("header"));
		for(int count = 0; count < this.getExportInfo().getDataProvider().size(); count++)
    	{
    		ExportDataDTO exportData = this.getExportInfo().getDataProvider().get(count);
    		createBody(workbook,sheet,exportData.getLstChildren(),styles.get("oddRow"),styles.get("evenRow"));
    	}
        setColumnWidth(sheet);
	}
	
	protected void createHeader(Workbook workbook,Sheet sheet,CellStyle style)
	{
		if(workbook!= null && sheet != null)
    	{
			Row headerRow = sheet.createRow(headerRowIndex);
         	for(int count = 0; count < this.getExportInfo().getColumns().size(); count++)
         	{
         		GridColumnInfo gridColumnInfo = this.getExportInfo().getColumns().get(count);
 				Cell cell = headerRow.createCell(count);
 				cell.setCellValue(gridColumnInfo.getHeaderText());
 				cell.setCellStyle(style);
 				//not working now
 				/*if(gridColumnInfo.isWordWrap())
 				{
 					setColumnWordWrap(workbook,sheet,count);
 				}*/
         	}
    	}
	}
	
	protected void createBody(Workbook workbook,Sheet sheet,List<Map<String,Object>> lstRows,CellStyle oddStyle,CellStyle evenStyle) throws IllegalAccessException
	{
		if(lstRows != null && lstRows.size() > 0)
		{
			for(int rowCount = 0;rowCount < lstRows.size(); rowCount++)
         	{
				// (rowCount + 1) as 0 is for Header Row
        		Row row = sheet.createRow(rowCount + headerRowIndex + 1);
        		Map<String,Object> objData = lstRows.get(rowCount);
             	for(int colCount = 0; colCount < this.getExportInfo().getColumns().size(); colCount++)
             	{
             		GridColumnInfo columnInfo = this.getExportInfo().getColumns().get(colCount);
             		Object colValue = objData.get(columnInfo.getDataField());
     				Cell cell = row.createCell(colCount);
     				setCellValue(cell,colValue);
     				CellStyle style; 
     				if(rowCount % 2 == 0)
    				{
     					style = oddStyle;
    				}
    				else
    				{
    					style = evenStyle;
    				}
     				if(columnInfo.isWordWrap())
     				{
     					style.setWrapText(true);
     				}
     				cell.setCellStyle(style);
             	}
             }
		}
	}
	
	protected void setColumnWidth(Sheet sheet)
	{
		if(sheet != null)
    	{
    		for(int count = 0; count < this.getExportInfo().getColumns().size(); count++)
         	{
    			GridColumnInfo columnInfo = this.getExportInfo().getColumns().get(count);
     			if(columnInfo.getWidth() > -1.0f)
     			{
     				sheet.setColumnWidth(count,(int)(256 * Math.round(columnInfo.getWidth())));
     			}
     			else
     			{
     				sheet.autoSizeColumn(count);
     			}
         	}
    	}
	}
	
	protected void setColumnWordWrap(Workbook workbook,Sheet sheet,int columnCount)
	{
		if(workbook!=null && sheet != null)
		{
			//CellStyle style = sheet.getColumnStyle(columnCount);
			//if(style == null)
			//{
			CellStyle style = workbook.createCellStyle();
			//}
			style.setWrapText(true);
		    sheet.setDefaultColumnStyle(columnCount, style);
		}
	}
	
	protected void setCellValue(Cell cell,Object value)
	{
		  CreationHelper helper = cell.getSheet().getWorkbook().getCreationHelper();
	      Object newValue = value;
	      if (value == null)
	      {
	         newValue = helper.createRichTextString("");
	         cell.setCellValue((RichTextString) newValue);
	         cell.setCellType(Cell.CELL_TYPE_BLANK);
	      }
	      else if (value instanceof String)
	      {
	         newValue = helper.createRichTextString(value.toString());
	         cell.setCellValue((RichTextString) newValue);
	      }
	      else if (value instanceof RichTextString)
	      {
	         cell.setCellValue((RichTextString) value);
	      }
	      else if (value instanceof Double)
	      {
	         cell.setCellValue((Double) value);
	      }
	      else if (value instanceof Integer)
	      {
	         cell.setCellValue((Integer) value);
	      }
	      else if (value instanceof Float)
	      {
	         cell.setCellValue((Float) value);
	      }
	      else if (value instanceof Long)
	      {
	         cell.setCellValue((Long) value);
	      }
	      else if (value instanceof Date)
	      {
	         cell.setCellValue((Date) value);
	      }
	      else if (value instanceof Calendar)
	      {
	         cell.setCellValue((Calendar) value);
	      }
	      else if (value instanceof Short)
	      {
	         cell.setCellValue((Short) value);
	      }
	      else if (value instanceof Byte)
	      {
	         cell.setCellValue((Byte) value);
	      }
	      else if (value instanceof Boolean)
	      {
	         cell.setCellValue((Boolean) value);
	      }
	      else if (value instanceof BigInteger)
	      {
	         BigInteger bi = (BigInteger) value;
	         cell.setCellValue(bi.doubleValue());
	      }
	      else if (value instanceof BigDecimal)
	      {
	         BigDecimal bd = (BigDecimal) value;
	         cell.setCellValue(bd.doubleValue());
	      }
	      else
	      {
	         newValue = helper.createRichTextString(value.toString());
	         cell.setCellValue((RichTextString) newValue);
	      }
	}
	
	protected void setPrinterConfiguration(Sheet sheet)
	{
		 PrintSetup printSetup = sheet.getPrintSetup();
	     printSetup.setLandscape(true);
	    // printSetup.setPaperSize((short)PaperSize.A4_PAPER);
	}
	
	protected static Map<String, CellStyle> createStyles(Workbook workbook)
	{
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short)14);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setColor(HEADER_TEXT_COLOR);
        style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(HEADER_BACKGROUND_COLOR);
        style.setFont(titleFont);
        styles.put("title", style);

        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short)11);
        headerFont.setColor(TABLE_HEADER_TEXT_COLOR);
        style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(TABLE_HEADER_BACKGROUND_COLOR);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        style.setWrapText(true);
        styles.put("header", style);
        
        Font oddRowStyle = workbook.createFont();
        oddRowStyle.setColor(TABLE_BODY_TEXT_COLOR);
        style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(TABLE_BODY_COLOR[0]);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(oddRowStyle);
        styles.put("oddRow", style);
        
        
        Font evenRowStyle = workbook.createFont();
        evenRowStyle.setColor(TABLE_BODY_TEXT_COLOR);
        style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(TABLE_BODY_COLOR[1]);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(evenRowStyle);
        styles.put("evenRow", style);

        return styles;
    }
	
	//from Spring AbstractExcelView
	public Cell getCell(Sheet sheet, int row, int col)
	{
		Row sheetRow = sheet.getRow(row);
		if (sheetRow == null) 
		{
			sheetRow = sheet.createRow(row);
		}
		Cell cell = sheetRow.getCell(col);
		if (cell == null) 
		{
			cell = sheetRow.createCell(col);
		}
		return cell;
	}

	//from Spring AbstractExcelView
	public void setText(Cell cell, String text) 
	{
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(text);
	}

}
