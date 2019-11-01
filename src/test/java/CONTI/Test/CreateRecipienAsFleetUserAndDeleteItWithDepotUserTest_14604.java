package CONTI.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.AssignRecipientPage;
import CONTI.pages.ConfigurationPage;
import CONTI.pages.CreateRecipientPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.testdata.JsonDataHandlerRecipientsData;
import CONTI.testdata.RecipientsData;

public class CreateRecipienAsFleetUserAndDeleteItWithDepotUserTest_14604 extends BaseTest {

	private static final String TESTNAME = "Delete Recipient Without Assignment Test";
	private static final String BAT11989_2119 = "BAT11989-2119";
	private static final String KEINE_DATEN_VERFÜGBAR = "Keine Daten verfügbar.";
	private static final String RECIPIENT3 = "recipient3";	
	
	private Logger log = Logger.getLogger(CreateRecipienAsFleetUserAndDeleteItWithDepotUserTest_14604.class);

	private SoftAssert assertSoftly = new SoftAssert();	
	private JsonDataHandlerRecipientsData readRecipientsData;
	private RecipientsData recipientsData;
	private AssignRecipientPage assignRecipientPage;

	private LoginPage loginPage;
	private MainPage mainPage;
	private ConfigurationPage configurationPage;
	private CreateRecipientPage createRecipientPage;

	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Create Recipient as Fleet user for deletion"
                                       + "<br> 3. Assign Recipient"
                                       + "<br> 4. Login as Depot User" 
                                       + "<br> 5. Delete assigned Recipient" );

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readRecipientsData = new JsonDataHandlerRecipientsData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		recipientsData = readRecipientsData.getRecipientsData(RECIPIENT3);
	}

	@AfterClass
	public void cleanUp() {		
		mainPage.doLogout();
		getDriver().close();
		ExtentTestManager.endTest();
	}
	
	@Test(priority = 27, description = "1. Open Homepage and Verify Login")
	public void openHomepageAndVerifyLogin() {		

		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "1-beforeLogin");

		log.info("login with username: " + propertyManager.getFleetUser3288193());
		mainPage = loginPage.doLogin(propertyManager.getFleetUser3288193(), propertyManager.getPassFleetUser3288193());
		
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");

		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
			
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getFleetUser3288193());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-afterLogin");
	
		
		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "1. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();			
	}
	
	@Test(priority = 28, description = "2. Create Recipient as Fleet user for deletion")
	public void createRecipient() {
		
		configurationPage = mainPage.openConfiguration();
		createRecipientPage = configurationPage.openRecipients()
		                                        .clickCreateRecipients();
		
		createRecipientPage.enterFirstName(recipientsData.getFirstName())
		                   .enterLastName(recipientsData.getLastName())
		                   .enterEMail(recipientsData.getEmail())
		                   .enterPhoneNumber(recipientsData.getTelNo())
		                   .scroll("200")
		                   .selectCountry(recipientsData.getCountry())
		                   .selectLanguage(recipientsData.getLanguage())
		                   .checkNotifyIssuesFlag()
		                   .checkNotifyUpdatesFlag();
				                   
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "2-createRecipientBeforeSaving");
		
		createRecipientPage.scroll("-100")
                           .clickSaveDe();
		
        ScreenshotUtil.takeScreenshot(driver,TESTNAME, "2-createRecipientAfterSaving");

        configurationPage.scroll("-100")
                         .searchRecipientsDe(recipientsData.getFirstName()); 
		
        Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		result.put("firstName", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsData.getFirstName()));
		result.put("lastName", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsData.getLastName()));
		result.put("telNo", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsData.getTelNo().replaceAll(" ","")));
		result.put("eMail", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsData.getEmail()));
		result.put("language", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsData.getLanguage()));
		
		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "2. Create recipient check: attribute " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.PASS, "2. Create recipient check: attribute " + k + " FAILED");			
		});
	      
		assertSoftly.assertAll();
	}
	
	@Test(priority = 29, description = "3. Assign Recipient")
	public void assignRecipient() {
		configurationPage =mainPage.openConfiguration();
		
		assignRecipientPage = configurationPage.openSources()
		                                       .searchSources(BAT11989_2119)		                                       
		                                       .clickVehicleInSourceTable(BAT11989_2119)
		                                       .clickAssignRecipient();
		
		assignRecipientPage.search(recipientsData.getFirstName())
                           .assingSearchedRecipient(recipientsData.getFirstName());

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-assignRecipient");
		
		//check recipient was assigned
		assertSoftly.assertTrue(
				configurationPage.getWebElementText(configurationPage.getBody()).contains(recipientsData.getFirstName()));

		boolean successfullyDeleted = configurationPage.getWebElementText(configurationPage.getBody())
				.contains(recipientsData.getFirstName());

		if (successfullyDeleted) {
			ExtentTestManager.getTest().log(Status.PASS, "3. Assign Recipient PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "3. Assign Recipient FAILED");
		}

		assertSoftly.assertAll();
	}
	
	@Test(priority = 30, description = "4. Login as Depot User")
	public void loginWithDepotUser() {
		driverManager.quitDriver();		
		runDriver();
		loginPage = new LoginPage(driver, wait);
			
		loginPage.openUrl(propertyManager.getContiUrl());
		
		log.info("login with username: " + propertyManager.getDepotUser3288194());
		mainPage = loginPage.doLogin(propertyManager.getDepotUser3288194(), propertyManager.getPassDepotUser3288194());
		loginPage.openUrl(propertyManager.getTcUserAUrl());
		
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");
	 	
		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getDepotUser3288194());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "4-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "4. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "4. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();
	}
	
	@Test(priority = 31, description = "5. Delete assigned Recipient")
	public void deleteAssignedRecipient() {
		
		configurationPage =mainPage.openConfiguration();
		
		configurationPage.openSources()		
		                 .searchSources(BAT11989_2119)
                         .clickFirstVehicleOfFirstSourceInTable();		
		
		//check that recipient is attached to the depot vehicle
		assertSoftly.assertTrue(
				configurationPage.getWebElementText(configurationPage.getBody()).contains(recipientsData.getFirstName()));
		
		configurationPage.openRecipients()
		                 .searchRecipientsDe(recipientsData.getFirstName())
		                 .deleteFirstRecipient();
	
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "5-deletedRecipient");

		assertSoftly.assertTrue(
				configurationPage.getWebElementText(configurationPage.getBody()).contains(KEINE_DATEN_VERFÜGBAR));

		boolean successfullyDeleted = configurationPage.getWebElementText(configurationPage.getBody())
				.contains(KEINE_DATEN_VERFÜGBAR);

		if (successfullyDeleted) {
			ExtentTestManager.getTest().log(Status.PASS, "5. Delete Recipient PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "5. Delete Recipient FAILED");
		}

		assertSoftly.assertAll();
	}
}
