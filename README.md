# google-file-service
Automatically exported from code.google.com/p/google-file-service

 <h2><a name="What_is_?"></a>What is GoogleFileService?<a href="#What_is_?" class="section_anchor"></a></h2><p><tt>GoogleFileService</tt> is based on <a href="http://code.google.com/appengine/" rel="nofollow">Google App Engine for Java</a>, and its aim is to provide data models and APIs for uploading and downloading large files of each size is up to 10MB <tt>[1]</tt> to and from Google Datastore via HTTPS securely. <p> Please visit the latest <a href="https://gae-file-service.appspot.com/" rel="nofollow">demo on Google App Engine</a> or download the Zip files of source codes to develop or run it yourself. </p><h2><a name="Key_Features_(What_is_New?)"></a>Key Features (What is New?)<a href="#Key_Features_(What_is_New?)" class="section_anchor"></a></h2><blockquote><h4><a name="Sep_19,_2010._v0.4"></a>Sep 19, 2010. GoogleFileService v0.4<a href="#Sep_19,_2010._v0.4" class="section_anchor"></a></h4>
</blockquote><ul><li>Upgrade to Apache-HttpClient 4.0.2 and the latest SDK appengine-java-sdk-1.3.7. </li><li>Fix the bug of the escaped chars </li></ul><blockquote><h4><a name="Nov_05,_2009._v0.3"></a>Nov 05, 2009. GoogleFileService v0.3<a href="#Nov_05,_2009._v0.3" class="section_anchor"></a></h4>
</blockquote><ul><li>Add the setting in <tt>&#x27;appengine-web.xml&#x27;</tt> to determine if only administrators can upload files. If not, then logged in users can just upload and delete their own files, while administrators can upload and delete files belong to any users. </li><li>Fix the bug of getGoogleFileById() to be compatible with the latest SDK appengine-java-sdk-1.2.6.  (<a href="http://groups.google.com/group/google-file-service/browse_thread/thread/52ff7383972ed2b1" rel="nofollow">Thanks for Zhang Yu&#x27;s help</a>) </li></ul><blockquote><h4><a name="Jun_07,_2009._v0.2"></a>Jun 07, 2009. GoogleFileService v0.2<a href="#Jun_07,_2009._v0.2" class="section_anchor"></a></h4>
</blockquote><ul><li>Add web management UI (Two roles as administrator or regular user. Only administrators can upload files.) </li><li>Add uploading progress bar. <tt>[2]</tt> </li></ul><blockquote><h4><a name="Jun_05,_2009._v0.1"></a>Jun 05, 2009. GoogleFileService v0.1<a href="#Jun_05,_2009._v0.1" class="section_anchor"></a></h4>
</blockquote><ul><li>To break down barriers of the <a href="http://code.google.com/intl/en/appengine/docs/java/datastore/overview.html#Quotas_and_Limits" rel="nofollow">1MB size limitation of each Google datastore entity</a>, and extend singal file size up to 10MB. </li><li>To send correct UTF-8 downloaded file name for different browsers such as IE, Firefox, and Google Chrome. </li><li>To upload and download files via HTTPS securely. </li></ul><h2><a name="Installation"></a>Installation<a href="#Installation" class="section_anchor"></a></h2><ol><li>Download the latest <tt>GoogleFileService</tt> Zip files of source codes. </li><li>Locate <a href="http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/war/WEB-INF/appengine-web.xml" rel="nofollow"><tt>GoogleFileService\war\WEB-INF\appengine-web.xml</tt></a> and modify the property <tt>&#x27;upload.allowed-client-ip&#x27;</tt> value to be a list of IPs that can upload files via Apache HttpClient API (if required). </li><li>Apply your account on Google App Engine for Java. </li><li>Upload <tt>GoogleFileService</tt> source codes to Google App Engine for Java. <a href="http://code.google.com/intl/en/appengine/docs/java/gettingstarted/uploading.html" rel="nofollow">(How to upload)</a> </li><li>Finally, you can try it on <tt>https://[your_app_id].appspot.com/</tt> </li></ol><h2><a name="Getting_Started"></a>Getting Started<a href="#Getting_Started" class="section_anchor"></a></h2><p>You can upload your file via the webpage&#x27;s form or via <a href="http://hc.apache.org/" rel="nofollow">Apache HttpClient API</a>. <p> We handle uploaded file by using <a href="http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/src/sinica/googlefileservice/server/servlet/FileUploadServlet.java" rel="nofollow"><tt>FileUploadServlet.java</tt></a> defined in <a href="http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/war/WEB-INF/web.xml" rel="nofollow"><tt>web.xml</tt></a> as follows: </p><pre class="prettyprint">&lt;servlet&gt;
    &lt;servlet-name&gt;upload&lt;/servlet-name&gt;
    &lt;servlet-class&gt;sinica.googlefileservice.server.servlet.FileUploadServlet&lt;/servlet-class&gt;
&lt;/servlet&gt;
&lt;servlet-mapping&gt;
    &lt;servlet-name&gt;upload&lt;/servlet-name&gt;
    &lt;url-pattern&gt;/upload&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;</pre><blockquote><h4><a name="1._Webpage&#x27;s_form"></a>1. Webpage&#x27;s form<a href="#1._Webpage&#x27;s_form" class="section_anchor"></a></h4>
If you want to upload files via the webpage, please read as follows: 
</blockquote><ul><li>Ensure the field names <tt>&#x27;fileOwner&#x27;</tt>, <tt>&#x27;fileId&#x27;</tt>, <tt>&#x27;upfile&#x27;</tt> all exist, and the <tt>&#x27;upfile&#x27;</tt> is the last one of these three fields to work correctly with <a href="http://commons.apache.org/fileupload/streaming.html" rel="nofollow">Apache Commons FileUpload Streaming API</a>. </li><li><tt>[Notice]</tt> Only logged in users can upload files. </li><pre class="prettyprint">&lt;form action=&quot;/upload&quot; method=&quot;post&quot; enctype=&quot;multipart/form-data&quot;&gt;
  &lt;input type=&quot;hidden&quot; name=&quot;fileOwner&quot; /&gt;
  &lt;div&gt;FileId:　　&lt;input type=&quot;text&quot; name=&quot;fileId&quot; size=&quot;50&quot; /&gt;&lt;/div&gt;
  &lt;div&gt;FileObject: &lt;input type=&quot;file&quot; name=&quot;upfile&quot; size=&quot;50&quot; /&gt;&lt;/div&gt;
  &lt;div&gt;&lt;input type=&quot;submit&quot; name=&quot;submit&quot; value=&quot;Upload&quot; /&gt;&lt;/div&gt;
&lt;/form&gt;</pre></ul><blockquote><h4><a name="2._Apache"></a>2. Apache HttpClient<a href="#2._Apache" class="section_anchor"></a></h4>
If you want to upload files via other java application, please read as follows: 
</blockquote><ul><li>You can use Apache <tt>&#x27;httpclient-4.0.2.jar&#x27;</tt> to send HTTP POST data to Servlet as we do with the webpage&#x27;s form. </li><li>We write a convenient <tt>FileServiceClient.java</tt> to handle all the things for sending HTTP POST data, so you just need to create an instance of <tt>FileServiceClient</tt> and invoke <tt>submit()</tt> method. </li><li><tt>[Notice]</tt> Only allowed client IPs can upload files via Apache HttpClient API. Change <a href="http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/war/WEB-INF/appengine-web.xml" rel="nofollow"><tt>WEB-INF/appengine-web.xml</tt></a> if required. </li><pre class="prettyprint">// Google App Engine URL
String url = &quot;https://[your_app_id].appspot.com/upload&quot;;		
// Create an instance of FileServiceClient for Google App Engine
FileServiceClient client = FileServiceClientFactory.getFileServiceClient(url);
// submit(String fileId, String fileOwner, String fileName, byte[] fileData, String contentType)
int fileSize = client.submit(fileId, fileOwner, fileName, fileData, contentType);</pre></ul><h2><a name="How_It_Works"></a>How It Works<a href="#How_It_Works" class="section_anchor"></a></h2><p>With <a href="/p/google-file-service/wiki/GoogleFile">GoogleFile</a> and <a href="/p/google-file-service/wiki/GoogleUnit">GoogleUnit</a> data models (datastore), you can upload a large file of size up to 10 MB very easily via our static method <tt>DatastoreUtils.insertGoogleFile()</tt>. <p> Some code of <a href="http://code.google.com/p/google-file-service/source/browse/trunk/GoogleFileService/src/sinica/googlefileservice/server/servlet/FileUploadServlet.java" rel="nofollow"><tt>FileUploadServlet.java</tt></a> is as follows: </p><pre class="prettyprint">String fileId = &quot;&quot;;
String fileOwner = &quot;&quot;;
String fileName = &quot;&quot;;
int fileSize = -1;
String contentType = &quot;&quot;;
// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload();
// Set overall request size constraint: the default value of -1 indicates that there is no limit.
upload.setSizeMax(10240000); //10MB, the maximum allowed size, in bytes.
// Set the UTF-8 encoding to grab the correct uploaded filename, especially for Chinese
upload.setHeaderEncoding(&quot;UTF-8&quot;);
// Parse the request
FileItemIterator iter = upload.getItemIterator(req);
while (iter.hasNext()) {
    FileItemStream item = iter.next();
    InputStream stream = item.openStream();
    String fieldName = item.getFieldName();
    if (item.isFormField()) {
    	//process a regular form field
        if (fieldName.equals(&quot;fileId&quot;))
            //set the UTF-8 encoding to grab the correct string
            fileId = Streams.asString(stream, &quot;UTF-8&quot;);
        if (fieldName.equals(&quot;fileOwner&quot;))
            //set the UTF-8 encoding to grab the correct string
            fileOwner = Streams.asString(stream, &quot;UTF-8&quot;);
    } else {
    	//process a file upload
        fileName = item.getName();
        if (fileName != null)
    	    fileName= FilenameUtils.getName(fileName);
        contentType = item.getContentType();
        if (fieldName.equals(&quot;upfile&quot;)) {
            // Check if the fileId conforms to the Key format of the Google datastore 
            // and all other uploaded fields are not empty.
            if (DatastoreUtils.isKey(fileId) &amp;&amp; fileOwner.length() &gt; 0 &amp;&amp; fileName.length() &gt; 0) {
                // Save into Google datastore
        	fileSize = DatastoreUtils.insertGoogleFile(fileId, fileOwner, fileName, contentType, stream);
            }
        }
    }
}</pre><h2><a name="Known_Issues"></a>Known Issues<a href="#Known_Issues" class="section_anchor"></a></h2><ol><li>If we submit a large file of size over 10 MB, then we&#x27;ll receive a HTTP Error 413  Request Entity Too Large (Your client issued a request that was too large) from Google App Engine. </li><li>The uploading progress bar works on local development server, but not works on Google App Engine. </li></ol><h2><a name="Questions_or_Suggestions?"></a>Questions or Suggestions?<a href="#Questions_or_Suggestions?" class="section_anchor"></a></h2><p><table cellspacing="0"> <blockquote><tr> 
<td valign="middle"> 
<a href="http://groups.google.com/group/google-file-service" rel="nofollow"> 
<img src="http://groups.google.com/intl/en/images/logos/groups_logo_sm.gif" alt="Google Groups"></img></a>
