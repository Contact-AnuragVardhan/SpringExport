package com.org.export.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.org.export.interfaces.IExport;
import com.org.export.model.Book;
import com.org.export.model.ExportDTO;


@Controller
public class PDFExportController
{
	/**
	 * Handle request to the default page
	 */
	@RequestMapping(value = "/exportPDF", method = RequestMethod.GET)
	public String viewHome() 
	{
		System.out.println("In View Home");
		return "export";
	}

	
	/**
	 * Handle request to download a PDF document 
	 */
	@RequestMapping(value = "/downloadPDF", method = RequestMethod.GET)
	public ModelAndView downloadPDF() 
	{
		// create some sample data
		List<IExport> listBooks = new ArrayList<IExport>();
		listBooks.add(new Book("Effective Java Effective Java Effective Java Effective Java Effective Java", "Craig Walls", "1935182358",
				"June 29th 2011", 31.98F));
		listBooks.add(new Book("Spring in Practice", "Willie Wheeler, Joshua White",
				"1935182056", "May 16th 2013", 31.95F));
		listBooks.add(new Book("Pro Spring 3",
				"Clarence Ho, Rob Harrop", "1430241071", "April 18th 2012", 31.85F));
		listBooks.add(new Book("Spring Integration in Action", "Mark Fisher", "1935182439",
				"September 26th 2012", 28.73F));
		
		ExportDTO export = new ExportDTO();
		export.setFileName("Book Record");
        export.setExcelSheetName("Book Record");
        export.setLstExports(listBooks);
        export.setHeaderText("Recommended books for Spring Framework");

		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("pdfView", "exportInfo", export);
	}

}
