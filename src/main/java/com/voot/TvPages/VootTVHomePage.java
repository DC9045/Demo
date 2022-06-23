package com.voot.TvPages;

import org.openqa.selenium.By;

public class VootTVHomePage {
	
	public static By objHighlightedTab = By.xpath("//*[@id='collection_header_label' and @selected='true']");

	public static By objSelectTab(String str) {
		return By.xpath("//*[@id='header_label' and text()='"+ str +"']");
		
}
		
// Skip Link in Welcome Screen
public static By objSkip = By.xpath("//*[@text='Skip']");
	
//Voot Logo In Home Page	
public static By objVootLogo = By.xpath("//*[@id='home_logo_imageView']"); 

// Leaving So Son Text In Exit Screen
public static By objLeavingSoSoonTextInExitScreen = By.xpath("//*[contains(@text,'Leaving so soon?')]");
	
//Yes CTA In Exit Screen	
public static By objYesCTAInExitScreen = By.xpath("//*[@id='yes_btn']");	
		
//No CTA In Exit Screen	
public static By objNoCTAInExitScreen = By.xpath("//*[@id='no_btn']");

//Yes CTA Focused  In Exit Screen	
public static By objYesCTAFocusedInExitScreen = By.xpath("//*[@id='yes_btn' and @focused='true']");


//No CTA Focused  In EXit Screen	
public static By objNoCTAFocusedInExitScreen = By.xpath("//*[@id='no_btn' and @focused='true']");

//To Select a tab in Left Menu 
public static By objToSelectTab(String str) {
	return By.xpath("//*[@text='"+ str +"']/following-sibling::*[@id='tab_selection_img']");
	
}

// to Find which tab is selected in left menu 
public static By objToFindTabSelected = By.xpath("//*[@id='tab_selection_img']/preceding-sibling::*[1]");


//Sidhu..

//Watch Latest Rail in My voot Page
public static By objWatchLatest = By.xpath("//*[contains(text(),'Watch Latest')]");

//Continue Watching Rail In My Voot Page 
public static By objContinueWatching = By.xpath("//*[contains(text(),'Continue Watching')]");

//
public static By objMyVootTABText = By.xpath("//*[@id='headerLayout' and ./*[@text='My Voot']]");

}
