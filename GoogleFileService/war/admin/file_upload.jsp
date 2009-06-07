<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/userService.jsp"%>
<%@ page import="java.util.Date"%>

<html>
<head>
<title>Google File Upload</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<script language="JavaScript">
<!--
function load_iframe() {
	document.getElementById('progress').src='file_upload_progress.jsp';
	setTimeout('load_iframe()', 5000); //5 seconds
}
//-->
</script>
</head>

<body>
<%
// Clear session used by FileUpload ProgressListener
request.getSession().removeAttribute("progressMap");

if (!userService.isUserAdmin())
	// Never execute.
	// The web browser will be redirected to "HTTP Error 403 (User not in required role)" sent by server  
	// while vialoting "web.xml <security-constraint> <url-pattern>(/admin/*)"
	response.sendRedirect("/home.jsp");

Date date = new Date();
%>
  <form action="/upload" method="post" enctype="multipart/form-data" 
  onSubmit="load_iframe(); document.getElementById('submit').disabled='y';">
    <input type="hidden" name="fileOwner" value="<%=userName%>" />
    <div>FileId:　　<input type="text" name="fileId" size="50" value="<%="f"+String.valueOf(date.getTime())%>" /></div>
    <div>FileObject: <input type="file" name="upfile" size="50" /></div>
    <div>
    <input type="submit" name="submit" value="Upload" /></div>
  </form>
  <iframe width="400" height="20" frameborder="0" scrolling="no" id="progress" name="progress">
  Your web browser doesn't support "iframe", please update.</iframe>
</body>
</html>