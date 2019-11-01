/*package CONTI.Test.Old;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CONTI.Test.AbstractContiTests;
import CONTI.pages.FleetPortalPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.YardReaderVehiclesPage;
import CONTI.pages.YardSelectionPage;
import CONTI.testdata.JsonDataReaderUsers;
import CONTI.testdata.JsonDataReaderYardID;
import CONTI.testdata.YardData;

public class WorkshopViewUser_14356 extends AbstractContiTests{
	
	private String URL = "https://cocqa.conti.de/";
	private Logger log = Logger.getLogger(this.getClass());

	private SoftAssert assertSoftly = new SoftAssert();
	private static final String TESTNAME = "WorkshopViewUserO";

	private JsonDataReaderUsers readUserData;
	private YardData yardData;


	@BeforeTest(alwaysRun = true)
	public void setup() throws MalformedURLException {
		testSetup(TESTNAME);
		readUserData = new JsonDataReaderUsers();
		JsonDataReaderYardID readYardData = new JsonDataReaderYardID();
		yardData = readYardData.getYardReader(3);
	}

	@AfterTest
	public void cleanUp() {
//		MainPage mainPage = new MainPage(driver);
//		mainPage.logout();
//		driver.close();
//		eReport.flush();
	}

	@Test(priority = 1, description = "1. Open Homepage and Login")
	public void OpenPageAndLogin() {
		ExtentTest eTest = eReport.startTest("1.Check Login");
		driver.get(URL);
		takeScreenshot(TESTNAME, "1-beforeLogin");
		LoginPage loginPage = new LoginPage(driver);

		String username = "test_ws_view";
		String password = readUserData.getUserPassword(username);
		log.info("login with username: " + username);
		loginPage.doLogin(username, password);

		eTest.log(LogStatus.INFO, "Login proceeded.");

		// After login there may be a popup article which needs to be closed
		boolean isArticleVisibleCheck = loginPage.isArticleVisibleCheck();
		if (isArticleVisibleCheck) {
			loginPage.closeArticle();
		}
	
		// Check if correct user has been logged in
	    boolean checkUsername = loginPage.getLoggedInUser().contains(username);
		takeScreenshot(TESTNAME, "1-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			eTest.log(LogStatus.PASS, "1. Open Homepage and Login PASSED");
		} else {
			eTest.log(LogStatus.FAIL, "1. Open Homepage and Login FAILED");
		}
		eReport.endTest(eTest);
		eReport.flush();
		assertSoftly.assertAll();
	}
	
	@Test(priority = 2, description = "2. Select Yard Reader")
	public void WebSeiteAufrufenundLoginVerifizieren() {
		ExtentTest eTest = eReport.startTest("2.YardReader");
		//FleetPortalPage fleetPortalPage = new FleetPortalPage(driver);
		//fleetPortalPage.openContiConnect();
		
		YardSelectionPage yardSelectionPage = new YardSelectionPage(driver);
		yardSelectionPage.selectYardReader(yardData.getYardID());
		
		
		//inComlepte Steps or missing entry
		System.out.println("exit");
		System.exit(0);
		
		YardReaderVehiclesPage yardReaderVehiclesPage  = new YardReaderVehiclesPage(driver);
		yardReaderVehiclesPage.selectVehicle("TRUCK");

		Map<String, Boolean> result = new HashMap<String, Boolean>();

		result.put("Checking vehicle type", yardReaderVehiclesPage.getVehicleType().equals("TRUCK"));
		result.put("Checking status", yardReaderVehiclesPage.getAmountOfOkStatus() == 6);
		result.put("Checking Alerts", yardReaderVehiclesPage.checkAlerts());
		
		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				eTest.log(LogStatus.PASS,
						"Vehicle check: " + k + " PASSED");
			else
				eTest.log(LogStatus.FAIL,
						"Vehicle check: " + k + " FAILED");
		});
		
		eReport.endTest(eTest);
		eReport.flush();
		assertSoftly.assertAll();
	}
}
*/