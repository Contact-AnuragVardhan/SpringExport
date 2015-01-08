package com.org.export.sample.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.export.enums.ExportType;
import com.org.export.interfaces.IExport;
import com.org.export.model.ExportDTO;
import com.org.export.model.ExportDataDTO;
import com.org.export.model.ExportMetaData;
import com.org.export.model.GridColumnInfo;
import com.org.export.model.LogoDetails;
import com.org.export.sample.model.BookDTO;
import com.org.export.util.DataSourceUtil;

@Controller
public class SampleExportController 
{
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String getExportJSP() 
	{
		System.out.println("In getExportJSP");
		return "export";
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException,IOException,ServletException
	{
		ExportDTO export = getExportDTO();
		
		List<GridColumnInfo> columns = DataSourceUtil.getColumnListInfo(export.getLstExports());
		List<ExportDataDTO> dataProvider = null;
		if(export.isHeirarchicalData())
		{
			dataProvider = DataSourceUtil.getGroupedData(export.getLstExports(), columns, export.getParentColumn());
		}
		else
		{
			dataProvider = DataSourceUtil.getNonGroupedData(export.getLstExports(), columns);
		}
		
		ExportMetaData pdfExportMetaData = new ExportMetaData();
		
		pdfExportMetaData.setExportType(export.getExportType());
		pdfExportMetaData.setColumns(columns);
		pdfExportMetaData.setFileName(export.getFileName());
		pdfExportMetaData.setSheetName(export.getExcelSheetName());
		pdfExportMetaData.setHeaderText(export.getHeaderText());
		pdfExportMetaData.setIsHeirarchicalData(export.isHeirarchicalData());
		pdfExportMetaData.setParentColumn(export.getParentColumn());
		pdfExportMetaData.setColumns(columns);
		pdfExportMetaData.setDataProvider(dataProvider);
		pdfExportMetaData.setFileImportPaths(export.getFileImportPaths());
		pdfExportMetaData.setLogoDetails(export.getLogoDetails());
		
		request.setAttribute("exportData", pdfExportMetaData);
		RequestDispatcher rd = request.getRequestDispatcher("exportDoc");
		rd.forward(request,response);
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
        export.setExportType(ExportType.PDF);
		export.setFileName("Book Record");
        export.setExcelSheetName("Book Record");
        export.setLstExports(listBooks);
        export.setHeaderText("Recommended books for Spring Framework");
        export.setHeirarchicalData(true);
        export.setParentColumn(new GridColumnInfo("isbn","IsBN", 1, 30.0f, 3.0f, false));
        
        LogoDetails logoDetails = new LogoDetails();
		logoDetails.setPath(this.getClass().getResource("/assets/logo2.png").getPath());
		logoDetails.setScaleWidthBy(73.0f);
		logoDetails.setScaleHeightBy(70.0f);
		logoDetails.setHeight(40.0f);
		export.setLogoDetails(logoDetails);

        return export;
	}
}
