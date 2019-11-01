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

public class DeleteRecipientWithAssignmentTest_14586 extends BaseTest{

	private static final String TESTNAME = "Delete recipient with assignment";
	private static final String PAS_DE_DONNÉE_DISPONIBLE = "Pas de donnée disponible";
	private static final String RECIPIENT2 = "recipient2";
	
	private Logger log = Logger.getLogger(DeleteRecipientWithAssignmentTest_14586.class);

	private SoftAssert assertSoftly = new SoftAssert();	
	private JsonDataHandlerRecipientsData readRecipientsData;
	private RecipientsData recipientsData;
	
	private LoginPage loginPage;
	private MainPage mainPage;
	private ConfigurationPage configurationPage;
	private CreateRecipientPage createRecipientPage;
	private AssignRecipientPage assignRecipientPage;
	
	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Create Recipient for deletion"
                                       + "<br> 3. Assign Recipient"
                                       + "<br> 4. Delete Recipient");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readRecipientsData = new JsonDataHandlerRecipientsData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		recipientsData = readRecipientsData.getRecipientsData(RECIPIENT2);
	}

	@AfterClass
	public void cleanUp() {		
		mainPage.doLogout();
		getDriver().close();
		ExtentTestManager.endTest();
	}
	
	@Test(priority = 20, description = "1. Open Homepage and Verify Login")
	public void openHomepageAndVerifyLogin() {
	
        ScreenshotUtil.takeScreenshot(driver,TESTNAME, "1-beforeLogin");		
		
		log.info("login with username: " + propertyManager.getUserTcUserA());
		mainPage = loginPage.doLogin(propertyManager.getUserTcUserA(), propertyManager.getPassTcUserA());
		loginPage.openUrl(propertyManager.getTcUserAUrl());
		
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");
	 	
		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getUserTcUserA());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-afterLogin");	
		
		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "1. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();
	}
	
	@Test(priority = 21, description = "2. Create Recipient for deletion")
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
		                   .clickSaveFr();
		
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "2-createRecipientAfterSaving");
		
		configurationPage.scroll("-100")
		                 .searchRecipientsFr(recipientsData.getFirstName());
		
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
	
	@Test(priority = 22, description = "3. Assign Recipient")
	public void assignRecipient() {		
	     
		configurationPage =mainPage.openConfiguration();
		configurationPage.openSources();
		
		assignRecipientPage = configurationPage.clickFirstSourceInTable()		
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
	
	@Test(priority = 23, description = "4. Delete Recipient")
	public void deleteRecipient() {		
		
		configurationPage.openRecipients()
		                 .deleteFirstRecipient();
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "4-deletedRecipient");
		
		configurationPage.searchRecipientsFr(recipientsData.getFirstName());
		
        assertSoftly.assertTrue(configurationPage.getWebElementText(configurationPage.getBody()).contains(PAS_DE_DONNÉE_DISPONIBLE));
		
		boolean successfullyDeleted = configurationPage.getWebElementText(configurationPage.getBody()).contains(PAS_DE_DONNÉE_DISPONIBLE);
			
		if(successfullyDeleted) {
			ExtentTestManager.getTest().log(Status.PASS, "4. Delete Recipient PASSED");			
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "4. Delete Recipient FAILED");			
		}

		assertSoftly.assertAll();		
	}		
}
