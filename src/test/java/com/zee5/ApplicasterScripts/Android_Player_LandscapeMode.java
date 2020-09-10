package com.zee5.ApplicasterScripts;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.business.zee.Zee5ApplicasterBusinessLogic;
import com.utility.Utilities;

public class Android_Player_LandscapeMode {

	private Zee5ApplicasterBusinessLogic ZEE5ApplicasterBusinessLogic;

	@BeforeTest
	public void AppLaunch() throws InterruptedException {
		System.out.println("Launching Android App");
		Utilities.relaunch = true; // Clear App Data on First Launch
		ZEE5ApplicasterBusinessLogic = new Zee5ApplicasterBusinessLogic("zee");
	}

	@Test(priority = 0)
	@Parameters({ "userType" })
	public void accessDeviceLocation(String userType) throws Exception {
		ZEE5ApplicasterBusinessLogic.accessDeviceLocationPopUp("Allow", userType);
	}
 
	@Test(priority = 1)
	@Parameters({ "userType" })
	public void Login(String userType) throws Exception {
		System.out.println("\nVerify Display Language Screen and login flow for various usertypes");
		ZEE5ApplicasterBusinessLogic.navigateToIntroScreen_DisplaylangScreen();
		ZEE5ApplicasterBusinessLogic.ZeeApplicasterLogin(userType);
	}

	@Test(priority = 2)
	@Parameters({ "userType", "searchKeyword1", "searchKeyword3", "searchKeyword4", "searchKeyword5" }) // Manasa
	public void verifyPlayerScreenInLandscapeMode(String userType, String searchKeyword1,String searchKeyword3,String searchKeyword4,String searchKeyword5) throws Exception {
		System.out.println("\nVerify Player Functionality in Landscape Mode");
		ZEE5ApplicasterBusinessLogic.playerValidationInFullScreenMode(userType, searchKeyword1);
		ZEE5ApplicasterBusinessLogic.skipIntroValidation(searchKeyword3);
		ZEE5ApplicasterBusinessLogic.subtitleAndPlaybackRateValidation(searchKeyword4,userType);
		ZEE5ApplicasterBusinessLogic.premiumContentWithoutTrailerInLandscapeMode(userType,searchKeyword5);
	}

	@AfterTest
	public void tearDownApp() {
		System.out.println("Quit the App");
		ZEE5ApplicasterBusinessLogic.tearDown();
	}
}
