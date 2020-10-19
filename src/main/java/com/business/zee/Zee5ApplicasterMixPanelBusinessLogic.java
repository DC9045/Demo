package com.business.zee;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.deviceDetails.DeviceDetails;
import com.driverInstance.CommandBase;
import com.emailReport.GmailInbox;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;
import com.zee5.ApplicasterPages.*;
import com.zee5.PWAPages.PWAHamburgerMenuPage;
import com.zee5.PWAPages.PWALoginPage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;

public class Zee5ApplicasterMixPanelBusinessLogic extends Utilities {

	public Zee5ApplicasterMixPanelBusinessLogic(String Application) {
		new CommandBase(Application);
		init();
	}
	
	private int timeout;

	/** Retry Count */
	private int retryCount;
	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */
//	final static Logger logger = Logger.getLogger("rootLogger");
	static LoggingUtils logger = new LoggingUtils();

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;

	/** The Android driver. */
	public IOSDriver<WebElement> iOSDriver;
	
	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public int getRetryCount() {
		return retryCount;
	}

	@Override
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	GmailInbox gmail = new GmailInbox();

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
//		logger.info("Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	/**
	 * Function to Relaunch the driver
	 */
	public void relaunch(boolean clearData) throws Exception {
		HeaderChildNode("Relaunch the app");
		logger.info("Relaunching the application");
		extent.extentLogger("Relaunch", "Relaunching the application");
		waitTime(10000);
		getDriver().quit();
		relaunch = clearData;
		new Zee5ApplicasterBusinessLogic("zee");
	    accessDeviceLocationPopUp("Allow", userType);
		navigateToIntroScreen_DisplaylangScreen();
		ZeeApplicasterLogin(userType);
	}

	/**
	 * Function to quit the driver
	 */
	public void tearDown() {
		getDriver().quit();
	}
	// Retrieve the Mobile Device Name
		String getOEMName = DeviceDetails.OEM;

		/**
		 * Function to access the device location
		 */
		public void accessDeviceLocationPopUp(String permission, String userType) throws Exception {
			extent.HeaderChildNode("Access Device Location PopUp");
			extent.extentLogger("User Type", "UserType : " + userType);
			logger.info("UserType : " + userType);
			System.out.println("Access Device Location PopUp");

			//click(AMDOnboardingScreen.objContinueBtnInDebugBuild, "Continue button");
			if (checkElementExist(AMDOnboardingScreen.objAllowBtn)) {
				Wait(5000);
				verifyElementPresent(AMDOnboardingScreen.objAllowBtn, "Allow button");
				verifyElementPresent(AMDOnboardingScreen.objDenyBtn, "Deny button");

				if (permission.equalsIgnoreCase("Allow")) {
					click(AMDOnboardingScreen.objAllowBtn, "Allow button");
				} else {
					click(AMDOnboardingScreen.objDenyBtn, "Deny button");
				}
			}
		}
		
		/**
		 * Functon to navigate to Intro screen
		 */
		public void navigateToIntroScreen_DisplaylangScreen() throws Exception {
			extent.HeaderChildNode("Navigation to Intro Screen");
			//click(AMDOnboardingScreen.objContinueBtnInCountryPopUp, "Continue button (Country_Screen)");
			click(AMDOnboardingScreen.objDiplay_ContinueBtn, "Continue button (Display-LanguageScreen)");
			click(AMDOnboardingScreen.objContent_ContinueBtn, "Continue button (Content-LanguageScreen)");
			verifyElementPresent(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		}
		
		/**
		 * Function to Login to Zee5
		 */
		public void ZeeApplicasterLogin(String LoginMethod) throws Exception {
			extent.HeaderChildNode("Login Functionality");

			String UserType = getParameterFromXML("userType");
			if (UserType.equals("Guest")) {
				extent.extentLogger("userType", "UserType : Guest");
			}

			verifyElementPresentAndClick(AMDLoginScreen.objLoginLnk, "Login link");
			waitTime(3000);

			switch (LoginMethod) {
			case "Guest":
				extent.HeaderChildNode("Guest User");
				extent.extentLogger("Accessing the application as Guest user", "Accessing the application as Guest user");
				waitTime(1000);
				hideKeyboard();
				verifyElementPresentAndClick(AMDLoginScreen.objLoginLnk, "Skip link");
				waitTime(3000);
				break;

			case "NonSubscribedUser":
				extent.HeaderChildNode("Login as NonSubscribed User");

				String Username = getParameterFromXML("NonsubscribedUserName");
				String Password = getParameterFromXML("NonsubscribedPassword");

				verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
				type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
				verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
				verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password Field");
				type(AMDLoginScreen.objPasswordField, Password, "Password field");
				hideKeyboard();
				verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login Button");
				waitTime(3000);
				break;

			case "SubscribedUser":
				extent.HeaderChildNode("Login as Subscribed User");

				String SubscribedUsername = getParameterFromXML("SubscribedUserName");
				String SubscribedPassword = getParameterFromXML("SubscribedPassword");

				verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
				type(AMDLoginScreen.objEmailIdField, SubscribedUsername, "Email Field");
				verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
				verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password Field");
				type(AMDLoginScreen.objPasswordField, SubscribedPassword, "Password field");
				hideKeyboard();
				verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login Button");
				waitTime(3000);
				break;

			}
		}	
		/**
		 * Function to verify Skip Login
		 */
		public void verifySkipLoginEvent(String usertype) throws Exception{
			extent.HeaderChildNode("Verify Skip login event");
			if(usertype.equalsIgnoreCase("Guest")) {
				verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
				verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
				verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");
			}
			
		}
		
		/**
		 * Function to verify Skip login by skipping LoginPopUp while content playback
		 * @throws Exception 
		 */
		public void verifySkipLogin_LoginInRegistrationPopUp(String usertype, String keyword1) throws Exception {
			if (userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Skip login event in Login or Register popUp during content playback");			
				click(AMDSearchScreen.objSearchIcon, "Search icon");
				click(AMDSearchScreen.objSearchEditBox, "Search Box");
				type(AMDSearchScreen.objSearchBoxBar, keyword1 + "\n", "Search bar");
				hideKeyboard();
				waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
				click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
				waitForAdToFinishInAmd();
				waitTime(5000);
				if (verifyIsElementDisplayed(AMDPlayerScreen.objRegisterPopUp, "Register popUp")) {
					verifyElementPresentAndClick(AMDPlayerScreen.objLoginButtonInRegisterPopUp, "Login link");
				}
				verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");	
			   }	
		}
		
		/**
		 * Function to handle Ad
		 */
		public void waitForAdToFinishInAmd() {
			waitTime(20000);
			if (verifyIsElementDisplayed(AMDPlayerScreen.objAd)) {
				logger.info("Ad is playing");
				extentLogger("Ad", "Ad is playing");

				verifyElementNotPresent(AMDPlayerScreen.objAd, 200);

				logger.info("Ad is completed");
				extentLogger("Ad", "Ad is completed");
			} else {
				logger.info("Ad is not played");
				extentLogger("Ad", "Ad is not played");
			}
		}
		
		/**
		 * Function to close the Register popUp
		 */
		public void registerPopUpClose() throws Exception {
			waitTime(5000);
			if (verifyIsElementDisplayed(AMDPlayerScreen.objRegisterPopUp)) {
				logger.info("Register Pop Up is displayed");
				extent.extentLogger("Register Pop Up", "Register Pop Up is displayed");
				Back(1);
			}
		}
		
		public void verifySkipLogin_LoginInGetPremiumPopUp(String userType, String keyword2) throws Exception {
			if(userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Skip login event on clicking login in Get premium popup during content playback");
				click(AMDSearchScreen.objSearchIcon, "Search icon");
				click(AMDSearchScreen.objSearchEditBox, "Search Box");
				type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
				hideKeyboard();
				waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
				click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");	
				verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeNowLinkOnPlayer, "Subscribe Now Link");
				Swipe("Up", 1);
				verifyElementPresentAndClick(AMDPlayerScreen.objLoginCTA, "Login CTA");
				verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");
			}		
		}
		
		public void verifyLogoutEvent(String userType) throws Exception {
			if (!(userType.equalsIgnoreCase("Guest"))) {
				extent.HeaderChildNode("Verify Logout Event");
				logout();
			}
		}
		
		public void logout() throws Exception {
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			Swipe("Up", 1);
			verifyElementPresentAndClick(AMDMoreMenu.objLogout, "Logout");
			verifyElementPresentAndClick(AMDMoreMenu.objLogoutPopup, "Logout popUp");
			verifyElementPresentAndClick(AMDMoreMenu.objLogoutButton, "Logout button");
			Swipe("Down", 1);
			verifyElementPresent(AMDMoreMenu.objGuestUserAccount, "Guest user Header");	
			verifyElementPresentAndClick(AMDHomePage.objHome, "Home tab");
		}
		
		public void verifyLoginScreenDisplayEventByClickingOnLoginButton(String userType) throws Exception{
			if (userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Login Screen Display Event By Clicking On Login Button");
				verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
				verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");	
		    }
		}
		
		public void verifyLoginScreenDisplayEventByClickingOnLoginButtonOnPlayer(String usertype, String keyword2) throws Exception {
			if (userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Login Screen Display Event By Clicking On Login Button On Player");
				click(AMDSearchScreen.objSearchIcon, "Search icon");
				click(AMDSearchScreen.objSearchEditBox, "Search Box");
				type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
				hideKeyboard();
				waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
				click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");	
				verifyElementPresentAndClick(AMDPlayerScreen.objLoginLinkOnPlayer, "Login Link");
				verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");
			}
			
		}
		
		public void verifyLoginScreenDisplayEventByClickingOnLoginButtonInRegistartionPopUp(String userType, String keyword1) throws Exception {
			if (userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Login Screen Display Event By Clicking On Login Button In Registartion Screen");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword1 + "\n", "Search bar");
			hideKeyboard();
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitForAdToFinishInAmd();
			waitTime(5000);
			if (verifyIsElementDisplayed(AMDPlayerScreen.objRegisterPopUp, "Register popUp")) {
				verifyElementPresentAndClick(AMDPlayerScreen.objLoginButtonInRegisterPopUp, "Login link");
				verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");
				}	
		   }	
		}
		
		public void verifyLoginScreenDisplayEventByClickingOnLoginButtonInGetPremiumPopUp(String userType, String keyword2) throws Exception {
			if(userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Login Screen Display Event By Clicking On Login Button In Get Premium Pop Up");
				click(AMDSearchScreen.objSearchIcon, "Search icon");
				click(AMDSearchScreen.objSearchEditBox, "Search Box");
				type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
				hideKeyboard();
				waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
				click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");	
				verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeNowLinkOnPlayer, "Subscribe Now Link");
				Swipe("Up", 1);
				verifyElementPresentAndClick(AMDPlayerScreen.objLoginCTA, "Login CTA");
			}		
		}
		
		public void verifyLoginInitiatedEventForValidCredentials(String userType, String loginMethod) throws Exception {
			if (userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Login Initiated Event for Valid Credentials");
				socialLogin(loginMethod);
			}
			
		}
		
		public void socialLogin(String LoginMethod) throws Exception {
			switch (LoginMethod) {

			case "twitterLogin":
				twitterLogin();
				waitTime(3000);
				break;

			case "fbLogin":
				facebookLogin();
				waitTime(3000);
				break;

			}
		}
		
		public void twitterLogin() throws Exception {
			extent.HeaderChildNode("Verify Login Initiated Event for Valid Credentials through Twitter");

			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(3000);
			waitForElementDisplayed(AMDLoginScreen.objtwitterBtn, 10);
			verifyElementPresentAndClick(AMDLoginScreen.objtwitterBtn, "Twitter icon");
			waitTime(10000);	
			if (verifyIsElementDisplayed(AMDHomePage.objHome, "Home tab")) {
				logger.info("User Logged in Successfully");
				extent.extentLogger("Logged in", "User Logged in Successfully");
				logout();
			}
			else if(verifyIsElementDisplayed(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App")){
				click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
			}
			else {
				verifyElementPresent(AMDLoginScreen.objEmailFieldInTwitterPage, " Email Field");
				type(AMDLoginScreen.objEmailFieldInTwitterPage, "zee5latest@gmail.com", "Email Field");

				verifyElementPresent(AMDLoginScreen.objPasswordFieldInTwitterPage, " Password Field");
				type(AMDLoginScreen.objPasswordFieldInTwitterPage, "User@123", "Password Field");

				verifyElementPresentAndClick(AMDLoginScreen.objLoginButtonInTwitterPage, "Login Button");
				waitTime(3000);
				Swipe("Up", 1);
				verifyElementPresentAndClick(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
				
				waitForElementDisplayed(AMDHomePage.objHome, 20);
				if (checkElementDisplayed(AMDHomePage.objHome, "Home tab")) {
					logger.info("User Logged in Successfully");
					extent.extentLogger("Logged in", "User Logged in Successfully");
					logout();
				} else {
					logger.info("User is not logged in Successfully");
					extent.extentLoggerFail("Logged in", "User is not logged in Successfully");
					Back(1);
				}
			}
		}
		
		public void facebookLogin() throws Exception {
			extent.HeaderChildNode("Verify Login Initiated Event for Valid Credentials through Facebook");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(3000);
			waitForElementDisplayed(AMDLoginScreen.objfbBtn, 10);
			verifyElementPresentAndClick(AMDLoginScreen.objfbBtn, "Facebook icon");
			waitTime(10000);	

			if (verifyIsElementDisplayed(AMDHomePage.objHome, "Home tab")) {
				logger.info("User Logged in Successfully");
				extent.extentLogger("Logged in", "User Logged in Successfully");
			}else {
				verifyElementPresent(AMDLoginScreen.objFBEmail, " Email Field");
				type(AMDLoginScreen.objFBEmail, "igstesttt@gmail.com", "Email Field");

				verifyElementPresent(AMDLoginScreen.objPasswordFieldInFBPage, " Password Field");
				type(AMDLoginScreen.objPasswordFieldInFBPage, "Igs123!@#", "Password Field");
				
				verifyElementPresentAndClick(AMDLoginScreen.objFBLoginBtn, "Login button");
				
				waitForElementDisplayed(AMDHomePage.objHome, 40);
				if (verifyIsElementDisplayed(AMDHomePage.objHome, "Home tab")) {
					logger.info("User Logged in Successfully");
					extent.extentLogger("Logged in", "User Logged in Successfully");
				}
			}
			logout();
		}
		
		public void verifyLoginResultEventForValidCredentials(String userType, String loginMethod) throws Exception {
			if (userType.equalsIgnoreCase("Guest")) {
				extent.HeaderChildNode("Verify Login Result Event for Valid Credentials");
				socialLogin(loginMethod);
			}
		}
		
		public void verifyTVAuthenticationScreenDisplayEvent(String userType) throws Exception {
			if (!(userType.equalsIgnoreCase("Guest"))) {
				extent.HeaderChildNode("Verify TV Authentication Screen Display Event");
				verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
				verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings");
				Swipe("Up", 1);
				verifyElementPresentAndClick(AMDSettingsScreen.objAuthenticateDevice, "Authenticate Device");
				waitTime(3000);
			}
		}
		
		public void verifySubscriptionPageViewedEventViaBuySubscription(String userType) throws Exception {
			extent.HeaderChildNode(
					"Verify Subscription Page Viewed Event by clicking on Buy subscription in more menu");
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
				verifyElementPresentAndClick(AMDMoreMenu.objBuySubscription, "Buy Subscription option");

			}
		}
		
		public void verifySubscriptionPageViewedEventViaSubscribeBtn(String userType) throws Exception {
			extent.HeaderChildNode("Verify Subscription Page Viewed Event");
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
			}
		}
		
		public void verifySubscriptionSelectedEvent(String userType) throws Exception {
			extent.HeaderChildNode("Verify Subscription Selected Event");
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
				Swipe("Up", 2);
				verifyElementPresentAndClick(AMDSubscibeScreen.objContinueBtn, "Continue Button");
				waitTime(2000);
			}
		}

		public void verifySubscriptionSelectedEventByClubPack(String userType) throws Exception {
			extent.HeaderChildNode("Verify Subscription Selected Event By selecting Club Pack");
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
				click(AMDSubscibeScreen.objClubTab, "Club Pack");
				Swipe("Up", 2);
//				click(AMDSubscibeScreen.objClub365daysPack, "Pack");
				verifyElementPresentAndClick(AMDSubscibeScreen.objContinueBtn, "Continue Button");
				waitTime(2000);
			}
		}
		
		public void verifyPromoCodeResultEventForValid(String userType) throws Exception {
			extent.HeaderChildNode("Verify Promo Code Result Event For Valid code");
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
				type(AMDSubscibeScreen.objApplyPromoCodeTextbox, "PNB20", "Prepaid Code");
				hideKeyboard();
				verifyElementPresentAndClick(AMDSubscibeScreen.objApply, "Apply Button");
				waitTime(2000);
			}
		}

		public void verifyPromoCodeResultEventForInvalid(String userType) throws Exception {
			extent.HeaderChildNode("Verify Promo Code Result Event For Invalid code");
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
				type(AMDSubscibeScreen.objApplyPromoCodeTextbox, "sdcrfd", "Prepaid Code");
				hideKeyboard();
				verifyElementPresentAndClick(AMDSubscibeScreen.objApply, "Apply Button");
				waitTime(2000);

			}
		}
		
		public void verifyCarouselBannerClickEvent() throws Exception {
			extent.HeaderChildNode(
					"Verify Carousel Banner Click Event And Video View Event For content played from Carousel");
			waitTime(5000);
			verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "Caorsel Content");

		}
		
		public void SelectTopNavigationTab(String pTabname) throws Exception {
			System.out.println("\nSelecting " + pTabname + " from Top navigation tabs");

			verifyElementPresentAndClick(AMDHomePage.objHome, "Home button");
			int noOfTabs = getCount(AMDHomePage.objTitle);
			System.out.println("\nTop Navigation Tabs: " + noOfTabs);
			for (int k = 1; k <= noOfTabs; k++) {
				if (verifyIsElementDisplayed(AMDGenericObjects.objPageTitle(pTabname))) {
					click(AMDGenericObjects.objPageTitle(pTabname), pTabname);
					break;
				} else {
					List<WebElement> element = getDriver().findElements(By.xpath("//*[@id='title']"));
					element.get(noOfTabs - 1).click();
					waitTime(1000);
				}
			}
		}
		
		public void verifyThumbnailClickEventFromTray() throws Exception {
			extent.HeaderChildNode("Verify Thumbnail Click Event For content played from trays");
			waitTime(5000);
			for(int i=0; i<5;i++) {
//				boolean var=verifyIsElementDisplayed(AMDHomePage.objFirstThumbnailOfTray, "Thumbnail from a tray");
//				if(var==false){
//					Swipe("Up", 1);
//				}else {
//					break;
//				}
			}
//			verifyElementPresentAndClick(AMDHomePage.objFirstThumbnailOfTray, "Thumbnail from a tray");
		}
		
		public void verifyThumbnailClickEventFromTrayInPlayBackPage(String keyword2) throws Exception {
			extent.HeaderChildNode("Verify Thumbnail Click Event For content played from trays in playback page");
			waitTime(5000);
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");	
			for(int i=0; i<5;i++) {
//				boolean var=verifyIsElementDisplayed(AMDConsumptionScreen.objContentCardOfTrayInConsumptionPage, "Thumbnail in playback page");
//				if(var==false){
//					Swipe("Up", 1);
//				}else {
//					break;
//				}
			}
//			verifyElementPresentAndClick(AMDConsumptionScreen.objContentCardOfTrayInConsumptionPage, "Thumbnail in playback page");
		}
		
		public void verifyParentalRestrictionEvent(String userType, String restriction) throws Exception {
			if (!(userType.equalsIgnoreCase("Guest"))) {

				extent.HeaderChildNode("Verify Parental Restriction Event");
				click(PWAHamburgerMenuPage.objHamburgerBtn, "Hamburger menu");
				click(PWAHamburgerMenuPage.objParentalControl, "ParentalControl");
				checkElementDisplayed(PWALoginPage.objPasswordField, "password field");
				String password = "";
				if (userType.equals("NonSubscribedUser")) {
					password = getParameterFromXML("SettingsNonsubscribedPassword");
				} else if (userType.equals("SubscribedUser")) {
					password = getParameterFromXML("SettingsSubscribedPassword");
				}
				type(PWALoginPage.objPasswordField, password, "Password field");
				click(PWAHamburgerMenuPage.objContinueButtonInVerifyAccount, "Continue button");
				waitTime(2000);
				checkElementDisplayed(PWAHamburgerMenuPage.objParentControlPageTitle, "Parent control page");

				switch (restriction) {

				case "Age13+":
					click(PWAHamburgerMenuPage.objRestrict13PlusContent, "Restrict 13+ content");
					click(PWAHamburgerMenuPage.objParentalLockPin1, "Set Lock Field");
					type(PWAHamburgerMenuPage.objParentalLockPin1, "1", "ParentalLockPin");
					type(PWAHamburgerMenuPage.objParentalLockPin2, "2", "ParentalLockPin");
					type(PWAHamburgerMenuPage.objParentalLockPin3, "3", "ParentalLockPin");
					type(PWAHamburgerMenuPage.objParentalLockPin4, "4", "ParentalLockPin");
					waitTime(4000);
					click(PWAHamburgerMenuPage.objSetParentalLockButton, "Set Parental lock button");
					waitTime(3000);

					click(PWAHamburgerMenuPage.objHamburgerBtn, "Hamburger menu");
					click(PWAHamburgerMenuPage.objParentalControl, "ParentalControl");
					checkElementDisplayed(PWALoginPage.objPasswordField, "password field");
					type(PWALoginPage.objPasswordField, password, "Password field");
					waitTime(2000);
					click(PWAHamburgerMenuPage.objContinueButtonInVerifyAccount, "Continue button");
					waitTime(2000);
					checkElementDisplayed(PWAHamburgerMenuPage.objParentControlPageTitle, "Parent control page");
					click(PWAHamburgerMenuPage.objParentalLockNoRestrictionOption, "No restriction option");
					click(PWAHamburgerMenuPage.objSetParentalLockButton, "Set Parental lock button");
					waitTime(2000);

				case "RestrictAll":
					click(PWAHamburgerMenuPage.objRestrictAll, "Restrict all option");
					click(PWAHamburgerMenuPage.objParentalLockPin1, "Set Lock Field");
					type(PWAHamburgerMenuPage.objParentalLockPin1, "1", "ParentalLockPin");
					type(PWAHamburgerMenuPage.objParentalLockPin2, "2", "ParentalLockPin");
					type(PWAHamburgerMenuPage.objParentalLockPin3, "3", "ParentalLockPin");
					type(PWAHamburgerMenuPage.objParentalLockPin4, "4", "ParentalLockPin");
					waitTime(4000);
					click(PWAHamburgerMenuPage.objSetParentalLockButton, "Set Parental lock button");
					waitTime(3000);

					click(PWAHamburgerMenuPage.objHamburgerBtn, "Hamburger menu");
					click(PWAHamburgerMenuPage.objParentalControl, "ParentalControl");
					checkElementDisplayed(PWALoginPage.objPasswordField, "password field");
					type(PWALoginPage.objPasswordField, password, "Password field");
					waitTime(2000);
					click(PWAHamburgerMenuPage.objContinueButtonInVerifyAccount, "Continue button");
					waitTime(2000);
					checkElementDisplayed(PWAHamburgerMenuPage.objParentControlPageTitle, "Parent control page");
					click(PWAHamburgerMenuPage.objParentalLockNoRestrictionOption, "No restriction option");
					click(PWAHamburgerMenuPage.objSetParentalLockButton, "Set Parental lock button");
					waitTime(2000);

				case "NoRestriction":
					click(PWAHamburgerMenuPage.objNoRestrictionSelected, "Restrict all option");
					click(PWAHamburgerMenuPage.objContinueButton, "Continue Button");

				}

			}
		}
		
		public void verifySubscriptionPageViewedEventByClickingGetPremiumCTAOnCarousel(String tabName) throws Exception {
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				extent.HeaderChildNode(
						"Verify Subscription Page Viewed event gets triggered, When user is navigating to Subscirption page by clicking on Get Premium CTA on carousel");
				waitTime(5000);
//				verifyElementPresentAndClick(AMDHomePage.objGetPremiumCTAOnCarousel, "Caorsel Content");
				waitTime(4000);
			}
		}
		
		public void verifySubscriptionPageViewedEventByClickingSubscriptionbelowThePlayer(String usertype, String keyword2) throws Exception {
			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				extent.HeaderChildNode("Verify Subscription page viewed event by clicking Subscription CTA below the player in consumption page");
				click(AMDSearchScreen.objSearchIcon, "Search icon");
				click(AMDSearchScreen.objSearchEditBox, "Search Box");
				type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
				hideKeyboard();
				waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
				click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");	
	            verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeButtonBelowThePlayer, "Get premium CTA below the Player in consumption page");
				
			}
		}
		
		public void verifyScreenViewEvent(String screen) throws Exception {
			extent.HeaderChildNode("Verify Screen View Event");
			SelectTopNavigationTab(screen);
			waitTime(3000);
		}	
		
		public void verifyViewMoreSelectedEventFromTray() throws Exception {
			extent.HeaderChildNode("Verify View More Selected Event For content played from Tray");
			waitTime(5000);
			for(int i=0; i<5;i++) {
//				boolean var=verifyIsElementDisplayed(AMDHomePage.objFirstViewAllbtn, "View All option a tray");
//				if(var==false){
//					Swipe("Up", 1);
//				}else {
//					break;
//				}
			}
//			verifyElementPresentAndClick(AMDHomePage.objFirstViewAllbtn, "View All option a tray");
		}
		
		public void verifyViewMoreSelectedEventFromPlaybackPage(String keyword2) throws Exception {
			extent.HeaderChildNode("Verify View More Selected Event For content played from Playback page");
			waitTime(5000);
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");	
			for(int i=0; i<5;i++) {
//				boolean var=verifyIsElementDisplayed(AMDHomePage.objFirstViewAllbtn, "View All option a tray");
//				if(var==false){
//					Swipe("Up", 1);
//				}else {
//					break;
//				}
			}
//			verifyElementPresentAndClick(AMDHomePage.objFirstViewAllbtn, "View All option a tray");
		
		}
		
		public void clearSearchHistoryEvent(String keyword1) throws Exception {
			extent.HeaderChildNode("Verify Clear Search History Event");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword1 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			verifyElementPresentAndClick(AMDSearchScreen.objClearSearch, "Clear Search Icon");
		}
		
		public void verifySearchButtonClickEvent() throws Exception {
			extent.HeaderChildNode("Verify Search Button Click Event");
			waitTime(5000);
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			waitTime(3000);
		}
		
		public void verifySearchExecutedEvent(String keyword2) throws Exception {
			extent.HeaderChildNode("Verify Search Executed Event");
			waitTime(5000);
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			waitTime(4000);	
		}
		
		public void verifySearchResultClickedEvent(String keyword2) throws Exception {
			waitTime(5000);
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(3000);
		}
		

}
