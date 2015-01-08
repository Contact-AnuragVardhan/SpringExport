package com.org.modelView.export.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
 



import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieHistory2 {
	
    /** The resulting PDF file. */
    public static final String RESULT
        = "header.pdf";
 
    /** Inner class to add a header and a footer. */
    class HeaderFooter extends PdfPageEventHelper {
        /** Alternating phrase for the header. */
        Phrase[] header = new Phrase[2];
        /** Current page number (will be reset for every chapter). */
        int pagenumber;
 
        /**
         * Initialize one of the headers.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("Header 1");
        }
 
        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         *      com.itextpdf.text.Paragraph)
         */
        public void onChapter(PdfWriter writer, Document document,
                float paragraphPosition, Paragraph title) {
            header[1] = new Phrase("Header 2");
            pagenumber = 1;
        }
 
        /**
         * Increase the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }
 
        /**
         * Adds the header and the footer.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            switch(writer.getPageNumber() % 2) {
            case 0:
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_RIGHT, header[0],
                        rect.getRight(), rect.getTop(), 0);
                break;
            case 1:
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_LEFT, header[1],
                        rect.getLeft(), rect.getTop(), 0);
                break;
            }
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("page %d", pagenumber)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        }
    }
 
    /** The different epochs. */
    public static final String[] EPOCH =
        { "Forties", "Fifties", "Sixties", "Seventies", "Eighties",
    	  "Nineties", "Twenty-first Century" };
    /** The fonts for the title. */
    public static final Font[] FONT = new Font[4];
    static {
        FONT[0] = new Font(FontFamily.HELVETICA, 24);
        FONT[1] = new Font(FontFamily.HELVETICA, 18);
        FONT[2] = new Font(FontFamily.HELVETICA, 14);
        FONT[3] = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
    }
 
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// Create a database connection
       // DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document(PageSize.A4, 36, 36, 54, 54);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        HeaderFooter event = new HeaderFooter();
        writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        writer.setPageEvent(event);
        // step 3
        document.open();
        // step 4
        
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(BaseColor.BLACK);
		font.setStyle(Font.BOLD);
		font.setSize(24);
		Paragraph paraHeader = new Paragraph("Evidence of Review",font);
		paraHeader.setSpacingBefore(40);
		paraHeader.setIndentationLeft(20);
		paraHeader.setAlignment(Element.ALIGN_LEFT);
		document.add(paraHeader);
		document.newPage();
		
		font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(BaseColor.BLACK);
		font.setStyle(Font.BOLD);
		font.setSize(24);
		paraHeader = new Paragraph("Evidence of Review",font);
		paraHeader.setSpacingBefore(40);
		paraHeader.setIndentationLeft(20);
		paraHeader.setAlignment(Element.ALIGN_LEFT);
		document.add(paraHeader);
		document.newPage();
		
		font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(BaseColor.BLACK);
		font.setStyle(Font.BOLD);
		font.setSize(24);
		paraHeader = new Paragraph("Evidence of Review",font);
		paraHeader.setSpacingBefore(40);
		paraHeader.setIndentationLeft(20);
		paraHeader.setAlignment(Element.ALIGN_LEFT);
		document.add(paraHeader);
		document.newPage();
        // step 5
        document.close();
    }
 
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        new MovieHistory2().createPdf(RESULT);
    }

}
