package com.org.export.impl;

import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.org.export.base.ExportBase;
import com.org.export.model.ExportDataDTO;
import com.org.export.model.GridColumnInfo;

public class PDFExportImpl extends ExportBase
{
	protected final float SPACE_BEFORE_TABLE = 5.0f;
	protected final int CELL_PADDING = 5;
	protected final float DEFAULT_RELATIVE_WIDTH = 3.0f;
	
	protected final int TITLE_FONT_SIZE = 20;
	protected BaseColor HEADER_BACKGROUND_COLOR;
	protected BaseColor HEADER_TEXT_COLOR;
	protected BaseColor GROUPED_HEADER_BACKGROUND_COLOR;
	protected BaseColor GROUPED_HEADER_TEXT_COLOR;
	protected BaseColor TABLE_HEADER_BACKGROUND_COLOR;
	protected BaseColor TABLE_HEADER_TEXT_COLOR;
	protected BaseColor[] TABLE_BODY_ALTERNATE_COLOR;
	protected BaseColor TABLE_BODY_TEXT_COLOR;
	
	@Override
	public void renderDocument() throws Exception
    {
		HEADER_BACKGROUND_COLOR = BaseColor.WHITE;
		HEADER_TEXT_COLOR = BaseColor.BLACK; 
		GROUPED_HEADER_BACKGROUND_COLOR = BaseColor.GRAY;
		GROUPED_HEADER_TEXT_COLOR = BaseColor.WHITE; 
		TABLE_HEADER_BACKGROUND_COLOR = BaseColor.DARK_GRAY;
		TABLE_HEADER_TEXT_COLOR = BaseColor.WHITE; 
		TABLE_BODY_ALTERNATE_COLOR = new BaseColor[]{BaseColor.WHITE,BaseColor.LIGHT_GRAY};
		TABLE_BODY_TEXT_COLOR = BaseColor.BLACK; 
		
    	Document document = new Document();
    	setDocumentPrinterConfigurations(document);
		PdfWriter writer = PdfWriter.getInstance(document, this.getExportInfo().getOutputStream());
		setWriterPrinterConfigurations(writer);
		buildPdfMetadata(document);

		document.open();
		buildPdfDocument(document, writer);
		document.close();
    }
    
    protected void buildPdfDocument(Document document, PdfWriter writer) throws DocumentException,IllegalAccessException
    {
    	float spaceBeforeTable = 0.0f;
    	if(this.getExportInfo().getHeaderText() != null && this.getExportInfo().getHeaderText().length() > 0)
		{
    		Font font = FontFactory.getFont(FontFactory.COURIER);
    		font.setColor(HEADER_TEXT_COLOR);
    		font.setStyle(Font.BOLD);
    		font.setSize(TITLE_FONT_SIZE);
			document.add(new Paragraph(this.getExportInfo().getHeaderText(),font));
			spaceBeforeTable = SPACE_BEFORE_TABLE;
		}
    	PdfPTable table = null;
    	if(this.getExportInfo().isHeirarchicalData())
    	{
    		for(int count = 0; count < this.getExportInfo().getColumns().size(); count++)
         	{
    			GridColumnInfo gridColumnInfo = this.getExportInfo().getColumns().get(count);
    			if(gridColumnInfo.getDataField()!=null && gridColumnInfo.getDataField().equals(this.getExportInfo().getParentColumn().getDataField()))
    			{
    				this.getExportInfo().getColumns().remove(count);
    				break;
    			}
         	}
	    	for(int count = 0; count < this.getExportInfo().getDataProvider().size(); count++)
	    	{
	    		ExportDataDTO exportData = this.getExportInfo().getDataProvider().get(count);
	    		table = createTable(exportData,spaceBeforeTable);
	    		document.add(table);
	            //document.newPage();
	    	}
    	}
    	else
    	{
    		table = createTable(this.getExportInfo().getDataProvider().get(0),spaceBeforeTable);
    		document.add(table);
    	}
    }
    
    protected PdfPTable createTable(ExportDataDTO exportDataDTO,float spaceBeforeTable) throws DocumentException,IllegalAccessException
    {
    	PdfPTable table = new PdfPTable(this.getExportInfo().getColumns().size());
    	table.setWidthPercentage(100.0f);
		table.setSpacingBefore(spaceBeforeTable);
		table.getDefaultCell().setPadding(CELL_PADDING);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
		if(this.getExportInfo().isHeirarchicalData())
    	{
			createGroupedRow(table,exportDataDTO.getParent());
    	}
		createTableHeader(table);
		createTableBody(table,exportDataDTO.getLstChildren());
		
		return table;
    }
    
    protected void createGroupedRow(PdfPTable table,Object groupedLabel)
    {
        table.getDefaultCell().setColspan(this.getExportInfo().getColumns().size());
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBackgroundColor(GROUPED_HEADER_BACKGROUND_COLOR);
        
		table.addCell(((groupedLabel == null)? "" :  groupedLabel.toString()));
    }
    
    protected void createTableHeader(PdfPTable table) throws DocumentException 
	{
    	table.getDefaultCell().setColspan(1);
    	table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	    
		float[] arrColWidth = new float[this.getExportInfo().getColumns().size()];
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(TABLE_HEADER_TEXT_COLOR);
		font.setStyle(Font.NORMAL);
		
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(TABLE_HEADER_BACKGROUND_COLOR);
		
		for(int count = 0; count < this.getExportInfo().getColumns().size(); count++)
     	{
			GridColumnInfo gridColumnInfo = this.getExportInfo().getColumns().get(count);
 			cell.setPhrase(new Phrase(gridColumnInfo.getHeaderText(),font));
 			if(gridColumnInfo.getRelativeWidth() > -1.0f)
 			{
 				arrColWidth[count] = gridColumnInfo.getRelativeWidth();
 			}
 			else
 			{
 				arrColWidth[count] = DEFAULT_RELATIVE_WIDTH;
 			}
 			table.addCell(cell);
     	}
		//setting Table Width
		table.setWidths(arrColWidth);
	}
	
	protected void createTableBody(PdfPTable table,List<Map<String,Object>> lstRows) throws IllegalAccessException
	{
		if(lstRows != null && lstRows.size() > 0)
		{
			for(int rowCount = 0;rowCount < lstRows.size(); rowCount++)
         	{
				if(rowCount % 2 == 0)
				{
					table.getDefaultCell().setBackgroundColor(TABLE_BODY_ALTERNATE_COLOR[0]);
				}
				else
				{
					table.getDefaultCell().setBackgroundColor(TABLE_BODY_ALTERNATE_COLOR[1]);
				}
				Map<String,Object> objData = lstRows.get(rowCount);
				for(int colCount = 0; colCount < this.getExportInfo().getColumns().size(); colCount++)
	            {
					GridColumnInfo columnInfo = this.getExportInfo().getColumns().get(colCount);
					String colValue = ((objData.get(columnInfo.getDataField()) == null) ? "" : objData.get(columnInfo.getDataField()).toString());
					Font font = FontFactory.getFont(FontFactory.defaultEncoding);
					font.setColor(TABLE_BODY_TEXT_COLOR);
					font.setStyle(Font.NORMAL);
					PdfPCell cell = new PdfPCell();
					cell.setPhrase(new Phrase(colValue,font));
					table.addCell(colValue);
		        }
	         }
		}
	}
	
	protected void setDocumentPrinterConfigurations(Document document)
	{
		document.setPageSize(PageSize.A4);
	}
	
	protected void setWriterPrinterConfigurations(PdfWriter writer)
	{
		writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
	}
	
	protected void buildPdfMetadata(Document document) 
	{
		
	}
}
