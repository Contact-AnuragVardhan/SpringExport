package com.org.export.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.org.export.annotation.ExportInfo;
import com.org.export.interfaces.IExport;
import com.org.export.model.ExportDataDTO;
import com.org.export.model.GridColumnInfo;

public class DataSourceUtil 
{
	public static List<GridColumnInfo> getColumnListInfo(List<? extends IExport> lstExport)
	{
		List<GridColumnInfo> lstColumnInfo = new ArrayList<GridColumnInfo>();
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
	         			ExportInfo exportInfo = field.getAnnotation(ExportInfo.class);
	         			GridColumnInfo columnInfo = getColumnInfo(field.getName(),exportInfo);
	         			lstColumnInfo.add(columnInfo);
	         		}
	         	}
        	}
    	}
		
		return lstColumnInfo;
	}
		
	public static GridColumnInfo getColumnInfo(String fieldName,ExportInfo exportInfo)
	{
		GridColumnInfo columnInfo = new GridColumnInfo();
		if(exportInfo != null)
		{
			columnInfo.setDataField(fieldName);
			columnInfo.setHeaderText(exportInfo.headerText());
			columnInfo.setOrder(exportInfo.order());
			columnInfo.setWidth(exportInfo.width());
			columnInfo.setRelativeWidth(exportInfo.relativeWidth());
			columnInfo.setWordWrap(exportInfo.wordWrap());
		}
		
		return columnInfo;
	}
	
	public static List<ExportDataDTO> getGroupedData(List<? extends IExport> lstExport,List<GridColumnInfo> columns,GridColumnInfo parentColumn) throws IllegalAccessException
	{
		List<ExportDataDTO> lstExportData = null;
		if(lstExport!=null && lstExport.size() > 0 && columns!=null && columns.size() > 0 && parentColumn!=null)
		{
			lstExportData = new ArrayList<ExportDataDTO>();
			Field parentField = null;
			IExport exportClass = lstExport.get(0);
        	if(exportClass != null)
        	{
        		Field[] fields = exportClass.getClass().getDeclaredFields();
	         	for(int count = 0; count < fields.length; count++)
	         	{
	         		Field field = fields[count];
	         		field.setAccessible(true);
	         		if(field.isAnnotationPresent(ExportInfo.class) && field.getName().equals(parentColumn.getDataField()))
	         		{
         				parentField = field;
         				break;
	         		}
	         	}
	         	if(parentField == null)
	         	{
	         		throw new IllegalAccessException("The Parent Column is not present or not Annotated with annotation ExportInfo Class");
	         	}
	         	Object lastParentValue = null;
	         	ExportDataDTO exportData = null;
	         	for(int rowCount = 0;rowCount < lstExport.size(); rowCount++)
	         	{
	        		IExport export = lstExport.get(rowCount);
	        		if(parentField.get(export) != null)
					{
						Map<String,Object> objData = getRowData(export);
						if(parentField.get(export).equals(lastParentValue))
						{
							exportData.getLstChildren().add(objData);
						}
						else
						{
							exportData = new ExportDataDTO();
							exportData.setParent(parentField.get(export));
							exportData.setLstChildren(new ArrayList<Map<String,Object>>());
							exportData.getLstChildren().add(objData);
							lstExportData.add(exportData);
							lastParentValue = parentField.get(export);
						}
					}
	             }
        	}
		}
		return lstExportData;
	}
	
	public static List<ExportDataDTO> getNonGroupedData(List<? extends IExport> lstExport,List<GridColumnInfo> columns) throws IllegalAccessException
	{
		List<ExportDataDTO> lstExportData = null;
		if(lstExport!=null && lstExport.size() > 0 && columns!=null && columns.size() > 0)
		{
			IExport exportClass = lstExport.get(0);
        	if(exportClass != null)
        	{
        		lstExportData = new ArrayList<ExportDataDTO>();
    			ExportDataDTO exportData =  new ExportDataDTO();
    			exportData.setLstChildren(new ArrayList<Map<String,Object>>());
	         	for(int rowCount = 0;rowCount < lstExport.size(); rowCount++)
	         	{
	        		IExport export = lstExport.get(rowCount);
	        		Map<String,Object> objData = getRowData(export);
	        		exportData.getLstChildren().add(objData);
	         	}
    			lstExportData.add(exportData);
        	}
		}
		return lstExportData;
	}
	
	protected static Map<String,Object> getRowData(IExport exportInfo) throws IllegalAccessException
	{
		Map<String,Object> objData = new HashMap<String,Object>();
		Field[] fields = exportInfo.getClass().getDeclaredFields();
		for(Field field:fields)
		{
			field.setAccessible(true);
			objData.put(field.getName(),field.get(exportInfo));
		}
		return objData;
	}
	
	public static List<ExportDataDTO> getGroupedData(ResultSet resultSet,List<GridColumnInfo> columns,GridColumnInfo parentColumn) throws SQLException
	{
		List<ExportDataDTO> lstExportData = null;
		if(resultSet!=null && columns!=null && columns.size() > 0 && parentColumn!=null)
		{
			lstExportData = new ArrayList<ExportDataDTO>();
			Object lastParentValue = null;
			ExportDataDTO exportData = null;
			while (resultSet.next()) 
			{
				if(resultSet.getObject(parentColumn.getDataField()) != null)
				{
					Map<String,Object> objData = getRowData(resultSet,columns);
					if(resultSet.getObject(parentColumn.getDataField()).equals(lastParentValue))
					{
						exportData.getLstChildren().add(objData);
					}
					else
					{
						exportData = new ExportDataDTO();
						exportData.setParent(resultSet.getObject(parentColumn.getDataField()));
						exportData.setLstChildren(new ArrayList<Map<String,Object>>());
						exportData.getLstChildren().add(objData);
						lstExportData.add(exportData);
						lastParentValue = resultSet.getObject(parentColumn.getDataField());
					}
				}
			}
		}
		return lstExportData;
	}
	
	public static List<ExportDataDTO> getNonGroupedData(ResultSet resultSet,List<GridColumnInfo> columns) throws SQLException
	{
		List<ExportDataDTO> lstExportData = null;
		if(resultSet!=null && columns!=null && columns.size() > 0)
		{
			lstExportData = new ArrayList<ExportDataDTO>();
			ExportDataDTO exportData =  new ExportDataDTO();
			exportData.setLstChildren(new ArrayList<Map<String,Object>>());
			while (resultSet.next()) 
			{
				Map<String,Object> objData = getRowData(resultSet,columns);
				exportData.getLstChildren().add(objData);
			}
			lstExportData.add(exportData);
		}
		return lstExportData;
	}
	
	protected static Map<String,Object> getRowData(ResultSet resultSet,List<GridColumnInfo> columns) throws SQLException
	{
		Map<String,Object> objData = new HashMap<String,Object>();
		for(GridColumnInfo columnInfo:columns)
		{
			objData.put(columnInfo.getDataField(),resultSet.getObject(columnInfo.getDataField()));
		}
		return objData;
	}
	
	public static char getAlphabetByIndex(int index, boolean isUpperCase)
	{
		int startPoint = 0;
		if(isUpperCase)
		{
			startPoint = 64;
		}
		else
		{
			startPoint = 96;
		}
		int charAscii = startPoint + index;
		return (char)charAscii;
	}

}
