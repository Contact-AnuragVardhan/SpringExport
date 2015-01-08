package com.org.modelView.export.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.org.export.sample.model.BookDTO;
import com.org.export.util.PDFPageEventHandler;

public class PDFHeaderExample 
{
	 protected final float HEADER_PADDING_LEFT = 30.0f;
	 
	 public void createPdf(String filename) throws SQLException, DocumentException, IOException 
	 {
		 	String[] files = {"src/main/resources/assets/template.pdf"};
	    	// create a database connection
	        // step 1
	        Document document = new Document(PageSize.A4.rotate(), 0,0,0,0); //new Document(PageSize.A4.rotate());
	        
	        // step 2
	      // PdfCopy copyWriter = new PdfCopy(document, new FileOutputStream(filename));
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
	        PDFPageEventHandler event = new PDFPageEventHandler();
	        writer.setPageEvent(event);
	        
	        document.setMargins(0,0,0,0);
	        //document.setMarginMirroring(true);
	        
	        PdfReader reader;
	        document.open();
	        //PdfContentByte canvas = writer.getDirectContent();
	        // loop over the documents you want to concatenate
	       /* for (int count = 0; count < files.length; count++) 
	        {
	            reader = new PdfReader(files[count]);
	            // loop over the pages in that document
	            int totalPages = reader.getNumberOfPages();
	            //writer.setOutputIntents(reader, false)
	            PdfPTable table = new PdfPTable(1);
	            table.setWidthPercentage(100f);
	            String foobar = "I am here";
	            table.getDefaultCell().setBorder(0);
	            BaseFont bf_times = BaseFont.createFont(BaseFont.COURIER, "", BaseFont.EMBEDDED);
	            for (int pageCount = 0; pageCount < totalPages; ) 
	            {
	            	//document.
	            	//copyWriter.addPage(copyWriter.getImportedPage(reader, ++pageCount));
	            	document.newPage();
	            	document.setMargins(0,0,0,0);
	            	PdfImportedPage page =  writer.getImportedPage(reader, ++pageCount);
	         		PdfDocument pageDocument = page.getPdfDocument();
	         		PdfWriter pdfWriter = page.getPdfWriter();
	         		PdfContentByte canvas = pdfWriter.getDirectContent();
	         		//pageDocument.add(paraHeader);
	         		//pageWriter.add(paraHeader);
	         		
	         		canvas.beginText();
	         		canvas.setFontAndSize(bf_times, 20);
	         		canvas.showTextAligned(Element.ALIGN_CENTER,"Test 123",5 ,5, 0);
	         		canvas.endText();
	         		//canvas.saveState();
	         		//canvas.restoreState();
	         		
	         	   //canvas.saveState();
	         		//canvas.restoreState();
	         		
	            	
	            	//cb.add(reader.getPageContent(page));
	            	//cb.addTemplate(page,1,1);
	            	table.addCell(Image.getInstance(page));
	                
	            }
	            document.add(table);
	            //copyWriter.freeReader(reader);
	            //reader.close();
	        }
	        document.newPage();*/
	        // step 3
	        //(this.getClass().getResource("/config/ModulesInfo.xml").getPath()
	        Image logo = Image.getInstance("src/main/resources/assets/logo2.png");
	        logo.scalePercent(73.0f,70.0f);
	        logo.setWidthPercentage(100);
	        float logoY = document.getPageSize().getHeight();
	        float spaceBeforeTable = logo.getHeight();
	        float scalerWidth = ((document.getPageSize().getWidth() - document.leftMargin()
	        		- document.rightMargin() ) / (logo.getWidth()+10)) * 100;
	        float pageHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
	        float scalerHeight = ((document.getPageSize().getHeight() - document.topMargin()
	        		- document.bottomMargin() ) / (logo.getHeight()+10)) * 100;
	        
	        //logo.setAbsolutePosition(0,500);
	        //logo.setAbsolutePosition(0,200);




//	        logo.setAbsolutePosition(
//	        		(PageSize.POSTCARD.getWidth() - logo.getScaledWidth()) / 2,
//	                (PageSize.POSTCARD.getHeight() - logo.getScaledHeight()) / 2);
	        
	        document.add(logo);
	        
	        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    		font.setColor(BaseColor.BLACK);
    		font.setStyle(Font.BOLD);
    		font.setSize(24);
    		Paragraph paraHeader = new Paragraph("Evidence of Review",font);
    		paraHeader.setSpacingBefore(40);
    		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
    		paraHeader.setAlignment(Element.ALIGN_LEFT);
			document.add(paraHeader);
			
			font.setSize(18);
    		paraHeader = new Paragraph("Nomura Surveillance System - 2",font);
    		paraHeader.setSpacingBefore(20);
    		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
    		paraHeader.setAlignment(Element.ALIGN_LEFT);
			document.add(paraHeader);
			
			font.setSize(14);
			font.setStyle(Font.NORMAL);
			String searchCriteria = "From : " + "08/01/2014";
    		paraHeader = new Paragraph(searchCriteria , font);
    		paraHeader.setSpacingBefore(20);
    		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
    		paraHeader.setAlignment(Element.ALIGN_LEFT);
			document.add(paraHeader);
			
			searchCriteria = "To : " + "08/31/2014";
    		paraHeader = new Paragraph(searchCriteria , font);
    		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
    		paraHeader.setAlignment(Element.ALIGN_LEFT);
			document.add(paraHeader);
			
			/*paraHeader = new Paragraph("Generated: 01/06/2015" , font);
    		paraHeader.setIndentationLeft(HEADER_PADDING_LEFT);
    		paraHeader.setAlignment(Element.ALIGN_LEFT);
			document.add(paraHeader);*/
			
			PdfPTable footerTable = new PdfPTable(2);
			footerTable.setWidthPercentage(100f);
			footerTable.setSpacingBefore(100);
			footerTable.getDefaultCell().setBorder(0);
			
			
			paraHeader = new Paragraph("Generated: 01/06/2015" , font);
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
			
			 
			document.setMargins(2,2,2,2);
			 
			document.newPage();
	        // step 4
	        List<Date> days = new ArrayList<Date>();
	        for(int count = 1;count < 10; count++)
	        {
	        	Date today = new Date(2014, 12, 18);
	        	days.add(new Date(today.getTime() + (1000 * 60 * 60 * 24 * count)));
	        }
	        int chapCount = 1;
	        for (Date day : days)
	        {
	        	Paragraph paragraph = new Paragraph();
	            paragraph.add(new Phrase(""));
	        	PdfPTable table = getTable(day);
	        	Chapter chapter = new Chapter(paragraph,chapCount++);
	        	chapter.setNumberDepth(0);
	        	chapter.setBookmarkTitle(day.toString());
	        	//Section section = chapter.addSection(day.toString());
	        	chapter.add(table);
	        	table.setSpacingBefore(30.0f);
	        	document.add(chapter);
	            document.newPage();
	        }
	        // step 5
	        document.close();
	 
	  }
	 
	    /**
	     * Creates a table with screenings.
	     * @param connection the database connection
	     * @param day a film festival day
	     * @return a table with screenings
	     * @throws SQLException
	     * @throws DocumentException
	     * @throws IOException
	     */
	    public PdfPTable getTable(Date day)
	        throws SQLException, DocumentException, IOException {
	    	// Create a table with 7 columns
	    	PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
	        table.setWidthPercentage(100f);
	        table.getDefaultCell().setUseAscender(true);
	        table.getDefaultCell().setUseDescender(true);
	        // Add the first header row
	        Font f = new Font();
	        f.setColor(BaseColor.WHITE);
	        PdfPCell cell = new PdfPCell(new Phrase(day.toString(), f));
	        cell.setBackgroundColor(BaseColor.RED);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setColspan(5);
	        table.addCell(cell);
	        // Add the second header row twice
	        table.getDefaultCell().setBackgroundColor(BaseColor.RED);
	        for (int i = 0; i < 1; i++) {
	            table.addCell("Title");
	            table.addCell("Author");
	            table.addCell("ISBN");
	            table.addCell("Published Date");
	            table.addCell("Price");
	        }
	        table.getDefaultCell().setBackgroundColor(null);
	        // There are three special rows
	        table.setHeaderRows(2);
	        // One of them is a footer
	        //table.setFooterRows(1);
	        // Now let's loop over the screenings
	        List<BookDTO> listBooks = getBooksDTO();
	       /* Movie movie;
	        for (Screening screening : screenings) {
	            movie = screening.getMovie();
	            table.addCell(screening.getLocation());
	            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
	            table.addCell(String.format("%d '", movie.getDuration()));
	            table.addCell(movie.getMovieTitle());
	            table.addCell(String.valueOf(movie.getYear()));
	            cell = new PdfPCell();
	            cell.setUseAscender(true);
	            cell.setUseDescender(true);
	            cell.addElement(PojoToElementFactory.getDirectorList(movie));
	            table.addCell(cell);
	            cell = new PdfPCell();
	            cell.setUseAscender(true);
	            cell.setUseDescender(true);
	            cell.addElement(PojoToElementFactory.getCountryList(movie));
	            table.addCell(cell);
	        }*/
	        for (BookDTO book : listBooks) {
	            table.addCell(book.getTitle());
	            table.addCell(book.getAuthor());
	            table.addCell(book.getIsbn());
	            table.addCell(book.getPublishedDate());
	            table.addCell(String.valueOf(book.getPrice()));
	        }
	        return table;
	    }
	    
	    private List<BookDTO> getBooksDTO()
		{
			List<BookDTO> listBooks = new ArrayList<BookDTO>();
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));  listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	    	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	    	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	    	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Effective Java Effective Java Effective Java Effective Java Effective Java", "Joshua Bloch", "0321356683",
	                "May 28, 2008", 38.11F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
	        listBooks.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
	                "0596009208", "February 9, 2005", 30.80F));
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
	    
	    public static void main(String[] args)
	        throws SQLException, DocumentException, IOException {
	        new PDFHeaderExample().createPdf("header.pdf");
	    }


}
