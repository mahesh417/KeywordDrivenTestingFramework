package com.qualitestgroup.keywords.netsuite;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.print.DocFlavor.STRING;

import static io.restassured.RestAssured.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.eclipse.jetty.websocket.common.AcceptHash;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.getproperty.GetProperty;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.keywords.common.ElementOperation;
import com.qualitestgroup.util.seleniumtools.SeleniumTools;
import com.testautomationguru.utility.PDFUtil;

import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class NetSuiteOld extends KeywordGroup {

	static int waittime = 10;
	private static final String CURR_APP = "NetSuite";
	public static GetElementIdentifier gei = new GetElementIdentifier();
	static GetProperty getProps = new GetProperty();
	public static ElementOperation eo = new ElementOperation();
	public static String fullName;
	public static String countryName;
	public static String randNum = eo.gnerateRandomNo(5);
	public static String companyName;
	static List<Double> arl = new ArrayList<>();




	public static class OpenOrderNetsuite extends Keyword {

		private String orderNumber;

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {
				orderNumber = context.getData().get("$orderNo");

				eo.enterText(driver, "xpath", "globalSerachNetsuite", orderNumber, CURR_APP);
				this.addComment("Serached for the order number " + orderNumber);

				eo.waitForWebElementVisible(driver, "XPATH", "actualDisplayedOrder", waiTime, CURR_APP);
				String actualOrderDisplayed = eo.getText(driver, "xpath", "actualDisplayedOrder", CURR_APP);

				if (actualOrderDisplayed.contains(orderNumber)) {
					eo.clickElement(driver, "xpath", "actualDisplayedOrder", CURR_APP);
					this.addComment("Clicked on the order " + orderNumber);
					eo.waitForPageload(driver);
				}

				else {
					this.addComment("Unable to click on the order " + orderNumber);
					throw new KDTKeywordExecException("Unable to click on the order");
				}
			} catch (Exception e) {
				this.addComment("User is Unable to Open Order NetSuite");
				throw new KDTKeywordExecException("User is Unable to Open Order NetSuite", e);
			}
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>FulFillOrderNetSuite</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to fulfill the order</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>CreateAccount</li>
	 * <li>CreateContact</li>
	 * <li>CreateOpportunity</li>
	 * <li>CreateQuote</li>
	 * <li>OpportunityStageChange</li>
	 * <li>Submit Order</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>OrderNumber(Mandatory): The order number created in salesforce</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class FulFillOrderNetSuite extends Keyword {

		private String[] serialNumber1;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			serialNumber1 = args.get("SerialNumber1").split(";");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			WebDriver driver = context.getWebDriver();
			eo.clickElement(driver, "xpath", "netSuiteApprovebtn", CURR_APP);

			eo.waitForPageload(driver);
			this.addComment("Approved the order");

			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("rateLink", CURR_APP))));
			eo.actionClick(driver, "xpath", "dropShipLink", CURR_APP);

			this.addComment("Clicked on Drop ship link");
			eo.waitForPageload(driver);

			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("itemslink", CURR_APP))));

			eo.clickElement(driver, "xpath", "itemslink", CURR_APP);
			this.addComment("Clicked on Items link");

			List<WebElement> serialNumberList = eo.getListOfWebElements(driver, "xpath", "serielNumberList", CURR_APP);
			int size = serialNumberList.size();
			String listxpath = gei.getProperty("serielNumberList", CURR_APP);

			for (int i = 0; i < size; i++) {
				String count = listxpath + gei.getProperty("serialListCount", CURR_APP);
				String index = count.replace("{count}", Integer.toString(i + 1));
				eo.clickElementUsingString(driver, "XPATH", index, CURR_APP);

				this.addComment("Clicked on the first icon of serial number");

				eo.clickElement(driver, "xpath", "serialNumberIcon2", CURR_APP);
				this.addComment("Clicked on the second icon of serial number");

				driver.switchTo().defaultContent();
				driver.switchTo().frame(driver.findElement(By.xpath(gei.getProperty("childFrame", CURR_APP))));
				eo.javaScriptScrollToViewElement(driver, "xpath", "scrollToSeriel", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "serialNumberField", waiTime, CURR_APP);

				Actions action = new Actions(driver);
				action.sendKeys(Keys.PAGE_DOWN).build().perform();

				eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i], CURR_APP);
				this.addComment("Added the serial number");

				for (int j = 0; j < 4; j++) {
					try {
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();
					} catch (Exception e) {

					}
				}

				this.addComment("Clicked on OK button after enter the serial number");

				eo.waitForWebElementVisible(driver, "XPATH", "okButtonAfterSerialAdd", waiTime, CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "okButtonAfterSerialAdd", CURR_APP);
				this.addComment("Clicked on OK button after add serial number");

			}

			eo.clickElement(driver, "xpath", "saveButtonPurchaseOrder", CURR_APP);

			this.addComment("Clicked on SAVE button after add serial number for all the appliances");
			eo.waitForPageload(driver);

			eo.clickElement(driver, "xpath", "fullFillButton", CURR_APP);
			eo.waitForPageload(driver);
			this.addComment("Clicked on FullFill button");

			eo.clickElement(driver, "xpath", "markAll", CURR_APP);
			this.addComment("Clicked on Mark All");

			List<WebElement> inventoryNumberList = eo.getListOfWebElements(driver, "xpath", "inventoryList", CURR_APP);
			int size1 = inventoryNumberList.size();
			String Inventorylistxpath = gei.getProperty("inventoryList", CURR_APP);

			for (int i = 0; i < size1; i++) {

				String count = Inventorylistxpath + gei.getProperty("serialListCount", CURR_APP);
				String index = count.replace("{count}", Integer.toString(i + 1));
				eo.javaScriptClick(driver, driver.findElement(By.xpath(index)), CURR_APP);

				this.addComment("Clicked on the inventory icon");

				driver.switchTo().defaultContent();
				driver.switchTo().frame(driver.findElement(By.xpath(gei.getProperty("childFrame", CURR_APP))));
				eo.waitForWebElementVisible(driver, "XPATH", "serialNumberField", waiTime, CURR_APP);

				Actions action = new Actions(driver);
				action.sendKeys(Keys.PAGE_DOWN).build().perform();

				eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i], CURR_APP);
				this.addComment("Added the inventory number");

				for (int j = 0; j < 4; j++) {
					try {
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();

					} catch (Exception e) {

					}
				}

				this.addComment("Clicked on OK button after enter the serial number");

			}
			eo.waitForWebElementVisible(driver, "XPATH", "saveButtonPurchaseOrder", waiTime, CURR_APP);
			eo.clickElement(driver, "xpath", "saveButtonPurchaseOrder", CURR_APP);
			this.addComment("Saved the order after fulfillment");

		}
	}

	///////////////////////////////// LoginNetSuite/////////////////////////////////
	public static class LoginNetSuite extends Keyword {
		private String url;
		private String username;
		private String password;
		private String saveto;
		private String browser = "";

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			try {

				verifyArgs("Url", "Username", "Password", "SaveTo");
				url = args.get("Url");
				username = args.get("Username");
				password = args.get("Password");
				if (hasArgs("Browser")) {
					browser = args.get("Browser");
				}
				saveto = args.get("SaveTo");
			} catch (Exception e) {
				this.addComment("Error while initializing LoginNetSuite");
				throw new KDTKeywordInitException("Error while initializing LoginNetSuite", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			// String userfolder = System.getProperty("user.dir");
			String userfolder = "C:/sw/kdt/sf-regression/Rubrik_Automation";

			if (!(browser.isEmpty())) {
				try {
					Keyword.run("Browser", "Launch", "Browser", browser);
				} catch (KDTException e) {
					// TODO Auto-generated catch block
					throw new KDTKeywordExecException("Unable to launch browser");
				}
			}

			// try {
			// Keyword.run("Browser", "Launch", "Browser", browser);
			// } catch (KDTException e) {
			// e.printStackTrace();
			// }

			try {
				WebDriver driver = context.getWebDriver();
				driver.get(url);
				eo.waitForWebElementVisible(driver, "ID", "username", waitTime, CURR_APP);
				eo.enterText(driver, "ID", "username", username, CURR_APP);
				addComment("Successfully  enter the username");
				eo.enterText(driver, "ID", "password", password, CURR_APP);
				addComment("Successfully enter the password");
				eo.clickElement(driver, "ID", "login_btn", CURR_APP);
				addComment("Successfully clicked on login button");
				eo.wait(8);

				String ns_bkcodefile = "";

				System.out.println("user dir path: " + userfolder);
				this.addComment("user dir path: " + userfolder);

				ns_bkcodefile = userfolder + "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt";
				System.out.println("user ns backup file path: " + ns_bkcodefile);
				this.addComment("user ns backup file path: " + ns_bkcodefile);

				File f = new File(ns_bkcodefile);

				if (f.exists()) {
					BufferedReader reader = new BufferedReader(
							new FileReader((userfolder + "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt")));

					int lines = 0;
					while (reader.readLine() != null) {
						lines++;
					}
					reader.close();

					if (lines > 1) {
						File tempFile = new File(
								userfolder + "/src/com/qualitestgroup/keywords/netsuite/myTempFile.txt");
						tempFile.createNewFile();
						BufferedReader readerr = new BufferedReader(new FileReader(
								(userfolder + "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt")));
						String data = readerr.readLine();

						saveValue(data);
						String bkcoderun = context.getData().get("$backupCode");
						System.out.println(bkcoderun);
						eo.waitForWebElementVisible(driver, "XPATH", "backup_code_link", waitTime, CURR_APP);
						eo.hoverMouseUsingJavascript(driver, "XPATH", "backup_code_link", CURR_APP);
						eo.isDisplayed(driver, "XPATH", "backup_code_link", CURR_APP);
						addComment("Successfully backup link displayed");
						eo.waitForWebElementVisible(driver, "XPATH", "backup_code_link", waitTime, CURR_APP);
						addComment("Successfully verified the backup code is dispalyed");
						eo.clickElement(driver, "XPATH", "backup_code_link", CURR_APP);
						addComment("Successfully clicked on the back up code link");
						eo.isDisplayed(driver, "XPATH", "verificationcode_txtfield", CURR_APP);
						addComment("Successfully verificationcode txtfield displayed");
						eo.enterText(driver, "XPATH", "verificationcode_txtfield", bkcoderun, CURR_APP);
						addComment("Successfully entered verification code in the text field");

						eo.waitForPageload(driver);
						eo.waitForWebElementVisible(driver, "XPATH", "submit_btn", waitTime, CURR_APP);
						eo.hoverMouseUsingJavascript(driver, "XPATH", "submit_btn", CURR_APP);
						eo.clickElement(driver, "XPATH", "submit_btn", CURR_APP);
						addComment("Successfully clicked on submit button");

						eo.waitForWebElementVisible(driver, "XPATH", "home_btn", waitTime, CURR_APP);
						addComment("Successfully verified the home tab is dispalyed");

						BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
						String lineToRemove = data;
						String currentLine;

						while ((currentLine = readerr.readLine()) != null) {
							String trimmedLine = currentLine.trim();
							if (trimmedLine.equals(lineToRemove))
								continue;
							writer.write(currentLine + System.getProperty("line.separator"));
						}

						writer.close();
						reader.close();

						BufferedReader readertemp = new BufferedReader(new FileReader(
								userfolder + "/src/com/qualitestgroup/keywords/netsuite/myTempFile.txt"));
						BufferedWriter writerback = new BufferedWriter(new FileWriter(f));
						while ((currentLine = readertemp.readLine()) != null) {

							writerback.write(currentLine + System.getProperty("line.separator"));
						}
						readertemp.close();
						writerback.close();
						tempFile.delete();

					} else {

						BufferedReader readerr1 = new BufferedReader(new FileReader(
								(userfolder + "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt")));
						String data1 = readerr1.readLine();

						saveValue(data1);
						String bkcoderun1 = context.getData().get("$backupCode");
						System.out.println(bkcoderun1);
						eo.waitForWebElementVisible(driver, "XPATH", "backup_code_link", waitTime, CURR_APP);
						eo.hoverMouseUsingJavascript(driver, "XPATH", "backup_code_link", CURR_APP);
						eo.isDisplayed(driver, "XPATH", "backup_code_link", CURR_APP);
						addComment("Successfully backup link displayed");

						addComment("Successfully verified the backup code is dispalyed");
						eo.clickElement(driver, "XPATH", "backup_code_link", CURR_APP);
						addComment("Successfully clicked on the back up code link");

						eo.isDisplayed(driver, "XPATH", "verificationcode_txtfield", CURR_APP);
						addComment("Successfully verificationcode txtfield displayed");
						eo.enterText(driver, "XPATH", "verificationcode_txtfield", bkcoderun1, CURR_APP);
						addComment("Successfully entered verification code in the text field");
						eo.waitForWebElementVisible(driver, "XPATH", "submit_btn", waitTime, CURR_APP);
						eo.clickElement(driver, "XPATH", "submit_btn", CURR_APP);
						addComment("Successfully clicked on submit button");

						eo.waitForWebElementVisible(driver, "XPATH", "home_btn", waitTime, CURR_APP);
						addComment("Successfully verified the home tab is dispalyed");

						eo.waitForWebElementVisible(driver, "XPATH", "Generate_code", waitTime, CURR_APP);

						eo.actionClick(driver, "XPATH", "Generate_code", CURR_APP);

						eo.waitForWebElementVisible(driver, "XPATH", "password_text", waitTime, CURR_APP);

						eo.clickElement(driver, "xpath", "password_text", CURR_APP);

						eo.enterText(driver, "xpath", "password_text", password, CURR_APP);
						eo.clickElement(driver, "xpath", "Generate_btn", CURR_APP);

						eo.waitForWebElementVisible(driver, "XPATH", "codes", waitTime, CURR_APP);
						List<WebElement> codes = eo.getListOfWebElements(driver, "xpath", "codes", CURR_APP);
						String returnValue;
						String[] temp = new String[10];

						FileWriter fw = new FileWriter(f);

						for (int i = 0; i < codes.size(); i++) {
							System.out.println(codes.get(i).getText());
							String bcode = codes.get(i).getText();

							String result[] = bcode.split(":");
							returnValue = (result[result.length - 1]).trim();
							temp[i] = returnValue;
							fw.write(temp[i] + "\n");

						}
						fw.close();
						eo.waitForWebElementVisible(driver, "XPATH", "continue_btn", waitTime, CURR_APP);
						eo.clickElement(driver, "XPATH", "continue_btn", CURR_APP);
						eo.waitForPageload(driver);
					}

				}

				else {

					File fi = new File((userfolder + "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt"));
					fi.createNewFile();
					eo.wait(20);
					eo.waitForPageload(driver);
					eo.hoverMouseUsingJavascript(driver, "XPATH", "submit_btn", CURR_APP);
					eo.clickElement(driver, "XPATH", "submit_btn", CURR_APP);
					eo.clickElement(driver, "XPATH", "submit_btn", CURR_APP);

					addComment("Successfully clicked on submit button");

					eo.waitForWebElementVisible(driver, "XPATH", "home_btn", waitTime, CURR_APP);
					addComment("Successfully verified the home tab is dispalyed");

					eo.waitForWebElementVisible(driver, "XPATH", "Generate_code", waitTime, CURR_APP);

					eo.actionClick(driver, "XPATH", "Generate_code", CURR_APP);

					eo.waitForWebElementVisible(driver, "XPATH", "password_text", waitTime, CURR_APP);
					eo.clickElement(driver, "xpath", "password_text", CURR_APP);
					eo.enterText(driver, "xpath", "password_text", password, CURR_APP);
					eo.clickElement(driver, "xpath", "Generate_btn", CURR_APP);

					eo.waitForWebElementVisible(driver, "XPATH", "codes", waitTime, CURR_APP);
					List<WebElement> codes = eo.getListOfWebElements(driver, "xpath", "codes", CURR_APP);
					String returnValue;
					String[] temp = new String[10];
					FileWriter fw = new FileWriter(fi);

					for (int i = 0; i < codes.size(); i++) {
						System.out.println(codes.get(i).getText());
						String bcode = codes.get(i).getText();
						String result[] = bcode.split(":");
						returnValue = (result[result.length - 1]).trim();
						temp[i] = returnValue;
						fw.write(temp[i] + "\n");
					}
					fw.close();
					eo.waitForWebElementVisible(driver, "XPATH", "continue_btn", waitTime, CURR_APP);
					eo.clickElement(driver, "XPATH", "continue_btn", CURR_APP);
					eo.waitForPageload(driver);

				}

			} catch (Exception e) {
				this.addComment("User is Unable to Login NetSuite" + e.getMessage());
				throw new KDTKeywordExecException("User is Unable to Login NetSuite", e);
			}
		}

	}

	////////////////////////////// Logout Netsuite///////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>LogoutNetsuite</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to Logout from Netsuite
	 * Application.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>Login</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i>Author:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class LogoutNetsuite extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			// TODO Auto-generated method stub
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {

				WebDriver driver = context.getWebDriver();
				eo.wait(5);
				eo.actionMoveToElement(driver, "xpath", "userrole", CURR_APP);
				eo.clickElement(driver, "xpath", "logout_button", CURR_APP);
				eo.waitForPageload(driver);

				//eo.waitForWebElementVisible(driver, "ID", "login_button", waiTime, CURR_APP);
				this.addComment("Successfully logout from Netsuite");
			}

			catch (Exception e) {
				this.addComment("Unable to Logout Netsuite application");
				throw new KDTKeywordExecException("Unable to Logout Netsuite application", e);
			}

		}

	}

	//////////////////////////////////

	public static class Test extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			String url = "https://api.twilio.com/2010-04-01/Accounts/AC7c4e29ebffeaec573751cb7f5010402e/Messages.json";
			String authCookie = ("SK9bd21f95e98cc306e40eb52fe668afe9" + ":" + "Mrdpz1p4STXu3i2cvb1xRGOXJfrJWAei");
			String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
			System.out.println(authCookieEncoded);
			Response response = given().header("Authorization", "Basic " + authCookieEncoded).get(url);
			JsonPath jsonPathEvaluator = response.jsonPath();
			List<String> otp = jsonPathEvaluator.getList("messages.body");
			String otpMes = otp.get(0);
			String otpNo = otpMes.substring(otpMes.lastIndexOf(" ") + 1);
			System.out.print(otpNo);
		}

	}

	/////////////////////////////////// ApproveOrderNetsuite////////////////////////////////

	public static class ApproveOrderNetsuite extends Keyword {
		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {
				eo.wait(5);
				// click on approve button
				eo.clickElement(driver, "xpath", "netSuiteApprovebtn", CURR_APP);

				eo.verifyElementIsPresent(driver, "xpath", "OrderSuccessfullyApprovedMsg", CURR_APP);
				// verify Order Successfully Approved Message is displayed
				eo.verifyExactScreenText(driver, "xpath", "OrderSuccessfullyApprovedMsg",
						"Sales Order successfully Approved", CURR_APP);

				eo.verifyElementIsPresent(driver, "xpath", "pendingFullfillmentStatus", CURR_APP);
				// verify status is displayed as 'pending Fullfillment'
				eo.verifyExactScreenText(driver, "xpath", "pendingFullfillmentStatus", "Pending Fulfillment", CURR_APP);

			} catch (Exception e) {
				this.addComment("Unable to Approve Order Netsuite application");
				throw new KDTKeywordExecException("Unable to Approve Order Netsuite application", e);
			}

		}
	}

	//////////////////////////////////////////////// ClickOnDropShip/////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ClickOnDropShip</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to fullfill the order in
	 * PO</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>CreateAccount</li>
	 * <li>CreateContact</li>
	 * <li>CreateOpportunity</li>
	 * <li>CreateQuote</li>
	 * <li>OpportunityStageChange</li>
	 * <li>Submit Order</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Neethu</i></b>
	 * </p>
	 * </div>
	 */
	/*
	 * public static class ClickOnDropShip extends Keyword {
	 * 
	 * @Override public void exec() throws KDTKeywordExecException { int waitTime =
	 * Integer.parseInt(gei.getProperty("waitTime", CURR_APP)); WebDriver driver =
	 * context.getWebDriver(); try { eo.javaScriptScrollToViewElement(driver,
	 * driver.findElement(By.xpath(gei.getProperty("rateLink", CURR_APP))));
	 * eo.actionClick(driver, "xpath", "dropShipLink", CURR_APP);
	 * 
	 * this.addComment("Clicked on Drop ship link"); eo.waitForPageload(driver);
	 * 
	 * this.
	 * addComment("Successfuly verified dropship Purchase order details page is displayed."
	 * ); } catch (Exception e) {
	 * this.addComment("Unable to clickondropship Netsuite application"); throw new
	 * KDTKeywordExecException("Unable to clickondropship Netsuite application", e);
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 */
	///////////////// clickOnDropShip///////////////////////

	public static class ClickOnDropShip extends Keyword {
		private String poVendorName;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("PoVendorName");
				poVendorName = args.get("PoVendorName");

			} catch (Exception e) {
				this.addComment("PO Vendor Name Miss Matched");
				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));
			// eo.javaScriptScrollToViewElement(driver, "xpath", "items", CURR_APP);
			List<WebElement> poVendor = eo.getListOfWebElements(driver, "xpath", "poVendor", CURR_APP);
			String poVendorNameNS = null;

			for (int i = 0; i < poVendor.size(); i++) {
				System.out.println(poVendor.get(i).getText());
				String POVendorname = poVendor.get(i).getText().trim();
				System.out.println("fetched :"+ POVendorname);
				if (POVendorname.contains(poVendorName.trim())) {
					System.out.println(poVendorName);
					poVendorNameNS = poVendor.get(i).getText();
					this.addComment("PO vendor Name Should Be Super Micro Computer, Inc.");

				}
			}

			List<WebElement> supportStartDate = eo.getListOfWebElements(driver, "xpath", "startDate", CURR_APP);
			String startdate = null;
			for (int i = 0; i < supportStartDate.size(); i++) {
				System.out.println(supportStartDate.get(i).getText());
				startdate = supportStartDate.get(i).getText();
				this.addComment("SupportStartDate Must Be Blank");

			}

			if ((poVendorName.equals(poVendorNameNS) && startdate.contains(" "))) {
				this.addComment("Both Conditions Are Satishfied");

				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("rateLink", CURR_APP))));
				List<WebElement> dropShipLink = eo.getListOfWebElements(driver, "xpath", "dropShip", CURR_APP);
				for (int i = 0; i < 1; i++) {
					eo.wait(10);
					dropShipLink.get(i).click();
					this.addComment("Dropship Link Clicked");
				}

			} else {
				System.out.println("Dropship Link Is Not Clickable");
			}
			eo.verifyExactScreenText(driver, "XPATH", "verifyPurchaseOrder", "Purchase Order", CURR_APP);
			this.addComment("Purchase Order Page Sucessfully Verified");

		}

	}
	/////////////////////////////////////////////////// FullfillOrder_PO////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>FullfillOrder_PO</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to fullfill the order in
	 * PO</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>CreateAccount</li>
	 * <li>CreateContact</li>
	 * <li>CreateOpportunity</li>
	 * <li>CreateQuote</li>
	 * <li>OpportunityStageChange</li>
	 * <li>Submit Order</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Neethu</i></b>
	 * </p>
	 * </div>
	 */

	public static class FullfillOrder_PO extends Keyword {

		private String[] serialNumber1;
		private String ponumber;
		private String vendorname;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			serialNumber1 = args.get("SerialNumber1").split(";");
			ponumber = args.get("PONumber");
			vendorname = args.get("VendorName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Actions action = new Actions(driver);
			try {
				eo.waitForPageload(driver);
				eo.waitForPopUp(driver);
				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("itemslink", CURR_APP))));
				eo.clickElement(driver, "xpath", "itemslink", CURR_APP);
				this.addComment("Clicked on Items link");

				List<WebElement> serialNumberList = eo.getListOfWebElements(driver, "xpath", "serielNumberList",
						CURR_APP);
				int size = serialNumberList.size();
				String listxpath = gei.getProperty("serielNumberList", CURR_APP);
				for (int i = 0; i < size; i++) {
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "serielNumberList_Clickable", "{iteration}",
							Integer.toString(i + 1), CURR_APP);
					this.addComment("Clicked on the first icon of serial number");
					eo.clickElement(driver, "xpath", "serialNumberIcon2", CURR_APP);
					this.addComment("Clicked on the second icon of serial number");
					driver.switchTo().defaultContent();
					driver.switchTo().frame(driver.findElement(By.xpath(gei.getProperty("childFrame", CURR_APP))));
					// eo.waitForWebElementVisible(driver, "XPATH", "qty",
					// waiTime, CURR_APP);
					eo.wait(10);
					eo.javaScriptScrollToViewElement(driver,
							driver.findElement(By.xpath(gei.getProperty("qty", CURR_APP))));

					String product_qty = eo.getText(driver, "XPATH", "qty", CURR_APP);
					this.addComment("Product quantity is:" + product_qty);
					System.out.println(product_qty);
					double d = Double.parseDouble(product_qty);
					int qty_count = (int) d;
					System.out.println(qty_count);
					// Verifying quantity greater than one.
					Actions action = new Actions(driver);

					if (qty_count > 1) {
						action.sendKeys(Keys.PAGE_DOWN).build().perform();
						eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i], CURR_APP);
						driver.findElement(By.xpath(gei.getProperty("serialNumberField", CURR_APP))).sendKeys(Keys.TAB);
						eo.wait(1);
						this.addComment("Added the serial number");
						qty_count--;
						for (int k = 0; k < qty_count; k++) {
									eo.clickElement(driver,"XPATH", "add_button", CURR_APP);
							this.addComment("Clicked on add button");
							eo.waitForWebElementVisible(driver, "xpath", "serialNumberField", 30, CURR_APP);
							eo.enterText(driver, "xpath", "serialnumber_add", serialNumber1[i + 1], CURR_APP);
							this.addComment("Added the serial number");
						}
					} else {
						action.sendKeys(Keys.PAGE_DOWN).build().perform();
						eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i], CURR_APP);
						this.addComment("Added the serial number");
					}
					for (int j = 0; j < 4; j++) {

						try {
							driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();
							driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();

						} catch (Exception e) {

						}
					}
					this.addComment("Clicked on OK button after entering the serial number");
					eo.waitForWebElementVisible(driver, "XPATH", "okButtonAfterSerialAdd", waiTime, CURR_APP);
					eo.actionClick(driver, "xpath", "okButtonAfterSerialAdd", CURR_APP);
					this.addComment("Clicked on OK button after add serial number");
				}

				eo.clickElement(driver, "xpath", "saveButtonPurchaseOrder", CURR_APP);
				this.addComment("Clicked on SAVE button after add serial number for all the appliances");
				eo.waitForPageload(driver);
				// verify Sales order page is displayed.
				eo.verifyExactScreenText(driver, "xpath", "salesorder_txt", "Sales Order", CURR_APP);
				this.addComment("verified Sales order page is successfully displayed");
				eo.verifyExactScreenText(driver, "xpath", "transactionsaved_msg", "Transaction successfully Saved",
						CURR_APP);
				this.addComment("verified Transaction successfully Saved meassage");
				// Verifying PO number and VendorName is displayed
				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("rateLink", CURR_APP))));

				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("POnumbertxt", CURR_APP))));
				String purchaseorder_ns = eo.getText(driver, "XPATH", "POnumbertxt", CURR_APP);
				System.out.println(purchaseorder_ns);
				this.addComment("PO number is : " + purchaseorder_ns);
				eo.getTextAfterReplacingKeyValue(driver, "XPATH", "vendorname_txt", "{link}", vendorname, CURR_APP);
				this.addComment("Vendor name is : " + vendorname);
				eo.waitForPopUp(driver);
				eo.javaScriptToScrollUpPage(driver, "40");
				eo.clickElement(driver, "xpath", "fullFillButton", CURR_APP);
				eo.waitForPageload(driver);
				this.addComment("Clicked on FullFill button");
			} catch (Exception e) {
				this.addComment("Unable to clickondropship Netsuite application");
				throw new KDTKeywordExecException("Unable to clickondropship Netsuite application", e);
			}

		}
	}

	//////////////////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ItemFullfillment</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to fullfill the order in
	 * Netsuite PO</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>CreateAccount</li>
	 * <li>CreateContact</li>
	 * <li>CreateOpportunity</li>
	 * <li>CreateQuote</li>
	 * <li>OpportunityStageChange</li>
	 * <li>Submit Order</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Neethu</i></b>
	 * </p>
	 * </div>
	 */

	public static class ItemFullfillment extends Keyword {
		private String[] serialNumber1;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			serialNumber1 = args.get("SerialNumber1").split(";");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			int waitTime1 = Integer.parseInt(gei.getProperty("waitTime1", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);
			eo.waitForPopUp(driver);
			eo.clickElement(driver, "xpath", "markAll", CURR_APP);
			this.addComment("Clicked on Mark All");
			eo.waitForPopUp(driver);
			List<WebElement> inventoryNumberList = eo.getListOfWebElements(driver, "xpath", "inventoryList", CURR_APP);
			int size1 = inventoryNumberList.size();
			System.out.println("number of inventory icon" + size1);
			String Inventorylistxpath = gei.getProperty("inventoryList", CURR_APP);
			List<WebElement> qtyrowelmnt_list = eo.getListOfWebElements(driver, "xpath", "qty_txt", CURR_APP);
			System.out.println("qty row count:" + qtyrowelmnt_list.size());
			String qtyrowelmntxpath = gei.getProperty("qty_txt", CURR_APP);
			System.out.println(qtyrowelmntxpath);
			////////////////////////////////
			((JavascriptExecutor) driver).executeScript("scroll(1350,0)");
			List<WebElement> s_list = eo.getListOfWebElements(driver, "xpath", "serialnofield_itemfullfill", CURR_APP);
			System.out.println("serial no count:" + s_list.size());
			String Serialnumberxpath = gei.getProperty("serialnofield_itemfullfill", CURR_APP);
			String rownumber = "";
			for (int i = 0; i < size1; i++) {
				String count = Inventorylistxpath + gei.getProperty("serialListCount", CURR_APP);
				String index = count.replace("{count}", Integer.toString(i + 1));
				String inventory = driver.findElement(By.xpath(index)).getAttribute("id");
				String[] row = inventory.split("inventorydetail_helper_popup_");
				for (int n = 0; n < row.length; n++) {
					rownumber = row[n];
				}
				System.out.println(rownumber);
				String count2 = qtyrowelmntxpath + gei.getProperty("serialListCount", CURR_APP);
				String index2 = count2.replace("{count}", rownumber);
				String qtyrowtxt = driver.findElement(By.xpath(index2)).getAttribute("value");
				System.out.println("row content" + qtyrowtxt);
				String count1 = Serialnumberxpath + gei.getProperty("serialListCount", CURR_APP);
				String index1 = count1.replace("{count}", rownumber);
				if (Integer.parseInt(qtyrowtxt) > 1) {
					driver.findElement(By.xpath(index1)).sendKeys(serialNumber1[i] + "," + serialNumber1[i + 1]);
				} else {
					driver.findElement(By.xpath(index1)).sendKeys(serialNumber1[i]);
				}
				((JavascriptExecutor) driver).executeScript("scroll(-1000,0)");
				eo.javaScriptClick(driver, driver.findElement(By.xpath(index)), CURR_APP);
				this.addComment("Clicked on the inventory icon");
				driver.switchTo().defaultContent();
				driver.switchTo().frame(driver.findElement(By.xpath(gei.getProperty("childFrame", CURR_APP))));
				eo.wait(10);
				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("qty", CURR_APP))));

				// eo.waitForWebElementVisible(driver, "XPATH",
				// "serialNumberField", waiTime, CURR_APP);
				String product_qty = eo.getText(driver, "XPATH", "qty", CURR_APP);
				this.addComment("Product quantity is:" + product_qty);
				System.out.println(product_qty);
				double d = Double.parseDouble(product_qty);
				int qty_count = (int) d;
				System.out.println(qty_count);
				// Verifying quantity greater than one.
				Actions action = new Actions(driver);
				if (qty_count > 1) {
					action.sendKeys(Keys.PAGE_DOWN).build().perform();
					eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i], CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("serialNumberField", CURR_APP))).sendKeys(Keys.TAB);
					this.addComment("Added the serial number");
					for (int k = 0; k < qty_count - 1; k++) {
						eo.waitForPopUp(driver);
						for (int j = 0; j < 4; j++) {
							try {
								driver.findElement(By.xpath(gei.getProperty("add_button", CURR_APP))).click();
								driver.findElement(By.xpath(gei.getProperty("add_button", CURR_APP))).click();
							} catch (Exception e) {
							}
						}
						try {
							driver.switchTo().alert();
							return;
						} catch (NoAlertPresentException Ex) {
							addComment("No alert present");
						}
						eo.waitForPopUp(driver);
						this.addComment("Clicked on add button");
						List<WebElement> serialnumber_list = eo.getListOfWebElements(driver, "xpath",
								"serialnumber_rowlist", CURR_APP);
						System.out.println("serial number row list count" + serialnumber_list.size());
						for (int l = 0; l < serialnumber_list.size(); l++) {
							System.out.println(serialnumber_list.get(l).getText());
							if (serialnumber_list.get(l).getText().equals("1")) {
								WebElement addtxt = serialnumber_list.get(l);
								System.out.println(addtxt);
								eo.enterText(driver, "xpath", "serialnumber_add", serialNumber1[i + 1], CURR_APP);
								this.addComment("Added the serial number");
							}
						}
					}
				} else {
					action.sendKeys(Keys.PAGE_DOWN).build().perform();
					eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i], CURR_APP);
					this.addComment("Added the serial number");
				}
				for (int j = 0; j < 4; j++) {
					try {
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();

					} catch (Exception e) {
					}
				}
				this.addComment("Clicked on OK button after enter the serial number");
			}
			eo.waitForWebElementVisible(driver, "XPATH", "saveButtonPurchaseOrder", waiTime, CURR_APP);
			eo.clickElement(driver, "xpath", "saveButtonPurchaseOrder", CURR_APP);
			this.addComment("Saved the order after fulfillment");
			int count = 0;
			this.addComment(
					"Clicking on the customtab and refreshing the page to check sendtoSFDC checkbox is unchecked");
			for (int i = 0; i < 8; i++) {
				driver.navigate().refresh();
				eo.clickElement(driver, "id", "custom_tab", CURR_APP);
				this.addComment("Clicked on Custom tab");
				eo.javaScriptScrollToViewElement(driver, "xpath", "sendtoSFDC_checkbox", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "sendtoSFDC_checkbox", 20, CURR_APP);
				if (eo.isSelected(driver, "XPATH", "sendtoSFDC_checkbox", CURR_APP)) {
					this.addComment("PO details is not synchrnized with salesforce.");
					break;
				} else {
					count++;
				}
				if (count == i) {
					throw new KDTKeywordExecException("Status does not changes Waited for 8 minutes ");
				}
			}
		}
	}

	/////////////////////// Order Billing///////////////
	public static class OrderBilling extends Keyword {

		private String orderNumber;

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			orderNumber = context.getData().get("$orderNo");

			eo.enterText(driver, "xpath", "globalSerachNetsuite", orderNumber, CURR_APP);
			this.addComment("Serached for the order number " + orderNumber);

			eo.waitForWebElementVisible(driver, "XPATH", "actualDisplayedOrder", waiTime, CURR_APP);
			String actualOrderDisplayed = eo.getText(driver, "xpath", "actualDisplayedOrder", CURR_APP);

			if (actualOrderDisplayed.contains(orderNumber)) {
				eo.clickElement(driver, "xpath", "actualDisplayedOrder", CURR_APP);
				this.addComment("Clicked on the order " + orderNumber);
				eo.waitForPageload(driver);
			}

			else {
				this.addComment("Unable to click on the order " + orderNumber);
				throw new KDTKeywordExecException("Unable to click on the order");
			}

			eo.waitForPageload(driver);
			eo.clickElement(driver, "xpath", "nextBillBtn", CURR_APP);
			eo.waitForPageload(driver);
			this.addComment("Clicked on the Nextbill button");

			eo.verifyExactScreenText(driver, "xpath", "invoicePageHeader", "Invoice", CURR_APP);
			eo.verifyExactScreenText(driver, "xpath", "invoiceToBeGenMsg", "To Be Generated", CURR_APP);
			this.addComment("Successfully verified Invoice page is displayed");
		}
	}

	/////////////////////// Invoice Generation For Save///////////////

	// ****************************
	public static class InvoiceGeneration extends Keyword {
		private String orderNumber;
		boolean flag = true;

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);
			orderNumber = context.getData().get("$orderNo");
			// String save_txt = eo.getText(driver, "XPATH", "saveButton", CURR_APP);
			String save_txt = eo.getAttribute(driver, "xpath", "saveButton", "value", CURR_APP);
			System.out.println(save_txt);
			this.addComment("Invoice page is displayed");
			eo.clickElement(driver, "XPATH", "saveButton", CURR_APP);
			this.addComment("Clicked on Save Button");
			try {
				driver.switchTo().alert().accept();
				flag = true;
				this.addComment("Clicked on email alert");
			} catch (NoAlertPresentException Ex) {
				flag = false;
				addComment("No alert present");
			}

			if (flag) {
				//eo.clickElement(driver, "xpath", "invoiceEditBtn", CURR_APP);
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("communicationTab", CURR_APP))));

				eo.actionClick(driver, "XPATH", "communicationTab", CURR_APP);
				this.addComment("Clicked on communication tab");
				eo.clickElement(driver, "XPATH", "massageTab", CURR_APP);
				this.addComment("Clicked on message tab");

				if (eo.getAttribute(driver, "xpath", "emailIdCheckBox", "class", CURR_APP)
						.equalsIgnoreCase("checkbox_ck")) {
					eo.clickElement(driver, "XPATH", "emailIdCheckBox", CURR_APP);
					this.addComment("Unchecked emailID Checkbox");
				}
				eo.clickElement(driver, "XPATH", "saveButton_so", CURR_APP);
			}

			
			orderNumber = context.getData().get("$orderNo");

			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("globalSerachNetsuite", CURR_APP))));
			eo.enterText(driver, "xpath", "globalSerachNetsuite", orderNumber, CURR_APP);
			this.addComment("Serached for the order number " + orderNumber);
			eo.waitForWebElementVisible(driver, "XPATH", "actualDisplayedOrder", waiTime, CURR_APP);
			String actualOrderDisplayed = eo.getText(driver, "xpath", "actualDisplayedOrder", CURR_APP);

			if (actualOrderDisplayed.contains(orderNumber)) {
				eo.clickElement(driver, "xpath", "actualDisplayedOrder", CURR_APP);
				this.addComment("Clicked on the order " + orderNumber);
				eo.waitForPageload(driver);
			}

			else {
				this.addComment("Unable to click on the order " + orderNumber);
				throw new KDTKeywordExecException("Unable to click on the order");
			}
			eo.wait(60);
			driver.navigate().refresh();
			eo.waitForWebElementVisible(driver, "XPATH", "billedOptionVerify", 10, CURR_APP);
			System.out.println("Sucessfully verified Status of order in Sales order Page");
			// eo.verifyExactScreenText(driver, "XPATH", "salesOrderPageVerify", "Sales
			// Order", CURR_APP);
			eo.verifyExactScreenText(driver, "XPATH", "billedOptionVerify", "Billed", CURR_APP);
			this.addComment("Sucessfully verified Status of order in Sales order Page changed to Billed");
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateSalesOrderNetSuite</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Create new Sales Order in
	 * NetSuite </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Neethu</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateSalesOrderNetSuite extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			WebDriver driver = context.getWebDriver();
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			eo.waitForPageload(driver);

			try {
				eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);
				this.addComment("Mouse hover to Transactions Tab");

				eo.hoverMouseUsingJavascript(driver, "xpath", "sales_subtab", CURR_APP);
				this.addComment("Mouse hover to Sales Subtab");

				eo.hoverMouseUsingJavascript(driver, "xpath", "entersalesorder_tab", CURR_APP);
				this.addComment(
						"Mouse hover to EnterSalesOrdethis.addComment(\"Sales order list page is displayed\");rs Subtab");

				eo.hoverMouseUsingJavascript(driver, "xpath", "saleslist_tab", CURR_APP);
				this.addComment("Mouse hover to List Subtab");

				eo.clickElement(driver, "xpath", "saleslist_tab", CURR_APP);
				this.addComment("Clicked on List Subtab");
				this.addComment("Sales order list page is displayed");

				eo.waitForPageload(driver);

				eo.clickElement(driver, "xpath", "newsalesorder_button", CURR_APP);
				this.addComment("Clicked on NewSalesOrder button");
				this.addComment("New sales order page is displayed");

				eo.waitForPageload(driver);

			} catch (Exception e) {

				throw new KDTKeywordExecException("Failed to create new Sales order in Netsuite ");
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateBillingScheduleforSalesOrderNS</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Create Billing Schedule for
	 * the NetSuite Order based on the Terms </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>CreateSalesOrderNetSuite</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Neethu</i></b>
	 * </p>
	 * </div>
	 */
	public static class CreateBillingScheduleforSalesOrderNS extends Keyword {
		public static String term;
		public static String amount;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				term = args.get("Term");
				amount = args.get("Amount");
			} catch (Exception e) {
				this.addComment("Error while initializing");
				throw new KDTKeywordInitException("Unable to initialize", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);
			Actions action = new Actions(driver);

			//String salesorderdate = eo.getAttribute(driver, "xpath", "sodate", "value", CURR_APP);
			String salesorderdate = eo.getText(driver, "xpath", "sodate", CURR_APP);
			this.addComment("SalesOrder date is: " + salesorderdate);

			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));
			this.addComment("Scrolled to items tab");
			String annualbillschedule = eo.getText(driver, "xpath", "billscehedule_column", CURR_APP);

			int terminyear = Integer.parseInt(term) / 12;
			int termremainder = Integer.parseInt(term) % 12;
			System.out.println("Terms in year is:" + terminyear);
			System.out.println("Terms remainder is:" + termremainder);
			System.out.print(annualbillschedule);
			addComment("annualbillschedule:"+annualbillschedule);
			if (annualbillschedule.contains("Custom Term" + term + "months")) {
				this.addComment("Successfully verified the custom schedule content under billing schedule column: "
						+ annualbillschedule);
			}
			else if (annualbillschedule.contains("Annual (" + terminyear + " Yr cont)")) {
				this.addComment("Successfully verified the annual schedule content under billing schedule column: "
						+ annualbillschedule);

			}else 
			{
				this.addComment("Failed verification of Annual schedule content under billing schedule column: "
						+ annualbillschedule);
			}
			

			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("subsidiaryText", CURR_APP))));
			this.addComment("Scrolled to items tab");
			// eo.waitForPopUp(driver);

			eo.clickElement(driver, "ID", "billing_tab", CURR_APP);
			// eo.clickElement(driver, "ID", "billing_tab", CURR_APP);
			this.addComment("Clicked on Billing Tab");

			// eo.waitForPopUp(driver);

			eo.clickElement(driver, "ID", "billing_schedule", CURR_APP);
			this.addComment("Clicked on Schedule SubTab");
			eo.waitForPopUp(driver);

			List<WebElement> date_splits = eo.getListOfWebElements(driver, "xpath", "datelines", CURR_APP);
			int date_splits_number = date_splits.size();
			this.addComment("Number of invoices is:" + date_splits_number);
			if (termremainder == 0) {
				if (Integer.toString(date_splits_number).equals(term) && termremainder == 0) {
					System.out.println("Number of invoices is:" + date_splits_number);
					this.addComment("Number of invoices generated and term are equal");
				}

				for (int i = 0; i < date_splits_number; i++) {
					String invoice_date = date_splits.get(i).getText();

					System.out.println(invoice_date);
					if (invoice_date.contains(salesorderdate)) {
						this.addComment("Invoice start date and line start date are same" + invoice_date);
					}
				}

			} else {
				if (Integer.toString(date_splits_number).equals(term + 1)) {
					System.out.println("Number of invoices is:" + date_splits_number);
					this.addComment("Number of invoices generated and term are equal");
				}

				for (int i = 0; i < date_splits_number; i++) {
					String invoice_date = date_splits.get(i).getText();

					System.out.println(invoice_date);
					if (invoice_date.contains(salesorderdate)) {
						this.addComment("Invoice start date and line start date are same" + invoice_date);
					}
				}

			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * 
	 * <p>
	 * 
	 * <b><i>Keyword Name:</i>VendorDropshipFee</b>
	 * 
	 * </p>
	 * 
	 * <p>
	 * 
	 * <b><i>Description:</i></b> This Keyword is used to verify Dropship Fee Amount
	 * for Super Micro Computer Vendor
	 * 
	 * PO</i>
	 * 
	 * </P>
	 * 
	 * <b><i>Dependencies</i></b>:
	 * 
	 * <ul>
	 * 
	 * <li>Launch</li>
	 * 
	 * <li>LoginNetSuite</li>
	 * 
	 * <li>OpenOrderNetsuite</li>
	 * 
	 * <li>ApproveOrderNetsuite</li>
	 * 
	 * <li>ClickOnDropShip</li>
	 * 
	 * <li>FullfillOrder_PO</li>
	 * 
	 * <li>OpenOrderNetsuite</li>
	 * 
	 * <li>VendorDropshipFee</li>
	 * 
	 * <li>LogoutNetsuite</li>
	 * 
	 * </ul>
	 * 
	 * <b><i>Arguments</i></b>:
	 * 
	 * <ul>
	 * 
	 * 
	 * 
	 * </ul>
	 * 
	 * <p>
	 * 
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Arun Swain</i></b>
	 * 
	 * </p>
	 * 
	 * </div>
	 * 
	 */

	public static class VendorDropshipFee extends Keyword {

		private String type;

		public String amountIndexNum;

		public String expectedDropshipfee;

		public String dropshipAmount;

		@Override

		public void init() throws KDTKeywordInitException {

			super.init();

			try {

				verifyArgs("Type");

				type = args.get("Type");

				expectedDropshipfee = args.get("DropshipFee");

			} catch (Exception e) {

				this.addComment("Type Is miss matched");

				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);

			}

		}

		@Override

		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			WebDriver driver = context.getWebDriver();

			eo.javaScriptScrollToViewElement(driver,

					driver.findElement(By.xpath(gei.getProperty("department", CURR_APP))));

			eo.waitForPopUp(driver);

			eo.actionDoubleClick(driver, "XPATH", "relatedRecordsTab", CURR_APP);

			this.addComment("Clicked on Related records tab");

			eo.waitForPopUp(driver);

			eo.verifyExactScreenText(driver, "xpath", "type", "Purchase Order", CURR_APP);

			this.addComment("Sucessfully Verified Type As Purchase Order");

			eo.clickElement(driver, "XPATH", "date", CURR_APP);

			this.addComment("Sucessfully Clicked On Date Link ");

			eo.verifyExactScreenText(driver, "XPATH", "verifyPurchaseOrder", "Purchase Order", CURR_APP);

			this.addComment("Sucessfully verified Purchase Order Page");

			eo.verifyExactScreenText(driver, "XPATH", "vendorName", "Super Micro Computer, Inc.", CURR_APP);

			this.addComment("Sucessfully Verified Vendor Name As Super Micro Computer, Inc. ");

			eo.javaScriptScrollToViewElement(driver,

					driver.findElement(By.xpath(gei.getProperty("itemslink", CURR_APP))));

			eo.clickElement(driver, "xpath", "itemslink", CURR_APP);

			this.addComment("Clicked on Items link");

			List<WebElement> itemsList = eo.getListOfWebElements(driver, "xpath", "items_List", CURR_APP);

			for (int i = 0; i < itemsList.size(); i++) {

				String ItemList = itemsList.get(i).getText();

				if (ItemList.equalsIgnoreCase("AMOUNT")) {

					int amountIndex = i + 1;

					amountIndexNum = Integer.toString(amountIndex);

					break;

				}

			}

			String dropshipAmount = eo.getTextAfterReplacingKeyValue(driver, "xpath", "dropship_amount", "{count}",
					amountIndexNum, CURR_APP);

			if (dropshipAmount.equals(expectedDropshipfee))

			{

				this.addComment("Dropship Amount Successfully verified");

			} else {

				this.addComment("ActualDropship Fee Amount is" + dropshipAmount);

				this.addComment("ExpectedDropShip Fee Amount is" + expectedDropshipfee);

				throw new KDTKeywordExecException(
						"ActualDropShipFeeAmount And ExpectedDropShipAmount Are Miss Matched");

			}

		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class NavigateToPOPageEnterVendorAndLocation extends Keyword {
		private String POHeaderText = "";
		private String vendorName = "";
		private String locationName = "";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			POHeaderText = args.get("poheadertext").trim();
			vendorName = args.get("vendorname").trim();
			locationName = args.get("locationname").trim();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Navigate to purchase order page
			eo.hoverMouseUsingJavascript(driver, "xpath", "transactionMenuOpt", CURR_APP);
			eo.wait(1);
			eo.hoverMouseUsingJavascript(driver, "xpath", "PurchsesOpt", CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "enterPurchaseOrder", CURR_APP);
			eo.wait(1);
			// Verify the purchase order page is displayed
			eo.verifyExactScreenText(driver, "xpath", "purchaseOrderHeaderText", POHeaderText, CURR_APP);
			addComment("Purchase order page is displayed");
			// Enter Vendor name and Location
			eo.enterText(driver, "xpath", "vendorField", vendorName, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("vendorField", CURR_APP))).sendKeys(Keys.ENTER);
			addComment("Entered Vendor name successfully");
			eo.wait(5);
			eo.enterText(driver, "xpath", "locationField", locationName, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("vendorField", CURR_APP))).sendKeys(Keys.ENTER);
			addComment("Entered Location name successfully");
			eo.wait(2);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// public static class addItemsAndSN extends Keyword { /// need to finish
	// private String[] getNameOfItems;
	// private String[] getQtyForEachItem;
	// private int i = 0;
	//
	// @Override
	// public void init() throws KDTKeywordInitException {
	// super.init();
	// getNameOfItems = args.get("Getnameofitems").trim().split(";");
	// getQtyForEachItem = args.get("Getqtyforeachitem").split(";");
	// }
	//
	// @Override
	// public void exec() throws KDTKeywordExecException {
	// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
	// WebDriver driver = context.getWebDriver();
	// eo.javaScriptScrollToViewElement(driver,
	// driver.findElement(By.xpath(gei.getProperty("saveBtnIRPage", CURR_APP))));
	// eo.wait(3);
	// eo.clickElement(driver, "xpath", "subItemTab", CURR_APP);
	// addComment("Navigation to Sub-items tab is successfull");
	// eo.wait(1);
	// for (i = 0; i < getNameOfItems.length; i++) {
	// // Click on itemdropdown icon
	// eo.enterText(driver, "xpath", "itemField", getNameOfItems[i].trim(),
	// CURR_APP);
	// eo.waitForPageload(driver);
	// driver.findElement(By.xpath(gei.getProperty("itemField",
	// CURR_APP))).sendKeys(Keys.TAB);
	// // driver.findElement(By.xpath(gei.getProperty("itemField",
	// // CURR_APP))).sendKeys(Keys.ENTER);
	// addComment("Entered Item: " + getNameOfItems[i]);
	// eo.wait(3);
	// }
	// eo.actionClick(driver, "xpath", "serialNumIcon", CURR_APP);
	// eo.actionClick(driver, "xpath", "serialNumIcon1", CURR_APP);
	// driver.switchTo().defaultContent();
	// driver.switchTo().frame(driver.findElement(By.xpath(gei.getProperty("childFrame",
	// CURR_APP))));
	// eo.waitForWebElementVisible(driver, "XPATH", "serialNumberField", waiTime,
	// CURR_APP);
	// String randonString = eo.gnerateRandomNo(6);
	// Actions action1 = new Actions(driver);
	// action1.sendKeys(Keys.PAGE_DOWN).build().perform();
	// eo.actionDoubleClick(driver, "xpath", "serialnumber_addSPO", CURR_APP);
	// eo.enterText(driver, "xpath", "serialnumber_addSPO", randonString, CURR_APP);
	// driver.findElement(By.xpath(gei.getProperty("serialnumber_addSPO",
	// CURR_APP))).sendKeys(Keys.TAB);
	// this.addComment("Added the serial number");
	// eo.wait(1);
	// // eo.waitForWebElementVisible(driver, "XPATH", "secondaryokSNpopup",
	// waiTime,
	// // CURR_APP);
	// // driver.findElement(By.xpath(gei.getProperty("secondaryokSNpopup",
	// // CURR_APP))).click();
	// eo.wait(1);
	// driver.findElement(By.xpath(gei.getProperty("secondaryokSNpopup",
	// CURR_APP))).click();
	// eo.waitForWebElementVisible(driver, "XPATH", "saveBtnIRPage", waiTime,
	// CURR_APP);
	// eo.wait(1);
	// eo.clickElement(driver, "xpath", "saveBtnIRPage", CURR_APP);
	// addComment("Added item and serial number and saved record successfully");
	// eo.wait(2);
	// }
	// }
	///////////////////////////////////////////////////////////////////////////////////////////
	public static class addItemsAndSN extends Keyword { /// need to finish
		private String[] getNameOfItems;
		private String[] getQtyForEachItem;
		private int i = 0;
		private String[] serialNumber1;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			serialNumber1 = args.get("SerialNumber1").split(";");
			getNameOfItems = args.get("Getnameofitems").trim().split(";");
			getQtyForEachItem = args.get("Getqtyforeachitem").split(";");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("saveBtnIRPage", CURR_APP))));
			eo.wait(3);
			eo.clickElement(driver, "xpath", "subItemTab", CURR_APP);
			addComment("Navigation to Sub-items tab is successfull");
			eo.wait(1);
			for (i = 0; i < getNameOfItems.length; i++) {
				// Click on itemdropdown icon
				eo.enterText(driver, "xpath", "itemField", getNameOfItems[i].trim(), CURR_APP);
				eo.waitForPageload(driver);
				driver.findElement(By.xpath(gei.getProperty("itemField", CURR_APP))).sendKeys(Keys.TAB);
				// driver.findElement(By.xpath(gei.getProperty("itemField",
				// CURR_APP))).sendKeys(Keys.ENTER);
				addComment("Entered Item: " + getNameOfItems[i]);
				eo.wait(3);
			}
			List<WebElement> serialNumberList = eo.getListOfWebElements(driver, "xpath", "serielNumberList", CURR_APP);
			int size = serialNumberList.size();
			String listxpath = gei.getProperty("serielNumberList", CURR_APP);
			for (int i = 0; i < size; i++) {
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "serielNumberList_Clickable", "{iteration}",
						Integer.toString(i + 1), CURR_APP);
				this.addComment("Clicked on the first icon of serial number");
				eo.clickElement(driver, "xpath", "serialNumberIcon2", CURR_APP);
				eo.wait(3);
				this.addComment("Clicked on the second icon of serial number");
				driver.switchTo().defaultContent();
				driver.switchTo().frame(driver.findElement(By.xpath(gei.getProperty("childFrame", CURR_APP))));
				eo.waitForWebElementVisible(driver, "XPATH", "serialNumberField", waiTime, CURR_APP);
				String product_qty = eo.getText(driver, "XPATH", "qty", CURR_APP);
				this.addComment("Product quantity is:" + product_qty);
				System.out.println(product_qty);
				double d = Double.parseDouble(product_qty);
				int qty_count = (int) d;
				System.out.println(qty_count);
				// Verifying quantity greater than one.
				Actions action = new Actions(driver);
				if (qty_count > 1) {
					action.sendKeys(Keys.PAGE_DOWN).build().perform();
					eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i] + randNum, CURR_APP);
					eo.wait(1);
					this.addComment("Added the serial number");
					qty_count--;
					for (int k = 0; k < qty_count; k++) {
						eo.actionClick(driver, "XPATH", "add_button", CURR_APP);
						eo.wait(1);
						eo.actionClick(driver, "XPATH", "add_button", CURR_APP);
						eo.wait(2);
						this.addComment("Clicked on add button");
						eo.waitForWebElementVisible(driver, "xpath", "serialNumberField", 30, CURR_APP);
						eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i + 1] + randNum, CURR_APP);
						driver.findElement(By.xpath(gei.getProperty("serialNumberField", CURR_APP))).sendKeys(Keys.TAB);
						this.addComment("Added the serial number");
					}
				} else {
					action.sendKeys(Keys.PAGE_DOWN).build().perform();
					eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i] + randNum, CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("serialNumberField", CURR_APP))).sendKeys(Keys.TAB);
					this.addComment("Added the serial number");
				}

			}
			for (int j = 0; j < 4; j++) {

				try {
					driver.findElement(By.xpath(gei.getProperty("secondaryokSNpopup", CURR_APP))).click();
				} catch (Exception e) {

				}
			}
			eo.waitForWebElementVisible(driver, "XPATH", "saveBtnIRPage", waiTime, CURR_APP);
			eo.wait(1);
			eo.clickElement(driver, "xpath", "saveBtnIRPage", CURR_APP);
			addComment("Added item and serial number and saved record successfully");
			eo.wait(2);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class editPOThenVerifyStatusAndClickRec extends Keyword {
		private String enterApprovalStatusTxt; // Approved
		private String transactionSucMsgTxt;
		private String approvalStatusPendingRecieveTxt;
		private String itemReceiptPageHeaderTxt;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			enterApprovalStatusTxt = args.get("ApprovalStatusText");
			transactionSucMsgTxt = args.get("TransactionSucMsg");
			approvalStatusPendingRecieveTxt = args.get("ApprovalStatusPendingRecieveText");
			itemReceiptPageHeaderTxt = args.get("ItemReceiptPageHeaderText");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Click on edit botton
			eo.clickElement(driver, "xpath", "editBtn", CURR_APP);
			// Change approval status to approved
			eo.clearData(driver, "xpath", "approvalStatusField", CURR_APP);
			eo.enterText(driver, "xpath", "approvalStatusField", enterApprovalStatusTxt, CURR_APP);
			// Click on save button
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("purchaseOrderHeaderText", CURR_APP))));
			eo.clickElement(driver, "xpath", "saveBtnPOPage", CURR_APP);
			eo.wait(2);
			addComment("Record saved successfully");
			// Wait for page to load
			// Verify the transaction successful message is displayed and approval status is
			// changed to 'Approved'
			eo.verifyExactScreenText(driver, "xpath", "transactionSavedMsg", transactionSucMsgTxt, CURR_APP);
			addComment("Transaction Successfull Message displayed successfully");
			// Verify the PO status is changed to APPROVED BY SUPERVISOR/PENDING RECEIPT
			eo.verifyExactScreenText(driver, "xpath", "approvalStatusChangedTxt", approvalStatusPendingRecieveTxt,
					CURR_APP);
			addComment("Approval Status Changed to 'Pending recieve' successfully");
			// Click on Recieve button on Po Page
			eo.clickElement(driver, "xpath", "receiveBtn", CURR_APP);
			addComment("Clicked recieve button");
			// wait until the page loads and Verify the Item reciept page is displayed
			eo.verifyExactScreenText(driver, "xpath", "itemReceiptPageHeaderTxt", itemReceiptPageHeaderTxt, CURR_APP);
			addComment("Item Receipt Page displayed successful");
		}

	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class MarkAllItemsAndClickOnSave extends Keyword {
		private String transactionSucMsgTxt;
		private String POHeaderText;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			transactionSucMsgTxt = args.get("TransactionSucMsgText");
			POHeaderText = args.get("POHeaderText");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("markAllBtn", CURR_APP))));
			// Click on MArk All Button
			eo.clickElement(driver, "xpath", "markAllBtn", CURR_APP);
			addComment("Clicked on Markall check box");
			// Click on save button
			eo.clickElement(driver, "xpath", "saveBtnIRPage", CURR_APP);
			addComment("Clicked on Save button");
			eo.wait(2);
			// Verify the 'transaction successfully saved message is displayed'
			eo.verifyExactScreenText(driver, "xpath", "transactionSavedMsg", transactionSucMsgTxt, CURR_APP);
			addComment("Saved record and transaction successfully saved message is displayed");
			// Click on Purchase Order link under 'Create from' section to goto Purchase
			// order page
			eo.clickElement(driver, "xpath", "POLinkOnItemRecPage", CURR_APP);
			addComment("Clicked on PO link");
			// Verify the 'Purchase Order' page is displayed
			eo.verifyExactScreenText(driver, "xpath", "purchaseOrderHeaderText", POHeaderText, CURR_APP);
			addComment("Navigated to Purchase Order page successfully");
		}
	}

	////////////////////// add refresh page
	////////////////////// code/////////////////////////////////////////////////////////////

	public static class VerifyItemReceiptAndInvoiceDate extends Keyword {
		private String approvalStatusFullyBilledtext;
		private String itemReceiptDate;
		private String invoiceDate;
		private String approvalStatusPendingBillingTxt;

		@Override
		public void init() throws KDTKeywordInitException {
			// TODO Auto-generated method stub
			super.init();
			approvalStatusPendingBillingTxt = args.get("ApprovalStatusPendingBillingText");
			approvalStatusFullyBilledtext = args.get("FullyBilledtext");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Verify the Purchase order status is changed to "Pending Billing"
			eo.verifyExactScreenText(driver, "xpath", "approvalStatusChangedTxt", approvalStatusPendingBillingTxt,
					CURR_APP);
			addComment("Approval status chaged to Pending Billing");
			// Wait for script execution
			for (int i = 0; i < 20; i++) {
				if (!eo.getText(driver, "xpath", "approvalStatusChangedTxt", CURR_APP)
						.equalsIgnoreCase(approvalStatusFullyBilledtext)) {
					System.out.println(eo.getText(driver, "xpath", "approvalStatusChangedTxt", CURR_APP));
					eo.wait(60);
					driver.navigate().refresh();
				} else if (eo.getText(driver, "xpath", "approvalStatusChangedTxt", CURR_APP)
						.equalsIgnoreCase(approvalStatusFullyBilledtext)) {
					// After refreshing the page verify the status is chnged to fully billed
					System.out.println(eo.getText(driver, "xpath", "approvalStatusChangedTxt", CURR_APP));
					addComment("Approval status chaged to Fully Billed");
					break;
				}
			}
			// Navigate to Related Records tab
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("relatedRecordstab", CURR_APP))));
			// click on related records tab
			eo.clickElement(driver, "xpath", "relatedRecordstab", CURR_APP);
			addComment("Navigated to related records");
			// Fetch Invoice(Bill) date
			invoiceDate = eo.getText(driver, "xpath", "invoiceDate", CURR_APP).trim();
			addComment("Fetched Invoice date");
			// Fetch Item receipt date
			itemReceiptDate = eo.getText(driver, "xpath", "itemReceiptDate", CURR_APP).trim();
			addComment("Fetched item reciept date");
			// Verify the Item receipt date and invoice date is same
			if (itemReceiptDate.equals(invoiceDate)) {
				addComment("Verification of Item reciept and Invoice date is successfull");
				System.out.println("Verification of Item Receipt date and Invoice date is successful:" + " "
						+ "Item Receipt date:" + itemReceiptDate + " " + "Invoice date:" + invoiceDate);
			} else {
				throw new KDTKeywordExecException("Verification of Item Receipt date and Invoice date is unsuccessful");
			}

		}

	}

	public static class EditLocationAndVerifyLineVendorName extends Keyword {
		private String newLocationName;
		private String expectedVendorName;
		private String currentLocationName;
		private String vendorNameBeforeUpdate;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			expectedVendorName = args.get("ExpectedVendorName").trim();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Fetch the current location
			currentLocationName = eo.getText(driver, "xpath", "locationFieldSaleOrderPage", CURR_APP);
			// fetch vendor name before update
			vendorNameBeforeUpdate = eo.getText(driver, "xpath", "vendorFieldSalesOrderPage", CURR_APP);

			// enter new location name into location field
			if (!currentLocationName.equals(newLocationName)) {
				eo.enterText(driver, "xpath", "locationFieldSaleOrderPage", newLocationName, CURR_APP);// Entered new
																										// location name
				driver.findElement(By.xpath(gei.getProperty("locationFieldSaleOrderPage", CURR_APP)))
						.sendKeys(Keys.ENTER);
				eo.clickElement(driver, "xpath", "saveButton", CURR_APP); // delete if not required
				eo.wait(3);
				driver.navigate().refresh();
			}

			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("secEdit", CURR_APP))));
			List<WebElement> vendorList = driver
					.findElements(By.xpath(gei.getProperty("vendorNameListSalesOrderPage", CURR_APP)));
			ArrayList<String> listofvendorname = new ArrayList<String>();
			for (WebElement ele : vendorList) {
				if (!ele.getText().isEmpty()) {
					listofvendorname.add(ele.getText().trim());
				}

			}
			LinkedHashSet<String> hashSet = new LinkedHashSet<>(listofvendorname);
			ArrayList<String> listOfVendorsWithoutDuplicates = new ArrayList<>(hashSet);
			if (listOfVendorsWithoutDuplicates.contains(expectedVendorName)) {
				addComment("successfull:" + listOfVendorsWithoutDuplicates);
				addComment("successfull:" + expectedVendorName);
			} else {
				throw new KDTKeywordExecException("validation Unsuccessful");
			}
		}
	}

	////////////////////// EnterSalesOrderDetails///////////////////////////////////////////////////////////////////////

	public static class EnterSalesOrderDetails extends Keyword {
		public static String subscriptionitem;
		public static String distributorname;
		public static String distributorponumber;
		public static String enduser;
		public static String quantity;
		public static String term;
		public static String amount;
		private String saveto;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				distributorname = args.get("DistributorName");
				enduser = args.get("EndUser");
				subscriptionitem = args.get("SubscriptionItem");
				quantity = args.get("ProductQuantity");
				/*
				 * term = args.get("Term"); amount = args.get("Amount"); saveto =
				 * args.get("SaveTo");
				 */
			} catch (Exception e) {
				this.addComment("Error while initializing");
				throw new KDTKeywordInitException("Unable to initialize", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "distributor_txtfield", distributorname, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("distributor_txtfield", CURR_APP))).sendKeys(Keys.ENTER);
			this.addComment("Entered Distributor name is:" + distributorname);
			// eo.wait(10);
			eo.waitForPageload(driver);
			eo.waitForPageload(driver);
			distributorponumber = eo.gnerateRandomNo(5);
			eo.enterText(driver, "xpath", "distributorpo_txtfield", distributorponumber, CURR_APP);
			// eo.clearData(driver, "xpath", "salesDate", CURR_APP);
			// eo.enterText(driver, "xpath", "salesDate", "7/19/2019", CURR_APP);
			this.addComment("Entered DistributorPO number is: " + distributorponumber);
			eo.enterText(driver, "xpath", "enduser_txtfield", enduser, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("distributor_txtfield", CURR_APP))).sendKeys(Keys.ENTER);
			this.addComment("Entered Enduser name is: " + enduser);

		}
	}
	////////////////////// SelectProductAndEnterDetails///////////////////////////////////////////////////////////////////////

	public static class SelectProductAndEnterDetails extends Keyword {
		public static String subscriptionitem;
		public static String quantity;
		public static String term;
		public static String rate;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				subscriptionitem = args.get("SubscriptionItem");
				quantity = args.get("ProductQuantity");
				rate = args.get("ProductRate");
				if (hasArgs("Term")) {
					term = args.get("Term");
				} else {
					term = "";
				}

			} catch (Exception e) {
				this.addComment("Error while initializing");
				throw new KDTKeywordInitException("Unable to initialize", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			WebDriver driver = context.getWebDriver();
			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));

			this.addComment("Scrolled to items tab");

			eo.clickElement(driver, "xpath", "subscriptionitem_tab", CURR_APP);

			eo.enterText(driver, "ID", "subscription_item", subscriptionitem, CURR_APP);
			this.addComment("Entered subscription item :" + subscriptionitem);
			eo.waitForPageload(driver);
			List<WebElement> subscriptionitem_list = eo.getListOfWebElements(driver, "xpath", "subscriptionline_items",
					CURR_APP);
			for (int i = 0; i < subscriptionitem_list.size(); i++) {
				String subscriptionitem_txt = subscriptionitem_list.get(i).getText();
				if (subscriptionitem_txt.equalsIgnoreCase(subscriptionitem)) {
					subscriptionitem_list.get(i).click();
					eo.wait(5);
					try {
						// Wait 10 seconds till alert is present
						WebDriverWait wait = new WebDriverWait(driver, 10);
						Alert alert = wait.until(ExpectedConditions.alertIsPresent());
						alert.accept();
					} catch (Exception e) {
						addComment("Alert is not present");
					}

					break;
				}
			}
			// eo.waitForPageload(driver);
			int qtyCount = headerCount(driver, "Qty");
			String qtyHeadercountValue = Integer.toString(qtyCount);
			eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "headerValue", "{count}", qtyHeadercountValue,
					CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("product_quantity", CURR_APP))).sendKeys(Keys.BACK_SPACE);
			eo.enterText(driver, "XPATH", "product_quantity", quantity, CURR_APP);
			addComment("Successfully enter the quantity " + quantity);
			if (term.length() > 0) {
				int termCount = headerCount(driver, "Term");
				String termHeadercountValue = Integer.toString(termCount);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "headerValue", "{count}", termHeadercountValue,
						CURR_APP);
				eo.enterText(driver, "XPATH", "product_term", term, CURR_APP);
				addComment("Successfully enter the term " + term);
			}
			int rateCount = headerCount(driver, "Rate");
			String rateHeadercountValue = Integer.toString(rateCount);
			eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "headerValue", "{count}", rateHeadercountValue,
					CURR_APP);
			eo.enterText(driver, "XPATH", "product_rate", rate, CURR_APP);
			addComment("Successfully enter the rate " + rate);

			if (term.length() > 0) {
				Calendar cal = Calendar.getInstance();
				String supportStartDate = getDate(cal);
				int termvalue = Integer.parseInt(term);
				cal.add(Calendar.MONTH, termvalue);
				String supportEndDate = getDate(cal);
				int supportStartDateCount = headerCount(driver, "Support Start Date");
				String supportStartDateHeadercountValue = Integer.toString(supportStartDateCount);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "headerValue", "{count}",
						supportStartDateHeadercountValue, CURR_APP);
				eo.enterText(driver, "XPATH", "supportStartDate", supportStartDate, CURR_APP);
				addComment("Successfully enter the support start date " + supportStartDate);
				int supportEndDateCount = headerCount(driver, "Support End Date");
				String supportEndDateHeadercountValue = Integer.toString(supportEndDateCount);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "headerValue", "{count}",
						supportEndDateHeadercountValue, CURR_APP);
				eo.enterText(driver, "XPATH", "supportEndDate", supportEndDate, CURR_APP);
				addComment("Successfully enter the support start date " + supportEndDate);

			}
			eo.clickElement(driver, "XPATH", "addButton", CURR_APP);
			eo.wait(1);
			addComment("Successfully click on the add button");
		}

	}

	public static int headerCount(WebDriver driver, String headerName) throws KDTKeywordExecException {
		List<WebElement> ele = eo.getListOfWebElements(driver, "XPATH", "HeaderRow", CURR_APP);
		int count = 0;
		for (int i = 0; i < ele.size(); i++) {
			if (ele.get(i).getText().equalsIgnoreCase(headerName)) {
				break;
			} else {
				count++;
			}
		}
		if (count == ele.size()) {
			throw new KDTKeywordExecException("Header is not present or header name changed" + headerName);
		}
		return count + 1;
	}

	public static String getDate(Calendar cal) {
		return "" + (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.DATE)) + "/" + cal.get(Calendar.YEAR);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class SaveandApproveNetsuiteSalesOrder extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			WebDriver driver = context.getWebDriver();

			eo.clickElement(driver, "xpath", "saveButtonPurchaseOrder", CURR_APP);
			this.addComment("Clicked on SAVE button ");
			eo.waitForPageload(driver);

			eo.verifyExactScreenText(driver, "xpath", "salesorder_txt", "Sales Order", CURR_APP);
			this.addComment("verified Sales order page is successfully displayed");

			eo.verifyExactScreenText(driver, "xpath", "transactionsaved_msg", "Transaction successfully Saved",
					CURR_APP);
			this.addComment("verified Transaction successfully Saved meassage");

			eo.clickElement(driver, "xpath", "netSuiteApprovebtn", CURR_APP);

			eo.waitForPageload(driver);
			this.addComment("Approved the order");

			eo.verifyExactScreenText(driver, "xpath", "transactionsaved_msg", "Sales Order successfully Approved",
					CURR_APP);
			this.addComment("verified Sales Order successfully Approved meassage");

		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static class ClickNextBillandGenearateInvoice extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);

			eo.clickElement(driver, "xpath", "nextBillBtn", CURR_APP);
			this.addComment("Clicked on the Nextbill button");
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "invoicePageHeader", "Invoice", CURR_APP);
			eo.verifyExactScreenText(driver, "xpath", "invoiceToBeGenMsg", "To Be Generated", CURR_APP);
			this.addComment("Successfully verified Invoice page is displayed");

			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("classification_header", CURR_APP))));

			eo.actionClick(driver, "XPATH", "communicationTab", CURR_APP);
			this.addComment("Clicked on communication tab");
			eo.clickElement(driver, "XPATH", "massageTab", CURR_APP);
			this.addComment("Clicked on message tab");
			eo.clickElement(driver, "XPATH", "emailIdCheckBox", CURR_APP);
			this.addComment("Unchecked emailID Checkbox");
			// eo.javaScriptScrollToViewElement(driver,
			// driver.findElement(By.xpath(gei.getProperty("invoice_save", CURR_APP))));
			eo.clickElement(driver, "id", "invoice_save", CURR_APP);
			this.addComment("Clicked on SaveButton");
			eo.waitForPageload(driver);
			eo.clickElement(driver, "xpath", "print_icon", CURR_APP);
			ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs2.get(1));
			eo.wait(5);
			String pdfurl = driver.getCurrentUrl();

			try {
				URL url = new URL(pdfurl);
				InputStream pdfpath = url.openStream();
				// InputStream is = new FileInputStream(pdfpath);
				File file = new File(pdfurl);
				PDFParser parser = new PDFParser((RandomAccessRead) new FileInputStream(file));
				// BufferedInputStream input = new BufferedInputStream(pdfpath);
				PDDocument doc = PDDocument.load(file);
				// PDFParser parsepdf = new PDFParser(input);
				// parsepdf.parse();
				String textofpdf = new PDFTextStripper().getText(doc);
				System.out.println(textofpdf);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			/*
			 * try {
			 * 
			 * Robot robot = new Robot();
			 * 
			 * robot.keyPress(KeyEvent.VK_CONTROL);
			 * 
			 * 
			 * robot.keyRelease(KeyEvent.VK_C); robot.keyRelease(KeyEvent.VK_CONTROL);
			 * eo.wait(5); robot.keyPress(KeyEvent.VK_V);
			 * 
			 * } catch(Exception e) {
			 * 
			 * }
			 * 
			 */

			// eo.hoverMouseUsingJavascript(driver,"id", "downoad_btn", CURR_APP);
			// eo.wait(10);
			// eo.clickElement(driver,"id", "downoad_btn", CURR_APP);
			// eo.javaScriptClick(driver,"id", "downoad_btn", CURR_APP);
			// driver.close();
			// driver.switchTo().window(tabs2.get(0));
			// eo.clickElement(driver, property, keyValue, application);

		}

		public static class verifyPDF extends Keyword {

			@Override
			public void exec() throws KDTKeywordExecException {
				// TODO Auto-generated method stub
				WebDriver driver = context.getWebDriver();
				String filename = System.getProperty("user.dir") + "\\ApplicationData\\Downloads";
				File folder = new File(filename);
				try {
					FileUtils.cleanDirectory(folder);
					eo.wait(2);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				driver.get("chrome://settings/content/pdfDocuments");
				String url = driver.getCurrentUrl();
				Actions action = new Actions(driver);
				action.sendKeys(Keys.TAB).build().perform();
				action.sendKeys(Keys.TAB).build().perform();
				action.sendKeys(Keys.ENTER).build().perform();
				driver.navigate().back();
				eo.clickElement(driver, "XPATH", "printButton", CURR_APP);
				eo.wait(5);
				String pdfFolderPath = System.getProperty("user.dir") + "\\ApplicationData\\Downloads";
				File pdffolder = new File(pdfFolderPath);
				File[] listOfFiles = pdffolder.listFiles();
				String fileName = "";
				if (listOfFiles.length == 1) {
					fileName = listOfFiles[0].toString();
				} else {
					throw new KDTKeywordExecException("More than 1 file is present in download folder");
				}
				PDFUtil pdfUtil = new PDFUtil();
				try {
					String fullpdfContent = pdfUtil.getText(fileName);
					System.out.print(fullpdfContent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public static class EditLocationAndVerifyPOVendorName extends Keyword {
		private String locationName;
		private String expectedPOVendorName;
		ArrayList<String> poVendorNameAfterChange = new ArrayList<String>();

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			locationName = args.get("Location");
			expectedPOVendorName = args.get("ExpectedPOVendor");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			// Verify the sales order is in pending billing stage
			eo.verifyExactScreenText(driver, "xpath", "approvalStatusChangedTxt", "PENDING FULFILLMENT", CURR_APP);

			// Verify the po is not created for all any items
			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));
			List<WebElement> createPOList = driver.findElements(By.xpath(gei.getProperty("CreatePOList", CURR_APP)));
			for (WebElement createPO : createPOList) {
				if (!createPO.getText().equalsIgnoreCase("Drop Ship")) {
					addComment("Purchase order is created for the product");
					throw new KDTKeywordExecException("Purchase order is created for the product/s");
				} else {
					addComment("Purchase order is not created");
				}
			}
			eo.wait(1);
			// Verify the sales order is in pending billing stage
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("salesOrderHeaderText", CURR_APP))));
			eo.clickElement(driver, "xpath", "editBtn", CURR_APP);
			addComment("Edit is done");
			eo.wait(2);

			// Enter Location name in location field
			eo.javaScriptScrollToViewElement(driver, "xpath", "locationSOPage_PFStage", CURR_APP);
			eo.enterText(driver, "xpath", "locationSOPage_PFStage", locationName, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("locationSOPage_PFStage", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered Location name successfully");
			eo.wait(1);

			// Save the record
			eo.javaScriptScrollToViewElement(driver, "xpath", "salesOrderHeaderText", CURR_APP);
			addComment("Found save");
			eo.clickElement(driver, "xpath", "saveBtn", CURR_APP);
			addComment("Clicked on save");
			eo.wait(2);

			// Verify the vendor is changed to new vendor
			// Fetch the list of new vendor
			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));
			List<WebElement> vendorListAfterchange = driver
					.findElements(By.xpath(gei.getProperty("vendorNameListSalesOrderPage", CURR_APP)));
			for (WebElement VL : vendorListAfterchange) {
				String PO = VL.getText().trim();
				if (PO.equalsIgnoreCase(expectedPOVendorName) || PO.isEmpty()) {
					addComment("PO vendor is changed");
				} else {
					throw new KDTKeywordExecException("PO vendor is not changed");
				}
			}
			eo.clickElement(driver, "xpath", "homeicon", CURR_APP);
			eo.wait(3);
		}

	}

	// New vendor page
	public static class NavigateToNewVendorPage extends Keyword {
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);
			// Navigate To New vendorpage
			eo.hoverMouseUsingJavascript(driver, "xpath", "List_Tab", CURR_APP);
			addComment("Mouse hover to List Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "Relationship_Tab", CURR_APP);
			addComment("MovetoElement to Relationships Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "Vendors_Tab", CURR_APP);
			eo.clickElement(driver, "xpath", "Vendors_Tab", CURR_APP);
			addComment("Click on element Vendors_Tab");
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "VendorsHeaderText", "Vendors ", CURR_APP);
			eo.clickElement(driver, "xpath", "NewVendorBtn", CURR_APP);
			addComment("Clicked on new vendor button");
			eo.verifyExactScreenText(driver, "xpath", "VendorFormHeaderText", "Vendor", CURR_APP);
		}
	}
////////////////////////////////////////////////////////

	public static class DeleteVendorBills extends Keyword {
	 
	 private String[] paymentMode;
	

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			try {

				paymentMode = args.get("PaymentMode").split(";");
			} catch (Exception e) {
				this.addComment("Error while initializing LoginNetSuite");
				throw new KDTKeywordInitException("Error while initializing LoginNetSuite", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
				WebDriver driver = context.getWebDriver();
				int j=0;
				int counter=0;
			for(int k=0;k<paymentMode.length;k++)					
			{	
			// mousehover on transaction option
			eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);
			
			this.addComment("Mouse hover to Transactions Tab");
			// mouse hover on payables
			eo.hoverMouseUsingJavascript(driver, "xpath", "payablesTab", CURR_APP);
			
			// Click create payment batch option
			eo.clickElement(driver, "xpath", "Payment_Batch_List", CURR_APP);
			this.addComment("Successfully clicked on Vendor Payment search Batch");
			eo.waitForPageload(driver);
			eo.wait(10);
			if(j==0)
			{
			eo.clickElement(driver, "xpath", "open_filter", CURR_APP);
			j=j+1;
			this.addComment("Successfully clicked on Filter");
			}
			eo.clickElement(driver, "xpath", "Searchid", CURR_APP);
			eo.clickElement(driver, "xpath", "Dropdown", CURR_APP);
			eo.waitForPageload(driver);
			eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "Dropdown_value", "{text}", paymentMode[k],CURR_APP);
			this.addComment("Successfully clicked Payment mode"+paymentMode[k]);
			eo.waitForPageload(driver);
			List<WebElement> EditLink_list = driver.findElements(By.xpath(gei.getProperty("EditLinkList", CURR_APP)));
			if(EditLink_list.size()==0)
			{
				this.addComment("No Edit links");
			}else
			{
			for(int i=0;i<EditLink_list.size();i++){
				//EditLink_list1 = driver.findElements(By.xpath(gei.getProperty("EditLink", CURR_APP)));
				counter=i+1;
				this.addComment(counter+" number of links to be clicked for Payment mode : "+paymentMode[k]);
				
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "EditLink", "{iteration}", Integer.toString(i+1) , CURR_APP);
				eo.waitForPageload(driver);
				if(eo.isSelected(driver, "xpath", "void", CURR_APP))			
				{
				eo.clickElement(driver, "xpath", "void_Click", CURR_APP);
				//eo.wait(10);
				eo.clickElement(driver, "xpath", "void_Click", CURR_APP);
				//eo.wait(10);
				eo.clickElement(driver, "xpath", "saveVendorSearch", CURR_APP);
				eo.wait(10);
				}
				else
				{
					eo.clickElement(driver, "xpath", "void_Click", CURR_APP);
					eo.clickElement(driver, "xpath", "saveVendorSearch", CURR_APP);
					eo.wait(10);
				}
				
			}
			}
			}
	}
		}//last line
		
		
	
	
	
	/////////////////////////////////////////////////
	public static class NavigateToScriptPage extends Keyword {
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);
			// Navigate to scripts page
			eo.hoverMouseUsingJavascript(driver, "xpath", "customizationTab", CURR_APP);
			addComment("Mouse hover to customization Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "scriptingSubTab", CURR_APP);
			addComment("MovetoElement to scriptingSubTab Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "scripts", CURR_APP);
			eo.clickElement(driver, "xpath", "scripts", CURR_APP);
			addComment("Click on element scripts");
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "scriptsHeaderText", "Scripts ", CURR_APP);

		}
	}

	//////////////////////
	public static class alertPresent extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			if (ExpectedConditions.alertIsPresent() != null) {
				Alert alert = driver.switchTo().alert();
				String alertText = alert.getText().trim();
				if (alertText.equals("Please enter value(s) for: Coupa PO Email")) {
					addComment(alertText + " is displayed");
					alert.accept();
					// Select coup po method as prompt or cxml
					eo.waitForPageload(driver);
					eo.clickElement(driver, "xpath", "secondaryCancelBtn", CURR_APP);
				} else if (alertText.contains("This record may have duplicates")) {
					addComment(alertText + " is displayed");
					alert.accept();
					addComment("alert done");
					// Select coup po method as prompt or cxml
					eo.waitForPageload(driver);

					eo.clickElement(driver, "xpath", "gobackLink", CURR_APP);
					addComment("Clicked go back button");
					eo.clickElement(driver, "xpath", "secondaryCancelBtn", CURR_APP);
					addComment("Clicked cancel button");
				}

			}

		}
	}
	/////////////////////////////

	public static class VendorSuccessfullySaved extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			if (eo.verifyExactScreenText(driver, "xpath", "vendorSavedSucc", "Vendor successfully Saved", CURR_APP)) {
				addComment("Vendor successfully Saved message is displayed");
			}
		}
	}

	//////////////////////

	public static class EditVendorRecord extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Click edit vendor
			eo.clickElement(driver, "xpath", "editVendorBtn", CURR_APP);
			// clear email field
			eo.waitForPageload(driver);
			eo.clearData(driver, "xpath", "coupaPOEmailTextBox", CURR_APP);
			// Click on the save button
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("saveButton_so", CURR_APP))));
			eo.clickElement(driver, "xpath", "saveButton_so", CURR_APP);
		}
	}

	////////////
	public static class FillNewVendorForm extends Keyword {
		private String typeFlag;
		private String emailFlag;
		private String includeInCoupaFlag;
		// private String companyName;
		private String companyNameFlag;
		private String email;
		private String fname;
		private String lname;
		private String coupaPOMethod;
		private String phoneNum;
		private String primarySubsidiary;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			typeFlag = args.get("TypeFlag");
			emailFlag = args.get("EmailFlag");
			includeInCoupaFlag = args.get("IncludeInCoupaFlag");
			companyName = args.get("Company");
			companyNameFlag = args.get("CompanyNameFlag");
			email = args.get("Email");
			fname = args.get("Fname");
			lname = args.get("Lname");
			coupaPOMethod = args.get("CoupaPOMethod");
			phoneNum = args.get("PhoneNum");
			primarySubsidiary = args.get("PrimarySubsidiary");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Select the type

			if (typeFlag.equalsIgnoreCase("true")) {
				// Select type as Individual
				eo.actionDoubleClick(driver, "xpath", "individualRadioBtn", CURR_APP);
				addComment("User selected type as individual");
				// add code for name and last name

				fname = fname + randNum;
				lname = lname + randNum;
				eo.enterText(driver, "xpath", "firstNameTextBox", fname, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("firstNameTextBox", CURR_APP))).sendKeys(Keys.TAB);
				addComment("Added first name");
				eo.enterText(driver, "xpath", "lastNameTextBox", lname, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("lastNameTextBox", CURR_APP))).sendKeys(Keys.TAB);
				addComment("added last name");
			}

			// fill all fields
			if (companyNameFlag.equalsIgnoreCase("Duplicate")) {
				eo.enterText(driver, "xpath", "CompanyNameTextBox", companyName, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("CompanyNameTextBox", CURR_APP))).sendKeys(Keys.TAB);
				addComment("added company name");
			} else {
				companyName = companyName + randNum;
				eo.enterText(driver, "xpath", "CompanyNameTextBox", companyName, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("CompanyNameTextBox", CURR_APP))).sendKeys(Keys.TAB);
				addComment("added company name");
			}
			eo.enterText(driver, "xpath", "EmailTextBox", email, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("EmailTextBox", CURR_APP))).sendKeys(Keys.TAB);
			addComment("added Email");
			eo.enterText(driver, "xpath", "PhoneTextBox", phoneNum, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("PhoneTextBox", CURR_APP))).sendKeys(Keys.TAB);
			addComment("added Phone number");
			eo.clickElement(driver, "xpath", "click_Dropdown", CURR_APP);
			eo.wait(3);
			List<WebElement> options = eo.getListOfWebElements(driver, "xpath", "list_webelements", CURR_APP);
			for (WebElement option : options) {
				if (option.getText().trim().equals(primarySubsidiary)) {
					option.click();
					addComment("Added primary subsidiary");
					break;
				}
			}

			///////////////////////////////////////////////////////////
			// content group
			switch (primarySubsidiary) {
			case "Rubrik, Inc.":
				eo.enterText(driver, "xpath", "coupaContentGroup", "USA", CURR_APP);
				break;
			case "Rubrik International, Inc":
				eo.enterText(driver, "xpath", "coupaContentGroup", "International", CURR_APP);
				break;
			case "Rubrik Australia (AUD)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "Australia", CURR_APP);
				break;
			case "Rubrik Italy S.r.l. (EUR)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "International", CURR_APP);
				break;
			case "Rubrik Switzerland (CHF)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "International", CURR_APP);
				break;
			case "Rubrik India Private Limited (INR)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "India", CURR_APP);
				// driver.findElement(By.xpath(gei.getProperty("coupaContentGroup",
				// CURR_APP))).sendKeys(Keys.TAB);
				break;
			case "Rubrik Japan KK (JPY)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "Japan", CURR_APP);
				break;
			case "Rubrik Netherlands BV (EUR)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "Netherlands", CURR_APP);
				break;
			case "Rubrik Singapore Pte Limited (SGD)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "Singapore", CURR_APP);
				break;
			case "Rubrik UK Limited (GBP)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "International", CURR_APP);
				break;
			case "Rubrik France SAS (EUR)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "International", CURR_APP);
				break;
			case "Rubrik Ireland (EUR)":
				eo.enterText(driver, "xpath", "coupaContentGroup", "International", CURR_APP);
				break;
			default:
				System.out.println("No match found");
			}

			//////////////////////////////////////
			if (includeInCoupaFlag.equalsIgnoreCase("true")) {
				// Check the Include in coupa checkbox
				eo.actionClick(driver, "xpath", "IncludeInCoupaCheckBox", CURR_APP);
				addComment("Checked IncludeInCoupaCheckBox");
			}
			// Select coup po method
			eo.enterText(driver, "xpath", "CoupaPOMethod", coupaPOMethod, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("CoupaPOMethod", CURR_APP))).sendKeys(Keys.TAB);
			addComment("added coupa po method");
			// if coupa po method is email

			if (coupaPOMethod.equalsIgnoreCase("email")) {
				if (emailFlag.equalsIgnoreCase("True")) {
					eo.enterText(driver, "xpath", "coupaPOEmailTextBox", email, CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("coupaPOEmailTextBox", CURR_APP))).sendKeys(Keys.TAB);
					addComment("added coupa po email");
				} else {
					addComment("Empty email field");
				}
			}

			// Click on the save button
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("saveButton_so", CURR_APP))));
			eo.clickElement(driver, "xpath", "saveButton_so", CURR_APP);

			try {
				WebDriverWait wait = new WebDriverWait(driver, 10);
				if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
					System.out.println("alert was not present");
					addComment("No alert present");
				} else {
					Alert alert = driver.switchTo().alert();
					String alertText = alert.getText().trim();
					if (alertText.contains("This record may have duplicates")) {
						addComment(alertText + " is displayed");
						alert.accept();
						addComment("alert done");
						// Select coup po method as prompt or cxml
						eo.waitForPageload(driver);
						eo.clickElement(driver, "xpath", "gobackLink", CURR_APP);
						addComment("Clicked go back button");
						eo.clickElement(driver, "xpath", "secondaryCancelBtn", CURR_APP);
						addComment("Clicked cancel button");
					}
				}
			} catch (Exception e) {
				addComment("Alert is not present");
			}

		}

	}

	////////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VendorRecordPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to navigate to VendorPage And
	 * Verify Name,Subsidiary, Address, Category </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>OpenVendorRecordId</li>
	 * <li>IntegrationForExtensityToNetSuite</li>
	 * <li>VendorRecordPage</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */

	public static class VendorRecordPage extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			// TODO Auto-generated method stub
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.clickElement(driver, "xpath", "VendorMailId", CURR_APP);
			this.addComment("Sucessfully Clicked on Vendor Email Link");
			eo.verifyExactScreenText(driver, "xpath", "vendorPage", "Vendor", CURR_APP);
			this.addComment("Sucessfully verified Vendor Page");
			String nameText = eo.getText(driver, "xpath", "name_VendorPage", CURR_APP);
			String nameOfEmployee = nameText.replaceAll("\\s+", "");
			if (nameOfEmployee.equals(fullName)) {
				this.addComment("sucessfully verified Name As per Employee Records");
			} else {
				this.addComment("Name field Missmatch As per Employee Records");
			}
			String companyName = eo.getText(driver, "xpath", "CompanyName_vendorPage", CURR_APP);

			if (companyName.endsWith("rubrik.com")) {
				String category = eo.getText(driver, "xpath", "Category_VendorPage", CURR_APP);
				this.addComment("verify Category When company name ends with 'rubrik.com'");
			} else {
				this.addComment("Company Name is not Ends with 'rubrik.com'");
			}

			String address = eo.getText(driver, "xpath", "Address_vendorPage", CURR_APP);

			if (address.contains(countryName)) {
				this.addComment("Sucessfully verified Countryname As per Employee Records");
			} else {
				this.addComment("Missmatch Country Name As per Employee Records");
			}
			eo.isDisplayed(driver, "xpath", "primarySubsidiary_vendorPage", CURR_APP);
			this.addComment("Sucessfully Verified Subsidiary");

		}
	}

	////////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpenVendorRecordIdInNS</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Open Vendor Record Id In
	 * NetSuite </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>OpenVendorRecordId</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */

	public static class OpenVendorRecordId extends Keyword {

		private String vendorRecord;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				vendorRecord = args.get("VendorRecord");

			} catch (Exception e) {
				this.addComment("Error while initializing");
				throw new KDTKeywordInitException("Unable to initialize", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				eo.enterText(driver, "xpath", "globalSerachNetsuite", vendorRecord, CURR_APP);
				this.addComment("Serached for the order number " + vendorRecord);

				eo.waitForWebElementVisible(driver, "XPATH", "actualVendorId", waiTime, CURR_APP);
				String actualvendorIdDisplayed = eo.getText(driver, "xpath", "actualVendorId", CURR_APP);

				if (actualvendorIdDisplayed.contains(vendorRecord)) {
					eo.clickElement(driver, "xpath", "actualVendorId", CURR_APP);
					this.addComment("Clicked on the order " + vendorRecord);
					eo.waitForPageload(driver);
				} else {
					this.addComment("Unable to click on the order " + vendorRecord);
					throw new KDTKeywordExecException("Unable to click on the order");
				}
			} catch (Exception e) {
				this.addComment("User is Unable to Open Order NetSuite");
				throw new KDTKeywordExecException("User is Unable to Open Order NetSuite", e);
			}
		}
	}

	//////////////////////////////////////////
	public static class IntegrationForExtensityToNetSuite extends Keyword {
		private boolean text;
		private String vendorRecordId;
		public String email;
		public static String firstNameText;
		public static String lastNameText;
		public static String middleNameText;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				vendorRecordId = args.get("VendorRecordId");
			} catch (Exception e) {
				this.addComment("Error while initializing");
				throw new KDTKeywordInitException("Unable to initialize", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.verifyExactScreenText(driver, "xpath", "vendorRecordType", "Bill", CURR_APP);
			this.addComment("Sucessfully Verified Record Type");
			eo.verifyExactScreenText(driver, "xpath", "vendorRecordId", "VBR45095501", CURR_APP);
			this.addComment("Sucessfully Verified Record Id");
			String createdfrom_txt = eo.getText(driver, "xpath", "createdFrom", CURR_APP);
			if (createdfrom_txt.contains("https://www.expensify.com")) {
				this.addComment("Sucessfully Verified Created From" + createdfrom_txt);
			}
			text = eo.isDisplayed(driver, "xpath", "createdFrom", CURR_APP);
			this.addComment("Sucessfully Verified  Vendor bill Created From Expensify" + createdfrom_txt);
			if (text = true) {
				email = eo.getText(driver, "xpath", "VendorMailId", CURR_APP);
			} else {
				this.addComment("Created From filed data is not present");
			}
			eo.enterText(driver, "xpath", "globalSerachNetsuite", email, CURR_APP);
			this.addComment("Serached for the Employee records " + email);
			eo.waitForWebElementVisible(driver, "XPATH", "actualVendorId", waiTime, CURR_APP);
			eo.clickElement(driver, "xpath", "actualVendorId", CURR_APP);
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "EmployeePage", "Employee", CURR_APP);
			this.addComment("Sucessfully Verified Employee Page");
			List<WebElement> name = driver.findElements(By.xpath(gei.getProperty("nameField_employee", CURR_APP)));
			for (int i = 0; i <= name.size(); i++) {
				firstNameText = name.get(0).getText().trim();
				middleNameText = name.get(1).getText().trim();
				lastNameText = name.get(2).getText().trim();
				break;
			}
			if (middleNameText.trim().length() == 0) {
				fullName = firstNameText + lastNameText;
			} else {
				fullName = firstNameText + middleNameText + lastNameText;
			}
			this.addComment("Employee Fullname is:" + fullName);
			countryName = eo.getText(driver, "xpath", "employeeCountry", CURR_APP);
			eo.verifyExactScreenText(driver, "xpath", "vendorRecordType", "Bill", CURR_APP);
			this.addComment("Sucessfully Verified Record Type");
		}
	}

	////////////////////////////////////
	public static class SearchVerifyEmailIsOfEmployeeRecord extends Keyword {
		private String email;
		private String flag = "false";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			email = args.get("Email");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", email, CURR_APP);
			this.addComment("Searched for the email " + email);
			eo.waitForWebElementVisible(driver, "XPATH", "displayedEmployeeRecord", waiTime, CURR_APP);

			List<WebElement> recordList = driver
					.findElements(By.xpath(gei.getProperty("displayedEmployeeRecord", CURR_APP)));
			for (WebElement record : recordList) {
				if (record.getText().contains("Employee")) {
					System.out.println(record.getText());
					flag = "true";
					break;
				} else if (record.getText().contains("No results found")) {
					flag = "false";
				}
			}
			if (flag.equals("true")) {
				this.addComment("The employee record present for the email:" + email);
			} else {
				throw new KDTKeywordExecException("No employee record present for the email: " + email);
			}

		}
	}

	///////////////////////////////////////
	// public static class VendorAddressCreation extends Keyword {
	//
	//
	//
	// private String addressOne;
	// private String addressTwo;
	// private String cityName;
	// private String zipCode;
	//
	//
	//
	// @Override
	// public void init() throws KDTKeywordInitException {
	// super.init();
	// addressOne = args.get("AddressLine1");
	// addressTwo = args.get("AddressLine2");
	// cityName = args.get("CityName");
	// zipCode= args.get("ZipCode");
	// }
	// @Override
	// public void exec() throws KDTKeywordExecException {
	// // TODO Auto-generated method stub
	// //addressCountry = args.get("AddressCountry");
	// //System.out.println(addressCountry);
	// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
	// WebDriver driver = context.getWebDriver();
	// eo.clickElement(driver, "xpath", "getAddressText", CURR_APP);
	// addComment("Clicked on Address Tab");
	//
	// eo.waitForPageload(driver);
	//
	//
	//
	// ElementOperation.isElementPresnt_click(driver,
	// "//div[@class='listinlinefocusedrowcell']/span/span/span/span/a", 10);
	// addComment("Clicked on Edit");
	// //eo.clickElement(driver, "xpath", "getAddressEditButton", CURR_APP);
	// eo.switchToFrameByIdentifier(driver, "name", "getAddressChildFrameName", 5,
	// CURR_APP);
	// // eo.enterText(driver, "xpath", "getAddressCountry", addressCountry,
	// CURR_APP);
	// eo.enterText(driver, "xpath", "firstaddress",addressOne, CURR_APP);
	// addComment("Entered address line1");
	// eo.enterText(driver, "xpath", "secondaddress",addressTwo, CURR_APP);
	// addComment("Entered address line2");
	// eo.enterText(driver, "xpath", "City",cityName,CURR_APP);
	// addComment("Entered city name");
	// eo.enterText(driver, "xpath", "zipcode",zipCode,CURR_APP);
	// addComment("Entered zipcode");
	//
	// try {
	// WebElement element = driver.findElement(By.name("ok"));
	// JavascriptExecutor executor = (JavascriptExecutor) driver;
	// executor.executeScript("arguments[0].click();", element);
	// addComment("Clicked on OK Button");
	// } catch (Exception e) {
	// System.out.println(e);
	// }
	// eo.clickElement(driver, "id", "getAddressAddButton", CURR_APP);
	//
	//
	//
	// }
	// }

	public static class addContact extends Keyword {
		private String contactText;
		private String title;
		private String fname;
		private String lname;
		private String roleText;
		private String contactflag;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			contactText = args.get("Contact");
			title = args.get("Title");
			fname = args.get("Fname");
			lname = args.get("Lname");
			roleText = args.get("RoleText");
			contactflag = args.get("Contactflag");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			if (contactflag.equalsIgnoreCase("false")) {
				contactText = contactText + randNum;
				fname = fname + randNum;
				lname = lname + randNum;
			}

			String parentWindow = driver.getWindowHandle();
			System.out.println(parentWindow);
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("getContactNewButton", CURR_APP))));
			eo.clickElement(driver, "xpath", "getContactNewButton", CURR_APP);
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "contactHeaderText", "Contact", CURR_APP);
			addComment("Contact window displayed");
			/////////////

			Set<String> allWindows = driver.getWindowHandles();
			Iterator<String> I1 = allWindows.iterator();
			while (I1.hasNext()) {
				String child_window = I1.next();
				if (!parentWindow.equals(child_window)) {
					driver.switchTo().window(child_window);
					////////////
					eo.enterText(driver, "xpath", "contactTextBox", contactText, CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("contactTextBox", CURR_APP))).sendKeys(Keys.TAB);
					addComment("Added contact text");

					eo.enterText(driver, "xpath", "titleTextBox", title, CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("titleTextBox", CURR_APP))).sendKeys(Keys.TAB);
					addComment("Added title text");
					// Role

					eo.clickElement(driver, "xpath", "roleDropDownIcon", CURR_APP);
					eo.wait(3);
					List<WebElement> options = eo.getListOfWebElements(driver, "xpath", "roleOptionsList", CURR_APP);
					for (WebElement option : options) {
						if (option.getText().trim().equals(roleText)) {
							option.click();
							addComment("Added role text");
							break;
						}
					}
					/////////////////////
					eo.enterText(driver, "xpath", "FirstNameTextBox", fname, CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("FirstNameTextBox", CURR_APP))).sendKeys(Keys.TAB);
					addComment("Added First name text");

					eo.enterText(driver, "xpath", "LastNameTextBox", lname, CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("LastNameTextBox", CURR_APP))).sendKeys(Keys.TAB);
					addComment("Added Last name text");

					eo.clickElement(driver, "xpath", "ContactSaveButton", CURR_APP);

					//////////////////////////////////////////////////////////

					try {
						WebDriverWait wait = new WebDriverWait(driver, 10);
						if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
							System.out.println("alert was not present");
							addComment("No alert present");
						} else {
							Alert alert = driver.switchTo().alert();
							String alertText = alert.getText().trim();
							if (alertText.contains("This record may have duplicates")) {
								addComment(alertText + " is displayed");
								alert.accept();
								addComment("alert accepted");
								// Select coup po method as prompt or cxml
								eo.waitForPageload(driver);
							}
						}
					} catch (Exception e) {
						addComment("Alert is not present");
					}

				} ////////////
			}
			driver.switchTo().window(parentWindow);
		}
	}

	/////////////////////////////
	public static class removeContact extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			List<WebElement> elementList = driver.findElements(By.xpath(gei.getProperty("removeLinkList", CURR_APP)));
			for (WebElement ele : elementList) {
				ele.click();
				addComment("removed contact");
			}

		}

	}

	/////////////////////////////////////////
	public static class verifyVendorIsAutoChangedToIndividualType extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
		}

		@Override
		public void exec() throws KDTKeywordExecException {

		}

	}

	////////////////////////////////////////
	public static class VendorTest extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);
			// Navigate to scripts page
			eo.hoverMouseUsingJavascript(driver, "xpath", "customizationTab", CURR_APP);
			addComment("Mouse hover to customization Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "scriptingSubTab", CURR_APP);
			addComment("MovetoElement to scriptingSubTab Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "scriptSubTab", CURR_APP);
			eo.wait(3);
			eo.clickElement(driver, "xpath", "scriptSubTab", CURR_APP);
			addComment("Click on element scripts");
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "scriptsHeaderText", "Scripts ", CURR_APP);
			eo.clickElement(driver, "xpath", "coupaVendorIntegrationView", CURR_APP);
			this.addComment("Sucessfully Clicked on view button");
			eo.verifyExactScreenText(driver, "xpath", "scriptPage", "Script", CURR_APP);
			this.addComment("Sucessfully Verified Script page");
			eo.clickElement(driver, "xpath", "executionLogs", CURR_APP);
			List<WebElement> typeMassages = eo.getListOfWebElements(driver, "xpath", "typeMassages", CURR_APP);
			List<WebElement> details = eo.getListOfWebElements(driver, "xpath", "detailMassges", CURR_APP);
			String vname = "";
			for (int i = 0; i <= typeMassages.size() - 1; i++) {
				String massage = typeMassages.get(i).getText().trim();
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + massage);
				String message2 = details.get(i).getText().trim();

				String[] vendorname = message2.split("Id= ,");
				for (int j = 0; j < vendorname.length; j++) {
					vname = vendorname[j];
					if (vname.contains(companyName)) {
						System.out.println("#######################" + vname);
						break;
					}
				}
				System.out.println("Log messages :" + massage + "message 2:" + message2);
				System.out.println("*************" + companyName);
				if (companyName.contains(vname)) {
					this.addComment("Sucessfully Verified :" + companyName + vname);
					break;

				} else {
					this.addComment("Both Are different");
				}
			}
		}
	}

	////////////////////////////////////////////////////
	public static class changeToSB1 extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			// Mousehover on user icon
			eo.actionMoveToElement(driver, "xpath", "userrole", CURR_APP);
			eo.clickElement(driver, "xpath", "sb1Option", CURR_APP);
			// eo.hoverMouseUsingJavascript(driver, "xpath", "userIcon", CURR_APP);
			// select SB1

		}

	}
	////////////////////////////////////////////////////
	public static class changeToSB2 extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			// Mousehover on user icon
			eo.actionMoveToElement(driver, "xpath", "userrole", CURR_APP);
			eo.clickElement(driver, "xpath", "sb2Option", CURR_APP);
			eo.wait(6);
			// eo.hoverMouseUsingJavascript(driver, "xpath", "userIcon", CURR_APP);
			// select SB1

		}
	}

	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	public static class navigateToCreatePaymentBatchPage extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// mousehover on transaction option
			eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);
			this.addComment("Mouse hover to Transactions Tab");
			// mouse hover on payables
			eo.hoverMouseUsingJavascript(driver, "xpath", "payablesTab", CURR_APP);
			this.addComment("Mouse hover to Sales Subtab");
			// Click create payment batch option
			eo.clickElement(driver, "xpath", "createPaymentBatchTab", CURR_APP);

		}

	}

	////////////////////////////////////
	public static class enterBankAccNumSelVendorBillSubmit extends Keyword {
		private String bankAccNum;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			bankAccNum = args.get("BankAccNum");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Enter bank acc Number
			eo.enterText(driver, "xpath", "bankAccNumberField", bankAccNum, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("bankAccNumberField", CURR_APP))).sendKeys(Keys.TAB);
			this.addComment("Entered bank acc name::" + bankAccNum);
			// scroll down
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("custpage_sub_detailsmarkall", CURR_APP))));
			// Select a vendor bill
			eo.clickElement(driver, "xpath", "VendorBillCheckBox1", CURR_APP);
			this.addComment("Clicked vendor bill");
			eo.javaScriptScrollToViewElement(driver, "xpath", "submitter", CURR_APP);
			eo.waitForWebElementVisible(driver, "xpath", "submitter", 5, CURR_APP);
			eo.clickElement(driver, "xpath", "submitter", CURR_APP);
			addComment("Clicked submitter");
			// wait for page load
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "verifySuccessMessage",
					"A payment batch will be created. You will get an email notification on completion.", CURR_APP);
			addComment("Verified success message");
		}
	}
	/////////////////////////

	////////////////////////////////////////
	public static class SearchForVendor extends Keyword {
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", companyName, CURR_APP);
			this.addComment("Searched for the companyName " + companyName);
			eo.waitForWebElementVisible(driver, "XPATH", "searchVendor", waiTime, CURR_APP);
			List<WebElement> recordList = driver.findElements(By.xpath(gei.getProperty("searchVendor", CURR_APP)));
			for (WebElement record : recordList) {
				if (record.getText().contains(companyName)) {
					record.click();
					addComment("Clicked on vendor record");
					break;
				} else {
					throw new KDTKeywordExecException("No employee record present for the companyName: " + companyName);
				}
			}
		}

	}

	/////////////////////////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>NavigateToSandBox1</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Navigate to sand box 1</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox1</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */

	public static class NavigateToSandBox1 extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.actionMoveToElement(driver, "xpath", "userrole", CURR_APP);
			eo.clickElement(driver, "xpath", "sb1Option", CURR_APP);

		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>NavigateToCreatePaymentBatch</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to navigate to create payment
	 * batch page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox1</li>
	 * <li>NavigateToCreatePaymentBatch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */

	public static class NavigateToCreatePaymentBatch extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// mousehover on transaction option
			eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);
			this.addComment("Mouse hover to Transactions Tab");
			// mouse hover on payables
			eo.hoverMouseUsingJavascript(driver, "xpath", "payablesTab", CURR_APP);
			this.addComment("Mouse hover to Sales Subtab");
			// Click create payment batch option
			eo.clickElement(driver, "xpath", "createPaymentBatchTab", CURR_APP);

		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreatePaymentBatch</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to create payment batch in
	 * NS</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox1</li>
	 * <li>NavigateToCreatePaymentBatch</li>
	 * <li>CreatePaymentBatch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */
	///////////////////////////////////////////////////////////////
	/*public static class DeleteVendorBills extends Keyword {

		private String[] paymentMode;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			try {

				paymentMode = args.get("PaymentMode").split(";");
			} catch (Exception e) {
				this.addComment("Error while initializing LoginNetSuite");
				throw new KDTKeywordInitException("Error while initializing LoginNetSuite", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			int j = 0;
			int counter = 0;
			for (int k = 0; k < paymentMode.length; k++) {
				// mousehover on transaction option
				eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);

				this.addComment("Mouse hover to Transactions Tab");
				// mouse hover on payables
				eo.hoverMouseUsingJavascript(driver, "xpath", "payablesTab", CURR_APP);

				// Click create payment batch option
				eo.clickElement(driver, "xpath", "Payment_Batch_List", CURR_APP);
				this.addComment("Successfully clicked on Vendor Payment search Batch");
				eo.waitForPageload(driver);
				eo.wait(10);
				if (j == 0) {
					eo.clickElement(driver, "xpath", "open_filter", CURR_APP);
					j = j + 1;
					this.addComment("Successfully clicked on Filter");
				}
				eo.clickElement(driver, "xpath", "Searchid", CURR_APP);
				eo.clickElement(driver, "xpath", "Dropdown", CURR_APP);
				eo.waitForPageload(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "Dropdown_value", "{text}", paymentMode[k],
						CURR_APP);
				this.addComment("Successfully clicked Payment mode" + paymentMode[k]);
				eo.waitForPageload(driver);
				List<WebElement> EditLink_list = driver
						.findElements(By.xpath(gei.getProperty("EditLinkList", CURR_APP)));
				if (EditLink_list.size() == 0) {
					this.addComment("No Edit links");
				} else {
					for (int i = 0; i < EditLink_list.size(); i++) {
						// EditLink_list1 = driver.findElements(By.xpath(gei.getProperty("EditLink",
						// CURR_APP)));
						counter = i + 1;
						this.addComment(
								counter + " number of links to be clicked for Payment mode : " + paymentMode[k]);

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "EditLink", "{iteration}",
								Integer.toString(i + 1), CURR_APP);
						eo.waitForPageload(driver);
						if (eo.isSelected(driver, "xpath", "void", CURR_APP)) {
							eo.clickElement(driver, "xpath", "void_Click", CURR_APP);
							// eo.wait(10);
							eo.clickElement(driver, "xpath", "void_Click", CURR_APP);
							// eo.wait(10);
							eo.clickElement(driver, "xpath", "saveVendorSearch", CURR_APP);
							eo.wait(10);
						} else {
							eo.clickElement(driver, "xpath", "void_Click", CURR_APP);
							eo.clickElement(driver, "xpath", "saveVendorSearch", CURR_APP);
							eo.wait(10);
						}

					}
				}
			}
		}
	}// last line
*/
	////////////////////////////////////////////////////
	public static class CreatePaymentBatch extends Keyword {
		private String bankAccNum;
		private String[] vendorBillCheckBoxNum;
		private int indexValue = 0;
		private int count = 1;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			bankAccNum = args.get("BankAccNum");
			vendorBillCheckBoxNum = args.get("VendorBillCheckBoxNum").split(",");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			arl.clear();
			// Enter bank acc Number
			eo.clickElement(driver, "xpath", "bankAccNumberField", CURR_APP);
			List<WebElement> bnkAccNameList = driver
					.findElements(By.xpath(gei.getProperty("BnkAccNameList", CURR_APP)));
			for (WebElement ele : bnkAccNameList) {
				if (ele.getText().trim().equals(bankAccNum)) {
					ele.click();
					break;
				}
			}
			eo.waitForPageload(driver);
			// scroll down
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("custpage_sub_detailsmarkall", CURR_APP))));
			// Select a vendor bill
			List<WebElement> checkBoxList = eo.getListOfWebElements(driver, "xpath", "vendorBillCheckBoxList",
					CURR_APP);
			indexValue = checkBoxList.toArray().length;
			System.out.println(indexValue);

			for (int k = 0; k < vendorBillCheckBoxNum.length; k++) {
				if (Integer.parseInt(vendorBillCheckBoxNum[k]) <= indexValue) {
					System.out.println(vendorBillCheckBoxNum[k]);
					eo.actionClickAfterReplacingKeyValue(driver, "xpath", "vendorBillCheckBoxList1", "{icount}",
							vendorBillCheckBoxNum[k], CURR_APP);
					arl.add(Double.parseDouble(eo.getTextAfterReplacingKeyValue(driver, "xpath", "amountList",
							"{iteration}", vendorBillCheckBoxNum[k], CURR_APP).replaceAll(",", "")));
					System.out.println(arl);
					addComment("Vendor bill amount" + arl);
				} else {
					addComment("Checkbox list is:" + indexValue);
				}
			}
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("submitter", CURR_APP))));
			eo.clickElement(driver, "xpath", "submitter", CURR_APP);
			addComment("Clicked submitter");
			// wait for page load
			eo.waitForPageload(driver);
			eo.verifyExactScreenText(driver, "xpath", "verifySuccessMessage",
					"A payment batch will be created. You will get an email notification on completion.", CURR_APP);
			addComment("Verified success message");
		}
	}

	public static class VerifyTotalAndLargestAmt extends Keyword {
		private double sum = 0;
		private double returnLargestAmt = 0;
		private DecimalFormat df = new DecimalFormat("0.00");
		private String amounts = "";
		static List<Double> arlist = new ArrayList<>();

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			for (Double i : arl) {
				sum += i;
			}
			sum = Double.parseDouble(df.format(sum));
			System.out.println("Sum:" + sum);
			addComment("Sum:" + sum);
			driver.navigate().refresh();
			eo.wait(3);
			eo.javaScriptScrollToViewElement(driver, "xpath", "dueDateFrom_Scrolling", CURR_APP);
			eo.clickElement(driver, "ID", "vendorBil_Payment", CURR_APP);
			eo.wait(3);
			List<WebElement> amountList = eo.getListOfWebElements(driver, "xpath", "Amount_VendorBill_Payment",
					CURR_APP);
			for (WebElement ele : amountList) {
				arlist.add(Double.parseDouble(ele.getText().trim()));
			}
			eo.wait(2);
			returnLargestAmt = Double.parseDouble(df.format(Collections.max(arlist)));
			eo.wait(2);
			System.out.println("returnLargestAmount:" + returnLargestAmt);
			Double totalAmt = Double.parseDouble(df.format(Double.parseDouble(
					eo.getText(driver, "xpath", "totalAmountField", CURR_APP).replaceAll(",", "").trim())));
			addComment("total amount:" + totalAmt);
			System.out.println("total amount:" + totalAmt);

			Double largestAmtValue = Double.parseDouble(df.format(Double
					.parseDouble(eo.getText(driver, "xpath", "largestAmt", CURR_APP).replaceAll(",", "").trim())));
			addComment("Largest amount:" + largestAmtValue);
			System.out.println("Largest amount:" + largestAmtValue);

			if (totalAmt == sum) {
				addComment("Verified total amount");
			} else {
				addComment("Failed to verify Largest amount");
				throw new KDTKeywordExecException("Total amount verification unsuccessful");
			}
			if (largestAmtValue == returnLargestAmt) {
				addComment("Verified Largest amount");
			} else {
				addComment("Failed to verify Largest amount");
				throw new KDTKeywordExecException("Largest amount verification unsuccessful");
			}
		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>NavigateToPaymentBatchListPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Navigate Payment batch list
	 * page after creation of payment batch</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox1</li>
	 * <li>NavigateToCreatePaymentBatch</li>
	 * <li>CreatePaymentBatch</li>
	 * <li>NavigateToPaymentBatchListPage</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */

	public static class NavigateToPaymentBatchListPage extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// mousehover on transaction option
			eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);
			this.addComment("Mouse hover to Transactions Tab");
			// mouse hover on payables
			eo.hoverMouseUsingJavascript(driver, "xpath", "payablesTab", CURR_APP);
			this.addComment("Mouse hover to Sales Subtab");
			// Click create payment batch option
			eo.clickElement(driver, "xpath", "Payment_Batch_List", CURR_APP);
			eo.wait(2);
			eo.clickElement(driver, "xpath", "Quick_Sort", CURR_APP);
			this.addComment("Successfully Selected Quick Sort Option");
			eo.wait(5);
			eo.clickElement(driver, "xpath", "view_payment_batch", CURR_APP);
			this.addComment("Successfully Clicked On View Payment Batch");
			driver.navigate().refresh();
			eo.wait(5);
			driver.navigate().refresh();
			eo.wait(5);
			driver.navigate().refresh();
			eo.clickElement(driver, "xpath", "vendor_payment_batch_edit", CURR_APP);
			this.addComment("Successfully clicked on Vendor Payment Batch Edit Button");
			eo.wait(3);
			eo.clickElement(driver, "xpath", "approve_To_issue_payment_checkbox", CURR_APP);
			eo.waitForPageload(driver);
			eo.clickElement(driver, "xpath", "approve_To_issue_paymentSubmit", CURR_APP);
			eo.waitForPageload(driver);
			try {
				driver.switchTo().alert();
				return;
			} catch (NoAlertPresentException Ex) {
				addComment("No alert present");
			}
			driver.navigate().refresh();
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>verifyJiraUpdates</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Verify JIRA Id, JiRA Status
	 * List, JIRA Url, JIRA Status Massage</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox1</li>
	 * <li>NavigateToCreatePaymentBatch</li>
	 * <li>CreatePaymentBatch</li>
	 * <li>NavigateToPaymentBatchListPage</li>
	 * <li>VerifyJiraUpdates</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * 
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */
	public static class VerifyJiraUpdates extends Keyword {
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			String text = eo.getText(driver, "xpath", "jiraID", CURR_APP);
			this.addComment("JIRA ID :" + text);
			eo.verifyPartialScreenText(driver, "xpath", "jiraID", "PAY-", CURR_APP);
			this.addComment("Sucesfully Verified JIRA ID");
			eo.verifyExactScreenText(driver, "xpath", "jiraStatusList", "Processed", CURR_APP);
			this.addComment("Sucessfully Verified JIRA Status List");
			eo.verifyExactScreenText(driver, "xpath", "jiraStatusMsg", "Success", CURR_APP);
			this.addComment("Sucessfully Verified JIRA Status Message");
			String JiraUrl = eo.getText(driver, "xpath", "jiraURL", CURR_APP);
			if (JiraUrl.contains(text)) {
				this.addComment("Url is Based on JIra Id :" + JiraUrl + text);
			} else {
				this.addComment("Jira Url Is not based on Jira Id :" + JiraUrl + text);
			}
			eo.verifyPartialScreenText(driver, "xpath", "jiraURL", text, CURR_APP);
			addComment("Sucessfully Verified JIRA URL");
		}
	}

}// Last Line
