	package com.qualitestgroup.util;
	import java.io.*;

	import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

	import org.xml.sax.*;
import org.w3c.dom.*;
	
	/**
	 * This reads and writes to XML file
	 * Used by Service Tracking to get the case number from 
	 * web services
	 * 
	 * @author Jason Chan
	 *
	 */
	public class XMLHelper {
		public static String caseNumber;
		//static LocalLogger log = new LocalLogger();
		public static void main(String argv[])
		{
			File file = new File("");
			file = new File(file.getAbsolutePath());
			String parentPath = file.getParent(); 
			String xmlFile = parentPath + "\\Webservices_CreateCase\\resources\\CreateCase.xml";
			WriteValueToParam("userType", "CPP", xmlFile);
		}
		
		public static void WriteValueToParam(String paramName, String paramValue, String filePath) {
			//LocalLogger log = new LocalLogger();
			try {
				String xmlFile = filePath; //parentPath + "\\Webservices_CreateCase\\resources\\CreateCase.xml";
				File file = new File(filePath);
				if(file.exists()){
					System.out.println("xml file does exist at the given location.");
					//eo.wait(7000); // Wait so that the create process is fully completed
					
				} else{
					System.out.println("Not able to locate the xml file.");
				}
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(xmlFile);
				
				System.out.println("length: " +doc.getElementsByTagName("parameter").getLength());
				// Get the root element
				//int index = 0;
				for(int i=0; i < doc.getElementsByTagName("parameter").getLength(); i++){
					
					Node param = doc.getElementsByTagName("parameter").item(i);
					String attrName = param.getAttributes().item(0).getNodeValue();
					System.out.println("parameter name?: " + attrName);
					
					// Set case number
					if(attrName.equals(paramName)){
						System.out.println("parameter value was: " + param.getAttributes().item(1).getNodeValue());
						param.getAttributes().item(1).setTextContent(paramValue);
						//index = i;
					}
					
				}
				
				//String paramName = doc.getElementsByTagName("parameter").item(index).getAttributes().item(1).getNodeValue();
				// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(xmlFile));
					transformer.transform(source, result);
			 
					System.out.println("Wrote " + paramName + " to the xml file.");
				 
			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
				
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (SAXException sae) {
				sae.printStackTrace();
			}
			
		}
		public static String ReadBackCasenumber(String filePath) 
		{
	
		 try 
		 {
		  String xmlFile = filePath; //parentPath + "\\Webservices_CreateCase\\resources\\CreateCase.xml";
		  DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		  DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		  Document doc = docBuilder.parse(xmlFile);
		  
		  System.out.println("length: " +doc.getElementsByTagName("parameter").getLength());
		  // Get the root element
		  int index = 0;
		  for(int i=0; i < doc.getElementsByTagName("parameter").getLength(); i++)
		  {
		   
		   Node param = doc.getElementsByTagName("parameter").item(i);
		   String attrName = param.getAttributes().item(0).getNodeValue();
		   System.out.println("parameter name?: " + attrName);
		   
		   // Set case number
		   if(attrName.equals("caseNumber"))
		   {
		    System.out.println("CaseNumber is: " + param.getAttributes().item(1).getNodeValue());
		    caseNumber= param.getAttributes().item(1).getNodeValue();
		    index = i;  
		    System.out.println(index);
		   }
		   
		     }
		 }
		  catch (Exception e)
		  {
		   e.printStackTrace();
		  }
		 return caseNumber;
		  
		 }
	}