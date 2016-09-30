<%@ page import = "idcserver.*" %>
<jsp:useBean id="sb" class="idcserver.ServerBean" />
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="com.ecs.utils.*" %>
<%@ page import="com.ecs.utils.stellent.*" %>
<%@ page import="com.ecs.utils.uploader.*" %>
<html>
<head><title>ECS Upload Plus</title></head>


<body  bgcolor="FFFFCC">
<%
    String id=request.getParameter("id");
    String user=request.getParameter("dUser");
    String type=request.getParameter("type");
    
    // MAX_FILE_SIZE - maximum file size expressed in MB
    final int MAX_FILE_SIZE = 100;
    // MAX_MEMORY_SIZE - maximum size that will be stored in memory expressed in MB
    // SL NOTE: setting this greater than 7 causes upload failure.
    final int MAX_MEMORY_SIZE = 1;
    
    // Load all attributes on request into Hashtable
    Hashtable parameters = new Hashtable();
    for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
      String attrName = (String)e.nextElement();
      String attrValue = request.getParameter(attrName);
      parameters.put(attrName, attrValue);
    }

		List fileItems = null;
    try {
      System.out.println(
        "File Uploader called with parameters id=" + id + "/ user=" + user + "/ type=" +
        type + "[session: " + session.getId() + " size: " +
        request.getContentLength() + "]");

      DiskFileUpload fu = new DiskFileUpload();
      // maximum size before a FileUploadException will be thrown
      fu.setSizeMax(MAX_FILE_SIZE * 1024 * 1024);
      // maximum size that will be stored in memory
      fu.setSizeThreshold(MAX_MEMORY_SIZE * 1024 * 1024);
      
      System.out.println(
        "MAX_FILE_SIZE=" + fu.getSizeMax() + " bytes, MAX_MEMORY_SIZE=" + fu.getSizeThreshold() + " bytes");
        
      // the location for saving data that is larger than getSizeThreshold()
      String storePath = System.getProperty("java.io.tmpdir");
      System.out.println("File Uploader setting temporary store path to : " + storePath);
      fu.setRepositoryPath(storePath);

      fileItems = fu.parseRequest(request);
      
      Upload loader = new Upload();
      
      loader.setId(id);
      loader.setType(type);
      loader.setAttributes(parameters);
      
      String responseString = loader.store(fileItems);
			
      out.println(responseString);
    } catch (Throwable ex) {
      ex.printStackTrace();
      out.println("<p>Error uploading files!</p>");
      if (ex.getMessage().indexOf("size exceeds allowed range") > -1) {
      	out.println(
      	  "[A selected file exceeds the file upload limit of " + MAX_FILE_SIZE + "MB]");
      } else {
        out.println("[" + ex.getMessage() + "]");
      }
    } finally {
      System.out.println("Deleting temporary files");
      if (fileItems != null) {
        Iterator item = fileItems.iterator();
        while(item.hasNext()) {
				  FileItem fi = (FileItem) item.next();
				  fi.delete();				    
				}
      }
    }
%>

<p>&nbsp</p>

</body>
</html>
