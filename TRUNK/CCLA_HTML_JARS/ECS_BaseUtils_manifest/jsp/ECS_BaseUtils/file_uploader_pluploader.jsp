<%@ page import = "idcserver.*" %>
<jsp:useBean id="sb" class="idcserver.ServerBean" />
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="com.ecs.utils.*" %>
<%@ page import="com.ecs.utils.stellent.*" %>
<%@ page import="com.ecs.utils.uploader.*" %>

<%@page import="java.util.Hashtable"%>
<%@page import="javazoom.upload.MultipartFormDataRequest" %>
<%@page import="javazoom.upload.parsing.*" %>

<html>
<head><title>ECS Upload Plus</title></head>


<body  bgcolor="FFFFCC">
<%!
	//Writes the given inputstream to the filesystem
	void writeFile(InputStream is, String fileName, String dir){
		
		try{
		    File f = new File(dir + fileName);
		    
		    OutputStream out = new FileOutputStream(f);
		    byte buf[]=new byte[1024];
		    int len;
		    
		    while((len=is.read(buf))>0){
		    	out.write(buf,0,len);
		    }
		    //System.out.println("Written file to: " + f.getAbsolutePath() + " (" + (f.length()/1024) + "KB)");
		    
		    out.close();
		    is.close();
	    }catch (IOException e){
	    	System.out.println("Unable to create temporary file '" + fileName + "':" + e.getMessage());
	    	e.printStackTrace();
	    }
	}

%>

<%
	try {
		MultipartFormDataRequest data = new MultipartFormDataRequest(request);
	
		int chunkNum = Integer.parseInt(data.getParameter("chunk"));
		int chunkTotal = Integer.parseInt(data.getParameter("chunks"));
		String chunkId = data.getParameter("name");

		
		// The location for saving chunks
		String tempStorePath = System.getProperty("java.io.tmpdir");
		
		//If this is the first chunk
		if(chunkNum == 0){
			System.out.println("Recieving " + chunkTotal +" chunks for chunk with ID '" + chunkId + "'");
		}
		
		System.out.println("Recieving chunk " + (chunkNum+1) + " of " + chunkTotal);
		
		//Write chunk to filesystem
		CosUploadFile fileChunk = (CosUploadFile) data.getFiles().get("file");
		
		writeFile(fileChunk.getInpuStream(), chunkId + "_" + chunkNum, tempStorePath);
				
		
		//If this is the last chunk
		if((chunkNum+1) == chunkTotal){
			
			//Last chunk recieved, assemble them all
			System.out.println("Recieved last chunk, assembling chunks.");
			byte[] buffer = new byte[1024];
			
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File fileChunkTemp = null;
			
			File outputFile = new File(tempStorePath + fileChunk.getFileName());
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			
			//Combine chunks
			try{
				for(int i=0; i<chunkTotal; i++){
					try {
						fileChunkTemp = new File(tempStorePath + chunkId + "_" + i);
						bis = new BufferedInputStream(new FileInputStream(fileChunkTemp));

						int bytesRead = 0;
			            
			            //Keep reading from the file while there is any content
			            //when the end of the stream has been reached, -1 is returned
			            while ((bytesRead = bis.read(buffer)) != -1) {
			                
			                //Process the chunk of bytes read
			                bos.write(buffer, 0, bytesRead);
			            }
					    
					    
					}catch (FileNotFoundException e) {
						String msg = "File does not exist: " 
							+ fileChunkTemp.getAbsolutePath() +":" + e.getMessage();
							
						System.out.println(msg);
						e.printStackTrace();
						throw new Exception(msg);
					}catch (IOException e) {
						String msg = "Error reading/writing to file: " + e.getMessage();
						System.out.println(msg);
						e.printStackTrace();
						throw new Exception(msg);
					}finally{
						bis.close();
						fileChunkTemp.delete();
					}
				}
			}catch(Exception e){
				throw e;
			}finally{
				bos.close();
			}
			
			System.out.println("Successfully combined file chunks");
			
			//Do the checkin etc...
			List fileItems = new ArrayList();
			
			fileItems.add(outputFile);
			
			String id=request.getParameter("id");
			String user=request.getParameter("dUser");
			String type=request.getParameter("type");
			
			// Load all attributes on request into Hashtable
			Hashtable parameters = new Hashtable();
			for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
				String attrName = (String)e.nextElement();
				String attrValue = request.getParameter(attrName);
				parameters.put(attrName, attrValue);
			}
			
			Upload loader = new Upload();
			
			loader.setId(id);
			loader.setType(type);
			loader.setAttributes(parameters);
			
			String responseString = loader.store(fileItems);
			
			out.println(responseString);
		}
		
	} catch (Throwable t) {
		System.out.println("Unable to complete file checkin: " + t.getMessage());
		t.printStackTrace();
		out.println("<p>Error uploading files! " + t.getMessage() + "</p>");
		throw new Exception("Unable to complete file checkin: " + t.getMessage());
	}
%>

<p>&nbsp</p>

</body>
</html>
