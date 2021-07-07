package com.metadata;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import static com.jayway.restassured.RestAssured.given;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Reporter;

import com.appsflyerValidation.AppsFlyer;
import com.driverInstance.DriverInstance;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.jayway.restassured.RestAssured;
//import com.jayway.restassured.response.Response;
//import com.jayway.restassured.specification.RequestSpecification;
import com.mixpanelValidation.Mixpanel;
import com.mixpanelValidation.MixpanelAndroid;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ResponseInstance {

	protected static Response resp = null;
	public static String assetSubType = "Empty"; 
	static LoggingUtils logger = new LoggingUtils();
	public static String searchContentID = null;
	public static String pageName = "shows";
	public static boolean trailer = false;
	public static String newEmailID = null;
	public static String newPassword = null;
	
	public static Response getResponse() {
		resp = RestAssured.given().urlEncodingEnabled(false).when().get(
				"https://gwapi.zee5.com/content/collection/0-8-homepage?limit=20&page=1&item_limit=20&desc=no&version=6&translation=en&languages=en,kn&country=IN");
		return resp;
	}

	public static Response getResponse(String URL) {
		resp = RestAssured.given().urlEncodingEnabled(false).when().get(URL);
		System.out.println(resp.statusCode());
		return resp;
	}

	public static void traysTitle() {
		int numberOfTrays = getResponse().jsonPath().getList("buckets").size();
		for (int i = 1; i < numberOfTrays; i++) {
			System.out.println(getResponse().jsonPath().getString("buckets[" + i + "].title"));
		}
	}

	public static ArrayList<String> traysIndiviualTags(String NameOfTheTray) {
		ArrayList<String> Arraytags = new ArrayList<String>();
		int numberOfTrays = getResponse().jsonPath().getList("buckets").size();
		for (int i = 1; i < numberOfTrays; i++) {
			String TrayName = getResponse().jsonPath().getString("buckets[" + i + "].title");
			int numberOfitmesTrays = getResponse().jsonPath().getList("buckets[" + i + "].items").size();
			if (TrayName.equals(NameOfTheTray)) {
				for (int j = 0; j < numberOfitmesTrays; j++) {
					Arraytags.add(getResponse().jsonPath().getString("buckets[" + i + "].items[" + j + "].title"));
				}
				break;
			}
			if (Arraytags.size() > 0) {
				break;
			}
		}
		return Arraytags;
	}

	public static ArrayList<String> ContentOfViewAll() {
		ArrayList<String> contentFromViewAll = new ArrayList<String>();
		for (int i = 1; i <= 3; i++) {
			String page1 = "https://gwapi.zee5.com/content/collection/0-8-2074?page=" + i
					+ "&limit=20&desc=no&country=IN&languages=en,kn&translation=en&version=6";
			resp = RestAssured.given().urlEncodingEnabled(false).when().get(page1);
			int sizeOfAnItem = resp.jsonPath().getList("buckets[0].items").size();
			if (sizeOfAnItem > 0) {
				for (int j = 0; j < sizeOfAnItem; j++) {
					contentFromViewAll.add(resp.jsonPath().getString("buckets[0].items[" + j + "].title"));
					System.out.println(contentFromViewAll);
				}
			}
		}
		return contentFromViewAll;
	}

	public static ArrayList<String> getCollectionContent(String CollectionName) {

		ArrayList<String> contentList = new ArrayList<String>();
		int sizeOfAnCollection = 0;
		int NumberOfCollection = resp.jsonPath().getList("buckets[0].items").size();
		for (int i = 0; i < NumberOfCollection; i++) {
			if (resp.jsonPath().getString("").equals(CollectionName)) {
				sizeOfAnCollection = resp.jsonPath().getList("buckets[0].items").size();
				for (int j = 0; j < sizeOfAnCollection; j++) {
					contentList.add(resp.jsonPath().getString(""));
				}
			}
			if (sizeOfAnCollection > 0) {
				break;
			}
		}
		return contentList;
	}

//	BASAVARAJ
	public static Response getRECOResponse(String URL, String username, String password) {
		Response respp = null;
		String Uri = URL;
		respp = RestAssured.given()
				.headers("X-ACCESS-TOKEN", getXAccessToken(), "Authorization", getBearerToken(username, password))
				.when().get(Uri);
		System.out.println(respp.getBody());
		return respp;
	}

	/**
	 * Function to return X-ACCESS TOKEN
	 * 
	 * @param page
	 * @return
	 */
	public static String getXAccessToken() {
//		Response resp = null;
//		String xAccessToken = "";
//		String url = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("url");
//		resp = RestAssured.given().urlEncodingEnabled(false).when().get(url);
//		String respString = resp.getBody().asString();
//		respString = respString.replace("\"gwapiPlatformToken\":\"\"", "");
//		respString = respString.split("\"gwapiPlatformToken\":\"")[1];
//		xAccessToken = respString.split("\"")[0];
//		return xAccessToken;
		try {
		Response respToken = null, respForKey = null;
		// get APi-KEY
		String Uri = "https://useraction.zee5.com/token/platform_tokens.php?platform_name=desktop_web";
		respForKey = RestAssured.given().urlEncodingEnabled(false).when().get(Uri);
		String xAccessToken = respForKey.jsonPath().getString("token");
		return xAccessToken;
		}catch(Exception e) {
			return "";
		}
	
	}
	
	public static String getXAccessToken1() {
		Response respToken = null, respForKey = null;
		// get APi-KEY
		String Uri = "https://gwapi.zee5.com/user/getKey?=aaa";
		respForKey = RestAssured.given().urlEncodingEnabled(false).when().get(Uri);
		String rawApiKey = respForKey.getBody().asString();
		String apiKeyInResponse = rawApiKey.substring(0, rawApiKey.indexOf("<br>airtel "));
		String finalApiKey = apiKeyInResponse.replaceAll("<br>rel - API-KEY : ", "");
		String UriForToken = "http://gwapi.zee5.com/user/getToken";
		respToken = RestAssured.given().headers("API-KEY", finalApiKey).when().get(UriForToken);
		String xAccessToken = respToken.jsonPath().getString("X-ACCESS-TOKEN");
		return xAccessToken;
	}

	/**
	 * Function to return Bearer token
	 * 
	 * @param email, password
	 * @return bearer token
	 */
	public static String getBearerToken(String email, String password) {
		JSONObject requestParams = new JSONObject();
		requestParams.put("email", email);
		requestParams.put("password", password);
		RequestSpecification req = RestAssured.given();
		req.header("Content-Type", "application/json");
		req.config(io.restassured.RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig
				.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
		req.body(requestParams.toString());
		Response resp = req.post("https://userapi.zee5.com/v2/user/loginemail");
		String accesstoken = resp.jsonPath().getString("access_token");
		String tokentype = resp.jsonPath().getString("token_type");
		String bearerToken = tokentype.replaceAll("Bearer", "bearer") + " " + accesstoken;
		return bearerToken;
	}

	/**
	 * Get Recommendation data for the tab
	 * 
	 * @param tab
	 * @return reco response
	 */
	public static Response getRecoDataFromTab(String userType, String tab, String contLang) {
		Response respReco = null;
		if (tab.equals("tvshows") || tab.equals("tv shows")) {
			tab = "tvshows";
		}else if (tab.equalsIgnoreCase("premium")) {
			tab = "premiumcontents";
		} else if (tab.equalsIgnoreCase("home")) {
			tab = "homepage";
		} else if (tab.equalsIgnoreCase("kids")) {
			tab = "3673";
		} else if (tab.equalsIgnoreCase("videos")) {
			tab = "5011";
		} else if (tab.equalsIgnoreCase("movies")) {
			tab = "movies";
		} else if (tab.equalsIgnoreCase("music")) {
			tab = "2707";
		} else if (tab.equalsIgnoreCase("zeeoriginals")) {
			tab = "zeeoriginals";
		} else if (tab.equalsIgnoreCase("news")) {
			tab = "626";
		}
		Response regionResponse = RestAssured.given().urlEncodingEnabled(false).when().get("https://xtra.zee5.com/country");
		String region = regionResponse.getBody().jsonPath().getString("state_code");
		String Uri;
		if(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getSuite().getName().equals("TV")) {
			Uri = "https://gwapi.zee5.com/content/reco?country=IN&translation=en&languages=" + contLang
					+ "&version=9&collection_id=0-8-" + tab + "&region=" + region;
		}else {
		 Uri = "https://gwapi.zee5.com/content/reco?country=IN&translation=en&languages=" + contLang
				+ "&version=6&collection_id=0-8-" + tab + "&region=" + region;
		}
		String xAccessToken = getXAccessToken();
		if (userType.equalsIgnoreCase("Guest")) {
			// Get Guest Token
			JSONObject requestParams = new JSONObject();
			requestParams.put("aid", "91955485578");
			requestParams.put("apikey", "6BAE650FFC9A3CAA61CE54D");
			requestParams.put("DekeyVal", "Z5G211");
			requestParams.put("UserAgent", "pwaweb");
			RequestSpecification request = RestAssured.given();
			request.header("Content-Type", "application/json");
			request.config(io.restassured.RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig
					.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
			request.body(requestParams.toString());
			Response response = request.post("https://whapi-prod-node.zee5.com/fetchGuestToken");
			String guestToken = response.jsonPath().get("guest_user").toString();
			respReco = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", guestToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respReco = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respReco = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
		}
		respReco.body().print();
		return respReco;
	}

	/**
	 * Get Recommendation data for the content
	 * 
	 * @param tab
	 * @return reco response
	 */
	public static Response getRecoTraysInDetailsPage(String userType, String contentID) {
		Response respReco = null;
		String Uri = "https://gwapi.zee5.com/content/reco?asset_id=" + contentID
				+ "&country=IN&translation=en&languages=en,kn&version=6&region=KA";
		System.out.println("Hitting content api:\n" + Uri);
		String xAccessToken = getXAccessToken();
		if (userType.equalsIgnoreCase("Guest")) {
			respReco = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", "12345").when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respReco = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respReco = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		respReco.body().print();
		return respReco;
	}

	/**
	 * Function to tray list on carousel for different pages
	 * 
	 * @param page
	 * @return
	 */
	public static List<String> traysTitleCarousel(String page) {
		Response responseCarouselTitle = getResponseForPages(page);
		List<String> titleOnCarouselList = new LinkedList<String>();
		int numberOfCarouselSlides = responseCarouselTitle.jsonPath().getList("buckets[0].items").size();
		System.out.println("api size : " + numberOfCarouselSlides);
		for (int i = 0; i < numberOfCarouselSlides; i++) {
			String title = responseCarouselTitle.jsonPath().getString("buckets[0].items[" + i + "].title");
			titleOnCarouselList.add(title);
		}
		return titleOnCarouselList;
	}

	/**
	 * Function to return response for different pages
	 * 
	 * @param page
	 * @return
	 */
	public static Response getResponseForPages(String page) {
		Response respCarousel = null;
		String Uri ;
		if (page.equals("news")) {
			page = "626";
		} else if (page.equals("music")) {
			page = "2707";
		} else if (page.equals("home")) {
			page = "homepage";
		}else if(page.equals("live tv")) {
			Uri = "https://catalogapi.zee5.com/v1/channel/genres?translation=en&country=IN";
		}
			Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page
				+ "?page=1&limit=5&item_limit=20&country=IN&translation=en&languages=en,kn&version=6";
		respCarousel = RestAssured.given().urlEncodingEnabled(false).when().get(Uri);
		return respCarousel;
	}

	/**
	 * Method to get content details passing content ID
	 * 
	 */
	public static Response getContentDetails(String contentID, String contentType) {
		Response respContentDetails = null;
		String Uri = "";
		if (contentType.equals("original")) {
			Uri = "https://gwapi.zee5.com/content/tvshow/" + contentID + "?translation=en&country=IN";
		} else if (contentType.contentEquals("Manual")) {
			Uri = "https://gwapi.zee5.com/content/collection/" + contentID + "?translation=en&country=IN";
		} else {
			Uri = "https://gwapi.zee5.com/content/details/" + contentID + "?translation=en&country=IN&version=2";
		}
		respContentDetails = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessTokenWithApiKey()).when().get(Uri);
//		System.out.println("Content Details API Response: "+respContentDetails.getBody().asString());
		return respContentDetails;
	}

	/**
	 * Function to return response for different pages
	 * 
	 * @param page
	 * @return
	 */
	public static Response getResponseForPages(String page, String contLang) {
		Response respCarousel = null;
		String Uri = "";
		if (page.equals("news")) {
			page = "5857";
		} else if (page.equals("music")) {
			page = "2707";
		} else if (page.equals("home")) {
			page = "homepage";
		} else if (page.equals("kids")) {
			page = "3673";
		} else if (page.equals("freemovies")) {
			page = "5011";
		} else if (page.equals("play")) {
			page = "4603";
		} else if (page.equals("webseries") || page.equals("web series")) {
			page = "zeeoriginals";
		} else if (page.equals("videos")) {
			page = "videos";
		} else if (page.equals("movies")) {
			page = "movies";
		} else if (page.equals("tvshows") || page.equals("tv shows")) {
			page = "tvshows";
		} else if (page.equals("premium")) {
			page = "premiumcontents";
		} else if (page.equals("club")) {
			page = "5851";
		}
		if (page.equals("stories")) {
			Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
		} else if(page.equals("live tv")) {
			Uri = "https://catalogapi.zee5.com/v1/channel/genres?translation=en&country=IN";
		}else {
			Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page
					+ "?page=1&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=10";
			System.out.println(Uri);
		}
		respCarousel = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(Uri);
		System.out.println("Response status : " + respCarousel.statusCode());
		// System.out.println("Response Body : "+respCarousel.getBody().asString());
		return respCarousel;
	}

	/**
	 * Function to tray list on carousel for different pages
	 * 
	 * @param page
	 * @return
	 */
	public static List<String> traysTitleCarousel(String page, String contLang) {
		Response responseCarouselTitle = getResponseForPages(page, contLang);
		List<String> titleOnCarouselList = new LinkedList<String>();
		int numberOfCarouselSlides = 0;
		String title = "";
		if (page.equals("stories")) {
			numberOfCarouselSlides = responseCarouselTitle.jsonPath().getList("posts").size();
		} else {
			numberOfCarouselSlides = responseCarouselTitle.jsonPath().getList("buckets[0].items").size();
		}
		System.out.println("api size : " + numberOfCarouselSlides);
		if (numberOfCarouselSlides > 7)
			numberOfCarouselSlides = 7;
		for (int i = 0; i < numberOfCarouselSlides; i++) { // Only 7 tray visible on UI
			if (page.equals("stories")) {
				title = responseCarouselTitle.jsonPath().getString("posts[" + i + "].title");
			} else {
				title = responseCarouselTitle.jsonPath().getString("buckets[0].items[" + i + "].title");
			}
			titleOnCarouselList.add(title);
		}
		return titleOnCarouselList;
	}

	public static Response getResponseForPages2(String page, String contLang, int q) {
		Response respCarousel = null;
		String Uri = "";
		if (page.equals("news")) {
			page = "626";
		} else if (page.equals("music")) {
			page = "2707";
		} else if (page.equals("home")) {
			page = "homepage";
		} else if (page.equals("kids")) {
			page = "3673";
		} else if (page.equals("freemovies")) {
			page = "5011";
		} else if (page.equals("play")) {
			page = "4603";
		}

		if (page.equals("stories")) {
			Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
		} else {
			Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page + "?page=" + q
					+ "&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=6";
		}

		respCarousel = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessToken()).when().get(Uri);
		System.out.println("Response status : " + respCarousel.statusCode());
		return respCarousel;
	}

	public static Response getResponseForUpcomingPage(String userType) {
		Response respCarousel = null;

		
		String xAccessToken = getXAccessTokenWithApiKey();
//		respCarousel = RestAssured.given().urlEncodingEnabled(false).when().get(Url);
		
		if (userType.equalsIgnoreCase("Guest")) {
			String Uri = "https://gwapi.zee5.com/content/collection/0-8-3367?page=1&limit=10&item_limit=20&translation=en&country=IN&languages=en,kn&version=6&";
			
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).when().get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String Uri = "https://gwapi.zee5.com/content/collection/0-8-3367?page=1&limit=10&item_limit=20&translation=en&country=IN&languages=en,kn,hi&version=6&";
			
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String Uri = "https://gwapi.zee5.com/content/collection/0-8-3367?page=1&limit=10&item_limit=20&translation=en&country=IN&languages=en,kn&version=6&";
			
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		return respCarousel;
	}

	public static Response getResponseForApplicasterPages(String userType, String page) {
		Response respHome = null;
		String language = getLanguage(userType);
		String Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page
				+ "?page=1&limit=10&item_limit=20&translation=en&country=IN&version=6&languages=" + language;
		System.out.println(Uri);

		String xAccessToken = getXAccessTokenWithApiKey();
		if (userType.equalsIgnoreCase("Guest")) {
			respHome = RestAssured.given().headers("x-access-token", xAccessToken).when().get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respHome = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respHome = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		return respHome;
	}

	// Getting Content Language API response for the NonSubscribedUser and
	// SubscribedUser ..

	// Getting API response ::

	public static Response getUserinfoforNonSubORSub(String userType) {
		Response resp = null;

		String url = "https://userapi.zee5.com/v1/settings";

		String xAccessToken = getXAccessTokenWithApiKey();
//					if (userType.equalsIgnoreCase("Guest")) {
//						resp = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", "12345").when()
//								.get(url);
//					} else
		if (userType.equalsIgnoreCase("SubscribedUser")) {
//						String email="zeetest998@test.com";
//						String password ="123456";
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
//						String email="igstesting001@gmail.com";
			// String password ="igs@12345";
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
		} else {
			System.out.println("Incorrect user type passed to method");
		}

		return resp;

	}

	// Fetching language from response ::
	public static String getLanguage(String userType) {
		String language = null;
		if (userType.contains("Guest")) {
			language = "en,kn";
		} else {
			Response resplanguage = getUserinfoforNonSubORSub(userType);
			// System.out.println(resplanguage.print());

			// System.out.println(resplanguage.jsonPath().getList("array").size());

			for (int i = 0; i < resplanguage.jsonPath().getList("array").size(); i++) {

				String key = resplanguage.jsonPath().getString("[" + i + "].key");
				// System.out.println(language);
				if (key.contains("content_language")) {
					language = resplanguage.jsonPath().getString("[" + i + "].value");
					System.out.println("UserType Language: " + language);
					break;
				}
			}
		}
		return language;
	}

	public static String getXAccessTokenWithApiKey() {
		try {
		Response respToken = null, respForKey = null;
		// get APi-KEY
//		String Uri = "https://gwapi.zee5.com/user/getKey?=aaa"; //partner 
		String Uri = "https://useraction.zee5.com/token/platform_tokens.php?platform_name=desktop_web";
		respForKey = RestAssured.given().urlEncodingEnabled(false).when().get(Uri);
//		respForKey.print();
		String xAccessToken = respForKey.jsonPath().getString("token");
		System.out.println("Toke : "+xAccessToken);
//		String rawApiKey = respForKey.getBody().asString();
//		String apiKeyInResponse = rawApiKey.substring(0, rawApiKey.indexOf("<br>airtel "));
//		String finalApiKey = apiKeyInResponse.replaceAll("<br>rel - API-KEY : ", "");
//		String UriForToken = "http://gwapi.zee5.com/user/getToken";
//		respToken = RestAssured.given().headers("API-KEY", finalApiKey).when().get(UriForToken);
//		String xAccessToken = respToken.jsonPath().getString("X-ACCESS-TOKEN");
		return xAccessToken;
		}catch(Exception e) {
			return "";
		}
	}

	public static Response getRespofCWTray(String userType) {
		Response respCW = null;
//		String language = getLanguage(userType);

		String Uri = "https://gwapi.zee5.com/user/v2/watchhistory?country=IN&translation=en";

		String xAccessToken = getXAccessTokenWithApiKey();
		System.out.println(xAccessToken);
		if (userType.equalsIgnoreCase("Guest")) {
			respCW = RestAssured.given().headers("x-access-token", xAccessToken).when().get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String email = "testupgrade123@gmail.com";
			String password = "123456";
//			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
//					.getParameter("SubscribedUserName");
//			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
//					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
//			System.out.println(bearerToken.replace("Bearer", "bearer"));
			respCW = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
			respCW.print();
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {

//			String email = "igstesting001@gmail.com";
//			String password = "igs@12345";
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCW = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		return respCW;
	}


	public static Response getResponseForApplicasterPagesVersion2(String userType, String page, String pUsername,
			String pPassword) {
		Response respHome = null;
		String language = getLanguageVersion2(userType, pUsername, pPassword);
		String Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page
				+ "?page=1&limit=10&item_limit=20&translation=en&country=IN&version=6&languages=" + language;

		String xAccessToken = getXAccessTokenWithApiKey();
		if (userType.equalsIgnoreCase("Guest")) {
			respHome = RestAssured.given().headers("x-access-token", xAccessToken).when().get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String bearerToken = getBearerToken(pUsername, pPassword);
			respHome = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String bearerToken = getBearerToken(pUsername, pPassword);
			respHome = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		return respHome;
	}

	public static String getLanguageVersion2(String userType, String pUsername, String pPassword) {
		String language = null;
		if (userType.contains("Guest")) {
			language = "en,kn";
		} else {
			Response resplanguage = getUserinfoforNonSubORSubversion2(userType, pUsername, pPassword);
			// System.out.println(resplanguage.print());
			// System.out.println(resplanguage.jsonPath().getList("array").size());

			for (int i = 0; i < resplanguage.jsonPath().getList("array").size(); i++) {

				String key = resplanguage.jsonPath().getString("[" + i + "].key");
				// System.out.println(language);
				if (key.contains("content_language")) {
					language = resplanguage.jsonPath().getString("[" + i + "].value");
					System.out.println("UserType Language: " + language);
					break;
				}
			}
		}
		return language;
	}

	public static Response getUserinfoforNonSubORSubversion2(String userType, String pUsername, String pPassword) {
		Response resp = null;
		String url = "https://userapi.zee5.com/v1/settings";
		String xAccessToken = getXAccessTokenWithApiKey();

		if (userType.equalsIgnoreCase("SubscribedUser") | userType.equalsIgnoreCase("NonSubscribedUser")) {
			String bearerToken = getBearerToken(pUsername, pPassword);
			resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		return resp;
	}

	public static boolean BeforeTV(String UserType, String tabName) {
		Response resp = null;
		boolean title = false;
		String xAccessToken = getXAccessTokenWithApiKey();
		if (tabName.equals("Home")) {
			resp = RestAssured.given().headers("x-access-token", xAccessToken).when().get(
					"https://gwapi.zee5.com/content/collection/0-8-homepage?page=1&limit=10&item_limit=20&translation=en&country=IN&languages=en,kn&version=6");
		} else if (tabName.equals("Show")) {
			resp = RestAssured.given().headers("x-access-token", xAccessToken).when().get(
					"https://gwapi.zee5.com/content/collection/0-8-tvshows?page=1&limit=10&item_limit=20&translation=en&country=IN&version=6&languages=en,kn&version=6");
		}

		for (int i = 0; i < resp.jsonPath().getList("buckets").size(); i++) {
			title = resp.jsonPath().getString("buckets[" + i + "].title").contains("Premiere Episodes | Before");
			if (title) {
				break;
			}
		}
		return title;
	}

//			:::::::::::::::Mixpanel:::::::::::::::::::::: 

	public static void main(String[] args) throws Exception {
//		getResponseForApplicasterPages("Guest", "3673").print();
//		System.out.println(BeforeTV("Guest","Home"));
//		getUserData();
//		getRegion();
//		getresponse("kam");
//		getDetailsOfCustomer("zeetest@gmail.com","zee123");
//		getUserSettingsDetails("","");	
//		getFreeContent("home", "zee5latest@gmail.com", "User@123");
//		getContentDetails("0-0-232924","originals");
//		Player("basavaraj.pn5@gmail.com","igsindia123");
//		getWatchList("basavaraj.pn5@gmail.com","igsindia123");
//		getUserData("basavaraj.pn5@gmail.com","igsindia123");
//		getUserOldSettingsDetails("amdnonmixpanel@yopmail.com","123456");
//		ValidateRailsAndContents("Guest","Movies");
//		getUserData("amdnonmixpanel@yopmail.com","123456");
//		ArrayList<List<String>> AL = new ArrayList<List<String>>();
//		List<String> List = new ArrayList<String>();
//		
//		for(int i=0;i<10;i++) {
//			if(i<5) {
//					List.add(String.valueOf(i));
//			}else if(i>5) {
//				List.add(String.valueOf(i));
//			}
//			if(i == 5) {
//				AL.add(List);
//			}else if(i == 9) {
//				AL.add(List);
//			}
//		}
//		System.out.println(AL);
//		subscriptionDetails();
//		getSubscriptionDetails("zeetest10@test.com", "123456");
//		resp = RestAssured.given().headers("x-access-token", getXAccessToken()).when().get("https://gwapi.zee5.com/content/details/0-0-manual_5dtffmaonrs0?translation=en&country=IN&version=2");
//		resp.prettyPrint();
//		getTrayNameFromPage("home");
//		System.out.println(getPageResponse("home","free"));
//		System.out.println(getTrayResponse("Shows","premium"));
//		assetSubType = "video"; //0-1-manual_2g3a9k82241g
//		getContentDetails("0-6-972");
//		getTrayResponse("home","trailer");
//		subscriptionDetails();
//		getResponse("http://igs-machine4.ngrok.io/login");
//		getUserSettingsValues("zeetest10@test.com","123456");
//		getUserSettingsValues("zeeprime@mailnesia.com","123456");
//		 String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOiJST0xFX0FETUlOIiwiaXNzIjoibXlzZWxmIiwiZXhwIjoxNDcxMDg2MzgxfQ.1EI2haSz9aMsHjFUXNVz2Z4mtC0nMdZo6bo3-x-aRpw";
		
//		fetchSubscriptionDetailsFromToken();
		
		getRespofCWTray("SubscribedUser");
		
	}
	
	public static void fetchSubscriptionDetailsFromToken() {
		 String jwtToken = getBearerToken("zeein7@mailnesia.com","123456");
		 System.out.println(jwtToken);
		 java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
         String[] parts = jwtToken.split("\\.");
         String payloadJson = new String(decoder.decode(parts[1]));
         JSONObject jsonObject = new JSONObject(payloadJson);
         String payload = jsonObject.get("subscriptions").toString();
         Pattern p = Pattern.compile("\\[(.*)\\]");
         Matcher m = p.matcher(payload);
         String value = null;
         while (m.find()) {
			value = m.group(1);
         }
         System.out.println(value);
         JSONObject jsonObject2 = new JSONObject(value);
         System.out.println(jsonObject2.get("subscription_plan"));
         JSONObject jsonObject3 = new JSONObject(jsonObject2.get("subscription_plan").toString());
         System.out.println(jsonObject3.get("subscription_plan_type"));
	}

	public static Properties getUserSettingsDetails(String pUsername, String pPassword) {
		Properties pro = new Properties();
		if (!pUsername.equals("")) {
			String bearerToken = getBearerToken(pUsername, pPassword);
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken)
					.when().get("https://userapi.zee5.com/v1/settings");

			String[] comm = resp.asString().replace("{", "").replace("}", "").replace("[", "").replace("]", "")
					.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			System.out.println(comm.length);
			String value = null;
			for (int i = 0; i < comm.length;) {
				String key = comm[i].replace("\"", "").split("y:")[1];
				try {
					value = comm[i + 1].replace("\"", "").split("e:")[1];
				} catch (Exception e) {
					value = "Empty";
				}
				if (value.contains("age_rating")) {
					key = "Parent Control Setting";
					value = value.replace("\\", "").split(":")[1].replaceAll(" ", "").replace(",pin", "");
				}
				pro.setProperty(key, value);
				i = i + 2;
			}
		}
		return pro;
	}

	public static Properties getUserData(String pUsername,String pPassword) { 
		String[] userData = { "email", "first_name", "last_name", "birthday", "gender", "registration_country",
				"recurring_enabled" };
		Properties pro = new Properties();
		String xAccessToken = getXAccessTokenWithApiKey();
		String bearerToken = getBearerToken(pUsername, pPassword);
		String url = "https://userapi.zee5.com/v1/user";
		resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
		String commaSplit[] = resp.asString().replace("{", "").replace("}", "").replaceAll("[.,](?=[^\\[]*\\])", "-")
				.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		for (int i = 0; i < commaSplit.length; i++) {
			if (Stream.of(userData).anyMatch(commaSplit[i]::contains)) {
				String com[] = commaSplit[i].split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				String key = com[0].replace("\"", "");
				Mixpanel.FEProp.setProperty(key, com[1].replace("\"", ""));
				pro.setProperty(key, com[1].replace("\"", ""));
			}
		}
		pro.forEach((key, value) -> System.out.println(key + " : " + value));
		getDOB();
		Mixpanel.fetchUserdata = true;
		
		return pro;
	}
	
	
	
	private static void getDOB() {
		String birthday=resp.jsonPath().get("birthday").toString();
		LocalDate dob = LocalDate.parse(birthday.split("T")[0]);
		LocalDate curDate = LocalDate.now();
		int ageYears=Period.between(dob, curDate).getYears();
		int ageMonths=Period.between(dob, curDate).getMonths();
		if(ageMonths>=6) {
			++ageYears;
		}
		Mixpanel.FEProp.setProperty("Age", String.valueOf(ageYears));
	}

	public static String getRegion() {
		Response regionResponse = RestAssured.given().urlEncodingEnabled(false).when().get("https://xtra.zee5.com/country");
		return regionResponse.getBody().jsonPath().getString("state");
	}

	public static String getresponse(String searchText) {
		Response resp = RestAssured.given().urlEncodingEnabled(false).when().get("https://gwapi.zee5.com/content/search_all?q="
				+ searchText
				+ "&limit=10&asset_type=0,6,1&country=IN&languages=hi,en,mr,te,kn,ta,ml,bn,gu,pa,hr,or&translation=en&version=3&");
		return resp.jsonPath().getString("results[0].total");
	}

	public static String getFreeContent(String tabName, String pUsername, String pPassword) {
		PropertyFileReader handler = new PropertyFileReader("properties/MixpanelKeys.properties");
		String language;
		String pageName = handler.getproperty(tabName.toLowerCase());
		if (!pUsername.equals("")) {
			Properties prop = getUserSettingsDetails(pUsername, pPassword);
			System.out.println(prop.getProperty("content_language"));
			language = prop.getProperty("content_language");
		} else {
			language = "en,kn";
		}
		String url = "https://gwapi.zee5.com/content/collection/0-8-" + pageName
				+ "?page=1&limit=5&item_limit=20&country=IN&translation=en&languages=" + language + "&version=6";
		String xAccessToken = getXAccessTokenWithApiKey();
		if (!pUsername.equals("")) {
			String bearerToken = getBearerToken(pUsername, pPassword);
			resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
		} else {
			resp = RestAssured.given().headers("x-access-token", xAccessToken).when().get(url);
		}
		String title = "No Free Contents";
		for (int i = 0; i < 7; i++) {
			if (resp.jsonPath().getString("buckets[0].items[" + i + "].business_type").equals("free")) {
				title = resp.jsonPath().getString("buckets[0].items[" + i + "].title");
				if (!title.contains("HiPi")) {
					System.out.println(title);
					break;
				}
			}
		}
		
		if(!pUsername.equals("")) {
			getUserData(pUsername,pPassword);
		}
		return title;
	}
	
	public static Properties getUserOldSettingsDetails(String pUsername, String pPassword) {
		Properties pro = new Properties();
		if (!pUsername.equals("")) {
			String bearerToken = getBearerToken(pUsername, pPassword);
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken)
					.when().get("https://userapi.zee5.com/v1/settings");

			String[] comm = resp.asString().replace("{", "").replace("}", "").replace("[", "").replace("]", "")
					.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			String value = null;
			for (int i = 0; i < comm.length;) {
				String key = comm[i].replace("\"", "").split("y:")[1];
				try {
					value = comm[i + 1].replace("\"", "").split("e:")[1];
				} catch (Exception e) {
					value = "Empty";
				}
				if (value.contains("age_rating")) {
					key = "Parent Control Setting";
					value = value.replace("\\", "").split(":")[1].replace(", pin", "");
				}
				pro.setProperty(key, value);
				i = i + 2;
			}
		}
		pro.forEach((key, value) -> System.out.println(key + " : " + value));
		return pro;
	}
	
	public static void getContentDetails(String ID) {
		System.out.println("Content ID :"+ID);
		
		if (assetSubType.equalsIgnoreCase("tvshow") || assetSubType.equalsIgnoreCase("episode")
				|| assetSubType.equalsIgnoreCase("external_link")) {
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
					.get("https://gwapi.zee5.com/content/tvshow/" + ID + "?translation=en&country=IN");
		} else if (assetSubType.equalsIgnoreCase("external_link")) { 

		} else if(assetSubType.equalsIgnoreCase("original") || assetSubType.equalsIgnoreCase("video")
				|| assetSubType.equalsIgnoreCase("movie")|| assetSubType.equalsIgnoreCase("trailer")){
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
					.get("https://gwapi.zee5.com/content/details/" + ID + "?translation=en&country=IN&version=2");
			if(!assetSubType.equalsIgnoreCase("trailer")) {
			if(resp.jsonPath().getList("related")  != null) {
				ID = resp.jsonPath().getString("related[0].id");
				resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/"+ID+"?translation=en&country=IN&version=2");
				}
			}
		} else {
			if (pageName.equals("episode") || pageName.equals("shows")) {
				resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
						.get("https://gwapi.zee5.com/content/tvshow/" + ID + "?translation=en&country=IN");
			} else if (pageName.equals("movies")) {
				resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
						.get("https://gwapi.zee5.com/content/details/" + ID + "?translation=en&country=IN&version=2");
				if(trailer) {
					ID = resp.jsonPath().getString("related[0].id");
					resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/"+ID+"?translation=en&country=IN&version=2");
				}
			}
		}
		
		Mixpanel.FEProp.setProperty("Content Duration", resp.jsonPath().getString("duration"));
		Mixpanel.FEProp.setProperty("Content ID", resp.jsonPath().getString("id"));
		Mixpanel.FEProp.setProperty("Content Name", resp.jsonPath().getString("original_title"));
		Mixpanel.FEProp.setProperty("Content Specification", resp.jsonPath().getString("asset_subtype"));
		Mixpanel.FEProp.setProperty("Characters",resp.jsonPath().getList("actors").toString().replaceAll(",","-").replaceAll("\\s", ""));
//		Mixpanel.FEProp.setProperty("Audio Language",resp.jsonPath().getList("audio_languages").toString().replace("[", "").replace("]", ""));
		Mixpanel.FEProp.setProperty("Subtitle Language", resp.jsonPath().getString("subtitle_languages").toString());
		Mixpanel.FEProp.setProperty("Content Type",resp.jsonPath().getString("business_type"));
		Mixpanel.FEProp.setProperty("Genre",resp.jsonPath().getList("genre.id").toString().replaceAll(",","-").replaceAll("\\s", ""));
		Mixpanel.FEProp.setProperty("Content Original Language",resp.jsonPath().getString("languages").replace("[", "").replace("]", ""));
		if(resp.jsonPath().getString("is_drm").equals("1")) {
		Mixpanel.FEProp.setProperty("DRM Video","true");
		}else {
			Mixpanel.FEProp.setProperty("DRM Video","false");
		}
		resp.print();
		Mixpanel.FEProp.forEach((key, value) -> System.out.println(key + " : " + value));
		trailer = false;
	}
	
	
	public static void Player(String pUsername, String pPassword) {
		String url = "https://gwapi.zee5.com/content/player/0-1-136585";
		String bearerToken = getBearerToken(pUsername, pPassword);
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken).when().get(url);
		resp.print();
	}
	
	@SuppressWarnings("unused")
	public static void getWatchList(String pUsername, String pPassword) {
		String url = "https://userapi.zee5.com/v1/watchlist";
		String bearerToken = getBearerToken(pUsername, pPassword);
		System.out.println(getXAccessTokenWithApiKey()+"\n");
		System.out.println(bearerToken);
//		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken).when().get(url);
//		resp.print();
	}
	
	
	public static String getLanguage1(String userType)
	{
		String language = null;
		if(userType.contains("Guest"))
		{
			language = "en,hi,kn";
		}else{
			Response resplanguage = ResponseInstance.getUserinfoforNonSubORSub(userType);			
		//	System.out.println(resplanguage.print());
			//System.out.println(resplanguage.jsonPath().getList("array").size());
			for(int i=0 ; i<resplanguage.jsonPath().getList("array").size() ; i++)
			{
				String key = resplanguage.jsonPath().getString("["+i+"].key");
			//	System.out.println(language);
				if(key.contains("content_language"))
				{
					language = resplanguage.jsonPath().getString("["+i+"].value");
					System.out.println("UserType Language: "+language);
					break;
				}
			}
		}
		return language;
	}
	
	public static void getContentDetailsForNews(String ID) {
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/" + ID + "?translation=en&country=IN&version=2");
		Mixpanel.FEProp.setProperty("Content Duration", resp.jsonPath().getString("duration"));
		Mixpanel.FEProp.setProperty("Content ID", resp.jsonPath().getString("id"));
		Mixpanel.FEProp.setProperty("Content Name", resp.jsonPath().getString("original_title"));
		Mixpanel.FEProp.setProperty("Characters",resp.jsonPath().getList("actors").toString().replaceAll(",", "-").replaceAll("\\s", ""));
		Mixpanel.FEProp.setProperty("Audio Language",resp.jsonPath().getList("audio_languages").toString().replace("[", "").replace("]", ""));
		Mixpanel.FEProp.setProperty("Subtitle Language", resp.jsonPath().getString("subtitle_languages").toString());
		Mixpanel.FEProp.setProperty("Content Type", resp.jsonPath().getString("business_type"));
		Mixpanel.FEProp.setProperty("Genre",resp.jsonPath().getList("genre.id").toString().replaceAll(",", "-").replaceAll("\\s", ""));
		Mixpanel.FEProp.setProperty("Content Original Language",resp.jsonPath().getString("languages").replace("[", "").replace("]", ""));
//		if(resp.jsonPath().getString("is_drm").equals("1")) {
		Mixpanel.FEProp.setProperty("DRM Video", resp.jsonPath().getString("is_drm"));
//		}else {
//			Mixpanel.FEProp.setProperty("DRM Video","false");
//		}
//		Mixpanel.FEProp.forEach((key, value) -> System.out.println(key + " : " + value));
	}

	public static Response getResponseDetails(String contentID) {

		Response responseRAW = null;
		String URI = "https://gwapi.zee5.com/content/details/" + contentID + "?translation=en&country=IN&version=2";
		responseRAW = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessTokenAMD()).when().get(URI);
		System.out.println("\n" + URI);
		if (responseRAW.statusCode() != 200) {
			System.out.println(responseRAW.getStatusLine() + " | Content ID: " + contentID);
		}
		return responseRAW;
	}

	public static String getXAccessTokenAMD() {
		Response respToken = null, respForKey = null;
		// get APi-KEY
		String Uri = "https://gwapi.zee5.com/user/getKey?=aaa";
		respForKey = RestAssured.given().urlEncodingEnabled(false).when().get(Uri);
		String rawApiKey = respForKey.getBody().asString();
		String apiKeyInResponse = rawApiKey.substring(0, rawApiKey.indexOf("<br>airtel "));
		String finalApiKey = apiKeyInResponse.replaceAll("<br>rel - API-KEY : ", "");
		String UriForToken = "http://gwapi.zee5.com/user/getToken";
		respToken = RestAssured.given().headers("API-KEY", finalApiKey).when().get(UriForToken);
		String xAccessToken = respToken.jsonPath().getString("X-ACCESS-TOKEN");
		return xAccessToken;
	}

	public static Response getResponseForAppPages(String page, String contLang, String pUserType) {
		Response respCarousel = null;
		String Url = "";
		
		if(page.equalsIgnoreCase("Web Series")) {
			page= "ZEE5 Originals";
		}
		
		switch (page.toLowerCase()) {
			case "home":
				page = "homepage";
				break;
				
			case "tv shows":
				page = "tvshows";
				break;
				
			case "movies":
				page = "movies";
				break;
				
			case "premium":
				page = "premiumcontents";
				break;
				
			case "news":
				page = "5857";
				break;
			
			case "club":
				page = "5851";
				break;
				
			case "eduauraa":
				page = "6184";
				break;
				
			case "music":
				page = "2707";
				break;
			
			case "zeeoriginals":
				page = "zeeoriginals";
				break;
				
			case "zee5 originals":
				page = "zeeoriginals";
				break;
			
			case "freemovies":
				page = "5011";
				break;
			
			case "videos":
				page = "videos";
				break;
					
			case "live tv":
				Url = "https://catalogapi.zee5.com/v1/channel/bygenre?sort_by_field=channel_number&sort_order=ASC&page=1&page_size=100&genres=FREE%20Channels%2CNews%2CHindi%20Entertainment%2CKids%2CMusic%2CElectro%20Dance%20Music%2CHindi%20Movies%2CEnglish%20Entertainment%2CHindi%20News%2CEnglish%20News%2CMarathi%2CTamil%2CTelugu%2CKannada%2CMalayalam%2CBengali%2CPunjabi%2CGujarati%2COdiya%2CEntertainment%2CMovie%2CLifestyle%2CDevotional%2CComedy%2CDrama%2CSports%2CInfotainment%2CMythology%2CEducation%2CTrap%2CCrime%20%26%20Mystery%2CFREE%20News%20Channels%2CSunburn%2CIndie%2CFitness%2CLive%20Event%2CMusical%2CSpiritual&languages="+contLang+"&translation=en&country=IN";
				break;
				
			case "stories":
				Url = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
				break;
				
			default:
				System.out.println("\n*** INVALID TAB NAME PASSED!!! ***\n");
				break;
		}
		
		if(Url.length() == 0) {
			Url = "https://gwapi.zee5.com/content/collection/0-8-" + page+ "?page=1&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=10";
//			respCarousel = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessTokenAMD()).when().get(Url);
			
			if (pUserType.equalsIgnoreCase("SubscribedUser")) {
				String email = getParameterFromXML("SubscribedUserName");
				String password = getParameterFromXML("SubscribedPassword");
				String bearerToken = getBearerToken(email, password);
				respCarousel = RestAssured.given().headers("x-access-token", getXAccessTokenAMD()).header("authorization", bearerToken).when().get(Url);
			} else if (pUserType.equalsIgnoreCase("NonSubscribedUser")) {
				String email = getParameterFromXML("NonSubscribedUserName");
				String password = getParameterFromXML("NonSubscribedUserPassword");
				String bearerToken = getBearerToken(email, password);
				respCarousel = RestAssured.given().headers("x-access-token", getXAccessTokenAMD()).header("authorization", bearerToken).when().get(Url);
			} else {
				respCarousel = RestAssured.given().headers("X-ACCESS-TOKEN", getXAccessTokenAMD()).when().get(Url);
			}	
		}else if(page.equalsIgnoreCase("Live TV") ||page.equalsIgnoreCase("Livetv")) {
			respCarousel = getResponse(Url);
		}
		System.out.println(Url);
		System.out.println("Response status : " + respCarousel.statusCode());
		return respCarousel;
	}
	
public static String getTrayNameFromPage(String pTabName, String UserType) {
		
		String tabName = pTabName;
		String pUserType = UserType;
		String assettype="",contentID="",contentName="",pTrayName="",setContentType = "0";
		
		String pContentLang = getContentLanguageForAppMixpanel(pUserType);
		System.out.println("CONTENT LANG: "+pContentLang);
		
		resp=ResponseInstance.getResponseForAppPages(tabName,pContentLang,pUserType);
		System.out.println(resp.toString());
		
		if(tabName.equalsIgnoreCase("club")) {
			setContentType = "1";
		}else if(tabName.equalsIgnoreCase("premium")) {
			setContentType = "8";
		}else if(tabName.equalsIgnoreCase("Live TV")|| tabName.equalsIgnoreCase("livetv")){
			setContentType = "9";
		}else if(tabName.equalsIgnoreCase("ZEE5 Originals")|| tabName.equalsIgnoreCase("ZEEOriginals")){
			setContentType = "6";
		}

		if(tabName.equalsIgnoreCase("live tv") || tabName.equalsIgnoreCase("livetv")) {
			int totalCollection = resp.jsonPath().getList("items").size();
			for (int i = 0; i < totalCollection; i++) {
				int totalItems = resp.jsonPath().getList("items[" + i + "].items").size();
				if(totalItems > 0) {
					assettype = resp.jsonPath().get("items[" + i + "].items[0].asset_type").toString();
					pTrayName = resp.jsonPath().get("items[" + i + "].title").toString();
					if (assettype.equals(setContentType)) {
						contentID = resp.jsonPath().get("items[" + i + "].items[0].id").toString();
						contentName = resp.jsonPath().get("items[" + i + "].items[0].title").toString();
						
						System.out.println("\nTrayName: " + pTrayName);
						System.out.println("ContentID: " + contentID);
						System.out.println("ContentName: " + contentName);
						break;
					}	
				}
			}
		}else if(tabName.equalsIgnoreCase("club")){
			for (int i = 1; i < 5; i++) {
				assettype = resp.jsonPath().get("buckets[" + i + "].items[0].asset_type").toString();
				pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
				if (assettype.equals(setContentType)) {
					contentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
					contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
					
					System.out.println("\nTrayName: " + pTrayName);
					System.out.println("ContentID: " + contentID);
					System.out.println("ContentName: " + contentName);
					break;
				}
			}
			Response contentResp = getContentDetails(contentID, "");
			String assetSubType = contentResp.jsonPath().getString("asset_subtype");
			if(assetSubType.equalsIgnoreCase("episode")) {
				String newContentID = contentResp.jsonPath().getString("tvshow.id");
				contentResp = getContentDetails(newContentID, "original");
				assetSubType = contentResp.jsonPath().getString("asset_subtype");
				if(assetSubType.equalsIgnoreCase("tvshow")) {
					int seasonsCnt = contentResp.jsonPath().getList("seasons").size();
					if(pUserType.equalsIgnoreCase("guest") || pUserType.equalsIgnoreCase("NonSubscribedUser") && (seasonsCnt!=0)) {
						contentID = contentResp.jsonPath().get("seasons[0].trailers[0].id");
					}else if(seasonsCnt!=0){	
						contentID = contentResp.jsonPath().get("seasons[0].episodes[0].id");
					}else {
						System.out.println("\nNO ContentID to Fetch...");
					}
				}
			}
		}else if(tabName.equalsIgnoreCase("premium") ||tabName.equalsIgnoreCase("ZEE5 Originals")|| tabName.equalsIgnoreCase("ZEEOriginals")){
			for (int i = 1; i < 5; i++) {
				if(tabName.equalsIgnoreCase("premium")) {
					assettype = resp.jsonPath().get("buckets[" + i + "].asset_type").toString();
				}else {
					assettype = resp.jsonPath().get("buckets[" + i + "].items[0].asset_type").toString();
				}
				pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
				if (assettype.equals(setContentType)) {
					contentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
					contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
					
					System.out.println("\nTrayName: " + pTrayName);
					System.out.println("ContentID: " + contentID);
					System.out.println("ContentName: " + contentName);
					break;
				}
			}
			Response contentResp = getContentDetails(contentID, "original");
			int seasonCount = contentResp.jsonPath().getList("seasons").size();
			if(pUserType.equalsIgnoreCase("guest") && (seasonCount> 1)) {
				contentID = contentResp.jsonPath().get("seasons[0].trailers[0].id");
			}else if(seasonCount!=0){	
				contentID = contentResp.jsonPath().get("seasons[0].episodes[0].id");
			}else {
				System.out.println("\nNO EPISODES...");
			}
		}else {
			for (int i = 1; i < 5; i++) {
				assettype = resp.jsonPath().get("buckets[" + i + "].items[0].asset_type").toString();
				pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
				if (assettype.equals("0") || assettype.equals("1")) {
					contentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
					contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
					
					System.out.println("\nTrayName: " + pTrayName);
					System.out.println("ContentID: " + contentID);
					System.out.println("ContentName: " + contentName);
					break;
				}
			}
		}
		setContentFEProperty(pUserType, contentID, pTabName,pTrayName);
		return pTrayName;
	}


	
public static void setContentFEProperty(String pUserType,String pContentID,String pTabName,String pTrayName) {
	
	String newContentId = null;
	String pCharacters = null;
	String setBusinessType = "free_downloadable";

	boolean relatedFlag = true;
	Response contentResp = null, subContentResp = null;
	
	contentResp = getResponseDetails(pContentID);
	System.out.println("ContentID RESPONSE Status: " + contentResp.getStatusCode());
	
	if(pTabName.equalsIgnoreCase("premium") | pTabName.equalsIgnoreCase("Zee5 Originals") | pTabName.equalsIgnoreCase("ZeeOriginals")) {
		relatedFlag=false;
	}

	int relatedNodelength = contentResp.jsonPath().getList("related").size();
	System.out.println("Related Node: " + relatedNodelength);
	
	if (!pUserType.equalsIgnoreCase("SubscribedUser") || !pUserType.equalsIgnoreCase("NonSubscribedUser") && (relatedNodelength >= 1) && (relatedFlag)) {
		
		String assetSubType = contentResp.jsonPath().get("asset_subtype").toString();
		System.out.println(assetSubType);
		if(assetSubType.equalsIgnoreCase("movie")) {
			for(int rCount=0;rCount < relatedNodelength;rCount++) {
				String getBusinessType = contentResp.jsonPath().get("related["+rCount+"].business_type").toString();
				System.out.println(getBusinessType);
				if(getBusinessType.equalsIgnoreCase(setBusinessType)) {
					newContentId = contentResp.jsonPath().get("related["+rCount+"].id").toString();
					System.out.println(newContentId);
					break;
				}
			}
		}else {
			newContentId = contentResp.jsonPath().get("related[0].id");
		}
		
//		newContentId = contentResp.jsonPath().get("related[0].id");
		System.out.println("Related Node is Present | contentID: " + newContentId);
		subContentResp = getResponseDetails(newContentId);
		System.out.println("RESPONSE StatusCode: " + subContentResp.statusCode());

		String pOriginalTitle = subContentResp.jsonPath().get("original_title");
		int contentDuration = subContentResp.jsonPath().get("duration");
		String pDuration = Integer.toString(contentDuration);
		String onAir = subContentResp.jsonPath().get("on_air");
		String pBussinessType = subContentResp.jsonPath().get("business_type");
		String pImgURL = subContentResp.jsonPath().get("image_url");
		int assetType = subContentResp.jsonPath().get("asset_type");
		String pContentSpecification = subContentResp.jsonPath().get("asset_subtype");
		int episode = subContentResp.jsonPath().get("episode_number");
		String pEpisodeNum = Integer.toString(episode);
		String release_date = subContentResp.jsonPath().get("release_date").toString();
		String pPublishedDate = release_date.replace(release_date.substring(10), "");
		String pContentBillingType = subContentResp.jsonPath().get("billing_type");
		if (pContentBillingType.length() == 0) {
			pContentBillingType = "N/A";
		}

		//---Getting Languages from API
		int languageCount = subContentResp.jsonPath().getList("languages").size();
		String pLanguages = null;
		ArrayList<String> getLanguages = new ArrayList<String>();
		if (languageCount > 1) {
			for (int l = 0; l < languageCount; l++) {
				getLanguages.add(subContentResp.jsonPath().get("languages[" + l + "]").toString());
			}
			pLanguages = getLanguages.toString();
		} else if (languageCount == 1) {
			pLanguages = subContentResp.jsonPath().get("languages[0]");
		} else {
			System.out.println("Original Languages is Empty");
		}

		//---Getting Audio Languages from API
		int audioLangCount = subContentResp.jsonPath().getList("audio_languages").size();
		String pAudioLangguage = null;
		ArrayList<String> getAudioLanguages = new ArrayList<String>();
		if (audioLangCount > 1) {
			for (int a = 0; a < audioLangCount; a++) {
				getAudioLanguages.add(subContentResp.jsonPath().get("audio_languages[" + a + "]").toString());
			}
			pAudioLangguage = getAudioLanguages.toString();
		} else if (audioLangCount == 1) {
			pAudioLangguage = subContentResp.jsonPath().get("audio_languages[0]");
		} else {
			pAudioLangguage = "N/A";
		}

		//---Getting Subtitle Languages from API
		int subTitleLangCount = subContentResp.jsonPath().getList("subtitle_languages").size();
		String pSubTitleLangguage = null;
		ArrayList<String> getSubTitles = new ArrayList<String>();
		if (subTitleLangCount > 1) {
			for (int s = 0; s < subTitleLangCount; s++) {
				getSubTitles.add(subContentResp.jsonPath().get("subtitle_languages[" + s + "]").toString());
			}
			pSubTitleLangguage = getSubTitles.toString();
		} else if (subTitleLangCount == 1) {
			pSubTitleLangguage = subContentResp.jsonPath().get("subtitle_languages[0]");
		} else {
			pSubTitleLangguage = "N/A";
		}

		//---Getting Genres from API
		int genreCount = subContentResp.jsonPath().getList("genres").size();
		String pGenre = null;
		for (int i = 0; i < genreCount; i++) {
			if (genreCount == 1) {
				pGenre = subContentResp.jsonPath().get("genre[" + i + "].value");
			} else {
				pGenre = pGenre + "," + subContentResp.jsonPath().get("genre[" + i + "].value");
			}
		}
		pGenre = pGenre.replace("null,", "");

		//---Getting Characters from API
		int actorsCount = subContentResp.jsonPath().getList("actors").size();
		ArrayList<String> getCharacters = new ArrayList<String>();
		if (actorsCount >= 1) {
			for (int j = 0; j < actorsCount; j++) {
				getCharacters.add(subContentResp.jsonPath().get("actors[" + j + "]").toString());
			}
			pCharacters = getCharacters.toString();
		} else {
			pCharacters = getCharacters.toString();
		}

		int isDRM = subContentResp.jsonPath().get("is_drm");
		String pDRM_Video = "false";
		if (isDRM == 1) {
			pDRM_Video = "true";
		}

		System.out.println("\n--PRINTING VALIDATION PARAMETERS--");
		System.out.println(pTrayName);
		System.out.println(newContentId);
		System.out.println(pOriginalTitle);
		System.out.println(pDuration);
		System.out.println(pBussinessType);
		System.out.println(pGenre);
		System.out.println(pContentSpecification);
		System.out.println(pEpisodeNum);
		System.out.println(pPublishedDate);
		System.out.println(pLanguages);
		System.out.println(pAudioLangguage);
		System.out.println(pSubTitleLangguage);
		System.out.println(pCharacters);
		System.out.println(pDRM_Video);
		System.out.println(pImgURL);
		System.out.println(pContentBillingType);
		System.out.println("AssetType : " +assetType);
		System.out.println(onAir);
		
		// ----- Mix Panel Content Parameters Validation ------
		Mixpanel.FEProp.setProperty("Audio Language", pAudioLangguage);
		Mixpanel.FEProp.setProperty("Carousal Name", pTrayName);
		Mixpanel.FEProp.setProperty("Content ID", newContentId);
		Mixpanel.FEProp.setProperty("Content Name", pOriginalTitle);
		Mixpanel.FEProp.setProperty("Content Duration", pDuration);
		Mixpanel.FEProp.setProperty("Content Type", pBussinessType);
		Mixpanel.FEProp.setProperty("Content Specification", pContentSpecification);
		Mixpanel.FEProp.setProperty("Content Original Language", pLanguages);
		Mixpanel.FEProp.setProperty("Characters", pCharacters);
		Mixpanel.FEProp.setProperty("Content Billing Type", pContentBillingType);
		Mixpanel.FEProp.setProperty("DRM Video", pDRM_Video);
		Mixpanel.FEProp.setProperty("Episode No", pEpisodeNum);
		Mixpanel.FEProp.setProperty("Genre", pGenre);
		Mixpanel.FEProp.setProperty("Image URL", pImgURL);
		Mixpanel.FEProp.setProperty("Publishing Date", pPublishedDate);
		Mixpanel.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);
		
	}else if(pTabName.equalsIgnoreCase("Live TV")){
		
		int getDuration = contentResp.jsonPath().get("duration");
		String pDuration = Integer.toString(getDuration);
		if(pDuration.equalsIgnoreCase("0")) {
			pDuration = "N/A";
		}
		String onAir = contentResp.jsonPath().get("on_air");
		String pBusinessType = contentResp.jsonPath().get("business_type");
		int assetType = contentResp.jsonPath().get("asset_type");
		String pImgURL = contentResp.jsonPath().get("image_url");
		int getEpisodeNumber = contentResp.jsonPath().get("episode_number");
		String pEpisodeNumber = Integer.toString(getEpisodeNumber);
		if(pEpisodeNumber.equalsIgnoreCase("0")) {
			pEpisodeNumber = "N/A";
		}
		String pTitle = contentResp.jsonPath().get("title");
		String pContentBillingType = contentResp.jsonPath().get("billing_type");
		if (pContentBillingType.length() == 0) {
			pContentBillingType = "N/A";
		}

		//---Getting Languages from API
		int languageCount = contentResp.jsonPath().getList("languages").size();
		String pLanguages = null;
		ArrayList<String> getLanguages = new ArrayList<String>();
		if (languageCount > 1) {
			for (int l = 0; l < languageCount; l++) {
				getLanguages.add(contentResp.jsonPath().get("languages[" + l + "]").toString());
			}
			pLanguages = getLanguages.toString();
		} else if (languageCount == 1) {
			pLanguages = contentResp.jsonPath().get("languages[0]");
		} else {
			System.out.println("Original Languages is Empty");
		}

		//---Getting Genres from API
		int genreSize = contentResp.jsonPath().getList("genre").size();
		String pGenre = null;
		for (int i = 0; i < genreSize; i++) {
			if (genreSize == 1) {
				pGenre = contentResp.jsonPath().get("genre[" + i + "].value");
			} else {
				pGenre = pGenre + "," + contentResp.jsonPath().get("genre[" + i + "].value");
			}
		}
		pGenre = pGenre.replace("null,", "");

		//---Getting Audio Languages from API
		int audioLangCount = contentResp.jsonPath().getList("audio_languages").size();
		String pAudioLangguage = null;
		ArrayList<String> getAudioLanguages = new ArrayList<String>();
		if (audioLangCount > 1) {
			for (int a = 0; a < audioLangCount; a++) {
				getAudioLanguages.add(contentResp.jsonPath().get("audio_languages[" + a + "]").toString());
			}
			pAudioLangguage = getAudioLanguages.toString();
		} else if (audioLangCount == 1) {
			pAudioLangguage = contentResp.jsonPath().get("audio_languages[0]");
		} else {
			pAudioLangguage = "NA";
		}

		//---Getting Subtitle Languages from API
		int subTitleLangCount = contentResp.jsonPath().getList("subtitle_languages").size();
		String pSubTitleLangguage = null;
		ArrayList<String> getSubTitles = new ArrayList<String>();
		if (subTitleLangCount > 1) {
			for (int s = 0; s < subTitleLangCount; s++) {
				getSubTitles.add(contentResp.jsonPath().get("subtitle_languages[" + s + "]").toString());
			}
			pSubTitleLangguage = getSubTitles.toString();
		} else if (subTitleLangCount == 1) {
			pSubTitleLangguage = contentResp.jsonPath().get("subtitle_languages[0]");
		} else {
			pSubTitleLangguage = "N/A";
		}

		//---Getting Characters from API
		int actorsCount = contentResp.jsonPath().getList("actors").size();
		ArrayList<String> getCharacters = new ArrayList<String>();
		if (actorsCount >= 1) {
			for (int j = 0; j < actorsCount; j++) {
				getCharacters.add(contentResp.jsonPath().get("actors[" + j + "]").toString());
			}
			pCharacters = getCharacters.toString();
		} else if (pTabName.equalsIgnoreCase("Live TV") | pTabName.equalsIgnoreCase("Livetv")) {
			getCharacters.add("N/A");
			pCharacters = getCharacters.toString().replace("[", "").replace("]", "");
		} else {
			pCharacters = getCharacters.toString();
		}

		int isDRM = contentResp.jsonPath().get("is_drm");
		String pDRM_Video = "false";
		if (isDRM == 1) {
			pDRM_Video = "true";
		}
		
		String pPublishedDate = "N/A";

		System.out.println("\n--PRINTING VALIDATION PARAMETERS--");
		System.out.println(pTrayName);
		System.out.println(pContentID);
		System.out.println(pTitle);
		System.out.println(pDuration);
		System.out.println(pBusinessType);
		System.out.println(pGenre);
		System.out.println(pEpisodeNumber);
		System.out.println(pLanguages);
		System.out.println(pAudioLangguage);
		System.out.println(pSubTitleLangguage);
		System.out.println(pCharacters);
		System.out.println(pDRM_Video);
		System.out.println(pImgURL);
		System.out.println(pContentBillingType);
		System.out.println(pPublishedDate);
		System.out.println("AssetType : " + assetType);
		System.out.println(onAir);
		
		// ----- Mix Panel Content Parameters Validation ------
		Mixpanel.FEProp.setProperty("Audio Language", pAudioLangguage);
		Mixpanel.FEProp.setProperty("Carousal Name", pTrayName);
		Mixpanel.FEProp.setProperty("Content ID", pContentID);
		Mixpanel.FEProp.setProperty("Content Name", pTitle);
		Mixpanel.FEProp.setProperty("Content Duration", pDuration);
		Mixpanel.FEProp.setProperty("Content Type", pBusinessType);
		Mixpanel.FEProp.setProperty("Content Original Language", pLanguages);
		Mixpanel.FEProp.setProperty("Characters", pCharacters);
		Mixpanel.FEProp.setProperty("Content Billing Type", pContentBillingType);
		Mixpanel.FEProp.setProperty("DRM Video", pDRM_Video);
		Mixpanel.FEProp.setProperty("Episode No", pEpisodeNumber);
		Mixpanel.FEProp.setProperty("Genre", pGenre);
		Mixpanel.FEProp.setProperty("Image URL", pImgURL);
		Mixpanel.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);
		Mixpanel.FEProp.setProperty("Publishing Date", pPublishedDate);
		
	}else {
		
		int getDuration = contentResp.jsonPath().get("duration");
		String pDuration = Integer.toString(getDuration);
		String onAir = contentResp.jsonPath().get("on_air");
		String pBusinessType = contentResp.jsonPath().get("business_type");
		int assetType = contentResp.jsonPath().get("asset_type");
		String pAssetSubType = contentResp.jsonPath().get("asset_subtype");
		String pImgURL = contentResp.jsonPath().get("image_url");
		int getEpisodeNumber = contentResp.jsonPath().get("episode_number");
		String pEpisodeNumber = Integer.toString(getEpisodeNumber);
		String pTitle = contentResp.jsonPath().get("title");
		String releaseDate = contentResp.jsonPath().get("release_date").toString();
		String pPublishedDate = releaseDate.replace(releaseDate.substring(10), "");
		String pContentBillingType = contentResp.jsonPath().get("billing_type");
		if (pContentBillingType.length() == 0) {
			pContentBillingType = "N/A";
		}

		//---Getting Languages from API
		int languageCount = contentResp.jsonPath().getList("languages").size();
		String pLanguages = null;
		ArrayList<String> getLanguages = new ArrayList<String>();
		if (languageCount > 1) {
			for (int l = 0; l < languageCount; l++) {
				getLanguages.add(contentResp.jsonPath().get("languages[" + l + "]").toString());
			}
			pLanguages = getLanguages.toString();
		} else if (languageCount == 1) {
			pLanguages = contentResp.jsonPath().get("languages[0]");
		} else {
			System.out.println("Original Languages is Empty");
		}

		//---Getting Genres from API
		int genreSize = contentResp.jsonPath().getList("genre").size();
		String pGenre = null;
		for (int i = 0; i < genreSize; i++) {
			if (genreSize == 1) {
				pGenre = contentResp.jsonPath().get("genre[" + i + "].value");
			} else {
				pGenre = pGenre + "," + contentResp.jsonPath().get("genre[" + i + "].value");
			}
		}
		pGenre = pGenre.replace("null,", "");

		//---Getting Audio Languages from API
		int audioLangCount = contentResp.jsonPath().getList("audio_languages").size();
		String pAudioLangguage = null;
		ArrayList<String> getAudioLanguages = new ArrayList<String>();
		if (audioLangCount > 1) {
			for (int a = 0; a < audioLangCount; a++) {
				getAudioLanguages.add(contentResp.jsonPath().get("audio_languages[" + a + "]").toString());
			}
			pAudioLangguage = getAudioLanguages.toString();
		} else if (audioLangCount == 1) {
			pAudioLangguage = contentResp.jsonPath().get("audio_languages[0]");
		} else {
			pAudioLangguage = "N/A";
		}

		//---Getting Subtitle Languages from API
		int subTitleLangCount = contentResp.jsonPath().getList("subtitle_languages").size();
		String pSubTitleLangguage = null;
		ArrayList<String> getSubTitles = new ArrayList<String>();
		if (subTitleLangCount > 1) {
			for (int s = 0; s < subTitleLangCount; s++) {
				getSubTitles.add(contentResp.jsonPath().get("subtitle_languages[" + s + "]").toString());
			}
			pSubTitleLangguage = getSubTitles.toString();
		} else if (subTitleLangCount == 1) {
			pSubTitleLangguage = contentResp.jsonPath().get("subtitle_languages[0]");
		} else {
			pSubTitleLangguage = "N/A";
		}

		//---Getting Characters from API
		int actorsCount = contentResp.jsonPath().getList("actors").size();
		ArrayList<String> getCharacters = new ArrayList<String>();
		if (actorsCount >= 1) {
			for (int j = 0; j < actorsCount; j++) {
				getCharacters.add(contentResp.jsonPath().get("actors[" + j + "]").toString());
			}
			pCharacters = getCharacters.toString();
		} else {
			pCharacters = getCharacters.toString();
		}

		int isDRM = contentResp.jsonPath().get("is_drm");
		String pDRM_Video = "false";
		if (isDRM == 1) {
			pDRM_Video = "true";
		}

		System.out.println("\n--PRINTING VALIDATION PARAMETERS--");
		System.out.println(pTrayName);
		System.out.println(pContentID);
		System.out.println(pTitle);
		System.out.println(pDuration);
		System.out.println(pBusinessType);
		System.out.println(pGenre);
		System.out.println(pAssetSubType);
		System.out.println(pEpisodeNumber);
		System.out.println(pPublishedDate);
		System.out.println(pLanguages);
		System.out.println(pAudioLangguage);
		System.out.println(pSubTitleLangguage);
		System.out.println(pCharacters);
		System.out.println(pDRM_Video);
		System.out.println(pImgURL);
		System.out.println(pContentBillingType);
		System.out.println("AssetType : " + assetType);
		System.out.println(onAir);
		
		// ----- Mix Panel Content Parameters Validation ------
		Mixpanel.FEProp.setProperty("Audio Language", pAudioLangguage);
		Mixpanel.FEProp.setProperty("Carousal Name", pTrayName);
		Mixpanel.FEProp.setProperty("Content ID", pContentID);
		Mixpanel.FEProp.setProperty("Content Name", pTitle);
		Mixpanel.FEProp.setProperty("Content Duration", pDuration);
		Mixpanel.FEProp.setProperty("Content Type", pBusinessType);
		Mixpanel.FEProp.setProperty("Content Specification", pAssetSubType);
		Mixpanel.FEProp.setProperty("Content Original Language", pLanguages);
		Mixpanel.FEProp.setProperty("Characters", pCharacters);
		Mixpanel.FEProp.setProperty("Content Billing Type", pContentBillingType);
		Mixpanel.FEProp.setProperty("DRM Video", pDRM_Video);
		Mixpanel.FEProp.setProperty("Episode No", pEpisodeNumber);
		Mixpanel.FEProp.setProperty("Genre", pGenre);
		Mixpanel.FEProp.setProperty("Image URL", pImgURL);
		Mixpanel.FEProp.setProperty("Publishing Date", pPublishedDate);
		Mixpanel.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);
	}	
}

public static Response getUserinfoforNonSubORSubForAppMixpanel(String userType) {
	Response resp = null;
	String url = "https://userapi.zee5.com/v1/settings";

	String xAccessToken = getXAccessTokenWithApiKey();

	if (userType.equalsIgnoreCase("SubscribedUser")) {
		String email = getParameterFromXML("SubscribedUserName");
		String password = getParameterFromXML("SubscribedPassword");
		String bearerToken = getBearerToken(email, password);
		resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
	} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
		String email = getParameterFromXML("NonSubscribedUserName");
		String password = getParameterFromXML("NonSubscribedUserPassword");
		String bearerToken = getBearerToken(email, password);
		resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
	} else {
		System.out.println("Incorrect user type passed to method");
	}
	return resp;
}
	
	public static String getContentLanguageForAppMixpanel(String userType) {
		String language = null;
		if (userType.equalsIgnoreCase("Guest")) {
			language = "en,kn";
		} else {
			Response resplanguage = getUserinfoforNonSubORSubForAppMixpanel(userType);
			
			for (int i = 0; i < resplanguage.jsonPath().getList("array").size(); i++) {

				String key = resplanguage.jsonPath().getString("[" + i + "].key");
				// System.out.println(language);
				if (key.contains("content_language")) {
					language = resplanguage.jsonPath().getString("[" + i + "].value");
					System.out.println("UserType Language: " + language);
					break;
				}
			}
		}
		return language;
	}
	
public static String getCarouselContentFromAPI(String usertype, String tabName) {
		
		String pContentLang = ResponseInstance.getContentLanguageForAppMixpanel(usertype);
		System.out.println("CONTENT LANG: "+pContentLang);
		
		Response pageResp=ResponseInstance.getResponseForAppPages(tabName,pContentLang,usertype);
		pageResp.print();
		
		String contentName = null;
		
		String asset_subtype=null;
		for (int i = 0; i < 5; i++) {
			asset_subtype = pageResp.jsonPath().get("buckets[0].items["+i+"].asset_subtype");
			contentName = pageResp.jsonPath().get("buckets[0].items["+i+"].title");
			System.out.println("asset_subtype: "+asset_subtype);
			String contentID1 = null;
			Response ContentResp1 = null;
			String ContentId2=null;
			Response ContentResp2=null;
			boolean flag = false;
			String var = null;
			if(tabName.equalsIgnoreCase("Shows")|| tabName.contentEquals("News")) {
				if(asset_subtype.equalsIgnoreCase("external_link")) {
					var=asset_subtype;
				}else {
					var="Invalid";
				}
			}else if(tabName.equalsIgnoreCase("Home")|| tabName.equalsIgnoreCase("Premium")|| tabName.equalsIgnoreCase("Club")||tabName.equalsIgnoreCase("Movies")){
				if(asset_subtype.equalsIgnoreCase("movie")) {
					var=asset_subtype;
				}else if(asset_subtype.equalsIgnoreCase("original")){
					var=asset_subtype;
				}else {
					var="Invalid";
				}
				
			}else {
				var=asset_subtype;
			}
			
			switch (var) {
			case "movie":
				contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].id");
				ContentResp1 = getResponseDetails(contentID1);
				int relatedNodelength = ContentResp1.jsonPath().getList("related").size();		
				if (relatedNodelength >= 1 && (!(usertype.equalsIgnoreCase("SubscribedUser")))) {
				  ContentId2 = ContentResp1.jsonPath().get("related[0].id");
			      ContentResp2 = getResponseDetails(ContentId2);
			      setFEPropertyOfContentFromAPI(ContentId2,ContentResp2, tabName);
				}else {
				  setFEPropertyOfContentFromAPI(contentID1,ContentResp1, tabName);
				}
				flag = true;
				break;
			case "original":
			    contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].id");
				ContentResp1 = getContentDetails(contentID1, "original");
				ContentId2 = ContentResp1.jsonPath().getString("seasons[0].episodes[0].id");
				ContentResp2 = getContentDetails(ContentId2, "original");
				setFEPropertyOfContentFromAPI(ContentId2,ContentResp2, tabName);
				flag = true;
				break;
			
			case "external_link":
				contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].slug");
				String value = fetchContentIDFromUrl(contentID1);
				System.out.println(value);
				ContentResp1 = ResponseInstance.getContentDetails(value, "original");
				if(!(tabName.equalsIgnoreCase("News") || tabName.equalsIgnoreCase("Eduauraa"))) {
					List<String> noOfEpisodes = ContentResp1.jsonPath().getList("seasons[0].episodes");
					for(int n=0; n<noOfEpisodes.size();n++) {
						String contenttype = ContentResp1.jsonPath().getString("seasons[0].episodes["+n+"].business_type");
						if(contenttype.equalsIgnoreCase("advertisement_downloadable")) {
							ContentId2 = ContentResp1.jsonPath().getString("seasons[0].episodes["+n+"].id");
							break;
						}
					}
					ContentResp2 = getContentDetails(ContentId2, "original");
					setFEPropertyOfContentFromAPI(ContentId2,ContentResp2, tabName);
				}else {
					setFEPropertyOfContentFromAPI(value,ContentResp1, tabName);
				}
				flag = true;
				break;
				
			case "video":
				contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].id");
				ContentResp1 = getResponseDetails(contentID1);
				setFEPropertyOfContentFromAPI(contentID1,ContentResp1, tabName);
				flag = true;
			    break;
			    
			default :System.out.println("not a required asset_subtype");
		    }
			
			if(flag==true) {
				break;
			}
		}
		return contentName;
	}
	
public static void setFEPropertyOfContentFromAPI(String contentID, Response ContentResp, String tabName) {
	
	String pCharacters = null;
	String contentDuration =null;
	if(tabName.equalsIgnoreCase("News")) {
		contentDuration="0";
	}else {
		contentDuration = ContentResp.jsonPath().getString("duration");
	}
	String pBusinessType = ContentResp.jsonPath().getString("business_type");
	String pAssetSubType = null;
	if(tabName.equalsIgnoreCase("News")) {
		pAssetSubType="N/A";
	}else {
		pAssetSubType = ContentResp.jsonPath().getString("asset_subtype");
	}
	int episode = ContentResp.jsonPath().get("episode_number");
	String pEpisodeNum = Integer.toString(episode);
	if(pEpisodeNum.equalsIgnoreCase("0")) {
		pEpisodeNum = "N/A";
	}
	
	String pTitle = ContentResp.jsonPath().getString("title");
	String pPublishedDate=null;
	if(tabName.equalsIgnoreCase("News")) {
		pPublishedDate = "null";
	}else {
		String release_date = ContentResp.jsonPath().get("release_date").toString();
	    pPublishedDate = release_date.replace(release_date.substring(10), "");
	}
	
	String pContentBillingType = ContentResp.jsonPath().getString("billing_type");
	if (pContentBillingType.length() == 0) {
		pContentBillingType = "N/A";
	}

	int languageCount = ContentResp.jsonPath().getList("languages").size();
	String pLanguages = null;
	if (languageCount > 1) {
		System.out.println("MULTIPLE LANGUAGES");
	} else if (languageCount == 1) {
		pLanguages = ContentResp.jsonPath().get("languages[0]");
	} else {
		System.out.println("Original Languages is Empty");
	}

	int genreSize = ContentResp.jsonPath().getList("genre").size();
	String pGenre = null;
	for (int j = 0; j < genreSize; j++) {
		if (genreSize == 1) {
			pGenre = ContentResp.jsonPath().get("genre[" + j + "].value");
		} else {
			pGenre = pGenre + "," + ContentResp.jsonPath().get("genre[" + j + "].value");
		}
	}
	pGenre = pGenre.replace("null,", "");

	int audioLangCount = ContentResp.jsonPath().getList("audio_languages").size();
	String pAudioLangguage = null;
	if (audioLangCount > 1) {
		System.out.println("AUDIO LANGUAGES");
	} else if (audioLangCount == 1) {
		pAudioLangguage = ContentResp.jsonPath().get("audio_languages[0]");
	} else {
		pAudioLangguage = "N/A";
		System.out.println("Audio Language is Empty");
	}

	int subTitleLangCount = ContentResp.jsonPath().getList("subtitle_languages").size();
	String pSubTitleLangguage = null;
	if (subTitleLangCount > 1) {
		System.out.println("SUBTITLE LANGUAGES");
	} else if (subTitleLangCount == 1) {
		pSubTitleLangguage = ContentResp.jsonPath().get("subtitle_languages[0]");
	} else {
		System.out.println("Subtitle Language is Empty");
		pSubTitleLangguage = "N/A";
	}

	int actorsCount = ContentResp.jsonPath().getList("actors").size();
	ArrayList<String> getCharacters = new ArrayList<String>();
	if (actorsCount >= 1) {
		for (int j = 0; j < actorsCount; j++) {
			getCharacters.add(ContentResp.jsonPath().get("actors[" + j + "]").toString());
		}
		pCharacters = getCharacters.toString();
		pCharacters = pCharacters.replace("[", "").replace("]", "");
	} else if (tabName.equalsIgnoreCase("Live TV") | tabName.equalsIgnoreCase("Livetv")) {
		getCharacters.add("N/A");
		pCharacters = getCharacters.toString().replace("[", "").replace("]", "");
	} else {
		System.out.println("charactrs");
		pCharacters = getCharacters.toString();	
		System.out.println(pCharacters);
	}

	int isDRM = ContentResp.jsonPath().get("is_drm");
	String pDRM_Video = "false";
	if (isDRM == 1) {
		pDRM_Video = "true";
	}

	System.out.println("\n--PRINTING VALIDATION PARAMETERS--");

	System.out.println(contentID);
	System.out.println(pTitle);
	System.out.println(contentDuration);
	System.out.println(pBusinessType);
	System.out.println(pGenre);
	System.out.println(pAssetSubType);
	System.out.println(episode);
	System.out.println(pPublishedDate);
	System.out.println(pLanguages);
	System.out.println(pAudioLangguage);
	System.out.println(pSubTitleLangguage);
	System.out.println(pCharacters);
	System.out.println(pDRM_Video);
	System.out.println(pContentBillingType);

	// ----- Mix Panel Content Parameters Validation ------
	Mixpanel.FEProp.setProperty("Audio Language", pAudioLangguage);
	Mixpanel.FEProp.setProperty("Content ID", contentID);
	Mixpanel.FEProp.setProperty("Content Name", pTitle);
	Mixpanel.FEProp.setProperty("Content Duration", contentDuration);
	Mixpanel.FEProp.setProperty("Content Type", pBusinessType);
	Mixpanel.FEProp.setProperty("Content Specification", pAssetSubType);
	Mixpanel.FEProp.setProperty("Content Original Language", pLanguages);
	Mixpanel.FEProp.setProperty("Characters", pCharacters.toString());
	Mixpanel.FEProp.setProperty("Content Billing Type", pContentBillingType);
	Mixpanel.FEProp.setProperty("DRM Video", pDRM_Video);
	Mixpanel.FEProp.setProperty("Episode No", pEpisodeNum);
	Mixpanel.FEProp.setProperty("Genre", pGenre);
	Mixpanel.FEProp.setProperty("Publishing Date", pPublishedDate);
	Mixpanel.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);
	
}
	
public static void setPropertyForContentDetailsFromSearchPage(String contentID) {
	
	//--- Initialized Variables ---
	Response contentResp = null;
	String pCharacters = null;

	contentResp = getResponseDetails(contentID);
	System.out.println("Status Code: " + contentResp.statusCode());

	String pOriginalTitle = contentResp.jsonPath().get("original_title");
	int contentDuration = contentResp.jsonPath().get("duration");
	String pDuration = Integer.toString(contentDuration);
	String onAir = contentResp.jsonPath().get("on_air");
	String pBussinessType = contentResp.jsonPath().get("business_type");
	String pImgURL = contentResp.jsonPath().get("image_url");
	int assetType = contentResp.jsonPath().get("asset_type");
	String pContentSpecification = contentResp.jsonPath().get("asset_subtype");
	int episode = contentResp.jsonPath().get("episode_number");
	String pEpisodeNum = Integer.toString(episode);
	if (pEpisodeNum.equalsIgnoreCase("0")) {
		pEpisodeNum = "N/A";
	}
	String release_date = contentResp.jsonPath().get("release_date").toString();
	String pPublishedDate = release_date.replace(release_date.substring(10), "");
	String pContentBillingType = contentResp.jsonPath().get("billing_type");
	if (pContentBillingType.length() == 0) {
		pContentBillingType = "N/A";
	}

	// ---Getting Languages from API
	int languageCount = contentResp.jsonPath().getList("languages").size();
	String pLanguages = null;
	ArrayList<String> getLanguages = new ArrayList<String>();
	if (languageCount > 1) {
		for (int l = 0; l < languageCount; l++) {
			getLanguages.add(contentResp.jsonPath().get("languages[" + l + "]").toString());
		}
		pLanguages = getLanguages.toString();
	} else if (languageCount == 1) {
		pLanguages = contentResp.jsonPath().get("languages[0]");
	} else {
		System.out.println("Original Languages is Empty");
	}

	// ---Getting Audio Languages from API
	int audioLangCount = contentResp.jsonPath().getList("audio_languages").size();
	String pAudioLangguage = null;
	ArrayList<String> getAudioLanguages = new ArrayList<String>();
	if (audioLangCount > 1) {
		for (int a = 0; a < audioLangCount; a++) {
			getAudioLanguages.add(contentResp.jsonPath().get("audio_languages[" + a + "]").toString());
		}
		pAudioLangguage = getAudioLanguages.toString();
	} else if (audioLangCount == 1) {
		pAudioLangguage = contentResp.jsonPath().get("audio_languages[0]");
	} else {
		pAudioLangguage = "N/A";
	}

	// ---Getting Subtitle Languages from API
	int subTitleLangCount = contentResp.jsonPath().getList("subtitle_languages").size();
	String pSubTitleLangguage = null;
	ArrayList<String> getSubTitles = new ArrayList<String>();
	if (subTitleLangCount > 1) {
		for (int s = 0; s < subTitleLangCount; s++) {
			getSubTitles.add(contentResp.jsonPath().get("subtitle_languages[" + s + "]").toString());
		}
		pSubTitleLangguage = getSubTitles.toString();
	} else if (subTitleLangCount == 1) {
		pSubTitleLangguage = contentResp.jsonPath().get("subtitle_languages[0]");
	} else {
		pSubTitleLangguage = "N/A";
	}

	// ---Getting Genres from API
	int genreCount = contentResp.jsonPath().getList("genres").size();
	String pGenre = null;
	for (int i = 0; i < genreCount; i++) {
		if (genreCount == 1) {
			pGenre = contentResp.jsonPath().get("genre[" + i + "].value");
		} else {
			pGenre = pGenre + "," + contentResp.jsonPath().get("genre[" + i + "].value");
		}
	}
	pGenre = pGenre.replace("null,", "");

	// ---Getting Characters from API
	int actorsCount = contentResp.jsonPath().getList("actors").size();
	ArrayList<String> getCharacters = new ArrayList<String>();
	if (actorsCount >= 1) {
		for (int j = 0; j < actorsCount; j++) {
			getCharacters.add(contentResp.jsonPath().get("actors[" + j + "]").toString());
		}
		pCharacters = getCharacters.toString();
		pCharacters = pCharacters.replace("[", "").replace("]", "");
	} else {
//				pCharacters = getCharacters.toString();
		pCharacters = "N/A";
	}

	int isDRM = contentResp.jsonPath().get("is_drm");
	String pDRM_Video = "false";
	if (isDRM == 1) {
		pDRM_Video = "true";
	}

	System.out.println("\n--PRINTING VALIDATION PARAMETERS--");
	System.out.println(contentID);
	System.out.println(pOriginalTitle);
	System.out.println(pDuration);
	System.out.println(pBussinessType);
	System.out.println(pGenre);
	System.out.println(pContentSpecification);
	System.out.println(pEpisodeNum);
	System.out.println(pPublishedDate);
	System.out.println(pLanguages);
	System.out.println(pAudioLangguage);
	System.out.println(pSubTitleLangguage);
	System.out.println(pCharacters);
	System.out.println(pDRM_Video);
	System.out.println(pImgURL);
	System.out.println(pContentBillingType);
	System.out.println(onAir);
	System.out.println("AssetType: " + assetType);

	// ----- Mix Panel Content Parameters Validation ------
	
	MixpanelAndroid.FEProp.setProperty("Audio Language", pAudioLangguage);
	MixpanelAndroid.FEProp.setProperty("Content ID", contentID);
	MixpanelAndroid.FEProp.setProperty("Content Name", pOriginalTitle);
	MixpanelAndroid.FEProp.setProperty("Content Duration", pDuration);
	MixpanelAndroid.FEProp.setProperty("Content Type", pBussinessType);
	MixpanelAndroid.FEProp.setProperty("Content Specification", pContentSpecification);
	MixpanelAndroid.FEProp.setProperty("Content Original Language", pLanguages);
	MixpanelAndroid.FEProp.setProperty("Characters", pCharacters);
	MixpanelAndroid.FEProp.setProperty("Content Billing Type", pContentBillingType);
	MixpanelAndroid.FEProp.setProperty("DRM Video", pDRM_Video);
	MixpanelAndroid.FEProp.setProperty("Episode No", pEpisodeNum);
	MixpanelAndroid.FEProp.setProperty("Genre", pGenre);
	MixpanelAndroid.FEProp.setProperty("Image URL", pImgURL);
	MixpanelAndroid.FEProp.setProperty("Publishing Date", pPublishedDate);
	MixpanelAndroid.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);		
}
	
	public static void subscriptionDetails() {
		String url = "https://subscriptionapi.zee5.com/v1/subscription?include_all=true";
		
//		String UserType = getParameterFromXML("userType");
//		String pUsername = getParameterFromXML(UserType + "Name");
//		String pPassword = getParameterFromXML(UserType + "Password");
		
		String pUsername = "zeetest10@test.com";
		String pPassword = "123456";
		
		String bearerToken = getBearerToken(pUsername, pPassword);
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken).when().get(url);
		resp.prettyPrint();
		System.out.println("\n Subscrition related");
		Mixpanel.FEProp.setProperty("Pack Duration",resp.jsonPath().getString("[0].subscription_plan.billing_frequency")); 
		String uri = "[0].subscription_plan.";
		String id = resp.jsonPath().getString(uri+"id");
		String original_title = resp.jsonPath().getString(uri+"original_title");
		String subscription_plan_Type = resp.jsonPath().getString(uri+"subscription_plan_type");
		Mixpanel.FEProp.setProperty("Pack Selected",id+"_"+original_title+"_"+subscription_plan_Type); 
		Mixpanel.FEProp.setProperty("Cost",resp.jsonPath().getString("[0].subscription_plan.price"));
		
		Mixpanel.FEProp.setProperty("Region",resp.jsonPath().getString("[0].region"));
		Mixpanel.FEProp.setProperty("User ID",resp.jsonPath().getString("[0].user_id"));
		Mixpanel.FEProp.setProperty("Unique ID",resp.jsonPath().getString("[0].user_id"));
		Mixpanel.FEProp.setProperty("Active Plan Name",resp.jsonPath().getString(uri+"original_title"));
		Mixpanel.FEProp.setProperty("Subscription Status",resp.jsonPath().getString("[0].state"));
		
		String getUserCountry = resp.jsonPath().getString(uri+"country");
		Mixpanel.FEProp.setProperty("Country ID",getUserCountry);
		Mixpanel.FEProp.setProperty("Registring Country",getUserCountry);
		
		String pCountry = "NA";
		if(getUserCountry.equalsIgnoreCase("IN")) {
			pCountry = "INDIA";
		}
		Mixpanel.FEProp.setProperty("User Country",pCountry);
	}


	@SuppressWarnings("unused")
	public static String getPageResponse(String tabName, String typeOfContent) {
		String Uri;
		Response respCarousel = null;
		String userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		String q = null;
		String contLang = getLanguage(userType);

		PropertyFileReader handler = new PropertyFileReader("properties/MixpanelKeys.properties");
		String page = handler.getproperty(tabName.toLowerCase());

		if (page.equals("stories")) {
			Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
		} else {
			Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page
					+ "?page=1&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=10";
		}
		String xAccessToken = getXAccessTokenWithApiKey();
		if (userType.equalsIgnoreCase("Guest")) {
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", "12345").when().get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonSubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonSubscribedUserPassword");
			String bearerToken = getBearerToken(email,password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}

		if (typeOfContent.equalsIgnoreCase("Free")) {
			int itemsSize = respCarousel.jsonPath().getList("buckets[0].items").size();
			for (int k = 0; k < 6; k++) {
				if (respCarousel.jsonPath().getString("buckets[0].items[" + k + "].business_type").contains("free")
						|| respCarousel.jsonPath().getString("buckets[0].items["+k+"].business_type").contains("advertisement_downloadable") ) {
					return respCarousel.jsonPath().getString("buckets[0].items[" + k + "].title");
					
				}
			}
		} else if (typeOfContent.equalsIgnoreCase("premium")) {
			int itemsSize = respCarousel.jsonPath().getList("buckets[0].items").size();
			for (int k = 0; k < 6; k++) {
				if (respCarousel.jsonPath().getString("buckets[0].items[" + k + "].business_type")
						.contains("premium_downloadable")) {
					return respCarousel.jsonPath().getString("buckets[0].items[" + k + "].title");
				}
			}
		} else if (typeOfContent.equalsIgnoreCase("trailer")) {
			int itemsSize = respCarousel.jsonPath().getList("buckets[0].items").size();
			for (int k = 0; k < 6; k++) {
				if (respCarousel.jsonPath().getList("buckets[0].items[" + k + "].related") != null) {
					String relatedID = respCarousel.jsonPath().getString("buckets[0].items[" + k + "].id");
					Response relatedtrailerID = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/" + relatedID+ "?translation=en&country=IN&version=2");
					String trailerID = relatedtrailerID.jsonPath().getString("related[0].id");
					System.out.println("Trailer ID : " + trailerID);
					RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/" + trailerID+ "?translation=en&country=IN&version=2");
					getContentDetails(trailerID);
					return respCarousel.jsonPath().getString("buckets[0].items[" + k + "].title");
				}
			}
		}
		return "NoContent";
	}
	
	@SuppressWarnings("unused")
	public static ArrayList<String> getTrayResponse(String tabName,String typeOfContent) {
		ArrayList<String> contentAndTrayTitle = new ArrayList<>();
		String Uri;
		Response respCarousel = null;
		String userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
		String q= null;
		String contLang = getLanguage(userType);
		
		PropertyFileReader handler = new PropertyFileReader("properties/MixpanelKeys.properties");
		String page = handler.getproperty(tabName.toLowerCase());
		
		title:for (int i = 1; i < 5; i++) {
		if(page.equals("stories")) {
			Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
		}else {
			Uri = "https://gwapi.zee5.com/content/collection/0-8-"+ page+"?page="+i+"&limit=5&item_limit=20&country=IN&translation=en&languages="+contLang+"&version=10";
		}
		String xAccessToken = getXAccessTokenWithApiKey();
		if (userType.equalsIgnoreCase("Guest")) {
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", "12345").when().get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser"))
		{
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser"))
		{
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		
		if(typeOfContent.equalsIgnoreCase("Free")) {
			int bucketSize = respCarousel.jsonPath().getList("buckets").size();
			System.out.println(bucketSize);
			for (int j = 1; j < bucketSize; j++) {
				int itemsSize = respCarousel.jsonPath().getList("buckets["+j+"].items").size();
				for (int k = 0; k < itemsSize; k++) {
					if(respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].business_type").contains("free") 
							|| respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].business_type").contains("advertisement_downloadable") ) {
						logger.info("Tray name : "+respCarousel.jsonPath().getString("buckets["+j+"].title"));
						System.out.println("title : "+respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].title"));
						contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets["+j+"].title"));
						contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets["+j+"].items[" + k + "].title"));
						assetSubType = respCarousel.jsonPath().getString("buckets["+j+"].items[" + k + "].asset_subtype");
						return contentAndTrayTitle;
					}
				}
			}
		}else if(typeOfContent.equalsIgnoreCase("premium")){
			int bucketSize = respCarousel.jsonPath().getList("buckets").size();
			for (int j = 1; j < bucketSize; j++) {
				int itemsSize = respCarousel.jsonPath().getList("buckets["+j+"].items").size();
				for (int k = 0; k < itemsSize; k++) {
					System.out.println(respCarousel.jsonPath().getString("buckets["+j+"].title"));
					if(respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].business_type").contains("premium_downloadable")) {
						System.out.println("title : "+respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].title"));
						contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets["+j+"].title"));
						contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets["+j+"].items[" + k + "].title"));
						assetSubType = respCarousel.jsonPath().getString("buckets["+j+"].items[" + k + "].asset_subtype");
						return contentAndTrayTitle;
					}
				}
			}
		}else if(typeOfContent.equalsIgnoreCase("trailer")){
			int bucketSize = respCarousel.jsonPath().getList("buckets").size();
			for (int j = 1; j < bucketSize; j++) {
				int itemsSize = respCarousel.jsonPath().getList("buckets["+j+"].items").size();
				for (int k = 0; k < itemsSize; k++) {
					if(respCarousel.jsonPath().getList("buckets["+j+"].items["+k+"].related")  != null 
							&& !respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].related").contains("false")) {
//						respCarousel.print();
						String relatedID = respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].id");
						Response relatedtrailerID =  RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/"+relatedID+"?translation=en&country=IN&version=2");
//						relatedtrailerID.print();
						String trailerID = relatedtrailerID.jsonPath().getString("related[0].id");
						getContentDetails(trailerID);
						contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets["+j+"].title"));
						contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets["+j+"].items[" + k + "].title"));
						assetSubType = respCarousel.jsonPath().getString("buckets["+j+"].items[" + k + "].asset_subtype");
						return contentAndTrayTitle;
					}
				}
			}
		}
	}
		return contentAndTrayTitle ;
}
	
	
	public static String getParameterFromXML(String parameter) {
		return Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(parameter);
	}
	
	public static String fetchContentIDFromUrl(String url) {
		String contentID = null;
		String[] id = url.split("/");
		for (int i = 0; i < id.length; i++) {
			if (id[i].contains("0-")) {
				contentID = id[i];
			}
		}
		return contentID;
	}
	

	public static Response getResponseForPagesTv(String tabName, String contLang, int q, String userType) {
		Response respCarousel = null;
		String Uri = "";

		PropertyFileReader handler = new PropertyFileReader("properties/MixpanelKeys.properties");
		String page = handler.getproperty(("tv_" + tabName).toLowerCase());

		if (page.equals("stories")) {
			Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
		} else {
			Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page + "?page=" + q
					+ "&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=8";
		}

		String xAccessToken = getXAccessToken();
		if (userType.equalsIgnoreCase("Guest")) {
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", "12345").when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");

			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when()
					.get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}
		return respCarousel;
	}

	public static ArrayList<String> getTrayResponseTv(String tabName, String typeOfContent) {
		ArrayList<String> contentAndTrayTitle = new ArrayList<>();
		String Uri;
		Response respCarousel = null;
		String userType = "Guest";
		String q = null;
		String contLang = getLanguage(userType);

		PropertyFileReader handler = new PropertyFileReader("properties/MixpanelKeys.properties");
		String page = handler.getproperty(("tv_" + tabName).toLowerCase());

		title: for (int i = 1; i < 5; i++) {
			if (page.equals("stories")) {
				Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
			} else {
				Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page + "?page=" + i
						+ "&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=10";
			}
			String xAccessToken = getXAccessTokenWithApiKey();
			if (userType.equalsIgnoreCase("Guest")) {
				respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", "12345")
						.when().get(Uri);
			} else if (userType.equalsIgnoreCase("SubscribedUser")) {
				String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
						.getParameter("SubscribedUserName");
				String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
						.getParameter("SubscribedPassword");
				String bearerToken = getBearerToken(email, password);
				respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken)
						.when().get(Uri);
			} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {

				String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
						.getParameter("NonsubscribedUserName");
				String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
						.getParameter("NonsubscribedPassword");
//			String email="tvautomation@gmail.com";
//			 String password ="123456";
				String bearerToken = getBearerToken(email, password);
				respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken)
						.when().get(Uri);
			} else {
				System.out.println("Incorrect user type passed to method");
			}

			if (typeOfContent.equalsIgnoreCase("Free")) {

				int bucketSize = respCarousel.jsonPath().getList("buckets").size();
				// System.out.println(bucketSize);
				for (int j = 1; j < bucketSize; j++) {
					int itemsSize = respCarousel.jsonPath().getList("buckets[" + j + "].items").size();
					for (int k = 0; k < itemsSize; k++) {
						// System.out.println(respCarousel.jsonPath().getString("buckets["+j+"].title"));
						if (respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].business_type")
								.contains("free")) {
							// System.out.println("title :
							// "+respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].title"));
							contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
							contentAndTrayTitle.add(
									respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
							contentAndTrayTitle
									.add(respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].id"));
							return contentAndTrayTitle;
						}
					}
				}
			} else if (typeOfContent.equalsIgnoreCase("premium")) {
				int bucketSize = respCarousel.jsonPath().getList("buckets").size();
				for (int j = 1; j < bucketSize; j++) {
					int itemsSize = respCarousel.jsonPath().getList("buckets[" + j + "].items").size();
					for (int k = 0; k < itemsSize; k++) {
						// System.out.println(respCarousel.jsonPath().getString("buckets["+j+"].title"));
						if (respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].asset_subtype")
								.equals("movie")) {
							if (respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].business_type")
									.contains("premium_downloadable")) {
								// System.out.println("title :
								// "+respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].title"));
								contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
								contentAndTrayTitle.add(
										respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
								contentAndTrayTitle.add(
										respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].id"));
								return contentAndTrayTitle;
							}
						}
					}
				}

			} else if (typeOfContent.equals("carousel")) {
				int bucketSize = respCarousel.jsonPath().getList("buckets").size();
				// System.out.println(bucketSize);
				for (int j = 0; j < bucketSize; j++) {
					int itemsSize = respCarousel.jsonPath().getList("buckets[" + j + "].items").size();
					for (int k = 0; k < itemsSize; k++) {
						// System.out.println(respCarousel.jsonPath().getString("buckets["+j+"].title"));
						if (respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].business_type")
								.contains("premium_downloadable")) {
							// System.out.println("title :
							// "+respCarousel.jsonPath().getString("buckets["+j+"].items["+k+"].title"));
							contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
							contentAndTrayTitle.add(
									respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
							contentAndTrayTitle
									.add(respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].id"));
							return contentAndTrayTitle;
						}
					}
				}
			} else if (typeOfContent.equalsIgnoreCase("trailer")) {
				int bucketSize = respCarousel.jsonPath().getList("buckets").size();
				for (int j = 1; j < bucketSize; j++) {
					int itemsSize = respCarousel.jsonPath().getList("buckets[" + j + "].items").size();
					for (int k = 0; k < itemsSize; k++) {
						if (respCarousel.jsonPath().getList("buckets[" + j + "].items[" + k + "].related") != null) {
							if (respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].asset_subtype")
									.equals("movie")) {
								String relatedID = respCarousel.jsonPath()
										.getString("buckets[" + j + "].items[" + k + "].id");
								System.out.println("Related id :" + relatedID);
								String contentName = respCarousel.jsonPath()
										.getString("buckets[" + j + "].items[" + k + "].title");
								contentAndTrayTitle.add(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
								contentAndTrayTitle.add(
										respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
								System.out.println("Related contentName :" + contentName);
								Response relatedtrailerID = RestAssured.given()
										.headers("x-access-token", getXAccessTokenWithApiKey()).when()
										.get("https://gwapi.zee5.com/content/details/" + relatedID
												+ "?translation=en&country=IN&version=2");
								for (int l = 0; l <= 10; l++) {
									String trailercontentName = relatedtrailerID.jsonPath()
											.getString("related[" + l + "].title");
									System.out.println("Trailer contentName : " + trailercontentName);
									if (trailercontentName.contains("Trailer")) {
										String trailerID = relatedtrailerID.jsonPath()
												.getString("related[" + l + "].id");

										System.out.println("Trailer ID : " + trailerID);
										RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
												.get("https://gwapi.zee5.com/content/details/" + trailerID
														+ "?translation=en&country=IN&version=2");
										getContentDetails(trailerID);
										return contentAndTrayTitle;
									}
									break;
								}

							}
						}
					}
				}
			}
		}
		return contentAndTrayTitle;
	}

	public static String getSearchresponseTv(String searchText) {
		Response resp = RestAssured.given().urlEncodingEnabled(false).when().get("https://gwapi.zee5.com/content/search_all?q="
				+ searchText
				+ "&limit=10&asset_type=0,6,1&country=IN&languages=hi,en,mr,te,kn,ta,ml,bn,gu,pa,hr,or&translation=en&version=3&");

		String title = resp.jsonPath().getString("results[0].items[0].title");
		searchContentID = resp.jsonPath().getString("results[0].items[0].id");
		assetSubType = resp.jsonPath().getString("results[0].items[0].asset_subtype");
		getContentDetails(searchContentID);
		return searchContentID;
	}
	
	public static String getRailNameFromPage(String pTabName, String pUserType) {
		
		String tabName = pTabName;
		String assettype="",pContentID="",contentName="",pTrayName="",setContentType = "0";
		String guestTrayName = "Top Free Movies";
		
		String pContentLang = getContentLanguageForAppMixpanel(pUserType);
		System.out.println("CONTENT LANGUAGES: "+pContentLang);
		
		resp=ResponseInstance.getResponseForAppPages(tabName,pContentLang,pUserType);
		
		if(tabName.equalsIgnoreCase("premium")) {
			setContentType = "8";
		}else if(tabName.equalsIgnoreCase("Live TV")|| tabName.equalsIgnoreCase("livetv")){
			setContentType = "9";
		}else if(tabName.equalsIgnoreCase("ZEE5 Originals")|| tabName.equalsIgnoreCase("ZEEOriginals")){
			setContentType = "6";
		}
		
		if(tabName.equalsIgnoreCase("live tv") || tabName.equalsIgnoreCase("livetv")) {
			int totalCollection = resp.jsonPath().getList("items").size();
			for (int i = 0; i < totalCollection; i++) {
				int totalItems = resp.jsonPath().getList("items[" + i + "].items").size();
				if(totalItems > 0) {
					assettype = resp.jsonPath().get("items[" + i + "].items[0].asset_type").toString();
					pTrayName = resp.jsonPath().get("items[" + i + "].title").toString();
					if (assettype.equals(setContentType)) {
						pContentID = resp.jsonPath().get("items[" + i + "].items[0].id").toString();
						contentName = resp.jsonPath().get("items[" + i + "].items[0].title").toString();
						
						System.out.println("\nTrayName: " + pTrayName);
						System.out.println("ContentID: " + pContentID);
						System.out.println("ContentName: " + contentName);
						break;
					}	
				}
			}
		}else if(tabName.equalsIgnoreCase("premium") ||tabName.equalsIgnoreCase("ZEE5 Originals")|| tabName.equalsIgnoreCase("ZEEOriginals")){
			for (int i = 1; i < 5; i++) {
				if(tabName.equalsIgnoreCase("premium")) {
					assettype = resp.jsonPath().get("buckets[" + i + "].asset_type").toString();
				}else {
					assettype = resp.jsonPath().get("buckets[" + i + "].items[0].asset_type").toString();
				}
				pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
				if (assettype.equals(setContentType)) {
					pContentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
					contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
					
					System.out.println("\nTrayName: " + pTrayName);
					System.out.println("ContentID: " + pContentID);
					System.out.println("ContentName: " + contentName);
					break;
				}
			}
			Response contentResp = getContentDetails(pContentID, "original");
			int seasonCount = contentResp.jsonPath().getList("seasons").size();
			
			if(pUserType.equalsIgnoreCase("guest") && tabName.equalsIgnoreCase("ZEE5 Originals") && (seasonCount> 1)) {
				pContentID = contentResp.jsonPath().get("seasons[0].episodes[0].id");
			}else if(pUserType.equalsIgnoreCase("guest") && (seasonCount> 1)) {
				pContentID = contentResp.jsonPath().get("seasons[0].trailers[0].id");
			}else if(seasonCount!=0){	
				pContentID = contentResp.jsonPath().get("seasons[0].episodes[0].id");
			}else {
				System.out.println("\nNO EPISODES...");
			}
			
		}else if(tabName.equalsIgnoreCase("home")){
			boolean freeContentCard = false;
			for (int i = 1; i < 5; i++) {
				pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
				System.out.println(pTrayName);
				if(pTrayName.contains(guestTrayName)) {
					freeContentCard=true;
					int totalItems = resp.jsonPath().getList("buckets[" + i + "].items").size();
					if(totalItems > 0) {
						pContentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
						contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
						
						System.out.println("\nTrayName: " + pTrayName);
						System.out.println("ContentID: " + pContentID);
						System.out.println("ContentName: " + contentName);
						break;
						}	
					}
				}
				if(!freeContentCard) {
					for (int i = 1; i < 5; i++) {
						assettype = resp.jsonPath().get("buckets[" + i + "].items[0].asset_type").toString();
						pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
						if (assettype.equals("0") || assettype.equals("1")) {
							pContentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
							contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
							
							System.out.println("\nTrayName: " + pTrayName);
							System.out.println("ContentID: " + pContentID);
							System.out.println("ContentName: " + contentName);
							break;
						}
					}
				}
		}else if(pUserType.equalsIgnoreCase("Guest") && tabName.equalsIgnoreCase("movies")){
			boolean freeContentCard = false;
			for (int i = 1; i < 5; i++) {
				pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
				System.out.println(pTrayName);
				if(pTrayName.contains(guestTrayName)) {
					freeContentCard=true;
					int totalItems = resp.jsonPath().getList("buckets[" + i + "].items").size();
					if(totalItems > 0) {
						pContentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
						contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
						
						System.out.println("\nTrayName: " + pTrayName);
						System.out.println("ContentID: " + pContentID);
						System.out.println("ContentName: " + contentName);
						break;
						}	
					}
				}
				if(!freeContentCard) {
					for (int i = 1; i < 5; i++) {
						assettype = resp.jsonPath().get("buckets[" + i + "].items[0].asset_type").toString();
						pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
						if (assettype.equals("0") || assettype.equals("1")) {
							pContentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
							contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
							
							System.out.println("\nTrayName: " + pTrayName);
							System.out.println("ContentID: " + pContentID);
							System.out.println("ContentName: " + contentName);
							break;
						}
					}
				}
		}else {
			for (int i = 1; i < 5; i++) {
				assettype = resp.jsonPath().get("buckets[" + i + "].items[0].asset_type").toString();
				pTrayName = resp.jsonPath().get("buckets[" + i + "].title").toString();
				if (assettype.equals("0") || assettype.equals("1")) {
					pContentID = resp.jsonPath().get("buckets[" + i + "].items[0].id").toString();
					contentName = resp.jsonPath().get("buckets[" + i + "].items[0].title").toString();
					
					System.out.println("\nTrayName: " + pTrayName);
					System.out.println("ContentID: " + pContentID);
					System.out.println("ContentName: " + contentName);
					break;
				}
			}
		}
		
		switch (tabName.toLowerCase()) {	
			case "live tv":
				setFEPropertyOfLIVETVCardDetails(pTrayName, pContentID);
				break;

			default:
				setFEPropertyOfContentCardDetails(pTrayName, pContentID);
				break;
		}
		return pTrayName;
	}
	
	
public static void setFEPropertyOfContentCardDetails(String pTrayName, String pContentID) {
		
		//--- Initialized Variables ---
		Response contentResp = null;
		String pCharacters = null;
		
		contentResp = getResponseDetails(pContentID);
		System.out.println("Status Code: " + contentResp.statusCode());

		String pOriginalTitle = contentResp.jsonPath().get("original_title");
		int contentDuration = contentResp.jsonPath().get("duration");
		String pDuration = Integer.toString(contentDuration);
		String onAir = contentResp.jsonPath().get("on_air");
		String pBussinessType = contentResp.jsonPath().get("business_type");
		String pImgURL = contentResp.jsonPath().get("image_url");
		int assetType = contentResp.jsonPath().get("asset_type");
		String pContentSpecification = contentResp.jsonPath().get("asset_subtype");
		int episode = contentResp.jsonPath().get("episode_number");
		String pEpisodeNum = Integer.toString(episode);
		if(pEpisodeNum.equalsIgnoreCase("0")) {
			pEpisodeNum = "N/A";
		}
		String release_date = contentResp.jsonPath().get("release_date").toString();
		String pPublishedDate = release_date.replace(release_date.substring(10), "");
		String pContentBillingType = contentResp.jsonPath().get("billing_type");
		if (pContentBillingType.length() == 0) {
			pContentBillingType = "N/A";
		}

		//---Getting Languages from API
		int languageCount = contentResp.jsonPath().getList("languages").size();
		String pLanguages = null;
		ArrayList<String> getLanguages = new ArrayList<String>();
		if (languageCount > 1) {
			for (int l = 0; l < languageCount; l++) {
				getLanguages.add(contentResp.jsonPath().get("languages[" + l + "]").toString());
			}
			pLanguages = getLanguages.toString();
		} else if (languageCount == 1) {
			pLanguages = contentResp.jsonPath().get("languages[0]");
		} else {
			System.out.println("Original Languages is Empty");
		}

		//---Getting Audio Languages from API
		int audioLangCount = contentResp.jsonPath().getList("audio_languages").size();
		String pAudioLangguage = null;
		ArrayList<String> getAudioLanguages = new ArrayList<String>();
		if (audioLangCount > 1) {
			for (int a = 0; a < audioLangCount; a++) {
				getAudioLanguages.add(contentResp.jsonPath().get("audio_languages[" + a + "]").toString());
			}
			pAudioLangguage = getAudioLanguages.toString();
		} else if (audioLangCount == 1) {
			pAudioLangguage = contentResp.jsonPath().get("audio_languages[0]");
		} else {
			pAudioLangguage = "N/A";
		}

		//---Getting Subtitle Languages from API
		int subTitleLangCount = contentResp.jsonPath().getList("subtitle_languages").size();
		String pSubTitleLangguage = null;
		ArrayList<String> getSubTitles = new ArrayList<String>();
		if (subTitleLangCount > 1) {
			for (int s = 0; s < subTitleLangCount; s++) {
				getSubTitles.add(contentResp.jsonPath().get("subtitle_languages[" + s + "]").toString());
			}
			pSubTitleLangguage = getSubTitles.toString();
		} else if (subTitleLangCount == 1) {
			pSubTitleLangguage = contentResp.jsonPath().get("subtitle_languages[0]");
		} else {
			pSubTitleLangguage = "N/A";
		}

		//---Getting Genres from API
		int genreCount = contentResp.jsonPath().getList("genres").size();
		String pGenre = null;
		for (int i = 0; i < genreCount; i++) {
			if (genreCount == 1) {
				pGenre = contentResp.jsonPath().get("genre[" + i + "].value");
			} else {
				pGenre = pGenre + "," + contentResp.jsonPath().get("genre[" + i + "].value");
			}
		}
		pGenre = pGenre.replace("null,", "");

		//---Getting Characters from API
		int actorsCount = contentResp.jsonPath().getList("actors").size();
		ArrayList<String> getCharacters = new ArrayList<String>();
		if (actorsCount >= 1) {
			for (int j = 0; j < actorsCount; j++) {
				getCharacters.add(contentResp.jsonPath().get("actors[" + j + "]").toString());
			}
			pCharacters = getCharacters.toString();
			pCharacters = pCharacters.replace("[", "").replace("]", "");
		} else {
			pCharacters = "N/A";
		}
		

		int isDRM = contentResp.jsonPath().get("is_drm");
		String pDRM_Video = "false";
		if (isDRM == 1) {
			pDRM_Video = "true";
		}

		System.out.println("\n--PRINTING VALIDATION PARAMETERS--");
		System.out.println(pTrayName);
		System.out.println(pContentID);
		System.out.println(pOriginalTitle); 
		System.out.println(pDuration);
		System.out.println(pBussinessType);
		System.out.println(pGenre);
		System.out.println(pContentSpecification);
		System.out.println(pEpisodeNum);
		System.out.println(pPublishedDate);
		System.out.println(pLanguages);
		System.out.println(pAudioLangguage);
		System.out.println(pSubTitleLangguage);
		System.out.println(pCharacters);
		System.out.println(pDRM_Video);
		System.out.println(pImgURL);
		System.out.println(pContentBillingType);
		System.out.println(onAir);
		System.out.println("AssetType: "+assetType);
		
		// ----- Mix Panel Content Parameters Validation ------
		MixpanelAndroid.FEProp.setProperty("Audio Language", pAudioLangguage);
		MixpanelAndroid.FEProp.setProperty("Carousal Name", "N/A");
		MixpanelAndroid.FEProp.setProperty("Content ID", pContentID);
		MixpanelAndroid.FEProp.setProperty("Content Name", pOriginalTitle);
		MixpanelAndroid.FEProp.setProperty("Content Duration", pDuration);
		MixpanelAndroid.FEProp.setProperty("Content Type", pBussinessType);
		MixpanelAndroid.FEProp.setProperty("Content Specification", pContentSpecification);
		MixpanelAndroid.FEProp.setProperty("Content Original Language", pLanguages);
		MixpanelAndroid.FEProp.setProperty("Characters", pCharacters);
		MixpanelAndroid.FEProp.setProperty("Content Billing Type", pContentBillingType);
		MixpanelAndroid.FEProp.setProperty("DRM Video", pDRM_Video);
		MixpanelAndroid.FEProp.setProperty("Episode No", pEpisodeNum);
		MixpanelAndroid.FEProp.setProperty("Genre", pGenre);
		MixpanelAndroid.FEProp.setProperty("Image URL", pImgURL);
		MixpanelAndroid.FEProp.setProperty("Publishing Date", pPublishedDate);
		MixpanelAndroid.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);
	}
	
public static void setFEPropertyOfLIVETVCardDetails(String pTrayName, String pContentID) {
	
	//--- Initialized Variables ---
	Response contentResp = null;
	String pCharacters = "N/A", pPublishedDate = "N/A";
	
	contentResp = getResponseDetails(pContentID);
	System.out.println("Status Code: " + contentResp.getStatusCode());
	
	int getDuration = contentResp.jsonPath().get("duration");
	String pDuration = Integer.toString(getDuration);
	if(pDuration.equalsIgnoreCase("0")) {
		pDuration = "N/A";
	}
	String onAir = contentResp.jsonPath().get("on_air");
	String pBusinessType = contentResp.jsonPath().get("business_type");
	int assetType = contentResp.jsonPath().get("asset_type");
	String pImgURL = contentResp.jsonPath().get("image_url");
	int getEpisodeNumber = contentResp.jsonPath().get("episode_number");
	String pEpisodeNumber = Integer.toString(getEpisodeNumber);
	if(pEpisodeNumber.equalsIgnoreCase("0")) {
		pEpisodeNumber = "N/A";
	}
	String pTitle = contentResp.jsonPath().get("title");
	String pContentBillingType = contentResp.jsonPath().get("billing_type");
	if (pContentBillingType.length() == 0) {
		pContentBillingType = "N/A";
	}

	//---Getting Languages from API
	int languageCount = contentResp.jsonPath().getList("languages").size();
	String pLanguages = null;
	ArrayList<String> getLanguages = new ArrayList<String>();
	if (languageCount > 1) {
		for (int l = 0; l < languageCount; l++) {
			getLanguages.add(contentResp.jsonPath().get("languages[" + l + "]").toString());
		}
		pLanguages = getLanguages.toString();
	} else if (languageCount == 1) {
		pLanguages = contentResp.jsonPath().get("languages[0]");
	} else {
		System.out.println("Original Languages is Empty");
	}

	//---Getting Genres from API
	int genreSize = contentResp.jsonPath().getList("genre").size();
	String pGenre = null;
	for (int i = 0; i < genreSize; i++) {
		if (genreSize == 1) {
			pGenre = contentResp.jsonPath().get("genre[" + i + "].value");
		} else {
			pGenre = pGenre + "," + contentResp.jsonPath().get("genre[" + i + "].value");
		}
	}
	pGenre = pGenre.replace("null,", "");

	//---Getting Audio Languages from API
	int audioLangCount = contentResp.jsonPath().getList("audio_languages").size();
	String pAudioLangguage = null;
	ArrayList<String> getAudioLanguages = new ArrayList<String>();
	if (audioLangCount > 1) {
		for (int a = 0; a < audioLangCount; a++) {
			getAudioLanguages.add(contentResp.jsonPath().get("audio_languages[" + a + "]").toString());
		}
		pAudioLangguage = getAudioLanguages.toString();
	} else if (audioLangCount == 1) {
		pAudioLangguage = contentResp.jsonPath().get("audio_languages[0]");
	} else {
		pAudioLangguage = "NA";
	}

	//---Getting Subtitle Languages from API
	int subTitleLangCount = contentResp.jsonPath().getList("subtitle_languages").size();
	String pSubTitleLangguage = null;
	ArrayList<String> getSubTitles = new ArrayList<String>();
	if (subTitleLangCount > 1) {
		for (int s = 0; s < subTitleLangCount; s++) {
			getSubTitles.add(contentResp.jsonPath().get("subtitle_languages[" + s + "]").toString());
		}
		pSubTitleLangguage = getSubTitles.toString();
	} else if (subTitleLangCount == 1) {
		pSubTitleLangguage = contentResp.jsonPath().get("subtitle_languages[0]");
	} else {
		pSubTitleLangguage = "N/A";
	}

	int isDRM = contentResp.jsonPath().get("is_drm");
	String pDRM_Video = "false";
	if (isDRM == 1) {
		pDRM_Video = "true";
	}
	

	System.out.println("\n--PRINTING VALIDATION PARAMETERS--");
	System.out.println(pTrayName);
	System.out.println(pContentID);
	System.out.println(pTitle);
	System.out.println(pDuration);
	System.out.println(pBusinessType);
	System.out.println(pGenre);
	System.out.println(pEpisodeNumber);
	System.out.println(pLanguages);
	System.out.println(pAudioLangguage);
	System.out.println(pSubTitleLangguage);
	System.out.println(pCharacters);
	System.out.println(pDRM_Video);
	System.out.println(pImgURL);
	System.out.println(pContentBillingType);
	System.out.println(pPublishedDate);
	System.out.println(onAir);
	System.out.println("AssetType: " + assetType);
	
	// ----- Mix Panel Content Parameters Validation ------
	MixpanelAndroid.FEProp.setProperty("Audio Language", pAudioLangguage);
	MixpanelAndroid.FEProp.setProperty("Carousal Name", "N/A");
	MixpanelAndroid.FEProp.setProperty("Content ID", pContentID);
	MixpanelAndroid.FEProp.setProperty("Content Name", pTitle);
	MixpanelAndroid.FEProp.setProperty("Content Duration", pDuration);
	MixpanelAndroid.FEProp.setProperty("Content Type", pBusinessType);
	MixpanelAndroid.FEProp.setProperty("Content Original Language", pLanguages);
	MixpanelAndroid.FEProp.setProperty("Characters", pCharacters);
	MixpanelAndroid.FEProp.setProperty("Content Billing Type", pContentBillingType);
	MixpanelAndroid.FEProp.setProperty("DRM Video", pDRM_Video);
	MixpanelAndroid.FEProp.setProperty("Episode No", pEpisodeNumber);
	MixpanelAndroid.FEProp.setProperty("Genre", pGenre);
	MixpanelAndroid.FEProp.setProperty("Image URL", pImgURL);
	MixpanelAndroid.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);
	MixpanelAndroid.FEProp.setProperty("Publishing Date", pPublishedDate);
}
	
public static void setSubscriptionDetails() {
		
		String url = "https://subscriptionapi.zee5.com/v1/subscription?include_all=true";		
		String pUsername = getParameterFromXML("SubscribedUserName");
		String pPassword = getParameterFromXML("SubscribedPassword");
//		String pUsername = "clubRK@g.com";
//		String pPassword = "123456";
		
		String bearerToken = getBearerToken(pUsername, pPassword);
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken).when().get(url);
		
		System.out.println("\nSubscrition related");
		Mixpanel.FEProp.setProperty("Pack Duration",resp.jsonPath().getString("[0].subscription_plan.billing_frequency")); 
		String uri = "[0].subscription_plan.";
		String id = resp.jsonPath().getString(uri+"id");
		String original_title = resp.jsonPath().getString(uri+"original_title");
		String subscription_plan_Type = resp.jsonPath().getString(uri+"subscription_plan_type");
		
		Mixpanel.FEProp.setProperty("Pack Selected",id+"_"+original_title+"_"+subscription_plan_Type); 
		Mixpanel.FEProp.setProperty("Cost",resp.jsonPath().getString("[0].subscription_plan.price"));
		Mixpanel.FEProp.setProperty("region",resp.jsonPath().getString("[0].region"));
		Mixpanel.FEProp.setProperty("user_id",resp.jsonPath().getString("[0].user_id"));
		Mixpanel.FEProp.setProperty("Unique ID",resp.jsonPath().getString("[0].user_id"));
		Mixpanel.FEProp.setProperty("Active Plan Name",resp.jsonPath().getString(uri+"original_title"));
		Mixpanel.FEProp.setProperty("Subscription Status",resp.jsonPath().getString("[0].state"));
		
		String getUserCountry = resp.jsonPath().getString(uri+"country");
		Mixpanel.FEProp.setProperty("Country ID",getUserCountry);
		Mixpanel.FEProp.setProperty("Registring Country",getUserCountry);
		
		String pCountry = "NA";
		if(getUserCountry.equalsIgnoreCase("IN")) {
			pCountry = "INDIA";
		}
		Mixpanel.FEProp.setProperty("Country",pCountry);
		Mixpanel.FEProp.setProperty("State",resp.jsonPath().getString("[0].region").toUpperCase());
		
	}

public static void getUserSettingsValues(String pUsername, String pPassword) {
	if (!pUsername.equals("")) {
		String bearerToken = getBearerToken(pUsername, pPassword);
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken)
				.when().get("https://userapi.zee5.com/v1/settings");

		String[] comm = resp.asString().replace("{", "").replace("}", "").replace("[", "").replace("]", "")
				.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
		String value = null;
		for (int i = 0; i < comm.length;) {
			String key = comm[i].replace("\"", "").split("y:")[1];
			try {
				value = comm[i + 1].replace("\"", "").split("e:")[1];
			} catch (Exception e) {
				value = "Empty";
			}
			if (value.contains("age_rating")) {
				key = "Parent Control Setting";
				value = value.replace("\\", "").split(":")[1].replaceAll(" ", "").replace(",pin", "");
			}
			if(key.equalsIgnoreCase("content_language")) {
				Mixpanel.FEProp.setProperty("New Content Language", "["+value.replaceAll(",", "-")+"]");
			}else if(key.equalsIgnoreCase("display_language")) {
				Mixpanel.FEProp.setProperty("New App Language", value);
			}else if(key.equalsIgnoreCase("eduauraaClaimed")) {
//				Mixpanel.FEProp.setProperty("", value);
			}
			
			Mixpanel.FEProp.setProperty(key, value);
			i = i + 2;
		}
	}
	Mixpanel.FEProp.forEach((key, value) -> System.out.println(key + " : " + value));
	}

public static ArrayList<String> getTrayNameAndContentID(String tabName, String typeOfContent) {
	ArrayList<String> listOfContentDetails = new ArrayList<>();
	String Uri;
	Response respCarousel = null;
	String userType = getParameterFromXML("userType");
//	String userType = "Guest";

	String contLang = getLanguage(userType);
	String page = getTabNameForAPI(tabName);

	for (int i = 1; i < 5; i++) {
		if (page.equals("stories")) {
			Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
		} else {
			Uri = "https://gwapi.zee5.com/content/collection/0-8-" + page + "?page=" + i
					+ "&limit=5&item_limit=20&country=IN&translation=en&languages=" + contLang + "&version=10";
		}
		String xAccessToken = getXAccessTokenWithApiKey();
		if (userType.equalsIgnoreCase("Guest")) {
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("x-z5-guest-token", "12345")
					.when().get(Uri);
		} else if (userType.equalsIgnoreCase("SubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken)
					.when().get(Uri);
		} else if (userType.equalsIgnoreCase("NonSubscribedUser")) {
			String email = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedUserName");
			String password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonsubscribedPassword");
			String bearerToken = getBearerToken(email, password);
			respCarousel = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken)
					.when().get(Uri);
		} else {
			System.out.println("Incorrect user type passed to method");
		}

		if (typeOfContent.equalsIgnoreCase("Free")) {
			int bucketSize = respCarousel.jsonPath().getList("buckets").size();
			System.out.println(bucketSize);
			for (int j = 1; j < bucketSize; j++) {
				int itemsSize = respCarousel.jsonPath().getList("buckets[" + j + "].items").size();
				for (int k = 0; k < itemsSize; k++) {
					if (respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].business_type")
							.contains("free")
							|| respCarousel.jsonPath()
									.getString("buckets[" + j + "].items[" + k + "].business_type")
									.contains("advertisement_downloadable")) {
						logger.info("Tray name : " + respCarousel.jsonPath().getString("buckets[" + j + "].title"));
						System.out.println("title : "
								+ respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
						listOfContentDetails.add(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
						listOfContentDetails.add(
								respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
						listOfContentDetails
								.add(respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].id"));
						assetSubType = respCarousel.jsonPath()
								.getString("buckets[" + j + "].items[" + k + "].asset_subtype");
						return listOfContentDetails;
					}
				}
			}
		} else if (typeOfContent.equalsIgnoreCase("premium")) {
			int bucketSize = respCarousel.jsonPath().getList("buckets").size();
			for (int j = 1; j < bucketSize; j++) {
				int itemsSize = respCarousel.jsonPath().getList("buckets[" + j + "].items").size();
				for (int k = 0; k < itemsSize; k++) {
					System.out.println(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
					if (respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].business_type")
							.contains("premium_downloadable")) {
						System.out.println("title : "
								+ respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
						listOfContentDetails.add(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
						listOfContentDetails.add(
								respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
						listOfContentDetails
								.add(respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].id"));
						assetSubType = respCarousel.jsonPath()
								.getString("buckets[" + j + "].items[" + k + "].asset_subtype");
						return listOfContentDetails;
					}
				}
			}
		} else if (typeOfContent.equalsIgnoreCase("trailer")) {
			int bucketSize = respCarousel.jsonPath().getList("buckets").size();
			for (int j = 1; j < bucketSize; j++) {
				int itemsSize = respCarousel.jsonPath().getList("buckets[" + j + "].items").size();
				for (int k = 0; k < itemsSize; k++) {
					if (respCarousel.jsonPath().getList("buckets[" + j + "].items[" + k + "].related") != null
							&& !respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].related")
									.contains("false")) {
//				respCarousel.print();
						String relatedID = respCarousel.jsonPath()
								.getString("buckets[" + j + "].items[" + k + "].id");
						Response relatedtrailerID = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey())
								.when().get("https://gwapi.zee5.com/content/details/" + relatedID
										+ "?translation=en&country=IN&version=2");
//				relatedtrailerID.print();
						String trailerID = relatedtrailerID.jsonPath().getString("related[0].id");
						getContentDetails(trailerID);
						listOfContentDetails.add(respCarousel.jsonPath().getString("buckets[" + j + "].title"));
						listOfContentDetails.add(
								respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].title"));
						listOfContentDetails
								.add(respCarousel.jsonPath().getString("buckets[" + j + "].items[" + k + "].id"));
						assetSubType = respCarousel.jsonPath()
								.getString("buckets[" + j + "].items[" + k + "].asset_subtype");
						return listOfContentDetails;
					}
				}
			}
		}
	}
	return listOfContentDetails;
}

public static String getTabNameForAPI(String pageName) {

	switch (pageName.toLowerCase()) {
	case "home":
		pageName = "homepage";
		break;

	case "shows":
		pageName = "tvshows";
		break;

	case "movies":
		pageName = "movies";
		break;

	case "premium":
		pageName = "premiumcontents";
		break;

	case "news":
		pageName = "5857";
		break;

	case "eduauraa":
		pageName = "6184";
		break;

	case "music":
		pageName = "2707";
		break;

	case "zeeoriginals":
		pageName = "zeeoriginals";
		break;

	case "zee5 originals":
		pageName = "zeeoriginals";
		break;

	default:
		pageName = "homepage";
		System.out.println("\n*** INVALID TAB NAME PASSED!!! ***\n");
		break;
	}
	
	return pageName;
}

public static Response getSubscriptionDetails(String username, String password) {
	String url = "https://subscriptionapi.zee5.com/v1/subscription?translation=en&country=IN&include_all=true";
	Response response = null;
	String b = getBearerToken(username, password);
	// System.out.println("\nbearer token :"+b+"\n");
	response = RestAssured.given().headers("Authorization", b).when().get(url);
	// System.out.println("\nresponse body: "+response.getBody().asString());
	return response;
}

public static Response getSettingsDetails(String username, String password) {
	String url = "https://userapi.zee5.com/v1/settings";
	Response response = null;
	String b = getBearerToken(username, password);
	// System.out.println("\nbearer token :"+b+"\n");
	response = RestAssured.given().headers("Authorization", b).when().get(url);
	// System.out.println("\nresponse body: "+response.getBody().asString());
	return response;
}

public static Response getTVODDetails(String username, String password) {
	String url = "https://subscriptionapi.zee5.com/v1/purchase";
	Response response = null;
	String b = getBearerToken(username, password);
	// System.out.println("\nbearer token :"+b+"\n");
	response = RestAssured.given().headers("Authorization", b).when().get(url);
	// System.out.println("\nresponse body: "+response.getBody().asString());
	return response;
}

public static void getContentDetailsForCarouselClick(String tab, int carousalDot, String contentLanguages) {
	String userType = "", username = "", password = "";
	userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
	if (!userType.equalsIgnoreCase("Guest")) {
		if (userType.equals("NonSubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonSubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("NonSubscribedUserPassword");
		}
		if (userType.equals("SubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
					.getParameter("SubscribedPassword");
		}
		Response userDetails = getUserSettingDetails(username, password);
		int keyCount = userDetails.jsonPath().get("key.size()");
		for (int i = 0; i < keyCount; i++) {
			if (userDetails.jsonPath().get("key[" + i + "]").toString().equals("content_language")) {
				contentLanguages = userDetails.jsonPath().get("value[" + i + "]").toString();
				break;
			}
		}
	}
	Response respContent = ResponseInstance.getResponseForPages(tab.toLowerCase(), contentLanguages);
	String id = "", name = "", type = "", genre = "", duration = "", specification = "";
	String subtitleLang = "", originalLang = "", characters = "", drm = "";
	try {
		id = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].id").toString();
	} catch (Exception e) {
		id = null;
	}
	if (id == null)
		id = "N/A";
	try {
		name = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].title").toString();
	} catch (Exception e) {
		name = null;
	}
	if (name == null)
		name = "N/A";
	try {
		type = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].business_type").toString();
	} catch (Exception e) {
		type = null;
	}
	if (type == null)
		type = "N/A";
	try {
		genre = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].genre.value").toString().replace(",",
				"-");
	} catch (Exception e) {
		genre = null;
	}
	if (genre == null)
		genre = "N/A";
	try {
		duration = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].duration").toString();
	} catch (Exception e) {
		duration = null;
	}
	if (duration == null)
		duration = "N/A";
	try {
		specification = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].asset_subtype").toString();
	} catch (Exception e) {
		specification = null;
	}
	if (specification == null || id.contains("external"))
		specification = "N/A";
	try {
		subtitleLang = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].subtitle_languages")
				.toString();
	} catch (Exception e) {
		subtitleLang = null;
	}
	if (subtitleLang == null)
		subtitleLang = "N/A";
	try {
		originalLang = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].languages[0]").toString();
	} catch (Exception e) {
		originalLang = null;
	}
	if (originalLang == null)
		originalLang = "N/A";
	try {
		characters = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].characters").toString();
	} catch (Exception e) {
		characters = null;
	}
	if (characters == null)
		characters = "N/A";
	try {
		drm = respContent.jsonPath().get("buckets[0].items[" + carousalDot + "].is_drm").toString();
	} catch (Exception e) {
		drm = null;
	}
	if (drm == null)
		drm = "N/A";
	System.out.println(id + " --- " + name + " --- " + type + " --- " + genre + " --- " + duration + " --- "
			+ specification + " --- " + subtitleLang + " --- " + originalLang + " --- " + characters + " --- " + drm);
	Mixpanel.FEProp.setProperty("Content ID", id);
	Mixpanel.FEProp.setProperty("Content Name", name);
	Mixpanel.FEProp.setProperty("Content Type", type);
	Mixpanel.FEProp.setProperty("Genre", genre);
	Mixpanel.FEProp.setProperty("Content Duration", duration);
	Mixpanel.FEProp.setProperty("Content Specification", specification);
	Mixpanel.FEProp.setProperty("Subtitle Language", subtitleLang);
	Mixpanel.FEProp.setProperty("Content Original Language", originalLang);
	Mixpanel.FEProp.setProperty("Characters", characters);
	if (drm.equals("1"))
		Mixpanel.FEProp.setProperty("DRM Video", "true");
	else
		Mixpanel.FEProp.setProperty("DRM Video", "false");
}

public static Response getUserSettingDetails(String pUsername, String pPassword) {
	Response response = null;
	String xAccessToken = getXAccessTokenWithApiKey();
	String bearerToken = getBearerToken(pUsername, pPassword);
	String url = "https://userapi.zee5.com/v1/settings";
	response = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
	System.out.println("\nresponse body: " + response.getBody().asString());
	return response;
}

public static void getContentDetailsForPlayer(String contentID,String asset_subtype) {
	try {			
		Response respContent = ResponseInstance.getContentDetails(contentID, "content");
		Mixpanel.FEProp.setProperty("Content Specification", asset_subtype);
		System.out.println("asset_subtype : "+asset_subtype);
		Mixpanel.FEProp.setProperty("Content Name", respContent.jsonPath().getString("original_title").replace(",", "-"));
		System.out.println("content name : "+respContent.jsonPath().getString("original_title").replace(",", "-"));
		Mixpanel.FEProp.setProperty("Content Type",respContent.jsonPath().getString("business_type"));
		System.out.println("content type : "+respContent.jsonPath().getString("business_type"));
		Mixpanel.FEProp.setProperty("Content Duration", respContent.jsonPath().getString("duration"));
		System.out.println("duration : "+respContent.jsonPath().getString("duration"));
		Mixpanel.FEProp.setProperty("Content ID", respContent.jsonPath().getString("id"));
		System.out.println("id : "+respContent.jsonPath().getString("id"));
		Mixpanel.FEProp.setProperty("Characters",respContent.jsonPath().getList("actors").toString().replaceAll(",","-").replaceAll("\\s", ""));
		System.out.println("characters : "+respContent.jsonPath().getList("actors").toString().replaceAll(",","-").replaceAll("\\s", ""));
		Mixpanel.FEProp.setProperty("Subtitle Language", respContent.jsonPath().getString("subtitle_languages").toString());
		System.out.println("subtitle lang : "+respContent.jsonPath().getString("subtitle_languages").toString());
		Mixpanel.FEProp.setProperty("Genre",respContent.jsonPath().getList("genre.id").toString().replaceAll(",","-").replaceAll("\\s", ""));
		System.out.println("genre : "+respContent.jsonPath().getList("genre.id").toString().replaceAll(",","-").replaceAll("\\s", ""));
		Mixpanel.FEProp.setProperty("Content Original Language",respContent.jsonPath().getString("languages").replace("[", "").replace("]", ""));
		System.out.println("content ori lang: "+respContent.jsonPath().getString("languages").replace("[", "").replace("]", ""));
		if(respContent.jsonPath().getString("is_drm").equals("1")) Mixpanel.FEProp.setProperty("DRM Video","true");
		else Mixpanel.FEProp.setProperty("DRM Video","false");
	}
	catch(Exception e) {
		logger.error("Error in fetching content details");
	}
}

public static Response getResponseForPages(String tab, int pageNumber,String contentLanguages) {
	String userType = "", username = "", password = "";
	userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
	Response respPages = null;
	String Uri = "";
	if (tab.equals("news")) {
		tab = "5857";
	} else if (tab.equals("music")) {
		tab = "2707";
	} else if (tab.equals("home")) {
		tab = "homepage";
	} else if (tab.equals("kids")) {
		tab = "3673";
	} else if (tab.equals("freemovies")) {
		tab = "5011";
	} else if (tab.equals("play")) {
		tab = "4603";
	} else if (tab.equals("webseries") || tab.equals("web series")) {
		tab = "zeeoriginals";
	} else if (tab.equals("videos")) {
		tab = "videos";
	} else if (tab.equals("movies")) {
		tab = "movies";
	} else if (tab.equals("tvshows") || tab.equals("tv shows")) {
		tab = "tvshows";
	} else if (tab.equals("premium")) {
		tab = "premiumcontents";
	} else if (tab.equals("club")) {
		tab = "5851";
	}
	if (tab.equals("stories")) {
		Uri = "https://zeetv.zee5.com/wp-json/api/v1/featured-stories";
	} else if(tab.equals("live tv")) {
		Uri = "https://catalogapi.zee5.com/v1/channel/genres?translation=en&country=IN";
	}else {
		Uri = "https://gwapi.zee5.com/content/collection/0-8-"+tab+"?page="+pageNumber+"&limit=5&item_limit=20&country=IN&translation=en&languages=" + contentLanguages + "&version=10";
		//System.out.println(Uri);
	}		
	if (!userType.equalsIgnoreCase("Guest")) {
		if (userType.equals("NonSubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonSubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonSubscribedUserPassword");
		}
		if (userType.equals("SubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
		}
		Response userDetails = getUserSettingDetails(username, password);
		int keyCount = userDetails.jsonPath().get("key.size()");
		for (int i = 0; i < keyCount; i++) {
			if (userDetails.jsonPath().get("key[" + i + "]").toString().equals("content_language")) {
				contentLanguages = userDetails.jsonPath().get("value[" + i + "]").toString();
				break;
			}
		}
	}		
	String xAccessToken = getXAccessTokenWithApiKey();
	if (userType.equalsIgnoreCase("Guest")) {
		respPages = RestAssured.given().headers("x-access-token", xAccessToken).when().get(Uri);
	} else {		
		String bearerToken = getBearerToken(username, password);
		respPages = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(Uri);
	}
	//System.out.println("Response status : " + respPages.statusCode());
	return respPages;
}

public static ArrayList<String> getTrayValues (String userType,String tab,int assetType,String assetSubtype, String requiredType,String contentLanguages,int scenario) {
	ArrayList<String> trayDetails=new ArrayList<String>();
	String trayTitle="",contentTitle="",contentDataID="",contentID="",itemID="",channel="N/A",epNo="N/A",series="N/A",channels="N/A",horizontalIndex="N/A",imageUrl="N/A",trayId="";
	boolean foundContent=false;
	main : for(int i=1;i<20;i++) {//Considering each tab may have upto 20 pages
		Response respContent = ResponseInstance.getResponseForPages(tab.toLowerCase(), i, contentLanguages);
		int bucketsSize=0;
		try{ bucketsSize=respContent.jsonPath().get("buckets.size()"); }
		catch(Exception e) {break;}
		if(bucketsSize==0) break;
		else {
			int iter=0,j=0;
			if(i==1) j=1;//In first page skip carousel
			else j=0; 
			for(iter=j;iter<bucketsSize;iter++) {//iterate through trays
				for(int k=0;k<4;k++) {//iterate through 4 cards of the tray
					try {
						int assetypeAPI=Integer.valueOf(respContent.jsonPath().get("buckets["+iter+"].items["+k+"].asset_type").toString());													
						if(assetypeAPI==assetType) {
							String assetSubtypeAPI=respContent.jsonPath().get("buckets["+iter+"].items["+k+"].asset_subtype").toString();
							String businesssType=respContent.jsonPath().get("buckets["+iter+"].items["+k+"].business_type").toString();
							String ageRating=respContent.jsonPath().get("buckets["+iter+"].items["+k+"].age_rating").toString();
							if(assetSubtypeAPI.equals(assetSubtype)) {
								trayTitle=respContent.jsonPath().get("buckets["+iter+"].title").toString();
								System.out.println("trayTitle : "+trayTitle);
								trayId=respContent.jsonPath().get("buckets["+iter+"].id").toString();
								itemID=respContent.jsonPath().get("buckets["+iter+"].items["+k+"].id").toString();
								contentTitle=respContent.jsonPath().get("buckets["+iter+"].items["+k+"].title").toString();		
								series=contentTitle;
								contentDataID=respContent.jsonPath().get("buckets["+iter+"].items["+k+"].id").toString();
								imageUrl=respContent.jsonPath().get("buckets["+iter+"].items["+k+"].image_url.list").toString();
								if(userType.equals("Guest")) {
									//Guest user - free content - not A content
									if(requiredType.equalsIgnoreCase("free") && !businesssType.equals("premium_downloadable") && !ageRating.equals("A")) {
										contentID=contentDataID;
										horizontalIndex=String.valueOf(k+1);
										foundContent=true;
									}
								}
								else {
									//Non Sub user and sub user - free content 
									if(requiredType.equalsIgnoreCase("free") && !businesssType.equals("premium_downloadable")) {
										contentID=contentDataID;
										horizontalIndex=String.valueOf(k+1);
										foundContent=true;
									}
								}					
								if(requiredType.equalsIgnoreCase("premium")) {
									if(userType.equals("SubscribedUser") && businesssType.equals("premium_downloadable")) {
										contentID=contentDataID;
										horizontalIndex=String.valueOf(k+1);
										foundContent=true;
									}
									else if(businesssType.equals("premium_downloadable")){		
										int relatedsize=0,trailerSize= 0,seasonSize=0,orderID=0,previewSize=0;
										Response contentDetails=ResponseInstance.getContentDetails(contentDataID, "content");
										orderID = contentDetails.jsonPath().get("orderid");
										relatedsize = contentDetails.jsonPath().get("related.size()");
										for(int z=0;z<relatedsize;z++) {
											try {
												if(contentDetails.jsonPath().get("related["+z+"].asset_subtype").equals("trailer")) {
													contentID=contentDetails.jsonPath().get("related["+z+"].id").toString();													
													foundContent=true;
													horizontalIndex=String.valueOf(k+1);
													break;
												}
											}catch(Exception e) {}
										}
										if(relatedsize==0) {
											try {
												trailerSize=contentDetails.jsonPath().get("tvshow_details.trailers.size()");
												contentID=contentDetails.jsonPath().get("tvshow_details.trailers["+(trailerSize-1)+"]id").toString();	
												foundContent=true;
												horizontalIndex=String.valueOf(k+1);
												break;
											}catch(Exception e) {}
										}
										if(trailerSize==0) {
											try {
												String tvshowid=contentDetails.jsonPath().get("tvshow.id");															
												Response showDetails=ResponseInstance.getContentDetails(tvshowid, "original");
												series = showDetails.jsonPath().getString("title");
												epNo=showDetails.jsonPath().getString("episode_number").toString();
												if(epNo.equals("0")) epNo="N/A";
												channels=showDetails.jsonPath().getString("channels[0].title").toString();
												channels="["+channels+"]";	
												seasonSize=showDetails.jsonPath().get("seasons.size()");
												previewSize=showDetails.jsonPath().get("seasons["+(seasonSize-1)+"].previews.size()");
												for(int l=0;l<previewSize;l++) {
													int temporderid=Integer.valueOf(showDetails.jsonPath().get("seasons["+(seasonSize-1)+"].previews["+l+"].orderid").toString());
													if(temporderid==orderID) {
														contentID=showDetails.jsonPath().get("seasons["+(seasonSize-1)+"].previews["+l+"].id").toString();
														channel=showDetails.jsonPath().get("seasons["+(seasonSize-1)+"].previews["+l+"].channels[0].original_title").toString();
														imageUrl=showDetails.jsonPath().get("seasons["+(seasonSize-1)+"].previews["+l+"].image_url").toString();
														foundContent=true;
														horizontalIndex=String.valueOf(k+1);
														break;
													}
												}
											}
											catch(Exception e) {}
										}
									}	
								}
							}
						}
					}catch(Exception e) {
						System.out.println("Tab "+tab+", page number : "+i+", bucket : "+iter+", card "+k+" caused exception");
					}
					if(foundContent==true) break;
				}
				if(foundContent==true) break;
			}
			if(foundContent==true) break;
		}
	}
	trayDetails.add(trayTitle);//tray title					
	trayDetails.add(contentTitle);//content title
	trayDetails.add(contentDataID);//data content id
	trayDetails.add(contentID);//content id
	if(scenario==6) {
		Mixpanel.FEProp.setProperty("Carousal Name", "N/A");
		Mixpanel.FEProp.setProperty("Carousal ID", itemID);
	}
	else {
		Mixpanel.FEProp.setProperty("Carousal Name", trayTitle);
		Mixpanel.FEProp.setProperty("Carousal ID", trayId);
	}		
	Mixpanel.FEProp.setProperty("Channel Name", channel);
	Mixpanel.FEProp.setProperty("Series", series);
	Mixpanel.FEProp.setProperty("Episode No", epNo);
	Mixpanel.FEProp.setProperty("Channel Name", channels);
	Mixpanel.FEProp.setProperty("Horizontal Index", horizontalIndex);
	Mixpanel.FEProp.setProperty("Image URL", imageUrl);
	return trayDetails;
}


public static HashMap findVerticalIndex(String tab, String trayTitle, String contentLanguages) {
	ArrayList<String> trays=new ArrayList<String>();
	ArrayList<String> recotrays=new ArrayList<String>();
	int bucketsSize=0;
	main : for(int i=1;i<20;i++) {//Considering each tab may have upto 20 pages
		Response respContent = ResponseInstance.getResponseForPages(tab.toLowerCase(), i, contentLanguages);			
		try{ bucketsSize=respContent.jsonPath().get("buckets.size()"); }
		catch(Exception e) {break;}
		if(bucketsSize==0) break;
		else {
			for(int iter=0;iter<bucketsSize;iter++) {//iterate through trays
				String tray=respContent.jsonPath().get("buckets["+iter+"].title");
				trays.add(tray);
				if(tray.equals(trayTitle)) break main;
			}
		}
	}
	int nonRecoTraySize=trays.size();
	System.out.println(trays);
	//Response recoresp=getRecoResponseForPages(tab, contentLanguages);
	String userType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
	Response recoresp=getRecoDataFromTab(userType,tab.toLowerCase(),contentLanguages);
	int recoStart=recoresp.jsonPath().getInt("rails_position");
	System.out.println("recoStart"+recoStart);
	int rbucketsSize=0;
	try{ rbucketsSize=recoresp.jsonPath().get("buckets.size()"); }
	catch(Exception e) {}
	if(rbucketsSize!=0) {
		for(int iter=0;iter<rbucketsSize;iter++) {//iterate through trays
			String tray=recoresp.jsonPath().get("buckets["+iter+"].title");
			recotrays.add(tray);
		}
	}
	System.out.println(recotrays);
	HashMap<String,Integer> allTrays=new HashMap<String,Integer>();
	int contTrayIndex=0;
	for(int i=0;i<recoStart;i++) {
		allTrays.put(trays.get(i),i);
		contTrayIndex=i+1;
	}
	System.out.println("allTray: "+allTrays);
	int recoindex=0;
	for(int i=recoStart;i<(rbucketsSize+recoStart);i++) {
		allTrays.put(recotrays.get(recoindex),i);
		recoindex++;
	}
	System.out.println("allTray: "+allTrays);
	int currentsize=allTrays.size();
	System.out.println("currentsize"+currentsize);
	System.out.println("nonRecoTraySize+rbucketsSize"+(nonRecoTraySize+rbucketsSize));

	for(int i=currentsize;i<(nonRecoTraySize+rbucketsSize);i++) {
		allTrays.put(trays.get(contTrayIndex),i);
		contTrayIndex++;
	}
	System.out.println("allTray: "+allTrays);
	return allTrays;
}

public static Properties getUserData_NativeAndroid(String pUsername,String pPassword) { 
	String[] userData = { "email", "first_name", "last_name", "birthday", "gender", "registration_country",
			"recurring_enabled" };
	Properties pro = new Properties();
	String xAccessToken = getXAccessTokenWithApiKey();
	String bearerToken = getBearerToken(pUsername, pPassword);
	String url = "https://userapi.zee5.com/v1/user";
	resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
	String commaSplit[] = resp.asString().replace("{", "").replace("}", "").replaceAll("[.,](?=[^\\[]*\\])", "-")
			.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	for (int i = 0; i < commaSplit.length; i++) {
		if (Stream.of(userData).anyMatch(commaSplit[i]::contains)) {
			String com[] = commaSplit[i].split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			String key = com[0].replace("\"", "");
			MixpanelAndroid.FEProp.setProperty(key, com[1].replace("\"", ""));
			pro.setProperty(key, com[1].replace("\"", ""));
		}
	}
	pro.forEach((key, value) -> System.out.println(key + " : " + value));
	getDOB_NativeAndroid();
	MixpanelAndroid.fetchUserdata = true;
	
	return pro;
}

private static void getDOB_NativeAndroid() {
	String birthday=resp.jsonPath().get("birthday").toString();
	LocalDate dob = LocalDate.parse(birthday.split("T")[0]);
	LocalDate curDate = LocalDate.now();
	int ageYears=Period.between(dob, curDate).getYears();
	int ageMonths=Period.between(dob, curDate).getMonths();
	System.out.println(String.valueOf(ageYears));
	MixpanelAndroid.FEProp.setProperty("Age", String.valueOf(ageYears));
}

public static void setSubscriptionDetails_NativeAndroid() {
	
	String url = "https://subscriptionapi.zee5.com/v1/subscription?include_all=true";		
	String pUsername = getParameterFromXML("SubscribedUserName");
	String pPassword = getParameterFromXML("SubscribedPassword");
//	String pUsername = "clubRK@g.com";
//	String pPassword = "123456";
	
	String bearerToken = getBearerToken(pUsername, pPassword);
	resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken).when().get(url);
	//resp.prettyPrint();
	System.out.println("\nSubscrition related");
	MixpanelAndroid.FEProp.setProperty("Pack Duration",resp.jsonPath().getString("[0].subscription_plan.billing_frequency")); 
	String uri = "[0].subscription_plan.";
	String id = resp.jsonPath().getString(uri+"id");
	String original_title = resp.jsonPath().getString(uri+"original_title");
	String subscription_plan_Type = resp.jsonPath().getString(uri+"subscription_plan_type");
	
	MixpanelAndroid.FEProp.setProperty("Pack Selected",id+"_"+original_title+"_"+subscription_plan_Type); 
	MixpanelAndroid.FEProp.setProperty("Cost",resp.jsonPath().getString("[0].subscription_plan.price"));
	MixpanelAndroid.FEProp.setProperty("region",resp.jsonPath().getString("[0].region"));
	MixpanelAndroid.FEProp.setProperty("user_id",resp.jsonPath().getString("[0].user_id"));
	MixpanelAndroid.FEProp.setProperty("Unique ID",resp.jsonPath().getString("[0].user_id"));
	MixpanelAndroid.FEProp.setProperty("Active Plan Name",resp.jsonPath().getString(uri+"original_title"));
	MixpanelAndroid.FEProp.setProperty("Subscription Status",resp.jsonPath().getString("[0].state"));
	
	String getUserCountry = resp.jsonPath().getString(uri+"country");
	MixpanelAndroid.FEProp.setProperty("Country ID",getUserCountry);
	MixpanelAndroid.FEProp.setProperty("Registering Country",getUserCountry);
	
	String pCountry = "NA";
	if(getUserCountry.equalsIgnoreCase("IN")) {
		pCountry = "INDIA";
	}
	MixpanelAndroid.FEProp.setProperty("Country",pCountry);
	MixpanelAndroid.FEProp.setProperty("State",resp.jsonPath().getString("[0].region").toUpperCase());
}

public static String getCarouselContentFromAPI2(String usertype, String tabName) {
	
	String pContentLang = ResponseInstance.getContentLanguageForAppMixpanel(usertype);
	System.out.println("CONTENT LANG: "+pContentLang);
	
	Response pageResp=ResponseInstance.getResponseForAppPages(tabName,pContentLang,usertype);
	pageResp.print();
	
	String contentName = null;
	
	String asset_subtype=null;
	for (int i = 0; i < 5; i++) {
		String carousalName = pageResp.jsonPath().get("buckets[0].title");
		MixpanelAndroid.FEProp.setProperty("Carousal Name", carousalName);
		asset_subtype = pageResp.jsonPath().get("buckets[0].items["+i+"].asset_subtype");
		contentName = pageResp.jsonPath().get("buckets[0].items["+i+"].title");
		System.out.println("asset_subtype: "+asset_subtype);
		String contentID1 = null;
		Response ContentResp1 = null;
		String ContentId2=null;
		Response ContentResp2=null;
		boolean flag = false;
		String var = null;
		if(tabName.equalsIgnoreCase("TV Shows")|| tabName.contentEquals("News")) {
			if(asset_subtype.equalsIgnoreCase("external_link")) {
				var=asset_subtype;
			}else {
				var="Invalid";
			}
		}else if(tabName.equalsIgnoreCase("Home")|| tabName.equalsIgnoreCase("Premium")|| tabName.equalsIgnoreCase("Club")||tabName.equalsIgnoreCase("Movies")){
			if(asset_subtype.equalsIgnoreCase("movie")) {
				var=asset_subtype;
			}else if(asset_subtype.equalsIgnoreCase("original")){
				var=asset_subtype;
			}else {
				var="Invalid";
			}
			
		}else {
			var=asset_subtype;
		}
		
		
		contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].id");
		ContentResp1 = getResponseDetails(contentID1);
		
		switch (var) {
		case "movie":
			contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].id");
			ContentResp1 = getResponseDetails(contentID1);
			int relatedNodelength = ContentResp1.jsonPath().getList("related").size();		
			if (relatedNodelength >= 1 && (!(usertype.equalsIgnoreCase("SubscribedUser")))) {
				 ContentId2 = ContentResp1.jsonPath().get("related[0].id");
			      ContentResp2 = getResponseDetails(ContentId2);
			      setFEPropertyOfContentFromAPI2(ContentId2,ContentResp2, tabName);
			}else {
				setFEPropertyOfContentFromAPI2(contentID1,ContentResp1, tabName);
			}
			flag = true;
			break;
		case "original":
		    contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].id");
		    ContentResp1 = getContentDetails(contentID1, "original");
			ContentId2 = ContentResp1.jsonPath().getString("seasons[0].episodes[0].id");
			ContentResp2 = getContentDetails(ContentId2, "original");
			setFEPropertyOfContentFromAPI2(ContentId2,ContentResp2, tabName);
			flag = true;
			break;
			
		case "external_link":
			contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].slug");
			String value = fetchContentIDFromUrl(contentID1);
			System.out.println(value);
			ContentResp1 = ResponseInstance.getContentDetails(value, "original");
			if(!(tabName.equalsIgnoreCase("News") || tabName.equalsIgnoreCase("Eduauraa"))) {
				List<String> noOfEpisodes = ContentResp1.jsonPath().getList("seasons[0].episodes");
				for(int n=0; n<noOfEpisodes.size();n++) {
					String contenttype = ContentResp1.jsonPath().getString("seasons[0].episodes["+n+"].business_type");
					if(contenttype.equalsIgnoreCase("advertisement_downloadable")) {
						ContentId2 = ContentResp1.jsonPath().getString("seasons[0].episodes["+n+"].id");
						break;
					}
				}
				ContentResp2 = getContentDetails(ContentId2, "original");
				setFEPropertyOfContentFromAPI2(ContentId2,ContentResp2, tabName);
			}else {
				setFEPropertyOfContentFromAPI2(value,ContentResp1, tabName);
			}
			flag = true;
			break;
			
		case "video":
			contentID1 = pageResp.jsonPath().get("buckets[0].items["+i+"].id");
			ContentResp1 = getResponseDetails(contentID1);
			setFEPropertyOfContentFromAPI2(contentID1,ContentResp1, tabName);
			flag = true;
		    break;
		    
		default :System.out.println("not a required asset_subtype");
	    }
		
		if(flag==true) {
			break;
		}
	}
	return contentName;
}

public static void setFEPropertyOfContentFromAPI2(String contentID, Response ContentResp, String tabName) {
	
	String pCharacters = null;
	String contentDuration =null;
	contentDuration = ContentResp.jsonPath().getString("duration");
	
	
	String pBusinessType = ContentResp.jsonPath().getString("business_type");
	String pAssetSubType = "";
		boolean ex = ContentResp.jsonPath().getString("asset_subtype") != null;
		if(ex) {
			pAssetSubType = ContentResp.jsonPath().getString("asset_subtype");
		}else {
		    pAssetSubType = "N/A";
		}
	
	int episode = ContentResp.jsonPath().get("episode_number");
	String pEpisodeNum = Integer.toString(episode);
	if(pEpisodeNum.equalsIgnoreCase("0")) {
		pEpisodeNum = "N/A";
	}else {
		pEpisodeNum = "Episode "+pEpisodeNum;
	}
	
	String pTitle = ContentResp.jsonPath().getString("title");
	String pPublishedDate=null;
	try {
		String release_date = ContentResp.jsonPath().get("release_date").toString();
	    pPublishedDate = release_date.replace(release_date.substring(10), "");	
	}
	catch(Exception e) {
		pPublishedDate= "null";
		
	}
			
	
	
	String pContentBillingType = null;
	try {
		pContentBillingType = ContentResp.jsonPath().getString("billing_type");
		if (pContentBillingType.length() == 0) {
			pContentBillingType = "N/A";
		}
	}catch(Exception e) {
		pContentBillingType=null;
	}
	

	int languageCount = ContentResp.jsonPath().getList("languages").size();
	String pLanguages = null;
	if (languageCount > 1) {
		System.out.println("MULTIPLE LANGUAGES");
	} else if (languageCount == 1) {
		pLanguages = ContentResp.jsonPath().get("languages[0]");
	} else {
		System.out.println("Original Languages is Empty");
	}

	int genreSize = ContentResp.jsonPath().getList("genre").size();
	String pGenre = null;
	for (int j = 0; j < genreSize; j++) {
		if (genreSize == 1) {
			pGenre = ContentResp.jsonPath().get("genre[" + j + "].value");
		} else {
			pGenre = pGenre + "," + ContentResp.jsonPath().get("genre[" + j + "].value");
		}
	}
	pGenre = pGenre.replace("null,", "");

	int audioLangCount = ContentResp.jsonPath().getList("audio_languages").size();
	String pAudioLangguage = null;
	if (audioLangCount > 1) {
		System.out.println("Audio LANGUAGES");
		ArrayList<String> getAudioLanguages = new ArrayList<String>();
		for (int j = 0; j < audioLangCount; j++) {
			getAudioLanguages.add(ContentResp.jsonPath().get("audio_languages[" + j + "]").toString());
		}
		pAudioLangguage = getAudioLanguages.toString().replace("[", "").replace("]", "");
		pLanguages= pAudioLangguage;
	} else if (audioLangCount == 1) {
		pAudioLangguage = ContentResp.jsonPath().get("audio_languages[0]");
	} else {
		pAudioLangguage = "N/A";
		System.out.println("Audio Language is Empty");
	}

	int subTitleLangCount = ContentResp.jsonPath().getList("subtitle_languages").size();
	String pSubTitleLangguage = null;
	if (subTitleLangCount > 1) {
		System.out.println("SUBTITLE LANGUAGES");
		ArrayList<String> getSubtitles = new ArrayList<String>();
		for (int j = 0; j < subTitleLangCount; j++) {
			getSubtitles.add(ContentResp.jsonPath().get("subtitle_languages[" + j + "]").toString());
		}
		pSubTitleLangguage = getSubtitles.toString().replace("[", "").replace("]", "");
	} else if (subTitleLangCount == 1) {
		pSubTitleLangguage = ContentResp.jsonPath().get("subtitle_languages[0]");
	} else {
		System.out.println("Subtitle Language is Empty");
		pSubTitleLangguage = "N/A";
	}

	int actorsCount = ContentResp.jsonPath().getList("actors").size();
	ArrayList<String> getCharacters = new ArrayList<String>();
	if (actorsCount >= 1) {
		for (int j = 0; j < actorsCount; j++) {
			getCharacters.add(ContentResp.jsonPath().get("actors[" + j + "]").toString());
		}
		pCharacters = getCharacters.toString();
		pCharacters = pCharacters.replace("[", "").replace("]", "");
	} else if (tabName.equalsIgnoreCase("Live TV") | tabName.equalsIgnoreCase("Livetv") | tabName.equalsIgnoreCase("News")) {
		getCharacters.add("N/A");
		pCharacters = getCharacters.toString().replace("[", "").replace("]", "");
	} else {
		System.out.println("charactrs");
		pCharacters = getCharacters.toString();	
		System.out.println(pCharacters);
	}

	int isDRM = ContentResp.jsonPath().get("is_drm");
	String pDRM_Video = "false";
	if (isDRM == 1) {
		pDRM_Video = "true";
	}

	System.out.println("\n--PRINTING VALIDATION PARAMETERS--");

	System.out.println(contentID);
	System.out.println(pTitle);
	System.out.println(contentDuration);
	System.out.println(pBusinessType);
	System.out.println(pGenre);
	System.out.println(pAssetSubType);
	System.out.println(episode);
	System.out.println(pPublishedDate);
	System.out.println(pLanguages);
	System.out.println(pAudioLangguage);
	System.out.println(pSubTitleLangguage);
	System.out.println(pCharacters);
	System.out.println(pDRM_Video);
	System.out.println(pContentBillingType);

	// ----- Mix Panel Content Parameters Validation ------
	MixpanelAndroid.FEProp.setProperty("Audio Language", pAudioLangguage);
	MixpanelAndroid.FEProp.setProperty("Content ID", contentID);
	MixpanelAndroid.FEProp.setProperty("Content Name", pTitle);
	MixpanelAndroid.FEProp.setProperty("Content Duration", contentDuration);
	MixpanelAndroid.FEProp.setProperty("Content Type", pBusinessType);
	MixpanelAndroid.FEProp.setProperty("Content Specification", pAssetSubType);
	MixpanelAndroid.FEProp.setProperty("Content Original Language", pLanguages);
	MixpanelAndroid.FEProp.setProperty("Characters", pCharacters.toString());
	MixpanelAndroid.FEProp.setProperty("Content Billing Type", pContentBillingType);
	MixpanelAndroid.FEProp.setProperty("DRM Video", pDRM_Video);
	MixpanelAndroid.FEProp.setProperty("Episode No", pEpisodeNum);
	MixpanelAndroid.FEProp.setProperty("Genre", pGenre);
	MixpanelAndroid.FEProp.setProperty("Publishing Date", pPublishedDate);
	MixpanelAndroid.FEProp.setProperty("Subtitle Language", pSubTitleLangguage);
	
}

public static Response updateWatchHistory(String contentID,int duration) {
	String userType = "", username = "", password = "", url="",authorizationToken="";
	userType=Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
	Response updateWatchHistory=null;
	if (!userType.equalsIgnoreCase("Guest")) {
		if (userType.equals("NonSubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonSubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonSubscribedUserPassword");
		}
		if (userType.equals("SubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
		}
		authorizationToken=getAuthorization(username, password);
		RequestSpecification updateWatchHistoryRequestSpecification = RestAssured.given();
		updateWatchHistoryRequestSpecification.header("Content-Type", "application/json");
		updateWatchHistoryRequestSpecification.header("accept", "application/json");
		updateWatchHistoryRequestSpecification.header("Authorization", authorizationToken);
		updateWatchHistoryRequestSpecification.body("{\"id\":\""+contentID+"\",\"duration\":"+duration+"}");			
		updateWatchHistoryRequestSpecification.config(io.restassured.RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig
				.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
		url="https://userapi.zee5.com/v1/watchhistory";
		updateWatchHistory = updateWatchHistoryRequestSpecification.put(url);
		System.out.println(updateWatchHistory.getStatusCode());
		updateWatchHistory.prettyPrint();
	}			
	return updateWatchHistory;
}

public static String getAuthorization(String email, String password) {
	JSONObject requestParams = new JSONObject();
	requestParams.put("email", email);
	requestParams.put("password", password);
	RequestSpecification req = RestAssured.given();
	req.header("Content-Type", "application/json");
	req.config(io.restassured.RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig
			.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
	req.body(requestParams.toString());
	Response resp = req.post("https://userapi.zee5.com/v2/user/loginemail");
	String accesstoken = resp.jsonPath().getString("access_token");
	return accesstoken;
}


public static Response updateWatchHistory(String contentID,int duration,String guestToken) {
	String userType = "", username = "", password = "", url="",authorizationToken="";
	userType=Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
	Response userDetailsResponse=null;
	Response updateWatchHistoryResponse=null;
	if (!userType.equalsIgnoreCase("Guest")) {
		if (userType.equals("NonSubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedPassword");
		}
		if (userType.equals("SubscribedUser")) {
			username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
			password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
		}
		userDetailsResponse=getUserDetails(username, password);
		String userID=userDetailsResponse.jsonPath().get("id");
		System.out.println("userID:"+userID);
		RequestSpecification updateWatchHistoryRequestSpecification = RestAssured.given();
		updateWatchHistoryRequestSpecification.header("Content-Type", "application/json");
		updateWatchHistoryRequestSpecification.header("accept", "application/json");
		updateWatchHistoryRequestSpecification.header("Authorization", userID);
		updateWatchHistoryRequestSpecification.body("{\"id\":\""+contentID+"\",\"duration\":"+duration+",\"device_id\":\""+guestToken+"\"}");			
		updateWatchHistoryRequestSpecification.config(io.restassured.RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig
				.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
		url="https://api.zee5.com/api/v1/watchhistory";
		updateWatchHistoryResponse = updateWatchHistoryRequestSpecification.post(url);
		//System.out.println(updateWatchHistoryResponse.getStatusCode());
		//updateWatchHistoryResponse.prettyPrint();
		System.out.println("Updated watch history for: "+contentID);
	}			
	return updateWatchHistoryResponse;
}


public static Response getUserDetails(String username, String password) {
	Response response = null;
	String bearerToken = getBearerToken(username, password);
	String url = "https://userapi.zee5.com/v1/user";
	response = RestAssured.given().headers("Authorization", bearerToken).when().get(url);
	//System.out.println("\nresponse body: " + response.prettyPrint());
	return response;
}



//----------------Appsflyer------------------


public static void fetchExpectedDataforAppsFlyer(String event, String userType, String idNumber, String userValue, String topnavtab, String tabname, String showID, String packID, String musicID) throws IOException, ParseException{
	
	getLanguageforAppsFlyer(userType);
	
	//USERDATA
	getUserDataforAppsFlyer(userType);

	//SETTINGS
	getUserSettingsDetailsforAppsFlyer(userType);

	//LOCATION
	getUserLocationforAppsFlyer();

	//DEVICE DETAILS
	getDeviceDetails();
	
	
	StaticValuesforAppsFlyer(event,idNumber,userValue,topnavtab,tabname);
	
	
	DynamicValuesAsUserTypeforAppsFlyer(userType);
	
	
	if(event.contains("TVShows_Content_Play")||event.contains("af_svod_first_episode_free")||event.contains("video_view_50_percent")||event.contains("video_view_85_percent")||event.contains("videos_viewed_is_20")){
		getContentDetailsOFOriginalForAppsflyer(showID);
	}
	
	if(event.equalsIgnoreCase("af_add_to_cart")){
		getBuyPlanDataForAppsFlyer(packID);
	}
	
	if(event.equalsIgnoreCase("Videos_Content_Play")){
		getContentDetailsOFMusicForAppsflyer(musicID);
	}

	if(userType.equals("Guest")){
		if(event.equalsIgnoreCase("af_complete_registration") || event.equals("content_lang_done") || event.equals("display_lang_done")){
			getRegistrationDetailsForAppsflyer();	
		}	
	
	}
	
	
	
}



public static void getLanguageforAppsFlyer(String userType) {
	System.out.println("- - - - getLanguageforAppsFlyer - - - -");
	String language = null;
	if (userType.contains("Guest")) {
		AppsFlyer.expectedData.setProperty("New Content Language", "en-kn");
	} else {
		Response resplanguage = getUserinfoforNonSubORSub(userType);
		for (int i = 0; i < resplanguage.jsonPath().getList("array").size(); i++) {
			String key = resplanguage.jsonPath().getString("[" + i + "].key");
			if (key.contains("content_language")) {
				language = resplanguage.jsonPath().getString("[" + i + "].value");
				System.out.println("New Content Language : " + language);
				AppsFlyer.expectedData.setProperty("New Content Language", language.replace(",", "-"));
				break;
			}
		}
	}
	
	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
}





public static Properties getUserDataforAppsFlyer(String userType) {
	System.out.println("- - - - getUserDataforAppsFlyer - - - -");
	Properties pro = new Properties();
	if(!userType.equals("Guest")){
	String[] userData = { "id", "email", "mobile", "birthday", "gender", "ip_address",
			"registration_country", "recurring_enabled" };
	
	String xAccessToken = getXAccessTokenWithApiKey();

	String pUsername = null;
	String pPassword = null;
	
	if(userType.contains("NonSubscribedUser")){
		pUsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedUserName");
		pPassword = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedPassword");
		
	}else{
		pUsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
		pPassword = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
		
	}

	
	String bearerToken = getBearerToken(pUsername, pPassword);
	String url = "https://userapi.zee5.com/v1/user";
	resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
	System.out.println(resp.print());
	AppsFlyer.expectedData.setProperty("Email", resp.jsonPath().getString("email"));
	AppsFlyer.expectedData.setProperty("Gender", resp.jsonPath().getString("gender"));
	AppsFlyer.expectedData.setProperty("Registering Country", resp.jsonPath().getString("registration_country"));

	AppsFlyer.expectedData.setProperty("Unique ID", resp.jsonPath().getString("id"));
	
	//user_id only for Registration scenario
	AppsFlyer.expectedData.setProperty("user_id", resp.jsonPath().getString("id"));
	
	
	AppsFlyer.expectedData.setProperty("IP", resp.jsonPath().getString("ip_address"));
	AppsFlyer.expectedData.setProperty("Partner Name", resp.jsonPath().getString("additional.partner"));
	getDOBforAppsFlyer();			
	try{
		AppsFlyer.expectedData.setProperty("Phone Number", resp.jsonPath().getString("mobile"));			
	}catch(Exception e){
		System.out.println("Phone number not displayed");
	}

	
	AppsFlyer.expectedData.setProperty("Name", resp.jsonPath().getString("first_name")+" "+resp.jsonPath().getString("last_name"));
	
	try{
		AppsFlyer.expectedData.setProperty("Platform Name", resp.jsonPath().getString("additional.platform"));	
	}catch(Exception e){
		System.out.println("platform not displayed");
	}
	

	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	
	}else{
		
		AppsFlyer.expectedData.setProperty("Unique ID", "Z5X_ca2ef614db067f977ae58776fb46d3b4c4301e2d33e175b5a10ccc7c3dd2d393");
	}
	return pro;
	

}



private static String getDOBforAppsFlyer() {
	String birthday=resp.jsonPath().get("birthday").toString();
	LocalDate dob = LocalDate.parse(birthday.split("T")[0]);
	LocalDate curDate = LocalDate.now();
	int ageYears=Period.between(dob, curDate).getYears();
	int ageMonths=Period.between(dob, curDate).getMonths();
	if(ageMonths>=6) {
		++ageYears;
	}
	AppsFlyer.expectedData.setProperty("Age", String.valueOf(ageYears));
	
	return String.valueOf(ageYears);
}


public static Properties getUserSettingsDetailsforAppsFlyer(String userType) {
//	String[] userData = { "streaming_quality", "auto_play", "download_quality", "stream_over_wifi", "download_over_wifi", "display_language", "parental_control", "content_language", "eduauraaClaimed", "paytmconsent" };
	System.out.println("- - - - getUserSettingsDetailsforAppsFlyer - - - -");
	Properties pro = new Properties();
	
	if(!userType.equals("Guest")){
		
		String pUsername = null;
		String pPassword = null;
		
		if(userType.contains("NonSubscribedUser")){
			pUsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedUserName");
			pPassword = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("NonsubscribedPassword");
//			pUsername = "zeenonsubhipi@gmail.com";
//			pPassword = "123456";
		}else{
			pUsername = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
			pPassword = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");
//			pUsername = "zeetest10@test.com";
//			pPassword = "123456";
		}

		
		
		String bearerToken = getBearerToken(pUsername, pPassword);
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken)
				.when().get("https://userapi.zee5.com/v1/settings");
		resp.print();
		AppsFlyer.expectedData.setProperty("New Download Over Wifi Setting", resp.jsonPath().getString("[4].value"));
		AppsFlyer.expectedData.setProperty("New Download Quality Setting", resp.jsonPath().getString("[2].value"));
		AppsFlyer.expectedData.setProperty("New Content Language", resp.jsonPath().getString("[9].value").replace(",", "-"));
		AppsFlyer.expectedData.setProperty("New Stream Over Wifi Setting", resp.jsonPath().getString("[3].value"));
		AppsFlyer.expectedData.setProperty("New Video Streaming Quality Setting", resp.jsonPath().getString("[0].value"));
		AppsFlyer.expectedData.setProperty("New App Language", resp.jsonPath().getString("[7].value"));
		AppsFlyer.expectedData.setProperty("New Autoplay Setting", resp.jsonPath().getString("[1].value"));
		String prentControl = "N/A";
		try{
							
		System.out.println(resp.jsonPath().getString("[17].value"));
		if(!resp.jsonPath().getString("[17].value").equals("{}")){
			prentControl = resp.jsonPath().getString("[17].value").split(",")[0].replace("pin", "").split(":")[1].replace("\"", "");
			AppsFlyer.expectedData.setProperty("Parent Control Setting",prentControl);
		}
		}catch(Exception e){
			AppsFlyer.expectedData.setProperty("Parent Control Setting",prentControl);
		}
		
		

	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	}
	return pro;
}




public static Properties getUserLocationforAppsFlyer() {
	System.out.println("- - - - getUserLocationforAppsFlyer - - - -");
	String[] userData = { "country_code", "country", "state", "state_code" };
	Properties pro = new Properties();
	String url = "https://xtra.zee5.com/country";
	resp = RestAssured.given().urlEncodingEnabled(false).when().get(url);
	resp.print();
	AppsFlyer.expectedData.setProperty("State", resp.jsonPath().getString("state"));
	AppsFlyer.expectedData.setProperty("Country", resp.jsonPath().getString("country"));
	AppsFlyer.expectedData.setProperty("Country Code", resp.jsonPath().getString("country_code"));
//	AppsFlyer.expectedData.setProperty("State", resp.jsonPath().getString("state_code"));
	return pro;

}



public static Properties getDeviceDetails() throws IOException{
	System.out.println("- - - - getDeviceDetails - - - -");
	Properties pro = new Properties();

	//APP VERSION
	String temp;
	String appVersion = null;
	String cmd = "adb shell \"dumpsys package com.graymatrix.did | grep versionName\"";
	Process p = Runtime.getRuntime().exec(cmd);
	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	while ((temp = br.readLine()) != null) {
	//	System.out.println(temp);	
		appVersion = temp.trim();
	}
	String[] str = appVersion.split("=");
	AppsFlyer.expectedData.setProperty("App Version", str[1]);

	
	//MOBILE BRAND+MODEL
	String MobileBrand = null,MobileModel = null;
	String DeviceType = null;
	String cmd1 = "adb shell getprop ro.product.vendor.brand";
	Process p1 = Runtime.getRuntime().exec(cmd1);
	BufferedReader br1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
	while ((temp = br1.readLine()) != null) {
	//	System.out.println(temp);	
		MobileBrand = temp.trim();
	}
	String cmd2 = "adb shell getprop ro.product.vendor.model";
	Process p2 = Runtime.getRuntime().exec(cmd2);
	BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
	while ((temp = br2.readLine()) != null) {
	//	System.out.println(temp);	
		MobileModel = temp.trim();
	}
	DeviceType = MobileBrand +"-"+ MobileModel;
	AppsFlyer.expectedData.setProperty("Device Type", DeviceType);

	
	//OS VERSION
	float osVersion = 0 ;
	String cmd4 = "adb shell getprop ro.build.version.release";
	Process p4 = Runtime.getRuntime().exec(cmd4);
	BufferedReader br4 = new BufferedReader(new InputStreamReader(p4.getInputStream()));
	while ((temp = br4.readLine()) != null) {
	//	System.out.println(temp);	
		osVersion = Float.parseFloat(temp.trim());
	}
	AppsFlyer.expectedData.setProperty("OS Version", String.valueOf(osVersion));
	
	
	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	
	return pro;
	
}



public static Properties StaticValuesforAppsFlyer(String eventName, String idNumber,String userType, String topnavtab, String tabname){
	System.out.println("- - - - StaticValuesforAppsFlyer - - - -");
	Properties pro = new Properties();
	
	AppsFlyer.expectedData.setProperty("App UTM Medium", "N/A");
	AppsFlyer.expectedData.setProperty("Advertisement ID", "7e128be0-8f02-4eb4-93e4-e7382eb01d82");
	AppsFlyer.expectedData.setProperty("Latitude", "N/A");
	AppsFlyer.expectedData.setProperty("App isRetargeting", "N/A");
	AppsFlyer.expectedData.setProperty("App UTM Source", "N/A");
	AppsFlyer.expectedData.setProperty("App Campaign", "None");
	AppsFlyer.expectedData.setProperty("App UTM Content", "N/A");
	AppsFlyer.expectedData.setProperty("Sugar Box Value", "N/A");
	AppsFlyer.expectedData.setProperty("Appsflyer Campaign", "None");
	AppsFlyer.expectedData.setProperty("App UTM Campaign", "N/A");
	AppsFlyer.expectedData.setProperty("App UTM Term", "N/A");
	AppsFlyer.expectedData.setProperty("WIFI", "true");
	AppsFlyer.expectedData.setProperty("Appsflyer Source", "Basapplicastertest"+idNumber);
	AppsFlyer.expectedData.setProperty("App Source", "Basapplicastertest"+idNumber);
	
	AppsFlyer.expectedData.setProperty("User Type", userType);
	if(eventName.contains("Tab_View")){
		AppsFlyer.expectedData.setProperty("Tab Name", tabname);
		AppsFlyer.expectedData.setProperty("top_nav_tab", topnavtab);
	}else if(eventName.contains("screen_view")){
		AppsFlyer.expectedData.setProperty("Source", topnavtab);
		AppsFlyer.expectedData.setProperty("Page Name", tabname);
	}else if(eventName.contains("landing_on_subscriber_plans_screen")){
		AppsFlyer.expectedData.setProperty("Source", topnavtab);
		AppsFlyer.expectedData.setProperty("Page Name", tabname);
	}else if(eventName.contains("af_add_to_cart")){
		AppsFlyer.expectedData.setProperty("Source", topnavtab);
		AppsFlyer.expectedData.setProperty("Page Name", tabname);
	}
	
	
	
	
	
	AppsFlyer.expectedData.setProperty("City", "N/A");
	AppsFlyer.expectedData.setProperty("Longitude", "N/A");
	AppsFlyer.expectedData.setProperty("New Setting Value", "N/A");
	//AppsFlyer.expectedData.setProperty("Session ID", "1620393330");
	AppsFlyer.expectedData.setProperty("Appsflyer Medium", "N/A");
	//AppsFlyer.expectedData.setProperty("Appsflyer ID", "1620393331603-1718919116979185738");
	//AppsFlyer.expectedData.setProperty("First App Launch Date", "2021-05-07T06:45:30Z");
	AppsFlyer.expectedData.setProperty("Language", "English");
	AppsFlyer.expectedData.setProperty("AppsFlyer ID", "1620307847410-6601692054452113154");
	AppsFlyer.expectedData.setProperty("Advertising ID", "7e128be0-8f02-4eb4-93e4-e7382eb01d82");
	AppsFlyer.expectedData.setProperty("Platform", "android");
	AppsFlyer.expectedData.setProperty("SDK Version", "true");

	
	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	
	return pro;
}



public static Properties DynamicValuesAsUserTypeforAppsFlyer(String userType) throws ParseException{
	System.out.println("- - - - DynamicValuesAsUserTypeforAppsFlyer - - - -");
	Properties pro = new Properties();
			
	if(userType.equals("Guest")){
		dynamicValuesOfSubscription();
		
//		AppsFlyer.expectedData.setProperty("Unique ID", resp.jsonPath().getString("additional.guest_token"));	
		AppsFlyer.expectedData.setProperty("IP", "null");
		AppsFlyer.expectedData.setProperty("Partner Name", "N/A");	
		AppsFlyer.expectedData.setProperty("Age", "null");
		
		AppsFlyer.expectedData.setProperty("Email", "null");
		AppsFlyer.expectedData.setProperty("Gender", "null");
		AppsFlyer.expectedData.setProperty("Name", "null");
		AppsFlyer.expectedData.setProperty("Platform Name", "Android");
		AppsFlyer.expectedData.setProperty("Registering Country", "IN");
		
		AppsFlyer.expectedData.setProperty("New Download Over Wifi Setting", "false");
		AppsFlyer.expectedData.setProperty("New Download Quality Setting", "Ask Each Time");
		AppsFlyer.expectedData.setProperty("New Content Language", "en-kn");
		AppsFlyer.expectedData.setProperty("New Stream Over Wifi Setting", "false");
		AppsFlyer.expectedData.setProperty("New Video Streaming Quality Setting", "Auto");
		AppsFlyer.expectedData.setProperty("New App Language", "en");
		AppsFlyer.expectedData.setProperty("New Autoplay Setting", "true");
		String prentControl = "N/A";	
		AppsFlyer.expectedData.setProperty("Parent Control Setting",prentControl);
		
		AppsFlyer.expectedData.setProperty("hasRental", "null");
		AppsFlyer.expectedData.setProperty("Method", "N/A");
		AppsFlyer.expectedData.setProperty("isRental", "false");
		
		
	}else if(userType.equals("NonSubscribedUser")){
		dynamicValuesOfSubscription();
		AppsFlyer.expectedData.setProperty("hasRental", "false");
		AppsFlyer.expectedData.setProperty("Method", "N/A");
		AppsFlyer.expectedData.setProperty("isRental", "false");
		AppsFlyer.expectedData.setProperty("ad_id", "7e128be0-8f02-4eb4-93e4-e7382eb01d82");
	}else if(userType.equals("SubscribedUser")){
		SubcribedDetailsforAppsFlyer(userType);
	}
	


	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	
	return pro;
}



public static void dynamicValuesOfSubscription(){
	AppsFlyer.expectedData.setProperty("Pack Duration", "N/A");
	AppsFlyer.expectedData.setProperty("Latest Subscription Pack", "N/A");
	AppsFlyer.expectedData.setProperty("Free Trial Package", "N/A");
	AppsFlyer.expectedData.setProperty("Latest Subscription Pack Expiry", "N/A");
	AppsFlyer.expectedData.setProperty("Next Pack Expiry Date", "N/A");
	AppsFlyer.expectedData.setProperty("Subscription Status", "N/A");
	AppsFlyer.expectedData.setProperty("hasEduauraa", "false");
	AppsFlyer.expectedData.setProperty("Next Expiring Pack", "N/A");
}



public static void SubcribedDetailsforAppsFlyer(String userType) throws ParseException {	
//	String UserType;
//	UserType = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType");
	String username = null,password = null;
	if(userType.equals("SubscribedUser")) {
		username = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedUserName");
		password = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("SubscribedPassword");

//		username = "zeetest10@test.com"; //"zeein7@mailnesia.com";
//		password = "123456";
	
	}
	

	Response subscriptionResp=ResponseInstance.getSubscriptionDetails(username, password);
	subscriptionResp.print();
	
	int subscriptionItems=subscriptionResp.jsonPath().get("subscription_plan.size()");	
	String state=subscriptionResp.jsonPath().get("["+(subscriptionItems-1)+"].state");
	String id=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].id").toString();
	String subscription_plan_type=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].subscription_plan_type").toString();
	String title=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].title").toString();
	String Latest_Subscription_Pack=id+"_"+title+"_"+subscription_plan_type;
	
//	String packExpiry=subscriptionResp.jsonPath().get("subscription_end["+(subscriptionItems-1)+"]").toString();
	String packExpiry=subscriptionResp.jsonPath().get("["+(subscriptionItems-1)+"].subscription_end").toString();
//	System.out.println(" PACK "+packExpiry);
//	SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
//	java.text.DateFormat actualFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//	actualFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
//	java.util.Date packExpiryDate = actualFormat.parse(packExpiry);
	String Latest_Subscription_Pack_Expiry=packExpiry.toString();
	String Next_Expiring_Pack=Latest_Subscription_Pack;
	String Next_Pack_Expiry_Date=Latest_Subscription_Pack_Expiry;
	
	String billing_frequency=subscriptionResp.jsonPath().get("subscription_plan["+(subscriptionItems-1)+"].billing_frequency").toString();	
	Response tvodResp=ResponseInstance.getTVODDetails(username, password);
	int tvodItems=tvodResp.jsonPath().get("playback_state.size()");
	String HasRental="";
	try {			
		if(tvodResp.jsonPath().get("playback_state["+(tvodItems-1)+"]").toString().equalsIgnoreCase("purchased")) HasRental="true";
		else HasRental="false";
	}catch(Exception e) {HasRental="false";}
	
	Response settingsResp=ResponseInstance.getSettingsDetails(username, password);
	String hasEduauraa="false",key="";
	int pairs=settingsResp.jsonPath().get("key.size()");
	for (int i=0;i<pairs;i++) {
		key=settingsResp.jsonPath().get("key["+i+"]").toString();
		if(key.equals("eduauraaClaimed")) { 			
			hasEduauraa=settingsResp.jsonPath().get("value["+i+"]").toString();
			if(hasEduauraa.equals("")) hasEduauraa="false";
			break;
		}
	}
	
	AppsFlyer.expectedData.setProperty("Latest Subscription Pack", Latest_Subscription_Pack);
	AppsFlyer.expectedData.setProperty("Latest Subscription Pack Expiry", Latest_Subscription_Pack_Expiry);
	AppsFlyer.expectedData.setProperty("Next Expiring Pack", Next_Expiring_Pack);
	AppsFlyer.expectedData.setProperty("Next Pack Expiry Date", Next_Pack_Expiry_Date);
	AppsFlyer.expectedData.setProperty("Free Trial Package", "N/A");	
	AppsFlyer.expectedData.setProperty("hasEduauraa", hasEduauraa);
	AppsFlyer.expectedData.setProperty("Subscription Status", state);
	AppsFlyer.expectedData.setProperty("Free Trial Expiry Date", "N/A");
	AppsFlyer.expectedData.setProperty("Pack Duration", billing_frequency);
	AppsFlyer.expectedData.setProperty("HasRental", HasRental);
	
	
//	printProperties1(AppsFlyer.expectedData);
	
	
}




public static Properties getContentDetailsOFOriginalForAppsflyer(String ID) {
	Properties pro = new Properties();
	System.out.println("Content ID :"+ID);
	
	if (assetSubType.equalsIgnoreCase("tvshow") || assetSubType.equalsIgnoreCase("episode")
			|| assetSubType.equalsIgnoreCase("external_link")) {
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
				.get("https://gwapi.zee5.com/content/tvshow/" + ID + "?translation=en&country=IN");
	} else if (assetSubType.equalsIgnoreCase("external_link")) { 

	} else if(assetSubType.equalsIgnoreCase("original") || assetSubType.equalsIgnoreCase("video")
			|| assetSubType.equalsIgnoreCase("movie")|| assetSubType.equalsIgnoreCase("trailer")){
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
				.get("https://gwapi.zee5.com/content/details/" + ID + "?translation=en&country=IN&version=2");
		if(!assetSubType.equalsIgnoreCase("trailer")) {
		if(resp.jsonPath().getList("related")  != null) {
			ID = resp.jsonPath().getString("related[0].id");
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/"+ID+"?translation=en&country=IN&version=2");
			}
		}
	} else {
		if (pageName.equals("episode") || pageName.equals("shows")) {
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
					.get("https://gwapi.zee5.com/content/tvshow/" + ID + "?translation=en&country=IN");
		} else if (pageName.equals("movies")) {
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
					.get("https://gwapi.zee5.com/content/details/" + ID + "?translation=en&country=IN&version=2");
			if(trailer) {
				ID = resp.jsonPath().getString("related[0].id");
				resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/"+ID+"?translation=en&country=IN&version=2");
			}
		}
	}

	
	
//	Mixpanel.FEProp.setProperty("Content Duration", resp.jsonPath().getString("duration"));
	AppsFlyer.expectedData.setProperty("show_id", resp.jsonPath().getString("id"));
	AppsFlyer.expectedData.setProperty("season_id", resp.jsonPath().getString("season.id"));
	AppsFlyer.expectedData.setProperty("Content ID", resp.jsonPath().getString("seasons[0].episodes[0].id"));
	AppsFlyer.expectedData.setProperty("Series", resp.jsonPath().getString("original_title"));
	AppsFlyer.expectedData.setProperty("Content Name", resp.jsonPath().getString("seasons[0].episodes[0].title"));	
	AppsFlyer.expectedData.setProperty("subtitle language", resp.jsonPath().getList("seasons[0].episodes[0].video_details.subtitles").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	AppsFlyer.expectedData.setProperty("Audio Language", resp.jsonPath().getList("seasons[0].episodes[0].video_details.audiotracks").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	AppsFlyer.expectedData.setProperty("Content Original Language", resp.jsonPath().getList("seasons[0].episodes[0].audio_languages").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	AppsFlyer.expectedData.setProperty("Subtitle Language", resp.jsonPath().getList("seasons[0].episodes[0].subtitle_languages").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	AppsFlyer.expectedData.setProperty("Genre",resp.jsonPath().getList("seasons[0].episodes[0].genres.id").toString().replaceAll(",","-").replaceAll("\\s", " ").replaceAll("\\[", "").replaceAll("\\]", ""));
	AppsFlyer.expectedData.setProperty("Content Type", resp.jsonPath().getString("seasons[0].episodes[0].business_type"));
	
	if(resp.jsonPath().getString("seasons[0].episodes[0].tvshow_details.asset_subtype").contains("original")){
		AppsFlyer.expectedData.setProperty("Top Category", "Zee Original");	
	}else{
		AppsFlyer.expectedData.setProperty("Top Category", "Original");
	}
	

//	Mixpanel.FEProp.setProperty("Content Specification", resp.jsonPath().getString("asset_subtype"));
//	Mixpanel.FEProp.setProperty("Characters",resp.jsonPath().getList("actors").toString().replaceAll(",","-").replaceAll("\\s", ""));
////	Mixpanel.FEProp.setProperty("Audio Language",resp.jsonPath().getList("audio_languages").toString().replace("[", "").replace("]", ""));
//	Mixpanel.FEProp.setProperty("Subtitle Language", resp.jsonPath().getString("subtitle_languages").toString());
//	Mixpanel.FEProp.setProperty("Content Type",resp.jsonPath().getString("business_type"));
//	Mixpanel.FEProp.setProperty("Genre",resp.jsonPath().getList("genre.id").toString().replaceAll(",","-").replaceAll("\\s", ""));
//	Mixpanel.FEProp.setProperty("Content Original Language",resp.jsonPath().getString("languages").replace("[", "").replace("]", ""));
//	if(resp.jsonPath().getString("is_drm").equals("1")) {
//	Mixpanel.FEProp.setProperty("DRM Video","true");
//	}else {
//		Mixpanel.FEProp.setProperty("DRM Video","false");
//	}
	resp.print();
	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	trailer = false;
	
	return pro;
}



public static void getBuyPlanDataForAppsFlyer(String PlanId){

	//String PlanId="0-11-1842";
	Response buyPlanResp = null;
	
	String url = "https://subscriptionapi.zee5.com/v1/subscriptionplan?country=IN&translation=en&system=Z5&version=2&platform_name=web_app";

	//String b = getBearerToken(username, password);
	// System.out.println("\nbearer token :"+b+"\n");
	//response = given().headers("Authorization", b).when().get(url);
	buyPlanResp = RestAssured.given().when().get(url);
	System.out.println("\nresponse body: "+buyPlanResp.getBody().asString());
			
	//buyPlanResp.print();
	
	int buyPlanItems=buyPlanResp.jsonPath().get("subscription_plan.size()");	
	//System.out.println(buyPlanItems);
	int j = 0;
	for(int i=0; i<=buyPlanItems; i++){
		
		String id = buyPlanResp.jsonPath().get("["+i+"].id").toString();
		
		if(PlanId.equalsIgnoreCase(id)){
			j=i;
			//System.out.println(i);
			break;
		}
		
	}
	
	String id = buyPlanResp.jsonPath().get("["+j+"].id").toString();
	String title = buyPlanResp.jsonPath().get("["+j+"].title").toString();
	String PackSelected = id+"_"+title;
	AppsFlyer.expectedData.setProperty("Pack Selected", PackSelected);
	
	String description = buyPlanResp.jsonPath().get("["+j+"].description").toString().replace("50% off on ₹", "").concat(".0");
	String price = buyPlanResp.jsonPath().get("["+j+"].price").toString();
	String currency = buyPlanResp.jsonPath().get("["+j+"].currency").toString();
	AppsFlyer.expectedData.setProperty("af_revenue", description);
	AppsFlyer.expectedData.setProperty("Cost", price);
	AppsFlyer.expectedData.setProperty("pack_id", id);
	AppsFlyer.expectedData.setProperty("af_currency", currency);
	AppsFlyer.expectedData.setProperty("login_method", "N/A");
	AppsFlyer.expectedData.setProperty("ft_available", "N/A");
	AppsFlyer.expectedData.setProperty("Promo Code", "N/A");


	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	
}



public static Properties getContentDetailsOFMusicForAppsflyer(String ID) {
	Properties pro = new Properties();
	System.out.println("Content ID :"+ID);
	Response resp;
	resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
			.get("https://gwapi.zee5.com/content/details/" +ID +"?translation=en&country=IN&version=2");

	System.out.println(resp.print());
	
	AppsFlyer.expectedData.setProperty("show_id", resp.jsonPath().getString("id"));
	AppsFlyer.expectedData.setProperty("season_id", "N/A");
	AppsFlyer.expectedData.setProperty("Content Name", resp.jsonPath().getString("title"));
	AppsFlyer.expectedData.setProperty("Content ID", resp.jsonPath().getString("id"));
	AppsFlyer.expectedData.setProperty("Genre", resp.jsonPath().getString("genre[0].value"));
	AppsFlyer.expectedData.setProperty("Content Type", resp.jsonPath().getString("business_type"));
	AppsFlyer.expectedData.setProperty("Audio Language", resp.jsonPath().getList("video_details.audiotracks").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	AppsFlyer.expectedData.setProperty("Content Original Language", resp.jsonPath().getList("languages").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	AppsFlyer.expectedData.setProperty("Top Category", "Music");
	String subtitle = resp.jsonPath().getList("subtitle_languages").toString();
	if(subtitle.equals("[]")){
		AppsFlyer.expectedData.setProperty("Subtitle Language", "N/A");
	}else{
		AppsFlyer.expectedData.setProperty("Subtitle Language", subtitle.replaceAll("\\[", "").replaceAll("\\]", ""));
	}
	
	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	
	return pro;
}



public static Properties getRegistrationDetailsForAppsflyer() {
	Properties pro = new Properties();

	
	String xAccessToken = getXAccessTokenWithApiKey();

	String pUsername = newEmailID;
	String pPassword = newPassword;
	

	String bearerToken = getBearerToken(pUsername, pPassword);
	String url = "https://userapi.zee5.com/v1/user";
	resp = RestAssured.given().headers("x-access-token", xAccessToken).header("authorization", bearerToken).when().get(url);
	System.out.println(resp.print());
	
	//user_id only for Registration scenario
	AppsFlyer.expectedData.setProperty("user_id", resp.jsonPath().getString("id"));


	AppsFlyer.expectedData.setProperty("ad_id", "7e128be0-8f02-4eb4-93e4-e7382eb01d82");
	AppsFlyer.expectedData.setProperty("af_registration_method", "email_password");
	AppsFlyer.expectedData.forEach((key, value) -> System.out.println(key + " : " + value));
	
	return pro;
}


@SuppressWarnings("unused")
public static Response getWatchList1(String pUsername, String pPassword) {
	String url = "https://userapi.zee5.com/v1/watchlist";
	String bearerToken = getBearerToken(pUsername, pPassword);
	System.out.println(getXAccessTokenWithApiKey()+"\n");
	System.out.println(bearerToken);
	resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).header("authorization", bearerToken).when().get(url);
	resp.print();
	return resp;
}

public static Response getContentDetails1(String ID) {
	System.out.println("Content ID :"+ID);
	
	if (assetSubType.equalsIgnoreCase("tvshow") || assetSubType.equalsIgnoreCase("episode")
			|| assetSubType.equalsIgnoreCase("external_link")) {
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
				.get("https://gwapi.zee5.com/content/tvshow/" + ID + "?translation=en&country=IN");
	} else if (assetSubType.equalsIgnoreCase("external_link")) { 

	} else if(assetSubType.equalsIgnoreCase("original") || assetSubType.equalsIgnoreCase("video")
			|| assetSubType.equalsIgnoreCase("movie")|| assetSubType.equalsIgnoreCase("trailer")){
		resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
				.get("https://gwapi.zee5.com/content/details/" + ID + "?translation=en&country=IN&version=2");
		if(!assetSubType.equalsIgnoreCase("trailer")) {
		if(resp.jsonPath().getList("related")  != null) {
			ID = resp.jsonPath().getString("related[0].id");
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/"+ID+"?translation=en&country=IN&version=2");
			}
		}
	} else {
		if (pageName.equals("episode") || pageName.equals("shows")) {
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
					.get("https://gwapi.zee5.com/content/tvshow/" + ID + "?translation=en&country=IN");
		} else if (pageName.equals("movies")) {
			resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when()
					.get("https://gwapi.zee5.com/content/details/" + ID + "?translation=en&country=IN&version=2");
			if(trailer) {
				ID = resp.jsonPath().getString("related[0].id");
				resp = RestAssured.given().headers("x-access-token", getXAccessTokenWithApiKey()).when().get("https://gwapi.zee5.com/content/details/"+ID+"?translation=en&country=IN&version=2");
			}
		}
	}
	
	Mixpanel.FEProp.setProperty("Content Duration", resp.jsonPath().getString("duration"));
	Mixpanel.FEProp.setProperty("Content ID", resp.jsonPath().getString("id"));
	Mixpanel.FEProp.setProperty("Content Name", resp.jsonPath().getString("original_title"));
	Mixpanel.FEProp.setProperty("Content Specification", resp.jsonPath().getString("asset_subtype"));
	Mixpanel.FEProp.setProperty("Characters",resp.jsonPath().getList("actors").toString().replaceAll(",","-").replaceAll("\\s", ""));
//	Mixpanel.FEProp.setProperty("Audio Language",resp.jsonPath().getList("audio_languages").toString().replace("[", "").replace("]", ""));
	Mixpanel.FEProp.setProperty("Subtitle Language", resp.jsonPath().getString("subtitle_languages").toString());
	Mixpanel.FEProp.setProperty("Content Type",resp.jsonPath().getString("business_type"));
	Mixpanel.FEProp.setProperty("Genre",resp.jsonPath().getList("genre.id").toString().replaceAll(",","-").replaceAll("\\s", ""));
	Mixpanel.FEProp.setProperty("Content Original Language",resp.jsonPath().getString("languages").replace("[", "").replace("]", ""));
	if(resp.jsonPath().getString("is_drm").equals("1")) {
	Mixpanel.FEProp.setProperty("DRM Video","true");
	}else {
		Mixpanel.FEProp.setProperty("DRM Video","false");
	}
	resp.print();
	Mixpanel.FEProp.forEach((key, value) -> System.out.println(key + " : " + value));
	trailer = false;
	return resp;
}


}

