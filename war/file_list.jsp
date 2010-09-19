<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/config.jsp"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.List"%>
<%@ page import="javax.jdo.PersistenceManager, javax.jdo.Query"%>
<%@ page import="sinica.googlefileservice.server.datastore.GoogleFile"%>
<%@ page import="sinica.googlefileservice.server.datastore.GoogleUnit"%>
<%@ page import="sinica.googlefileservice.server.datastore.util.PMF"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="org.apache.commons.io.FileUtils"%>

<html>
<head>
<title>Google File List</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
</head>

<body>
<%
String message = request.getParameter("msg");
if (message != null)
	out.write("<font color=red>"+ message +"</font>");

PersistenceManager pm = PMF.get().getPersistenceManager();
Query query = pm.newQuery(GoogleFile.class);
// Administrators can see all uploaded files, 
// while other logged-in users can just see their own uploaded files.
if (!userService.isUserAdmin()) {
	query.setFilter("fileOwner == '"+userName+"'");
}
query.setOrdering("date desc");
query.setRange(0, 10);
List<GoogleFile> entities = (List<GoogleFile>) query.execute();
if (entities.isEmpty()) {
%>
<h4>There's no uploaded files.</h4>
<%
} else {
%>
<h4>Uploaded files are listed as follows:</h4>
<%
	for (GoogleFile g : entities) {
		String contentType = g.getContentType();
		if (contentType == null) {
			contentType = "";
		}
%>
<hr/>
<div>Date: "<%=g.getDate()%>"</div>
<div>FileId: "<%=g.getId()%>"</div>
<div>FileOwner: "<%=g.getFileOwner()%>"</div>
<div>FileName: "<%=g.getFileName()%>"</div>
<div>ContentType: "<%=contentType%>"</div>
<div>
	FileSize: "<%=FileUtils.byteCountToDisplaySize(g.getFileSize())%>" 
<%
		if (isUserAllowedToUpload) {
%>
<a href="#" onClick="if(confirm('Are you sure?')) location.href='/manage?action=delete&id=<%=URLEncoder.encode(g.getId())%>';">Delete</a>
<%
		}
%>
<a href="/download?id=<%=g.getId()%>">Download</a>
<%
		if (g.getFileSize() < 300000 && (contentType.endsWith("jpeg") || contentType.endsWith("gif") || contentType.endsWith("png"))) {
%>	
<img height="50" src="/download?id=<%=g.getId()%>"/>
<%
		}
%>
</div>
<%
		List<GoogleUnit> googleUnits = g.getGoogleUnits();
		for (GoogleUnit gUnit : googleUnits) {
			Key key = gUnit.getKey();
			Key pkey = key.getParent();
			out.print("<br/>"+pkey.getKind()+"("+pkey.getName()+")"+"("+pkey.getId()+")/");
			out.println(key.getKind()+"("+key.getId()+")");
		}
	}
}
pm.close();
%>
</body>
</html>
