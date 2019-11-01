package CONTI.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
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
import CONTI.pages.PressureSimulatorPage;
import CONTI.pages.SelectHhtFilePage;
import CONTI.testdata.JsonDataHandlerVehicleData;
import CONTI.testdata.VehicleData;
import CONTI.utils.TimeUtil;

public class MyFleetUpdate_14542 extends BaseTest{
	
	
	private static final String TESTNAME = "My Fleet Update";
	private static final String DATA_FOR_MY_FLEET_UPDATE = "dataForMyFleetUpdate";
	private static final String HHT_FILE_S2D4 = "hhtFiles/generic_HHT_S2_D4forMHP.asc";
	private static final String UPLOAD_CONFIRMATION_MSG = "Le fichier HHT a été téléchargé avec succès et les informations d'axe et de capteurs ont été assignés au véhicule sélectionné";
	private static final String TEMPERATURE = "20";
	private static final String PRESSURE = "0";	
	
	private Logger log = Logger.getLogger(MyFleetUpdate_14542.class);

	private SoftAssert assertSoftly = new SoftAssert();	
	private JsonDataHandlerVehicleData readVehicleData;
	private VehicleData vehicleData;

	private LoginPage loginPage;
	private MainPage mainPage;
	private MyFleetPage myFleetPage;
	private GeneralInfoMyFleetPage myFleetGenInfoPage;
	private SelectHhtFilePage selectHhtFilePage;
	private PressureSimulatorPage pressureSimulatorPage;

	
	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
                                       + "<br> 2. Check MyFleet for new vehicle"
                                       + "<br> 3. Edit and upload Hht File"
                                       + "<br> 4. Open pressure simulator and modify pressure"
                                       + "<br> 5. Check pressure in MyFleet");

		loginPage = new LoginPage(driver, wait);		
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readVehicleData = new JsonDataHandlerVehicleData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		vehicleData = readVehicleData.getVehicleData(DATA_FOR_MY_FLEET_UPDATE);
	}

	@AfterClass
	public void cleanUp() {				
		mainPage.doLogout();
		getDriver().close();				
		ExtentTestManager.endTest();
	}
	
	@Test(priority = 51, description = "1. Open Homepage and Verify Login")
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
	
	@Test(priority = 52, description = "2. Check MyFleet for new vehicle")
	public void checkMyFleet() {
			
		myFleetPage = mainPage.openContiConnect()
                              .openMyFleet();	
			
		myFleetPage.filterCustomerVehicleIdFr(vehicleData.getInternalCustomerVehicleId());
		
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "2-checkForVehicle");
		
		myFleetGenInfoPage = myFleetPage.clickDetailsForFirstAlertInTable();                                
		
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		TimeUtil.sleep(1, TimeUnit.SECONDS);
		result.put("fleetID", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getFleetID()));		
		result.put("DepotID", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getDepotName()));
		result.put("CustomerVehicleID",
				myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getInternalCustomerVehicleId()));
		result.put("Status", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getStatus().replaceAll("ve", "f")));
		result.put("AxleConfiguration",
				myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getAxleConfiguration()));
		result.put("Vehicle type", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getVehicleType().toLowerCase()));
		result.put("Make", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getMake()));
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "2-checkVehicle");
		
		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "2. MyFleet vehicle check: attribute " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.FAIL, "2. MyFleet vehicle check: attribute " + k + " FAILED");			
		});
		
		assertSoftly.assertAll();	
	}
	
	@Test(priority = 53, description = "3. Edit and upload Hht File")
	public void openAndEditVehicleDetails() {
		
		selectHhtFilePage = myFleetGenInfoPage.clickEdit()
		                                       .selectTelecharger();
		
		File file = new File(getClass().getClassLoader().getResource(HHT_FILE_S2D4).getFile());
		
		selectHhtFilePage.uploadHTT(file.getPath())
		                 .clickTelecharger(); 
			
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-editAndUploadHhtFile");
		
        boolean uploadConfirmationMsg= selectHhtFilePage.getWebElementText(selectHhtFilePage.getBody()).contains(UPLOAD_CONFIRMATION_MSG);		

		assertSoftly.assertTrue(uploadConfirmationMsg);
		
		if (uploadConfirmationMsg) {
			ExtentTestManager.getTest().log(Status.PASS, "3. Edit and upload Hht File");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "3. Edit and upload Hht File");
		}		
				
		assertSoftly.assertAll();		
	}
	
	//could be implemented in new tab need vnc tuneling
	@Test(priority = 54, description = "4. Open pressure simulator and modify pressure")
	public void changePressure() {
		
		driverManager.quitDriver();
		runDriver();
		loginPage = new LoginPage(driver, wait);
		pressureSimulatorPage = new PressureSimulatorPage(driver, wait);
		
		loginPage.openUrl(propertyManager.getPressureSimulatorLinkSEBU015());
		
		pressureSimulatorPage.setTempPressure(TEMPERATURE, PRESSURE)
		                     .clickSubmit();
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "4-openPressureSimultorAndModifyPressure");
		
		boolean submitPressure= pressureSimulatorPage.getTempValues().equals(StringUtils.EMPTY);		

		assertSoftly.assertTrue(submitPressure);
		
		if (submitPressure) {
			ExtentTestManager.getTest().log(Status.PASS, "4. Open pressure simultor and modify pressure");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "4. Open pressure simultor and modify pressure");
		}		
		
		driverManager.quitDriver();
		assertSoftly.assertAll();
	}
	
	@Test(priority = 55, description = "5. Check pressure in MyFleet")
	public void checkPressureAndReleaseIt() {
		myFleetPage = mainPage.openContiConnect()
                              .openMyFleet();	

        myFleetPage.filterCustomerVehicleIdFr(vehicleData.getInternalCustomerVehicleId())
                   .checkCriticalIcon()
                   .clickFirstEntryInTable()
                   .scroll("400")
                   .checkPresureValueAndAlertsMessages();    
	}	
}
