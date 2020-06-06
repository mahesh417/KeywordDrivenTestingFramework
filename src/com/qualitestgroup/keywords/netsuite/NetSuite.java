package com.qualitestgroup.keywords.netsuite;


import com.qualitestgroup.keywords.gtc.*;
import static io.restassured.RestAssured.given;



import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
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
import com.qualitestgroup.keywords.common.Common;
import com.qualitestgroup.keywords.common.ElementOperation;

import com.qualitestgroup.util.seleniumtools.SeleniumTools;
import com.testautomationguru.utility.PDFUtil;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;



public class NetSuite extends KeywordGroup {
	
	
	static int waittime = 10;
	private static final String CURR_APP = "NetSuite";
	public static GetElementIdentifier gei = new GetElementIdentifier();
	static GetProperty getProps = new GetProperty();
	public static ElementOperation eo = new ElementOperation();
	public static String fullName;
	public static String countryName;
	public static String randNum = eo.gnerateRandomNo(5);
	public static Integer termmax_value;
	public static String companyName;
	public static String term;
	public static String empName1;
	public static String empName2;
	public static String jeValue;
	public static String jeAmount;
	static List<Double> arl = new ArrayList<>();
	public static  String Orderamt;


	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpenOrderNetsuite</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to open the order from
	 * NetSuite global search.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>CreateAccount</li>
	 * <li>CreateContact</li>
	 * <li>CreateOpportunity</li>
	 * <li>CreateQuote</li>
	 * <li>OpportunityStageChange</li>
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

	public static class SetTheCreditLimit extends Keyword {
	
		private String lessThanOrderAmount = "";
		private String greaterThanAmount= "";
		private String ZeroCreiditLimitAmount= "";
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			

		}


		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			
		
			try {
				
				
				Orderamt = context.getData().get("$orderAmount");
				String OrderAmount=Orderamt.replace("$", "");
				System.out.println("orderamount"+ OrderAmount);
				
				SeleniumTools.waitForWebElementVisible(By.xpath(gei.getProperty("editButton", CURR_APP)), waiTime);
				eo.clickElement(driver, "xpath", "editButton", CURR_APP);
				this.addComment("Successfully clicked the Edit button");
				SeleniumTools.waitForWebElementVisible(By.xpath(gei.getProperty("financialButton", CURR_APP)), waiTime);
				eo.clickElement(driver, "xpath", "financialButton", CURR_APP);
				this.addComment("Successfully clicked the financial button");

				SeleniumTools.waitForWebElementVisible(By.xpath(gei.getProperty("creditLimitField", CURR_APP)),
						waiTime);
				
				
				eo.javaScriptScrollToViewElement(driver, "xpath", "creditLimitField", CURR_APP);
				
				if (hasArgs("greaterThanAmount")) {
					
					greaterThanAmount = args.get("GreaterThanOrderAmount");

					int Amount1 = Integer.parseInt(OrderAmount);
					int Amount2 = Integer.parseInt(greaterThanAmount);
					int AdditionAmount = Amount1 + Amount2;
					String FinalAmount = String.valueOf(AdditionAmount);

					eo.enterText(driver, "xpath", "creditLimitInput", FinalAmount, CURR_APP);

				}
				if (hasArgs("lessThanOrderAmount")) {

					lessThanOrderAmount = args.get("LessThanOrderAmount");

					int Amount1 = Integer.parseInt(OrderAmount);
					int Amount2 = Integer.parseInt(lessThanOrderAmount);
					int subtarctionAmount = Amount1 - Amount2;
					String FinalAmount = String.valueOf(subtarctionAmount);

					eo.enterText(driver, "xpath", "creditLimitInput", FinalAmount, CURR_APP);

				}
				if (hasArgs("ZeroCreditLimit")) {
					ZeroCreiditLimitAmount = args.get("ZeroCreditLimit");

					
					eo.enterText(driver, "xpath", "creditLimitInput", ZeroCreiditLimitAmount, CURR_APP);
					
				}
				
				if (!hasArgs("greaterThanAmount")&& !hasArgs("lessThanOrderAmount")&& !hasArgs("ZeroCreditLimit")) {
					
				eo.enterText(driver, "xpath", "creditLimitInput", OrderAmount, CURR_APP);
				
				this.addComment("Successfully entered the credit limit as:"+"<b>"+OrderAmount+"</b>");
				
				}

				eo.javaScriptScrollToViewElement(driver, "xpath", "saveButton", CURR_APP);
				eo.actionClick(driver, "xpath", "saveButton", CURR_APP);
				this.addComment("successsfully clicked the save button");

			} catch (Exception e) {

			}
		}

	}

	public static class OpenAccountNetsuite extends Keyword {

		private String accountName;

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {
				
				
				accountName=context.getData().get("$expectedAccountName");
				

				eo.enterText(driver, "xpath", "globalSerachNetsuite", accountName, CURR_APP);
				this.addComment("Serached for the order number " + accountName);

				eo.waitForWebElementVisible(driver, "XPATH", "actualDisplayedAccount", waiTime, CURR_APP);
				String actualAccountDisplayed = eo.getText(driver, "xpath", "actualDisplayedAccount", CURR_APP);

				if (actualAccountDisplayed.contains(accountName)) {
					eo.clickElement(driver, "xpath", "actualDisplayedAccount", CURR_APP);
					this.addComment("Clicked on the account name " + accountName);
					eo.waitForPageload(driver);
				}

				else {
					this.addComment("Unable to click on the order " + accountName);
					throw new KDTKeywordExecException("Unable to click on the order");
				}
			} catch (Exception e) {
				this.addComment("User is Unable to Open the Account NetSuite");
				throw new KDTKeywordExecException("User is Unable to Open the Account NetSuite", e);
			}
		}
	}

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

	/////////////////////////////////////////// FulFillOrderNetSuite/////////////////////////////////
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
				eo.waitForWebElementVisible(driver, "XPATH", "serialNumberField", waiTime, CURR_APP);

				Actions action = new Actions(driver);
				action.sendKeys(Keys.PAGE_DOWN).build().perform();

				eo.enterText(driver, "xpath", "serialNumberField", serialNumber1[i], CURR_APP);
				this.addComment("Added the serial number");

				for (int j = 0; j < 4; j++) {
					try {
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();
						driver.findElement(By.xpath(gei.getProperty("okButtonSerialNumber", CURR_APP))).click();
						/*
						 * eo.clickElement(driver, "xpath", "okButtonSerialNumber", CURR_APP);
						 * eo.actionDoubleClick(driver, "xpath", "okButtonSerialNumber", CURR_APP);
						 * eo.waitForWebElementVisible(driver, "XPATH", "okButtonAfterSerialAdd", 10,
						 * CURR_APP); break;
						 */
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
						/*
						 * eo.clickElement(driver, "xpath", "okButtonSerialNumber", CURR_APP);
						 * eo.actionDoubleClick(driver, "xpath", "okButtonSerialNumber", CURR_APP);
						 * eo.waitForWebElementVisible(driver, "XPATH", "okButtonAfterSerialAdd", 10,
						 * CURR_APP); break;
						 */
					} catch (Exception e) {

					}
				}

				this.addComment("Clicked on OK button after enter the serial number");

				/*
				 * eo.actionDoubleClick(driver, "xpath", "okButtonAfterSerialAdd", CURR_APP);
				 * this. addComment("Clicked on OK button after add serial number"); eo.wait(1);
				 */

			}
			eo.waitForWebElementVisible(driver, "XPATH", "saveButtonPurchaseOrder", waiTime, CURR_APP);
			eo.clickElement(driver, "xpath", "saveButtonPurchaseOrder", CURR_APP);
			this.addComment("Saved the order after fulfillment");

			// eo.waitForWebElementVisible(driver, "xpath", keyValue, 10,
			// CURR_APP);

		}
	}

	///////////////////////////////// LoginNetSuite/////////////////////////////////

	public static class LoginNetSuite extends Keyword {

		private String url;

		private String username;

		private String password;

		private String saveto;

		private String browser = "";

		private String expectedfirstapprover;

		private String expectedsecondapprover;

		BufferedReader reader, readerr, readerr1;

		File fi;

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

				if (hasArgs("Expectedfirstapprover")) {

					expectedfirstapprover = args.get("Expectedfirstapprover");

				}

				if (hasArgs("Expectedsecondapprover")) {

					expectedsecondapprover = args.get("Expectedsecondapprover");

				}

			} catch (Exception e) {

				this.addComment("Error while initializing LoginNetSuite");

				throw new KDTKeywordInitException("Error while initializing LoginNetSuite", e);

			}

		}

		@Override

		public void exec() throws KDTKeywordExecException {

			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			File f;

			if (!(browser.isEmpty())) {

				try {

					Keyword.run("Browser", "Launch", "Browser", browser);

				} catch (KDTException e) {

					throw new KDTKeywordExecException("Unable to launch browser" + e.getMessage());

				}

			}

			/*
			 * 
			 * try { Keyword.run("Browser", "Launch", "Browser", browser); } catch
			 * 
			 * (KDTException e) { e.printStackTrace(); }
			 * 
			 */

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

				if (username.equalsIgnoreCase(expectedfirstapprover))

				{

					f = new File(

							System.getProperty("user.dir")
									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_firstapprover.txt");

				}

				else if (username.equalsIgnoreCase(expectedsecondapprover))

				{

					f = new File(

							System.getProperty("user.dir")
									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_secondapprover.txt");

				}

				else

				{

					f = new File(

							System.getProperty("user.dir")
									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt");

				}

				if (f.exists()) {

					if (username.equalsIgnoreCase(expectedfirstapprover))

					{

						reader = new BufferedReader(new FileReader((System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_firstapprover.txt")));

					}

					else if (username.equalsIgnoreCase(expectedsecondapprover))

					{

						reader = new BufferedReader(new FileReader((System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_secondapprover.txt")));

					}

					else {

						reader = new BufferedReader(new FileReader((System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt")));

					}

					int lines = 0;

					while (reader.readLine() != null) {

						lines++;

					}

					reader.close();

					if (lines > 1)

					{

						File tempFile = new File(System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/myTempFile.txt");

						tempFile.createNewFile();

						if (username.equalsIgnoreCase(expectedfirstapprover))

						{

							readerr = new BufferedReader(new FileReader((System.getProperty("user.dir")

									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_firstapprover.txt")));

						}

						else if (username.equalsIgnoreCase(expectedsecondapprover))

						{

							readerr = new BufferedReader(new FileReader((System.getProperty("user.dir")

									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_secondapprover.txt")));

						}

						else {

							readerr = new BufferedReader(new FileReader((System.getProperty("user.dir")

									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt")));

						}

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
						///////////////////////////////////////////////////////
						//////////////////////////////////////////////////////
//						boolean check=eo.isSelected(driver, "id", "checkBoxForNSRefresh", CURR_APP);
//						if (check==false){
//						eo.clickElement(driver, "id", "checkBoxForNSRefresh", CURR_APP);
//						}else{
//							throw new KDTKeywordExecException("Unable to Logout Netsuite application");
//						}
//						eo.clickElement(driver, "xpath", "continueForNSRefresh", CURR_APP);
//					
//						
						int waitTime2 = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
						SeleniumTools.waitForWebElementVisible(driver,
								By.xpath(gei.getProperty("home_btn", CURR_APP)), waitTime2);
						
						
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

						BufferedReader readertemp = new BufferedReader(new FileReader(System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/myTempFile.txt"));

						BufferedWriter writerback = new BufferedWriter(new FileWriter(f));

						while ((currentLine = readertemp.readLine()) != null) {

							writerback.write(currentLine + System.getProperty("line.separator"));

						}

						readertemp.close();

						writerback.close();

						tempFile.delete();

					} else {

						if (username.equalsIgnoreCase(expectedfirstapprover))

						{

							readerr1 = new BufferedReader(new FileReader((System.getProperty("user.dir")

									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_firstapprover.txt")));

						}

						else if (username.equalsIgnoreCase(expectedsecondapprover))

						{

							readerr1 = new BufferedReader(new FileReader((System.getProperty("user.dir")

									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_secondapprover.txt")));

						}

						else

						{

							readerr1 = new BufferedReader(new FileReader((System.getProperty("user.dir")

									+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt")));

						}

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

						if (eo.isDisplayed(driver, "xpath", "Securitysetup", CURR_APP))

						{
							eo.wait(2);

							eo.clickElement(driver, "xpath", "skiptoBackup", CURR_APP);

							eo.wait(2);

							eo.clickElement(driver, "xpath", "clickNext", CURR_APP);

							eo.wait(2);

						}

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

					if (username.equalsIgnoreCase(expectedfirstapprover))

					{

						fi = new File((System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_firstapprover.txt"));

					}

					else if (username.equalsIgnoreCase(expectedsecondapprover))

					{

						fi = new File((System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode_secondapprover.txt"));

					}

					else

					{

						fi = new File((System.getProperty("user.dir")

								+ "/src/com/qualitestgroup/keywords/netsuite/BackUpCode.txt"));

					}

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

				this.addComment("User is Unable to Login NetSuite");

				throw new KDTKeywordExecException("User is Unable to Login NetSuite", e);

			}

		}

	}

	/////////////////////////////////////////////////////////////////////
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
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {

				WebDriver driver = context.getWebDriver();
				eo.actionMoveToElement(driver, "xpath", "userrole", CURR_APP);
				SeleniumTools.waitForWebElementVisible(driver, By.xpath(gei.getProperty("logout_button", CURR_APP)), waiTime);
				eo.clickElement(driver, "xpath", "logout_button", CURR_APP);
				eo.waitForPageload(driver);

				eo.waitForWebElementVisible(driver, "xpath", "login_button", waiTime, CURR_APP);
				this.addComment("Successfully logout from Netsuite");
			}

			catch (Exception e) {
				this.addComment("Unable to Logout Netsuite application");
				throw new KDTKeywordExecException("Unable to Logout Netsuite application", e);
			}

		}

	}

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
			// eo.javaScriptScrollToViewElement(driver, "xpath", "items",
			// CURR_APP);
			List<WebElement> poVendor = eo.getListOfWebElements(driver, "xpath", "poVendor", CURR_APP);
			String poVendorNameNS = null;

			for (int i = 0; i < poVendor.size(); i++) {
				System.out.println(poVendor.get(i).getText());
				String POVendorname = poVendor.get(i).getText();
				if (POVendorname.contains(poVendorName)) {
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

			if (poVendorName.equals(poVendorNameNS) && startdate.contains(" ")) {
				this.addComment("Both Conditions Are Satishfied");

				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("rateLink", CURR_APP))));
				List<WebElement> dropShipLink = eo.getListOfWebElements(driver, "xpath", "dropShip", CURR_APP);
				for (int i = 0; i < 1; i++) {
					eo.wait(5);
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
		// private String ponumber;
		private String vendorname;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			serialNumber1 = args.get("SerialNumber1").split(";");
			// ponumber = args.get("PONumber");
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
							eo.clickElement(driver, "XPATH", "add_button", CURR_APP);
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

			if (hasArgs("SerialNumber1")) {
				serialNumber1 = args.get("SerialNumber1").split(";");

				List<WebElement> inventoryNumberList = eo.getListOfWebElements(driver, "xpath", "inventoryList",
						CURR_APP);
				int size1 = inventoryNumberList.size();
				System.out.println("number of inventory icon" + size1);
				String Inventorylistxpath = gei.getProperty("inventoryList", CURR_APP);
				List<WebElement> qtyrowelmnt_list = eo.getListOfWebElements(driver, "xpath", "qty_txt", CURR_APP);
				System.out.println("qty row count:" + qtyrowelmnt_list.size());
				String qtyrowelmntxpath = gei.getProperty("qty_txt", CURR_APP);
				System.out.println(qtyrowelmntxpath);
				////////////////////////////////
				((JavascriptExecutor) driver).executeScript("scroll(1350,0)");
				List<WebElement> s_list = eo.getListOfWebElements(driver, "xpath", "serialnofield_itemfullfill",
						CURR_APP);
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
			// String save_txt = eo.getText(driver, "XPATH", "saveButton",
			// CURR_APP);
			String save_txt = eo.getAttribute(driver, "xpath", "saveButton", "value", CURR_APP);
			System.out.println(save_txt);
			this.addComment("Invoice page is displayed");
			eo.clickElement(driver, "XPATH", "saveButton", CURR_APP);
			this.addComment("Clicked on Save Button");
//			try {
//				// Wait 10 seconds till alert is present
//				WebDriverWait wait = new WebDriverWait(driver, 10);
//				Alert alert = wait.until(ExpectedConditions.alertIsPresent());
//				alert.accept();
//			} catch (Exception e) {
//				addComment("Alert is not present");
//			}
			try {
				driver.switchTo().alert();
				flag = true;
				WebDriverWait wait = new WebDriverWait(driver, 10);
				Alert alert = wait.until(ExpectedConditions.alertIsPresent());
				alert.accept();
				this.addComment("Clicked on email alert");
			} catch (NoAlertPresentException Ex) {
				flag = false;
				addComment("No alert present");
			}

			if (flag == false) {
				eo.clickElement(driver, "xpath", "invoiceEditBtn", CURR_APP);
				eo.waitForPageload(driver);
			} else {

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
				eo.clickElement(driver, "XPATH", "saveButton", CURR_APP);
			}

			// eo.javaScriptScrollToViewElement(driver,
			// driver.findElement(By.xpath(gei.getProperty("saveButton",
			// CURR_APP))));
			// ((JavascriptExecutor) driver).executeScript("scroll(0,-50)");
			// if (save_txt.equalsIgnoreCase("Save")) {
			// eo.clickElement(driver, "XPATH", "saveButton", CURR_APP);
			// }
			//
			// else {
			//
			// eo.actionClick(driver, "XPATH", "save_dropdown", CURR_APP);
			// List<WebElement> savelist_items = eo.getListOfWebElements(driver,
			// "XPATH", "savemenu_list", CURR_APP);
			// for (int i = 0; i < savelist_items.size(); i++) {
			// String saveTxt = savelist_items.get(i).getText();
			// if (saveTxt.equals("Save")) {
			// eo.javaScriptClick(driver, "XPATH", "save_Menu", CURR_APP);
			// break;
			// }
			// }
			//
			// }
			// this.addComment("Clicked on Save button");
			// eo.waitForPageload(driver);
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
			eo.waitForWebElementVisible(driver, "XPATH", "billedOptionVerify", 10, CURR_APP);
			System.out.println("Sucessfully verified Status of order in Sales order Page");
			// eo.verifyExactScreenText(driver, "XPATH", "salesOrderPageVerify",
			// "Sales
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
				this.addComment("Mouse hover to EnterSalesOrde");
				this.addComment("Sales order list page is displayed");

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

		public static String distributorname;
		public static String distributorponumber;
		public static String enduser;
		public static String subscriptionitem;
		// public static String term;
		public static String amount;
		private String saveto;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("DistributorName", "EndUser");
				distributorname = args.get("DistributorName");
				distributorponumber = args.get("DistributorPO");
				enduser = args.get("EndUser");
				subscriptionitem = args.get("SubscriptionItem");
				term = args.get("Term");
				amount = args.get("Amount");
				saveto = args.get("SaveTo");
			} catch (Exception e) {
				this.addComment("Error while initializing");
				throw new KDTKeywordInitException("Unable to initialize", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			Actions action = new Actions(driver);

			String salesordernum = eo.getAttribute(driver, "xpath", "salesorder_numberfield", "value", CURR_APP);
			this.addComment("SalesOrder number is: " + salesordernum);
			saveValue(salesordernum);
			String salesorder_number = context.getData().get("$salesorder");

			String salesorderdate = eo.getAttribute(driver, "xpath", "sodate", "value", CURR_APP);
			this.addComment("SalesOrder date is: " + salesorderdate);

			eo.enterText(driver, "xpath", "distributor_txtfield", distributorname, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("distributor_txtfield", CURR_APP))).sendKeys(Keys.TAB);
			this.addComment("Entered Distributor name is:" + distributorname);
			eo.wait(3);
			// eo.waitForPageload(driver);
			// eo.waitForPageload(driver);

			eo.enterText(driver, "xpath", "distributorpo_txtfield", eo.gnerateRandomNo(5), CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("distributorpo_txtfield", CURR_APP))).sendKeys(Keys.TAB);
			this.addComment("Entered DistributorPO number is: " + distributorponumber);

			eo.enterText(driver, "xpath", "enduser_txtfield", enduser, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("distributor_txtfield", CURR_APP))).sendKeys(Keys.TAB);
			this.addComment("Entered Enduser name is: " + enduser);
			/*
			 * eo.enterText(driver, "xpath", "distributorpo_txtfield",
			 * eo.gnerateRandomNo(5), CURR_APP);
			 * addComment("Entered DistributorPO number is:" + distributorponumber);
			 * 
			 * eo.enterText(driver, "xpath", "enduser_txtfield", enduser, CURR_APP);
			 * this.addComment("Entered Enduser name is:" + enduser);
			 */

			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));
			this.addComment("Scrolled to items tab");

			eo.clickElement(driver, "xpath", "subscriptionitem_tab", CURR_APP);
			eo.actionDoubleClick(driver, "xpath", "clickItem", CURR_APP);

			eo.enterText(driver, "ID", "subscription_item", subscriptionitem, CURR_APP);
			// driver.findElement(By.xpath(gei.getProperty("subscription_item",CURR_APP))).sendKeys(Keys.TAB);
			this.addComment("Entered subscription item :" + subscriptionitem);
			eo.waitForPageload(driver);
			// eo.enterText(driver, "xpath", "subscriptionitem_tab", data,
			// application);

			// driver.switchTo().alert().accept();

			List<WebElement> subscriptionitem_list = eo.getListOfWebElements(driver, "xpath", "subscriptionline_items",
					CURR_APP);

			for (int i = 0; i < subscriptionitem_list.size(); i++) {
				String subscriptionitem_txt = subscriptionitem_list.get(i).getText();
				if (subscriptionitem_txt.equalsIgnoreCase(subscriptionitem)) {
					subscriptionitem_list.get(i).click();
					// eo.waitForPopUp(driver);
					break;
				}
			}
			try {
				Robot robot = new Robot();

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			} catch (Exception e) {

				addComment("popup is not present");

			}
			eo.waitForPageload(driver);

			/*
			 * eo.javaScriptScrollToViewElement(driver,
			 * driver.findElement(By.xpath(gei.getProperty("termcell", CURR_APP))));
			 * this.addComment("Scrolled to terms tab");
			 */
			// eo.hoverMouseUsingJavascript(driver, "xpath", "termcell",
			// CURR_APP);

			eo.clickElement(driver, "xpath", "termcell", CURR_APP);
			// eo.actionDoubleClick(driver, "xpath", "termcell", CURR_APP);
			this.addComment("Clicked on Term");
			eo.wait(1);
			eo.enterText(driver, "ID", "term_txtfield", term, CURR_APP);
			eo.wait(1);
			this.addComment("Entered Term value: " + term);

			// try
			// {
			// driver.switchTo().alert();
			// return;
			// }
			// catch (NoAlertPresentException Ex)
			// {
			// addComment("No alert present");
			// }
			eo.clickElement(driver, "xpath", "amount_cell", CURR_APP);
			eo.wait(1);
			this.addComment("Clicked on Amount");
			eo.enterText(driver, "xpath", "amount_txtfield", amount, CURR_APP);
			eo.wait(1);
			this.addComment("Entered Amount as:" + amount);
			eo.clickElement(driver, "xpath", "saveButton_so", CURR_APP);
			this.addComment("Clicked on Save");
			eo.waitForPageload(driver);
			this.addComment("Successfully saved SalesOrder");
			String annualbillschedule = eo.getText(driver, "xpath", "billscehedule_column", CURR_APP);

			int terminyear = Integer.parseInt(term) / 12;
			int termremainder = Integer.parseInt(term) % 12;
			System.out.println("Terms in year is:" + terminyear);
			System.out.println("Terms remainder is:" + termremainder);

			if (annualbillschedule.contains("Custom Term" + term + "months")) {
				this.addComment("Successfully verified the custom schedule content under billing schedule column: "
						+ annualbillschedule);
			}

			if (annualbillschedule.contains("Annual (" + terminyear + " Yr cont)")) {
				this.addComment("Successfully verified the annual schedule content under billing schedule column: "
						+ annualbillschedule);

			}

			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));
			this.addComment("Scrolled to items tab");
			eo.waitForPopUp(driver);

			eo.clickElement(driver, "ID", "billing_tab", CURR_APP);
			// eo.clickElement(driver, "ID", "billing_tab", CURR_APP);
			this.addComment("Clicked on Billing Tab");

			eo.waitForPopUp(driver);

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

			// eo.javaScriptScrollToViewElement(driver,
			// driver.findElement(By.xpath(gei.getProperty("secFullFil",
			// CURR_APP))));
			// eo.clickElement(driver, "xpath", "secFullFil", CURR_APP);
			// eo.waitForPageload(driver);

			eo.clickElement(driver, "xpath", "bottomapprove_btn", CURR_APP);
			this.addComment("Clicked on Approve button");
			eo.waitForPageload(driver);

			try {

			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new KDTKeywordExecException("Unable to Create  ");
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

	public static class addItemsAndSN extends Keyword { /// need to finish
		private String[] getNameOfItems;
		private String[] getQtyForEachItem;
		private int i = 0;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
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
			eo.wait(2);
			for (i = 0; i < getNameOfItems.length; i++) {
				// Click on itemdropdown icon
				eo.enterText(driver, "ID", "itemField", getNameOfItems[i].trim(), CURR_APP);
				eo.waitForPageload(driver);
				driver.findElement(By.xpath(gei.getProperty("itemField", CURR_APP))).sendKeys(Keys.ENTER);
				driver.findElement(By.xpath(gei.getProperty("itemField", CURR_APP))).sendKeys(Keys.ENTER);
				addComment("Entered Item: " + getNameOfItems[i]);
				eo.wait(3);
			}
			eo.actionClick(driver, "xpath", "serialNumIcon", CURR_APP);
			eo.actionClick(driver, "xpath", "serialNumIcon1", CURR_APP);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(driver.findElement(By.xpath(gei.getProperty("childFrame", CURR_APP))));
			eo.waitForWebElementVisible(driver, "XPATH", "serialNumberField", waiTime, CURR_APP);
			String randonString = eo.gnerateRandomNo(6);
			Actions action1 = new Actions(driver);
			action1.sendKeys(Keys.PAGE_DOWN).build().perform();
			eo.enterText(driver, "xpath", "serialnumber_add", randonString, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("serialnumber_add", CURR_APP))).sendKeys(Keys.ENTER);
			this.addComment("Added the serial number");
			eo.wait(1);
			eo.waitForWebElementVisible(driver, "XPATH", "secondaryokSNpopup", waiTime, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("secondaryokSNpopup", CURR_APP))).click();
			eo.wait(1);
			// driver.findElement(By.xpath(gei.getProperty("secondaryokSNpopup",
			// CURR_APP))).click();
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
			// Verify the transaction successful message is displayed and
			// approval status is
			// changed to 'Approved'
			eo.verifyExactScreenText(driver, "xpath", "transactionSavedMsg", transactionSucMsgTxt, CURR_APP);
			addComment("Transaction Successfull Message displayed successfully");
			// Verify the PO status is changed to APPROVED BY SUPERVISOR/PENDING
			// RECEIPT
			eo.verifyExactScreenText(driver, "xpath", "approvalStatusChangedTxt", approvalStatusPendingRecieveTxt,
					CURR_APP);
			addComment("Approval Status Changed to 'Pending recieve' successfully");
			// Click on Recieve button on Po Page
			eo.clickElement(driver, "xpath", "receiveBtn", CURR_APP);
			addComment("Clicked recieve button");
			// wait until the page loads and Verify the Item reciept page is
			// displayed
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
			// Click on Purchase Order link under 'Create from' section to goto
			// Purchase
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
					// After refreshing the page verify the status is chnged to
					// fully billed
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
				eo.enterText(driver, "xpath", "locationFieldSaleOrderPage", newLocationName, CURR_APP);// Entered
																										// new
																										// location
																										// name
				driver.findElement(By.xpath(gei.getProperty("locationFieldSaleOrderPage", CURR_APP)))
						.sendKeys(Keys.ENTER);
				eo.clickElement(driver, "xpath", "saveButton", CURR_APP); // delete
																			// if
																			// not
																			// required
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
		// public static String term;
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
			// eo.enterText(driver, "xpath", "salesDate", "7/19/2019",
			// CURR_APP);
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
		// public static String term;
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
				String supportEndDate = getEndDate(cal);
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

	public static String getEndDate(Calendar cal) {
		return "" + (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.DATE) - 1) + "/" + cal.get(Calendar.YEAR);
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
			eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("items", CURR_APP))));

			List<WebElement> totalTerms = eo.getListOfWebElements(driver, "xpath", "termList", CURR_APP);
			int noOfTerms = totalTerms.size();
			System.out.println(noOfTerms);
			int[] myArray = new int[30];
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < noOfTerms; i++) {
				String termvalue = totalTerms.get(i).getText();
				System.out.println(termvalue);
				if (termvalue.trim().length() == 0) {

				} else {
					list.add(Integer.parseInt(termvalue));
				}

			}

			termmax_value = Collections.max(list);
			System.out.println(termmax_value);
			this.addComment("Term Max Value: " + termmax_value);

			eo.clickElement(driver, "xpath", "bottomapprove_btn", CURR_APP);

			eo.waitForPageload(driver);
			this.addComment("Approved the order");

			// eo.verifyExactScreenText(driver, "xpath", "transactionsaved_msg",
			// "Sales Order successfully Approved",
			// CURR_APP);
			// this.addComment("verified Sales Order successfully Approved
			// meassage");

		}

	}

	/*
	 * public static class ClickNextBillandGenerateInvoice extends Keyword { public
	 * static String sdate; public static String eDate; public static String rate;
	 * public static String quantity; public static String terms;
	 * 
	 * @Override public void init() throws KDTKeywordInitException { super.init();
	 * 
	 * try { quantity = args.get("ProductQuantity"); rate = args.get("ProductRate");
	 * if (hasArgs("Term")) { term = args.get("Term"); } else { term = ""; }
	 * 
	 * } catch (Exception e) { this.addComment("Error while initializing"); throw
	 * new KDTKeywordInitException("Unable to initialize", e); } }
	 * 
	 * @Override public void exec() throws KDTKeywordExecException {
	 * 
	 * WebDriver driver = context.getWebDriver(); eo.waitForPageload(driver); int
	 * term_iterator = termmax_value / 12; Calendar cale = Calendar.getInstance();
	 * String supportStartDatee = getDate(cale); String supportEndDatee =
	 * getDate(cale);
	 * 
	 * if (termmax_value % 12 != 0) { term_iterator++; } try { sdate =
	 * eo.startDateGenerationbasedonTerm(
	 * "RBK-GO-FNDN-R6408-PA,RBK-10TB-ADDON-HDD-01,RBK-POL-RADAR-PA",
	 * supportStartDatee, termmax_value); eDate = eo.endDateGenerationbasedonTerm(
	 * "RBK-GO-FNDN-R6408-PA,RBK-10TB-ADDON-HDD-01,RBK-POL-RADAR-PA",
	 * supportEndDatee, termmax_value); this.addComment("Support Start Date: " +
	 * sdate); this.addComment("Support End Date: " + eDate);
	 * 
	 * } catch (Exception e) { System.out.println(e.getStackTrace());
	 * this.addComment("Start Date And End Date Are not Fetching"); }
	 * 
	 * boolean nextbill = eo.isDisplayed(driver, "xpath", "nextBillBtn", CURR_APP);
	 * for (int i = 0; i < term_iterator; i++) { if (nextbill == true) {
	 * eo.clickElement(driver, "xpath", "nextBillBtn", CURR_APP);
	 * this.addComment("Clicked on the Nextbill button");
	 * eo.waitForPageload(driver); eo.verifyExactScreenText(driver, "xpath",
	 * "invoicePageHeader", "Invoice", CURR_APP); eo.verifyExactScreenText(driver,
	 * "xpath", "invoiceToBeGenMsg", "To Be Generated", CURR_APP);
	 * this.addComment("Successfully verified Invoice page is displayed");
	 * 
	 * // eo.javaScriptScrollToViewElement(driver, //
	 * driver.findElement(By.xpath(gei.getProperty("classification_header", //
	 * CURR_APP)))); // // eo.actionClick(driver, "XPATH", "communicationTab", //
	 * CURR_APP); // this.addComment("Clicked on communication tab"); //
	 * eo.clickElement(driver, "XPATH", "massageTab", CURR_APP); //
	 * this.addComment("Clicked on message tab"); // eo.clickElement(driver,
	 * "XPATH", "emailIdCheckBox", // CURR_APP); //
	 * this.addComment("Unchecked emailID Checkbox"); eo.clickElement(driver, "id",
	 * "invoice_save", CURR_APP); this.addComment("Clicked on SaveButton");
	 * eo.waitForPageload(driver);
	 * 
	 * List<WebElement> TermofInvoices = eo.getListOfWebElements(driver, "xpath",
	 * "termOfInvoice", CURR_APP); for (int z = 0; z <= TermofInvoices.size(); z++)
	 * { terms = TermofInvoices.get(i).getText().trim();
	 * System.out.println("List of terms present under Invoice page:" + terms); }
	 * 
	 * String filename = System.getProperty("user.dir") +
	 * "\\ApplicationData\\Downloads"; File folder = new File(filename); try {
	 * FileUtils.cleanDirectory(folder); eo.wait(2); } catch (IOException e1) { //
	 * TODO Auto-generated catch block e1.printStackTrace(); } if (i == 0) {
	 * driver.get("chrome://settings/content/pdfDocuments");
	 * eo.waitForPageload(driver); String url = driver.getCurrentUrl(); Actions
	 * action = new Actions(driver); action.sendKeys(Keys.TAB).build().perform();
	 * action.sendKeys(Keys.TAB).build().perform();
	 * action.sendKeys(Keys.ENTER).build().perform();
	 * 
	 * driver.navigate().back(); eo.waitForPageload(driver); }
	 * eo.clickElement(driver, "xpath", "print_icon", CURR_APP); eo.wait(5); String
	 * pdfFolderPath = System.getProperty("user.dir") +
	 * "\\ApplicationData\\Downloads"; File pdffolder = new File(pdfFolderPath);
	 * File[] listOfFiles = pdffolder.listFiles(); String fileName = ""; if
	 * (listOfFiles.length == 1) { fileName = listOfFiles[0].toString(); } else {
	 * throw new
	 * KDTKeywordExecException("More than 1 file is present in download folder"); }
	 * PDFUtil pdfUtil = new PDFUtil(); String fullpdfContent = ""; try {
	 * 
	 * fullpdfContent = pdfUtil.getText(fileName); String lines[] =
	 * fullpdfContent.split("\\r?\\n"); for (String line : lines) { String pdf =
	 * line; this.addComment("PDF Path:" + pdf); } } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } String startdate =
	 * fullpdfContent.substring(fullpdfContent.indexOf("Start Date") + 11,
	 * fullpdfContent.lastIndexOf("End Date")); String[] arr_stdate =
	 * sdate.split(":"); String FstartDate = ""; for (int j = i; j <=
	 * arr_stdate.length - 1; j++) { if (j == i) { FstartDate = arr_stdate[j]; if
	 * (startdate.contains(FstartDate)) { this.addComment("Both Dates Are Same:" +
	 * startdate + FstartDate); } else { this.addComment("Both Dates Are Not Same:"
	 * + startdate + FstartDate); } } }
	 * 
	 * String enddate = fullpdfContent.substring(fullpdfContent.indexOf("End Date")
	 * + 9, fullpdfContent.lastIndexOf("Qty")); String[] arr_Edate =
	 * eDate.split(":"); String FEndDate = "";
	 * 
	 * for (int j = i; j <= arr_Edate.length - 1; j++) { if (j == i) { FEndDate =
	 * arr_Edate[j]; if (enddate.contains(FEndDate)) {
	 * this.addComment("Both Dates Are Same:" + enddate + FEndDate); } else {
	 * this.addComment("Both Dates Are Not Same:" + enddate + FEndDate); } } }
	 * 
	 * } eo.javaScriptScrollToViewElement(driver,
	 * driver.findElement(By.xpath(gei.getProperty("primaryinfo_section",
	 * CURR_APP)))); eo.waitForPageload(driver); eo.clickElement(driver, "xpath",
	 * "createdfrom_so", CURR_APP); eo.waitForPageload(driver); }
	 * 
	 * } }
	 */
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class BrowserTabNavigation extends Keyword {
		public static String tab_tiltle;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				tab_tiltle = args.get("TabTitle");
			} catch (Exception e) {
				this.addComment("Error while initializing");
				throw new KDTKeywordInitException("Unable to initialize", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);

			ArrayList<String> tabs_list = new ArrayList<String>(driver.getWindowHandles());
			System.out.println("No of tabs" + tabs_list.size());
			for (int i = 0; i < tabs_list.size(); i++) {
				String tab_name = tabs_list.get(i);
				System.out.println("Tab name is:" + tab_name);
				String tab_title = driver.getTitle();
				System.out.println(tab_title);
				if (tab_name.contains(tab_tiltle)) {

					driver.close();
					driver.switchTo().window(tabs_list.get(i));
					break;
				}
			}

		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class OpenSalesOrderFromCreatedFromLink extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waitTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.waitForPageload(driver);
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("salesinformation_section", CURR_APP))));
			eo.waitForPageload(driver);
			eo.clickElement(driver, "xpath", "createdfrom_so", CURR_APP);
			eo.waitForPageload(driver);

		}

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
			driver.navigate().refresh();
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

			// Mousehover on user icon
			eo.actionMoveToElement(driver, "xpath", "userrole", CURR_APP);
			eo.clickElement(driver, "xpath", "sb1Option", CURR_APP);
			eo.waitForPageload(driver);

		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>NavigateToSandBox2</b>
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

	public static class NavigateToSandBox2 extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			SeleniumTools.waitForWebElementVisible(driver, By.xpath(gei.getProperty("userrole", CURR_APP)), waiTime);
			// Mousehover on user icon
			eo.actionMoveToElement(driver, "xpath", "userrole", CURR_APP);
			eo.clickElement(driver, "xpath", "sb2Option", CURR_APP);
			eo.waitForPageload(driver);

		}
	}

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

			// Enter bank acc Number
			eo.enterText(driver, "xpath", "bankAccNumberField", bankAccNum, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("bankAccNumberField", CURR_APP))).sendKeys(Keys.TAB);
			// scroll down
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("custpage_sub_detailsmarkall", CURR_APP))));
			// Select a vendor bill
			List<WebElement> checkBoxList = eo.getListOfWebElements(driver, "xpath", "vendorBillCheckBoxList",
					CURR_APP);
			indexValue = checkBoxList.toArray().length;
			System.out.println(indexValue);

			for (int k = 0; k < vendorBillCheckBoxNum.length; k++) {
				System.out.println(vendorBillCheckBoxNum[k]);
				eo.actionClickAfterReplacingKeyValue(driver, "xpath", "vendorBillCheckBoxList1", "{icount}",
						vendorBillCheckBoxNum[k], CURR_APP);
				// String value = eo.getTextAfterReplacingKeyValue(driver,
				// "xpath", "amountList", "{iteration}",
				// vendorBillCheckBoxNum[k], CURR_APP);
				// value= value.replaceAll(",", "");
				// double element = Double.parseDouble(value);
				// arl.add(element);
				arl.add(Double.parseDouble(eo.getTextAfterReplacingKeyValue(driver, "xpath", "amountList",
						"{iteration}", vendorBillCheckBoxNum[k], CURR_APP).replaceAll(",", "")));
				System.out.println(arl);
				this.addComment("Vendor Bills Amount List:" + arl);
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
			eo.clickElement(driver, "xpath", "Quick_Sort", CURR_APP);
			this.addComment("Successfully Selected Quick Sort Option");
			eo.wait(5);
			eo.clickElement(driver, "xpath", "view_payment_batch", CURR_APP);
			this.addComment("Successfully Clicked On View Payment Batch");
			eo.clickElement(driver, "xpath", "vendor_payment_batch_edit", CURR_APP);
			this.addComment("Successfully clicked on Vendor Payment Batch Edit Button");
			eo.wait(2);
			eo.clickElement(driver, "xpath", "approve_To_issue_payment_checkbox", CURR_APP);
			eo.waitForPageload(driver);
			eo.clickElement(driver, "xpath", "approve_To_issue_paymentSubmit", CURR_APP);
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

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>verifyJiraUpdates</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Verify to Total Ammount And
	 * Largest Amount in USD on Vendorpage</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox1</li>
	 * <li>NavigateToCreatePaymentBatch</li>
	 * <li>CreatePaymentBatch</li>
	 * <li>NavigateToPaymentBatchListPage</li>
	 * <li>VerifyTotalAndLargestAmt</li>
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

	public static class VerifyTotalAndLargestAmt extends Keyword {
		private double sum = 0;
		private double returnLargestAmt = 0;
		// private double vendorBillAmount =0;
		private DecimalFormat df = new DecimalFormat("0.00");
		private String amounts = "";

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
			eo.clickElement(driver, "ID", "vendorBil_Payment", CURR_APP);
			List<WebElement> amountList = eo.getListOfWebElements(driver, "xpath", "Amount_VendorBill_Payment",
					CURR_APP);
			for (int i = 0; i <= amountList.size(); i++) {
				amounts = amountList.get(i).getText();
				returnLargestAmt = Double.parseDouble(df.format(Double.parseDouble(amounts)));
				System.out.println("amounts under Billing Tab:" + returnLargestAmt);
				returnLargestAmt = Double.parseDouble(df.format(Collections.max(arl)));
				System.out.println("returnLargestAmount:" + returnLargestAmt);
			}

			Double totalAmt = Double.parseDouble(df.format(Double.parseDouble(
					eo.getText(driver, "xpath", "totalAmountField", CURR_APP).replaceAll(",", "").trim())));
			addComment("total amount:" + totalAmt);
			System.out.println("total amount:" + totalAmt);

			Double largestAmtValue = Double.parseDouble(df.format(Double
					.parseDouble(eo.getText(driver, "xpath", "largestAmt", CURR_APP).replaceAll(",", "").trim())));
			addComment("Largest amount:" + largestAmtValue);
			System.out.println("Largest amount:" + largestAmtValue);
			/*
			 * returnLargestAmt = Double.parseDouble(df.format(Collections.max(arl)));
			 * addComment("returnLargestAmt:" + returnLargestAmt);
			 * System.out.println("returnLargestAmt:" + returnLargestAmt);
			 */

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

	///////////////////// Pepsi SKU Automation//////////////////

	public static class Click_Print extends Keyword {
		WebDriver driver;
		String pdf_path = "C:\\Users\\vidaya.anand\\Downloads";
		String pdf_repfile = pdf_path + "\\" + "CustInvc1884444.pdf";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			driver = context.getWebDriver();
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			eo.waitForPageload(driver);
			eo.hoverMouseUsingJavascript(driver, "xpath", "print_icon", CURR_APP);
			eo.clickElement(driver, "xpath", "print_icon", CURR_APP);
			eo.waitForPageload(driver);
			System.out.println(pdf_repfile);
		}
	}
	///////////////////////////////////////////////////////////////////////////////

	// Method to Compare PDF
	public static class Validate_Invoice_Contents extends Keyword {
		WebDriver driver;
		String pdf_repfile = "C:\\Users\\vidaya.anand\\Downloads\\" + "CustInvc1884444.pdf";
		static String search_key;
		String other_fields;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			driver = context.getWebDriver();

			// Setting field values
			search_key = "RBK-SVC-PREM-R344";
			other_fields = "4/28/2018~4/27/2019~1.0~11,872.00~11,872.00"; // pass
			// search_key = "RBK-CLOUT-10-1YR";
			// other_fields = "4/28/2018~4/27/2019~1.0~11,872.00~11,872.10"; //
			// fail

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			eo.waitForPageload(driver);
			if (Common.Compare_pdf_ByLine(pdf_repfile, search_key, other_fields))
				System.out.println("is Found");
			else
				System.out.println("is not found!");
		}
	}

	public static class ItemFulfillmentPepsiSKU extends Keyword {
		private String productName = "";
		private String term = "";
		private String startDateFetched = "";
		private String actualEndDateFetched = "";
		private String expectedStartDate = "", expectedEndDate = "";

		@Override

		public void init() throws KDTKeywordInitException {
			super.init();
			productName = args.get("ProductName");
			term = args.get("Term");
		}

		@Override

		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.clickElement(driver, "xpath", "fullfilBtn", CURR_APP);
			addComment("Clicked on fullfilment button");
			eo.verifyPartialScreenText(driver, "xpath", "itemFulPageHeaderTxt", "Item Fulfillment", CURR_APP);
			addComment("Verified Item fulfillmentpage is displayed");
			eo.clickElement(driver, "xpath", "markAll", CURR_APP);
			addComment("Clicked on MArk all button");
			eo.clickElement(driver, "xpath", "saveButton_so", CURR_APP);
			addComment("Clicked on save Button button");
			// Verification part
			// Generate end date based on term
			startDateFetched = eo.getText(driver, "xpath", "startDateField", CURR_APP).trim();
			System.out.println("startDateFetched" + startDateFetched);
			actualEndDateFetched = eo.getText(driver, "xpath", "endDateField", CURR_APP).trim();
			System.out.println("actualEndDateFetched" + actualEndDateFetched);
			////////////////////// verify date , sdate and end date
			// expectedStartDate = eo.getUpdatedDate(startDateFetched,
			////////////////////// "MM/dd/yyyy", 0,Integer.parseInt(term) , 0);
			// expectedEndDate =eo.getUpdatedDate(expectedEndDate, "MM/dd/yyyy",
			////////////////////// -1, 0, 0);

			System.out.println("expectedEndDate" + expectedEndDate);
			System.out.println(actualEndDateFetched.equals(expectedEndDate));
			System.out.println(expectedEndDate.equals(actualEndDateFetched));
			if (actualEndDateFetched.equals(expectedEndDate)) {
				addComment("Expected end date:" + expectedEndDate + " Matches actual end date:" + actualEndDateFetched);
			} else {
				addComment("Expected end date:" + expectedEndDate + " doesn't Matche actual end date:"
						+ actualEndDateFetched);
			}
			////////////////////////// verify product quantity, termand
			eo.verifyExactScreenText(driver, "xpath", "productNameField", productName, CURR_APP);
			addComment("Verified product name");
			if (Integer.parseInt(eo.getText(driver, "xpath", "productQtyField", CURR_APP).trim()) == 1) {
				addComment("Quantity verified" + eo.getText(driver, "xpath", "productQtyField", CURR_APP).trim());
			}
			if (Integer.parseInt(eo.getText(driver, "xpath", "termValueField", CURR_APP).trim()) == Integer
					.parseInt(term)) {
				addComment("Quantity verified" + eo.getText(driver, "xpath", "termValueField", CURR_APP).trim());
			}

		}

	}

	/////////////////////// OrderBillingWithIteration ///////////////
	public static class OrderBillingWithIteration extends Keyword {

		private String orderNumber;
		private String ns_sterm, ns_prodname, ns_unitprice, ns_amount, ns_slno;
		private int ns_term;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("ProductName", "Term");

				ns_sterm = args.get("Term");
				ns_term = Integer.parseInt(ns_sterm);
				ns_prodname = args.get("ProductName");
				ns_unitprice = args.get("UnitPrice");
				ns_amount = args.get("Amount");
				ns_slno = args.get("SerialNo");

			} catch (Exception e) {
				this.addComment("Term not specified in test data");
				throw new KDTKeywordInitException("Error while initializing Term", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			orderNumber = context.getData().get("$orderNo");

			eo.enterText(driver, "xpath", "globalSerachNetsuite", "10651", CURR_APP);
			this.addComment("Serached for the order number " + orderNumber);

			eo.waitForWebElementVisible(driver, "XPATH", "actualDisplayedOrder", waiTime, CURR_APP);
			String actualOrderDisplayed = eo.getText(driver, "xpath", "actualDisplayedOrder", CURR_APP);

			if (actualOrderDisplayed.contains("10651")) {
				eo.clickElement(driver, "xpath", "actualDisplayedOrder", CURR_APP);
				this.addComment("Clicked on the order " + orderNumber);
				eo.waitForPageload(driver);
			}

			else {
				this.addComment("Unable to click on the order " + orderNumber);
				throw new KDTKeywordExecException("Unable to click on the order");
			}

			// Get start dates
			String[] term_stDate = Common.get_startDateByTerm("10/24/2019", ns_term).split(":"); // Start
			// passed
			String[] term_endDate = Common.get_endDateByTerm("10/24/2019", ns_term).split(":"); // Start

			// Get End dates

			for (int inv_iter = 1; inv_iter <= ns_term; inv_iter++) {
				// loop here - start - if period is 36 iterate for 36 as it is
				// month wise
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "nextBillBtn", CURR_APP);
				this.addComment("Clicked on the Nextbill button");
				eo.clickElement(driver, "xpath", "communicationTab", CURR_APP);
				this.addComment("Clicked on the Communication button");
				eo.clickElement(driver, "xpath", "massageTab", CURR_APP);
				this.addComment("Clicked on the Messages button");
				eo.clickElement(driver, "xpath", "emailIdCheckBox", CURR_APP);
				this.addComment("Clicked on the toBeEmailed button");
				// eo.javaScriptClick(driver, "InvoiceSave", CURR_APP);
				WebElement invoice = driver.findElement(By.xpath(gei.getProperty("InvoiceSave", CURR_APP)));
				eo.javaScriptClick(driver, invoice, CURR_APP);
				this.addComment("Clicked on the InvoiceSave button");
				eo.clickElement(driver, "xpath", "Print", CURR_APP);
				this.addComment("Clicked on the Print button: " + inv_iter);
				eo.waitForPageload(driver);

				// put download and pdf verification code here
				String pdf_repfile, search_key, other_fields;
				String projdir = "C:\\Users\\vidaya.anand\\Downloads";

				// Get file name
				try {
					pdf_repfile = Common.GetlastModifiedFile(projdir);

					// forming verification values
					search_key = ns_prodname;
					other_fields = ns_sterm + "~" + term_stDate[inv_iter - 1] + "~" + term_endDate[inv_iter - 1];

					if (Common.Compare_pdf_ByLine(pdf_repfile, search_key, other_fields)) {
						System.out.println("PDF verification Passed");
						this.addComment("PDF verification Passed");
					} else {
						System.out.println("PDF verification Failed!");
						this.addComment("PDF verification Failed");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Going back to SO screen
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "ClickSalesOrder", CURR_APP);
				this.addComment("In SalesOrder Screen");

				// loop till here ---
			}

			/*
			 * eo.verifyExactScreenText(driver, "xpath", "invoicePageHeader", "Invoice",
			 * CURR_APP); eo.verifyExactScreenText(driver, "xpath", "invoiceToBeGenMsg",
			 * "To Be Generated", CURR_APP);
			 * 
			 * this.addComment("Successfully verified Invoice page is displayed" );
			 */
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ApproverSetUpInEmployeeRecords</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Check and Un check the
	 * Approver level Check boxes According to business Requirement</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
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

	public static class ApproverSetUpInEmployeeRecords1 extends Keyword {

		private String approvalLevel1;
		private boolean firstApprovalCheckBox;
		private boolean secondApprovalcheckbox;
		private String s1 = "FirstApprover";
		private String s2 = "SecondApprover";
		private String s3 = "BothApprover";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("EmployeeName1");
				empName1 = args.get("EmployeeName1");
				verifyArgs("ApproverLevel1");
				approvalLevel1 = args.get("ApproverLevel1");

			} catch (Exception e) {
				this.addComment("PO Vendor Name Miss Matched");
				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", empName1, CURR_APP);
			this.addComment("Searched for the companyName " + empName1);
			eo.clickElement(driver, "xpath", "empRecord", CURR_APP);
			this.addComment("Successfully clicked on Employee Records");
			String Header = eo.getText(driver, "xpath", "employeePageHeader", CURR_APP);
			if (Header.equals("Employee")) {
				this.addComment("Sucessfully verify HeaderName:" + Header);
			} else {
				this.addComment("Header Name miss matched");
			}
			eo.clickElement(driver, "xpath", "edit_employeeRecord", CURR_APP);
			eo.javaScriptScrollToViewElement(driver, "xpath", "classification_mouseOver", CURR_APP);
			firstApprovalCheckBox = eo.isSelected(driver, "xpath", "firstLevelApprover", CURR_APP);
			secondApprovalcheckbox = eo.isSelected(driver, "xpath", "secondLevelApprover", CURR_APP);

			if (firstApprovalCheckBox && secondApprovalcheckbox) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);
				this.addComment("Sucessfully unchecked the checkboxes");
			} else if (firstApprovalCheckBox) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
				this.addComment("FirstLevel Approval Checkbox unchecked");
			} else if (secondApprovalcheckbox) {
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);

			} else {

				throw new KDTKeywordExecException("Both check boxes are not present");
			}

			if (approvalLevel1.equalsIgnoreCase(s1)) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
			} else if (approvalLevel1.equalsIgnoreCase(s2)) {
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);
			} else if (approvalLevel1.equalsIgnoreCase(s3)) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);
			} else {
				throw new KDTKeywordExecException("Approver level is not present in Sheet");
			}

			eo.clickElement(driver, "xpath", "saveButton_Employee", CURR_APP);
		}

	}

	//////////////////////////////////////////////////
	public static class ApproverSetUpInEmployeeRecords2 extends Keyword {

		private String approvalLevel2;

		private boolean firstApprovalCheckBox;
		private boolean secondApprovalcheckbox;
		private String s1 = "FirstApprover";
		private String s2 = "SecondApprover";
		private String s3 = "BothApprover";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("EmployeeName2");
				empName2 = args.get("EmployeeName2");
				verifyArgs("ApproverLevel2");
				approvalLevel2 = args.get("ApproverLevel2");

			} catch (Exception e) {
				this.addComment("PO Vendor Name Miss Matched");
				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", empName2, CURR_APP);
			this.addComment("Searched for the companyName " + empName2);
			eo.clickElement(driver, "xpath", "empRecord", CURR_APP);
			this.addComment("Successfully clicked on Employee Records");
			String Header = eo.getText(driver, "xpath", "employeePageHeader", CURR_APP);
			if (Header.equals("Employee")) {
				this.addComment("Sucessfully verify HeaderName:" + Header);
			} else {
				this.addComment("Header Name miss matched");
			}
			eo.clickElement(driver, "xpath", "edit_employeeRecord", CURR_APP);
			eo.javaScriptScrollToViewElement(driver, "xpath", "classification_mouseOver", CURR_APP);
			firstApprovalCheckBox = eo.isSelected(driver, "xpath", "firstLevelApprover", CURR_APP);
			secondApprovalcheckbox = eo.isSelected(driver, "xpath", "secondLevelApprover", CURR_APP);

			if (firstApprovalCheckBox && secondApprovalcheckbox) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);
				this.addComment("Sucessfully unchecked the checkboxes");
			} else if (firstApprovalCheckBox) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
				this.addComment("FirstLevel Approval Checkbox unchecked");
			} else if (secondApprovalcheckbox) {
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);

			} else {

				throw new KDTKeywordExecException("Both check boxes are not present");
			}

			if (approvalLevel2.equalsIgnoreCase(s1)) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
			} else if (approvalLevel2.equalsIgnoreCase(s2)) {
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);
			} else if (approvalLevel2.equalsIgnoreCase(s3)) {
				eo.javaScriptClick(driver, "xpath", "firstLevelApprover", CURR_APP);
				eo.javaScriptClick(driver, "xpath", "secondLevelApprover", CURR_APP);
			} else {
				throw new KDTKeywordExecException("Approver level is not present in Sheet");
			}

			eo.clickElement(driver, "xpath", "saveButton_Employee", CURR_APP);
		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreationOfJournalEntry</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Create new journal
	 * Entries</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
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

	public static class CreationOfJournalEntry extends Keyword {

		private String subsidiary;
		private String departmentName;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("Subsidiary");
				subsidiary = args.get("Subsidiary");
				verifyArgs("EmployeeName");
				empName1 = args.get("EmployeeName");
				verifyArgs("EmployeeName");
				empName2 = args.get("EmployeeName");
				verifyArgs("DepartmentName");
				departmentName = args.get("DepartmentName");
				verifyArgs("JEAmount");
				jeAmount = args.get("JEAmount");
			} catch (Exception e) {
				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);
			this.addComment("Mouse hover to Transactions Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "Finacial_SubTab", CURR_APP);
			this.addComment("Mouse hover to Finacial Subtab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "Journal_Entry", CURR_APP);
			this.addComment("Mouse hover to Journal Entry Subtab");
			eo.clickElement(driver, "xpath", "Journal_Entry", CURR_APP);
			String JEPageName = eo.getText(driver, "xpath", "header_JE", CURR_APP);
			if (JEPageName.equals("Journal")) {
				this.addComment("Sucessfully verify Journal Entry Page");
			} else {
				throw new KDTKeywordExecException("Actual And Expected result's are miss matched");
			}
			eo.wait(2);
			eo.clickElement(driver, "xpath", "subsidiary_JEDropDown", CURR_APP);
			List<WebElement> subsidiaryList = eo.getListOfWebElements(driver, "xpath", "subsidiary_JEDropDownList",
					CURR_APP);
			for (int i = 0; i <= subsidiaryList.size() - 1; i++) {
				String subsidiaryBundle = subsidiaryList.get(i).getText().trim();
				System.out.println("List of Subsidiaries:" + subsidiaryBundle);
				if (subsidiaryBundle.contains(subsidiary)) {
					subsidiaryList.get(i).click();
					break;
				}
			}
			eo.wait(3);
			eo.clickElement(driver, "xpath", "firstLevelApprover_JEDropDown", CURR_APP);
			List<WebElement> firstLevelApprover_JEDropDownList = eo.getListOfWebElements(driver, "xpath",
					"subsidiary_JEDropDownList", CURR_APP);
			for (int i = 0; i <= firstLevelApprover_JEDropDownList.size() - 1; i++) {
				String firstLevelApproverBundle = firstLevelApprover_JEDropDownList.get(i).getText().trim();
				System.out.println("List of FirstLevelApprover's Name:" + firstLevelApproverBundle);
				if (firstLevelApproverBundle.contains(empName1)) {
					firstLevelApprover_JEDropDownList.get(i).click();
					break;
				}
			}
			eo.clickElement(driver, "xpath", "secondLevelApprover_JEDropDown", CURR_APP);
			List<WebElement> secondLevelApprover_JEDropDownList = eo.getListOfWebElements(driver, "xpath",
					"subsidiary_JEDropDownList", CURR_APP);
			for (int i = 0; i <= secondLevelApprover_JEDropDownList.size() - 1; i++) {
				String secondLevelApproverBundle = secondLevelApprover_JEDropDownList.get(i).getText().trim();
				System.out.println("SecondLevelApprover's Name:" + secondLevelApproverBundle);
				if (secondLevelApproverBundle.contains(empName2)) {
					secondLevelApprover_JEDropDownList.get(i).click();
					break;
				}
			}
			eo.javaScriptScrollToViewElement(driver, "xpath", "linesTab_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "Acountlist_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "ListSubtab_JE", CURR_APP);
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

			List<WebElement> optionsInnerText = eo.getListOfWebElements(driver, "xpath", "dropDownOf_JE", CURR_APP);
			for (WebElement option : optionsInnerText) {
				if (option.getText().contains("60901 - 72000")) {
					option.click();
					break;
				}
			}

			List<WebElement> search_List = eo.getListOfWebElements(driver, "xpath", "text_JE", CURR_APP);
			for (int i = 0; i < search_List.size(); i++) {
				search_List.get(3).click();
				break;
			}
			eo.enterText(driver, "xpath", "debit_JE", jeAmount, CURR_APP);

			eo.wait(2);
			eo.clickElement(driver, "id", "addButton_JE", CURR_APP);

			eo.javaScriptScrollToViewElement(driver, "xpath", "linesTab_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "Acountlist_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "ListSubtab_JE", CURR_APP);
			JavascriptExecutor jsExecutor2 = (JavascriptExecutor) driver;

			List<WebElement> search_List2 = eo.getListOfWebElements(driver, "xpath", "text_JE2", CURR_APP);
			for (int j = 0; j < search_List2.size(); j++) {
				search_List2.get(5).click();
				break;
			}
			eo.wait(3);
			Actions act = new Actions(driver);
			act.sendKeys(Keys.TAB).perform();
			act.sendKeys(Keys.ENTER).perform();
			eo.clickElement(driver, "id", "saveButton_JE", CURR_APP);
			eo.wait(3);
			String JENumber = eo.getText(driver, "xpath", "JE_Number", CURR_APP);
			jeValue = JENumber.replaceAll("[^0-9]", "");
			System.out.println("Number Of JE:" + jeValue);

			String status = eo.getText(driver, "xpath", "approval_Status_JE", CURR_APP);
			if (status.equalsIgnoreCase("Pending Approval")) {
				this.addComment("Successfully verified Approval Status:" + status);
			} else {
				throw new KDTKeywordExecException("Actual And Expected approval Status are miss matched");
			}
			eo.clickElement(driver, "xpath", "approval_Buttton_JE", CURR_APP);

		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>FirstLevelApprove</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Check SubmitApprove button
	 * and check approval Status</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
	 * <li>Creation of Journal</li>
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

	public static class FirstLevelApprove extends Keyword {

		private String jeSearch;
		private Double usdAmount;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("JESearch");
				jeSearch = args.get("JESearch");

			} catch (Exception e) {
				this.addComment("JE Number is woorng");
				throw new KDTKeywordInitException("Error while initializing JeNumber", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", jeSearch, CURR_APP);
			this.addComment("Searched for the jeSearch " + jeSearch);
			eo.clickElement(driver, "xpath", "empRecord", CURR_APP);
			this.addComment("Successfully clicked on JE Records");

			eo.clickElement(driver, "xpath", "approveButton_JE", CURR_APP);
			this.addComment("Successfully clicked on Approve Button");

			eo.javaScriptScrollToViewElement(driver, "xpath", "journalRejectReason_JE", CURR_APP);
			String amount = eo.getText(driver, "xpath", "ammountUsd_JE", CURR_APP);
			try {
				Number amountInUsd = NumberFormat.getInstance(Locale.US).parse(amount);
				usdAmount = amountInUsd.doubleValue();
			} catch (ParseException e) {

				e.printStackTrace();
			}
			System.out.println("JE Amount in Double:" + usdAmount);
			if (usdAmount > 500000) {
				String approvalQue = eo.getText(driver, "xpath", "approvalQue_JE", CURR_APP);
				approvalQue.equalsIgnoreCase("Approved");
				this.addComment("Succesfully compare Amount And verify");
			} else {
				this.addComment("there is no amount");
				throw new KDTKeywordExecException("Amount is not present in JE");
			}

		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SecondLevelApprove</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Check SubmitApprove button
	 * and check approval Status</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
	 * <li>Creation of Journal</li>
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

	public static class SecondLevelApprove extends Keyword {

		private String jeSearch;
		private Double usdAmount;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("JESearch");
				jeSearch = args.get("JESearch");

			} catch (Exception e) {
				this.addComment("JE Number is woorng");
				throw new KDTKeywordInitException("Error while initializing JeNumber", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", jeSearch, CURR_APP);
			this.addComment("Searched for the jeSearch " + jeSearch);
			eo.clickElement(driver, "xpath", "empRecord", CURR_APP);
			this.addComment("Successfully clicked on JE Records");

			eo.clickElement(driver, "xpath", "approveButton_JE", CURR_APP);
			this.addComment("Successfully clicked on Approve Button");

			eo.javaScriptScrollToViewElement(driver, "xpath", "journalRejectReason_JE", CURR_APP);
			String amount = eo.getText(driver, "xpath", "ammountUsd_JE", CURR_APP);
			try {
				Number amountInUsd = NumberFormat.getInstance(Locale.US).parse(amount);
				usdAmount = amountInUsd.doubleValue();
			} catch (ParseException e) {

				e.printStackTrace();
			}
			System.out.println("JE Amount in Double:" + usdAmount);
			if (usdAmount <= 500000) {
				String approvalQue = eo.getText(driver, "xpath", "approvalQue_JE", CURR_APP);
				approvalQue.equalsIgnoreCase("Approved");
				this.addComment("Succesfully compare Amount And verify");

			} else if (usdAmount > 500000) {
				String approvalQue2 = eo.getText(driver, "xpath", "approvalQue_JE", CURR_APP);
				approvalQue2.equalsIgnoreCase("Pending Second Level Approver");
			} else {
				this.addComment("there is no amount");
				throw new KDTKeywordExecException("Amount is not present in JE");
			}

		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SubsidiarySetUpJE</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Setup Subsidiary in NS</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
	 * <li>SubsidiarySetUpJE</li>
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

	public static class SubsidiarySetUpJE extends Keyword {

		private String subsidiary;
		private String empName;
		private String subsidiariesName;

		@Override

		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("Subsidiary");
				subsidiary = args.get("Subsidiary");

				verifyArgs("EmployeeName");
				empName = args.get("EmployeeName");

				verifyArgs("Subsidiaries");
				subsidiariesName = args.get("Subsidiaries");
			} catch (Exception e) {
				this.addComment("Subsidiary Name Wrong");
				throw new KDTKeywordInitException("Error while initializing JeNumber", e);
			}

		}

		@Override

		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", subsidiariesName, CURR_APP);
			this.addComment("Searched for the jeSearch " + subsidiariesName);

			List<WebElement> subsidiaryPage = eo.getListOfWebElements(driver, "xpath", "subsidiaries_Search", CURR_APP);
			for (int i = 0; i < subsidiaryPage.size(); i++) {
				String subsidiariesOptions = subsidiaryPage.get(i).getText().trim();
				if (subsidiariesOptions.equalsIgnoreCase("Page: Subsidiaries")) {
					subsidiaryPage.get(i).click();
					break;
				}
			}

			this.addComment("Successfully clicked on Subsidiaries");

			eo.waitForPageload(driver);
			String pageHeader = eo.getText(driver, "xpath", "subsidiaries_Text_AdvJE", CURR_APP);
			if (pageHeader.equalsIgnoreCase("Subsidiaries")) {
				this.addComment("Successsfully verified Page Header");
			} else {
				this.addComment("Page header is not displayed");
			}

			addComment("Subsidiary list page is displayed");

			List<WebElement> list_subsidiary = eo.getListOfWebElements(driver, "xpath", "subsidiary_List", CURR_APP);

			List<WebElement> list_edit = eo.getListOfWebElements(driver, "xpath", "Edit_list", CURR_APP);

			for (int i = 0; i < list_subsidiary.size(); i++)

			{

				String actualsubsidiary_name = list_subsidiary.get(i).getText().trim();
				if (actualsubsidiary_name.equalsIgnoreCase(subsidiary)) {
					list_edit.get(i).click();
					addComment("Successfully verified expected and Actual Subsidiary");
					addComment("Successfully verified expected Subsidiary:" + subsidiary);
					break;

				}
			}

			eo.waitForPageload(driver);

			eo.clickElement(driver, "xpath", "advance_JEApproverDropdown", CURR_APP);
			List<WebElement> advance_JEApproveList = eo.getListOfWebElements(driver, "xpath",
					"advance_JEApproverDropdownList", CURR_APP);
			for (int i = 0; i < advance_JEApproveList.size(); i++) {
				String approverName = advance_JEApproveList.get(i).getText().trim();
				if (approverName.equalsIgnoreCase(empName)) {
					advance_JEApproveList.get(i).click();
					break;
				}
			}

			eo.clickElement(driver, "xpath", "subsidiary_save", CURR_APP);

			addComment("Successfully saved Subsidiary");

			try {

				WebDriverWait wait = new WebDriverWait(driver, 10);

				Alert alert = wait.until(ExpectedConditions.alertIsPresent());

				alert.accept();

				addComment("Successfully clicked on OK button");

			} catch (Exception e) {

				addComment("Alert is not present");

			}

			eo.waitForPageload(driver);

		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>FirstEmployeeRecordSetup Entry</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Set up the First employee
	 * Records</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>FirstEmployeeRecordSetup</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Author:Arun Swain</i></b>
	 * </p>
	 * </div>
	 */

	public static class FirstEmployeeRecordSetup extends Keyword {

		private boolean advanceJEApproverCheckbox;
		boolean status = false;
		private String empname;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {
				verifyArgs("EmployeeName");
				empName1 = args.get("EmployeeName");
				verifyArgs("EmployeeName");
				empName2 = args.get("EmployeeName");

			} catch (Exception e) {
				this.addComment("PO Vendor Name Miss Matched");
				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", empName1, CURR_APP);
			this.addComment("Searched for the companyName " + empName1);

			eo.wait(3);
			// WebElement showmoreitem = eo.isElementPresnt(driver, , 2);
			boolean showmoreitem = eo.isDisplayed(driver, "xpath", "showmore", CURR_APP);
			if (showmoreitem == true) {
				eo.clickElement(driver, "xpath", "showmore", CURR_APP);
				eo.waitForPageload(driver);
				List<WebElement> type = eo.getListOfWebElements(driver, "xpath", "globalsearchtype", CURR_APP);
				List<WebElement> name = eo.getListOfWebElements(driver, "xpath", "globalsearchname", CURR_APP);
				List<WebElement> editlink = eo.getListOfWebElements(driver, "xpath", "globalsearchEdit", CURR_APP);
				for (int i = 0; i < type.size(); i++) {

					String typename = type.get(i).getText();
					if (typename.equals("Employee")) {

						empname = name.get(i).getText();
						if (empname.equalsIgnoreCase(empName1)) {

							editlink.get(i).click();
							eo.waitForPageload(driver);
							break;
						}

					}
				}
			} else {
				this.addComment("Searched for the employeeName " + empName1);
				eo.clickElement(driver, "xpath", "empRecord", CURR_APP);
				this.addComment("Successfully clicked on Employee Records");
			}

			String Header = eo.getText(driver, "xpath", "employeePageHeader", CURR_APP);
			if (Header.equals("Employee")) {
				this.addComment("Sucessfully verify HeaderName:" + Header);
			} else {
				this.addComment("Header Name miss matched");
			}

			boolean editbuttoncheck = eo.isDisplayed(driver, "xpath", "edit_employeeRecord", CURR_APP);
			if (editbuttoncheck == true) {
				eo.clickElement(driver, "xpath", "edit_employeeRecord", CURR_APP);
				this.addComment("Successfully clicked on Edit Button");
			} else {
			}

			eo.javaScriptScrollToViewElement(driver, "xpath", "classification_mouseOver", CURR_APP);
			advanceJEApproverCheckbox = eo.isSelected(driver, "xpath", "advanceJE_Approver_Checkbox", CURR_APP);

			if (advanceJEApproverCheckbox == status) {
				eo.javaScriptClick(driver, "xpath", "advanceJE_Approver_Checkbox", CURR_APP);

			}
			eo.clickElement(driver, "xpath", "saveButton_Employee", CURR_APP);
			try {
				WebDriverWait wait = new WebDriverWait(driver, 2);
				wait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				alert.accept();
			} catch (Exception e) {

			}
		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreationOfAdvanceJournalEntry</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Create Advance Journal</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
	 * <li>Creation of Advance Journal</li>
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

	public static class CreationOfAdvanceJournalEntry extends Keyword {

		private String fromsubsidiary;
		private String toSubsidiary;
		private String customForm;
		private String fromEmpName;
		private String toEmpName;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("FromSubsidiary");
				fromsubsidiary = args.get("FromSubsidiary");
				verifyArgs("ToSubsidiary");
				toSubsidiary = args.get("ToSubsidiary");
				verifyArgs("CustomForm");
				customForm = args.get("CustomForm");
				verifyArgs("JEAmount");
				jeAmount = args.get("JEAmount");
				verifyArgs("FromEmployeeName");
				fromEmpName = args.get("FromEmployeeName");
				verifyArgs("ToEmployeeName");
				toEmpName = args.get("ToEmployeeName");
			} catch (Exception e) {
				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.hoverMouseUsingJavascript(driver, "xpath", "transactions_tab", CURR_APP);
			this.addComment("Mouse hover to Transactions Tab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "Finacial_SubTab", CURR_APP);
			this.addComment("Mouse hover to Finacial Subtab");
			eo.hoverMouseUsingJavascript(driver, "xpath", "Advance_Journal_Entry", CURR_APP);
			this.addComment("Mouse hover to Journal Entry Subtab");
			eo.clickElement(driver, "xpath", "Advance_Journal_Entry", CURR_APP);
			String advanceJEPageName = eo.getText(driver, "xpath", "header_JE", CURR_APP);
			if (advanceJEPageName.equals("Advanced Intercompany Journa")) {
				this.addComment("Sucessfully verify Journal Entry Page");
			} else {
				this.addComment("Header is missmatched");
			}

			eo.wait(2);

			eo.clickElement(driver, "id", "customForm", CURR_APP);

			List<WebElement> dorpDownList = eo.getListOfWebElements(driver, "xpath", "customFormDropdownList",
					CURR_APP);
			for (int i = 0; i < dorpDownList.size(); i++) {
				String dropDownListText = dorpDownList.get(i).getText().trim();
				if (dropDownListText.equalsIgnoreCase(customForm)) {
					dorpDownList.get(i).click();
					break;
				}
			}
			eo.waitForPageload(driver);
			eo.clickElement(driver, "xpath", "subsidiary_JEDropDown", CURR_APP);
			List<WebElement> subsidiaryList = eo.getListOfWebElements(driver, "xpath", "subsidiary_JEDropDownList",
					CURR_APP);
			for (int i = 0; i <= subsidiaryList.size() - 1; i++) {
				String subsidiaryBundle = subsidiaryList.get(i).getText().trim();
				System.out.println("List of Subsidiaries:" + subsidiaryBundle);
				if (subsidiaryBundle.contains(fromsubsidiary)) {
					subsidiaryList.get(i).click();
					break;
				}
			}

			eo.javaScriptScrollToViewElement(driver, "xpath", "linesTab_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			eo.actionClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			List<WebElement> subsidiaryList_JE = eo.getListOfWebElements(driver, "xpath", "subsidiaryList_AdvanceJE",
					CURR_APP);
			for (int i = 0; i < subsidiaryList_JE.size(); i++) {
				String subsidiaryNames = subsidiaryList_JE.get(i).getText().trim();
				if (subsidiaryNames.equalsIgnoreCase(fromsubsidiary)) {
					subsidiaryList_JE.get(i).click();
					break;
				}
			}
			eo.wait(2);
			List<WebElement> typeAndThenList = eo.getListOfWebElements(driver, "xpath", "typeAndThen", CURR_APP);
			for (int i = 0; i <= typeAndThenList.size(); i++) {
				typeAndThenList.get(1).click();
				break;
			}

			eo.javaScriptClick(driver, "xpath", "Acountlist_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "ListSubtab_JE", CURR_APP);

			List<WebElement> account_List_AdvanceJE = eo.getListOfWebElements(driver, "xpath", "text_JE2", CURR_APP);
			for (int j = 0; j < account_List_AdvanceJE.size(); j++) {
				account_List_AdvanceJE.get(5).click();
				break;
			}

			eo.enterText(driver, "xpath", "debit_JE", jeAmount, CURR_APP);
			/////////////////////////////////////////////////
			eo.wait(3);
			List<WebElement> listName = eo.getListOfWebElements(driver, "xpath", "name_AdvanceJE", CURR_APP);
			for (int i = 1; i < listName.size(); i++) {
				listName.get(1).click();
				break;
			}

			eo.javaScriptClick(driver, "xpath", "downArrow_Name_AdvJE", CURR_APP);
			eo.wait(3);
			eo.javaScriptClick(driver, "xpath", "list_Downarrow_Adv_JE", CURR_APP);
			eo.wait(10);
			eo.enterText(driver, "xpath", "search_innerTextField", "i/c ", CURR_APP);
			eo.wait(3);
			Actions act = new Actions(driver);
			act.sendKeys(Keys.TAB).perform();

			eo.wait(3);
			List<WebElement> searchOptions = eo.getListOfWebElements(driver, "xpath", "innerSearchOptions", CURR_APP);
			for (int i = 1; i < searchOptions.size(); i++) {
				String searchOptionsFromInnerDropdown = searchOptions.get(i).getText().trim();
				System.out.println("+++++++++++++++" + searchOptionsFromInnerDropdown);
				if (searchOptionsFromInnerDropdown.contains("I/C Cust INC to INTL")) {
					searchOptions.get(i).click();
					break;
				}
			}

			eo.wait(2);
			eo.clickElement(driver, "id", "addButton_JE", CURR_APP);

			eo.javaScriptClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			eo.actionClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			List<WebElement> subsidiaryList2_JE = eo.getListOfWebElements(driver, "xpath", "subsidiaryList_AdvanceJE",
					CURR_APP);
			for (int i = 0; i < subsidiaryList2_JE.size(); i++) {
				String subsidiaryNames = subsidiaryList2_JE.get(i).getText().trim();
				if (subsidiaryNames.equalsIgnoreCase(fromsubsidiary)) {
					subsidiaryList2_JE.get(i).click();
					break;
				}
			}
			eo.wait(2);
			List<WebElement> typeAndThenList2 = eo.getListOfWebElements(driver, "xpath", "typeAndThen", CURR_APP);
			for (int i = 0; i <= typeAndThenList2.size(); i++) {
				typeAndThenList2.get(2).click();
				break;
			}

			eo.javaScriptClick(driver, "xpath", "Acountlist_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "ListSubtab_JE", CURR_APP);

			List<WebElement> account_List2_AdvanceJE = eo.getListOfWebElements(driver, "xpath", "text_JE2", CURR_APP);
			for (int j = 0; j < account_List2_AdvanceJE.size(); j++) {
				account_List2_AdvanceJE.get(9).click();
				break;
			}

			eo.wait(3);
			Actions act1 = new Actions(driver);
			act1.sendKeys(Keys.TAB).perform();
			act1.sendKeys(Keys.ENTER).perform();

			eo.wait(2);
			eo.javaScriptClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			eo.actionClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			List<WebElement> subsidiaryList3_JE = eo.getListOfWebElements(driver, "xpath", "subsidiaryList_AdvanceJE",
					CURR_APP);
			for (int i = 0; i < subsidiaryList3_JE.size(); i++) {
				String subsidiaryNames = subsidiaryList3_JE.get(i).getText().trim();
				if (subsidiaryNames.equalsIgnoreCase(toSubsidiary)) {
					subsidiaryList3_JE.get(i).click();
					break;
				}
			}
			eo.wait(2);
			List<WebElement> typeAndThenList3 = eo.getListOfWebElements(driver, "xpath", "typeAndThen", CURR_APP);
			for (int i = 0; i <= typeAndThenList3.size(); i++) {
				typeAndThenList3.get(3).click();
				break;
			}
			eo.javaScriptClick(driver, "xpath", "Acountlist_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "ListSubtab_JE", CURR_APP);

			List<WebElement> account_List3_AdvanceJE = eo.getListOfWebElements(driver, "xpath", "text_JE2", CURR_APP);
			for (int j = 0; j < account_List3_AdvanceJE.size(); j++) {
				account_List3_AdvanceJE.get(5).click();
				break;
			}

			eo.enterText(driver, "xpath", "debit_JE", jeAmount, CURR_APP);

			eo.wait(3);
			/////////////////////////////////////////////////////////
			List<WebElement> listName2 = eo.getListOfWebElements(driver, "xpath", "name_AdvanceJE", CURR_APP);
			for (int i = 1; i < listName2.size(); i++) {
				listName2.get(3).click();
				break;
			}

			eo.javaScriptClick(driver, "xpath", "downArrow_Name_AdvJE", CURR_APP);
			eo.wait(3);
			eo.javaScriptClick(driver, "xpath", "list_Downarrow_Adv_JE", CURR_APP);
			eo.wait(10);
			eo.enterText(driver, "xpath", "search_innerTextField", "i/c ", CURR_APP);
			eo.wait(3);
			Actions act3 = new Actions(driver);
			act.sendKeys(Keys.TAB).perform();

			eo.wait(3);
			List<WebElement> searchOptions2 = eo.getListOfWebElements(driver, "xpath", "innerSearchOptions", CURR_APP);
			for (int i = 1; i < searchOptions2.size(); i++) {
				String searchOptionsFromInnerDropdown = searchOptions2.get(i).getText().trim();
				System.out.println("+++++++++++++++" + searchOptionsFromInnerDropdown);
				if (searchOptionsFromInnerDropdown.contains("I/C Cust INTL to INC")) {
					searchOptions2.get(i).click();
					break;
				}
			}
			eo.wait(2);
			eo.clickElement(driver, "id", "addButton_JE", CURR_APP);

			eo.javaScriptClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			eo.actionClick(driver, "xpath", "subsidiary_AdvanceJE", CURR_APP);
			List<WebElement> subsidiaryList4_JE = eo.getListOfWebElements(driver, "xpath", "subsidiaryList_AdvanceJE",
					CURR_APP);
			for (int i = 0; i < subsidiaryList4_JE.size(); i++) {
				String subsidiaryNames = subsidiaryList4_JE.get(i).getText().trim();
				if (subsidiaryNames.equalsIgnoreCase(toSubsidiary)) {
					subsidiaryList4_JE.get(i).click();
					break;
				}
			}
			eo.wait(2);
			List<WebElement> typeAndThenList4 = eo.getListOfWebElements(driver, "xpath", "typeAndThen", CURR_APP);
			for (int i = 0; i <= typeAndThenList4.size(); i++) {
				typeAndThenList4.get(4).click();
				break;
			}
			eo.javaScriptClick(driver, "xpath", "Acountlist_JE", CURR_APP);
			eo.javaScriptClick(driver, "xpath", "ListSubtab_JE", CURR_APP);

			List<WebElement> account_List4_AdvanceJE = eo.getListOfWebElements(driver, "xpath", "text_JE2", CURR_APP);
			for (int j = 0; j < account_List4_AdvanceJE.size(); j++) {
				account_List4_AdvanceJE.get(5).click();
				break;
			}

			eo.wait(3);
			Actions act2 = new Actions(driver);
			act2.sendKeys(Keys.TAB).perform();
			act2.sendKeys(Keys.ENTER).perform();

			eo.javaScriptScrollToViewElement(driver, "xpath", "general_information_AdvJE", CURR_APP);
			eo.wait(3);
			eo.actionClick(driver, "xpath", "from_Subsidiary_Approver_DropdownArrow_AdvJE", CURR_APP);
			eo.wait(2);
			List<WebElement> fromSubsidiaryApproverList = eo.getListOfWebElements(driver, "xpath",
					"from_Subsidiary_Approver_DropdownList_AdvJE", CURR_APP);
			for (int i = 0; i <= fromSubsidiaryApproverList.size(); i++) {
				String fromSubsidiaryBundle = fromSubsidiaryApproverList.get(i).getText().trim();
				System.out.println("List of Subsidiaries:" + fromSubsidiaryBundle);
				if (fromSubsidiaryBundle.equalsIgnoreCase(fromEmpName)) {
					fromSubsidiaryApproverList.get(i).click();
					break;
				}
			}
			eo.wait(3);
			eo.actionClick(driver, "xpath", "to_Subsidiary_Approver_DropdownArrow_AdvJE", CURR_APP);
			eo.wait(3);
			List<WebElement> toSubsidiaryApproverList = eo.getListOfWebElements(driver, "xpath",
					"to_Subsidiary_Approver_DropdownList_AdvJE", CURR_APP);
			for (int i = 0; i <= toSubsidiaryApproverList.size(); i++) {
				String toSubsidiaryBundle = toSubsidiaryApproverList.get(i).getText().trim();
				System.out.println("List of Subsidiaries:" + toSubsidiaryBundle);
				if (toSubsidiaryBundle.equalsIgnoreCase(toEmpName)) {
					toSubsidiaryApproverList.get(i).click();
					break;

				}
			}

			Actions action = new Actions(driver);
			WebElement we = driver.findElement(By.xpath("//h1[contains(text(),'Journal')]"));
			action.moveToElement(we).moveToElement(driver.findElement(By.id("spn_multibutton_submitter"))).click()
					.build().perform();

			eo.wait(3);
			String JENumber = eo.getText(driver, "xpath", "JE_Number", CURR_APP);
			jeValue = JENumber.replaceAll("[^0-9]", "");
			System.out.println("Number Of JE:" + jeValue);

			String status = eo.getText(driver, "xpath", "approval_Status_JE", CURR_APP);
			if (status.equalsIgnoreCase("Pending Approval")) {
				eo.clickElement(driver, "xpath", "approval_Buttton_JE", CURR_APP);
				this.addComment("Successfully Clicked on Submit For Approval");
			} else {
				throw new KDTKeywordExecException("Actual And Expected approval Status are miss matched");
			}

		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>FromSubsidiaryApproverAdvanceJE</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Open Created JE And Click
	 * either approve or Reject Button based on requirement</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
	 * <li>FromSubsidiaryApproverAdvanceJE</li>
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

	public static class FromSubsidiaryApproverAdvanceJE extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", jeValue, CURR_APP);
			this.addComment("Searched for the Advance JE Number" + jeValue);
			List<WebElement> journalList = eo.getListOfWebElements(driver, "xpath", "journal_advJE", CURR_APP);
			for (int i = 0; i < journalList.size(); i++) {
				journalList.get(0).click();
				break;
			}
			// eo.clickElement(driver, "xpath", "journal_advJE", CURR_APP);
			this.addComment("Successfully clicked on Advance JE Number");
			String Header = eo.getText(driver, "xpath", "Advanced_Intercompany_JournalPage_Header", CURR_APP);
			if (Header.equalsIgnoreCase("Advanced Intercompany Journal")) {
				this.addComment("Sucessfully verify HeaderName:" + Header);
			} else {
				this.addComment("Header Name miss matched");
			}
			String approvalStatus = eo.getText(driver, "id", "pending_approval_Staus_AdvJE", CURR_APP);
			if (approvalStatus.equalsIgnoreCase("Pending Approval")) {
				eo.clickElement(driver, "xpath", "Advanced_Intercompany_JournalPage_approve_Button", CURR_APP);
				this.addComment("Succesfully Clicked on Approve Button");
			} else {
				this.addComment("Approve button is missing/disabled in Advance Intercompany Journal page");
			}
			eo.waitForPageload(driver);
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ToSubsidiaryApproverAdvanceJE</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Open Created JE And Click
	 * either approve or Reject Button based on requirement</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
	 * <li>FromSubsidiaryApproverAdvanceJE</li>
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

	public static class ToSubsidiaryApproverAdvanceJE extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", jeValue, CURR_APP);
			this.addComment("Searched for the Advance JE Number" + jeValue);
			List<WebElement> journalList = eo.getListOfWebElements(driver, "xpath", "journal_advJE", CURR_APP);
			for (int i = 0; i < journalList.size(); i++) {
				journalList.get(0).click();
				break;
			}
			// eo.clickElement(driver, "xpath", "journal_advJE", CURR_APP);
			this.addComment("Successfully clicked on Advance JE Number");
			String Header = eo.getText(driver, "xpath", "Advanced_Intercompany_JournalPage_Header", CURR_APP);
			if (Header.equalsIgnoreCase("Advanced Intercompany Journal")) {
				this.addComment("Sucessfully verify HeaderName:" + Header);
			} else {
				this.addComment("Header Name miss matched");
			}
			String approvalStatus = eo.getText(driver, "id", "pending_approval_Staus_AdvJE", CURR_APP);
			if (approvalStatus.equalsIgnoreCase("Pending Approval")) {
				eo.clickElement(driver, "xpath", "Advanced_Intercompany_JournalPage_approve_Button", CURR_APP);
				this.addComment("Succesfully Clicked on Approve Button");
			} else {
				this.addComment("Approve button is missing/disabled in Advance Intercompany Journal page");
			}
			eo.waitForPageload(driver);
			String StatusMassage = eo.getText(driver, "xpath", "approval_Status_AdvJE", CURR_APP);
			if (StatusMassage.equalsIgnoreCase("Approved")) {
				this.addComment(
						"Successfully verified Approval Status after approve from both approver" + StatusMassage);
			} else if (StatusMassage.equalsIgnoreCase("Rejected")) {
				this.addComment("Advance Intercompany journal is already rejected" + StatusMassage);
			} else {
				throw new KDTKeywordExecException("Approval Status is Miss matched" + StatusMassage);
			}
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>EditAdvanceJE</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Edit JE BAsed on
	 * Requirement</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproverSetUpInEmployeeRecords</li>
	 * <li>FromSubsidiaryApproverAdvanceJE</li>
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

	public static class EditAdvanceJE extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.enterText(driver, "xpath", "globalSerachNetsuite", jeValue, CURR_APP);
			this.addComment("Searched for the Advance JE Number" + jeValue);
			List<WebElement> journalList = eo.getListOfWebElements(driver, "xpath", "journal_advJE", CURR_APP);
			for (int i = 0; i < journalList.size(); i++) {
				journalList.get(0).click();
				break;
			}
			// eo.clickElement(driver, "xpath", "journal_advJE", CURR_APP);
			this.addComment("Successfully clicked on Advance JE Number");
			String Header = eo.getText(driver, "xpath", "Advanced_Intercompany_JournalPage_Header", CURR_APP);
			if (Header.equalsIgnoreCase("Advanced Intercompany Journal")) {
				this.addComment("Sucessfully verify HeaderName:" + Header);
			} else {
				this.addComment("Header Name miss matched");
			}
			boolean result = eo.isDisplayed(driver, "xpath", "Advanced_Intercompany_JournalPage_Reject_Button",
					CURR_APP);
			if (result == false) {
				eo.clickElement(driver, "xpath", "edit_employeeRecord", CURR_APP);
				eo.waitForPageload(driver);
				eo.clickElement(driver, "id", "saveButton_JE", CURR_APP);
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "approval_Buttton_JE", CURR_APP);
				eo.clickElement(driver, "xpath", "Advanced_Intercompany_JournalPage_Reject_Button", CURR_APP);
			} else if (result == true) {
				eo.clickElement(driver, "xpath", "Advanced_Intercompany_JournalPage_Reject_Button", CURR_APP);
			} else {
				throw new KDTKeywordExecException("Reject button is appearing");

			}
			String pageHeader = eo.getText(driver, "xpath", "rejection_PageText", CURR_APP);
			if (pageHeader.equalsIgnoreCase("Rejection Reason")) {
				this.addComment("Successfully verify page Header after click on Reject button" + pageHeader);
			} else {
				throw new KDTKeywordExecException("page header miss matched" + pageHeader);
			}
			eo.clickElement(driver, "id", "rejectionReason_SaveButton", CURR_APP);

			try {
				// Wait 10 seconds till alert is present
				WebDriverWait wait = new WebDriverWait(driver, 10);
				Alert alert = wait.until(ExpectedConditions.alertIsPresent());
				alert.accept();
			} catch (Exception e) {
				addComment("Alert is not present");
			}

			eo.enterText(driver, "xpath", "rejectionReason_TextBox",
					"For Testing Purpose I am rejecting this Advance JE", CURR_APP);
			eo.clickElement(driver, "id", "rejectionReason_SaveButton", CURR_APP);
			eo.waitForPageload(driver);
			String approvaltatusAfterRejectJE = eo.getText(driver, "xpath", "approval_Status_AdvJE", CURR_APP);
			if (approvaltatusAfterRejectJE.equalsIgnoreCase("Rejected")) {
				this.addComment("Successfully rejected Advance JE" + approvaltatusAfterRejectJE);
			} else {
				throw new KDTKeywordExecException("Advance JE is not rejected yet" + approvaltatusAfterRejectJE);
			}
		}

	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>EditAdvanceJE</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b>This Keyword is used to Craete New file Desktop and
	 * compare the text file</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginNetSuite</li>
	 * <li>NavigateToSandBox2</li>
	 * <li>ApproveOrderNetsuite</li>
	 * <li>ProductCompareNetsuite</li>
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

	public static class ProductCompareNetsuite extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Navigate to purchase order page
			List<WebElement> productList = eo.getListOfWebElements(driver, "xpath", "productList", CURR_APP);
			// File file = new File("C:\\Users\\arun.swain\\Desktop\\productListForNS.txt");
			File file = new File(
					System.getProperty("user.dir") + "/src/com/qualitestgroup/keywords/netsuite/ProductCompareNS.txt");
			if (file.exists()) {
				file.delete();
				this.addComment("Sucessfully deleted existing file");
			} else {
				this.addComment("file is not present in specfic location");
			}
			try {
				file.createNewFile();

				FileWriter writer = new FileWriter(file);
				int size = productList.size();
				for (int i = 0; i < size; i++) {
					String str = productList.get(i).getText().trim().toString();
					writer.write(str);
					if (i < size - 1)
						writer.write("\n");
				}

				writer.close();

				BufferedReader reader1 = new BufferedReader(new FileReader(file));

				BufferedReader reader2 = new BufferedReader(new FileReader(file));

				String line1 = reader1.readLine();
				System.out.println("linr1++++++++++++:" + line1);

				String line2 = reader2.readLine();
				System.out.println("line===========:" + line2);

				boolean areEqual = true;

				int lineNum = 1;

				while (line1 != null || line2 != null) {
					if (line1 == null || line2 == null) {
						areEqual = false;

						break;
					} else if (!line1.equalsIgnoreCase(line2)) {
						areEqual = false;

						break;
					}

					line1 = reader1.readLine();

					line2 = reader2.readLine();

					lineNum++;
				}

				if (areEqual) {
					System.out.println("Two files have same content.");
				} else {
					System.out.println("Two files have different content. They differ at line " + lineNum);

					System.out.println("File1 has " + line1 + " and File2 has " + line2 + " at line " + lineNum);
				}

				reader1.close();

				reader2.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
}
