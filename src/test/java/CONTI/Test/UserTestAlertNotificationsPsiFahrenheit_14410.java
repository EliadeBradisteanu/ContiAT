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
import CONTI.pages.AssignRecipientPage;
import CONTI.pages.ConfigurationPage;
import CONTI.pages.CreateRecipientPage;
import CONTI.pages.CreateVehiclePage;
import CONTI.pages.GeneralDataVehiclePage;
import CONTI.pages.GeneralInfoMyFleetPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.pages.PressureSimulatorPage;
import CONTI.pages.SearchAccountPage;
import CONTI.pages.SearchVehiclesPage;
import CONTI.pages.SelectHhtFilePage;
import CONTI.testdata.JsonDataHandlerRecipientsData;
import CONTI.testdata.JsonDataHandlerVehicleData;
import CONTI.testdata.RecipientsData;
import CONTI.testdata.VehicleData;
import CONTI.utils.TimeUtil;

public class UserTestAlertNotificationsPsiFahrenheit_14410 extends BaseTest {
	
	
	private static final String TESTNAME = "User Test Alert Notifications Psi Fahrenheit";
	private static final String DATA_FOR_NOTIFICATION = "dataForNotification";
	private static final String RECIPIENT5 = "recipient5";
	private static final String RECIPIENT4 = "recipient4";
	private static final String HHT_FILE_S2D4 = "hhtFiles/generic_HHT_S2_D4forMHP.asc";
	private static final String UPLOAD_CONFIRMATION_MSG = "Die HHT Datei wurde erfolgreich hochgeladen und die Achs- und Sensorinformationen wurden dem gewählten Fahrzeug zugewiesen.";
	private static final String TEMPERATURE = "20";
	private static final String PRESSURE = "0";
	private static final String PAS_DE_DONNÉE_DISPONIBLE = "Pas de donnée disponible";
	
	private Logger log = Logger.getLogger(DeleteRecipientWithoutAssignmentTest_14584.class);

	private SoftAssert assertSoftly = new SoftAssert();	
	private JsonDataHandlerVehicleData readVehicleData;
	private VehicleData vehicleData;
	private JsonDataHandlerRecipientsData readRecipientsData;
	private RecipientsData recipientsDataSms;
	private RecipientsData recipientsDataEmail;
	
	private LoginPage loginPage;
	private MainPage mainPage;	
	private MyFleetPage myFleetPage;
	private GeneralInfoMyFleetPage myFleetGenInfoPage;
	private SelectHhtFilePage selectHhtFilePage;
	private ConfigurationPage configurationPage;
	private CreateRecipientPage createRecipientPage;
	private AssignRecipientPage assignRecipientPage;
	private PressureSimulatorPage pressureSimulatorPage;
	
	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Login as Depot user and check vehicle in MyFleet"
                                       + "<br> 2. Upload HTT File"
                                       + "<br> 3. Create Recipient with SMS notification"
                                       + "<br> 4. Create Recipient with Email notification"
                                       + "<br> 5. Assign Recipients"
                                       + "<br> 6. Open pressure simulator and modify pressure"
                                       + "<br> 7. Verify Email and Sms"
                                       + "<br> 8. Delete Recipients");

		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());

		try {
			readRecipientsData = new JsonDataHandlerRecipientsData();
			readVehicleData = new JsonDataHandlerVehicleData();
		} catch (IOException e) {
			log.error("could not read data", e);
			e.printStackTrace();
		}
		vehicleData = readVehicleData.getVehicleData(DATA_FOR_NOTIFICATION);
    	recipientsDataSms = readRecipientsData.getRecipientsData(RECIPIENT4);
		recipientsDataEmail = readRecipientsData.getRecipientsData(RECIPIENT5);		
	}

	@AfterClass
	public void cleanUp() {		
		mainPage.doLogout();
		getDriver().close();
		ExtentTestManager.endTest();
	}
		
	@Test(priority = 56, description = "1. Login as Depot user and check vehicle in MyFleet")
	public void openMyFleetAndSearchVehicle() {
		
		driverManager.quitDriver();		
		runDriver();
		loginPage = new LoginPage(driver, wait);
			
		loginPage.openUrl(propertyManager.getContiUrl());
		log.info("login with username: " + propertyManager.getDepotUser3288200());
		mainPage = loginPage.doLogin(propertyManager.getDepotUser3288200(), propertyManager.getPassDepotUser3288200());
		
		ExtentTestManager.getTest().log(Status.INFO, "Login proceeded.");
	 	
		// After login there may be a popup article which needs to be closed
		mainPage.closeArticleIfVisible();

		myFleetPage = mainPage.openContiConnect()
				              .openMyFleet();

		myFleetPage.filterCustomerVehicleIdFr(vehicleData.getInternalCustomerVehicleId());

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "1-checkForVehicleInMyFleet");

		myFleetGenInfoPage = myFleetPage.clickDetailsForFirstAlertInTable();

		Map<String, Boolean> result = new HashMap<String, Boolean>();
	
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		result.put("fleetID", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getFleetID()));
		result.put("DepotID", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getDepotName()));
		result.put("CustomerVehicleID",
				myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getInternalCustomerVehicleId()));
		result.put("Status",
				myFleetGenInfoPage.getGeneralInformation().contains("Aktiv"));
		result.put("AxleConfiguration",
				myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getAxleConfiguration()));
		result.put("Vehicle type",
				myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getVehicleType()));
		result.put("Make", myFleetGenInfoPage.getGeneralInformation().contains(vehicleData.getMake()));
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-checkVehicleAttributes");

		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "1. MyFleet vehicle check: attribute " + k + " PASSED");
			else
				ExtentTestManager.getTest().log(Status.FAIL, "1. MyFleet vehicle check: attribute " + k + " FAILED");
		});

		assertSoftly.assertAll();
	}
	
	//@Test(priority = 57, description = "2. Upload HTT File")
	public void UploadHHTFile() {
		selectHhtFilePage = myFleetGenInfoPage.clickEdit()
				                              .selectTelecharger();

		File file = new File(getClass().getClassLoader().getResource(HHT_FILE_S2D4).getFile());

		selectHhtFilePage.uploadHTT(file.getPath())
		                 .clickTelecharger();

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "2-editAndUploadHhtFile");

		boolean uploadConfirmationMsg = selectHhtFilePage.getWebElementText(selectHhtFilePage.getBody())
				.contains(UPLOAD_CONFIRMATION_MSG);

		assertSoftly.assertTrue(uploadConfirmationMsg);

		if (uploadConfirmationMsg) {
			ExtentTestManager.getTest().log(Status.PASS, "2. Upload Hht File");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "2. Upload Hht File");
		}

		assertSoftly.assertAll();
	}

	//@Test(priority = 58, description = "3. Create Recipient with SMS notification")
	public void createRecipientSMS() {
		configurationPage = mainPage.openConfiguration();
		createRecipientPage = configurationPage.openRecipients()
				                               .clickCreateRecipients();

		createRecipientPage.enterFirstName(recipientsDataSms.getFirstName())
		                   .enterLastName(recipientsDataSms.getLastName())
				           .enterEMail(recipientsDataSms.getEmail())
				           .enterPhoneNumber(recipientsDataSms.getTelNo())
				           .scroll("200")
				           .selectCountry(recipientsDataSms.getCountry())
				           .selectLanguage(recipientsDataSms.getLanguage())
				           .checkNotifyIssuesFlag()
				           .checkNotifyUpdatesFlag();

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-createRecipientBeforeSaving");

		createRecipientPage.scroll("-100")
		                   .clickSaveDe();

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-createRecipientAfterSaving");

		configurationPage.scroll("-100").searchRecipientsDe(recipientsDataSms.getFirstName());

		Map<String, Boolean> result = new HashMap<String, Boolean>();

		result.put("firstName",
				configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataSms.getFirstName()));
		result.put("lastName", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataSms.getLastName()));
		result.put("telNo", configurationPage.getTextOfFirstRecipientEntry()
				.contains(recipientsDataSms.getTelNo().replaceAll(" ", "")));
		result.put("eMail", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataSms.getEmail()));
		result.put("language", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataSms.getLanguage()));

		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "3. Create recipient check: attribute " + k + " PASSED");
			else
				ExtentTestManager.getTest().log(Status.PASS, "3. Create recipient check: attribute " + k + " FAILED");
		});

		assertSoftly.assertAll();
	}
	
	//@Test(priority = 59, description = "4. Create Recipient with Email notification")
	public void createRecipientEmail() {
		configurationPage = mainPage.openConfiguration();
		createRecipientPage = configurationPage.openRecipients()
		                                        .clickCreateRecipients();
		
		createRecipientPage.enterFirstName(recipientsDataEmail.getFirstName())
		                   .enterLastName(recipientsDataEmail.getLastName())
		                   .enterEMail(recipientsDataEmail.getEmail())
		                   .enterPhoneNumber(recipientsDataEmail.getTelNo())
		                   .scroll("200")
		                   .selectCountry(recipientsDataEmail.getCountry())
		                   .selectLanguage(recipientsDataEmail.getLanguage())
		                   .checkNotifyIssuesFlag()
		                   .checkNotifyUpdatesFlag();
				                   
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "4-createRecipientBeforeSaving");
		
		createRecipientPage.scroll("-100")
                           .clickSaveDe();
		
        ScreenshotUtil.takeScreenshot(driver,TESTNAME, "4-createRecipientAfterSaving");

        configurationPage.scroll("-100")
                         .searchRecipientsDe(recipientsDataEmail.getFirstName()); 
		
        Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		result.put("firstName", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataEmail.getFirstName()));
		result.put("lastName", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataEmail.getLastName()));
		result.put("telNo", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataEmail.getTelNo().replaceAll(" ","")));
		result.put("eMail", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataEmail.getEmail()));
		result.put("language", configurationPage.getTextOfFirstRecipientEntry().contains(recipientsDataEmail.getLanguage()));
		
		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "4. Create recipient check: attribute " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.PASS, "4. Create recipient check: attribute " + k + " FAILED");			
		});
	      
		assertSoftly.assertAll();
	}
	
	//@Test(priority = 60, description = "5. Assign Recipients")
	public void assignRecipient() {
		configurationPage =mainPage.openConfiguration();
		
		assignRecipientPage = configurationPage.openSources()		
		                                       .clickPlusOfFirstDepot()
		                                       .clickVehicleInSourceTable(vehicleData.getLpn())
		                                       .clickAssignRecipient();
		
		assignRecipientPage.search(recipientsDataEmail.getFirstName())
                           .assingSearchedRecipient(recipientsDataEmail.getFirstName());
                          
		configurationPage.clickAssignRecipient();

		assignRecipientPage.search(recipientsDataSms.getFirstName())
                           .assingSearchedRecipient(recipientsDataSms.getFirstName());

		configurationPage.checkboxEmailAndSms();
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "5-assignRecipients");
	
		assertSoftly.assertTrue(
				configurationPage.getWebElementText(configurationPage.getBody()).contains(recipientsDataSms.getFirstName()));

		boolean successfullyDeleted = configurationPage.getWebElementText(configurationPage.getBody())
				.contains(recipientsDataSms.getFirstName());

		if (successfullyDeleted) {
			ExtentTestManager.getTest().log(Status.PASS, "5. Assign Recipient PASSED");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "5. Assign Recipient FAILED");
		}

		assertSoftly.assertAll();		
	}
	
	// could be implemented in new tab need vnc tuneling
	//@Test(priority = 61, description = "6. Open pressure simulator and modify pressure")
	public void changePressure() {

		driverManager.quitDriver();
		runDriver();
		loginPage = new LoginPage(driver, wait);
		pressureSimulatorPage = new PressureSimulatorPage(driver, wait);

		loginPage.openUrl(propertyManager.getPressureSimulatorUrlAtBu000());

		pressureSimulatorPage.setTempPressure(TEMPERATURE, PRESSURE).clickSubmit();

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "6-openPressureSimultorAndModifyPressure");

		boolean submitPressure = pressureSimulatorPage.getTempValues().equals(StringUtils.EMPTY);

		assertSoftly.assertTrue(submitPressure);

		if (submitPressure) {
			ExtentTestManager.getTest().log(Status.PASS, "6. Open pressure simultor and modify pressure");
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "6. Open pressure simultor and modify pressure");
		}

		driverManager.quitDriver();
		assertSoftly.assertAll();
	}
	
	@Test(priority = 62, description = "7. Verify Email and Sms")
	public void verifyEmailAndSms() {

		//implement for email and/or sms
	}
	
	@Test(priority = 62, description = "8. Delete Recipients")
	public void deleteRecipients() {		
		
		configurationPage.openRecipients()
		                 .deleteFirstRecipient()
		                 .deleteFirstRecipient();
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "8-deletedRecipient");
		
		configurationPage.searchRecipientsFr(recipientsDataSms.getFirstName());
		
        assertSoftly.assertTrue(configurationPage.getWebElementText(configurationPage.getBody()).contains(PAS_DE_DONNÉE_DISPONIBLE));
		
		boolean successfullyDeleted = configurationPage.getWebElementText(configurationPage.getBody()).contains(PAS_DE_DONNÉE_DISPONIBLE);
			
		if(successfullyDeleted) {
			ExtentTestManager.getTest().log(Status.PASS, "8. Delete Recipients PASSED");			
		} else {
			ExtentTestManager.getTest().log(Status.FAIL, "8. Delete Recipients FAILED");			
		}

		assertSoftly.assertAll();		
	}	
}
