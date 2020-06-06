package com.qualitestgroup.keywords.gtc;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
import org.openqa.selenium.Keys;

public class GTC extends KeywordGroup {

	private static final String CURR_APP = "gtc";
	public static GetElementIdentifier gei = new GetElementIdentifier();
	static GetProperty getProps = new GetProperty();
	public static ElementOperation eo = new ElementOperation();
	public static String lastName;
	public static String expectedAccountNameFetched = "";
	public static String isScreeningByPass;
	public static String OrderAmount="";
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>LoginSalesForce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to login into the
	 * application.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Username(Mandatory):Username</li>
	 * <li>Password(Mandatory):Password</li>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash</i></b>
	 * </p>
	 * </div>
	 */

	public static class LoginSalesForce extends Keyword {
		private String url;
		private String username;
		private String password;
		private String browser = "";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			url = args.get("Url");
			username = args.get("Username");
			password = args.get("Password");
			if (hasArgs("Browser")) {
				browser = args.get("Browser");
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			if (!(browser.isEmpty())) {
				try {
					Keyword.run("Browser", "Launch", "Browser", browser);
				} catch (KDTException e) {
					throw new KDTKeywordExecException("Unable to launch browser");
				}
			}

			WebDriver driver = context.getWebDriver();
			driver.get(url);

			eo.waitForWebElementVisible(driver, "ID", "username", waiTime, CURR_APP);
			eo.enterText(driver, "ID", "username", username, CURR_APP);
			addComment("Successfully  enter the username");

			eo.enterText(driver, "ID", "password", password, CURR_APP);
			addComment("Successfully enter the password");

			eo.clickElement(driver, "ID", "login_sandbox", CURR_APP);
			eo.waitForPageload(driver);
			addComment("Successfully click on login button");

			try {

				// eo.waitForWebElementVisible(driver, "xpath", "userProfileBtn",5, CURR_APP);

				Boolean blnLightning = eo.isExists(driver, "xpath", "userProfileBtn", CURR_APP);
				Boolean blnClassic = eo.isExists(driver, "xpath", "home_tab", CURR_APP);

				int maxTime = 300;
				while ((!blnLightning && !blnClassic) && maxTime > 0) {

					eo.wait(1);
					blnLightning = eo.isExists(driver, "xpath", "userProfileBtn", CURR_APP);
					blnClassic = eo.isExists(driver, "xpath", "home_tab", CURR_APP);

					maxTime--;

				}

				if (blnLightning) {
					eo.actionDoubleClick(driver, "xpath", "userProfileBtn", CURR_APP);
					eo.waitForWebElementVisible(driver, "xpath", "switchToClassicLink", 5, CURR_APP);
					eo.wait(2);
					eo.actionDoubleClick(driver, "xpath", "switchToClassicLink", CURR_APP);
					eo.waitForPageload(driver);
				} else if (blnClassic) {
					addComment("Already in classic mode");
				}
			} catch (Exception e) {
				addComment("no issues");
			}
			eo.waitForWebElementVisible(driver, "XPATH", "home_tab", waiTime, CURR_APP);
			addComment("Successfully verified the home tab is dispalyed");
		}
	}

	/////////////////////// Logout salesforce lightning mode////
	public static class LogoutSalesforceLM extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			try {
				driver.switchTo().alert().dismiss();
			} catch (Exception e) {
				addComment("No alert present");
			}
			eo.clickElement(driver, "xpath", "userProfileBtn", CURR_APP);
			eo.wait(3);
			eo.clickElement(driver, "xpath", "logoutLightningMode", CURR_APP);

		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateAccount</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to create account in
	 * salesforce by passing the account name and record type</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesForce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>NewRecordType(Mandatory):newrecordType</li>
	 * <li>AccountName(Mandatory):accountName</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash</i></b>
	 * </p>
	 * </div>
	 */

	///////////////////////

	public static class CreateAccount extends Keyword {
		private String accountName;
		private String billingCountry;
		private String billingState;
		private String billingCity;
		private String billingStreet;
		private String billingPostalCode;
		private String newrecordType;
		private String L3Elegible;
		private String rASP;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			accountName = args.get("AccountName").trim() + eo.generateCurrDate();
			billingCountry = args.get("BillingCountry").trim();
			billingState = args.get("BillingState").trim();
			billingCity = args.get("BillingCity").trim();
			billingStreet = args.get("BillingStreet").trim();
			billingPostalCode = args.get("BillingPostalCode").trim();
			newrecordType = args.get("NewRecordType").trim();

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.clickElement(driver, "XPATH", "account_tab", CURR_APP);
			addComment("Successfully click on account tab");
			eo.waitForWebElementVisible(driver, "XPATH", "account_new_btn", waiTime, CURR_APP);
			eo.clickElement(driver, "XPATH", "account_new_btn", CURR_APP);
			addComment("Successfully click on new button ");

			eo.waitForWebElementVisible(driver, "XPATH", "accSearchPage", waiTime, CURR_APP);
			addComment("Successfully verified the <b> new account search page</b> is displayed");
			// Enter Account name

			saveValue(accountName);
			context.getData().get("$expectedAccountName");

			eo.wait(8);
			eo.enterText(driver, "XPATH", "accNameField", accountName, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("accNameField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered acc name");
			// Select billing country
			eo.selectComboBoxByVisibleText(driver, "xpath", "billingCountryField", billingCountry, CURR_APP);
			// select billing state
			eo.selectComboBoxByVisibleText(driver, "xpath", "billingStateField", billingState, CURR_APP);
			// enter billing city
			eo.enterText(driver, "XPATH", "billingCityField", billingCity, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingCityField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing city");
			// enter billing street
			eo.enterText(driver, "XPATH", "billingStreetField", billingStreet, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingStreetField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing street");
			// enter postal code
			eo.enterText(driver, "XPATH", "billingPostalCode", billingPostalCode, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingPostalCode", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered postal code");
			eo.clickElement(driver, "xpath", "accSearchOrCreateBtn", CURR_APP);
			eo.wait(10);
			eo.waitForPageload(driver);
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("accCreateBtn", CURR_APP))));
			// eo.actionDoubleClick(driver, "xpath", "accCreateBtn", CURR_APP);
			eo.clickElement(driver, "xpath", "accCreateBtn", CURR_APP);
			eo.waitForPageload(driver);

			switch (newrecordType) {

			case "Customer/Prospect":

				eo.selectComboBoxByVisibleText(driver, "xpath", "recordeTypeField", newrecordType, CURR_APP);
				break;

			case "Partner/Distributor":
				eo.selectComboBoxByVisibleText(driver, "xpath", "recordeTypeField", newrecordType, CURR_APP);

				break;

			default:
				break;
			}

			eo.wait(5);
			eo.clickElement(driver, "xpath", "continueBtn", CURR_APP);
			eo.waitForPopUp(driver);
			eo.waitForPopUp(driver);

			if (hasArgs("IsL3Elegible") && hasArgs("RASP")) {
				L3Elegible = args.get("IsL3Elegible");
				rASP = args.get("RASP");

				for (int i = 0; i <= 1; i++) {

					// verifying and Clicking isL3Elegible checkbox

					if (eo.getAttributeAfterReplacingValue(driver, "xpath", "IsL3Eligible", "{text}", L3Elegible,
							"title", CURR_APP).equalsIgnoreCase("Not Checked")) {

						this.addComment("Suucessfully verified" + " " + "<b>" + L3Elegible + " " + "</b>"
								+ "field is Unchecked by default");

						eo.javaScriptDoubleClickAfterReplacingValue(driver, "xpath", "IsL3Eligible", "{text}",
								L3Elegible, CURR_APP);

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "IsL3EligibleCheckbox", "{text}",
								L3Elegible, CURR_APP);
						this.addComment(
								"Successfully clicked the" + " " + "<b>" + L3Elegible + " " + "</b>" + "checkbox");

					}

					// verifying and Clicking RASP checkbox
					if (eo.getAttributeAfterReplacingValue(driver, "xpath", "IsL3Eligible", "{text}", rASP, "title",
							CURR_APP).equalsIgnoreCase("Not Checked")) {
						this.addComment("Suucessfully verified" + " " + "<b>" + rASP + " " + "</b>"
								+ "field is Unchecked by default");

						eo.javaScriptDoubleClickAfterReplacingValue(driver, "xpath", "IsL3Eligible", "{text}", rASP,
								CURR_APP);

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "IsL3EligibleCheckbox", "{text}", rASP,
								CURR_APP);
						this.addComment("Successfully clicked the" + " " + "<b>" + rASP + " " + "</b>" + "checkbox");

						eo.clickElement(driver, "xpath", "saveButton", CURR_APP);
						driver.navigate().refresh();
						eo.waitForPageload(driver);
					}
				}

			}

			SeleniumTools.waitForWebElementVisible(driver, By.xpath(gei.getProperty("createdAccNameText", CURR_APP)), waiTime);
			//eo.waitForWebElementVisible(driver, "XPATH", "createdAccNameText", waiTime, CURR_APP);
			String actualaccountName = eo.getText(driver, "XPATH", "createdAccNameText", CURR_APP);
			System.out.println("actualaccountName::" + actualaccountName);
			System.out.print("accountName:" + accountName);
			if (actualaccountName.contains(accountName)) {
				addComment("Successfully verified the created account name is <b>" + accountName + "</b>");
			} else {
				addComment("Expected account name  is " + accountName);
				addComment("Actual account name  is " + actualaccountName);
				throw new KDTKeywordExecException("Actual and Expected account name are not same");
			}
			
		}
	}

	//////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>LogoutSalesforce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to logout the
	 * application.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class LogoutSalesforce extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

				eo.javaScriptScrollToViewElement(driver, "xpath", "userMenu", CURR_APP);
				eo.clickElement(driver, "xpath", "userMenu", CURR_APP);
				eo.clickElement(driver, "xpath", "logoutButton", CURR_APP);
				// eo.waitForWebElementVisible(driver, "id", "userNameVerify", waiTime,
				// CURR_APP);
			}

			catch (Exception e) {

				this.addComment("Failed to logout from salesforce");
				throw new KDTKeywordExecException("Failed to logout from salesforce", e);
			}
		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SubTabName</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to select the sub tabs in
	 * application pages.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>CreateAccount</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>SubTabName(Mandatory): The tab need to be selected</li>
	 * <li>NewButtonName(Mandatory): The button to be clicked</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class SelectSubTab extends Keyword {

		private String subTabName;
		private String newButtonName;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("SubTabName", "NewButtonName");
				subTabName = args.get("SubTabName");
				newButtonName = args.get("NewButtonName");

			} catch (Exception e) {
				this.addComment("Error while initializing SelectSubTab");
				throw new KDTKeywordInitException("Error while initializing SelectSubTab", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				if (subTabName.equalsIgnoreCase("Quotes")) {

					eo.actionMoveToElementAfterReplace(driver, "xpath", "subTabQuotes", "{link}", subTabName, CURR_APP);

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "newButtonQuote", "{link}", newButtonName,
							CURR_APP);

					eo.waitForPageload(driver);

					this.addComment("Clicked on new button for " + subTabName);
					eo.waitForPageload(driver);
				}

				else if (subTabName.equalsIgnoreCase("Account Team")) {

					eo.actionMoveToElementAfterReplace(driver, "xpath", "subTab", "{link}", subTabName, CURR_APP);

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "newButton", "{link}", " Add ", CURR_APP);

					this.addComment("Clicked on add button for " + subTabName);
					eo.waitForPageload(driver);

				}

				else {

					eo.actionMoveToElementAfterReplace(driver, "xpath", "subTab", "{link}", subTabName, CURR_APP);

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "newButton", "{link}", newButtonName,
							CURR_APP);

					this.addComment("Clicked on new button for " + subTabName);
					eo.waitForPageload(driver);

				}

			}

			catch (Exception e) {

				this.addComment("User is Unable to Click on " + subTabName);
				throw new KDTKeywordExecException("User is Unable to Click on " + subTabName, e);

			}

		}
	}

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateContact</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to Create Contact.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>SelectSubTab</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ContactLastName(Mandatory): The contact name need to be entered</li>
	 * <li>ContactLeadSource(Mandatory): The lead source need to be selected</li>
	 * <li>ContactEmailId(Mandatory): The email id need to be entered</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateContact extends Keyword {

		//private String lastName;
		private String leadSource;
		private String emailId;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ContactLastName", "ContactLeadSource", "ContactEmailId");
				lastName = args.get("ContactLastName") + eo.generateCurrDate().substring(6);
				leadSource = args.get("ContactLeadSource");
				emailId = args.get("ContactEmailId");

			} catch (Exception e) {
				this.addComment("Error while initializing NewContact");
				throw new KDTKeywordInitException("Error while initializing NewContact", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// eo.waitForWebElementVisible(driver, "xpath", "contactEdit", 20, CURR_APP);

				eo.enterText(driver, "xpath", "lastNameField", lastName, CURR_APP);
				this.addComment("Entered the last name in create contact page");

				eo.enterText(driver, "xpath", "emailField", emailId, CURR_APP);
				this.addComment("Entered the email id in create contact page");

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "leadSource", "{link}", leadSource, CURR_APP);
				this.addComment("Selected the lead source from drop down list");

				eo.clickElement(driver, "xpath", "saveContact", CURR_APP);
				this.addComment("Saved the created Contact");

				eo.waitForPageload(driver);

				// Capturing the Contact Id from URL

				String currUrl = driver.getCurrentUrl();
				String ar[] = currUrl.split("/");
				String contactId = ar[3];
				addComment("Successfully Created the contact with contact ID:" + "<b>" + contactId + "</b");

				// verification part

				System.out.println(eo.getText(driver, "xpath", "lastNameAfterSave", CURR_APP));
				System.out.println(eo.getText(driver, "xpath", "emailAfterSave", CURR_APP));
				System.out.println(eo.getText(driver, "xpath", "leadSourceAfterSave", CURR_APP));

				if (eo.getText(driver, "xpath", "lastNameAfterSave", CURR_APP).equalsIgnoreCase(lastName)) {
					this.addComment("The last name of created contact is verified");
				}

				else {
					this.addComment("The last name of created contact is failed to verify");
					throw new KDTKeywordExecException("The last name of created contact is failed to verify");
				}

				if (eo.getText(driver, "xpath", "emailAfterSave", CURR_APP).equalsIgnoreCase(emailId)) {
					this.addComment("The emaild id of created contact is verified");
				}

				else {
					this.addComment("The email id of created contact is failed to verify");
					throw new KDTKeywordExecException("The email id of created contact is failed to verify");
				}

				if (eo.getText(driver, "xpath", "leadSourceAfterSave", CURR_APP).equalsIgnoreCase(leadSource)) {

					this.addComment("The leadSource of created contact is verified");

				}

				else {
					this.addComment("The leadSource of created contact is failed to verify");
					throw new KDTKeywordExecException("The leadSource of created contact is failed to verify");
				}
			}

			catch (Exception e) {

				this.addComment("User is Unable to Create Contact");
				throw new KDTKeywordExecException("User is Unable Create Contact", e);

			}

		}

	}

	//////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpportunityStageChange</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to Change the Opp stage from
	 * 1 to 6 and create an order.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>CreateAccount</li>
	 * <li>CreateContact</li>
	 * <li>CreateOpportunity</li>
	 * <li>CreateQuote</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Stage(Mandatory): The Stage need to be selected in numbers(1,2,3,4,5,6
	 * etc)</li>
	 * <li>OppSubType(Mandatory): The Opportunity sub type need to be
	 * selected(Regular,MSP)</li>
	 * <li>InstallationAssignedTo(Mandatory): The Installation assigned to need to
	 * be selected(FE Install,Partner Install)</li>
	 * <li>PartnerLevelOfInvolvement(Mandatory): The Partner Level Of Involvement
	 * need to be selected(1.Rubrik initiated, Partner was fulfillment
	 * only,2.Partner initiated opportunity, but had little involvement in sales
	 * cycle,3.Rubrik initiated, but partner was engaged and brought value in sales
	 * cycle)</li>
	 * <li>LostToWonAgainst(Mandatory): The Lost To / Won Against need to be
	 * selected(Actifio,Arcserve etc)</li>
	 * <li>PolarisRegion(Mandatory): The Polaris region need to be
	 * selected(Europe,US)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class OpportunityStageChange extends Keyword {

		private String stage;
		private String oppSubType;
		private String textAreaComment;
		private String distributorLookup;
		private String partnerLookup;
		private String lostToWonAgainst;
		private String primarycompetitor;
		private String keyFeatureNeeded;
		private String polarisRegion;
		private String partnerLevelOfInvolvement;
		private String installationAssignedTo;
		private String compellingBusDriver;
		private String polarisPortalContactValue;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				verifyArgs("Stage", "OppSubType", "InstallationAssignedTo", "PartnerLevelOfInvolvement",
						"LostToWonAgainst", "PolarisRegion");
				stage = args.get("Stage");
				oppSubType = args.get("OppSubType");
				installationAssignedTo = args.get("InstallationAssignedTo");
				partnerLevelOfInvolvement = args.get("PartnerLevelOfInvolvement");
				lostToWonAgainst = args.get("LostToWonAgainst");
				polarisRegion = args.get("PolarisRegion");
				primarycompetitor = args.get("Primarycompetitor");
				compellingBusDriver = args.get("CompellingBusDriver");
				distributorLookup = args.get("DistributorLookup");
				textAreaComment = args.get("TextAreaComment");
				partnerLookup = args.get("PartnerLookup");
				keyFeatureNeeded = args.get("KeyFeatureNeeded");
				polarisPortalContactValue = args.get("PolarisPortalContactValue");
			} catch (Exception e) {
				this.addComment("Error while initializing OpportunityStageChange");
				throw new KDTKeywordInitException("Error while initializing OpportunityStageChange", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				eo.clickElement(driver, "xpath", "clickOppfromQuote", CURR_APP);
				this.addComment("Clicked on the opportunity link from created quote");
				// Stage change
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "xpath", "stageDoubleCick", waiTime, CURR_APP);

				eo.actionDoubleClick(driver, "xpath", "stageDoubleCick", CURR_APP);
				this.addComment("Double click on stage in order to select the required stage");

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "stage", "{link}", stage, CURR_APP);
				this.addComment("Selected the stage for opportunity as " + stage);

				eo.waitForWebElementVisible(driver, "xpath", "Okbutton", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "Okbutton", CURR_APP);
				this.addComment("Clicked on Ok button after select the required stage");
				// opp subtype
				// eo.waitForWebElementVisible(driver, "xpath", " FieldTrial", waiTime,
				// CURR_APP);
				eo.javaScriptScrollToViewElement(driver, "xpath", "oppSubTypeFieldTrial", CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "oppSubTypeField", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "oppSubTypeField", CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "oppSubTypeField", waiTime, CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "oppSubTypeField", CURR_APP);
				this.addComment("Clicked on Opportunity SubType field");

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "oppSubTypeName", "{link}", oppSubType,
						CURR_APP);
				this.addComment("Selected the Opportunity sub type from drop down as " + oppSubType);

				eo.waitForWebElementVisible(driver, "xpath", "Okbutton", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "Okbutton", CURR_APP);
				this.addComment(
						"Clicked on Ok button after select Opportunity sub type from drop down as " + oppSubType);
				// Win or lost comments
				eo.actionDoubleClick(driver, "xpath", "winLostCmt", CURR_APP);
				eo.enterText(driver, "xpath", "winLostCmtTextarea", textAreaComment, CURR_APP);
				eo.actionClick(driver, "xpath", "inputokBtn", CURR_APP);
				// Distributor and partner lookup
				eo.actionDoubleClick(driver, "xpath", "distributorLookup", CURR_APP);
				eo.enterText(driver, "xpath", "distributorLookupField", distributorLookup, CURR_APP);
				// eo.actionClick(driver, "xpath", "selectDistributorLookUp", CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("distributorLookupField", CURR_APP))).sendKeys(Keys.TAB);
				eo.actionDoubleClick(driver, "xpath", "partnerLookup", CURR_APP);				
				eo.enterText(driver, "xpath", "partnerLookupField", partnerLookup, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("partnerLookupField", CURR_APP))).sendKeys(Keys.TAB);
				// lost to won again
				eo.waitForWebElementVisible(driver, "xpath", "lostToWonAgainstField", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "lostToWonAgainstField", CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "lostToWonAgainstField", CURR_APP);
				this.addComment("Double clicked on Lost To Won Against field");
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "lostToWonAgainstDropdownValue", "{link}",
						lostToWonAgainst, CURR_APP);
				this.addComment("Selected the Lost To/Won Against as" + lostToWonAgainst);
				// Primary competitor
				eo.clickElement(driver, "xpath", "primaryCmptrField", CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "primaryCmptrField", CURR_APP);
				eo.selectComboBoxByValue(driver, "xpath", "selectPrimaryCmptr", primarycompetitor.trim(), CURR_APP);
				// cloudEnvironmentField
				eo.clickElement(driver, "xpath", "cloudEnvironmentField", CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "cloudEnvironmentField", CURR_APP);
				this.addComment("Double clicked on cloud Environment Field");
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "cloudEnvironmentDropdownValue", "{link}",
						"Azure", CURR_APP);
				this.addComment("Selected the cloud EnvironmentField as " + "Azure");
				// Compelling Bus Driver
				eo.actionDoubleClick(driver, "xpath", "compellingBusDriver", CURR_APP);
				eo.selectComboBoxByVisibleText(driver, "xpath", "selectCompellingBusdriver", compellingBusDriver,
						CURR_APP);
				// Key feature needed
				eo.actionDoubleClick(driver, "xpath", "keyFeatureNeeds", CURR_APP);
				eo.selectComboBoxByVisibleText(driver, "xpath", "selectKeyfeatureNeed", keyFeatureNeeded, CURR_APP);
				eo.actionClick(driver, "xpath", "keyFeatureNeedsOK", CURR_APP);
				// Polaris region
				eo.waitForWebElementVisible(driver, "xpath", "polarisRegionField", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "polarisRegionField", CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "polarisRegionField", CURR_APP);
				this.addComment("Double clicked on Polaris Region field");
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "polarisRegionDropdownValue", "{link}",
						polarisRegion, CURR_APP);
				this.addComment("Selected the Polaris Region as " + polarisRegion);
				//// PartnerLevel of involvement
				eo.waitForWebElementVisible(driver, "xpath", "partnelLevelOfInvolvement", waiTime, CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "partnelLevelOfInvolvement", CURR_APP);
				this.addComment("Double clicked on Partner Level Of Involvement");
				// eo.clickElementAfterReplacingKeyValue(driver, "xpath",
				// "partnelLevelOfInvolvementDropdown", "{link}",partnerLevelOfInvolvement,
				// CURR_APP);
				eo.selectComboBoxByVisibleText(driver, "xpath", "partnelLevelOfInvolvementDropdown",
						partnerLevelOfInvolvement.trim(), CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("partnelLevelOfInvolvementDropdown", CURR_APP)))
						.sendKeys(Keys.ENTER);
				this.addComment(
						"Selected the Partner Level Of Involvement from drop down as " + partnerLevelOfInvolvement);
				// eo.wait(2);
				// polaris portal contact
				System.out.print("lastName::::" + lastName);
				eo.javaScriptScrollToViewElement(driver, "xpath", "oppHandover", CURR_APP);
				// eo.clickElement(driver, "xpath", "polarisPortalContactField", CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "polarisPortalContactField", CURR_APP);
				// eo.actionDoubleClick(driver, "xpath", "polarisPortalContactField", CURR_APP);
				// this.addComment("Double clicked on Polaris PortalContactField");
				//if(! hasArgs(polarisPortalContactValue))
					polarisPortalContactValue = lastName;
				
				SeleniumTools.waitForWebElementVisible(driver, By.xpath(gei.getProperty("polarisPortalContactValue", CURR_APP)), waiTime);
				eo.enterText(driver, "xpath", "polarisPortalContactValue", polarisPortalContactValue, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("polarisPortalContactValue", CURR_APP))).sendKeys(Keys.TAB);
				// eo.actionClick(driver, "xpath", "polarisAutomatch", CURR_APP);
				this.addComment("Selected the polarisPortalContactValue");
				// Installation assigned to
				eo.waitForWebElementVisible(driver, "xpath", "installationAssignedToField", waiTime, CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "installationAssignedToField", CURR_APP);
				this.addComment("Double clicked on Installation assigned to field");
				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "installationAssignedToDropdownvalue",
						"{link}", installationAssignedTo, waiTime, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "installationAssignedToDropdownvalue", "{link}",
						installationAssignedTo, CURR_APP);
				this.addComment("Selected the Installation Assigned to from drop down as " + installationAssignedTo);
				eo.javaScriptScrollToViewElement(driver, "xpath", "currentState", CURR_APP);
				// Current state
				eo.actionDoubleClick(driver, "xpath", "currentState", CURR_APP);
				eo.enterText(driver, "xpath", "currentStatusTextArea", textAreaComment, CURR_APP);
				eo.actionClick(driver, "xpath", "inputokBtn", CURR_APP);
				// Negative Consequences
				eo.actionDoubleClick(driver, "xpath", "negativeConseq", CURR_APP);
				eo.enterText(driver, "xpath", "negativeConseqTextArea", textAreaComment, CURR_APP);
				eo.actionClick(driver, "xpath", "inputokBtn", CURR_APP);
				// Future State
				eo.actionDoubleClick(driver, "xpath", "futureState", CURR_APP);
				eo.enterText(driver, "xpath", "futureStateTextArea", textAreaComment, CURR_APP);
				eo.actionClick(driver, "xpath", "inputokBtn", CURR_APP);
				// Positive Business Outcomes
				eo.actionDoubleClick(driver, "xpath", "positiveBusinessOutCome", CURR_APP);
				eo.enterText(driver, "xpath", "positiveBusinessOutComeTextArea", textAreaComment, CURR_APP);
				eo.actionClick(driver, "xpath", "inputokBtn", CURR_APP);

				/////////////////////////////////////////////////////////////////////////////////////////
				eo.javaScriptScrollToViewElement(driver,
						driver.findElement(By.xpath(gei.getProperty("saveOpportunity", CURR_APP))));

				eo.waitForWebElementVisible(driver, "xpath", "saveOpportunity", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "saveOpportunity", CURR_APP);
				
				eo.waitForPopUp(driver);
				
				this.addComment("Saved the opportinity after stage change from 1 to 6");
				
				
				
				
				SeleniumTools.waitForWebElementVisible(driver,
						By.xpath(gei.getProperty("verifyOrderCount", CURR_APP)), waiTime);

				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				
				
				eo.waitForWebElementVisible(driver, "xpath", "verifyOrderCount", waiTime, CURR_APP);

				// Verification

				String ActualCountOfOrder = eo.getText(driver, "xpath", "verifyOrderCount", CURR_APP);

				String ExpectedCountOfOrder = "[1]";

				if (ActualCountOfOrder.equals(ExpectedCountOfOrder)) {
					this.addComment("Verified the order is created");

				} else {

					this.addComment("Failed to verify the order creation");
					throw new KDTKeywordInitException("Failed to verify the order creation");

				}

			}

			catch (Exception e) {

				this.addComment("User is Unable to Create the order by changing the opportunity status");
				throw new KDTKeywordExecException(
						"User is Unable to Create the order by changing the opportunity status", e);

			}

		}
	}

	////////////////////////////// Create opportunity///////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateOpportunity</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to Create Opportunity.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>SelectSubTab</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>AccountName(optional): The account name need to be verified</li>
	 * <li>DealInitiated(Mandatory): The Deal Initiated need to be selected</li>
	 * <li>Stage(optional): The stage need to be verified</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateOpportunity extends Keyword {
		
		private String dealinitiated;
		private String stage;
		private String accountType;
		

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {

				// accountName = args.get("AccountName");
				dealinitiated = args.get("DealInitiated");
				stage = args.get("Stage");

			

			} catch (Exception e) {
				this.addComment("Error while creating new opportunity");
				throw new KDTKeywordInitException("Error while creating new opportunity", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
				addComment("Create oppertunity started");

				eo.waitForPageload(driver);
		

				  eo.javaScriptScrollToViewElement(driver, "xpath", "accountname_On_Oppertunity", CURR_APP);
				  String accountname = eo.getAttribute(driver, "xpath", "accountname_On_Oppertunity", "value", CURR_APP);


				if (accountname.contains(context.getData().get("$expectedAccountName"))) {
					addComment("Successfully verified the account name is <b>"
							+ context.getData().get("$expectedAccountName") + "</b>");
				} else {
					addComment("Expected account name  is " + context.getData().get("$expectedAccountName"));
					addComment("Actual account name  is " + accountname);
					throw new KDTKeywordExecException("Actual and Expected account name are not same");
				}

			
				String stagename = eo.getAttribute(driver, "xpath", "opportunity_stage", "value", CURR_APP);
				
				if (stagename.contains(stage)) {
					addComment("Successfully verified the stage name is <b>" + stage + "</b>");
				} else {
					addComment("Expected stage name  is " + stage);
					addComment("Actual stage name  is " + stagename);
					throw new KDTKeywordExecException("Actual and Expected stage name are not same");
				}

				String opprtunityname = eo.getAttribute(driver, "xpath", "opportunity_name", "value", CURR_APP);

				//////////////// operation////////////////////////////////

				eo.javaScriptScrollToViewElement(driver, "id", "page_scroll", CURR_APP);
				this.addComment("Successfully scrolled opportunity page");
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "dealInitiated_type", "{link}", dealinitiated,CURR_APP);
				this.addComment("Selected the dealinitated type  from drop down list");
				
				
				if (hasArgs("AccountType")) {

					accountType = args.get("AccountType");

				if(accountType.equalsIgnoreCase("Partner/Distributor"))
				{
				
				    // partner lookup
				    
					 eo.javaScriptScrollToViewElement(driver, "xpath", "partnerLookupFieldCreateoppty", CURR_APP);
					 eo.enterText(driver, "xpath", "partnerLookupFieldCreateoppty",accountname , CURR_APP);
				     driver.findElement(By.xpath(gei.getProperty("partnerLookupFieldCreateoppty", CURR_APP))).sendKeys(Keys.TAB);

					
				}
				else {
					;
				}
				}
				
       
				eo.clickElement(driver, "xpath", "save_opportunity", CURR_APP);
				this.addComment("Saved the created opportunity");

				eo.waitForPageload(driver);

				// Capturing Opportunity Id from URL

				String currUrl = driver.getCurrentUrl();
				String ar[] = currUrl.split("/");
				String opptyId = ar[3];
				addComment("Successfully Created the opportunity with opportunityID:" + "<b>" + opptyId + "</b");

				eo.clickElement(driver, "xpath", "details_tab", CURR_APP);

				////////// verification part///////

                eo.waitForWebElementVisible(driver, "xpath", "opportunityaftersave", 500, CURR_APP);
                
				if (eo.getText(driver, "xpath", "opportunityaftersave", CURR_APP).equalsIgnoreCase(opprtunityname)) {
					this.addComment("The opportunity name is verified");
				}

				else {
					this.addComment("The created opportunity name  failed to verify");
					throw new KDTKeywordInitException("The created opportunity name  failed to verify");
				}

			}

			catch (Exception e) {

				this.addComment("User is Unable to Create opportunity");
				throw new KDTKeywordExecException("User is Unable Create opportunity", e);

			}

		}

	}

	//////////////////////////////////// CreateAndSubmitQuote/////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateAndSubmitQuote</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to create a quote and submit
	 * the quote</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesForce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>GuidedSellingTypeAndOptions(Mandatory) pass the guided selling popup
	 * option as ;seperate value</li>
	 * <li>ApplianceProduct(Mandatory) pass the appliance product name which need to
	 * be configured</li>
	 * <li>MinTerm(Mandatory) pass the minimum subscription term to verify error
	 * message</li>
	 * <li>MaxTerm(Mandatory) pass the maximum subscription term to save the
	 * quote</li>
	 * <li>Accessories(optional) pass the Accessories product ,if required by
	 * ,seperated value</li>
	 * <li>Support(optional) pass the Support product ,if required by ,seperated
	 * value</li>
	 * <li>Spare(optional) pass the Spare product ,if required by ,seperated
	 * value</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash</i></b>
	 * </p>
	 * </div>
	 */
	public static class CreateAndSubmitQuote extends Keyword {

		private String minterm = "";
		private String maxterm = "";
		private String approve;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			minterm = args.get("MinTerm");
			maxterm = args.get("MaxTerm");
			approve = args.get("Approve");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			if(eo.isExists(driver, "xpath", "save_product_config", CURR_APP)) {
				eo.clickElement(driver, "XPATH", "save_product_config", CURR_APP);
			}	
			eo.waitForPopUp(driver);
			
			if(eo.isExists(driver, "xpath", "save_product_config", CURR_APP)) {
				eo.clickElement(driver, "XPATH", "save_product_config", CURR_APP);
			}
			eo.waitForPopUp(driver);
			
			eo.wait(3);

			String quoteTotal = eo.getText(driver, "XPATH", "quote_total", CURR_APP);
			this.addComment("The Quote total of the created quote is "+quoteTotal);
			String lineSubTotal = eo.getText(driver, "XPATH", "line_subTotal", CURR_APP);
			if (quoteTotal.equalsIgnoreCase(lineSubTotal)) {
				addComment("Quote total is <b>" + quoteTotal + "</b>");
				addComment("Line sub  total is <b>" + lineSubTotal + "</b>");
				addComment("Sussessfully verified quote total and line sub total are same ");
			} else {
				addComment("Quote total is <b>" + quoteTotal + "</b>");
				addComment("Line sub  total is <b>" + lineSubTotal + "</b>");
				throw new KDTKeywordExecException("Quote total and line sub total are not same");
			}
			
/*			eo.enterText(driver, "XPATH", "subscription_Term", minterm, CURR_APP);
			SeleniumTools.waitForWebElementToClickable(driver, By.xpath(gei.getProperty("quote_save_btn", CURR_APP)),waiTime);
			
			eo.clickElement(driver, "XPATH", "quote_save_btn", CURR_APP);
			eo.waitForWebElementVisible(driver, "XPATH", "subscription_Term", waiTime, CURR_APP);
			addComment("Successfully verified the error message");
			eo.clickElement(driver, "XPATH", "close_error_mes", CURR_APP);
			eo.clearData(driver, "XPATH", "subscription_Term", CURR_APP);*/
			
			
			eo.enterText(driver, "XPATH", "subscription_Term", maxterm, CURR_APP);
			eo.waitForPopUp(driver);
			
			SeleniumTools.waitForWebElementVisible(driver, By.xpath(gei.getProperty("quote_calculate_btn",CURR_APP)), waiTime);
			eo.clickElement(driver, "XPATH", "quote_calculate_btn", CURR_APP);
			eo.waitForPopUp(driver);
			eo.waitForPopUp(driver);
			
			if(eo.isExists(driver, "xpath", "errorMsg", CURR_APP)) {
            	eo.clickElement(driver, "xpath", "errorCloseBtn", CURR_APP);
            }
			eo.waitForPopUp(driver);
			eo.waitForPopUp(driver);
			
			eo.clickElement(driver, "XPATH", "quote_save_btn", CURR_APP);
			addComment("Succesfully save the quote");
			eo.wait(5);
			eo.waitForPopUp(driver);
			eo.waitForPopUp(driver);
			if(eo.isExists(driver, "xpath", "continue_btn", CURR_APP)) {
				
				try {

					eo.waitForWebElementVisible(driver, "XPATH", "continue_btn", waiTime, CURR_APP);
					eo.clickElement(driver, "XPATH", "continue_btn", CURR_APP);
					addComment("Succesfully save the quote");

				} catch (Exception e) {
					
					addComment("No continue button present");
				}
			}
			
			eo.waitForPopUp(driver);
			eo.waitForWebElementVisible(driver, "XPATH", "quote_status", waiTime, CURR_APP);
			String quotestaus = eo.getText(driver, "XPATH", "quote_status", CURR_APP).trim();
			if (quotestaus.contains("Draft Quote")) {
				addComment("Successfully verified the quote staus is <b>" + quotestaus + "</b>");
			} else {
				addComment("Quote staus is <b>" + quotestaus + "</b>");
				throw new KDTKeywordExecException("quote staus is not verifed as <b>Draft Quote</b>");
			}
			eo.wait(3);
			eo.waitForWebElementVisible(driver, "XPATH", "quote_submit_btn", waiTime, CURR_APP);
			eo.clickElement(driver, "XPATH", "quote_submit_btn", CURR_APP);
			addComment("Successfully click on a submit approval button");
			eo.wait(5);
			
			for(int i = 0; i < 5; i++) {
				if (!eo.getText(driver, "XPATH", "quote_status", CURR_APP).contains("Locked Quote")) {
					eo.wait(10);
					driver.navigate().refresh();
				}
				else if (eo.getText(driver, "XPATH", "quote_status", CURR_APP).contains("Draft Quote")) {
					break;
				}
			}
			
			if (eo.getText(driver, "XPATH", "quote_status", CURR_APP).contains("Locked Quote")) {

				eo.wait(5);
				eo.javaScriptScrollToViewElement(driver, "xpath", "azureMarketPlace2", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "azureMarketPlace2", waiTime, CURR_APP);

				String ele = driver.findElement(By.xpath(gei.getProperty("azureMarketPlace2", CURR_APP)))
						.getAttribute("title");

				if (ele.equalsIgnoreCase("Checked")) {

					eo.waitForWebElementVisible(driver, "xpath", "approvalText", waiTime, CURR_APP);

					eo.javaScriptScrollToViewElement(driver, "xpath", "approvalText", CURR_APP);

					if (eo.getText(driver, "xpath", "approvalText", CURR_APP)
							.equalsIgnoreCase("Azure Marketplace Approval")) {

						this.addComment("Successfully verified the Approval is" + "<b>" + "Azure Marketplace Approval"
								+ "</b>");

						eo.clickElement(driver, "xpath", "approvalLink", CURR_APP);
						this.addComment("Successfully clicked the Approval link");
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "approveButton", "{link}", approve,
								CURR_APP);
						this.addComment("Sucessfully clicked on the Approve button");
						eo.waitForPopUp(driver);

						eo.enterText(driver, "xpath", "commentsText", approve, CURR_APP);
						this.addComment("Successfully enter the text in comments section");

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "approveButton", "{link}", approve,
								CURR_APP);
						this.addComment("Successfully approved");
					}

				} else {
					eo.clickElement(driver, "xpath", "editQuoteBtn", CURR_APP);
					eo.waitForPageload(driver);
					eo.selectComboBoxByVisibleText(driver, "xpath", "quoteApprovalStatus", "Approved", CURR_APP);
					eo.clickElement(driver, "XPATH", "account_save", CURR_APP);
					addComment("Saved Quote again");
					eo.waitForPageload(driver);

				}

			}
			eo.wait(3);
			eo.waitForWebElementVisible(driver, "XPATH", "quote_status", waiTime, CURR_APP);
			String submitquotestaus = eo.getText(driver, "XPATH", "quote_status", CURR_APP);

			if (submitquotestaus.contains("Approved Quote")) {
				addComment("Successfully verified the quote staus is <b>" + submitquotestaus + "</b>");
			} else {
				addComment("Quote staus is <b>" + submitquotestaus + "</b>");
				throw new KDTKeywordExecException("quote staus is not verifed as <b>Approved Quote</b>");
			}
		}

	}

	//////////////////////////////////////// VerifyAndSubmitOrder//////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyAndSubmitOrder</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to verify and submit
	 * order.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>OpenOrder</li>
	 * <li>ModifyOrder</li>
	 * <li>SubmitOrder</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ShippingCountry(Mandatory): To select shipping country</li>
	 * <li>ShippingState(Mandatory): To select shipping state</li>
	 * <li>EstDeliveryDate(Mandatory): To enter estimated delivery date</li>
	 * <li>CurrShipmentDate(Mandatory): To enter current shipping date</li>
	 * <li>ShippingCarrier(Mandatory): To select shipping Carrier</li>
	 * <li>ServiceLevel(Mandatory): To select service level</li>
	 * <li>ContactName(Mandatory): To select Contact name</li>
	 * <li>PODate(Mandatory): To Enter PO date</li>
	 * <li>PoNumber(Mandatory): To Enter PO number</li>
	 * <li>ShipContactName(Mandatory): To Enter Shipping contact name</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class SubmitTheOrder extends Keyword {

		private String shippingCountry;
		private String shippingState;
		private String shippingcarrier;
		private String servicelevel;
		private String shipcontactname;
		private String ponumber;
		private String shippingcity;
		private String shippingstreet;
		private String shippingzipcode;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			try {
				
				shippingCountry = args.get("ShippingCountry");
				shippingState = args.get("ShippingState");
				shippingcarrier = args.get("ShippingCarrier");
				servicelevel = args.get("ServiceLevel");
				ponumber = args.get("PoNumber");
				shipcontactname = args.get("ShipContactName");
				shippingcity = args.get("ShippingCity");
				shippingstreet = args.get("ShippingStreet");
				shippingzipcode = args.get("ShippingZipcode");
			} catch (Exception e) {
				this.addComment("Error while creating new Order");
				throw new KDTKeywordInitException("Error while creating new Order", e);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();
				addComment("Create Order started");

				// Verifying OrderStatus field

				if (eo.getText(driver, "xpath", "order_status", CURR_APP).trim().equalsIgnoreCase("Pending")) {
					this.addComment("The order status is Pending verified");
				} else {
					this.addComment("The order status does not match with expected result");
					throw new KDTKeywordInitException("The order status does not match with expected result");
				}

				eo.actionDoubleClick(driver, "xpath", "Po_number1", CURR_APP);
				eo.enterText(driver, "xpath", "po_numberAfterclick", ponumber, CURR_APP);
				this.addComment("Entered Po number details");
				//eo.clickElement(driver, "xpath", "enduser_ponumber", CURR_APP);
				
				
				eo.actionDoubleClick(driver, "xpath", "enduser_ponumber", CURR_APP);
				eo.enterText(driver, "xpath", "enduser_poAfterclick", ponumber, CURR_APP);
				this.addComment("Entered EnduserPO number details");

				//eo.clickElement(driver, "xpath", "Po_date", CURR_APP);
				
				eo.actionDoubleClick(driver, "xpath", "Po_date", CURR_APP);
				eo.enterText(driver, "xpath", "Po_dateAfterclick", eo.generateCurrDateWithoutTime(), CURR_APP);
				this.addComment("Entered PO date details");

				eo.clickElement(driver, "xpath", "ship_contact", CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "ship_contact", CURR_APP);
				eo.enterText(driver, "xpath", "ship_contactAfterclick", shipcontactname, CURR_APP);
				this.addComment("Entered ship contact details");

				eo.actionDoubleClick(driver, "xpath", "shipping_carrier", CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "shipping_carrierdropdown", "{link}",
						shippingcarrier, CURR_APP);
				this.addComment("Selected shiping carrier");
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "service_lavel", "{link}", servicelevel,
						CURR_APP);
				this.addComment("Selected servicelevel ");
				eo.clickElement(driver, "xpath", "Okbutton", CURR_APP);

				eo.javaScriptScrollToViewElement(driver, "xpath", "shipping_address", CURR_APP);
				eo.actionDoubleClick(driver, "xpath", "shipping_address", CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "shipping_country", "{link}", shippingCountry,
						CURR_APP);
				this.addComment("Selected the shippingcountry from drop down list");
				eo.enterText(driver, "xpath", "shipping_street", shippingstreet, CURR_APP);
				this.addComment("Entered shipping street");
				eo.enterText(driver, "xpath", "shipping_city", shippingcity, CURR_APP);
				this.addComment("Entered shipping city");
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "shipping_state", "{link}", shippingState,
						CURR_APP);
				this.addComment("Selected the shippingState from drop down list");
				eo.enterText(driver, "xpath", "shipping_zipcode", shippingzipcode, CURR_APP);
				this.addComment("Entered the city zipcode");
				eo.clickElement(driver, "xpath", "Okbutton", CURR_APP);

				eo.clickElement(driver, "xpath", "save_order", CURR_APP);
				this.addComment("Order saves successfully");
				eo.clickElement(driver, "xpath", "submitOrder_button", CURR_APP);
				eo.wait(10);
				driver.switchTo().alert().accept();
				this.addComment("Order Sumitted Successfully");

			} catch (Exception e) {
				this.addComment("User is Unable to Submit Order");
				throw new KDTKeywordExecException("User is Unable to Submit Order", e);
			}
		}
	}

	/////////////////////////////////////// OpenOrderSalesforceAndVerifyStatus/////////////////////////////////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpenOrderSalesforceAndVerifyStatus</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to open and verify order
	 * status</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>Search</li>
	 * <li>Verify</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>orderstatus(Mandatory): To verify order status</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class OpenOrderSalesforceAndVerifyStatus extends Keyword {

		private String ordernumber;

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				ordernumber = context.getData().get("$orderNo");

				addComment("Search for the order " + ordernumber);

				eo.waitForPageload(driver);

				eo.enterText(driver, "xpath", "search_homepage", ordernumber, CURR_APP);
				this.addComment("Searching for order from home page");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);
				eo.clickElement(driver, "xpath", "ordrrs_count_link", CURR_APP);

				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "searched_order", "{link}", ordernumber, 20,
						CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "searched_order", "{link}", ordernumber,
						CURR_APP);

				this.addComment("selected filtered order");
				eo.waitForPageload(driver);

				if (eo.getText(driver, "xpath", "orderdetail_text", CURR_APP).equalsIgnoreCase("Order Detail")) {
					this.addComment("User at Order Detail page");
				} else {
					this.addComment("User not at Order Detail page");
					throw new KDTKeywordInitException("User not at Order Detail page");
				}

				eo.javaScriptScrollToViewElement(driver, "xpath", "fulfillment_info", CURR_APP);

				////// Wait for Status change Sync to Sales force/////

				int waiTime1 = Integer.parseInt(gei.getProperty("waitTime1", CURR_APP));

				int count = 0;
				int i;
				for (i = 0; i < 50; i++) {
					driver.navigate().refresh();

					try {

						eo.wait(waiTime1);
						eo.waitForWebElementVisible(driver, "XPATH", "contractedCheck", waiTime1, CURR_APP);
						if (eo.getAttribute(driver, "xpath", "contractedCheck", "title", CURR_APP)
								.equalsIgnoreCase("Checked")) {

							this.addComment("The order status has been updated successfully");
							break;
						}

						else {
							count++;
						}

						if (count == i) {
							throw new KDTKeywordExecException("Status does not changes Waited for 8 minutes ");
						}

					} catch (Exception e) {
						addComment("The order status is not updated and order is not synced to sales force");

					}

				}

				if (eo.getText(driver, "xpath", "order_status", CURR_APP).equalsIgnoreCase("Shipped")) {
					this.addComment("The order status is verified");
				} else {
					this.addComment("The order status does not match with expected result");
					throw new KDTKeywordInitException("The order status does not match with expected result");
				}

			}

			catch (Exception e) {

				this.addComment("User is Unable to Search Order");
				throw new KDTKeywordExecException("User is Unable to Search Order", e);

			}

		}

	}

	//////////////////////////////////////////////// Verify Service
	//////////////////////////////////////////////// Contract////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ServiceContract</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to open and verify Service
	 * Contract</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>Search</li>
	 * <li>Verify</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>orderstatus(Mandatory): To verify order status</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyServiceContract extends Keyword {

		private String orderNumber;

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
				addComment("Search Service Contract");
				eo.waitForPageload(driver);

				orderNumber = context.getData().get("$orderNo");

				eo.enterText(driver, "xpath", "search_homepage", orderNumber, CURR_APP);
				this.addComment("Searching for order from home page");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);
				eo.clickElement(driver, "xpath", "ordrrs_count_link", CURR_APP);

				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "searched_order", "{link}", orderNumber, 20,
						CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "searched_order", "{link}", orderNumber,
						CURR_APP);

				this.addComment("selected filtered order");
				eo.waitForPageload(driver);

				eo.javaScriptScrollToViewElement(driver, "xpath", "new_servicecontract", CURR_APP);
				eo.clickElement(driver, "xpath", "service_contractname", CURR_APP);
				this.addComment("Click on Service Contract Link");

				eo.waitForWebElementVisible(driver, "XPATH", "verify_servicecontractText", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "verify_servicecontractText", CURR_APP)
						.equalsIgnoreCase("Service Contract Detail")) {
					this.addComment("User at Service contract detail page");
				} else {
					this.addComment("User not at Service contract detail page");
					throw new KDTKeywordExecException("User not at Service contract detail page");
				}
				eo.javaScriptScrollToViewElement(driver, "xpath", "verify_entitlement", CURR_APP);

				if (eo.getText(driver, "xpath", "verify_entitlement2", CURR_APP).equalsIgnoreCase("RBK-SVC-PREM-HW")) {
					this.addComment("Entitlement attached verified");
				} else {
					this.addComment("No entitkements available");
					throw new KDTKeywordInitException("No entitkements available");
				}

				List<WebElement> contractlist = driver
						.findElements(By.xpath(gei.getProperty("contractline_Product", CURR_APP)));
				List<WebElement> entitlementlist = driver
						.findElements(By.xpath(gei.getProperty("entitelmentlist_products", CURR_APP)));
				if (contractlist.size() == entitlementlist.size()) {

					for (int i = 0; i < contractlist.size(); i++) {
						if (contractlist.get(i).getText().equalsIgnoreCase(entitlementlist.get(i).getText())) {

							this.addComment("Entitlement andEntitlement Product list items matching and verified");
						} else {
							this.addComment("Entitlement and Product list items not matching");
							throw new KDTKeywordInitException("Entitlement and Product list items not matching");
						}
					}
				} else {
					this.addComment("Entitlement and Product list size not matching");
					throw new KDTKeywordInitException("Entitlement and Product list size not matching");
				}

				List<WebElement> contractstartdate = driver
						.findElements(By.xpath(gei.getProperty("start_date_contractlist", CURR_APP)));
				List<WebElement> entitlementstartdate = driver
						.findElements(By.xpath(gei.getProperty("start_date_entitlement", CURR_APP)));
				if (contractstartdate.size() == entitlementstartdate.size()) {

					for (int i = 0; i < contractlist.size(); i++) {
						if (contractstartdate.get(i).getText()
								.equalsIgnoreCase(entitlementstartdate.get(i).getText())) {

							this.addComment("Entitlement and  Product list start date  matching and verified");
						} else {
							this.addComment("Entitlement and Product start date  not matching");
							throw new KDTKeywordInitException("Entitlement and Product start date  not matching");
						}
					}
				} else {
					this.addComment("Entitlement and Product start date  not matching");
					throw new KDTKeywordInitException("Entitlement and Product start date  not matching");
				}

				List<WebElement> contractenddate = driver
						.findElements(By.xpath(gei.getProperty("end_date_contractlist", CURR_APP)));
				List<WebElement> entitlementenddate = driver
						.findElements(By.xpath(gei.getProperty("end_date_entitelmet", CURR_APP)));
				if (contractenddate.size() == entitlementenddate.size()) {

					for (int i = 0; i < contractlist.size(); i++) {
						if (contractenddate.get(i).getText().equalsIgnoreCase(entitlementenddate.get(i).getText())) {

							this.addComment("Entitlement and  Product list end date  matching and verified");
						} else {
							this.addComment("Entitlement and Product end date  not matching");
							throw new KDTKeywordInitException("Entitlement and Product end date  not matching");
						}
					}
				} else {
					this.addComment("Entitlement and Product end date  not matching");
					throw new KDTKeywordInitException("Entitlement and Product end date  not matching");
				}

			} catch (Exception e) {

				this.addComment("User is Unable to Verify Service Contract");
				throw new KDTKeywordExecException("User is Unable to Verify Service Contract", e);

			}

		}
	}

	////////////////////////////// Asset
	////////////////////////////// Creation/////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateAsset</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to create asset</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>productID(Mandatory)</li>
	 * <li>serialNumber(Mandatory)</li>
	 * <li>accountName(Optional)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateAsset extends Keyword {

		private String productID;
		// private String serialNumber;
		private String accountName;
		// private String mentionDeleteorCreate;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				// verifyArgs("ProductID", "SerialNumber");
				verifyArgs("ProductID");
				productID = args.get("ProductID");

				// serialNumber = args.get("SerialNumber");
				accountName = args.get("AccountName");

			} catch (Exception e) {
				this.addComment("Error while initializing CreateAsset");
				throw new KDTKeywordInitException("Error while initializing CreateAsset", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// remove this code///
				String[] serialNumberList = new String[2];
				Date dNow = new Date();
				SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
				String datetime1 = ft.format(dNow);

				Date dNoww = new Date();
				SimpleDateFormat ftw = new SimpleDateFormat("yyMMddhhmmss");
				String datetime2 = ftw.format(dNoww);

				serialNumberList[0] = datetime1;
				serialNumberList[1] = datetime2;

				/// till here///

				String[] productList = productID.split(";");
				// uncomment this code
				// String[] serialNumberList = serialNumber.split(";");

				int productListSize = productList.length;
				// int seralNumberListSize = serialNumberList.length;

				System.out.println(productListSize);
				System.out.println(Arrays.toString(productList));
				System.out.println(Arrays.toString(serialNumberList));

				for (int i = 0; i < productListSize; i++) {
					eo.waitForWebElementVisible(driver, "XPATH", "allTabArrow", waiTime, CURR_APP);
					eo.javaScriptClick(driver, "xpath", "allTabArrow", CURR_APP);
					this.addComment("Clicked on All tab arrow from the home page");
					eo.waitForPageload(driver);

					eo.waitForWebElementVisible(driver, "XPATH", "assetLink", waiTime, CURR_APP);
					eo.clickElement(driver, "xpath", "assetLink", CURR_APP);
					this.addComment("Clicked on Asset link");
					eo.waitForPageload(driver);

					eo.clickElement(driver, "xpath", "assetNewButton", CURR_APP);
					this.addComment("Clicked on New Asset button");
					eo.waitForPageload(driver);

					eo.enterText(driver, "xpath", "assetNameField", serialNumberList[i], CURR_APP);
					this.addComment("Entered the Asset name as " + serialNumberList[i]);

					eo.enterText(driver, "xpath", "serialNumber1Field", serialNumberList[i], CURR_APP);
					this.addComment("Entered the Serial Number1 as " + serialNumberList[i]);

					eo.enterText(driver, "xpath", "serialNumber2Field", serialNumberList[i], CURR_APP);
					this.addComment("Entered the Serial Number2 as " + serialNumberList[i]);

					eo.enterText(driver, "xpath", "productField", productList[i], CURR_APP);
					this.addComment("Entered the Product field as " + productList[i]);

					eo.enterText(driver, "xpath", "accountField", accountName, CURR_APP);
					this.addComment("Entered account name as " + accountName);

					eo.clickElement(driver, "xpath", "saveAssetButton", CURR_APP);
					this.addComment("Save the asset named " + serialNumberList[i]);

					eo.waitForWebElementVisible(driver, "xpath", "assetNameafterSave", waiTime, CURR_APP);

					String ActualAsset = eo.getText(driver, "xpath", "assetNameafterSave", CURR_APP);
					String ExpectedAsset = serialNumberList[i];

					if (ActualAsset.equalsIgnoreCase(ExpectedAsset)) {
						this.addComment("The asset is created successfully");

					} else {

						this.addComment("The created Asset ID is not matching with the expected");
						throw new KDTKeywordExecException("The created Asset ID is not matching with the expected");
					}
				}

				eo.clickElement(driver, "xpath", "home_tab", CURR_APP);
				eo.waitForPageload(driver);
			} catch (Exception e) {

				this.addComment("User is unable to create the asset");
				throw new KDTKeywordExecException("User is unable to create the asset", e);

			}

		}
	}

	public static class DeleteAsset extends Keyword {

		private String assetName;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("AssetName");
				assetName = args.get("AssetName");

			} catch (Exception e) {
				this.addComment("Error while initializing DeleteAsset");
				throw new KDTKeywordInitException("Error while initializing DeleteAsset", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				String[] assetNameList = assetName.split(";");
				int assetNameListSize = assetNameList.length;

				eo.waitForWebElementVisible(driver, "XPATH", "allTabArrow", waiTime, CURR_APP);
				eo.javaScriptClick(driver, "xpath", "allTabArrow", CURR_APP);
				this.addComment("Clicked on All tab arrow from the home page");
				eo.waitForPageload(driver);

				eo.waitForWebElementVisible(driver, "XPATH", "assetLink", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "assetLink", CURR_APP);
				this.addComment("Clicked on Asset link");
				eo.waitForPageload(driver);

				for (int i = 0; i < assetNameListSize; i++) {

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "assetNeedToDelete", "{link}",
							assetNameList[i], CURR_APP);
					this.addComment("Clicked on asset to be deleted " + assetNameList[i]);
					eo.waitForPageload(driver);

					eo.waitForWebElementVisible(driver, "xpath", "deleteAssetButton", waiTime, CURR_APP);

					eo.clickElement(driver, "xpath", "deleteAssetButton", CURR_APP);
					this.addComment("Clicked on Delete asset button for asset " + assetNameList[i]);

					WebDriverWait wait = new WebDriverWait(driver, 10 /* timeout in seconds */);
					if (wait.until(ExpectedConditions.alertIsPresent()) == null) {

					} else {
						Alert alert = driver.switchTo().alert();
						alert.accept();
						this.addComment("Successfully deleted the asset " + assetNameList[i]);
						driver.navigate().refresh();
					}

				}

			}

			catch (Exception e) {

				this.addComment("User is Unable to delete the asset");
				throw new KDTKeywordExecException("User is Unable to delete the asset", e);

			}

		}
	}
	//////////////////////////////////// Suggest_GuidedSelling/////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>Suggest_GuidedSelling</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to suggest the Guided selling
	 * product</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesForce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>GuidedSellingTypeAndOptions(Mandatory) pass the guided selling popup
	 * option as ;seperate value</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	public static class Suggest_GuidedSelling extends Keyword {

		private String guidedSellingOptions[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			guidedSellingOptions = args.get("GuidedSellingTypeAndOptions").split(";");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				/*
				 * // Here we are clicking on the primary check box for the quote
				 * 
				 * eo.clickElement(driver, "XPATH", "primary_checkbox", CURR_APP);
				 * addComment("Successfully clcik on the primary checkbox");
				 * eo.clickElement(driver, "XPATH", "account_save", CURR_APP);
				 * addComment("Successfully click on the Quote save button");
				 */
				eo.wait(10);

				for (int i = 0; i < guidedSellingOptions.length; i++) {

					String key = guidedSellingOptions[i].split(":")[0];

					String value = guidedSellingOptions[i].split(":")[1];

					String[] searchList = { "{guidSellingType}", "{guidSellingOption}" };
					String[] replacementList = { key, value };

					if (replacementList[0].equals("Select Network Type")
							|| replacementList[0].equals("Select Chassis Type")) {

						WebElement element = driver
								.findElement(By.xpath(gei.getProperty("guidedSelling_type_Network", CURR_APP)
										.replace("{guidSellingType}", replacementList[0])));
						Select s = new Select(element);
						s.selectByVisibleText(replacementList[1]);

					} else {

						eo.clickElementAfterReplacingMultipleKeyValue(driver, "xpath", "guidedSelling_option",
								searchList, replacementList, CURR_APP);

						addComment("Successfully selected the guided selling option<b>" + value + "</b> for the "
								+ "guided Selling type <b>" + key + "</b>");

					}
				}

				// eo.waitForWebElementVisible(driver, "XPATH", "suggestButton", waiTime,
				// CURR_APP);

				eo.clickElement(driver, "XPATH", "close_guideSelling_popup", CURR_APP);
				eo.waitForPopUp(driver);

				SeleniumTools.waitForWebElementVisible(driver,
						By.xpath(gei.getProperty("guidedSelling_button", CURR_APP)), waiTime);
				SeleniumTools.waitForWebElementToClickable(driver,
						By.xpath(gei.getProperty("guidedSelling_button", CURR_APP)), waiTime);
				eo.clickElement(driver, "XPATH", "guidedSelling_button", CURR_APP);

				eo.clickElement(driver, "XPATH", "suggestButton", CURR_APP);
				addComment("Successfully click on Suggest button");
				eo.waitForPopUp(driver);

				eo.waitForWebElementVisible(driver, "XPATH", "configure_Product", waiTime, CURR_APP);
				addComment("Successfully verified  configure product page is displayed");
				eo.waitForPopUp(driver);
			} catch (Exception e) {

				this.addComment("User is not able to suggest the Guided selling");
				throw new KDTKeywordExecException("User is not able to suggest the Guided Selling", e);

			}

		}
	}

	//////////////////////////////////// GuidedSellingPopUpUIVerification/////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>GuidedSellingPopUpUIVerification</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to Verify the Guided Selling
	 * pop up</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesForce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>"PurchaseType", "NetworkType", "ApplianceType"</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Neethu & Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class GuidedSellingPopUpUIVerification extends Keyword {
		private String purchaseType = "";
		private String networkType = "";
		private String applianceType = "";

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("PurchaseType", "NetworkType", "ApplianceType");
			purchaseType = args.get("PurchaseType");
			networkType = args.get("NetworkType");
			applianceType = args.get("ApplianceType");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();

			/*
			 * eo.waitForWebElementVisible(driver, "XPATH", "quote_edit_page", 60,
			 * CURR_APP);
			 * addComment("Successfully verified the Quote edit page is displayed");
			 * eo.clickElement(driver, "XPATH", "primary_checkbox", CURR_APP);
			 * 
			 * addComment("Successfully clcik on the primary checkbox");
			 * eo.clickElement(driver, "XPATH", "account_save", CURR_APP);
			 * addComment("Successfully click on the Quote save button");
			 */
			eo.waitForPageload(driver);
			eo.waitForWebElementVisible(driver, "XPATH", "guidedSelling_popup", 120, CURR_APP);

			eo.verifyElementIsPresent(driver, "xpath", "guidedSelling_text", CURR_APP);
			this.addComment("Successfully verified the presence of guidedSelling text ");

			// Verify Select Purchase Type:
			String[] purchase_type_list_expected = purchaseType.split(";");
			eo.waitForWebElementVisible(driver, "XPATH", "Select_purchase_Type_text", 120, CURR_APP);

			if (eo.isDisplayed(driver, "xpath", "Select_purchase_Type_text", CURR_APP)) {

				eo.clickElement(driver, "xpath", "Select_purchase_Type_dropdown", CURR_APP);

				List<WebElement> purchase_type_list = eo.getListOfWebElements(driver, "xpath",
						"Select_purchase_Type_dropdown_list", CURR_APP);
				int purchase_type_list_size = purchase_type_list.size();

				for (int i = 0; i < purchase_type_list_size; i++) {

					if (purchase_type_list.get(i).getText().equalsIgnoreCase(purchase_type_list_expected[i])) {

						this.addComment("Successfully verified the presence of Purchase Type: drop down item "
								+ purchase_type_list_expected[i]);

					}

					else {

						this.addComment("Failed to verify the presence of Purchase Type: drop down item "
								+ purchase_type_list_expected[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the presence of Purchase Type: drop down item "
										+ purchase_type_list_expected[i]);

					}

				}

			}

			// Verify Select Network Type:

			String[] network_type_list_expected = networkType.split(";");

			List<WebElement> network_type_list = eo.getListOfWebElements(driver, "xpath",
					"Select_Network_Type_dropdown_list", CURR_APP);

			int network_type_list_size = network_type_list.size() / 2;

			if (eo.isDisplayed(driver, "xpath", "Select_network_Type_text", CURR_APP)) {

				for (int i = 0; i < network_type_list_size; i++) {

					if (network_type_list.get(i).getText().equalsIgnoreCase(network_type_list_expected[i])) {

						this.addComment("Successfully verified the presence of network Type: drop down item "
								+ network_type_list_expected[i]);

					}

					else {

						this.addComment("Failed to verify the presence of network Type: drop down item "
								+ network_type_list_expected[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the presence of neywork Type: drop down item "
										+ network_type_list_expected[i]);

					}

				}

			}

			// Select Rubrik Appliance from Purchase Type

			String Select_First_Option_Purchase_Type = purchase_type_list_expected[1];
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Purchase_Type_SubItem", "{subitemName}",
					Select_First_Option_Purchase_Type, CURR_APP);

			// Verify Select Appliance Type:

			List<WebElement> appliance_type_list = eo.getListOfWebElements(driver, "xpath",
					"Select_Appliance_Type_dropdown_list", CURR_APP);

			int appliance_type_list_size = appliance_type_list.size();

			String[] appliance_type_list_expected = applianceType.split(";");

			if (eo.isDisplayed(driver, "xpath", "Select_purchase_Type_text", CURR_APP)) {

				for (int i = 0; i < appliance_type_list_size; i++) {

					if (appliance_type_list.get(i).getText().equalsIgnoreCase(appliance_type_list_expected[i])) {

						this.addComment("Successfully verified the presence of appliance Type: drop down item "
								+ appliance_type_list_expected[i]);

					}

					else {

						this.addComment("Failed to verify the presence of Appliance Type: drop down item "
								+ appliance_type_list_expected[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the presence of Appliance Type: drop down item "
										+ appliance_type_list_expected[i]);

					}

				}

			}

		}

	}

	////////////////////////// Verify_Navigate_To_ProductConfigurePage///////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>Verify_Navigate_To_ProductConfigurePage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to go to configure product
	 * page after click on product wrench icon</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesForce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>"productCode"</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyNavigateToProductConfigurePage extends Keyword {

		private String productCode;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			try {

				productCode = args.get("ProductCode");

			} catch (Exception e) {
				this.addComment("Error while initializing Verify_Navigate_To_ProductConfigurePage");
				throw new KDTKeywordInitException("Error while initializing Verify_Navigate_To_ProductConfigurePage",
						e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				SeleniumTools.waitForWebElementVisible(driver, By.xpath(
						gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", productCode)), 30);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);

				eo.actionMoveToElementAfterReplace(driver, "xpath", "product_configure_btn", "{producdCode}",
						productCode, CURR_APP);

				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "configureTooltip", "{producdCode}",
						productCode, 60, CURR_APP);

				String actual_tooltip = eo.getTextAfterReplacingKeyValue(driver, "xpath", "configureTooltip",
						"{producdCode}", productCode, CURR_APP);

				String expected_tooltip = "Configure";

				if (expected_tooltip.equalsIgnoreCase(actual_tooltip)) {
					this.addComment("The tool tip is displaying as expected " + actual_tooltip);
				} else {
					this.addComment("Failed to verify the tool tip as " + actual_tooltip);
					throw new KDTKeywordInitException("Failed to verify the tool tip as " + actual_tooltip);
				}

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_configure_btn", "{producdCode}",
						productCode, CURR_APP);

				if (SeleniumTools.waitForWebElementVisible(driver,
						By.xpath(gei.getProperty("product_code", CURR_APP).replace("{productCode}", productCode)), 60)
						&& SeleniumTools.waitForWebElementVisible(driver,
								By.xpath(gei.getProperty("configure_productpagename", CURR_APP)), 60)) {

					this.addComment("Succesfully verified product code is displayed in the Configure product page");
				}

				else {

					this.addComment("Failed to land in Configure Product page with product id as " + productCode);
					throw new KDTKeywordInitException(
							"Failed to land in Configure Product page with product id as " + productCode);
				}

			}

			catch (Exception e) {

				this.addComment("Failed to navigate Product Configuration page after click on Wrench icon");
				throw new KDTKeywordExecException(
						"Failed to navigate Product Configuration page after click on Wrench icon", e);

			}

		}

	}
	//////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SelectTabAndApplianceFilter</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to select the appliance
	 * filter e.g software,hardware,Annual</i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ProductTab(Mandatory):pass the name of tab e.g.R6000,SupportsAddon</li>
	 * <li>DropdownOptions(Optional): this is the optional argument which need to
	 * pass if want to select filters</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash</i></b>
	 * </p>
	 * </div>
	 */
	public static class SelectTabAndApplianceFilter extends Keyword {
		private String productTab = "";
		private String dropdownOptionAndValue[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("ProductTab");
			productTab = args.get("ProductTab");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			SeleniumTools.waitForWebElementToClickable(driver,
					By.xpath(gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", productTab)),
					waiTime);

			try {

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
						productTab, CURR_APP);

				this.addComment("Successfully verified the display of tab " + productTab);
			} catch (Exception e) {
				this.addComment("Failed to click on the tab " + productTab);
				throw new KDTKeywordExecException("Failed to click on the tab " + productTab, e);
			}
			this.addComment("Successfully clicked on tab " + productTab);

			eo.wait(8);
			if (hasArgs("DropdownOptions")) {
				dropdownOptionAndValue = args.get("DropdownOptions").split(";");
				this.addComment(args.get("DropdownOptions"));
				for (int i = 0; i < dropdownOptionAndValue.length; i++) {
					
					String label = dropdownOptionAndValue[i].split(":")[0].trim();
					String value = dropdownOptionAndValue[i].split(":")[1].trim();

					driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);

					try {
						if (productTab.equalsIgnoreCase("Support Add-Ons")) {
							
							
							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "filterOptionNameSupport","{label}", label, waiTime, CURR_APP);
							this.addComment("Successfully verified the display of Filter option " + label);
						}

						else {

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "filterOptionName", "{label}",label, waiTime, CURR_APP);
							this.addComment("Successfully verified the display of Filter option " + label);

						}

						eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "filterOption", "{label}", label,CURR_APP);

						String temp = gei.getProperty("filterValue", CURR_APP).replace("{value}", value);
						ExpectedConditions.visibilityOfElementLocated(By.xpath(temp.replace("{label}", label)));

						driver.findElement(By.xpath(temp.replace("{label}", label))).click();
						this.addComment("Successfully verified the display of drop down value " + value + " for option "+ label);

					}

					catch (Exception e) {
						eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "filterOption_supportAddon", "{label}",
								label, CURR_APP);
						eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "filterValue_supportAddon", "{value}",
								value, CURR_APP);
					}

					eo.waitForPopUp(driver);
					// eo.clickElement(driver, "XPATH", "configure_Product", CURR_APP);
					addComment("Succesfully selected the filter value is " + value);

				}
			}

		}

	}

	////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyAppliances</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to verify the applianced
	 * details for the individual tabs e.g R6000,Supports Add on</i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>AppliancesDetails(Mandatory):In this argument need to pass all the
	 * expected value e.g. Quantity,Product Code etc and seperate all the values
	 * based on ':' and seperate individual records ';' in datasheet</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash</i></b>
	 * </p>
	 * </div>
	 */
	public static class VerifyAppliances extends Keyword {

		private String appliances[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("AppliancesDetails");
			appliances = args.get("AppliancesDetails").split(";");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			String key = "";
			List<String> actualList = null;
			String listXpath = "";
			for (int i = 0; i < appliances.length; i++) {
				List<String> expList;
				String[] applianceDeatil = appliances[i].split(":");
				key = appliances[i].split(":")[1];
				expList = Arrays.asList(applianceDeatil);
				System.out.println(expList.size());
				try {
					actualList = new ArrayList<String>();
					listXpath = gei.getProperty("productCodeDeatils", CURR_APP).replace("{productCode}", key);
					List<WebElement> listele = driver.findElements(By.xpath(listXpath));
					for (int j = 0; j < listele.size(); j++) {
						actualList.add(listele.get(j).getText().trim());
					}
				} catch (Exception e) {
					throw new KDTKeywordExecException("unable to get the list for appliance details" + listXpath);
				}
				if (expList.size() == actualList.size()) {
					addComment("Actual appliances list is <b>" + actualList + "</b>");
					for (int j = 0; j < expList.size(); j++) {
						if (expList.get(j).trim().equals(actualList.get(j))) {
							addComment("The Expected Menu option is <b>" + expList.get(j) + "</b>"
									+ " present with actual menu list ");
						} else {
							addFailMessage(" Actual Menu option    <b>" + actualList.get(j) + "</b> is not present in "
									+ "expected list of menu's  <b>" + expList + "</b>");
						}
					}
				} else {
					throw new KDTKeywordExecException(
							"Actual List" + actualList + " And Expected List" + expList + " size is not matching");
				}
			}
			verifyAll();
		}

	}

	////////////////////////////////////// UIVerificationR6000//////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>UIVerificationR6000</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to verify </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>AppliancesDetails(Mandatory):In this argument need to pass all the
	 * expected value e.g. Quantity,Product Code etc and seperate all the values
	 * based on ':' and seperate individual records ';' in datasheet</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class UIVerificationConfigurePageSubTabs extends Keyword {

		private String tabName = "";
		private String encryptionType = "";
		private String rowcapacityType = "";
		private String columnname = "";
		private String supportType = "";
		private String paymentOptions = "";

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("EncryptionType", "RowCapacityType", "ColumnName", "SupportType", "PaymentOptions");
			encryptionType = args.get("EncryptionType");
			rowcapacityType = args.get("RowCapacityType");
			columnname = args.get("ColumnName");
			tabName = args.get("TabName");
			supportType = args.get("SupportType");
			paymentOptions = args.get("PaymentOptions");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			String[] tabNames = tabName.split(";");
			int tabNameSize = tabNames.length;

			for (int i = 0; i < tabNameSize; i++) {

				switch (tabNames[i]) {

				case "R6000":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					// Verifying the Appliance Filter text is displayed

					if (eo.isDisplayed(driver, "xpath", "appliance_filter", CURR_APP)) {
						this.addComment("The Appliance Filter text is displayed successfully in R6000 tab");

						// Verifying if the Encryption text and the drop downs are displayed

						if (eo.isDisplayed(driver, "xpath", "encryption_text", CURR_APP)) {
							this.addComment("The Encryption text is displayed in Appliance Filters section");

							// TS-REG-03_007 verification starts

							List<WebElement> encryption_list_act = eo.getListOfWebElements(driver, "xpath",
									"encryption_list", CURR_APP);

							int encryption_list_size = encryption_list_act.size();

							String[] encryptionType_list_expected = encryptionType.split(";");

							for (int j = 0; j < encryption_list_size; j++) {
								if (encryption_list_act.get(j).getText()
										.equalsIgnoreCase(encryptionType_list_expected[j])) {

									this.addComment("Verified that Encryption list contains "
											+ encryptionType_list_expected[j]);

								} else {

									this.addComment("Failed to find the " + encryptionType_list_expected[j]
											+ " in the encryption list");
									throw new KDTKeywordExecException("Failed to find the "
											+ encryptionType_list_expected[j] + " in the encryption list");
								}
							}

						}

						else {
							this.addComment("Failed to find the Encryption text in Appliance Filters section");
							throw new KDTKeywordExecException(
									"Failed to find the Encryption text in Appliance Filters section");
						}

						// Verifying of the Row Capacity and Its drop down is displyed

						if (eo.isDisplayed(driver, "xpath", "RawCapacity_text", CURR_APP)) {
							this.addComment("The Row Capacity text is displayed in Appliance Filters section");

							List<WebElement> rowcapacity_list_act = eo.getListOfWebElements(driver, "xpath",
									"rowcapacity_list", CURR_APP);

							int rowcapacity_list_size = rowcapacity_list_act.size();

							String[] rowcapacityType_list_expected = rowcapacityType.split(";");

							for (int k = 0; k < rowcapacity_list_size; k++) {
								if (rowcapacity_list_act.get(k).getText()
										.equalsIgnoreCase(rowcapacityType_list_expected[k])) {

									this.addComment("Verified that Row Capacity list contains "
											+ rowcapacityType_list_expected[k]);

								} else {

									this.addComment("Failed to find the " + rowcapacityType_list_expected[k]
											+ " in the row capacity list");
									throw new KDTKeywordExecException("Failed to find the "
											+ rowcapacityType_list_expected[k] + " in the row capacity list");
								}
							}

						}

						else {
							this.addComment("Failed to find the Raw Capacity text in Appliance Filters section");
							throw new KDTKeywordExecException(
									"Failed to find the Raw Capacity text in Appliance Filters section");
						}

					}

					else {
						this.addComment("Failed find the Appliance filter after select the product R6000");
						throw new KDTKeywordExecException(
								"Failed find the Appliance filter after select the product R6000");
					}

					try {

						String AppliancesText = eo.getText(driver, "xpath", "appliances_text", CURR_APP);
						if (eo.getText(driver, "xpath", "appliances_text", CURR_APP).equalsIgnoreCase("Appliances")) {
							verifyCoulmnName(driver, columnname, AppliancesText);
							this.addComment("Successfully verified the column names in the Appliances section of tab "
									+ tabNames[i]);
						}

					}

					catch (Exception e) {
						this.addComment(
								"Failed to verify the Column names in the Appliances section of tab " + tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Appliances section of tab " + tabNames[i]);
					}

					break;

				case "A-la-carte Subscription Add-Ons":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					// Verify the display of Account Add-Ons text

					String AccountAddsOnText = eo.getText(driver, "xpath", "accountAddsOnText", CURR_APP);

					if (eo.isDisplayed(driver, "xpath", "accountAddsOnText", CURR_APP)) {
						this.addComment("Successfully verified the text " + "<b> Account Add-Ons </b>"
								+ " in A-la-carte tab section");
					} else {
						this.addComment("Failed to find the text Account Add-Ons in A-la-carte tab section");
						throw new KDTKeywordExecException(
								"Failed to find the text " + "<b> Account Add-Ons </b>" + " in A-la-carte tab section");
					}

					// Verify the Support Type text and its drop down items

					if (eo.isDisplayed(driver, "xpath", "supportTypefilterText", CURR_APP)) {
						this.addComment("Successfully verified the text " + "<b> Support Type</b>"
								+ " in A-la-carte tab section");

						String[] support_type_list_expected = supportType.split(";");
						List<WebElement> suppotyType_list_act = eo.getListOfWebElements(driver, "xpath",
								"supportTypeList", CURR_APP);

						int suppotyType_list_act_size = suppotyType_list_act.size();

						for (int m = 0; m < suppotyType_list_act_size; m++) {
							if (suppotyType_list_act.get(m).getText().equalsIgnoreCase(support_type_list_expected[m])) {
								this.addComment("Successfully verified the drop down element for Support type as "
										+ suppotyType_list_act.get(m).getText());
							} else {
								this.addComment("Failed to find the drop down element for Support type as "
										+ suppotyType_list_act.get(m).getText());
								throw new KDTKeywordExecException(
										"Failed to find the drop down element for Support type as "
												+ suppotyType_list_act.get(m).getText());
							}
						}

					} else {
						this.addComment(
								"Failed to find the text" + "<b> Support Type</b>" + " in A-la-carte tab section");
						throw new KDTKeywordExecException(
								"Failed to find the text " + "<b> Support Type</b>" + " in A-la-carte tab section");
					}

					// Verify the Payment Option text and the drop down items

					if (eo.isDisplayed(driver, "xpath", "paymentOptionFilterText", CURR_APP)) {
						this.addComment("Successfully verified the text " + "<b> Payment Option</b>"
								+ " in A-la-carte tab section");

						String[] paymentOption_list_expected = paymentOptions.split(";");
						List<WebElement> paymentOption_list_act = eo.getListOfWebElements(driver, "xpath",
								"paymentOptionList", CURR_APP);

						int paymentOption_list_act_size = paymentOption_list_act.size();

						for (int n = 0; n < paymentOption_list_act_size; n++) {
							if (paymentOption_list_act.get(n).getText()
									.equalsIgnoreCase(paymentOption_list_expected[n])) {
								this.addComment("Successfully verified the drop down element for Payment option as "
										+ paymentOption_list_act.get(n).getText() + " in Ala carte section");
							} else {
								this.addComment("Failed to find the drop down element for Payment Options as "
										+ paymentOption_list_act.get(n).getText() + " in Ala carte section");
								throw new KDTKeywordExecException(
										"Failed to find the drop down element for Payment Option as "
												+ paymentOption_list_act.get(n).getText() + " in Ala carte section");
							}
						}

					} else {
						this.addComment(
								"Failed to find the text" + "<b> Payment Option<</b>" + " in A-la-carte tab section");
						throw new KDTKeywordExecException(
								"Failed to find the text " + "<b> Payment Option<</b>" + " in A-la-carte tab section");
					}

					try {

						verifyCoulmnName(driver, columnname, AccountAddsOnText);
						this.addComment("Successfully verified the column names in the Account Add-Ons section of tab "
								+ tabNames[i]);

					}

					catch (Exception e) {
						this.addComment("Failed to verify the Column names in the Account Add-Ons section of tab "
								+ tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Account Add-Ons section of tab "
										+ tabNames[i]);
					}

					break;

				case "Support Add-Ons":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					// Verify the display of Support Add-Ons text

					String SupportAddsOnText = eo.getText(driver, "xpath", "supportAddsOnText", CURR_APP);

					if (eo.isDisplayed(driver, "xpath", "supportAddsOnText", CURR_APP)) {
						this.addComment("Successfully verified the text " + "<b> Support Add-Ons </b>"
								+ " in SUPPORT ADD-ONS section");
					} else {
						this.addComment("Failed to find the text Support Add-Ons in SUPPORT ADD-ONS section");
						throw new KDTKeywordExecException("Failed to find the text " + "<b> Support Add-Ons </b>"
								+ " in SUPPORT ADD-ONS section");
					}

					// Verify Payment Option drop down text

					if (eo.isDisplayed(driver, "xpath", "paymentOptionFilterTextInSupportAddoN", CURR_APP)) {
						this.addComment(
								"Successfully verified the text " + "<b> Payment Options</b>" + " in Support Add-Ons");

						String[] paymentOption_list_expected = paymentOptions.split(";");
						List<WebElement> paymentOption_list_act = eo.getListOfWebElements(driver, "xpath",
								"paymentOptionListSupportAddOns", CURR_APP);

						int paymentOption_list_act_size = paymentOption_list_act.size();

						for (int n = 0; n < paymentOption_list_act_size; n++) {
							if (paymentOption_list_act.get(n).getText()
									.equalsIgnoreCase(paymentOption_list_expected[n])) {
								this.addComment("Successfully verified the drop down element for Payment option as "
										+ paymentOption_list_act.get(n).getText() + " in Support Add Ons section");
							} else {
								this.addComment("Failed to find the drop down element for Payment Options as "
										+ paymentOption_list_act.get(n).getText() + " in Support Add Ons section");
								throw new KDTKeywordExecException(
										"Failed to find the drop down element for Payment Option as "
												+ paymentOption_list_act.get(n).getText());
							}
						}

					} else {
						this.addComment(
								"Failed to find the text" + "<b> Payment Option </b>" + " in Support Add Ons section");
						throw new KDTKeywordExecException(
								"Failed to find the text " + "<b> Payment Option<</b>" + " Support Add Ons section");
					}

					try {

						verifyCoulmnName(driver, columnname, SupportAddsOnText);
						this.addComment("Successfully verified the column names in the Support Add-Ons section of tab "
								+ tabNames[i]);

					}

					catch (Exception e) {
						this.addComment("Failed to verify the Column names in the Support Add-Ons section of tab "
								+ tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Support Add-Ons section of tab "
										+ tabNames[i]);
					}

					break;

				case "Professional Services":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					// Verify the display of Professional Services text

					String ProfessionalServicesText = eo.getText(driver, "xpath", "professionalServicesText", CURR_APP);

					if (eo.isDisplayed(driver, "xpath", "professionalServicesText", CURR_APP)) {
						this.addComment("Successfully verified the text " + "<b> ProfessionalServices </b>"
								+ " in ProfessionalServices section");
					} else {
						this.addComment(
								"Failed to find the text Professional Services in ProfessionalServices section");
						throw new KDTKeywordExecException("Failed to find the text " + "<b> Professional Services </b>"
								+ " in  ProfessionalServices section");
					}

					try {

						verifyCoulmnName(driver, columnname, ProfessionalServicesText);
						this.addComment(
								"Successfully verified the column names in the Professional Services section of tab "
										+ tabNames[i]);

					}

					catch (Exception e) {
						this.addComment("Failed to verify the Column names in the Professional Services section of tab "
								+ tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Professional Services section of tab "
										+ tabNames[i]);
					}

					break;

				case "Customer Training Offerings":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					// Verify the display of Customer Training text

					String CustomerTrainingText = eo.getText(driver, "xpath", "customerTrainingText", CURR_APP);

					if (eo.isDisplayed(driver, "xpath", "customerTrainingText", CURR_APP)) {
						this.addComment("Successfully verified the text " + "<b> Customer Training </b>"
								+ " in Customer Training Offering section");
					} else {
						this.addComment(
								"Failed to find the text Customer Training in Customer Training Offering section");
						throw new KDTKeywordExecException("Failed to find the text " + "<b> Customer Training </b>"
								+ " in  Customer Training Offering section");
					}

					try {

						verifyCoulmnName(driver, columnname, CustomerTrainingText);
						this.addComment(
								"Successfully verified the column names in the Customer Training Offering section of tab "
										+ tabNames[i]);

					}

					catch (Exception e) {
						this.addComment("Failed to verify the Column names in the Professional Services section of tab "
								+ tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Professional Services section of tab "
										+ tabNames[i]);
					}

				}

			}

		}

		// This is a method starts here
		public static void verifyCoulmnName(WebDriver driver, String ColumnnameFromExcel, String sectionName)
				throws KDTKeywordExecException {

			String[] section_column_names_expected_list = ColumnnameFromExcel.split(";");

			String Quantity_text = eo.getTextAfterReplacingKeyValue(driver, "xpath", "section_quantitytext", "{link}",sectionName, CURR_APP);

			if (section_column_names_expected_list[4].equalsIgnoreCase(Quantity_text)) {
		
                   ;
			} else {

				throw new KDTKeywordExecException("Failed to find the column name as " + Quantity_text);
			}

			List<WebElement> section_column_names_act_list = null;

			try {

				section_column_names_act_list = driver.findElements(
						By.xpath(gei.getProperty("section_column_names", CURR_APP).replace("{link}", sectionName)));
			} catch (Exception e) {
				throw new KDTKeywordExecException("Failed to find the element" + section_column_names_act_list);
			}

			int section_column_names_list_act_size = section_column_names_act_list.size();

			for (int l = 0; l < section_column_names_list_act_size; l++) {

				if (section_column_names_act_list.get(l).getText()
						.equalsIgnoreCase(section_column_names_expected_list[l])) {
				
				}

				else {
				
					throw new KDTKeywordExecException("Failed to find the column name as "
							+ section_column_names_act_list.get(l).getText() + " in the Appliances section");
				}

			}

		}
		// This method ends here
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyCancelConfigureProduct</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to verify the cancel button
	 * in the Configure product page </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyCancelConfigureProduct extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			eo.waitForWebElementVisible(driver, "xpath", "cancelButtonEachPage", 120, CURR_APP);
			eo.clickElement(driver, "xpath", "cancelButtonEachPage", CURR_APP);
			this.addComment("Successfully clicked on cancel button");
			eo.waitForPopUp(driver);

			if (eo.isDisplayed(driver, "xpath", "configureProductPageTitle", CURR_APP)) {

				this.addComment("Successfully landed to Configure Product page after click on Cancel button");

			}

			else {
				this.addComment("Failed to land in to Configure Product Page after click on Cancel button");
				throw new KDTKeywordExecException(
						"Failed to land in to Configure Product Page after click on Cancel button");
			}

		}

	}

	/////////////////////////////////// SelectProductCheckbox///////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>selectProductCheckbox</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to select the check box
	 * corresponding to the product in Configure product page </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class SelectProductCheckbox extends Keyword {

		private String productCode;
		private String tabName;
		private String saveOrCancel;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ProductCode");
				productCode = args.get("ProductCode");

			} catch (Exception e) {
				this.addComment("Error while initializing selectProductCheckbox");
				throw new KDTKeywordInitException("Error while initializing selectProductCheckbox", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				if (hasArgs("TabName")) {

					tabName = args.get("TabName");

					eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "tabToSelect", "{tabName}", tabName, 120,
							CURR_APP);
					eo.actionClickAfterReplacingKeyValue(driver, "xpath", "tabToSelect", "{tabName}", tabName,
							CURR_APP);

				}

				// Selecting the provided product check box

				String[] productList = productCode.split(";");

				for (int i = 0; i < productList.length; i++) {
					SeleniumTools.waitForWebElementVisible(driver, By.xpath(gei
							.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", productList[i])),
							30);
					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
							productList[i], CURR_APP);
				}

				if (hasArgs("SaveOrCancel")) {
					saveOrCancel = args.get("SaveOrCancel");

					switch (saveOrCancel) {

					case "Save":
						eo.wait(2);

						eo.javaScriptScrollToViewElement(driver, "xpath", "confifureProductSave", CURR_APP);
						eo.clickElement(driver, "xpath", "confifureProductSave", CURR_APP);
						eo.waitForWebElementVisible(driver, "xpath", "editQuotePageLanding", 500, CURR_APP);
						this.addComment("Successfully clicked on SAVE button form Configure Products page");
						break;

					case "Cancel":

						eo.javaScriptScrollToViewElement(driver, "xpath", "cancelButton", CURR_APP);
						eo.clickElement(driver, "xpath", "cancelButton", CURR_APP);
						eo.wait(5);
						this.addComment("Successfully clicked on CANCEL button form Prodcut configuration page");

						break;

					default:
						this.addComment("There is no action requested by the user");

						break;

					}

				}

			}

			catch (Exception e) {

				this.addComment("User is Unable to selec the product check box");
				throw new KDTKeywordExecException("User is Unable to selec the product check box", e);

			}

		}
	}

	///////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyConfigureFilter</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to Verify the filter in every
	 * pages </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin & Mahesh</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyConfigureFilter extends Keyword {

		private String firstFiledInFilter = "";
		private String secondFieldInFilter = "";
		private String thirdFieldInFilter = "";
		private String productToSearch = "";

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("FirstFiledInFilter", "SecondFieldInFilter", "ThirdFieldInFilter", "ProductToSearch");

				firstFiledInFilter = args.get("FirstFiledInFilter");
				secondFieldInFilter = args.get("SecondFieldInFilter");
				thirdFieldInFilter = args.get("ThirdFieldInFilter");
				productToSearch = args.get("ProductToSearch");

			} catch (Exception e) {
				this.addComment("Error while initializing VerifyConfigureFilter");
				throw new KDTKeywordInitException("Error while initializing VerifyConfigureFilter", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			// Actions act = new Actions(driver);
			try {

				// Verifying the tooltip
				try {
					eo.waitForWebElementVisible(driver, "xpath", "filterIcon", 120, CURR_APP);
				} catch (Exception e) {
					this.addComment("Failed to capture the tool tip for filter icon");
					throw new KDTKeywordExecException("Failed to capture the tool tip for filter icon");
				}

				eo.actionMoveToElement(driver, "xpath", "filterIcon", CURR_APP);

				String actual_filter_tooltip = eo.getText(driver, "xpath", "filterTooltip", CURR_APP);
				String expected_filter_tooltip = "Filter";

				if (expected_filter_tooltip.equalsIgnoreCase(actual_filter_tooltip)) {
					this.addComment("Successfully verified the tool tip over filter icon displayed as " + "<b>"
							+ actual_filter_tooltip + "</b>");
				} else {
					this.addComment(
							"Failed to verify the tool tip over filter as " + "<b>" + actual_filter_tooltip + "</b>");
					throw new KDTKeywordExecException(
							"Failed to verify the tool tip over filter as " + "<b>" + actual_filter_tooltip + "</b>");
				}

				// Clicking on the Filter icon

				eo.actionClick(driver, "xpath", "filterIcon", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "applyFieldButton", 120, CURR_APP);
				this.addComment("Successfully Clicked on the filter icon");

				// Closing the Filter pop up

				eo.waitForWebElementVisible(driver, "xpath", "closeSidePopup", 120, CURR_APP);
				eo.actionClick(driver, "xpath", "closeSidePopup", CURR_APP);

				// Clicking again on the filter icon
				try {
					eo.waitForWebElementVisible(driver, "xpath", "filterIcon", waiTime, CURR_APP);
					this.addComment("Successfully closed the filter popup");
				} catch (Exception e) {
					this.addComment("Failed to close the filter pop up");
					throw new KDTKeywordExecException("Failed to close the filter pop up");
				}

				eo.actionClick(driver, "xpath", "filterIcon", CURR_APP);
				eo.waitForPageload(driver);

				// Verifying the display of Field names

				try {
					eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "fieldNameXpath", "{fieldName}",
							firstFiledInFilter, 120, CURR_APP);
					this.addComment("Successfully verified the display of field " + firstFiledInFilter);
				} catch (Exception e) {
					this.addComment("Failed to verify the display of field " + firstFiledInFilter);
					throw new KDTKeywordExecException("Failed to verify the display of field " + firstFiledInFilter);
				}

				try {

					eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "fieldNameXpath", "{fieldName}",
							secondFieldInFilter, 120, CURR_APP);
					this.addComment("Successfully verified the display of field " + secondFieldInFilter);
				} catch (Exception e) {
					this.addComment("Failed to verify the display of field " + secondFieldInFilter);
					throw new KDTKeywordExecException("Failed to verify the display of field " + secondFieldInFilter);

				}

				try {
					eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "fieldNameXpath", "{fieldName}",
							"Product Name", 120, CURR_APP);
					this.addComment("Successfully verified the display of field " + thirdFieldInFilter);
				} catch (Exception e) {
					this.addComment("Failed to verify the display of field " + thirdFieldInFilter);
					throw new KDTKeywordExecException("Failed to verify the display of field " + thirdFieldInFilter);

				}

				// Entering the product code in the text input fields

				try {
					eo.enterTextAfterReplacingKeyValue(driver, "xpath", "filterInput", "{fieldName}",
							firstFiledInFilter, productToSearch, CURR_APP);
					this.addComment("Successfully entered the product code in" + firstFiledInFilter);
				} catch (Exception e) {
					this.addComment("Failed to Successfully entered the product code in " + firstFiledInFilter);
					throw new KDTKeywordExecException(
							"Failed to Successfully entered the product code in " + firstFiledInFilter);

				}

				try {
					eo.enterTextAfterReplacingKeyValue(driver, "xpath", "filterInput", "{fieldName}",
							secondFieldInFilter, productToSearch, CURR_APP);
					this.addComment("Successfully entered the product code in" + secondFieldInFilter);
				} catch (Exception e) {
					this.addComment("Failed to Successfully entered the product code in " + secondFieldInFilter);
					throw new KDTKeywordExecException(
							"Failed to Successfully entered the product code in " + secondFieldInFilter);

				}

				try {
					eo.enterTextAfterReplacingKeyValue(driver, "xpath", "filterInput", "{fieldName}",
							thirdFieldInFilter, productToSearch, CURR_APP);
					this.addComment("Successfully entered the product code in" + thirdFieldInFilter);
				} catch (Exception e) {
					this.addComment("Failed to Successfully entered the product code in " + thirdFieldInFilter);
					throw new KDTKeywordExecException(
							"Failed to Successfully entered the product code in " + thirdFieldInFilter);

				}

				// Clearing the field

				eo.waitForWebElementVisible(driver, "xpath", "clearField", 120, CURR_APP);

				Actions a = new Actions(driver);
				WebElement clear = driver.findElement(By.xpath("//text[@id='clear']/sb-i18n/.."));
				a.moveToElement(clear).click().build().perform();
				addComment("SucccessFully clicked the:" + "<b>ClearFileds</b>" + " in Popup");

				// Entering the same field inputs

				/*
				 * try { eo.enterTextAfterReplacingKeyValue(driver, "xpath", "filterInput",
				 * "{fieldName}", firstFiledInFilter, productToSearch, CURR_APP);
				 * this.addComment("Successfully entered the product code in"+firstFiledInFilter
				 * ); } catch(Exception e) {
				 * this.addComment("Failed to Successfully entered the product code in "
				 * +firstFiledInFilter); throw new
				 * KDTKeywordExecException("Failed to Successfully entered the product code in "
				 * +firstFiledInFilter);
				 * 
				 * }
				 */

				try {
					eo.enterTextAfterReplacingKeyValue(driver, "xpath", "filterInput", "{fieldName}",
							secondFieldInFilter, productToSearch, CURR_APP);
					this.addComment("Successfully entered the product code in" + secondFieldInFilter);
				} catch (Exception e) {
					this.addComment("Failed to Successfully entered the product code in " + secondFieldInFilter);
					throw new KDTKeywordExecException(
							"Failed to Successfully entered the product code in " + secondFieldInFilter);

				}

				try {
					eo.enterTextAfterReplacingKeyValue(driver, "xpath", "filterInput", "{fieldName}",
							thirdFieldInFilter, productToSearch, CURR_APP);
					this.addComment("Successfully entered the product code in" + thirdFieldInFilter);
				} catch (Exception e) {
					this.addComment("Failed to Successfully entered the product code in " + thirdFieldInFilter);
					throw new KDTKeywordExecException(
							"Failed to Successfully entered the product code in " + thirdFieldInFilter);

				}

				// Clicking on Apply Button

				eo.clickElement(driver, "xpath", "applyFieldButton", CURR_APP);

				try {
					eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "verifyProduct", "{link}", productToSearch,
							120, CURR_APP);
					addComment("Successfully Verified the Product after search through filter");
				} catch (Exception e) {
					this.addComment("Failed to find the product after apply the Filter");
					throw new KDTKeywordExecException("Failed to find the product after apply the Filter", e);

				}

			}

			catch (Exception e) {

				this.addComment("User is Unable to VerifyConfigureFilter");
				throw new KDTKeywordExecException("User is Unable to VerifyConfigureFilter", e);

			}

		}
	}

	//////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>AddProductEditQuotePage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to add products in Edit Quote
	 * page </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther: Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class AddProductEditQuotePage extends Keyword {

		private String productsToadd;
		private String doYouWantToDeleteExistingProducts;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			verifyArgs("AddProducts");
			productsToadd = args.get("AddProducts");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				// Delete the existing products as per user input

				if (hasArgs("DoYouWantToDeleteExistingProducts")) {

					doYouWantToDeleteExistingProducts = args.get("DoYouWantToDeleteExistingProducts");

					if (doYouWantToDeleteExistingProducts.equalsIgnoreCase("yes")) {

						driver.findElement(By.xpath("//div[@class='checkboxcontainer style-scope sf-le-table-header']"))
								.click();

						driver.findElement(By.xpath("//paper-button[text()='Delete Lines']")).click();
						eo.wait(10);

					}

					if (doYouWantToDeleteExistingProducts.equalsIgnoreCase("no")) {
						;
					}
				}

				// Getting all the product exist in the list

				List<WebElement> actualproductList = eo.getListOfWebElements(driver, "xpath", "existingProducts",
						CURR_APP);

				for (int i = 0; i < actualproductList.size(); i++) {

					this.addComment(actualproductList.size() + " ");
					this.addComment(actualproductList.get(i).getText());

				}

				int countbefore = 0;
				for (int i = 0; i < actualproductList.size(); i++) {

					if (actualproductList.get(i).getText().equalsIgnoreCase(productsToadd)) {

						countbefore++;

					}

				}

				this.addComment("The count of product " + productsToadd
						+ " exist in the edit quote line before adding a new product is " + countbefore);

				// Clicking on the Add Product button
				eo.clickElement(driver, "xpath", "addProducts_button", CURR_APP);

				eo.wait(10);

				// Search for the product
				eo.waitForWebElementVisible(driver, "xpath", "search_textbox", 500, CURR_APP);
				eo.clickElement(driver, "xpath", "search_textbox", CURR_APP);
				eo.enterText(driver, "xpath", "search_textbox", productsToadd, CURR_APP);
				SeleniumTools.waitForWebElementToClickable(driver,
						By.xpath(gei.getProperty("searchproducts_button", CURR_APP)), waiTime);
				eo.clickElement(driver, "xpath", "searchproducts_button", CURR_APP);

				eo.waitForWebElementVisibleAfterReplace(driver, "XPATH", "addproduct_checkbox", "{product}",
						productsToadd, 500, CURR_APP);

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "addproduct_checkbox", "{product}",
						productsToadd, CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "select_button", 500, CURR_APP);
				eo.clickElement(driver, "xpath", "select_button", CURR_APP);

				eo.waitForPopUp(driver);

				SeleniumTools.waitForWebElementToClickable(driver,
						By.xpath(gei.getProperty("save_product_config", CURR_APP)), waiTime);

				eo.waitForPopUp(driver);
				eo.waitForPopUp(driver);

				eo.clickElement(driver, "XPATH", "save_product_config", CURR_APP);
				addComment("Successfully clicked on Save button after add the product " + productsToadd);

				eo.waitForPopUp(driver);
				eo.waitForPopUp(driver);
				eo.waitForPopUp(driver);

				List<WebElement> actualproductListafter = eo.getListOfWebElements(driver, "xpath", "existingProducts",
						CURR_APP);

				this.addComment(actualproductListafter.size() + "");

				for (WebElement elemnt : actualproductListafter) {

					this.addComment(elemnt.getText());
					ExpectedConditions.visibilityOf(elemnt);
				}

				int countafter = 0;
				for (int j = 0; j < actualproductListafter.size(); j++) {

					if (actualproductListafter.get(j).getText().equalsIgnoreCase(productsToadd)) {

						countafter++;

					}

				}

				this.addComment("The count of products exist in the edit quote line after adding a new product is "
						+ countafter);
				this.addComment("The count of product exist in the edit quote line before adding a new product is "
						+ countbefore);

				if (countbefore == 0) {

					if (countafter == countbefore + 1) {
						this.addComment(
								"Successfully verified that the product <b>" + productsToadd + "</b> added to list");

					}

					else {
						addComment("Selected Product is not added<b> " + productsToadd + "</b>");
						throw new KDTKeywordExecException("Selected Product is not added");
					}

				}

				if (countbefore == 1) {

					if (countafter == countbefore+1) {
						this.addComment("Successfully verified that the product <b>" + productsToadd + "</b> added to list");

					}

					else {
						addComment("Selected Product is not added<b> " + productsToadd + "</b>");
						throw new KDTKeywordExecException("Selected Product is not added");
					}

				}

			}

			catch (Exception e) {
				addComment("Add Product to quote line Failed");
				throw new KDTKeywordExecException("Add Product to quote line Failed");
			}

		}
	}

	///////////////////////////////// DeleteProducts/////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>DeleteProducts</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to delete any product from
	 * the edit quote page </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther: Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class DeleteProductsEditQuotePage extends Keyword {

		private String productsName;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("DeleteProducts");
			productsName = args.get("DeleteProducts");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				// Added this line to delete all the products

				driver.findElement(By.xpath("//div[@class='checkboxcontainer style-scope sf-le-table-header']"))
						.click();

				driver.findElement(By.xpath("//paper-button[text()='Delete Lines']")).click();
				eo.wait(10);

				// till here

				// Clicking on the Add Product button
				eo.clickElement(driver, "xpath", "addProducts_button", CURR_APP);

				eo.wait(10);

				// Search for the product
				eo.waitForWebElementVisible(driver, "xpath", "search_textbox", 500, CURR_APP);
				eo.clickElement(driver, "xpath", "search_textbox", CURR_APP);
				eo.enterText(driver, "xpath", "search_textbox", productsName, CURR_APP);
				SeleniumTools.waitForWebElementToClickable(driver,
						By.xpath(gei.getProperty("searchproducts_button", CURR_APP)), waiTime);
				eo.clickElement(driver, "xpath", "searchproducts_button", CURR_APP);

				eo.waitForWebElementVisibleAfterReplace(driver, "XPATH", "addproduct_checkbox", "{product}",
						productsName, 500, CURR_APP);

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "addproduct_checkbox", "{product}", productsName,
						CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "select_button", 500, CURR_APP);
				eo.clickElement(driver, "xpath", "select_button", CURR_APP);

				eo.wait(10);

				SeleniumTools.waitForWebElementToClickable(driver,
						By.xpath(gei.getProperty("save_product_config", CURR_APP)), waiTime);

				eo.waitForPopUp(driver);
				eo.waitForPopUp(driver);

				eo.clickElement(driver, "XPATH", "save_product_config", CURR_APP);
				addComment("Successfully clicked on Save button after add the product " + productsName);

				eo.waitForPopUp(driver);
				eo.waitForPopUp(driver);
				eo.waitForPopUp(driver);

				Actions a = new Actions(driver);
				// Getting the count of product exist in the list with check box

				List<WebElement> availableCheckbox = driver.findElements(
						By.xpath(gei.getProperty("quoteLine_checkbox", CURR_APP).replace("{product}", productsName)));

				int countbefore = availableCheckbox.size();

				this.addComment(
						"The total product count having check box in the quote edit line before delete the product "
								+ productsName + " is " + countbefore);

				for (int i = 0; i < countbefore; i++) {

					WebElement ele = driver.findElement(By
							.xpath(gei.getProperty("quoteLine_checkbox", CURR_APP).replace("{product}", productsName)));
					a.moveToElement(ele);

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "quoteLine_checkbox", "{product}",
							productsName, CURR_APP);
					this.addComment("Selected the Products to be deleted <b>" + productsName + "</b>");

					SeleniumTools.waitForWebElementToClickable(driver,
							By.xpath(gei.getProperty("deletebutton_productline", CURR_APP)), waiTime);
					eo.clickElement(driver, "xpath", "deletebutton_productline", CURR_APP);

					SeleniumTools.waitForWebElementToClickable(driver,
							By.xpath(gei.getProperty("quote_save_btn", CURR_APP)), waiTime);

					eo.waitForPopUp(driver);

				}

				List<WebElement> availableCheckboxafter = driver.findElements(
						By.xpath(gei.getProperty("quoteLine_checkbox", CURR_APP).replace("{product}", productsName)));

				int countafter = availableCheckboxafter.size();
				this.addComment(
						"The total product count having check box in the quote edit line after delete the product "
								+ productsName + " is " + countafter);

				if (countafter == 0) {
					this.addComment("Successfully deleted the product:<b>" + productsName + "</b>");

				}

				else {

					addComment("Failed to delete the product");
					throw new KDTKeywordExecException("Failed to delete the product");
				}

			}

			catch (Exception e) {
				addComment("Delete Product from quote line Failed");
				throw new KDTKeywordExecException("Delete Product from quote line Failed");
			}
		}

	}
	///////////////////////// VerifySuccessfulconfigurationWithoutRedIndication///////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifySuccessfulconfigurationWithoutRedIndication</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used verify the red indication in
	 * the configure prodcut page </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ProductCode(Mandatory)</li>
	 * <li>SpareProduct(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther: Amara</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifySuccessfulconfigurationWithoutRedIndication extends Keyword {

		private String productCode;
		private String productTab;
		private String productCodeProductConfig[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ProductCode", "ProductTab", "ProdcutCodeProdcutConfig");

				productCode = args.get("ProductCode");
				productTab = args.get("ProductTab");
				productCodeProductConfig = args.get("ProdcutCodeProdcutConfig").split(";");

			} catch (Exception e) {
				this.addComment("Error while initializing Verify_Successful_configuration_withoutRedIndication");
				throw new KDTKeywordInitException(
						"Error while initializing Verify_Successful_configuration_withoutRedIndication", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				// Clicking on any tab in the Product Configure page

				try {

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							productTab, CURR_APP);

					this.addComment("Successfully verified the display of tab " + productTab);
					this.addComment("Successfully clicked on tab " + productTab);
				} catch (Exception e) {
					this.addComment("Failed to click on the tab " + productTab);
					throw new KDTKeywordExecException("Failed to click on the tab " + productTab, e);
				}

				// Selecting any product from the Selected tab in Product configuration page

				try {
					for (String element : productCodeProductConfig) {

						SeleniumTools.waitForWebElementVisible(driver, By.xpath(
								gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", element)),
								30);
						eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "product_code_checkbox",
								"{producdCode}", element, CURR_APP);
						eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
								element, CURR_APP);
						this.addComment("successfully selected the product " + element + " for the tab " + productTab);

					}

				}

				catch (Exception e) {
					this.addComment("Failed to select the product from the tab " + productTab);
					throw new KDTKeywordInitException("Failed to select the product from the tab" + productTab);

				}

				// Clicked on Cancel button
				eo.javaScriptScrollToViewElement(driver, "xpath", "cancel2", CURR_APP);
				eo.clickElement(driver, "xpath", "cancel2", CURR_APP);
				addComment("Successfully clicked on cancel button from Configure product page");

				// Verify the red color
				eo.waitForWebElementVisible(driver, "xpath", "colorVerifying", waiTime, CURR_APP);
				if (eo.isDisplayed(driver, "xpath", "colorVerifying", CURR_APP)) {

					// WebElement ele =
					// driver.findElement(By.xpath(gei.getProperty("colorVerifying", CURR_APP)));
					// String col = ele.getCssValue("color");
					this.addComment(
							"Successfully verified the red color is displayed for the selected product in Configure page");

				} else {
					this.addComment("Fail to verify the red color for the selected product");
				}

				// Clicking on the Configure wrench icon
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_configure_btn", "{producdCode}",
						productCode, CURR_APP);

				this.addComment("Clicked on the configure wrench icon for the selected product " + productCode);

				// Clicking on any tab in the Product Configure page

				try {

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							productTab, CURR_APP);

					this.addComment("Successfully verified the display of tab " + productTab);
					this.addComment("Successfully clicked on tab " + productTab);
				} catch (Exception e) {
					this.addComment("Failed to click on the tab " + productTab);
					throw new KDTKeywordExecException("Failed to click on the tab " + productTab, e);
				}

				// Selecting any product from the Selected tab in Product configuration page

				try {
					for (String element : productCodeProductConfig) {

						SeleniumTools.waitForWebElementVisible(driver, By.xpath(
								gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", element)),
								30);
						eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "product_code_checkbox",
								"{producdCode}", element, CURR_APP);
						eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
								element, CURR_APP);
						this.addComment("successfully selected the product " + element + " for the tab " + productTab);

					}

				}

				catch (Exception e) {
					this.addComment("Failed to select the product from the tab " + productTab);
					throw new KDTKeywordInitException("Failed to select the product from the tab" + productTab);

				}

				// Clicked on save button
				eo.javaScriptScrollToViewElement(driver, "xpath", "save2", CURR_APP);
				eo.clickElement(driver, "xpath", "save2", CURR_APP);
				this.addComment("Saved the product configuration");
				eo.waitForWebElementVisible(driver, "xpath", "appliances_text", waiTime, CURR_APP);

				try {
					eo.waitForWebElementVisible(driver, "xpath", "colorVerifying", 10, CURR_APP);
					this.addComment(
							"Found the red indication still displaying after save a product configuration : Test Failed");
					throw new KDTKeywordExecException(
							"Found the red indication still displaying after save a product configuration : Test Failed");
				}

				catch (Exception e) {
					this.addComment("Verified no red indication displayed after save the product configuration");
				}

			} catch (Exception e) {
				this.addComment("Failed to verify the save product configuration");
				throw new KDTKeywordExecException("Failed to verify the save product configuration", e);

			}
		}
	}

	///////////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>UIVerificationR6000</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to verify </i>
	 * </P>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>AppliancesDetails(Mandatory):In this argument need to pass all the
	 * expected value e.g. Quantity,Product Code etc and seperate all the values
	 * based on ':' and seperate individual records ';' in datasheet</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class UIVerificationProductConfigurePageSubTabs extends Keyword {

		private String tabNameProdcutConfig = "";
		private String columnname = "";
		private String dropdownOptionAndValue[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("TabNameProdcutConfig", "ColumnName", "DropdownOptions");
			columnname = args.get("ColumnName");
			tabNameProdcutConfig = args.get("TabNameProdcutConfig");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			String[] tabNames = tabNameProdcutConfig.split(";");
			int tabNameSize = tabNames.length;

			for (int i = 0; i < tabNameSize; i++) {

				switch (tabNames[i]) {

				case "RCDM & Subscription Add-Ons":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					this.addComment("Successfully clicked on the sub tab " + tabNames[i]);

					// Verify that below mentioned values are displayed by default for the mentioned
					// fields:

					dropdownOptionAndValue = args.get("DropdownOptions").split(";");
					this.addComment(args.get("DropdownOptions"));
					for (int j = 0; j < dropdownOptionAndValue.length; j++) {
						String label = dropdownOptionAndValue[j].split(":")[0];
						String value = dropdownOptionAndValue[j].split(":")[1];

						try {

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "filterValueDefault", "{label}",
									label, waiTime, CURR_APP);
							eo.getTextAfterReplacingKeyValue(driver, "xpath", "filterValueDefault", "{label}", label,
									CURR_APP).equalsIgnoreCase(value);
							this.addComment(
									"Verified the display of filter value as " + value + " for option " + label);

						}

						catch (Exception e) {

							this.addComment("Failed to Verify the display of filter value as " + value + " for option "
									+ label);
							throw new KDTKeywordExecException("Failed to Verify the display of filter value as " + value
									+ " for option " + label);
						}

					}

					// Verify the display of text Rubrik Go
					try {

						eo.waitForWebElementVisible(driver, "xpath", "ribricGo_text", waiTime, CURR_APP);
						this.addComment("Successfully verified the display of "
								+ eo.getText(driver, "xpath", "ribricGo_text", CURR_APP));

					}

					catch (Exception e) {
						this.addComment("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "ribricGo_text", CURR_APP));
						throw new KDTKeywordExecException("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "ribricGo_text", CURR_APP));
					}

					// Verify the display of Prodcut in the Rubrik Go section

					try {

						eo.waitForWebElementVisible(driver, "xpath", "rubrikGoProdcut", waiTime, CURR_APP);
						this.addComment("Successfully verified the display of prodcut "
								+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP));

						// Verify the product is checked by default in the Rubrik Go
						try {

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "prodcutCheckboxtrue",
									"{productCode}", eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP), waiTime,
									CURR_APP);
							this.addComment("Successfully verifed the product "
									+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP)
									+ " is selected by default");
						}

						catch (Exception e) {
							this.addComment("Failed to verify the prodcut "
									+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP)
									+ " is checked by default");
							throw new KDTKeywordExecException("Failed to verify the product "
									+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP)
									+ " is checked by default");
						}

						// verify the product in Rubrik Go is not able to check again

						try {

							eo.waitForWebElementVisible(driver, "xpath", "disabledCheckboxInRubrikGo", 120, CURR_APP);
							this.addComment("Successfully verifed the product "
									+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP)
									+ " check box is disabled");
						}

						catch (Exception e) {
							this.addComment("Failed to verify the prodcut "
									+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP)
									+ " check box is disabled");
							throw new KDTKeywordExecException("Failed to verify the product "
									+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP)
									+ " check box is disabled");
						}

					}

					catch (Exception e) {
						this.addComment("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP));
						throw new KDTKeywordExecException("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "rubrikGoProdcut", CURR_APP));
					}

					// Verify the display of text Appliance Add-Ons

					try {

						eo.waitForWebElementVisible(driver, "xpath", "applianceaddOnsText", waiTime, CURR_APP);
						this.addComment("Successfully verified the display of "
								+ eo.getText(driver, "xpath", "applianceaddOnsText", CURR_APP));

					}

					catch (Exception e) {
						this.addComment("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "applianceaddOnsText", CURR_APP));
						throw new KDTKeywordExecException("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "applianceaddOnsText", CURR_APP));
					}

					// Verify the display of text No visible options in feature 'Perpetual to Life
					// of Device'

					try {

						eo.waitForWebElementVisible(driver, "xpath", "noVisibleOptionText", waiTime, CURR_APP);
						this.addComment("Successfully verified the display of "
								+ eo.getText(driver, "xpath", "noVisibleOptionText", CURR_APP));

					}

					catch (Exception e) {
						this.addComment("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "noVisibleOptionText", CURR_APP));
						throw new KDTKeywordExecException("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "noVisibleOptionText", CURR_APP));
					}

					// Verify the display of Column names in Rubrik Go

					try {

						verifyCoulmnName(driver, columnname, eo.getText(driver, "xpath", "ribricGo_text", CURR_APP));
						this.addComment("Successfully verified the column names in the Rubrik Go section of tab "
								+ tabNames[i]);

					}

					catch (Exception e) {
						this.addComment(
								"Failed to verify the Column names in the Rubrik Go section of tab " + tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Rubrik Go section of tab " + tabNames[i]);
					}

					// Verify the display of Column names in Appliance Add-Ons

					try {

						verifyCoulmnName(driver, columnname,
								eo.getText(driver, "xpath", "applianceaddOnsText", CURR_APP));
						this.addComment(
								"Successfully verified the column names in the Appliance Add-Ons section of tab "
										+ tabNames[i]);

					}

					catch (Exception e) {
						this.addComment("Failed to verify the Column names in the Appliance Add-Ons section of tab "
								+ tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Appliance Add-Ons section of tab "
										+ tabNames[i]);
					}

					break;

				case "Accessories":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					this.addComment("Successfully clicked on the sub tab " + tabNames[i]);

					// Verify the display of text Accessories
					try {

						eo.waitForWebElementVisible(driver, "xpath", "accessoriesText", waiTime, CURR_APP);
						this.addComment("Successfully verified the display of "
								+ eo.getText(driver, "xpath", "accessoriesText", CURR_APP));

					}

					catch (Exception e) {
						this.addComment("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "accessoriesText", CURR_APP));
						throw new KDTKeywordExecException("Failed to verify the display of "
								+ eo.getText(driver, "xpath", "accessoriesText", CURR_APP));
					}

					// Verify the column names

					try {

						verifyCoulmnName(driver, columnname, eo.getText(driver, "xpath", "accessoriesText", CURR_APP));
						this.addComment("Successfully verified the column names in the Accessories section of tab "
								+ tabNames[i]);

					}

					catch (Exception e) {
						this.addComment(
								"Failed to verify the Column names in the Accessories section of tab " + tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Accessories section of tab " + tabNames[i]);
					}

					// Verify that by default 'RBK-F3M-CBL-01' and 'RBK-SFP-TSR-01' products should
					// be selected under Accessories section.

					try {

						eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "prodcutCheckboxtrue", "{productCode}",
								eo.getText(driver, "xpath", "accessoriesProdcut1", CURR_APP), 5, CURR_APP);
						this.addComment("Successfully verifed the product "
								+ eo.getText(driver, "xpath", "accessoriesProdcut1", CURR_APP)
								+ " is selected by default");
					}

					catch (Exception e) {
						this.addComment("Failed to verify the prodcut "
								+ eo.getText(driver, "xpath", "accessoriesProdcut1", CURR_APP)
								+ " is checked by default");
						throw new KDTKeywordExecException("Failed to verify the product "
								+ eo.getText(driver, "xpath", "accessoriesProdcut1", CURR_APP)
								+ " is checked by default");
					}

					try {

						eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "prodcutCheckboxtrue", "{productCode}",
								eo.getText(driver, "xpath", "accessoriesProdcut2", CURR_APP), 5, CURR_APP);
						this.addComment("Successfully verifed the product "
								+ eo.getText(driver, "xpath", "accessoriesProdcut2", CURR_APP)
								+ " is selected by default");
					}

					catch (Exception e) {
						this.addComment("Failed to verify the prodcut "
								+ eo.getText(driver, "xpath", "accessoriesProdcut2", CURR_APP)
								+ " is checked by default");
						throw new KDTKeywordExecException("Failed to verify the product "
								+ eo.getText(driver, "xpath", "accessoriesProdcut2", CURR_APP)
								+ " is checked by default");
					}

					// Verify that by default 'RBK-SFP28-TSR-01' product is unselected under
					// Accessories section
					try {

						eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "prodcutCheckboxtrue", "{productCode}",
								eo.getText(driver, "xpath", "accessoriesProdcut3", CURR_APP), 5, CURR_APP);

						this.addComment("Failed to verify the prodcut "
								+ eo.getText(driver, "xpath", "accessoriesProdcut3", CURR_APP)
								+ " is not checked by default");
						throw new KDTKeywordExecException("Failed to verify the product "
								+ eo.getText(driver, "xpath", "accessoriesProdcut3", CURR_APP)
								+ " is not checked by default");
					}

					catch (Exception e) {
						this.addComment("Successfully verifed the product "
								+ eo.getText(driver, "xpath", "accessoriesProdcut3", CURR_APP)
								+ " is unselected by default in Accessories section");
					}

					break;

				case "Support":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					this.addComment("Successfully clicked on the sub tab " + tabNames[i]);

					// Verify that by default 'RBK-SVC-PREM-HW' product should be selected under
					// Support section.

					try {

						eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "prodcutCheckboxtrue", "{productCode}",
								eo.getText(driver, "xpath", "supportProdcut1", CURR_APP), 5, CURR_APP);
						this.addComment("Successfully verifed the product "
								+ eo.getText(driver, "xpath", "supportProdcut1", CURR_APP) + " is selected by default");
					}

					catch (Exception e) {
						this.addComment("Failed to verify the prodcut "
								+ eo.getText(driver, "xpath", "supportProdcut1", CURR_APP) + " is checked by default");
						throw new KDTKeywordExecException("Failed to verify the product "
								+ eo.getText(driver, "xpath", "supportProdcut1", CURR_APP) + " is checked by default");
					}

					// Verify the column names

					try {

						verifyCoulmnName(driver, columnname, eo.getText(driver, "xpath", "supportText", CURR_APP));
						this.addComment(
								"Successfully verified the column names in the Support section of tab " + tabNames[i]);

					}

					catch (Exception e) {
						this.addComment(
								"Failed to verify the Column names in the Support section of tab " + tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Support section of tab " + tabNames[i]);
					}

					break;

				case "Spares":

					SeleniumTools.waitForWebElementToClickable(driver, By.xpath(
							gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", tabNames[i])),
							waiTime);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							tabNames[i], CURR_APP);

					this.addComment("Successfully clicked on the sub tab " + tabNames[i]);

					// Verify the column names

					try {

						verifyCoulmnName(driver, columnname, eo.getText(driver, "xpath", "spareText", CURR_APP));
						this.addComment(
								"Successfully verified the column names in the Spares section of tab " + tabNames[i]);

					}

					catch (Exception e) {
						this.addComment(
								"Failed to verify the Column names in the Spares section of tab " + tabNames[i]);
						throw new KDTKeywordExecException(
								"Failed to verify the Column names in the Spares section of tab " + tabNames[i]);
					}

				}
			}
		}

		// This is a method starts here
		public static void verifyCoulmnName(WebDriver driver, String ColumnnameFromExcel, String sectionName)
				throws KDTKeywordExecException {

			String[] section_column_names_expected_list = ColumnnameFromExcel.split(";");

			// Verify the display of Quantity
			String Quantity_text = eo.getTextAfterReplacingKeyValue(driver, "xpath", "section_quantitytext", "{link}",
					sectionName, CURR_APP);

			if (section_column_names_expected_list[4].equalsIgnoreCase(Quantity_text)) {
				// this.addComment("Successfully verified "+Quantity_text_in_Appliances+" as
				// column name in Appliances section" );

			} else {

				// this.addComment("Failed to find the column name as
				// "+Quantity_text_in_Appliances+" in the Appliances section");
				throw new KDTKeywordExecException("Failed to find the column name as " + Quantity_text);
			}

			// Verifying the display of other Columns other than Quantity

			List<WebElement> section_column_names_act_list = null;

			try {

				section_column_names_act_list = driver.findElements(
						By.xpath(gei.getProperty("section_column_names", CURR_APP).replace("{link}", sectionName)));
			} catch (Exception e) {
				throw new KDTKeywordExecException("Failed to find the element" + section_column_names_act_list);
			}

			int section_column_names_list_act_size = section_column_names_act_list.size();

			for (int l = 0; l < section_column_names_list_act_size; l++) {

				if (section_column_names_act_list.get(l).getText()
						.equalsIgnoreCase(section_column_names_expected_list[l])) {
					// this.addComment("The column name
					// "+appliance_section_column_names_act_list.get(l).getText()+" is displaying in
					// the Appliances section");
				}

				else {
					// this.addComment("Failed to find the column name as
					// "+appliance_section_column_names_act_list.get(l).getText()+" in the
					// Appliances section");
					throw new KDTKeywordExecException("Failed to find the column name as "
							+ section_column_names_act_list.get(l).getText() + " in the " + sectionName + " section");
				}

			}

		}
		// This method ends here
	}

	//// #########################VerifyUnselectAfterSaveConfigureProduct############################///

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyUnselectAfterSaveConfigureProduct</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to
	 * VerifyUnselectAfterSaveConfigureProduct.</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>productCode(Mandatory)</li>
	 * <li>productCode2(Mandatory):</li>
	 * <li>spareProduct(Mandatory):</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Amara</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyUnselectAfterSaveConfigureProduct extends Keyword {

		private String productCode;
		private String productCode2;
		private String spareProduct;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ProductCode", "SpareProduct", "ProductCode2");

				productCode = args.get("ProductCode");
				productCode2 = args.get("ProductCode2");
				spareProduct = args.get("SpareProduct");

			} catch (Exception e) {
				this.addComment("Error while initializing VerifyUnselectAfterSaveConfigureProduct");
				throw new KDTKeywordInitException("Error while initializing VerifyUnselectAfterSaveConfigureProduct",
						e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				SeleniumTools.waitForWebElementVisible(driver, By.xpath(
						gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", productCode)), 30);

				// Clicking the first Product code check box
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);

				this.addComment("Successfully selected the Prodcut :" + productCode);

				// Clicking on first Product configure button
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_configure_btn", "{producdCode}",
						productCode, CURR_APP);
				this.addComment("successfully clicked the:" + productCode + "configuration button");

				if (SeleniumTools.waitForWebElementVisible(driver,
						By.xpath(gei.getProperty("product_code", CURR_APP).replace("{productCode}", productCode)), 60)
						&& SeleniumTools.waitForWebElementVisible(driver,
								By.xpath(gei.getProperty("configure_productpagename", CURR_APP)), 60)) {

					this.addComment("Succesfully verified producd code is displayed in the Configure product page");
				}

				// clicking Spares Tab
				eo.waitForWebElementVisible(driver, "xpath", "sparesTab", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "sparesTab", CURR_APP);
				this.addComment("Successfully Clicked on the: <b>sparesTab</b>");

				SeleniumTools.waitForWebElementVisible(driver, By.xpath(
						gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", spareProduct)), 30);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						spareProduct, CURR_APP);

				this.addComment("successfully clicked the Spare product " + spareProduct);

				// saving the product
				eo.clickElement(driver, "xpath", "save2", CURR_APP);
				this.addComment("Saving the selected product configuration");

				// unchecking the product
				SeleniumTools.waitForWebElementVisible(driver, By.xpath(
						gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", productCode)), 30);

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);

				this.addComment("Successfully un-selected the configured product" + productCode);

				// Clicking the second Product code check box
				SeleniumTools.waitForWebElementVisible(driver, By.xpath(
						gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", productCode2)), 30);
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode2, CURR_APP);

				this.addComment("Selected another product" + productCode2 + " in Configure product page");

				/*
				 * // Clicking on second Product configure button
				 * eo.clickElementAfterReplacingKeyValue(driver, "XPATH",
				 * "product_code_checkbox", "{producdCode}", productCode2, CURR_APP);
				 * addComment("successfully clicked the:" + productCode2 + "configure btn");
				 */

				// Clicking on First Product configure button
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
						productCode, CURR_APP);
				addComment("Selecting again the previously configured product " + productCode);

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_configure_btn", "{producdCode}",
						productCode, CURR_APP);
				addComment("successfully clicked the configure button for the product:" + productCode);

				// clicking Spares Tab
				eo.waitForWebElementVisible(driver, "xpath", "sparesTab", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "sparesTab", CURR_APP);

				this.addComment("Selected the spares tab");

				SeleniumTools.waitForWebElementVisible(driver, By.xpath(
						gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", spareProduct)), 30);

				try {
					driver.findElement(By
							.xpath(gei.getProperty("selctedCheckbox", CURR_APP).replace("{productCode}", spareProduct)))
							.isDisplayed();
					this.addComment("Successfully Verified the product configuration saved for the product "
							+ productCode + " is displayed in the spares tab");

				} catch (Exception e) {

					this.addComment(
							"Failed to verify the saved selection is intact after making a different selection");
					throw new KDTKeywordExecException(
							"Failed to verify the saved selection is intact after making a different selection", e);
				}

			} catch (Exception e) {
				this.addComment(
						"User is failed to verify the saved selection is intact after making a different selection");
				throw new KDTKeywordExecException(
						"User is failed to verify the saved selection is intact after making a different selection", e);

			}
		}

	}

	//////////////////////////////////////////////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyProdcutCheckedByDefault</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used check if the product is
	 * selected or not</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>productCode(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyProdcutCheckedByDefault extends Keyword {

		private String productCodeByfaultSelected[];
		private String productCodeByfaultNotSelected[];

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				if (hasArgs("ProductCodeByfaultSelected")) {
					productCodeByfaultSelected = args.get("ProductCodeByfaultSelected").split(";");

					for (int i = 0; i < productCodeByfaultSelected.length; i++) {
						try {

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "theProduct", "{productCode}",
									productCodeByfaultSelected[i], waiTime, CURR_APP);
							eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "theProduct", "{productCode}",
									productCodeByfaultSelected[i], CURR_APP);

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "selctedCheckbox", "{productCode}",
									productCodeByfaultSelected[i], 5, CURR_APP);
							this.addComment("The product " + productCodeByfaultSelected[i] + " is selected by default");

						}

						catch (Exception e) {
							this.addComment(
									"The product " + productCodeByfaultSelected[i] + " is not selected by default");
							throw new KDTKeywordInitException(
									"The product " + productCodeByfaultSelected[i] + " is not selected by default", e);
						}

					}
				}

				if (hasArgs("ProductCodeByfaultNotSelected")) {

					productCodeByfaultNotSelected = args.get("ProductCodeByfaultNotSelected").split(";");
					for (int j = 0; j < productCodeByfaultNotSelected.length; j++) {
						try {

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "theProduct", "{productCode}",
									productCodeByfaultNotSelected[j], waiTime, CURR_APP);
							eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "theProduct", "{productCode}",
									productCodeByfaultNotSelected[j], CURR_APP);

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "noNselctedCheckbox",
									"{productCode}", productCodeByfaultNotSelected[j], 5, CURR_APP);
							this.addComment(
									"The product " + productCodeByfaultNotSelected[j] + " is not selected by default");

						}

						catch (Exception e) {
							this.addComment("The product " + productCodeByfaultNotSelected[j]
									+ " is wrongly selected by default");
							throw new KDTKeywordInitException("The product " + productCodeByfaultNotSelected[j]
									+ " is wrongly selected by default", e);
						}

					}

				}
			} catch (Exception e) {
				this.addComment("User is failed to verify the default selection of product");
				throw new KDTKeywordExecException("User is failed to verify the default selection of product");
			}
		}

	}
	////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyCancelProductConfigure</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to select particular product in
	 * provided tab of product configuration page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ProductTab(Mandatory)</li></li>ProdcutCode(Optional)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyCancelandSaveProductConfigure extends Keyword {

		private String productTab = "";
		private String productCodeProductConfig[];
		private String saveFlag = "";
		private String productCodeConfigProdcut;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("ProductTab", "ProdcutCodeProdcutConfig");
			productTab = args.get("ProductTab");
			productCodeProductConfig = args.get("ProdcutCodeProdcutConfig").split(";");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			WebDriver driver = context.getWebDriver();
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				SeleniumTools.waitForWebElementToClickable(driver,
						By.xpath(gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", productTab)),
						waiTime);

				// Clicking on any tab in the Product Configure page

				try {

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
							productTab, CURR_APP);

					this.addComment("Successfully verified the display of tab " + productTab);
					this.addComment("Successfully clicked on tab " + productTab);
				} catch (Exception e) {
					this.addComment("Failed to click on the tab " + productTab);
					throw new KDTKeywordExecException("Failed to click on the tab " + productTab, e);
				}

				// Selecting any product from the Selected tab in Product configuration page

				try {
					for (String element : productCodeProductConfig) {

						SeleniumTools.waitForWebElementVisible(driver, By.xpath(
								gei.getProperty("product_code_checkbox", CURR_APP).replace("{producdCode}", element)),
								30);
						eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "product_code_checkbox",
								"{producdCode}", element, CURR_APP);
						eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox", "{producdCode}",
								element, CURR_APP);
						this.addComment("successfully selected the product " + element + " for the tab " + productTab);

					}

				}

				catch (Exception e) {
					this.addComment("Failed to select the product from the tab " + productTab);
					throw new KDTKeywordInitException("Failed to select the product from the tab" + productTab);

				}

				if (hasArgs("SaveFlag") && hasArgs("ProductCodeConfigProdcut")) {
					saveFlag = args.get("SaveFlag");
					productCodeConfigProdcut = args.get("ProductCodeConfigProdcut");

					if (saveFlag.equalsIgnoreCase("Cancel")) {
						try {

							eo.javaScriptScrollToViewElement(driver, "xpath", "cancelButton", CURR_APP);
							eo.clickElement(driver, "xpath", "cancelButton", CURR_APP);
							eo.waitForWebElementVisible(driver, "xpath", "verifyDisplayR6000", waiTime, CURR_APP);
							this.addComment("Successfully clicked on CANCEL button form Prodcut configuration page");
							this.addComment(
									"Successfully landed to Configure product page after click on CANCEL button");

						}

						catch (Exception e) {
							this.addComment("Failed to land in Configure product page after click on CANCEL");
							throw new KDTKeywordInitException(
									"Failed to land in Configure product page after click on CANCEL", e);
						}

						// Gong to click on the Wrench icon for the already selected product

						try {

							eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_configure_btn",
									"{producdCode}", productCodeConfigProdcut, CURR_APP);

							SeleniumTools.waitForWebElementVisible(driver,
									By.xpath(gei.getProperty("product_code", CURR_APP).replace("{productCode}",
											productCodeConfigProdcut)),
									60);
							this.addComment(
									"Succesfully verified product code is displayed in the Configure product page");

						}

						catch (Exception e) {

							this.addComment("Failed to land in Configure Product page with product id as "
									+ productCodeConfigProdcut);
							throw new KDTKeywordInitException(
									"Failed to land in Configure Product page with product id as "
											+ productCodeConfigProdcut);

						}

						// Clicking on any tab in the Product Configure page

						try {

							eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct",
									"{parentProductCode}", productTab, CURR_APP);

							this.addComment("Successfully verified the display of tab " + productTab);
							this.addComment("Successfully clicked on tab " + productTab);
						} catch (Exception e) {
							this.addComment("Failed to click on the tab " + productTab);
							throw new KDTKeywordExecException("Failed to click on the tab " + productTab, e);
						}

						// Check if the Spare product is not selected in the Spare tab

						try {
							for (String elemnt : productCodeProductConfig) {

								eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "theProduct", "{productCode}",
										elemnt, waiTime, CURR_APP);
								eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "theProduct",
										"{productCode}", elemnt, CURR_APP);

								eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "noNselctedCheckbox",
										"{productCode}", elemnt, 5, CURR_APP);
								this.addComment("The product " + elemnt + " is not selected by default");

							}

							this.addComment(
									"Successfully verified the Configuration getting cleared after click on Cancel button");

						} catch (Exception e) {
							this.addComment(
									"Failed to verify the Configuration getting cleared after click on Cancel button");
							throw new KDTKeywordExecException(
									"Failed to verify the Configuration getting cleared after click on Cancel button",
									e);
						}

					}

					if (saveFlag.equalsIgnoreCase("Save")) {
						try {
							/*
							 * Robot robot = new Robot(); System.out.println("About to zoom in"); for (int i
							 * = 0; i < 3; i++) { robot.keyPress(KeyEvent.VK_CONTROL);
							 * robot.keyPress(KeyEvent.VK_SUBTRACT); robot.keyRelease(KeyEvent.VK_SUBTRACT);
							 * robot.keyRelease(KeyEvent.VK_CONTROL);
							 * 
							 * }
							 */

							eo.wait(2);

							eo.javaScriptScrollToViewElement(driver, "xpath", "confifureProductSave", CURR_APP);
							eo.clickElement(driver, "xpath", "confifureProductSave", CURR_APP);

							eo.waitForWebElementVisible(driver, "xpath", "verifyDisplayR6000", waiTime, CURR_APP);
							this.addComment("Successfully clicked on SAVE button form Prodcut configuration page");
							this.addComment("Successfully landed to Configure product page after click on SAVE button");

						}

						catch (Exception e) {
							this.addComment("Failed to land in Configure product page after click on SAVE");
							throw new KDTKeywordInitException(
									"Failed to land in Configure product page after click on SAVE", e);
						}

						// Gong to click on the Wrench icon for the already selected product

						try {

							eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_configure_btn",
									"{producdCode}", productCodeConfigProdcut, CURR_APP);

							SeleniumTools.waitForWebElementVisible(driver,
									By.xpath(gei.getProperty("product_code", CURR_APP).replace("{productCode}",
											productCodeConfigProdcut)),
									60);
							this.addComment(
									"Succesfully verified product code is displayed in the Configure product page");

						}

						catch (Exception e) {

							this.addComment("Failed to land in Configure Product page with product id as "
									+ productCodeConfigProdcut);
							throw new KDTKeywordInitException(
									"Failed to land in Configure Product page with product id as "
											+ productCodeConfigProdcut);

						}

						// Clicking on any tab in the Product Configure page

						try {

							eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct",
									"{parentProductCode}", productTab, CURR_APP);

							this.addComment("Successfully verified the display of tab " + productTab);
							this.addComment("Successfully clicked on tab " + productTab);
						} catch (Exception e) {
							this.addComment("Failed to click on the tab " + productTab);
							throw new KDTKeywordExecException("Failed to click on the tab " + productTab, e);
						}

						// Check if the Spare product is not selected in the Spare tab

						try {

							for (String elemnt : productCodeProductConfig) {

								eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "theProduct", "{productCode}",
										elemnt, waiTime, CURR_APP);
								eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "theProduct",
										"{productCode}", elemnt, CURR_APP);

								eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "selctedCheckbox",
										"{productCode}", elemnt, 5, CURR_APP);
								this.addComment("The product " + elemnt + " is selected by default");

							}

							this.addComment(
									"Successfully verified the Configuration is intact after click on SAVE button");

						} catch (Exception e) {
							this.addComment("Failed to verify the Configuration is intact after SAVE");
							throw new KDTKeywordExecException("Failed to verify the Configuration is intact after SAVE",
									e);
						}

					}

				}

				// driver.navigate().refresh();
			}

			catch (Exception e) {

				this.addComment(
						"Failed to verify  once user clicks on cancel button on the Configure Products page, then the selected configuration should be discarded and page automatically navigates to Configure Products Page");
				throw new KDTKeywordExecException(
						"Failed to verify  once user clicks on cancel button on the Configure Products page, then the selected configuration should be discarded and page automatically navigates to Configure Products Page",
						e);

			}

		}

	}

	///////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyLockAndEditIcon</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to verify the display of lock
	 * and edit icon in each tab of Product Configuration Page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ProductTab(Mandatory)</li></li>ProdcutCode(Optional)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyLockAndEditIcon extends Keyword {

		private String productTab = "";
		private String defaultSelectWithLock[];
		private String defaultSelectWithOutLock[];
		private String notSelectedWithLock[];
		private String notSelectedWithOutLock[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			verifyArgs("ProductTab");
			productTab = args.get("ProductTab");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			WebDriver driver = context.getWebDriver();
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			SeleniumTools.waitForWebElementToClickable(driver,
					By.xpath(gei.getProperty("parentProduct", CURR_APP).replace("{parentProductCode}", productTab)),
					waiTime);

			// Clicking on any tab in the Product Configure page

			try {

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "parentProduct", "{parentProductCode}",
						productTab, CURR_APP);

				this.addComment("Successfully verified the display of tab " + productTab);
				this.addComment("Successfully clicked on tab " + productTab);
			} catch (Exception e) {
				this.addComment("Failed to click on the tab " + productTab);
				throw new KDTKeywordExecException("Failed to click on the tab " + productTab, e);
			}

			try {

				if (hasArgs("DefaultSelectWithLock")) {
					defaultSelectWithLock = args.get("DefaultSelectWithLock").split(";");

					for (String element : defaultSelectWithLock) {

						if (eo.isDisplayedAfterReplace(driver, "xpath", "selctedCheckbox", "{productCode}", element,
								CURR_APP)) {

							if (eo.isDisplayedAfterReplace(driver, "xpath", "editableField", "{ProductCode}", element,
									CURR_APP)) {

								this.addComment("Failed to verify the product " + element + " is locked in section "
										+ productTab);
								throw new KDTKeywordExecException("Failed to verify the product " + element
										+ " is locked in section " + productTab);
							}

							else {

								this.addComment("Successfully verified the product " + element
										+ " is selected bydefault and locked in section " + productTab);
							}

						}

						else {

							this.addComment("Failed to verify the check box is selected for " + element + " in section "
									+ productTab);
							throw new KDTKeywordExecException("Failed to verify the check box is  selected for "
									+ element + " in section " + productTab);
						}
					}

				}
				//////////////////////////

				if (hasArgs("DefaultSelectWithOutLock")) {
					defaultSelectWithOutLock = args.get("DefaultSelectWithOutLock").split(";");

					for (String element1 : defaultSelectWithOutLock) {

						if (element1.equalsIgnoreCase("RBK-HDD-4TB-B")) {

							if (eo.isDisplayedAfterReplace(driver, "xpath", "selctedCheckboxRBK_HDD_4TB_B",
									"{productCode}", element1, CURR_APP)) {

								if (eo.isDisplayedAfterReplace(driver, "xpath", "editableFieldRBK_HDD_4TB_B",
										"{ProductCode}", element1, CURR_APP)) {

									this.addComment("Successfully verified the product " + element1
											+ " is selected bydefault and editable in section " + productTab);

								}

								else {

									this.addComment("Failed to verify the check box is selected for product " + element1
											+ " in section " + productTab);
									throw new KDTKeywordExecException(
											"Failed to verify the check box is selected for product " + element1
													+ " in section " + productTab);
								}

							}

						}

						else if (eo.isDisplayedAfterReplace(driver, "xpath", "selctedCheckbox", "{productCode}",
								element1, CURR_APP)) {

							if (eo.isDisplayedAfterReplace(driver, "xpath", "editableField", "{ProductCode}", element1,
									CURR_APP)) {

								this.addComment("Successfully verified the product " + element1
										+ " is selected bydefault and editable in section " + productTab);

							}

							else {

								this.addComment("Failed to verify the check box is selected for product " + element1
										+ " in section " + productTab);
								throw new KDTKeywordExecException(
										"Failed to verify the check box is selected for product " + element1
												+ " in section " + productTab);
							}

						}

						else {

							this.addComment("Failed to verify the check box is selected for " + element1
									+ " in section " + productTab);
							throw new KDTKeywordExecException("Failed to verify the check box is selected for "
									+ element1 + " in section " + productTab);
						}

					}

				}
				//////////////////////////////

				if (hasArgs("NotSelectedWithLock")) {
					notSelectedWithLock = args.get("NotSelectedWithLock").split(";");

					for (String element2 : notSelectedWithLock) {

						if (eo.isDisplayedAfterReplace(driver, "xpath", "noNselctedCheckbox", "{productCode}", element2,
								CURR_APP)) {

							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox", "{ProductCode}",
									element2, CURR_APP);

							if (eo.isDisplayedAfterReplace(driver, "xpath", "editableField", "{ProductCode}", element2,
									CURR_APP)) {

								this.addComment("Failed to verify the product " + element2 + " is locked in section "
										+ productTab);
								throw new KDTKeywordExecException("Failed to verify the product " + element2
										+ " is locked in section " + productTab);
							}

							else {

								this.addComment("Successfully verified the product " + element2
										+ " is not selected bydefault but locked in section " + productTab);
							}

						}

						else {

							this.addComment("Failed to verify the check box is not selected for " + element2
									+ " in section " + productTab);
							throw new KDTKeywordExecException("Failed to verify the check box is not selected for "
									+ element2 + " in section " + productTab);

						}

					}
				}
				///////////////////////////////////////////////////////

				if (hasArgs("NotSelectedWithOutLock")) {

					notSelectedWithOutLock = args.get("NotSelectedWithOutLock").split(";");

					for (String element3 : notSelectedWithOutLock) {

						if (eo.isDisplayedAfterReplace(driver, "xpath", "noNselctedCheckbox", "{productCode}", element3,
								CURR_APP)) {

							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox", "{ProductCode}",
									element3, CURR_APP);

							eo.javaScriptScrollToViewElement(driver, "xpath", "configure_productpagename", CURR_APP);
							eo.clickElement(driver, "xpath", "configure_productpagename", CURR_APP);

							// eo.actionMoveToElementAfterReplace(driver, "XPATH", "moveToEditable",
							// "{ProductCode}", element3, CURR_APP);

							eo.actionMoveToElementAfterReplace(driver, "XPATH", "editableField", "{ProductCode}",
									element3, CURR_APP);

							eo.wait(2);

							Boolean blnEditEltExists = (eo.isExistsAfterReplace(driver, "XPATH", "editableField",
									"{ProductCode}", element3, CURR_APP));

							if (blnEditEltExists) {

								this.addComment("Successfully verified the product " + element3
										+ " is not selected by default and is editable in section " + productTab);

							}

							/*
							 * eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "editableField",
							 * "{ProductCode}",element3, 150, CURR_APP);
							 * 
							 * if (eo.isDisplayedAfterReplace(driver, "xpath", "editableField",
							 * "{ProductCode}", element3,CURR_APP)) {
							 * 
							 * this.addComment("Successfully verified the product " + element3 +
							 * " is not selected by default and is editable in section " + productTab);
							 * 
							 * }
							 */

							else {

								this.addComment("Failed to verify the product " + element3 + " is editable in section "
										+ productTab);
								throw new KDTKeywordExecException("Failed to verify the product " + element3
										+ " is editable in section " + productTab);
							}

						}

						else {

							this.addComment("Failed to verify the check box is not selectd for " + element3
									+ " in section " + productTab);
							throw new KDTKeywordExecException("Failed to verify the check box is not selectd for "
									+ element3 + " in section " + productTab);

						}

					}
				}
			} catch (Exception e) {
				this.addComment(
						"Failed to Verify that on Product Configure page, when user hovers mouse over below listed products, a lock icon should be displayed");
				throw new KDTKeywordExecException(
						"Failed to verifyVerify that on Product Configure page, when user hovers mouse over below listed products, a lock icon should be displayed",
						e);
			}

		}

	}
	/////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>UpdateConfigurableProductsQuantityAndClickSave</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to update the quantity of
	 * product in Configuration page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ProductTab(Mandatory)</li></li>ProdcutCode(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class UpdateConfigurableProductsQuantityAndClickSave extends Keyword {

		private String productCodeForUpdateQty;
		private String quantityToUpdate;
		private String subTabName[];
		private String tabWiseProduct[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ProductCodeForUpdateQty", "QuantityToUpdate", "SubTabName", "TabWiseProduct");
				productCodeForUpdateQty = args.get("ProductCodeForUpdateQty");
				quantityToUpdate = args.get("QuantityToUpdate");
				subTabName = args.get("SubTabName").split(";");
				tabWiseProduct = args.get("TabWiseProduct").split(":");

			} catch (Exception e) {
				this.addComment("Error while initializing UpdateConfigurableProductsQuantityAndClickSave");
				throw new KDTKeywordInitException(
						"Error while initializing UpdateConfigurableProductsQuantityAndClickSave", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				// Click on the check box for the given product

				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "productCheckBox", "{ProductCode}",
						productCodeForUpdateQty, waiTime, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox", "{ProductCode}",
						productCodeForUpdateQty, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox", "{ProductCode}",
						productCodeForUpdateQty, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox", "{ProductCode}",
						productCodeForUpdateQty, CURR_APP);
				this.addComment("Clicked on the check box for the product " + productCodeForUpdateQty);

			
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "qttyUpdate", "{productcode}",
						productCodeForUpdateQty, quantityToUpdate, CURR_APP);
				this.addComment("Entered the quantity as " + quantityToUpdate + "for the product " + productCodeForUpdateQty);

				// Click on Configure icon button

				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_configure_btn", "{producdCode}",
						productCodeForUpdateQty, CURR_APP);
				this.addComment("Clicked on Configure button for the product " + productCodeForUpdateQty);

				// Verify landed to Product Config page

				eo.waitForWebElementVisible(driver, "xpath", "rcdmTab", waiTime, CURR_APP);
				this.addComment("Successfully landed to Product Configuration page");

				int tabSize = subTabName.length;
				List<String> quantityUpdatedProducts = new ArrayList<>();

				for (int i = 0; i < tabSize; i++) {

					// Clicking on the sub tab name
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "parentProduct", "{parentProductCode}",
							subTabName[i], CURR_APP);
					this.addComment("Clicked on the tab " + subTabName[i]);

					// Getting the quantity of all items

					String eachTabItem[] = tabWiseProduct[i].split(";");

					for (int j = 0; j < eachTabItem.length; j++) {
						eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "theProduct", "{productCode}",
								eachTabItem[j], waiTime, CURR_APP);
						eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "theProduct", "{productCode}",
								eachTabItem[j], CURR_APP);

						ExpectedConditions.visibilityOf(driver.findElement(By.xpath(gei
								.getProperty("qttyColumnClick", CURR_APP).replace("{ProductCode}", eachTabItem[j]))));

						String actualQuantity = driver.findElement(By.xpath(
								gei.getProperty("qttyColumnClick", CURR_APP).replace("{ProductCode}", eachTabItem[j])))
								.getText();

						String expectedQuantity = "1";

						if (actualQuantity.equalsIgnoreCase(expectedQuantity)) {

							this.addComment("Successfully verified the quantity is displaying as " + expectedQuantity
									+ " for the product" + eachTabItem[j] + " in tab" + subTabName[i]);

						} else {

							this.addComment("Failed to verify the quantity as " + expectedQuantity + "for the product "
									+ eachTabItem[j] + "in tab" + subTabName[i]);
							throw new KDTKeywordExecException("Failed to verify the quantity as " + expectedQuantity
									+ "for the product " + eachTabItem[j] + " in tab" + subTabName[i]);

						}

						if (eo.isDisplayedAfterReplace(driver, "xpath", "noNselctedCheckbox", "{productCode}",
								eachTabItem[j], CURR_APP)) {

							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox", "{ProductCode}",
									eachTabItem[j], CURR_APP);

							eo.javaScriptScrollToViewElement(driver, "xpath", "configure_productpagename", CURR_APP);
							eo.clickElement(driver, "xpath", "configure_productpagename", CURR_APP);

							if (eo.isDisplayedAfterReplace(driver, "xpath", "editableField", "{ProductCode}",
									eachTabItem[j], CURR_APP)) {

								this.addComment("Successfully verified the product " + eachTabItem[j]
										+ " is editable in section " + subTabName[i]);
								eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox",
										"{ProductCode}", eachTabItem[j], CURR_APP);
								eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productCheckBox",
										"{ProductCode}", eachTabItem[j], CURR_APP);
								eo.enterTextAfterReplacingKeyValue(driver, "xpath", "qttyUpdate", "{productcode}",
										eachTabItem[j], quantityToUpdate, CURR_APP);
								this.addComment("Successfully edited the product " + eachTabItem[j] + " quantity as "
										+ quantityToUpdate + " in section " + subTabName[i]);

								quantityUpdatedProducts.add(eachTabItem[j]);

							}

							else {

								this.addComment("The product " + eachTabItem[j] + " is not editable in section "
										+ subTabName[i]);

							}

						}

						else {

							this.addComment("The check box is bydefault selectd for " + eachTabItem[j] + " in section "
									+ subTabName[i]);

						}

					}

					for (int k = 0; k < quantityUpdatedProducts.size(); k++) {
						this.addComment(
								"The products which quantity updated in product configuration page are as followes:"
										+ quantityUpdatedProducts.get(k));
					}

				}

				// Click on SAVE button
				eo.waitForPopUp(driver);
				eo.clickElement(driver, "xpath", "confifureProductSave", CURR_APP);
				eo.waitForPopUp(driver);

				try {
					eo.clickElement(driver, "xpath", "confifureProductSave", CURR_APP);
				} catch (Exception e) {
					this.addComment("The page loading taking more time than 10 seconds");

				}

				eo.waitForPopUp(driver);

				this.addComment("Successfully saved the product configuration");

				try {
					eo.waitForWebElementVisible(driver, "xpath", "addProducts_button", 120, CURR_APP);
				}

				catch (Exception e) {

					this.addComment("The page loading taking more time than 10 seconds");

				}

				/*
				 * for(String element:quantityUpdatedProducts) {
				 * ExpectedConditions.visibilityOfElementLocated(By.xpath(gei.getProperty(
				 * "productInEditQuote", CURR_APP).replace("{product}", element)));
				 * 
				 * }
				 */

				for (String element : quantityUpdatedProducts) {

					eo.wait(5);

					eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "productInEditQuote", "{product}",
							element, CURR_APP);
					// eo.javaScriptScrollToViewElement(driver,
					// driver.findElement(By.xpath(gei.getProperty("productInEditQuote",
					// CURR_APP).replace("{product}", element))));

					eo.wait(5);

					if (eo.getTextAfterReplacingKeyValue(driver, "xpath", "qqtyFromEditQuotePage", "{ProductCode}",
							element, CURR_APP).equalsIgnoreCase(quantityToUpdate)) {
						this.addComment("Successfully verified the updated qtty of configurable product: " + element
								+ " is displaying in edit quote page");

					}
					break;
				}

			}

			catch (Exception e) {

				this.addComment("Failed to update and verify the product quantity after save from Configuration page");
				throw new KDTKeywordExecException(
						"Failed to update and verify the product quantity after save from Configuration page", e);

			}

		}
	}

	////////////////////////// ValidateColumsAvailableOnEditQuotepage////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateColumsAvailableOnEditQuotepage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to verify the column names of
	 * edit quote page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ColumnNames(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh and Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateColumsAvailableOnEditQuotepage extends Keyword {

		private String columnNames[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ColumnNames");
				columnNames = args.get("ColumnNames").split(",");

			} catch (Exception e) {
				this.addComment("Error while initializing ValidateColumsAvailableOnEditQuotepage");
				throw new KDTKeywordInitException("Error while initializing ValidateColumsAvailableOnEditQuotepage ",
						e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			try {
				WebDriver driver = context.getWebDriver();

				// Capturing the All Column names from QuoteEditPage.

				List<WebElement> appcolumnnames = driver
						.findElements(By.xpath(gei.getProperty("editquote_columns", CURR_APP)));

				for (int i = 0; i < appcolumnnames.size(); i++) {
					String Expcolnames = columnNames[i];
					String Actualcolnames = appcolumnnames.get(i).getText();

					// verifiaction
					if (Expcolnames.equalsIgnoreCase(Actualcolnames)) {
						this.addComment("Verified Column Name :" + "<b>" + Expcolnames + "</b>"
								+ " is available in Edit Quote page");
					}

					else {
						this.addComment("Verified Column Name:" + "<b>" + Expcolnames + "</b>"
								+ " is not available in  Quote Edit page");
					}

				}

			} catch (Exception e) {
				addComment("Column name verification in edit quote page  failed");
				throw new KDTKeywordExecException("Column name verification in edit quote page  failed", e);
			}
		}
	}

	////////////////////////////// EditQuotePage_Sort//////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>EditQuotePageSort</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to sort the products in edit
	 * quote page wrt to Product Name</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ColumnNames(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh and Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class EditQuotePageSort extends Keyword {

		private String productNametoSort;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ProductNametoSort");

				productNametoSort = args.get("ProductNametoSort");

			} catch (Exception e) {
				this.addComment("Error while initializing EditQuotePageSort");
				throw new KDTKeywordInitException("Error while initializing EditQuotePageSort ", e);
			}

		}

		@Override

		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			WebDriver driver = context.getWebDriver();

			try {

				// Capturing the Elements with default sorting order
				ArrayList<String> actualList = new ArrayList<>();
				ExpectedConditions
						.visibilityOfAllElements(eo.getListOfWebElements(driver, "xpath", "quoteProducts", CURR_APP));
				List<WebElement> elementList = eo.getListOfWebElements(driver, "xpath", "quoteProducts", CURR_APP);

				for (WebElement we : elementList) {

					actualList.add(we.getText());

				}

				this.addComment(
						"Successfully Captured the Default display items as follows:" + "<b>" + actualList + "</b>");

				// sorting the product elements
				Collections.sort(actualList);

				this.addComment(
						"Sorted the Captured Default display items internally as:" + "<b>" + actualList + "</b>");

				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "product_sort", "{product_sort}",
						productNametoSort, waiTime, CURR_APP);

				// clicking the sort icon for Product Name column

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "product_sort", "{product_sort}",
						productNametoSort, CURR_APP);
				this.addComment(
						"Successfully Clicked on the Sort icon over the tab:" + "<b>" + productNametoSort + "</b>");

				// Capturing the elements After clicking the Sorting order in Quote Edit PAge
				ArrayList<String> obtainedList2 = new ArrayList<>();
				List<WebElement> elementList2 = eo.getListOfWebElements(driver, "xpath", "quoteProducts", CURR_APP);

				for (WebElement we1 : elementList2) {

					obtainedList2.add(we1.getText());

				}

				int sizeOfList = obtainedList2.size();
				this.addComment("Successfully Captured the items after click on Sort over tab:" + "<b>"
						+ productNametoSort + "as follows:" + obtainedList2 + "</b>");

				// verification part
				try {

					for (int i = 0; i < sizeOfList; i++) {

						if (obtainedList2.get(i).equalsIgnoreCase(actualList.get(i))) {

							this.addComment("Successfully verified the Sorting is working as expected ");
							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "product_sort", "{product_sort}",
									productNametoSort, CURR_APP);

						}
					}

				} catch (Exception e) {
					this.addComment("Failed to verify the Sorting in edit quote page");
					throw new KDTKeywordExecException("Failed to verify the Sorting in edit quote page", e);
				}

			} catch (Exception e) {
				this.addComment("User is unable to verify the Sorting the Prodcuts in Edit Quote Page");
				throw new KDTKeywordExecException(
						"User is unable to verify the Sorting the Prodcuts in Edit Quote Page", e);

			}
		}

	}

	/////////////////////////////////// Update Product Quantity on edit quote
	/////////////////////////////////// page//////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>UpdateProductQuantityOnEditQuotePage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to update the quantity of
	 * products in the edit quote page and verify</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ProductQuantity(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh and Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class UpdateProductQuantityOnEditQuotePage extends Keyword {

		private String productQuantity;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ProductQuantity");
				productQuantity = args.get("ProductQuantity");

			} catch (Exception e) {
				this.addComment("Error while initializing UpdateProductQuantityOneditQuotePage");
				throw new KDTKeywordInitException("Error while initializing UpdateProductQuantityOneditQuotePage ", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			try {

				List<WebElement> listOfAllProducts = eo.getListOfWebElements(driver, "xpath", "product_text", CURR_APP);
				int totalProductsNos = listOfAllProducts.size();
				ArrayList<String> allProdcutNames = new ArrayList<>();

				for (WebElement element : listOfAllProducts) {
					allProdcutNames.add(element.getText().trim());

				}

				for (String ele : allProdcutNames) {
					this.addComment(ele);
				}

				for (int i = 0; i < totalProductsNos; i++) {

					eo.actionMoveToElementAfterReplace(driver, "xpath", "Product_text2", "{product}",
							allProdcutNames.get(i), CURR_APP);

					if (eo.isDisplayedAfterReplace(driver, "xpath", "editquote_productquantity", "{product}",
							allProdcutNames.get(i), CURR_APP)) {

						eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "Product_text2", "{product}",
								allProdcutNames.get(i), waiTime, CURR_APP);
						eo.javaScriptScrollToViewElementAfterReplace(driver, "xpath", "Product_text2", "{product}",
								allProdcutNames.get(i), CURR_APP);
						eo.actionMoveToElementAfterReplace(driver, "xpath", "Product_text2", "{product}",
								allProdcutNames.get(i), CURR_APP);

						eo.actionMoveToElementAfterReplace(driver, "xpath", "editquote_productquantity", "{product}",
								allProdcutNames.get(i), CURR_APP);
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "editquote_productquantity", "{product}",
								allProdcutNames.get(i), CURR_APP);
						addComment("Successfully Clicked the Quantity");

						eo.clearDataAfterReplacingKeyValue(driver, "XPATH", "Edit_quantity", "{product}",
								allProdcutNames.get(i), CURR_APP);
						eo.waitForPopUp(driver);

						addComment("Successfully Cleared the Qauntity");

						try {
							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "editquote_productquantity",
									"{product}", allProdcutNames.get(i), CURR_APP);
						}

						catch (Exception e) {

							this.addComment("The page loading taking more time than 10 seconds");

						}

						eo.enterTextAfterReplacingKeyValue(driver, "xpath", "Edit_quantity", "{product}",
								allProdcutNames.get(i), productQuantity, CURR_APP);

						addComment("<b>" + allProdcutNames.get(i) + "</b>" + "Quantity:" + "<b>" + productQuantity
								+ "</b> is updated successfully");
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Product_text2", "{product}",
								allProdcutNames.get(i), CURR_APP);
						eo.waitForPopUp(driver);

					} else {

						addComment(allProdcutNames.get(i) + ":" + "is not Editable");
					}

				}

			} catch (Exception e) {
				addComment("Failed to Update the Product quantity");
				throw new KDTKeywordExecException("Failed to Update the Product quantity", e);
			}

		}

	}

	////////////////////////// ValidateColumsAvailableOnEditQuotepage////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>UpdateAndVerifySubscriptionEditQuotePage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to verify the column names of
	 * edit quote page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>ColumnNames(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh and Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class UpdateAndVerifySubscriptionEditQuotePage extends Keyword {

		private String productCanHaveSubscription[];
		private String subscriptionTerm;
		private String saveOrQuickSaveorCancel;
		private String doYouNeedToCaptureMessage;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("ProductCanHaveSubscription", "SubscriptionTerm");
				productCanHaveSubscription = args.get("ProductCanHaveSubscription").split(";");
				subscriptionTerm = args.get("SubscriptionTerm");

			} catch (Exception e) {
				this.addComment("Error while initializing UpdateAndVerifySubscriptionEditQuotePage");
				throw new KDTKeywordInitException("Error while initializing UpdateAndVerifySubscriptionEditQuotePage ",
						e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			try {
				WebDriver driver = context.getWebDriver();

				// Enter the Subscription term in the field

				eo.waitForWebElementVisible(driver, "xpath", "subscription_Term", 120, CURR_APP);
				eo.clearData(driver, "xpath", "subscription_Term", CURR_APP);
				eo.enterText(driver, "xpath", "subscription_Term", subscriptionTerm, CURR_APP);
				this.addComment("Successfully entered the subscription term as " + subscriptionTerm);

				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_SHIFT);
	     	    robot.keyRelease(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_SHIFT);

				eo.waitForPopUp(driver);

				if (hasArgs("SaveOrQuickSaveorCancel")) {

					saveOrQuickSaveorCancel = args.get("SaveOrQuickSaveorCancel");

					switch (saveOrQuickSaveorCancel) {

					case "Quick Save":

						// statement sequence
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "saveOrQuickSaveorCancel",
								"{buttonName}", saveOrQuickSaveorCancel, CURR_APP);
						eo.waitForPopUp(driver);
						eo.waitForPopUp(driver);
						eo.waitForPopUp(driver);

						// Check if the subscription updated as expected

						for (int i = 0; i < productCanHaveSubscription.length; i++) {

							// getting the actual subscription displayed in application for the products

							for (int j = 0; j < productCanHaveSubscription.length; j++) {

								ExpectedConditions.visibilityOf(
										driver.findElement(By.xpath(gei.getProperty("productWithSubscription", CURR_APP)
												.replace("{productCode}", productCanHaveSubscription[j]))));

							}

							String actualTermFromApplication = eo.getTextAfterReplacingKeyValue(driver, "xpath",
									"productWithSubscription", "{productCode}", productCanHaveSubscription[i],
									CURR_APP);

							if (actualTermFromApplication.equalsIgnoreCase(subscriptionTerm)) {

								this.addComment(
										"Successfully verified the subscription term as provided for the product "
												+ productCanHaveSubscription[i]);

							}

							else {

								this.addComment("Failed to verify the subscription term as provided for the product "
										+ productCanHaveSubscription[i]);
								throw new KDTKeywordExecException(
										"Failed to verify the subscription term as provided for the product "
												+ productCanHaveSubscription[i]);

							}

						}

						break;

					case "Save":

						// statement sequence
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "saveOrQuickSaveorCancel",
								"{buttonName}", saveOrQuickSaveorCancel, CURR_APP);

						break;

					case "Cancel":
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "saveOrQuickSaveorCancel",
								"{buttonName}", saveOrQuickSaveorCancel, CURR_APP);
						// statement sequence

						break;

					default:

						// default statement sequence
						this.addComment(
								"User not expect any action after provide the subscription term in Edit quote page");

					}

				}

				if (hasArgs("DoYouNeedToCaptureMessage")) {

					doYouNeedToCaptureMessage = args.get("DoYouNeedToCaptureMessage");
					String actualValidationMessage;

					switch (doYouNeedToCaptureMessage) {

					case "Yes":

						// statement sequence
						actualValidationMessage = eo.getText(driver, "xpath", "validationMessageForSubscription",CURR_APP);
						saveValue(actualValidationMessage);
						context.getData().get("$actualValidationMessage");
						addComment("Actual Validation Message" + context.getData().get("$actualValidationMessage"));
						eo.clickElement(driver, "xpath", "closePopUp", CURR_APP);

						break;

					case "No":

						// statement sequence
						this.addComment("User doesnt want to capture the message");
						
					case "Yes For Prompt":

						// statement sequence
						actualValidationMessage = eo.getText(driver, "xpath", "promptMessage", CURR_APP);
						saveValue(actualValidationMessage);
						context.getData().get("$actualValidationMessage");
						addComment("Actual Validation Message" + context.getData().get("$actualValidationMessage"));
						eo.clickElement(driver, "xpath", "cancelPrompt", CURR_APP);
						
						this.addComment("User has been captured the alert message and saved to variable");
						break;

					default:

						// default statement sequence
						System.out.println("User doesnt want to capture any message");

					}

				}

			}

			catch (Exception e)

			{
				addComment("Failed to update the subscription term and verify");
				throw new KDTKeywordExecException("Failed to update the subscription term and verify", e);
			}
		}
	}

	/////////////////////////////////// UpdateDiscountOnEditQuotePage///////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>UpdateDiscountOnEditQuotePage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword used to verify the discount
	 * acceptance values</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>AlldiscountValues(Mandatory)</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Maheshk</i></b>
	 * </p>
	 * </div>
	 */

	public static class UpdateDiscountOnEditQuotePage extends Keyword {

		private String alldiscountValues[];
		private String discountValuesWithFlag[];

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("AlldiscountValues");
				alldiscountValues = args.get("AlldiscountValues").split(";");

			} catch (Exception e) {
				this.addComment("Error while initializing UpdateDiscountOnEditQuotePage");
				throw new KDTKeywordInitException("Error while initializing UpdateDiscountOnEditQuotePage", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				List<WebElement> listOfAllProducts = eo.getListOfWebElements(driver, "xpath", "product_text", CURR_APP);
				int totalProductsNos = listOfAllProducts.size();
				ArrayList<String> allProdcutNames = new ArrayList<>();

				for (WebElement element : listOfAllProducts) {
					allProdcutNames.add(element.getText().trim());

				}

				for (String element2 : allProdcutNames) {
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath(gei.getProperty("edit_discount", CURR_APP).replace("{product}", element2)));

				}

				for (int j = 0; j < alldiscountValues.length; j++) {

					discountValuesWithFlag = alldiscountValues[j].split(":");
					String discount = discountValuesWithFlag[j];
					String flag = discountValuesWithFlag[j + 1];

					for (int i = 0; i < totalProductsNos; i++) {

						try {
							eo.actionMoveToElementAfterReplace(driver, "xpath", "Product_text2", "{product}",
									allProdcutNames.get(i), CURR_APP);
						}

						catch (Exception e) {

							this.addComment("The page loading taking more time than 10 seconds");

						}

						eo.waitForPopUp(driver);

						if (eo.isDisplayedAfterReplace(driver, "xpath", "edit_discount", "{product}",
								allProdcutNames.get(i), CURR_APP)) {

							eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "edit_discount", "{product}",
									allProdcutNames.get(i), waiTime, CURR_APP);
							eo.actionMoveToElementAfterReplace(driver, "xpath", "edit_discount", "{product}",
									allProdcutNames.get(i), CURR_APP);
							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "edit_discount", "{product}",
									allProdcutNames.get(i), CURR_APP);
							addComment("Successfully Clicked the Additional Discount");

							eo.enterTextAfterReplacingKeyValue(driver, "xpath", "edit_discount_input", "{product}",
									allProdcutNames.get(i), discount, CURR_APP);

							this.addComment("Successfully added the discount as " + "<b>" + discount + "</b>"
									+ " for the product" + "<b>" + allProdcutNames.get(i) + "</b>");

							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Product_text2", "{product}",
									allProdcutNames.get(i), CURR_APP);
							eo.waitForPopUp(driver);

							if (flag.equalsIgnoreCase("ACCEPT")) {

								if (eo.getTextAfterReplacingKeyValue(driver, "xpath", "text", "{product}",
										allProdcutNames.get(i), CURR_APP).equalsIgnoreCase(discount)) {

									this.addComment("Successfully verified the discount " + discount + "is accepted");
								}

								else {

									this.addComment("Failed to accept the discount " + discount);
									throw new KDTKeywordExecException("Failed to accept the discount " + discount);
								}
							}

							if (flag.equalsIgnoreCase("NOTACCEPT")) {

								String actual = eo.getTextAfterReplacingKeyValue(driver, "xpath", "text", "{product}",
										allProdcutNames.get(i), CURR_APP);

								if (actual.equalsIgnoreCase("")) {

									this.addComment("Successfully verified the discount " + discount + "is accepted");
								}

								else {

									this.addComment("Failed to accept the discount " + discount);
									throw new KDTKeywordExecException("Failed to accept the discount " + discount);
								}
							}

						} else {

							this.addComment(
									"Additional Discount of the product" + allProdcutNames.get(i) + "is not Editable");
						}

					}

				}

			} catch (Exception e) {
				addComment("Failed to UpdateDiscountOnEditQuotePage");
				throw new KDTKeywordExecException("Failed to UpdateDiscountOnEditQuotePage", e);
			}

		}

	}

	///////////////////////////////////////// ValidateNetTotalAndSubTotal///////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateNetTotalAndSubTotal</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to compare Net Total and
	 * subtotal amount on Edit Quote page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>Edit Quote</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh</i></b>
	 * </p>
	 * </div>
	 */

	// TS-REG-30_010

	public static class ValidateNetTotalAndSubTotal extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {
				eo.waitForWebElementVisible(driver, "xpath", "netAmount", waiTime, CURR_APP);
				DecimalFormat df = new DecimalFormat("0.00");
				List<WebElement> netValues = driver.findElements(By.xpath(gei.getProperty("netAmount", CURR_APP)));
				double sum = 0;
				int count = 1;

				// capturing the individual prices
				for (int i = 1; i <= netValues.size(); i++) {
					String netValuesAmount = netValues.get(i).getAttribute("textContent").replace("$", "").replace(",",
							"");
					System.out.println(netValuesAmount);
					float floatnumber = Float.parseFloat(netValuesAmount);
					sum += floatnumber;
					count++;
					if (count == 13) {
						break;
					}
				}

				for (int j = 1; j <= 2; j++) {
					String netValuesAmount = netValues.get(j).getAttribute("textContent").replace("$", "").replace(",",
							"");
					System.out.println(netValuesAmount);
					float floatnumber = Float.parseFloat(netValuesAmount);
					sum += floatnumber;

				}

				String inLineNetTotal = df.format(sum);
				System.out.println(inLineNetTotal);

				String SubTotal = (eo.getText(driver, "XPATH", "line_subTotal", CURR_APP)).replace("$", "").replace(",",
						"");
				System.out.println("Total: " + SubTotal);

				// verication
				if (inLineNetTotal.equalsIgnoreCase(SubTotal)) {
					addComment("Quote Nettotal is <b>" + "$" + inLineNetTotal + "</b>");
					addComment("Line sub  total is <b>" + "$" + SubTotal + "</b>");
					addComment("Sussessfully verified quote nettotal and line sub total are same ");
				} else {
					addComment("Quote total is <b>" + "$" + SubTotal + "</b>");
					addComment("Line sub  total is <b>" + "$" + inLineNetTotal + "</b>");
					throw new KDTKeywordExecException("Quote total and line sub total are not same");
				}
			}

			catch (Exception e) {
				addComment("Net Toatal And Sub Total Amount is not same");
				throw new KDTKeywordExecException("Net Toatal And Sub Total Amount is not same");
			}
		}
	}

	//////////////////////////////////////// SelectAnyConfigureProduct/////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SelectAnyConfigureProduct</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to select any product
	 * [Argument] to select from the configure tabs</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>VerifyNavigateToProductConfigurePage</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class SelectAnyConfigureProduct extends Keyword {

		private String accessoriesProduct;
		private String supportProduct;
		private String spareProduct;
		private String quantity;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {

				verifyArgs("Quantity");

				if (hasArgs("Accessories")) {
					accessoriesProduct = args.get("Accessories");
				}
				if (hasArgs("Support")) {
					supportProduct = args.get("Support");
				}
				if (hasArgs("Spare")) {
					spareProduct = args.get("Spare");
				}

				quantity = args.get("Quantity");

			} catch (Exception e) {
				this.addComment("Error while initializing SelectAnyConfigureProduct");
				throw new KDTKeywordInitException("Error while initializing SelectAnyConfigureProduct", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			{
				// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

				try {

					WebDriver driver = context.getWebDriver();

					eo.waitForWebElementVisible(driver, "xpath", "spare_Tab", 120, CURR_APP);

					try {

						if (accessoriesProduct.length() > 0) {
							String[] accessoriesProductName = accessoriesProduct.split(";");
							eo.clickElement(driver, "XPATH", "Accessories_Tab", CURR_APP);

							for (int i = 0; i < accessoriesProductName.length; i++) {
								eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox",
										"{producdCode}", accessoriesProductName[i], CURR_APP);

							}
						}

					} catch (Exception e) {
						this.addComment("There is no accessoried product provided by the user");
					}

					try {

						if (supportProduct.length() > 0) {
							String[] supportProductName = supportProduct.split(";");
							eo.clickElement(driver, "XPATH", "Support_Tab", CURR_APP);

							for (int i = 0; i < supportProductName.length; i++) {
								eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox",
										"{producdCode}", supportProductName[i], CURR_APP);

								eo.clearDataAfterReplacingKeyValue(driver, "XPATH", "quantityUpdate", "{productCode}",
										supportProductName[i], CURR_APP);
								eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox",
										"{producdCode}", supportProductName[i], CURR_APP);
								eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox",
										"{producdCode}", supportProductName[i], CURR_APP);
								eo.enterTextAfterReplacingKeyValue(driver, "XPATH", "quantityUpdate", "{productCode}",
										supportProductName[i], quantity, CURR_APP);
								this.addComment("selected the support product " + supportProductName[i]);

							}
						}

					}

					catch (Exception e) {

						this.addComment("There is no support product provided by the user");
					}

					try {
						if (spareProduct.length() > 0) {
							String[] spareProductName = spareProduct.split(";");
							eo.clickElement(driver, "XPATH", "spare_Tab", CURR_APP);
							// eo.wait(1);
							for (int i = 0; i < spareProductName.length; i++) {
								eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox",
										"{producdCode}", spareProductName[i], CURR_APP);
								eo.clearDataAfterReplacingKeyValue(driver, "XPATH", "quantityUpdate", "{productCode}",
										spareProductName[i], CURR_APP);
								eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox",
										"{producdCode}", spareProductName[i], CURR_APP);
								eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "product_code_checkbox",
										"{producdCode}", spareProductName[i], CURR_APP);
								eo.enterTextAfterReplacingKeyValue(driver, "XPATH", "quantityUpdate", "{productCode}",
										spareProductName[i], quantity, CURR_APP);
								this.addComment("selected the spare product " + spareProductName[i]);
							}
						}
					}

					catch (Exception e) {

						this.addComment("There is no spare product provided by the user");
					}

					eo.waitForPopUp(driver);
					// Click on Save form Configure Product page
					eo.clickElement(driver, "xpath", "confifureProductSave", CURR_APP);
					this.addComment("Saved the Product configuration page");

					// Click on Save form Configure Product page

					eo.waitForPopUp(driver);
					eo.waitForPopUp(driver);

					eo.clickElement(driver, "xpath", "confifureProductSave", CURR_APP);
					this.addComment("Saved the configuration page");
					eo.waitForPopUp(driver);
					eo.waitForPopUp(driver);

				}

				catch (Exception e) {

					this.addComment("User is Unable to select the required configuration");
					throw new KDTKeywordExecException("User is Unable to select the required configuration", e);

				}

			}
		}

	}

	//////////////////////// SelectLinkFromAllTabs///////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SelectLinkFromAllTabs</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will click on the tabs from home page
	 * and select the tab what you have mentioned in the test input</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class SelectLinkFromAllTabs extends Keyword {

		private String subTab;
		private String linkName;
		private String buttonName;
		private String linkOrButton;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {

				verifyArgs("SubTab");

				subTab = args.get("SubTab");
				linkName = args.get("LinkName");
				buttonName = args.get("ButtonName");
				linkOrButton = args.get("LinkOrButton");

			} catch (Exception e) {
				this.addComment("Error while initializing SelectLinkFromAllTabs");
				throw new KDTKeywordInitException("Error while initializing SelectLinkFromAllTabs", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// Click on all tab arrow

				eo.waitForWebElementVisible(driver, "XPATH", "allTabArrow", waiTime, CURR_APP);
				eo.javaScriptClick(driver, "xpath", "allTabArrow", CURR_APP);
				this.addComment("Clicked on All tab arrow from the home page");
				eo.waitForPageload(driver);

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "subTabNameLink", "{subTabName}", subTab,
						CURR_APP);
				eo.waitForPageload(driver);

				this.addComment("Clicked on subtab " + subTab + " link");

				if (linkOrButton.equalsIgnoreCase("Link")) {

					if (eo.isExistsAfterReplace(driver, "xpath", "firstElementLink", "{link}", linkName, CURR_APP)) {

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "firstElementLink", "{link}", linkName,
								CURR_APP);
						eo.waitForPageload(driver);

					}
				}

				if (linkOrButton.equalsIgnoreCase("Button")) {

					if (eo.isExistsAfterReplace(driver, "xpath", "availableButton", "{buttonName}", buttonName,
							CURR_APP)) {
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "availableButton", "{buttonName}",
								buttonName, CURR_APP);
						eo.waitForPageload(driver);
					}
				}
			}

			catch (Exception e) {

				this.addComment("User is unable to open the sub tab link " + subTab);
				throw new KDTKeywordExecException("User is unable to open the sub tab link " + subTab, e);

			}

		}
	}

	//////////////////////// SelectLinkFromAllTabs///////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ExecuteBookingBatch</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will click on Run Batch button in
	 * Booking Batch VF page</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * <li>SelectLinkFromAllTabs</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class ExecuteBookingBatch extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// Click on Run Batch button

				eo.waitForWebElementVisible(driver, "XPATH", "runBatch", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "runBatch", CURR_APP);

				eo.waitForWebElementVisible(driver, "xpath", "completStatus", 250, CURR_APP);
				this.addComment("Successfully executed the booking batch and verified the status as Completed");

			}

			catch (Exception e) {

				this.addComment("User is unable to execute the booking batch");
				throw new KDTKeywordExecException("User is unable to execute the booking batch", e);

			}

		}
	}

	/////////////////////////////// CreateCCROfAnyType/////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateCCROfAnyType</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will click on the new CCR button from
	 * oppty and create a CCR with provided inputs</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * <li>CreateAccount</li>
	 * <li>CreateOpportunity</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class CreateCCROfAnyType extends Keyword {

		private String ccrRecordType;
		private String ccrType;
		private String ccrStatus;
		private String commentString;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {

				verifyArgs("CCRRecordType", "CCRType", "CCRStatus", "Comment");

				ccrRecordType = args.get("CCRRecordType");//
				ccrType = args.get("CCRType"); // "Customer Returns / Refunds"
				ccrStatus = args.get("CCRStatus");//
				commentString = args.get("Comment");

			} catch (Exception e) {
				this.addComment("Error while initializing CreateCCROfAnyType");
				throw new KDTKeywordInitException("Error while initializing CreateCCROfAnyType", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// Select CCR Record Type
				eo.selectComboBoxByVisibleText(driver, "xpath", "ccrRecordType", ccrRecordType, CURR_APP);
				eo.clickElement(driver, "xpath", "ccrContinueBtn", CURR_APP);
				eo.waitForPageload(driver);
				this.addComment("Selected the CCR record type as " + ccrRecordType);

				// Select CCR type

				eo.selectComboBoxByVisibleText(driver, "xpath", "ccrType", ccrType, CURR_APP);
				this.addComment("Selected the CCR type as " + ccrType);

				// Select CCR status
				eo.selectComboBoxByVisibleText(driver, "xpath", "ccrStatus", ccrStatus, CURR_APP);
				this.addComment("Selected the CCR type as " + ccrType);

				// Enter the Effective date and Effective end date

				eo.clickElement(driver, "xpath", "effectiveDate", CURR_APP);
				this.addComment("Added the effective date as current date");

				eo.clickElement(driver, "xpath", "effectiveEndDate", CURR_APP);
				this.addComment("Added the effective end date as current date");

				// Enter the comment

				eo.enterText(driver, "xpath", "commentText", commentString, CURR_APP);

				// Save the CCR

				eo.clickElement(driver, "xpath", "ccrsave", CURR_APP);
				eo.waitForPageload(driver);
				this.addComment("Successfully created the CCR of type " + ccrType);

				// Getting the Created CCR it and saving to a parameter

				eo.waitForWebElementVisible(driver, "xpath", "ccrId", 250, CURR_APP);
				String CreatedCCRID = eo.getText(driver, "xpath", "ccrId", CURR_APP);
				saveValue(CreatedCCRID);
				context.getData().get("$createdCCRId");

			}

			catch (Exception e) {

				this.addComment("User is failed to create CCR");
				throw new KDTKeywordExecException("User is failed to create CCR", e);

			}

		}
	}

	///////////////// ReadingWindowsPopup//////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ReadingWindowsPopup</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will fetch the windows pop up</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class ReadingWindowsPopup extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// Reading the windows pop up message
				String validationMessage = driver.switchTo().alert().getText();
				this.addComment(validationMessage);

			}

			catch (Exception e) {

				this.addComment("User unable to read the windows pop up message");
				throw new KDTKeywordExecException("User unable to read the windows pop up message", e);

			}

		}
	}

	/////////////////////////////// ReturnObjectFieldValues/////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ReturnObjectFieldValues</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will return the field values</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class ReturnObjectFieldValues extends Keyword {

		private String fields;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {

				verifyArgs("Fields");

				fields = args.get("Fields");

			} catch (Exception e) {
				this.addComment("Error while initializing ReturnObjectFieldValues");
				throw new KDTKeywordInitException("Error while initializing ReturnObjectFieldValues", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				String[] str = fields.split(":");

				for (int i = 0; i < str.length; i++) {

					saveValue(eo.getTextAfterReplacingKeyValue(driver, "xpath", "fieldValues", "{field}", str[i],
							CURR_APP));
					context.getData().get("$" + fields + "Value");
				}

			}

			catch (Exception e) {

				this.addComment("Failed to return object value");
				throw new KDTKeywordExecException("Failed to return object value", e);

			}

		}
	}

	//// CompareTwoValues////////

	public static class CompareTwoValues extends Keyword {

		private String string1;
		private String string2;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("String1", "String2");
				string1 = args.get("String1");
				string2 = args.get("String2");

			} catch (Exception e) {
				this.addComment("Error while initializing CompareTwoValues");
				throw new KDTKeywordInitException("Error while initializing CompareTwoValues", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			try {

				String[] s1 = string1.split(";");
				String[] s2 = string2.split(" ");

				ArrayList<String> a1 = new ArrayList<String>();
				ArrayList<String> a2 = new ArrayList<String>();

				for (int i = 0; i < s1.length; i++) {
					a1.add(s1[i].trim());

				}

				for (int i = 0; i < s2.length; i++) {
					a2.add(s2[i].trim());

				}

				boolean flag;
				for (int k = 0; k < a1.size(); k++) {

					flag = a1.contains(a2.get(k));

					if (flag) {

						this.addComment("The actual field value " + a1.get(k)
								+ " is matching with expected field value " + a2.get(k));

					}

					else {
						this.addComment("The actual field value " + a1.get(k)
								+ " is not matching with expected field value " + a2.get(k));
						throw new KDTKeywordExecException("The actual field value " + a2.get(k)
								+ " is not matching with expected field value " + a2.get(k));

					}
				}

			} catch (Exception e) {

				this.addComment("User is Unable to CompareTwoValues");
				throw new KDTKeywordExecException("User is Unable to CompareTwoValues", e);

			}

		}
	}

	//////////////////////////// ApproveAnyCCR////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ApproveAnyCCR</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will approve any ccr</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * <li>CreateCCROfAnyType</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class ApproveAnyCCR extends Keyword {

		private String ccrtype;
		private String bookingId;
		private String productToAdjust;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {

				verifyArgs("CCRType");

				ccrtype = args.get("CCRType");
				bookingId = args.get("BookingID");
				productToAdjust = args.get("ProductToAdjust");

			} catch (Exception e) {
				this.addComment("Error while initializing ReturnObjectFieldValues");
				throw new KDTKeywordInitException("Error while initializing ReturnObjectFieldValues", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// Submitting the CCR for approval

				eo.waitForWebElementVisible(driver, "xpath", "submitApproval", 150, CURR_APP);
				eo.clickElement(driver, "xpath", "submitApproval", CURR_APP);
				this.addComment("Submitted the CCR of type " + ccrtype + " for approval");

				eo.wait(5);

				if (ccrtype.equalsIgnoreCase("Customer Returns / Refunds")) {

					// Reading the windows pop up message
					String validationMessage = driver.switchTo().alert().getText();
					this.addComment(validationMessage);

					saveValue(validationMessage);
					context.getData().get("$validationCustomerReturn");
					driver.switchTo().alert().accept();

					// Enter the booking id
					eo.actionDoubleClick(driver, "xpath", "addBooking", CURR_APP);
					eo.enterText(driver, "xpath", "enterBookingId", bookingId, CURR_APP);

					this.addComment("Added the booking id as " + bookingId);

					// Clicking on save button
					eo.clickElement(driver, "xpath", "save_ccr", CURR_APP);
					eo.waitForPageload(driver);

					// selecting the booking adjustment

					String[] prod = productToAdjust.split(":");

					for (int i = 0; i < prod.length; i++) {

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "booking_adjItem", "{productId}",
								prod[i], CURR_APP);

						String existingQtty = eo.getTextAfterReplacingKeyValue(driver, "xpath", "booking_adjItemQtty",
								"{productId}", prod[i], CURR_APP);

						String newQuantity = existingQtty + 1;
						eo.enterTextAfterReplacingKeyValue(driver, "xpath", "booking_adjItemQtty", "{productId}",
								prod[i], newQuantity, CURR_APP);

						eo.waitForWebElementVisible(driver, "xpath", "submitApproval", 150, CURR_APP);
						eo.clickElement(driver, "xpath", "submitApproval", CURR_APP);
						this.addComment("Submitted the CCR of type " + ccrtype + " for approval");

					}

				}

			}

			catch (Exception e) {

				this.addComment("Failed to return object value");
				throw new KDTKeywordExecException("Failed to return object value", e);

			}

		}
	}

	////////////////////////////////////////////// CreateLead//////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateLead</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will be creating a new lead and
	 * verify the lead status and lead rejection reason picklist values</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * <li>CreateLead</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateLead extends Keyword {

		private String leadStatus;
		private String leadRejectionReasonList;
		private String leadStatusList;

		private String lastName;
		private String company;
		private String email;
		private String leadSource;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {

				verifyArgs("LeadStatus", "LastName", "Company", "Email", "LeadSource");
				leadStatus = args.get("LeadStatus");
				lastName = args.get("LastName");
				company = args.get("Company");
				email = args.get("Email");
				leadSource = args.get("LeadSource");

			}

			catch (Exception e) {
				this.addComment("Error while initializing CreateLead");
				throw new KDTKeywordInitException("Error while initializing CreateLead", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				// Check for values available in the Lead Status picklist

				if (hasArgs("LeadStatusList")) {

					leadStatusList = args.get("LeadStatusList");

					String[] expectedLeadStatus = leadStatusList.split(",");

					List<String> expectedLeadStatusList = new <String>ArrayList();

					for (int i = 0; i <= expectedLeadStatus.length - 1; i++) {
						expectedLeadStatusList.add(expectedLeadStatus[i]);

					}

					List<WebElement> element1 = eo.getListOfWebElements(driver, "xpath", "leadStatus", CURR_APP);

					List<String> actualLeadStatusList = new <String>ArrayList();

					for (WebElement ele : element1) {
						actualLeadStatusList.add(ele.getText());

					}

					for (int j = 0; j < expectedLeadStatusList.size(); j++) {

						if (actualLeadStatusList.size() == expectedLeadStatusList.size()) {

							if (actualLeadStatusList.contains(expectedLeadStatusList.get(j))) {

								this.addComment("The expected lead status <b>" + expectedLeadStatusList.get(j)
										+ "</b> is available in the drop down");

							}

							else {

								this.addComment("Failed to find the expected lead status <b>"
										+ expectedLeadStatusList.get(j) + "</b>in the drop down");
								throw new KDTKeywordExecException("Failed to find the expected lead status <b>"
										+ expectedLeadStatusList.get(j) + "</b>in the drop down");
							}

						}

						else {

							this.addComment(
									"Expected list size of lead status is not matching with the actual list size");
							throw new KDTKeywordExecException(
									"Expected list size of lead status is not matching with the actual list size");

						}
					}

				}

				// Select the lead status

				eo.selectComboBoxByVisibleText(driver, "xpath", "leadStatusDropown", leadStatus, CURR_APP);

				if (hasArgs("LeadRejectectionReasonList")) {

					leadRejectionReasonList = args.get("LeadRejectectionReasonList");

					// Check for values available in the Lead Rejection Reason picklist

					String[] expectedLeadRejectionReason = leadRejectionReasonList.split(",");

					List<String> expectedLeadRejectionReasonList = new <String>ArrayList();

					for (int i = 0; i <= expectedLeadRejectionReason.length - 1; i++) {
						expectedLeadRejectionReasonList.add(expectedLeadRejectionReason[i]);

					}

					List<WebElement> element = eo.getListOfWebElements(driver, "xpath", "leadRejectionReson", CURR_APP);

					List<String> actualLeadRejectionReasonList = new <String>ArrayList();

					for (WebElement ele : element) {
						actualLeadRejectionReasonList.add(ele.getText());

					}

					for (int j = 0; j < expectedLeadRejectionReasonList.size(); j++) {

						if (actualLeadRejectionReasonList.size() == expectedLeadRejectionReasonList.size()) {

							if (actualLeadRejectionReasonList.contains(expectedLeadRejectionReasonList.get(j))) {

								this.addComment("The expected lead rejection reason <b>"
										+ expectedLeadRejectionReasonList.get(j)
										+ "</b> is available in the drop down");

							}

							else {

								this.addComment("Failed to find the expected lead rejection reason <b>"
										+ expectedLeadRejectionReasonList.get(j) + "</b>in the drop down");
								throw new KDTKeywordExecException(
										"Failed to find the expected lead rejection reason <b>\"+expectedLeadRejectionReasonList.get(j)+\"</b>in the drop down");
							}

						}

						else {

							this.addComment(
									"Expected list size of lead rejection reason is not matching with the actual list size");
							throw new KDTKeywordExecException(
									"Expected list size of lead rejection reason is not matching with the actual list size");

						}
					}

				}

				// Check if user is able to create a lead by providing all the required fields

				// Enter last name
				lastName = eo.generateCurrDate() + lastName;
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "fieldInput", "{fieldName}", "Last Name", lastName,
						CURR_APP);

				this.addComment("Entered the last name as " + lastName);

				// Enter Company
				company = eo.generateCurrDate() + company;
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "fieldInput", "{fieldName}", "Company", company,
						CURR_APP);
				this.addComment("Entered the company as " + company);

				// Enter Email
				eo.enterTextAfterReplacingKeyValue(driver, "xpath", "fieldInput", "{fieldName}", "Email", email,
						CURR_APP);
				this.addComment("Entered the Email as " + email);

				// Enter Lead Source
				eo.selectComboBoxByVisibleText(driver, "xpath", "leadSourceOption", leadSource, CURR_APP);
				this.addComment("Selected the lead source as " + leadSource);

				// Save the lead

				eo.clickElement(driver, "xpath", "saveLead", CURR_APP);
				eo.waitForPageload(driver);

				if (leadStatus.equalsIgnoreCase("Non-Qualified")) {

					if (eo.isExists(driver, "xpath", "errorMessage", CURR_APP))
						this.addComment(eo.getWebElement(driver, "xpath", "errorMessage").getAttribute("innerHTML"));

				}

				else {

					if (eo.isExists(driver, "xpath", "home_tab", CURR_APP))
						this.addComment("Successfully landed to Home page after create the lead");

				}

				this.addComment("Successfully created the lead");

			}

			catch (Exception e) {

				this.addComment("Failed to Create Lead and verify the available dropdown values");
				throw new KDTKeywordExecException("Failed to Create Lead and verify the available dropdown values", e);

			}
		}

	}

	////////////////////////////////// SelectPrimaryCheckBox////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SelectPrimaryCheckBox</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will select the primary check box and
	 * AWS Marketplace and approve the quote</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * <li>SelectPrimaryCheckBox</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh</i></b>
	 * </p>
	 * </div>
	 */

	public static class SelectPrimaryCheckBox extends Keyword {

		private String azureMarketplaceDeal;

		@Override
		public void exec() throws KDTKeywordExecException {

			try {

				WebDriver driver = context.getWebDriver();

				// Here we are clicking on the primary check box for the quote

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "primary_checkbox", "{link}", "Primary",
						CURR_APP);
				addComment("Successfully clcik on the " + "<b>" + "Primary" + "</b>" + " checkbox");

				if (hasArgs("AzureMarketplaceDeal")) {
					azureMarketplaceDeal = args.get("AzureMarketplaceDeal");

					WebElement ele = driver.findElement(By.xpath(gei.getProperty("azureMarketPlace1", CURR_APP)));
					if (!ele.isSelected()) {

						addComment("Suuccessfully verifed the azureMarketPlace check box is unchecked bydefault");
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "azureMarketPlace", "{link}",
								azureMarketplaceDeal, CURR_APP);
						addComment("Successfully clcik on the" + "<b>" + azureMarketplaceDeal + "</b>" + "checkbox");
					} else {
						this.addComment("azureMarketplaceDeal Check box is already selected");
					}

				}
				eo.clickElement(driver, "XPATH", "account_save", CURR_APP);
				addComment("Successfully click on the Quote save button");

			} catch (Exception e) {

				this.addComment("Failed to return object value");
				throw new KDTKeywordExecException("Failed to return object value", e);

			}
		}

	}
	///////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SearchAndAddAnyProductToQuoteLine</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will serach for product in the edit
	 * quote page and add the required product as per user input</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * <li>SearchAndAddAnyProductToQuoteLine</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class SearchAndAddAnyProductToQuoteLine extends Keyword {

		private String searchElement;
		private String productsToAdd;
		private String productToDelete;;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			verifyArgs("SearchElement", "ProductsToAdd");
			searchElement = args.get("SearchElement");
			productsToAdd = args.get("ProductsToAdd");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				String[] productsToAddList = productsToAdd.split(";");

				if (hasArgs("ProductToDelete")) {

					productToDelete = args.get("ProductToDelete");

					String[] productToDeleteList = productToDelete.split(";");

					for (int i = 0; i < productToDeleteList.length; i++) {

						if (eo.isExistsAfterReplace(driver, "xpath", "quoteLine_checkbox", "{product}",
								productToDeleteList[i], CURR_APP)) {

							eo.clickElementAfterReplacingKeyValue(driver, "xpath", "quoteLine_checkbox", "{product}",
									productToDeleteList[i], CURR_APP);

						} else {

							eo.clickElement(driver, "xpath", "topUpCheckbox", CURR_APP);
							this.addComment("Clicked on the check box to delete all quote lines");
						}

						eo.clickElement(driver, "xpath", "deletebutton_productline", CURR_APP);
						this.addComment("Successfully deleted all quote lines");

						eo.waitForPopUp(driver);

					}

				}

				// Clicking on the Add Product button
				eo.clickElement(driver, "xpath", "addProducts_button", CURR_APP);
				eo.waitForPopUp(driver);
				this.addComment("Clicked on the Add product button");

				// Search for the product
				eo.waitForWebElementVisible(driver, "xpath", "search_textbox", 500, CURR_APP);
				eo.wait(3);
				eo.clickElement(driver, "xpath", "search_textbox", CURR_APP);
				eo.enterText(driver, "xpath", "search_textbox", searchElement, CURR_APP);

				SeleniumTools.waitForWebElementToClickable(driver,
						By.xpath(gei.getProperty("searchproducts_button", CURR_APP)), waiTime);
				eo.clickElement(driver, "xpath", "searchproducts_button", CURR_APP);

				for (int i = 0; i < productsToAddList.length; i++) {

					eo.waitForWebElementVisibleAfterReplace(driver, "XPATH", "addproduct_checkbox", "{product}",
							productsToAddList[i], 500, CURR_APP);

					eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "addproduct_checkbox", "{product}",
							productsToAddList[i], CURR_APP);

					this.addComment("Searched and added the product " + productsToAddList[i]);

				}

				eo.waitForWebElementVisible(driver, "xpath", "select_button", 500, CURR_APP);
				eo.clickElement(driver, "xpath", "select_button", CURR_APP);

				eo.waitForPopUp(driver);
			} catch (Exception e) {
				addComment("SearchAndAddAnyProductToQuoteLine Failed");
				throw new KDTKeywordExecException("SearchAndAddAnyProductToQuoteLine Failed");
			}

		}
	}
	////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>GetCurrentPageURL</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword willregresh the page with current
	 * url</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * <li>GetCurrentPageURL</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class GetCurrentPageURL extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				String url = driver.getCurrentUrl();
				driver.get(url);
				driver.navigate().refresh();
				eo.waitForPageload(driver);

			} catch (Exception e) {
				addComment("GetCurrentPageURL Failed");
				throw new KDTKeywordExecException("GetCurrentPageURL Failed");
			}

		}
	}

	//////////// SalesforceGlobalSearch//////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SalesforceGlobalSearch</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will serach for in the SFDC Global
	 * serach with the give search input</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class SalesforceGlobalSearch extends Keyword {

		private String searchInput;
		private String recordToSelect;
		private String requiredAction;

		@Override
		public void init() throws KDTKeywordInitException {

			try {
				super.init();

				verifyArgs("SearchInput", "RecordName");
				searchInput = args.get("SearchInput");
				recordToSelect = args.get("RecordName");

				if (hasArgs("RequiredAction"))
					requiredAction = args.get("RequiredAction");

			}

			catch (Exception e) {
				addComment("SalesforceGlobalSearch initialization Failed");
				throw new KDTKeywordInitException("SalesforceGlobalSearch initialization Failed");

			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {

				eo.clearData(driver, "xpath", "globalSearchField", CURR_APP);
				eo.enterText(driver, "xpath", "globalSearchField", searchInput, CURR_APP);
				eo.clickElement(driver, "xpath", "serachButton", CURR_APP);
				eo.waitForPageload(driver);

				this.addComment("Entered the search input " + searchInput
						+ " in the global search field and clicked on Search button");

				// Going to select the record type

				List<WebElement> recordList = eo.getListOfWebElements(driver, "xpath", "recordList", CURR_APP);

				for (int i = 0; i < recordList.size(); i++) {

					if (eo.isExistsAfterReplace(driver, "xpath", "partiCularRecord", "{recordToSelect}", recordToSelect,
							CURR_APP)) {

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "partiCularRecord", "{recordToSelect}",
								recordToSelect, CURR_APP);
						eo.waitForPopUp(driver);
						this.addComment("Clicked on the record type " + recordToSelect);
						break;
					} else
						;

				}

				// Going to select the particular record from the record result

				List<WebElement> recordResultList = eo.getListOfWebElements(driver, "xpath", "recordResultLists",
						CURR_APP);

				for (int i = 0; i < recordResultList.size(); i++) {

					if (eo.isExistsAfterReplace(driver, "xpath", "particularRecordFromRecordResultLists",
							"{serachInput}", searchInput, CURR_APP)) {

						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "particularRecordFromRecordResultLists",
								"{serachInput}", searchInput, CURR_APP);
						eo.waitForPopUp(driver);
						this.addComment(
								"Clicked on the serach record " + searchInput + " from the serach result table");
						break;
					}

					else
						;
				}

				if (recordToSelect.equalsIgnoreCase("People")) {
					eo.waitForWebElementVisible(driver, "xpath", "userDetailsDropdown", 500, CURR_APP);
					eo.clickElement(driver, "xpath", "userDetailsDropdown", CURR_APP);
					this.addComment("Clicked on the user details drop down");

					eo.clickElement(driver, "xpath", "userDetailsDropdownOption", CURR_APP);
					eo.waitForPageload(driver);
					this.addComment("Clicked on User Details");

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "actionInUserDetails", "{requiredAction}",
							requiredAction, CURR_APP);
					eo.waitForPageload(driver);
					this.addComment("Clicked on action " + requiredAction + " in user details page");

					if (eo.isExists(driver, "xpath", "defaultSelectedActiveCheckbox", CURR_APP)) {
						// Un select Active check box
						eo.clickElement(driver, "xpath", "activeCheckBox", CURR_APP);
						this.addComment("Un selected the  Active check box");

						String alertMessage = driver.switchTo().alert().getText();
						this.addComment("<b>" + alertMessage + "</b>");
						saveValue(alertMessage);
						context.getData().get("$actualValidationMessageInactivateUser");
						driver.switchTo().alert().accept();

						eo.clickElement(driver, "xpath", "saveUserDetails", CURR_APP);
						eo.waitForPageload(driver);

						eo.waitForWebElementVisible(driver, "xpath", "userDeactivationPage", 500, CURR_APP);
						eo.clickElement(driver, "xpath", "saveUserDetails", CURR_APP);
						eo.waitForPageload(driver);

						this.addComment("Successfully saved the updated user details");
					}

					else {

						eo.clickElement(driver, "xpath", "activeCheckBox", CURR_APP);
						this.addComment("Selected the Active check box");
						eo.clickElement(driver, "xpath", "saveUserDetails", CURR_APP);
						eo.waitForPageload(driver);
						this.addComment("Successfully saved the updated user details");
					}
				}

				else if (recordToSelect.equalsIgnoreCase("Accounts")) {
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "openGivenAccount", "{accountName}",
							searchInput, CURR_APP);
					eo.waitForPageload(driver);
					this.addComment("Opened the account " + searchInput);

				}

			} catch (Exception e) {
				addComment("SalesforceGlobalSearch Failed");
				throw new KDTKeywordExecException("SalesforceGlobalSearch Failed");
			}

		}
	}

	///////////////////////////// AddNewAccTeamMembers/////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>AddNewAccTeamMembers</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword add Account Team member</i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class AddNewAccTeamMembers extends Keyword {
		private String[] parameters;

		private int i = 0;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			parameters = args.get("Parameters").split(";");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();
			System.out.println(parameters.length);
			addComment("Going to add a new account team member");
			for (i = 0; i < parameters.length; i++) {
				String key = parameters[i].split(":")[0].trim();
				String value = parameters[i].split(":")[1].trim();
				switch (key) {
				case "username":
					if (!value.isEmpty()) {
						eo.enterText(driver, "xpath", "usernamefield", value, CURR_APP);
						driver.findElement(By.xpath(gei.getProperty("usernamefield", CURR_APP))).sendKeys(Keys.TAB);
						addComment("Entered user name as:" + value);
					} else {
						addComment("Value for Mandatory field" + key + " is not present");
						throw new KDTKeywordExecException("Value for Mandatory field" + key + " is not present");
					}
					break;
				case "accountaccess":
					if (!value.isEmpty()) {
						eo.selectComboBoxByVisibleText(driver, "xpath", "selectaccountaccess", value, CURR_APP);
						addComment("Selected account access value as:" + value);
					}
					break;
				case "opportunityacccess":
					if (!value.isEmpty()) {
						eo.selectComboBoxByVisibleText(driver, "xpath", "selectoppaccess", value, CURR_APP);
						addComment("Selected opportunity access value as:" + value);
					}
					break;
				case "caseaccess":
					if (!value.isEmpty()) {
						eo.selectComboBoxByVisibleText(driver, "xpath", "caseaccess", value, CURR_APP);
						addComment("Selected case access value as:" + value);
					} else {
						addComment("Value for Mandatory field" + key + " is not present");
						throw new KDTKeywordExecException("Value for Mandatory field" + key + " is not present");
					}
					break;
				case "teamrole":
					if (!value.isEmpty()) {
						eo.selectComboBoxByVisibleText(driver, "xpath", "teamrole", value, CURR_APP);
						addComment("Selected team role access value as:" + value);
					} else {
						addComment("Value for Mandatory field" + key + " is not present");
						throw new KDTKeywordExecException("Value for Mandatory field" + key + " is not present");
					}
					break;
				default:
					addComment("default option");
					break;

				}

			}
			eo.clickElement(driver, "xpath", "saveAccTeam", CURR_APP);
			addComment("Saved the new account team members details");

		}

	}

	///////////////////////////// GetCountOfAnyLinkMembers/////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>GetCountOfAnyLinkElements</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will get the count of any link
	 * members </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */

	public static class GetCountOfAnyLinkMembers extends Keyword {

		private String linkName;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			linkName = args.get("LinkName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			WebDriver driver = context.getWebDriver();

			String countOfLinkMembers = eo.getTextAfterReplacingKeyValue(driver, "xpath", "linkMemberCount",
					"{linkName}", linkName, CURR_APP);
			saveValue(countOfLinkMembers);
			context.getData().get("$actualCount");

			this.addComment("Successfully captured the count of members for given link " + linkName);

		}
	}

	///////////////////////////// VerifyTheDisplayOfProductAndGetItsCountInEditQuotePage/////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword
	 * Name:</i>VerifyTheDisplayOfProductAndGetItsCountInEditQuotePage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will check if the given product
	 * available in the edit quote page and return the count as per user request
	 * (Yes/No) </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class VerifyTheDisplayOfProductAndGetItsCountInEditQuotePage extends Keyword {

		private String product;
		private String doYouNeedCountAsWell;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			product = args.get("Product");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			WebDriver driver = context.getWebDriver();

			try {

				List<WebElement> actualproductList = eo.getListOfWebElements(driver, "xpath", "existingProducts",
						CURR_APP);

				for (int i = 0; i < actualproductList.size(); i++) {

					this.addComment(actualproductList.size() + " ");
					this.addComment(actualproductList.get(i).getText());

				}

				int countOfProduct = 0;
				for (int i = 0; i < actualproductList.size(); i++) {

					if (actualproductList.get(i).getText().equalsIgnoreCase(product)) {

						countOfProduct++;
					}
				}

				if (countOfProduct > 1) {

					this.addComment("The product " + product + " is dispalyed in the edit quote page");
				} else {

					addComment("Failed to find the product " + product + " in the dit quote page");
					throw new KDTKeywordExecException(
							"Failed to find the product " + product + " in the dit quote page");
				}

				if (hasArgs("DoYouNeedCountAsWell")) {

					doYouNeedCountAsWell = args.get("DoYouNeedCountAsWell");

					if (doYouNeedCountAsWell.equalsIgnoreCase("yes")) {

						saveValue(String.valueOf(countOfProduct));
						context.getData().get("$actualCountOfProduct");

					}

					else {

						;
					}

				}

			}

			catch (Exception e) {
				addComment("VerifyTheDisplayOfProductAndGetItsCountInEditQuotePage Failed");
				throw new KDTKeywordExecException("VerifyTheDisplayOfProductAndGetItsCountInEditQuotePage Failed");
			}

		}
	}

	///////////////////////////// ClickSaveCancelAnyActionRequired/////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ClickSaveCancelAnyActionRequired</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword Can be user to perform Click
	 * SAVE,CANCEL button in any page </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class ClickSaveCancelAnyActionRequired extends Keyword {

		private String requiredAction;
		private String pageName;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			requiredAction = args.get("RequiredAction");

			if (hasArgs("PageName")) {

				pageName = args.get("PageName");

			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			WebDriver driver = context.getWebDriver();

			try {

				if (requiredAction.equalsIgnoreCase("Save")) {
					
					
					if(pageName.equalsIgnoreCase("Edit Quote Page")) {
						
						eo.clickElement(driver, "xpath", "quote_save_btn", CURR_APP);
						eo.waitForPopUp(driver);
						eo.waitForPopUp(driver);
						
					}
					else {
						
			
					eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "saveButtonInPage", "{pageName}", pageName,
							500, CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "saveButtonInPage", "{pageName}", pageName,
							CURR_APP);
					eo.waitForPageload(driver);
					}
				}

				else if (requiredAction.equalsIgnoreCase("Cancel")) {

					eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "cancelButtonInPage", "{pageName}",
							pageName, 500, CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "cancelButtonInPage", "{pageName}", pageName,
							CURR_APP);
					eo.waitForPageload(driver);

				}

				else {

					;
				}
			}

			catch (Exception e) {
				addComment("ClickSaveCancelAnyActionRequired Failed");
				throw new KDTKeywordExecException("ClickSaveCancelAnyActionRequired Failed");
			}

		}
	}

	///////////////////////////// SelectParticularProfileUsers/////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SelectParticularProfileUsers</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword get the list of users from any
	 * profile </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh</i></b>
	 * </p>
	 * </div>
	 */

	public static class SelectParticularProfileUsers extends Keyword {

		private String rubrikLegalProfile;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			rubrikLegalProfile = args.get("ProfileName");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();

				String setUpTabtext = "Setup";
				String manageUsersText = "Manage Users";
				String profileText = "Profiles";

				// clicking SetUp Tab
				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "headerLinks", "{link}", setUpTabtext, waiTime,
						CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "headerLinks", "{link}", setUpTabtext, CURR_APP);
				this.addComment("Successfully clicked the" + "<b>" + setUpTabtext + "</b>" + "tab");

				// clicking MangeUSers subTab
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "headerLinks", "{link}", manageUsersText,
						CURR_APP);
				this.addComment("Successfully clicked the" + "<b>" + manageUsersText + "</b>" + "tab");

				// clicking Profiles under MangeUsers tab
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "headerLinks", "{link}", profileText, CURR_APP);
				this.addComment("Successfully clicked the" + "<b>" + profileText + "</b>" + "tab");

				// Clicking on user provided profile first character

				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "profileSelect", "{text}",
						String.valueOf(rubrikLegalProfile.charAt(0)), waiTime, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "profileSelect", "{text}",
						String.valueOf(rubrikLegalProfile.charAt(0)), CURR_APP);
				this.addComment(
						"Successfully clicked on the first charecter <b>" + rubrikLegalProfile.charAt(0) + "</b>");

				eo.waitForPopUp(driver);

				// clicking RubRIk Legal Profile
				eo.waitForWebElementVisibleAfterReplace(driver, "xpath", "profileSelect", "{text}", rubrikLegalProfile,
						waiTime, CURR_APP);

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "profileSelect", "{text}", rubrikLegalProfile,
						CURR_APP);
				eo.waitForPageload(driver);
				this.addComment("Successfully clicked the" + "<b>" + rubrikLegalProfile + "</b>");

				// clicking View Users tab

				eo.waitForWebElementVisible(driver, "Xpath", "viewUserTab", waiTime, CURR_APP);

				eo.clickElement(driver, "Xpath", "viewUserTab", CURR_APP);

				this.addComment("Successfully clicked the" + "<b>" + "ViewUsers" + "</b>" + "tab");

				// getting all user's from RubrikLegal

				List<String> actualUsersList = new ArrayList<>();

				List<WebElement> allUsers = eo.getListOfWebElements(driver, "xpath", "allUsers", CURR_APP);

				for (int i = 0; i < allUsers.size(); i++) {
					actualUsersList.add(allUsers.get(i).getText());
				}

				String actualUserListOutput = "";

				for (int k = 0; k < actualUsersList.size(); k++) {
					actualUserListOutput += actualUsersList.get(k) + " ";

				}

				// System.out.println(actualUserListOutput);

				saveValue(actualUserListOutput);
				context.getData().get("$actualprofileUsers");

				if (rubrikLegalProfile.equals("Rubrik Legal")) {

					List<WebElement> allroles = eo.getListOfWebElements(driver, "xpath", "role", CURR_APP);

					for (int i = 0; i <= allroles.size(); actualUsersList.size(), i++) {
						String usertext = allroles.get(i).getText();

						if (usertext.equalsIgnoreCase("Head of Legal")) {

							this.addComment("Verified the user" + " " + actualUsersList.get(i) + " " + "having role"
									+ " " + "<b>" + usertext + "</b>");
						} else {
							this.addComment("failed to verify the user" + " " + actualUsersList.get(i) + " "
									+ "having role" + " " + "<b>" + usertext + "</b>");
						}

					}
				}

			} catch (Exception e) {
				this.addComment("Failed to find the user from profile");
			}
		}

	}
	
	//////////////////////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CompareTwoValuesString</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This keyword will compare two string values
	 * profile </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * <li>LoginSalesforce</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
	 * </p>
	 * </div>
	 */
	public static class CompareTwoValuesString extends Keyword {

		private String string1;
		private String string2;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();

			try {
				verifyArgs("String1", "String2");
				string1 = args.get("String1");
				string2 = args.get("String2");

			} catch (Exception e) {
				this.addComment("Error while initializing CompareTwoValuesString");
				throw new KDTKeywordInitException("Error while initializing CompareTwoValuesString", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			try {
                   
				
				this.addComment("The expected message is "+string1);
				this.addComment("The actual message is "+string2);
				if(string1.equalsIgnoreCase(string2))
					
				{
					this.addComment("The expected message is matching with the actual message");
					
				}
				else {
					
					this.addComment("The expected message is not matching with the actual message");
					throw new KDTKeywordExecException("The expected message is not matching with the actual message");
				}
			}
	         catch (Exception e) {

			;

			}

		}
	}
///////////////////////////VerifyApproval/////////////////
		/**
		 * <div align="left">
		 * <p>
		 * <b><i>Keyword Name:</i>VerifyApproval</b>
		 * </p>
		 * <p>
		 * <b><i>Description:</i></b> This keyword will verify the approval is triggered for any opened object
		 * profile </i>
		 * </P>
		 * <b><i>Dependencies</i></b>:
		 * <ul>
		 * <li>Launch</li>
		 * <li>LoginSalesforce</li>
		 * </ul>
		 * <b><i>Arguments</i></b>:
		 * <ul>
		 * </ul>
		 * <p>
		 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Shavin</i></b>
		 * </p>
		 * </div>
		 */
	public static class VerifyApproval extends Keyword {

		private String approvalName;

		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			try {
				verifyArgs("ApprovalName");
				approvalName = args.get("ApprovalName");	

			} catch (Exception e) {
				this.addComment("Error while initializing VerifyApproval");
				throw new KDTKeywordInitException("Error while initializing VerifyApproval", e);
			}

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			try {
                    
				WebDriver driver = context.getWebDriver();
				List<WebElement> listXpath =eo.getListOfWebElements(driver, "xpath", "approvalSectionValues", CURR_APP);
				List<String> xpathText = new ArrayList<String>();

				for (int j = 0; j < listXpath.size(); j++) {
					xpathText.add(listXpath.get(j).getText().trim());
				}
				
				for(Object print:xpathText) {
					this.addComment((String)print);
				}
				
				if(xpathText.contains(approvalName)) {
					this.addComment("Successfully verified the approval "+approvalName+" is triggered");
				}
				else {
					throw new KDTKeywordExecException("Failed to verify the display of provided approval "+approvalName);
				}
			}
	         catch (Exception e) {
				this.addComment("Failed to verify the display of provided approval "+approvalName);
				throw new KDTKeywordExecException("Failed to verify the display of provided approval "+approvalName, e);
			}
		}
	}

////////////////////OpenTheOrder /////////////////////////////////////
	
	public static class OpenTheOrder extends Keyword {
	
		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();
				
				String subTabName = "Orders";
				
				addComment("Create Order started");
				eo.waitForPageload(driver);
				eo.actionMoveToElementAfterReplace(driver, "xpath", "subTab", "{link}", subTabName, CURR_APP);
				String OrderNumberText = eo.getText(driver, "xpath", "ordernumber_link", CURR_APP);
				
				saveValue(OrderNumberText);
				context.getData().get("$orderNo");
				
				System.out.println("OrderNum:"+OrderNumberText);
				addComment("ORDER NUMBER" + context.getData().get("$orderNo"));
				eo.clickElement(driver, "xpath", "ordernumber_link", CURR_APP);
				this.addComment("Clicked on created order");
				eo.waitForPageload(driver);
				
				
				// verifying Order Page
				if (eo.getText(driver, "xpath", "orderdetail_text", CURR_APP).trim().equalsIgnoreCase("Order Detail")) {
					this.addComment("User at Order Detail page");
				} else {
					this.addComment("User not at Order Detail page");
					throw new KDTKeywordInitException("User not at Order Detail page");
				}
				
				//Taking OrderAmount
				
			    OrderAmount =eo.getText(driver, "xpath", "orderAmount", CURR_APP);
				
				System.out.println(OrderAmount);
				
				saveValue(OrderAmount);
				
				context.getData().get("$orderAmount");
				this.addComment("Sucessfully stored the Order Amount as:"+"<b>"+OrderAmount+"</b>");
				
			} catch (Exception e) {
				this.addComment("User is Unable to Open the Order");
				throw new KDTKeywordExecException("User is Unable to Open the Order", e);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////
	///////////////////////// VerifyingGtcFields ///////////////////////////
	
	public static class VerifyingGtcFields extends Keyword {
		
		//private String creditLimit;
		private String isScreeningByPass;
		
		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			try {
				//creditLimit = args.get("CreditLimit");
				isScreeningByPass = args.get("IsScreeningByPass");
				
				if(isScreeningByPass == null) {
					isScreeningByPass = GTC.isScreeningByPass;
				}
				
			} catch (Exception e) {
				this.addComment("Error while initializing VerifyingGtcFields");
				throw new KDTKeywordInitException("Error while initializing VerifyingGtcFields", e);
			}

		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
			
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			
			try {
				WebDriver driver = context.getWebDriver();

				// verifying order detail 
					
				// Verifying OrderStatus field
				String orderStatus = eo.getText(driver, "xpath", "order_status", CURR_APP).trim();
				if (orderStatus.equalsIgnoreCase("Pending") || orderStatus.equalsIgnoreCase("PO Received") || 
						orderStatus.equalsIgnoreCase("Order Accepted")) {
					this.addComment("The order status is <b>" + orderStatus + "</b> verified as expected");
				} else {
					this.addComment("The order status does not match with expected result");
					throw new KDTKeywordInitException("The order status does not match with expected result");
				}

				// Verifying OrderType field
				if (eo.getText(driver, "xpath", "order_type", CURR_APP).trim().equalsIgnoreCase("Revenue")) {
					this.addComment("The order type is <b>" + "Revenue" + "</b> verified as expected");
				} else {
					this.addComment("The order type does not match with expected result");
					throw new KDTKeywordInitException("The order type does not match with expected result");
				}
					
				// Verifying Order sub-type field
				String orderSubType = eo.getText(driver, "xpath", "order_subtype", CURR_APP).trim();
				if (orderSubType.equalsIgnoreCase("Renewal") || orderSubType.isBlank()) {
					this.addComment("The order sub-type is <b>" + (orderSubType.isBlank() ? "Blank" : orderSubType) + "</b> verified as expected");
				} else {
					this.addComment("The order sub-type does not match with expected result");
					throw new KDTKeywordInitException("The order sub-type does not match with expected result");
				}
					
				// Verifying Hold Code field
				if (eo.getText(driver, "xpath", "hold_code", CURR_APP).trim().isBlank()) {
					this.addComment("The order hold code is verified as <b>Blank</b>");
				} else {
					this.addComment("The order hold code is not blank as expected");
					throw new KDTKeywordInitException("The order hold code is not blank as expected");
				}
			
				
				
					
				////////////////// Restricted Party Screening /////////////////////
					
				// Verifying that Is RPS required
				eo.javaScriptScrollToViewElement(driver, "xpath", "is_RPS_required", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "is_RPS_required", waiTime, CURR_APP);
				
				String is_RPS_required = eo.getAttribute(driver, "xpath", "is_RPS_required", "title", CURR_APP);
				
				if(isScreeningByPass != null) {
					if(isScreeningByPass.equalsIgnoreCase("Yes") && is_RPS_required.equalsIgnoreCase("Checked")) {
						eo.actionDoubleClick(driver, "xpath", "is_RPS_required", CURR_APP);
						eo.clickElement(driver, "xpath", "is_RPS_required_chkbx", CURR_APP);
						eo.actionClick(driver, "xpath", "is_RPS_required_title", CURR_APP);
					}
				}
				
				eo.waitForWebElementVisible(driver, "XPATH", "is_RPS_required", waiTime, CURR_APP);
				is_RPS_required = eo.getAttribute(driver, "xpath", "is_RPS_required", "title", CURR_APP);
				
				if (is_RPS_required.equalsIgnoreCase("Checked") && isScreeningByPass == null) {
					this.addComment("Is RPS required checkbox is verified successfully as <b>selected</b>");
				}
				else if (isScreeningByPass != null){
					if(is_RPS_required.equalsIgnoreCase("Not Checked") && isScreeningByPass.equalsIgnoreCase("Yes")) {
						this.addComment("Is RPS required checkbox is verified successfully as <b>Unselected</b>");
					}
				} else {
					this.addComment("User is unable to verify the Is RPS required checkbox");
					throw new KDTKeywordInitException("User is unable to verify the Is RPS required checkbox");
				}
				
				
				if(isScreeningByPass != null) {
					if(isScreeningByPass.equalsIgnoreCase("Yes")) {
						eo.actionDoubleClick(driver, "xpath", "screenStatus", CURR_APP);
						eo.clickElement(driver, "xpath", "screenStatusValue", CURR_APP);
						eo.actionClick(driver, "xpath", "screenStatus_title", CURR_APP);
						
						eo.clickElement(driver, "xpath", "save_order", CURR_APP);
						eo.wait(10);
					}
				}
					
				// Verify Screen Status
				eo.waitForWebElementVisible(driver, "xpath", "screenStatus", 10, CURR_APP);
				String screenStatus = eo.getText(driver, "xpath", "screenStatus", CURR_APP);
				
				if(isScreeningByPass != null && isScreeningByPass.equalsIgnoreCase("Yes")) {
					if(screenStatus.equalsIgnoreCase("Approved-Bypassed")) {
						this.addComment("Screen status is successfully verified as <b>Approved-Bypassed</b>");
					}
					else {
						this.addComment("User is unable to verify the screen status");
						throw new KDTKeywordInitException("User is unable to verify the screen status");
					}
				}
				else {
					if(orderStatus.equalsIgnoreCase("Pending")) {
						screenStatus.isBlank();
						this.addComment("Screen status is successfully verified as <b>Blank</b>");
					}
					else if(orderStatus.equalsIgnoreCase("PO Received")) {
						screenStatus.equalsIgnoreCase("Required");
						this.addComment("Screen status is successfully verified as <b>Required</b>");
					}
					else if(orderStatus.equalsIgnoreCase("Order Accepted")) {
						screenStatus.equalsIgnoreCase("Approved");
						this.addComment("Screen status is successfully verified as <b>Approved</b>");
					}
					else {
						this.addComment("User is unable to verify the screen status");
						throw new KDTKeywordInitException("User is unable to verify the screen status");
					}
				}
				
				///////////////////////////////////////////
				
				if (eo.getText(driver, "xpath", "screening_msg", CURR_APP).trim().isBlank()) {
					this.addComment("Screening message is verified as <b>Blank</b>");
				} else {
					this.addComment("Screening message is not verified as <b>Blank</b>");
					throw new KDTKeywordInitException("Screening message is not verified as <b>Blank</b> as expected");
				}
				
				boolean isIdBlank = true;
				String status = "";
				String msg = "";
				if(!orderStatus.equalsIgnoreCase("Order Accepted") || (orderStatus.equalsIgnoreCase("Order Accepted") && 
						isScreeningByPass != null && isScreeningByPass.equalsIgnoreCase("Yes"))) {
					status = "Required";
				}
				
				else {
					isIdBlank = false;
					status = "Approved";
					msg = "N/A";
				}
	
				String billToEU_id = eo.getText(driver, "xpath", "billToEU_id", CURR_APP).trim();
				if (eo.getText(driver, "xpath", "billToEU_id", CURR_APP).trim().isBlank() == isIdBlank) {
					this.addComment("Billto EndUser Screening Id is verified as <b>" + (billToEU_id.isBlank() ? "Blank" : billToEU_id) + "</b>");
				} else {
					this.addComment("User is unable to verify the Billto EndUser Screening Id");
					throw new KDTKeywordInitException("User is unable to verify the Billto EndUser Screening Id");
				}
				
				if (eo.getText(driver, "xpath", "billToEU_status", CURR_APP).trim().equalsIgnoreCase(status)) {
					this.addComment("Billto EndUser Screen Status is verified as <b>" + status + "</b>");
				} else {
					this.addComment("Billto EndUser Screen Status is not verified as <b>Required</b>");
					throw new KDTKeywordInitException("Billto EndUser Screen Status is not verified as <b>" + status + "</b>");
				}
				
				if (eo.getText(driver, "xpath", "billToEU_msg", CURR_APP).trim().equalsIgnoreCase(msg)) {
					this.addComment("Billto EndUser Screen StatusMessage is verified as <b>"+ (msg.isBlank() ? "Blank" : msg) +"</b>");
				} else {
					this.addComment("User is unable to verify the Billto EndUser Screen StatusMessage");
					throw new KDTKeywordInitException("User is unable to verify the Billto EndUser Screen StatusMessage");
				}
				
				String shipToContactId = eo.getText(driver, "xpath", "shipToContactId", CURR_APP).trim();
				if (eo.getText(driver, "xpath", "shipToContactId", CURR_APP).trim().isBlank() == isIdBlank) {
					this.addComment("ShipTo Contact Screening Id is verified as <b>" + (shipToContactId.isBlank() ? "Blank" : shipToContactId) + "</b>");
				} else {
					this.addComment("User is unable to verify the ShipTo Contact Screening Id");
					throw new KDTKeywordInitException("User is unable to verify the ShipTo Contact Screening Id");
				}
				
				if (eo.getText(driver, "xpath", "shipToContactStatus", CURR_APP).trim().equalsIgnoreCase(status)) {
					this.addComment("ShipTo Contact Screen Status is verified as <b>"+ status + "</b>");
				} else {
					this.addComment("ShipTo Contact Screen Status is not verified as <b>"+ status + "</b>");
					throw new KDTKeywordInitException("ShipTo Contact Screen Status is not verified as <b>"+ status + "</b>");
				}
				
				if (eo.getText(driver, "xpath", "shipToContactMsg", CURR_APP).trim().equalsIgnoreCase(msg)) {
					this.addComment("ShipTo Contact Screen StatusMessage is verified as <b>"+ (msg.isBlank() ? "Blank" : msg) +"</b>");
				} else {
					this.addComment("User is unable to verify the ShipTo Contact Screen StatusMessage");
					throw new KDTKeywordInitException("User is unable to verify the ShipTo Contact Screen StatusMessage");
				}
				
				String shipToEU_Id = eo.getText(driver, "xpath", "shipToContactId", CURR_APP).trim();
				if (eo.getText(driver, "xpath", "shipToEU_Id", CURR_APP).trim().isBlank() == isIdBlank) {
					this.addComment("Shipto EndUser Screening Id is verified as <b>" + (shipToEU_Id.isBlank() ? "Blank" : shipToEU_Id) + "</b>");
				} else {
					this.addComment("User is unable to verify the Shipto EndUser Screening Id");
					throw new KDTKeywordInitException("User is unable to verify the Shipto EndUser Screening Id");
				}
				
				if (eo.getText(driver, "xpath", "shipToEU_status", CURR_APP).trim().equalsIgnoreCase(status)) {
					this.addComment("Shipto EndUser Screen Status is verified as <b>"+ status + "</b>");
				} else {
					this.addComment("Shipto EndUser Screen Status is not verified as <b>"+ status + "</b>");
					throw new KDTKeywordInitException("Shipto EndUser Screen Status is not verified as <b>"+ status + "</b>");
				}
				
				if (eo.getText(driver, "xpath", "shipToEU_msg", CURR_APP).trim().equalsIgnoreCase(msg)) {
					this.addComment("Shipto EndUser Screen StatusMessage is verified as <b>"+ (msg.isBlank() ? "Blank" : msg) +"</b>");
				} else {
					this.addComment("User is unable to verify the Shipto EndUser Screen StatusMessage");
					throw new KDTKeywordInitException("User is unable to verify the Shipto EndUser Screen StatusMessage");
				}
				
				String shipToId = eo.getText(driver, "xpath", "shipToContactId", CURR_APP).trim();
				if (eo.getText(driver, "xpath", "shipToId", CURR_APP).trim().isBlank() == isIdBlank) {
					this.addComment("ShipTo Screening Id is verified as <b>" + (shipToId.isBlank() ? "Blank" : shipToId) + "</b>");
				} else {
					this.addComment("User is unable to verify the ShipTo Screening Id");
					throw new KDTKeywordInitException("User is unable to verify the ShipTo Screening Id");
				}
				
				if (eo.getText(driver, "xpath", "shipToStatus", CURR_APP).trim().equalsIgnoreCase(status)) {
					this.addComment("ShipTo Screen Status is verified as <b>"+ status + "</b>");
				} else {
					this.addComment("ShipTo Screen Status is not verified as <b>"+ status + "</b>");
					throw new KDTKeywordInitException("ShipTo Screen Status is not verified as <b>"+ status + "</b>");
				}
				
				if (eo.getText(driver, "xpath", "shipToMsg", CURR_APP).trim().equalsIgnoreCase(msg)) {
					this.addComment("ShipTo Screen StatusMessage is verified as <b>"+ (msg.isBlank() ? "Blank" : msg) +"</b>");
				} else {
					this.addComment("User is unable to verify the ShipTo Screen StatusMessage");
					throw new KDTKeywordInitException("User is unable to verify the ShipTo Screen StatusMessage");
				}
				
				
				//////////////////verify credit check ///////////////////////////
				
				
				
				eo.javaScriptClickAfterReplacingKeyValue(driver, "xpath", "credit_check_failure_reason", "{text}", "Credit Check Failure Reason", CURR_APP);
				
				//CreditCheckFailureReason
				String CreditCheckFailureReason=eo.getTextAfterReplacingKeyValue(driver, "xpath", "credit_check_failure_reason", "{text}", "Credit Check Failure Reason", CURR_APP);
				
				
				if (CreditCheckFailureReason == msg) {

					addComment("Successfully verified the Credit Check FailureReason as:" + "<b>"
							+ (msg.isBlank() ? "Blank" : msg) + "</b>");

				} else {
					addComment("Unable to verify the Credit Check FailureReason as:" + "<b>"
							+ (msg.isBlank() ? "Blank" : msg) + "</b>");
				}
				//Credit Limit	
				String CreditLimit	=eo.getTextAfterReplacingKeyValue(driver, "xpath", "credit_check_failure_reason", "{text}", "Credit Limit", CURR_APP);
				
				if(CreditLimit==msg) {
					addComment("Successfully verified the Credit Check FailureReason as:"+"<b>"+ (msg.isBlank() ? "Blank" : msg) + "</b>");
				}else {
					addComment("Unable to verify the Credit Check FailureReason as:"+"<b>"+ (msg.isBlank() ? "Blank" : msg) + "</b>");
				}
				//Customer Balance	

				String CustomerBalance =eo.getText(driver, "xpath", "customerBalance", CURR_APP);
				
				if(CustomerBalance==msg) {
					addComment("Successfully verified the Credit Check FailureReason as:"+"<b>"+ (msg.isBlank() ? "Blank" : msg) + "</b>");
				}else {
					addComment("Unable to verify the Credit Check FailureReason as:"+"<b>"+ (msg.isBlank() ? "Blank" : msg) + "</b>");
				}
				
				/*
				 * if (! orderStatus.equalsIgnoreCase("Order Accepted") && eo.getText(driver,
				 * "xpath", "credit_check_failure_reason", CURR_APP).trim().isBlank()) {
				 * this.addComment("The credit check Failure Reason is verified as <b>Blank</b>"
				 * ); } else if (orderStatus.equalsIgnoreCase("Order Accepted") &&
				 * eo.getText(driver, "xpath", "credit_check_failure_reason", CURR_APP)
				 * .trim().equalsIgnoreCase("NA")) {
				 * 
				 * this.addComment("The credit check Failure Reason is <b>NA</b> as expected");
				 * } else {
				 * this.addComment("User is unable to verify the credit check Failure Reason");
				 * throw new
				 * KDTKeywordInitException("User is unable to verify the credit check Failure Reason"
				 * ); }
				 * 
				 * eo.wait(2); if (creditLimit != null) { eo.actionDoubleClick(driver, "xpath",
				 * "credit_limit", CURR_APP); eo.enterText(driver, "xpath",
				 * "credit_limit_after_click", creditLimit, CURR_APP); eo.actionClick(driver,
				 * "xpath", "credit_limit_title", CURR_APP); eo.clickElement(driver, "xpath",
				 * "save_order", CURR_APP); eo.wait(10); }
				 * 
				 * // verify credit Limit if (eo.getText(driver, "xpath", "credit_limit",
				 * CURR_APP).trim().isBlank()) {
				 * this.addComment("The credit limit is verified as <b>Blank</b>");
				 * 
				 * } else if (eo.getText(driver, "xpath", "credit_limit",
				 * CURR_APP).trim().equalsIgnoreCase(creditLimit)) {
				 * 
				 * this.addComment("The credit check Failure Reason is <b>NA</b> as expected");
				 * } else { this.addComment("User is unable to verify the credit limit"); throw
				 * new KDTKeywordInitException("User is unable to verify the credit limit"); }
				 */
				
				
				/////////////////// verify System Information /////////////////////////
				eo.javaScriptScrollToViewElement(driver, "xpath", "updateNS", CURR_APP);
				
				// verify Update Netsuite checkbox
				//if(!orderStatus.equalsIgnoreCase("Order Accepted") && 
				if(eo.getAttribute(driver, "xpath", "updateNS", "title", CURR_APP).equalsIgnoreCase("Checked")) {
					this.addComment("Successfully verified the Update Netsuite checkbox as <b>selected</b>");	 
				}
				
				//else if(orderStatus.equalsIgnoreCase("Order Accepted") && 
				//		eo.getAttribute(driver, "xpath", "updateNS", "title", CURR_APP).equalsIgnoreCase("Not Checked")) {
				//	this.addComment("Successfully verified the Update Netsuite checkbox as <b>unselected</b>");
				//}
				
				else {
					this.addComment("User is unable to verify the Update Netsuite checkbox");
					throw new KDTKeywordInitException("User is unable to verify the Update Netsuite checkbox");
				}
				
				// verify sync to Netsuite checkbox
				if(GTC.isScreeningByPass != null && GTC.isScreeningByPass.equalsIgnoreCase("Yes")) {
					for(int i = 1; i <= 20; i++) {
						if(eo.getAttribute(driver, "xpath", "syncToNS", "title", CURR_APP).equalsIgnoreCase("Checked")) {
							eo.wait(12);
							driver.navigate().refresh();
						} 
						else {
							break;
						}
					}
				}
				
				if(isScreeningByPass != null) {
					GTC.isScreeningByPass = isScreeningByPass;
				}
				
				if((orderStatus.equalsIgnoreCase("PO Received")) &&
						eo.getAttribute(driver, "xpath", "syncToNS", "title", CURR_APP).equalsIgnoreCase("Checked")) {
					this.addComment("Successfully verified the sync to Netsuite checkbox as <b>selected</b>");	 
				}
				else if((orderStatus.equalsIgnoreCase("Order Accepted")  || orderStatus.equalsIgnoreCase("Pending")) &&
						eo.getAttribute(driver, "xpath", "syncToNS", "title", CURR_APP).equalsIgnoreCase("Not Checked")) {
					this.addComment("Successfully verified the sync to Netsuite checkbox as <b>unselected</b>");
				}
				else {
					this.addComment("User is unable to verify the sync to Netsuite checkbox");
					throw new KDTKeywordInitException("User is unable to verify the sync to Netsuite checkbox");
				}
				
				// verify Netsuite Order No
				String orderNo = eo.getText(driver, "xpath", "NSorderNo", CURR_APP);
				if(!orderStatus.equalsIgnoreCase("Order Accepted") && orderNo.isBlank()) {
					this.addComment("Successfully verified Netsuite Order No as <b>Blank</b>");	 
				}
				else if(orderStatus.equalsIgnoreCase("Order Accepted") && !orderNo.isBlank()) {
					this.addComment("Successfully verified Netsuite Order No is <b>" + orderNo + "</b>");
				}
				else {
					this.addComment("User is unable to verify the Netsuite Order No");
					throw new KDTKeywordInitException("User is unable to verify the Netsuite Order No");
				}
				
			/*
				// Verify Internal Order Stage
				if(orderStatus.equalsIgnoreCase("Pending") && eo.getText(driver, "xpath", "internalOrderStage", CURR_APP).trim().equalsIgnoreCase("Pending")) {
					this.addComment("Internal order stage is successfully verified as <b>Pending</b>");	 
				}
				else if(!orderStatus.equalsIgnoreCase("Pending") && eo.getText(driver, "xpath", "internalOrderStage", CURR_APP).trim().equalsIgnoreCase("Order Submitted")) {
					this.addComment("Internal order stage is successfully verified as <b>Order Submitted</b>");
				}
				else {
					this.addComment("User is unable to verify Internal order stage");
					throw new KDTKeywordInitException("User is unable to verify Internal order stage");
				}
				
			*/	
				eo.javaScriptScrollToViewElement(driver, "xpath", "order_status", CURR_APP);
					
			} catch (Exception e) {
				this.addComment("User is Unable to verify the Order GTC fields");
				throw new KDTKeywordExecException("User is Unable to verify the Order GTC fields");
			}
		}
		
	}
	
	
	
	public static class VerifyScreenAndOrderStatus extends Keyword {

		private String resubmitRequired;
		
		@Override
		public void init() throws KDTKeywordInitException {

			super.init();
			try {
				resubmitRequired = args.get("ResubmitRequired");	

			} catch (Exception e) {
				this.addComment("Error while initializing VerifyScreenAndOrderStatus");
				throw new KDTKeywordInitException("Error while initializing VerifyScreenAndOrderStatus", e);
			}

		}
		@Override
		public void exec() throws KDTKeywordExecException {
			// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();
				eo.waitForPageload(driver);
				
				// Post submission Screen Status Validation
				boolean screeningFlag = false;
				
				String expectedScreenStatus = (GTC.isScreeningByPass != null && GTC.isScreeningByPass.equalsIgnoreCase("Yes")) ? "Approved-Bypassed" : "Approved" ;
				
				eo.javaScriptScrollToViewElement(driver, "xpath", "screenStatus", CURR_APP);
				
				for (int i = 1; i < 100; i++) {
					System.out.println("Screen Status:" + i);
					if (! eo.getText(driver, "xpath", "screenStatus", CURR_APP).equalsIgnoreCase(expectedScreenStatus)) {
						screeningFlag = false;
						eo.wait(20);
						driver.navigate().refresh();
					} else if (eo.getText(driver, "xpath", "screenStatus", CURR_APP).equalsIgnoreCase(expectedScreenStatus)) {
						screeningFlag = true;
						break;
					}
				}

				if (screeningFlag == false) {
					this.addComment("The screen status does not match with expected result");
					throw new KDTKeywordInitException("The screen status does not match with expected result");
				} else {
					this.addComment("The screen status is verified as Screen status = <b>"
							+ eo.getText(driver, "xpath", "screenStatus", CURR_APP) + "</b>");
				}

				// Resubmit The order
				if(resubmitRequired != null && resubmitRequired.equalsIgnoreCase("Yes")) {
					try {
						
						eo.clickElement(driver, "xpath", "submitOrder_button", CURR_APP);
						eo.wait(10);
						driver.switchTo().alert().accept();
						this.addComment("Order Resumitted Successfully");

					} catch (Exception e) {
						this.addComment("User is Unable to Submit Order");
						throw new KDTKeywordExecException("User is Unable to Submit Order", e);
					}
				}
				else {
					// Post submission Order Status Validation
					boolean orderFlag = false;
					eo.javaScriptScrollToViewElement(driver, "xpath", "order_status", CURR_APP);
					
					for (int i = 1; i < 100; i++) {
						System.out.println("order status: " + i);
						if (!eo.getText(driver, "xpath", "order_status", CURR_APP).equalsIgnoreCase("Order Accepted")) {
							orderFlag = false;
							eo.wait(40);
							driver.navigate().refresh();
						} else if (eo.getText(driver, "xpath", "order_status", CURR_APP)
								.equalsIgnoreCase("Order Accepted")) {
							orderFlag = true;
							break;
						}
					}
	
					if (orderFlag == false) {
						this.addComment("The order status does not match with expected result");
						throw new KDTKeywordInitException("TThe order status does not match with expected result");
					} else {
						this.addComment("The order status is verified order status = <b>"
								+ eo.getText(driver, "xpath", "order_status", CURR_APP) + "</b>");
					}
				}
				
			} catch (Exception e) {
				this.addComment("User is Unable to Verify the Screen Status and Order Status");
				throw new KDTKeywordExecException("User is Unable to Verify the Screen Status and Order Status", e);
			}
		}
	
	}
	
	
	public static class OpenOrderSFDC extends Keyword {

		private String orderNumber;

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();

			try {
				orderNumber = context.getData().get("$orderNo");

				eo.enterText(driver, "xpath", "searchFieldForSO", orderNumber, CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "searchedOrderLink", CURR_APP);
				driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);

			} catch (Exception e) {
				this.addComment("User is Unable to Open Order SFDC");
				throw new KDTKeywordExecException("User is Unable to Open Order SFDC", e);
			}
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
}