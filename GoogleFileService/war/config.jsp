<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<%
UserService userService = UserServiceFactory.getUserService();
String userName = "";
boolean isUserAllowedToUpload = false;
if (!userService.isUserLoggedIn())
	response.sendRedirect("/index.jsp");
else {
	userName = userService.getCurrentUser().getNickname();
	
	// Administrators can upload or delete all files belong to any users.
	if (userService.isUserAdmin()) {
		isUserAllowedToUpload = true;
	}
	// Users can just upload or delete their own files.
	if ("user".equals(System.getProperty("upload.allowed-role"))) { //see appengine-web.xml
		isUserAllowedToUpload = true;
	}
}
%>