package CONTI.Test;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.GeneralInfoMyFleetPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.pages.SelectHhtFilePage;
import CONTI.testdata.JsonDataHandlerVehicleData;
import CONTI.testdata.VehicleData;

public class SensorDataAvailableNormalUser_14385 extends BaseTest {

	private static final String TESTNAME = "Sensor data available for normal user";
	private static final String SENOR_DATA_AVAILABLE_DEPOT_1 = "sensorDataAvailableDepot1";
	private static final String SENOR_DATA_AVAILABLE_DEPOT_2 = "sensorDataAvailableDepot2";
	private static final String HHT_FILE_S2D4 = "hhtFiles/generic_HHT_S2_D4forMHP.asc";
	private static final String UPLOAD_CONFIRMATION_MSG = "Die HHT Datei wurde erfolgreich hochgeladen und die Achs- und Sensorinformationen wurden dem gewählten Fahrzeug zugewiesen.";

	private Logger log = Logger.getLogger(SensorDataAvailableNormalUser_14385.class);

	private SoftAssert assertSoftly = new SoftAssert();
	private JsonDataHandlerVehicleData readVehicleData;
	private VehicleData vehicleDataDepot1;
	private VehicleData vehicleDataDepot2;

	private LoginPage loginPage;
	private MainPage mainPage;	

	private MyFleetPage myFleetPage;
	private GeneralInfoMyFleetPage myFleetGenInfoPage;
	private SelectHhtFilePage selectHhtFilePage;
	private GeneralInfoMyFleetPage myFleetGenInfoPageN;

	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME,"1. Open Homepage and Verify Login"		                              
				                      + "<br> 2. Check vehicle for first depot contains sensors"
				                      + "<br> 3. Login as second depot user"
				                      + "<br> 4. Upload HTT File"
				                      + "<br> 5. Check Sensor information");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readVehicleData = new JsonDataHandlerVehicleData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		
		vehicleDataDepot1 = readVehicleData.getVehicleData(SENOR_DATA_AVAILABLE_DEPOT_1);
		vehicleDataDepot2 = readVehicleData.getVehicleData(SENOR_DATA_AVAILABLE_DEPOT_2);
	}

	@AfterClass
	public void cleanUp() {
		mainPage.doLogout();
		getDriver().close();
		ExtentTestManager.endTest();
	}

	@Test(priority = 63, description = "1. Open Homepage and Verify Login")
	public void openHomepageAndVerifyLogin() {

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-beforeLogin");

		log.info("login with username: " + propertyManager.getDepotUser3288194());
		mainPage = loginPage.doLogin(propertyManager.getDepotUser3288194(), propertyManager.getPassDepotUser3288194());

		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");

		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();

		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getDepotUser3288194());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "1. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "1. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();
	}

	@Test(priority = 64, description = "2. Check vehicle for first depot contains sensors")
	public void openMyFleetAndSearchVehicleSensors() {
		myFleetPage = mainPage.openContiConnect()
				              .openMyFleet();

		myFleetGenInfoPageN = myFleetPage.filterCustomerVehicleId(vehicleDataDepot1.getInternalCustomerVehicleId())
				.clickDetailsForFirstAlertInTable();

		boolean checkSensors = myFleetGenInfoPageN.getSensorAmount() == vehicleDataDepot1.getSensorAmount();
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "2-checkSensorExist");

		assertSoftly.assertTrue(checkSensors, "successfully created");
		if (checkSensors) {
			ExtentTestManager.getTest().log(Status.PASS, "2-Sensor exists information PASSED");

		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "2-Sensor Exists information FAILED");
		}

		assertSoftly.assertAll();

	}

	//@Test(priority = 65, description = "3. Login as second depot user and upload HTT File")
	public void loginAsSecondDepotUser() {
		
		driverManager.quitDriver();		
		runDriver();
		loginPage = new LoginPage(driver, wait);
			
		loginPage.openUrl(propertyManager.getContiUrl());
		
		log.info("login with username: " + propertyManager.getDepotUser3288200());
		mainPage = loginPage.doLogin(propertyManager.getDepotUser3288200(), propertyManager.getPassDepotUser3288200());
	
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");
		
		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();
		
		// Check if correct user has been logged in
		boolean checkUsername = mainPage.getLoggedInUser().contains(propertyManager.getDepotUser3288200());
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-afterLogin");

		assertSoftly.assertTrue(checkUsername, "checkusername");
		if (checkUsername) {
			ExtentTestManager.getTest().log(Status.PASS, "3. Open Homepage and Verify Login PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "3. Open Homepage and Verify Login FAILED");
		}

		assertSoftly.assertAll();			
	}

	//@Test(priority = 66, description = "4. Upload HTT File")
	public void uploadHhtFile() {
		myFleetPage = mainPage.openContiConnect()
				              .openMyFleet();

		myFleetPage.filterCustomerVehicleIdFr(vehicleDataDepot2.getInternalCustomerVehicleId());

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "4-checkForVehicleInMyFleet");

		myFleetGenInfoPage = myFleetPage.clickDetailsForFirstAlertInTable();
		
		selectHhtFilePage = myFleetGenInfoPage.clickEdit()
                                              .selectTelecharger();
		
		File file = new File(getClass().getClassLoader().getResource(HHT_FILE_S2D4).getFile());

		selectHhtFilePage.uploadHTT(file.getPath())
		                 .clickTelecharger();
		
		boolean uploadConfirmationMsg = selectHhtFilePage.getWebElementText(selectHhtFilePage.getBody())
				                                         .contains(UPLOAD_CONFIRMATION_MSG);

		assertSoftly.assertTrue(uploadConfirmationMsg);

		if (uploadConfirmationMsg) {
			ExtentTestManager.getTest().log(Status.PASS, "4. Upload Hht File");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "4. Upload Hht File");
		}

		assertSoftly.assertAll();
	}
	
	//@Test(priority = 67, description = "5. Check Sensor information")
	public void checkSensorsSecondUser() {
		myFleetPage = mainPage.openContiConnect().openMyFleet();

		myFleetGenInfoPageN = myFleetPage.filterCustomerVehicleId(vehicleDataDepot2.getInternalCustomerVehicleId())
				.clickDetailsForFirstAlertInTable();

		boolean checkSensors = myFleetGenInfoPageN.getSensorAmount() == vehicleDataDepot2.getSensorAmount();

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "5-checkSensorExist");

		assertSoftly.assertTrue(checkSensors, "successfully created");
		if (checkSensors) {
			ExtentTestManager.getTest().log(Status.PASS, "5-Sensor exists information PASSED");

		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "5-Sensor Exists information FAILED");
		}

		assertSoftly.assertAll();
	}
}
