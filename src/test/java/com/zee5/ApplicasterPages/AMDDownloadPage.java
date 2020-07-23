package com.zee5.ApplicasterPages;

import org.openqa.selenium.By;

//-- Dev by Sushma

public class AMDDownloadPage {
	
	public static By objBrowseToDownloadBtn = By.xpath("//*[@id='btn_browse_to_download']");
	
	public static By objCardContentImage = By.xpath("//*[@id='cardContentImage']");
	
	public static By objShowDetails = By.xpath("//*[@id='rl_show_details']");
	
	public static By objShowTitle = By.xpath("//*[@resource-id='com.graymatrix.did:id/tv_title']");
	
	public static By objEpisodeMetadata = By.xpath("//*[@id='ll_episode_size']");
	
	public static By objEpisodeNumber = By.xpath("//*[@resource-id='com.graymatrix.did:id/tv_episode']");
	
	public static By objEpisodeSize = By.xpath("//*[@resource-id='com.graymatrix.did:id/tv_size']");
	
	public static By objContentCardRightArrowMark = By.xpath("//*[@id='img_right_arrow']");
	
	//Select the Show card
	public static By objSelectShowName(String showName) {
		return By.xpath("//*[@id='img_show']//following::*[@text='"+showName+"']//following::*[@id='img_right_arrow'][1]");
	}
	
	public static By objEpisodeDuration = By.xpath("//*[@resource-id='com.graymatrix.did:id/tv_time']");
	
	public static By objEpisodeState = By.xpath("//*[@resource-id='com.graymatrix.did:id/img_state']");
	
	public static By objDownloadMoreEpisodesButton = By.xpath("//*[@id='btn_download_more_episode']");

//Downloads text at the top center 
	public static By objDwnloadsHeader= By.xpath("//*[@id='title']");
	
	//Shows tab
	public static By objshowstab = By.xpath("//*[@text='Shows']");
	
	//Movies tab
	public static By objmoviestab=By.xpath("//*[@text='Movies']");
	//videos tab
	public static By objvideostab= By.xpath("//*[@text='Videos']");
	
	/**
	 * Bhavana
	 */
	public static By objGreyedThumbnail = By.xpath("//*[@id='downloadView' and @class='android.view.View']");
	public static By objdownloadpopup = By
			.xpath("//*[@class='android.widget.LinearLayout' and ./*[@text='Pause Download']]");
	public static By objPauseDownloadoption = By.xpath("//*[@id='tvPauseDownload']");
	public static By objCancelDownloadOption = By.xpath("//*[@id='tvCancelDownload']");
}
