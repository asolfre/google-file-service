<%/* author: Tsai-Yeh Tung */%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/config.jsp"%>

<html>
<head>
<title>Google File Service Center</title>
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<style type="text/css">
<!--
a {
	text-decoration:none;
	color: #666666;
	padding: 3px;
}
a:hover {
	background-color: #CCD6EC;
	color: #333333;
	padding: 2px;
	border: 1px solid #336699;
}
td {
	font-size:10pt;
}
-->
</style>
</head>

<body bgcolor="#999999" leftmargin="3" topmargin="3" marginwidth="3" marginheight="3"> 
<table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#669900"> 
  <tr bgcolor="#99CC66"> 
    <td height="50" align="center" bgcolor="#99CC66">
      <p style="color:#333333 ;font-size:14pt; font-weight:bold; font-family:Courier New">
☆ Google File Service Center ☆
      </p>
    </td> 
  </tr> 
  <tr>
    <td height="25">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr align="center" bgcolor="#eeeeee">
        <td height="25" align="right">&nbsp;</td>
        <td align="center" bgcolor="#eeeeee">
          <table cellpadding="3" cellspacing="0">
            <tr align="center"> 
              <td><font face="Courier New">《 Welcome <font color="#0066CC"><%=userName%></font> 》</font></td>
              <% if (isUserAllowedToUpload) { %>
              <td><a href="/file_upload.jsp" target="main">:: File upload</a></td>
              <% } %>
              <td><a href="/file_list.jsp" target="main">:: File listing</a></td>
              <% if (userService.isUserAdmin()) { %>
              <td><a href="/admin/info" target="main">:: Server Info</a></td>
              <% } %>
              <td><a href="<%=userService.createLogoutURL("/index.jsp")%>">:: Logout</a></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    </td>
  </tr>
  <tr> 
    <td align="center" bgcolor="#FFFFFF">
      <table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0"> 
        <tr>
          <td>
            <iframe width="100%" height="100%" frameborder="0" src="/file_list.jsp" name="main">
            Your web browser doesn't support "iframe", please update.</iframe>
          </td>
        </tr> 
      </table>
    </td> 
  </tr> 
  <tr> 
    <td height="20" align="center" bgcolor="#99CC66">
      <font style="color:#999966; font-family:Courier New">Copyright&copy; 2009 T.Y.Tung, Webcollab, Institute of Information Science, Academia Sinica.</font>
    </td> 
  </tr> 
</table> 
</body>
</html>