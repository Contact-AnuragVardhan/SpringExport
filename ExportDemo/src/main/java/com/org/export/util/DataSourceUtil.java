package com.org.export.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.org.export.model.ExportDataDTO;
import com.org.export.model.GridColumnInfo;

public class DataSourceUtil 
{
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
	
	private static Map<String,Object> getRowData(ResultSet resultSet,List<GridColumnInfo> columns) throws SQLException
	{
		Map<String,Object> objData = new HashMap<String,Object>();
		for(GridColumnInfo columnInfo:columns)
		{
			objData.put(columnInfo.getDataField(),resultSet.getObject(columnInfo.getDataField()));
		}
		return objData;
	}

}
