package sinica.googlefileservice.client.examples;

import java.io.IOException;
import java.util.Date;

import sinica.googlefileservice.client.FileServiceClient;
import sinica.googlefileservice.client.FileServiceClientFactory;

/**
 * @author <a href="mailto:tytung@iis.sinica.edu.tw">Tsai-Yeh Tung</a>
 * @version 1.0
 */
public class MyFileServiceClient {
	
	public static void main(String[] args) throws IOException {
		Date date = new Date();
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><projectDescription><name>FileService專案</name></projectDescription>";
		String attachmentID = "mail"+String.valueOf(date.getTime());
		String owner = "tytung";
		String filename = ".project.xml";
		String mimetype = "text/xml";
		byte[] attachmentBytes = xml.getBytes("UTF-8");
		
		System.out.println(attachmentID);
		System.out.println(owner);
		System.out.println(filename);
		System.out.println(mimetype);
		System.out.println(attachmentBytes.length);
		
		// The URL of the uploading Servlet entry point
		String url = "";
		url = "http://localhost:8080/upload";
		//url = "https://gae-file-service.appspot.com/upload"; //Google App Engine
		
		// Create an instance of FileServiceClient for Google App Engine
		FileServiceClient client = FileServiceClientFactory.getFileServiceClient(url);
		int fileSize = 0;
		
		// Submit 1
		System.out.println("**1**");
		fileSize = client.submit(attachmentID, owner, filename, attachmentBytes, mimetype);
		printResult(fileSize, filename);

		// Submit 2
//		System.out.println("**2**");
//		fileSize = client.submit("AAAAA", owner, filename, attachmentBytes, mimetype);
//		printResult(fileSize, filename);
	}
	
	public static void printResult(int fileSize, String filename) {
		if (fileSize > 0)
			System.out.println("The filename '"+filename+"' of size "+String.valueOf(fileSize)+" bytes had been uploaded.");
		else if (fileSize == -1)
			System.out.println("Upload fail: FileUploadException or IOException");
		else if (fileSize == -2)
			System.out.println("Upload fail: fields are empty or wrong formats");
		else if (fileSize == -3)
			System.out.println("Upload fail: disallowed client IP");
		else if (fileSize == -4)
			System.out.println("Upload fail: HTTP status code isn't 200 OK");
		else //fileSize == -5
			System.out.println("Upload fail: connection problem");
	}
}