package com.org.export.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFPageEventHandler extends PdfPageEventHelper 
{
	public boolean isPageNumberNeeded = true;
	public boolean startPageNumber = true;
	
	protected int pageNumber = 1;
	
	
	@Override
	public void onOpenDocument(PdfWriter writer,Document document) 
	{
		
    }
	
	@Override
	public void onStartPage(PdfWriter writer,Document document) 
	{
		if(isPageNumberNeeded && startPageNumber)
		{
			pageNumber++;
		}
	}
	
	@Override
	public void onEndPage(PdfWriter writer,Document document) 
	{
        /* switch(writer.getPageNumber() % 2) {
         case 0:
             ColumnText.showTextAligned(writer.getDirectContent(),
                     Element.ALIGN_RIGHT, new Phrase("Header 1"),
                     rect.getRight(), rect.getTop(), 0);
             break;
         case 1:
             ColumnText.showTextAligned(writer.getDirectContent(),
                     Element.ALIGN_LEFT, new Phrase("Header 2"),
                     rect.getLeft(), rect.getTop(), 0);
             break;
         }*/
		if(isPageNumberNeeded && startPageNumber)
		{
			ColumnText.showTextAligned(writer.getDirectContent(),
                 Element.ALIGN_CENTER, new Phrase(String.format("Page %d", pageNumber)),
                 (document.left() + document.right()) / 2, document.bottomMargin() - 10, 0);
		}
	}
	
	@Override
	public void onCloseDocument(PdfWriter writer,Document document) 
	{
	}
	 
	@Override
	public void onParagraph(PdfWriter writer,Document document,float paragraphPosition) 
	{
		
	}
	
	@Override
	public void onParagraphEnd(PdfWriter writer,Document document,float paragraphPosition) 
	{
	}
	
	@Override
	public void onChapter(PdfWriter writer,Document document,float paragraphPosition,Paragraph title) 
	{
		startPageNumber = true;
	}
	
	@Override
	public void onChapterEnd(PdfWriter writer,Document document,float position) 
	{
	}
	
	@Override
	public void onSection(PdfWriter writer,Document document,float paragraphPosition,int depth,Paragraph title) 
	{
	}
	
	@Override
	public void onSectionEnd(PdfWriter writer,Document document,float position) 
	{
	}
	
	@Override
	public void onGenericTag(PdfWriter writer,Document document,Rectangle rect,String text) 
	{
	}
}
