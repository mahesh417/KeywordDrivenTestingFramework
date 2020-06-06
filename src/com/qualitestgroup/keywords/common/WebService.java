package com.qualitestgroup.keywords.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.util.EggTimer;
import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.getproperty.GetProperty;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.util.logging.Logger;

public class WebService extends KeywordGroup {

	static GetProperty getProps = new GetProperty();
	public static GetElementIdentifier gei = new GetElementIdentifier();
	private static HttpURLConnection con = null;
	private static String stringAppendedUrl;

	/**
	 * Description: Method to get appended URL
	 * 
	 * @return appendedUrl
	 */
	public static URL getAppendedUrl() {
		URL appendedUrl = null;
		try {
			appendedUrl = new URL(stringAppendedUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return appendedUrl;
	}

	/**
	 * Description: Method to get connection
	 * 
	 * @return con
	 */
	public static HttpURLConnection getConnection() {
		return con;
	}

	/**
	 * Description: Method used to get response from ECR
	 * 
	 * @param property
	 *            - Required property which is declared in
	 *            configuration.properties file
	 * @param itemId
	 *            - Required ItemID
	 * @param application
	 *            - CURR_APP
	 * @return ECR Response in XML
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getECRResponse(String property, String itemId, String application)
			throws KDTKeywordExecException {
		String ecr_url = getProps.getPropertyValue(property, application);
		String ecr_response = WebService.getResponseFromURL(ecr_url.concat(itemId));
		return ecr_response;
	}

	/**
	 * Description: Method used to get response from ECR for different versions
	 * 
	 * @param property
	 *            - Required property which is declared in
	 *            configuration.properties file
	 * @param itemId
	 *            - Required ItemID
	 * @param application
	 *            - CURR_APP
	 * @return ECR Response in XML
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getECRResponseForVersions(String property, String itemId, String version, String application)
			throws KDTKeywordExecException {
		String ecr_url = getProps.getPropertyValue(property, application);
		String ecr_response = WebService
				.getResponseFromURL(ecr_url.replace("{itemid}", itemId).replace("{rsn}", version));
		return ecr_response;
	}

	/**
	 * Description: Method used to convert XML file to document object
	 * 
	 * @param file
	 *            - Required XML file path in String object to convert into
	 *            document object
	 * @return Document object after conversion
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static Document convertXmlFileToDoc(String file) throws KDTKeywordExecException {
		Document doc = null;
		File filename = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(file));
			filename = new File(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Not able to convert XML file - <b>" + filename.getName()
					+ "</b> to document present in <b>" + file + "</b>");
		}
		return doc;
	}

	/**
	 * Description: Method used to convert String XML response to document
	 * object
	 * 
	 * @param response
	 *            - Required XML response in String object to convert into
	 *            document object
	 * @return Document object after conversion
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static Document convertResponseToDoc(String response) throws KDTKeywordExecException {
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(response)));
		} catch (SAXException e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Not able to convert xml response to document" + e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Not able to convert xml response to document" + e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new KDTKeywordExecException("Not able to convert xml response to document" + e.getMessage(), e);
		}
		return doc;
	}

	/**
	 * Description: Method used to get single node list value in String object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param doc
	 *            - Required document object
	 * @return value
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getSingleNodeListValue(String xpath, Document doc) throws KDTKeywordExecException {
		String value = null;
		XPath path = XPathFactory.newInstance().newXPath();
		NodeList nodeList = null;
		try {
			nodeList = (NodeList) path.compile(xpath).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (nodeList.getLength() == 0) {
			throw new KDTKeywordExecException("Size of node is :" + nodeList.getLength());
		}
		value = (nodeList.item(0).getTextContent());
		return value;
	}

	/**
	 * Description: Method used to get multiple node list values in ArrayList
	 * <String> object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param doc
	 *            - Required document object
	 * @return values
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static List<String> getMultipleNodeListValue(String xpath, Document doc) throws KDTKeywordExecException {
		List<String> values = new ArrayList<String>();
		XPath path = XPathFactory.newInstance().newXPath();
		NodeList nodeList = null;
		try {
			nodeList = (NodeList) path.compile(xpath).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (nodeList.getLength() == 0) {
			throw new KDTKeywordExecException("Size of node is :" + nodeList.getLength());
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			values.add(nodeList.item(i).getTextContent());
		}
		return values;
	}

	/**
	 * Description: Method used to get single value from file in String object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param file
	 *            - Required path of the file to get single node value
	 * @return: value
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getSingleValueFromFile(String xpath, String file) throws KDTKeywordExecException {
		Document doc = convertXmlFileToDoc(file.toString());
		String value = getSingleNodeListValue(xpath, doc);
		return value;
	}

	/**
	 * Description: Method used to get multiple values from file in List
	 * <String> object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param file
	 *            - Required path of the file to get multiple node values
	 * @return values
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static List<String> getMultipleValuesFromFile(String xpath, String file) throws KDTKeywordExecException {
		Document doc = convertXmlFileToDoc(file.toString());
		List<String> values = getMultipleNodeListValue(xpath, doc);
		return values;
	}

	/**
	 * Description: Method used to get single value from response in String
	 * object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param response
	 *            - Required response in String object to get single node value
	 * @return value
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getSingleValueFromResponse(String xpath, String response) throws KDTKeywordExecException {
		Document doc = convertResponseToDoc(response);
		String value = getSingleNodeListValue(xpath, doc);
		return value;
	}

	/**
	 * Description: Method used to get multiple values from response in List
	 * <String> object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param response
	 *            - Required response in List<String> object to get multiple
	 *            node value
	 * @return values
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static List<String> getMultipleValuesFromResponse(String xpath, String response)
			throws KDTKeywordExecException {
		Document doc = convertResponseToDoc(response);
		List<String> values = getMultipleNodeListValue(xpath, doc);
		return values;
	}

	/**
	 * Description: Method used to get response code from URL
	 * 
	 * @param url
	 *            - Required URL to get the response code
	 * @return response_code
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static int getResponseCodeFromURL(String url) throws KDTKeywordExecException {
		HttpURLConnection connection = null;
		URL verifyURL = null;
		try {
			verifyURL = new URL(url);
			Logger.info("End Point : " + verifyURL);
		} catch (MalformedURLException e) {
			throw new KDTKeywordExecException("Error in URL - " + e.getMessage());
		}
		try {
			connection = (HttpURLConnection) verifyURL.openConnection();
		} catch (IOException e) {
			throw new KDTKeywordExecException("Error in Connection - " + e.getMessage());
		}
		int response_code = 0;
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			throw new KDTKeywordExecException("Error in GET request - " + e.getMessage());
		}
		try {
			connection.connect();
		} catch (IOException e) {
			throw new KDTKeywordExecException("Error in establishing the connection - " + e.getMessage());
		}
		try {
			response_code = connection.getResponseCode();
		} catch (IOException e) {
			throw new KDTKeywordExecException("Error in getting response code - " + e.getMessage());
		}
		Logger.info("Response code for the URL : " + response_code);
		System.out.println("Response code of the URL : " + response_code);
		return response_code;
	}

	/**
	 * Description: Method used to get the entire response from URL in XML
	 * format
	 * 
	 * @param url
	 *            - Required URL to get the response
	 * @return response
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getResponseFromURL(String url) throws KDTKeywordExecException {
		String response = null;
		int code = getResponseCodeFromURL(url);
		if (code == 200) {
			try {
				response = Request.Get((new URL(url).toURI())).execute().returnContent().toString();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				throw new KDTKeywordExecException(
						"Not able to get the response from the URL - <b>" + url + "</b>" + e.getMessage(), e);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new KDTKeywordExecException(
						"Not able to get the response from the URL - <b>" + url + "</b>" + e.getMessage(), e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new KDTKeywordExecException(
						"Not able to get the response from the URL - <b>" + url + "</b>" + e.getMessage(), e);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				throw new KDTKeywordExecException(
						"Not able to get the response from the URL - <b>" + url + "</b>" + e.getMessage(), e);
			}

			EggTimer timer = new EggTimer(240, 1);
			timer.start();
			boolean finished = false;
			while (!finished) {
				if (!(response == null)) {
					finished = true;
				} else {
					finished = false;
				}
				timer.delay();
			}
			if (finished)
				timer.cancel();
			if (timer.done() && !finished) {
				throw new KDTKeywordExecException(
						"Not able to get the response after waiting - <b>" + timer.getRunTime() + "</b> seconds");
			}
		} else {
			throw new KDTKeywordExecException("For URL - <b> " + url + ", Expected Response code is = <b> 200 </b> "
					+ "and Actual Response code is = <b>" + code + "</b>");
		}
		return response;
	}

	/**
	 * Description- Method used to get response status code from https URLs
	 * 
	 * @param httpsurl
	 *            Required https URL for getting response
	 * @return response_code
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static int getResponseCodeFromHttps(String httpsurl) throws KDTKeywordExecException {
		int response_code = 0;
		HttpClient httpclient = new HttpClient();
		GetMethod httpget = new GetMethod(httpsurl.replaceAll(" ", "%20"));
		try {
			httpclient.executeMethod(httpget);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response_code = httpget.getStatusCode();
		return response_code;
	}

	/**
	 * Description: Method used to get the entire response from URL with request
	 * in JSON format
	 * 
	 * @param requestBody
	 *            Required request body to pass with URL
	 * @param url
	 *            required URL to get the response
	 * @return Response in JSON
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getJSONResponse(String requestBody, String url) throws KDTKeywordExecException {
		String response;
		HttpURLConnection con = null;
		URL uri = null;
		int code = 0;
		try {
			uri = new URL(url);
			System.out.println(uri);
			con = (HttpURLConnection) uri.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("charset", "UTF-8");
			con.setDoOutput(true);
			con.setDoInput(true);
		} catch (Exception e) {
			throw new KDTKeywordExecException(e.getMessage());
		}
		if (requestBody != null) {
			con.setDoInput(true);
			con.setDoOutput(true);
			try {
				DataOutputStream out = new DataOutputStream(con.getOutputStream());
				out.writeBytes(requestBody);
				out.flush();
				out.close();
			} catch (IOException e) {
				throw new KDTKeywordExecException(e.getMessage());
			}
		}
		try {
			code = con.getResponseCode();
			if (code == 200) {
				System.out.println("Actual Status Code: " + code);
			} else {
				throw new KDTKeywordExecException("Expected Status code: 200. Actual Status code: " + code);
			}
			con.getContent().toString();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			response = IOUtils.toString(in, encoding);
			System.out.println("Response: " + response);
		} catch (Exception e) {
			throw new KDTKeywordExecException("Failed to get json response");
		}
		return response;
	}

	/**
	 * Description: Method used to get the entire response from URL in JSON
	 * format
	 * 
	 * @param URL
	 *            - required URL to get the response
	 * @return jsonresponse
	 * @author QualiTest
	 */
	public static String getResponseInJSON(String URL) {
		StringBuilder jsonresponse = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		URL url = null;
		try {
			url = new URL(URL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						jsonresponse.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:" + url, e);
		}
		return (jsonresponse.toString()).replace("﻿", "");
	}

	/**
	 * Description: Method used to get multiple values from JSON response in
	 * List<String> object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node values
	 * @param jsonresponse
	 *            - Required JSON response in List<String> object to get
	 *            multiple node values
	 * @return values
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static List<String> getListOfFieldDataInJSON(String xpath, String jsonresponse)
			throws KDTKeywordExecException {
		List<String> values = new ArrayList<String>();
		try {
			values = JsonPath.read(jsonresponse, xpath);
		} catch (InvalidPathException e) {
			return null;
		}
		if (values == null) {
			throw new KDTKeywordExecException("Not able to get the data with the xpath - <b>" + xpath + "</b>");
		}
		return values;
	}

	/**
	 * Description: Method used to get single value from JSON response in String
	 * object
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param jsonresponse
	 *            - Required JSON response in String object to get single node
	 *            value
	 * @return value
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static String getFieldDataInJSON(String xpath, String jsonresponse) throws KDTKeywordExecException {
		String value = null;
		try {
			value = (JsonPath.read(jsonresponse, xpath)).toString();
		} catch (InvalidPathException e) {
			return null;
		}
		if (value == null) {
			throw new KDTKeywordExecException("Not able to get the data with the xpath - <b>" + xpath + "</b>");
		}
		return value;
	}

	/**
	 * Description: Method used to verify whether the tag is present or not
	 * 
	 * @param xpath
	 *            - Required valid xpath to find node value
	 * @param jsonresponse
	 *            - Required JSON response in String object to get single node
	 *            value
	 * @return false
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static boolean verifyPresenceOfTagInJSON(String jsonresponse, String xpath) throws KDTKeywordExecException {
		String value;
		try {
			value = JsonPath.read(jsonresponse, xpath);
		} catch (InvalidPathException e) {
			throw new KDTKeywordExecException("Invalid xpath Expression");
		}
		if (value != null) {
			return true;
		}
		return false;
	}

	/**
	 * Description: Method to get a random number within a specified range.
	 * 
	 * @param min
	 *            - Required minimum value of the range
	 * @param max
	 *            - Required maximum value of the range
	 * @return rannum
	 * @author QualiTest
	 */
	public static int getRandomNumber(int min, int max) {
		Random rn = new Random();
		int range = max - min + 1;
		int rannum = rn.nextInt(range) + min;
		return rannum;
	}

	/**
	 * Description: Method to download binaries in specified location.
	 * 
	 * @param fileUrl
	 *            - Required the file URL
	 * @param fileName
	 *            - Required the location to store download file
	 * @author QualiTest
	 */
	public static void saveFileFromUrlWithCommonsIO(String fileUrl, String filepath)
			throws MalformedURLException, IOException, KDTKeywordExecException {
		FileUtils.copyURLToFile(new URL(fileUrl), new File(filepath), 30000, 30000);
	}

	/**
	 * Description: Method used to get output field data in HashMap
	 * 
	 * @param expression
	 *            - Required valid expression of xpath
	 * @param response
	 *            - Required response in XML format
	 * @param currApp
	 *            - Required current application
	 * @return expected HashMap<String, String>
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static HashMap<String, String> getOutputFieldDataMap(String expression, String response, String currApp)
			throws KDTKeywordExecException {
		HashMap<String, String> expected = new HashMap<String, String>();
		String value;
		Document doc = convertResponseToDoc(response);
		String xpath1 = "feed_xml_output_" + expression;
		String xpath = gei.getProperty(xpath1, currApp);
		value = getSingleNodeListValue(xpath, doc);
		expected.put(expression, value);
		return expected;
	}

	/**
	 * Description: Method used to get node names in ArrayList<String> object
	 * 
	 * @param xpath
	 *            - Required valid expression of xpath
	 * @param response
	 *            - Required response in XML format
	 * @return values
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static ArrayList<String> getNodeNames(String xpath, String response) throws KDTKeywordExecException {
		ArrayList<String> values = new ArrayList<String>();
		Document doc = convertResponseToDoc(response);
		NodeList nodeList;
		try {
			XPath path = XPathFactory.newInstance().newXPath();
			nodeList = (NodeList) path.compile(xpath).evaluate(doc, XPathConstants.NODESET);
			if (nodeList.getLength() == 0) {
				throw new KDTKeywordExecException("Size of node is :" + nodeList.getLength());
			}
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				System.out.println(element.getNodeName());
				values.add(element.getNodeName());
			}
		} catch (Exception e) {
			throw new KDTKeywordExecException("Error occured while fetching the value for Node: " + xpath);
		}
		return values;
	}

	/**
	 * Description: Method used to get all the tag names in List<HashMap<String,
	 * List<String>>>
	 * 
	 * @param doc
	 *            - Required XML file in form of document
	 * @return tags
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @author QualiTest
	 */
	public static List<HashMap<String, List<String>>> getAllTagNamesFromXml(Document doc)
			throws ParserConfigurationException, SAXException, IOException {
		List<HashMap<String, List<String>>> tags = new ArrayList<HashMap<String, List<String>>>();
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			HashMap<String, List<String>> tempAttributes = listAllAttributes(element);
			if (tempAttributes.size() == 0) {
				tempAttributes.put(element.getNodeName(), new ArrayList<String>());
				tags.add(tempAttributes);
			} else
				tags.add(tempAttributes);
		}
		return tags;
	}

	/**
	 * Description: Method used to get all the attributes in HashMap<String,
	 * List<String>>
	 * 
	 * @param element
	 *            - Required element to get attributes
	 * @return expectedAttributes
	 * @author QualiTest
	 */
	public static HashMap<String, List<String>> listAllAttributes(Element element) {
		HashMap<String, List<String>> expectedAttributes = new HashMap<String, List<String>>();
		List<String> attributesInTag = new ArrayList<String>();
		NamedNodeMap attributes = element.getAttributes();
		int numAttrs = attributes.getLength();
		if (numAttrs > 0) {
			for (int i = 0; i < numAttrs; i++) {
				Attr attr = (Attr) attributes.item(i);
				String attrName = attr.getNodeName();
				attributesInTag.add(attrName);
			}
			expectedAttributes.put(element.getNodeName(), attributesInTag);
		}
		return expectedAttributes;
	}

	/**
	 * Description: Method used to verify the presence of a tag
	 * 
	 * @param xpath
	 *            - Required valid expression of xpath
	 * @param response
	 *            - Required response in XML format
	 * @return flag
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static boolean verifyTagIsPresent(String xpath, String response) throws KDTKeywordExecException {
		boolean flag = false;
		Document doc = convertResponseToDoc(response);
		NodeList nodeList;
		try {
			XPath path = XPathFactory.newInstance().newXPath();
			nodeList = (NodeList) path.compile(xpath).evaluate(doc, XPathConstants.NODESET);
			if (nodeList.getLength() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			throw new KDTKeywordExecException("Error occured while fetching the value for Node: " + xpath);
		}
		return flag;
	}

	/**
	 * Description: Method used to compare two XMLs
	 * 
	 * @param expectedXML
	 *            - Required expected XML response as a reference for comparison
	 * @param actualXML
	 *            - Required actual XML response that has to be compared
	 * @return allDifferences
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static List<?> assertXMLEquals(String expectedXML, String actualXML) throws KDTKeywordExecException {
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreComments(Boolean.TRUE);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setCompareUnmatched(Boolean.FALSE);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		DetailedDiff diff = null;
		try {
			diff = new DetailedDiff(XMLUnit.compareXML(expectedXML, actualXML));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		List<?> allDifferences = diff.getAllDifferences();
		return allDifferences;
	}

	/**
	 * Description: Method used to compare two XMLs without ignoring white
	 * spaces
	 * 
	 * @param expectedXML
	 *            - Required expected XML response as a reference for comparison
	 * @param actualXML
	 *            - Required actual XML response that has to be compared
	 * @return allDifferences
	 * @throws KDTKeywordExecException
	 * @author QualiTest
	 */
	public static List<?> assertXMLEqualsWithIgnoreWhiteSpaceFalse(String expectedXML, String actualXML)
			throws KDTKeywordExecException {
		XMLUnit.setIgnoreWhitespace(false);
		XMLUnit.setIgnoreComments(Boolean.TRUE);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setCompareUnmatched(Boolean.FALSE);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		DetailedDiff diff = null;
		try {
			diff = new DetailedDiff(XMLUnit.compareXML(expectedXML, actualXML));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		List<?> allDifferences = diff.getAllDifferences();
		return allDifferences;
	}

	/**
	 * Description: Method used to write XML content in a file
	 * 
	 * @param response
	 *            - Required response in XML format as source
	 * @param filePath
	 *            - Required file path as destination where XML content has to
	 *            be copied
	 * @throws KDTKeywordExecException
	 */
	public static void writeContentToFile(String response, String filePath) throws KDTKeywordExecException {
		try {
			Document document = convertResponseToDoc(response);
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			Source src = new DOMSource(document);
			Result dest = new StreamResult(new File(filePath));
			aTransformer.transform(src, dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: Method used to get XML response from URL
	 * 
	 * @param apiUrl
	 *            - Required URL for getting response
	 * @return response
	 * @throws KDTKeywordExecException
	 */
	public static String getResponse(String apiUrl) throws KDTKeywordExecException {
		InputStreamReader in = null;
		StringBuilder sb = new StringBuilder();
		String response = null;
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			System.out.println("Code:" + con.getResponseCode());
			if (con != null)
				con.setReadTimeout(60 * 1000);
			if (con != null && con.getInputStream() != null) {
				in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
			System.out.println(sb.toString());
			response = sb.toString();
		} catch (MalformedURLException e1) {
			throw new KDTKeywordExecException(e1.getMessage());
		} catch (IOException e) {
			response = null;
		}
		return response;
	}

	/**
	 * Description:
	 * 
	 * @param filepath
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String copyFileContentToString(String filepath) {
		File xmlFile = new File(filepath);
		String xmlResponse = null;
		Reader fileReader;
		try {
			fileReader = new FileReader(xmlFile);
			BufferedReader bufReader = new BufferedReader(fileReader);
			StringBuilder sb = new StringBuilder();
			String line = bufReader.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = bufReader.readLine();
			}
			xmlResponse = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlResponse;
	}

	public static class AppendIdToUrl extends Keyword {
		private String id;
		private String url;
		private String app;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("Id", "Url", "Application");
			app = args.get("Application");
			id = args.get("Id");
			url = args.get("Url");
			try {
				url = getProps.getPropertyValue(url, app);
			} catch (KDTKeywordExecException e) {
				throw new KDTKeywordInitException("Unable to get Property Value", e.getCause());
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			stringAppendedUrl = url.concat("/" + id);
			if (hasArgs("Position")) {
				stringAppendedUrl = stringAppendedUrl.concat("?position=" + args.get("Position"));
			}
			System.out.println("Appended Url : " + stringAppendedUrl);
			this.addComment("Appended Url : " + stringAppendedUrl);
			// to save the values for later use
			context.store(args.get("SaveTo"), stringAppendedUrl);
			saveValue(args.get("SaveTo"));
		}
	}
	// ############################### Keyword Ends
	// ##############################################

	// ############################### Keyword Begins
	// ############################################
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i></b> AppendPkgIdToUrl
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword takes package from configuration
	 * file and append it to URL
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>No Dependencies</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url (Mandatory): an URL giving the base location of API.</li>
	 * <li>Id(Mandatory): Id to be appended to the URL.</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 Date - 22 December 2015
	 * </p>
	 * <p>
	 * <b><i>Author:</i></b> Cutinho Reshma
	 * </p>
	 * </div>
	 */

	public static class AppendPkgIdToUrl extends Keyword {
		private String id;
		private String url;
		private String app;
		private String pkgId = "";

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("Id", "Url", "Application");
			app = args.get("Application");
			id = args.get("Id");
			url = args.get("Url");
			try {
				url = getProps.getPropertyValue(url, app);
				pkgId = getProps.getPropertyValue(id, app);
			} catch (KDTKeywordExecException e) {
				throw new KDTKeywordInitException("Unable to get Property Value", e.getCause());
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			stringAppendedUrl = url.concat("/" + pkgId);
			if (hasArgs("Position")) {
				stringAppendedUrl = stringAppendedUrl.concat("?position=" + args.get("Position"));
			}
			System.out.println("Appended Url : " + stringAppendedUrl);
			this.addComment("Appended Url : " + stringAppendedUrl);
			// to save the values for later use
			context.store(args.get("SaveTo"), stringAppendedUrl);
			saveValue(args.get("SaveTo"));
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i></b> GetHttpConnection
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to establish the Http
	 * connection and sets all the required properties for HttpURLConnection
	 * instance.
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>AppendIdToUrl</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url (Mandatory): an URL giving the base location of API.</li>
	 * <li>Type (Mandatory): Method type(POST, PUT, DELETE, GET)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 Date - 12 February 2015
	 * </p>
	 * <p>
	 * <b><i>Author:</i></b> Cutinho Reshma
	 * </p>
	 * </div>
	 */

	public static class GetHttpConnection extends Keyword {
		private URL uri = null;
		private String url;
		private String type;
		private String expected;
		private String app;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("Type", "Url", "Application");
				type = args.get("Type");
				app = args.get("Application");
				if (hasArgs("GetAppendedUrl")) {
					if (args.get("GetAppendedUrl").equalsIgnoreCase("true"))
						url = (String) context.retrieve(args.get("Url"));
				} else {
					url = args.get("Url");
					try {
						url = getProps.getPropertyValue(url, app);
					} catch (KDTKeywordExecException e) {
						throw new KDTKeywordInitException("Unable to get Property Value", e.getCause());
					}
				}
				if (hasArgs("Expected"))
					expected = args.get("Expected");
			} catch (Exception e) {
				e.printStackTrace();
				throw new KDTKeywordInitException(e.getMessage());
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			try {
				uri = new URL(url);
				con = (HttpURLConnection) uri.openConnection();
				con.setRequestMethod(type); // type: POST, PUT, DELETE, GETset
				con.setConnectTimeout(60000); // 60 secs
				con.setRequestProperty("Content-Type", "application/xml");
				con.setRequestProperty("charset", "UTF-8");
				con.setReadTimeout(60000); // 60 secs
				con.setDoOutput(true);
				con.setDoInput(true);
				this.addComment("Connection Established");
			} catch (MalformedURLException e) {
				if (e.getMessage().contains(expected))
					this.addComment("Actual and expected Response matches");
				else
					throw new KDTKeywordExecException(e.getMessage());
			} catch (ProtocolException e) {
				if (e.getMessage().contains(expected))
					this.addComment("Actual and expected Response matches");
				else
					throw new KDTKeywordExecException(e.getMessage());
			} catch (NullPointerException e) {
				if (e.getMessage().contains(expected))
					this.addComment("Actual and expected Response matches");
				else
					throw new KDTKeywordExecException(e.getMessage());
			} catch (IOException e) {
				this.addComment("Error : " + e.getMessage());
				e.printStackTrace();
				throw new KDTKeywordExecException(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				throw new KDTKeywordExecException(e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public static HostnameVerifier addSecureCertificate() {
		HostnameVerifier allHostsValid = null;
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return allHostsValid;
	}

	/**
	 * 
	 * @param https_url
	 * @return
	 */
	public static String getResponseFromHttpsUrl(String https_url) {
		String wf_Response = null;
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			URL url;
			try {
				StringBuilder sb = new StringBuilder();
				InputStreamReader in = null;

				url = new URL(https_url);
				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
				System.out.println("Code:" + con.getResponseCode());

				if (con != null)
					con.setReadTimeout(60 * 1000);
				if (con != null && con.getInputStream() != null) {
					in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());
					BufferedReader bufferedReader = new BufferedReader(in);
					if (bufferedReader != null) {
						int cp;
						while ((cp = bufferedReader.read()) != -1) {
							sb.append((char) cp);
						}
						bufferedReader.close();
					}
				}
				in.close();
				System.out.println(sb.toString());
				wf_Response = sb.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return wf_Response;
	}

	/**
	 * 
	 * @param https_url
	 * @return
	 */
	public static int getResponseCodeHttpsUrl(String https_url) {
		int code = 0;
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			URL url;
			try {
				url = new URL(https_url);
				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
				con.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				code = con.getResponseCode();
				System.out.println("Code:" + con.getResponseCode());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return code;
	}

	public static String getFieldData(String xpath, String response) throws KDTKeywordExecException {
		String value;
		Document doc;

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(response)));

		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to convert input xml file to document", e);
		}
		NodeList nodeList;
		try {
			XPath path = XPathFactory.newInstance().newXPath();
			// XPathExpression xPathExpression = xPathExpression =
			// xPath.compile("//namespace::*");
			nodeList = (NodeList) path.compile(xpath).evaluate(doc, XPathConstants.NODESET);
			if (nodeList.getLength() == 0) {
				throw new KDTKeywordExecException("Size of node is :" + nodeList.getLength());
			}
			value = (nodeList.item(0).getTextContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new KDTKeywordExecException("Error occured while fetching the value for Node: " + xpath);
		}
		return value;
	}

	// ############################### Method Ends
	// ############################################

	/**
	 * This method returns the response code (Works for GET method)
	 * 
	 * @return Response Code
	 */
	public static int getResponseFromURL(HttpURLConnection con, URL url) {
		int code = 0;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			code = con.getResponseCode();
			Logger.info("Response code for get request : " + code);
			System.out.println("Response code of the object is " + code);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * This method used to write file contents into string.
	 * 
	 * @return string
	 */
	public static String writeFileContentToString(String filepath) {
		File xmlFile = new File(filepath);
		String filePathNew = null;
		BufferedWriter writer = null;
		String xmlResponse01 = null;
		Reader fileReader;
		try {
			fileReader = new FileReader(xmlFile);
			BufferedReader bufReader = new BufferedReader(fileReader);
			StringBuilder sb = new StringBuilder();
			String line = bufReader.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = bufReader.readLine();
			}
			xmlResponse01 = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlResponse01;
	}

	// ############################### Method Begins
	// ##########################################

	public static List<String> getListOfFiledData(String xpath, String response) throws KDTKeywordExecException {
		ArrayList<String> values = new ArrayList<String>();
		Document doc;

		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(response)));

		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to convert input xml file to document", e);
		}
		NodeList nodeList;
		try {
			XPath path = XPathFactory.newInstance().newXPath();
			nodeList = (NodeList) path.compile(xpath).evaluate(doc, XPathConstants.NODESET);
			if (nodeList.getLength() == 0) {
				throw new KDTKeywordExecException("Size of node is :" + nodeList.getLength());
			}
			for (int i = 0; i < nodeList.getLength(); i++) {
				values.add(nodeList.item(i).getTextContent().trim());
			}
		} catch (Exception e) {
			throw new KDTKeywordExecException("Error occured while fetching the value for Node: " + xpath);
		}
		return values;
	}

	// ############################### Method Ends
	// ############################################

	/**
	 * This method waits till the Response is not null and returns the
	 * Response(Works for GET method)
	 * 
	 * @return Response Code
	 */
	public static String waitForResponse(String response, URL url) throws KDTKeywordExecException {
		EggTimer timer = new EggTimer(240, 10);
		timer.start();
		boolean finished = false;
		while (!finished) {
			response = response(url);
			if (!(response == null)) {
				finished = true;
			} else {
				finished = false;
			}
			System.out.println("Waiting for 10 seconds..");
			timer.delay();
		}
		if (finished)
			timer.cancel();

		if (timer.done() && !finished) {
			throw new KDTKeywordExecException(
					"respone is : " + response + " even after egg timer wait of " + timer.getRunTime());
		}
		return response;
	}

	public static String response(URL url) {
		String response = null;
		try {
			response = Request.Get(url.toURI()).execute().returnContent().toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return response;
	}
}
