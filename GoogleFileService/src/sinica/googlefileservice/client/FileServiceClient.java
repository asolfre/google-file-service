package sinica.googlefileservice.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.IOUtils;

import sinica.googlefileservice.server.datastore.GoogleFile;

/**
 * A FileServiceClient for uploading one file into {@link GoogleFile}.
 * 
 * @author <a href="mailto:tytung@iis.sinica.edu.tw">Tsai-Yeh Tung</a>
 * @version 1.0
 */
public class FileServiceClient {
	private String url = "http://localhost.:8080/upload";
	private HttpClient httpClient;
	private PostMethod postMethod;
	
	/**
	 * Protected Constructor.
	 * 
	 * @param url The URL of the uploading Servlet entry point.
	 */
	protected FileServiceClient(String url){
		// Set the URL of the uploading Servlet entry point
		this.url = url;
		// Create an instance of HttpClient.
		httpClient = new HttpClient();
		// Timeout in milliseconds.
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	}
	
	/**
	 * @return The URL of the uploading Servlet entry point.
	 */
	public String getURL() {
		return url;
	}
	
	/**
	 * Set the URL of the uploading Servlet entry point used by the FileServiceClient.
	 * 
	 * @param url The URL of the uploading Servlet entry point.
	 */
	public void setURL(String url) {
		this.url = url;
	}
	
	/**
	 * Submit <code>multipart/form-data</code> via HTTP POST method.
	 * 
	 * @param fileId 	The primary key in Google datastore, and is provided as String.
	 * @param fileOwner The field name in Google datastore, and is provided as String.
	 * @param fileName 	The field name in Google datastore, and is provided as String.
	 * @param fileData 	The field name in Google datastore, and is provided as byte array of file's content.
	 * 
	 * @return The size of successfully uploaded file, 
	 *         <div>or -1 if the server throws FileUploadException or IOException, </div>
	 *         <div>or -2 if the server says that uploaded fields are empty or wrong formats, </div>
	 *         <div>or -3 if the server says that the client isn't in allowed client IPs list, </div>
	 *         <div>or -4 if the server doesn't return correct HTTP status code: 200 OK (HTTP/1.0 - RFC 1945), </div>
	 *         <div>or -5 if the client connects to the server fail.</div>
	 */
	public int submit(String fileId, String fileOwner, String fileName, byte[] fileData, String contentType) {
		// Create a method instance.
		postMethod = new PostMethod(url);
		// Provide form data
		Part[] parts = {
				new StringPart("fileId", fileId), 
				new StringPart("fileOwner", fileOwner), 
				new FilePart("upfile", new ByteArrayPartSource(fileName, fileData), contentType, "UTF-8")
				};
		MultipartRequestEntity multipart = new MultipartRequestEntity(parts, postMethod.getParams());
	    postMethod.setRequestEntity(multipart);
	    // Submit
	    try {
			// Execute the method.
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// Read the response body.
				InputStream stream = postMethod.getResponseBodyAsStream();
				String responseBody = IOUtils.toString(stream);
				// Upload success(>0)
				// or (-1)FileUploadException, (-2)fields are empty or wrong formats, (-3)disallowed client IP
				return Integer.parseInt(responseBody);
			} else {
				// Upload fail
				System.err.println("Method failed: " + postMethod.getStatusLine());
				return -4; //return -4 (HTTP status codes problem)
			}
		} catch (IOException e) {
			// Upload fail
			System.err.println("Fatal transport error: " + e.getMessage());
			return -5; //return -5 (connection problem)
		} finally {
			// Release the connection.
			postMethod.releaseConnection();
		}
	}
}