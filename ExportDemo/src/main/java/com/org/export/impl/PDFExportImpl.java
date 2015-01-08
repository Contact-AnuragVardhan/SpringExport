package com.org.export.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.org.export.base.ExportBase;
import com.org.export.model.ExportDataDTO;
import com.org.export.model.GridColumnInfo;
import com.org.export.model.LogoDetails;
import com.org.export.util.PDFPageEventHandler;

public class PDFExportImpl extends ExportBase
{
	protected final float SPACE_BEFORE_TABLE = 7.0f;
	protected final int CELL_PADDING = 5;
	protected final float DEFAULT_RELATIVE_WIDTH = 3.0f;
	protected final float PAGE_HEIGHT = 400.0f;
	protected final float PADDING_LEFT = 2.0f;
	protected final float PADDING_RIGHT = 2.0f;
	protected final float PADDING_TOP = 2.0f;
	protected final float PADDING_BOTTOM = 30.0f;
	protected BaseColor RED_COLOR ;
	protected final float HEADER_PADDING_LEFT = 30.0f;
	
	protected final int TITLE_FONT_SIZE = 20;
	protected BaseColor HEADER_BACKGROUND_COLOR;
	protected BaseColor HEADER_TEXT_COLOR;
	protected BaseColor GROUPED_HEADER_BACKGROUND_COLOR;
	protected BaseColor GROUPED_HEADER_TEXT_COLOR;
	protected BaseColor TABLE_HEADER_BACKGROUND_COLOR;
	protected BaseColor TABLE_HEADER_TEXT_COLOR;
	protected BaseColor[] TABLE_BODY_ALTERNATE_COLOR;
	protected BaseColor TABLE_BODY_TEXT_COLOR;
	
	protected PDFPageEventHandler pageEventHandler =null;
	
	private String companyTitle = "Nomura Surveillance System - 2";
	private String reportTitle = "Evidence of Review";
	private String[] searchCriteria;
	private String generatedOn = "Generated: 01/06/2015";
	
	@Override
	public void renderDocument() throws Exception
    {
		searchCriteria = new String[4];
		searchCriteria[0]= "From : 08/01/2014";
		searchCriteria[1]= "To : 08/31/2014";
		//searchCriteria[2]= "Group Name like : Group";
		//searchCriteria[3]= "Task like : Task";
		
		RED_COLOR = new BaseColor(192, 0, 0); 
		HEADER_BACKGROUND_COLOR = BaseColor.WHITE;
		HEADER_TEXT_COLOR = BaseColor.WHITE; 
		GROUPED_HEADER_BACKGROUND_COLOR = RED_COLOR;
		GROUPED_HEADER_TEXT_COLOR = BaseColor.WHITE; 
		TABLE_HEADER_BACKGROUND_COLOR = RED_COLOR;
		TABLE_HEADER_TEXT_COLOR = BaseColor.WHITE; 
		TABLE_BODY_ALTERNATE_COLOR = new BaseColor[]{BaseColor.WHITE,BaseColor.LIGHT_GRAY};
		TABLE_BODY_TEXT_COLOR = BaseColor.BLACK; 
		
    	Document document = new Document();
    	setDocumentPrinterConfigurations(document);
		PdfWriter writer = PdfWriter.getInstance(document, this.getExportInfo().getOutputStream());
		pageEventHandler = new PDFPageEventHandler();
		pageEventHandler.isPageNumberNeeded = true;
		pageEventHandler.startPageNumber = false;
	    writer.setPageEvent(pageEventHandler);
		setWriterPrinterConfigurations(writer);
		buildPdfMetadata(document);
		document.setMargins(0,0,0,0);
		document.open();
		buildPdfDocument(document, writer);
		document.close();
    }
    
    protected void buildPdfDocument(Document document, PdfWriter writer) throws DocumentException,IllegalAccessException,IOException
    {
    	mergeFiles(document,writer,this.getExportInfo().getFileImportPaths());
    	float spaceBeforeTable = createLogo(document,this.getExportInfo().getLogoDetails(),0.0f);
    	createNomuraPage(document);
    	document.setMargins(PADDING_LEFT,PADDING_RIGHT,PADDING_TOP,PADDING_BOTTOM);
    	document.setMarginMirroring(true);
    	spaceBeforeTable = createPageHeader(document,this.getExportInfo().getHeaderText(),spaceBeforeTable);
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
	    		Chapter chapter = createChapter(exportData, count + 1, spaceBeforeTable);
	    		document.add(chapter);
	            document.newPage();
	    	}
    	}
    	else
    	{
    		PdfPTable table = createTable(this.getExportInfo().getDataProvider().get(0),spaceBeforeTable,null);
    		document.add(table);
    	}
    }
    
    
    
    protected void mergeFiles(Document document, PdfWriter writer,String[] fileImportPaths) throws DocumentException,IOException
    {
    	if(fileImportPaths != null && fileImportPaths.length > 0)
		{
 	       for (int count = 0; count < fileImportPaths.length; count++) 
	       {
 	    	   	PdfReader reader = new PdfReader(fileImportPaths[count]);
 	    	    PdfPTable table = new PdfPTable(1);
	            table.setWidthPercentage(100f);
	            table.getDefaultCell().setBorder(0);
	            int totalPages = reader.getNumberOfPages();
	            for (int pageCount = 0; pageCount < totalPages; ) 
	            {
	            	document.newPage();
	            	PdfImportedPage page =  writer.getImportedPage(reader, ++pageCount);
	            	table.addCell(Image.getInstance(page));
	            }
	            document.add(table);
	       }
		}
    }
    
    protected void createNomuraPage(Document document) throws DocumentException
    {
    	Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(BaseColor.BLACK);
		font.setStyle(Font.BOLD);
		font.setSize(24);
		Paragraph paraHeader = new Paragraph(reportTitle,font);
		paraHeader.setSpacingBefore(40);
		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
		paraHeader.setAlignment(Element.ALIGN_LEFT);
		document.add(paraHeader);
		
		font.setSize(18);
		paraHeader = new Paragraph(companyTitle,font);
		paraHeader.setSpacingBefore(20);
		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
		paraHeader.setAlignment(Element.ALIGN_LEFT);
		document.add(paraHeader);
		
		font.setSize(14);
		font.setStyle(Font.NORMAL);
		for(int count = 0;count < searchCriteria.length;count++)
		{
			String criteria = searchCriteria[count];
			paraHeader = new Paragraph(criteria , font);
			if(count == 0)
			{
				paraHeader.setSpacingBefore(20);
			}
			paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
			paraHeader.setAlignment(Element.ALIGN_LEFT);
			document.add(paraHeader);
		}
		
		PdfPTable footerTable = new PdfPTable(2);
		footerTable.setWidthPercentage(100f);
		footerTable.setSpacingBefore(140 - (20 * searchCriteria.length));
		footerTable.getDefaultCell().setBorder(0);
		
		
		paraHeader = new Paragraph(generatedOn , font);
		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT - 2);
		paraHeader.setAlignment(Element.ALIGN_LEFT);
		
        PdfPCell cell = new PdfPCell();
        cell.addElement(paraHeader);
        cell.setBorder(0);
        //cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        footerTable.addCell(cell);
        
        font.setSize(11);
        paraHeader = new Paragraph("STRICTLY PRIVATE AND CONFIDENTIAL" , font);
		paraHeader.setIndentationRight(HEADER_PADDING_LEFT);
		paraHeader.setAlignment(Element.ALIGN_RIGHT);
		
        cell = new PdfPCell();
        cell.addElement(paraHeader);
        cell.setBorder(0);
        //cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        footerTable.addCell(cell);
        
		document.add(footerTable);
    }
    
    protected float createLogo(Document document,LogoDetails logoDetails,float spaceBeforeTable) throws DocumentException,IOException
    {
    	if(logoDetails != null && logoDetails.getPath()!=null && logoDetails.getPath().length() > 0)
		{
    		 Image logo = Image.getInstance(logoDetails.getPath());
    		 if(logoDetails.getScaleWidthBy() > 0.0f && logoDetails.getScaleHeightBy() > 0.0f)
    		 {
    			  logo.scalePercent(logoDetails.getScaleWidthBy(),logoDetails.getScaleHeightBy());
    		 }
    		 logo.setWidthPercentage(100);
    		 //logo.setAbsolutePosition(0,PAGE_HEIGHT);
    		 document.add(logo);
    		 if(logoDetails.getHeight() > 0.0f)
    		 {
    			 spaceBeforeTable = logoDetails.getHeight() + spaceBeforeTable;
    		 }
		}
    	return spaceBeforeTable;
    }
    
    protected float createPageHeader(Document document,String headerText,float spaceBeforeTable) throws DocumentException
    {
    	if(headerText != null && headerText.length() > 0)
		{
    		Font font = FontFactory.getFont(FontFactory.COURIER);
    		font.setColor(HEADER_TEXT_COLOR);
    		font.setStyle(Font.BOLD);
    		font.setSize(TITLE_FONT_SIZE);
    		Paragraph paraHeader = new Paragraph(headerText,font);
    		paraHeader.setAlignment(Element.ALIGN_CENTER);
			document.add(paraHeader);
			spaceBeforeTable = SPACE_BEFORE_TABLE + spaceBeforeTable;
		}
    	
    	return spaceBeforeTable;
    }
    
    protected Chapter createChapter(ExportDataDTO exportDataDTO,int chapterNumber,float spaceBeforeTable) throws DocumentException,IllegalAccessException
    {
    	Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(""));
		Chapter chapter = new Chapter(paragraph,chapterNumber);
        chapter.setNumberDepth(0);
        
    	PdfPTable table = createTable(exportDataDTO,spaceBeforeTable,chapter);
    	chapter.add(table);
        
        return chapter;
    }
    
    protected PdfPTable createTable(ExportDataDTO exportDataDTO,float spaceBeforeTable,Chapter chapter) throws DocumentException,IllegalAccessException
    {
    	PdfPTable table = new PdfPTable(this.getExportInfo().getColumns().size());
    	table.setWidthPercentage(100.0f);
		table.setSpacingBefore(spaceBeforeTable);
		table.setSpacingAfter(10.0f);
		table.getDefaultCell().setPadding(CELL_PADDING);
		//table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
		if(this.getExportInfo().isHeirarchicalData())
    	{
			createGroupedRow(table,exportDataDTO.getParent(),chapter);
    	}
		createTableHeader(table);
		createTableBody(table,exportDataDTO.getLstChildren());
		
		return table;
    }
    
    protected void createGroupedRow(PdfPTable table,Object groupedLabel,Chapter chapter)
    {
       // table.getDefaultCell().setColspan(this.getExportInfo().getColumns().size());
       // table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
       // table.getDefaultCell().setBackgroundColor(GROUPED_HEADER_BACKGROUND_COLOR);
    	String parentLabel = (groupedLabel == null)? "" :  groupedLabel.toString();
        if(chapter!=null)
        {
        	chapter.setBookmarkTitle(parentLabel);
        }
    	Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(GROUPED_HEADER_TEXT_COLOR);
		font.setStyle(Font.NORMAL);
		
		PdfPCell cell = new PdfPCell();
		cell.setPhrase(new Phrase(parentLabel,font));
		cell.setBackgroundColor(GROUPED_HEADER_BACKGROUND_COLOR);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setColspan(this.getExportInfo().getColumns().size());
		table.addCell(cell);
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
					font.setStyle(Font.STRIKETHRU);
					font.setSize(5);
					PdfPCell cell = new PdfPCell();
					cell.setPhrase(new Phrase(colValue,font));
					table.addCell(colValue);
		        }
	         }
		}
	}
	
	protected void setDocumentPrinterConfigurations(Document document)
	{
		document.setPageSize(PageSize.A4.rotate());
	}
	
	protected void setWriterPrinterConfigurations(PdfWriter writer)
	{
		writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
	}
	
	protected void buildPdfMetadata(Document document) 
	{
		
	}
}
