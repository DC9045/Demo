package com.zee5.MixpanelScripts;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.business.zee.Zee5ApplicasterMixPanelBusinessLogic;
import com.utility.Utilities;

public class ApplicasterMixpanel_Player {

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
	public void VideoViewEventofPremiumContent(String userType) throws Exception {
		System.out.println("Video view event of Premium content");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.videoViewEventForPremiumContent(userType,"Home");
		
	}
	
	@Test(priority = 3)
	@Parameters({ "userType", "keyword3"})
	public void VideoViewEventofTrailerContent(String userType, String keyword3) throws Exception {
		System.out.println("Video view event of Trailer content");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.videoViewEventForTrailerContent(userType,keyword3);
		
	}

	@Test(priority = 4)
	public void VideoViewEventofCarouselContent() throws Exception {
		System.out.println("Video view event of Carousel content");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.videoViewEventForCarouselContent("Home");
	}

	@Test(priority = 5)
	@Parameters({ "userType","keyword4"})
	public void VideoViewEventFromsearchpage(String userType, String keyword4) throws Exception {
		System.out.println("Video view event of Content from Search page");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.videoViewEventOfcontentFromSearchPage(userType, keyword4);
	}
	
	@Test(priority = 6)
	@Parameters({ "userType"})
	public void VideoViewEventFromMyWatchList(String userType) throws Exception {
		System.out.println("Video view event of Content from My WatchList page");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.videoViewEventOfContentFromMyWatchListPage(userType);
		
	}
	
	@Test(priority = 7)
	@Parameters({ "userType", "keyword4"})
	public void VideoViewEventFromUpNextRail(String userType, String keyword4) throws Exception {
		System.out.println("Video view event of Content from Upnext Rail");
		Zee5ApplicasterMixPanelBusinessLogic.relaunch(true);
		Zee5ApplicasterMixPanelBusinessLogic.videoViewEventOfContentFromUpNextRail(userType, keyword4);
		
	}
}