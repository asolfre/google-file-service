package sinica.googlefileservice.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
//import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import sinica.googlefileservice.server.datastore.util.DatastoreUtils;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author <a href="mailto:tytung@iis.sinica.edu.tw">Tsai-Yeh Tung</a>
 * @version 0.1
 */
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 8367618333138027430L;
//	private static final Logger log = Logger.getLogger(FileUploadServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		out.print("<p>Error: The request method <code>"+req.getMethod()+"</code> is inappropriate for the URL <code>"+req.getRequestURI()+"</code></p>");
		out.close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		//check if User-Agent is HTTP client (Jakarta Commons-HttpClient/3.1) or not
		boolean isHttpClient = false;
		if (req.getHeader("User-Agent").indexOf("HttpClient") > -1)
			isHttpClient = true;
		boolean isFailureSubmit = false;
    	boolean isAllowedUser = false;
		boolean isDuplicatedId = false;
		String fileId = "";
		String fileOwner = "";
		String fileName = "";
		int fileSize = -1;
		String contentType = "";
		try {
			// see appengine-web.xml
			long maxsize = Long.parseLong(System.getProperty("upload.allowed-maxsize"));
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload();
			// Set overall request size constraint: the default value of -1 indicates that there is no limit.
			upload.setSizeMax(maxsize); //the maximum allowed size, in bytes.
			// Set the UTF-8 encoding to grab the correct uploaded filename, especially for Chinese
			upload.setHeaderEncoding("UTF-8");
			
			// Parse the request
			FileItemIterator iter = upload.getItemIterator(req);
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
				InputStream stream = item.openStream();
			    String fieldName = item.getFieldName();
			    if (item.isFormField()) {
			    	//process a regular form field
			        if (fieldName.equals("fileId"))
			        	//set the UTF-8 encoding to grab the correct string
			        	fileId = Streams.asString(stream, "UTF-8");
			        if (fieldName.equals("fileOwner"))
			        	//set the UTF-8 encoding to grab the correct string
			        	fileOwner = Streams.asString(stream, "UTF-8");
			    } else {
			    	//process a file upload
			        fileName = item.getName();
			        fileName = fileName.substring(fileName.lastIndexOf("\\")+1); // for Windows and Linux
			        contentType = item.getContentType();
			        if (fieldName.equals("upfile")) {
			        	// HttpClient can upload data via allowed client IPs (see appengine-web.xml), 
			        	// and web form can upload data via user login as administrator.
			        	if (isHttpClient) {
			        		//via HTTP client
			        		isAllowedUser = true;
			        	} else {
			        		//via web form
				        	UserService userService = UserServiceFactory.getUserService();
				        	if (userService.isUserLoggedIn())
				        		if (userService.isUserAdmin())
				        			isAllowedUser = true;
			        	}
			        	//check if allowed user or not
			        	if (isAllowedUser) {
				        	//check if the fileId conforms to the Key format of the Google datastore 
			        		//and all other uploaded fields are not empty.
				        	if (DatastoreUtils.isKey(fileId) && fileOwner != "" && fileName != "") {
					        	//save into Google datastore
					        	fileSize = DatastoreUtils.insertGoogleFile(fileId, fileOwner, fileName, contentType, stream);
					        	if (fileSize < 0) {
					        		//fileSize == -1 or -2
					        		isFailureSubmit = true;
					        		if (fileSize == -2)
						        		isDuplicatedId = true;
					        	}
				        	} else {
				        		isFailureSubmit = true;
				        	}
			        	} else {
			        		isFailureSubmit = true;
			        	}
			        }
			    }
			}
//			if (!isWrongData) {
//				log.info("fileId: " + fileId);
//				log.info("fileOwner: " + fileOwner);
//				log.info("fileName: " + fileName);
//				log.info("fileSize: " + fileSize);
//				log.info("ContentType: " + contentType);
//			}
		} catch (IOException e) {
			// Upload fail
			if (isHttpClient) {
				//via HTTP client
				out.print(-1); //return -1 (IOException)
			} else {
				//via web form
				out.print("Error: IOException");
//				e.printStackTrace();
			}
		} catch (FileUploadException e) {
			// Upload fail
			if (isHttpClient) {
				//via HTTP client
				out.print(-1); //return -1 (FileUploadException)
			} else {
				//via web form
				out.print("Error: FileUploadException");
//				e.printStackTrace();
			}
		}
		//return result
		if (!isFailureSubmit) {
			// Upload success
			if (isHttpClient) {
				//via HTTP client
				out.print(fileSize); //return fileSize
			} else {
				//via web form
				resp.sendRedirect("/index.jsp?msg=Upload success");
			}
		} else {
			// Upload fail while the fileId violates the limitation of com.google.appengine.api.datastore.Key
			if (isHttpClient) {
				//via HTTP client
				out.print(-2); //return -2 (fields are empty or wrong formats)
			} else {
				//via web form
				if (isDuplicatedId)
					resp.sendRedirect("/index.jsp?msg=Upload fail: duplicated primary key.");
				else if (!isAllowedUser)
					resp.sendRedirect("/index.jsp?msg=Upload fail: no permissions, please login as a administrator.");
				else
					resp.sendRedirect("/index.jsp?msg=Upload fail: the uploaded fields are empty or wrong formats.");
			}
		}
		out.close();
	}
}