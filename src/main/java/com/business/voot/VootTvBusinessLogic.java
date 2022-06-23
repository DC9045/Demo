package com.business.voot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.driverInstance.CommandBase;
import com.extent.ExtentReporter;
import com.mixpanelValidation.Mixpanel;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;
import com.voot.TvPages.VootTVHomePage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.response.Response;

public class VootTvBusinessLogic extends Utilities {

		public VootTvBusinessLogic(String Application) throws InterruptedException {
			new CommandBase(Application);
			init();
		}

		private int timeout;
		SoftAssert softAssertion = new SoftAssert();
		boolean launch = "" != null;
		/** Retry Count */
		private int retryCount;
		Mixpanel mixpanel = new Mixpanel();
		ExtentReporter extent = new ExtentReporter();

		/** The Constant logger. */
//		final static Logger logger = Logger.getLogger("rootLogger");
		static LoggingUtils logger = new LoggingUtils();

		String language = "abcdefghijklmnopqrstuvwxyz";

		/** The Android driver. */
		public AndroidDriver<AndroidElement> androidDriver;

		/** The Android driver. */
		public IOSDriver<WebElement> iOSDriver;

		static String code = "";
		String asset_SubType = "";
		String assetType = "";
		Set<String> hash_Set = new HashSet<String>();
		String viewAllTrayname = "";
		@SuppressWarnings("unused")
		private String LacationBasedLanguge;

		
		
		public String titleLanguage = "";

		List<String> LocationLanguage = new ArrayList<String>();

		List<String> DefaultLanguage = new ArrayList<String>();

		List<String> SelectedCONTENTLanguageInWelcomscreen = new ArrayList<String>();

		List<String> SelectedCONTENTLanguageInHamburgerMenu = new ArrayList<String>();

		Response resp;

		ArrayList<String> MastheadTitleApi = new ArrayList<String>();
		public String onboardingTraytitle = "";
		public String onboardingPremiumContenttitle = "";

		public static boolean relaunchFlag = false;
		public static boolean appliTools = false;

		public static boolean PopUp = false;

		private static String IP = "-s 192.168.0.89:5555";

		public String title = "";

		public int getTimeout() {
			return timeout;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

		public int getRetryCount() {
			return retryCount;
		}

		public void setRetryCount(int retryCount) {
			this.retryCount = retryCount;
		}

		public String carouselTitle = "";
		public String carouselDescription = "";
		public String trayTitle = "";

		public String contentName = "";

		public String titleDescription = "";

		String userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		String parentpasswordNonSub = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
				.getParameter("NonsubscribedPassword");

		String parentpasswordSub = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
				.getParameter("SubscribedPassword");

		/**
		 * Initiate Property File.
		 *
		 * @param byLocator the by locator
		 */

		public void init() {

			PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
			setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
			setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
			logger.info(
					"Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
		}

		public void TvtearDown() {
			getDriver().quit();
		}		

//==================================================================================================		
		// gen
/**
 * Method to click on skipLink and Navigate to MyVoot Page as Guest User
 * @throws Exception
 */
		public void skipLink() throws Exception {
			getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			if (verifyIsElementDisplayed(VootTVHomePage.objSkip, "Skip Link")) {
				TVclick(VootTVHomePage.objSkip, "Skip Link");
				tvClickLeftButton(1);
				if (!verifyIsElementDisplayed(VootTVHomePage.objSkip, "Skip Link")
						&& verifyIsElementDisplayed(VootTVHomePage.objSelectTab("My Voot"), "My Voot Tab Focused")) {
					logger.info("Clicked on Skip Link user is navigate to My Voot page");
					extent.extentLoggerPass("Clicked on Skip Link user is navigate to My Voot page",
							"Clicked on Skip Link user is navigate to My Voot page");
				} else {
					logger.error("Clicked on Skip Link user is not navigated to My Voot page");
					extent.extentLoggerFail("Clicked on Skip Link user is not navigated to My Voot page",
							"Clicked on Skip Link user is not navigated to My Voot page");
				}
				tvClickRightButton(1);
			} 
		}
/**
 * Method to click the specified Remote button to specified number of time
 * @param direction
 * @param count
 */
		public void tvRemoteButtonClick(String direction, int count) {
			String dire = direction;
			try {
				if (dire.equalsIgnoreCase("LEFT")) {
					for (int i = 1; i <= count; i++) {
						TVRemoteEvent(21);
						logger.info("Moving screen in " + " " + dire + " direction" + " " + i + " times");
						extent.extentLogger("MoveLeft",
								"Moving screen in " + " " + dire + " direction" + " " + i + " times");
					}
				} else if (dire.equalsIgnoreCase("UP")) {

					for (int j = 1; j <= count; j++) {
						TVRemoteEvent(19);
						logger.info("Moving screen in " + dire + " direction" + " " + j + " times");
						extent.extentLogger("MoveUp",
								"Moving screen in " + " " + dire + " direction" + " " + j + " times");
					}
				} else if (dire.equalsIgnoreCase("DOWN")) {
					for (int j = 1; j <= count; j++) {
						TVRemoteEvent(20);
						logger.info("Moving screen in " + " " + dire + " direction" + " " + j + " times");
						extent.extentLogger("MoveDown",
								"Moving screen in " + " " + dire + " direction" + " " + j + " times");
					}
				} else if (dire.equalsIgnoreCase("Right")) {
					for (int j = 1; j <= count; j++) {
						TVRemoteEvent(22);
						logger.info("Moving screen in " + " " + dire + " direction" + " " + j + " times");
						extent.extentLogger("MoveRight",
								"Moving screen in " + " " + dire + " direction" + " " + j + " times");
					}
				} else if (dire.equalsIgnoreCase("BACK")) {
					for (int j = 1; j <= count; j++) {
						TVRemoteEvent(4);
						logger.info("Moving screen in " + " " + dire + " direction" + " " + j + " times");
						extent.extentLogger("MoveBack",
								"Moving screen in " + " " + dire + " direction" + " " + j + " times");
					}
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
/**
 * Method to move to the given direction until the given element is displayed
 * @param locator
 * @param direction
 * @return
 * @throws Exception
 */
		public boolean moveUntilFindElementTv(By locator, String direction) throws Exception {
			boolean checkLocator, eletFound = false;
			if (direction.equalsIgnoreCase("UP")) {
				for (int i = 1; i < 25; i++) {
					TVRemoteEvent(19);
					checkLocator = verifyIsElementDisplayed(locator);
					if (checkLocator) {
						eletFound = true;
						break;
					}
				}
			}
			if (direction.equalsIgnoreCase("DOWN")) {
				for (int i = 1; i < 25; i++) {
					TVRemoteEvent(20);
					checkLocator = verifyIsElementDisplayed(locator);
					if (checkLocator) {
						eletFound = true;
						break;
					}
				}
			}
			if (direction.equalsIgnoreCase("RIGHT")) {
				for (int i = 1; i < 25; i++) {
					TVRemoteEvent(22);
					checkLocator = verifyIsElementDisplayed(locator);
					if (checkLocator) {
						eletFound = true;
						break;
					}
				}
			}
			if (direction.equalsIgnoreCase("LEFT")) {
				for (int i = 1; i < 25; i++) {
					TVRemoteEvent(21);
					checkLocator = verifyIsElementDisplayed(locator);
					if (checkLocator) {
						eletFound = true;
						break;
					}
				}
			}
			if (direction.equalsIgnoreCase("BACK")) {
				for (int i = 1; i < 25; i++) {
					TVRemoteEvent(4);
					waitTime(2000);
					checkLocator = verifyIsElementDisplayed(locator);
					if (checkLocator) {
						eletFound = true;
						break;
					}
				}
			}
			return eletFound;
		}
/**
 * Method to wait until the element is displayed
 * @param locator
 * @param countWait
 * @return
 * @throws Exception
 */
		public boolean waitUntilElementDisplayed(By locator, int countWait) throws Exception {
			int iterator = 0;
			while (verifyElementDisplayed(locator) == false) {
				if (iterator <= countWait) {
					waitTime(1000);
					iterator++;
				} else {
					return false;
				}
			}
			return true;
		}
/**
 * Method to navigate to the given tab
 * @param str
 * @throws Exception
 */
		public void TVTabSelect(String str) throws Exception {
			TVclick(VootTVHomePage.objSelectTab(str), str);
			Thread.sleep(2000);
			try {
				if (TVgetAttributValue("focused", VootTVHomePage.objSelectTab(str)).equals("false")) {
					TVclick(VootTVHomePage.objSelectTab(str), str);
				} else {
					logger.info("Highlighted Tab:" + TVgetText(VootTVHomePage.objHighlightedTab));
					extent.extentLoggerPass("Tab", "Highlighted Tab:" + TVgetText(VootTVHomePage.objHighlightedTab));
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
/**
 * Method to Click Up button in remote
 * @param nNumber
 * @throws Exception
 */
		public void tvClickUpButton(int nNumber) throws Exception {
			for (int i = 1; i <= nNumber; i++) {
				TVRemoteEvent(19);
				logger.info("Clicked on Up Button "+ nNumber +"  time");
				waitTime(2000);
			}
		}
/**
 * Method to Click Down button in remote
 * @param nNumber
 * @throws Exception
 */
		public void tvClickDownButton(int nNumber) throws Exception {
			for (int i = 1; i <= nNumber; i++) {
				TVRemoteEvent(20);
				logger.info("Clicked on Down Button "+ nNumber +"  time");
			
			}
		}

/**
 * Method to Click Left button in remote 
 * @param nNumber
 * @throws Exception
 */
		public void tvClickLeftButton(int nNumber) throws Exception {
			for (int i = 1; i <= nNumber; i++) {
				TVRemoteEvent(21);
				logger.info("Clicked on Left Button "+ nNumber +"  time");
				
			}
		}

/**
 * Method to Click Right button in remote
 * @param nNumber
 * @throws Exception
 */
		public void tvClickRightButton(int nNumber) throws Exception {
			for (int i = 1; i <= nNumber; i++) {
				TVRemoteEvent(22);
				logger.info("Clicked on Right Button "+ nNumber +"  time");
			
			}
		}

/**
 * Method to Click Back button in remote 
 * @param nNumber
 * @throws Exception
 */
		public void tvClickBackButton(int nNumber) throws Exception {
			for (int i = 1; i <= nNumber; i++) {
				TVRemoteEvent(4);
				logger.info("Clicked on Back Button "+ nNumber +"  time");
				
			}
		}
/**
 * Method to Click OK button in remote
 * @param nNumber
 * @throws Exception
 */
		public void tvClickOkButton(int nNumber) throws Exception {
			for (int i = 1; i <= nNumber; i++) {
				TVRemoteEvent(23);
				logger.info("Clicked on Ok Button "+ nNumber +"  time");
			
			}
		}

// Sidhu..
/**
 * Navigate To Any Tab[Page] From Any Page
 * @param tabName
 * @throws Exception
 */
public void navigateToAnyTABFromAnyPage(String tabName) throws Exception {
	extent.HeaderChildNode("Navigation To " + tabName + " Page");
	try {
		int nNumber = 1;
		while (!verifyElementDisplayed(VootTVHomePage.objSelectTab(tabName))) {
			TVRemoteEvent(4);
			logger.info("Clicked on Back Button " + nNumber + "  time");
			nNumber++;
		}

		if (!(findElement(VootTVHomePage.objToFindTabSelected).getText()).equals(tabName)) {
			waitTime(1000);
			TVclick(VootTVHomePage.objSelectTab(tabName), tabName);
		}else {
		logger.info("User is already in "+tabName+" Page");
	}
	} catch (Exception e) {
		System.out.println(e);
	}
}
/**
 * Method To Navigate Back To My VootPage 
 * @throws Exception
 */
public void navigateToMyVootPage() throws Exception {
	extent.HeaderChildNode("Navigation To My Voot Page");
	try {
		int nNumber = 1;
		while (!verifyElementDisplayed(VootTVHomePage.objMyVootTABText)) {
			TVRemoteEvent(4);
			logger.info("Clicked on Back Button " + nNumber + "  time");
			nNumber++;
		}
		String sSelectedTABText = findElement(VootTVHomePage.objToFindTabSelected).getText();
		if (!(sSelectedTABText.equals("My Voot"))) {
			waitTime(1000);
			TVclick(VootTVHomePage.objMyVootTABText, "My Voot");
			waitTime(3000);
			if (verifyIsElementDisplayed(VootTVHomePage.objWatchLatest)
					|| verifyIsElementDisplayed(VootTVHomePage.objContinueWatching)) {
				logger.info("Naviagted To My Voot Page from " + sSelectedTABText + " Page");
				extent.extentLoggerPass("Naviagted To My Voot Page", "Naviagted To My Voot Page");
			} else {
				logger.error("Failed To Naviagte To My Voot Page");
				extent.extentLoggerFail("Failed To Naviagte To My Voot Page", "Failed To Naviagte To My Voot Page");
			}
		} else {
			logger.info("User is already in My Voot Page");
		}
	} catch (Exception e) {
		System.out.println(e);
	}
}
			
//=============================================================================================		
/**
 * Exit PopUp UI Verification and Yes and No CTA Functionality of exit PopUp
 * @param userType
 * @throws Exception
 */
		public void demo(String userType) throws Exception {
			getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			if (userType.equals("Guest")) {
				extent.HeaderChildNode("Verification Of Exit PopUP Functionality");
				skipLink();
				tvClickBackButton(2);
				if (verifyIsElementDisplayed(VootTVHomePage.objLeavingSoSoonTextInExitScreen, "Leaving So Soon Text")) {
					logger.info("Exit Screen is displayed ");
					extent.extentLoggerPass("Exit Screen is displayed ", "Exit Screen is displayed ");
				} else {
					logger.error("Exit Screen is not displayed ");
					extent.extentLoggerFail("Exit Screen is not displayed ", "Exit Screen is not displayed ");
				}
				verifyElementPresent(VootTVHomePage.objYesCTAFocusedInExitScreen, "Focused Yes CTA");
				verifyElementPresent(VootTVHomePage.objNoCTAInExitScreen, "No CTA");
				tvClickDownButton(1);
				verifyElementPresentAndClick(VootTVHomePage.objNoCTAFocusedInExitScreen, "Focused No CTA ");
				if (verifyIsElementDisplayed(VootTVHomePage.objVootLogo, "Voot Logo")) {
					logger.info("No CTA Functionality Pass");
					extent.extentLoggerPass("No CTA Functionality Pass", "No CTA Functionality Pass");
				} else {
					logger.error("No CTA Functionality Fail");
					extent.extentLoggerFail("No CTA Functionality Fail", "No CTA Functionality Fail");
				}
				moveUntilFindElementTv(VootTVHomePage.objYesCTAFocusedInExitScreen, "Back");
				verifyElementPresentAndClick(VootTVHomePage.objYesCTAFocusedInExitScreen, "Focused Yes CTA");
				if (!verifyIsElementDisplayed(VootTVHomePage.objVootLogo, "Voot Logo")) {
					logger.info("Yes CTA Functionality Pass");
					extent.extentLoggerPass("Yes CTA Functionality Pass", "Yes CTA Functionality Pass");
				} else {
					logger.error("Yes CTA Functionality Fail");
					extent.extentLoggerFail("Yes CTA Functionality Fail", "Yes CTA Functionality Fail");
				}
			}
		}
	}
	
		
		
		
		
	

