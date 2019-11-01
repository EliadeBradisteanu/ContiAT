package CONTI.Test;

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
import CONTI.pages.GeneralDataYardReaderPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.SearchAccountPage;
import CONTI.pages.SearchYardReaderPage;
import CONTI.testdata.JsonDataReaderYardID;
import CONTI.testdata.YardData;

public class AdminYardReaderAssignment_14409 extends BaseTest {
	
	private static final String TESTNAME = "Yard Reader Assignment";
	private static final String DEPOT_ID = "0003261943";
	private static final String FLEET_ID = "0003261942";	
	
	private Logger log = Logger.getLogger(AdminYardReaderAssignment_14409.class);

	private SoftAssert assertSoftly = new SoftAssert();
	private YardData yardData;
	private JsonDataReaderYardID readYardData;
	
	private LoginPage loginPage;
	private MainPage mainPage;
	private SearchYardReaderPage searchYardReaderPage;
	private GeneralDataYardReaderPage generalDataYardReaderPage;
	private SearchAccountPage searchAccountPage;

	@BeforeClass
	public void setup() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Open Yard Reader"
                                       + "<br> 3. Update Yard Reader"
                                       + "<br> 4. Set Initial Values");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		readYardData = new JsonDataReaderYardID();
		yardData = readYardData.getYardReader(2);
	}

	@AfterClass
	public void cleanUp() {	
		mainPage.doLogout();
		getDriver().close();
		ExtentTestManager.endTest();
	}

	@Test(priority = 9, description = "1. Open Homepage and Verify Login")
	public void openHomepageAndVerifyLogin() {
			
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-beforeLogin");

		log.info("login with username: " + propertyManager.getUserMhpcku1());
		mainPage = loginPage.doLogin(propertyManager.getUserMhpcku1(), propertyManager.getPasswordMhpcku12());
				
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");

		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getUserMhpcku1());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "1. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}
		
		assertSoftly.assertAll();
	}

	@Test(priority = 10, description = "2. Open Yard Reader")
	public void openYardReader() {
		searchYardReaderPage = mainPage.openAdministration()
		                                .openYardReaders();
			
		generalDataYardReaderPage = searchYardReaderPage.searchYardReaders(yardData.getYardID())
		                                                .clickFirstEntry();	
		
		boolean checkYardReader = generalDataYardReaderPage.getYardReaderId().contains(yardData.getYardID());
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "2-openYardReader");

		assertSoftly.assertTrue(checkYardReader);
		if (checkYardReader) {
			ExtentTestManager.getTest().log(Status.PASS, "2. Open Yard Reader PASSED");		
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "2. Open Yard Reader FAILED");
		}

		assertSoftly.assertAll();
	}

	@Test(priority = 11, description = "3. Update Yard Reader")
	public void updateYardReader() {
		
		searchAccountPage = generalDataYardReaderPage.openFleetSearch();
		
		searchAccountPage.searchAndSelectFirstEntry(yardData.getFleetID(), yardData.getDepotID());
		
		//click inactive to make save button clickable
		generalDataYardReaderPage.scroll("100")
		                         .setYardReaderStatusToInactive()
		                         .setYardReaderStatusToActive()
		                         .scroll("-100");
		                         
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-saveYardReader");

		generalDataYardReaderPage.clickOnSave();
		log.info("saving yard reader changes");
		
		//reopen yard reader and check values
		searchYardReaderPage.clickFirstEntry();	
		
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		result.put("YardID", generalDataYardReaderPage.getYardReaderId().equals(yardData.getYardID()));		
		result.put("FleetID", generalDataYardReaderPage.getFleetId().equals(yardData.getFleetID()));
		result.put("Status", generalDataYardReaderPage.getStatus().equals(yardData.getStatus()));
		result.put("DepotID", generalDataYardReaderPage.getDepotId().equals(yardData.getDepotID()));
	
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-reopenedYardReader");
	
		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "Yard reader check: attribute " + k + " PASSED");
			else
				ExtentTestManager.getTest().log(Status.FAIL, "Yard reader check: attribute " + k + " FAILED");				
		});

		assertSoftly.assertAll();
	}

	// Not actually part of the test. Needed so test can run multiple times
	@Test(priority = 12, description = "4. Set Initial Values")
	public void setInitialValues() {		
		
		generalDataYardReaderPage.openFleetSearch();
		
		searchAccountPage.searchAndSelectFirstEntry(FLEET_ID, DEPOT_ID);
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "4-resetData");
		
		generalDataYardReaderPage.scroll("100")
                                 .setYardReaderStatusToInactive()
                                 .setYardReaderStatusToActive()
                                 .scroll("-100");
		
		generalDataYardReaderPage.clickOnSave();
		
		log.info("resetting yard reader");
	
	}
}
