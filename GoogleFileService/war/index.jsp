<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="sinica.googlefileservice.server.datastore.GoogleFile"%>
<%@ page import="sinica.googlefileservice.server.datastore.util.PMF"%>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
  </head>
  <body>
<%
	String message = request.getParameter("msg");
	if (message != null)
		out.write("<font color=red>"+ message +"</font>");
	
	String username = "";
	UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
    	username = user.getNickname();
%>
<p>Hello, <%=username%>! (You can
<a href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign out</a>.)</p>
<%
	} else {
%>
<p>Hello!
<a href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign in</a>
your name to upload your file.</p>
<%
	}
%>
  <form action="/upload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="fileOwner" value="<%=username%>" />
    <div>FileId:　　<input type="text" name="fileId" size="50" /></div>
    <div>FileObject: <input type="file" name="upfile" size="50" /></div>
    <div><input type="submit" name="submit" value="Upload" /></div>
  </form>
<%
	PersistenceManager pm = PMF.get().getPersistenceManager();
	String query = "select from " + GoogleFile.class.getName() + " order by date desc range 0,5";
	@SuppressWarnings("unchecked")
	List<GoogleFile> entities = (List<GoogleFile>) pm.newQuery(query).execute();
	if (entities.isEmpty()) {
%>
<p>There's no uploaded files.</p>
<%
	} else {
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
if (userService.isUserLoggedIn()) {
	if (userService.isUserAdmin()) {
%>
	<a href="#" onClick="if(confirm('Are you sure?')) location.href='/admin/manage?action=delete&id=<%=g.getId()%>';">Delete</a>
<%
	}
}
%>
	<a href="/download?id=<%=g.getId()%>">Download</a>
<%
String contentType = g.getContentType();
if (contentType.endsWith("jpeg") || contentType.endsWith("gif") || contentType.endsWith("png")) {
%>	
	<img height="50" src="/download?id=<%=g.getId()%>"/>
<%
}
%>
</div>
<%
		}
	}
	pm.close();
%>
  </body>
</html>