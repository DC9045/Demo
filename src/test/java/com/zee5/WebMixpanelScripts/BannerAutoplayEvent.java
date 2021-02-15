package com.zee5.WebMixpanelScripts;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.business.zee.Zee5PWAWEBMixPanelBusinessLogic;

public class BannerAutoplayEvent {

	private Zee5PWAWEBMixPanelBusinessLogic Zee5PWAWEBMixPanelBusinessLogic;

	@BeforeTest
	public void init() throws Exception {
		Zee5PWAWEBMixPanelBusinessLogic = new Zee5PWAWEBMixPanelBusinessLogic("Chrome");
	}

	@Test(priority = 1)
	public void verifyBannerAutoplayEventForNewsContent() throws Exception {
		System.out.println("Verify Banner Autoplay Event");
		Zee5PWAWEBMixPanelBusinessLogic.verifyBannerAutoplayEventForNewsContent();
	}

	@AfterClass
	public void tearDown() {
		Zee5PWAWEBMixPanelBusinessLogic.tearDown();
	}
}
