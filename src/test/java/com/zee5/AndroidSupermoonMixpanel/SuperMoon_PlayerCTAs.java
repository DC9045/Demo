package com.zee5.AndroidSupermoonMixpanel;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.business.zee.Zee5ApplicasterMixPanelBusinessLogic;
import com.utility.Utilities;

public class SuperMoon_PlayerCTAs {

private Zee5ApplicasterMixPanelBusinessLogic Zee5ApplicasterMixPanelBusinessLogic;
	
	@BeforeTest
	public void init() throws Exception {
		Utilities.relaunch = true;
		Zee5ApplicasterMixPanelBusinessLogic = new Zee5ApplicasterMixPanelBusinessLogic("zee");
	}

	@Test(priority = 1)
	@Parameters({ "userType" })
	public void AndroidAppMixPanelLogin(String userType) throws Exception {
		System.out.println("\nLogin");
		Zee5ApplicasterMixPanelBusinessLogic.navigateToIntroScreen_DisplaylangScreen();
		Zee5ApplicasterMixPanelBusinessLogic.ZeeApplicasterLogin(userType);
	}
	
	@Test(priority = 2)
	@Parameters({ "userType", "supermoonContentName" })
	public void PlayerCTAsEvent(String usertype, String supermoonContentName) throws Exception {
		System.out.println("\n Player CTAs event");
		Zee5ApplicasterMixPanelBusinessLogic.playerCTAsEvent(usertype, supermoonContentName);
	}

	//###############-------END OF TEST-------###############
	
	@AfterTest
	public void tearDownApp() {
		System.out.println("\nQuit the App");
		Zee5ApplicasterMixPanelBusinessLogic.tearDown();
	}

}
