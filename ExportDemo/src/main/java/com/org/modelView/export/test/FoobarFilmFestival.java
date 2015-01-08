package com.org.modelView.export.test;

import java.io.FileOutputStream;
import java.io.IOException;
 
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class FoobarFilmFestival {
	
    public static final String RESULT
    = "template.pdf";

/**
 * Main method.
 *
 * @param    args    no arguments needed
 * @throws DocumentException 
 * @throws IOException
 */
public static void main(String[] args)
    throws IOException, DocumentException {
	// step 1
    Document document = new Document();
    // step 2
    PdfWriter writer
        = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
    // step 3
    document.open();
    // step 4
    Chunk c;
    String foobar = "Foobar Film Festival";
    // Measuring a String in Helvetica
    Font helvetica = new Font(FontFamily.HELVETICA, 12);
    BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);
 
    // Measuring a String in Times
    BaseFont bf_times = BaseFont.createFont(BaseFont.COURIER, "", BaseFont.EMBEDDED);
    Font times = new Font(bf_times, 12);
   
   
    // Drawing lines to see where the text is added
    PdfContentByte canvas = writer.getDirectContent();
    /*canvas.beginText();
    canvas.setFontAndSize(bf_helv, 12);
    canvas.showTextAligned(Element.ALIGN_LEFT, foobar, 400, 788, 0);
    canvas.showTextAligned(Element.ALIGN_RIGHT, foobar, 400, 752, 0);
    canvas.showTextAligned(Element.ALIGN_CENTER, foobar, 400, 716, 0);
    canvas.showTextAligned(Element.ALIGN_CENTER, foobar, 400, 680, 30);
    canvas.showTextAlignedKerned(Element.ALIGN_LEFT, foobar, 400, 644, 0);
    canvas.endText();*/
    Phrase phrase = new Phrase(foobar, times);
    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 200, 572, 0);
    ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase, 200, 536, 0);
    ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 200, 500, 0);
    ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 200, 464, 30);
    ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 200, 428, -30);
    // Chunk attributes
   /* c = new Chunk(foobar, times);
    c.setHorizontalScaling(0.5f);
    phrase = new Phrase(c);
    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 572, 0);
    c = new Chunk(foobar, times);
    c.setSkew(15, 15);
    phrase = new Phrase(c);
    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 536, 0);
    c = new Chunk(foobar, times);
    c.setSkew(0, 25);
    phrase = new Phrase(c);
    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 500, 0);
    c = new Chunk(foobar, times);
    c.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_STROKE, 0.1f, BaseColor.RED);
    phrase = new Phrase(c);
    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 464, 0);
    c = new Chunk(foobar, times);
    c.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 1, null);
    phrase = new Phrase(c);
    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 428, -0);*/
    // step 5
    document.close();
}

}
