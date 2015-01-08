package com.org.modelView.export.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

public class PDFCopyExample {

	public static void main(String[] args) throws IOException, DocumentException, SQLException {
        // using previous examples to create PDFs
    	//MovieLinks1.main(args);
        //MovieHistory.main(args);
		String[] files = {"src/main/resources/assets/template.pdf"};
        // step 1
        Document document = new Document(PageSize.A4.rotate(), 0, 0, 0, 0);
        // step 2
        PdfCopy copy = new PdfCopy(document, new FileOutputStream("header.pdf"));
        // step 3
        document.open();
        // step 4
        PdfReader reader;
        int n;
        // loop over the documents you want to concatenate
        for (int i = 0; i < files.length; i++) {
            reader = new PdfReader(files[i]);
            // loop over the pages in that document
            n = reader.getNumberOfPages();
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(reader, ++page));
            }
            copy.freeReader(reader);
            reader.close();
        }
        // step 5
        document.close();
    }


}
