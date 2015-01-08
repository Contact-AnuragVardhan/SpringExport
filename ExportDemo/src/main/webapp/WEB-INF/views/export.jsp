<%@page import="com.org.export.model.LogoDetails"%>
<%@page import="java.io.InputStream"%>
<%@page import="com.org.export.enums.ExportType"%>
<%@page import="com.org.export.util.DataSourceUtil"%>
<%@page import="com.org.export.model.ExportDataDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.org.export.model.GridColumnInfo"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.org.export.model.ExportMetaData"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Export</title>
</head>
<body>
<%!
		public List<ExportDataDTO> dataProvider;
		public boolean isHeirarchicalData = true;
		public List<GridColumnInfo> columns = null;
		public GridColumnInfo parentElement = null;
		public LogoDetails logoDetails = null;
		
		public void getData()
		{
			logoDetails = new LogoDetails();
    		logoDetails.setPath(this.getClass().getResource("/assets/logo2.png").getPath());
    		logoDetails.setScaleWidthBy(73.0f);
    		logoDetails.setScaleHeightBy(70.0f);
    		logoDetails.setHeight(40.0f);
    		
			parentElement = getColumnInfo("EffectiveDate","Effective Date");
			ResultSet rs = null;
			String url = "jdbc:jtds:sqlserver://TOTWSCTDEVPCA01;databaseName=complianceportal"; 
			String username = "syscompliancedev"; 
			String password = "Nomura99";
			columns = new ArrayList<GridColumnInfo>();
    		columns.add(getColumnInfo("ID","Task ID"));
    		columns.add(getColumnInfo("Name","Task Name"));
    		columns.add(getColumnInfo("EffectiveDate","Effective Date"));
    		columns.add(getColumnInfo("CreatedBy","Created By"));
    		columns.add(getColumnInfo("CreatedDate","Created Date"));
    		columns.add(getColumnInfo("UpdatedBy","Updated By"));
			try
		    {
		      Class.forName("net.sourceforge.jtds.jdbc.Driver"); 
		      Connection conn = DriverManager.getConnection(url,username,password);  
		      Statement stmt = conn.createStatement();
		      rs = stmt.executeQuery("SELECT ID,Name,EffectiveDate,CreatedBy,CreatedDate,UpdatedBy FROM dbo.TM_TASK_MASTER");
		      if(isHeirarchicalData)
		      {
		    	  dataProvider = DataSourceUtil.getGroupedData(rs,columns,parentElement);
		      }
		      else
		      {
		    	  dataProvider = DataSourceUtil.getNonGroupedData(rs,columns);
		      }
		      conn.close();
		    }
		    catch (Exception e)
		    {
		    	e.printStackTrace();
		      System.err.println("Got an exception! "); 
		      System.err.println(e.getMessage()); 
		    } 
		}
	
		public GridColumnInfo getColumnInfo(String dataField,String headerText)
		{
			GridColumnInfo gridColumnInfo = new GridColumnInfo();
			gridColumnInfo.setDataField(dataField);
			gridColumnInfo.setHeaderText(headerText);
			gridColumnInfo.setOrder(1);
			gridColumnInfo.setRelativeWidth(3);
			gridColumnInfo.setWidth(40.0f);
			gridColumnInfo.setWordWrap(true);
			
			return gridColumnInfo;
		}
	%>
	<!-- http://www.codejava.net/frameworks/spring/spring-mvc-with-excel-view-example-apache-poi-and-jexcelapi -->
    <div align="center">
        <h1>Spring MVC Export View Demo</h1>
        <h3><a href="download">Download Document</a></h3>
    </div>
    <form name="formPDFExport" action="exportDoc" method="post">
    	<%
    		System.out.println("In PDFExport");
    		getData();
    		ExportMetaData pdfExportMetaData = new ExportMetaData();
    		
    		pdfExportMetaData.setExportType(ExportType.PDF);
    		pdfExportMetaData.setColumns(columns);
    		pdfExportMetaData.setFileName("Tasks");
    		pdfExportMetaData.setHeaderText("Tasks Details");
    		pdfExportMetaData.setLogoDetails(logoDetails);
    		pdfExportMetaData.setIsHeirarchicalData(isHeirarchicalData);
    		pdfExportMetaData.setParentColumn(parentElement);
    		pdfExportMetaData.setDataProvider(dataProvider);
    		
    	    session.setAttribute("exportData", pdfExportMetaData);
    	%>
    	<center><input type="BUTTON" onclick="exportPDF()">PDF Export</input> </center>  
    </form>
     <form name="formExcelExport" action="exportDoc" method="post">
    	<%
    		System.out.println("In ExcelExport");
    		getData();
    		ExportMetaData excelExportMetaData = new ExportMetaData();
    		
    		excelExportMetaData.setExportType(ExportType.XLS);
    		excelExportMetaData.setColumns(columns);
    		excelExportMetaData.setFileName("Tasks");
    		excelExportMetaData.setHeaderText("Tasks Details");
    		pdfExportMetaData.setLogoDetails(logoDetails);
    		excelExportMetaData.setIsHeirarchicalData(isHeirarchicalData);
    		excelExportMetaData.setParentColumn(parentElement);
    		excelExportMetaData.setDataProvider(dataProvider);
    		
    	    //session.setAttribute("exportData", excelExportMetaData);
    	%>
    	<center><input type="BUTTON" onclick="exportExcel()">Excel Export</input> </center>  
    </form>
    <script type="text/javascript">
    
    function exportPDF()
    {
    	document.formPDFExport.submit() ; 
    }
    
    function exportExcel()
    {
    	document.formExcelExport.submit() ; 
    }
    
    </script>
</body>
</html>