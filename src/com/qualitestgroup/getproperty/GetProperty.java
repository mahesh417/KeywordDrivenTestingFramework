package com.qualitestgroup.getproperty;

import java.io.InputStream;
import java.util.Properties;

import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;

public class GetProperty {

	/**
	 * 
	 * @param propertyName
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getPropertyValue(String propertyName, String application) throws KDTKeywordExecException {
		Properties property = new Properties();
		InputStream input = null;
		String propertyValue = null;
		application = application.toLowerCase();
		try {
			//input = new FileInputStream(
			//		System.getProperty("user.dir") + "/configuration/" + application + "/configuration.properties");
			input = GetElementIdentifier.class.getResourceAsStream(
					"/com/qualitestgroup/keywords/" + application + "/configuration.properties");
			property.load(input);
			propertyValue = property.getProperty(propertyName);
		} catch (Exception e) {
			throw new KDTKeywordExecException("The Property <b>" + propertyName
					+ "</b> is not present for the Application <b>" + application + "</b>.", e);
		}
		return propertyValue.trim();
	}
}
