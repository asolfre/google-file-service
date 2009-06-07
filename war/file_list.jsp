<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/userService.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="sinica.googlefileservice.server.datastore.GoogleFile"%>
<%@ page import="sinica.googlefileservice.server.datastore.GoogleUnit"%>
<%@ page import="sinica.googlefileservice.server.datastore.util.PMF"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>

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
String query = "select from " + GoogleFile.class.getName() + " order by date desc range 0,5";
@SuppressWarnings("unchecked")
List<GoogleFile> entities = (List<GoogleFile>) pm.newQuery(query).execute();
if (entities.isEmpty()) {
%>
<h4>There's no uploaded files.</h4>
<%
} else {
%>
<h4>Uploaded files are listed as follows:</h4>
<%
	for (GoogleFile g : entities) {
%>
<hr/>
<div>Date: "<%=g.getDate()%>"</div>
<div>FileId: "<%=g.getId()%>"</div>
<div>FileOwner: "<%=g.getFileOwner()%>"</div>
<div>FileName: "<%=g.getFileName()%>"</div>
<div>ContentType: "<%=g.getContentType()%>"</div>
<div>
	FileSize: "<%=g.getFileSize()%> Bytes" 
<%
		if (userService.isUserAdmin()) {
%>
<a href="#" onClick="if(confirm('Are you sure?')) location.href='/admin/manage?action=delete&id=<%=g.getId()%>';">Delete</a>
<%
		}
%>
<a href="/download?id=<%=g.getId()%>">Download</a>
<%
		String contentType = g.getContentType();

		if (g.getFileSize() < 300000 && (contentType.endsWith("jpeg") || contentType.endsWith("gif") || contentType.endsWith("png"))) {
%>	
<img height="50" src="/download?id=<%=g.getId()%>"/>
<%
		}
%>
</div>
<%
		if (userService.isUserAdmin()) {
			List<GoogleUnit> googleUnits = g.getGoogleUnits();
			for (GoogleUnit gUnit : googleUnits) {
				Key key = gUnit.getKey();
				Key pkey = key.getParent();
				out.print("<br/>"+pkey.getKind()+"("+pkey.getName()+")"+"("+pkey.getId()+")/");
				out.println(key.getKind()+"("+key.getName()+")"+"("+key.getId()+")");
			}
		}
	}
}
pm.close();
%>
</body>
</html>