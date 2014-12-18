package com.org.modelView.export.sample.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.org.modelView.export.interfaces.IExport;
import com.org.modelView.export.model.ExportDTO;
import com.org.modelView.export.sample.model.BookDTO;

@Controller
public class SampleExportController 
{
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String getExportJSP() 
	{
		System.out.println("In getExportJSP");
		return "export";
	}
	
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	public ModelAndView exportToExcel()
	{
		ExportDTO export = getExportDTO();
		return new ModelAndView("excelView", "exportInfo", export);
	}
	
	@RequestMapping(value = "/downloadPDF", method = RequestMethod.GET)
	public ModelAndView exportToPDF()
	{
		ExportDTO export = getExportDTO();
		return new ModelAndView("pdfView", "exportInfo", export);
	}
	
	private ExportDTO getExportDTO()
	{
		List<IExport> listBooks = new ArrayList<IExport>();
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
        
        ExportDTO export = new ExportDTO();
		export.setFileName("Book Record");
        export.setExcelSheetName("Book Record");
        export.setLstExports(listBooks);
        export.setHeaderText("Recommended books for Spring Framework");

        return export;
	}
}
