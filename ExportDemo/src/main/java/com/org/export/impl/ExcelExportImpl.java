package com.org.export.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.export.base.ExportBase;
import com.org.export.enums.ExportType;
import com.org.export.model.ExportDataDTO;
import com.org.export.model.GridColumnInfo;

public class ExcelExportImpl extends ExportBase
{
	protected static final String DEFAULT_SHEET_NAME = "Sheet";
	protected static short HEADER_BACKGROUND_COLOR;
	protected static short HEADER_TEXT_COLOR;
	protected static short TABLE_HEADER_BACKGROUND_COLOR;
	protected static short TABLE_HEADER_TEXT_COLOR;
	protected static short TABLE_BODY_ODD_COLOR;
	protected static short TABLE_BODY_EVEN_COLOR;
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
		TABLE_BODY_ODD_COLOR = IndexedColors.GREY_50_PERCENT.getIndex();
		TABLE_BODY_EVEN_COLOR = IndexedColors.WHITE.getIndex();
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
        if(this.getExportInfo().getHeaderText() != null && this.getExportInfo().getHeaderText().length() > 0)
		{
        	Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(HEADER_TEXT_HEIGHT);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(this.getExportInfo().getHeaderText());
            titleCell.setCellStyle(styles.get("title"));
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$L$1"));
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
// 				if(gridColumnInfo.isWordWrap())
// 				{
// 					setColumnWordWrap(workbook,sheet,count);
// 				}
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
             		Object colValue = ((objData.get(columnInfo.getDataField()) == null) ? "" : objData.get(columnInfo.getDataField()).toString());
         			int cellType = getCellDataType(colValue);
     				Cell cell = row.createCell(colCount,cellType);
     				cell.setCellStyle(oddStyle);
     				setCellValue(cell,cellType,colValue);
     				
     				/*if(rowCount % 2 == 0)
    				{
     					cell.setCellStyle(oddStyle);
    				}
    				else
    				{
    					cell.setCellStyle(evenStyle);
    				}*/
     				
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
			CellStyle style = sheet.getColumnStyle(columnCount);
			if(style == null)
			{
				style = workbook.createCellStyle();
			}
			style.setWrapText(true);
		    sheet.setDefaultColumnStyle(columnCount, style);
		}
	}
	
	protected int getCellDataType(Object value)
	{
		int cellType = -1;
		String className = value.getClass().getSimpleName().toLowerCase();
		
		if(className.equals("boolean"))
		{
			cellType = Cell.CELL_TYPE_BOOLEAN;
		}
		else if(className.equals("double") || className.equals("float") || className.equals("int") || className.equals("integer") 
				|| className.equals("long") || className.equals("short"))
		{
			cellType = Cell.CELL_TYPE_NUMERIC;
		}
			
		else
		{
			cellType = Cell.CELL_TYPE_STRING;
		}
		
		return cellType;
	}
	
	protected void setCellValue(Cell cell,int cellType,Object value)
	{
		switch(cellType)
		{
			case Cell.CELL_TYPE_BOOLEAN:
				cell.setCellValue((Boolean)value);
			break;
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellValue((Double)value);
			break;
			case Cell.CELL_TYPE_STRING:
				cell.setCellValue((String)value);
			break;
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
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFillForegroundColor(TABLE_BODY_ODD_COLOR);
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
        style.setFillForegroundColor(TABLE_BODY_EVEN_COLOR);
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