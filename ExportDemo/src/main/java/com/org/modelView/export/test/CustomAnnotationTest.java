package com.org.modelView.export.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.org.modelView.export.annotation.ExportInfo;
import com.org.modelView.export.interfaces.IExport;
import com.org.modelView.export.sample.model.BookDTO;

public class CustomAnnotationTest 
{

	public static void main(String[] args) 
	{
		// create some sample data
        List<IExport> lstExports = new ArrayList<IExport>();
        lstExports.add(new BookDTO("Effective Java", "Joshua Bloch", "0321356683",
                "May 28, 2008", 38.11F));
        lstExports.add(new BookDTO("Head First Java", "Kathy Sierra & Bert Bates",
                "0596009208", "February 9, 2005", 30.80F));
        lstExports.add(new BookDTO("Java Generics and Collections",
                "Philip Wadler", "0596527756", "Oct 24, 2006", 29.52F));
        lstExports.add(new BookDTO("Thinking in Java", "Bruce Eckel", "0596527756",
                "February 20, 2006", 43.97F));
        lstExports.add(new BookDTO("Spring in Action", "Craig Walls", "1935182358",
                "June 29, 2011", 31.98F));
        
        if(lstExports!=null && lstExports.size() > 0)
        {
        	IExport exportClass = lstExports.get(0);
        	if(exportClass != null)
        	{
        		Field[] fields = exportClass.getClass().getDeclaredFields();
             	for(Field field:fields)
             	{
             		field.setAccessible(true);
             		if(field.isAnnotationPresent(ExportInfo.class))
             		{
             			ExportInfo exportInfo = field.getAnnotation(ExportInfo.class);
             			if(field!= null)
             			{
             				System.out.println("headerName:: " + exportInfo.headerText() + " orderId:: " + exportInfo.order() + " width:: " + exportInfo.width());
             			}
             		}
             	}
        	}
        	try
        	{
	        	for(IExport export:lstExports)
	            {
	             	Field[] fields = export.getClass().getDeclaredFields();
	             	for(Field field:fields)
	             	{
	             		field.setAccessible(true);
	             		if(field.isAnnotationPresent(ExportInfo.class))
	             		{
	             			ExportInfo exportInfo = field.getAnnotation(ExportInfo.class);
	             			if(field!= null)
	             			{
	             				System.out.println("Field Name " + field.getName() + " Field Value " + field.get(export));
	             			}
	             		}
	             	}
	             }
        	}
        	catch(IllegalAccessException exception)
        	{
        		exception.printStackTrace();
        	}
        }

	}

}
