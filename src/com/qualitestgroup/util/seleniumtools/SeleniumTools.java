package com.qualitestgroup.util.seleniumtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qualitestgroup.kdt.TestContext;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.util.EggTimer;

public class SeleniumTools {
	
	/**
	 * Waits for the WebElement located by locator to exist or timeout
	 * seconds to transpire, whichever happens first.
	 * 
	 * @param driver The WebDriver instance to use to process this wait
	 * @param locator The By object to use to locate the WebElement
	 * @param timeout The maximum number of seconds that should transpire
	 * @return the WebElement if it exists, null otherwise
	 * 
	 * @author Brian Van Stone
	 */
	public static WebElement waitForWebElement(WebDriver driver, By locator, int timeout) {
		try {
			return (new WebDriverWait(driver, timeout)).until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (TimeoutException e) {
			return null;
		}
	}
	
	/**
	 * Waits for the WebElement located by locator to exist or timeout
	 * seconds to transpire, whichever happens first.
	 * 
	 * @param locator The By object to use to locate the WebElement
	 * @param timeout The maximum number of seconds that should transpire
	 * @return the WebElement if it exists, null otherwise
	 * 
	 * @author Brian Van Stone
	 */
	public static WebElement waitForWebElement(By locator, int timeout) {
		TestContext context = TestContext.getContext();
		return waitForWebElement(context.getWebDriver(), locator, timeout);
	}
	
	/**
	 * Waits up to 'timeout' seconds for the WebElement described by 'locator'
	 * to no longer exist within the context of 'driver'
	 * 
	 * @param driver The WebDriver instance to use when locating the WebElement
	 * @param locator The by instance that describes the WebElement
	 * @param timeout The number of seconds to wait for the WebElement to no longer exist
	 * @return true if the WebElement no longer exists and false otherwise
	 */
	public static boolean waitForWebElementDNE(WebDriver driver, By locator, int timeout) {
		return (new WebDriverWait(driver, timeout)).until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(locator)));
	}
	
	/**
	 * Waits up to 'timeout' seconds for the WebElement described by 'locator'
	 * to no longer exist within the context of 'TestContext.getContext().getWebDriver()'
	 * 
	 * @param locator The by instance that describes the WebElement
	 * @param timeout The number of seconds to wait for the WebElement to no longer exist
	 * @return true if the WebElement no longer exists and false otherwise
	 */
	public static boolean waitForWebElementDNE(By locator, int timeout) {
		WebDriver driver = TestContext.getContext().getWebDriver();
		return waitForWebElementDNE(driver, locator, timeout);
	}
	
	/**
	 * Waits up to 'timeout' seconds for the WebElement described by 'locator' to exist and
	 * be visible within the context of 'driver'
	 * 
	 * @param driver The WebDriver instance to use when locating the WebElement
	 * @param locator The by instance which describes the WebElement
	 * @param timeout The number of seconds to wait for the WebElement to be visible
	 * @return true if the WebElement exists and is visible, false otherwise
	 * @throws KDTKeywordExecException 
	 */
	public static boolean waitForWebElementVisible(WebDriver driver, By locator, int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (TimeoutException e) {
			throw new KDTKeywordExecException("Waited for "+timeout+" sec but still unable to find the element");
			//return false;
		}
		
		return true;
	}
	/**
	 * Waits up to 'timeout' seconds for the WebElement described by 'locator' to exist and
	 * be visible within the context of 'driver'
	 * 
	 * @param driver The WebDriver instance to use when locating the WebElement
	 * @param locator The by instance which describes the WebElement
	 * @param timeout The number of seconds to wait for the WebElement to be visible
	 * @return true if the WebElement exists and is visible, false otherwise
	 * @throws KDTKeywordExecException 
	 */
	public static boolean waitForWebElementToClickable(WebDriver driver, By locator, int timeout) throws KDTKeywordExecException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.elementToBeClickable(locator));
		} catch (TimeoutException e) {
			throw new KDTKeywordExecException("Timeout exception");
			//return false;
		}
		
		return true;
	}
	public static boolean visibilityOfElement(WebDriver driver,List<WebElement> element, int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.visibilityOfAllElements(element));
		} catch (TimeoutException e) {
			//throw new KDTKeywordExecException("Timeout exception");
			return false;
		}
		
		return true;
	}
	public static boolean elementIsStaleness(WebDriver driver, WebElement element, int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.stalenessOf(element));
		} catch (TimeoutException e) {
			//throw new KDTKeywordExecException("Timeout exception");
			return false;
		}
		
		return true;
	}
	public static boolean presenceOfAllElements(WebDriver driver, By locator , int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
		} catch (TimeoutException e) {
			//throw new KDTKeywordExecException("Timeout exception");
			return false;
		}
		return true;
	}
	public static boolean presenceOfElement(WebDriver driver, By locator , int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (TimeoutException e) {
			//throw new KDTKeywordExecException("Timeout exception");
			return false;
		}
		return true;
	}
	public static boolean invisibilityOfElement(WebDriver driver, By locator , int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.invisibilityOfElementLocated(locator));
		} catch (TimeoutException e) {
			//throw new KDTKeywordExecException("Timeout exception");
			return false;
		}
		return true;
	}
	public static boolean textTOValidate(WebDriver driver, By locator ,String textToValidate, int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.textToBePresentInElementValue(locator, textToValidate));
		} catch (TimeoutException e) {
			throw new KDTKeywordExecException("Timeout exception");
			//return false;
		}
		return true;
	}
	public static boolean switchToFrame(WebDriver driver, By locator ,String textToValidate, int timeout) throws KDTKeywordExecException {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
		} catch (TimeoutException e) {
			throw new KDTKeywordExecException("Timeout exception");
			//return false;
		}
		return true;
	}

	public static boolean ValidateElementIsSelectedFromDropDown(WebDriver driver, WebElement element , int timeout) throws KDTKeywordExecException {
		
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.elementToBeSelected(element));
		} catch (TimeoutException e) {
			//throw new KDTKeywordExecException("Timeout exception");
			return false;
		}
		return true;
	}

	public static boolean ValidateElementIsSelectedFromDropDown(WebDriver driver, By locator , int timeout) throws KDTKeywordExecException {
		
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.elementToBeSelected(locator));
		} catch (TimeoutException e) {
			throw new KDTKeywordExecException("Timeout exception");
			//return false;
		}
		return true;
	}

	
	/**
	 * Waits up to 'timeout' seconds for the WebElement described by 'locator' to exist and
	 * be visible within the context of 'TestContext.getContext().getWebDriver()'
	 * 
	 * @param locator The by instance which describes the WebElement
	 * @param timeout The number of seconds to wait for the WebElement to be visible
	 * @return true if the WebElement exists and is visible, false otherwise
	 * @throws KDTKeywordExecException 
	 */
	public static boolean waitForWebElementVisible(By locator, int timeout) throws KDTKeywordExecException {
		WebDriver driver = TestContext.getContext().getWebDriver();
		return waitForWebElementVisible(driver, locator, timeout);
	}
	
	/**
	 * Waits up to 'timeout' seconds for the WebElement described by 'locator' to not be visible
	 * or not exist within the context of 'driver'
	 * 
	 * @param driver The WebDriver instance to use when locating the WebElement
	 * @param locator The by instance that describes the WebElement
	 * @param timeout The number of seconds to wait for the WebElement to not be visible
	 * @return true if the WebElement does not exist or is not visible, false otherwise
	 */
	public static boolean waitForWebElementNotVisible(WebDriver driver, By locator, int timeout) {
		try {
			(new WebDriverWait(driver, timeout)).until(ExpectedConditions.invisibilityOfElementLocated(locator));
		} catch (TimeoutException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Waits up to 'timeout' seconds for the WebElement described by 'locator' to not be visible
	 * or not exist within the context of 'TestContext.getContext().getWebDriver()'
	 * 
	 * @param locator The by instance that describes the WebElement
	 * @param timeout The number of seconds to wait for the WebElement to not be visible
	 * @return true if the WebElement does not exist or is not visible, false otherwise
	 */
	public static boolean waitForWebElementNotVisible(By locator, int timeout) {
		WebDriver driver = TestContext.getContext().getWebDriver();
		return waitForWebElementNotVisible(driver, locator, timeout);
	}
	
	/**
	 * A Comparator implementation for use with WebElements. By default, it is set
	 * up to sort WebElements by their position on the containing page/frame (either
	 * horizontally or vertically), but public int compare(WebElement a, WebElement b)
	 * may be overridden anonymously for a different implementation.
	 * 
	 * @author Brian Van Stone
	 *
	 */
	public static class WebElementComparator implements Comparator<WebElement> {

		@Override
		public int compare(WebElement a, WebElement b) {
			return (a.getLocation().x + a.getLocation().y) - (b.getLocation().x + b.getLocation().y);
		}
	}
	
	/**
	 * Sorts a Collection of WebElements based on their position on the screen, 
	 * either horizontally or vertically.
	 * 
	 * @param elements The Collection of WebElements to sort
	 * @return A sorted List of the WebElements
	 * 
	 * @author Brian Van Stone
	 */
	public static List<WebElement> sortWebElements(Collection<WebElement> elements) {
		return sortWebElements(elements, new WebElementComparator());
	}
	
	/**
	 * Sorts a Collection of WebElements based on the comparator passed as an argument
	 * 
	 * @param elements The Collection of elements to sort
	 * @param comparator The Comparator instance by which they are sorted
	 * @return A sorted List of the WebElements
	 * 
	 * @author Brian Van Stone
	 */
	public static List<WebElement> sortWebElements(Collection<WebElement> elements, Comparator<WebElement> comparator) {
		List<WebElement> sortedList = new ArrayList<WebElement>(elements);
		Collections.sort(sortedList, comparator);
		return sortedList;
	}
	
	/**
	 * Sorts a array of WebElements based on their position on the screen, 
	 * either horizontally or vertically.
	 * 
	 * @param elements The array of elements to sort
	 * @return A sorted List of the WebElements
	 * 
	 * @author Brian Van Stone
	 */
	public static List<WebElement> sortWebElements(WebElement[] elements) {
		return sortWebElements(elements, new WebElementComparator());
	}
	
	/**
	 * Sorts an array of WebElements based on the comparator passed as an argument
	 * 
	 * @param elements The Collection of elements to sort
	 * @param comparator The Comparator instance by which they are sorted
	 * @return A sorted List of the WebElements
	 * 
	 * @author Brian Van Stone
	 */
	public static List<WebElement> sortWebElements(WebElement[] elements, Comparator<WebElement> comparator) {
		List<WebElement> sortedList = Arrays.asList(elements);
		Collections.sort(sortedList, comparator);
		return sortedList;
	}
	
	/**
	 * Allows for translation of two strings to a By instance. The first
	 * parameter, <i>String expression</i>, is the parameter passed to the static
	 * By creator methods. The second parameter, <i>String by</i>, is used to determine
	 * which By creator method to invoke.
	 * 
	 * @param expression the expression used to construct the By instance
	 * @param by the type of By instance to create
	 * @return a By instance for the requested WebElement
	 */
	public static By getWebElementIdentifier(String expression, String by) {
		switch(by.toLowerCase()) {
			case "classname":
			    return By.className(expression);
			case "cssselector":
			case "csspath":
			    return By.cssSelector(expression);
			case "id":
			    return By.id(expression);
			case "linktext":
			    return By.linkText(expression);
			case "name":
			    return By.name(expression);
			case "partiallinktext":
			    return By.partialLinkText(expression);
			case "tagname":
			    return By.tagName(expression);
			case "xpath":
			    return By.xpath(expression);
			default:
				throw new IllegalArgumentException("Illegal value of " + by + " passed as 'by' arg to getWebElementIdentifier");
		}
	}
	
	/**
	 * Refresh the page every interval seconds, for up to timeout seconds, until the WebElement
	 * located by locator exists.
	 * 
	 * @param locator The WebElement to wait for
	 * @param timeout The max duration of the wait
	 * @param interval The interval at which to refresh the page
	 * @return The WebElement if it can be located, null otherwise.
	 */
	public static WebElement refreshUntilWebElementExists(By locator, int timeout, int interval) {
		EggTimer timer = new EggTimer(timeout, interval);
		WebDriver driver = TestContext.getContext().getWebDriver();
		timer.start();
		while(!timer.done()) {
			WebElement we = waitForWebElement(locator, interval);
			if(we != null) {
				timer.cancel();
				return we;
			} else {
				driver.navigate().refresh();
			}
		}
		System.out.println("refresh timer ran for " + timer.getRunTime() + " seconds.");
		return waitForWebElement(locator, interval);
	}
	
	/**
	 * Refresh the page every 10 seconds, for up to timeout seconds, until the WebElement
	 * located by locator exists.
	 * 
	 * @param locator The WebElement to wait for
	 * @param timeout The max duration of the wait
	 * @return The WebElement if it can be located, null otherwise.
	 */
	public static WebElement refreshUntilWebElementExists(By locator, int timeout) {
		return refreshUntilWebElementExists(locator, timeout, 10);
	}
	
	/**
	 * Refresh the page every 10 seconds, for up to 600 seconds, until the WebElement
	 * located by locator exists.
	 * 
	 * @param locator The WebElement to wait for
	 * @return The WebElement if it can be located, null otherwise.
	 */
	public static WebElement refreshUntilWebElementExists(By locator) {
		return refreshUntilWebElementExists(locator, 600);
	}
	
	/*public static boolean textToPresent(WebDriver driver, By locator , int timeout,String text) {
	  try {
	   (new WebDriverWait(driver, timeout)).until(ExpectedConditions.textToBePresentInElementValue(locator, text));
	  } catch (TimeoutException e) {
	   return false;
	  }
	  return true;
	 }*/

	
}
