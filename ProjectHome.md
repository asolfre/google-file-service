## What is GoogleFileService? ##
`GoogleFileService` is based on <a href='http://code.google.com/appengine/'>Google App Engine for Java</a>, and its aim is to provide data models and APIs for uploading and downloading large files of each size is up to 10MB `[1]` to and from Google Datastore via HTTPS securely.
<p>
Please visit the latest <a href='https://gae-file-service.appspot.com/'>demo on Google App Engine</a> or download the Zip files of source codes to develop or run it yourself.<br>
<br>
<h2>Key Features (What is New?)</h2>
<blockquote><h4>Sep 19, 2010. GoogleFileService v0.4</h4>
</blockquote><ul><li>Upgrade to Apache-HttpClient 4.0.2 and the latest SDK appengine-java-sdk-1.3.7.<br>
</li><li>Fix the bug of the escaped chars<br>
</li></ul><blockquote><h4>Nov 05, 2009. GoogleFileService v0.3</h4>
</blockquote><ul><li>Add the setting in <code>'appengine-web.xml'</code> to determine if only administrators can upload files. If not, then logged in users can just upload and delete their own files, while administrators can upload and delete files belong to any users.<br>
</li><li>Fix the bug of getGoogleFileById() to be compatible with the latest SDK appengine-java-sdk-1.2.6.  (<a href='http://groups.google.com/group/google-file-service/browse_thread/thread/52ff7383972ed2b1'>Thanks for Zhang Yu's help</a>)<br>
</li></ul><blockquote><h4>Jun 07, 2009. GoogleFileService v0.2</h4>
</blockquote><ul><li>Add web management UI (Two roles as administrator or regular user. Only administrators can upload files.)<br>
</li><li>Add uploading progress bar. <code>[2]</code>
</li></ul><blockquote><h4>Jun 05, 2009. GoogleFileService v0.1</h4>
</blockquote><ul><li>To break down barriers of the <a href='http://code.google.com/intl/en/appengine/docs/java/datastore/overview.html#Quotas_and_Limits'>1MB size limitation of each Google datastore entity</a>, and extend singal file size up to 10MB.<br>
</li><li>To send correct UTF-8 downloaded file name for different browsers such as IE, Firefox, and Google Chrome.<br>
</li><li>To upload and download files via HTTPS securely.</li></ul>

<h2>Installation</h2>
<ol><li>Download the latest <code>GoogleFileService</code> Zip files of source codes.<br>
</li><li>Locate <a href='http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/war/WEB-INF/appengine-web.xml'><code>GoogleFileService\war\WEB-INF\appengine-web.xml</code></a> and modify the property <code>'upload.allowed-client-ip'</code> value to be a list of IPs that can upload files via Apache HttpClient API (if required).<br>
</li><li>Apply your account on Google App Engine for Java.<br>
</li><li>Upload <code>GoogleFileService</code> source codes to Google App Engine for Java. <a href='http://code.google.com/intl/en/appengine/docs/java/gettingstarted/uploading.html'>(How to upload)</a>
</li><li>Finally, you can try it on <code>https://[your_app_id].appspot.com/</code></li></ol>

<h2>Getting Started</h2>
You can upload your file via the webpage's form or via <a href='http://hc.apache.org/'>Apache HttpClient API</a>.<br>
<p>
We handle uploaded file by using <a href='http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/src/sinica/googlefileservice/server/servlet/FileUploadServlet.java'><code>FileUploadServlet.java</code></a> defined in <a href='http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/war/WEB-INF/web.xml'><code>web.xml</code></a> as follows:<br>
<pre><code>&lt;servlet&gt;<br>
    &lt;servlet-name&gt;upload&lt;/servlet-name&gt;<br>
    &lt;servlet-class&gt;sinica.googlefileservice.server.servlet.FileUploadServlet&lt;/servlet-class&gt;<br>
&lt;/servlet&gt;<br>
&lt;servlet-mapping&gt;<br>
    &lt;servlet-name&gt;upload&lt;/servlet-name&gt;<br>
    &lt;url-pattern&gt;/upload&lt;/url-pattern&gt;<br>
&lt;/servlet-mapping&gt;<br>
</code></pre>
<blockquote><h4>1. Webpage's form</h4>
If you want to upload files via the webpage, please read as follows:<br>
</blockquote><ul><li>Ensure the field names <code>'fileOwner'</code>, <code>'fileId'</code>, <code>'upfile'</code> all exist, and the <code>'upfile'</code> is the last one of these three fields to work correctly with <a href='http://commons.apache.org/fileupload/streaming.html'>Apache Commons FileUpload Streaming API</a>.<br>
</li><li><code>[Notice]</code> Only logged in users can upload files.<br>
<pre><code>&lt;form action="/upload" method="post" enctype="multipart/form-data"&gt;<br>
  &lt;input type="hidden" name="fileOwner" /&gt;<br>
  &lt;div&gt;FileId:　　&lt;input type="text" name="fileId" size="50" /&gt;&lt;/div&gt;<br>
  &lt;div&gt;FileObject: &lt;input type="file" name="upfile" size="50" /&gt;&lt;/div&gt;<br>
  &lt;div&gt;&lt;input type="submit" name="submit" value="Upload" /&gt;&lt;/div&gt;<br>
&lt;/form&gt;<br>
</code></pre>
</li></ul><blockquote><h4>2. Apache HttpClient</h4>
If you want to upload files via other java application, please read as follows:<br>
</blockquote><ul><li>You can use Apache <code>'httpclient-4.0.2.jar'</code> to send HTTP POST data to Servlet as we do with the webpage's form.<br>
</li><li>We write a convenient <code>FileServiceClient.java</code> to handle all the things for sending HTTP POST data, so you just need to create an instance of <code>FileServiceClient</code> and invoke <code>submit()</code> method.<br>
</li><li><code>[Notice]</code> Only allowed client IPs can upload files via Apache HttpClient API. Change <a href='http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/war/WEB-INF/appengine-web.xml'><code>WEB-INF/appengine-web.xml</code></a> if required.<br>
<pre><code>// Google App Engine URL<br>
String url = "https://[your_app_id].appspot.com/upload";		<br>
// Create an instance of FileServiceClient for Google App Engine<br>
FileServiceClient client = FileServiceClientFactory.getFileServiceClient(url);<br>
// submit(String fileId, String fileOwner, String fileName, byte[] fileData, String contentType)<br>
int fileSize = client.submit(fileId, fileOwner, fileName, fileData, contentType);<br>
</code></pre></li></ul>


<h2>How It Works</h2>
With GoogleFile and GoogleUnit data models (datastore), you can upload a large file of size up to 10 MB very easily via our static method <code>DatastoreUtils.insertGoogleFile()</code>.<br>
<p>
Some code of <a href='http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/src/sinica/googlefileservice/server/servlet/FileUploadServlet.java'><code>FileUploadServlet.java</code></a> is as follows:<br>
<pre><code>String fileId = "";<br>
String fileOwner = "";<br>
String fileName = "";<br>
int fileSize = -1;<br>
String contentType = "";<br>
// Create a new file upload handler<br>
ServletFileUpload upload = new ServletFileUpload();<br>
// Set overall request size constraint: the default value of -1 indicates that there is no limit.<br>
upload.setSizeMax(10240000); //10MB, the maximum allowed size, in bytes.<br>
// Set the UTF-8 encoding to grab the correct uploaded filename, especially for Chinese<br>
upload.setHeaderEncoding("UTF-8");<br>
// Parse the request<br>
FileItemIterator iter = upload.getItemIterator(req);<br>
while (iter.hasNext()) {<br>
    FileItemStream item = iter.next();<br>
    InputStream stream = item.openStream();<br>
    String fieldName = item.getFieldName();<br>
    if (item.isFormField()) {<br>
    	//process a regular form field<br>
        if (fieldName.equals("fileId"))<br>
            //set the UTF-8 encoding to grab the correct string<br>
            fileId = Streams.asString(stream, "UTF-8");<br>
        if (fieldName.equals("fileOwner"))<br>
            //set the UTF-8 encoding to grab the correct string<br>
            fileOwner = Streams.asString(stream, "UTF-8");<br>
    } else {<br>
    	//process a file upload<br>
        fileName = item.getName();<br>
        if (fileName != null)<br>
    	    fileName= FilenameUtils.getName(fileName);<br>
        contentType = item.getContentType();<br>
        if (fieldName.equals("upfile")) {<br>
            // Check if the fileId conforms to the Key format of the Google datastore <br>
            // and all other uploaded fields are not empty.<br>
            if (DatastoreUtils.isKey(fileId) &amp;&amp; fileOwner.length() &gt; 0 &amp;&amp; fileName.length() &gt; 0) {<br>
                // Save into Google datastore<br>
        	fileSize = DatastoreUtils.insertGoogleFile(fileId, fileOwner, fileName, contentType, stream);<br>
            }<br>
        }<br>
    }<br>
}<br>
</code></pre>


<h2>Known Issues</h2>
<ol><li>If we submit a large file of size over 10 MB, then we'll receive a HTTP Error 413  Request Entity Too Large (Your client issued a request that was too large) from Google App Engine.<br>
</li><li>The uploading progress bar works on local development server, but not works on Google App Engine.</li></ol>


<h2>Questions or Suggestions?</h2>
<table cellspacing='0'>
<blockquote><tr>
<td valign='middle'>
<a href='http://groups.google.com/group/google-file-service'>
<img src='http://groups.google.com/intl/en/images/logos/groups_logo_sm.gif' alt='Google Groups' /></a>
</td>
<td> </td>
<td valign='middle'>
=><br>
<a href='http://groups.google.com/group/google-file-service'>
<b>GoogleFileService</b>
</a>
</td>
</tr>
</table>