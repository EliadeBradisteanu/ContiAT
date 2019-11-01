package CONTI.Test;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.YardSelectionPage;
import CONTI.testdata.JsonDataReaderYardID;
import CONTI.testdata.YardData;

public class WorkshopViewUser_14356 extends BaseTest{
	
    private static final String TESTNAME = "Workshop View User";
	
	private Logger log = Logger.getLogger(WorkshopViewUser_14356.class);

	private SoftAssert assertSoftly = new SoftAssert();
	private YardData yardData;
	private JsonDataReaderYardID readYardData;
	
	private LoginPage loginPage;
	private MainPage mainPage;
	
	@BeforeClass
	public void setup() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Select Yard Reader");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		readYardData = new JsonDataReaderYardID();
		yardData = readYardData.getYardReader(3);
	}

	@AfterClass
	public void cleanUp() {	
		mainPage.doLogout();
		getDriver().close();	
		ExtentTestManager.endTest();
	}
	
	@Test(priority = 49, description = "1. Open Homepage and Verify Login")
	public void openHomepageAndVerifyLogin() {
				
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-beforeLogin");

		log.info("login with username: " + propertyManager.getUserTestWsView());
		mainPage = loginPage.doLogin(propertyManager.getUserTestWsView(), propertyManager.getPassTestWsView());
			
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");

		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getUserTestWsView());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "1. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}		
	
		assertSoftly.assertAll();
		
	}
	
	@Test(priority = 50, description = "2. Select Yard Reader")
	public void WebSeiteAufrufenundLoginVerifizieren() {	
		
		YardSelectionPage yardSelectionPageN = new YardSelectionPage(driver, wait);
		
		yardSelectionPageN.selectYardReader(yardData.getYardID())
		                  .clickContinue();
		
	   // vehicle should be there in oreder to continue with the test		
		
		
		System.out.println("exit");System.exit(0);
		
	
		assertSoftly.assertAll();
	}

}
