package com.qualitestgroup.keywords.common;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.security.InvalidKeyException;
import java.security.Key;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;


public class ElementOperation {
	static GetElementIdentifier gei = new GetElementIdentifier();
	static ElementOperation eo = new ElementOperation();
	private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param elementIdentifier
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public By getWebElementByLocator(WebDriver driver, String locator, String elementIdentifier)
			throws KDTKeywordExecException {
		By ele = null;
		switch (locator.toLowerCase()) {
		case "classname":
			ele = By.className(elementIdentifier);
			break;
		case "cssselector":
			ele = By.cssSelector(elementIdentifier);
			break;
		case "id":
			ele = By.id(elementIdentifier);
			break;
		case "linkText":
			ele = By.linkText(elementIdentifier);
			break;
		case "name":
			ele = By.name(elementIdentifier);
			break;
		case "partiallinktext":
			ele = By.partialLinkText(elementIdentifier);
			break;
		case "tagname":
			ele = By.tagName(elementIdentifier);
			break;
		case "xpath":
			ele = By.xpath(elementIdentifier);
			break;
		default:
			throw new KDTKeywordExecException(
					"There is no Locator with the name - <b>" + locator + "</b>, please specify proper locator");
		}

		return ele;
	}
	/**
	 * 
	 * @param time
	 */
	public void wait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			System.out.println("Time Exceeded for: " + time * 1000 + " seconds");
		}
	}	
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public WebElement returnWebElement(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		WebElement element;
		try {
			element = driver.findElement(ele);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to find an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
		return element;
	}
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public List<WebElement> returnWebElements(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		List<WebElement> element;
		try {
			element = driver.findElements(ele);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to find an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
		return element;
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public WebElement returnWebElementAfterReplacingValues(WebDriver driver, String property, String keyValue,
			String oldChar, String newChar, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		WebElement element;
		try {
			element = driver.findElement(ele);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to find an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
		return element;
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public WebElement returnWebElementAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		WebElement element;
		try {
			element = driver.findElement(ele);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to find an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
		return element;
	}
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public List<WebElement> returnWebElementsAfterReplacingValues(WebDriver driver, String property, String keyValue,
			String oldChar, String newChar, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		List<WebElement> element;
		try {
			element = driver.findElements(ele);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to find an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
		return element;
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public List<WebElement> returnWebElementsAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		List<WebElement> element;
		try {
			element = driver.findElements(ele);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to find an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
		return element;
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param application
	 * @param keyvalue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void clickElement(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			driver.findElement(ele).click();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
	}
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param application
	 * @param keyvalue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void clickElementUsingString(WebDriver driver, String property, String elementIdentifier, String application)
			throws KDTKeywordExecException {
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			driver.findElement(ele).click();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element having " + property + " - <b>" + elementIdentifier + "</b>", e);
		}
	}
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void clickElementAfterReplacingKeyValue(WebDriver driver, String property, String keyValue, String oldChar,
			String newChar, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			driver.findElement(ele).click();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public void clickElementAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			driver.findElement(ele).click();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void submit(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			driver.findElement(ele).submit();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to submit on an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void clearData(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		System.out.println(elementIdentifier);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			driver.findElement(ele).clear();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to clear textbox in an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void clearDataAfterReplacingKeyValue(WebDriver driver, String property, String keyValue,String oldChar,
			String newChar, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			driver.findElement(ele).clear();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to clear textbox in an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public void clearDataAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			driver.findElement(ele).clear();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to clear textbox in an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param data
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void enterText(WebDriver driver, String property, String keyValue, String data, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			driver.findElement(ele).sendKeys(data);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to enter text in textbox in an element having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param data
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void enterTextAfterReplacingKeyValue(WebDriver driver, String property, String keyValue, String oldChar,
			String newChar, String data, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			driver.findElement(ele).sendKeys(data);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to enter on an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public void enterTextAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String data, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			driver.findElement(ele).sendKeys(data);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getText(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String gettext;
		try {
			gettext = driver.findElement(ele).getText();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get text from an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return gettext;
	}
	
	public String getTextThroughLocator(WebDriver driver, String locator, String identifier, String application) throws Exception {
		String returnText = null;
		try {
			Thread.sleep(2000);
			GetElementIdentifier gei = new GetElementIdentifier();
			String eleIdentifier = gei.getProperty(identifier, application);
			System.out.println("eleIdentifier: " + eleIdentifier);
			WebElement ele = getWebElement(driver, locator, identifier);
			returnText = ele.getText();
			System.out.println("Return text for identifier: " + identifier + "is: " + returnText);
			return returnText;
		} catch (Exception e) {
			throw new KDTKeywordExecException("Could find the webElement to get the Text", e);
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getTextAfterReplacingKeyValue(WebDriver driver, String property, String keyValue, String oldChar,
			String newChar, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String gettext;
		try {
			gettext = driver.findElement(ele).getText();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get text from an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return gettext;
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getTextAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String gettext;
		try {
			gettext = driver.findElement(ele).getText();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get text from an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return gettext;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param attributeName
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getAttribute(WebDriver driver, String property, String keyValue, String attributeName,
			String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String getattribute;
		try {
			getattribute = driver.findElement(ele).getAttribute(attributeName);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get attribute from an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return getattribute;
	}
	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param attributeName
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getAttributeAfterReplacingValue(WebDriver driver, String property, String keyValue, String oldChar,
			String newChar, String attributeName, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String getattribute;
		try {
			getattribute = driver.findElement(ele).getAttribute(attributeName);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get attribute from an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return getattribute;
	}
	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String getAttributeAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String attributeName, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String getattribute;
		try {
			getattribute = driver.findElement(ele).getAttribute(attributeName);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get attribute from an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return getattribute;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String geTagName(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String gettagname;
		try {
			gettagname = driver.findElement(ele).getTagName();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get tag name from an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return gettagname;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public Point geLocation(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		Point getlocation;
		try {
			getlocation = driver.findElement(ele).getLocation();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get the location of an element having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return getlocation;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean isDisplayed(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		boolean isdisplayed;
		try {
			isdisplayed =driver.findElement(ele).isDisplayed();
		
		}catch(Exception e) {
			isdisplayed=false;
		}
     
	    return isdisplayed;
	}

	
	
	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean isDisplayedAfterReplace(WebDriver driver, String property, String keyValue,String oldChar, String newChar, String application)
			throws KDTKeywordExecException {
		
		eo.wait(2);
		
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try{
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(ele));
		}
		catch(Exception e)
		{
			
		}
		boolean isdisplayed;
		try {
			isdisplayed =driver.findElement(ele).isDisplayed();
		
		}catch(Exception e) {
			isdisplayed=false;
		}
     
	    return isdisplayed;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean isSelected(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		boolean isSelected;
		try {
			isSelected = driver.findElement(ele).isSelected();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to verify whether element is selected or not having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
		return isSelected;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @param timeout
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean invisibilityOfElement(WebDriver driver, String property, String keyValue, String application,
			int timeout) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.MILLISECONDS);
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.invisibilityOfElementLocated(ele));
		} catch (TimeoutException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param application
	 * @param timeout
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean invisibilityOfElementAfterReplacing(WebDriver driver, String property, String keyValue,
			String oldChar, String newChar, String application, int timeout) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.invisibilityOfElementLocated(ele));
		} catch (TimeoutException e) {
			return false;
		}
		return true;
	}
	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean invisibilityOfElementAfterReplacingMultipleKeyValue(WebDriver driver, String property,
			String keyValue, String searchList[], String replacementList[], String application, int timeout)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.invisibilityOfElementLocated(ele));
		} catch (TimeoutException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void bringEleToViewUsingPoint(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		Point location = driver.findElement(ele).getLocation();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("javascript:window.scrollBy(" + location.getX() + "," + location.getY() + ")");
	}

	public void bringEleToViewUsingPointAfterReplace(WebDriver driver, String property, String keyValue, String oldChar,
			String newChar, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		Point location = driver.findElement(ele).getLocation();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("javascript:window.scrollBy(" + location.getX() + "," + location.getY() + ")");
	}

	/**
	 * @param driver
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String replaceSingleValuesInXPath(WebDriver driver, String keyValue, String oldChar, String newChar,
			String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		return elementIdentifier;
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public String replaceMultipleValuesInXPath(WebDriver driver, String keyValue, String searchList[],
			String replacementList[], String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		return elementIdentifier;
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param selectvalue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void selectComboBoxByVisibleText(WebDriver driver, String property, String keyValue, String selectvalue,
			String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		System.out.println(elementIdentifier);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			Select obj = new Select(getWebElement(driver, property, elementIdentifier));
			obj.selectByVisibleText(selectvalue);
			//Select obj = new Select(driver.findElement(ele));
			//obj.selectByVisibleText(selectvalue);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Element is not present in the combobox with text having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param byValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void selectComboBoxByValue(WebDriver driver, String property, String keyValue, String byValue,
			String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			Select obj = new Select(driver.findElement(ele));
			obj.selectByValue(byValue);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Element is not present in the combobox with value having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param byindexValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void selectComboBoxByIndex(WebDriver driver, String property, String keyValue, int byindexValue,
			String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		System.out.println(elementIdentifier);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			Select obj = new Select(getWebElement(driver, property, elementIdentifier));
			obj.selectByIndex(byindexValue);
			//Select obj = new Select(driver.findElement(ele));
			//obj.selectByIndex(byindexValue);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Element is not present in the combobox with index having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void checkCheckBox(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		boolean isSelected;
		try {
			isSelected = driver.findElement(ele).isSelected();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to verify whether element is selected or not having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
		if (!isSelected) {
			try {
				driver.findElement(ele).click();
			} catch (Exception e) {
				throw new KDTKeywordExecException(
						"Not able to click on an element having xpath - <b>" + elementIdentifier + "</b>", e);
			}
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void uncheckCheckBox(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		boolean isSelected;
		try {
			isSelected = driver.findElement(ele).isSelected();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to verify whether element is selected or not having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
		if (isSelected) {
			try {
				driver.findElement(ele).click();
			} catch (Exception e) {
				throw new KDTKeywordExecException(
						"Not able to click on an element having xpath - <b>" + elementIdentifier + "</b>", e);
			}
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void selectRadioButton(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		boolean isSelected;
		try {
			isSelected = driver.findElement(ele).isSelected();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to verify whether element is selected or not having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
		if (!isSelected) {
			try {
				driver.findElement(ele).click();
			} catch (Exception e) {
				throw new KDTKeywordExecException(
						"Not able to click on an element having xpath - <b>" + elementIdentifier + "</b>", e);
			}
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void deSelectRadioButton(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		boolean isSelected;
		try {
			isSelected = driver.findElement(ele).isSelected();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to verify whether element is selected or not having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
		if (isSelected) {
			try {
				driver.findElement(ele).click();
			} catch (Exception e) {
				throw new KDTKeywordExecException(
						"Not able to click on an element having xpath - <b>" + elementIdentifier + "</b>", e);
			}
		}
	}

	public void selectLastElement(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		Select sel = new Select(driver.findElement(ele));
		List<WebElement> lis = sel.getOptions();
		int size = lis.size();
		sel.selectByIndex(size - 1);
	}

	/**
	 * 
	 * @param driver
	 */
	public void waitForPageload(WebDriver driver) throws KDTKeywordExecException {
		String BrowserStatus = null;
		do {
			eo.wait(5);
			try {
				BrowserStatus = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState;");
			} catch (Exception e) {

			}
		} while (!(BrowserStatus.compareToIgnoreCase("complete") == 0));
		BrowserStatus = null;
	}

	/**
	 * 
	 * @param driver
	 * @param element
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptClick(WebDriver driver, WebElement element, String application)
			throws KDTKeywordExecException {
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(element));
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].click()", element);
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to click on an element using javascript", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptClick(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].click()", driver.findElement(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element using javascript having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptDoubleClick(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String dblclkObj = "var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false,"
				+ "false, 0,null); arguments[0].dispatchEvent(evt);";
		((JavascriptExecutor) driver).executeScript(dblclkObj, driver.findElement(ele));
		System.out.println("Double click done");
	}

	public void javaScriptDoubleClickAfterReplacingValue(WebDriver driver, String property, String keyValue,
			String oldChar, String newChar, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String dblclkObj = "var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false,"
				+ "false, 0,null); arguments[0].dispatchEvent(evt);";
		((JavascriptExecutor) driver).executeScript(dblclkObj, driver.findElement(ele));
		System.out.println("Double click done");
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param elementIdentifier
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptClickAfterReplacingKeyValue(WebDriver driver, String property, String keyValue,
			String sourceValue, String targetValue, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(sourceValue, targetValue);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].click()", driver.findElement(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element using javascript having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptClickAfterReplacingMultipleKeyValue(WebDriver driver, String property, String keyValue,
			String searchList[], String replacementList[], String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].click()", driver.findElement(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to click on an element using javascript having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}
	/**
	 * 
	 * @param driver
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void executeJScript(WebDriver driver, String keyValue, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript("" + elementIdentifier + "");
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to execute javascript having xpath - <b>" + elementIdentifier + "</b>", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param element
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptScrollToViewElement(WebDriver driver, WebElement element, String application)
			throws KDTKeywordExecException {
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.visibilityOf(element));
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to scroll on an element using javascript</b>", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptScrollToViewElement(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to scroll on an element using javascript having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	
	
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptScrollToViewElementAfterReplace(WebDriver driver, String property, String keyValue,String oldChar, String newChar, String application)throws KDTKeywordExecException 
	
	{
		
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);

		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(ele));
		} catch (Exception e) {
			//throw new KDTKeywordExecException("Not able to scroll on an element using javascript having xpath - <b>" + elementIdentifier + "</b>",e);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void hoverMouseUsingJavascript(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		try {
			((JavascriptExecutor) driver).executeScript(mouseOverScript, driver.findElement(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to hover on an element using javascript having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptRightClickOnElement(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			String rightClickObj = "var evt = arguments[0].ownerDocument.createEvent('MouseEvents');"
					+ "evt.initMouseEvent('contextmenu', true, true,arguments[0].ownerDocument.defaultView, 1, 0, 0, 0, 0, false,"
					+ "false, false, false, 2, null);if (document.createEventObject){return arguments[0].fireEvent('onclick', evt);"
					+ " }else{return !arguments[0].dispatchEvent(evt);}";
			((JavascriptExecutor) driver).executeScript(rightClickObj, driver.findElement(ele));
		} catch (JavaScriptException e) {
			throw new KDTKeywordExecException(", Not able to Right click on the element", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldChar
	 * @param newChar
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptRightClickOnEleAfterReplacingValue(WebDriver driver, String property, String keyValue,
			String oldChar, String newChar, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			String rightClickObj = "var evt = arguments[0].ownerDocument.createEvent('MouseEvents');"
					+ "evt.initMouseEvent('contextmenu', true, true,arguments[0].ownerDocument.defaultView, 1, 0, 0, 0, 0, false,"
					+ "false, false, false, 2, null);if (document.createEventObject){return arguments[0].fireEvent('onclick', evt);"
					+ " }else{return !arguments[0].dispatchEvent(evt);}";
			((JavascriptExecutor) driver).executeScript(rightClickObj, driver.findElement(ele));
		} catch (JavaScriptException e) {
			throw new KDTKeywordExecException(", Not able to Right click on the element", e);
		}
	}

	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptRightClickOnEleAfterReplacingMultipleKeyValue(WebDriver driver, String property,
			String keyValue, String searchList[], String replacementList[], String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			String rightClickObj = "var evt = arguments[0].ownerDocument.createEvent('MouseEvents');"
					+ "evt.initMouseEvent('contextmenu', true, true,arguments[0].ownerDocument.defaultView, 1, 0, 0, 0, 0, false,"
					+ "false, false, false, 2, null);if (document.createEventObject){return arguments[0].fireEvent('onclick', evt);"
					+ " }else{return !arguments[0].dispatchEvent(evt);}";
			((JavascriptExecutor) driver).executeScript(rightClickObj, driver.findElement(ele));
		} catch (JavaScriptException e) {
			throw new KDTKeywordExecException(", Not able to Right click on the element", e);
		}
	}
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param oldValue
	 * @param newValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void hoverMouseAfterReplacingXpathUsingJS(WebDriver driver, String property, String keyValue,
			String oldValue, String newValue, String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldValue, newValue);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		try {
			((JavascriptExecutor) driver).executeScript(mouseOverScript, driver.findElement(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to hover on an element using javascript having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param coordinatevalue
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptToScrollUpPage(WebDriver driver, String coordinatevalue) throws KDTKeywordExecException {
		try {
			((JavascriptExecutor) driver).executeScript("scroll(" + coordinatevalue + ",0)");
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to scroll up the page", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param coordinatevalue
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptToScrollDownPage(WebDriver driver, String coordinatevalue) throws KDTKeywordExecException {
		try {
			((JavascriptExecutor) driver).executeScript("scroll(0, " + coordinatevalue + ")");
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to scroll down the page", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param textToEnter
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void javaScriptToEnterText(WebDriver driver, String property, String keyValue, String textToEnter,
			String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		eo.clearData(driver, property, keyValue, application);
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].value='" + textToEnter + "';",
					driver.findElement(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to enter text in an element using javascript having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param expectedtext
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean verifyExactScreenText(WebDriver driver, String property, String keyValue, String expectedtext,
			String application) throws KDTKeywordExecException {
		String actualtext = eo.getText(driver, property, keyValue, application);
		if (!actualtext.trim().equals(expectedtext)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param expectedtext
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean verifyPartialScreenText(WebDriver driver, String property, String keyValue, String expectedtext,
			String application) throws KDTKeywordExecException {
		String actualtext = eo.getText(driver, property, keyValue, application);
		if (!actualtext.trim().contains(expectedtext)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean verifyElementIsPresent(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		System.out.println(elementIdentifier);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		if (driver.findElement(ele) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public boolean isEnabled(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		System.out.println(elementIdentifier);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		boolean isEnabled;
		try {
			isEnabled = driver.findElement(ele).isEnabled();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Element is not enabled having xpath - <b>" + elementIdentifier + "</b>",
					e);
		}
		return isEnabled;
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param elementIdentifier
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public By getListOfWebElementByLocator(WebDriver driver, String locator, String elementIdentifier)
			throws KDTKeywordExecException {
		By ele = null;
		switch (locator.toLowerCase()) {
		case "classname":
			ele = By.className(elementIdentifier);
			break;
		case "cssselector":
			ele = By.cssSelector(elementIdentifier);
			break;
		case "id":
			ele = By.id(elementIdentifier);
			break;
		case "linkText":
			ele = By.linkText(elementIdentifier);
			break;
		case "name":
			ele = By.name(elementIdentifier);
			break;
		case "partiallinktext":
			ele = By.partialLinkText(elementIdentifier);
			break;
		case "tagname":
			ele = By.tagName(elementIdentifier);
			break;
		case "xpath":
			ele = By.xpath(elementIdentifier);
			break;
		default:
			throw new KDTKeywordExecException(
					"There is no Locator with the name - <b>" + locator + "</b>, please specify proper locator");
		}

		// Wait for all the web element to be displayed on the web page for 60 Seconds of time
		try {
			driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
			(new WebDriverWait(driver, 150)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to find web elements with locator - <b>" + locator + " = "
					+ elementIdentifier + "</b> after 60 seconds of wait", e);
		}
		return ele;
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public List<WebElement> getListOfWebElements(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By elements = getWebElementByLocator(driver, property, elementIdentifier);
		try {
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(elements));
		}
		catch(Exception e)
		{
			
		}
		List<WebElement> allwebele;
		try {
			allwebele = driver.findElements(elements);
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to get all the elements having xpath - <b>" + elementIdentifier + "</b>", e);
		}
		return allwebele;
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param texttoclick
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void clickElementFromList(WebDriver driver, String property, String keyValue, String texttoclick,
			String application) throws KDTKeywordExecException {
		List<WebElement> allwebele = eo.getListOfWebElements(driver, property, keyValue, application);
		for (WebElement ele : allwebele) {
			String value = ele.getText();
			if (value.trim().contains(texttoclick)) {
				eo.javaScriptClick(driver, ele, application);
				break;
			}
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public int getSize(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		List<WebElement> allwebele = eo.getListOfWebElements(driver, property, keyValue, application);
		int size = allwebele.size();
		return size;
	}

	/**
	 * 
	 * @param driver
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionClick(WebDriver driver, String application) throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		try {
			action.click().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to click using actions", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionClickAfterReplacingKeyValue(WebDriver driver, String property, String keyValue,String oldChar,String newChar, String application)
			throws KDTKeywordExecException {
		
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			action.moveToElement(driver.findElement(ele)).click().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to click on an element using action class having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
	}
	/**
	 * @param driver
	 * @param text
	 * @param searchList
	 * @param replacementList
	 * @param keyValue
	 * @param application
	 * @return
	 * @throws KDTKeywordExecException
	 */
	public void actionClickAfterReplacingMultipleKeyValue(WebDriver driver,String property, String keyValue, String searchList[],String replacementList[], String application) throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = StringUtils.replaceEach(elementIdentifier, searchList, replacementList);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			action.moveToElement(driver.findElement(ele)).click().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to click on an element using action class having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionClick(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			action.moveToElement(driver.findElement(ele)).click().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to click on an element using action class having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionMoveToElement(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			action.moveToElement(driver.findElement(ele)).build().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to hover on an element using action class having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionClickAndHold(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			action.moveToElement(driver.findElement(ele)).clickAndHold().build().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to clickAndHold an element using action class having xpath - <b>" + elementIdentifier
							+ "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionDoubleClick(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			action.moveToElement(driver.findElement(ele)).doubleClick().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to doubleclick on an element using action class having xpath - <b>" + elementIdentifier
							+ "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionContextClick(WebDriver driver, String property, String keyValue, String application)
			throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			action.moveToElement(driver.findElement(ele)).contextClick().build().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to perform context click on an element using action class having xpath - <b>"
							+ elementIdentifier + "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param sourceproperty
	 * @param sourcekeyValue
	 * @param targetproperty
	 * @param targetkeyValue
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void actionDragAndDrop(WebDriver driver, String sourceproperty, String sourcekeyValue, String targetproperty,
			String targetkeyValue, String application) throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String sourceelementIdentifier = gei.getProperty(sourcekeyValue, application);
		By sourceele = getWebElementByLocator(driver, sourceproperty, sourceelementIdentifier);
		String targetelementIdentifier = gei.getProperty(targetkeyValue, application);
		By targetele = getWebElementByLocator(driver, targetproperty, targetelementIdentifier);
		try {
			action.dragAndDrop(driver.findElement(sourceele), driver.findElement(targetele)).build().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException(
					"Not able to drag and drop an element using action class having source xpath - <b>"
							+ sourceelementIdentifier + "</b> " + "and target xpath - <b>" + targetelementIdentifier
							+ "</b>",
					e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @param property
	 * @param keyValue
	 * @param timeout
	 * @param application
	 * @throws KDTKeywordExecException
	 */
	public void switchToFrameByIdentifier(WebDriver driver, String property, String keyValue, int timeout,
			String application) throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		try {
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			eo.wait(3);
			eo.waitForPageload(driver);
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(ele));
		} catch (Exception e) {
			throw new KDTKeywordExecException("Unable to Switch to the frame", e);
		}
	}

	/**
	 * 
	 * @param driver
	 * @throws KDTKeywordExecException
	 */
	public void switchToDefaultContent(WebDriver driver) throws KDTKeywordExecException {
		try {
			driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
			eo.waitForPageload(driver);
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to switch to default content of the page", e);
		}
	}

	/**
	 * 
	 * @param fileLocation
	 * @throws KDTKeywordExecException
	 */
	public void fileUploadRobot(String fileLocation) throws KDTKeywordExecException {
		try {
			Robot robot = new Robot();
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection stringSelection = new StringSelection(fileLocation);
			clipboard.setContents(stringSelection, null);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			throw new KDTKeywordExecException("Unable to Upload file", e);
		}
	}
	
	public WebElement getWebElement(WebDriver driver, String locator, String identifier) throws Exception {
		try {
			if (locator.toLowerCase().contains("linkText")) {
				return driver.findElement(By.linkText(identifier));
			} else if (locator.toLowerCase().contains("id")) {
				return driver.findElement(By.id(identifier));
			} else if (locator.toLowerCase().contains("name")) {
				return driver.findElement(By.name(identifier));
			} else if (locator.toLowerCase().contains("xpath")) {
				return driver.findElement(By.xpath("" + identifier + ""));
			} else if (locator.toLowerCase().contains("cssSelector")) {
				return driver.findElement(By.cssSelector(identifier));
			} else if (locator.toLowerCase().contains("partialLinkText")) {
				return driver.findElement(By.partialLinkText(identifier));
			} else if (locator.toLowerCase().contains("className")) {
				return driver.findElement(By.className(identifier));
			} else if (locator.toLowerCase().contains("tagName")) {
				return driver.findElement(By.tagName(identifier));
			}
		} catch (Exception e) {
			throw new KDTKeywordExecException("Could not find the matching element", e);
		}
		return null;
	}
	
	public void selectElement(String property, String keyValue, String data, String application) throws KDTKeywordExecException {
		try {
			String eleIdentifier = gei.getProperty(keyValue, application);
			System.out.println(eleIdentifier);
			Keyword.run("Browser", "SelectElement", property, eleIdentifier, "SelectValue", data);
		} catch (KDTException e) {
			throw new KDTKeywordExecException("Could not select the element", e);
		}
	}
	
	public void SetCheckBox(String property, String keyValue, String data, String application) throws KDTKeywordExecException {
		try {
			String eleIdentifier = gei.getProperty(keyValue, application);
			System.out.println(eleIdentifier);
			Keyword.run("Browser", "SetCheckBox", property, eleIdentifier, "CheckState", data);
		} catch (KDTException e) {
			throw new KDTKeywordExecException("Could not set the CheckBox", e);
		}
	}
	
	public void CheckChkBox(String property, String keyValue, String application) throws KDTKeywordExecException {
		try {
			String eleIdentifier = gei.getProperty(keyValue, application);
			System.out.println(eleIdentifier);
			Keyword.run("Browser", "CheckChkBox", property, eleIdentifier);
		} catch (KDTException e) {
			throw new KDTKeywordExecException("Could not check the CheckBox.", e);
		}
	}

	public void UncheckChkBox(String property, String keyValue, String application) throws KDTKeywordExecException {
		try {
			String eleIdentifier = gei.getProperty(keyValue, application);
			System.out.println(eleIdentifier);
			Keyword.run("Browser", "UncheckChkBox", property, eleIdentifier);
		} catch (KDTException e) {
			throw new KDTKeywordExecException("Could not Uncheck the CheckBox.", e);
		}
	}
	
	public void verifyScreenText(WebDriver driver, String property, String keyValue, String verifyExpString, String application)
			throws KDTKeywordExecException {
		WebElement ele;
		String actualText = "";
		try {
			String eleIdentifier = gei.getProperty(keyValue, application);
			System.out.println(eleIdentifier);
			ele = getWebElement(driver, property, eleIdentifier);
			actualText = ele.getText();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Could not extract the String.", e);
		}

		if (!actualText.trim().toLowerCase().contains(verifyExpString.trim().toLowerCase())) {
			throw new KDTKeywordExecException(
					"The Expected value: " + verifyExpString + " and Actual Value: " + actualText + "dosenot match");
		}
	}
	
	public boolean WaitForElementProperty(WebDriver driver, String locator, String attribute, String AtribVal,
			int timeout, String application) throws Exception {
		try {
			GetElementIdentifier gei = new GetElementIdentifier();
			WebElement ele = driver.findElement(By.xpath(gei.getProperty(locator, application)));
			while (timeout > 0) {
				if (ele.getAttribute(attribute).equalsIgnoreCase(AtribVal)) {
					return true;
				}
				timeout--;
				Thread.sleep(1000);
			}
			;
			return false;
		} catch (Exception e) {
			throw new KDTKeywordExecException("Could find the webElement", e);
		}
	}
	public void executeJScriptThroughJsIdentifier(WebDriver driver, String jsIdentifier, String application) throws Exception {
		try {
			String eleIdentifier = gei.getProperty(jsIdentifier, application);
			System.out.println(eleIdentifier);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("" + eleIdentifier + "");
		} catch (Exception e) {
			throw new KDTKeywordExecException("Could not execute Java Script method", e);
		}
	}
	public void executeJScriptDirect(WebDriver driver, String jsCode, String application) throws Exception {
		try {
			System.out.println(jsCode);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("" + jsCode + "");

		} catch (Exception e) {
			throw new KDTKeywordExecException("Could not execute Java Script method", e);
		}
	}
	public void bringElementintoView(WebDriver driver, String eleId, String application) throws Exception {
		try {
			String eleIdentifier = gei.getProperty("JSbringtoView", application);
			String jsCode = eleIdentifier.replace("####", eleId);
			System.out.println(jsCode);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("" + jsCode + "");
		} catch (Exception e) {
			throw new KDTKeywordExecException("Could not execute Java Script method", e);
		}
	}
	public void setText(String property, String keyValue, String data, String application) throws KDTKeywordExecException {
		GetElementIdentifier gei = new GetElementIdentifier();
		try {
			String eleIdentifier = gei.getProperty(keyValue, application);
			System.out.println(eleIdentifier);
			Keyword.run("Browser", "SetElementText", property, eleIdentifier, "Value", data);
		} catch (KDTException e) {
			throw new KDTKeywordExecException("Could not enter data", e);
		}
	}
	public  void javaScriptScrollToViewElement(WebDriver driver, WebElement element)
            throws KDTKeywordExecException {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);

        } catch (Exception e) {
            throw new KDTKeywordExecException("Not able to scroll to the element", e);
        }

    }
	public Date stringToDateConversion(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		try {
			date1 = df.parse(date);
			df.format(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date1;
	}
	
	
	public String gnerateRandomNo(int digit) {
        /*
        Random rnd = new Random();
        int min =(int) Math.pow(10,digit-1);
        int max =  ((int) Math.pow(10,digit)-(int)Math.pow(10,digit-1));
        int no = min + rnd.nextInt(max);
        return Integer.toString(no);
        */
        String strTimeStamp = "";
       
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        strTimeStamp = timestamp.toString().replace("-", "").replace(" ", "").replace(":", "");
        int dotPos = strTimeStamp.indexOf(".");
        if(dotPos!=-1){
            strTimeStamp = strTimeStamp.substring(2,dotPos);
        } else {
            strTimeStamp = strTimeStamp.substring(2);
        }
        return strTimeStamp;
    }
	
	
	
	
	
	public void actionMoveToElementAfterReplace(WebDriver driver, String property, String keyValue, String oldChar,
			String newChar, String application)
			throws KDTKeywordExecException {
		Actions action = new Actions(driver);
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
		try {
			action.moveToElement(driver.findElement(ele)).build().perform();
		} catch (Exception e) {
			throw new KDTKeywordExecException("Not able to hover on an element using action class having xpath - <b>"
					+ elementIdentifier + "</b>", e);
		}
	}
	
	public void waitForWebElementVisible(WebDriver driver, String property, String keyValue, int timeout,String application)
			throws KDTKeywordExecException {
		String elementIdentifier = gei.getProperty(keyValue, application);
		By ele = getWebElementByLocator(driver, property, elementIdentifier);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.visibilityOfElementLocated(ele));
		}catch (Exception e) {
			throw new KDTKeywordExecException(
					"Waited for "+timeout+" sec but still unable to find the webelement - <b>" + elementIdentifier + "</b>", e);
		}
	}
	
	
	public By getWebElementByLocatorAfterReplace(WebDriver driver, String locator,String keyValue, String oldChar,
			String newChar,String application)throws KDTKeywordExecException {
		
		String elementIdentifier = gei.getProperty(keyValue, application);
		elementIdentifier = elementIdentifier.replace(oldChar, newChar);
		By ele = null;
		switch (locator.toLowerCase()) {
		case "classname":
			ele = By.className(elementIdentifier);
			break;
		case "cssselector":
			ele = By.cssSelector(elementIdentifier);
			break;
		case "id":
			ele = By.id(elementIdentifier);
			break;
		case "linkText":
			ele = By.linkText(elementIdentifier);
			break;
		case "name":
			ele = By.name(elementIdentifier);
			break;
		case "partiallinktext":
			ele = By.partialLinkText(elementIdentifier);
			break;
		case "tagname":
			ele = By.tagName(elementIdentifier);
			break;
		case "xpath":
			ele = By.xpath(elementIdentifier);
			break;
		default:
			throw new KDTKeywordExecException(
					"There is no Locator with the name - <b>" + locator + "</b>, please specify proper locator");
		}

		return ele;
	}
	
	
	
	public void waitForWebElementVisibleAfterReplace(WebDriver driver, String locator, String keyValue,String oldChar, String newChar, int timeout,String application)
			throws KDTKeywordExecException {
		
		By ele=eo.getWebElementByLocatorAfterReplace(driver, locator, keyValue, oldChar, newChar, application);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.visibilityOfElementLocated(ele));
		}catch (Exception e) {
			throw new KDTKeywordExecException(
					"Waited for "+timeout+" sec but still unable to find the webelement - <b>" + ele + "</b>", e);
		}
	}
	
	
	public void waitForPopUp(WebDriver driver)throws KDTKeywordExecException {
	
		int time=10;
		try {
			
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			System.out.println("Time Exceeded for: " + time * 1000 + " seconds");
		}
	
	}
	
	
	
	
	public boolean isExists(WebDriver driver, String property, String keyValue, String application)
            throws KDTKeywordExecException {
        String elementIdentifier = gei.getProperty(keyValue, application);
        By ele = getWebElementByLocator(driver, property, elementIdentifier);
        boolean isExists;
        try {
            List<WebElement> elts = driver.findElements(ele);
           
            if(elts.size()>0){
                isExists = true;
            } else {
                isExists = false;
            }
        } catch (Exception e) {
          throw new KDTKeywordExecException(
                    "Element is not displayed having xpath - <b>" + elementIdentifier + "</b>", e);
        }
        return isExists;
    }
	

    public boolean isExistsAfterReplace(WebDriver driver, String property, String keyValue, String oldChar,
            String newChar, String application)
            throws KDTKeywordExecException {
       
        boolean isExists;
       
        String elementIdentifier = gei.getProperty(keyValue, application);
        elementIdentifier = elementIdentifier.replace(oldChar, newChar);
        By ele = getWebElementByLocator(driver, property, elementIdentifier);
        //(new WebDriverWait(driver, 150)).until(ExpectedConditions.elementToBeClickable(ele));
     
           
            List<WebElement> elts = driver.findElements(ele);
           
            if(elts.size()>0){
                isExists = true;
            } else {
                isExists = false;
        }
       
        return isExists;
    }
    
    
    public  String generateCurrDate() {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMYYYYhhmmss");
		String dateAsString = simpleDateFormat.format(new Date());
		//System.out.println(dateAsString);
		return dateAsString;
	}

    public  String generateCurrDateWithoutTime() {

    	Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
	    String strDate= formatter.format(date);   
		return strDate;
	}

	
	
}
