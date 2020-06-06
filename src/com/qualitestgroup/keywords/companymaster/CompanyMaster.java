package com.qualitestgroup.keywords.companymaster;

import java.util.ArrayList;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Key;
import org.testng.Assert;

import com.itextpdf.text.log.SysoCounter;
import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.getproperty.GetProperty;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.keywords.common.ElementOperation;
import com.qualitestgroup.util.seleniumtools.SeleniumTools;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

public class CompanyMaster extends KeywordGroup {
	
	public static final String CURR_APP = "companymaster";
	public static GetElementIdentifier gei = new GetElementIdentifier();
	static GetProperty getProps = new GetProperty();
	public static ElementOperation eo = new ElementOperation();
	public static String accountName;
	private static String federalAccFlag=" "; //Flag = false is non federal, flag= true is federal account
	public static ArrayList<String> accCreateFieldVal = new ArrayList<String>();
	
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
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			if (!(browser.isEmpty())) {
				try {
					Keyword.run("Browser", "Launch", "Browser", browser);
				} catch (KDTException e) {
					// TODO Auto-generated catch block
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
	
	public static class VerifyExistingLeadValidation extends Keyword {
		private String lastName;
		private String company;
		private String email;
		private String leadStatus = "";
		private String leadSource;
		private String businessUnit;
		private String browser = "";
		private String firstName;
		private String country;
		private String street;
		private String city;
		private String state;
		private String zip;
		private String convertedStatus;
		private String subject;
		private String type;
		
		private String phone;
		private String status;
		private String priority;
		private String meetingType;
	

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			
			if (hasArgs("Browser")) {
				browser = args.get("Browser");
			}
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		//clickOnSearchField
		eo.clickElement(driver, "xpath", "leadsTab", CURR_APP);
		eo.waitForPageload(driver);
		eo.clickElement(driver, "xpath", "newButtonLeads", CURR_APP);
		eo.waitForPageload(driver);
		addComment("Clicked new Button  is displayed");
		eo.clickElement(driver, "xpath", "saveLead", CURR_APP);
	
		eo.wait(10);
		eo.verifyElementIsPresent(driver, "xpath", "LastNameError", CURR_APP);
		eo.verifyElementIsPresent(driver, "xpath", "CompanyError", CURR_APP);
		eo.verifyElementIsPresent(driver, "xpath", "EmailError", CURR_APP);
		eo.verifyElementIsPresent(driver, "xpath", "LeadSourceError", CURR_APP);
		
		Select scon= new Select(driver.findElement(By.id("lea16country")));
		WebElement Wb= scon.getFirstSelectedOption();
		System.out.println(Wb.getText());
		
		
		if (hasArgs("LastName")) {
			lastName = args.get("LastName");
		}else {
			lastName= "";}
		eo.enterText(driver, "xpath", "LastNameLeads", lastName, CURR_APP);
		
		if (hasArgs("Company")) {
			company = args.get("Company");
		}else {
			company= "";}
		eo.enterText(driver, "xpath", "CompanyLeads", company, CURR_APP);
		
		 
		if (hasArgs("Phone")) {
			phone = args.get("Phone");
		}else {
			phone= "";}
		eo.enterText(driver, "xpath", "Phone", phone, CURR_APP);
		
		if (hasArgs("Email")) {
			email = args.get("Email");
		}else {
			email= "";}
		eo.enterText(driver, "xpath", "EmailLeads", email, CURR_APP);
		
		
		if (hasArgs("FirstName")) {
			firstName = args.get("FirstName");
		}else {
			firstName= "";}
		eo.enterText(driver, "xpath", "FirstNameLeads", firstName, CURR_APP);
		
		
		if (hasArgs("LeadSource")) {
			leadSource = args.get("LeadSource");
		}else {
			leadSource= "--None--";}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "LeadSource", "{Lead}", leadSource, CURR_APP);
		
	
		
		
		if (hasArgs("BusinessUnit")) {
			businessUnit = args.get("BusinessUnit");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Businessunit", "{BU}", businessUnit, CURR_APP);
		
		if (hasArgs("LeadStatus")) {
			leadStatus = args.get("LeadStatus");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "LeadStatus", "{LS}", leadStatus, CURR_APP);

		
		if (hasArgs("Country")) {
			country = args.get("Country");
		}else {
			country= "--None--";}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "CountryLeads", "{CON}", country, CURR_APP);
		
		
		if (hasArgs("State")) {
			state = args.get("State");
		}else {
			state= "--None--";}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "StateLeads", "{St}", state, CURR_APP);
		
		
		if (hasArgs("Street")) {
			street = args.get("Street");
		}else {
			street= "";}
		eo.enterText(driver, "xpath", "StreetLeads", street, CURR_APP);
		
		
		
		if (hasArgs("City")) {
			city = args.get("City");
		}else {
			city= "";}
		eo.enterText(driver, "xpath", "CityLeads", city, CURR_APP);
		
		
		
		
		if (hasArgs("Zip")) {
			zip = args.get("Zip");
		}else {
			zip= "";}
		eo.enterText(driver, "xpath", "ZipLeads", zip, CURR_APP);
		
		
		eo.clickElement(driver, "xpath", "SaveNew", CURR_APP);
		eo.waitForPageload(driver);
		eo.verifyElementIsPresent(driver, "xpath", "duplicateError", CURR_APP);
		eo.clickElement(driver, "xpath", "SaveIgnore", CURR_APP);
		eo.waitForPageload(driver);
		eo.clickElement(driver,  "xpath", "Convert", CURR_APP);
		eo.waitForPageload(driver);
		eo.wait(10);
		eo.clickElement(driver, "xpath", "AccountNameClick", CURR_APP);
		eo.wait(5);
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickName", CURR_APP);
		eo.verifyElementIsPresent(driver, "xpath", "AName", CURR_APP);
		String AName= eo.getText(driver, "xpath", "AName", CURR_APP);
		
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickStatus", CURR_APP);
		String AccountClickStatus=eo.getText(driver, "xpath", "AccountClickStatus", CURR_APP);
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickRecordType", CURR_APP);
		String AccountClickRecordType=eo.getText(driver, "xpath", "AccountClickStatus", CURR_APP);
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickOwner", CURR_APP);
		String AccountClickOwner=eo.getText(driver, "xpath", "AccountClickOwner", CURR_APP);
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickID", CURR_APP);
		String AccountClickID=eo.getText(driver, "xpath", "AccountClickID", CURR_APP);
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickStreet", CURR_APP);
		String AccountClickStreet=eo.getText(driver, "xpath", "AccountClickStreet", CURR_APP);
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickState", CURR_APP);
		String AccountClickState=eo.getText(driver, "xpath", "AccountClickState", CURR_APP);
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickPostalCode", CURR_APP);
		String AccountClickPostalCode=eo.getText(driver, "xpath", "AccountClickPostalCode", CURR_APP);
		
		eo.verifyElementIsPresent(driver, "xpath", "AccountClickCountry", CURR_APP);
		String AccountClickCountry=eo.getText(driver, "xpath", "AccountClickCountry", CURR_APP);
		
		eo.clickElement(driver, "xpath", "ClickAccount", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "Cancel", CURR_APP);
		eo.wait(5);
		eo.waitForPageload(driver);
		eo.clickElement(driver, "xpath", "AccountNameClick", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "ClickAccount", CURR_APP);
		eo.wait(10);
		eo.clickElement(driver, "xpath", "OK", CURR_APP);
		
		eo.wait(10);
		eo.waitForPageload(driver);

		
		if (hasArgs("ConvertedStatus")) {
			convertedStatus = args.get("ConvertedStatus");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "ConvertedStatus", "{CONSTA}", convertedStatus, CURR_APP);
		
		
		if (hasArgs("ConvertedStatus")) {
			convertedStatus = args.get("ConvertedStatus");
		}
		
		if (hasArgs("Subject")) {
			subject = args.get("Subject");
		}
		eo.enterText(driver, "xpath", "Subject", subject, CURR_APP);
		eo.clickElement(driver, "xpath", "DueDate", CURR_APP);
		
		if (hasArgs("Type")) {
			type = args.get("Type");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Type", "{Typ}", type, CURR_APP);
		
		
		if (hasArgs("Priority")) {
			priority = args.get("Priority");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Priority", "{Prio}", priority, CURR_APP);
		
		
		if (hasArgs("Status")) {
			status = args.get("Status");
		}
		
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Status", "{Stat}", status, CURR_APP);
		

		if (hasArgs("MeetingType")) {
			meetingType = args.get("MeetingType");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "MeetingType", "{MT}", meetingType, CURR_APP);
		eo.clickElement(driver,  "xpath", "Convert", CURR_APP);
		eo.waitForPageload(driver);
		addComment("Convert Button Clicked");
		eo.wait(30);
		eo.clickElement(driver, "xpath", "Edit", CURR_APP);
		eo.waitForPageload(driver);
		eo.wait(10);
		String S=driver.findElement(By.xpath("//label[contains(text(),'CMID')]/ancestor::tr[1]//td[@class='dataCol col02']")).getText();
		if(S!=null)
		{
			addComment("CMID VALUE IS : "+S);
			addComment("Account with data as below is inserted:- ");
			addComment("Company Name :"+company);
			addComment("Street Name :"+street);
			addComment("City Name :"+city);
			addComment("State Name :"+state);
			addComment("Country Name :"+country);
			addComment("Zip  :"+zip);
		}
		else {
		
			throw new KDTKeywordExecException("CMID not displayed");
			
			}		
		}
}
	public static class AddCountry extends Keyword {
		private static String country;
		@Override
		public void exec() throws KDTKeywordExecException  {
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			
			if (hasArgs("Country")) {
				country = args.get("Country");
			}else {
				country= "--None--";}
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "CountryNone", "{CON}", country, CURR_APP);
			
		}
	}
	
	
	public static class VerifyExistingValidationInsufficientData extends Keyword {
		private String lastName;
		private String company;
		private String email;
		private String leadStatus = "";
		private String leadSource;
		private String businessUnit;
		private String browser = "";
		private String firstName;
		private String country;
		private String street;
		private String city;
		private String state;
		private String zip;
		private String convertedStatus;
		private String subject;
		private String type;
		
		private String phone;
		private String status;
		private String priority;
		private String meetingType;
	

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			
			if (hasArgs("Browser")) {
				browser = args.get("Browser");
			}
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		//clickOnSearchField
		eo.clickElement(driver, "xpath", "leadsTab", CURR_APP);
		eo.waitForPageload(driver);
		eo.clickElement(driver, "xpath", "newButtonLeads", CURR_APP);
		eo.waitForPageload(driver);
		
				
		
		if (hasArgs("LastName")) {
			lastName = args.get("LastName");
		}else {
			lastName= "";}
		eo.enterText(driver, "xpath", "LastNameLeads", lastName, CURR_APP);
		
		if (hasArgs("Company")) {
			company = args.get("Company");
		}else {
			company= "";}
		eo.enterText(driver, "xpath", "CompanyLeads", company, CURR_APP);
		
		 
		if (hasArgs("Phone")) {
			phone = args.get("Phone");
		}else {
			phone= "";}
		eo.enterText(driver, "xpath", "Phone", phone, CURR_APP);
		
		if (hasArgs("Email")) {
			email = args.get("Email");
		}else {
			email= "";}
		eo.enterText(driver, "xpath", "EmailLeads", email, CURR_APP);
		
		
		if (hasArgs("FirstName")) {
			firstName = args.get("FirstName");
		}else {
			firstName= "";}
		eo.enterText(driver, "xpath", "FirstNameLeads", firstName, CURR_APP);
		
		
		if (hasArgs("LeadSource")) {
			leadSource = args.get("LeadSource");
		}else {
			leadSource= "--None--";}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "LeadSource", "{Lead}", leadSource, CURR_APP);
		
	
		
		
		if (hasArgs("BusinessUnit")) {
			businessUnit = args.get("BusinessUnit");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Businessunit", "{BU}", businessUnit, CURR_APP);
		
		if (hasArgs("LeadStatus")) {
			leadStatus = args.get("LeadStatus");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "LeadStatus", "{LS}", leadStatus, CURR_APP);

		
		
			country= "--None--";
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "CountryNone", "{CON}", country, CURR_APP);
	
				
				
		if(country!="--None--")
		{
		if (hasArgs("State")) {
			state = args.get("State");
		}else {
			state= "--None--";}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "StateLeads", "{St}", state, CURR_APP);
		}
		
		if (hasArgs("Street")) {
			street = args.get("Street");
			street= "";
		}else {
			street= "";}
		eo.enterText(driver, "xpath", "StreetLeads", street, CURR_APP);
			
		
		if (hasArgs("City")) {
			city = args.get("City");
		}else {
			city= "";}
		eo.enterText(driver, "xpath", "CityLeads", city, CURR_APP);
		
		
		
		
		if (hasArgs("Zip")) {
			zip = args.get("Zip");
		}else {
			zip= "";}
		eo.enterText(driver, "xpath", "ZipLeads", zip, CURR_APP);
		
		
		eo.clickElement(driver, "xpath", "SaveNew", CURR_APP);
		eo.waitForPageload(driver);
		eo.verifyElementIsPresent(driver, "xpath", "duplicateError", CURR_APP);
		eo.clickElement(driver, "xpath", "SaveIgnore", CURR_APP);
		eo.waitForPageload(driver);
		
		eo.actionDoubleClick(driver,  "xpath", "Convert", CURR_APP);
			eo.wait(50);
		
		
		
		
		String alertConvertMessage=driver.switchTo().alert().getText();
		String convertMessage="Please enter country";
		if(alertConvertMessage.contentEquals(convertMessage))
		{
			driver.switchTo().alert().accept();
		driver.navigate().back();
		if (hasArgs("Country")) {
			country = args.get("Country");
			
		}else {
			country= "--None--";}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "CountryLeads", "{CON}", country, CURR_APP);
		}
		
		eo.clickElement(driver, "xpath", "SaveNew", CURR_APP);
		eo.waitForPageload(driver);
		eo.verifyElementIsPresent(driver, "xpath", "duplicateError", CURR_APP);
		eo.clickElement(driver, "xpath", "SaveIgnore", CURR_APP);
		eo.waitForPageload(driver);
		eo.clickElement(driver,  "xpath", "Convert", CURR_APP);
	
	
		
		
		
		
		
		eo.clickElement(driver, "xpath", "AccountNameClick", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "ClickAccountP", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "Cancel", CURR_APP);
		eo.wait(5);
		eo.waitForPageload(driver);
		eo.clickElement(driver, "xpath", "AccountNameClick", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "ClickAccountP", CURR_APP);
		eo.wait(10);
		eo.clickElement(driver, "xpath", "OK", CURR_APP);
		
		eo.wait(10);
		eo.waitForPageload(driver);

		
		if (hasArgs("ConvertedStatus")) {
			convertedStatus = args.get("ConvertedStatus");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "ConvertedStatus", "{CONSTA}", convertedStatus, CURR_APP);
		
		
		if (hasArgs("ConvertedStatus")) {
			convertedStatus = args.get("ConvertedStatus");
		}
		
		if (hasArgs("Subject")) {
			subject = args.get("Subject");
		}
		eo.enterText(driver, "xpath", "Subject", subject, CURR_APP);
		eo.clickElement(driver, "xpath", "DueDate", CURR_APP);
		
		if (hasArgs("Type")) {
			type = args.get("Type");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Type", "{Typ}", type, CURR_APP);
		
		
		if (hasArgs("Priority")) {
			priority = args.get("Priority");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Priority", "{Prio}", priority, CURR_APP);
		
		
		if (hasArgs("Status")) {
			status = args.get("Status");
		}
		
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Status", "{Stat}", status, CURR_APP);
		

		if (hasArgs("MeetingType")) {
			meetingType = args.get("MeetingType");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "MeetingType", "{MT}", meetingType, CURR_APP);
		eo.clickElement(driver,  "xpath", "Convert", CURR_APP);
		eo.waitForPageload(driver);
		addComment("Convert Button Clicked");
		eo.wait(30);
		eo.clickElement(driver, "xpath", "Edit", CURR_APP);
		eo.waitForPageload(driver);
		eo.wait(10);
		String S=driver.findElement(By.xpath("//label[contains(text(),'CMID')]/ancestor::tr[1]//td[@class='dataCol col02']")).getText();
		if(S!=null)
		{
			addComment("CMID VALUE IS : "+S);
			addComment("Account with data as below is inserted:- ");
			addComment("Company Name :"+company);
			addComment("Street Name :"+street);
			addComment("City Name :"+city);
			addComment("State Name :"+state);
			addComment("Country Name :"+country);
			addComment("Zip  :"+zip);
		}
		else {
		
			throw new KDTKeywordExecException("CMID not displayed");
			
			}		
		}
}


	
	public static class VerifyNewLeadValidation extends Keyword {
		private String lastName;
		private String company;
		private String email;
		private String leadStatus = "";
		private String leadSource;
		private String businessUnit;
		private String browser = "";
		private String firstName;
		private String country;
		private String street;
		private String city;
		private String state;
		private String zip;
		private String convertedStatus;
		private String subject;
		private String type;
		private String status;
		private String priority;
		private String meetingType;
		private String phone;
	

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			if (hasArgs("Browser")) {
				browser = args.get("Browser");
			}
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		//clickOnSearchField
		eo.clickElement(driver, "xpath", "leadsTab", CURR_APP);
		eo.waitForPageload(driver);
		eo.clickElement(driver, "xpath", "newButtonLeads", CURR_APP);
		eo.waitForPageload(driver);
		addComment("Clicked new Button  is displayed");
		eo.clickElement(driver, "xpath", "saveLead", CURR_APP);
		eo.wait(10);
		
		eo.verifyElementIsPresent(driver, "xpath", "LastNameError", CURR_APP);
		eo.verifyElementIsPresent(driver, "xpath", "CompanyError", CURR_APP);
		eo.verifyElementIsPresent(driver, "xpath", "EmailError", CURR_APP);
		eo.verifyElementIsPresent(driver, "xpath", "LeadSourceError", CURR_APP);
		
		Select scon= new Select(driver.findElement(By.id("lea16country")));
		WebElement Wb= scon.getFirstSelectedOption();
		System.out.println(Wb.getText());
		if (hasArgs("LastName")) {
			lastName = args.get("LastName");
		}
		eo.enterText(driver, "xpath", "LastNameLeads", lastName, CURR_APP);
		
		if (hasArgs("Company")) {
			company = args.get("Company");
		}
		eo.enterText(driver, "xpath", "CompanyLeads", company, CURR_APP);
		
		 
		if (hasArgs("Phone")) {
			phone = args.get("Phone");
		}
		eo.enterText(driver, "xpath", "Phone", phone, CURR_APP);
		
		if (hasArgs("Email")) {
			email = args.get("Email");
		}
		eo.enterText(driver, "xpath", "EmailLeads", email, CURR_APP);
		
		
		if (hasArgs("FirstName")) {
			firstName = args.get("FirstName");
		}
		eo.enterText(driver, "xpath", "FirstNameLeads", firstName, CURR_APP);
		
		
		if (hasArgs("LeadSource")) {
			leadSource = args.get("LeadSource");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "LeadSource", "{Lead}", leadSource, CURR_APP);
		
	
		
		
		if (hasArgs("BusinessUnit")) {
			businessUnit = args.get("BusinessUnit");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Businessunit", "{BU}", businessUnit, CURR_APP);
		
		if (hasArgs("LeadStatus")) {
			leadStatus = args.get("LeadStatus");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "LeadStatus", "{LS}", leadStatus, CURR_APP);

		
		if (hasArgs("Country")) {
			country = args.get("Country");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "CountryLeads", "{CON}", country, CURR_APP);
		
		
		if (hasArgs("State")) {
			state = args.get("State");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "StateLeads", "{St}", state, CURR_APP);
		
		
		if (hasArgs("Street")) {
			street = args.get("Street");
		}
		eo.enterText(driver, "xpath", "StreetLeads", street, CURR_APP);
		
		
		
		if (hasArgs("City")) {
			city = args.get("City");
		}
		eo.enterText(driver, "xpath", "CityLeads", city, CURR_APP);
		
		
		
		
		if (hasArgs("Zip")) {
			zip = args.get("Zip");
		}
		eo.enterText(driver, "xpath", "ZipLeads", zip, CURR_APP);
		
		
		
		
		eo.clickElement(driver, "xpath", "saveLead", CURR_APP);
		eo.waitForPageload(driver);
		eo.clickElement(driver,  "xpath", "Convert", CURR_APP);
		eo.waitForPageload(driver);
		eo.wait(10);
		eo.clickElement(driver, "xpath", "AccountNameClick", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "ClickAccount", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "Cancel", CURR_APP);
		eo.wait(5);
		eo.waitForPageload(driver);
		eo.clickElement(driver, "xpath", "AccountNameClick", CURR_APP);
		eo.wait(5);
		eo.clickElement(driver, "xpath", "ClickAccount", CURR_APP);
		eo.wait(10);
		eo.clickElement(driver, "xpath", "OK", CURR_APP);
		eo.wait(10);
		eo.waitForPageload(driver);
		
		
		
		if (hasArgs("ConvertedStatus")) {
			convertedStatus = args.get("ConvertedStatus");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "ConvertedStatus", "{CONSTA}", convertedStatus, CURR_APP);
		
		
		if (hasArgs("ConvertedStatus")) {
			convertedStatus = args.get("ConvertedStatus");
		}
		
		if (hasArgs("Subject")) {
			subject = args.get("Subject");
		}
		eo.enterText(driver, "xpath", "Subject", subject, CURR_APP);
		eo.clickElement(driver, "xpath", "DueDate", CURR_APP);
		
		
		
		if (hasArgs("Type")) {
			type = args.get("Type");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Type", "{Typ}", type, CURR_APP);
		
		
		if (hasArgs("Priority")) {
			priority = args.get("Priority");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Priority", "{Prio}", priority, CURR_APP);
		
		
		if (hasArgs("Status")) {
			status = args.get("Status");
		}
		
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "Status", "{Stat}", status, CURR_APP);
		
		
	
		if (hasArgs("MeetingType")) {
			meetingType = args.get("MeetingType");
		}
		eo.clickElementAfterReplacingKeyValue(driver, "xpath", "MeetingType", "{MT}", meetingType, CURR_APP);
		eo.clickElement(driver,  "xpath", "Convert", CURR_APP);
		eo.waitForPageload(driver);
		addComment("Convert Button Clicked");
		eo.wait(30);
		eo.clickElement(driver, "xpath", "Edit", CURR_APP);
		eo.waitForPageload(driver);
		eo.wait(10);
		String S=driver.findElement(By.xpath("//label[contains(text(),'CMID')]/ancestor::tr[1]//td[@class='dataCol col02']")).getText();
		if(S!=null)
		{
			addComment("CMID VALUE IS : "+S);
			
		}
		else {
			throw new KDTKeywordExecException("CMID not displayed");
			
			}		
}
}
	
	public static class SearchAndLoginAsUser extends Keyword {

		private String searchLoginUserName;
		
		@Override
		public void init() throws KDTKeywordInitException {
		// TODO Auto-generated method stub
		super.init();
		searchLoginUserName = args.get("SearchLoginUserName").trim();
		
}
		@Override
			public void exec() throws KDTKeywordExecException {
				int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
				WebDriver driver = context.getWebDriver();
				
				//clickOnSearchField
				eo.clickElement(driver, "XPATH", "search_input_tab", CURR_APP);
				addComment("Successfully click on search input tab");
				
				//enter user name in search field
				eo.enterText(driver, "XPATH", "search_input_tab", searchLoginUserName, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("search_input_tab", CURR_APP))).sendKeys(Keys.TAB);
				addComment("Entered Login User Name in search Field");
				
				//click on search button
				eo.clickElement(driver, "ID", "searchButton", CURR_APP);
				eo.waitForPageload(driver);
				addComment("Successfully click on search button");
				
				//click on user name
				eo.clickElementAfterReplacingKeyValue(driver, "XPATH", "ClickOnUserName","{searchLoginUserName}",searchLoginUserName, CURR_APP);
				addComment("Successfully click on user name link");				
				
				//click on action menu
				eo.clickElement(driver, "XPATH", "UserActionMenu", CURR_APP);
				addComment("Successfully click on user action menu");				
			
				//click on user detail
				eo.clickElement(driver, "XPATH", "UserDetailLink", CURR_APP);
				addComment("Successfully click on user detail link");				
			
				// login as user
				eo.clickElement(driver, "XPATH", "UserLoginButton", CURR_APP);
				addComment("Successfully click on user login button");				
				eo.waitForPageload(driver);
				
		}

}

	public static class EnterAccountDetails extends Keyword {
		
		private String billingCountry;
		private String billingState;
		private String billingCity;
		private String billingStreet;
		private String billingPostalCode;
		private int i=0;
		
		@Override
		public void init() throws KDTKeywordInitException {
		super.init();
		
		accCreateFieldVal.clear();
		if(hasArgs("AccountName")){
		accountName = args.get("AccountName").trim();
		}
		if(hasArgs("BillingCountry")){
		billingCountry = args.get("BillingCountry").trim();
		accCreateFieldVal.add("billingCountry:"+billingCountry);
		}
		if(hasArgs("BillingState")){
		billingState = args.get("BillingState").trim();
		accCreateFieldVal.add("billingState:"+billingState);
		}
		if(hasArgs("BillingCity")){
		billingCity = args.get("BillingCity").trim();
		accCreateFieldVal.add("billingCity:"+billingCity);
		}
		if(hasArgs("BillingStreet")){
		billingStreet = args.get("BillingStreet").trim();
		accCreateFieldVal.add("billingStreet:"+billingStreet);
		}
		if(hasArgs("BillingPostalCode")){
		billingPostalCode = args.get("BillingPostalCode").trim();
		accCreateFieldVal.add("billingPostalCode:"+billingPostalCode);
		}
		federalAccFlag = args.get("FederalAccFlag").trim();
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
				//Enter Account name
                saveValue(accountName);
                context.getData().get("$expectedAccountName");
                if(!(accountName.isEmpty())) {
                accountName=accountName+ eo.gnerateRandomNo(10);
				eo.enterText(driver, "XPATH", "accNameField", accountName, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("accNameField", CURR_APP))).sendKeys(Keys.TAB);
				addComment("Entered acc name");
                }
  ////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println(accCreateFieldVal);
       for(i=0;i<accCreateFieldVal.size()-1;i++)
       {
    	  String[] FieldValue= accCreateFieldVal.get(i).toString().split(":");
    	  String key = FieldValue[0];
    	  String value="";
    	  if(FieldValue.length==2){
    		   value = FieldValue[1].trim();
    	  }else {
    		  value="";
    	  }
    	  
   	  if(!(value.isEmpty())){
    	  switch(key)
    	  {
    	  		case "billingCountry":eo.selectComboBoxByVisibleText(driver, "xpath", "billingCountryField", value, CURR_APP);
    	  							  addComment("Selected billing country");
    	  							  break;
    	  		case "billingState": eo.selectComboBoxByVisibleText(driver, "xpath", "billingStateField", billingState, CURR_APP);
    	  							 addComment("Selected billing state");	
    	  							 break;
    	  		case "billingCity":	eo.enterText(driver, "XPATH", "billingCityField", billingCity, CURR_APP);
    	  							driver.findElement(By.xpath(gei.getProperty("billingCityField", CURR_APP))).sendKeys(Keys.TAB);
    	  							addComment("Entered billing city");	
    	  							break;
    	  		case "billingStreet":eo.enterText(driver, "XPATH", "billingStreetField",billingStreet , CURR_APP);
    	  							 driver.findElement(By.xpath(gei.getProperty("billingStreetField", CURR_APP))).sendKeys(Keys.TAB);
    	  							 addComment("Entered billing sitreet");
    	  							 break;
    	  		case "billingPostalCode":eo.enterText(driver, "XPATH", "billingPostalCode",billingPostalCode , CURR_APP);
    	  								 driver.findElement(By.xpath(gei.getProperty("billingPostalCode", CURR_APP))).sendKeys(Keys.TAB);
    	  								 addComment("Entered postal code");	
    	  								 break;
    	  		 default:addComment("Default option");					
    	  						
    	  }
       }else
       {
    	   addComment("key:"+key+"Value:"+value);
       }
   	  
   	  }
       if(federalAccFlag.equalsIgnoreCase("true"))
            {
                	eo.clickElement(driver, "xpath", "federalIntelCheckbox", CURR_APP);
                	addComment("Federal account");
              } else  if(federalAccFlag.equalsIgnoreCase("false"))
                {
                	addComment("Non-Federal account");
             }
                //click on search/create button
				eo.clickElement(driver, "xpath", "accSearchOrCreateBtn", CURR_APP);
				eo.wait(5);
				eo.waitForPageload(driver);
				if(federalAccFlag.equalsIgnoreCase("true"))
				{
					eo.verifyExactScreenText(driver, "xpath", "warningText", "Salesforce internal search result", CURR_APP);
					addComment("Verified warnig message as -Salesforce internal search result");
				}
		}
}
	
	////////////////////////// verifyCreateButton///////////////////////

	public static class verifyCreateButton extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
		super.init();
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			//verify create button is displayed in result section
			String createButton=eo.getText(driver, "xpath", "accCreateBtn", CURR_APP);
			System.out.println("The result section displays::"+createButton);
			if (createButton.equalsIgnoreCase("Create")) {
				addComment("Successfully verified the string for create account button is <b> Create </b>");
				} else {
				addComment("Expected text on button is <b> Create </b>");
				addComment("Actual text on button is  name  is " + createButton);
				throw new KDTKeywordExecException("Actual and Expected button in result section are not same");
				}
			
		}
	}

	///////////////////////FROM CREATE BUTTON TO CREATE ACCOUNT//////////////////////////
	
	public static class CreateNewAccountFromCreateButton extends Keyword {

		private String newrecordType;
		@Override
		public void init() throws KDTKeywordInitException {
		super.init();
		newrecordType = args.get("NewRecordType").trim();
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			//verify create button is displayed in result section
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("accCreateBtn", CURR_APP))));
					//eo.actionDoubleClick(driver, "xpath", "accCreateBtn", CURR_APP);
					eo.clickElement(driver, "xpath", "accCreateBtn", CURR_APP);
					eo.waitForPageload(driver);
					eo.selectComboBoxByVisibleText(driver, "xpath", "recordeTypeField", newrecordType.trim(), CURR_APP);
					eo.clickElement(driver, "xpath", "continueBtn", CURR_APP);
					eo.wait(10);
					eo.waitForPageload(driver);
					eo.wait(5);
					String actualaccountName = eo.getText(driver, "XPATH", "createdAccNameText", CURR_APP);
					System.out.println("actualaccountName::"+actualaccountName);
					System.out.print("accountName:"+ accountName);
					if (actualaccountName.contains(accountName)) {
					addComment("Successfully verified the created account name is <b>" + accountName + "</b>");
					} else {
					addComment("Expected account name  is " + accountName);
					addComment("Actual account name  is " + actualaccountName);
					throw new KDTKeywordExecException("Actual and Expected account name are not same");
					}
			
		}
	}
	
	/////////////////////////verifyViewButton/////////////////////////
	public static class verifyViewButton extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
		super.init();
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			//verify create button is displayed in result section
			String viewButton=eo.getText(driver, "xpath", "accViewBtn", CURR_APP);
			System.out.println("The result section displays::"+viewButton);
			if (viewButton.equalsIgnoreCase("View")) {
				addComment("Successfully verified the string for view account button is <b> View </b>");
				} else {
				addComment("Expected text on button is <b> View </b>");
				addComment("Actual text on button is  name  is " + viewButton);
				throw new KDTKeywordExecException("Actual and Expected button in result section are not same");
				}
			
		}
	}


	public static class ViewAccountFromViewButton extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
		super.init();
		
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			//verify create button is displayed in result section
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("accViewBtn", CURR_APP))));
					//eo.actionDoubleClick(driver, "xpath", "accCreateBtn", CURR_APP);
					eo.clickElement(driver, "xpath", "accViewBtn", CURR_APP);
					eo.waitForPageload(driver);
			
		}
	}
	
	public static class VerifyWarningMessage extends Keyword {

		private String actualWarningMessage;
		private String[] expectedWarningMessageAndCommand;
		@Override
		public void init() throws KDTKeywordInitException {
		super.init();
		expectedWarningMessageAndCommand = args.get("ExpectedWarningMessageAndCommand").trim().split(";");
		}
		
		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			WebDriver driver = context.getWebDriver();
			eo.wait(10);
			
			//verify create button is displayed in result section
			actualWarningMessage= eo.getText(driver, "xpath", "warningText", CURR_APP);
			if ((actualWarningMessage.contains(expectedWarningMessageAndCommand[0]))&& (actualWarningMessage.contains(expectedWarningMessageAndCommand[1]))){
				addComment("Successfully verified the warning command, message is <b>" +expectedWarningMessageAndCommand[0]+":" +expectedWarningMessageAndCommand[1] + "</b>");
				} else {
				addComment("Expected warning message is " + expectedWarningMessageAndCommand[1]);
				addComment("Actual warning message is " + expectedWarningMessageAndCommand[0]);
				throw new KDTKeywordExecException("Actual and Expected warning message in result section are not same");
				}
			
		}
	}
	

public static class VerifyCreateAccSection extends Keyword {

	private String actualSectionTitle;
	private String expectedSectionTitle = "Create New Account";
	private String actualNameColumn;
	private String expectedNameColumn = "Name";
	private String actualStreetColumn;
	private String expectedStreetColumn = "Billing Street";
	private String actualCityColumn;
	private String expectedCityColumn = "Billing City";
	private String actualStateColumn;
	private String expectedStateColumn = "Billing State";
	private String actualPostalColumn;
	private String expectedPostalColumn = "Billing Postal Code";
	private String actualCountryColumn;
	private String expectedCountryColumn = "Billing Country";
	private String billingState;
	private String billingCity;
	private String billingPostalCode;
	private String cityInResultSection;
	private String postalCodeInResultSection;
	private String stateInResultSection;
	
	
	
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	if(hasArgs("BillingState")){
		billingState = args.get("BillingState").trim();
		}
		if(hasArgs("BillingCity")){
		billingCity = args.get("BillingCity").trim();
		}
		if(hasArgs("BillingPostalCode")){
		billingPostalCode = args.get("BillingPostalCode").trim();
		}
		
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		//verify create section tile is displayed in result section
		actualSectionTitle= eo.getText(driver, "xpath", "createAccountSection", CURR_APP);
		if (actualSectionTitle.equalsIgnoreCase(expectedSectionTitle)) {
			addComment("Successfully verified the section tile is <b>" + expectedSectionTitle + "</b>");
			} else {
			addComment("Expected section tile is " + expectedSectionTitle);
			addComment("Actual section tile is " + actualSectionTitle);
			throw new KDTKeywordExecException("Actual and Expected section tile in result section are not same");
			}
		
		//verify name column title
		actualNameColumn = eo.getText(driver, "xpath", "nameColumn", CURR_APP);
		if (actualNameColumn.equalsIgnoreCase(expectedNameColumn)) {
			addComment("Successfully verified the Name Column Title is <b>" + expectedNameColumn + "</b>");
			} else {
			addComment("Expected Name Column Title is " + expectedNameColumn);
			addComment("Actual Name Column Title is " + actualNameColumn);
			throw new KDTKeywordExecException("Actual and Expected Name Column Title in result section are not same");
			}
		
		//verify street column tile
		actualStreetColumn = eo.getText(driver, "xpath", "streetColumn", CURR_APP);
		if (actualStreetColumn.equalsIgnoreCase(expectedStreetColumn)) {
			addComment("Successfully verified the Street Column Title is <b>" + expectedStreetColumn + "</b>");
			} else {
			addComment("Expected Street Column Title is " + expectedStreetColumn);
			addComment("Actual Street Column Title is " + actualStreetColumn);
			throw new KDTKeywordExecException("Actual and Expected Street Column Title in result section are not same");
			}
		
		//verify city column tile
		actualCityColumn = eo.getText(driver, "xpath", "cityColumn", CURR_APP);
		if (actualCityColumn.equalsIgnoreCase(expectedCityColumn)) {
			addComment("Successfully verified the City Column Title is <b>" + expectedCityColumn + "</b>");
			} else {
			addComment("Expected City Column Title is " + expectedCityColumn);
			addComment("Actual City Column Title is " + actualCityColumn);
			throw new KDTKeywordExecException("Actual and Expected City Column Title in result section are not same");
			}
		
		//verify state column tile 
		actualStateColumn = eo.getText(driver, "xpath", "stateColumn", CURR_APP);
		if (actualStateColumn.equalsIgnoreCase(expectedStateColumn)) {
			addComment("Successfully verified the State Column Title is <b>" + expectedStateColumn + "</b>");
			} else {
			addComment("Expected State Column Title is " + expectedStateColumn);
			addComment("Actual State Column Title is " + actualStateColumn);
			throw new KDTKeywordExecException("Actual and Expected State Column Title in result section are not same");
			}
		
		
		//verify postal code column tile
		actualPostalColumn = eo.getText(driver, "xpath", "postalColumn", CURR_APP);
		if (actualPostalColumn.equalsIgnoreCase(expectedPostalColumn)) {
			addComment("Successfully verified the Postal Column Title is <b>" + expectedPostalColumn + "</b>");
			} else {
			addComment("Expected Postal Column Title is " + expectedPostalColumn);
			addComment("Actual Postal Column Title is " + actualPostalColumn);
			throw new KDTKeywordExecException("Actual and Expected Postal Column Title in result section are not same");
			}
		
		
		//verify country column tile
		actualCountryColumn = eo.getText(driver, "xpath", "countryColumn", CURR_APP);
		if (actualCountryColumn.equalsIgnoreCase(expectedCountryColumn)) {
			addComment("Successfully verified the Country Column Title is <b>" + expectedCountryColumn + "</b>");
			} else {
			addComment("Expected Country Column Title is " + expectedCountryColumn);
			addComment("Actual Country Column Title is " + actualCountryColumn);
			throw new KDTKeywordExecException("Actual and Expected Country Column Title in result section are not same");
			}
		
		//verify if city is populated blank when data for city is not given
		if(billingCity.isEmpty()) {
		cityInResultSection = eo.getTextAfterReplacingKeyValue(driver, "xpath", "cityCreateResultSection", "{accountName}",accountName, CURR_APP).trim();
		if(cityInResultSection.equalsIgnoreCase(billingCity)) {
			addComment("Successfully verified that city is blank and is <b>" + billingCity + "</b>");
		} else {
		addComment("Expected City data is " + billingCity);
		addComment("Actual City data is " + cityInResultSection);
		throw new KDTKeywordExecException("Actual and Expected City data in result section are not same");
		}
		}	
		
		//verify if pincode is populated blank when data for pincode is not given
		if(billingPostalCode.isEmpty()) {
		postalCodeInResultSection = eo.getTextAfterReplacingKeyValue(driver, "xpath", "postalCodeCreateResultSection", "{accountName}",accountName, CURR_APP).trim();
		if(postalCodeInResultSection.equalsIgnoreCase(billingPostalCode)) {
			addComment("Successfully verified that pincode is blank and is <b>" + billingPostalCode + "</b>");
			} else {
				addComment("Expected pincode data is " + billingPostalCode);
				addComment("Actual pincode data is " + postalCodeInResultSection);
				throw new KDTKeywordExecException("Actual and Expected pincode data in result section are not same");
				}
				}
		
		//verify if state is populated blank when data for state is not given
		if(billingState.isEmpty()) {
		stateInResultSection = eo.getTextAfterReplacingKeyValue(driver, "xpath", "stateCreateResultSection", "{accountName}",accountName, CURR_APP).trim();
		if(stateInResultSection.equalsIgnoreCase(billingState)) {
		addComment("Successfully verified that state data is blank and is <b>" + billingState + "</b>");
			} else {
				addComment("Expected state data is " + billingState);
				addComment("Actual state data is " + stateInResultSection);	
				throw new KDTKeywordExecException("Actual and Expected state data in result section are not same");
				}
				}
		
	}
}

public static class VerifySimilarAccSection extends Keyword {

	private String actualSectionTitle;
	private String expectedSectionTitle = "Similar accounts found";
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		//verify create button is displayed in result section
		actualSectionTitle= eo.getText(driver, "xpath", "SimilarAccountSection", CURR_APP);
		if (actualSectionTitle.equalsIgnoreCase(expectedSectionTitle)) {
			addComment("Successfully verified the section tile is <b>" + expectedSectionTitle + "</b>");
			} else {
			addComment("Expected section tile is " + expectedSectionTitle);
			addComment("Actual section tile is " + actualSectionTitle);
			throw new KDTKeywordExecException("Actual and Expected section tile in result section are not same");
			}
		
	}
}

public static class VerifyAlreadyExistsSection extends Keyword {

	private String actualSectionTitle;
	private String expectedSectionTitle = "Account already exists";
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		//verify create button is displayed in result section
		actualSectionTitle= eo.getText(driver, "xpath", "AlreadyExistsSection", CURR_APP);
		if (actualSectionTitle.equalsIgnoreCase(expectedSectionTitle)) {
			addComment("Successfully verified the section tile is <b>" + expectedSectionTitle + "</b>");
			} else {
			addComment("Expected section tile is " + expectedSectionTitle);
			addComment("Actual section tile is " + actualSectionTitle);
			throw new KDTKeywordExecException("Actual and Expected section tile in result section are not same");
			}
		
	}
}

public static class LogoutSalesforce extends Keyword {

	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

		try {
			WebDriver driver = context.getWebDriver();

			eo.javaScriptScrollToViewElement(driver, "xpath", "userMenu", CURR_APP);
			eo.clickElement(driver, "xpath", "userMenu", CURR_APP);
			eo.clickElement(driver, "xpath", "logoutButton", CURR_APP);
			//eo.waitForWebElementVisible(driver, "id", "userNameVerify", waiTime, CURR_APP);
		}

		catch (Exception e) {
			// TODO Auto-generated catch block

			this.addComment("Failed to logout from salesforce");
			throw new KDTKeywordExecException("Failed to logout from salesforce", e);
		}
	}
}

public static class SearchingExistingAccDetails extends Keyword {

	private String accountExistingName;
	private String countryName;
	private String street;
	private String streetValidation;
	private String cityExisting;
	private String cityValidation;
	private String postalValidation;
	private String postalExisting;
	private String stateExisting;
	private String stateValidation;
	private String federalAccFlag;


	
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	streetValidation = args.get("StreetValidation");
	cityValidation = args.get("CityValidation");
	postalValidation = args.get("PostalValidation");
	stateValidation = args.get("StateValidation");
	federalAccFlag = args.get("FederalAccFlag");

	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		//click on account tab
		eo.clickElement(driver, "XPATH", "account_tab", CURR_APP);
		addComment("Successfully click on account tab");
		eo.waitForWebElementVisible(driver, "XPATH", "account_new_btn", waiTime, CURR_APP);
		
		// pick up existing account name and country
		eo.waitForWebElementVisible(driver, "XPATH", "firstExistingAccount", waiTime, CURR_APP);
		eo.isDisplayed(driver, "xpath", "firstExistingAccount", CURR_APP);
		
		accountExistingName = eo.getText(driver, "xpath", "firstExistingAccount", CURR_APP);
		countryName = eo.getText(driver, "xpath", "countryFirstExistingAcc", CURR_APP);
        cityExisting = eo.getText(driver, "xpath", "cityFirstExistingAcc", CURR_APP).trim();
        
        // click on account existing
		eo.clickElement(driver, "XPATH", "firstExistingAccount", CURR_APP);
		eo.waitForPageload(driver);
		
		// click on update grain attribute
		eo.clickElement(driver, "XPATH", "updateGrainAttribute", CURR_APP);
		eo.waitForPageload(driver);
		
		if(federalAccFlag.equalsIgnoreCase("true"))
		{
			eo.verifyExactScreenText(driver, "xpath", "warningText", "Federal accounts can be updated directly", CURR_APP);
			addComment("Verified message:Federal accounts can be updated directly");
			eo.clickElement(driver, "xpath", "backBtn", CURR_APP);
			eo.wait(5);
			eo.verifyExactScreenText(driver, "xpath", "updatePageTitle", "Account Detail", CURR_APP);
			eo.clickElement(driver, "xpath", "editButton", CURR_APP);
			eo.wait(3);
		}else{
		// enter updated account details
		eo.waitForWebElementVisible(driver, "XPATH", "updateAccountTitle", waiTime, CURR_APP);
		addComment("Successfully verified the <b> update account page</b> is displayed");
		}
		
	////////////////////////////////////////////////////////////////////////////////////////	
		eo.javaScriptScrollToViewElement(driver,
				driver.findElement(By.xpath(gei.getProperty("updateStreet", CURR_APP))));
		//fetch street data and postal code data
		street = eo.getText(driver, "xpath", "updateStreet", CURR_APP);
		postalExisting = eo.getAttribute(driver, "xpath", "postalFirstExistingAcc","value", CURR_APP).trim();
		
		// fetch state name
		Select scon= new Select(driver.findElement(By.xpath("//select[@id='j_id0:accUpdateform:j_id28:j_id29:statecode' or @id='acc17state']")));
        WebElement Wb= scon.getFirstSelectedOption();
        stateExisting = Wb.getText();
		
		//click on account tab
		eo.clickElement(driver, "XPATH", "account_tab", CURR_APP);
		addComment("Successfully click on account tab");	
			
		//click on new tab
		eo.clickElement(driver, "XPATH", "account_new_btn", CURR_APP);
		addComment("Successfully click on new button ");
		
		// enter account name and country details
		eo.waitForWebElementVisible(driver, "XPATH", "accSearchPage", waiTime, CURR_APP);
		addComment("Successfully verified the <b> new account search page</b> is displayed");
			
			
			//Enter Account name
	       if(!(accountExistingName.isEmpty())) {
			eo.enterText(driver, "XPATH", "accNameField", accountExistingName, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("accNameField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered acc name");
	        }
	       
			//Select billing country
	        if(!(countryName.isEmpty())) {
			eo.selectComboBoxByVisibleText(driver, "xpath", "billingCountryField", countryName, CURR_APP);
	        }
	        
	        //enter billing street
	       if(streetValidation.equalsIgnoreCase("Y")) {
	        if(!(street.isEmpty())) {
			eo.enterText(driver, "XPATH", "billingStreetField",street , CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingStreetField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing street");
	        }
	       }
	        
	       if(cityValidation.equalsIgnoreCase("Y")) {
	       if(!(cityExisting.isEmpty())) {
	    	   System.out.println(cityExisting.isEmpty());
				eo.enterText(driver, "XPATH", "billingCityField",cityExisting , CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("billingCityField", CURR_APP))).sendKeys(Keys.TAB);
				addComment("Entered billing city");
		        }
	       }
	       
	       if(postalValidation.equalsIgnoreCase("Y")) {
		       if(!(postalExisting.isEmpty())) {
					eo.enterText(driver, "XPATH", "billingPostalCode",postalExisting , CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("billingPostalCode", CURR_APP))).sendKeys(Keys.TAB);
					addComment("Entered postal code ");
			        }
		       }
	       
	       if(stateValidation.equalsIgnoreCase("Y")) {
		       if(!(stateExisting.isEmpty())) {
					eo.enterText(driver, "XPATH", "billingStateField",stateExisting , CURR_APP);
					driver.findElement(By.xpath(gei.getProperty("billingStateField", CURR_APP))).sendKeys(Keys.TAB);
					addComment("Entered billing state");
			        }
		       }
	   
	        // click on search/create button 
	       eo.clickElement(driver, "xpath", "accSearchOrCreateBtn", CURR_APP);
	       eo.wait(5);
	       eo.waitForPageload(driver);
			
	
	}
}

public static class FetchDataSimilarAndCreateSection extends Keyword {

	
	private String countryName;
	private String street;
	private String streetValidation;
	private String cityName;
	private String postalCodeName;
	private String stateName;
	private String billingCountry;
	//private String federalAccFlag;
	
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	streetValidation = args.get("StreetValidation").trim();
	
	if(hasArgs("CityName")){
	cityName = args.get("CityName").trim();
	}
	if(hasArgs("PostalCodeName")){
	postalCodeName = args.get("PostalCodeName").trim();
	}
	if(hasArgs("StateName")){
	stateName = args.get("StateName").trim();
	}
	if(hasArgs("BillingCountry")){
	billingCountry = args.get("BillingCountry").trim();
	}
	federalAccFlag = args.get("FederalAccFlag").trim();
}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		//click on account tab
		eo.clickElement(driver, "XPATH", "account_tab", CURR_APP);
		addComment("Successfully click on account tab");
		eo.waitForWebElementVisible(driver, "XPATH", "account_new_btn", waiTime, CURR_APP);
		
		// pick up existing account name and country
		eo.waitForWebElementVisible(driver, "XPATH", "firstExistingAccount", waiTime, CURR_APP);
		eo.isDisplayed(driver, "xpath", "firstExistingAccount", CURR_APP);
		
		accountName = eo.getText(driver, "xpath", "firstExistingAccount", CURR_APP);
		countryName = eo.getText(driver, "xpath", "countryFirstExistingAcc", CURR_APP);
        
        // click on account existing
		eo.clickElement(driver, "XPATH", "firstExistingAccount", CURR_APP);
		eo.waitForPageload(driver);
		
		// click on update grain attribute
		eo.clickElement(driver, "XPATH", "updateGrainAttribute", CURR_APP);
		eo.waitForPageload(driver);
		
		if(federalAccFlag.equalsIgnoreCase("true"))
		{
			eo.verifyExactScreenText(driver, "xpath", "warningText", "Federal accounts can be updated directly", CURR_APP);
			addComment("Verified message:Federal accounts can be updated directly");
			eo.clickElement(driver, "xpath", "backBtn", CURR_APP);
			eo.wait(5);
			eo.verifyExactScreenText(driver, "xpath", "updatePageTitle", "Account Detail", CURR_APP);
			eo.clickElement(driver, "xpath", "editButton", CURR_APP);
			eo.wait(3);
		}else{
		// enter updated account details
		eo.waitForWebElementVisible(driver, "XPATH", "updateAccountTitle", waiTime, CURR_APP);
		addComment("Successfully verified the <b> update account page</b> is displayed");
		}
		eo.javaScriptScrollToViewElement(driver,
				driver.findElement(By.xpath(gei.getProperty("updateStreet", CURR_APP))));
		//fetch street data and postal code data
		street = eo.getText(driver, "xpath", "updateStreet", CURR_APP);

		
		//click on account tab
		eo.clickElement(driver, "XPATH", "account_tab", CURR_APP);
		addComment("Successfully click on account tab");	
			
		//click on new tab
		eo.clickElement(driver, "XPATH", "account_new_btn", CURR_APP);
		addComment("Successfully click on new button ");
		
		// enter account name and country details
		eo.waitForWebElementVisible(driver, "XPATH", "accSearchPage", waiTime, CURR_APP);
		addComment("Successfully verified the <b> new account search page</b> is displayed");
			
			
			//Enter Account name
	       if(!(accountName.isEmpty())) {
			eo.enterText(driver, "XPATH", "accNameField", accountName, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("accNameField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered acc name");
	        }
	       
			//Select billing country
	        if(!(countryName.isEmpty())) {
			eo.selectComboBoxByVisibleText(driver, "xpath", "billingCountryField", countryName, CURR_APP);
	        }
	        
	        //enter billing street
	       if(streetValidation.equalsIgnoreCase("Y")) {
	        if(!(street.isEmpty())) {
			eo.enterText(driver, "XPATH", "billingStreetField",street , CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingStreetField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing street");
	        }
	       }
	        
	     //select billing state
           if(!(stateName.isEmpty())) {
        	eo.selectComboBoxByVisibleText(driver, "xpath", "billingCountryField", billingCountry, CURR_APP);
			eo.selectComboBoxByVisibleText(driver, "xpath", "billingStateField", stateName, CURR_APP);
           }
			//enter billing city
           if(!(cityName.isEmpty())) {
			eo.enterText(driver, "XPATH", "billingCityField", cityName, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingCityField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing city");
           }
			
			//enter postal code
           if(!(postalCodeName.isEmpty())) {
			eo.enterText(driver, "XPATH", "billingPostalCode",postalCodeName , CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingPostalCode", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered postal code");
           }
	     
	       
	        // click on search/create button
	        eo.clickElement(driver, "xpath", "accSearchOrCreateBtn", CURR_APP);
			eo.wait(5);
			eo.waitForPageload(driver);
			
	
	}
}

public static class ValidateCMID extends Keyword {

	private String CMID;
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		//verify create button is displayed in result section
				CMID = eo.getText(driver, "xpath", "CMID_Field", CURR_APP).trim();
				System.out.print("federalAccFlag:"+federalAccFlag);
				switch(federalAccFlag.trim().toLowerCase())
				{
				case "true": if(CMID.isEmpty())
							{
									addComment("Successfully verified that CMID is not populated for federal account");
									break;
							}else
							{
								throw new KDTKeywordExecException("CMID is populated for federal account");
							}
				case "false" :if(CMID.isEmpty())
							{
								throw new KDTKeywordExecException("CMID is not populated for non federal account");
							}else{
								addComment("Successfully verified that CMID is  populated for non federal account:"+CMID);
								break;
							}
				default: addComment("default");
				}
			
	}
}

public static class ValidateAccAndCMID extends Keyword {

	private String updateAcc;
	private String updateCountry;
	private String updateStreet;
	private String updateState;
	private String updateCity;
	private String updatePostalCode;
	private String Updated_CMID;
	private String old_CMID;
	private String oldAccID;
	private String AccIDAfterUpdate;
	private String cancelValidation;
	
	

	
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	if(hasArgs("UpdateAcc")){
	updateAcc = args.get("UpdateAcc").trim() + eo.gnerateRandomNo(10);
	}
	if(hasArgs("UpdateCountry")){
	updateCountry = args.get("UpdateCountry").trim();
	}
	if(hasArgs("UpdateStreet")){
	updateStreet = args.get("UpdateStreet").trim();
	}
	if(hasArgs("UpdateState")){
	updateState = args.get("UpdateState").trim();
	}
	if(hasArgs("UpdateCity")){
	updateCity = args.get("UpdateCity").trim();
	}
	if(hasArgs("UpdatePostalCode")){
	updatePostalCode = args.get("UpdatePostalCode").trim();
	}
	if(hasArgs("CancelUpdate")){
		cancelValidation = args.get("CancelUpdate").trim();// + eo.gnerateRandomNo(10);
		}
	
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		// capture old CMID and Account ID
		old_CMID = eo.getText(driver, "xpath", "CMID_Field", CURR_APP);
		oldAccID = eo.getText(driver, "xpath", "accountID", CURR_APP);
		
		// click on update grain attribute
		eo.clickElement(driver, "XPATH", "updateGrainAttribute", CURR_APP);
		eo.waitForPageload(driver);
		
		if(federalAccFlag.equalsIgnoreCase("true"))
		{
			eo.verifyExactScreenText(driver, "xpath", "warningText", "Federal accounts can be updated directly", CURR_APP);
			addComment("Verified message:Federal accounts can be updated directly");
			eo.clickElement(driver, "xpath", "backBtn", CURR_APP);
			eo.wait(5);
			eo.verifyExactScreenText(driver, "xpath", "updatePageTitle", "Account Detail", CURR_APP);
			eo.clickElement(driver, "xpath", "editButton", CURR_APP);
			eo.wait(3);
		}
		else{
		// enter updated account details
		eo.waitForWebElementVisible(driver, "XPATH", "updateAccountTitle", waiTime, CURR_APP);
		addComment("Successfully verified the <b> update account page</b> is displayed");
		
	}
			
			//update Account name
	       if(!(updateAcc.isEmpty())) {
	    	eo.clearData(driver, "XPATH", "updateAccName", CURR_APP);
			eo.enterText(driver, "XPATH", "updateAccName", updateAcc, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("updateAccName", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered acc name");
	        }
	       eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("updateCountry", CURR_APP))));
	       
			//update billing country
	        if(!(updateCountry.isEmpty())) {
			eo.selectComboBoxByVisibleText(driver, "xpath", "updateCountry", updateCountry, CURR_APP);
			addComment("Updated country");
	        }
	        
	        //update billing street
	        if(!(updateStreet.isEmpty())) {
	        	  eo.clearData(driver, "XPATH", "updateStreet", CURR_APP);
			eo.enterText(driver, "XPATH", "updateStreet",updateStreet , CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("updateStreet", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing street");
	        }
	      
	     //update billing state
           if(!(updateState.isEmpty())) {
			eo.selectComboBoxByVisibleText(driver, "xpath", "updateState", updateState, CURR_APP);
			addComment("Updated state");
           }
			//update billing city
           if(!(updateCity.isEmpty())) {
        	eo.clearData(driver, "XPATH", "updateCity", CURR_APP);
			eo.enterText(driver, "XPATH", "updateCity", updateCity, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("updateCity", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing city");
           }
			
			//update postal code
           if(!(updatePostalCode.isEmpty())) {
        	eo.clearData(driver, "XPATH", "updatePostal", CURR_APP);
			eo.enterText(driver, "XPATH", "updatePostal",updatePostalCode , CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("updatePostal", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered postal code");
           }
	     
           // save and cancel button for update account page
           
           // click on cancel 
           if(cancelValidation.equalsIgnoreCase("Y"))   {
        	   eo.javaScriptScrollToViewElement(driver,
   					driver.findElement(By.xpath(gei.getProperty("updateCancel", CURR_APP))));
	        	eo.clickElement(driver, "xpath", "updateCancel", CURR_APP);
	        	eo.wait(5);
	   			eo.waitForPageload(driver);
	   			Updated_CMID = eo.getText(driver, "xpath", "CMID_Field", CURR_APP);
	   			
	   		//verify if CMID is not updated
	   			if(federalAccFlag.equalsIgnoreCase("false")){
				if(old_CMID.equalsIgnoreCase(Updated_CMID)) {
					addComment("Successfully verified that CMID is same for non federal account and cancel is successful and CMID after cancel is <b>" + Updated_CMID + "</b>");
					addComment("CMID before cancel is <b>" + old_CMID + "</b>");
				} else {
					addComment("Old CMID is " + old_CMID);
					addComment("CMID after cancel is " + Updated_CMID);
					throw new KDTKeywordExecException("CMID has changed after clicking on cancel button for non federal account");
				}
	   			}
	   			else {
	   				addComment("No CMID is generated for Federal account");
	   			}
	   		}
           else {
	        // click on save button
        	   eo.javaScriptScrollToViewElement(driver,
   					driver.findElement(By.xpath(gei.getProperty("updateSave", CURR_APP))));
	        eo.clickElement(driver, "xpath", "updateSave", CURR_APP);
			eo.wait(5);
			eo.waitForPageload(driver);
			
			Updated_CMID = eo.getText(driver, "xpath", "CMID_Field", CURR_APP);
			AccIDAfterUpdate = eo.getText(driver, "xpath", "accountID", CURR_APP);
			
			if(federalAccFlag.equalsIgnoreCase("false")) {
					//verify if CMID is updated
					if(!(old_CMID.equalsIgnoreCase(Updated_CMID))) {
							addComment("Successfully verified that CMID is updated and new updated CMID generated is <b>" + Updated_CMID + "</b>");
							addComment("Old CMID is <b>" + old_CMID + "</b>");
						} else {
							addComment("Old CMID for non federal acc is " + old_CMID);
							addComment("Updated CMID for non federal acc is " + Updated_CMID);
							throw new KDTKeywordExecException("CMID IS NOT UPDATED and Account UPDATE process is not completed for non federal acc");
						}
			}else
				{
					addComment("CMID will not be generated for federal acc");
				}
				
	
			//verify if Account ID is same
			if(oldAccID.equalsIgnoreCase(AccIDAfterUpdate)) {
					addComment("Successfully verified that Account ID is same after update and is <b>" + AccIDAfterUpdate + "</b>");
					addComment("Old Account ID is <b>" + oldAccID + "</b>");
			} else {
					addComment("Old Account ID is " + oldAccID);
					addComment("New Account ID is " + AccIDAfterUpdate);
					throw new KDTKeywordExecException("Account ID has changed and Account UPDATE process is not completed");
	}
	}
	}
	
}

public static class ValidateErrOnFieldClearInUpdateGrainAtt extends Keyword {

	private String expectedWarningMessage = ""; 
	private String fieldTobeCleared="";
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
			expectedWarningMessage = args.get("ExpectedWarningMessage").trim();
			fieldTobeCleared = args.get("FieldTobeCleared").trim().toLowerCase();
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		// click on update grain attribute
		eo.clickElement(driver, "XPATH", "updateGrainAttribute", CURR_APP);
		eo.waitForPageload(driver);
		if(federalAccFlag.equalsIgnoreCase("true"))
		{
			eo.verifyExactScreenText(driver, "xpath", "warningText", "Federal accounts can be updated directly", CURR_APP);
			addComment("Verified message:Federal accounts can be updated directly");
			eo.clickElement(driver, "xpath", "backBtn", CURR_APP);
			eo.wait(5);
			eo.verifyExactScreenText(driver, "xpath", "updatePageTitle", "Account Detail", CURR_APP);
			eo.clickElement(driver, "xpath", "editButton", CURR_APP);
			eo.wait(3);
		}else{
		// enter updated account details
		eo.waitForWebElementVisible(driver, "XPATH", "updateAccountTitle", waiTime, CURR_APP);
		addComment("Successfully verified the <b> update account page</b> is displayed");
		}
		
		if(fieldTobeCleared.equalsIgnoreCase("account"))
		{
		//clear account name
		eo.clearData(driver, "XPATH", "updateAccName", CURR_APP);
		}else if(fieldTobeCleared.equalsIgnoreCase("street"))
			{
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("updateStreet", CURR_APP))));
				eo.clearData(driver, "XPATH", "updateStreet", CURR_APP);
			}else{
				throw new KDTKeywordExecException("No input given to clear field");
			}
	
		// click on save
		eo.clickElement(driver, "xpath", "updateSave", CURR_APP);
		eo.wait(2);
		 String warningMessage = eo.getText(driver, "xpath", "errorMessageText", CURR_APP);
		if(warningMessage.trim().equals(expectedWarningMessage)) {
			addComment("Successfully verified warning for not entering "+fieldTobeCleared+" name is <b>" + expectedWarningMessage + "</b>");
		} else {
				addComment("Expected warning message is " + expectedWarningMessage);
				addComment("Actual warning message is " + warningMessage);
				throw new KDTKeywordExecException("Actual and Expected warning message are not same");
				}
}
}


public static class DetailsForExistingResultSection extends Keyword {

	private String billingCountry;
	private String billingStreet;
	private String billingState;
	private String billingCity;
	private String billingPostalCode;
	private String streetValidation;
	private String postalValidation;
	private String cityValidation;
	private String stateValidation;
	
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	streetValidation = args.get("StreetValidation");
	cityValidation = args.get("CityValidation");
	postalValidation = args.get("PostalValidation");
	stateValidation = args.get("StateValidation");
	if(hasArgs("BillingCountry")){
		billingCountry = args.get("BillingCountry").trim();
		}
		if(hasArgs("BillingState")){
		billingState = args.get("BillingState").trim();
		}
		if(hasArgs("BillingCity")){
		billingCity = args.get("BillingCity").trim();
		}
		if(hasArgs("BillingStreet")){
		billingStreet = args.get("BillingStreet").trim();
		}
		if(hasArgs("BillingPostalCode")){
		billingPostalCode = args.get("BillingPostalCode").trim();
		}
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		//click on account tab
		eo.clickElement(driver, "XPATH", "account_tab", CURR_APP);
		addComment("Successfully click on account tab");	
			
		//click on new tab
		eo.clickElement(driver, "XPATH", "account_new_btn", CURR_APP);
		addComment("Successfully click on new button ");
		
		// enter account name and country details
		eo.waitForWebElementVisible(driver, "XPATH", "accSearchPage", waiTime, CURR_APP);
		addComment("Successfully verified the <b> new account search page</b> is displayed");
			
			
			//Enter Account name
		  if(!(accountName.isEmpty())) {
				eo.enterText(driver, "XPATH", "accNameField", accountName, CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("accNameField", CURR_APP))).sendKeys(Keys.TAB);
				addComment("Entered acc name");
              }
	       
			//Select billing country
	        if(!(billingCountry.isEmpty())) {
			eo.selectComboBoxByVisibleText(driver, "xpath", "billingCountryField", billingCountry, CURR_APP);
	        }
	        
	        //enter billing street
	       if(streetValidation.equalsIgnoreCase("Y")) {
	        if(!(billingStreet.isEmpty())) {
			eo.enterText(driver, "XPATH", "billingStreetField",billingStreet , CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingStreetField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing street");
	        }
	       }
	        
	     //select billing state
	       if(stateValidation.equalsIgnoreCase("Y")) {
           if(!(billingState.isEmpty())) {
			eo.selectComboBoxByVisibleText(driver, "xpath", "billingStateField", billingState, CURR_APP);
           }
	       }
	       
			//enter billing city
	       if(cityValidation.equalsIgnoreCase("Y")) {
           if(!(billingCity.isEmpty())) {
			eo.enterText(driver, "XPATH", "billingCityField", billingCity, CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingCityField", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered billing city");
           }
	       }
			
			//enter postal code
	       if(postalValidation.equalsIgnoreCase("Y")) {
           if(!(billingPostalCode.isEmpty())) {
			eo.enterText(driver, "XPATH", "billingPostalCode",billingPostalCode , CURR_APP);
			driver.findElement(By.xpath(gei.getProperty("billingPostalCode", CURR_APP))).sendKeys(Keys.TAB);
			addComment("Entered postal code");
           }
	       }
	     
			
	    	   if(federalAccFlag.equalsIgnoreCase("true"))
               {
               	eo.clickElement(driver, "xpath", "federalIntelCheckbox", CURR_APP);
               	addComment("Federal account");
               }
               else  if(federalAccFlag.equalsIgnoreCase("false"))
               {
               	addComment("Non-Federal account");
               }
			
	       
	        // click on search/create button
	        eo.clickElement(driver, "xpath", "accSearchOrCreateBtn", CURR_APP);
			eo.wait(5);
			eo.waitForPageload(driver);		
		
	}
	

}

public static class ValidateErrorInUpdateForCompletenessCriteria extends Keyword {

	private String warningMessage;
	private String expectedWarningMessage = "Please enter Billing City or Billing State or Billing Postal Code to complete update process";
	private String updateCountry;
	
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	updateCountry = args.get("UpdateCountry").trim();
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		// click on update grain attribute
		eo.clickElement(driver, "XPATH", "updateGrainAttribute", CURR_APP);
		eo.waitForPageload(driver);
		if(federalAccFlag.equalsIgnoreCase("true"))
		{
			eo.verifyExactScreenText(driver, "xpath", "warningText", "Federal accounts can be updated directly", CURR_APP);
			addComment("Verified message:Federal accounts can be updated directly");
			eo.clickElement(driver, "xpath", "backBtn", CURR_APP);
			eo.wait(5);
			eo.verifyExactScreenText(driver, "xpath", "updatePageTitle", "Account Detail", CURR_APP);
			eo.clickElement(driver, "xpath", "editButton", CURR_APP);
			eo.wait(3);
		}else{
		// enter updated account details
		eo.waitForWebElementVisible(driver, "XPATH", "updateAccountTitle", waiTime, CURR_APP);
		addComment("Successfully verified the <b> update account page</b> is displayed");
		}
		eo.javaScriptScrollToViewElement(driver,
				driver.findElement(By.xpath(gei.getProperty("updateCountry", CURR_APP))));
		//update billing country
        if(!(updateCountry.isEmpty())) {
		eo.selectComboBoxByVisibleText(driver, "xpath", "updateCountry", updateCountry, CURR_APP);
        }
		// click on save
		eo.clickElement(driver, "xpath", "updateSave", CURR_APP);
		eo.wait(2);
		warningMessage = eo.getText(driver, "xpath", "errorMessageText", CURR_APP);
		if(warningMessage.contains(expectedWarningMessage)) {
			addComment("Successfully verified warning for not fulfilling completeness criteria is <b>" + expectedWarningMessage + "</b>");
		} else {
				addComment("Expected warning message is " + expectedWarningMessage);
				addComment("Actual warning message is " + warningMessage);
				throw new KDTKeywordExecException("Actual and Expected warning message are not same");
				}
}
}

public static class ValidateUpdateDirectlyError extends Keyword {

	private String warningMessage;
	private String expectedWarningMessage = "use 'Update Gran Attributes' button on the account page to update account name and billing address";
	private String updateCity;
	
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	updateCity = args.get("UpdateCity").trim();
	}
	
	@Override
	public void exec() throws KDTKeywordExecException {
		// TODO Auto-generated method stub
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		
		// double click on billing address block
		eo.actionDoubleClick(driver, "XPATH", "DirectUpdateAddress", CURR_APP);
		eo.wait(2);
		
		// enter billing city
		eo.clearData(driver, "XPATH", "DirectUpdateCity", CURR_APP);
		eo.enterText(driver, "XPATH", "DirectUpdateCity", updateCity, CURR_APP);
		driver.findElement(By.xpath(gei.getProperty("DirectUpdateCity", CURR_APP))).sendKeys(Keys.TAB);
		addComment("Entered billing city");
			
		// click on OK
		eo.clickElement(driver, "xpath", "DirectUpdateOK", CURR_APP);
		eo.wait(2);
		//click on save 
		eo.clickElement(driver, "xpath", "DirectUpdateSave", CURR_APP);
		eo.waitForPageload(driver);
		if(federalAccFlag.equalsIgnoreCase("false")){
		warningMessage = eo.getText(driver, "xpath", "DirectUpdateError", CURR_APP);
		System.out.println(warningMessage);
		if(warningMessage.contains(expectedWarningMessage)) {
			addComment("Successfully verified warning for directly updating non federal account is <b>" + expectedWarningMessage + "</b>");
		} else {
				addComment("Expected warning message for direct update of non federal account is " + expectedWarningMessage);
				addComment("Actual warning message for direct update of non federal account is " + warningMessage);
				throw new KDTKeywordExecException("Actual and Expected warning message for direct update of non federal account are not same");
				}	
		}else if(federalAccFlag.equalsIgnoreCase("true"))
		{   
			if(!eo.verifyElementIsPresent(driver, "xpath", "editButton", CURR_APP))
			{
				throw new KDTKeywordExecException("Direct update is unsuccessful for federal account");
			}else
			addComment("Direct update is successful for federal account");
		}
	}
  }
public static class globalSearchAndVerify extends Keyword {

	private String searchParameter  = "";
	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	searchParameter =args.get("SearchchParameter");
	}

	@Override
	public void exec() throws KDTKeywordExecException {
				int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
				WebDriver driver = context.getWebDriver();
				
		//Click on the global search bar
				eo.clickElement(driver, "xpath", "search_input_tab", CURR_APP);
				addComment("Successfully click on search input tab");
		//enter search text in search bar
				eo.enterText(driver, "XPATH", "search_input_tab", accountName , CURR_APP);
				driver.findElement(By.xpath(gei.getProperty("search_input_tab", CURR_APP))).sendKeys(Keys.TAB);
				addComment("Entered "+accountName+" in search Field");
		//click on search button
				eo.clickElement(driver, "ID", "searchButton", CURR_APP);
				eo.waitForPageload(driver);
				addComment("Successfully click on search button");
		//Verify element is displayed
				eo.isDisplayedAfterReplace(driver, "xpath", "ClickOnUserName", "{searchLoginUserName}", accountName, CURR_APP);
				addComment("Searched element is present::"+accountName);
		//Verify the number of results present
				int count = eo.getSize(driver, "xpath", "noOfSearchResults", CURR_APP);
				if(count==1)
				{
					addComment("Number of search result is only one:"+count);
				}else{
				addComment("Number of search result is more than one:"+count);
				throw new  KDTKeywordExecException("More than one search results are present");
				}
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "ClickOnSearchedElement", "{searchLoginUserName}", accountName, CURR_APP);	
			addComment("Clicked on "+accountName+ "link");	
			eo.verifyExactScreenText(driver, "xpath", "accountPageHeader", "Account Detail", CURR_APP);
			eo.javaScriptScrollToViewElement(driver,
					driver.findElement(By.xpath(gei.getProperty("federalCheckBox", CURR_APP))));
			String checkboxTitle=eo.getAttribute(driver, "xpath", "federalCheckBox", "title", CURR_APP);
			if(federalAccFlag.equalsIgnoreCase("true") && checkboxTitle.equals("Checked"))
			{
				addComment("Federal account is created and the check box is checked::"+checkboxTitle);
			}else if(federalAccFlag.equalsIgnoreCase("false") && checkboxTitle.equals("Not Checked"))
			{
				addComment("Non Federal account is created and the check box is not checked::"+checkboxTitle);
			}
			else{
				addComment("Account checkbox title is ::"+checkboxTitle);	
				throw new KDTKeywordExecException("flag:"+federalAccFlag+"Account checkbox title is ::"+checkboxTitle+"And result is not as expected");
			}
				
	}
}
public static class fetchAccountFieldValues extends Keyword {

	@Override
	public void init() throws KDTKeywordInitException {
	super.init();
	}

	@Override
	public void exec() throws KDTKeywordExecException {
		int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
		WebDriver driver = context.getWebDriver();
		//public static ArrayList<String> accCreateFieldVal = new ArrayList<String>();
		ArrayList<String> fetchedAccountDetails = new ArrayList<String>();
		eo.clickElement(driver, "xpath", "editButton", CURR_APP);
		eo.wait(3);
		
		String fetchedAccName = eo.getAttribute(driver, "xpath",  "updateAccName", "value", CURR_APP).trim();
		if(fetchedAccName.equalsIgnoreCase(accountName))
		{
			addComment("Actual account name:"+fetchedAccName+" is equal to expected account name:"+accountName);
		}else{
			addComment("Actual account name:"+fetchedAccName+" is not equal to expected account name:"+accountName);
			throw new KDTKeywordExecException("Actual account name:"+fetchedAccName+" is equal to expected account name:"+accountName);
		}
		
		eo.javaScriptScrollToViewElement(driver, driver.findElement(By.xpath(gei.getProperty("updateCountry", CURR_APP))));
		
//		fetchedAccountDetails.add("billingCountry:"+eo.getText(driver, "xpath",  "updateCountry", CURR_APP).trim());
//		addComment("fetched billingCountry value");
//		fetchedAccountDetails.add("billingState:" + eo.getText(driver, "xpath",  "updateState", CURR_APP).trim());
//		addComment("fetched billingState value");
		fetchedAccountDetails.add("billingCity:" + eo.getText(driver, "xpath",  "updateCity",  CURR_APP).trim());
		addComment("fetched billingCity value");
		fetchedAccountDetails.add("billingStreet:" + eo.getText(driver, "xpath",  "updateStreet",  CURR_APP).trim());
		addComment("fetched billingStreet value");
		fetchedAccountDetails.add("BillingPostalCode:" + eo.getText(driver, "xpath",  "updatePostal",  CURR_APP).trim());
		addComment("fetched BillingPostalCode value");
		for(int i=0;i<fetchedAccountDetails.size()-1;i++){
			if(accCreateFieldVal.contains(fetchedAccountDetails.get(i))){
				addComment("expected data is present:"+fetchedAccountDetails.get(i));
			}else{
				addComment("expected data is not present:"+fetchedAccountDetails.get(i));
				throw new KDTKeywordExecException("expected data is not present:"+fetchedAccountDetails.get(i));
			}
		}
	}
}

}//last line



