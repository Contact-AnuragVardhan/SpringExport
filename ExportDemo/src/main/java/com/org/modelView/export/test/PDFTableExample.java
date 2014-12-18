package com.org.modelView.export.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
 


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.org.modelView.export.sample.model.BookDTO;

public class PDFTableExample implements PdfPTableEvent 
{
	public static final String RESULT = "alternating.pdf";
	
	public void createPdf(String filename) throws DocumentException, IOException {
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        List<Date> days = new ArrayList<Date>();
        for(int count = 1;count < 10; count++)
        {
        	Date today = new Date(2014, 12, 18);
        	days.add(new Date(today.getTime() + (1000 * 60 * 60 * 24 * count)));
        }
        PdfPTableEvent event = new PDFTableExample();
        for (Date day : days) {
            PdfPTable table = getTable(day);
            table.setTableEvent(event);
            document.add(table);
            document.newPage();
        }
        // step 5
        document.close();
        // Close the database connection
    }
	
	public PdfPTable getTable(Date day)
	        throws DocumentException, IOException {
	        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
	        table.setWidthPercentage(100f);
	        table.getDefaultCell().setPadding(3);
	        table.getDefaultCell().setUseAscender(true);
	        table.getDefaultCell().setUseDescender(true);
	        table.getDefaultCell().setColspan(5);
	        table.getDefaultCell().setBackgroundColor(BaseColor.RED);
	        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(day.toString());
	        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.getDefaultCell().setColspan(1);
	        table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
	        for (int i = 0; i < 2; i++) {
	            table.addCell("title");
	            table.addCell("author");
	            table.addCell("isbn");
	            table.addCell("publishedDate");
	            table.addCell("price");
	        }
	        table.getDefaultCell().setBackgroundColor(null);
	        table.setHeaderRows(3);
	        table.setFooterRows(1);
	        List<BookDTO> listBooks = getBooksDTO();
	        for (BookDTO book : listBooks) {
	            table.addCell(book.getTitle());
	            table.addCell(book.getAuthor());
	            table.addCell(book.getIsbn());
	            table.addCell(book.getPublishedDate());
	            table.addCell(String.valueOf(book.getPrice()));
	        }
	        return table;
	    }
	
	public void tableLayout(PdfPTable table, float[][] widths, float[] heights,
	        int headerRows, int rowStart, PdfContentByte[] canvases) {
	        int columns;
	        Rectangle rect;
	        int footer = widths.length - table.getFooterRows();
	        int header = table.getHeaderRows() - table.getFooterRows() + 1;
	        for (int row = header; row < footer; row += 2) {
	            columns = widths[row].length - 1;
	            rect = new Rectangle(widths[row][0], heights[row],
	                        widths[row][columns], heights[row + 1]);
	            rect.setBackgroundColor(BaseColor.YELLOW);
	            rect.setBorder(Rectangle.NO_BORDER);
	            canvases[PdfPTable.BASECANVAS].rectangle(rect);
	        }
	    }
	
	private List<BookDTO> getBooksDTO()
	{
		List<BookDTO> listBooks = new ArrayList<BookDTO>();
        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
                "May 28, 2008", 38.11F));
        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
                "0596009208", "February 9, 2005", 30.80F));
        listBooks.add(new BookDTO("Java Generics and Collections",
                "Philip Wadler", "0596527756", "Oct 24, 2006", 29.52F));
        listBooks.add(new BookDTO("Thinking in Java", "Bruce Eckel", "0596527756",
                "February 20, 2006", 43.97F));
        listBooks.add(new BookDTO("Spring in Action", "Craig Walls", "1935182358",
                "June 29, 2011", 31.98F));
        
        return listBooks;
	}




	public static void main(String[] args) throws Exception
	{
		 new PDFTableExample().createPdf(RESULT);

	}

}
