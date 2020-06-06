package com.qualitestgroup.keywords.customersupport;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qualitestgroup.getproperty.GetElementIdentifier;
import com.qualitestgroup.getproperty.GetProperty;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.keywords.common.ElementOperation;

public class CustomerSupport extends KeywordGroup {
	
	
	private static final String CURR_APP = "customersupport";
	public static GetElementIdentifier gei = new GetElementIdentifier();
	static GetProperty getProps = new GetProperty();
	public static ElementOperation eo = new ElementOperation();
	public static String casenumbertext;
	public static String prioritytext;
	public static String statustext;
	public static String casenumbertext2;
	public static String accountname;
	
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>LoginSupportPortal</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to login into the SupportPortal
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
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	


	public static class LoginSupportPortal extends Keyword {
		private String urlcs;
		private String usernamecs;
		private String passwordcs;
		private String browser = "";
		

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			urlcs = args.get("UrlCS");
			usernamecs = args.get("UsernameCS");
			passwordcs= args.get("PasswordCS");
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
			driver.get(urlcs);
			            		
			eo.waitForWebElementVisible(driver, "xpath", "login_text", waiTime, CURR_APP);
			
            /////////////validate wrong credentials////////////
			
			eo.enterText(driver, "xpath", "username1", usernamecs, CURR_APP);
			addComment("Successfully  enter the username");

			eo.enterText(driver, "xpath", "password1", passwordcs, CURR_APP);
			addComment("Successfully enter the password");
			eo.waitForWebElementVisible(driver, "XPATH", "login_button", waiTime, CURR_APP);

			eo.clickElement(driver, "xpath", "login_button", CURR_APP);
			addComment("Successfully click on login button");

			eo.waitForWebElementVisible(driver, "XPATH", "home_page", waiTime, CURR_APP);
			addComment("Successfully verified the home tab is dispalyed");
			
		}
	}
	
	//////////////////////////////////////////// VerifyRegisterForLogin//////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyRegisterForLogin</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to VerifyRegisterForLogin
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>fieldnames(Mandatory):fieldnames</li>
	 * <li>fieldvalues(Mandatory):fieldvalues</li>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyRegisterForLogin extends Keyword {
		private String urlcs;
		private String browser = "";
		private String fieldnames[];
		private String wrongfieldvalues[];
		private String fieldvalues[];
		private String wrongfields[];
		private String registerconferm;

		@Override
		public void init() throws KDTKeywordInitException {

			// TODO Auto-generated method stub

			super.init();

			// verifyArgs("ContactLastName", "ContactLeadSource", "ContactEmailId");
			urlcs = args.get("UrlCS");
			fieldnames = args.get("FieldNames").split(",");
			wrongfieldvalues = args.get("WrongFieldValues").split(",");
			wrongfields = args.get("WrongFields").split(",");
			fieldvalues = args.get("FieldValues").split(",");
			registerconferm = args.get("RegisterConferm");
			

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

			try {
				WebDriver driver = context.getWebDriver();
				driver.get(urlcs);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "xpath", "registerforlogin_link", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "registerforlogin_link", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "registerpage_landing", waiTime, CURR_APP);
				eo.javaScriptScrollToViewElement(driver, "xpath", "register_button", CURR_APP);
				eo.clickElement(driver, "xpath", "register_button", CURR_APP);
				this.addComment("Clicked on Register button for all blank entries");
				eo.waitForPageload(driver);
				
				List<WebElement> textboxcount = driver
						.findElements(By.xpath(gei.getProperty("textfield_labelnamesList", CURR_APP)));

				for (int i = 0; i < textboxcount.size(); i++) {
					String errormessage = eo.getTextAfterReplacingKeyValue(driver, "xpath", "Inline_errormessage",
							"{input}", fieldnames[i], CURR_APP);
					if (errormessage.equalsIgnoreCase("This Field Is Required")) {
						this.addComment("Inline error message displayed for <b>" + fieldnames[i] + "is:" + "</b>"
								+ " <b> This Field Is Required</b>");
					}
				}

				eo.javaScriptScrollToViewElement(driver, "xpath", "registerpage_landing", CURR_APP);

				for (int i = 0; i < wrongfieldvalues.length; i++) {
					eo.enterTextAfterReplacingKeyValue(driver, "xpath", "registerpage_textfield", "{input}",
							wrongfields[i], wrongfieldvalues[i], CURR_APP);
					String errormessage = eo.getTextAfterReplacingKeyValue(driver, "xpath", "Inline_errormessage",
							"{input}", wrongfields[i], CURR_APP);
					if (errormessage.equalsIgnoreCase("Please Enter Correct Email")) {
						this.addComment("Email field is verified ");
					} else if (errormessage.equalsIgnoreCase("Please Enter Correct Phone Number")) {
						this.addComment("Phone Number field is verified ");
					} else {
						this.addComment("Email and Phone Numbet field is not verified for wrong inputs");
					}
				}

				for (int i = 0; i < textboxcount.size(); i++) {
					if (fieldvalues[i].equals("deepak123@rubrik.com")) {
						String no = eo.gnerateRandomNo(5);
						fieldvalues[i] = "deepak1" + no + "@rubrik.com";
					}
					eo.clearDataAfterReplacingKeyValue(driver, "xpath", "registerpage_textfield", "{input}",
							fieldnames[i], CURR_APP);
					eo.enterTextAfterReplacingKeyValue(driver, "xpath", "registerpage_textfield", "{input}",
							fieldnames[i], fieldvalues[i], CURR_APP);
					this.addComment("Entered values for <b>" + fieldnames[i] + "</b>");
				}

				eo.clickElement(driver, "xpath", "register_button", CURR_APP);
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "register_button", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "xpath", "closebutton_confermationmessage", 40, CURR_APP);
				this.addComment("Clicked on Register button for all Correct entries");
				
				this.addComment("Confermation Popup Displayed");

				String confermationmessage = eo.getText(driver, "xpath", "confermationmessage", CURR_APP);

				if (confermationmessage.equalsIgnoreCase(registerconferm)) {
					this.addComment("User Successfully Registered");
				} else {
					this.addComment("Error in User Registration");
				}
				
				eo.clickElement(driver, "xpath", "closebutton_confermationmessage", CURR_APP);
				this.addComment("Closed confermation message");
				eo.clickElement(driver, "xpath", "returntologin_link", CURR_APP);
				this.addComment("Clicked on Return to Login Link");
				
				
			}

			catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("Failed to Register User in Support Portal");
				throw new KDTKeywordExecException("Failed to Register User in Support Portal", e);
			}
		}
	}
	
	

	//////////////////////////////////////////// VerifyForgetPassword//////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyForgetPassword</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to VerifyForgetPassword
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class VerifyForgetPassword extends Keyword {
		
		//private String browser = "";
		private String wrongfieldvalues[];
	
	

		@Override
		public void init() throws KDTKeywordInitException {

			// TODO Auto-generated method stub

			super.init();

			// verifyArgs("ContactLastName", "ContactLeadSource", "ContactEmailId");
			
			wrongfieldvalues = args.get("WrongFieldValues").split(",");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			
			

			try {
				WebDriver driver = context.getWebDriver();
				
				eo.waitForWebElementVisible(driver, "xpath", "forgotpassword_link", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "forgotpassword_link", CURR_APP);
				eo.waitForWebElementVisible(driver, "xpath", "forgotpassword_text", waiTime, CURR_APP);
				for (int i=0 ; i<wrongfieldvalues.length;i++) {
					
				eo.clickElement(driver, "xpath", "emailaddresstextfield_forgotpassword", CURR_APP);
				eo.enterText(driver, "xpath", "emailaddresstextfield_forgotpassword", wrongfieldvalues[i], CURR_APP);
				eo.clickElement(driver, "xpath", "resetbutton_forgotpasswd", CURR_APP);
				eo.waitForPageload(driver);
				
				String confermationmessage = eo.getText(driver, "xpath", "confermationmessage", CURR_APP);

				if (confermationmessage.equalsIgnoreCase("Please enter a valid Email.")) {
					this.addComment("Email entered:  <b>" + wrongfieldvalues[i] +"</b> is wrong");
				} else {
					this.addComment("Error message for wrong email is not verified");
				}
				
				eo.clickElement(driver, "xpath", "closebutton_confermationmessage", CURR_APP);
				this.addComment("Closed confermation message");
				}
				
				//String no = eo.gnerateRandomNo(5);
				String correctemail = "deepak.kumar@rubrik.com";
				eo.clearData(driver, "xpath", "emailaddresstextfield_forgotpassword", CURR_APP);
				eo.enterText(driver, "xpath", "emailaddresstextfield_forgotpassword", correctemail, CURR_APP);
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "resetbutton_forgotpasswd", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "xpath", "closebutton_confermationmessage", waiTime, CURR_APP);
				String confermationmessage = eo.getText(driver, "xpath", "confermationmessage", CURR_APP);

				if (confermationmessage.equalsIgnoreCase("Your password has been reset. Please check your email from Rubrik Support with the password reset link. If you have any issues, please contact support@rubrik.com.")) {
					this.addComment("Password Reset Done for <b> " + correctemail + "</b>");
				} else {
					this.addComment("Email entered is wrong");
				}
				
				eo.clickElement(driver, "xpath", "closebutton_confermationmessage", CURR_APP);
				eo.clickElement(driver, "xpath", "returntologin_link", CURR_APP);
				
			}

			catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("Failed to Register User in Support Portal");
				throw new KDTKeywordExecException("Failed to Register User in Support Portal", e);
			}
		}
	}
	
	
	////////////////////////////////ValidateLogin Page //////////////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateLogin</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateLogin
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	public static class ValidateLogin extends Keyword {
			

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			
			WebDriver driver = context.getWebDriver();
					            		
			eo.waitForWebElementVisible(driver, "xpath", "login_text", waiTime, CURR_APP);
			
            /////////////validate With no  credentials////////////
			eo.clickElement(driver, "xpath", "login_button", CURR_APP);
			eo.waitForPageload(driver);
			String confermationmessage = eo.getText(driver, "xpath", "confermationmessage", CURR_APP);

			if (confermationmessage.equalsIgnoreCase("Enter a value in the User Name field.")) {
				this.addComment("Error message for blank username and password verified");
			} else {
				this.addComment("No error message");
			}
			
			eo.clickElement(driver, "xpath", "closebutton_confermationmessage", CURR_APP);
			this.addComment("Closed confermation message");
			eo.waitForPageload(driver);
			///////////////////Verify for no password///////////.
			eo.enterText(driver, "xpath", "username1", "deepak@rubrik.com", CURR_APP);
			addComment("Successfully  enter the username");
			eo.clickElement(driver, "xpath", "login_button", CURR_APP);
			eo.waitForPageload(driver);
			String confermationmessage1 = eo.getText(driver, "xpath", "confermationmessage", CURR_APP);

			if (confermationmessage1.equalsIgnoreCase("Enter a value in the Password field.")) {
				this.addComment("Error message for blank  password verified");
			} else {
				this.addComment("No error message");
			}
			
			eo.clickElement(driver, "xpath", "closebutton_confermationmessage", CURR_APP);
			this.addComment("Closed confermation message");

			eo.waitForPageload(driver);
			/// Verify for wrong username and password////////////////
			eo.clearData(driver, "xpath", "username1", CURR_APP);
			eo.enterText(driver, "xpath", "username1", "deepak@rubrik.com", CURR_APP);
			addComment("Successfully  enter the username");
			eo.enterText(driver, "xpath", "password1", "1234", CURR_APP);
			addComment("Successfully enter the password");

			eo.clickElement(driver, "xpath", "login_button", CURR_APP);
			addComment("Successfully click on login button");
			eo.waitForPageload(driver);
			String confermationmessage2 = eo.getText(driver, "xpath", "confermationmessage", CURR_APP);

			if (confermationmessage2.equalsIgnoreCase("Your login attempt has failed. Make sure the username and password are correct.")) {
				this.addComment("Error message for wrong user credentials  verified");
			} else {
				this.addComment("No error message");
			}
			
			eo.clickElement(driver, "xpath", "closebutton_confermationmessage", CURR_APP);
			this.addComment("Closed confermation message");

			
			
		}
	}

	//////////////////////////////// ValidateHeaderLinks//////////////////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateHeaderLinks</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateHeaderLinks
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash KP</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateHeaderLinks extends Keyword {

		// private String urlcs;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			// urlcs = args.get("UrlCS");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
				eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage", waiTime, CURR_APP);

				
				 //Verify Announcements header
				 
				  eo.clickElement(driver, "xpath", "announcements_header", CURR_APP);
				  //eo.waitForWebElementVisible(driver, "XPATH", "announcements_header_verify", waiTime, CURR_APP);
				  eo.waitForPageload(driver);
				 // String announce =	 eo.getAttribute(driver, "xpath", "accountname_On_Oppertunity", "value", CURR_APP);
				//  eo.getText(driver, "xpath", "announcements_header_verify", CURR_APP); System.out.println("Announcement text" + announce );
				  if
				  (eo.getText(driver, "xpath", "announcements_header_verify", CURR_APP) .equalsIgnoreCase("Announcements"))
				  {
				  this.addComment("User is at " +"<b> Announcements </b>" + " header and verified Announcements header link successfully"); 
				  } else
				  {
				  this.addComment("User is not at Announcements header"); 
				  throw new KDTKeywordExecException("User is not at Announcements header");
				  }
				 

				// Verify forums header
                 eo.wait(5);
				eo.clickElement(driver, "xpath", "forums_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "forums_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "forums_header_verify", CURR_APP)
						.equalsIgnoreCase("Have a question about Rubrik CDM? Choose a product area below!")) {
					this.addComment(
							"User is at" + "<b> Forums </b>" + "header and verified Forums header link successfully");
				} else {
					this.addComment("User is not at forums header");
					throw new KDTKeywordExecException("User is not at forums header");
				}

				// Verify Training header

				eo.clickElement(driver, "xpath", "training_header", CURR_APP);
				this.addComment("Clicked on " + "<b> Training </b>"
						+ " link on the header and verified Training header link successfully");

				// Verify Knowledge Base header

				eo.clickElement(driver, "xpath", "knowledgebase_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "knowledgebase_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "knowledgebase_header_verify", CURR_APP)
						.equalsIgnoreCase("Knowledge Base")) {
					this.addComment("User is at " + "<b> Knowledge Base </b>"
							+ " header and verified Knowledge Base header link successfully");
				} else {
					this.addComment("User is not at Knowledge Base header");
					throw new KDTKeywordExecException("User is not at Knowledge Base header");
				}

				// Verify My cases header

				eo.clickElement(driver, "xpath", "mycases_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "mycases_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "mycases_header_verify", CURR_APP).equalsIgnoreCase("My Cases")) {
					this.addComment("User is at " + "<b> My Cases </b>"
							+ " header and verified My Cases header link successfully");
				} else {
					this.addComment("User is not at My Cases header");
					throw new KDTKeywordExecException("User is not at My Cases header");
				}

				// Verify ideas header
				eo.wait(5);
				eo.clickElement(driver, "xpath", "ideas_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "ideas_header_verify", waiTime, CURR_APP);
				eo.waitForPopUp(driver);

				if (eo.getText(driver, "xpath", "ideas_header_verify", CURR_APP).equalsIgnoreCase("Ideas")) {
					this.addComment(
							"User is at " + "<b> Ideas </b>" + " header and verified Ideas header link successfully");
				} else {
					this.addComment("User is not at Ideas header");
					throw new KDTKeywordExecException("User is not at Ideas header");
				}

				// Verify My products header
				eo.wait(5);
				eo.clickElement(driver, "xpath", "myproducts_header", CURR_APP);
				eo.waitForPopUp(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "myproducts_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "myproducts_header_verify", CURR_APP).equalsIgnoreCase("My Products")) {
					this.addComment("User is at " + "<b> My Products </b>"
							+ " header and verified My Products header link successfully");
				} else {
					this.addComment("User is not at My Products header");
					throw new KDTKeywordExecException("User is not at My Products header");
				}

				// Verify videos header
				eo.wait(5);
				eo.clickElement(driver, "xpath", "videos_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "videos_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "videos_header_verify", CURR_APP).equalsIgnoreCase("Videos Articles")) {
					this.addComment("User is at " + "<b> Videos </b>"
							+ " header and verified Videos header link successfully ");
				} else {
					this.addComment("User is not at videos header");
					throw new KDTKeywordExecException("User is not at videos header");
				}

				// Verify Doc&Downloads header
				eo.wait(5);
				eo.clickElement(driver, "xpath", "docdownload_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "docdownload_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "docdownload_header_verify", CURR_APP)
						.equalsIgnoreCase("Documentation and Downloads")) {
					this.addComment("User is at " + "<b> Documentation & Downloads </b>"
							+ " header and verified Documentation & Downloads header link successfully");
				} else {
					this.addComment("User is not at Documentation and Downloads header");
					throw new KDTKeywordExecException("User is not at Documentation and Downloads header");
				}

				// Verify About header
				eo.wait(5);
				eo.clickElement(driver, "xpath", "about_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "about_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "about_header_verify", CURR_APP)
						.equalsIgnoreCase("Our Customer Advocates Ensure Your Success")) {
					this.addComment(
							"User is at " + "<b> About </b>" + " header and verified About header link successfully");
				} else {
					this.addComment("User is not at About header");
					throw new KDTKeywordExecException("User is not at About header");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("User is Unable to verify the header links");
				throw new KDTKeywordExecException("User is Unable to verify the header links ", e);

			}

		}
	}

	///////////////////////////////// ValidateFooterLinks////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateFooterLinks</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateFooterLinks
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash KP</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateFooterLinks extends Keyword {

		// private String urlcs;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			// urlcs = args.get("UrlCS");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
				// eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage",
				// waiTime, CURR_APP);

				// Verify create case link in the Footer
				
				eo.javaScriptScrollToViewElement(driver, "xpath", "rubriklandingpage_scrolldown", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "createcase_footer", CURR_APP);
				eo.waitForPopUp(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "createcase_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "createcase_verify", CURR_APP).equalsIgnoreCase("New Case Details")) {
					this.addComment("User is at " + "<b> New Case Details </b>"
							+ " page and and verified New cases footer link successfully");
				} else {
					this.addComment("User is not at New Case Details page");
					throw new KDTKeywordExecException("User is not at New Case Details page");
				}

				// Verify view cases link in the Footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "viewcase_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "viewcase_footer", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "viewcase_footer_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "viewcase_footer_verify", CURR_APP).equalsIgnoreCase("My Cases")) {
					this.addComment("User is at " + "<b> My Cases </b>"
							+ " page and and verified view cases footer link successfully");
				} else {
					this.addComment("User is not at my cases page");
					throw new KDTKeywordExecException("User is not at my cases page");
				}

				// Verify View knowledge base link in the Footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "viewcase_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "viewknowledgebase_footer", CURR_APP);
				eo.waitForPopUp(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "knowledgebase_footer_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "knowledgebase_footer_verify", CURR_APP)
						.equalsIgnoreCase("All Articles")) {
					this.addComment("User is at " + "<b> All Articles </b>"
							+ " page and verified Knowledge Base footer link successfully");
				} else {
					this.addComment("User is not at All Articles page");
					throw new KDTKeywordExecException("User is not at All Articles page ");
				}

				// Verify Submit idea link in the Footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "viewcase_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "submitidea_footer", CURR_APP);
				eo.waitForPopUp(driver);
				eo.clickElement(driver, "xpath", "closebutton_newidea", CURR_APP);
				eo.waitForPopUp(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "idea_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "idea_verify", CURR_APP).equalsIgnoreCase("Ideas")) {
					this.addComment("User is at " + "<b> Ideas </b>"
							+ " page and verified Submit idea footer link successfully");
				} else {
					this.addComment("User is not at Ideas  page");
					throw new KDTKeywordExecException("User is not at ideas page ");
				}

				// Verify Search ideas link in the Footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "search_ideas_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "search_ideas_footer", CURR_APP);
				eo.waitForPopUp(driver);
				if (eo.getText(driver, "xpath", "idea_verify", CURR_APP).equalsIgnoreCase("Ideas")) {
					this.addComment("User is at " + "<b> Search Ideas </b>"
							+ " page and verified Search ideas footer link successfully");
				} else {
					this.addComment("User is not at search Ideas  page");
					throw new KDTKeywordExecException("User is not at search ideas page ");
				}

				// Verify View Products link in the footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "viewproducts_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "viewproducts_footer", CURR_APP);
				eo.waitForPopUp(driver);
				if (eo.getText(driver, "xpath", "myproducts_verify", CURR_APP).equalsIgnoreCase("My Products")) {
					this.addComment("User is at " + "<b> My Products </b>"
							+ " page and verified View Products footer link successfully");
				} else {
					this.addComment("User is not at My Products page");
					throw new KDTKeywordExecException("User is not at products page ");
				}

				// Verify View documentation link in the footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "viewdocumentation_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "viewdocumentation_footer", CURR_APP);
				eo.waitForPopUp(driver);
				if (eo.getText(driver, "xpath", "docdownloads_verify", CURR_APP)
						.equalsIgnoreCase("Documentation and Downloads")) {
					this.addComment("User is at " + "<b> Documentation & Downloads </b>"
							+ " page and verified View documentation footer link successfully");
				} else {
					this.addComment("User is not at Documentation and Downloads page");
					throw new KDTKeywordExecException("User is not at Documentation and Downloads page ");
				}

				// Verify Request upgrade link in the footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "requestupgrade_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "requestupgrade_footer", CURR_APP);
				eo.waitForPopUp(driver);
				if (eo.getText(driver, "xpath", "requestupgrade_verify", CURR_APP)
						.equalsIgnoreCase("New Case Details")) {
					this.addComment("User is at " + "<b> New Case </b>"
							+ " details page and verified Request upgrade footer link successfully");
				} else {
					this.addComment("User is not at New case details page");
					throw new KDTKeywordExecException("User is not at New case details page ");
				}

				// Verify About Rubrik link in the footer

				eo.javaScriptScrollToViewElement(driver, "xpath", "aboutrubrik_footer", CURR_APP);
				eo.wait(5);
				eo.clickElement(driver, "xpath", "aboutrubrik_footer", CURR_APP);
				eo.waitForPageload(driver);
				if (eo.getText(driver, "xpath", "about_header_verify", CURR_APP)
						.equalsIgnoreCase("Our Customer Advocates Ensure Your Success")) {
					this.addComment("User is at " + "<b> About Rubrik </b>"
							+ " header page and verified About Rubrik footer link successfully");
				} else {
					this.addComment("User is not at About Rubrik page");
					throw new KDTKeywordExecException("User is not at About Rubrik page");
				}

				// Verify Contact Rubrik Support link in the footer

				/*eo.javaScriptScrollToViewElement(driver, "xpath", "contactrubriksupport_footer", CURR_APP);
				eo.clickElement(driver, "xpath", "contactrubriksupport_footer", CURR_APP);
				eo.waitForPopUp(driver);

				if (eo.getText(driver, "xpath", "contactrubriksupport_verify", CURR_APP)
						.equalsIgnoreCase("Get In Touch")) {
					this.addComment("User is at " + "<b> Rubrik Contact Support </b>"
							+ " page and verified Contact Rubrik Support footer link successfully");
				} else {
					this.addComment("User is not at Rubrik contact support page");
					throw new KDTKeywordExecException("User is not at Rubrik contact support page");
				}*/

			} catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("User is Unable to verify the Footer links");
				throw new KDTKeywordExecException("User is Unable to verify the Footer links ", e);

			}

		}
	}
	


	
	/////////////////////////////////////LogoutCommunity portal////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>LogoutCustomerSupport</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to LogoutCustomerSupport
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	public static class LogoutCustomerSupport extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

				eo.clickElement(driver, "xpath", "userprofile_link", CURR_APP);
				eo.clickElement(driver, "xpath", "logout_button_community", CURR_APP);
				//eo.waitForWebElementVisible(driver, "id", "userNameVerify", waiTime, CURR_APP);
			}

			catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("Failed to LogoutCustomerSupport");
				throw new KDTKeywordExecException("Failed to LogoutCustomerSupport", e);
			}
		}
	}
	
	
	
/////////////////////////////////ValidateMycasesLandingpage//////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>MyCaseLandingPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to MyCaseLandingPage
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>columnNamesmycase(Mandatory):columnNamesmycase</li>
	 * <li>viewDropdownmyCase(Mandatory):viewDropdownmyCase</li>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	
	public static class MyCaseLandingPage extends Keyword {

		private String columnNamesmycase[];
		private String viewDropdownmyCase[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			// urlcs = args.get("UrlCS");
			columnNamesmycase = args.get("ColumnNamesMyCase").split(",");
			viewDropdownmyCase = args.get("ViewDropdownMyCase").split(",");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
				eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage", waiTime, CURR_APP);

				// Verify My cases header

				eo.clickElement(driver, "xpath", "mycases_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "mycases_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "mycases_header_verify", CURR_APP).equalsIgnoreCase("My Cases")) {
					this.addComment("User is at My Cases Landing page");
				} else {
					this.addComment("User is not at My Cases header");
					throw new KDTKeywordExecException("User is not at My Cases header");
				}

				eo.waitForPageload(driver);
				// Validate Columns available on my cases landing page

				try {

					List<WebElement> appcolumnnames = driver
							.findElements(By.xpath(gei.getProperty("mycasescompletecolumn", CURR_APP)));

					System.out.println(appcolumnnames);

					for (int i = 0; i < appcolumnnames.size(); i++) {
						String Expcolnames = columnNamesmycase[i];
						String Actualcolnames = appcolumnnames.get(i).getText();

						if (Expcolnames.equalsIgnoreCase(Actualcolnames)) {
							addComment("Verified Column Name <b>" + Expcolnames + "</b>"
									+ " is available on my cases page");
						}

						else {
							addComment("Verified Column Name <b>" + Expcolnames + "</b>"
									+ " is not available on my cases page");
						}

					}

					// Verify the presence of the view dropdown field

					eo.waitForWebElementVisible(driver, "XPATH", "view_verify", waiTime, CURR_APP);

					if (eo.getText(driver, "xpath", "view_verify", CURR_APP).equalsIgnoreCase("View:")) {
						this.addComment(" user is able to see View: Dropdown is present in the my cases landing page");
					} else {
						this.addComment(
								"user is able not to see View: Dropdown is present in the my cases landing page");
						throw new KDTKeywordExecException("User is not able to see the View: Dropdown ");
					}

					// Verify the default value of the the view dropdown
					eo.clickElement(driver, "xpath", "viewdefaultvalue", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "viewdefaultvalue", waiTime, CURR_APP);

					if (eo.getText(driver, "xpath", "viewdefaultvalue", CURR_APP)
							.equalsIgnoreCase("My Open Cases (P1/P2/P3)")) {
						this.addComment(" User is able to view the cases sorted by My open cases P1/P2/P3");
					} else {
						this.addComment("user is able not to view the cases sorted by My open cases P1/P2/P3");
						throw new KDTKeywordExecException(
								"User is not able to to view the cases sorted by My open cases P1/P2/P3 in the my cases landing page ");
					}

				} catch (Exception e) {
					this.addComment("User is Unable to verify the columns on the my cases landing page");
					throw new KDTKeywordExecException(
							"User is Unable to verify the columns in the my cases landing page ", e);
				}

				// Verify the dropdown values of the the view dropdown field

				try {

					List<WebElement> appdropdownname = driver
							.findElements(By.xpath(gei.getProperty("viewlist_verify", CURR_APP)));

					System.out.println(appdropdownname);

					for (int i = 0; i < appdropdownname.size(); i++) {
						String Expdropdownnames = viewDropdownmyCase[i];
						String Actualdropdownnames = appdropdownname.get(i).getText();

						if (Expdropdownnames.equalsIgnoreCase(Actualdropdownnames)) {
							addComment("Verified dropdownvalue Name <b>" + Expdropdownnames + "</b>"
									+ " is available on my cases page");
						}

						else {
							addComment("Verified dropdownvalue Name <b>" + Expdropdownnames + "</b>"
									+ " is not available on view dropdown of my cases page");
							throw new KDTKeywordInitException("Failed to verify the view dropdown");
						}

					}

					// click on My open cases (p4) and verify the behavior
					eo.clickElement(driver, "xpath", "viewdefaultvalue", CURR_APP);
					eo.clickElement(driver, "xpath", "viewlist_secondoption", CURR_APP);
					eo.waitForPopUp(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "priority_p4_low", waiTime, CURR_APP);
					if (eo.getText(driver, "xpath", "priority_p4_low", CURR_APP).equalsIgnoreCase("P4 - Low")) {
						this.addComment(" User is able to view the cases sorted by P4 - Low");
					} else {
						this.addComment("user is able not to view the cases sorted by P4 - Low");
						throw new KDTKeywordExecException(
								"User is not able to to view the cases sorted by P4 - Low in the my cases landing page ");
					}

					// verify the presence of the new case button

					eo.clickElement(driver, "xpath", "newcasebutton", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "newcasedetailspage", waiTime, CURR_APP);

					if (eo.getText(driver, "xpath", "newcasedetailspage", CURR_APP)
							.equalsIgnoreCase("New Case Details")) {
						this.addComment("User is at new case details page");
					} else {
						this.addComment("User is not at new case details page");
						throw new KDTKeywordExecException("User is not at new case details page");
					}

				} catch (Exception e) {
					addComment("View dropdown validation failed in the my cases page");
					throw new KDTKeywordExecException("View dropdown validation Failed in the my cases page");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("User is Unable to verify the columns on the my cases landing page");
				throw new KDTKeywordExecException("User is Unable to verify the columns in the my cases landing page ",
						e);

			}

		}
	}
	
	
/////////////////////////////////VerifyFieldsOnNewCase//////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyFieldsOnNewCase</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to VerifyFieldsOnNewCase
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>typevalues(Mandatory):typevalues</li>
	 * <li>priorityvalues(Mandatory):priorityvalues</li>
	 * <li>contactmethodvalues(Mandatory):contactmethodvalues</li>
	 * <li>fieldnamesnewcase(Mandatory):fieldnamesnewcase</li>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	
	public static class VerifyFieldsOnNewCase extends Keyword {

		
		private String typevalues[];
		private String priorityvalues[];
		private String contactmethodvalues[];
		private String fieldnamesnewcase[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
		typevalues = args.get("TypeValues").split(",");
		priorityvalues = args.get("PriorityValues").split(",");
		contactmethodvalues = args.get("ContactMethodValues").split(",");
		fieldnamesnewcase = args.get("FieldNames").split(",");
		
			

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
			/*	eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage", waiTime, CURR_APP);

				eo.clickElement(driver, "xpath", "mycases_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "mycases_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "mycases_header_verify", CURR_APP).equalsIgnoreCase("My Cases")) {
					this.addComment("User is at My Cases header");
				} else {
					this.addComment("User is not at My Cases header");
					throw new KDTKeywordExecException("User is not at My Cases header");
				}

			 eo.clickElement(driver, "xpath", "mycasebutton", CURR_APP)*/;
			 
			 
			// eo.waitForWebElementVisible(driver, "xpath", "newcasedetailspage", waiTime, CURR_APP);
					 
			 
			 /////////////////// verify values on  Type dropdown on new case page 
			 
			 
				 eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "type_click", CURR_APP);
				 List<WebElement> typedropdown = driver
							.findElements(By.xpath(gei.getProperty("typedropdown_values", CURR_APP)));

					
					for (int i = 0; i < typedropdown.size(); i++) {
						String Exptypedropdownnames = typevalues[i];
						String Actualdropdownnames = typedropdown.get(i).getText();

						if (Exptypedropdownnames.equalsIgnoreCase(Actualdropdownnames)) {
							this.addComment("Verified Type dropdownvalue Values <b>" + Exptypedropdownnames + "</b>"
									+ " on new cases page");
						}

						else {
							this.addComment("Verified dropdownvalue Name <b>" + Exptypedropdownnames + "</b> is not available ");
							
						}
					}
			 

			 
   /////////////////// verify Values on Priority  dropdown on new case page 
			 

				
	 eo.clickElement(driver, "xpath", "prioritydropdown1", CURR_APP);
				 List<WebElement> prioritydropdown = driver
							.findElements(By.xpath(gei.getProperty("prioritydropdown_values", CURR_APP)));

					
					for (int i = 0; i < prioritydropdown.size(); i++) {
						String Expprioritydropdownnames = priorityvalues[i];
						String Actualdropdownnames = prioritydropdown.get(i).getText();

						if (Expprioritydropdownnames.equalsIgnoreCase(Actualdropdownnames)) {
							this.addComment("Verified Priority dropdownvalue Values <b>" + Expprioritydropdownnames + "</b>"
									+ " on new cases page");
						}

						else {
							this.addComment("Verified Priority dropdownvalue Name <b>" + Expprioritydropdownnames + "</b> is not available ");
							
						}
					}
			 

			 
			 
/////////////////// verify Values on Contact Method  dropdown on new case page 

				

					 eo.clickElement(driver, "xpath", "contactmethoddropdown1", CURR_APP);
					List<WebElement> contactdropdown = driver
							.findElements(By.xpath(gei.getProperty("contactmethoddropdown_values", CURR_APP)));

					for (int i = 0; i < contactdropdown.size(); i++) {
						String Expcontactdropdownnames = contactmethodvalues[i];
						String Actualdropdownnames = contactdropdown.get(i).getText();

						if (Expcontactdropdownnames.equalsIgnoreCase(Actualdropdownnames)) {
							this.addComment("Verified Type dropdownvalue Values <b>" + Expcontactdropdownnames + "</b>"
									+ " on new cases page");
						}

						else {
							this.addComment("Verified dropdownvalue Name <b>" + Expcontactdropdownnames + "</b> is not available ");

						}
					}
				
 
			 ////////////////Verify inline Error Message ////////////////////////
				
				
				

					eo.javaScriptScrollToViewElement(driver, "xpath", "submitcase_button", CURR_APP);
					 eo.clickElement(driver, "xpath", "submitcase_button", CURR_APP);
					

					for (int i = 0; i < fieldnamesnewcase.length; i++) {
						String ExpErrorMessage = eo.getText(driver, "xpath", "inlineerrormessage_newcase", CURR_APP);
						String Actualdropdownnames = fieldnamesnewcase[i];
						
							this.addComment("Inline Error Message for  <b>" + fieldnamesnewcase[i] + "</b>"
									+ " is" + "<b>" + ExpErrorMessage +"/b" );
						
				}
		
				}

			 catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("User is Unable to Validate Fields on New Case page");
				throw new KDTKeywordExecException("User is Unable to Validate Fields on New Case page ");
						
						
						

			}

		}
	}

	
	
	
///////////////////////////////// ValidateRecordsPerPage/////////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateRecordsPerPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateRecordsPerPage
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>recordsperpage(Mandatory):recordsperpage</li>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash KP</i></b>
	 * </p>
	 * </div>
	 */
	

	public static class ValidateRecordsPerPage extends Keyword {

		// private String urlcs;
		private String recordsperpage[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			// urlcs = args.get("UrlCS");
			recordsperpage = args.get("RecordsPerPage").split(",");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			// Verify the dropdown values of the the number of records per page field

			try {
				WebDriver driver = context.getWebDriver();
				//eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage", waiTime, CURR_APP);
				eo.waitForPopUp(driver);
				//eo.clickElement(driver, "xpath", "mycases_header", CURR_APP);
				//eo.waitForWebElementVisible(driver, "XPATH", "mycases_header_verify", waiTime, CURR_APP);
				//eo.javaScriptScrollToViewElement(driver, "xpath", "numberofrecperpage", CURR_APP);

				List<WebElement> numberrecperpagedropdown = driver
						.findElements(By.xpath(gei.getProperty("numberofrecperpage_dropdown", CURR_APP)));

				System.out.println(numberrecperpagedropdown);

				for (int i = 0; i < numberrecperpagedropdown.size(); i++) {
					String Expdropdownnames = recordsperpage[i];
					String Actualdropdownnames = numberrecperpagedropdown.get(i).getText();

					if (Expdropdownnames.equalsIgnoreCase(Actualdropdownnames)) {
						addComment("Verified dropdownvalue Name <b>" + Expdropdownnames + "</b>"
								+ " is available on my cases page");
					}

					else {
						addComment("Verified dropdownvalue Name <b>" + Expdropdownnames + "</b>"
								+ " is not available on number of records per page dropdown of my cases page");
						throw new KDTKeywordInitException(
								"Failed to verify the number of records dropdown in the my cases page");
					}

					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "recordperpage_count", "{count}",
							recordsperpage[i], CURR_APP);

					eo.waitForPageload(driver);

					List<WebElement> caserowcount = driver
							.findElements(By.xpath(gei.getProperty("case_rowcount", CURR_APP)));

					if (caserowcount.size() == Integer.parseInt(Expdropdownnames)) {
						this.addComment("Number of records displayed on Mycase page is <b> " + caserowcount.size());
					}

					else {
						addComment("Number of records for <b>" + Expdropdownnames + "<b> is not matching with <b>"
								+ caserowcount.size() + "</b>available on page");
						throw new KDTKeywordInitException("Number of records for <b>" + Expdropdownnames
								+ "<b> is not matching with <b>" + caserowcount.size() + "</b> available on page");
					}

				}

			} catch (Exception e) {
				this.addComment("User is not able to verify Records per page");
				throw new KDTKeywordExecException("User is not able to verify Records per page ");
			}
		}
	}
	
	
////////////////////////////////VerifyPagination/////////////////////////
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>VerifyPagination</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to VerifyPagination
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Akash KP</i></b>
	 * </p>
	 * </div>
	 */
	
	public static class VerifyPagination extends Keyword {

		// private String urlcs;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			// urlcs = args.get("UrlCS");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {

				WebDriver driver = context.getWebDriver();
				
				eo.clickElement(driver, "xpath", "mycases_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "mycases_header_verify", waiTime, CURR_APP);
				
				eo.javaScriptScrollToViewElement(driver, "xpath", "numberofrecords_perpage", CURR_APP);
				
				eo.waitForPopUp(driver);
				eo.clickElement(driver, "xpath", "next_button", CURR_APP);
			
				eo.waitForWebElementVisible(driver, "XPATH", "page_verify", waiTime, CURR_APP);
				String[] s = eo.getText(driver, "xpath", "page_verify", CURR_APP).split(" ");
				System.out.println(s);
				if (s[1].equalsIgnoreCase("2")) {
					this.addComment(" User is able to navigate" + s[1] + "nd  page");
				} else {
					this.addComment("User is not ableto to navigate to <b>" + s[1] + "</b> page");
					throw new KDTKeywordExecException("User is not able to to navigate to next page ");
				}
				
				eo.clickElement(driver, "xpath", "previous_button", CURR_APP);
				eo.waitForPageload(driver);
				
				String[] s1 = eo.getText(driver, "xpath", "page_verify", CURR_APP).split(" ");
				if (s1[1].equalsIgnoreCase("1")) {
					this.addComment(" User is able  to navigate to" + s1[1] +"st  page");
				} else {
					this.addComment("User is not ableto to navigate to  " + s[1] +"previous page");
					throw new KDTKeywordExecException(
							"User is not able to to navigate to previous page of my cases page ");
				}

			} catch (Exception e) {
				this.addComment("Page navigation failed");
				throw new KDTKeywordExecException(
						"User is not able to to verify Pagination of my cases page ");
			}

		}
	}

	
	///////////////////////////// NewCaseWithoutAttachment//////////////////////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateNewCaseWithoutAttachment</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to CreateNewCaseWithoutAttachment
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>newCaseTypes(Mandatory): newCaseTypes</li>
	 * <li>newCasePriorities(Mandatory): newCasePriorities</li>
	 * <li>newCaseContactMethods(Mandatory): newCaseContactMethods</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateNewCaseWithoutAttachment extends Keyword {

		private String newCaseTypes[];
		private String newCasePriorities[];
		private String newCaseContactMethods[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			// urlcs = args.get("UrlCS");
			newCaseTypes = args.get("NewCaseTypes").split(",");
			newCasePriorities = args.get("NewCasePriorities").split(",");
			newCaseContactMethods = args.get("NewCaseContactMethods").split(",");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();
				eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage", waiTime, CURR_APP);
				eo.waitForPopUp(driver);

				for (int i = 0; i < newCaseTypes.length; i++) {
					/*if (i == 1) {
						break;
					}*/

					eo.clickElement(driver, "xpath", "mycases_header", CURR_APP);
					eo.waitForPopUp(driver);
					eo.waitForPopUp(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "mycases_header_verify", waiTime, CURR_APP);
					eo.clickElement(driver, "xpath", "newcasebutton", CURR_APP);
					eo.waitForPopUp(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "newcasedetailspage", waiTime, CURR_APP);
					this.addComment("The user is able to navigate to <b>" + "New Case" + "</b>" + "details page");

					eo.clickElement(driver, "xpath", "subject_newcasedetails_page", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "newcasedetailspage", waiTime, CURR_APP);
					// eo.waitForPopUp(driver);
					String subject = eo.gnerateRandomNo(5);
					eo.enterText(driver, "xpath", "subject_input", "QATest" + subject, CURR_APP);
					this.addComment("The user is able to enter the the <b>" + "Subject" + "</b>"
							+ " in the new case details page successfully");

					eo.clickElement(driver, "xpath", "type_click", CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "alltypedropdown", "{type}", newCaseTypes[i],
							CURR_APP);
					this.addComment("The user is able to select <b>" + "Type" + "</b>" + " of case successfully"
							+ newCaseTypes[i]);

					eo.clickElement(driver, "xpath", "priority_click", CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "allprioritydropdown", "{priority}",
							newCasePriorities[i], CURR_APP);
					this.addComment("The user is added the case <b>" + "Priority" + "</b>" + " successfully"
							+ newCasePriorities[i]);

					eo.clickElement(driver, "xpath", "contactmethod_click", CURR_APP);
				//	eo.waitForPopUp(driver);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "allcontactmethod", "{contact}",
							newCaseContactMethods[i], CURR_APP);
					this.addComment("The user is added the <b>" + "Contact Method" + "</b>" + " successfully "
							+ newCaseContactMethods[i]);

					eo.enterText(driver, "xpath", "supporttunnelnumber", subject, CURR_APP);
					this.addComment(
							"The user is able to add the <b>" + "Support Tunnel Number" + "</b>" + " successfully");

					eo.clickElement(driver, "xpath", "description_entertext", CURR_APP);
					eo.enterText(driver, "xpath", "description_entertext",
							"testing creating a new case without attachment", CURR_APP);
					this.addComment("The user added the required text in  <b>" + "Description field" + "</b>"
							+ " successfully");

					eo.javaScriptScrollToViewElement(driver, "xpath", "submit_button", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "submit_button", waiTime, CURR_APP);
					eo.clickElement(driver, "xpath", "submit_button", CURR_APP);
			        this.addComment("Clicked on Submit button");
					eo.waitForPageload(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "createdcase_verify", waiTime, CURR_APP);
					eo.waitForPageload(driver);
					if (eo.getText(driver, "xpath", "createdcase_verify", CURR_APP).equalsIgnoreCase("Case Number")) {
						this.addComment("User is at created case details page");
					} else {
						this.addComment("User is not at created case details page");
						throw new KDTKeywordExecException("User is not at created case details page");
					}
					eo.waitForPageload(driver);
					// Validate "Add Comment" on case detail page
					eo.clickElement(driver, "xpath", "addcomment_button", CURR_APP);
					eo.clickElement(driver, "xpath", "addcomment_text", CURR_APP);
					eo.enterText(driver, "xpath", "addcomment_text", "Testingcomment", CURR_APP);
					this.addComment("Added case comment <b>" + "Testingcomment" + "</b>" );
					eo.clickElement(driver, "xpath", "addcommentbutton_popup", CURR_APP);
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.javaScriptScrollToViewElement(driver, "xpath", "casecomments", CURR_APP);
					eo.waitForPageload(driver);
					if (eo.getText(driver, "xpath", "comment_verify", CURR_APP).equalsIgnoreCase("Testingcomment")) {
						this.addComment("The <b>" + "comment added by the user is verified successfully" + "</b>");
					} else {
						this.addComment("User is not at able to add the comment ");
						throw new KDTKeywordExecException("User is failed to add the comment");
					}

				}
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver, "xpath", "casenumber_text", CURR_APP);
				casenumbertext = eo.getText(driver, "xpath", "casenumber_text", CURR_APP);

				prioritytext = eo.getText(driver, "xpath", "priority_text", CURR_APP);

				statustext = eo.getText(driver, "xpath", "status_text", CURR_APP);

			}

			catch (Exception e) {
				this.addComment("user is not able to submit the case without attachment");
				throw new KDTKeywordExecException("user is not able to submit the case without attachment");
			}

		}
	}

	
	
	
	///////////////////////////////////////////CreateNewCaseWithAttachment///////////////////////////
	
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateNewCaseWithAttachment</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to CreateNewCaseWithAttachment
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>newCaseTypes(Mandatory): newCaseTypes</li>
	 * <li>newCasePriorities(Mandatory): newCasePriorities</li>
	 * <li>newCaseContactMethods(Mandatory): newCaseContactMethods</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateNewCaseWithAttachment extends Keyword {

		private String newCaseTypes[];
		private String newCasePriorities[];
		private String newCaseContactMethods[];
		private String expectedFileName;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			newCaseTypes = args.get("NewCaseTypes").split(",");
			newCasePriorities = args.get("NewCasePriorities").split(",");
			newCaseContactMethods = args.get("NewCaseContactMethods").split(",");
			expectedFileName = args.get("ExpectedFileName");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();
				eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage", waiTime, CURR_APP);
				eo.waitForPopUp(driver);

				for (int i = 0; i < newCaseTypes.length; i++) {
					if (i == 1) {
						break;
					}

					eo.clickElement(driver, "xpath", "mycases_header", CURR_APP);
					eo.waitForPopUp(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "mycases_header_verify", waiTime, CURR_APP);
					eo.clickElement(driver, "xpath", "newcasebutton", CURR_APP);
					eo.waitForPopUp(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "newcasedetailspage", waiTime, CURR_APP);
					this.addComment("The user is able to navigate to <b>" + "New Case" + "</b>" + "details page");

					eo.clickElement(driver, "xpath", "subject_newcasedetails_page", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "newcasedetailspage", waiTime, CURR_APP);
					// eo.waitForPopUp(driver);
					String subject = eo.gnerateRandomNo(5);
					eo.enterText(driver, "xpath", "subject_input", "QATest" + subject, CURR_APP);
					this.addComment("The user is able to enter the the <b>" + "Subject" + "</b>"
							+ " in the new case details page successfully");

					eo.clickElement(driver, "xpath", "type_click", CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "alltypedropdown", "{type}", newCaseTypes[i],
							CURR_APP);
					this.addComment("The user is able to select <b>" + "Type" + "</b>" + " of case successfully"
							+ newCaseTypes[i]);

					eo.clickElement(driver, "xpath", "priority_click", CURR_APP);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "allprioritydropdown", "{priority}",
							newCasePriorities[i], CURR_APP);
					this.addComment("The user is added the case <b>" + "Priority" + "</b>" + " successfully"
							+ newCasePriorities[i]);

					eo.clickElement(driver, "xpath", "contactmethod_click", CURR_APP);
					// eo.waitForPopUp(driver);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "allcontactmethod", "{contact}",
							newCaseContactMethods[i], CURR_APP);
					this.addComment("The user is added the <b>" + "Contact Method" + "</b>" + " successfully "
							+ newCaseContactMethods[i]);

					eo.enterText(driver, "xpath", "supporttunnelnumber", subject, CURR_APP);
					this.addComment(
							"The user is able to add the <b>" + "Support Tunnel Number" + "</b>" + " successfully");

					eo.clickElement(driver, "xpath", "description_entertext", CURR_APP);
					eo.enterText(driver, "xpath", "description_entertext",
							"testing creating a new case with attachment", CURR_APP);
					this.addComment("The user added the required text in  <b>" + "Description field" + "</b>"
							+ " successfully");

					eo.waitForPageload(driver);
					eo.javaScriptScrollToViewElement(driver, "xpath", "saveAndAttachFile_button", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "saveAndAttachFile_button", waiTime, CURR_APP);
					eo.clickElement(driver, "xpath", "saveAndAttachFile_button", CURR_APP);
					eo.waitForPageload(driver);

					eo.waitForWebElementVisible(driver, "XPATH", "chooseAttachemtnt_button", waiTime, CURR_APP);

					WebElement ele = driver
							.findElement(By.xpath(gei.getProperty("chooseAttachemtnt_button", CURR_APP)));

					String file = System.getProperty("user.dir") + "//Datasheets/AttachmentFile.txt";

					ele.sendKeys(file);
					this.addComment("Successfully File Uploaded");

					eo.waitForPageload(driver);

					eo.waitForWebElementVisible(driver, "xpath", "done_button", 90, CURR_APP);

					eo.clickElement(driver, "xpath", "done_button", CURR_APP);

					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "casedetail_text", waiTime, CURR_APP);
					
					eo.javaScriptScrollToViewElement(driver, "xpath", "verifyFile", CURR_APP);
					eo.waitForWebElementVisible(driver, "xpath", "verifyFile", waiTime, CURR_APP);

					List<WebElement> allfiles = eo.getListOfWebElements(driver, "xpath", "verifyFile", CURR_APP);
					String FileNames;
					for (WebElement allfilesText : allfiles) {
						FileNames = allfilesText.getText();

						if (FileNames.equals(expectedFileName)) {
							this.addComment("Successfully Verified Uploaded" + "<b>" + FileNames + "</b> File ");
						} else {
							this.addComment("Failed to verify the Uploaded" + "<b>" + FileNames + "</b> File");
						}
					}

					eo.javaScriptScrollToViewElement(driver, "xpath", "casenumber_text", CURR_APP);
					casenumbertext2 = eo.getText(driver, "xpath", "casenumber_text", CURR_APP);

				}
			} catch (Exception e) {
				this.addComment("user is not able to submit the case with attachment");
				throw new KDTKeywordExecException("user is not able to submit the case with attachment");
			}
		}
	}
	
	////////////////////////ValidateCasewithattachmentInSalesforce
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateCasewithattachmentInSalesforce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateCasewithattachmentInSalesforce
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Mahesh</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateCasewithattachmentInSalesforce extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {

			 int waiTime = Integer.parseInt(gei.getProperty("waitTime",
			 CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				eo.enterText(driver, "xpath", "search_homepage", casenumbertext2, CURR_APP);
				this.addComment("Case number is added in the search field");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);

				eo.waitForPopUp(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "casenumber_searched", "{case}", casenumbertext2,
						CURR_APP);

				eo.waitForPageload(driver);

				// Verification for Attached Files

				eo.javaScriptScrollToViewElement(driver, "xpath", "AttachmentsInSL", CURR_APP);
				List<WebElement> all = eo.getListOfWebElements(driver, "xpath", "allAttachFiles", CURR_APP);
				for (WebElement AllFiles : all) {
					String filenames = AllFiles.getText();
					if (filenames.equals("AttachmentFile")) {
						this.addComment("Successfully Verified the Attached Filename in SalesForce Application");
					} else {
						this.addComment("Fail to verify the Attached file in SalesForce Application");
					}
				}

			}

			catch (Exception e) {
				this.addComment("User is not able to Validate Attached file name in SalesForce");
				throw new KDTKeywordExecException("User is not able to Validate Attached file name in SalesForce");
			}

		}
	}
	
	///////////////////////////// ValidateCaseInSaleforce//////////////////////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateCaseInSalesforce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateCaseInSalesforce
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateCaseInSalesforce extends Keyword {

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			// urlcs = args.get("UrlCS");

		}

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

				
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.enterText(driver, "xpath", "search_homepage", casenumbertext, CURR_APP);
				this.addComment("Case number is added in the search field");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);
				
				eo.waitForPopUp(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "casenumber_searched", "{case}", casenumbertext,
						CURR_APP);

				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver, "xpath", "scrolldownto_casedetails", CURR_APP);

				if (eo.getText(driver, "xpath", "priority_salesforce", CURR_APP).equalsIgnoreCase(prioritytext)) {
					this.addComment(
							"Verification of priority of the case  is completed successfully in saleforce!!!!!!!!!");
				} else {
					this.addComment("The priority verification is unsuccessfull in salesforce");
					throw new KDTKeywordExecException("The priority verification is failed in salesforce");
				}

				if (eo.getText(driver, "xpath", "status_salesforce", CURR_APP).equalsIgnoreCase(statustext)) {
					this.addComment(" Verification of status of the case completed successfully in saleforce!!!!!!!!!");
				} else {
					this.addComment("The status of the created case verification is unsuccessfull in salesforce");
					throw new KDTKeywordExecException("The status of the created case verification is failed ");
				}

			}

			catch (Exception e) {
				this.addComment("user is not able to fetch the case number");
				throw new KDTKeywordExecException("User is not able to fetch the case number");
			}

		}
	}

///////////////////////////////////////////////	OpenCaseInSalesforce///////////////////////////////
	

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpenCaseInSalesforce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to OpenCaseInSalesforce
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	public static class OpenCaseInSalesforce extends Keyword {

		private String casenumber;
		
		
		

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			casenumber = args.get("CaseNumber");
			
			
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			// int waiTime =
			// Integer.parseInt(gei.getProperty("waitTime",CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.enterText(driver, "xpath", "search_homepage", casenumber, CURR_APP);
				this.addComment("Case number is added in the search field");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);

				eo.waitForPopUp(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "casenumber_searched", "{case}", casenumber,
						CURR_APP);

				eo.waitForPageload(driver);

				eo.javaScriptScrollToViewElement(driver, "xpath", "caseSpecificsField", CURR_APP);

				eo.javaScriptDoubleClick(driver, "xpath", "Fields", CURR_APP);
				
				
						
		} catch (Exception e) {
			this.addComment("user is not able to fetch the case number");
			throw new KDTKeywordExecException("User is not able to fetch the case number");
		}
	}
	}
	
	
	
//////////////////////////////////ValidateProductLineItems//////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateProductLineItems</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateProductLineItems
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>component(Mandatory): component</li>
	 * <li>productLine(Mandatory): productLine</li>
	 * <li>functionalArea(Mandatory): functionalArea</li>
	 * <li>subcomponent(Mandatory): subcomponent</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	
	public static class ValidateProductLineItems extends Keyword {

		private String component;
		private String productLine;
		private String functionalArea;
		private String subcomponent[];
		private String subcomponentvalue;
		
		

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			
			functionalArea = args.get("FunctionalArea");
			component = args.get("Component");
			productLine = args.get("ProductLine");
			subcomponent = args.get("Subcomponent").split(",");
			subcomponentvalue = args.get("SubcomponetValue");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			// int waiTime =
			// Integer.parseInt(gei.getProperty("waitTime",CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();
			
				
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productline_dropdown", "{value}", productLine, CURR_APP);
				this.addComment("Slected Product line <b>" + productLine + "</b>");
				
				
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "functionalArea_dropdown", "{value}", functionalArea, CURR_APP);
				this.addComment("Slected functional area <b>" + functionalArea + "</b>");
				
			
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "component_dropdown", "{value}", component, CURR_APP);
				this.addComment("Slected component <b>" + component + "</b>");
				//eo.waitForPageload(driver);
				//eo.javaScriptScrollToViewElement(driver, "xpath", "caseSpecificsField", CURR_APP);

			//	eo.javaScriptDoubleClickAfterReplacingValue(driver, "xpath", "Fields", "{fieldName}", productLine,
						//CURR_APP);
				eo.clickElement(driver, "xpath", "subcomponent", CURR_APP);
			//	eo.waitForPageload(driver);
				
				List<WebElement> Subcomponents = driver
						.findElements(By.xpath(gei.getProperty("subcomponent_list", CURR_APP)));
				for (int i=1;i<Subcomponents.size(); i++)
				{
					String expectedsubcomponet = Subcomponents.get(i).getText();
					if(expectedsubcomponet.equalsIgnoreCase(subcomponent[i]))
					{
						this.addComment("Subcomponent <b>" +expectedsubcomponet + "</b>"+ " is verified " );
					}
				}
								
			this.addComment("Subcomponent is verified and matching  for productLine: <b>" + productLine + "</b>" + " Functionl Area: <b>" +  functionalArea +"</b>" + " Component:<b> " + component + "</b>" );	
			
			//eo.clickElement(driver, "xpath", "ok_button", CURR_APP);
			/*eo.clickElementAfterReplacingKeyValue(driver, "xpath", "subcomonent_dropdown", "{value}", subcomponentvalue, CURR_APP);
			this.addComment("Slected subcomponent <b>" + subcomponentvalue + "</b>");
			
			eo.clickElement(driver, "xpath", "ok_button", CURR_APP);
			
			eo.waitForPageload(driver);
			
			eo.javaScriptScrollToViewElement(driver, "xpath", "save_button_caseproduct", CURR_APP);
			eo.clickElement(driver, "xpath", "save_button_caseproduct", CURR_APP);
			eo.waitForPageload(driver);
			eo.javaScriptScrollToViewElement(driver, "xpath", "Fields", CURR_APP);
			eo.waitForPageload(driver);*/
			
			
			
			
		} catch (Exception e) {
			this.addComment("Subcomponent is not matching  for productLine: <b>\" + productLine + \"</b>\" + \" Functionl Area: <b>\" +  functionalArea +\"</b>\" + \"Component:<b> \" + component + \"</b>\"");
			throw new KDTKeywordExecException("user is not able to verify the subcomponents");
		}
	}
	}
	
	
	////////////////////////////////// SelectProductLineItems//////////////////
	

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>SelectProductLineItems</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to SelectProductLineItems
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>component(Mandatory): component</li>
	 * <li>productLine(Mandatory): productLine</li>
	 * <li>functionalArea(Mandatory): functionalArea</li>
	 * <li>subcomponent(Mandatory): subcomponent</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	

	public static class SelectProductLineItems extends Keyword {

		private String component;
		private String productLine;
		private String functionalArea;
		private String subcomponent[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			functionalArea = args.get("FunctionalArea");
			component = args.get("Component");
			productLine = args.get("ProductLine");
			subcomponent = args.get("Subcomponent").split(",");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

		int waiTime =
			Integer.parseInt(gei.getProperty("waitTime",CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "productline_dropdown", "{value}", productLine,
						CURR_APP);
				this.addComment("Slected Product line <b>" + productLine + "</b>");
				
				

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "functionalArea_dropdown", "{value}",
						functionalArea, CURR_APP);
				this.addComment("Slected functional area <b>" + functionalArea + "</b>");

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "component_dropdown", "{value}", component,
						CURR_APP);
				this.addComment("Slected component <b>" + component + "</b>");
				
				eo.javaScriptDoubleClickAfterReplacingValue(driver, "xpath", "Fields", "{fieldName}", productLine,
						CURR_APP);
				eo.clickElement(driver, "xpath", "subcomponent", CURR_APP);
				eo.waitForPageload(driver);

				List<WebElement> Subcomponents = driver
						.findElements(By.xpath(gei.getProperty("subcomponent_list", CURR_APP)));
				for (int i = 1; i < Subcomponents.size(); i++) {
					String expectedsubcomponet = Subcomponents.get(i).getText();
					if (expectedsubcomponet.equalsIgnoreCase(subcomponent[i])) {
						this.addComment("Subcomponent <b>" + expectedsubcomponet + "</b>" + " is verified ");
					}
				}
				
				eo.clickElement(driver, "xpath", "ok_button_productline", CURR_APP);

				this.addComment("Subcomponent is verified and matching  for productLine: <b>" + productLine + "</b>"
						+ " Functionl Area: <b>" + functionalArea + "</b>" + " Component:<b> " + component + "</b>");

			} catch (Exception e) {
				this.addComment(
						"Subcomponent is not matching  for productLine: <b>\" + productLine + \"</b>\" + \" Functionl Area: <b>\" +  functionalArea +\"</b>\" + \"Component:<b> \" + component + \"</b>\"");
				throw new KDTKeywordExecException("user is not able to verify the subcomponents");
			}
		}
	}


	
	///////////////////////////////// Validate KnowledgeBase LandingPage//////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>KnowledgeBaseLandingPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to KnowledgeBaseLandingPage
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>knowLedgeHeaderText(Mandatory): knowLedgeHeaderText</li>
	 * <li>expectedCategories(Mandatory): expectedCategories</li>
	 * <li>expectedHeaderText(Mandatory): expectedHeaderText</li>
	 * <li>sortbyoptions(Mandatory): sortbyoptions</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class KnowledgeBaseLandingPage extends Keyword {

		private String knowLedgeHeaderText;
		private String expectedCategories[];
		private String expectedHeaderText[];
		private String sortbyoptions[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			knowLedgeHeaderText = args.get("KnowLedgeHeaderText");
			expectedCategories = args.get("ExpectedCategories").trim().split(",");
			expectedHeaderText = args.get("ExpectedHeaderText").split(",");
			sortbyoptions = args.get("SortbyOptions").split(",");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
				eo.waitForWebElementVisible(driver, "XPATH", "rubrik_support_landingpage", waiTime, CURR_APP);

				// Verify KnowledgeBase header

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "mycases_headerlink", "{header}",
						knowLedgeHeaderText, CURR_APP);

				eo.waitForWebElementVisible(driver, "XPATH", "knowledge_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "knowledge_header_verify", CURR_APP)
						.equalsIgnoreCase(knowLedgeHeaderText)) {
					this.addComment("User is at KnowledgeBase header");
				} else {
					this.addComment("User is not at KnowledgeBase header");
					throw new KDTKeywordExecException("User is not at KnowledgeBase header");
				}
				
                 eo.clickElement(driver, "xpath", "managesubscription_button", CURR_APP);
                 this.addComment("clicked on Managed Subscrption");
				
                // driver.findElement(By.xpath("//div[contains(text(),'All')]/..")).click();
                 eo.clickElement(driver, "xpath", "All_Category_icon", CURR_APP);
                 this.addComment("clicked on All category  Icon and all cateroy is selected for subscription");
               //  eo.waitForPageload(driver);
                 
                 eo.clickElement(driver, "xpath", "All_Category_icon", CURR_APP);
                 this.addComment("clicked on All category  Icon and all cateroy is deselected for subscription");
                 
                 eo.clickElement(driver, "xpath", "api_category_icon", CURR_APP);
                 this.addComment("Selected API category for subscription");
                 
                 eo.clickElement(driver, "xpath", "cancelbutton_subscribepage", CURR_APP);
                 eo.waitForWebElementVisible(driver, "XPATH", "knowledge_header_verify", waiTime, CURR_APP);
                 
                 eo.clickElement(driver, "xpath", "managesubscription_button", CURR_APP);
                 this.addComment("clicked on Managed Subscrption");
				
                 eo.clickElement(driver, "xpath", "All_Category_icon", CURR_APP);
                 this.addComment("clicked on All category  Icon and all cateroy is selected for subscription");
                // eo.waitForPageload(driver);
                 
                 eo.clickElement(driver, "xpath", "savebutton_subscribepage", CURR_APP);
                 this.addComment(" Subscrption Selected for all Categories");
                eo.waitForPageload(driver);
                 
                 eo.waitForWebElementVisible(driver, "XPATH", "knowledge_header_verify", waiTime, CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "categories", "{category}", "All", CURR_APP);
				eo.waitForPageload(driver);
				
                eo.waitForWebElementVisible(driver, "xpath", "category_text", waiTime, CURR_APP);
				List<WebElement> appcolumnnames = driver
						.findElements(By.xpath(gei.getProperty("allCategories", CURR_APP)));
				
			
				
                String xpath ="((//div[contains(text(),'Categories')])[1]/..//span[@class='categName'])[{count}]";
                
				for (int i = 0; i < appcolumnnames.size(); i++) {
					String xpath2= xpath.replace("{count}", Integer.toString(i+1));
					String ActualCategories = driver.findElement(By.xpath(xpath2)).getText();
					//String ActualCategories = appcolumnnames.get(i).getText();

					if (ActualCategories.equalsIgnoreCase(expectedCategories[i])) {
						this.addComment("Successfully verified the" + "<b>" + ActualCategories + "</b>" + " categorie");
						
						driver.findElement(By.xpath(xpath2)).click();
						eo.waitForPageload(driver);
					
						
						String articlename = eo.getText(driver, "xpath", "verify_header2", CURR_APP);
						if(articlename.equalsIgnoreCase(expectedHeaderText[i])) {
							
							this.addComment("Successfully verified the" + "<b>" + articlename + "</b>");	
						}
						else
						{
							this.addComment("Failed to verify the" + "<b>" + articlename + "</b>");
						}
						
											
					} else {
						this.addComment("Failed to verify the" + "<b>" + ActualCategories + "</b>" + "category");
					}
					
					for (int j =0; j<sortbyoptions.length;j++)
					{
						//eo.clickElement(driver, "xpath", "", application);
						eo.clickElementAfterReplacingKeyValue(driver, "xpath", "sortby_dropdown", "{sortoption}", sortbyoptions[j], CURR_APP);
						//eo.waitForPageload(driver);
					
					}
					if(eo.getText(driver, "xpath", "subscribe_button", CURR_APP).equalsIgnoreCase("Subscribe"))
					{
					eo.clickElement(driver, "xpath", "subscribe_button", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "okbutton", waiTime, CURR_APP);
					
					String subconfmessage = eo.getText(driver, "xpath", "confmessage_subandunsub", CURR_APP);
					if(subconfmessage.equalsIgnoreCase("You have subscribed to this category."))
					{
						this.addComment("Category <b>"+ expectedCategories[i] + "</b>" +  "is subscribed successfully");
					}
					
					else {
						this.addComment("Category <b>"+ expectedCategories[i] + "</b>" +  "is not subscribed ");
					}
										
					eo.clickElement(driver, "xpath", "okbutton", CURR_APP);
					eo.clickElement(driver, "xpath", "unsubscribe_button", CURR_APP);
					eo.waitForWebElementVisible(driver, "XPATH", "okbutton", waiTime, CURR_APP);
					
					String unsubconfmessage = eo.getText(driver, "xpath", "confmessage_subandunsub", CURR_APP);
					if(unsubconfmessage.equalsIgnoreCase("You have unsubscribed to this category."))
					{
						this.addComment("Category<b>"+ expectedCategories[i] + "</b>" +  " is unsubscribed successfully");
					}
					
					else {
						this.addComment("Category<b>"+ expectedCategories[i] + "</b>" +  " is not unsubscribed ");
					}
					
					eo.clickElement(driver, "xpath", "okbutton", CURR_APP);
					}
					else
					{
						eo.clickElement(driver, "xpath", "unsubscribe_button", CURR_APP);
						eo.waitForWebElementVisible(driver, "XPATH", "okbutton", waiTime, CURR_APP);
						
						String unsubconfmessage = eo.getText(driver, "xpath", "confmessage_subandunsub", CURR_APP);
						if(unsubconfmessage.equalsIgnoreCase("You have unsubscribed to this category."))
						{
							this.addComment("Category<b>"+ expectedCategories[i] + "</b>" +  " is unsubscribed successfully");
						}
						
						else {
							this.addComment("Category<b>"+ expectedCategories[i] + "</b>" +  " is not unsubscribed ");
						}
						
						eo.clickElement(driver, "xpath", "okbutton", CURR_APP);
						
					}
					
				}
					
					

			} catch (Exception e) {
				
				this.addComment("All categories at KB is not validated ");
				throw new KDTKeywordExecException("All categories at KB is not validated");
			}

		}
	}
	
	////////////////////////////////////////ValidateIdeasLandingPage/////////////////////////////////////////////
	
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateIdeasLandingPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateIdeasLandingPage
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>filtersideas(Mandatory): filtersideas</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */
	
	public static class ValidateIdeasLandingPage extends Keyword {

		
		private String filtersideas[];
		

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			filtersideas = args.get("FiltersIdeas").split(",");
			
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {

				WebDriver driver = context.getWebDriver();
				eo.clickElement(driver, "xpath", "idea_link", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "Ideas_text", waiTime, CURR_APP);
               String ideatext = eo.getText(driver, "xpath", "Ideas_text", CURR_APP);
				// Verify Ideas header page
				
				if (ideatext.equalsIgnoreCase("Ideas"))
				{
					this.addComment("User at Ideas Page");
				}
				else
				{
					this.addComment("User is not at Ideas page ");
					throw new KDTKeywordExecException("Ideas landing page validation failed");
				}
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "submitanIdea_button", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "newIdea_Text", waiTime, CURR_APP);
				this.addComment("Clicked on Submit An Idea button");
				String newideatext = eo.getText(driver, "xpath", "newIdea_Text", CURR_APP);
				if(newideatext.equalsIgnoreCase("New Idea"))
				{
					this.addComment("User at Create New Idea page");
				}
				else
				{
					this.addComment("User is not at New Idea page ");
					throw new KDTKeywordExecException("Create Idea button page is not working for Submit and idea");
				}
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "cancel_newidea", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "Ideas_text", waiTime, CURR_APP);
				
          ///////////////////Verify Pagination////////////////////////////////////////////////
				
				eo.clickElement(driver, "xpath", "next_arrow", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "pagination_text", waiTime, CURR_APP);
				String[] s = eo.getText(driver, "xpath", "pagination_text", CURR_APP).split(" ");
				System.out.println("pagination text" + s);
				if (s[1].equalsIgnoreCase("51-100")) {
					this.addComment(" User is able to navigate 2nd  page");
				} else {
					this.addComment("User is not able to navigate to 2nd  page");
					throw new KDTKeywordExecException("User is not able to to navigate to next page ");
				}

				eo.clickElement(driver, "xpath", "previous_arrow", CURR_APP);
				//eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "pagination_text", waiTime, CURR_APP);
				String[] s1 = eo.getText(driver, "xpath", "pagination_text", CURR_APP).split(" ");
				if (s1[1].equalsIgnoreCase("1-50")) {
					this.addComment(" User is able  to navigate to" + "1st  page");
				} else {
					this.addComment("User is not ableto to navigate to  " + s[1] + "previous page");
					throw new KDTKeywordExecException("User is not able to to navigate to previous page of my cases page ");
				}
				
         ///////////////////////////////////////Verify filters///////////////////////////////////////////
				
				
				List<WebElement> filtersname = driver
						.findElements(By.xpath(gei.getProperty("filterIdeas_list", CURR_APP)));
								
				
				for(int i=1;i<filtersname.size();i++)
				{
					
					eo.waitForPageload(driver);
					
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "filterIdeas_listvalues", "{filters}", filtersideas[i], CURR_APP);
														
					eo.javaScriptClick(driver,"xpath", "filterIdea_button", CURR_APP);
					eo.waitForPageload(driver);
					eo.waitForWebElementVisible(driver, "XPATH", "filterIdea_button", waiTime, CURR_APP);
				    String expcategory = eo.getText(driver, "xpath", "category_text_filterideas", CURR_APP);
					if(expcategory.contains(filtersideas[i]))
					{
						this.addComment("User at <b>" + filtersideas[i] +"</b> page");
					}
					else {
						
						this.addComment("User at not at <b>" + filtersideas[i] +"</b> page");
						//throw new KDTKeywordExecException("User is not at filtered " + filtersideas[i] + "page");
					}
					
				}
				
	

			} catch (Exception e) {
				
				//int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
								
					this.addComment("Idea Landing page is  not validated ");
					throw new KDTKeywordExecException("Idea Landing page is  not validated ");
				}
				}
	}
		
	
	//////////////////// Create Idea/////////////////////////////////////////////
	
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>CreateIdea</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to CreateIdea
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>title(Mandatory): title</li>
	 * <li>ideabody(Mandatory): ideabody</li>
	 * <li>titleerror(Mandatory): titleerror</li>
	 * <li>ideabodyerror(Mandatory): ideabodyerror</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class CreateIdea extends Keyword {

		private String title;
		private String ideabody;
		private String titleerror;
		private String ideabodyerror;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			title = args.get("Title");
			ideabody = args.get("IdeaBody");
			titleerror = args.get("TitleError");		
			ideabodyerror = args.get("IdeaBodyError");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();
				eo.clickElement(driver, "xpath", "idea_link", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "submitanIdea_button", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "submitanIdea_button", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "newIdea_Text", waiTime, CURR_APP);
				this.addComment("Clicked on Submit An Idea button");
				String newideatext = eo.getText(driver, "xpath", "newIdea_Text", CURR_APP);
				if(newideatext.equalsIgnoreCase("New Idea"))
				{
					this.addComment("User at Create New Idea page");
				}
				else
				{
					this.addComment("User is not at New Idea page ");
					throw new KDTKeywordExecException("Create Idea button page is not working for Submit and idea");
				}
				eo.waitForPageload(driver);
				
				
				eo.clickElement(driver, "xpath", "savebutton_ideapage", CURR_APP);
				this.addComment("New Idea added successfully");
				eo.waitForPageload(driver);
				String titleerrotext = eo.getText(driver, "xpath", "title_error", CURR_APP);
				if(titleerrotext.equalsIgnoreCase(titleerror))
				{
					this.addComment("Error Message for blank entry of title is verified");
					
				}
				else
				{
					this.addComment("Error Message for blank entry of title is not  verified");
					throw new KDTKeywordExecException("Error Message for blank entry of title is  not verified");
				}
				String ideabodyerrortext = eo.getText(driver, "xpath", "ideabody_error", CURR_APP);
				if(ideabodyerrortext.equalsIgnoreCase(ideabodyerror))
				{
					this.addComment("Error Message for blank entry of body is verified");
					
				}
				else
				{
					this.addComment("Error Message for blank entry of body is not  verified");
					throw new KDTKeywordExecException("Error Message for blank entry of body is  not verified");
				}
				
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "cancelbutton_ideapage", CURR_APP);
				
				eo.waitForWebElementVisible(driver, "XPATH", "submitanIdea_button", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "submitanIdea_button", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "newIdea_Text", waiTime, CURR_APP);
				this.addComment("Clicked on Submit An Idea button");
				//eo.clickElement(driver, "xpath", "title_textfield", CURR_APP);
				eo.enterText(driver, "xpath", "title_textfield", title, CURR_APP);
				this.addComment("Title added successfully");
				eo.enterText(driver, "xpath", "ideabody_field", title, CURR_APP);	
				this.addComment("Idea added successfully");
				eo.javaScriptScrollToViewElement(driver, "xpath", "select_left", CURR_APP);
				eo.clickElement(driver, "xpath", "caterogy1", CURR_APP);
				this.addComment("Selected category successfully");
				eo.clickElement(driver, "xpath", "select_right", CURR_APP);
				eo.clickElement(driver, "xpath", "caterogy2", CURR_APP);
				eo.clickElement(driver, "xpath", "select_right", CURR_APP);
				eo.clickElement(driver, "xpath", "catergory_left", CURR_APP);				
				eo.clickElement(driver, "xpath", "select_left", CURR_APP);
				this.addComment("added category");
				eo.clickElement(driver, "xpath", "savebutton_ideapage", CURR_APP);
				this.addComment("New Idea added successfully");
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "add_comment_button", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "add_comment_button", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "add_comment_button", waiTime, CURR_APP);
				eo.waitForPageload(driver);
				
				/////////////////// Verify////////////////////////////////////
				eo.clickElement(driver, "xpath", "add_comment_button", CURR_APP);
				String commenterror = eo.getText(driver, "xpath", "comment_errormessage", CURR_APP);
				if (commenterror.equalsIgnoreCase("Comment is required."))
				{
					this.addComment("Error message for blank comment is verified");
				}
				else
				{
					this.addComment("Error Message for blank entry of comment is not  verified");
					throw new KDTKeywordExecException("Error Message for blank entry of comment is  not verified");
				}
				eo.enterText(driver, "xpath", "comment_box", "Testing_Dk", CURR_APP);
				eo.clickElement(driver, "xpath", "add_comment_button", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "add_comment_button", waiTime, CURR_APP);
				String expcomment  = eo.getText(driver, "xpath", "comment_text_entered", CURR_APP);
						if(expcomment.equalsIgnoreCase("Testing_Dk"))
						{
							this.addComment("Entered comment <b> " + expcomment + "</b>" + " is verified successfully");
						}
						else
						{
							this.addComment("Entered comment <b> " + expcomment + "</b>" + " is not matching");
							throw new KDTKeywordExecException("Add new Idea is not  validated");
						}
						eo.clickElement(driver, "xpath", "idea_link", CURR_APP);
						eo.waitForPageload(driver);
						eo.waitForPageload(driver);
						eo.waitForWebElementVisible(driver, "XPATH", "submitanIdea_button", waiTime, CURR_APP);
						
						String ideaname = eo.getText(driver, "xpath", "created_idea", CURR_APP);
						
						if(ideaname.equalsIgnoreCase(title))
						{
							this.addComment("Newly Added Idea<b> " + ideaname + "</b>"  + " is verified successfully");
						}
						
						else
						{
							this.addComment("Newly Added Idea<b> " + ideaname + "</b>"  + " is not  verified successfully");
							throw new KDTKeywordExecException("Add new Idea is not  validated");
						}
								
						

			} catch (Exception e) {

				// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

				this.addComment("Add new Idea is not  validated ");
				throw new KDTKeywordExecException("Add new Idea is not  validated");
			}
		}
	}
	
	
	//////////////////// ValidateForumLandingPage/////////////////////////////////////////////
	
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateForumLandingPage</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to CreateIdea
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>title(Mandatory): title</li>
	 * <li>ideabody(Mandatory): ideabody</li>
	 * <li>titleerror(Mandatory): titleerror</li>
	 * <li>ideabodyerror(Mandatory): ideabodyerror</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateForumLandingPage extends Keyword {

		private String columnnvalues[];
		private String titlevalues[];
		private String headerpost[];

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			columnnvalues = args.get("ColumnValues").split(",");
			titlevalues = args.get("TitleValues").split(":");
			headerpost = args.get("HeaderPost").split(",");
	

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();
				eo.clickElement(driver, "xpath", "forums_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "forums_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "forums_header_verify", CURR_APP)
						.equalsIgnoreCase("Have a question about Rubrik CDM? Choose a product area below!")) {
					this.addComment(
							"User is at" + "<b> Forums </b>" + "header and verified Forums header link successfully");
				} else {
					this.addComment("User is not at forums header");
					throw new KDTKeywordExecException("User is not at forums header");
				}
				
	/////////////////verify the column headers on forum landing page//////////////////////////////////			
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				
				List<WebElement> headerlist = driver.findElements(By.xpath(gei.getProperty("title_columnvalues", CURR_APP)));
		
				for(int i=0;i<headerlist.size();i++)
				{
					
					eo.waitForPageload(driver);
					if(headerlist.get(i).getText().contains(columnnvalues[i]))
					{
						this.addComment("Column values <b>" + headerlist.get(i).getText() +" is equal to " + columnnvalues[i]+"</b> is validated" );
					}
					
					else
					{
						this.addComment("Column values <b>" + headerlist.get(i).getText() +" is not equal to " + columnnvalues[i]+ " </b> is not  validated");
						throw new KDTKeywordExecException("Column values are not validated ");
					}
				}
				
				List<WebElement> postlinks = driver.findElements(By.xpath(gei.getProperty("postLink_list", CURR_APP)));
				//List<WebElement> postlinkslandingpage = driver.findElements(By.xpath(gei.getProperty("postlandingpage", CURR_APP)));
				
				for(int i=0;i<postlinks.size();i++)
				{
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "postlinks", "{post}", headerpost[i], CURR_APP);	
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					eo.waitForPageload(driver);
					//String authorlandingpage = eo.getText(driver, "xpath", "author_landingpage", CURR_APP);
					//String postlandingpage = eo.getText(driver, "xpath", "postlandingpage", CURR_APP);
					/*
					if(authorlandingpage.contains(headerpost[0]))
					
					{
						this.addComment("User at <b>" + authorlandingpage + "</b> page" );
					}
					
					
					else
					{
						this.addComment("User not at <b>" + authorlandingpage + "</b> page" );
						throw new KDTKeywordExecException("Post Landing Page not verified ");
					}*/
					
					eo.waitForWebElementVisible(driver, "XPATH", "backtoForum_button", waiTime, CURR_APP);
					eo.clickElement(driver, "xpath", "backtoForum_button", CURR_APP);
					 eo.waitForPageload(driver);
					
				}
				
						
				
				List<WebElement> listvalues = driver.findElements(By.xpath(gei.getProperty("title_list", CURR_APP)));	
				
				for(int i=0;i<listvalues.size();i++)
				{
				
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "title_values", "{title}", titlevalues[i], CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "backtoForum_button", waiTime, CURR_APP);
				String expected = eo.getText(driver, "xpath", "forum_landing", CURR_APP);
				 if(expected.equalsIgnoreCase(titlevalues[i]))
				 {
					 this.addComment("User at <b>"+ expected + "</b> page");
				 }
				
				 else
				 {
					 this.addComment("Forum values <b>" + expected + " </b> is not verified");
					throw new KDTKeywordExecException("Forum page is not ");
				 }
				eo.waitForPageload(driver);
				eo.javaScriptClick(driver, "xpath", "newpost_button", CURR_APP);
				// eo.clickElement(driver, "xptah", "newpost_button", CURR_APP);
				 eo.waitForWebElementVisible(driver, "XPATH", "cancel_button", waiTime, CURR_APP);
				 eo.clickElement(driver, "xpath", "cancel_button", CURR_APP);
				 eo.waitForPageload(driver);
				 eo.waitForWebElementVisible(driver, "XPATH", "backtoForum_button", waiTime, CURR_APP);
				 eo.clickElement(driver, "xpath", "backtoForum_button", CURR_APP);
				 eo.waitForPageload(driver);
				
				}
				
							

			} catch (Exception e) {

				// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

				this.addComment("Forum Landing Page is not validated");
				throw new KDTKeywordExecException("Forum Landing Page is not validated");
			}
		}
	}
	
	
	//////////////////// ValidateNewPost/////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateNewPost</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateNewPost </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * <li>title(Mandatory): title</li>
	 * <li>ideabody(Mandatory): ideabody</li>
	 * <li>titleerror(Mandatory): titleerror</li>
	 * <li>ideabodyerror(Mandatory): ideabodyerror</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateNewPost extends Keyword {

		

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();
				eo.clickElement(driver, "xpath", "forums_header", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "forums_header_verify", waiTime, CURR_APP);

				if (eo.getText(driver, "xpath", "forums_header_verify", CURR_APP)
						.equalsIgnoreCase("Have a question about Rubrik CDM? Choose a product area below!")) {
					this.addComment(
							"User is at" + "<b> Forums </b>" + "header and verified Forums header link successfully");
				} else {
					this.addComment("User is not at forums header");
					throw new KDTKeywordExecException("User is not at forums header");
				}

				
				eo.waitForPageload(driver);

				eo.clickElement(driver, "xpath", "archive_link", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForWebElementVisible(driver, "XPATH", "backtoForum_button", waiTime, CURR_APP);	
				eo.javaScriptClick(driver, "xpath", "newpost_button", CURR_APP);
				eo.waitForPageload(driver);
				
				eo.enterText(driver, "xpath", "title_textarea", "Automation Testing", CURR_APP);
				this.addComment("Added Title");
				
				
				eo.enterText(driver, "xpath", "decription_textarea", "Testing", CURR_APP);
			
				driver.findElement(By.xpath("//div[@class='ql-editor slds-rich-text-area__content slds-text-color_weak slds-grow']")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				eo.clickElement(driver, "xpath", "bold_button", CURR_APP);
				this.addComment("Marked the text as Bold");
				eo.clickElement(driver, "xpath", "italic_button", CURR_APP);
				this.addComment("Marked the text as Italic");
				eo.clickElement(driver, "xpath", "underline_button", CURR_APP);
				this.addComment("Marked the text as Underlined");
				eo.clickElement(driver, "xpath", "bullet_button", CURR_APP);
				this.addComment("Added the bullet points");
				/*eo.clickElement(driver, "xpath", "attachImage_link", CURR_APP);
				eo.waitForPageload(driver);		
				eo.waitForPageload(driver);	
				eo.waitForWebElementVisible(driver, "XPATH", "selectImage_text", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "selectImage_link", CURR_APP);
				
				eo.clickElement(driver, "xpath", "insert_button", CURR_APP);
				eo.waitForPageload(driver);
				this.addComment("Attached a Image to the Post");*/
				eo.clickElement(driver, "xpath", "attachfile_link", CURR_APP);
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "attachfile_checkbox", CURR_APP);
				eo.clickElement(driver, "xpath", "addbutton_selectfile", CURR_APP);
				this.addComment("Attached a File to the Post");
				eo.waitForWebElementVisible(driver, "XPATH", "ask_button", waiTime, CURR_APP);
				eo.javaScriptClick(driver,"xpath", "ask_button", CURR_APP);
				//eo.clickElement(driver, "xpath", "ask_button", CURR_APP);
		     	eo.waitForPageload(driver);
			    eo.waitForPageload(driver);
			
				eo.waitForWebElementVisible(driver, "XPATH", "post_headingText", 180, CURR_APP);
				
				if (eo.getText(driver, "xpath", "post_headingText", CURR_APP)
						.equalsIgnoreCase("Automation Testing")) {
					this.addComment(
							"Newly Craeted Post <b> "+ "Automation Testing" + "</b> is verified ");
				} else {
					this.addComment("Newly created post is not verofied");
					throw new KDTKeywordExecException("Newly created post is not verofied");
				}
				
				eo.javaScriptScrollToViewElement(driver, "xpath", "answer_textarea", CURR_APP);
				eo.clickElement(driver, "xpath", "answer_textarea", CURR_APP);
				eo.enterText(driver, "xpath", "decription_textarea", "This Post is created through Automation Test", CURR_APP);
				eo.clickElement(driver, "xpath", "answer_button", CURR_APP);
				eo.waitForPageload(driver);
				
				eo.clickElement(driver, "xpath", "reply_button", CURR_APP);
				
				eo.enterText(driver, "xpath", "reply_textarea", "Testing Done", CURR_APP);
				
				eo.clickElement(driver, "xpath", "reply_button_conferm", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "selectasbest_button", waiTime, CURR_APP);
			
				 eo.clickElement(driver, "xpath", "selectasbest_button", CURR_APP);
				 this.addComment("Post comment Selected as Best ");
				
				 eo.clickElement(driver, "xpath", "removeasbest_button", CURR_APP);
				 this.addComment("Post comment Removed as Best ");
				 
				 eo.javaScriptScrollToViewElement(driver, "xpath", "edit_button", CURR_APP);
				 eo.clickElement(driver, "xpath", "edit_button", CURR_APP);
				 eo.clickElement(driver, "xpath", "bookmark_button", CURR_APP);
				 this.addComment("Post is Bookmarked");
				 eo.clickElement(driver, "xpath", "edit_button", CURR_APP);
				 eo.clickElement(driver, "xpath", "removebookmark_button", CURR_APP);
				 this.addComment("Post  Bookmarked is Removed");
				 eo.waitForPageload(driver);
				 eo.clickElement(driver, "xpath", "edit_button", CURR_APP);
				 eo.clickElement(driver, "xpath", "delete_post_button", CURR_APP);
				 eo.clickElement(driver, "xpath", "cancel_button_delte", CURR_APP);
				 eo.waitForPageload(driver);
				 eo.clickElement(driver, "xpath", "edit_button", CURR_APP);
				 eo.clickElement(driver, "xpath", "delete_post_button", CURR_APP);
				 eo.clickElement(driver, "xpath", "delete_buttonPost", CURR_APP);
				 this.addComment("Deleted ");
				 eo.waitForPageload(driver);
				 
				 eo.clickElement(driver, "xpath", "backtoForum_button", CURR_APP);
				 eo.waitForPageload(driver);

			} catch (Exception e) {

				// int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

				this.addComment("Create new Forum Landing Page is not validated");
				throw new KDTKeywordExecException("Create new Forum Landing Page is not validated");
			}
		}
	}
	
///////////////////////////////////////////////	OpenContactInSalesforce///////////////////////////////
	

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpenContactInSalesforce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to OpenContactInSalesforce
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class OpenContactInSalesforce extends Keyword {

		private String contactname;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			contactname = args.get("ContactName");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			// int waiTime =
			// Integer.parseInt(gei.getProperty("waitTime",CURR_APP));
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				eo.enterText(driver, "xpath", "search_homepage", contactname, CURR_APP);
				this.addComment("Contact number is added in the search field");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);

				

				eo.waitForPageload(driver);
				this.addComment("Contact is displayed");
				eo.javaScriptScrollToViewElement(driver, "xpath", "contct_searched_list", CURR_APP);
				eo.javaScriptClick(driver, "xpath", "contct_searched_list", CURR_APP);
				//eo.waitForWebElementVisible(driver, "ID", "username", waiTime, CURR_APP);

				eo.waitForPopUp(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "contactname_searched", "{contact}", contactname,
						CURR_APP);
				
				eo.waitForPageload(driver);
				accountname = eo.getText(driver, "xpath", "accountname_text", CURR_APP);
				eo.clickElement(driver, "xpath", "manageExternalUser_dropdown", CURR_APP);
				eo.clickElement(driver, "xpath", "login_to_supportportal", CURR_APP);

			} catch (Exception e) {
				this.addComment("Open Contact is validated");
				throw new KDTKeywordExecException("Open Contact is validated");
			}
		}
	}

	
///////////////////////////////////////////////	ValidateUpdateUserAdminPanel///////////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateUpdateUserAdminPanel</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateUpdateUserAdminPanel
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateUpdateUserAdminPanel extends Keyword {

		//private String contactname;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			//contactname = args.get("ContactName");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			
			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				eo.clickElement(driver, "xpath", "userprofile_link", CURR_APP);
				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "AdminPannel_link", CURR_APP);
				this.addComment("Clicked on Admin Panel");
				eo.waitForPageload(driver);

			String actualaccount = eo.getText(driver, "xpath", "supportportal_account", CURR_APP);
			
			if(actualaccount.equalsIgnoreCase(accountname))
			{
				this.addComment("Account validated for the login contact");
			}
			else
			{
				this.addComment("Login account is not validated ");
				throw new KDTKeywordExecException("Login account is not validated");
			}
			
			eo.clickElement(driver, "xpath", "edituser_adminpannel", CURR_APP);
			eo.waitForWebElementVisible(driver, "XPATH", "update_user", waiTime, CURR_APP);
			eo.clearData(driver, "xpath", "edit_title", CURR_APP);
			eo.enterText(driver, "xpath", "edit_title", "Mr.", CURR_APP);
			this.addComment("Entered title");
			eo.clearData(driver, "xpath", "edit_phone", CURR_APP);
			eo.enterText(driver, "xpath", "edit_phone", "123456789", CURR_APP);
			this.addComment("Entered Phone Number");
			eo.clickElement(driver, "xpath", "update_user", CURR_APP);
			eo.waitForPageload(driver);
			eo.clickElement(driver, "xpath", "close_updatepopup", CURR_APP);
			eo.waitForWebElementVisible(driver, "XPATH", "edituser_adminpannel", waiTime, CURR_APP);
			eo.waitForPageload(driver);
			String actualtitle = eo.getText(driver, "xpath", "homepage_title", CURR_APP);
			String actualphone = eo.getText(driver, "xpath", "homepage_phone", CURR_APP);
			
			if(actualtitle.equalsIgnoreCase("Mr."))
			{
				this.addComment("Title updated as <b>" +actualtitle+ "<>/b for user is validate");
			}
			
			if(actualphone.equalsIgnoreCase("123456789"))
			{
				this.addComment("Phone updated as <b>" +actualphone+ "<>/b for user is validate");
			}
			
			else
			{
				this.addComment("Title and phone added for user is not validated ");
				throw new KDTKeywordExecException("Title and phone added for user is not validated");
			}

			} catch (Exception e) {
				this.addComment("User is not able to update Contact User");
				throw new KDTKeywordExecException("User is not able to update Contact User");
			}
		}
	}
	
	/////////////////////////////////////////////// ValidateAddContactAdminPanel///////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateAddContactAdminPanel</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to
	 * ValidateAddContactAdminPanel </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateAddContactAdminPanel extends Keyword {

		 private String columnnames[];
		//private String columnvalues[];
		 
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			columnnames = args.get("ColumnNamesContact").split(",");
			//columnvalues =args.get("CloumnValues").split(",");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				
				eo.clickElement(driver, "xpath", "addcontact_adminpanel", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "saveuseraddcontct_button", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "addnewrow_button", CURR_APP);
				this.addComment("Added a new row to add contact");
				eo.clickElement(driver, "xpath", "deleterow_button", CURR_APP);
				this.addComment("Deleted the old row to add contact");
				
				
				eo.clickElement(driver, "xpath", "activatecontact_checkbox", CURR_APP);		
				this.addComment("Unchecked ActivateContact Checkbox");
				eo.clickElement(driver, "xpath", "saveuseraddcontct_button", CURR_APP);
				
				List<WebElement> columnnamescount = driver
						.findElements(By.xpath(gei.getProperty("errormessagecontact_list", CURR_APP)));

				for (int i = 0; i < columnnamescount.size(); i++) {
					String errormessage = eo.getText(driver, "xpath", "errormessage", CURR_APP);
					if (errormessage.equalsIgnoreCase("Please Complete This Field"))
					{
						this.addComment("Error Message <b>" + errormessage + "</b> verified for <b>" + columnnames[i] + " </b>column ");
					}
								
				else 
				{
					this.addComment("Inline error message is not validated  ");
					throw new KDTKeywordExecException("Inline error message is not validated");
				}
				}
				
				eo.clickElement(driver, "xpath", "cancelbutton_addcontact", CURR_APP);
				
				eo.waitForWebElementVisible(driver, "XPATH", "addcontact_adminpanel", waiTime, CURR_APP);

			} catch (Exception e) {
				this.addComment("User is not able to update Contact User");
				throw new KDTKeywordExecException("User is not able to update Contact User");
			}
		}
	}

	
	/////////////////////////////////////////////// ValidateProactiveContact///////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateProactiveContact</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to
	 * ValidateProactiveContact </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateProactiveContact extends Keyword {

		 private String timezone;
		 private String availabledays[];
		 private String selecteddays[];
		 public String emailaddress;
		 public String contactmobile;
		 public String streettext;
		 
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			timezone = args.get("TimeZone");
			availabledays = args.get("AvailableDays").split(",");
			selecteddays = args.get("SelectedDays").split(",");
			emailaddress = args.get("EmailAddress");
			contactmobile = args.get("ContactMobile");
			streettext = args.get("StreetText");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "proactivecontact", CURR_APP);
				this.addComment("Clicked on Proactive Contact");
				//eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button", waiTime, CURR_APP);
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver, "xpath", "updatebusinesshours_button", CURR_APP);
				eo.clickElement(driver, "xpath", "updatebusinesshours_button", CURR_APP);
				
				String exptext = eo.getText(driver, "xpath", "updatebusinesshour_text",CURR_APP );
				
				if(exptext.equalsIgnoreCase("Update your business hours."))
				{
					this.addComment("Update Businesshours window is displayed");
				}
				else
				{
					this.addComment("User not at Businesshours window");
					throw new KDTKeywordExecException("User not at Businesshours window");
				}
				
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "timezone_dropdown", "{timezone}", timezone, CURR_APP);	
			eo.waitForPageload(driver);
			this.addComment("Time zone selected <b>" + timezone + "</b>");
				
				for (int i=0;i<availabledays.length;i++)
				{
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "availabledays_select", "{days}", availabledays[i], CURR_APP);
			eo.clickElement(driver, "xpath", "select_right", CURR_APP);
			this.addComment("Selected available days <b>" + availabledays[i] + "</b>");
			
				}
				
				for (int i=0;i<selecteddays.length;i++)
				{
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "availabledays_select", "{days}", selecteddays[i], CURR_APP);
			eo.clickElement(driver, "xpath", "select_left", CURR_APP);
			this.addComment("Removed Selected  days <b>" + selecteddays[i] + "</b>");
			
				}
				
				eo.clickElement(driver, "xpath", "update_button", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button", waiTime, CURR_APP);
				
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				
				this.addComment("Updated Business hours");
				
				eo.javaScriptScrollToViewElement(driver, "xpath", "selectcontact_businesshrs", CURR_APP);
				eo.clickElement(driver, "xpath", "selectcontact_businesshrs", CURR_APP);
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver, "xpath", "contact_businesshrs", CURR_APP);
				eo.clickElement(driver, "xpath", "contact_businesshrs", CURR_APP);
                eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button", waiTime, CURR_APP);
				
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				this.addComment("Updated Contact for business hours");
				
				eo.clickElement(driver, "xpath", "selectcontact_nonbusinesshrs", CURR_APP);
				eo.waitForPageload(driver);		
				eo.clickElement(driver, "xpath", "contact_nonbusinesshrs", CURR_APP);
                eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button", waiTime, CURR_APP);
				
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				this.addComment("Updated Contact for non business hours");
				
				
				////////////////////Address///////////////////////
				
				eo.clickElement(driver, "xpath", "updateaddress_button", CURR_APP);
				eo.waitForPageload(driver);
				eo.clearData(driver, "xpath", "email_field", CURR_APP);
				
				String experror1 = eo.getText(driver, "xpath", "inlineerror", CURR_APP);
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				
				else
				{
					this.addComment("Email field is not verified");
					throw new KDTKeywordExecException("Email field is not verified");
				}
				eo.enterText(driver, "xpath", "email_field", emailaddress, CURR_APP);
				this.addComment("Entered Email Id");
				eo.clearData(driver, "xpath", "contactfield", CURR_APP);
				
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				else
				{
					this.addComment("Phone field is not verified");
					throw new KDTKeywordExecException("phone field is not verified");
				}
				
				
				eo.enterText(driver, "xpath", "contactfield", contactmobile, CURR_APP);
				this.addComment("Entered phone");
				
				eo.clearData(driver, "xpath", "street_textarea", CURR_APP);
				
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				else
				{
					this.addComment("Street field is not verified");
					throw new KDTKeywordExecException("Street field is not verified");
				}
				
				
				eo.enterText(driver, "xpath", "street_textarea", streettext, CURR_APP);
				this.addComment("Entered street");
				
				
				eo.clearData(driver, "xpath", "cityfield_textarea", CURR_APP);
				
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				else
				{
					this.addComment("City field is not verified");
					throw new KDTKeywordExecException("City field is not verified");
				}
				
				
				eo.enterText(driver, "xpath", "cityfield_textarea", "Bangalore", CURR_APP);
				this.addComment("Entered street");
				
				eo.clickElement(driver, "xpath", "update_button_Address", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
		    	eo.clickElement(driver, "xpath", "configure_clusterLevel", CURR_APP);
		    	eo.waitForPageload(driver);
				eo.waitForPageload(driver);
		    	this.addComment("Clicked on Configure Cluster Level Defaults");
		    	
		    	String clustertext = eo.getText(driver, "xpath", "clucter_textverify", CURR_APP);
		    	
		    	if(clustertext.equalsIgnoreCase("Clusters"))
		    	{
		    		this.addComment("User at Cluster Page to Update Proactive contact");
		    	}
			
				
			} catch (Exception e) {
				this.addComment("User is not able to update Contact User");
				throw new KDTKeywordExecException("User is not able to update Contact User");
			}
		}
	}
	
	
	/////////////////////////////////////////////// ValidateProactiveContactAtCluster///////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateProactiveContactAtCluster</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to
	 * ValidateProactiveContact </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateProactiveContactAtCluster extends Keyword {

		 private String timezone;
		 private String availabledays[];
		 private String selecteddays[];
		 public String emailaddress;
		 public String contactmobile;
		 public String streettext;
		 
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			timezone = args.get("TimeZone");
			availabledays = args.get("AvailableDays").split(",");
			selecteddays = args.get("SelectedDays").split(",");
			emailaddress = args.get("EmailAddress");
			contactmobile = args.get("ContactMobile");
			streettext = args.get("StreetText");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

			

				eo.waitForPageload(driver);
				eo.clickElement(driver, "xpath", "proactive_clusterevel", CURR_APP);
				//eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button", waiTime, CURR_APP);
				eo.clickElement(driver, "xpath", "updatebusinesshours_button_cluster", CURR_APP);
				String exptext = eo.getText(driver, "xpath", "updatebusinesshour_text",CURR_APP );
				
				if(exptext.equalsIgnoreCase("Update your business hours."))
				{
					this.addComment("Update Businesshours window is displayed");
				}
				else
				{
					this.addComment("User not at Businesshours window");
					throw new KDTKeywordExecException("User not at Businesshours window");
				}
				
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "timezone_dropdown", "{timezone}", timezone, CURR_APP);	
			eo.waitForPageload(driver);
			this.addComment("Time zone selected <b>" + timezone + "</b>");
				
				for (int i=0;i<availabledays.length;i++)
				{
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "availabledays_select", "{days}", availabledays[i], CURR_APP);
			eo.clickElement(driver, "xpath", "select_right", CURR_APP);
			this.addComment("Selected available days <b>" + availabledays[i] + "</b>");
			
				}
				
				for (int i=0;i<selecteddays.length;i++)
				{
			eo.clickElementAfterReplacingKeyValue(driver, "xpath", "availabledays_select", "{days}", selecteddays[i], CURR_APP);
			eo.clickElement(driver, "xpath", "select_left", CURR_APP);
			this.addComment("Removed Selected  days <b>" + selecteddays[i] + "</b>");
			
				}
				
				eo.clickElement(driver, "xpath", "update_button", CURR_APP);
				eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button_cluster", waiTime, CURR_APP);
				
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				
				this.addComment("Updated Business hours");
				eo.javaScriptScrollToViewElement(driver, "xpath", "selectcontact_businesshrs_cluster", CURR_APP);
				eo.clickElement(driver, "xpath", "selectcontact_businesshrs_cluster", CURR_APP);
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver, "xpath", "contact_businesshrs", CURR_APP);
				eo.clickElement(driver, "xpath", "contact_businesshrs", CURR_APP);
                eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button_cluster", waiTime, CURR_APP);
				
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				this.addComment("Updated Contact for business hours");
				
				eo.clickElement(driver, "xpath", "selectcontact_nonbusinesshrs_cluster", CURR_APP);
				eo.waitForPageload(driver);		
				eo.clickElement(driver, "xpath", "contact_nonbusinesshrs", CURR_APP);
                eo.waitForWebElementVisible(driver, "XPATH", "updatebusinesshours_button_cluster", waiTime, CURR_APP);
				
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				this.addComment("Updated Contact for non business hours");
				
				
				////////////////////Address///////////////////////
				
				eo.clickElement(driver, "xpath", "updateaddress_button_cluster", CURR_APP);
				eo.waitForPageload(driver);
				eo.clearData(driver, "xpath", "email_field", CURR_APP);
				
				String experror1 = eo.getText(driver, "xpath", "inlineerror", CURR_APP);
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				
				else
				{
					this.addComment("Email field is not verified");
					throw new KDTKeywordExecException("Email field is not verified");
				}
				eo.enterText(driver, "xpath", "email_field", emailaddress, CURR_APP);
				this.addComment("Entered Email Id");
				eo.clearData(driver, "xpath", "contactfield", CURR_APP);
				
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				else
				{
					this.addComment("Phone field is not verified");
					throw new KDTKeywordExecException("phone field is not verified");
				}
				
				
				eo.enterText(driver, "xpath", "contactfield", contactmobile, CURR_APP);
				this.addComment("Entered phone");
				
				eo.clearData(driver, "xpath", "street_textarea", CURR_APP);
				
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				else
				{
					this.addComment("Street field is not verified");
					throw new KDTKeywordExecException("Street field is not verified");
				}
				
				
				eo.enterText(driver, "xpath", "street_textarea", streettext, CURR_APP);
				this.addComment("Entered street");
				
				
				eo.clearData(driver, "xpath", "cityfield_textarea", CURR_APP);
				
				if(experror1.equalsIgnoreCase("Complete this field."))
				{
					this.addComment("Inline error message is verified");
				}
				else
				{
					this.addComment("City field is not verified");
					throw new KDTKeywordExecException("City field is not verified");
				}
				
				
				eo.enterText(driver, "xpath", "cityfield_textarea", "Bangalore", CURR_APP);
				this.addComment("Entered street");
				
				eo.clickElement(driver, "xpath", "update_button_Address", CURR_APP);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
				eo.waitForPageload(driver);
			
				
			} catch (Exception e) {
				this.addComment("User is not able to update Contact User");
				throw new KDTKeywordExecException("User is not able to update Contact User");
			}
		}
	}
	
///////////////////////////////////////////////	OpenAccounttInSalesforce///////////////////////////////
	
	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpenAccounttInSalesforce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to OpenAccounttInSalesforce
	 * </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class OpenAccounttInSalesforce extends Keyword {

		private String accountname;
		private String emailaddress;
		public String contactmobile;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			accountname = args.get("AccountName");
			emailaddress = args.get("EmailAddress");
			contactmobile = args.get("ContactMobile");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			// int waiTime =
			// Integer.parseInt(gei.getProperty("waitTime",CURR_APP));
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				eo.enterText(driver, "xpath", "search_homepage", accountname, CURR_APP);
				this.addComment("Account number is added in the search field");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);

				eo.waitForPageload(driver);
				this.addComment("Account is displayed");
				eo.javaScriptScrollToViewElement(driver, "xpath", "accountsearch_onlist", CURR_APP);
				eo.javaScriptClick(driver, "xpath", "accountsearch_onlist", CURR_APP);

				eo.waitForPopUp(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "searched_account", "{account}", accountname,
						CURR_APP);

				eo.waitForPageload(driver);

				eo.javaScriptScrollToViewElement(driver, "xpath", "proactivecontact_accountdetail", CURR_APP);

				String rmaemail = eo.getText(driver, "xpath", "shippingaddress_email", CURR_APP);

				if (rmaemail.equalsIgnoreCase(emailaddress)) {
					this.addComment("ShippingRMA email id <b>" + rmaemail + "</b> is verified");
				} else {
					this.addComment("ShippingRMA email is not verified");
					throw new KDTKeywordExecException("ShippingRMA email is not verified");
				}

				String rmaphone = eo.getText(driver, "xpath", "shippingaddress_phone", CURR_APP);

				if (rmaemail.equalsIgnoreCase(emailaddress)) {
					this.addComment("ShippingRMA phone id <b>" + rmaphone + "</b> is verified");
				}

				else {
					this.addComment("ShippingRMA phone is not verified");
					throw new KDTKeywordExecException("ShippingRMA phone is not verified");
				}

			} catch (Exception e) {
				this.addComment("Open Account and validate RAM details is not  validated");
				throw new KDTKeywordExecException("Open Account and validate RAM details is not  validated");
			}
		}
	}

	/////////////////////////////////////////////// OpenNewCaseInSalesforce///////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>OpenCaseInSalesforce</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to OpenCaseInSalesforce </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class OpenNewCaseInSalesforce extends Keyword {

		private String newcasenumber;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			newcasenumber = args.get("NewCaseNumber");

		}

		@Override
		public void exec() throws KDTKeywordExecException {

			// int waiTime =
			// Integer.parseInt(gei.getProperty("waitTime",CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				eo.enterText(driver, "xpath", "search_homepage", "00095259", CURR_APP);
				this.addComment("Case number is added in the search field");
				eo.clickElement(driver, "xpath", "search_button", CURR_APP);

				eo.waitForPopUp(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "casenumber_searched", "{case}", "00095259",
						CURR_APP);

				eo.waitForPageload(driver);

				// eo.javaScriptScrollToViewElement(driver, "xpath", "caseSpecificsField",
				// CURR_APP);

				// eo.javaScriptDoubleClick(driver, "xpath", "Fields", CURR_APP);

			} catch (Exception e) {
				this.addComment("user is not able to fetch the case number");
				throw new KDTKeywordExecException("User is not able to fetch the case number");
			}
		}
	}

	////////////////////////////////// ValidateCaseClose/////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateCaseClose</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateCaseClose </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateCaseClose extends Keyword {

		private String resolutiontype[];
		private String errormessagedisplayed;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			resolutiontype = args.get("ResolutionType").split(",");
			errormessagedisplayed = args.get("ErrorMessage");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();

				eo.waitForPageload(driver);

				eo.javaScriptScrollToViewElement(driver, "xpath", "closecase_button", CURR_APP);
				eo.clickElement(driver, "xpath", "closecase_button", CURR_APP);
				this.addComment("Clicked on closed case");
				eo.waitForPageload(driver);
				eo.clearData(driver, "xpath", "permamentfixdate", CURR_APP);
				// eo.clickElementAfterReplacingKeyValue(driver, "xpath", "resolution_type",
				// "{type}", "--None--", CURR_APP);

				eo.clickElement(driver, "xpath", "savebutton_caseclose", CURR_APP);

				List<WebElement> errormessage_closecase = driver
						.findElements(By.xpath(gei.getProperty("errormessage_closecaselist", CURR_APP)));

				for (int i = 0; i < errormessage_closecase.size(); i++) {
					String errormessage = eo.getText(driver, "xpath", "err_message", CURR_APP);
					if (errormessage.equalsIgnoreCase("Error: You must enter a value")) {
						this.addComment("Error Message <b>" + errormessage + "</b>");
					}

					else {
						this.addComment("Inline error message is not validated  ");
						throw new KDTKeywordExecException("Inline error message is not validated");
					}
				}

				eo.waitForPageload(driver);
				// eo.clearData(driver, "xpath", "permamentfixdate", CURR_APP);
				eo.clickElement(driver, "xpath", "permamentfixdate", CURR_APP);
				eo.clickElement(driver, "xpath", "today_link_calender", CURR_APP);
				this.addComment("Selected Permanent Fix date from calender");

				// eo.waitForPageload(driver);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "priority_value", "{priority}", "None",
						CURR_APP);
				eo.clickElement(driver, "xpath", "savebutton_caseclose", CURR_APP);

				String priorityerror = eo.getText(driver, "xpath", "priority_errormessage", CURR_APP);

				if (priorityerror.equalsIgnoreCase(
						"Error: Please select an appropriate priority (P1 - Critical service impact, P2 - Major service impact, P3 - Standard request, P4 - Documentation or low-priority request).")) {
					this.addComment("error message for priority is verified");
				}

				else {
					this.addComment("Inline error message is not validated  ");
					throw new KDTKeywordExecException("Inline error message is not validated");
				}

				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "priority_value", "{priority}", "P4 - Low",
						CURR_APP);
				eo.javaScriptScrollToViewElement(driver, "xpath", "KCS_article", CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "KVS_articlevalue", "{KCSvalue}", "--None--",
						CURR_APP);

				eo.clearData(driver, "xpath", "KCS_comment", CURR_APP);

				for (int i = 0; i <= resolutiontype.length - 1; i++) {
					eo.clickElementAfterReplacingKeyValue(driver, "xpath", "resolution_type", "{type}",
							resolutiontype[i], CURR_APP);

					eo.clickElement(driver, "xpath", "savebutton_caseclose", CURR_APP);

					//String error = driver.findElement(By.xpath("//td[@class='messageCell']")).getText();
				String error=	eo.getText(driver, "xpath", "KCS_errormessage", CURR_APP);

					if (error.equalsIgnoreCase(errormessagedisplayed)) {
						this.addComment(
								"KCS article field is required for <b>" + resolutiontype[i] + "</b> Resolution type");
					} else {
						this.addComment(
								"KCS field is not required for Resolution type<b> " + resolutiontype[i] + "</b>");
						throw new KDTKeywordExecException(
								"KCS field is not required for Resolution type<b> " + resolutiontype[i] + "</b>");
					}
				}

				eo.javaScriptScrollToViewElement(driver, "xpath", "KCS_article", CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "KVS_articlevalue", "{KCSvalue}",
						"No Article required", CURR_APP);
				eo.enterText(driver, "xpath", "KCS_comment", "Testing", CURR_APP);

				eo.clickElement(driver, "xpath", "savebutton_caseclose", CURR_APP);

			} catch (Exception e) {
				this.addComment("Case close is not validate");
				throw new KDTKeywordExecException("Case close is not validate");
			}
		}
	}

	////////////////////////////////// ValidateAttachArticle/////////////////////////////////////////////////////

	/**
	 * <div align="left">
	 * <p>
	 * <b><i>Keyword Name:</i>ValidateCaseClose</b>
	 * </p>
	 * <p>
	 * <b><i>Description:</i></b> This Keyword is used to ValidateCaseClose </i>
	 * </P>
	 * <b><i>Dependencies</i></b>:
	 * <ul>
	 * <li>Launch</li>
	 * </ul>
	 * <b><i>Arguments</i></b>:
	 * <ul>
	 * <li>Url(Mandatory): URL of Application</li>
	 * </ul>
	 * <p>
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class ValidateAttachArticle extends Keyword {

		private String errormessagedisplayed;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			errormessagedisplayed = args.get("ErrorMessage");
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
			try {
				WebDriver driver = context.getWebDriver();

				/*
				 * if(eo.isDisplayed(driver, "xpath", "switchtolightning_button", CURR_APP)) {
				 * eo.clickElement(driver, "xpath", "switchtolightning_button", CURR_APP); }
				 */
				eo.waitForPopUp(driver);
				eo.enterText(driver, "xpath", "searchbar", "00095259", CURR_APP);

				driver.findElement(By.xpath("//div[@class='uiInput uiAutocomplete uiInput--default']//input"))
						.sendKeys(Keys.ENTER);

				eo.clickElement(driver, "xpath", "casenumber_link", CURR_APP);
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver, "xpath", "case_description", CURR_APP);
				eo.clickElement(driver, "xpath", "article_section", CURR_APP);
				this.addComment("clicked on Artcle Section");
				eo.enterText(driver, "xpath", "findartcile_searchbar", "Demo", CURR_APP);
				eo.clickElement(driver, "xpath", "find_article", CURR_APP);
				this.addComment("Search Result for Articles displayed");
				eo.waitForPopUp(driver);
				// eo.clickElement(driver, "xpath", "existingarticle_link", CURR_APP);
				eo.javaScriptScrollToViewElement(driver, "xpath", "articletoattach_link", CURR_APP);

				String articlenumbertoattach = eo.getText(driver, "xpath", "artcle_numbertoattach", CURR_APP);
				eo.waitForPageload(driver);

				eo.clickElement(driver, "xpath", "attachtocase_button", CURR_APP);

				eo.wait(5);
				Alert alert = driver.switchTo().alert();
				alert.accept();

				/*
				 * try { Robot robot = new Robot(); robot.keyPress(KeyEvent.VK_ENTER);
				 * robot.keyRelease(KeyEvent.VK_ENTER);
				 * 
				 * } catch (Exception e) {
				 * 
				 * addComment(" ConfPop up is not handeled"); throw new
				 * KDTKeywordExecException("Enter key  on Popup is not working with Action class"
				 * , e); }
				 */

				eo.waitForPageload(driver);

				this.addComment("Article attached successfully");
				eo.waitForPageload(driver);
				eo.javaScriptScrollToViewElement(driver, "xpath", "case_description", CURR_APP);

				eo.clickElement(driver, "xpath", "existingarticle_link", CURR_APP);

				String articlenumberattached = eo.getText(driver, "xpath", "articlenumber_attached", CURR_APP);

				if (articlenumbertoattach.equalsIgnoreCase(articlenumberattached)) {
					this.addComment("Searched article is attached to case and verified");
				}

				else {
					this.addComment("Searched article is not  attached to case");
					throw new KDTKeywordExecException("Searched article is not  attached to case");
				}

				eo.clickElement(driver, "xpath", "existingarticle_link", CURR_APP);

				eo.enterText(driver, "xpath", "findartcile_searchbar", articlenumberattached, CURR_APP);
				eo.clickElement(driver, "xpath", "find_article", CURR_APP);

				String errormessagearticle = eo.getText(driver, "xpath", "errormessage_searcharticle", CURR_APP);

				if (errormessagearticle.equalsIgnoreCase(
						"Either this article is already attached or there is no article found w.r.t search.")) {
					this.addComment("Article is already attached ");
				}

				eo.clickElement(driver, "xpath", "existingarticle_link", CURR_APP);
				eo.clickElementAfterReplacingKeyValue(driver, "xpath", "remove_addedarticle", "{articlenumber}",
						articlenumberattached, CURR_APP);

				Alert alert1 = driver.switchTo().alert();
				alert1.accept();

				this.addComment("Added article <b> " + articlenumberattached + "</b> is removed successfully");

			} catch (Exception e) {
				this.addComment("Case close is not validate");
				throw new KDTKeywordExecException("Attach article is not verified");
			}
		}
	}


	
	/////////////////////////////////////////////////////////////LoginSalesForce/////////////////////
	public static class LoginSalesForce extends Keyword {
		
		private String url;
		private String username;
		private String password;
		private String browser="";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			url = args.get("Url");
			username = args.get("Username");
			password = args.get("Password");
			 if(hasArgs("Browser")){
				browser = args.get("Browser");
			 }
		} 

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub

			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));
               if(!(browser.isEmpty())) {
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
			addComment("Successfully click on login button");
			eo.waitForPageload(driver);
			

			eo.waitForWebElementVisible(driver, "XPATH", "home_tab", waiTime, CURR_APP);
			addComment("Successfully verified the home tab is dispalyed");

		}
	}
	


	//////////////////////////////////////////////////////////////Logout Sales Force//////////////////////////


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
	 * <b><i>Version:</i></b> 0.1 <b><i> Auther:Deepak</i></b>
	 * </p>
	 * </div>
	 */

	public static class LogoutSalesforce extends Keyword {

		@Override
		public void exec() throws KDTKeywordExecException {
			// TODO Auto-generated method stub
			int waiTime = Integer.parseInt(gei.getProperty("waitTime", CURR_APP));

			try {
				WebDriver driver = context.getWebDriver();

				eo.clickElement(driver, "xpath", "userMenu", CURR_APP);
				eo.clickElement(driver, "xpath", "logoutButton", CURR_APP);
				eo.waitForWebElementVisible(driver, "id", "userNameVerify", waiTime, CURR_APP);
			}

			catch (Exception e) {
				// TODO Auto-generated catch block

				this.addComment("Failed to logout from salesforce");
				throw new KDTKeywordExecException("Failed to logout from salesforce", e);
			}
		}
	}
	
	


}
