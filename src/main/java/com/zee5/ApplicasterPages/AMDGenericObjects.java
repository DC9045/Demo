package com.zee5.ApplicasterPages;

import org.openqa.selenium.By;

//-- Dev by Kushal
public class AMDGenericObjects {

	// Get Screen title across all screen
	public static By objgetScreenTitle = By.xpath("//*[@id='screen_title']");

	// Verifying page title displayed
	public static By objScreenTitleName(String title) {
		return By.xpath("//*[@id='screen_title' and @text='" + title + "']");
	}

	// Device Backout button
	public static By objDeviceBackBtn = By.xpath("//*[@resource-id='com.android.systemui:id/back']");

	// Text object
	public static By objText(String text) {
		return By.xpath("//*[@text='" + text + "']");
	}

	public static By objHideKeyboard = By.xpath("//*[@id='hide_btn']");

	public static By objCloseInterstitialAd = By.xpath(
			"//*[@contentDescription='Interstitial close button'] | //*[@content-desc='Interstitial close button']");

	public static By objWifiToggle = By.xpath("//*[@id='switch_widget']");

	// Check Screen Title displayed
	public static By objCheckTitle(String title) {
		return By.xpath(
				"//*[@id='screen_title' and @text='" + title + "'] | //*[@id='title' and @text='" + title + "']");
	}

	// Back button
	public static By objBackBtn = By.xpath("//*[@id='icon_back'] | //*[@id='action_icon']");

	public static By objPageLoadingIcon = By.xpath("//*[@id='compoiste_progress_bar']");

	public static By objTrayTitleByIndx(int index) {
		return By.xpath("(//*[@id='header_primary_text'])[" + index + "]");
	}

	public static By objPopUpDivider = By.xpath("//*[@id='dialog_divider']");

	public static By objPremiumTags = By.xpath("//*[@id='special_image_1']");
	public static By objMetaData = By.xpath(
			"//*[@id='main_genre_tv'] | //*[@id='release_year_tv'] | //*[@id='duration_tv'] | //*[@id='genresTv'] | //*[@id='ageRatingTv']");

	public static By objContainText(String text) {
		return By.xpath("//*[contains(text(),'" + text + "']");
	}

	public static By objTrayTitle = By.xpath("//*[@id='header_primary_text']");

	public static By objTrayTitleByIndex(int index) {
		return By.xpath("(//*[@id='header_primary_text'])[" + index + "]");
	}

	public static By objBottomNavigation(String tabName) {
		return By.xpath("//*[@id='bb_bottom_bar_title' and @text='" + tabName + "']");
	}

	public static By objSelectFirstCardFromTrayTitle(String text) {
		return By.xpath("(//*[@id='header_primary_text' and contains(text(),\"" + text
				+ "\")]//following::*[@id='item_image'])[1]");
	}

	public static By objViewAllBtn(String trayName) {
		return By.xpath("//*[contains(text(),'" + trayName + "')]//following::*[@text='a']");
	}

	public static By objSearchcontentTitle(String title) {
		return By.xpath("//*[@text='" + title + "' and @class='android.widget.TextView']");
	}

	public static By objNoOfTrays = By
			.xpath("//*[@id='header_primary_text'] | //*[@class='android.widget.TextView']/following::*[@text='a']");
	
	
	public static By objCarouselTitle(String title) {
		return By.xpath("//*[@id='cell_top_container']//*[@text='" + title + "']");
	}

	public static By objContentNameInTray(int index) {
		return By.xpath("(//*[@id='cell_top_container'])[" + index + "]");
	}

	public static By objContentNameInTray = By.xpath("//*[@id='cell_top_container']");

	public static By objPageTitle(String title) {
		return By.xpath(
				"//*[@class='android.widget.LinearLayout']//*[@id='homeTabLayout']//*[@class='android.widget.LinearLayout']//*[@text='"
						+ title + "']");
	}
	
	public static By objFirstTrayTitle = By.xpath("(//*[@id='header_primary_text'])[1] | (//*[@id='cell_top_container']/following::*[@text='a']//preceding-sibling::*)");
	
	public static By objTrayTitle(String text) {
		return By.xpath("//*[contains(text(),'" + text + "')]");
	}
	
	public static By objcontentnameinTray(int index) {
		return By.xpath("(//*[@id='cell_top_container']//*[@class='android.widget.TextView'])["+index+"]");
	}
}
