package com.qualitestgroup.util.mail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import com.google.common.collect.Lists;

public class Mailer {
		static String reportFile = "";

		private static String hostname;
		private static String emailFrom;
		private static String username;
		private static String password;
		private static String emailTo;
		private static String emailCc, subjectLine,mailBody;
		private static String reportPath;
		private static String propertyFilePath = System
				.getProperty("user.dir")+"\\mailconfig.properties";
		
		
	public static void main(String[] args) {
		System.out.println(propertyFilePath);
		// calling send email
		try {
			Send_ExtentReportMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void Send_ExtentReportMail() throws  Exception {
		
			System.out.println(propertyFilePath);
			Properties properties = new Properties();
			System.out.println("Email test started");
			EmailAttachment attachment = new EmailAttachment();
			
			// Create the email message
			MultiPartEmail email = new MultiPartEmail();
			
			properties.load(new FileInputStream(propertyFilePath));
			
			String sendemail = properties.getProperty("sendemail");
			
			if (sendemail.trim().toUpperCase().equals("YES")) {
					
				hostname = properties.getProperty("hostname");
				username = properties.getProperty("username");
				password = properties.getProperty("password");
				emailTo =  properties.getProperty("emailTo");
				emailCc =  properties.getProperty("emailCc");
				emailFrom = properties.getProperty("emailFrom");
	//			reportPath = properties.getProperty("reportpath"); 
				subjectLine = properties.getProperty("subjectLine");
				mailBody = properties.getProperty("mailBody");
				reportPath = com.qualitestgroup.util.reporting.EventCase.graphReport;
				//graphReport
				
				attachment.setPath(reportPath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription("Extend report");
				
				List<String> Toaddresses = Lists.newArrayList(emailTo.split(","));
				List<String> Ccaddresses = Lists.newArrayList(emailCc.split(","));
				
				try {				
					email.setHostName(hostname);
					email.setSmtpPort(465);
					email.setAuthenticator(new DefaultAuthenticator(username, password));
					email.setSSLOnConnect(true);
					email.setFrom(emailFrom);
	
					for (String ToEmail : Toaddresses) {
						email.addTo(ToEmail);
					}
					
					for (String CcEmail : Ccaddresses) {
					 email.addCc(CcEmail);
					}
					email.setSubject(subjectLine);
					email.setMsg("Hi All" + "\r\n" + "\r\n" + "Rubrik Regression Test report Email. \r\n" +
						"Find attached " + mailBody + "\r\n" + "\r\n" +
						"\r\n Regards, \r\n Team QA");
	
					email.attach(attachment);
					// adding screenshots zip file
					if (ZipUtils.has_screenshots) {
						email.attach(ZipUtils.outputZipFile);
					}
					email.send();
					
					System.out.println("Email sent");
	
				} catch (EmailException e) {
					System.out.println("Error: " + e.getMessage());
					System.out.println("Fail to send  Email as attachment ");
				}
			}
	}
}
