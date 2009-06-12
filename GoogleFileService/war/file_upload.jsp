<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/config.jsp"%>
<%@ page import="java.util.Date"%>

<html>
<head>
<title>Google File Upload</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<script language="JavaScript">
<!--
function load_iframe() {
	document.getElementById('progress_frame').src='file_upload_progress.jsp';
	document.getElementById('progress_bar').innerHTML+='...';
	setTimeout('load_iframe()', 100); //10 seconds
}
//-->
</script>
</head>

<body>
<%
// Clear session used by FileUpload ProgressListener
request.getSession().removeAttribute("progressMap");

if (!isUserAllowedToUpload)
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
  <div id="progress_bar"></div>
  <iframe width="400" height="20" frameborder="0" scrolling="no" id="progress_frame" name="progress_frame">
  Your web browser doesn't support "iframe", please update.</iframe>
</body>
</html>