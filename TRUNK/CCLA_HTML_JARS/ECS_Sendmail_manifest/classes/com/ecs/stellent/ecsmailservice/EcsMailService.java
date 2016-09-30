/*
 * Created on 21-Jul-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ecs.stellent.ecsmailservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.File;
import intradoc.apputilities.idccommand.IdcExecuteServer;
import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataBinder;
import intradoc.data.DataResultSet;
import intradoc.server.InternetFunctions;
import intradoc.server.Service;
import intradoc.server.UserStorage;
import intradoc.shared.SharedObjects;
import intradoc.common.DynamicHtml;
import intradoc.server.PageMerger;
import intradoc.server.PageMergerData;
import intradoc.server.DataLoader;
import intradoc.shared.UserData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.ecs.utils.stellent.*;
import com.ecs.utils.*;
import org.springframework.mail.javamail.*;


/**
 * @author Andrew Smark
 * 
 * This code can be executed by any IDC Service that has a service type of
 * "com.ecs.stellent.EcsMailService.EcsMailService".
 * 
 */
public class EcsMailService extends Service {
	/**
	 * Debug flag set to true by adding debug=y to binder on sendMail() call
	 */
	private boolean isDebug = false;

	/**
	 * Name of the default Stellent template to use for the email message body
	 * if no <code>template</code> parameter passed to service.
	 */
	public static final String DEFAULT_TEMPLATE="ECS_MAIL_SIMPLE";
	
	/**
	 * This code will create an html page based on the data in the databinder
	 * and e-mail to the Recipient specified by the user. The e-mail template
	 * and e-mail subject heading is determined by the databinder.
	 * 
	 * Note: to force the mail to be sent from the sysadmin email account as
	 * opposed to the user mail address of the user session calling this
	 * service add the <code>emailFromAddresss</code> (yes, with 3 s's) to the
	 * binder and pass it the <code>SysAdminAddress</code> field from the 
	 * Stellent config.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void sendMail() throws DataException, ServiceException, IOException, Exception {
		boolean allowAliases = false;

		/*
		 * Get the following data from the DataBinder: + Recipient + E-mail
		 * Subject + E-mail template
		 */

		System.out.println(
			"EcsMailService called - set debug=y in sendMail() binder for logging");

		String debug = m_binder.getLocal("debug");
		if ((debug != null) && (debug.equalsIgnoreCase("y")))
			isDebug = true;
		
		log("EcsMailService: START");
		
		String help = m_binder.getLocal("help");
		if (help != null) {
			log("help parameter passed on service call, throwing exception with help details");
			String s = "Parameters available are:";
			s += "\n\tdebug=Y [optional, sends debug info to server out]";
			s += "\n\trecipient [optional, either a Stellent user or alias who should receive the email]";
			s += "\n\temail [optional, if set it overrides recipient and should be a valid email address]";
			s += "\n\tusersAndAliases=Y [mandatory if the passed recipient is an Stellent alias otherwise the recipient is assumed to be a Stellent user]";
			s += "\n\ttemplate [optional, the name of the Stellent template for generating the email body. If not set the default template " + EcsMailService.DEFAULT_TEMPLATE + " is used]";
			s += "\n\tsubject [optional, the email subject]";
			s += "\n\tsendAttachments=Y [mandatory if sending file attachments either by passing attachments or by posting a file to this service]";
			s += "\n\tattachments [optional, a comma separated list of Stellent Content IDs which will be added as file attachments to the email]";
			s += "\n\tdatafile [if POSTing a file set its name=datafile in the form to ensure the original filename is used on the email attachment.  If name is not set to datafile the temporary file name will be used]";
			
			throw new ServiceException(s);
		}
		
		String recipient = m_binder.getLocal("recipient");
		String email = m_binder.getLocal("email");
		String subject = m_binder.getLocal("subject");
		String template = m_binder.getLocal("template");
		if (template == null || template.length() == 0) {
			log("No email template defined, using default of " + EcsMailService.DEFAULT_TEMPLATE);
			template = EcsMailService.DEFAULT_TEMPLATE;
		}
		
		String usersAndAliases = m_binder.getLocal("usersAndAliases");
		
		String attachments = m_binder.getLocal("attachments");
		boolean sendAttachments = false;
		if (m_binder.getLocal("sendAttachments") != null && 
			m_binder.getLocal("sendAttachments").equalsIgnoreCase("y")) {
			sendAttachments = true;
		}
		
		Properties p = m_binder.getEnvironment();
		log(p.toString());
		p = m_binder.getLocalData();
		log(p.toString());
		//TBD
		
		log("\trecipient : " + recipient);
		log("\temail : " + email);
		log("\tsubject : " + subject);
		log("\ttemplate : " + template);
		log("\tusersAndAliases : " + usersAndAliases);
		log("\tsendAttachments : " + sendAttachments);
		log("\tattachments : " + attachments);


		// If an email address has been specified just send the mail to that
		// address, otherwise expect
		// a list of usernames and aliases
		if (email != null) {

			boolean wasMsgSent = true;
			if (sendAttachments) {
				wasMsgSent = sendJavaMail(email, template, subject, attachments);
			} else {
				wasMsgSent = 
					InternetFunctions.sendMailTo(
							email, template, subject, this) ;
			}
			
			if (wasMsgSent) {
				log(
					"The mail message was sent to :" + email);
			} else {
				log(
					"The mail message to :" + email +
					" failed. Check Stellent error log for details");
			}

		} else {

			if ((usersAndAliases != null) && 
				(usersAndAliases.equalsIgnoreCase("y"))) {
				log(
					"Will check recipient " + recipient + 
					" against Stellent Aliases");
				allowAliases = true;
			} else {
				log(
					"Alias checking [usersAndAliases=y] not set, recipient " +
					recipient +	" assumed to be Stellent user");	
			}

			ArrayList recipients = new ArrayList();

			if (allowAliases) {

				if (!getUsersForAlias(recipients, recipient)) {
					recipients.add(recipient);
				}

			} else {
				recipients.add(recipient);
			}

			Iterator recipientsIterator = recipients.iterator();

			while (recipientsIterator.hasNext()) {

				String user = (String) recipientsIterator.next();

				/*
				 * Get the e-mail address for the recipient
				 */
				String emailAddress = UserStorage
						.retrieveUserDatabaseProfileData(user, m_workspace,
								this).getProperty("dEmail");

				if (emailAddress == null || emailAddress.length() == 0) {
					logE("No email address found for user " + user);
				} else {
					log(
						"Sending user " + user + " email on address : " +
						emailAddress);
	
					/*
					 * This command will send the e-mail to the client. The message
					 * sent is a template (emailTemplate), and the data to format
					 * the message is taken out of m_binder, contained in "this".
					 */
					boolean wasMsgSent = true;
					if (sendAttachments) {
						wasMsgSent = sendJavaMail(emailAddress, template, subject, attachments);
					} else {
						InternetFunctions.sendMailTo(emailAddress,
							template, subject, this);
					}	
	
					if (wasMsgSent) {
						log(
							"The mail message was sent to :" + user + " (" +
							emailAddress + ")");
					} else {
						logE(
							"The mail message to :" + user + " (" +
							emailAddress + 
							") failed. Check Stellent error log for details");
					}
				}
			}	
		}
		
		m_binder.putLocal("ECS_MAIL_response", "Request on subject " + subject + " completed");
		log("EcsMailService: END");

	}

	private DataBinder callService(DataBinder serviceBinder)
			throws ServiceException, IOException {
		IdcExecuteServer server = new IdcExecuteServer();
		DataBinder response = new DataBinder();

		// Do I need to get the current logged in user?
		server.init("sysadmin", null);
		server.connectToServer();

		response.receive(new BufferedReader(new StringReader(server
				.executeCommand(serviceBinder))));

		String sMessage = response.getLocal("StatusMessage");

		if (!sMessage.equalsIgnoreCase("!syOK")) {
			throw new ServiceException("Exception:ChangeStatus[" + sMessage
					+ "]");
		}

		return response;
	}

	private int getIndex(intradoc.data.DataResultSet oData,
			java.lang.String sField) {
		int numOfFields = oData.getNumFields();
		for (int x = 0; x < numOfFields; x++)
			if (sField.compareToIgnoreCase(oData.getFieldName(x)) == 0)
				return x;

		return -1;
	}

	private boolean getUsersForAlias(ArrayList usersList, String alias)
			throws ServiceException, IOException {

		boolean returnValue = false;

		DataBinder aliasBinder = new DataBinder();

		aliasBinder.putLocal("dAlias", alias);
		aliasBinder.putLocal("IdcService", "ECS_GETALIASUSERS");
		aliasBinder.putLocal("IsJava", "1");

		DataBinder response = callService(aliasBinder);

		DataResultSet users = (intradoc.data.DataResultSet) response
				.getResultSet("rsUsers");

		log("How many users on Alias " + alias + "? : " + users.getNumRows());

		if (users.getNumRows() == 0) {
			return false;
		}

		users.first();

		String user = null;
		do {
			user = users.getStringValue(getIndex(users, "dUserName"));
			returnValue = true;
			usersList.add(user);
		} while (users.next());

		return returnValue;
	}
	
	private void logE(String s) {
		log(s, true);
	}

	private void log(String s) {
		log(s, false);
	}
	
	private void log(String s, boolean error) {
		if (isDebug) {
			if (error) {
				System.out.println("ERROR: " + s);
			} else {
				System.out.println(s);
			}
		}
		
		// If ECSBaseUtil log exists in scope output debug log line
		try {
			if (error) {
				com.ecs.utils.Log.error(s);
			} else {
				com.ecs.utils.Log.debug(s);
			}
		} catch (Exception e) {
			//Swallow exception Log class not found, ignore
		}
		
	}
	
	private boolean sendJavaMail(String email, String template, String subject, String attachments) throws Exception{
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		// Work out from address details
		String fromAddress = m_binder.getLocal("emailFromAddresss");
		UserData userData = (UserData)getCachedObject("UserData");
		if (userData != null && (fromAddress == null || fromAddress.length() == 0)) {
			fromAddress = userData.getProperty("dEmail");
		}
		
		log("Using from address of " + fromAddress);

		String host = SharedObjects.getEnvironmentValue("MailServer");
		String port = "25";
		if (SharedObjects.getEnvironmentValue("SmtpPort") != null &&
			SharedObjects.getEnvironmentValue("SmtpPort").length() > 0) {
			port = SharedObjects.getEnvironmentValue("SmtpPort");
		}
		
		mailSender.setHost(host);
		mailSender.setPort(Integer.parseInt(port));

		log("Initiated connect to host:port " + host + ":" + port);

	    // Define message
		MimeMessage message = mailSender.createMimeMessage();
		if (fromAddress != null && fromAddress.length() > 0) {
			message.setFrom(new InternetAddress(fromAddress));
		}
	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
	    if (subject != null && subject.length() > 0) {
	    	message.setSubject(getEmailSubject(subject));
	    }

	    // create the message part
	    MimeBodyPart messageBodyPart = new MimeBodyPart();
	    String msgBody = getEmailMessage(template);
	    messageBodyPart.setText(msgBody);
	    log("Email message is " + msgBody);
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(messageBodyPart);

	    Vector tempFiles = new Vector();
	    
	    // Add any attachments
	    if (attachments != null && attachments.length() > 0 ) {
	    	String[] attachment = attachments.split(",");
	    
	    	for (int i=0; i<attachment.length; i++) {
	    		// Instantiate document but ensure its done using the
	    		// current user to avoid any security issues
	    		LWDocument lwd = new LWDocument(LWFacade.DEFAULT_SERVER, m_binder.getLocal("dUser"), LWFacade.DEFAULT_PORT, attachment[i]);
	    		String fileAttachment =
	    			FileUtils.readBytesToFile(
	    				lwd.getContent(), lwd.getAttribute("dExtension"));
	    		tempFiles.add(fileAttachment);
	    		
			    messageBodyPart = new MimeBodyPart();
			    DataSource source = new FileDataSource(fileAttachment);
			    messageBodyPart.setDataHandler(new DataHandler(source));
			    messageBodyPart.setFileName(lwd.getAttribute("dOriginalName"));
			    multipart.addBodyPart(messageBodyPart);
	    	}
	    }
	    
	    Vector binderAttachments = m_binder.getTempFiles();
	    // This only supports upload of first file using its original name at 
	    // present
	    String filename = m_binder.getLocal("datafile");
	    if (filename != null && filename.length() > 0) {
	    	filename = (new File(filename)).getName();
	    }
	    
	    for (int i=0; i<binderAttachments.size(); i++) {
	    	String fileAttachment = (String)binderAttachments.get(i);
	    	if (filename == null || filename.length() == 0) {
	    		log(
	    			"WARNING: POSTed file did not have name=datafile, " +
	    			"the file's temporary file name will be used instead");
	    		filename = (new File(fileAttachment)).getName();
	    	}
	    	if (i>0) {
	    		log(
	    			"Warning: multiple attachments on call but only one filename " +
	    			filename + " available for naming, using temp file name");
	    		filename = (new File(fileAttachment)).getName();
	    	}
	    	log(
	    		"Got temp file for attachment of " + fileAttachment +
	    		" [attachment name is " + filename + "]");
	    	messageBodyPart = new MimeBodyPart();
		    DataSource source = new FileDataSource(fileAttachment);
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(filename);
		    multipart.addBodyPart(messageBodyPart);
	    }
	    
	    // Put parts in message
	    message.setContent(multipart);

	    // Send the message
	    mailSender.send(message);	
	    
	    // Delete any temp files created
	    for (int i=0; i<tempFiles.size(); i++) {
	    	File f = new File((String)tempFiles.get(i));
	    	try {
	    		f.delete();
	    	} catch (Exception e) {}
	    }
	    return true;
	}
	
	private String getEmailMessage(String template) throws Exception {
	    PageMergerData.loadTemplateData(template, m_binder.getLocalData());
	    DataLoader.checkCachedPage(template, this);
	    DynamicHtml dynamichtml = SharedObjects.getHtmlPage(template);
	    StringWriter stringwriter = new StringWriter();
	    dynamichtml.outputHtml(stringwriter, (PageMerger)getCachedObject("PageMerger"));
	    stringwriter.close();
	    return stringwriter.toString();
	}
	
	private String getEmailSubject(String subject) throws Exception {
		return ((PageMerger)getCachedObject("PageMerger")).evaluateScript(subject);
	}
}
