package com.qualitestgroup.util.restservices;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;

public class RestServicesMethods {

	/**
	 * Takes end point and entity xml file path as arguments and binds the
	 * entity XML body to the request and executes httpost request and returns
	 * response of type CloseableHttpResponse.
	 * 
	 * @param endPoint
	 *            Web service end point.
	 * @param entityXMLFilePath
	 *            Request xml file path.
	 * @return Returns response object of type CloseableHttpResponse.
	 * @throws Exception
	 */
	public static CloseableHttpResponse getHttpPostResponse(String endPoint, String entityXMLFilePath)
			throws KDTKeywordExecException {
		File entityXMLFile = new File(entityXMLFilePath);
		if (!(entityXMLFile.isFile())) {
			throw new KDTKeywordExecException("File doesn't exists. Invalid File Path: " + entityXMLFilePath);
		}

		return getHttpPostResponse(endPoint, entityXMLFile);
	}

	/**
	 * Takes end point and entity xml file as arguments and binds the entity XML
	 * body to the request and executes httpost request and returns response of
	 * type CloseableHttpResponse.
	 * 
	 * @param endPoint
	 *            Web service end point.
	 * @param entityXMLFile
	 *            Request xml file.
	 * @return Returns response object of type CloseableHttpResponse.
	 */
	public static CloseableHttpResponse getHttpPostResponse(String endPoint, File entityXMLFile)
			throws KDTKeywordExecException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(endPoint);

		FileEntity entityXMLFileentity = new FileEntity(entityXMLFile, ContentType.create("application/xml", "UTF-8"));
		httppost.setEntity(entityXMLFileentity);
		CloseableHttpResponse response = null;

		try {
			response = httpclient.execute(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Error ocurred while posting the request", e);
		}

		return response;
	}

	/**
	 * returns the node value for specific xpath in the response object.
	 * 
	 * @param response
	 *            response object of type CloseableHttpResponse.
	 * @param nodeXpath
	 *            node xpath.
	 * @return returns the value for the specific node xpath
	 */
	public static String getXMLNodeValue(CloseableHttpResponse response, String nodeXpath)
			throws KDTKeywordExecException {
		HttpEntity hEntity = response.getEntity();
		String responseStr = null;
		String xmlNodeValue = null;

		try {
			responseStr = String.valueOf(EntityUtils.toString(hEntity));
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new StringReader(responseStr));
			xmlNodeValue = doc.selectSingleNode(nodeXpath).getText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Error occured while fetching the value for Node: " + nodeXpath, e);
		}

		return xmlNodeValue;
	}

	/**
	 * Sets the node value to the specific node in the xml file and returns the
	 * file object.
	 * 
	 * @param XMLFilePath
	 *            XML file path.
	 * @param nodeXpath
	 *            XML of specific node.
	 * @param nodeValue
	 *            value for specific node.
	 * @return returns the file object.
	 */
	public static File setXMLNodeValue(String XMLFilePath, String nodeXpath, String nodeValue)
			throws KDTKeywordExecException {
		File xmlFile = new File(XMLFilePath);

		if (!(xmlFile.isFile())) {
			throw new KDTKeywordExecException("File doesn't exists. Invalid File Path: " + XMLFilePath);
		}

		return setXMLNodeValue(xmlFile, nodeXpath, nodeValue);
	}

	/**
	 * Sets the node value to the specific node in the xml file and returns the
	 * file object.
	 * 
	 * @param XMLFile
	 *            XML file.
	 * @param nodeXpath
	 *            XML of specific node.
	 * @param nodeValue
	 *            value for specific node.
	 * @return returns the file object.
	 */
	public static File setXMLNodeValue(File XMLFile, String nodeXpath, String nodeValue)
			throws KDTKeywordExecException {
		SAXReader saxReader = new SAXReader();
		try {
			Document xmldocument = saxReader.read(XMLFile);
			xmldocument.selectSingleNode(nodeXpath).setText(nodeValue);
			FileWriter fw = new FileWriter(XMLFile);
			XMLWriter writer = new XMLWriter(fw);
			writer.write(xmldocument);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Error occurred while setting the value for Node Xpath: " + nodeXpath
					+ " ;Node Value: " + nodeValue, e);
		}
		return XMLFile;
	}

	/**
	 * Converts response XML in string type to Document type.
	 * 
	 * @param XmlResponseString
	 *            response in XML format
	 * @return returns object of type Document
	 */
	public static Document stringToDocument(String XmlResponseString) throws KDTKeywordExecException {
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			doc = saxReader.read(new StringReader(XmlResponseString));
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Error occurred while converting the String to Document object", e);
		}
		return doc;
	}

	/**
	 * Converts CloseableHttpResponse object to Document object
	 * 
	 * @param closeableHttpResponse
	 * @return return the document object
	 */
	public static Document httpPostResponseToDocument(CloseableHttpResponse closeableHttpResponse)
			throws KDTKeywordExecException {
		HttpEntity httpEntity = closeableHttpResponse.getEntity();
		Document doc = null;
		String closeableHttpResponseStr = "";
		SAXReader saxReader = new SAXReader();
		if (httpEntity != null) {
			long len = httpEntity.getContentLength();
			System.out.println(len);
			try {
				closeableHttpResponseStr = String.valueOf(EntityUtils.toString(httpEntity));
				doc = saxReader.read(new StringReader(closeableHttpResponseStr));
			} catch (Exception e) {
				e.printStackTrace();
				throw new KDTKeywordExecException(
						"Error occurred while converting the HTTPPOST response to Document object", e);
			}
		}

		return doc;
	}

	public static File setTagValueInXmlFile(File file, String XMLTagXpath, String tagValue)
			throws KDTKeywordExecException, IOException {
		SAXReader reader = new SAXReader();
		Document queryxmldocument = null;
		FileWriter fw = null;

		try {
			queryxmldocument = reader.read(file);
			queryxmldocument.selectSingleNode(XMLTagXpath).setText(tagValue);
			fw = new FileWriter(file);
			queryxmldocument.write(fw);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KDTKeywordExecException(
					"Error occurred while setting the Node value for file: " + file.getCanonicalPath(), e);
		}

		return file;
	}

	public static File replaceValueInFileObject(File file, String oldString, String newString)
			throws KDTKeywordExecException {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = null;
			document = saxReader.read(file);
			String fileStr = document.asXML();
			fileStr = fileStr.replace(oldString, newString);
			document = stringToDocument(fileStr);
			FileWriter fw;
			fw = new FileWriter(file);
			XMLWriter writer = new XMLWriter(fw);
			writer.write(document);
			writer.close();

		} catch (DocumentException e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Error occurred while converting the String to Document object", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("IOException", e);
		}
		return file;
	}

}
