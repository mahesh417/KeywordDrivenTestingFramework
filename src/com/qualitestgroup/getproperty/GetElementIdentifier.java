package com.qualitestgroup.getproperty;

import java.io.InputStream;
import java.util.Properties;

import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;

public class GetElementIdentifier {

	/**
	 * 
	 * @param propertyName
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getProperty(String propertyName, String application) throws KDTKeywordExecException {
		Properties property = new Properties();
		InputStream input = null;
		String propertyValue = null;
		application = application.toLowerCase();
		try {
			input = GetElementIdentifier.class.getResourceAsStream(
					"/com/qualitestgroup/keywords/" + application + "/elementidentifier.properties");
			property.load(input);
			propertyValue = property.getProperty(propertyName);
		} catch (Exception e) {
			throw new KDTKeywordExecException("The Property of the element " + propertyName
					+ " is not present for the Application " + application + ".", e);
		}
		return propertyValue;
	}

	/**
	 * 
	 * @param propertyName
	 * @param application
	 * @param subApplication
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getProperty(String propertyName, String application, String subApplication)
			throws KDTKeywordExecException {
		Properties property = new Properties();
		InputStream input = null;
		String propertyValue = null;
		application = application.toLowerCase();
		subApplication = subApplication.toLowerCase();
		try {
			input = GetElementIdentifier.class.getResourceAsStream("/com/qualitestgroup/keywords/" + application + "/"
					+ subApplication + "_elementIdentifier.properties");
			property.load(input);
			propertyValue = property.getProperty(propertyName);
		} catch (Exception e) {
			throw new KDTKeywordExecException("The Property of the element " + propertyName
					+ " is not present for the Application " + application + ".", e);
		}
		return propertyValue;
	}
}
