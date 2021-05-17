package com.zee5.Zee5TvScripts;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TvZee5ATVScripts {

	private com.business.zee.Zee5TvBusinessLogic Zee5TvBusiness;

	@BeforeTest
	public void Before() throws InterruptedException {
		// Utilities.relaunch = true;
		Zee5TvBusiness = new com.business.zee.Zee5TvBusinessLogic("zeeTV");
	}

//	@Test(priority = 1)
	@Parameters({ "userType" })
	public void chooseLanguage(String userType) throws Exception {
		Zee5TvBusiness.chooseLanguagePopup(userType);
	}

//	@Test(priority = 2)
	@Parameters({ "userType" })
	public void club(String userType) throws Exception {
		Zee5TvBusiness.clubScenarios();
	}

//	@Test(priority = 3)
	public void deviceAuthentication() throws Exception {
		Zee5TvBusiness.device();
	}

//	@Test(priority = 4)
	@Parameters({ "userType" })
	public void welcomescreen(String userType) throws Exception {
		Zee5TvBusiness.welcomescreen();
	}

//	@Test(priority = 5)
	@Parameters({ "userType" })
	public void TvLogin(String userType) throws Exception {
		Zee5TvBusiness.login(userType);
	}

//	@Test(priority = 6)
	@Parameters({ "userType" })
	public void searchScenarios(String userType) throws Exception {
		Zee5TvBusiness.searchScenarios(userType);
	}

//	@Test(priority = 7)
	@Parameters({ "userType" })
	public void playback(String userType) throws Exception {
		Zee5TvBusiness.playbackHomepage();
	}

//	@Test(priority = 8)
	@Parameters({ "userType" })
	public void carousel(String tab) throws Exception {
		Zee5TvBusiness.carouselValidation("Home");
	}

//	@Test(priority = 9)
	@Parameters({ "userType" })
	public void landingScenarios(String userType) throws Exception {

		Zee5TvBusiness.landingPageHome(userType);
	}

//	@Test(priority = 10)
	@Parameters({ "userType" })
	public void playerScenarios(String userType) throws Exception {
		Zee5TvBusiness.playerScenarios();
	}

//	@Test(priority = 11)
	@Parameters({ "userType" })
	public void setting(String userType) throws Exception {
		Zee5TvBusiness.setting(userType);
	}

//	@Test(priority = 12)
	public void collectingPage() throws Exception {
		Zee5TvBusiness.collectionpage();
	}

//	@Test(priority = 13)
	public void subscription() throws Exception {
		Zee5TvBusiness.subscription();
	}

//	@Test(priority = 14)
	public void continueWatching() throws Exception {
		Zee5TvBusiness.continueWatching();
	}

//	@Test(priority = 15)
	public void liveTV() throws Exception {
		Zee5TvBusiness.liveTv();
	}

//	@Test(priority = 16)
	public void beforeTV() throws Exception {
		Zee5TvBusiness.beforeTV();
	}

//	@Test(priority = 17)
	public void upNext() throws Exception {
		Zee5TvBusiness.upnext();
	}

//	@Test(priority = 18)
	public void language() throws Exception {
		Zee5TvBusiness.languagePage();
	}

//	@Test(priority = 19)
	public void ads() throws Exception {
		Zee5TvBusiness.ads();
	}

//   @Test(priority = 20)
	public void profile() throws Exception {
		Zee5TvBusiness.profile();
	}

//	@Test(priority = 21)
	public void deeplinking() throws Exception {
		Zee5TvBusiness.deeplinking();
	}

//	@Test(priority = 22)
	public void headerSection() throws Exception {
		Zee5TvBusiness.headerSection();
	}

//	@Test(priority = 23)
	public void talamoos() throws Exception {
		Zee5TvBusiness.talamoos();
	}

//	@Test(priority = 24)
	public void staticPage() throws Exception {
		Zee5TvBusiness.staticPages();
	}

//	@Test(priority = 25)
	public void contactUS() throws Exception {
		Zee5TvBusiness.contactUs();
	}

//	@Test(priority = 26) //Insprint
	@Parameters({ "userType" })
	public void authenticateInprint(String userType) throws Exception {
		Zee5TvBusiness.autheticateInsprint(userType);
	}

//	@Test(priority = 27) //Insprint
	public void zeelogoVerificationInPlayer() throws Exception {
		Zee5TvBusiness.zeelogoVerificationInPlayer();
	}
	
//	@Test(priority = 28) //Insprint
	public void zeeplexTVOD() throws Exception {
		Zee5TvBusiness.zeeplex();
	}
	
//	@Test(priority = 29) //Insprint
	public void nowplaying() throws Exception {
		Zee5TvBusiness.nowplayingButton();
	}
	
//	@Test(priority = 30) //Insprint
	public void premiumLiveChannel() throws Exception {
		Zee5TvBusiness.premiumLiveChannel();
	}
	
//	@Test(priority = 31) //Insprint
	public void premiumcontentFromInfo() throws Exception {
		Zee5TvBusiness.premiumInUpnext();
	}
	
//	@Test(priority = 32) //Insprint
	public void e24channelSearch() throws Exception {
		Zee5TvBusiness.e24ChannelSearch();
	}
	
//	@Test(priority = 33) //Insprint
	public void noReminderText() throws Exception {
		Zee5TvBusiness.noReminderText();
	}
	
//	@Test(priority = 34) //Insprint
	public void premiumContentPlayback() throws Exception {
		Zee5TvBusiness.premiumconetntPlayback();
	}

	@AfterTest
	public void After() {
		System.out.println("Tear Down");
		Zee5TvBusiness.TvtearDown();
	}

}
