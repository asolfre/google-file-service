<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<html>
<head>
<title>Google File Service Center</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
</head>
<body>
<h2>Welcome to GoogleFileService Center</h2>
<p>GoogleFileService is based on Google App Engine for Java, and its aim is to provide data models and APIs for uploading and downloading large files of each size is up to 10MB to and from Google Datastore via HTTPS securely.</p>
<%
UserService userService = UserServiceFactory.getUserService();
if (!userService.isUserLoggedIn()) {
%>
	<b>Hello! Please <a href="<%=userService.createLoginURL("/home.jsp")%>">sign in</a> to view the list of uploaded files or to upload your file.</b>
<%
} else {
	response.sendRedirect("/home.jsp");
}
%>
<p>For more information on GoogleFileService, please visit <a href="http://code.google.com/p/google-file-service/">project homepage</a>.</p>
</body>
</html>