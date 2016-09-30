package com.ecs.applet;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.JApplet;

public class ClipboardImageUploadApplet extends JApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String boundary 		= "---------------------------7d226f700d0";
	private final static String FIELD_NAME 		= "image";
	private final static String formatName		= "jpeg";
	private final static String dataMimeType 	= "image/jpeg"; 
	
    private Clipboard clipboard;
    private Toolkit toolkit;
	
	private HttpURLConnection connection;
	private DataOutputStream output;
	
	
	//***** Parameter variables from applet page **** 
	//userName of current user who loaded the applet.
	private String userName;
	//location of the service to post the data to
	private String targetUrl;
	//http browser cookie
	private String mycookie;

	//Max image width and height
	private Integer maxImageHeight = null;
	private Integer maxImageWidth = null;
	
	public String getClipboardImageURL
    (String personID, String action) {
		return getClipboardImageURL(personID, null, action);
	}
	
    public String getClipboardImageURL
     (String personID, String sourceDocId, String action){
    	
        String url = "";
        
        if (personID==null || personID.length()==0) {
        	 //JOptionPane.showMessageDialog(null, "No person ID specified.");
        	 return "ERROR: No person ID specified.";
        }
        
        if (!personID.endsWith(".jpg")) {
        	personID = personID+".jpg";
        }
        
        try {
            DataFlavor dataFlavor = DataFlavor.imageFlavor;
            Object object  = null;

            try {
                object = clipboard.getContents(null).getTransferData(dataFlavor);
            } catch (Exception e){
                //JOptionPane.showMessageDialog(null, "No image found on clipboard.");
                return "ERROR: No image found on clipboard.";
            }
     
            BufferedImage bimg = (BufferedImage) object;
        	
        	if (maxImageHeight < bimg.getHeight()){
        		 //JOptionPane.showMessageDialog(null, "This image is too tall");
                 return "ERROR: This image is too tall";
        	}
        	if (maxImageWidth < bimg.getWidth()){
        		 //JOptionPane.showMessageDialog(null, "This image is too wide.");
                 return "ERROR: This image is too wide";
        	}
            
        	if (sourceDocId == null)
        		sourceDocId = ""; // Allow empty value to be passed.
        	
            startPOSTRequest(personID, dataMimeType, targetUrl, action, sourceDocId);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Iterator iter = ImageIO.getImageWritersByFormatName(formatName);
            
            if (iter.hasNext()) {
              ImageWriter writer = (ImageWriter) iter.next();
              ImageWriteParam iwp = writer.getDefaultWriteParam();
              iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
              iwp.setCompressionQuality(1.0f);

              ImageOutputStream ios = new MemoryCacheImageOutputStream(baos);
              writer.setOutput(ios);
              writer.write(bimg);
              byte[] b = baos.toByteArray();
              this.output.write(b, 0, b.length);
            }
            
            endPOSTRequest();
            url = getServerFeedback();
        } catch (Exception exc){
        	exc.printStackTrace();
            return "ERROR: "+exc.getMessage();
        } 
        return url;
    }

    public void init() 
    {
    	System.out.println("ECS: Initialising applet");
        toolkit = Toolkit.getDefaultToolkit();
        clipboard = toolkit.getSystemClipboard();
        
        //Initalise parameter variables
        userName = this.getParameter("userName");
        targetUrl = this.getParameter("targetUrl");
        maxImageHeight = Integer.parseInt(this.getParameter("maxImageHeight"));
        maxImageWidth = Integer.parseInt(this.getParameter("maxImageWidth")); 
        mycookie = this.getParameter("mycookie");
        
        System.out.println("ECS: Applet initialised with: userName["+userName +
        		"], targetUrl["+targetUrl+"], maxImageHeight["+maxImageHeight +
        		"], maxImageWidth["+maxImageWidth+"], mycookie["+mycookie+"]");
    }	
    
    
    private boolean startPOSTRequest(String fileName, String dataMimeType, 
     String uploadURL, String action, String sourceDocId) {
        try {
        	URL url = new URL(uploadURL); 
		    connection = (HttpURLConnection) url.openConnection(); 
		    connection.setDoOutput(true);
		    connection.setRequestMethod("POST");
		    connection.setDoInput(true); 
		    connection.setUseCaches(false);
		    connection.setRequestProperty("Cookie", mycookie);
		    
		    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		    output = new DataOutputStream(connection.getOutputStream());
		    
		    output.writeBytes("--" + boundary + "\r\n"); // throws IOException		    
		    output.writeBytes("Content-Disposition: form-data; name=\"action\"\r\n");
		    output.writeBytes("\r\n");
		    output.writeBytes(action+"\r\n");
		    
		    output.writeBytes("--" + boundary + "\r\n"); // throws IOException		    
		    output.writeBytes("Content-Disposition: form-data; name=\"sourceDocId\"\r\n");
		    output.writeBytes("\r\n");
		    output.writeBytes(sourceDocId+"\r\n");
		    
		    output.writeBytes("--" + boundary + "\r\n"); // throws IOException		    
		    output.writeBytes("Content-Disposition: form-data; name=\"userName\"\r\n");
		    output.writeBytes("\r\n");
		    output.writeBytes(userName+"\r\n");
  
		    output.writeBytes("--" + boundary + "\r\n"); // throws IOException
		    output.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"" + fileName + "\"\r\n");
		    output.writeBytes("Content-Type: " + dataMimeType + "\r\n");
		    output.writeBytes("Content-Transfer-Encoding: binary\r\n\r\n");
		    
        } catch (Exception e) {
			e.printStackTrace();
		    return false; 
		}
        return true;
	}
    
    private boolean endPOSTRequest() 
    {
    	try {
	        output.writeBytes("\r\n--" + boundary + "--\r\n\r\n");
	        output.flush(); // throws IOException
    	} catch (Exception e) {
	        e.printStackTrace();
	        return false; // Problem
    	}
    	return true;
    }    
    
    private String getServerFeedback()
    {
    	if (connection == null) {
    		return null;
    	}

    	BufferedReader input = null;
    	StringBuffer answer = new StringBuffer();
    	
    	try {    		
    		input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    		String answerLine = null;
    		
    		do {
    			answerLine = input.readLine();
	    		if (answerLine != null) {
	    			answer.append(answerLine + "\n");
	    		}
    		} while (answerLine != null);
    	
    	} catch (Exception e) {
   		 	//JOptionPane.showMessageDialog(null, "There was an error: "+ e.getMessage());

	    	e.printStackTrace();
	    	return "ERROR: There was an error, "+e.getMessage();
    	} finally {
	    	if (input != null) {
	    		try { input.close(); } catch (IOException ioe) {}
	    	}
    	}
    	return answer.toString();
    }    
}
