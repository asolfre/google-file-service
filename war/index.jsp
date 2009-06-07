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
<%
UserService userService = UserServiceFactory.getUserService();
if (!userService.isUserLoggedIn()) {
%>
	Hello! Please <a href="<%=userService.createLoginURL("/home.jsp")%>">login</a> to view the list of uploaded files or to upload your file. 
<%
} else {
	response.sendRedirect("/home.jsp");
}
%>
</body>
</html>