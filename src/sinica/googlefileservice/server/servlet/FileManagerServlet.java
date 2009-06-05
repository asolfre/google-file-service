package sinica.googlefileservice.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sinica.googlefileservice.server.datastore.util.DatastoreUtils;

/**
 * @author <a href="mailto:tytung@iis.sinica.edu.tw">Tsai-Yeh Tung</a>
 * @version 0.1
 */
public class FileManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 2190879304615239209L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		out.print("<p>Error: The request method <code>"+req.getMethod()+"</code> is inappropriate for the URL <code>"+req.getRequestURI()+"</code></p>");
		out.close();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		String action = req.getParameter("action");
    	if (action != null && action.equals("delete")) {
        	String fileId = req.getParameter("id");
	    	boolean isRemoved = false;
	    	if (DatastoreUtils.isKey(fileId)) {
	    		isRemoved = DatastoreUtils.deleteGoogleFileById(fileId);
			}
	    	if (isRemoved)
	    		out.print("<script>alert('FileId \""+fileId+"\" has been successfully removed.'); location.href='/index.jsp';</script>");
	    	else
	    		out.print("<script>alert('Remove fail.'); location.href='/index.jsp';</script>");
    	} else {
    		String queryString = req.getQueryString();
    		if (queryString != null)
    			queryString = "?"+URLEncoder.encode(queryString, "UTF-8");
    		else
    			queryString = "";
    		//resp.sendRedirect("/index.jsp?msg=Incorrect operation for the URL <code>"+req.getRequestURI()+queryString+"</code>");
    		out.print("<script>alert('Incorrect operation for the URL "+req.getRequestURI()+queryString+"'); location.href='/index.jsp';</script>");
    	}
    	out.close();
	}
}