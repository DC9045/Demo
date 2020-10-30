package com.zee5.MixpanelScripts;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.business.zee.Zee5ApplicasterMixPanelBusinessLogic;
import com.utility.Utilities;

public class ApplicasterMixpanel_PlayerViewChanged {
	
	private Zee5ApplicasterMixPanelBusinessLogic Zee5ApplicasterMixPanelBusinessLogic;

	@BeforeTest
	public void init() throws Exception {
		Utilities.relaunch = true;
		Zee5ApplicasterMixPanelBusinessLogic = new Zee5ApplicasterMixPanelBusinessLogic("zee");
	}

	@Test(priority = 1)
	@Parameters({ "userType" })
	public void PWAWEBMixPanelLogin(String userType) throws Exception {
		System.out.println("Login");
		Zee5ApplicasterMixPanelBusinessLogic.accessDeviceLocationPopUp("Allow", userType);
		Zee5ApplicasterMixPanelBusinessLogic.navigateToIntroScreen_DisplaylangScreen();
		Zee5ApplicasterMixPanelBusinessLogic.ZeeApplicasterLogin(userType);
		
	}
	
	@Test(priority = 2)
	@Parameters({ "userType"})
	public void PlayerViewChangedEventofPremiumContent(String userType) throws Exception {
		System.out.println("Player View Changed event of Premium content");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.playerViewChangedEventForPremiumContent(userType,"Home");
		
	}
	
	@Test(priority = 3)
	@Parameters({ "userType", "keyword3"})
	public void PlayerViewChangedEventofTrailerContent(String userType, String keyword3) throws Exception {
		System.out.println("Player View Changed event of Trailer content");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.PlayerViewChangedEventForTrailerContent(userType,keyword3);
		
	}

	@Test(priority = 4)
	public void  PlayerViewChangedEventofCarouselContent() throws Exception {
		System.out.println("Player View Changed event of Carousel content");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.PlayerViewChangedEventForCarouselContent("Home");
	}

	@Test(priority = 5)
	@Parameters({ "userType","keyword4"})
	public void PlayerViewChangedEventFromsearchpage(String userType, String keyword4) throws Exception {
		System.out.println("Player View Changed event of Content from Search page");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.PlayerViewChangedEventOfcontentFromSearchPage(userType, keyword4);
	}
	
	@Test(priority = 6)
	@Parameters({ "userType"})
	public void PlayerViewChangedEventFromMyWatchList(String userType) throws Exception {
		System.out.println("Player View Changed event of Content from My WatchList page");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.PlayerViewChangedEventOfContentFromMyWatchListPage(userType);
		
	}
	
	@Test(priority = 7)
	@Parameters({ "userType", "keyword4"})
	public void PlayerViewChangedEventFromUpNextRail(String userType, String keyword4) throws Exception {
		System.out.println("Player View Changed event of Content from Upnext Rail");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.PlayerViewChangedEventOfContentFromUpNextRail(userType, keyword4);
		
	}

}
