package sinica.googlefileservice.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * A FileServiceClient for uploading one file into {@link GoogleDatastore}.
 * 
 * @author <a href="mailto:tytung@iis.sinica.edu.tw">Tsai-Yeh Tung</a>
 * @version 1.0
 */
public class FileServiceClient {
	private String url = "http://localhost.:8888/upload";
	private HttpClient httpClient;
	private HttpPost httpPost;
	
	/**
	 * Protected Constructor.
	 * 
	 * @param url The URL of the uploading Servlet entry point.
	 */
	protected FileServiceClient(String url){
		// Set the URL of the uploading Servlet entry point.
		this.url = url;
		// Create an instance of HttpClient.
		httpClient = new DefaultHttpClient();
		//System.out.println("a FileServiceClient has been created.");
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
	 *         <div>or -5 if the client connects to the server fail, </div>
	 *         <div>or -6 if the server doesn't return any data.</div>
	 *         
	 * @throws IOException 
	 */
	public int submit(String fileId, String fileOwner, String fileName, byte[] fileData, String contentType) throws IOException {
		// Create a method instance.
		httpPost = new HttpPost(url);
		// Provide form data
		InputStream fileStream = new ByteArrayInputStream(fileData);
		MultipartEntity multipart = new MultipartEntity();
		multipart.addPart("fileId", new StringBody(fileId));
		multipart.addPart("fileOwner", new StringBody(fileOwner));
		multipart.addPart("upfile", new InputStreamBody(fileStream, contentType, fileName));
		httpPost.setEntity(multipart);
	    // Submit
	    try {
			// Execute the method.
			HttpResponse response = httpClient.execute(httpPost);
			// Read the response status.
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				// Read the response body.
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// Upload success(>0)
					// or (-1)FileUploadException, (-2)fields are empty or wrong formats, (-3)disallowed client IP
					InputStream stream = entity.getContent();
					String responseBody = IOUtils.toString(stream);
					return Integer.parseInt(responseBody); //return ?
				} else {
					return -6; //return -6 (server problem)
				}
			} else {
				// Upload fail
				System.err.println("Error: " + statusLine);
				return -4; //return -4 (HTTP status codes problem)
			}
		} catch (IOException e) {
			// Upload fail
			System.err.println("Fatal transport error: " + e.getMessage());
			return -5; //return -5 (connection problem)
		}
	}
	
	/**
	 * When HttpClient instance is no longer needed,
	 * shut down the connection manager to ensure
	 * immediate deallocation of all system resources
	 */
	public void releaseConnection() {
        httpClient.getConnectionManager().shutdown();
	}
}