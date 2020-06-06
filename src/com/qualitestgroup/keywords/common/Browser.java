package com.qualitestgroup.keywords.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.kdt.KDTDriver;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;

public class Browser extends KeywordGroup {

	/**
	 * Enumerator for browser type.
	 * @author QualiTest
	 */
	public enum BrowserType {
		IE, CHROME, FIREFOX, SAFARI, MACCHROME,LINUXCHROME;
	}
	/**
	 * Opens a web browser.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Browser:</b> the browser to open. Accepted values are "Chrome", or
	 * "IE".</li>
	 * <li><i>Timeout:</i> Sets the maximum timeout (in seconds) for locating an
	 * element (Default 30).</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 * 
	 */
	public static class Launch extends Keyword {
		private static final String BROWSER = "Browser";
		public static BrowserType brow;
		private static int timeout = 30;
		public static WebDriver driver;
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			if (hasArgs(BROWSER)) {
				String brows = args.get(BROWSER);
				if (brows.equalsIgnoreCase("IE")) {
					brow = BrowserType.IE;
				} else if (brows.equalsIgnoreCase("Chrome")) {
					brow = BrowserType.CHROME;
				} else if (brows.equalsIgnoreCase("MacChrome")) {
					brow = BrowserType.MACCHROME;
				} else if (brows.equalsIgnoreCase("Firefox")) {
					brow = BrowserType.FIREFOX;
				} else if (brows.equalsIgnoreCase("Safari")) {
					brow = BrowserType.SAFARI;
				} else if (brows.equalsIgnoreCase("LinuxChrome")) {
					brow = BrowserType.LINUXCHROME;
				} else {
					throw new KDTKeywordInitException("Invalid browser name: \"" + brow + "\".\nExpected \"IE\" or \"Chrome\".or \"Firefox\"");
				}
			}
			if (hasArgs("Timeout")) {
				String time = args.get("Timeout");
				try {
					setTimeout(Integer.parseInt(time, 10));
				} catch (NumberFormatException e) {
					throw new KDTKeywordInitException("", e);
				}
			}
		}
		@Override
		public void exec() throws KDTKeywordExecException {
			
			try {
				DesiredCapabilities capabilities;
				TemporaryFilesystem.getDefaultTmpFS().deleteTemporaryFiles();
				switch (brow) {
				case IE:
					System.setProperty("webdriver.ie.driver", "./WebDrivers/IEDriverServer/IEDriverServer.exe");
					capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
					capabilities.setJavascriptEnabled(false); 
					capabilities.setCapability("ignoreZoomSetting", true);
					capabilities.setCapability("nativeEvents", false);
					capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
					driver = new InternetExplorerDriver(capabilities);
					driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
					context.setWebDriver(driver);
					this.comment += "Successfully Launched <b>'Internet Explorer'</b> browser";
					break;
				case FIREFOX:
					System.setProperty("webdriver.gecko.driver", "./WebDrivers/FirefoxDriver/geckodriver.exe");
					FirefoxProfile profile = new FirefoxProfile();
					profile.setPreference("network.proxy.type", 0);
					profile.setAcceptUntrustedCertificates(true);
					profile.setAssumeUntrustedCertificateIssuer(false);
					profile.setPreference("acceptSslCerts", "true");
					profile.setPreference("browser.download.folderList", 2);
					profile.setPreference("browser.download.manager.showWhenStarting", false);
					String path = System.getProperty("user.dir") + "\\ApplicationData\\Downloads";
					profile.setPreference("browser.download.dir", path);
					profile.setPreference("browser.helperApps.neverAsk.saveToDisk","image/jpg,text/csv,application/zip");
					profile.setPreference("browser.helperApps.alwaysAsk.force", false);
					profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
					profile.setPreference("browser.download.manager.focusWhenStarting", false);
					profile.setPreference("browser.download.manager.useWindow", false);
					profile.setPreference("browser.download.manager.showAlertOnComplete", false);
					profile.setPreference("browser.download.manager.closeWhenDone", false);
					capabilities = DesiredCapabilities.firefox();
					capabilities.setCapability("marionette", true);
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					capabilities.setCapability(FirefoxDriver.PROFILE, profile);
					driver = new FirefoxDriver();
					driver.manage().window().maximize();
					/*driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
					JavascriptExecutor js1 = ((JavascriptExecutor) driver);
					js1.executeScript("window.open('','testwindow','width=400,height=200')");
					driver.close();
					driver.switchTo().window("testwindow");
					js1.executeScript("window.moveTo(0,0);");
					js1.executeScript("window.resizeTo(1360,900);");
*/					context.setWebDriver(driver);
					this.comment += "Successfully Launched <b>'Mozilla Firefox'</b> browser";
					break;
				case CHROME:
					System.setProperty("webdriver.chrome.driver", "./WebDrivers/ChromeDriver/chromedriver.exe");
					String downloadFilepath = System.getProperty("user.dir") + "\\ApplicationData\\Downloads";
					downloadFilepath.replace("\\", "/");
					ChromeOptions options = new ChromeOptions();
					Map<String, Object> prefs = new HashMap<String, Object>();
					prefs.put("download.default_directory", downloadFilepath);
					prefs.put("credentials_enable_service", false);
					prefs.put("profile.password_manager_enabled", false);
					//prefs.put("profile.default_content_setting_values.notifications", 2);
					options.setExperimentalOption("prefs", prefs);
					options.addArguments("disable-infobars");
					options.addArguments("chrome.switches", "--disable-extensions");
					options.addArguments("--test-type");

					options.setExperimentalOption("useAutomationExtension", false);
					options.setExperimentalOption("excludeSwitches",Collections.singletonList("enable-automation"));
					
					//options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); 
					DesiredCapabilities cap = DesiredCapabilities.chrome();
					cap.setCapability(ChromeOptions.CAPABILITY, prefs);
					cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					cap.setCapability(ChromeOptions.CAPABILITY, options);
					
					driver = new ChromeDriver(cap);
					driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
					driver.manage().window().maximize();
	
					context.setWebDriver(driver);
					this.comment += "Successfully Launched <b>'Google Chrome'</b> browser";
					break;
				case MACCHROME:
					System.setProperty("webdriver.chrome.driver", "./WebDrivers/MacChromeDriver/chromedriver 2");
					String downloadFilepath1 = System.getProperty("user.dir") + "/ApplicationData/Downloads";
					downloadFilepath1.replace("\\", "/");
					HashMap<String, Object> chromePrefs1 = new HashMap<String, Object>();
					chromePrefs1.put("download.default_directory", downloadFilepath1);
					ChromeOptions options1 = new ChromeOptions();
					HashMap<String, Object> chromeOptionsMap1 = new HashMap<String, Object>();
					options1.setExperimentalOption("prefs", chromePrefs1);
					options1.addArguments("--test-type");
					DesiredCapabilities cap1 = DesiredCapabilities.chrome();
					cap1.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap1);
					cap1.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					cap1.setCapability(ChromeOptions.CAPABILITY, options1);
					driver = new ChromeDriver(cap1);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
					JavascriptExecutor js = ((JavascriptExecutor) driver);
					js.executeScript("window.open('','testwindow','width=400,height=200')");
					driver.close();
					driver.switchTo().window("testwindow");
					js.executeScript("window.moveTo(0,0);");
					js.executeScript("window.resizeTo(1360,900);");
					context.setWebDriver(driver);
					this.comment += "Successfully Launched <b>'Mac Google Chrome'</b> browser";
					break;
				case SAFARI:
					System.setProperty("webdriver.safari.driver", "./WebDrivers/SafariDriver/SafariDriver.safariextz");
					capabilities = DesiredCapabilities.safari();
					capabilities.setBrowserName("safari");
					SafariOptions safariOptions = new SafariOptions();
					safariOptions.setUseCleanSession(true);
					String osName = System.getProperty("os.name").toString();
					capabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);
					if (osName.contains("Windows")){
						capabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);
					}else if (osName.contains("Mac")){
						capabilities.setPlatform(org.openqa.selenium.Platform.MAC);
					}else{
						capabilities.setPlatform(org.openqa.selenium.Platform.ANY);
					}
					driver = new SafariDriver(capabilities);
					driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
					Set<String> windows = driver.getWindowHandles();
					if (windows.size() > 1) {
						driver.quit();
						Thread.sleep(4000);
						Keyword.run("Browser", "Launch", "Browser", "Safari");
						Thread.sleep(2000);
						break;
					}
					context.setWebDriver(driver);
					this.comment += "Successfully Launched <b>'Safari'</b> browser";
					break;
				case LINUXCHROME:
					System.setProperty("webdriver.chrome.driver", "./WebDrivers/LinuxChromeDriver/chromedriver");
					String downloadFilepath2 = System.getProperty("user.dir") + "\\ApplicationData\\Downloads";
					downloadFilepath2.replace("\\", "/");
					ChromeOptions options2 = new ChromeOptions();
					Map<String, Object> prefs2 = new HashMap<String, Object>();
					DesiredCapabilities cap2 = DesiredCapabilities.chrome();
					prefs2.put("download.default_directory", downloadFilepath2);
					prefs2.put("credentials_enable_service", false);
					prefs2.put("profile.password_manager_enabled", false);
					options2.setExperimentalOption("prefs", prefs2);
					options2.addArguments("disable-infobars");
					options2.addArguments("chrome.switches", "--disable-extensions");
					options2.addArguments("--test-type");
					// Headless browser
					//options2.addArguments("headless");
					//options2.addArguments("window-size=1200x600");
					ChromeDriverService serviceCR = new ChromeDriverService.Builder().usingAnyFreePort().build();
					serviceCR.start();
					cap2.setCapability(ChromeOptions.CAPABILITY, prefs2);
					cap2.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					cap2.setCapability(ChromeOptions.CAPABILITY, options2);
					driver = new RemoteWebDriver(serviceCR.getUrl(), cap2);
					driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
					driver.manage().window().maximize();
					context.setWebDriver(driver);
					this.comment += "Successfully Launched <b>' Linux Google Chrome'</b> browser";
					break;
				default:
					throw new KDTKeywordExecException("Invalid browser name: \"" + brow+ "\".\nExpected \"IE\" or \"Chrome\" or \"Firefox\" or \"Safari\"");
				}
			} catch (Exception e) {
				throw new KDTKeywordExecException("Unable to open browser." + e.getMessage(), e);
			}
		}
		public static int getTimeout() {
			return timeout;
		}
		public void setTimeout(int timeout) {
			Launch.timeout = timeout;
		}
		public static String getBrowser() {
			return brow.toString();
		}
	}

	/**
	 * @author QualiTest
	 */
	public static class Close extends Keyword {
		private boolean clearCookies = false;
		private static final String COOKIES = "ClearCookies";
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			if (context.getWebDriver() == null)
				throw new KDTKeywordInitException("Browser not open");
			if (hasArgs(COOKIES) && args.get(COOKIES).equals("Y")) {
				clearCookies = true;
			}
		}
		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			if (clearCookies) {
				driver.manage().deleteAllCookies();
			}
			driver.close();
			this.comment += "Successfully closed the Browser";
		}
	}
	/** 
	 * @author QualiTest
	 */
	public static class NavigateTo extends Keyword {
		private String url;
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("URL");
			if (context.getWebDriver() == null) {
				throw new KDTKeywordInitException("Browser not open");
			}
			url = args.get("URL");
		}
		@Override
		public void exec() {
			context.getWebDriver().get(url);
			this.addComment("Successfully Navigate to the URL: <b>'" + url + "'</b>");
		}
	}
	/**
	 * @author QualiTest
	 */
	public static class NavigateToApplicationUnderTest extends Keyword {
		private String url;
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			if (context.getWebDriver() == null) {
				throw new KDTKeywordInitException("Browser not open");
			}
			url = KDTDriver.getApplicationUrl();
		}
		@Override
		public void exec() {
			context.getWebDriver().get(url);
			this.addComment("Successfully Navigate to the URL: <b>'" + url + "'</b>");
		}
	}
}