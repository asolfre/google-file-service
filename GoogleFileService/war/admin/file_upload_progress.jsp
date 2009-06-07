<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Map"%>

<%
int percent = 0;
long bytesRead = 0;
long contentLength = -1;
Map<String, String> progressMap = (Map) request.getSession().getAttribute("progressMap");
if (progressMap != null) {
	percent = Integer.parseInt(progressMap.get("percent"));
	bytesRead = Long.parseLong(progressMap.get("bytesRead"));
	contentLength = Long.parseLong(progressMap.get("contentLength"));
}
// Provide Warning Logs on Google App Engine
System.err.println("("+percent+", "+bytesRead+", "+contentLength+")");
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="progress/style.css"/>
<script src="progress/progress_bar.js"></script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" 
 onLoad="show_progress_bar(<%=percent%>,<%=bytesRead%>,<%=contentLength%>)">
<div id="progressContainer" style="display: none;">
	<div id="progressBar"></div>
	<div id="progressText">
		<span id="percentText"></span>%
		(<span id="bytesRead"></span> KB / <span id="contentLength"></span> KB)
	</div>
</div>
</body>
</html>