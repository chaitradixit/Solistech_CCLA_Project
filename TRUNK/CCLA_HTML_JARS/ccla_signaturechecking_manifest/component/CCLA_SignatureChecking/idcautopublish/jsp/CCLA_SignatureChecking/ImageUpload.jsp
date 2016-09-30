<%@ page import="java.io.*, java.util.*" %>
<%@ page import="com.ecs.utils.stellent.*" %>
<%@ page import="com.ecs.utils.stellent.embedded.*" %>
<%@ page import="com.ecs.stellent.ccla.signature.*" %>
<%@ page import="com.ecs.stellent.ccla.signature.data.*" %>
<%

	
	
	//to get the content type information from JSP Request Header
	String contentType = request.getContentType();
	//for saving the file name
		
	//here we are checking the content type is not equal to Null andas well as the passed data from mulitpart/form-data is greater than or equal to 0
	if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
 		
 		System.out.println("OVERHERE!!!!");
 		Enumeration enumeration = request.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String value = request.getHeader(name);
			System.out.println("Name:"+name+", value="+value);
		}
		
 		DataInputStream in = new DataInputStream(request.getInputStream());
		//we are taking the length of Content type data
		int formDataLength = request.getContentLength();
		byte dataBytes[] = new byte[formDataLength];
		int byteRead = 0;
		int totalBytesRead = 0;
		//this loop converting the uploaded file into byte code
		while (totalBytesRead < formDataLength) {
			byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
			totalBytesRead += byteRead;
		}
		
		System.out.println("bytes read: "+byteRead+", totalRead:"+totalBytesRead);
		
		
		String file = new String(dataBytes);
		
		// The following will take all the parameters from the header and parses them to a hashmap 
		int valid = 0;
		HashMap mapping = new HashMap();
		String  nameVal = null;
		String contentVal = null;
		int endVal = 0;		
		
		while (valid>=0)
		{
			endVal = 0;
			String searchStr = "name=\"";
			valid = file.indexOf("name=\"", valid);
			if (valid>0) {
					valid = valid+searchStr.length();
					endVal = file.indexOf("\"", valid);
				if (endVal>0) {					
					nameVal = file.substring(valid,endVal);
					//incrememt by two to get to the value line
					valid = valid + searchStr.length() + nameVal.length()-1;
					contentVal = file.substring(valid,file.indexOf("\n",valid));
					System.out.println("nameVal: "+nameVal +", contentVal: "+contentVal);

					mapping.put(nameVal, contentVal);
					valid = valid+1;
				}
			}
		}
		
		System.out.println("fileBytes:"+file);

		int lastIndex = contentType.lastIndexOf("=");
		String boundary = contentType.substring(lastIndex + 1, contentType.length());
		

		int pos;
		String uniqueImage = null;
		File saveFile = null;
		
		//extracting the index of file 
		pos = file.indexOf("filename=\"");
		if (pos>=0) {
			uniqueImage = file.substring(pos+10, file.indexOf("\"", pos+11));
		} else {
			uniqueImage = "image_"+System.currentTimeMillis()+".jpg";
		}
		
		System.out.println("uniqueImage:"+uniqueImage);


		saveFile = new File(getServletContext().getRealPath("\\")+"\\CCLA_SignatureChecking\\images\\"+uniqueImage);

		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		System.out.println("pos:"+pos);

		int boundaryLocation = file.indexOf(boundary, pos) - 4;
				System.out.println("boundaryLocation:"+boundaryLocation);

		int startPos = ((file.substring(0, pos)).getBytes()).length;
				System.out.println("startPos:"+startPos);

		int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
				System.out.println("endPos:"+endPos);


		// creating a new file with the same name and writing the content in new file
		FileOutputStream fileOut = new FileOutputStream(saveFile);
				
		fileOut.write(dataBytes, startPos, (endPos - startPos));
		fileOut.flush();
		fileOut.close();

		String userName = mapping.get("userName").toString().trim();
		String action = mapping.get("action").toString().trim();
		String sourceDocIdStr = mapping.get("sourceDocId").toString().trim();
		
		// Capture Source Doc ID, if it was passed in the mapping.
		Integer sourceDocId = null;
		
		if (sourceDocIdStr != null && sourceDocIdStr.length() > 0) {
			sourceDocId = Integer.parseInt(sourceDocIdStr);
		}
		
		System.out.println
		 ("userName:" + userName + " |action:" + action + " |sourceDocId:" + sourceDocId);
		
		LWDocument lwDoc = new LWDocument();
		FWFacade f = new FWFacade("SystemDatabase");
		
		String docName = "";
		
		if (action.equals("add")){
		
			String personId = uniqueImage.substring(0,uniqueImage.length()-4);		
			lwDoc.setTitle("Signature for: "+personId);
			lwDoc.setAttribute("xPersonId", personId);
			lwDoc.setDocType("Signature");
			lwDoc.setSecurityGroup("Public");
			lwDoc.setAuthor(userName);
			docName = lwDoc.checkin(saveFile);
			
			//Now update the PERSON_SIGNATURE table
			try {
				PersonSignature.add(Integer.parseInt(personId), docName, sourceDocId, userName, f);	
			}  catch (Exception e2) {
				System.out.println("adding person signature "+docName+": "+e2.getMessage());
			}

		
		} else if (action.equals("update")){
			//remove .jpg from file name and extract docName and docValues
			docName = uniqueImage.substring(0,uniqueImage.length()-4);		
						
			System.out.println("docname: "+docName+ " |docAuthor:"+userName );
			
			lwDoc.useDatabase();
			lwDoc.setDocName(docName);
			String personId = lwDoc.getAttribute("xPersonId");
			try {
				lwDoc.checkout();
			} catch (Exception e) {
				System.out.println("error with checking out docName "+docName+", carrying on anyway! "+e.getMessage());
			}
			lwDoc.setAuthor(userName);
			lwDoc.checkin(saveFile);	
			
			//Now update the PERSON_SIGNATURE table
			try {
			PersonSignature.update(Integer.parseInt(personId), sourceDocId, userName, f);
			}  catch (Exception e2) {
				System.out.println("adding person signature "+docName+": "+e2.getMessage());
			}
		}

		System.out.println("finished writing data");
		try {
			if (saveFile.exists()) {
				boolean isDeleted = saveFile.delete();
				if (!isDeleted) {
					System.out.println("Cannot delete Signature File "+saveFile.getName());
				} else {
					System.out.println("Deleted Signature File");
				}
			}
		} catch (Exception e) {
			System.out.println("Error deleting Signature File "+saveFile.getName());
		}
		%>
		<% out.println(docName.trim()); %>
		<%
		} 
%>