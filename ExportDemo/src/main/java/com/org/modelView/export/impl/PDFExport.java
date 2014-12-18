package com.org.modelView.export.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.org.modelView.export.annotation.ExportInfo;
import com.org.modelView.export.base.AbstractPdfView;
import com.org.modelView.export.interfaces.IExport;
import com.org.modelView.export.model.ExportDTO;

public class PDFExport extends AbstractPdfView
{
	protected final float SPACE_BEFORE_TABLE = 5.0f;
	protected final int CELL_PADDING = 5;
	protected final float DEFAULT_RELATIVE_WIDTH = 3.0f;
	protected List<Field> lstAnnotatedFields = null;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model,Document document, PdfWriter writer, HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		// get data model which is passed by the Spring container
		ExportDTO export = (ExportDTO) model.get("exportInfo");
		if(export != null)
		{
			float spaceBeforeTable = 0.0f;
			this.setFileName(export.getFileName());
			List<IExport> lstExports = export.getLstExports();
			if(export.getHeaderText() != null && export.getHeaderText().length() > 0)
			{
				document.add(new Paragraph(export.getHeaderText()));
				spaceBeforeTable = SPACE_BEFORE_TABLE;
			}
			getAnnotatedField(lstExports);
			PdfPTable table = new PdfPTable(lstAnnotatedFields.size());
			table.setWidthPercentage(100.0f);
			table.setSpacingBefore(spaceBeforeTable);
			createHeader(table);
			createBody(table,lstExports);
			document.add(table);
		}
	}
	
	protected void getAnnotatedField(List<IExport> lstExport)
	{
		lstAnnotatedFields = new ArrayList<Field>();
		if(lstExport!=null && lstExport.size() > 0)
    	{
        	IExport exportClass = lstExport.get(0);
        	if(exportClass != null)
        	{
        		Field[] fields = exportClass.getClass().getDeclaredFields();
	         	for(int count = 0; count < fields.length; count++)
	         	{
	         		Field field = fields[count];
	         		field.setAccessible(true);
	         		if(field.isAnnotationPresent(ExportInfo.class))
	         		{
	         			lstAnnotatedFields.add(field);
	         		}
	         	}
        	}
    	}
	}
	
	protected void createHeader(PdfPTable table) throws DocumentException 
	{
		float[] arrColWidth = new float[lstAnnotatedFields.size()];
		//List<Float> lstColWidth = new ArrayList<Float>();
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setPadding(CELL_PADDING);
		
		for(int count = 0; count < lstAnnotatedFields.size(); count++)
     	{
     		Field field = lstAnnotatedFields.get(count);
 			ExportInfo exportInfo = field.getAnnotation(ExportInfo.class);
 			cell.setPhrase(new Phrase(exportInfo.headerText(), font));
 			if(exportInfo.relativeWidth() > -1.0f)
 			{
 				arrColWidth[count] = exportInfo.relativeWidth();
 				//lstColWidth.add(exportInfo.relativeWidth());
 			}
 			else
 			{
 				arrColWidth[count] = DEFAULT_RELATIVE_WIDTH;
 				//lstColWidth.add(DEFAULT_RELATIVE_WIDTH);
 			}
 			table.addCell(cell);
     	}
		//float[] arrColWidth = ArrayUtils.toPrimitive(lstColWidth.toArray(new Float[0]));
		table.setWidths(arrColWidth);
	}
	
	protected void createBody(PdfPTable table,List<IExport> lstExport) throws IllegalAccessException
	{
		if(lstExport != null && lstExport.size() > 0)
		{
			for(int rowCount = 0;rowCount < lstExport.size(); rowCount++)
         	{
				IExport export = lstExport.get(rowCount);
				for(int colCount = 0; colCount < lstAnnotatedFields.size(); colCount++)
             	{
					Field field = lstAnnotatedFields.get(colCount);
					String colValue = ((field.get(export) == null) ? "" : field.get(export).toString());
					table.addCell(colValue);
             	}
         	}
		}
	}

}
