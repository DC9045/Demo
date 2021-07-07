package com.business.zee;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import com.deviceDetails.DeviceDetails;
import com.driverInstance.CommandBase;
import com.emailReport.GmailInbox;
import com.extent.ExtentReporter;
//import com.jayway.restassured.response.Response;
import com.metadata.ResponseInstance;
import com.mixpanelValidation.Mixpanel;
import com.mixpanelValidation.MixpanelAndroid;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;
import com.zee5.ApplicasterPages.*;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.restassured.RestAssured;
import io.restassured.response.Response;

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

	MixpanelAndroid mixpanel = new MixpanelAndroid();
	String pUserType = getParameterFromXML("userType");

	String FirstName = getParameterFromXML("FirstName");
	String LastName = getParameterFromXML("LastName");
	String generatedEmailId = null;

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
//		logger.info("Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	public void relaunchTillDisplayLanguageScreen(boolean clearData) throws Exception {
		HeaderChildNode("Relaunch the app");
		logger.info("Relaunching the application");
		extent.extentLogger("Relaunch", "Relaunching the application");
		waitTime(10000);
		getDriver().quit();
		relaunch = clearData;
		new Zee5ApplicasterBusinessLogic("zee");
		accessDeviceLocationPopUp("Allow", userType);
		if (verifyIsElementDisplayed(AMDOnboardingScreen.objContinueBtnInCountryPopUp)) {
			click(AMDOnboardingScreen.objContinueBtnInCountryPopUp, "Continuebutton(Country_Screen)");
		}
	}

	public void relaunchTillIntroScreen(boolean clearData) throws Exception {
		HeaderChildNode("Relaunch the app");
		logger.info("Relaunching the application");
		extent.extentLogger("Relaunch", "Relaunching the application");
		waitTime(10000);
		getDriver().quit();
		relaunch = clearData;
		new Zee5ApplicasterBusinessLogic("zee");
		accessDeviceLocationPopUp("Allow", userType);
		navigateToIntroScreen_DisplaylangScreen();
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
		if (userType != "Guest" & clearData == false) {
			System.out.println("Navigates to Landing Sccreen..");
		} else {
			accessDeviceLocationPopUp("Allow", userType);
			navigateToIntroScreen_DisplaylangScreen();
			ZeeApplicasterLogin(userType);
		}
	}

	/**
	 * Function to Relaunch the driver
	 */
	public void relaunch2(boolean clearData) throws Exception {
		HeaderChildNode("Relaunch the app");
		logger.info("Relaunching the application");
		extent.extentLogger("Relaunch", "Relaunching the application");
		waitTime(10000);
		getDriver().quit();
		relaunch = clearData;
		new Zee5ApplicasterBusinessLogic("zee");
		accessDeviceLocationPopUp("Allow", userType);
		navigateToIntroScreen_DisplaylangScreen();
		ZeeApplicasterMixPanelLoginForParentalControl(userType);
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

		waitTime(5000);
		if (verifyIsElementDisplayed(AMDOnboardingScreen.objCancelZEE5Update)) {
			click(AMDOnboardingScreen.objCancelZEE5Update, "No Thanks");
		}

		if (verifyIsElementDisplayed(AMDOnboardingScreen.objContinueBtnInDebugBuild)) {
			click(AMDOnboardingScreen.objContinueBtnInDebugBuild, "Continue button");
		}
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
		waitTime(6000);
		if (verifyIsElementDisplayed(AMDOnboardingScreen.objContinueBtnInCountryPopUp)) {
			click(AMDOnboardingScreen.objContinueBtnInCountryPopUp, "Continuebutton(Country_Screen)");
		}
//		click(AMDOnboardingScreen.objContent_ContinueBtn, "Continue button (Content-LanguageScreen)");
//		verifyElementPresent(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
	}

	String Username;
	String Password;

	/**
	 * Function to Login to Zee5
	 */
public void ZeeApplicasterLogin(String LoginMethod) throws Exception {
		
		switch (LoginMethod) {
		case "Guest":
			extent.HeaderChildNode("Login as Guest User");
			extent.extentLoggerPass("Accessing the application as Guest user", "Accessing the application as Guest user");
			
			break;

		case "NonSubscribedUser":
			extent.HeaderChildNode("Login as NonSubscribed User");

			Username = getParameterFromXML("NonSubscribedUserName");
			Password = getParameterFromXML("NonSubscribedUserPassword");

			verifyElementPresentAndClick(AMDHomePage.objHomeBtn, "Home tab");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More Menu");
			verifyElementPresentAndClick(AMDMoreMenu.objLoginRegisterText, "Login/Register for best experience text");
			
			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
			verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
			hideKeyboard();
			verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password Field");
			type(AMDLoginScreen.objPasswordField, Password, "Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login Button");
			waitTime(3000);
			break;

		case "SubscribedUser":
			extent.HeaderChildNode("Login as Subscribed User");

			Username = getParameterFromXML("SubscribedUserName");
			Password = getParameterFromXML("SubscribedPassword");

			verifyElementPresentAndClick(AMDHomePage.objHomeBtn, "Home tab");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More Menu");
			verifyElementPresentAndClick(AMDMoreMenu.objLoginRegisterText, "Login/Register for best experience text");
			
			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
			verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
			hideKeyboard();
			verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password Field");
			type(AMDLoginScreen.objPasswordField, Password, "Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login Button");
			waitTime(3000);
			break;

		case "ClubUser":
			extent.HeaderChildNode("Login as Club User");

			String ClubUsername = getParameterFromXML("ClubSubscribedUserName");
			String ClubPassword = getParameterFromXML("ClubSubscribedPassword");

			verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");

			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			type(AMDLoginScreen.objEmailIdField, ClubUsername, "Email Field");
			verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
			verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password Field");
			type(AMDLoginScreen.objPasswordField, ClubPassword, "Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login Button");
			waitTime(3000);
			break;
		}
	}

	/**
	 * Function to verify Skip Login
	 */
	public void verifySkipLoginEvent(String usertype) throws Exception {
		extent.HeaderChildNode("Verify Skip login event");
		if (usertype.equalsIgnoreCase("Guest")) {
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");
			mixpanel.FEProp.setProperty("Element", "Skip");
			mixpanel.FEProp.setProperty("Source", "Intro");
			mixpanel.ValidateParameter("", "Skip Login");
		}
	}

	/**
	 * Function to verify Skip login by skipping LoginPopUp while content playback
	 * 
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
			click(AMDGenericObjects.objPopUpDivider, "Close PopUp");
		}
	}

	public void verifyLoginScreenDisplayEventByClickingOnLoginButtonInMandatoryRegistartionPopUp(String userType,
			String keyword1) throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode(
					"Verify Login Screen Display Event By Clicking On Login Button In Mandatory Registartion PopUp");
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
				verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
				String Username = getParameterFromXML("NonSubscribedUserName");
				type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
				verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
				waitTime(3000);
				verifyIsElementDisplayed(AMDLoginScreen.objLoginScreenTitle, "Login screen title");
			}
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
		} else if (verifyIsElementDisplayed(AMDLoginScreen.objTwitterEmail, "Email Field")) {
			verifyElementPresent(AMDLoginScreen.objTwitterEmail, " Email Field");
			type(AMDLoginScreen.objTwitterEmail, "zee5latest@gmail.com", "Email Field");

			verifyElementPresent(AMDLoginScreen.objTwitterPassword, " Password Field");
			type(AMDLoginScreen.objTwitterPassword, "User@123", "Password Field");

//			verifyElementPresentAndClick(AMDLoginScreen.objLoginButtonInTwitterPage, "Login Button");
			waitTime(2000);
			Swipe("Up", 1);
			verifyElementPresentAndClick(AMDLoginScreen.objtwAuthorizeAppBtn, "Authorize App");

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
		} else {
			click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
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
		} else {
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

	public void relaunchToIntroScreen(boolean clearData) throws Exception {
		HeaderChildNode("Relaunch the app");
		logger.info("Relaunching the application");
		extent.extentLogger("Relaunch", "Relaunching the application");
		waitTime(10000);
		getDriver().quit();
		relaunch = clearData;
		new Zee5ApplicasterBusinessLogic("zee");
		accessDeviceLocationPopUp("Allow", userType);
		navigateToIntroScreen_DisplaylangScreen();
	}

	public void verifySubscriptionPageViewedEventViaBuySubscription(String userType) throws Exception {
		extent.HeaderChildNode("Verify Subscription Page Viewed Event by clicking on Buy subscription in more menu");
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

	@SuppressWarnings("static-access")
	public void verifyCarouselBannerClickEvent() throws Exception {
		extent.HeaderChildNode(
				"Verify Carousel Banner Click Event And Video View Event For content played from Carousel");
		waitTime(5000);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "Carousel Content");

		setFEProperty(userType);

		mixpanel.FEProp.setProperty("Page Name", "home");
		mixpanel.ValidateParameter("", "Carousal Banner Click");

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
				List<WebElement> element = getDriver().findElements(By.xpath("(//*[@id='homeTabLayout']/*/child::*)"));
				element.get(noOfTabs - 1).click();
				waitTime(1000);
			}
		}
	}

	public void verifyThumbnailClickEventFromTray() throws Exception {
		extent.HeaderChildNode("Verify Thumbnail Click Event For content played from trays");
		waitTime(5000);
		for (int i = 0; i < 5; i++) {
			boolean var = verifyIsElementDisplayed(AMDHomePage.objFirstThumbnailOfTray, "Thumbnail from a tray");
			if (var == false) {
				Swipe("Up", 1);
			} else {
				break;
			}
		}
		verifyElementPresentAndClick(AMDHomePage.objFirstThumbnailOfTray, "Thumbnail from a tray");
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
		for (int i = 0; i < 5; i++) {
			boolean var = verifyIsElementDisplayed(AMDConsumptionScreen.objContentCardOfTrayInConsumptionPage,
					"Thumbnail in playback page");
			if (var == false) {
				Swipe("Up", 1);
			} else {
				break;
			}
		}
		verifyElementPresentAndClick(AMDConsumptionScreen.objContentCardOfTrayInConsumptionPage,
				"Thumbnail in playback page");
	}

	@SuppressWarnings("deprecation")
	public void verifyParentalRestrictionEvent(String userType, String restriction) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {

			extent.HeaderChildNode("Verify Parental Restriction Event");
			click(AMDHomePage.MoreMenuIcon, "More Menu tab");
			click(AMDMoreMenu.objSettings, "Settings option");
			waitTime(5000);
			Swipe("UP", 1);
			verifyElementPresentAndClick(AMDMoreMenu.objParentalControl, "Parental Control");
			verifyElementExist(AMDMoreMenu.objPasswordField, "Password field");
			String password = "";
			if (userType.equals("NonSubscribedUser")) {
				password = getParameterFromXML("SettingsNonsubscribedPassword");
			} else if (userType.equals("SubscribedUser")) {
				password = getParameterFromXML("SettingsSubscribedPassword");
			}
			click(AMDMoreMenu.objPasswordField, "Password field");
			getDriver().getKeyboard().sendKeys(password);

			hideKeyboard();
			if (getOEMName.contains("vivo")) {
				hidePwdKeyboard();
			}
			click(AMDMoreMenu.objPasswordContinueBtn, "Continue button");
			waitTime(2000);

			if (restriction.equalsIgnoreCase("Age13+")) {
				click(AMDMoreMenu.objRestrict13Above, "Restrict 13+ Content option");
				click(AMDMoreMenu.objContinueBtn, "Continue Button");
				waitTime(2000);

				verifyElementExist(AMDMoreMenu.objSetPin, "Set Pin");
				type(AMDMoreMenu.objParentalLockPin1, "1", "ParentalLockPin");
				hideKeyboard();
				type(AMDMoreMenu.objParentalLockPin2, "2", "ParentalLockPin");
				hideKeyboard();
				type(AMDMoreMenu.objParentalLockPin3, "3", "ParentalLockPin");
				hideKeyboard();
				type(AMDMoreMenu.objParentalLockPin4, "4", "ParentalLockPin");
				hideKeyboard();
				waitTime(4000);
				click(AMDMoreMenu.objSetPinContinueBtn, "Continue Button");
				waitTime(2000);
				click(AMDMoreMenu.objParentalLockDone, "Done Button");
			} else if (restriction.equalsIgnoreCase("Restrict All")) {
				click(AMDMoreMenu.objRestrictAllContent, "Restrict All Content option");
				click(AMDMoreMenu.objContinueBtn, "Continue Button");
				waitTime(2000);
				verifyElementExist(AMDMoreMenu.objSetPin, "Set Pin");
				type(AMDMoreMenu.objParentalLockPin1, "1", "ParentalLockPin");
				hideKeyboard();
				type(AMDMoreMenu.objParentalLockPin2, "2", "ParentalLockPin");
				hideKeyboard();
				type(AMDMoreMenu.objParentalLockPin3, "3", "ParentalLockPin");
				hideKeyboard();
				type(AMDMoreMenu.objParentalLockPin4, "4", "ParentalLockPin");
				hideKeyboard();
				waitTime(4000);
				click(AMDMoreMenu.objSetPinContinueBtn, "Continue Button");
				waitTime(2000);
				click(AMDMoreMenu.objParentalLockDone, "Done Button");
			}

			waitTime(3000);
			Swipe("Up", 2);
			verifyElementPresentAndClick(AMDMoreMenu.objParentalControl, "Parental Control");
			verifyElementExist(AMDMoreMenu.objPasswordField, "Password field");
			if (userType.equals("NonSubscribedUser")) {
				password = getParameterFromXML("SettingsNonsubscribedPassword");
			} else if (userType.equals("SubscribedUser")) {
				password = getParameterFromXML("SettingsSubscribedPassword");
			}
			click(AMDMoreMenu.objPasswordField, "Password field");
			getDriver().getKeyboard().sendKeys(password);

			hideKeyboard();
			if (getOEMName.contains("vivo")) {
				hidePwdKeyboard();
			}
			click(AMDMoreMenu.objPasswordContinueBtn, "Continue button");
			waitTime(2000);
			click(AMDMoreMenu.objNoRestriction, "No Restriction option");
			click(AMDMoreMenu.objContinueBtn, "Continue Button");
			waitTime(2000);
			click(AMDMoreMenu.objParentalLockDone, "Done Button");
			waitTime(3000);
		}
	}

	public void verifySubscriptionPageViewedEventByClickingGetPremiumCTAOnCarousel(String tabName) throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode(
					"Verify Subscription Page Viewed event gets triggered, When user is navigating to Subscirption page by clicking on Get Premium CTA on carousel");
			waitTime(5000);
			verifyElementPresentAndClick(AMDHomePage.objGetPremiumCTAOnCarousel, "Caorsel Content");
			waitTime(4000);
		}
	}

	public void verifySubscriptionPageViewedEventByClickingSubscriptionbelowThePlayer(String usertype, String keyword2)
			throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode(
					"Verify Subscription page viewed event by clicking Subscription CTA below the player in consumption page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeButtonBelowThePlayer,
					"Get premium CTA below the Player in consumption page");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyScreenViewEvent(String screen, String userType) throws Exception {
		extent.HeaderChildNode("Verify Screen View Event");
		SelectTopNavigationTab(screen);
		waitTime(3000);
		setFEProperty(userType);

		mixpanel.FEProp.setProperty("Source", "home");
		mixpanel.FEProp.setProperty("Page Name", "tvshows");
		mixpanel.ValidateParameter("", "Screen View");
	}

	public void verifyViewMoreSelectedEventFromTray() throws Exception {
		extent.HeaderChildNode("Verify View More Selected Event For content played from Tray");
		waitTime(5000);
		for (int i = 0; i < 5; i++) {
			boolean var = verifyIsElementDisplayed(AMDHomePage.objFirstViewAllbtn, "View All option a tray");
			if (var == false) {
				Swipe("Up", 1);
			} else {
				break;
			}
		}
		verifyElementPresentAndClick(AMDHomePage.objFirstViewAllbtn, "View All option a tray");
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
		for (int i = 0; i < 5; i++) {
			boolean var = verifyIsElementDisplayed(AMDHomePage.objFirstViewAllbtn, "View All option a tray");
			if (var == false) {
				Swipe("Up", 1);
			} else {
				break;
			}
		}
		verifyElementPresentAndClick(AMDHomePage.objFirstViewAllbtn, "View All option a tray");

	}

	public void clearSearchHistoryEvent(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Clear Search History Event");
			click(AMDHomePage.MoreMenuIcon, "More menu icon");
			verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
			waitTime(300);
			Swipe("Up", 2);
			verifyElementPresentAndClick(AMDSettingsScreen.objClearSearchHistory, "Clear Search Histroy");
			waitTime(4000);

			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "user_setting");
			mixpanel.FEProp.setProperty("Source", "More");
			mixpanel.FEProp.setProperty("Element", "Clear");
			mixpanel.FEProp.setProperty("Setting Changed", "User Cleared History");
			mixpanel.ValidateParameter("", "Clear Search History");
		}
	}

	@SuppressWarnings("static-access")
	public void verifySearchButtonClickEvent(String userType) throws Exception {
		extent.HeaderChildNode("Verify Search Button Click Event");
		waitTime(5000);
		String Username = "";
		String Password = "";
		if (userType.equals("NonSubscribedUser")) {
			Username = getParameterFromXML("NonSubscribedUserName");
			Password = getParameterFromXML("NonSubscribedUserPassword");
		} else {
			Username = getParameterFromXML("SubscribedUserName");
			Password = getParameterFromXML("SubscribedPassword");

		}

		verifyElementPresentAndClick(AMDSearchScreen.objSearchIcon, "Search icon");
		waitTime(3000);

		if (userType.equals("Guest")) {
			mixpanel.FEProp.setProperty("Old Content Language:",
					ResponseInstance.getUserOldSettingsDetails("", "").getProperty("content_language"));
		} else {
			mixpanel.FEProp.setProperty("Old Content Language:",
					ResponseInstance.getUserOldSettingsDetails(Username, Password).getProperty("content_language"));
		}
		setFEProperty(userType);

		mixpanel.FEProp.setProperty("Page Name", "HomePage");
		mixpanel.FEProp.setProperty("Element", "Search Icon");
		mixpanel.ValidateParameter("", "Search Button Click");
	}

	@SuppressWarnings("static-access")
	public void verifySearchExecutedEvent(String keyword2) throws Exception {
		extent.HeaderChildNode("Verify Search Executed Event");
		waitTime(5000);
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "user_setting");
		mixpanel.FEProp.setProperty("page_name", "Search");
		mixpanel.FEProp.setProperty("tab_name", "All");
		mixpanel.ValidateParameter("", "Search Executed");

	}

	public void verifySearchResultClickedEvent(String keyword2) throws Exception {
		extent.HeaderChildNode("Verify Search Result click Event");
		waitTime(5000);
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
		hideKeyboard();
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(3000);
	}

	@SuppressWarnings("static-access")
	public void verifyVideoQualityChangeEvent() throws Exception {
		extent.HeaderChildNode("Verify video quality change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		boolean var = verifyIsElementDisplayed(AMDMoreMenu.objSelectedVideoQualityOption("Auto"));
		if (var == true) {
			click(AMDMoreMenu.objVideo_Quality("Auto"), "Video quality option");
			click(AMDSettingsScreen.objVideoQualityBetter, "option Better");
			click(AMDMoreMenu.objVideo_Quality("Better"), "Video quality option");
			click(AMDMoreMenu.objAutoOption, "option Auto");

			mixpanel.FEProp.setProperty("Source", "More");
			mixpanel.FEProp.setProperty("Page Name", "selector");
			mixpanel.ValidateParameter("", "Video Streaming Quality Changed");

		} else {
			logger.error("the default selection in the Select Video Quality is not 'Auto' option");
			extentLoggerWarning("Default selected Video quality option",
					"the default selection in the Select Video Quality is not 'Auto' option");
		}

	}

	public void verifyVideoAutoPlayChangeEvent() throws Exception {
		extent.HeaderChildNode("Verify video AutoPlay change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		String elementAutoPlayToggleStatus = getText(AMDMoreMenu.objVideo_Autoply);
		if (elementAutoPlayToggleStatus.equalsIgnoreCase("ON")) {
			click(AMDMoreMenu.objVideo_Autoply, "Video Auto play toggle");
		} else {
			logger.info("the default state of the 'Auto Play' option is not in ON state");
			extentLoggerWarning("Video Auto Play", "the default state of the 'Auto Play' option is not in ON state");
		}
	}

	public void verifyDisplayLanguageChangeEvent(String usertype) throws Exception {
		extent.HeaderChildNode("Verify display language change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		waitTime(1000);
		SwipeUntilFindElement(AMDMoreMenu.objDisplayLang, "Up");
		click(AMDMoreMenu.objDisplayLang, "Display language");
		
		boolean flag = false;
		flag = verifyElementPresentAndClick(AMDOnboardingScreen.objSelectDisplayLang("Marathi"), "language");
		click(AMDOnboardingScreen.objDiplay_ContinueBtn, "Continue button");
		
		if(flag){
			
			setFEProperty(usertype);
			setUserType_SubscriptionProperties(usertype);
			
			
			mixpanel.FEProp.setProperty("Source", "user_setting");
			mixpanel.FEProp.setProperty("Page Name", "DisplayLanguage");
			mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
			mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
			
			if (!(pUserType.equalsIgnoreCase("Guest"))) {
				if(pUserType.equalsIgnoreCase("SubscribedUser")) {
					Username = getParameterFromXML("SubscribedUserName");
					Password = getParameterFromXML("SubscribedPassword");
				}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
					Username = getParameterFromXML("NonSubscribedUserName");
					Password = getParameterFromXML("NonSubscribedUserPassword");
				}
		      	mixpanel.FEProp.setProperty("Old App Language", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("display_language"));
			}else {
				mixpanel.FEProp.setProperty("User Type", "guest");
				mixpanel.FEProp.setProperty("Old App Language", "en");
			}
			
			mixpanel.FEProp.setProperty("New App Language", "mr");
			
			mixpanel.ValidateParameter("", "Display Language Changed");
			
			SwipeUntilFindElement(AMDMoreMenu.objDisplayLang, "Up");
			click(AMDMoreMenu.objDisplayLang, "Display language");
			verifyElementPresentAndClick(AMDOnboardingScreen.objSelectDisplayLang("English"), "language");
			click(AMDOnboardingScreen.objDiplay_ContinueBtn, "Continue button");
			
		} else {
			logger.info("Failed to change display language");
			extentLoggerWarning("Event", "Failed to change display language");
		}

	}

	public void verifyContentLanguageChangeEvent() throws Exception {
		extent.HeaderChildNode("Verify Content language change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		waitTime(1000);
		SwipeUntilFindElement(AMDMoreMenu.objContentLang, "Up");
		click(AMDMoreMenu.objContentLang, "Content language");
		click(AMDOnboardingScreen.objSelectContentLang("English"), "English language");
		click(AMDOnboardingScreen.objContent_ContinueBtn, "Continue button in Content language screen");

		mixpanel.FEProp.setProperty("Source", "user_setting");
		mixpanel.FEProp.setProperty("Page Name", "ContentLanguage");
		mixpanel.ValidateParameter("", "Content Language Changed");
		;

	}

	public boolean SwipeUntilFindElement(By locator, String direction) throws Exception {

		boolean checkLocator, eletFound = false;
		if (direction.equalsIgnoreCase("UP")) {
			for (int i = 1; i < 25; i++) {
				PartialSwipe("UP", 1);
				checkLocator = verifyIsElementDisplayed(locator);
				if (checkLocator) {
					eletFound = true;
					break;
				}
			}
		}

		if (direction.equalsIgnoreCase("DOWN")) {
			for (int i = 1; i < 25; i++) {
				PartialSwipe("DOWN", 1);
				checkLocator = verifyIsElementDisplayed(locator);
				if (checkLocator) {
					eletFound = true;
					break;
				}
			}
		}
		return eletFound;
	}

	@SuppressWarnings("static-access")
	public void verifyDefaultSettingRestoredEvent() throws Exception {
		extent.HeaderChildNode("Verify Default Setting Restored Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		Swipe("Up", 2);
		verifyElementPresentAndClick(AMDSettingsScreen.objDefaultSetting, "Default Setting Link");
		verifyElementPresentAndClick(AMDSettingsScreen.objYesCTA, "Yes CTA");

		setFEProperty(pUserType);
		setUserType_SubscriptionProperties(pUserType);
		
		if(pUserType.equalsIgnoreCase("Guest")) {
			mixpanel.FEProp.setProperty("User Type", "guest");
		}
		mixpanel.FEProp.setProperty("Element", "Restore settings to default");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
		mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
		mixpanel.ValidateParameter("", "Default Settings Restored");

	}

	public void ZeeApplicasterMixPanelLoginForParentalControl(String LoginMethod) throws Exception {
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

			String Username = getParameterFromXML("ParentalNonsubscribedUserName");
			String Password = getParameterFromXML("ParentalNonsubscribedPassword");

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

			String SubscribedUsername = getParameterFromXML("ParentalSubscribedUserName");
			String SubscribedPassword = getParameterFromXML("ParentalSubscribedPassword");

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

	public void verifyCarouselBannerSwipeEvent(String tabName) throws Exception {
		extent.HeaderChildNode("Verify Carousel Banner Swipe Event Across tabs");
		SelectTopNavigationTab(tabName);
		waitForElementDisplayed(AMDHomePage.objCarouselDots, 10);
		waitTime(10000);

		if (verifyElementDisplayed(AMDHomePage.objBannerAd)) {
			verifyElementPresent(AMDHomePage.objCarouselUnitwithmastHeadAdbanner,
					"Carousel unit as first unit on " + tabName + " screen");
		} else {
			verifyElementPresent(AMDHomePage.objCarouselUnitwhenNomastHeadAdbanner,
					"Carousel unit as first unit on " + tabName + " screen");
		}

		// Validating Carousel manual swipe
		String width = getAttributValue("width", AMDHomePage.objCarouselConetentCard);

		String bounds = getAttributValue("bounds", AMDHomePage.objCarouselConetentCard);
		String b = bounds.replaceAll(",", " ").replaceAll("]", " ");
		String height = b.split(" ")[1];

		waitTime(3000);
		String Carouseltitle1 = getText(AMDHomePage.objCarouselTitle1);
		logger.info(Carouseltitle1);
		extentLoggerPass("Carousel Title", Carouseltitle1);
		carouselCardsSwipe("LEFT", 1, width, height);

		String Carouseltitle2 = getText(AMDHomePage.objCarouselTitle1);
		logger.info(Carouseltitle2);
		extentLoggerPass("Carousel Title", Carouseltitle2);
		carouselCardsSwipe("RIGHT", 1, width, height);
	}

	@SuppressWarnings("rawtypes")
	public void carouselCardsSwipe(String direction, int count, String width, String height) throws Exception {
		touchAction = new TouchAction(getDriver());

		try {

			int yCordinate;
			if (verifyElementIsNotDisplayed(AMDHomePage.objAdBannerAboveCarousel)) {
				yCordinate = (int) ((Integer.valueOf(height)) * 0.4);
			} else {
				yCordinate = (int) ((Integer.valueOf(height)) * 0.5);
			}

			if (direction.equalsIgnoreCase("LEFT")) {

				for (int i = 0; i < count; i++) {

					int startx = (Integer.valueOf(width));
					startx = (int) (startx * 0.8);
					int endx = (int) (startx * 0.1);

					int starty = (Integer.valueOf(height)) + yCordinate;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
							.moveTo(PointOption.point(endx, starty)).release().perform();
					logger.info("Swiping screen in " + " " + direction + " direction" + " " + (i + 1) + " times");
					extent.extentLoggerPass("SwipeLeft",
							"Swiping screen in " + " " + direction + " direction" + " " + (i + 1) + " times");

					System.out.println("\n<<< Swipe <<<");
					System.out.println("[X,Y]: [" + startx + "," + starty + "] ===> [" + endx + "," + starty + "]");
				}
			} else if (direction.equalsIgnoreCase("RIGHT")) {

				for (int j = 0; j < count; j++) {
					int startx = (int) ((Integer.valueOf(width)) * 0.1);
					int endx = (int) ((Integer.valueOf(width)) * 0.8);
					int starty = (Integer.valueOf(height)) + yCordinate;
					touchAction.press(PointOption.point(startx, starty))
							.waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
							.moveTo(PointOption.point(endx, starty)).release().perform();

					logger.info("Swiping screen in " + " " + direction + " direction" + " " + (j + 1) + " times");
					extent.extentLoggerPass("SwipeRight",
							"Swiping screen in " + " " + direction + " direction" + " " + (j + 1) + " times");

					System.out.println("\n>>> Swipe >>>");
					System.out.println("[X,Y]: [" + startx + "," + starty + "] ===> [" + endx + "," + starty + "]");
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@SuppressWarnings("static-access")
	public void verifyPopUpLaunchEventForGetPremiumPopUp(String userType, String keyword2) throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode(
					"Verify Pop Up Launch Event when get premium popup is displayed on playing premium content");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeNowLinkOnPlayer, "Subscribe Now Link");
			setFEProperty(userType);
			mixpanel.ValidateParameter("", "Popup launch");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyPopUpLaunchEventForCompleteProfilePopUp(String usertype, String searchKeyword) throws Exception {
		if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			extent.HeaderChildNode("Verify Pop Up Launch Event when Complete Profile popup is displayed");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, searchKeyword + "\n", "Search bar");
			hideKeyboard();
			// closeInterstitialAd(AMDGenericObjects.objCloseInterstitialAd, 2000);
			// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");

			if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
				waitForAdToFinishInAmd();
			}
			completeProfilePopUpClose(usertype);

			setFEProperty(userType);
			mixpanel.ValidateParameter("", "Popup launch");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyPopUpLaunchEventForRegisterPopUp(String userType, String keyword) throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Verify Pop Up Launch Event when Native popup is displayed");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword + "\n", "Search bar");
			hideKeyboard();
			// closeInterstitialAd(AMDGenericObjects.objCloseInterstitialAd, 2000);
			// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");

			if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
				waitForAdToFinishInAmd();
			}
			registerPopUpClose();
			setFEProperty(userType);
			mixpanel.ValidateParameter("", "Popup launch");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyPopUpCTAsEvent(String userType, String keyword2) throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Pop Up CTA's Event when user clicks on CTA button displayed on the popup");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeNowLinkOnPlayer, "Subscribe Now Link");
			Swipe("Up", 1);
			verifyElementPresentAndClick(AMDConsumptionScreen.objProceedBtnOnSubscribePopUpInPlayerScreen,
					"Proceed CTA");
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Element", "Proceed");
			mixpanel.ValidateParameter("", "Pop Up CTAs");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyCTAsEvent(String userType, String tabName) throws Exception {
		extent.HeaderChildNode("Verify CTAs Event");
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
		waitTime(3000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "SUBSCRIBE");
		mixpanel.FEProp.setProperty("Page Name", "HomePage");

		mixpanel.ValidateParameter("", "CTAs");
	}

	public void findingTrayInscreen(int j, By byLocator1, By byLocator2, String str1, String str2, String userType,
			String tabName) throws Exception {
		boolean tray = false;
		for (int i = 0; i < j; i++) {
			if (!((verifyIsElementDisplayed(byLocator1)))) {
				Swipe("UP", 1);
			} else {
				verifyElementExist(byLocator1, str1);
				tray = true;
				if (tabName.equalsIgnoreCase("Home")) {
					if (str1.equalsIgnoreCase("Continue watching tray")) {

						Response respCW = ResponseInstance.getRespofCWTray(userType);

						List<String> ApinoOfContentsInCW = respCW.jsonPath().getList("array");
						logger.info("no.of contents in CW tray in Api " + ApinoOfContentsInCW.size());

						ArrayList<String> listOfContentsInCW = new ArrayList<String>();

						for (int k = 0; k < ApinoOfContentsInCW.size(); k++) {

							String title = respCW.jsonPath().getString("[" + k + "].title");
							listOfContentsInCW.add(title);
						}

						logger.info(listOfContentsInCW);

						for (int p = 0; p < ApinoOfContentsInCW.size(); p++) {

							verifyElementExist(AMDHomePage.objContentTitleOfCWTray(listOfContentsInCW.get(p)),
									"content title");

							if (verifyElementDisplayed(AMDHomePage.objLeftTimeOfFirstContentOfCWTray)) {
								logger.info("Left watch time info on cards is available");
								extent.extentLoggerPass("Left watch time info",
										"Left watch time info on cards is available");
							} else {
								logger.error("Left watch time info on cards is not available");
								extent.extentLoggerFail("Left watch time info",
										"Left watch time info on cards is not available");
							}
							if (verifyElementDisplayed(AMDHomePage.objProgressBarOfFirstContentOfCWTray)) {
								logger.info("Progress bar is displayed to indicate the content watched portion");
								extent.extentLoggerPass("Progress bar",
										"Progress bar is displayed to indicate the content watched portion");
							} else {
								logger.error("Progress bar is not displayed to indicate the content watched portion");
								extent.extentLoggerFail("Progress bar",
										"Progress bar is not displayed to indicate the content watched portion");
							}
							if (p != (ApinoOfContentsInCW.size() - 1)) {
								SwipeRail(AMDHomePage.objContentTitleOfCWTray(listOfContentsInCW.get(p + 1)));
							}
						}
					}
				}
				break;
			}
		}
		if (tray == false) {
			if (userType.equalsIgnoreCase("Guest")) {
				if (str1.equalsIgnoreCase("Continue watching tray")) {
					logger.info(str1 + " is not displayed for Guest user");
					extent.extentLoggerPass("Tray", str1 + " is not displayed for Guest user");
				} else {
					logger.error(str1 + " is not displayed");
					extent.extentLoggerWarning("Tray", str1 + " is not displayed");
				}
			} else {
				if (tabName.equalsIgnoreCase("Home")) {

					if (str1.equalsIgnoreCase("Continue watching tray")) {

						Response respCW = ResponseInstance.getRespofCWTray(userType);

						List<String> ApinoOfContentsInCW = respCW.jsonPath().getList("array");
						logger.info("no.of contents in CW tray in Api " + ApinoOfContentsInCW.size());

						if (ApinoOfContentsInCW.size() == 0) {

							logger.info(str1 + " is not displayed for this user");
							extent.extentLoggerPass("Tray", str1 + " is not displayed for this user");
						} else {
							logger.error(str1 + " is not displayed for this user");
							extent.extentLoggerWarning("Tray", str1 + " is not displayed for this user");
						}
					}
					logger.error(str1 + " is not displayed");
					extent.extentLoggerWarning("Tray", str1 + " is not displayed");
				} else {
					logger.error(str1 + " is not displayed");
					extent.extentLoggerWarning("Tray", str1 + " is not displayed");
				}
			}
		}
		for (int i = 0; i < j; i++) {
			if (!(verifyIsElementDisplayed(byLocator2))) {
				Swipe("DOWN", 1);
			} else {
				verifyElementExist(byLocator2, str2);
				break;
			}
		}
	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationDOBEnteredEvent(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Registration DOB entered event");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(5000);
			String pEmailID = generateRandomString(5) + "@gmail.com";
			type(AMDRegistrationScreen.objEmailIDTextField, pEmailID, "Email field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDRegistrationScreen.objProceedBtn, "Proceed button");
			verifyElementPresent(AMDRegistrationScreen.objScreenTitle, "Register for Free screen");
			click(AMDRegistrationScreen.objDOBTxtField, "DOB");
			type(AMDRegistrationScreen.objDOBTxtField, "01/01/1990", "DOB");
			hideKeyboard();
			waitTime(3000);

			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "Registration");
			mixpanel.FEProp.setProperty("Source", "LoginRegister");
			mixpanel.ValidateParameter("", "Registration DoB Entered");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationGenderEnteredEvent(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Registration Gender entered event");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(5000);
			String pEmailID = generateRandomString(5) + "@gmail.com";
			type(AMDRegistrationScreen.objEmailIDTextField, pEmailID, "Email field");
			verifyElementPresentAndClick(AMDRegistrationScreen.objProceedBtn, "Proceed button");
			verifyElementPresent(AMDRegistrationScreen.objScreenTitle, "Register for Free screen");
			verifyElementPresentAndClick(AMDRegistrationScreen.objGederTxtField, "Gender field");
			verifyElementPresentAndClick(AMDRegistrationScreen.objMale, "Gender male");
			waitTime(3000);
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "Registration");
			mixpanel.FEProp.setProperty("Source", "Login");
			mixpanel.ValidateParameter("", "Registration Gender Entered");
		}
	}

	public void verifyRegistrationUserNameEnteredEvent(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Registration userName entered event");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(5000);
			String pEmailID = generateRandomString(5) + "@gmail.com";
			type(AMDRegistrationScreen.objEmailIDTextField, pEmailID, "Email field");
			verifyElementPresentAndClick(AMDRegistrationScreen.objProceedBtn, "Proceed button");
			verifyElementPresent(AMDRegistrationScreen.objScreenTitle, "Register for Free screen");
			type(AMDRegistrationScreen.objFirstNameTxtField, FirstName, "First name field");
			hideKeyboard();
			type(AMDRegistrationScreen.objLastNameTxtField, LastName, "Last name field");
			hideKeyboard();
			waitTime(3000);

			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "Registration");
			mixpanel.FEProp.setProperty("Source", "Login");
			mixpanel.ValidateParameter("", "Registration First Name Entered");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationPasswordEnteredEvent(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Registration Password entered event");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(5000);
			String pEmailID = generateRandomString(5) + "@gmail.com";
			type(AMDRegistrationScreen.objEmailIDTextField, pEmailID, "Email field");
			verifyElementPresentAndClick(AMDRegistrationScreen.objProceedBtn, "Proceed button");
			verifyElementPresent(AMDRegistrationScreen.objScreenTitle, "Register for Free screen");
			click(AMDRegistrationScreen.objPasswordTxtField, "Passowrd");
			type(AMDRegistrationScreen.objPasswordTxtField, "123456", "Password field");
			hideKeyboard();
			waitTime(3000);
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "Registration");
			mixpanel.FEProp.setProperty("Source", "Login");
			mixpanel.ValidateParameter("", "Registration Password Entered");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyChangePasswordStartedEvent(String userType) throws Exception {
		extent.HeaderChildNode("Change Password started event");
		if (!(userType.equalsIgnoreCase("Guest"))) {
			String oldPassword = "123456";
			String NewPassword = "654321";
			
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(5000);
			verifyElementPresentAndClick(AMDMyProfileScreen.objChangePassword, "Change Password");
			verifyElementPresentAndClick(AMDChangePasswordScreen.objCurrentPwdField, "Current Password field");
			type(AMDChangePasswordScreen.objCurrentPwdField, oldPassword, "Current Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objNewPwdField, "New Password field");
			type(AMDChangePasswordScreen.objNewPwdField, NewPassword, "New Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objConfirmPwdField, "Confirm Password field");
			type(AMDChangePasswordScreen.objConfirmPwdField, NewPassword, "Confirm Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objUpdateBtn, "Update button");
			waitTime(5000);

			verifyElementPresentAndClick(AMDMyProfileScreen.objChangePassword, "Change Password");
			verifyElementPresentAndClick(AMDChangePasswordScreen.objCurrentPwdField, "Current Password field");
			type(AMDChangePasswordScreen.objCurrentPwdField, NewPassword, "Current Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objNewPwdField, "New Password field");
			type(AMDChangePasswordScreen.objNewPwdField, oldPassword, "New Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objConfirmPwdField, "Confirm Password field");
			type(AMDChangePasswordScreen.objConfirmPwdField, oldPassword, "Confirm Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objUpdateBtn, "Update button");
			waitTime(3000);

			setFEProperty(generatedEmailId, oldPassword);
			
			MixpanelAndroid.FEProp.setProperty("Page Name", "ChangePassword");
			MixpanelAndroid.FEProp.setProperty("Source", "MyProfile");
			MixpanelAndroid.FEProp.setProperty("Element", "Update");
			MixpanelAndroid.FEProp.setProperty("Partner Name", "N/A");

			MixpanelAndroid.ValidateParameter("", "Change Password Started");

		}else {
			logger.info("Change Password Event is Not applicable for Guest user");
			extentLogger("Change Password", "Change Password Event is Not applicable for Guest user");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyChangePasswordResultEvent(String userType) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Change Password result event");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(5000);
			verifyElementPresentAndClick(AMDMyProfileScreen.objChangePassword, "Change Password");
			verifyElementPresentAndClick(AMDChangePasswordScreen.objCurrentPwdField, "Current Password field");
			type(AMDChangePasswordScreen.objCurrentPwdField, "123456", "Current Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objNewPwdField, "New Password field");
			type(AMDChangePasswordScreen.objNewPwdField, "1234567", "New Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objConfirmPwdField, "Confirm Password field");
			type(AMDChangePasswordScreen.objConfirmPwdField, "1234567", "Confirm Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objUpdateBtn, "Update button");
			waitTime(5000);

			verifyElementPresentAndClick(AMDMyProfileScreen.objChangePassword, "Change Password");
			verifyElementPresentAndClick(AMDChangePasswordScreen.objCurrentPwdField, "Current Password field");
			type(AMDChangePasswordScreen.objCurrentPwdField, "1234567", "Current Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objNewPwdField, "New Password field");
			type(AMDChangePasswordScreen.objNewPwdField, "123456", "New Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objConfirmPwdField, "Confirm Password field");
			type(AMDChangePasswordScreen.objConfirmPwdField, "123456", "Confirm Password field");
			hideKeyboard();
			verifyElementPresentAndClick(AMDChangePasswordScreen.objUpdateBtn, "Update button");
			waitTime(3000);

			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "ChangePassword");
			mixpanel.FEProp.setProperty("Source", "MyProfile");
			mixpanel.FEProp.setProperty("Element", "ChangePassword");

			mixpanel.ValidateParameter("", "Change Password Result");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyVideoAutoPlayChangeEventForEnable() throws Exception {
		extent.HeaderChildNode("Verify video AutoPlay change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		String elementAutoPlayToggleStatus = getText(AMDMoreMenu.objVideo_Autoply);
		if (elementAutoPlayToggleStatus.equalsIgnoreCase("ON")) {
			logger.info("the default state of the 'Auto Play' option is in ON state");
			extentLoggerWarning("Video Auto Play", "the default state of the 'Auto Play' option is in ON state");
		} else {
			click(AMDMoreMenu.objVideo_Autoply, "Video Auto play toggle");
			waitTime(4000);
			click(AMDMoreMenu.objVideo_Autoply, "Video Auto play toggle");

			mixpanel.FEProp.setProperty("Source", "More");
			mixpanel.FEProp.setProperty("Page Name", "user_setting");

			mixpanel.ValidateParameter("", "Video Streaming Autoplay Changed");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyVideoAutoPlayChangeEventforDisable() throws Exception {
		extent.HeaderChildNode("Verify video AutoPlay change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		String elementAutoPlayToggleStatus = getText(AMDMoreMenu.objVideo_Autoply);
		if (elementAutoPlayToggleStatus.equalsIgnoreCase("ON")) {
			click(AMDMoreMenu.objVideo_Autoply, "Video Auto play toggle");
			waitTime(5000);
			click(AMDMoreMenu.objVideo_Autoply, "Video Auto play toggle");

			mixpanel.FEProp.setProperty("Source", "More");
			mixpanel.FEProp.setProperty("Page Name", "user_setting");
			mixpanel.ValidateParameter("", "Video Streaming Autoplay Changed");

		} else {
			logger.info("the default state of the 'Auto Play' option is not in ON state");
			extentLoggerWarning("Video Auto Play", "the default state of the 'Auto Play' option is not in ON state");
		}
	}

	public void verifyAddtoWatchlistFromPlaybackPageInPotrait(String userType, String keyword3) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Add to Watchlist Event From Playback Page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(3000);
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchlistBtn, "Watchlist icon");
			waitTime(5000);
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchlistBtn, "Watchlist icon");
			waitTime(5000);

			setFEProperty(userType);

			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Element", "search_results");
			mixpanel.ValidateParameter("", "add to Watchlist");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyRemoveFromWatchListPlaybackPageInPotrait(String userType, String keyword3) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Remove from Watchlist Event From Playback Page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(3000);
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchlistBtn, "Watchlist icon");
			waitTime(5000);
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchlistBtn, "Watchlist icon");
			waitTime(5000);

			setFEProperty(userType);

			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Element", "search_results");
			mixpanel.ValidateParameter("", "add to Watchlist");
			mixpanel.ValidateParameter("", "Remove from Watchlist");

		}
	}

	public void verifyPopUpLaunchEventForClubUser(String userType, String keyword6) throws Exception {
		if (userType.equalsIgnoreCase("ClubUser")) {
			extent.HeaderChildNode("Verify Pop Up Launch Event when user gets Upgrade popup for Club User");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword6 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(3000);
			verifyElementPresentAndClick(AMDClubPack.objUpgradeToPremiumLinkOnPlayer, "Upgrade button on player");
			verifyIsElementDisplayed(AMDClubPack.objUpgradePopUp, "Upgrade popUp");
			waitTime(3000);
		}
	}

	@SuppressWarnings("static-access")
	public void verifyContentBucketSwipeEvent() throws Exception {
		extent.HeaderChildNode("Verify Content Bucket Swipe Event Across tabs");
		waitForElementDisplayed(AMDHomePage.objHomeTab, 10);
		click(AMDHomePage.objPremiumTab, "Premium tab");
		waitTime(10000);
		SwipeRail(AMDHomePage.objContent);
		waitTime(3000);
		setFEProperty(userType);

		mixpanel.FEProp.setProperty("Source", "home");
		mixpanel.FEProp.setProperty("Tab Name", "premium");
		mixpanel.FEProp.setProperty("Page Name", "premium");

		mixpanel.ValidateParameter("", "Content Bucket Swipe");
	}

	@SuppressWarnings("static-access")
	public void verifyContentBucketSwipeEventInPlayBackPage(String keyword2) throws Exception {
		extent.HeaderChildNode("Verify Content Bucket Swipe Event in playback page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(3000);
		SwipeRail(AMDHomePage.objContent);
		waitTime(3000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.ValidateParameter("", "Content Bucket Swipe");

	}

	public void verifyProfileUpdateResultEvent(String userType) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Profile Update Result Event");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			waitTime(5000);
			verifyElementPresentAndClick(AMDProfileScreen.objEditBtn, "Edit");
			verifyElementPresentAndClick(AMDEditProfileScreen.objGenderDropdown, "Gender");
			String gender = getText(AMDEditProfileScreen.objSelectedGender);
			if (gender.equalsIgnoreCase("Male")) {
				click(AMDEditProfileScreen.objFemale, "Female option");
			} else {
				click(AMDEditProfileScreen.objMale, "Male option");
			}
			verifyElementPresentAndClick(AMDEditProfileScreen.objSaveChanges, "Save changes");

			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Source", "MyProfile");
			mixpanel.FEProp.setProperty("Settings Changed", "Profile Edit");
			mixpanel.FEProp.setProperty("Page Name", "EditProfile");
			mixpanel.FEProp.setProperty("Element", "Edit Profile");
			mixpanel.ValidateParameter("", "Setting Changed");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyAddtoWatchlistFromPlaybackPageInFullScreen(String userType, String keyword3) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Add to Watchlist Event From Playback Page in Full screen");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");

			click(AMDPlayerScreen.objThreeDotsOnPlayer, "Player option with 3 dots");
			verifyElementPresentAndClick(AMDPlayerScreen.objAddToWatchlist, "Add to Watchlist option");
			waitTime(6000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			click(AMDPlayerScreen.objThreeDotsOnPlayer, "Player option with 3 dots");
			verifyElementPresentAndClick(AMDPlayerScreen.objAddToWatchlist, "Add to Watchlist option");
			waitTime(5000);

			setFEProperty(userType);

			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Element", "search_results");

			mixpanel.ValidateParameter("", "add to Watchlist");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyRemoveFromWatchlistFromPlaybackPageInFullScreen(String userType, String keyword3)
			throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Remove from  Watchlist Event From Playback Page in Full screen");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");

//			click(AMDPlayerScreen.objThreeDotsOnPlayer, "Player option with 3 dots");
			JSClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Player option with 3 dots");
			verifyElementPresentAndClick(AMDPlayerScreen.objAddToWatchlist, "Add to Watchlist option");
			waitTime(6000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			JSClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			JSClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Player option with 3 dots");
			verifyElementPresentAndClick(AMDPlayerScreen.objAddToWatchlist, "Add to Watchlist option");
			waitTime(5000);
			setFEProperty(userType);

			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Element", "search_results");
			mixpanel.ValidateParameter("", "Remove from Watchlist");
		}
	}

	public void videoViewEventForPremiumContentInPotrait(String usertype, String tabName) throws Exception {
		if (userType.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode("Video View Event for premium content in potrait");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
					waitTime(3000);
					Back(1);
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		}
	}

	public void videoViewEventForPremiumContentInFullScreen(String usertype, String tabName) throws Exception {
		if (userType.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode("Video View Event for premium content in full screen");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
					waitTime(6000);
					click(AMDPlayerScreen.objPlayerScreen, "Player screen");
					verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
					waitTime(4000);
					Back(2);
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		}
	}

	public void videoViewEventForCarouselContentInPotrait(String tabName) throws Exception {
		extent.HeaderChildNode("Video View Event for carousel content in potrait");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 20);
		Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (var == true) {
			logger.info("Player screen is displayed");
			extentLoggerPass("Player screen", "Player screen is displayed");
		} else if (verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer)) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerPass("Player screen", "Player inline subscription link is displayed");
		}
		Back(1);
	}

	public void videoViewEventForCarouselContentInFullScreen(String tabName) throws Exception {
		extent.HeaderChildNode("Video View Event for carousel content in full screen");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 20);
		Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (var == true) {
			logger.info("Player screen is displayed");
			extentLoggerPass("Player screen", "Player screen is displayed");
			waitTime(6000);
			click(AMDPlayerScreen.objPlayerScreen, "Player screen");
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
			waitTime(4000);
			Back(2);
		} else if (verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer)) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerPass("Player screen", "Player inline subscription link is displayed");
		}
	}

	public void videoViewEventOfcontentFromSearchPageInPotrait(String keyword3) throws Exception {
		extent.HeaderChildNode("Video View Event of content from search page in potrait");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		Back(1);
	}

	public void videoViewEventOfcontentFromSearchPageInFullScreen(String keyword3) throws Exception {
		extent.HeaderChildNode("Video View Event of content from search page in Full screen");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		waitTime(6000);
		click(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(4000);
		Back(2);
	}

	public void videoViewEventOfContentFromMyWatchListPageInPotrait(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Video View Event of content from My WatchList page in potrait");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			boolean contentsInMoviesTab = getDriver()
					.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
					.isDisplayed();
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
				Back(3);
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
				Back(2);
			}
		}

	}

	public void videoViewEventOfContentFromMyWatchListPageInFullScreen(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Video View Event of content from My WatchList page in full screen");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			boolean contentsInMoviesTab = getDriver()
					.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
					.isDisplayed();
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
				if (var == true) {
					logger.info("Player screen is displayed");
					extentLoggerPass("Player screen", "Player screen is displayed");
					waitTime(6000);
					click(AMDPlayerScreen.objPlayerScreen, "Player screen");
					verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
					waitTime(4000);
					Back(4);
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
				Back(2);
			}
		}
	}

	public void verifyRemoveFromWatchlistFromWatchListPage(String userType) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Remove an content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			boolean flag = false;
			click(AMDUserSessionManagement.objShowsTabUnderWatchList, "Shows Tab");
			if (verifyIsElementDisplayed(AMDWatchlistPage.objEditBtn)) {
				verifyElementPresentAndClick(AMDWatchlistPage.objEditBtn, "Edit button");
				flag = true;
			} else {
				click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
				if (verifyIsElementDisplayed(AMDWatchlistPage.objEditBtn)) {
					verifyElementPresentAndClick(AMDWatchlistPage.objEditBtn, "Edit button");
					flag = true;
				} else {
					click(AMDUserSessionManagement.objVideosTabUnderWatchList, "Videos Tab");
					if (verifyIsElementDisplayed(AMDWatchlistPage.objEditBtn)) {
						verifyElementPresentAndClick(AMDWatchlistPage.objEditBtn, "Edit button");
						flag = true;
					} else {
						logger.info("No contents in watchlist to remove");
						extentLogger("Remove contents from watchlist page", "No contents in watchlist to remove");
					}
				}

			}
			if (flag == true) {
				verifyElementPresentAndClick(AMDWatchlistPage.objSelectContentByIndex(1), "check box");
				verifyElementPresentAndClick(AMDWatchlistPage.objDeleteAllBtn, "Delete All");

			}

		}

	}

	@SuppressWarnings("static-access")
	public void verifyVideoStreamOverWifiChangeEventForEnable() throws Exception {
		extent.HeaderChildNode("Verify video wifi change Event for Enable");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		verifyElementPresentAndClick(AMDMoreMenu.objVideo_WifiOnly, "Wifi only Switch");
		waitTime(3000);
		verifyElementPresentAndClick(AMDMoreMenu.objVideo_WifiOnly, "Wifi only Switch");
		waitTime(3000);

		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.ValidateParameter("", "Video Stream Over Wifi Changed");

	}

	@SuppressWarnings("static-access")
	public void verifyVideoStreamOverWifiChangeEventForDisable() throws Exception {
		extent.HeaderChildNode("Verify video wifi change Event for Disable");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		verifyElementPresentAndClick(AMDMoreMenu.objVideo_WifiOnly, "Wifi only Switch");
		waitTime(3000);
		verifyElementPresentAndClick(AMDMoreMenu.objVideo_WifiOnly, "Wifi only Switch");
		waitTime(3000);
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.ValidateParameter("", "Video Stream Over Wifi Changed");
	}

	@SuppressWarnings("static-access")
	public void verifyDownloadQualityChangeEvent(String option) throws Exception {
		extent.HeaderChildNode("Verify Download quality change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		
		if (!(pUserType.equalsIgnoreCase("Guest"))) {
			if(pUserType.equalsIgnoreCase("SubscribedUser")) {
				Username = getParameterFromXML("SubscribedUserName");
				Password = getParameterFromXML("SubscribedPassword");
			}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				Username = getParameterFromXML("NonSubscribedUserName");
				Password = getParameterFromXML("NonSubscribedUserPassword");
			}
	      	mixpanel.FEProp.setProperty("Old Download Quality Setting", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("download_quality"));
		}else {
			mixpanel.FEProp.setProperty("Old Download Quality Setting", "Ask each time");
		}
		
		String downloadQuality = getText(AMDMoreMenu.objDownloads_Quality);
		getDriver().findElement(By.xpath("//*[@id='downloadLabel']")).click();
		if(downloadQuality.equalsIgnoreCase("Ask each time")) {
			verifyElementPresentAndClick(AMDSettingsScreen.objDownloadVideoQualityOptions(option), "download quality option");
		}else {
			verifyElementPresentAndClick(AMDSettingsScreen.objVideoQualityAskEachTime, "Ask each time option");
		}
		
		setFEProperty(pUserType);
		setUserType_SubscriptionProperties(pUserType);
		
		
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "selector");
		mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
		mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
	
		if(pUserType.equalsIgnoreCase("Guest")) {
			mixpanel.FEProp.setProperty("User Type", "guest");
			mixpanel.FEProp.setProperty("New Download Quality Setting", option);
		}
		
		mixpanel.ValidateParameter("", "Download Quality Changed");

		click(AMDMoreMenu.objDownloads_Quality, "Download quality option");
		if(downloadQuality.equalsIgnoreCase("Ask each time")) {
			verifyElementPresentAndClick(AMDSettingsScreen.objDownloadVideoQualityOptions(option), "download quality option");
		}else {
			verifyElementPresentAndClick(AMDSettingsScreen.objVideoQualityAskEachTime, "Ask each time option");
		}	

	}

	@SuppressWarnings("static-access")
	public void verifyDownloadOverWifiChangeEventForEnable() throws Exception {
		extent.HeaderChildNode("Verify Download Over wifi change Event for Enable");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		verifyElementPresentAndClick(AMDMoreMenu.objDownloads_WifiOnly, "Download over wifi only switch");
		waitTime(10000);
		verifyElementPresentAndClick(AMDMoreMenu.objDownloads_WifiOnly, "Download over wifi only switch");
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.ValidateParameter("", "Download Over Wifi Changed");
	}

	@SuppressWarnings("static-access")
	public void verifyDownloadOverWifiChangeEventForDisable() throws Exception {
		extent.HeaderChildNode("Verify Download Over wifi change Event for Disable");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		verifyElementPresentAndClick(AMDMoreMenu.objDownloads_WifiOnly, "Download over wifi only switch");
		waitTime(10000);
		verifyElementPresentAndClick(AMDMoreMenu.objDownloads_WifiOnly, "Download over wifi only switch");
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.ValidateParameter("", "Download Over Wifi Changed");
	}

	@SuppressWarnings("static-access")
	public void verifyDisplayLanguageChangeFromWelcomePage(String usertype, String dsl) throws Exception {
		if (usertype.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Verify Display Language Change event from Welcome page");
			click(AMDOnboardingScreen.objSelectDisplayLang(dsl), "language");
			verifyElementPresentAndClick(AMDOnboardingScreen.objDiplay_ContinueBtn,
					"Continue button in Display language Page");
			waitTime(5000);
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "DisplayLanguage");
			mixpanel.ValidateParameter("", "Display Language Changed");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyContinueLanguageFromWelcomePage(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Verify Content Language Change event from Welcome page");
			
			click(AMDOnboardingScreen.objgetContentLangName(1), "Content Language");
			boolean flag = false;
			flag = verifyElementPresentAndClick(AMDOnboardingScreen.objContent_ContinueBtn, "Continue button in Content language page");
			waitTime(5000);
		
			if(flag){
				mixpanel.FEProp.setProperty("User Type", "guest");
				mixpanel.FEProp.setProperty("Source", "N/A");
				mixpanel.FEProp.setProperty("Page Name", "ContentLanguage");
				mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
				mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
				mixpanel.FEProp.setProperty("New Video Streaming Quality Setting", "Auto");
				mixpanel.FEProp.setProperty("New Autoplay Setting", "true");
				mixpanel.FEProp.setProperty("New Stream Over Wifi Setting", "false");
				mixpanel.FEProp.setProperty("New Download Quality Setting", "Ask Each Time");
				mixpanel.FEProp.setProperty("New Download Over Wifi Setting", "false");
				mixpanel.FEProp.setProperty("New App Language", "en");
				mixpanel.FEProp.setProperty("New Content Language", "en,kn,hi");
				mixpanel.FEProp.setProperty("Old Content Language", "en,kn");

				mixpanel.ValidateParameter("", "Content Language Changed");
			} else {
				logger.info("Failed to change content language");
				extentLoggerWarning("Event", "Failed to content language");
			}


		}
	}

	public void videoViewEventForCarouselContent(String tabName) throws Exception {
		extent.HeaderChildNode("Video View Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 20);
		Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (var == true) {
			logger.info("Player screen is displayed");
			extentLoggerPass("Player screen", "Player screen is displayed");
		} else if (verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer)) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerPass("Player screen", "Player inline subscription link is displayed");
		}

		waitTime(3000);
		if (var == true) {
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("Video View", "1");

			String pContentId = mixpanel.fetchContentId("", "Video View");
			String pDistinctId = mixpanel.DistinctId;
			ResponseInstance.getContentDetails(pContentId);

			mixpanel.ValidateParameter(pDistinctId, "Video View");
		}
	}

	public void videoViewEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Video View Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		Back(2);
		waitTime(4000);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Video View", "1");

		String pContentId = mixpanel.fetchContentId("", "Video View");
		String pDistinctId = mixpanel.DistinctId;
		ResponseInstance.getContentDetails(pContentId);

		mixpanel.ValidateParameter(pDistinctId, "Video View");
	}

	public void videoViewEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Video View Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(5000);
				verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
				flag = true;
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
			if (flag) {
				waitTime(3000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Video View", "1");

				String pContentId = mixpanel.fetchContentId("", "Video View");
				String pDistinctId = mixpanel.DistinctId;
				ResponseInstance.getContentDetails(pContentId);

				mixpanel.ValidateParameter(pDistinctId, "Video View");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}
	}

	public void videoViewEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify Video View event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(2000);
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		waitTime(5000);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Video View", "1");

		String pContentId = Mixpanel.fetchContentId("", "Video View");
		String pDistinctId = mixpanel.DistinctId;
		ResponseInstance.getContentDetails(pContentId);

		mixpanel.ValidateParameter(pDistinctId, "Video View");
	}

	public void videoViewEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Video View event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		waitTime(5000);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Video View", "1");

		String pContentId = mixpanel.fetchContentId("", "Video View");
		String pDistinctId = mixpanel.DistinctId;
		ResponseInstance.getContentDetails(pContentId);

		mixpanel.ValidateParameter(pDistinctId, "Video View");
	}

	public void videoExitEventForPremiumContent(String usertype, String tabName) throws Exception {
		if (userType.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode("Video Exit Event for premium content in potrait");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false, flag = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
					waitTime(3000);
					flag = true;
					Back(1);
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (flag) {
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				Mixpanel.ValidateParameter("", "Video Exit");
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		}
	}

	public void videoExitEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify Video Exit event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 10);
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchlistBtn, "Watch Trailer button");
			Back(1);
		}
		waitTime(5000);
		Back(1);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "home");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");

		Mixpanel.ValidateParameter("", "Video Exit");
	}

	public void videoExitEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Video Exit Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);

		boolean flagBox = verifyIsElementDisplayed(AMDHomePage.objSboxIcon);
		String pSugarBox = String.valueOf(flagBox);

		String contentName = ResponseInstance.getCarouselContentFromAPI(usertype, tabName);
		System.out.println(contentName);

		for (int i = 0; i < 3; i++) {
			if (verifyElementDisplayed(AMDHomePage.objCarouselContentTitle(contentName))) {
				click(AMDHomePage.objCarouselContentTitle(contentName), "carousal content");
				break;
			}
		}

		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		if (usertype.equalsIgnoreCase("Guest")) {
			registerPopUpClose();
		}
		completeProfilePopUpClose(usertype);

		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLogger("Player screen", "Player inline subscription link is displayed");
		} else {
			waitTime(5000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			boolean eventFlag = false;
			verifyElementPresent(AMDPlayerScreen.objPlayer, "Player screen");
			waitTime(2000);
			Back(1);
			eventFlag = verifyElementNotPresent(AMDPlayerScreen.objPlayerScreen, 5);
			waitTime(2000);

			if (!eventFlag) {
				if (usertype.equalsIgnoreCase("SubscribedUser")) {
					ResponseInstance.subscriptionDetails();
				}
				String pPage = getPageName(tabName);
				String pSource = getSource(tabName);
				String pManufacturer = DeviceDetails.OEM;
				// String pAdId = getAdId();

				setFEProperty(usertype);

//				mixpanel.FEProp.setProperty("Ad ID", pAdId);
//				mixpanel.FEProp.setProperty("Advertisement ID", pAdId);
				mixpanel.FEProp.setProperty("Source", pSource);
				mixpanel.FEProp.setProperty("Page Name", pPage);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Manufacturer", pManufacturer);
				mixpanel.FEProp.setProperty("Brand", pManufacturer);
				mixpanel.FEProp.setProperty("Sugar Box Value", pSugarBox);

				mixpanel.ValidateParameter("", "Video Exit");
			} else {
				logger.info("Failed to Exit the Video");
				extentLoggerWarning("Event", "Failed to Exit the video");
			}
		}
	}

	public void videoExitEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Video Exit Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		waitTime(4000);
		Back(2);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");

		mixpanel.ValidateParameter("", "Video Exit");
	}

	public void videoExitEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Video Exit Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(5000);
				verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
				Back(3);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");

				Mixpanel.ValidateParameter("", "Video Exit");

			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
				Back(2);
			}
		}

	}

	public void videoExitEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Video Exit event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		PartialSwipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		Back(2);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");

		Mixpanel.ValidateParameter("", "Video Exit");
	}

	public void playerViewChangedEventForPremiumContent(String usertype, String tabName) throws Exception {
		if (userType.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode("Player View Changed Event for premium content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
					waitTime(4000);

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.ValidateParameter("", "Player View Changed");

					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		}
	}

	public void PlayerViewChangedEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Player View Changed event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 10);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(5000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Player View Changed");
	}

	public void PlayerViewChangedEventForCarouselContent(String tabName) throws Exception {
		extent.HeaderChildNode("Player View Changed Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 20);
		Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (var == true) {
			logger.info("Player screen is displayed");
			extentLoggerPass("Player screen", "Player screen is displayed");
			waitTime(6000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
			waitTime(5000);

			String pUserType = userType;
			setFEProperty(pUserType);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.ValidateParameter("", "Player View Changed");
		} else if (verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer)) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerPass("Player screen", "Player inline subscription link is displayed");
		}
	}

	public void PlayerViewChangedEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Player View Changed Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(4000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(4000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Player View Changed");
	}

	public void PlayerViewChangedEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Player View Changed Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(5000);
				waitForElementDisplayed(AMDPlayerScreen.objPlayer, 10);
				verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
				verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
				verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
				waitTime(4000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.ValidateParameter("", "Player View Changed");

			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		}

	}

	public void PlayerViewChangedEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Player View Changed event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		PartialSwipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(4000);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Player View Changed");
	}

	public void seekVideoTillLast(By byLocator1) throws Exception {
		String beforeSeek = findElement(AMDPlayerScreen.objTimer).getText();
		logger.info("Current time before seeking : " + timeToSec(beforeSeek));
		extent.extentLogger("Seek", "Current time before seeking in seconds: " + timeToSec(beforeSeek));

		WebElement element = getDriver().findElement(byLocator1);
		Dimension size = element.getSize();
		int startx = (int) (size.width);
		int startX = startx + 180;
		System.out.println(startX);
		SwipeAnElement(element, startX, 0);

		waitTime(2000);
		String afterSeek = findElement(AMDPlayerScreen.objTimer).getText();
		logger.info("Current time after seeking in seconds : " + timeToSec(afterSeek));
		extent.extentLogger("Seek", "Current time after seeking in seconds : " + timeToSec(afterSeek));

		String totalDur = findElement(AMDPlayerScreen.objTotalDuration).getText();
		if (timeToSec(afterSeek) > (timeToSec(totalDur) - 120)) {
			logger.info("Seeked the video till last");
			extentLoggerPass("Seeking the video till last", "Seeked the video till last");
			logger.info("Seek bar is functional");
			extent.extentLogger("Seek", "Seek bar is functional");
		} else {
			logger.info("Not seeked the video till last");
			extentLoggerFail("Seeking the video till last", "Not seeked the video till last");
			logger.info("Seek bar is not functional");
			extent.extentLoggerFail("Seek", "Seek bar is not functional");
		}
	}

	public void VideoWatchDurationEventForPremiumContentComplete(String usertype, String tabName) throws Exception {
		if (userType.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode(
					"Video Watch Duration Event for premium content when user completely watches the content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					scrubProgressBarTillEnd(AMDPlayerScreen.objProgressBar);
					waitTime(4000);
					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
					mixpanel.ValidateParameter("", "Video Watch Duration");
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		} else {
			logger.info("This validation is not applicable for " + usertype);
			extentLoggerPass("Premium Content", "This validation is not applicable for " + usertype);
		}
	}

	public void VideoWatchDurationEventForTrailerContentComplete(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode(
				"Video Watch Duration event for Trailer content when user completely watches the content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}

		scrubProgressBarTillEnd(AMDPlayerScreen.objProgressBar);
		waitTime(2000);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void VideoWatchDurationEventForCarouselContentComplete(String tabName) throws Exception {
		extent.HeaderChildNode(
				"Video Watch Duration Event for carousel content when user completely watches the content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 20);
		Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (var == true) {
			logger.info("Player screen is displayed");
			extentLoggerPass("Player screen", "Player screen is displayed");
			waitTime(6000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			scrubProgressBarTillEnd(AMDPlayerScreen.objProgressBar);
			waitTime(2000);

			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.ValidateParameter("", "Video Watch Duration");

		} else if (verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer)) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerPass("Player screen", "Player inline subscription link is displayed");
		}
	}

	public void VideoWatchDurationEventOfcontentFromSearchPageComplete(String usertype, String keyword4)
			throws Exception {
		extent.HeaderChildNode(
				"Video Watch Duration Event of content from search page when user completely watches the content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(4000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		scrubProgressBarTillEnd(AMDPlayerScreen.objProgressBar);
		waitTime(4000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");

		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void VideoWatchDurationEventOfContentFromMyWatchListPageComplete(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode(
					"Video Watch Duration Event of content from My WatchList page when user completely watches the content");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(5000);
				waitForElementDisplayed(AMDPlayerScreen.objPlayer, 10);
				scrubProgressBarTillEnd(AMDPlayerScreen.objProgressBar);
				waitTime(4000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");

				mixpanel.ValidateParameter("", "Video Watch Duration");
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Watchlist validation is not applicable for " + usertype);
		}
	}

	public void VideoWatchDurationEventOfContentFromUpNextRailComplete(String usertype, String keyword4)
			throws Exception {
		extent.HeaderChildNode(
				"Verify Video Watch Duration event of content from Upnext rail when user completely watches the content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		PartialSwipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		scrubProgressBarTillEnd(AMDPlayerScreen.objProgressBar);
		waitTime(4000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void scrubProgressBarTillEnd(By byLocator1) throws Exception {
		String beforeSeek = findElement(AMDPlayerScreen.objTimer).getText();
		logger.info("Current time before seeking : " + timeToSec(beforeSeek));
		extent.extentLogger("Seek", "Current time before seeking in seconds: " + timeToSec(beforeSeek));
		click(AMDPlayerScreen.objPlayerScreen, "Player screen");
		click(AMDPlayerScreen.objPauseIcon, "Pause");
		WebElement element = getDriver().findElement(byLocator1);
		String xDuration = getAttributValue("x", AMDPlayerScreen.objTotalDuration);
		int endX = Integer.parseInt(xDuration) - 30;
		SwipeAnElement(element, endX, 0);
		String afterSeek = findElement(AMDPlayerScreen.objTimer).getText();
		logger.info("Current time after seeking : " + timeToSec(afterSeek));
		extent.extentLogger("Seek", "Current time after seeking in seconds: " + timeToSec(afterSeek));
		if (!userType.equalsIgnoreCase("SubscribedUser")) {
			waitForAdToFinishInAmd();
		}
		click(AMDPlayerScreen.objPlayIcon, "Play");
		waitTime(6000);
	}

	public void VideoWatchDurationEventForPremiumContentAbrupt(String usertype, String tabName) throws Exception {
		if (userType.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode("Verify Video Watch Duration event of Premium content when video closed abruptly");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
					waitTime(10000);
					Back(1);
					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.ValidateParameter("", "Video Watch Duration");

					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		} else {
			logger.info("This validation is not applicable for " + usertype);
			extentLoggerPass("Premium Content", "This validation is not applicable for " + usertype);
		}
	}

	public void VideoWatchDurationEventForTrailerContentAbrupt(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify Video Watch Duration event for Trailer content when video closed abruptly");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 10);
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
			Back(1);
		}
		waitTime(8000);
		Back(1);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void VideoWatchDurationEventForCarouselContentAbrupt(String tabName) throws Exception {
		extent.HeaderChildNode("Video Watch Duration Event for carousel content when video closed abruptly");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 20);
		Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		if (var == true) {
			logger.info("Player screen is displayed");
			extentLoggerPass("Player screen", "Player screen is displayed");
		} else if (verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer)) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerPass("Player screen", "Player inline subscription link is displayed");
		}
		Back(1);

		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "home");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void VideoWatchDurationEventOfcontentFromSearchPageAbrupt(String usertype, String keyword4)
			throws Exception {
		extent.HeaderChildNode("Video Watch Duration Event of content from search page when video closed abruptly");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(3000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(4000);
		Back(2);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");

		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void VideoWatchDurationEventOfContentFromMyWatchListPageAbrupt(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode(
					"Video Watch Duration Event of content from My WatchList page when video closed abruptly");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
//			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(5000);
				verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
				Back(3);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.ValidateParameter("", "Video Watch Duration");

			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
				Back(2);
			}
		} else {
			logger.info("Watchlist validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Watchlist validation is not applicable for " + usertype);
		}
	}

	public void VideoWatchDurationEventOfContentFromUpNextRailAbrupt(String usertype, String keyword4)
			throws Exception {
		extent.HeaderChildNode(
				"Verify Video Watch Duration event of content from Upnext rail  when video closed abruptly");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
//		Swipe("UP", 1);
		PartialSwipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyIsElementDisplayed(AMDPlayerScreen.objPlayer);
		Back(2);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void AdInitializedEvent(String userType, String contentType, String contentID, String contentName)
			throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			SearchContentFromSearchPage(userType, contentType, contentID, contentName);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitTime(5000);

				setFEProperty(userType);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Initialized");

			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}

		}
	}

	public void AdInitializedEventForTrailerContent(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad Initialized event for Trailer content");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitTime(5000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Initialized");

			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}

		}
	}

	public void AdInitializedEventForCarouselContent(String usertype, String tabName) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Initialized Event for carousel content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
			waitTime(10000);
			Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (var == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Initialized");

			} else {
				logger.info("Ad is not Displayed");
				extentLogger("Ad", "Ad is not Displayed");
			}
		}
	}

	public void AdInitializedEventForContentFromTray(String usertype, String tabName) throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Initialized Event for content from Tray");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false, ad = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					waitTime(10000);
					ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
					if (ad == true) {
						logger.info("Ad is Displayed");
						extentLogger("Ad", "Ad is Displayed");
					} else {
						logger.info("Ad is not displayed");
						extentLogger("Ad", "Ad is not displayed");
					}
					waitTime(3000);
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (ad == true) {
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Initialized");
			}

			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		}
	}

	public void AdInitializedEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Initialized Event of content from search page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Initialized");
			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(4000);
		}
	}

	public void AdInitializedEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("NonSubscribedUser")) {
			extent.HeaderChildNode("Ad Initialized Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(10000);
				boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
				if (ad == true) {
					logger.info("Ad is Displayed");
					extentLogger("Ad", "Ad is Displayed");

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
					mixpanel.FEProp.setProperty("Ad Location", "instream");
					mixpanel.ValidateParameter("", "Ad Initialized");

				} else {
					logger.info("Ad is not displayed");
					extentLogger("Ad", "Ad is not displayed");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist ad validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Watchlist ad validation is not applicable for " + usertype);
		}
	}

	public void AdInitializedEventOfContentFromUpNextRail(String usertype, String keyword5) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad Initialized event of content from Upnext rail");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword5 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Initialized");

			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(5000);
		}
	}

	public void AdViewEventForTrailerContent(String usertype, String keyword3) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad View event for Trailer content");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitTime(5000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

				mixpanel.ValidateParameter("", "Ad view");

			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Ad", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdViewEventForCarouselContent(String usertype, String tabName) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad View Event for carousel content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
			waitTime(10000);
			Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (var == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitTime(2000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

				mixpanel.ValidateParameter("", "Ad view");
			} else {
				logger.info("Ad is not Displayed");
				extentLogger("Ad", "Ad is not Displayed");
			}
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Ad", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdViewEventForContentFromTray(String usertype, String tabName) throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad View Event for content from Tray");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					waitTime(10000);
					boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
					if (ad == true) {
						logger.info("Ad is Displayed");
						extentLogger("Ad", "Ad is Displayed");

						waitTime(3000);
						setFEProperty(usertype);
						mixpanel.FEProp.setProperty("Source", "home");
						mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
						mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

						mixpanel.ValidateParameter("", "Ad view");
					} else {
						logger.info("Ad is not displayed");
						extentLogger("Ad", "Ad is not displayed");
					}
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Ad", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdViewEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad View Event of content from search page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");

				waitTime(2000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

				mixpanel.ValidateParameter("", "Ad view");
			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(4000);
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Ad", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdViewEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("NonSubscribedUser")) {
			extent.HeaderChildNode("Ad View Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(10000);
				boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
				if (ad == true) {
					logger.info("Ad is Displayed");
					extentLogger("Ad", "Ad is Displayed");
					waitTime(2000);
					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
					mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

					mixpanel.ValidateParameter("", "Ad view");
				} else {
					logger.info("Ad is not displayed");
					extentLogger("Ad", "Ad is not displayed");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Ad", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdViewEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad View event of content from Upnext rail");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitTime(2000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

				mixpanel.ValidateParameter("", "Ad view");
			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(5000);
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Ad", "Ad validation is not applicable for " + usertype);
		}
	}

	public void videoViewEventForPremiumContent(String usertype, String tabName) throws Exception {
		if (userType.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode("Video View Event for premium content in potrait");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			waitForElementDisplayed(AMDHomePage.objCarouselTitle, 60);
			Swipe("UP", 1);
			String pUsername = null, pPwd = null;
			switch (userType) {
			case "NonSubscribedUser":
				pUsername = getParameterFromXML("NonSubscribedUserName");
				pPwd = getParameterFromXML("NonSubscribedUserPassword");
				break;

			case "SubscribedUser":
				pUsername = getParameterFromXML("SubscribedUserName");
				pPwd = getParameterFromXML("SubscribedPassword");
				break;
			}

			boolean var = false;
			for (int i = 0; i < 3; i++) {
				waitTime(1000);
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					waitTime(5000);
					verifyIsElementDisplayed(AMDPlayerScreen.objPlayer, "Player screen");
					waitTime(3000);
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == true) {

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Video View", "1");

				String pContentId = mixpanel.fetchContentId("", "Video View");
				String pDistinctId = mixpanel.DistinctId;
				ResponseInstance.getContentDetails(pContentId);

				mixpanel.ValidateParameter(pDistinctId, "Video View");
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		} else {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void AdClickEventForTrailerContent(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad Click event for Trailer content");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				verifyElementPresentAndClick(AMDPlayerScreen.objLearnMoreTextOnAdPlayBack, "Learn More");
				waitTime(4000);

				setFEProperty(usertype);

				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

				mixpanel.ValidateParameter("", "Ad click");

			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(5000);
		}

	}

	public void AdClickEventForCarouselContent(String usertype, String tabName) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Click Event for carousel content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
			waitTime(10000);
			Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (var == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				verifyElementPresentAndClick(AMDPlayerScreen.objLearnMoreTextOnAdPlayBack, "Learn More");
				waitTime(4000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

				mixpanel.ValidateParameter("", "Ad click");
			} else {
				logger.info("Ad is not Displayed");
				extentLogger("Ad", "Ad is not Displayed");
			}
		}

	}

	public void AdClickEventForContentFromTray(String usertype, String tabName) throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Click Event for content from Tray");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					waitTime(10000);
					boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
					if (ad == true) {
						logger.info("Ad is Displayed");
						extentLogger("Ad", "Ad is Displayed");
						verifyElementPresentAndClick(AMDPlayerScreen.objLearnMoreTextOnAdPlayBack, "Learn More");
						waitTime(4000);

						setFEProperty(usertype);
						mixpanel.FEProp.setProperty("Source", "home");
						mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

						mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
						mixpanel.FEProp.setProperty("Ad Location", "instream");
						mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

						mixpanel.ValidateParameter("", "Ad click");
					} else {
						logger.info("Ad is not displayed");
						extentLogger("Ad", "Ad is not displayed");
					}
					waitTime(3000);
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		}
	}

	public void AdClickEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Click Event of content from search page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				verifyElementPresentAndClick(AMDPlayerScreen.objLearnMoreTextOnAdPlayBack, "Learn More");
				waitTime(4000);
			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(4000);

			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
			mixpanel.FEProp.setProperty("Ad Location", "instream");
			mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

			mixpanel.ValidateParameter("", "Ad click");
		}

	}

	public void AdClickEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("NonSubscribedUser")) {
			extent.HeaderChildNode("Ad Click Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(10000);
				boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
				if (ad == true) {
					logger.info("Ad is Displayed");
					extentLogger("Ad", "Ad is Displayed");
					verifyElementPresentAndClick(AMDPlayerScreen.objLearnMoreTextOnAdPlayBack, "Learn More");
					waitTime(4000);

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
					mixpanel.FEProp.setProperty("Ad Location", "instream");
					mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

					mixpanel.ValidateParameter("", "Ad click");
				} else {
					logger.info("Ad is not displayed");
					extentLogger("Ad", "Ad is not displayed");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Watchlist is not applicable for " + usertype);
		}
	}

	public void AdClickEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad click event of content from Upnext rail");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				verifyElementPresentAndClick(AMDPlayerScreen.objLearnMoreTextOnAdPlayBack, "Learn More");
				waitTime(4000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.FEProp.setProperty("Ad Position", "Pre-Roll");

				mixpanel.ValidateParameter("", "Ad click");
			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(5000);
		}
	}

	public void AdWatchDurationEventForTrailerContent(String usertype, String keyword) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad Watch Duration event for Trailer content");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitForAdToFinishInAmd();
				waitTime(4000);
				Back(1);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Watch Duration");

			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(5000);
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdWatchDurationEventForCarouselContent(String usertype, String tabName) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Watch Duration Event for carousel content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
			waitTime(10000);
			Boolean var = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (var == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitForAdToFinishInAmd();
				waitTime(4000);
				Back(1);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Watch Duration");

			} else {
				logger.info("Ad is not Displayed");
				extentLogger("Ad", "Ad is not Displayed");
			}
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdWatchDurationEventForContentFromTray(String usertype, String tabName) throws Exception {
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Watch Duration Event for content from Tray");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);
			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					waitTime(10000);
					boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
					if (ad == true) {
						logger.info("Ad is Displayed");
						extentLogger("Ad", "Ad is Displayed");
						waitForAdToFinishInAmd();
						waitTime(4000);
						Back(1);
						setFEProperty(usertype);
						mixpanel.FEProp.setProperty("Source", "home");
						mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

						mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
						mixpanel.FEProp.setProperty("Ad Location", "instream");
						mixpanel.ValidateParameter("", "Ad Watch Duration");
					} else {
						logger.info("Ad is not displayed");
						extentLogger("Ad", "Ad is not displayed");
					}
					waitTime(3000);
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdWatchDurationEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Ad Watch Duration Event of content from search page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitForAdToFinishInAmd();
				waitTime(4000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Watch Duration");

			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(4000);
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Ad validation is not applicable for " + usertype);
		}
	}

	public void AdWatchDurationEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("NonSubscribedUser")) {
			extent.HeaderChildNode("Ad Watch Duration Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(10000);
				boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
				if (ad == true) {
					logger.info("Ad is Displayed");
					extentLogger("Ad", "Ad is Displayed");
					waitForAdToFinishInAmd();
					waitTime(4000);

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
					mixpanel.FEProp.setProperty("Ad Location", "instream");
					mixpanel.ValidateParameter("", "Ad Watch Duration");
				} else {
					logger.info("Ad is not displayed");
					extentLogger("Ad", "Ad is not displayed");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist validation is not applicable for " + usertype);
			extentLoggerPass("Watchlist", "Watchlist validation is not applicable for " + usertype);
		}
	}

	public void AdWatchDurationEventOfContentFromUpNextRail(String usertype, String keyword) throws Exception {
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			extent.HeaderChildNode("Verify Ad Watch Duration event of content from Upnext rail");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(10000);
			boolean ad = verifyIsElementDisplayed(AMDPlayerScreen.objAd);
			if (ad == true) {
				logger.info("Ad is Displayed");
				extentLogger("Ad", "Ad is Displayed");
				waitForAdToFinishInAmd();
				waitTime(4000);
				Back(1);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Ad Title", "In-Stream Video");
				mixpanel.FEProp.setProperty("Ad Location", "instream");
				mixpanel.ValidateParameter("", "Ad Watch Duration");
			} else {
				logger.info("Ad is not displayed");
				extentLogger("Ad", "Ad is not displayed");
			}
			waitTime(5000);
		} else {
			logger.info("Ad Validation is not applicable for " + usertype);
			extentLoggerPass("Ad", "Ad validation is not applicable for " + usertype);
		}
	}

	public void PauseEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Pause Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(3000);
				boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
				if (inlineLink == true) {
					logger.info("Player inline subscription link is displayed");
					extentLogger("Player screen", "Player inline subscription link is displayed");
				} else {
					waitTime(6000);
					verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
					verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
					waitTime(2000);

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.ValidateParameter("", "Pause");
				}
				waitTime(3000);
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void PauseEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify Pause event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(5000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Pause");
	}

	public void PauseEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Pause Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);

		boolean flagBox = verifyIsElementDisplayed(AMDHomePage.objSboxIcon);
		String pSugarBox = String.valueOf(flagBox);

		String contentName = ResponseInstance.getCarouselContentFromAPI(usertype, tabName);
		System.out.println(contentName);

		for (int i = 0; i < 3; i++) {
			if (verifyElementDisplayed(AMDHomePage.objCarouselContentTitle(contentName))) {
				click(AMDHomePage.objCarouselContentTitle(contentName), "carousal content");
				break;
			}

		}

		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		if (usertype.equalsIgnoreCase("Guest")) {
			registerPopUpClose();
		}
		completeProfilePopUpClose(usertype);

		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLogger("Player screen", "Player inline subscription link is displayed");
		} else {
			waitTime(5000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			boolean eventFlag = false;
			eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			waitTime(2000);

			if (eventFlag) {
				if (usertype.equalsIgnoreCase("SubscribedUser")) {
					ResponseInstance.subscriptionDetails();
				}
				String pPage = getPageName(tabName);
				String pSource = getSource(tabName);
				String pManufacturer = DeviceDetails.OEM;
				// String pAdId = getAdId();

				setFEProperty(usertype);

//				mixpanel.FEProp.setProperty("Ad ID", pAdId);
//				mixpanel.FEProp.setProperty("Advertisement ID", pAdId);
				mixpanel.FEProp.setProperty("Source", pSource);
				mixpanel.FEProp.setProperty("Page Name", pPage);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Manufacturer", pManufacturer);
				mixpanel.FEProp.setProperty("Brand", pManufacturer);
				mixpanel.FEProp.setProperty("Sugar Box Value", pSugarBox);

				mixpanel.ValidateParameter("", "Pause");
			} else {
				logger.info("Failed to perform Event Action");
				extentLoggerWarning("Event Action", "Failed to perform Event Action");
			}
		}

	}

	public void PauseEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Pause Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(5000);
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(2000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(2000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Pause");
	}

	public void PauseEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Pause Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
				waitTime(2000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.ValidateParameter("", "Pause");
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}
	}

	public void PauseEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Pause event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		// waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		// Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(2000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Pause");
	}

	public void PauseEventForLinearContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Pause Event for Linear content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		waitTime(5000);
		boolean checkLoadingicon = verifyElementNotPresent(AMDLiveTVScreen.objLoadingIcon, 30);
		if (!checkLoadingicon) {
			logger.info("Continuous Loading icon is displayed in Live TV section");
			extentLoggerWarning("Loading Icon", "Continuous Loading icon is displayed in Live TV section");
		} else {
			verifyElementPresentAndClick(AMDLiveTVScreen.objFirstContentCardOfFreeChannelsTray, "linear content");
			if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
				waitForAdToFinishInAmd();
			}
			registerPopUpClose();
			completeProfilePopUpClose(usertype);
			waitTime(6000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			waitTime(2000);

			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.ValidateParameter("", "Pause");
		}
	}

	public void seekVideo(By byLocator, String usertype) throws Exception {

		String beforeSeek = findElement(AMDPlayerScreen.objTimer).getText();
		logger.info("Current time before seeking : " + timeToSec(beforeSeek));
		extent.extentLogger("Seek", "Current time before seeking in seconds: " + timeToSec(beforeSeek));
		WebElement element = getDriver().findElement(byLocator);
		Dimension size = element.getSize();
		int startx = (int) (size.width);

		SwipeAnElement(element, startx, 0);
		logger.info("Scrolling the seek bar");
		extent.extentLogger("Seek", "Scrolling the seek bar");

		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		waitTime(5000);
		boolean icon = verifyIsElementDisplayed(AMDPlayerScreen.objPlayIcon, "Play icon");
		if (icon == false) {
			waitTime(6000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		}
		String afterSeek = findElement(AMDPlayerScreen.objTimer).getText();
		logger.info("Current time after seeking in seconds : " + timeToSec(afterSeek));
		extent.extentLogger("Seek", "Current time after seeking in seconds : " + timeToSec(afterSeek));
		if (timeToSec(afterSeek) > timeToSec(beforeSeek)) {
			logger.info("Seek bar is functional");
			extent.extentLoggerPass("Seek", "Seek bar is functional");
		} else {
			logger.info("Seek bar is not functional");
			extent.extentLoggerFail("Seek", "Seek bar is not functional");
		}
	}

	public void SwipeAnElement(WebElement element, int posx, int posy) {
		AndroidTouchAction touch = new AndroidTouchAction(getDriver());
		touch.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(element)))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(posx, posy))
				.release().perform();
	}

	public static int timeToSec(String s) {
		String[] t = s.split(":");
		int num = 0;
		// System.out.println(t.length);
		if (t.length == 2) {
			num = Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]); // minutes since 00:00
		}
		if (t.length == 3) {
			num = ((Integer.parseInt(t[0]) * 60) * 60) + Integer.parseInt(t[1]) * 60 + Integer.parseInt(t[2]);
		}
		return num;
	}

	public void QualityChangeEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Quality Change Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(3000);
				boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
				if (inlineLink == true) {
					logger.info("Player inline subscription link is displayed");
					extentLogger("Player screen", "Player inline subscription link is displayed");
				} else {
					waitTime(8000);
					verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
					verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
					verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
					verifyElementPresentAndClick(AMDPlayerScreen.objQuality, "Quality");
					String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
					if (selectedQualityOption.equalsIgnoreCase("Auto")) {
						verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Quality option");
						waitTime(2000);
						setFEProperty(usertype);
						mixpanel.ValidateParameter("", "Qualitiy Change");

					} else {
						verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Quality option");
						waitTime(2000);
						setFEProperty(usertype);
						mixpanel.ValidateParameter("", "Qualitiy Change");
					}
				}
				waitTime(3000);
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void QualityChangeEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify Quality Change event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		waitTime(8000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
		verifyElementPresentAndClick(AMDPlayerScreen.objQuality, "Quality");
		String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
		if (selectedQualityOption.equalsIgnoreCase("Auto")) {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		} else {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		}
		waitTime(5000);

	}

	public void QualityChangeEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Quality Change Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		
		boolean flagBox = verifyIsElementDisplayed(AMDHomePage.objSboxIcon);
		String pSugarBox = String.valueOf(flagBox);
		
		String contentName = ResponseInstance.getCarouselContentFromAPI(usertype, tabName);
		System.out.println(contentName);
		
		waitForElementAndClickIfPresent(AMDHomePage.objContentTitle(contentName), 60, "carousal content");

		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		if(usertype.equalsIgnoreCase("Guest")) {
			registerPopUpClose();
		}
		completeProfilePopUpClose(usertype);
		
		
		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		boolean adulterrormsg = verifyIsElementDisplayed(AMDPlayerScreen.objAdultErrorMessage);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerWarning("Player screen", "Player inline subscription link is displayed");
		}else if(adulterrormsg == true){
			logger.info("error message saying 'This content is for Adult view only' is displayed");
			extentLoggerWarning("Player screen", "error message saying 'This content is for Adult view only' is displayed");
		}else {
			waitTime(5000);
			if(!(verifyIsElementDisplayed(AMDPlayerScreen.objFullscreenIcon))) {
				click(AMDPlayerScreen.objPlayerScreen, "Player screen");
			}
			boolean eventFlag = false;
			click(AMDPlayerScreen.objPauseIcon, "Pause icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
			click(AMDPlayerScreen.objPlayerScreen, "Player screen");
			
			waitTime(5000);
//			click(AMDPlayerScreen.objThreeDotsOnPlayer, "Three dots");
			touchAction = new TouchAction(getDriver());
			touchAction.press(PointOption.point(1855, 94)).release().perform();
	        
			String quality = getText(AMDPlayerScreen.objQuality);
			String oldQuality = quality.substring(8, quality.length()-1);
			System.out.println(oldQuality);
			click(AMDPlayerScreen.objQuality, "Quality");
			if (verifyIsElementDisplayed(AMDPlayerScreen.objQualityOpt(2))) {
				waitTime(3000);
				Swipe("UP", 1);
				eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(3), "Quality option");
			} else {
				eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Quality option");
			}

			click(AMDPlayerScreen.objPauseIcon, "Pause icon");
			
			waitTime(2000);
			touchAction = new TouchAction(getDriver());
			touchAction.press(PointOption.point(1855, 94)).release().perform();
			
			String newQuality = getText(AMDPlayerScreen.objQuality);
			newQuality = newQuality.substring(8, newQuality.length()-1);
			System.out.println(newQuality);
			
			if (eventFlag) {
				String pPage = "ConsumptionPage";
				String pSource = getSource(tabName);
				String pManufacturer = DeviceDetails.OEM;
				//String pAdId = getAdId();
				
				setFEProperty(usertype);
				setUserType_SubscriptionProperties(usertype);
				
				//mixpanel.FEProp.setProperty("Ad ID", getParameterFromXML("AdID"));
				//mixpanel.FEProp.setProperty("Advertisement ID", getParameterFromXML("AdID"));
				mixpanel.FEProp.setProperty("Source", pSource);
				mixpanel.FEProp.setProperty("Page Name", pPage);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Manufacturer", pManufacturer);
				mixpanel.FEProp.setProperty("Brand", pManufacturer);
				mixpanel.FEProp.setProperty("New Quality", newQuality);
				mixpanel.FEProp.setProperty("Old Quality", oldQuality);
				mixpanel.FEProp.setProperty("Carousal Name", "N/A");
				//mixpanel.FEProp.setProperty("Sugar Box Value",pSugarBox );

				mixpanel.ValidateParameter("", "Video Streaming Quality Changed");
			} else {
				logger.info("Failed to change the quality option");
				extentLoggerWarning("Event", "Failed to change the quality option");
			}
		}
	}

	public void QualityChangeEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Quality Change Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
			boolean popUp = verifyIsElementDisplayed(AMDConsumptionScreen.objRegisterPopUp);
			if (popUp) {
				Back(1);
			}
		}

		waitTime(8000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
		verifyElementPresentAndClick(AMDPlayerScreen.objQuality, "Quality");
		String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
		if (selectedQualityOption.equalsIgnoreCase("Auto")) {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		} else {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		}
	}

	public void QualityChangeEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Quality Change Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				if (!usertype.equalsIgnoreCase("SubscribedUser")) {
					waitForAdToFinishInAmd();
					boolean popUp = verifyIsElementDisplayed(AMDConsumptionScreen.objRegisterPopUp);
					if (popUp) {
						Back(1);
					}
				}
				waitTime(8000);
				verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
				verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
				verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
				verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
				verifyElementPresentAndClick(AMDPlayerScreen.objQuality, "Quality");
				String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
				if (selectedQualityOption.equalsIgnoreCase("Auto")) {
					verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Quality option");
					waitTime(2000);
					setFEProperty(usertype);
					mixpanel.ValidateParameter("", "Qualitiy Change");
				} else {
					verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Quality option");
					waitTime(2000);
					setFEProperty(usertype);
					mixpanel.ValidateParameter("", "Qualitiy Change");
				}
				waitTime(4000);
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}

	}

	public void QualityChangeEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Quality Change event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
			boolean popUP = verifyIsElementDisplayed(AMDConsumptionScreen.objRegisterPopUp);
			if (popUP) {
				Back(1);
			}
		}

		waitTime(8000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
		verifyElementPresentAndClick(AMDPlayerScreen.objQuality, "Quality");
		String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
		if (selectedQualityOption.equalsIgnoreCase("Auto")) {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		} else {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		}
		waitTime(5000);

	}

	public void QualityChangeEventForLinearContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Quality Change Event for Linear content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		waitTime(3000);
		verifyElementPresentAndClick(AMDLiveTVScreen.objFirstContentCardOfFreeChannelsTray, "linear content");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
			boolean popUP = verifyIsElementDisplayed(AMDConsumptionScreen.objRegisterPopUp);
			if (popUP) {
				Back(1);
			}
		}

		waitTime(8000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
		verifyElementPresentAndClick(AMDPlayerScreen.objQuality, "Quality");
		String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
		if (selectedQualityOption.equalsIgnoreCase("Auto")) {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		} else {
			verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Quality option");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.ValidateParameter("", "Qualitiy Change");
		}
		waitTime(3000);
	}

	/**
	 * SKIP_LOGIN
	 */

	/**
	 * Function to verify Skip Login
	 */

	@SuppressWarnings("static-access")
	public void verifySkipLoginEventBrowseForFreeScreen(String userType) throws Exception {

		extent.HeaderChildNode("Verify Skip Login event when user navigated from Browse for Free");
		click(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip login button");
		waitTime(5000);

		setFEProperty(userType);

		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Element", "Skip");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Skip Login");
	}

	@SuppressWarnings("static-access")
	public void verifySkipLoginEventFromLoginCTA() throws Exception {

		extent.HeaderChildNode("Verify Skip Login event when user navigated from Login CTA");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Element", "Skip");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Skip Login");

	}

	@SuppressWarnings("static-access")
	public void verifySkipLogin_LoginInGetPremiumPopUp(String userType, String keyword2) throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode(
					"Verify Skip login event on clicking login in Get premium popup during content playback");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeNowLinkOnPlayer, "Subscribe Now Link");
			waitTime(5000);
			Swipe("Up", 1);
			verifyElementPresentAndClick(AMDPlayerScreen.objLoginCTA, "Login CTA");
			verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Element", "Skip");
			mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
			mixpanel.ValidateParameter("", "Skip Login");
		}
	}

	@SuppressWarnings("static-access")
	public void verifySkipLoginEventFromHamburgerMenu(String usertype) throws Exception {
		extent.HeaderChildNode("Verify Skip login event");
		if (usertype.equalsIgnoreCase("SubscribedUser") | (usertype.equalsIgnoreCase("NonSubscribedUser"))) {
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			Swipe("Up", 2);
			click(AMDHomePage.objLogout, "Logout");
			click(AMDHomePage.objLogoutPopUpLogoutButton, "Logout button");
			Swipe("Down", 1);
		} else {
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
		}
		verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
		verifyElementPresentAndClick(AMDLoginScreen.objSkipBtn, "Skip link");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Skip");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Skip Login");

	}

	/**
	 * Logout
	 */

	@SuppressWarnings("static-access")
	public void verifyLogoutEvent(String userType) throws Exception {
		extent.HeaderChildNode("Verify Logout Event for " + userType);
		if (!(userType.equalsIgnoreCase("Guest"))) {
			logout();
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Element", "Logout");
			mixpanel.FEProp.setProperty("Page Name", "Logout");
			mixpanel.FEProp.setProperty("Source", "More");
			mixpanel.ValidateParameter("", "Logout");
		}
	}

	public void logout() throws Exception {
		if (!(pUserType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Logout from the App");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			waitTime(3000);
			Swipe("Up", 1);
			SwipeUntilFindElement(AMDMoreMenu.objLogout, "UP");
			verifyElementPresentAndClick(AMDMoreMenu.objLogout, "Logout");
			verifyElementPresentAndClick(AMDMoreMenu.objLogoutPopup, "Logout popUp");
			verifyElementPresentAndClick(AMDMoreMenu.objLogoutButton, "Logout button");
			waitTime(3000);
			Swipe("Down", 1);
			SwipeUntilFindElement(AMDMoreMenu.objGuestUserAccount, "Down");
			verifyElementPresent(AMDMoreMenu.objGuestUserAccount, "Guest user Header");
			verifyElementPresentAndClick(AMDHomePage.objHome, "Home tab");
		}else {
			logger.info("Logout option is Not applicable for Guest user");
			extentLogger("Logout option", "Logout option Event is Not applicable for Guest user");
		}
	}

	/**
	 * LoginScreen Displayed
	 */

	/*
	 * Login Screen Display event
	 */

	@SuppressWarnings("static-access")
	public void verifyLoginScreenDisplayEventThroughBrowseForScreen(String userType) throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode(
					"Verify Login Screen Display Event By Clicking On Browse for free button in welcome screen");

			verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			String Username = getParameterFromXML("NonSubscribedUserName");
			type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
			verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
			
			waitTime(3000);
			boolean flag = false;
			flag = verifyIsElementDisplayed(AMDLoginScreen.objLoginScreenTitle, "Login screen title");
			hideKeyboard();
			if(flag){
				setFEProperty(userType);
			
				mixpanel.FEProp.setProperty("User Type", "guest");
				mixpanel.FEProp.setProperty("Source", "LoginRegister");
				mixpanel.FEProp.setProperty("Page Name", "Login");
				mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
				mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);

				mixpanel.ValidateParameter("", "Login Screen Display");
			} else {
				logger.info("Failed to navigate Login screen");
				extentLoggerWarning("Event", "Failed to navigate Login screen");
			}

		}
	}

	@SuppressWarnings("static-access")
	public void verifyLoginScreenDisplayEventThroughLoginLink(String userType) throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Verify Login Screen Display Event By Clicking On Login link in welcome screen");

			verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login link");
			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			String Username = getParameterFromXML("NonSubscribedUserName");
			type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
			verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
			waitTime(3000);
			verifyIsElementDisplayed(AMDLoginScreen.objLoginScreenTitle, "Login screen title");
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Source", "Login");
			mixpanel.FEProp.setProperty("Page Name", "Login");
			mixpanel.ValidateParameter("", "Login Screen Display");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyLoginScreenDisplayEventThroughMoreMenu(String userType) throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Verify Login Screen Display Event through MoreMenu");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");
			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			String Username = getParameterFromXML("NonSubscribedUserName");
			type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
			verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
			waitTime(3000);
			verifyIsElementDisplayed(AMDLoginScreen.objLoginScreenTitle, "Login screen title");
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Source", "Login");
			mixpanel.ValidateParameter("", "Login Screen Display");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyLoginScreenDisplayEventByClickingOnLoginButtonInGetPremiumPopUp(String userType, String keyword2)
			throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode(
					"Verify Login Screen Display Event By Clicking On Login Button In Get Premium Pop Up");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword2 + "\n", "Search bar");
			hideKeyboard();
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			verifyElementPresentAndClick(AMDPlayerScreen.objSubscribeNowLinkOnPlayer, "Subscribe Now Link");
			waitTime(2000);
			Swipe("Up", 1);
			verifyElementPresentAndClick(AMDPlayerScreen.objLoginCTA, "Login CTA");
			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			String Username = getParameterFromXML("NonSubscribedUserName");
			type(AMDLoginScreen.objEmailIdField, Username, "Email Field");
			verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
			waitTime(3000);
			verifyIsElementDisplayed(AMDLoginScreen.objLoginScreenTitle, "Login screen title");
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Source", "Login");
			mixpanel.ValidateParameter("", "Login Screen Display");
		}
	}

	/**
	 * LoginInitiated
	 */
	/*
	 * Navigation : Get Premium tag Event : Login Initatied
	 */

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventGPlusUserFromGetPremiumCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Google login from Login screen");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objGoogleBtn, "Google Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Social Network", "Google");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventFBUserFromGetPremiumCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Facebook login from Login screen");
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objfbBtn, "Facebook Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Social Network", "Facebook");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventTwitterUserFromGetPremiumCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Twitter login from Login screen");
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objtwitterBtn, "Twitter Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Social Network", "Twitter");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventMobileOTPFromGetPremiumCTA() throws Exception {

		HeaderChildNode(
				"Verify Login Initiated event is triggered when user select Mobile Number login from Login screen");
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "User@123", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "mobile");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventNonSubUserGetPremiumCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Email ID login from Login screen");
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "1234567", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "email");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventSubUserFromGetPremiumCTA() throws Exception {

		HeaderChildNode(
				"Verify Login Initiated event is triggered when user select Mobile Number login from Login screen");
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "rsvod3@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "123456", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "email");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginInitiatedEventForInvalidDataEmailGetPremiumCTA() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Initiated event is triggered when user enters invalid email login credentials");
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "098765409876", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "email");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginInitiatedEventForInvalidDataMobileGetPremiumCTA() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Initiated event is triggered when user enters invalid Mobile login credentials");
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "098765409876", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "mobile");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	/*
	 * Navigation : Login CTA Event : Login Initiated
	 */

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventGPlusUserFromLoginCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Google login from Login screen");
		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objGoogleBtn, "Google Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Google");
		mixpanel.FEProp.setProperty("Method", "Social");
		Mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventFBUserFromLoginCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Facebook login from Login screen");
		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objfbBtn, "Facebook Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Facebook");
		mixpanel.FEProp.setProperty("Method", "Social");
		Mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventTwitterUserFromLoginCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Twitter login from Login screen");
		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objtwitterBtn, "Twitter Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Twitter");
		mixpanel.FEProp.setProperty("Method", "Social");
		Mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventMobileOTPFromLoginCTA() throws Exception {

		HeaderChildNode(
				"Verify Login Initiated event is triggered when user select Mobile Number login from Login screen");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "User@123", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "mobile");
		Mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventNonSubUserLoginCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Email ID login from Login screen");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "1234567", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "email");
		Mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventSubUserFromLoginCTA() throws Exception {

		HeaderChildNode(
				"Verify Login Initiated event is triggered when user select Mobile Number login from Login screen");
		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "rsvod3@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "123456", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "email");
		Mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginInitiatedEventForInvalidDataEmailLoginCTA() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Initiated event is triggered when user enters invalid email login credentials");
		click(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "098765409876", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "email");
		Mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginInitiatedEventForInvalidDataMobileLoginCTA() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Initiated event is triggered when user enters invalid Mobile login credentials");

		click(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "098765409876", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Method", "mobile");
		Mixpanel.ValidateParameter("", "Login Initatied");
	}

	/*
	 * Event : Login Initiated Navigation : Browse for free CTA
	 */

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventGPlusUserFromBrowseForFreeCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Google login from Login screen");

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objGoogleBtn, "Google Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Google");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventFBUserFromBrowseForFreeCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Facebook login from Login screen");
		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objfbBtn, "Facebook Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Facebook");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventTwitterUserFromBrowseForFreeCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Twitter login from Login screen");

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objtwitterBtn, "Twitter Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Twitter");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventMobileOTPFromBrowseForFreeCTA() throws Exception {

		HeaderChildNode(
				"Verify Login Initiated event is triggered when user select Mobile Number login from Login screen");

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "User@123", "Password field");
		hideKeyboard();
		verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Method", "mobile");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventNonSubUserBrowseForFreeCTA() throws Exception {

		HeaderChildNode("Verify Login Initiated event is triggered when user select Email ID login from Login screen");

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "1234567", "Password field");
		hideKeyboard();
		verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Method", "email");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginIntiatedEventSubUserBrowseForFreeCTA() throws Exception {

		HeaderChildNode(
				"Verify Login Initiated event is triggered when user select Mobile Number login from Login screen");

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "rsvod3@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "123456", "Password field");
		hideKeyboard();
		verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Method", "email");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	@SuppressWarnings("static-access")
	public void verifyLoginInitiatedEventForInvalidDataEmailLogin() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Initiated event is triggered when user enters invalid email login credentials");

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "098765409876", "Password field");
		hideKeyboard();
		verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Method", "email");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");

	}

	@SuppressWarnings("static-access")
	public void verifyLoginInitiatedEventForInvalidDataMobileLogin() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Initiated event is triggered when user enters invalid Mobile login credentials");

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "098765409876", "Password field");
		hideKeyboard();
		verifyElementPresentAndClick(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Method", "mobile");
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.ValidateParameter("", "Login Initatied");
	}

	/**
	 * Login Result
	 */
	/*
	 * Event : Login Result Navigation : Browse for free CTA
	 */

	@SuppressWarnings("static-access")
	public void LoginResultEventFromBrowseforFreeTwitterUser() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Result event when user taps on Login button, Post entering valid credentials");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objtwitterBtn, "Twitter button");
		waitTime(4000);
		if (verifyIsElementDisplayed(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App")) {
			click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
		}
		waitTime(15000);
		if (verifyIsElementDisplayed(AMDHomePage.objHome, "Home tab")) {
			logger.info("User Logged in Successfully");
			extent.extentLogger("Logged in", "User Logged in Successfully");
			logout();
		} else if (verifyIsElementDisplayed(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App")) {
			click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
		} else {
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
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Twitter");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromBrowseforFreeCTANonSubUser() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Result event when user taps on Login button, Post entering valid credentials for Non subscribed user");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "123456", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromBrowseforFreeCTASubUser() throws Exception {
		HeaderChildNode("Verify Login Result event is triggered when user enters valid subscribed user credentials");
		relaunchToIntroScreen(true);
		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "rsvod3@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "1234566", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.ValidateParameter("", "Login Result");
	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromBrowseforFreeCTAMobileLogin() throws Exception {

		HeaderChildNode("Verify Login Result event is triggered when user enters valid Mobile number user credentials");

		click(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "User@1234", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromBrowseforFreeCTAMobileLoginInvalidData() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered when user enters Invalid Mobile number user credentials");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "dfghj@123", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromBrowseforFreeCTAInvalidEmailID() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Result event when user taps on Login button, Post entering Invalid credentials for Email ID");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "asdfghj", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.ValidateParameter("", "Login Result");

	}

	/*
	 * Event : Login Result Navigation : Login CTA
	 */

	@SuppressWarnings("static-access")
	public void LoginResultEventFromLoginCTATwitterUser() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Result event when user taps on Login button, Post entering valid credentials");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objtwitterBtn, "Twitter button");
		waitTime(4000);
		if (verifyIsElementDisplayed(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App")) {
			click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
		}
		waitTime(15000);
		if (verifyIsElementDisplayed(AMDHomePage.objHome, "Home tab")) {
			logger.info("User Logged in Successfully");
			extent.extentLogger("Logged in", "User Logged in Successfully");
			logout();
		} else if (verifyIsElementDisplayed(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App")) {
			click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
		} else {
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
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Intro");
		mixpanel.FEProp.setProperty("Social Network", "Twitter");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromLoginCTANonSubUser() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Result event when user taps on Login button, Post entering valid credentials for Non subscribed user");

		relaunchToIntroScreen(true);
		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "1234567", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromLoginCTASubUser() throws Exception {
		HeaderChildNode("Verify Login Result event is triggered when user enters valid subscribed user credentials");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "rsvod3@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "123456", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromLoginCTAMobileLogin() throws Exception {

		HeaderChildNode("Verify Login Result event is triggered when user enters valid Mobile number user credentials");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "User@123", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromLoginCTAMobileLoginInvalidData() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered when user enters Invalid Mobile number user credentials");
		relaunchToIntroScreen(true);
		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "dfghj@123", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromLoginCTAInvalidEmailID() throws Exception {

		extent.HeaderChildNode(
				"Verify Login Result event when user taps on Login button, Post entering Invalid credentials for Email ID and Password");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "asdfghj", "Password field");
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	/*
	 * Event : Login Result Navigation : Get Premium CTA
	 */

	@SuppressWarnings("static-access")
	public void LoginResultEventFromGetPremiumCTATwitterLogin() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered  when user navigates to Login Screen from Get Premium CTA");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objtwitterBtn, "Twitter button");
		waitTime(4000);
		if (verifyIsElementDisplayed(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App")) {
			click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
		}
		waitTime(20000);
		if (verifyIsElementDisplayed(AMDPlayerScreen.objPlayerScreen, "Player Screen")) {
			logger.info("User Logged in Successfully");
			extent.extentLogger("Logged in", "User Logged in Successfully");
			Back(1);
			logout();
		} else if (verifyIsElementDisplayed(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App")) {
			click(AMDLoginScreen.objAuthorizeAppInTwitterpage, "Authorize App");
		} else {
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
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Social");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Social Network", "Twitter");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromGetPremiumCTANonSubUser() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered  when user navigates to Login Screen from Get Premium CTA");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "1234567", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromGetPremiumCTASubUser() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered  when user navigates to Login Screen from Get Premium CTA");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "rsvod3@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "123456", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");
	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromGetPremiumCTAMobileLogin() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered  when user navigates to Login Screen from Get Premium CTA");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "User@123", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");
	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromGetPremiumCTAMobileLoginInvlaidData() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered  when user navigates to Login Screen from Get Premium CTA for invali data");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "9880710182", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "asdf@123", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	@SuppressWarnings("static-access")
	public void LoginResultEventFromGetPremiumCTAEmailInvlaidData() throws Exception {

		HeaderChildNode(
				"Verify Login Result event is triggered  when user navigates to Login Screen from Get Premium CTA for invalid data");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email ID/Mobile Number");
		type(AMDLoginScreen.objEmailIdField, "zeebuild@gmail.com", "Email ID/Mobile Number");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password field");
		type(AMDLoginScreen.objPasswordField, "asdf@123", "Password field");
		hideKeyboard();
		click(AMDLoginScreen.objLoginBtn, "Login button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Login");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Login Result");

	}

	/*
	 * Event : TV Authentication Screen Display
	 */
	public void verifyTVAuthenticationScreenDisplayEvent(String userType) throws Exception {
		if (!(userType.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify TV Authentication Screen Display Event");
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings");
			waitTime(3000);
			Swipe("Up", 1);
			verifyElementPresentAndClick(AMDSettingsScreen.objAuthenticateDevice, "Authenticate Device");
			waitTime(10000);
		}
	}

	/**
	 * Register screen
	 */
	/*
	 * Register Screen Display Event
	 */

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromBrowseForFreeCTAEmailIDLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen post entering un-registered Email ID");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "unregemailtovalidate@gmail.com", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");

	}

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromBrowseForFreeCTAMobileLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen post entering un-registered Mobile Number");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "7656545374", "Email ID field");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");

	}

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromLoginCTAEmailIDLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen post entering un-registered Email ID");

		verifyElementPresentAndClick(AMDLoginScreen.objLoginLnk, "Login link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "unregemailtovalidate@gmail.com", "Email ID field");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");

	}

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromLoginCTAMobileLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen post entering un-registered Mobile Number");

		verifyElementPresentAndClick(AMDLoginScreen.objLoginLnk, "Login link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "7656545374", "Email ID field");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");

	}

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromProfilePageEmailIDLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen from Profile post entering un-registered Email ID");

		click(AMDHomePage.objMoreMenu, "More menu button");
		if (userType.equalsIgnoreCase("SubscribedUser") | userType.equalsIgnoreCase("NonSubscribedUser")) {
			Swipe("Up", 2);
			click(AMDMoreMenu.objLogoutBtn, "Logout button");
			click(AMDHomePage.objLogout, "Logout");
			click(AMDHomePage.objLogoutPopUpLogoutButton, "Logout button");
			Swipe("Down", 2);
		}

		click(AMDMoreMenu.objLoginRegisterText, "Profile icon");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "unregemailtovalidate@gmail.com", "Email ID field");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");
	}

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromProfilePageMobileLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen from Profile post entering un-registered Mobile Number");

		click(AMDHomePage.objMoreMenu, "More menu button");
		if (userType.equalsIgnoreCase("SubscribedUser") | userType.equalsIgnoreCase("NonSubscribedUser")) {
			Swipe("Up", 2);
			click(AMDMoreMenu.objLogoutBtn, "Logout button");
			click(AMDHomePage.objLogout, "Logout");
			click(AMDHomePage.objLogoutPopUpLogoutButton, "Logout button");
			Swipe("Down", 2);
		}

		click(AMDMoreMenu.objLoginRegisterText, "Profile icon");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "7656545374", "Email ID field");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");
	}

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromGetPremiumPageEmailIDLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen from Get Premium post entering un-registered Email ID Login");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "unregemailtovalidate@gmail.com", "Email ID field");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");

	}

	@SuppressWarnings("static-access")
	public void verifyRegisterScreenDisplayEventFromGetPremiumPageMobileLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Screen Display event is triggered  when user navigates to Registration screen from Get Premium post entering un-registered Mobile Number");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "7656545374", "Email ID field");
		click(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(5000);

		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
		mixpanel.ValidateParameter("", "Register Screen Display");

	}

	/**
	 * Register Initiated
	 */
	/*
	 * Event : Registration Initiated Navigation : Browse for Free
	 */

	public String createNewEmail() {
		String iD = generateRandomInt(100000);
		String emailID = "AutoUser" + iD + "@gmail.com";
		return emailID;
	}

	public void fillRegisterForm() throws Exception {
		HeaderChildNode("Entering all the data in Register screen");

		verifyElementPresentAndClick(AMDRegistrationScreen.objFirstNameTxtField, "First Name field");
		type(AMDRegistrationScreen.objFirstNameTxtField, "Name", "First Name field");
		verifyElementPresentAndClick(AMDRegistrationScreen.objLastNameTxtField, "Last Name field");
		type(AMDRegistrationScreen.objLastNameTxtField, "Name", "Last Name field");
		verifyElementPresentAndClick(AMDRegistrationScreen.objDOBTxtField, "Dob field");
		type(AMDRegistrationScreen.objDOBTxtField, "25/10/1996", "DOBTxtField field");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objGederTxtField, "Gender field");
		click(AMDRegistrationScreen.objMale, "Male gender");
		verifyElementPresentAndClick(AMDRegistrationScreen.objPasswordTxtField, "Password field");
		type(AMDRegistrationScreen.objPasswordTxtField, "123456", "Password field");
		hideKeyboard();

	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationInitiatedEventFromBrowseForFreeCTAEmailLogin() throws Exception {

		HeaderChildNode(
				"Verify Registration Initiated event is triggered when user navigates from browse for free CTA and entered all valid data");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.ValidateParameter("", "Registration Initiated");
	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationInitiatedEventFromBrowseForFreeCTAMobileLogin() throws Exception {

		HeaderChildNode(
				"Verify Registration Initiated event when user navigates from Browse for free CTA and entering Invalid data");
		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.ValidateParameter("", "Registration Initiated");
	}

	/*
	 * Event : Registration Initiated Navigation : LoginCTA
	 */

	@SuppressWarnings("static-access")
	public void verifyRegistrationInitiatedEventFromLoginCTAEmailLogin() throws Exception {

		HeaderChildNode(
				"Verify Registration Initiated event is triggered when user navigates from Login CTA and entered all valid data");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Initiated");
	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationInitiatedEventFromLoginCTAMobileLogin() throws Exception {

		HeaderChildNode(
				"Verify Registration Initiated event when user navigates from Login CTA and entering Invalid data");
		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Initiated");
	}

	/*
	 * Event : Registration Initiated Navigation : Get Premium CTA
	 */

	@SuppressWarnings("static-access")
	public void verifyRegistrationInitiatedEventFromGetPremiumCTAValidEmailLogin() throws Exception {

		HeaderChildNode(
				"Verify Registration Initiated event triggered when user navigates from Get Premium CTA for Valid Email Login");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Initiated");

	}

	@SuppressWarnings("static-access")
	public void verifyRegistrtionInitiatedEventFroGetPremiumCTAMobileLoginInvalidData() throws Exception {

		HeaderChildNode(
				"Verify Register Initiated event is triggered when user navigates from Get Premium CTA for invalid Mobile Login");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Initiated");

	}

	/*
	 * Event : Registration Initiated Navigation : Hamburger Menu
	 */

	@SuppressWarnings("static-access")
	public void verifyRegistrationInitiatedEventFromHamburgerMenuValidEmailLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Initiated event is triggered when user is navigated from Hamburger menu for valid email data");

		verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
		verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");

		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Initiated");

	}

	@SuppressWarnings("static-access")
	public void verifyRegistrtionInitiatedEventFromHamburgerMenuMobileLoginInvalidData() throws Exception {

		HeaderChildNode(
				"Verify Register Initiated event is triggered when user navigates from Hamburger menu for invalid Mobile Login");

		verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
		verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");

		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(1000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Initiated");
	}

	/**
	 * Register Result
	 */
	/*
	 * Event : Registration Result Navigation : Browse for Free
	 */

	@SuppressWarnings("static-access")
	public void verifyRegistrationResultEventFromBrowseForFreeCTAValidEmailRegistration() throws Exception {

		HeaderChildNode(
				"Verify Registration Result event is triggered when user navigates from Browse for Free CTA for valid email data registration");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.ValidateParameter("", "Registration Result");

	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationResultEventFromBrowseForFreeCTAInvalidMobileDataRegistration() throws Exception {

		HeaderChildNode(
				"Verify Registration Result event is triggered when user navigates from Browse for free CTA for invalid mobile registration");

		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Verify Mobile");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Source", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.ValidateParameter("", "Registration Result");

	}

	/*
	 * Event : Registration Result Navigation : Login CTA
	 */

	@SuppressWarnings("static-access")
	public void verifyRegistrationResultEventFromLoginCTAValidEmailRegistration() throws Exception {
		HeaderChildNode(
				"Verify Registration Result event is triggered when user navigates from Login CTA for Valid email registration");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Result");

	}

	@SuppressWarnings("static-access")
	public void verifyRegistrationResultEventFromLoginCTACTAInvalidMobileDataRegistration() throws Exception {

		HeaderChildNode(
				"Verify Registration Result event is triggered when user navigates from Login CTA for invalid Mobile Registration");

		verifyElementPresentAndClick(AMDOnboardingScreen.objLoginLnk, "Login Link");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Verify Mobile");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.FEProp.setProperty("Source", "Registration");
		mixpanel.ValidateParameter("", "Registration Result");

	}

	/*
	 * Event : Registration Result Navigation : Get Premium CTA
	 */

	@SuppressWarnings("static-access")
	public void verifyRegistrationResultEventFromGetPremiumCTAValidEmailLogin() throws Exception {

		HeaderChildNode(
				"Verify Registration Result event is triggered when user is navigates from Get Premium CTA for valid Email Registration");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Result");

	}

	@SuppressWarnings("static-access")
	public void verifyRegistrtionResultEventFroGetPremiumCTAMobileLoginInvalidData() throws Exception {

		HeaderChildNode(
				"Verify Register Result event is triggered when user navigates from Get Premium CTA for invalid Mobile Login");

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search button");
		verifyElementPresentAndClick(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, "Shivaji Surathkal", "Search bar");
		hideKeyboard();
		click(AMDSearchScreen.objSearchResult("Shivaji Surathkal"), "Search Result");
		verifyElementPresentAndClick(AMDConsumptionScreen.objGetPremiumCTA, "Get Premium CTA");
		waitTime(3000);
		Swipe("Up", 1);
		verifyElementPresentAndClick(AMDConsumptionScreen.objLoginCTA, "Login CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(5000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Verify Mobile");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.ValidateParameter("", "Registration Result");

	}

	/*
	 * Event : Registration Result Navigation : Hamburger menu
	 */

	@SuppressWarnings("static-access")
	public void verifyRegistrationResultEventFromHamburgerMenuValidEmailLogin() throws Exception {

		HeaderChildNode(
				"Verify Register Result event is triggered when user is navigated from Hamburger menu for valid email data");

		verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
		verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");

		String email = createNewEmail();
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, email, "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(10000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Registration");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Email");
		mixpanel.FEProp.setProperty("Source", "Login");
		mixpanel.ValidateParameter("", "Registration Result");

	}

	@SuppressWarnings("static-access")
	public void verifyRegistrtionResultEventFromHamburgerMenuMobileLoginInvalidData() throws Exception {

		HeaderChildNode(
				"Verify Register Result event is triggered when user navigates from Hamburger menu for invalid Mobile Login");

		verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
		verifyElementPresentAndClick(AMDMoreMenu.objProfile, "My account");

		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email/Phone Number");
		type(AMDLoginScreen.objEmailIdField, "5647393638", "Email ID field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed button");
		waitTime(3000);
		fillRegisterForm();
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(5000);
		type(AMDRegistrationScreen.objOTPField1, "1", "OTP box1");
		type(AMDRegistrationScreen.objOTPField2, "1", "OTP box2");
		type(AMDRegistrationScreen.objOTPField3, "1", "OTP box3");
		type(AMDRegistrationScreen.objOTPField4, "1", "OTP box4");
		hideKeyboard();
		verifyElementPresentAndClick(AMDRegistrationScreen.objVerifyOtpButton, "Verify button");
		waitTime(1000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Page Name", "Verify Mobile");
		mixpanel.FEProp.setProperty("Element", "Registration");
		mixpanel.FEProp.setProperty("Method", "Mobile");
		mixpanel.ValidateParameter("", "Registration Result");
	}

	/**
	 * Prepaid Code & Promo Code
	 */

	/*
	 * Event : Preapid Code Result
	 */

	@SuppressWarnings("static-access")
	public void verifySubscriptionSelectedEvent(String userType) throws Exception {
		extent.HeaderChildNode("Verify Subscription Selected Event");
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
			waitTime(3000);
			Swipe("Up", 2);
			verifyElementPresentAndClick(AMDSubscibeScreen.objContinueBtn, "Continue Button");
			waitTime(6000);
			hideKeyboard();
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Element", "Continue");
			mixpanel.FEProp.setProperty("Page Name", "subscriptionPlan");
			mixpanel.ValidateParameter("", "Subscription Selected");
		}
	}

	@SuppressWarnings("static-access")
	public void verifySubscriptionSelectedEventByClubPack(String userType) throws Exception {
		extent.HeaderChildNode("Verify Subscription Selected Event By selecting Club Pack");
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
			click(AMDSubscibeScreen.objClubTab, "Club Pack");
			Swipe("Up", 2);
			click(AMDSubscibeScreen.objClub365daysPack, "Pack");
			verifyElementPresentAndClick(AMDSubscibeScreen.objContinueBtn, "Continue Button");
			waitTime(2000);
			hideKeyboard();
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Element", "Continue");
			mixpanel.FEProp.setProperty("Page Name", "subscriptionPlan");
			mixpanel.ValidateParameter("", "Subscription Selected");

		}
	}

	/*
	 * Event : Promo Code Result
	 */

	@SuppressWarnings("static-access")
	public void verifyPromoCodeResultEventForValid(String userType) throws Exception {
		extent.HeaderChildNode("Verify Promo Code Result Event For Valid code");
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
			type(AMDSubscibeScreen.objApplyPromoCodeTextbox, "PNB20", "Prepaid Code");
			hideKeyboard();
			verifyElementPresentAndClick(AMDSubscibeScreen.objApply, "Apply Button");
			waitTime(6000);
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Element", "Apply");
			mixpanel.FEProp.setProperty("Page Name", "subscriptionPlan");
			mixpanel.ValidateParameter("", "Promo Code Result");

		}
	}

	@SuppressWarnings("static-access")
	public void verifyPromoCodeResultEventForInvalid(String userType) throws Exception {
		extent.HeaderChildNode("Verify Promo Code Result Event For Invalid code");
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			verifyElementPresentAndClick(AMDHomePage.objSubscribeIcon, "Subscribe button");
			type(AMDSubscibeScreen.objApplyPromoCodeTextbox, "sdcrfd", "Prepaid Code");
			hideKeyboard();
			verifyElementPresentAndClick(AMDSubscibeScreen.objApply, "Apply Button");
			waitTime(5000);
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Element", "Apply");
			mixpanel.FEProp.setProperty("Page Name", "subscriptionPlan");
			mixpanel.ValidateParameter("", "Promo Code Result");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyPrepaidCodeResultEvent() throws Exception {
		if (!userType.equals("Guest")) {
			extent.HeaderChildNode("Verify Prepaid Code Result event");
			waitTime(2000);
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More Menu");
			verifyElementPresentAndClick(AMDMoreMenu.objHaveaPrepaidCode, "Have a Prepaid code");
			verifyElementPresentAndClick(AMDMoreMenu.objPrepaidCodeTxt, "Prepaid code text box");
			type(AMDMoreMenu.objPrepaidCodeTxt, "abcdefg", "Prepaid code text box");
			hideKeyboard();
			click(AMDMoreMenu.objApplyBtn, "Apply button");
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("Page Name", "More");
			mixpanel.FEProp.setProperty("Element", "Apply");
			mixpanel.ValidateParameter("", "Prepaid Code Result");
		}
	}

	public void AudioLanguageChangeEventForTrailerContent(String usertype, String keyword7) throws Exception {
		extent.HeaderChildNode("Verify Audio Language Change event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword7 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(10000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objAvailableAudioLanguages,
				"Available Audio Languages");
		if (lang == true) {
			String noOfAudioLanguages = getText(AMDConsumptionScreen.objAvailableAudioLanguages);
			if (noOfAudioLanguages.equalsIgnoreCase("Available in 1 language")) {
				logger.info("Only one language is displayed");
				extent.extentLogger("Audio Language", "Only one language is displayed");
			} else {
				click(AMDConsumptionScreen.objAudioLanguageValue, "Audio Language Value");
				String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
				String firstAudioLanguageOption = getText(AMDConsumptionScreen.objAudioLanguages(1));
				if (selectedAudioLanguageOption.equalsIgnoreCase(firstAudioLanguageOption)) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Audio Language option");
				} else {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Audio Language option");
				}

				// Audio language change in Full screen mode
				click(AMDPlayerScreen.objPauseIcon, "Pause icon");
				verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
				verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
				verifyElementPresentAndClick(AMDPlayerScreen.objAudioTrackOption, "Audio track option");
				String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
				if (selectedQualityOption.equalsIgnoreCase("Hindi")) {
					verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Audio Language option");
				} else {
					verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Audio Language option");
				}
				Back(2);
				waitTime(4000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				String pContentId = mixpanel.fetchContentId("", "Language Audio Change");
				String pDistinctId = mixpanel.DistinctId;
				ResponseInstance.getContentDetails(pContentId);

				mixpanel.ValidateParameter(pDistinctId, "Language Audio Change");
			}
		} else {
			logger.info("no Audio Language is displayed");
			extent.extentLogger("Audio Language", "No Audio Language is displayed");
		}
	}

	public void AudioLanguageChangeEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Audio Language Change Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitTime(10000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objAvailableAudioLanguages,
				"Available Audio Languages");
		if (lang == true) {
			String noOfAudioLanguages = getText(AMDConsumptionScreen.objAvailableAudioLanguages);
			if (noOfAudioLanguages.equalsIgnoreCase("Available in 1 language")) {
				logger.info("Only one language is displayed");
				extent.extentLogger("Audio Language", "Only one language is displayed");
			} else {
				click(AMDConsumptionScreen.objAudioLanguageValue, "Audio Language Value");
				String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
				String firstAudioLanguageOption = getText(AMDConsumptionScreen.objAudioLanguages(1));
				if (selectedAudioLanguageOption.equalsIgnoreCase(firstAudioLanguageOption)) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Audio Language option");
				} else {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Audio Language option");
				}
				waitTime(3000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				String pContentId = mixpanel.fetchContentId("", "Language Audio Change");
				String pDistinctId = mixpanel.DistinctId;
				ResponseInstance.getContentDetails(pContentId);

				mixpanel.ValidateParameter(pDistinctId, "Language Audio Change");
			}
		} else {
			logger.info("no Audio Language is displayed");
			extent.extentLogger("Audio Language", "No Audio Language is displayed");
		}
	}

	public void AuioLanguageChangeEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Audio Language Change Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(15000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objAvailableAudioLanguages,
				"Available Audio Languages");
		if (lang == true) {
			String noOfAudioLanguages = getText(AMDConsumptionScreen.objAvailableAudioLanguages);
			if (noOfAudioLanguages.equalsIgnoreCase("Available in 1 language")) {
				logger.info("Only one language is displayed");
				extent.extentLogger("Audio Language", "Only one language is displayed");
			} else {
				click(AMDConsumptionScreen.objAudioLanguageValue, "Audio Language Value");
				String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
				String firstAudioLanguageOption = getText(AMDConsumptionScreen.objAudioLanguages(1));
				if (selectedAudioLanguageOption.equalsIgnoreCase(firstAudioLanguageOption)) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Audio Language option");
				} else {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Audio Language option");
				}
				waitTime(3000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				String pContentId = mixpanel.fetchContentId("", "Language Audio Change");
				String pDistinctId = mixpanel.DistinctId;
				ResponseInstance.getContentDetails(pContentId);

				mixpanel.ValidateParameter(pDistinctId, "Language Audio Change");
			}
		} else {
			logger.info("no Audio Language is displayed");
			extent.extentLogger("Audio Language", "No Audio Language is displayed");
		}
	}

	public void AudioLanguageChangeEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Audio Language Change Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(10000);
				boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objAvailableAudioLanguages,
						"Available Audio Languages");
				if (lang == true) {
					String noOfAudioLanguages = getText(AMDConsumptionScreen.objAvailableAudioLanguages);
					if (noOfAudioLanguages.equalsIgnoreCase("Available in 1 language")) {
						logger.info("Only one language is displayed");
						extent.extentLogger("Audio Language", "Only one language is displayed");
					} else {
						click(AMDConsumptionScreen.objAudioLanguageValue, "Audio Language Value");
						String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
						String firstAudioLanguageOption = getText(AMDConsumptionScreen.objAudioLanguages(1));
						if (selectedAudioLanguageOption.equalsIgnoreCase(firstAudioLanguageOption)) {
							verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2),
									"Audio Language option");
						} else {
							verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1),
									"Audio Language option");
						}
						waitTime(4000);
						setFEProperty(usertype);
						mixpanel.FEProp.setProperty("Source", "home");
						mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

						mixpanel.ValidateParameter("", "Language Audio Change");
					}
				} else {
					logger.info("no Audio Language is displayed");
					extent.extentLogger("Audio Language", "No Audio Language is displayed");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}

	}

	public void AudioLanguageChangeEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Audio Language Change event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		waitTime(10000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objAvailableAudioLanguages,
				"Available Audio Languages");
		if (lang == true) {
			String noOfAudioLanguages = getText(AMDConsumptionScreen.objAvailableAudioLanguages);
			if (noOfAudioLanguages.equalsIgnoreCase("Available in 1 language")) {
				logger.info("Only one language is displayed");
				extent.extentLogger("Audio Language", "Only one language is displayed");
			} else {
				click(AMDConsumptionScreen.objAudioLanguageValue, "Audio Language Value");
				String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
				String firstAudioLanguageOption = getText(AMDConsumptionScreen.objAudioLanguages(1));
				if (selectedAudioLanguageOption.equalsIgnoreCase(firstAudioLanguageOption)) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Audio Language option");
				} else {
					verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Audio Language option");
				}
				waitTime(4000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				String pContentId = mixpanel.fetchContentId("", "Language Audio Change");
				String pDistinctId = mixpanel.DistinctId;
				ResponseInstance.getContentDetails(pContentId);

				mixpanel.ValidateParameter(pDistinctId, "Language Audio Change");
			}
		} else {
			logger.info("no Audio Language is displayed");
			extent.extentLogger("Audio Language", "No Audio Language is displayed");
		}
	}

	public void SubtitleLanguageChangeEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Subtitle Language Change Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(10000);
				boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objSubtitleValue, "Subtitle option value");
				if (lang == true) {
					click(AMDConsumptionScreen.objSubtitleValue, "Subtitle Value");
					String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
					if (selectedAudioLanguageOption.equalsIgnoreCase("Off")) {
						verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2),
								"Subtitle Language option");
					} else {
						verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1),
								"Subtitle Language option");
					}
					waitTime(4000);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					String pContentId = mixpanel.fetchContentId("", "Subtitle Language Change");
					String pDistinctId = mixpanel.DistinctId;
					ResponseInstance.getContentDetails(pContentId);

					mixpanel.ValidateParameter(pDistinctId, "Subtitle Language Change");
				} else {
					logger.info("No Subtitles for this content");
					extent.extentLogger("Subtitle", "No Subtitles for this content");
				}
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void SubtitleLanguageChangeEventForTrailerContent(String usertype, String keyword7) throws Exception {
		extent.HeaderChildNode("Verify Subtitle Language Change event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword7 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(10000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objSubtitleValue, "Subtitle option value");
		if (lang == true) {
			click(AMDConsumptionScreen.objSubtitleValue, "Subtitle Value");
			String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
			if (selectedAudioLanguageOption.equalsIgnoreCase("Off")) {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Subtitle option");
			} else {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Subtitle option");
			}

			// Audio language change in Full screen mode
			click(AMDPlayerScreen.objPauseIcon, "Pause icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
			verifyElementPresentAndClick(AMDPlayerScreen.objThreeDotsOnPlayer, "Three Dots");
			verifyElementPresentAndClick(AMDPlayerScreen.objSubtitleOption, "Subtitle option");
			String selectedQualityOption = getText(AMDPlayerScreen.objSelectedQualityOption);
			if (selectedQualityOption.equalsIgnoreCase("Off")) {
				verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(2), "Subtitle option");
			} else {
				verifyElementPresentAndClick(AMDPlayerScreen.objQualityOptions(1), "Subtitle option");
			}
			waitTime(4000);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			String pContentId = mixpanel.fetchContentId("", "Subtitle Language Change");
			String pDistinctId = mixpanel.DistinctId;
			ResponseInstance.getContentDetails(pContentId);

			mixpanel.ValidateParameter(pDistinctId, "Subtitle Language Change");
		} else {
			logger.info("No Subtitles for this content");
			extent.extentLogger("Subtitle", "No Subtitles for this content");
		}

	}

	public void SubtitleLanguageChangeEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Subtitle Language Change Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitTime(10000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objSubtitleValue, "Subtitle option value");
		if (lang == true) {
			click(AMDConsumptionScreen.objSubtitleValue, "Subtitle Value");
			String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
			if (selectedAudioLanguageOption.equalsIgnoreCase("Off")) {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Subtitle option");
			} else {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Subtitle option");
			}
			waitTime(3000);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			String pContentId = mixpanel.fetchContentId("", "Subtitle Language Change");
			String pDistinctId = mixpanel.DistinctId;
			ResponseInstance.getContentDetails(pContentId);

			mixpanel.ValidateParameter(pDistinctId, "Subtitle Language Change");
		} else {
			logger.info("No Subtitles for this content");
			extent.extentLogger("Subtitle", "No Subtitles for this content");
		}

	}

	public void SubtitleLanguageChangeEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Subtitle Language Change Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(15000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objSubtitleValue, "Subtitle option value");
		if (lang == true) {
			click(AMDConsumptionScreen.objSubtitleValue, "Subtitle Value");
			String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
			if (selectedAudioLanguageOption.equalsIgnoreCase("Off")) {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Subtitle option");
			} else {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Subtitle option");
			}
			waitTime(3000);

			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			String pContentId = mixpanel.fetchContentId("", "Subtitle Language Change");
			String pDistinctId = mixpanel.DistinctId;
			ResponseInstance.getContentDetails(pContentId);

			mixpanel.ValidateParameter(pDistinctId, "Subtitle Language Change");
		} else {
			logger.info("No Subtitles for this content");
			extent.extentLogger("Subtitle", "No Subtitles for this content");
		}
	}

	public void SubtitleLanguageChangeEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Subtitle Language Change Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(10000);
				boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objSubtitleValue, "Subtitle option value");
				if (lang == true) {
					click(AMDConsumptionScreen.objSubtitleValue, "Subtitle Value");
					String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
					if (selectedAudioLanguageOption.equalsIgnoreCase("Off")) {
						verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Subtitle option");
					} else {
						verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Subtitle option");
					}
					waitTime(3000);

					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					String pContentId = mixpanel.fetchContentId("", "Subtitle Language Change");
					String pDistinctId = mixpanel.DistinctId;
					ResponseInstance.getContentDetails(pContentId);

					mixpanel.ValidateParameter(pDistinctId, "Subtitle Language Change");
				} else {
					logger.info("No Subtitles for this content");
					extent.extentLogger("Subtitle", "No Subtitles for this content");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}

	}

	public void SubtitleLanguageChangeEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Subtitle Language Change event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		waitTime(10000);
		boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objSubtitleValue, "Subtitle option value");
		if (lang == true) {
			click(AMDConsumptionScreen.objSubtitleValue, "Subtitle Value");
			String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
			if (selectedAudioLanguageOption.equalsIgnoreCase("Off")) {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2), "Subtitle option");
			} else {
				verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1), "Subtitle option");
			}
			waitTime(3000);

			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			String pContentId = mixpanel.fetchContentId("", "Subtitle Language Change");
			String pDistinctId = mixpanel.DistinctId;
			ResponseInstance.getContentDetails(pContentId);

			mixpanel.ValidateParameter(pDistinctId, "Subtitle Language Change");
		} else {
			logger.info("No Subtitles for this content");
			extent.extentLogger("Subtitle", "No Subtitles for this content");
		}
	}

	public void AudioLanguageChangeEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Audio Language Change Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(10000);
				boolean lang = verifyIsElementDisplayed(AMDConsumptionScreen.objAvailableAudioLanguages,
						"Available Audio Languages");
				if (lang == true) {
					String noOfAudioLanguages = getText(AMDConsumptionScreen.objAvailableAudioLanguages);
					if (noOfAudioLanguages.equalsIgnoreCase("Available in 1 language")) {
						logger.info("Only one language is displayed");
						extent.extentLogger("Audio Language", "Only one language is displayed");
					} else {
						click(AMDConsumptionScreen.objAudioLanguageValue, "Audio Language Value");
						String selectedAudioLanguageOption = getText(AMDConsumptionScreen.objSelectedAudioLanguage);
						String firstAudioLanguageOption = getText(AMDConsumptionScreen.objAudioLanguages(1));
						if (selectedAudioLanguageOption.equalsIgnoreCase(firstAudioLanguageOption)) {
							verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(2),
									"Audio Language option");
						} else {
							verifyElementPresentAndClick(AMDConsumptionScreen.objAudioLanguages(1),
									"Audio Language option");
						}
						waitTime(4000);
						setFEProperty(usertype);
						mixpanel.FEProp.setProperty("Source", "home");
						mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

						String pContentId = mixpanel.fetchContentId("", "Language Audio Change");
						String pDistinctId = mixpanel.DistinctId;
						ResponseInstance.getContentDetails(pContentId);

						mixpanel.ValidateParameter(pDistinctId, "Language Audio Change");
					}
				} else {
					logger.info("no Audio Language is displayed");
					extent.extentLogger("Audio Language", "No Audio Language is displayed");
				}
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void SkipIntroEventForPremiumContent(String usertype, String keyword4) throws Exception {
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			extent.HeaderChildNode("Skip Intro Event of Premium content");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitForElementDisplayed(AMDPlayerScreen.objSkipIntro, 30);
			verifyElementPresentAndClick(AMDPlayerScreen.objSkipIntro, "Skip Intro");
			waitTime(2000);
			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.ValidateParameter("", "Skip Intro");
		} else {
			logger.info("This scenario is not applicable to " + usertype);
			extent.extentLogger("Skip Intro", "This scenario is not applicable to " + usertype);
		}
	}

	public void SkipIntroEventOfContentFromContentTrays(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Skip Intro Event of Content from Content trays");
		waitTime(5000);
		SelectTopNavigationTab(tabName);
		waitTime(10000);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(10000);
				boolean skip = verifyIsElementDisplayed(AMDPlayerScreen.objSkipIntro, "Skip Intro");
				if (skip == true) {
					click(AMDPlayerScreen.objSkipIntro, "Skip Intro");
					waitTime(3000);
					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.ValidateParameter("", "Skip Intro");
				} else {
					logger.info("Skip Intro is not displayed for this content");
					extent.extentLogger("Skip Intro", "Skip Intro is not displayed for this content");
				}
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void SkipIntroEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Skip Intro Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitTime(10000);
		boolean skip = verifyIsElementDisplayed(AMDPlayerScreen.objSkipIntro, "Skip Intro");
		if (skip == true) {
			click(AMDPlayerScreen.objSkipIntro, "Skip Intro");
			waitTime(3000);
			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.ValidateParameter("", "Skip Intro");
		} else {
			logger.info("Skip Intro is not displayed for this content");
			extent.extentLogger("Skip Intro", "Skip Intro is not displayed for this content");
		}
	}

	public void SkipIntroEventOfcontentFromSearchPage(String usertype, String keyword8) throws Exception {
		extent.HeaderChildNode("Skip Intro Event of content from search page");
		if (!usertype.equalsIgnoreCase("guest")) {
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword8 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
				waitForAdToFinishInAmd();
			}
			registerPopUpClose();
			completeProfilePopUpClose(usertype);
			waitTime(3000);
			boolean skip = verifyIsElementDisplayed(AMDPlayerScreen.objSkipIntro, "Skip Intro");
			if (skip == true) {
				click(AMDPlayerScreen.objSkipIntro, "Skip Intro");
				waitTime(3000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.ValidateParameter("", "Skip Intro");
			} else {
				logger.info("Skip Intro is not displayed for this content");
				extent.extentLogger("Skip Intro", "Skip Intro is not displayed for this content");
			}
		} else {
			logger.info("Skip Intro is not applicable for " + usertype);
			extent.extentLogger("Skip Intro", "Skip Intro is not applicable for " + usertype);
		}

	}

	public void forwardAutoSeek(int n) throws Exception {
		Dimension sizee = getDriver().manage().window().getSize();
		ScreenOrientation orientation = getDriver().getOrientation();
		String ScreenOrientation = orientation.toString();
		int FwdXValue;
		if (ScreenOrientation.equalsIgnoreCase("Landscape")) {
			FwdXValue = (int) (sizee.getHeight() * 0.9);
		} else {
			FwdXValue = (int) (sizee.getWidth() * 0.9);
		}
		click(AMDPlayerScreen.objPlayerScreen, "Player screen");
		int YValue = Integer.valueOf(getAttributValue("y", AMDPlayerScreen.objNextIcon));
		touchAction = new TouchAction(getDriver());
		for (int j = 0; j < n; j++) {
			touchAction.press(PointOption.point(FwdXValue, YValue)).release().perform()
					.press(PointOption.point(FwdXValue, YValue)).release().perform();
		}
	}

	public void rewindAutoSeek(int n) throws Exception {
		click(AMDPlayerScreen.objPlayerScreen, "Player screen");
		int YValue = Integer.valueOf(getAttributValue("y", AMDPlayerScreen.objNextIcon));
		touchAction = new TouchAction(getDriver());
		for (int i = 0; i < n; i++) {
			touchAction.press(PointOption.point(100, YValue)).release().perform().press(PointOption.point(100, YValue))
					.release().perform();
		}
	}

	public void AutoSeekForwardEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("AutoSeek Forward Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(3000);
				boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
				if (inlineLink == true) {
					logger.info("Player inline subscription link is displayed");
					extentLogger("Player screen", "Player inline subscription link is displayed");
				} else {
					forwardAutoSeek(1);
					waitTime(3000);

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.ValidateParameter("", "Auto-seek");
				}
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void AutoSeekRewindEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("AutoSeek Rewind Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(3000);
				boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
				if (inlineLink == true) {
					logger.info("Player inline subscription link is displayed");
					extentLogger("Player screen", "Player inline subscription link is displayed");
				} else {
					rewindAutoSeek(1);
					waitTime(3000);

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.ValidateParameter("", "Auto-seek");
				}
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void AutoSeekForwardEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify AutoSeek Forward event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		forwardAutoSeek(1);
		waitTime(5000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Auto-seek");
	}

	public void AutoSeekRewindEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify AutoSeek Rewind event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		rewindAutoSeek(1);
		waitTime(3000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Auto-seek");
	}

	public void AutoSeekForwardEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("AutoSeek Forward Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitTime(3000);
		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLogger("Player screen", "Player inline subscription link is displayed");
		} else {
			forwardAutoSeek(1);

			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.ValidateParameter("", "Auto-seek");
		}
	}

	public void AutoSeekRewindEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("AutoSeek Rewind Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitTime(3000);
		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLogger("Player screen", "Player inline subscription link is displayed");
		} else {
			rewindAutoSeek(1);
			waitTime(3000);
			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

			mixpanel.ValidateParameter("", "Auto-seek");
		}
	}

	public void AutoSeekForwardEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("AutoSeek Forward Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		forwardAutoSeek(1);
		waitTime(3000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Auto-seek");
	}

	public void AutoSeekRewindEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("AutoSeek Rewind Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		rewindAutoSeek(1);
		waitTime(3000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Auto-seek");
	}

	public void AutoSeekForwardEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("AutoSeek Forward Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
					waitForAdToFinishInAmd();
				}
				forwardAutoSeek(1);
				waitTime(4000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.ValidateParameter("", "Auto-seek");
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}
	}

	public void AutoSeekRewindEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("AutoSeek Rewind Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();

				if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
					waitForAdToFinishInAmd();
				}
				waitTime(5000);
				rewindAutoSeek(1);
				waitTime(2000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.ValidateParameter("", "Auto-seek");
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}
	}

	public void AutoSeekForwardEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify AutoSeek Forward event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		forwardAutoSeek(1);
		waitTime(4000);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Auto-seek");
	}

	public void AutoSeekRewindEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify AutoSeek Rewind event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		rewindAutoSeek(1);
		waitTime(3000);
		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Auto-seek");
	}

	public void DownloadStartEventForPremiumContent(String usertype, String tabName) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Download Start Event for premium content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			Swipe("UP", 1);

			String pUserType;
			if (usertype.contains("SubscribedUser")) {
				pUserType = "Premium";
			} else if (usertype.contains("NonSubscribedUser")) {
				pUserType = "expired";
			} else {
				pUserType = "guest";
			}

			boolean var = false;
			for (int i = 0; i < 3; i++) {
				var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
				if (var == true) {
					verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
					waitTime(3000);
					boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
					if (inlineLink == true) {
						logger.info("Player inline subscription link is displayed");
						extentLogger("Player screen", "Player inline subscription link is displayed");
					} else {
						waitTime(3000);
						boolean download = verifyIsElementDisplayed(AMDConsumptionScreen.objDownloadBtn,
								"Download icon");
						if (download == true) {
							verifyElementPresentAndClick(AMDConsumptionScreen.objDownloadBtn, "Download icon");
							verifyElementPresentAndClick(AMDConsumptionScreen.objStartDowloadBtn,
									"Start download button");

//						boolean status = verifyIsElementDisplayed(AMDConsumptionScreen.objDowloadStatus, "Download status button");
//						if(status==true) {
//							verifyElementPresentAndClick(AMDConsumptionScreen.objDowloadStatus, "Download status button");
//							verifyElementPresentAndClick(AMDConsumptionScreen.objCancelDownload, "Cancel download button");
//						}
							setFEProperty(usertype);
							waitTime(2000);
							mixpanel.FEProp.setProperty("Source", "home");
							mixpanel.FEProp.setProperty("User Type", pUserType);
							mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

							mixpanel.FEProp.setProperty("Brand", getOEMName);
							mixpanel.FEProp.setProperty("Manufacturer", getOEMName);
							mixpanel.ValidateParameter("", "Download Start");
						} else {
							logger.info("Content is already downloaded");
							extentLogger("Download", "Content is already downloaded");
						}
					}
					break;
				} else {
					Swipe("UP", 1);
				}
			}
			if (var == false) {
				logger.info("Premium content is not displayed in the screen");
				extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
			}
		} else {
			logger.info("This is not applicable for " + usertype);
			extentLogger("Guest User", "This is not applicable for " + usertype);
		}
	}

	public void DownloadStartEventForTrailerContent(String usertype, String keyword3) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Download start  event for Trailer content");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);

			String pUserType;
			if (usertype.equalsIgnoreCase("SubscribedUser")) {
				pUserType = "Premium";
				verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
			} else if (usertype.equalsIgnoreCase("NonSubscribedUser")) {
				pUserType = "expired";
			} else {
				pUserType = "guest";
			}
			waitTime(3000);
			boolean download = verifyIsElementDisplayed(AMDConsumptionScreen.objDownloadBtn, "Download icon");
			if (download == true) {
				verifyElementPresentAndClick(AMDConsumptionScreen.objDownloadBtn, "Download icon");
				verifyElementPresentAndClick(AMDConsumptionScreen.objStartDowloadBtn, "Start download button");

				waitTime(2000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("User Type", pUserType);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Brand", getOEMName);
				mixpanel.FEProp.setProperty("Manufacturer", getOEMName);
				mixpanel.ValidateParameter("", "Download Start");
			} else {
				logger.info("Content is already downloaded");
				extentLogger("Download", "Content is already downloaded");
			}
		} else {
			logger.info("This is not applicable for " + usertype);
			extentLogger("Guest User", "This is not applicable for " + usertype);
		}
	}

	public void DownloadStartEventForCarouselContent(String usertype, String tabName) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Download start Event for carousel content");
			waitTime(10000);
			SelectTopNavigationTab(tabName);
			verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
			waitTime(3000);

			String pUserType;
			if (usertype.contains("SubscribedUser")) {
				pUserType = "Premium";
			} else if (usertype.contains("NonSubscribedUser")) {
				pUserType = "expired";
			} else {
				pUserType = "guest";
			}

			boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
			if (inlineLink == true) {
				logger.info("Player inline subscription link is displayed");
				extentLogger("Player screen", "Player inline subscription link is displayed");
			} else {
				waitTime(3000);
				boolean download = verifyIsElementDisplayed(AMDConsumptionScreen.objDownloadBtn, "Download icon");
				if (download == true) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objDownloadBtn, "Download icon");
					verifyElementPresentAndClick(AMDConsumptionScreen.objStartDowloadBtn, "Start download button");

					waitTime(2000);
					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("User Type", pUserType);
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.FEProp.setProperty("Brand", getOEMName);
					mixpanel.FEProp.setProperty("Manufacturer", getOEMName);
					mixpanel.ValidateParameter("", "Download Start");
				} else {
					logger.info("Content is already downloaded");
					extentLogger("Download", "Content is already downloaded");
				}

			}
			waitTime(3000);
		} else {
			logger.info("This is not applicable for " + usertype);
			extentLogger("Guest User", "This is not applicable for " + usertype);
		}

	}

	public void DownloadStartEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Download Start Event of content from search page");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
				waitForAdToFinishInAmd();
			}
			registerPopUpClose();
			completeProfilePopUpClose(usertype);
			waitTime(3000);

			String pUserType;
			if (usertype.contains("SubscribedUser")) {
				pUserType = "Premium";
			} else if (usertype.contains("NonSubscribedUser")) {
				pUserType = "expired";
			} else {
				pUserType = "guest";
			}

			boolean download = verifyIsElementDisplayed(AMDConsumptionScreen.objDownloadBtn, "Download icon");
			if (download == true) {
				verifyElementPresentAndClick(AMDConsumptionScreen.objDownloadBtn, "Download icon");
				verifyElementPresentAndClick(AMDConsumptionScreen.objStartDowloadBtn, "Start download button");
				boolean status = verifyIsElementDisplayed(AMDConsumptionScreen.objDowloadStatus,
						"Download status button");
				if (status == true) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objDowloadStatus, "Download status button");
					verifyElementPresentAndClick(AMDConsumptionScreen.objCancelDownload, "Cancel download button");
				}

				waitTime(2000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("User Type", pUserType);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Brand", getOEMName);
				mixpanel.FEProp.setProperty("Manufacturer", getOEMName);
				mixpanel.ValidateParameter("", "Download Start");
			} else {
				logger.info("Content is already downloaded");
				extentLogger("Download", "Content is already downloaded");
			}
		} else {
			logger.info("This is not applicable for " + usertype);
			extentLogger("Guest User", "This is not applicable for " + usertype);
		}

	}

	public void DownloadStartEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Download start Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);

			String pUserType;
			if (usertype.contains("SubscribedUser")) {
				pUserType = "Premium";
			} else if (usertype.contains("NonSubscribedUser")) {
				pUserType = "expired";
			} else {
				pUserType = "guest";
			}

			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				waitTime(3000);
				boolean download = verifyIsElementDisplayed(AMDConsumptionScreen.objDownloadBtn, "Download icon");
				if (download == true) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objDownloadBtn, "Download icon");
					verifyElementPresentAndClick(AMDConsumptionScreen.objStartDowloadBtn, "Start download button");

					waitTime(2000);
					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("User Type", pUserType);
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.FEProp.setProperty("Brand", getOEMName);
					mixpanel.FEProp.setProperty("Manufacturer", getOEMName);
					mixpanel.ValidateParameter("", "Download Start");
				} else {
					logger.info("Content is already downloaded");
					extentLogger("Download", "Content is already downloaded");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}
	}

	public void DownloadStartEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Verify Download start event of content from Upnext rail");
			click(AMDSearchScreen.objSearchIcon, "Search icon");
			click(AMDSearchScreen.objSearchEditBox, "Search Box");
			type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
			hideKeyboard();
			waitTime(4000);
			waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
			click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
			waitTime(8000);
			Swipe("UP", 1);
			if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
				verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray,
						"Upnext rail content");
			}
			if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
				waitForAdToFinishInAmd();
			}
			registerPopUpClose();
			completeProfilePopUpClose(usertype);
			waitTime(3000);

			String pUserType;
			if (usertype.contains("SubscribedUser")) {
				pUserType = "Premium";
			} else if (usertype.contains("NonSubscribedUser")) {
				pUserType = "expired";
			} else {
				pUserType = "guest";
			}

			boolean download = verifyIsElementDisplayed(AMDConsumptionScreen.objDownloadBtn, "Download icon");
			if (download == true) {
				verifyElementPresentAndClick(AMDConsumptionScreen.objDownloadBtn, "Download icon");
				verifyElementPresentAndClick(AMDConsumptionScreen.objStartDowloadBtn, "Start download button");
				boolean status = verifyIsElementDisplayed(AMDConsumptionScreen.objDowloadStatus,
						"Download status button");
				if (status == true) {
					verifyElementPresentAndClick(AMDConsumptionScreen.objDowloadStatus, "Download status button");
					verifyElementPresentAndClick(AMDConsumptionScreen.objCancelDownload, "Cancel download button");
				}

				waitTime(2000);
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "Search_Tab");
				mixpanel.FEProp.setProperty("User Type", pUserType);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.FEProp.setProperty("Brand", getOEMName);
				mixpanel.FEProp.setProperty("Manufacturer", getOEMName);
				mixpanel.ValidateParameter("", "Download Start");
			} else {
				logger.info("Content is already downloaded");
				extentLogger("Download", "Content is already downloaded");
			}
		} else {
			logger.info("This is not applicable for " + usertype);
			extentLogger("Guest User", "This is not applicable for " + usertype);
		}
	}

	public void completeProfilePopUpClose(String userType) throws Exception {
		waitTime(5000);
		if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			if (verifyIsElementDisplayed(AMDPlayerScreen.objCompleteProfilePopUp)) {
				logger.info("Complete Profile Pop Up is displayed");
				extent.extentLogger("Complete Profile Pop Up", "Complete Profile Pop Up is displayed");
				click(AMDGenericObjects.objPopUpDivider, "Click PopUp Divider");
			}
		}
	}

	public void ResumeEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Resume Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(3000);
				boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
				if (inlineLink == true) {
					logger.info("Player inline subscription link is displayed");
					extentLogger("Player screen", "Player inline subscription link is displayed");
				} else {
					waitTime(6000);
					verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
					verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
					waitTime(3000);
					boolean eventFlag = false;
					eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
					waitTime(2000);

					if (eventFlag) {
						setFEProperty(usertype);
						mixpanel.FEProp.setProperty("Source", "home");
						mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
						mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
						mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");

						mixpanel.ValidateParameter("", "Resume");
					} else {
						logger.info("Failed to perform Event Action");
						extentLoggerWarning("Event Action", "Failed to perform Event Action");
					}

				}

				waitTime(3000);
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void ResumeEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify Resume event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(3000);

		boolean eventFlag = false;
		eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(2000);

		if (eventFlag) {
			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
			mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");

			mixpanel.ValidateParameter("", "Resume");
		} else {
			logger.info("Failed to perform Event Action");
			extentLoggerWarning("Event Action", "Failed to perform Event Action");
		}

	}

	public void ResumeEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Resume Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);

		boolean flagBox = verifyIsElementDisplayed(AMDHomePage.objSboxIcon);
		String pSugarBox = String.valueOf(flagBox);

		String contentName = ResponseInstance.getCarouselContentFromAPI(usertype, tabName);
		System.out.println(contentName);

		for (int i = 0; i < 3; i++) {
			if (verifyElementDisplayed(AMDHomePage.objCarouselContentTitle(contentName))) {
				click(AMDHomePage.objCarouselContentTitle(contentName), "carousal content");
				break;
			}

		}

		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		if (usertype.equalsIgnoreCase("Guest")) {
			registerPopUpClose();
		}
		completeProfilePopUpClose(usertype);

		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLogger("Player screen", "Player inline subscription link is displayed");
		} else {
			waitTime(5000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			waitTime(3000);
			boolean eventFlag = false;
			eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
			waitTime(2000);

			if (eventFlag) {
				if (usertype.equalsIgnoreCase("SubscribedUser")) {
					ResponseInstance.subscriptionDetails();
				}
				String pPage = getPageName(tabName);
				String pSource = getSource(tabName);
				String pManufacturer = DeviceDetails.OEM;
				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", pSource);
				mixpanel.FEProp.setProperty("Page Name", pPage);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Manufacturer", pManufacturer);
				mixpanel.FEProp.setProperty("Brand", pManufacturer);
				mixpanel.FEProp.setProperty("Sugar Box Value", pSugarBox);

				mixpanel.ValidateParameter("", "Resume");
			} else {
				logger.info("Failed to perform Event Action");
				extentLoggerWarning("Event Action", "Failed to perform Event Action");
			}
		}
	}

	public void ResumeEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Resume Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(3000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(6000);

		// Resume event on Full Screen mode
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(2000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(3000);
		boolean eventFlag = false;
		eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(2000);

		if (eventFlag) {
			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
			mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");

			mixpanel.ValidateParameter("", "Resume");
		} else {
			logger.info("Failed to perform Event Action");
			extentLoggerWarning("Event Action", "Failed to perform Event Action");
		}
	}

	public void ResumeEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Resume Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
				waitTime(3000);
				boolean eventFlag = false;
				eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
				waitTime(2000);

				if (eventFlag) {
					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
					mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
					mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");

					mixpanel.ValidateParameter("", "Resume");
				} else {
					logger.info("Failed to perform Event Action");
					extentLoggerWarning("Event Action", "Failed to perform Event Action");
				}
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}
	}

	public void ResumeEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Resume event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		completeProfilePopUpClose(usertype);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(5000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(3000);
		boolean eventFlag = false;
		eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(2000);

		if (eventFlag) {
			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "Search_Tab");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
			mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");

			mixpanel.ValidateParameter("", "Resume");
		} else {
			logger.info("Failed to perform Event Action");
			extentLoggerWarning("Event Action", "Failed to perform Event Action");
		}
	}

	public void ResumeEventForLinearContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Resume Event for Linear content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		waitTime(3000);
		verifyElementPresentAndClick(AMDLiveTVScreen.objFirstContentCardUnderFreeChannelsTray, "linear content");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(3000);
		boolean eventFlag = false;
		eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(2000);

		if (eventFlag) {
			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "Live TV");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
			mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");
			mixpanel.ValidateParameter("", "Resume");
		} else {
			logger.info("Failed to perform Event Action");
			extentLoggerWarning("Event Action", "Failed to perform Event Action");
		}
	}

	public void ScrubSeekEventForPremiumContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Scrub/Seek Event for premium content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		Swipe("UP", 1);
		boolean var = false;
		for (int i = 0; i < 3; i++) {
			var = verifyIsElementDisplayed(AMDHomePage.objPremiumTag, "Premium Tag");
			if (var == true) {
				verifyElementPresentAndClick(AMDHomePage.objPremiumTag, "Premium content");
				waitTime(3000);
				boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
				if (inlineLink == true) {
					logger.info("Player inline subscription link is displayed");
					extentLogger("Player screen", "Player inline subscription link is displayed");
				} else {
					waitTime(6000);
					verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
					verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
					seekVideo(AMDPlayerScreen.objProgressBar, usertype);
					waitTime(3000);

					setFEProperty(usertype);
					mixpanel.FEProp.setProperty("Source", "home");
					mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

					mixpanel.ValidateParameter("", "Scrub/Seek");
				}
				break;
			} else {
				Swipe("UP", 1);
			}
		}
		if (var == false) {
			logger.info("Premium content is not displayed in the screen");
			extentLoggerWarning("Premium Content", "Premium content is not displayed in the screen");
		}
	}

	public void ScrubSeekEventForTrailerContent(String usertype, String keyword3) throws Exception {
		extent.HeaderChildNode("Verify Scrub/Seek event for Trailer content");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword3 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitForElementDisplayed(AMDPlayerScreen.objPlayer, 30);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			verifyElementPresentAndClick(AMDConsumptionScreen.objWatchTrialer, "Watch Trailer button");
		}
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		seekVideo(AMDPlayerScreen.objProgressBar, usertype);
		waitTime(5000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Scrub/Seek");
	}

	public void ScrubSeekEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Scrub/Seek Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);
		verifyElementPresentAndClick(AMDHomePage.objCarouselConetentCard, "carousel content");
		waitTime(3000);
		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLogger("Player screen", "Player inline subscription link is displayed");
		} else {
			waitTime(6000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
			verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
			seekVideo(AMDPlayerScreen.objProgressBar, usertype);
			waitTime(3000);

			setFEProperty(usertype);
			mixpanel.FEProp.setProperty("Source", "home");
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
			mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");
			mixpanel.ValidateParameter("", "Scrub/Seek");
		}
	}

	public void ScrubSeekEventOfcontentFromSearchPage(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Scrub/Seek Event of content from search page");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		seekVideo(AMDPlayerScreen.objProgressBar, usertype);
		waitTime(4000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "Search_Tab");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Appsflyer Source", "Organic");
		mixpanel.FEProp.setProperty("Appsflyer ID", "VzZG4KdWFLkrRKZheffaHe");
		mixpanel.ValidateParameter("", "Scrub/Seek");
	}

	public void ScrubSeekEventOfContentFromMyWatchListPage(String usertype) throws Exception {
		if (!(usertype.equalsIgnoreCase("Guest"))) {
			extent.HeaderChildNode("Scrub/Seek Event of content from My WatchList page");
			click(AMDHomePage.objMoreMenu, "More menu");
			click(AMDMoreMenu.objWatchlist, "Watchlist option");
			click(AMDUserSessionManagement.objMoviesTabUnderWatchList, "Movies Tab");
			waitTime(5000);
			boolean flag = false;
			boolean contentsInMoviesTab = verifyIsElementDisplayed(
					AMDUserSessionManagement.objcontentTitleInWatchListAndReminders);
			if (contentsInMoviesTab == true) {
				getDriver()
						.findElement(By.xpath("(//*[@resource-id='com.graymatrix.did:id/txt_reminder_item_title'])[1]"))
						.click();
				verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
				seekVideo(AMDPlayerScreen.objProgressBar, usertype);
				waitTime(4000);

				setFEProperty(usertype);
				mixpanel.FEProp.setProperty("Source", "home");
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

				mixpanel.ValidateParameter("", "Scrub/Seek");
			} else {
				logger.info("No contents in Watchlist");
				extentLoggerWarning("Watchlist", "No contents in Watchlist");
			}
		} else {
			logger.info("Watchlist is not applicable for " + usertype);
			extentLogger("Guest User", "Watchlist is not applicable for " + usertype);
		}
	}

	public void ScrubSeekEventOfContentFromUpNextRail(String usertype, String keyword4) throws Exception {
		extent.HeaderChildNode("Verify Scrub/Seek event of content from Upnext rail");
		click(AMDSearchScreen.objSearchIcon, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search Box");
		type(AMDSearchScreen.objSearchBoxBar, keyword4 + "\n", "Search bar");
		hideKeyboard();
		waitTime(4000);
		waitForElementDisplayed(AMDSearchScreen.objAllTab, 10);
		click(AMDSearchScreen.objFirstContentInSearchResult, "Search result");
		waitTime(8000);
		Swipe("UP", 1);
		if (verifyElementDisplayed(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray)) {
			verifyElementPresentAndClick(AMDPlayerScreen.objFirstContentCardTitleInUpnextTray, "Upnext rail content");
		}
		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		registerPopUpClose();
		completeProfilePopUpClose(usertype);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		seekVideo(AMDPlayerScreen.objProgressBar, usertype);
		waitTime(5000);

		setFEProperty(usertype);
		mixpanel.FEProp.setProperty("Source", "home");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");

		mixpanel.ValidateParameter("", "Scrub/Seek");
	}

	public void setFEProperty(String pUserType) {
		if (!(pUserType.equalsIgnoreCase("Guest"))) {

			if (pUserType.equalsIgnoreCase("SubscribedUser")) {
				Username = getParameterFromXML("SubscribedUserName");
				Password = getParameterFromXML("SubscribedPassword");
			} else if (pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				Username = getParameterFromXML("NonSubscribedUserName");
				Password = getParameterFromXML("NonSubscribedUserPassword");
			}

			Properties pro = new Properties();
			pro = ResponseInstance.getUserSettingsDetails(Username, Password);

			String value = pro.getProperty("eduauraaClaimed");
			if (value.equalsIgnoreCase("Empty")) {
				value = "false";
			}
			String value2 = pro.getProperty("parental_control");
			if (value2.equalsIgnoreCase("Empty")) {
				value2 = "N/A";
			}
			mixpanel.FEProp.setProperty("New Video Streaming Quality Setting", pro.getProperty("streaming_quality"));
			mixpanel.FEProp.setProperty("New Autoplay Setting", pro.getProperty("auto_play"));
			mixpanel.FEProp.setProperty("New Stream Over Wifi Setting", pro.getProperty("stream_over_wifi"));
			mixpanel.FEProp.setProperty("New Download Quality Setting", pro.getProperty("download_quality"));
			mixpanel.FEProp.setProperty("New Download Over Wifi Setting", pro.getProperty("download_over_wifi"));
			mixpanel.FEProp.setProperty("New App Language", pro.getProperty("display_language"));
			mixpanel.FEProp.setProperty("New Content Language", pro.getProperty("content_language"));
			mixpanel.FEProp.setProperty("hasEduauraa", value);
			mixpanel.FEProp.setProperty("isEduauraa", value);
			mixpanel.FEProp.setProperty("Parent Control Setting", value2);
			mixpanel.FEProp.setProperty("Partner Name", "Zee5");
			mixpanel.FEProp.setProperty("Gender",
			ResponseInstance.getUserData_NativeAndroid(Username, Password).getProperty("gender"));
		} else {
			mixpanel.FEProp.setProperty("New Video Streaming Quality Setting", "Auto");
			mixpanel.FEProp.setProperty("New Autoplay Setting", "true");
			mixpanel.FEProp.setProperty("New Stream Over Wifi Setting", "false");
			mixpanel.FEProp.setProperty("New Download Quality Setting", "Ask Each Time");
			mixpanel.FEProp.setProperty("New Download Over Wifi Setting", "false");
			mixpanel.FEProp.setProperty("New App Language", "en");
			mixpanel.FEProp.setProperty("New Content Language", "en,kn");
		}
	}

	public void VerifyFirstAppLaunchEvent(String usertype) throws Exception {
		if (usertype.equalsIgnoreCase("Guest")) {
			HeaderChildNode("Verify First App Launch event");
			logger.info("Relaunching the application");
			extent.extentLogger("Relaunch", "Relaunching the application");
			waitTime(10000);
			getDriver().quit();
			relaunch = true;
			new Zee5ApplicasterBusinessLogic("zee");
			setFEProperty(userType);
			mixpanel.FEProp.setProperty("User Type", "guest");
			mixpanel.FEProp.setProperty("Source", "N/A");
			mixpanel.FEProp.setProperty("Page Name", "N/A");
			mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
			mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
			mixpanel.ValidateParameter("", "First Launch");
		}
	}

	@SuppressWarnings("static-access")
	public void verifyLoginPasswordEnteredEvent() throws Exception {
		HeaderChildNode("Login Password Entered event");

		String userName = getParameterFromXML("NonSubscribedUserName");
		String Password = getParameterFromXML("NonSubscribedUserPassword");
		verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
		verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
		type(AMDLoginScreen.objEmailIdField, userName, "Email Field");
		verifyElementPresentAndClick(AMDLoginScreen.objProceedBtn, "Proceed Button");
		verifyElementPresentAndClick(AMDLoginScreen.objPasswordField, "Password Field");
		type(AMDLoginScreen.objPasswordField, Password, "Password field");
		hideKeyboard();
		waitTime(10000);
		setFEProperty(userType);

		mixpanel.FEProp.setProperty("Source", "LoginRegister");
		mixpanel.FEProp.setProperty("Page Name", "Login");
		mixpanel.ValidateParameter("", "Login Password Entered");

	}

	@SuppressWarnings("static-access")
	public void verifyUserNameEnteredEvent(String userType) throws Exception {
		if (userType.equalsIgnoreCase("Guest")) {
			HeaderChildNode("User Name Entered event");

			String userName = getParameterFromXML("NonSubscribedUserName");
			verifyElementPresentAndClick(AMDLoginScreen.objBrowseForFreeBtn, "Browse for Free CTA");
			verifyElementPresentAndClick(AMDLoginScreen.objEmailIdField, "Email field");
			type(AMDLoginScreen.objEmailIdField, userName, "Email Field");
			hideKeyboard();
			waitTime(10000);
			setFEProperty(userType);

			mixpanel.FEProp.setProperty("Page Name", "LoginRegister");
			mixpanel.FEProp.setProperty("Source", "Intro");
			mixpanel.ValidateParameter("", "Username Entered");
		}

	}

	@SuppressWarnings("static-access")
	public void verifyCarouselBannerSwipeEvent(String tabName, String userType) throws Exception {
		extent.HeaderChildNode("Verify Carousel Banner Swipe Event Across tabs");
		SelectTopNavigationTab(tabName);
		waitForElementDisplayed(AMDHomePage.objCarouselDots, 10);
		waitTime(10000);

		if (verifyElementDisplayed(AMDHomePage.objBannerAd)) {
			verifyElementPresent(AMDHomePage.objCarouselUnitwithmastHeadAdbanner,
					"Carousel unit as first unit on " + tabName + " screen");
		} else {
			verifyElementPresent(AMDHomePage.objCarouselUnitwhenNomastHeadAdbanner,
					"Carousel unit as first unit on " + tabName + " screen");
		}

		// Validating Carousel manual swipe
		String width = getAttributValue("width", AMDHomePage.objCarouselConetentCard);
		String bounds = getAttributValue("bounds", AMDHomePage.objCarouselConetentCard);
		String b = bounds.replaceAll(",", " ").replaceAll("]", " ");
		String height = b.split(" ")[1];
		waitTime(3000);
		HeaderChildNode("Carousel Banner Swipe Left");
		String Carouseltitle1 = getText(AMDHomePage.objCarouselTitle1);
		logger.info(Carouseltitle1);
		extentLoggerPass("Carousel Title", Carouseltitle1);
		carouselCardsSwipe("LEFT", 1, width, height);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Source", "home");
		mixpanel.FEProp.setProperty("Tab Name", "movies");
		mixpanel.ValidateParameter("", "Carousal Banner Swipe");

	}

	public void verifySessionEvent(String userType) throws Exception {
		HeaderChildNode("App Session event");
		relaunchTillDisplayLanguageScreen(true);
		mixpanel.FEProp.setProperty("User Type", "guest");
		setFEProperty(userType);
		mixpanel.ValidateParameter("", "App session");
	}

	public void VerifyUIUXDownloadScreen(String userType) throws Exception {
		extent.HeaderChildNode("Verify the UI/UX of Download landing screen as " + userType);
		System.out.println("\nVerify the UI/UX of Download landing screen as " + userType);
		waitTime(5000);
		verifyElementExist(AMDHomePage.objDownloadBtn, "Downloads tab at the bottom navigation bar");
		click(AMDHomePage.objDownloadBtn, "Downloads tab");
		waitTime(3000);
		verifyElementExist(AMDDownloadPage.objDwnloadsHeader, "Downloads header at the top on center of the screen");
		verifyElementExist(AMDDownloadPage.objshowstab, "Shows tab in Downloads landing screen");
		verifyElementExist(AMDDownloadPage.objmoviestab, "Movies tab in Downlaods landing screen");
		verifyElementExist(AMDDownloadPage.objvideostab, "Videos tab in Downloads landing screen ");
		String getPropertyValue = getAttributValue("enabled", AMDDownloadPage.objshowstab);
		if (getPropertyValue.equalsIgnoreCase("true")) {
			extent.extentLogger("Shows tab", "Shows tab is by default highlighted");
			logger.info("Shows tab is by default highlighted");
		} else {
			extent.extentLoggerFail("Shows tab", "Shows tab fails to highlight by default");
			logger.error("Shows tab fails to highlight by default");
		}
		verifyElementPresentAndClick(AMDDownloadPage.objshowstab, "Shows tab in Downloads landing screen");
		verifyElementExist(AMDDownloadPage.objBrowseToDownloadBtn, "Browse to Download CTA under Shows tab");
		verifyElementPresentAndClick(AMDDownloadPage.objmoviestab, "Movies tab in Downlaods landing screen");
		verifyElementExist(AMDDownloadPage.objBrowseToDownloadBtn, "Browse to Download CTA under Shows tab");
		verifyElementPresentAndClick(AMDDownloadPage.objvideostab, "Videos tab in Downloads landing screen");
		verifyElementExist(AMDDownloadPage.objBrowseToDownloadBtn, "Browse to Download CTA under Shows tab");
	}

	public void VerifyBrowseToDownloadFunctionality(String userType) throws Exception {
		extent.HeaderChildNode("Verify Browse to Download CTA functionality as " + userType);
		System.out.println("\nVerify Browse to Download CTA functionality as " + userType);
		String getSelectedTabName;
		verifyElementPresentAndClick(AMDDownloadPage.objshowstab, "Shows tab in Downloads landing screen");
		verifyElementPresentAndClick(AMDDownloadPage.objBrowseToDownloadBtn, "Browse to Download CTA under Shows tab");
		waitTime(3000);
		getSelectedTabName = getText(AMDHomePage.objSelectedTab);
		if (getSelectedTabName.equalsIgnoreCase("Shows")) {
			extent.extentLogger("Shows tab", "User is navigated to Shows landing page");
			logger.info("User is navigated to Shows landing page");
		} else {
			extent.extentLoggerFail("Shows tab", "User fails to navigate to Shows landing page and instead displayed : "
					+ getSelectedTabName + " landing screen");
			logger.error("User fails to navigate to Shows landing page and instead displayed : " + getSelectedTabName
					+ " landing screen");
		}
		click(AMDHomePage.objDownloadBtn, "Downloads tab");
		verifyElementPresentAndClick(AMDDownloadPage.objmoviestab, "Movies tab in Downlaods landing screen");
		verifyElementPresentAndClick(AMDDownloadPage.objBrowseToDownloadBtn, "Browse to Download CTA under Movies tab");
		waitTime(3000);
		getSelectedTabName = getText(AMDHomePage.objSelectedTab);
		if (getSelectedTabName.equalsIgnoreCase("Movies")) {
			extent.extentLogger("Movies tab", "User is navigated to Movies landing page");
			logger.info("User is navigated to Movies landing page");
		} else {
			extent.extentLoggerFail("Movies tab",
					"User fails to navigate to Movies landing page and instead displayed : " + getSelectedTabName
							+ " landing screen");
			logger.error("User fails to navigate to Movies landing page and instead displayed : " + getSelectedTabName
					+ " landing screen");
		}
		click(AMDHomePage.objDownloadBtn, "Downloads tab");
		verifyElementPresentAndClick(AMDDownloadPage.objvideostab, "Videos tab in Downloads landing screen");
		verifyElementPresentAndClick(AMDDownloadPage.objBrowseToDownloadBtn, "Browse to Download CTA under Videos tab");
		waitTime(3000);
		getSelectedTabName = getText(AMDHomePage.objSelectedTab);
		if (getSelectedTabName.equalsIgnoreCase("Videos")) {
			extent.extentLogger("Videos tab", "User is navigated to videos landing page");
			logger.info("User is navigated to Videos landing page");
		} else {
			extent.extentLoggerFail("Movies tab",
					"User fails to navigate to Videos landing page and instead displayed : " + getSelectedTabName
							+ " landing screen");
			logger.error("User fails to navigate to Videos landing page and instead displayed : " + getSelectedTabName
					+ " landing screen");
		}
	}

	public void videoViewEventFromHomePage(String usertype, String tabName) throws Exception {
		setFEProperty(usertype);
		String trayName = ResponseInstance.getTrayNameFromPage(tabName, usertype);
		SwipeUntilFindElement(AMDHomePage.objTrayTitle(trayName), "UP");
		waitTime(3000);
		click(AMDGenericObjects.objSelectFirstCardFromTrayTitle(trayName), "Content Card");
		waitTime(5000);
		String pManufacturer = DeviceDetails.OEM;
		System.out.println(pManufacturer);
		mixpanel.FEProp.setProperty("Source", "N/A");
		mixpanel.FEProp.setProperty("Page Name", "home");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
//		mixpanel.FEProp.setProperty("Manufacturer", pManufacturer);
		mixpanel.FEProp.setProperty("Brand", pManufacturer);
		mixpanel.ValidateParameter("", "Video View");
	}

	public static String getSource(String tabName) {

		String pSource = "N/A";
		switch (tabName.toLowerCase()) {
		case "home":
			pSource = "Homepage";
			break;

		case "tv shows":
			pSource = "Homepage";
			break;

		case "movies":
			pSource = "Homepage";
			break;

		case "premium":
			pSource = "Homepage";
			break;

		case "news":
			pSource = "Homepage";
			break;

		case "club":
			pSource = "news";
			break;

		case "eduauraa":
			pSource = "Homepage";
			break;

		case "music":
			pSource = "Homepage";
			break;

		case "live tv":
			pSource = "kids";
			break;

		case "zeeoriginals":
			pSource = "livetv";
			break;

		case "web series":
			pSource = "Homepage";
			break;

		}
		return pSource;
	}

	public static String getPageName(String tabName) {

		String pPageName = "N/A";
		switch (tabName.toLowerCase()) {
		case "home":
			pPageName = "home";
			break;

		case "shows":
			pPageName = "tvshows";
			break;

		case "movies":
			pPageName = "movies";
			break;

		case "premium":
			pPageName = "premium";
			break;

		case "music":
			pPageName = "music";
			break;

		case "eduauraa":
			pPageName = "kids";
			break;

		case "news":
			pPageName = "news";
			break;

		case "club":
			pPageName = "premium";
			break;

		case "zeeoriginals":
			pPageName = "originals";
			break;

		case "zee5 originals":
			pPageName = "originals";
			break;

		case "live tv":
			pPageName = "livetv";
			break;

		default:
			pPageName = "home";
			break;
		}
		return pPageName;
	}

	public void videoViewEventFromTopNavigationPage(String pUsertype, String pTabName) throws Exception {

		waitForElementDisplayed(AMDHomePage.objTitle, 20);
		SelectTopNavigationTab(pTabName);
		String contentLang = ResponseInstance.getContentLanguageForAppMixpanel(pUsertype);
		System.out.println(contentLang);

//		String trayName = ResponseInstance.getTrayNameFromPage(pTabName, pUsertype);
		String trayName = ResponseInstance.getRailNameFromPage(pTabName, pUsertype);

		if (pTabName.equalsIgnoreCase("Live TV") || pTabName.equalsIgnoreCase("News")) {
			waitTime(5000);
			waitForElementDisplayed(AMDGenericObjects.objTrayTitle, 30);
		}

		SwipeUntilFindElement(AMDHomePage.objRailName(trayName), "UP");
		waitTime(3000);
		click(AMDGenericObjects.objSelectFirstCardFromRailName(trayName), "Content Card");
		if (pUsertype.equalsIgnoreCase("Guest") || pUsertype.equalsIgnoreCase("NonSubscribedUser")) {
			registerPopUpClose();
			waitForAdToFinishInAmd();
			waitTime(2000);
			registerPopUpClose();
			Back(1);
		} else {
			waitTime(3000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player Screen");
			verifyElementPresentAndClick(AMDGenericObjects.objBackBtn, "Back button");
		}

//		####### Set All Parameters values ####### 
		setFEProperty(pUsertype);
		setUserType_SubscriptionProperties(pUsertype);

//		String pPage = getPageName(pTabName);
//		String pSource = getSource(pTabName);
		String pPage = "ConsumptionPage";
		String pSource = "Homepage";
		String pManufacturer = DeviceDetails.OEM;

//		String pAdId = getAdId();
//		mixpanel.FEProp.setProperty("Ad ID", pAdId);
//		mixpanel.FEProp.setProperty("Advertisement ID", pAdId);

		mixpanel.FEProp.setProperty("Source", pSource);
		mixpanel.FEProp.setProperty("Page Name", pPage);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
		mixpanel.FEProp.setProperty("brand", pManufacturer);

		mixpanel.ValidateParameter("", "Video View");
	}

	public void ResumeEventForTrayContent(String usertype, String tabName, String AdvertisementID) throws Exception {
		extent.HeaderChildNode("Resume Event for Tray content of " + tabName);
		String trayName = ResponseInstance.getTrayNameFromPage(tabName, usertype);
		waitTime(10000);
		SwipeUntilFindElement(AMDHomePage.objTrayTitle(trayName), "UP");
		waitTime(3000);
		click(AMDGenericObjects.objSelectFirstCardFromTrayTitle(trayName), "Content Card");
		verifyElementPresentAndClick(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(3000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		setFEProperty(usertype);
		if (usertype.equalsIgnoreCase("SubscribedUser")) {
			ResponseInstance.subscriptionDetails();
		}
		String pManufacturer = DeviceDetails.OEM;
		System.out.println(pManufacturer);
		mixpanel.FEProp.setProperty("Source", "N/A");
		mixpanel.FEProp.setProperty("Page Name", "home");
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("Brand", pManufacturer);
		mixpanel.FEProp.setProperty("Advertisement ID", AdvertisementID);
		mixpanel.ValidateParameter("", "Resume");
	}

	public void SearchContentFromSearchPage(String userType, String contentType, String contentId, String contentName)
			throws Exception {

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search edit box");
		type(AMDSearchScreen.objSearchEditBox, contentName, "Search edit box");
//		click(AMDSearchScreen.objSearchItemBySearchTitle(contentName),"Searched content");
		click(AMDSearchScreen.objFirstSearchResult(contentName), "Searched content");

		if (!userType.equalsIgnoreCase("SubscribedUser")) {
			waitForAdToFinishInAmd();
		}
		waitTime(2000);
		if (contentType.equalsIgnoreCase("Trailer")) {
			ResponseInstance.setPropertyForContentDetailsFromSearchPage(contentId);
		}

		if (contentType.equalsIgnoreCase("Free")) {
			ResponseInstance.setPropertyForContentDetailsFromSearchPage(contentId);
		}

		if (contentType.equalsIgnoreCase("Premium")) {
			ResponseInstance.setPropertyForContentDetailsFromSearchPage(contentId);
		}
	}

	public void PauseEvent(String userType, String contentType, String contentID, String contentName) throws Exception {
		HeaderChildNode("Verify Pause Event from search navigation");
		SearchContentFromSearchPage(userType, contentType, contentID, contentName);
		waitTime(3000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		click(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(2000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.ValidateParameter("", "Pause");

	}

	public void videoViewEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Video view Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);

		boolean flagBox = verifyIsElementDisplayed(AMDHomePage.objSboxIcon);
		String pSugarBox = String.valueOf(flagBox);

		String contentName = ResponseInstance.getCarouselContentFromAPI(usertype, tabName);
		System.out.println(contentName);

		waitForElementAndClickIfPresent(AMDHomePage.objContentTitle(contentName), 60, "carousal content");

		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		if (usertype.equalsIgnoreCase("Guest")) {
			registerPopUpClose();
		}
		completeProfilePopUpClose(usertype);

		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		boolean adulterrormsg = verifyIsElementDisplayed(AMDPlayerScreen.objAdultErrorMessage);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLoggerWarning("Player screen", "Player inline subscription link is displayed");
		} else if (adulterrormsg == true) {
			logger.info("error message saying 'This content is for Adult view only' is displayed");
			extentLoggerWarning("Player screen",
					"error message saying 'This content is for Adult view only' is displayed");
		} else {
			waitTime(5000);
			if (!(verifyIsElementDisplayed(AMDPlayerScreen.objFullscreenIcon))) {
				click(AMDPlayerScreen.objPlayerScreen, "Player screen");
			}
			boolean eventFlag = false;
			click(AMDPlayerScreen.objPauseIcon, "Pause icon");
			eventFlag = verifyElementPresent(AMDPlayerScreen.objFullscreenIcon, "Player screen");
			waitTime(2000);

			if (eventFlag) {
//				if (usertype.equalsIgnoreCase("SubscribedUser")) {
//					ResponseInstance.subscriptionDetails();
//				}
				String pPage = "ConsumptionPage";
				String pSource = getSource(tabName);
				String pManufacturer = DeviceDetails.OEM;
				// String pAdId = getAdId();

				setFEProperty(usertype);
				setUserType_SubscriptionProperties(usertype);

				// mixpanel.FEProp.setProperty("Ad ID", getParameterFromXML("AdID"));
				// mixpanel.FEProp.setProperty("Advertisement ID", getParameterFromXML("AdID"));
				mixpanel.FEProp.setProperty("Source", pSource);
				mixpanel.FEProp.setProperty("Page Name", pPage);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("Manufacturer", pManufacturer);
				mixpanel.FEProp.setProperty("Brand", pManufacturer);
				// mixpanel.FEProp.setProperty("Sugar Box Value",pSugarBox );
				mixpanel.FEProp.setProperty("Carousal Name", "N/A");

				mixpanel.ValidateParameter("", "Video View");
			} else {
				logger.info("Failed to display player screen");
				extentLoggerWarning("Event", "Failed to display player screen");
			}
		}
	}

	public void PlayerViewChangedEvent(String userType, String contentType, String contentID, String contentName)
			throws Exception {
		HeaderChildNode("Verify Resume Event from search navigation");
		SearchContentFromSearchPage(userType, contentType, contentID, contentName);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		click(AMDPlayerScreen.objPauseIcon, "Pause icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(4000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.ValidateParameter("", "Player View Changed");

	}

	public void VideoWatchDurationEvent(String userType, String contentType, String contentID, String contentName)
			throws Exception {
		HeaderChildNode("Verify Resume Event from search navigation");
		SearchContentFromSearchPage(userType, contentType, contentID, contentName);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		scrubProgressBarTillEnd(AMDPlayerScreen.objProgressBar);
		waitTime(4000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.ValidateParameter("", "Video Watch Duration");
	}

	public void VideoExitEvent(String userType, String contentType, String contentID, String contentName)
			throws Exception {
		HeaderChildNode("Verify Resume Event from search navigation");
		SearchContentFromSearchPage(userType, contentType, contentID, contentName);
		waitTime(6000);
		verifyElementPresent(AMDPlayerScreen.objPlayerScreen, "Player screen");
		Back(1);
		waitTime(2000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		Mixpanel.ValidateParameter("", "Video Exit");
	}

	public void ResumeEvent(String userType, String contentType, String contentID, String contentName)
			throws Exception {
		HeaderChildNode("Verify Resume Event from search navigation");
		SearchContentFromSearchPage(userType, contentType, contentID, contentName);
		waitTime(6000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player screen");
		click(AMDPlayerScreen.objPauseIcon, "Pause icon");
		waitTime(3000);
		verifyElementPresentAndClick(AMDPlayerScreen.objPlayIcon, "Play icon");
		waitTime(2000);
		verifyElementPresent(AMDPlayerScreen.objPlayerScreen, "Player screen");
		waitTime(2000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.ValidateParameter("", "Resume");
	}

	public void VideoViewEvent(String userType, String contentType, String contentID, String contentName)
			throws Exception {
		HeaderChildNode("Verify Pause Event from search navigation");
		SearchContentFromSearchPage(userType, contentType, contentID, contentName);
		waitTime(3000);
		verifyElementPresent(AMDPlayerScreen.objPlayerScreen, "Player screen");
		waitTime(2000);
		setFEProperty(userType);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.ValidateParameter("", "Video View");
	}

	public void setUserType_SubscriptionProperties(String pUsertype) {
		String pUsername, pPassword;
		if (!pUsertype.equalsIgnoreCase("Guest")) {
			if (pUsertype.equalsIgnoreCase("SubscribedUser")) {
				pUsername = getParameterFromXML("SubscribedUserName");
				pPassword = getParameterFromXML("SubscribedPassword");

				ResponseInstance.setSubscriptionDetails_NativeAndroid();
				mixpanel.FEProp.setProperty("User Type", "Premium");
			} else {
				pUsername = getParameterFromXML("NonSubscribedUserName");
				pPassword = getParameterFromXML("NonSubscribedUserPassword");
				mixpanel.FEProp.setProperty("User Type", "registered");
			}
			mixpanel.FEProp.setProperty("Email", pUsername);
		} else {
			mixpanel.FEProp.setProperty("User Type", "Guest");
		}
	}

	public void VideoViewEventThroughSearch(String userType, String pTypeOfContent, String pTabName) throws Exception {
		HeaderChildNode("Verify Video View Event through Search navigation");
		System.out.println("\nVerify Video View Event through Search navigation");

		String pPage = "ConsumptionPage";
		String pSource = "SearchPage";
		String pManufacturer = DeviceDetails.OEM;
		String pAdId = getAdId();

		ArrayList<String> arrContentDetails = ResponseInstance.getTrayNameAndContentID("Home", pTypeOfContent);
		String getTrayName, getContentName = null, getContentID = null;
		if (arrContentDetails.size() != 0) {
			getTrayName = arrContentDetails.get(0);
			getContentName = arrContentDetails.get(1);
			getContentID = arrContentDetails.get(2);

			System.out.println("Tray Name: " + getTrayName);
			System.out.println("Content Name: " + getContentName);
			System.out.println("Content ID: " + getContentID);
		}

		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search edit box");
		type(AMDSearchScreen.objSearchEditBox, getContentName, "Search edit box");
		click(AMDSearchScreen.objFirstSearchResult(getContentName), "Select Searched content");
		waitTime(2000);
		if (!userType.equalsIgnoreCase("SubscribedUser")) {
			registerPopUpClose();
			waitForAdToFinishInAmd();
			waitTime(2000);
			Back(1);
		} else {
			waitTime(4000);
			Back(1);
		}

		if (getContentID != null) {
			ResponseInstance.setPropertyForContentDetailsFromSearchPage(getContentID);
			setFEProperty(userType);
			setUserType_SubscriptionProperties(userType);

			mixpanel.FEProp.setProperty("Ad ID", pAdId);
			mixpanel.FEProp.setProperty("Advertisement ID", pAdId);
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);

			mixpanel.ValidateParameter("", "Video View");
		} else {
			logger.error("Failed to fetch the ContentID from API to validate against Mixpanel Dashboard");
			extent.extentLoggerWarning("Validation",
					"Failed to fetch the ContentID from API to validate against Mixpanel Dashboard");
		}

	}

	public void VideoViewEventFromSearchTab(String userType, String contentType, String contentID, String contentName)
			throws Exception {
		HeaderChildNode("Verify Video View Event from Search tab navigation");
		System.out.println("\nVerify Video View Event from Search tab navigation");

		String pSource = "SearchPage";
		String pPage = "ConsumptionPage";
		String pManufacturer = DeviceDetails.OEM;

		SearchContentFromSearchPage(userType, contentType, contentID, contentName);
		waitTime(3000);
		verifyElementPresent(AMDPlayerScreen.objPlayerScreen, "Player screen");
		waitTime(2000);
		Back(1);
		setFEProperty(userType);
		setUserType_SubscriptionProperties(userType);

		mixpanel.FEProp.setProperty("Source", pSource);
		mixpanel.FEProp.setProperty("Page Name", pPage);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
		mixpanel.FEProp.setProperty("brand", pManufacturer);
		mixpanel.ValidateParameter("", "Video View");
	}

	public void CarousalBannerImpressionEvent(String pUsertype, String pTabName) throws Exception {
		HeaderChildNode("Verify Carousek banner impression Event from top navigation");

		String pPage = "Homepage";
		String pSource = "SplashPage";
		String pManufacturer = DeviceDetails.OEM;

		waitForElementDisplayed(AMDHomePage.objTitle, 20);
		SelectTopNavigationTab(pTabName);
		waitTime(3000);
		String contentLang = ResponseInstance.getContentLanguageForAppMixpanel(pUsertype);
		System.out.println(contentLang);

		setFEProperty(pUsertype);
		setUserType_SubscriptionProperties(pUsertype);

		mixpanel.FEProp.setProperty("Source", pSource);
		mixpanel.FEProp.setProperty("Page Name", pPage);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
		mixpanel.FEProp.setProperty("brand", pManufacturer);

		mixpanel.ValidateParameter("", "Carousal Banner Impression");

	}

	public void ScreenViewEventFromTopNavigationPage(String pUsertype, String pTabName) throws Exception {

		waitForElementDisplayed(AMDHomePage.objTitle, 20);
		SelectTopNavigationTab(pTabName);
		String contentLang = ResponseInstance.getContentLanguageForAppMixpanel(pUsertype);
		System.out.println(contentLang);

		String trayName = ResponseInstance.getRailNameFromPage(pTabName, pUsertype);

		if (pTabName.equalsIgnoreCase("Live TV") || pTabName.equalsIgnoreCase("News")) {
			waitTime(5000);
			waitForElementDisplayed(AMDGenericObjects.objTrayTitle, 30);
		}

		SwipeUntilFindElement(AMDHomePage.objRailName(trayName), "UP");
		waitTime(3000);
		click(AMDGenericObjects.objSelectFirstCardFromRailName(trayName), "Content Card");
		if (pUsertype.equalsIgnoreCase("Guest") || pUsertype.equalsIgnoreCase("NonSubscribedUser")) {
			registerPopUpClose();
			waitForAdToFinishInAmd();
			waitTime(2000);
			registerPopUpClose();
			Back(1);
		} else {
			waitTime(3000);
			verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player Screen");
			verifyElementPresentAndClick(AMDGenericObjects.objBackBtn, "Back button");
		}

//		####### Set All Parameters values ####### 
		setFEProperty(pUsertype);
		setUserType_SubscriptionProperties(pUsertype);

//		String pPage = getPageName(pTabName);
//		String pSource = getSource(pTabName);
		String pPage = "ConsumptionPage";
		String pSource = "Homepage";
		String pManufacturer = DeviceDetails.OEM;

		mixpanel.FEProp.setProperty("Source", pSource);
		mixpanel.FEProp.setProperty("Page Name", pPage);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
		mixpanel.FEProp.setProperty("brand", pManufacturer);

		mixpanel.ValidateParameter("", "Screen View");
	}

	public void PlayerViewChangedEventForCarouselContent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Player view changed Event for carousel content");
		waitTime(10000);
		SelectTopNavigationTab(tabName);

		boolean flagBox = verifyIsElementDisplayed(AMDHomePage.objSboxIcon);
		String pSugarBox = String.valueOf(flagBox);

		String contentName = ResponseInstance.getCarouselContentFromAPI2(usertype, tabName);
		System.out.println(contentName);

		waitForElementAndClickIfPresent(AMDHomePage.objContentTitle(contentName), 7, "carousal content");

		if (!(usertype.equalsIgnoreCase("SubscribedUser"))) {
			waitForAdToFinishInAmd();
		}
		if (usertype.equalsIgnoreCase("Guest")) {
			registerPopUpClose();
		}
		completeProfilePopUpClose(usertype);

		boolean inlineLink = verifyIsElementDisplayed(AMDPlayerScreen.objPremiumTextOnPlayer);
		boolean adulterrormsg = verifyIsElementDisplayed(AMDPlayerScreen.objAdultErrorMessage);
		if (inlineLink == true) {
			logger.info("Player inline subscription link is displayed");
			extentLogger("Player screen", "Player inline subscription link is displayed");
		} else if (adulterrormsg == true) {
			logger.info("error message saying 'This content is for Adult view only' is displayed");
			extentLogger("Player screen", "error message saying 'This content is for Adult view only' is displayed");
		} else {
			waitTime(5000);
			if (!(verifyIsElementDisplayed(AMDPlayerScreen.objFullscreenIcon))) {
				click(AMDPlayerScreen.objPlayerScreen, "Player screen");
			}
			boolean eventFlag = true;
			click(AMDPlayerScreen.objPauseIcon, "Pause icon");

			ScreenOrientation orientation1 = getDriver().getOrientation();
			String ScreenOrientation1 = orientation1.toString();

			eventFlag = verifyElementPresentAndClick(AMDPlayerScreen.objFullscreenIcon, "Full screen icon");
			waitTime(2000);

			ScreenOrientation orientation2 = getDriver().getOrientation();
			String ScreenOrientation2 = orientation2.toString();

			if (eventFlag) {
				String pPage = "ConsumptionPage";
				String pSource = getSource(tabName);
				String pManufacturer = DeviceDetails.OEM;

				setFEProperty(usertype);
				setUserType_SubscriptionProperties(usertype);

				mixpanel.FEProp.setProperty("Source", pSource);
				mixpanel.FEProp.setProperty("Page Name", pPage);
				mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
				mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
				mixpanel.FEProp.setProperty("brand", pManufacturer);
				mixpanel.FEProp.setProperty("New View Position", ScreenOrientation2);
				mixpanel.FEProp.setProperty("Old View Position", ScreenOrientation1);
				mixpanel.FEProp.setProperty("Carousal Name", "N/A");
				if(tabName.equalsIgnoreCase("TV Shows") || tabName.equalsIgnoreCase("Web Series")) {
					mixpanel.FEProp.setProperty("Series", contentName);
				}
				
				mixpanel.ValidateParameter("", "Player View Changed");
			} else {
				logger.info("Failed to change the player view");
				extentLoggerWarning("Event", "Failed to change the player view");
			}
		}
	}

	public void AdInitializedEvent(String pUsertype, String pTabName) throws Exception {

//		String pPage = getPageName(pTabName);
//		String pSource = getSource(pTabName);
		String pPage = "ConsumptionPage";
		String pSource = "Homepage";
		String pManufacturer = DeviceDetails.OEM;
		if (!(userType.equalsIgnoreCase("SubscribedUser"))) {
			waitForElementDisplayed(AMDHomePage.objTitle, 20);
			SelectTopNavigationTab(pTabName);
			String contentLang = ResponseInstance.getContentLanguageForAppMixpanel(pUsertype);
			System.out.println(contentLang);

			String trayName = ResponseInstance.getRailNameFromPage(pTabName, pUsertype);

			if (pTabName.equalsIgnoreCase("Live TV") || pTabName.equalsIgnoreCase("News")) {
				waitTime(5000);
				waitForElementDisplayed(AMDGenericObjects.objTrayTitle, 30);
			}

			SwipeUntilFindElement(AMDHomePage.objRailName(trayName), "UP");
			waitTime(3000);
			click(AMDGenericObjects.objSelectFirstCardFromRailName(trayName), "Content Card");
			if (pUsertype.equalsIgnoreCase("Guest") || pUsertype.equalsIgnoreCase("NonSubscribedUser")) {
				registerPopUpClose();
				waitForAdToFinishInAmd();
				waitTime(2000);
				registerPopUpClose();
				Back(1);
			} else {
				waitTime(3000);
				verifyElementPresentAndClick(AMDPlayerScreen.objPlayerScreen, "Player Screen");
				verifyElementPresentAndClick(AMDGenericObjects.objBackBtn, "Back button");
			}

//		####### Set All Parameters values ####### 
			setFEProperty(pUsertype);
			setUserType_SubscriptionProperties(pUsertype);

			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);

			mixpanel.ValidateParameter("", "Ad Initialized");
		} else {
			logger.info("Ad Initialized Event is Not applicable for Subscribed user");
			extentLogger("Ad Initialized", "Ad Initialized Event is Not applicable for Subscribed user");
		}
	}

	public void event_LoginInitiated(String pUsertype) throws Exception {

		String pPage = "Login";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";

		waitTime(20000);
		if (!(userType.equalsIgnoreCase("Guest"))) {
			click(AMDHomePage.objDownloadBtn, "Download button");
			setFEProperty(pUsertype);
			setUserType_SubscriptionProperties(pUsertype);

			MixpanelAndroid.FEProp.setProperty("Source", pSource);
			MixpanelAndroid.FEProp.setProperty("Page Name", pPage);
			MixpanelAndroid.FEProp.setProperty("Player Name", "Kaltura Android");
			MixpanelAndroid.FEProp.setProperty("manufacturer", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("brand", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("Method", pMethod);
			MixpanelAndroid.FEProp.setProperty("User Type", "guest");
			MixpanelAndroid.FEProp.setProperty("Gender", "N/A");
			MixpanelAndroid.FEProp.setProperty("Age", "N/A");
			MixpanelAndroid.FEProp.setProperty("Partner Name", "N/A");

			MixpanelAndroid.ValidateParameter("", "Login Initatied");
		} else {
			logger.info("Login Initiated Event is Not applicable for Guest user");
			extentLogger("Login Initiated", "Login Initiated Event is Not applicable for Guest user");
		}
	}

	public void event_LoginResult(String pUsertype) throws Exception {

		String pPage = "Login";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";

		waitTime(5000);
		if (!(userType.equalsIgnoreCase("Guest"))) {
			setFEProperty(pUsertype);
			setUserType_SubscriptionProperties(pUsertype);

			MixpanelAndroid.FEProp.setProperty("Source", pSource);
			MixpanelAndroid.FEProp.setProperty("Page Name", pPage);
			MixpanelAndroid.FEProp.setProperty("Player Name", "Kaltura Android");
			MixpanelAndroid.FEProp.setProperty("manufacturer", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("brand", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("Method", pMethod);

			MixpanelAndroid.ValidateParameter("", "Login Result");
		} else {
			logger.info("Login Result Event is Not applicable for Guest user");
			extentLogger("Login Result", "Login Result Event is Not applicable for Guest user");
		}
	}

	public void event_LogOut(String pUsertype) throws Exception {

		String pPage = "Logout";
		String pSource = "More";
		String pManufacturer = DeviceDetails.OEM;

		waitTime(5000);
		if (!(userType.equalsIgnoreCase("Guest"))) {
			setFEProperty(pUsertype);
			setUserType_SubscriptionProperties(pUsertype);

			MixpanelAndroid.FEProp.setProperty("Source", pSource);
			MixpanelAndroid.FEProp.setProperty("Page Name", pPage);
			MixpanelAndroid.FEProp.setProperty("Player Name", "Kaltura Android");
			MixpanelAndroid.FEProp.setProperty("manufacturer", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("brand", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("User Type", "guest");

			MixpanelAndroid.ValidateParameter("", "Logout");
		} else {
			logger.info("Logout Event is Not applicable for Guest user");
			extentLogger("Logout", "Logout Event is Not applicable for Guest user");
		}
	}

	public void verifyVideoQualityChangeEvent(String qualityOption) throws Exception {
		extent.HeaderChildNode("Verify video quality change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		getDriver().findElement(By.xpath("//*[@id='qualityPixels']")).click();
		boolean var = verifyIsElementDisplayed(AMDMoreMenu.objSelectedVideoQualityOption("Auto"));
		
		if (!(pUserType.equalsIgnoreCase("Guest"))) {
			if(pUserType.equalsIgnoreCase("SubscribedUser")) {
				Username = getParameterFromXML("SubscribedUserName");
				Password = getParameterFromXML("SubscribedPassword");
			}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				Username = getParameterFromXML("NonSubscribedUserName");
				Password = getParameterFromXML("NonSubscribedUserPassword");
			}
	      	mixpanel.FEProp.setProperty("Old Video Streaming Quality Setting", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("streaming_quality"));
		}else {
			mixpanel.FEProp.setProperty("Old Video Streaming Quality Setting", "Auto");
		}
		if (var == true) {
			click(AMDMoreMenu.objVideoQualityOption(qualityOption), "Video quality option");
		} else {
			click(AMDMoreMenu.objAutoOption, "option Auto");
		}
		
		setFEProperty(pUserType);
		setUserType_SubscriptionProperties(pUserType);
		
		
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "selector");
		mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
		mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
	
		if(pUserType.equalsIgnoreCase("Guest")) {
			mixpanel.FEProp.setProperty("User Type", "guest");
		}
		if(var==true) {
			mixpanel.FEProp.setProperty("New Video Streaming Quality Setting", qualityOption);
		}else {
			mixpanel.FEProp.setProperty("New Video Streaming Quality Setting", "Auto");
		}
		
		mixpanel.ValidateParameter("", "Video Streaming Quality Changed");
		
		if(var==true) {
			click(AMDMoreMenu.objVideo_Quality(qualityOption), "Video quality option");
			click(AMDMoreMenu.objAutoOption, "option Auto");
		}
	}
	
	public void navigateToRegisterScreen() throws Exception {
		HeaderChildNode("Navigate to Login/Registration screen");
		verifyElementExist(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free button");
		logger.info("Browse for Free button is displayed in language : "
				+ getText(AMDOnboardingScreen.objBrowseForFreeBtn));
		extent.extentLoggerPass("Browse for Free button", "Browse for Free button is displayed in language : "
				+ getText(AMDOnboardingScreen.objBrowseForFreeBtn));

		verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");

		if (verifyIsElementDisplayed(AMDLoginScreen.objLoginLnk)) {
			logger.info("Login/Register Screen is displayed on selecting Browse for Free");
			extent.extentLoggerPass("Login/Register Screen", "Login/Register Screen is displayed on selecting Browse for Free");
		} else {
			logger.error("Login/Register Screen is not displayed on selecting Browse for Free");
			extent.extentLoggerFail("Login/Register Screen", "Login/Register Screen is not displayed on selecting Browse for Free");
		}
}
	
	public void newRegistrationThroughEmail() throws Exception {
		extent.HeaderChildNode("Register a New User");
		System.out.println("\nRegister a New User");
		
		String pDOB = "01/01/1990", pNewPassword = "123456";
		String regEmailID = generateRandomString(5) + "@zee.com";
		
		if(checkElementExist(AMDOnboardingScreen.objBrowseForFreeBtn)) {
			verifyElementPresentAndClick(AMDOnboardingScreen.objBrowseForFreeBtn, "Browse for Free");
		}
		
		type(AMDRegistrationScreen.objEmailIDTextField, regEmailID, "Email field");
		click(AMDRegistrationScreen.objProceedBtn, "Proceed button");
		
		verifyElementExist(AMDRegistrationScreen.objScreenTitle, "Register new user");
		type(AMDRegistrationScreen.objFirstNameTxtField, FirstName, "First Name");
		click(AMDRegistrationScreen.objEmailIDHeaderTxt, "HideKeyboard");
		
		click(AMDRegistrationScreen.objLastNameTxtField, "Last Name field");
		type(AMDRegistrationScreen.objLastNameTxtField, LastName, "Last Name");
		click(AMDRegistrationScreen.objEmailIDHeaderTxt, "HideKeyboard");
		
		click(AMDRegistrationScreen.objDOBTxtField, "DOB field");
		type(AMDRegistrationScreen.objDOBTxtField, pDOB, "DOB");
		click(AMDRegistrationScreen.objEmailIDHeaderTxt, "HideKeyboard");
		
		verifyElementPresentAndClick(AMDRegistrationScreen.objGederTxtField, "Gender field");
		verifyElementPresentAndClick(AMDRegistrationScreen.objMale, "Gender male");
		click(AMDRegistrationScreen.objPasswordTxtField, "Passowrd");
		type(AMDRegistrationScreen.objPasswordTxtField, pNewPassword, "Password field");
		click(AMDRegistrationScreen.objEmailIDHeaderTxt, "HideKeyboard");
		
		verifyElementPresentAndClick(AMDRegistrationScreen.objRegisterBtn, "Register button");
		waitTime(4000);
		boolean verifyHomePage = verifyElementExist(AMDHomePage.objHomeTab, "Home Screen");
		if (verifyHomePage) {
			logger.info("New User Registerd to ZEE5 App successfully with following Email Id: "+regEmailID);
			extent.extentLoggerPass("Registration", "New User Registerd to ZEE5 App successfully with following Email Id: "+regEmailID);
			click(AMDHomePage.objDownloadBtn, "Download button");
		} else {
			logger.error("New User failed to Register to ZEE5 App with EmailId: "+regEmailID);
			extent.extentLoggerFail("Registration", "New User failed to Register to ZEE5 App with EmailId: "+regEmailID);
		}
		
		//Assigning email Id to Global variable
		generatedEmailId = regEmailID;
	}
	
	public void event_LoginRegistrationScreenDisplayValiation(String pUsertype) throws Exception {
		System.out.println("\nLogin Registration Screen Display Event Validation");
		
		String pPage = "LoginRegister";
		String pSource = "Intro";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			verifyElementPresentAndClick(AMDHomePage.objDownloadBtn, "Download tab");
			waitTime(30000);
			setFEProperty(pUsertype);
			
			MixpanelAndroid.FEProp.setProperty("Source", pSource);
			MixpanelAndroid.FEProp.setProperty("Page Name", pPage);
			MixpanelAndroid.FEProp.setProperty("Player Name", "Kaltura Android");
			MixpanelAndroid.FEProp.setProperty("manufacturer", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("brand", pManufacturer);
			
			MixpanelAndroid.ValidateParameter("", "Login Registration Screen Display");	
		}else {
			logger.info("Login Registration Screen Display Event is Not applicable for "+pUsertype);
			extentLogger("Login Registration Screen Display", "Login Registration Screen Display Event is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_RegisterScreenDisplayValiation(String pUsertype) throws Exception {
		System.out.println("\nRegister Screen Display Event Validation");
	
		String pPage = "LoginRegister";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			waitTime(5000);
			setFEProperty(pUsertype);
			
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);
			
			mixpanel.ValidateParameter("", "Register Screen Display");
			
		}else {
			logger.info("Register Screen Display Event is Not applicable for "+pUsertype);
			extentLogger("Register Screen Display", "Register Screen Display Event is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_RegistrationInitiatedValiation(String pUsertype) throws Exception {
		System.out.println("\nRegistration Initiated Event Validation");
		
		String pPage = "Registration";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			waitTime(5000);
			setFEProperty(pUsertype);
			
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);
					
			mixpanel.ValidateParameter("", "Registration Initiated");	
		}else {
			logger.info("Registration Initiated Event is Not applicable for "+pUsertype);
			extentLogger("Registration Initiated Event", "Registration Initiated Event is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_RegistrationFirstNameEnteredValiation(String pUsertype) throws Exception {
		System.out.println("\nRegistration First Name Entered Event Validation");
		
		String pPage = "Registration";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			waitTime(5000);
			setFEProperty(pUsertype);
			
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);
					
			mixpanel.ValidateParameter("", "Registration First Name Entered");		
		}else {
			logger.info("Registration First Name Entered is Not applicable for "+pUsertype);
			extentLogger("Registration First Name Entered", "Registration First Name Entered is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_RegistrationLastNameEnteredValiation(String pUsertype) throws Exception {
		System.out.println("\nRegistration Last Name Entered Event Validation");
		
		String pPage = "Registration";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			waitTime(5000);
			setFEProperty(pUsertype);
			
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);
			
			mixpanel.ValidateParameter("", "Registration Last Name Entered");	
		}else {
			logger.info("Registration Last Name Entered is Not applicable for "+pUsertype);
			extentLogger("Registration Last Name Entered", "Registration Last Name Entered is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_RegistrationDoBEnteredValiation(String pUsertype) throws Exception {
		System.out.println("Registration DoB Entered Event Validation");
		
		String pPage = "Registration";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			waitTime(5000);
			setFEProperty(pUsertype);
			
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);
					
			mixpanel.ValidateParameter("", "Registration DoB Entered");
					
		}else {
			logger.info("Registration DoB Entered is Not applicable for "+pUsertype);
			extentLogger("Registration DoB Entered", "Registration DoB Entered is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_RegistrationGenderEnteredValiation(String pUsertype) throws Exception {
		System.out.println("Registration Gender Entered Event Validation");
		
		String pPage = "Registration";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			waitTime(5000);
			setFEProperty(pUsertype);
			
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);
				
			mixpanel.ValidateParameter("", "Registration Gender Entered");		
		}else {
			logger.info("Registration Gender Entered is Not applicable for "+pUsertype);
			extentLogger("Registration Gender Entered", "Registration Gender Entered is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_RegistrationPasswordEnteredValiation(String pUsertype) throws Exception {
		System.out.println("Registration Password Entered Event Validation");
		
		String pPage = "Registration";
		String pSource = "LoginRegister";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			waitTime(5000);
			setFEProperty(pUsertype);
			
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("brand", pManufacturer);
			
			mixpanel.ValidateParameter("", "Registration Password Entered");	
		}else {
			logger.info("Registration Password Entered is Not applicable for "+pUsertype);
			extentLogger("Registration Gender Entered", "Registration Password Entered is Not applicable for "+pUsertype);
		}	
	}
	
	
	public void verifyVideoStreamOverWifiChangedEvent() throws Exception {
		extent.HeaderChildNode("Verify video Stream over wifi changed Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		
		if (!(pUserType.equalsIgnoreCase("Guest"))) {
			if(pUserType.equalsIgnoreCase("SubscribedUser")) {
				Username = getParameterFromXML("SubscribedUserName");
				Password = getParameterFromXML("SubscribedPassword");
			}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				Username = getParameterFromXML("NonSubscribedUserName");
				Password = getParameterFromXML("NonSubscribedUserPassword");
			}
	      	mixpanel.FEProp.setProperty("Old Stream Over Wifi Setting", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("stream_over_wifi"));
		}else {
			mixpanel.FEProp.setProperty("Old Stream Over Wifi Setting", "false");
		}
		
		boolean var = verifyElementPresentAndClick(AMDMoreMenu.objVideo_WifiOnly, "Wifi only Switch");
		waitTime(3000);
		setFEProperty(pUserType);
		setUserType_SubscriptionProperties(pUserType);
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
		mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
	
		if(pUserType.equalsIgnoreCase("Guest")) {
			mixpanel.FEProp.setProperty("User Type", "guest");
			mixpanel.FEProp.setProperty("New Stream Over Wifi Setting", "true");
		}
		
		mixpanel.ValidateParameter("", "Video Stream Over Wifi Changed");
		
	    verifyElementPresentAndClick(AMDMoreMenu.objVideo_WifiOnly, "Wifi only Switch");

	}
	
	public void verifyVideoAutoPlayChangedEvent() throws Exception {
		extent.HeaderChildNode("Verify video AutoPlay change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		
		if (!(pUserType.equalsIgnoreCase("Guest"))) {
			if(pUserType.equalsIgnoreCase("SubscribedUser")) {
				Username = getParameterFromXML("SubscribedUserName");
				Password = getParameterFromXML("SubscribedPassword");
			}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				Username = getParameterFromXML("NonSubscribedUserName");
				Password = getParameterFromXML("NonSubscribedUserPassword");
			}
	      	mixpanel.FEProp.setProperty("Old Autoplay Setting", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("auto_play"));
		}else {
			mixpanel.FEProp.setProperty("Old Autoplay Setting", "true");
		}
		
		boolean var = verifyElementPresentAndClick(AMDMoreMenu.objVideo_Autoply, "Video Autoplay toggle");
		waitTime(3000);
	
		
		setFEProperty(pUserType);
		setUserType_SubscriptionProperties(pUserType);
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
		mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
	
		if(pUserType.equalsIgnoreCase("Guest")) {
			mixpanel.FEProp.setProperty("User Type", "guest");
			mixpanel.FEProp.setProperty("New Autoplay Setting", "false");
		}
		
		mixpanel.ValidateParameter("", "Video Streaming Autoplay Changed");
		
	    verifyElementPresentAndClick(AMDMoreMenu.objVideo_Autoply, "Video Autoplay toggle");

	}
	
	public void verifyDownloadOverWifiChangedEvent() throws Exception {
		extent.HeaderChildNode("Verify Download Over wifi change Event");
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
		
		if (!(pUserType.equalsIgnoreCase("Guest"))) {
			if(pUserType.equalsIgnoreCase("SubscribedUser")) {
				Username = getParameterFromXML("SubscribedUserName");
				Password = getParameterFromXML("SubscribedPassword");
			}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				Username = getParameterFromXML("NonSubscribedUserName");
				Password = getParameterFromXML("NonSubscribedUserPassword");
			}
	      	mixpanel.FEProp.setProperty("Old Download Over Wifi Setting", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("download_over_wifi"));
		}else {
			mixpanel.FEProp.setProperty("Old Download Over Wifi Setting", "false");
		}
		
		boolean var = verifyElementPresentAndClick(AMDMoreMenu.objDownloads_WifiOnly, "Download over wifi only switch");
		waitTime(3000);
		setFEProperty(pUserType);
		setUserType_SubscriptionProperties(pUserType);
		mixpanel.FEProp.setProperty("Source", "More");
		mixpanel.FEProp.setProperty("Page Name", "user_setting");
		mixpanel.FEProp.setProperty("Manufacturer", DeviceDetails.OEM);
		mixpanel.FEProp.setProperty("Brand", DeviceDetails.OEM);
	
		if(pUserType.equalsIgnoreCase("Guest")) {
			mixpanel.FEProp.setProperty("User Type", "guest");
			mixpanel.FEProp.setProperty("New Download Over Wifi Setting", "true");
		}
		mixpanel.ValidateParameter("", "Download Over Wifi Changed");
	    verifyElementPresentAndClick(AMDMoreMenu.objDownloads_WifiOnly, "Download over wifi only switch");
	
	}
	
	public void verifyCarouselBannerClickEvent(String usertype, String tabName) throws Exception {
		extent.HeaderChildNode("Carousel Banner Click");
		waitTime(10000);
		SelectTopNavigationTab(tabName);

		boolean flagBox = verifyIsElementDisplayed(AMDHomePage.objSboxIcon);
		String pSugarBox = String.valueOf(flagBox);

		String contentName = ResponseInstance.getCarouselContentFromAPI2(usertype, tabName);
		System.out.println(contentName);

		boolean flag = waitForElementAndClickIfPresent(AMDHomePage.objContentTitle(contentName), 60, "carousal content");

		if (flag==true) {

			String pPage = "Homepage";
			String pSource = getSource(tabName);
			String pManufacturer = DeviceDetails.OEM;
			// String pAdId = getAdId();

			setFEProperty(usertype);
			setUserType_SubscriptionProperties(usertype);

			// mixpanel.FEProp.setProperty("Ad ID", getParameterFromXML("AdID"));
			// mixpanel.FEProp.setProperty("Advertisement ID", getParameterFromXML("AdID"));
			mixpanel.FEProp.setProperty("Source", pSource);
			mixpanel.FEProp.setProperty("Page Name", pPage);
			mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
			mixpanel.FEProp.setProperty("Manufacturer", pManufacturer);
			mixpanel.FEProp.setProperty("Brand", pManufacturer);
			// mixpanel.FEProp.setProperty("Sugar Box Value",pSugarBox );
			//mixpanel.FEProp.setProperty("Carousal Name", "N/A");

			mixpanel.ValidateParameter("", "Carousal Banner Click");
		} else {
			logger.info("Failed to click on Carousal banner");
			extentLoggerWarning("Event", "Failed to click on Carousal banner");
		}
	}
	
	public void setFEProperty(String pUserId, String pPassword) {

		Properties pro = new Properties();
		pro = ResponseInstance.getUserSettingsDetails(pUserId, pPassword);

		System.out.println(pro);
		String value = pro.getProperty("eduauraaClaimed");
		if (value.equalsIgnoreCase("Empty")) {
			value = "false";
		}
		String value2 = pro.getProperty("parental_control");
		if (value2.equalsIgnoreCase("Empty")) {
			value2 = "N/A";
		}

		MixpanelAndroid.FEProp.setProperty("New Video Streaming Quality Setting", pro.getProperty("streaming_quality"));
		MixpanelAndroid.FEProp.setProperty("New Autoplay Setting", pro.getProperty("auto_play"));
		MixpanelAndroid.FEProp.setProperty("New Stream Over Wifi Setting", pro.getProperty("stream_over_wifi"));
		MixpanelAndroid.FEProp.setProperty("New Download Quality Setting", pro.getProperty("download_quality"));
		MixpanelAndroid.FEProp.setProperty("New Download Over Wifi Setting", pro.getProperty("download_over_wifi"));
		MixpanelAndroid.FEProp.setProperty("New App Language", pro.getProperty("display_language"));
		MixpanelAndroid.FEProp.setProperty("New Content Language", pro.getProperty("content_language"));
		MixpanelAndroid.FEProp.setProperty("hasEduauraa", value);
		MixpanelAndroid.FEProp.setProperty("isEduauraa", value);
		MixpanelAndroid.FEProp.setProperty("Parent Control Setting", value2);
		MixpanelAndroid.FEProp.setProperty("Partner Name", "Zee5");
		MixpanelAndroid.FEProp.setProperty("Gender",ResponseInstance.getUserData_NativeAndroid(pUserId, pPassword).getProperty("gender"));
	}
	
	public void event_HomePageVisitedValiation(String pUsertype) throws Exception {
		System.out.println("\nHomepage Visited Event Validation");
		
		String pPage = "Homepage";
		String pSource = "Intro";
		String pManufacturer = DeviceDetails.OEM;
		String pMethod = "email";
		if(pUsertype.equalsIgnoreCase("Guest")) {
			verifyElementPresentAndClick(AMDHomePage.objDownloadBtn, "Download tab");
			waitTime(30000);
			setFEProperty(pUsertype);
			
//			mixpanel.FEProp.setProperty("Source", pSource);
//			mixpanel.FEProp.setProperty("Page Name", pPage);
			MixpanelAndroid.FEProp.setProperty("Player Name", "Kaltura Android");
			MixpanelAndroid.FEProp.setProperty("manufacturer", pManufacturer);
			MixpanelAndroid.FEProp.setProperty("brand", pManufacturer);
			
			MixpanelAndroid.ValidateParameter("", "Homepage_visited");	
		}else {
			logger.info("Homepage_visited Event is Not applicable for "+pUsertype);
			extentLogger("Homepage_visited", "Homepage_visited Event is Not applicable for "+pUsertype);
		}	
	}
	
	public void event_ChangePasswordResultEvent(String userType) throws Exception {
		extent.HeaderChildNode("Change Password Result event");
		if (!(userType.equalsIgnoreCase("Guest"))) {
			MixpanelAndroid.FEProp.setProperty("Page Name", "ChangePassword");
			MixpanelAndroid.FEProp.setProperty("Source", "MyProfile");
			MixpanelAndroid.FEProp.setProperty("Element", "ChangePassword");
			MixpanelAndroid.FEProp.setProperty("Partner Name", "N/A");

			MixpanelAndroid.ValidateParameter("", "Change Password Result");
		}else {
			logger.info("Change Password Result Event is Not applicable for Guest user");
			extentLogger("Change Password Result", "Change Password Result Event is Not applicable for Guest user");
		}
	}
	
	public void SubscriptionPageEvent(String pUsertype) throws Exception {
		HeaderChildNode("Verify Subscription Page Event");
		
		if (!(pUsertype.equalsIgnoreCase("SubscribedUser"))) {
			verifyElementPresentAndClick(AMDHomePage.objMoreMenu, "More menu");
			verifyElementPresentAndClick(AMDMoreMenu.objBuySubscription, "Buy Subscription");
			waitTime(2000);
			verifyElementPresent(AMDSubscibeScreen.objPremiumBadge, "Subscription screen");

			setFEProperty(pUsertype);
			setUserType_SubscriptionProperties(pUsertype);

			MixpanelAndroid.FEProp.setProperty("Player Name", "Kaltura Android");
			MixpanelAndroid.FEProp.setProperty("Page Name", "Subscription");
			MixpanelAndroid.FEProp.setProperty("Source", "N/A");
			MixpanelAndroid.FEProp.setProperty("Partner Name", "N/A");
			MixpanelAndroid.FEProp.setProperty("Transaction Currency", "N/A");
			MixpanelAndroid.FEProp.setProperty("Current Subscription", "N/A");
			
			MixpanelAndroid.ValidateParameter("", "Subscription Page Viewed");
		}else {
			logger.info("Subscription Page Viewed Event is Not applicable for "+pUsertype);
			extentLogger("Subscription Page Viewed", "Subscription Page Viewed Event is Not applicable for "+pUsertype);
		}
	}
	
	public void navigateToHomeScreen() throws Exception {
		extent.HeaderChildNode("Navigation to Home Screen");
		if (verifyIsElementDisplayed(AMDOnboardingScreen.objContinueBtnInCountryPopUp)) {
			click(AMDOnboardingScreen.objContinueBtnInCountryPopUp, "Continuebutton(Country_Screen)");
		}
	}
	
	public void SearchEventFunctinality(String userType,String pContentName)
			throws Exception {
		HeaderChildNode("Search event verification");
		System.out.println("\nSearch event verification");

		String pSource = "More";
		String pPage = "Homepage";
		String pManufacturer = DeviceDetails.OEM;
		
		verifyElementPresentAndClick(AMDHomePage.objSearchBtn, "Search icon");
		click(AMDSearchScreen.objSearchEditBox, "Search edit box");
		type(AMDSearchScreen.objSearchEditBox, pContentName, "Search edit box");
		click(AMDSearchScreen.objFirstSearchResult(pContentName), "Searched content");
		
		
		if (!userType.equalsIgnoreCase("SubscribedUser")) {
			waitForAdToFinishInAmd();
		}

		waitTime(3000);
		verifyElementPresent(AMDPlayerScreen.objPlayerScreen, "Player screen");
		waitTime(2000);
		Back(1);
		setFEProperty(userType);
		setUserType_SubscriptionProperties(userType);

		mixpanel.FEProp.setProperty("Source", pSource);
		mixpanel.FEProp.setProperty("Page Name", pPage);
		mixpanel.FEProp.setProperty("Player Name", "Kaltura Android");
		mixpanel.FEProp.setProperty("manufacturer", pManufacturer);
		mixpanel.FEProp.setProperty("brand", pManufacturer);
		
		mixpanel.ValidateParameter("", "Search Button Click");
	}
	
	public void verifyDisplayLanguageChangeEvent(String usertype, String displayLanguage) throws Exception {
		extent.HeaderChildNode("Verify display language change Event");
		
		if (!(pUserType.equalsIgnoreCase("Guest"))) {
			if(pUserType.equalsIgnoreCase("SubscribedUser")) {
				Username = getParameterFromXML("SubscribedUserName");
				Password = getParameterFromXML("SubscribedPassword");
			}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				Username = getParameterFromXML("NonSubscribedUserName");
				Password = getParameterFromXML("NonSubscribedUserPassword");
			}
	      	mixpanel.FEProp.setProperty("Old App Language", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("display_language"));
		}else {
			mixpanel.FEProp.setProperty("Old App Language", "en");
		}
		
		click(AMDHomePage.MoreMenuIcon, "More menu icon");
		Swipe("UP", 1);
		click(AMDMoreMenu.objSettings, "Settings option");
		waitTime(1000);
		SwipeUntilFindElement(AMDMoreMenu.objDisplayLang, "Up");
		click(AMDMoreMenu.objDisplayLang, "Display language");
		
		boolean flag = false;
		flag = verifyElementPresentAndClick(AMDOnboardingScreen.objSelectDisplayLang(displayLanguage), "language");
		click(AMDOnboardingScreen.objDiplay_ContinueBtn, "Continue button");
		
		if(flag){
			
			setFEProperty(usertype);
			setUserType_SubscriptionProperties(usertype);
			
			mixpanel.FEProp.setProperty("Source", "user_setting");
			mixpanel.FEProp.setProperty("Page Name", "DisplayLanguage");
			mixpanel.FEProp.setProperty("manufacturer", DeviceDetails.OEM);
			mixpanel.FEProp.setProperty("brand", DeviceDetails.OEM);
			if (pUserType.equalsIgnoreCase("Guest")) {
				mixpanel.FEProp.setProperty("User Type", "Free");
			}
			
			if (!(pUserType.equalsIgnoreCase("Guest"))) {
				if(pUserType.equalsIgnoreCase("SubscribedUser")) {
					Username = getParameterFromXML("SubscribedUserName");
					Password = getParameterFromXML("SubscribedPassword");
				}else if(pUserType.equalsIgnoreCase("NonSubscribedUser")) {
					Username = getParameterFromXML("NonSubscribedUserName");
					Password = getParameterFromXML("NonSubscribedUserPassword");
				}
		      	mixpanel.FEProp.setProperty("New App Language", ResponseInstance.getUserSettingsDetails(Username,Password).getProperty("display_language"));
			}else {
				mixpanel.FEProp.setProperty("New App Language", "mr");
			}
			
			mixpanel.ValidateParameter("", "Display Language Changed");
			
			SwipeUntilFindElement(AMDMoreMenu.objDisplayLang, "Up");
			click(AMDMoreMenu.objDisplayLang, "Display language");
			verifyElementPresentAndClick(AMDOnboardingScreen.objSelectDisplayLang("English"), "language");
			click(AMDOnboardingScreen.objDiplay_ContinueBtn, "Continue button");
			
		} else {
			logger.info("Failed to change display language");
			extentLoggerWarning("Event", "Failed to change display language");
		}

	}

public void verifyContentLanguageChangeEvent(String userType) throws Exception {
		if(userType.equalsIgnoreCase("Guest")) {
			extent.HeaderChildNode("Verify Content language change Event");
			
			click(AMDHomePage.MoreMenuIcon, "More menu icon");
			verifyElementPresentAndClick(AMDMoreMenu.objSettings, "Settings option");
			waitTime(1000);
			SwipeUntilFindElement(AMDMoreMenu.objContentLang, "Up");
			click(AMDMoreMenu.objContentLang, "Content language");
			SwipeUntilFindElement(AMDOnboardingScreen.objSelectContentLang("Hindi"), "Up");
			boolean flag = verifyElementPresentAndClick(AMDOnboardingScreen.objSelectContentLang("Hindi"), "Hindi Content language");
			click(AMDOnboardingScreen.objContent_ContinueBtn, "Continue button in Content language screen");

			if(flag){
				
				setFEProperty(userType);
				setUserType_SubscriptionProperties(userType);
				
				mixpanel.FEProp.setProperty("Source", "user_setting");
				mixpanel.FEProp.setProperty("Page Name", "ContentLanguage");
				mixpanel.FEProp.setProperty("manufacturer", DeviceDetails.OEM);
				mixpanel.FEProp.setProperty("brand", DeviceDetails.OEM);
				mixpanel.FEProp.setProperty("User Type", "Free");
				mixpanel.FEProp.setProperty("Old Content Language", "en,kn");
				mixpanel.FEProp.setProperty("New Content Language", "en,kn,hi");	
				
				mixpanel.ValidateParameter("", "Content Language Changed");
				
				SwipeUntilFindElement(AMDMoreMenu.objContentLang, "Up");
				click(AMDMoreMenu.objContentLang, "Content language");
				SwipeUntilFindElement(AMDOnboardingScreen.objSelectContentLang("Hindi"), "Up");
				click(AMDOnboardingScreen.objSelectContentLang("Hindi"), "Hindi Content language");
				click(AMDOnboardingScreen.objContent_ContinueBtn, "Continue button in Content language screen");
				
			} else {
				logger.info("Failed to change content language");
				extentLoggerWarning("Event", "Failed to change content language");
			}
		}
	}

}
