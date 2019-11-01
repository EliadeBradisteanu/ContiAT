package CONTI.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.CsvUtils;
import CONTI.Test.utils.ExcelUtil;
import CONTI.Test.utils.ScreenshotUtil;
import CONTI.Test.utils.extentReports.ExtentTestManager;
import CONTI.pages.AlertsPage;
import CONTI.pages.DashboardPage;
import CONTI.pages.GeneralInfoAlertsPage;
import CONTI.pages.GeneralInfoMyFleetPage;
import CONTI.pages.LoginPage;
import CONTI.pages.MainPage;
import CONTI.pages.MyFleetPage;
import CONTI.utils.TimeUtil;

public class FileDownloadAndContentTest_32275 extends BaseTest {	

	private static final String TESTNAME = "Check download of files as .xls .csv and check static content";
	
	private static final String SHEET_OVERVIEW = "Overview";
	private static final String SHEET_PRESSURE_MINIMUM = "Pressure minimum";
	private static final String SHEET_PRESSURE_AVERAGE = "Pressure average";
	private static final String SHEET_TEMPERATURE_AVERAGE = "Temperature average";
	private static final String SHEET_TEMPERATURE_MAXIMUM = "Temperature maximum";
	
	private static final String CSV = ".csv";
	private static final String XLS = ".xls";
	
	private static final String ALERTS = "Alerts";
	private static final String DETAILS = "Details";
	private static final String MY_FLEET = "MyFleet";
	private static final String MYFLEETDETAILS = "MyFleetDetails";
	private static final String DASHBOARD = "Dashboard";	
	
	private Logger log = Logger.getLogger(FileDownloadAndContentTest_32275.class);

	private SoftAssert assertSoftly = new SoftAssert();	
	
	private LoginPage loginPage;
	private MainPage mainPage;
	private MyFleetPage myFleetPage;
	private DashboardPage dashboardPageN;
	private AlertsPage alertPage;
	private GeneralInfoAlertsPage generalInfoAlertsPage;
	private GeneralInfoMyFleetPage generalInfoMyFleetPage;
	
	@BeforeClass
	public void before() {
		runDriver();
		ExtentTestManager.startTest(TESTNAME, "1. Open Homepage and Verify Login"
				                       + "<br> 2. Download file(.xls) from Alerts"
				                       + "<br> 3. Download files(.cvs and .xls) from Alert Details"
				                       + "<br> 4. Download files(.xls and .cvs) from MyFleet"
				                       + "<br> 5. Download files(.xls and .cvs) from MyFleet Details"
				                       + "<br> 6. Download files(.xls and .cvs) from Dashboard");
		loginPage = new LoginPage(driver, wait);
		loginPage.openUrl(propertyManager.getContiUrl());
	}

	@AfterClass
	public void cleanUp() {				
		mainPage.doLogout();
		getDriver().close();	
		ExtentTestManager.endTest();
	}
	
	@Test(priority = 43, testName = "1. Open Homepage and Verify Login")
	public void openHomePageAndVerifyLogin() {		
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "1-beforeLogin");

		log.info("Login with username: " + propertyManager.getUserMhpcku1());
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
	
	
	@Test(priority = 44, testName = "2. Download files(.xls) from Alerts")
	public void downloadFilesFormAlerts() {	
		alertPage =  mainPage.openContiConnect()
		                     .openAlerts();
		
		alertPage.pressExportButton();
		
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "2-downloadExcelFileAlerts");
		
		List<Boolean> checkFiles = downloadAndCheckFile(ALERTS); 		
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        
        result.put("download .xls", checkFiles.get(0));
        result.put("content .xls, sheet Overview", checkFiles.get(1));
        
        result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "2. Alerts download file: " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.PASS, "2. Alerts download file: " + k + " FAILED");			
		});	
        
        assertSoftly.assertAll();
	}	
	
	@Test(priority = 45, testName = "3. Download files(.cvs and .xls) from Alert Details")
	public void downloadFilesFormMyAlertDetails() {
		TimeUtil.sleep(2, TimeUnit.SECONDS);
		alertPage = mainPage.openContiConnect()
		                    .openAlerts();
		
		generalInfoAlertsPage = alertPage.clickDetailsForFirstAlertInTable();
		
		generalInfoAlertsPage.pressExportButton()
		                     .selectCsv()
		                     .pressDownload();		
		
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "3-downloadCSVFileAlertsDetails");
		
		generalInfoAlertsPage.pressExportButton()
		                     .selectExcel()
		                     .pressDownload() ;

		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "3-downloadExcelFileAlertsDetails");
		
		List<Boolean> checkFiles = downloadAndCheckFile(DETAILS);		
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        
        result.put("download .csv", checkFiles.get(0));
        result.put("content .csv sheet Overview", checkFiles.get(1));
        result.put("download .xls", checkFiles.get(2));
        result.put("content .xls sheet Overview", checkFiles.get(3));
        result.put("content .xls sheet Pressure Minimum", checkFiles.get(4));
        result.put("content .xls sheet Pressure Average", checkFiles.get(5));
        result.put("content .xls sheet Temperature Maximum", checkFiles.get(6));
        result.put("content .xls sheet Temperature Average", checkFiles.get(7));
        
        
        result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "3. Alert Details download file " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.PASS, "3. Alert Details download file " + k + " FAILED");			
		});
        
        assertSoftly.assertAll();
	}
	
	@Test(priority = 46, testName = "4. Download files(.cvs and .xls) from MyFleet")
	public void downloadFilesFormMyFleet() {		
	
		myFleetPage = mainPage.openContiConnect()
                              .openMyFleet();
		
		myFleetPage.pressExportButton()
		           .selectCsv()
		           .pressDownload();
				
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "4-downloadCSVFileMyFleet");
		
		myFleetPage.pressExportButton()
                   .selectExcel()
                   .pressDownload();
	 	
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "4-downloadExcelFileMyFleet");
	
		List<Boolean> checkFiles = downloadAndCheckFile(MY_FLEET);		
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        
        result.put("download .csv", checkFiles.get(0));
        result.put("content .csv sheet Overview", checkFiles.get(1));
        result.put("download xls", checkFiles.get(2));
        result.put("content .xls sheet Overview", checkFiles.get(3));
        
        
        result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "4. MyFleet download file " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.PASS, "4. MyFleet download file " + k + " FAILED");			
		});
        
        assertSoftly.assertAll();		
	}
	
	@Test(priority = 47, testName = "5. Download files(.cvs and .xls) from MyFleet Details")
	public void downloadFilesFormMyFleetDetails() {

		myFleetPage = mainPage.openContiConnect()
				              .openMyFleet();
		
		generalInfoMyFleetPage = myFleetPage.clickDetailsForFirstAlertInTable();
		
		generalInfoMyFleetPage.pressExportButton()
		                      .selectCsv()
		                      .pressDownload();
		
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "5-downloadCSVFileMyFleetDetails");
		
		generalInfoMyFleetPage.pressExportButton()
                              .selectExcel()
                              .pressDownload();
		
		ScreenshotUtil.takeScreenshot(driver, TESTNAME, "5-downloadExcelFileFleetDetails");
		
		List<Boolean> checkFiles = downloadAndCheckFile(MYFLEETDETAILS);
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		result.put("download .csv", checkFiles.get(0));
        result.put("content .csv sheet Overview", checkFiles.get(1));
        result.put("download .xls", checkFiles.get(2));
        result.put("content .xls sheet Overview", checkFiles.get(3));
        result.put("content .xls sheet Pressure Minimum", checkFiles.get(4));
        result.put("content .xls sheet Pressure Average", checkFiles.get(5));
        result.put("content .xls sheet Temperature Maximum", checkFiles.get(6));
        result.put("content .xls sheet Temperature Average", checkFiles.get(7));

		result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "5. MyFleet Details download file " + k + " PASSED");
			else
				ExtentTestManager.getTest().log(Status.PASS, "5. MyFleet Details download file " + k + " FAILED");
		});

		assertSoftly.assertAll();
	}
	
	@Test(priority = 48, testName = "6. Download files(.cvs and .xls) from Dashboard")
	public void downloadFileFormDashboard() {	
		
		dashboardPageN = mainPage.openContiConnect()
				                 .openDashboard();
		
		dashboardPageN.pressExportButton()		             
		              .pressCsv()
		              .pressDownload();
		
		ScreenshotUtil.takeScreenshot(driver,TESTNAME, "6-downloadCSVFileDashboard");	
		
		dashboardPageN.pressExportButton()		             
                       .pressExcel()
                       .pressDownload();

        ScreenshotUtil.takeScreenshot(driver,TESTNAME, "6-downloadExcelFileDashboard");
      
        List<Boolean> checkFiles = downloadAndCheckFile(DASHBOARD);        
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        
        result.put("download .csv", checkFiles.get(0));
        result.put("content .csv sheet Overview", checkFiles.get(1));
        result.put("download .xls", checkFiles.get(2));
        result.put("content .xls sheet Overview", checkFiles.get(3));
        
        result.forEach((k, v) -> {
			assertSoftly.assertTrue(v);
			if (v)
				ExtentTestManager.getTest().log(Status.PASS, "6. Dashboard download file " + k + " PASSED");			
			else
				ExtentTestManager.getTest().log(Status.PASS, "6. Dashboard download file " + k + " FAILED");			
		});       
	
	}	
	
	private List<Boolean> downloadAndCheckFile(String location) {
		
		List<Boolean> checkFile = new ArrayList<>();
		
		File downloads = new File("src/main/resources/downloads");
		downloads.mkdir();
		
		// wait for file to download - xls are bigger
		while (!Arrays.stream(downloads.listFiles())
				      .map(m -> m.getName())
				      .anyMatch(fName -> fName.contains(XLS))) {
			TimeUtil.sleep(2, TimeUnit.SECONDS);			
		}

		// wait for .xls file not to have crdownload in file name
		while (Arrays.stream(downloads.listFiles())
				     .map(m -> m.getName())
				     .anyMatch(fName -> fName.contains("crdownload"))) {
			TimeUtil.sleep(2, TimeUnit.SECONDS);
		}
				
		// list of downloadable files contains current downloaded file
		for (File f : downloads.listFiles()) {
			log.info(f.getName() + " saved");
			assertSoftly.assertTrue(f.length() > 0);
			
			TimeUtil.sleep(2, TimeUnit.SECONDS);			
			checkFile.add(generatePosibleFileNames().contains(f.getName()));
						
			//check static content of files, (ex: titles, column header)
			if (f.getName().contains(XLS)) {
				
				switch (location) {
				
				case ALERTS:
					//Alerts, sheet: Overview							
					checkFile.add(checkFileContentXls(f.getPath(), SHEET_OVERVIEW, ALERTS).equals(propertyManager.getAlertsXlsText()));
					log.info("Alerts, Overview, xls: " + checkFileContentXls(f.getPath(), SHEET_OVERVIEW, ALERTS));
					log.info("Text from properties :   " + propertyManager.getAlertsXlsText());
					break;
					
				case DETAILS:
					//Alerts Details, sheets: Overview, Pressure minimum, Pressure average, Temperature maximum, Temperature maximum
					checkFile.add(checkFileContentXls(f.getPath(), SHEET_OVERVIEW, DETAILS).equals(propertyManager.getAlertsDetailsXlsOverviewText()));
					log.info("Alerts Details, Overview, xls: " + checkFileContentXls(f.getPath(), SHEET_OVERVIEW, DETAILS));
					log.info("AlertsDO text from properties: " + propertyManager.getAlertsDetailsXlsOverviewText());
			
					checkFile.add(checkFileContentXls(f.getPath(), SHEET_PRESSURE_MINIMUM, DETAILS)
							.equals(propertyManager.getAlertsDetailsXlsPressureMinimumText()));
			        log.info("Alerts Details, Pressure minimum, xls: " + checkFileContentXls(f.getPath(), SHEET_PRESSURE_MINIMUM, DETAILS));
			        log.info("AlertsDPressureM text from properties: " + propertyManager.getAlertsDetailsXlsPressureMinimumText());
					
			        checkFile.add(checkFileContentXls(f.getPath(), SHEET_PRESSURE_AVERAGE, DETAILS)
							.equals(propertyManager.getAlertsDetailsXlsPressureAverageText()));
			        log.info("Alerts Details, Pressure average, xls: " + checkFileContentXls(f.getPath(), SHEET_PRESSURE_AVERAGE, DETAILS));
			        log.info("AlertsDPressureA text from properties: " + propertyManager.getAlertsDetailsXlsPressureAverageText());
			        
			        checkFile.add(checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_MAXIMUM, DETAILS)
					        .equals(propertyManager.getAlertsDetailsXlsTemperatureMaximumText()));
			        log.info("Alerts Details, Temp max, xls: " + checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_MAXIMUM, DETAILS));
			        log.info("AlertTM, text from properties: " + propertyManager.getAlertsDetailsXlsTemperatureMaximumText());
			
			
			        checkFile.add(checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_AVERAGE, DETAILS)
			                   .equals(propertyManager.getAlertsDetailsXlsTemperatureAverageText()));
			        log.info("Alerts Details, Temp avr, xls: " + checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_AVERAGE, DETAILS));
			        log.info("AlertTA, text from properties: " + propertyManager.getAlertsDetailsXlsTemperatureAverageText());			        
			        break;
			        
				case MY_FLEET:
					//My Fleet, sheet: Overview
					checkFile.add(checkFileContentXls(f.getPath(), SHEET_OVERVIEW, MY_FLEET).equals(propertyManager.getMyfleetXlsText()));
					log.info("My Fleet, Overview, xls: " + checkFileContentXls(f.getPath(), SHEET_OVERVIEW, MY_FLEET));
					log.info("My FleetO txt from prop: " + propertyManager.getMyfleetXlsText());				
					break;
					
				case MYFLEETDETAILS:
					//Alerts Details, sheets: Overview, Pressure minimum, Pressure average, Temperature maximum, Temperature maximum
					checkFile.add(checkFileContentXls(f.getPath(), SHEET_OVERVIEW, MYFLEETDETAILS).equals(propertyManager.getMyfleetDetailsXlsOverviewText()));
					log.info("Alerts Details, Overview, xls: " + checkFileContentXls(f.getPath(), SHEET_OVERVIEW, MYFLEETDETAILS));
					log.info("AlertsDO text from properties: " + propertyManager.getMyfleetDetailsXlsOverviewText());
			
					checkFile.add(checkFileContentXls(f.getPath(), SHEET_PRESSURE_MINIMUM, MYFLEETDETAILS)
							.equals(propertyManager.getMyfleetDetailsXlsPressureMinimumText()));
			        log.info("Alerts Details, Pressure minimum, xls: " + checkFileContentXls(f.getPath(), SHEET_PRESSURE_MINIMUM, MYFLEETDETAILS));
			        log.info("AlertsDPressureM text from properties: " + propertyManager.getMyfleetDetailsXlsPressureMinimumText());
					
			        checkFile.add(checkFileContentXls(f.getPath(), SHEET_PRESSURE_AVERAGE, MYFLEETDETAILS)
							.equals(propertyManager.getMyfleetDetailsXlsPressureAverageText()));
			        log.info("Alerts Details, Pressure average, xls: " + checkFileContentXls(f.getPath(), SHEET_PRESSURE_AVERAGE, MYFLEETDETAILS));
			        log.info("AlertsDPressureA text from properties: " + propertyManager.getMyfleetDetailsXlsPressureAverageText());
			        
			        checkFile.add(checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_MAXIMUM, MYFLEETDETAILS)
					        .equals(propertyManager.getMyfleetDetailsXlsTemperatureMaximumText()));
			        log.info("Alerts Details, Temp max, xls: " + checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_MAXIMUM, MYFLEETDETAILS));
			        log.info("AlertTM, text from properties: " + propertyManager.getMyfleetDetailsXlsTemperatureMaximumText());
			
			
			        checkFile.add(checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_AVERAGE, MYFLEETDETAILS)
			                   .equals(propertyManager.getMyfleetDetailsXlsTemperatureAverageText()));
			        log.info("Alerts Details, Temp avr, xls: " + checkFileContentXls(f.getPath(), SHEET_TEMPERATURE_AVERAGE, MYFLEETDETAILS));
			        log.info("AlertTA, text from properties: " + propertyManager.getMyfleetDetailsXlsTemperatureAverageText());			        
			        break;	
			        
				case DASHBOARD:
					//Dashboard, sheet: Overview
					checkFile.add(checkFileContentXls(f.getPath(), SHEET_OVERVIEW, DASHBOARD).equals(propertyManager.getDashboardXlsText()));
					log.info("Dashboard, Overview, xls: " + checkFileContentXls(f.getPath(), SHEET_OVERVIEW, DASHBOARD));
					log.info("DashboardO txt from prop: " + propertyManager.getDashboardXlsText());				
			        break;    
					
				default: break;
				}			
				
			
			} else if (f.getName().contains(CSV)) {							
				
				switch (location) {
				
				case DETAILS:
					checkFile.add(checkFileContentCsv(f.getPath(), DETAILS).equals(propertyManager.getAlertsDetailsCvs()));
					log.info("Alerts Details, Overview, cvs: " + checkFileContentCsv(f.getPath(), DETAILS));
					log.info("AlertsDO text from properties: " + propertyManager.getAlertsDetailsCvs());		
					break;
			    
			    //sometimes it reads forever the file
				case MY_FLEET:					
					checkFile.add(checkFileContentCsv(f.getPath(), MY_FLEET).equals(propertyManager.getMyfleetCsvText()));
					log.info("My Fleet, Overview, cvs: " + checkFileContentCsv(f.getPath(), MY_FLEET));
					log.info("My FleetO txt from prop: " + propertyManager.getMyfleetCsvText());		
					break;
					
				case MYFLEETDETAILS:
					checkFile.add(checkFileContentCsv(f.getPath(), MYFLEETDETAILS).equals(propertyManager.getMyfleetDetailsCvs()));
					log.info("Alerts Details, Overview, cvs: " + checkFileContentCsv(f.getPath(), MYFLEETDETAILS));
					log.info("AlertsDO text from properties: " + propertyManager.getMyfleetDetailsCvs());		
					break;	
			        
				case DASHBOARD:
					checkFile.add(checkFileContentCsv(f.getPath(), DASHBOARD).equals(propertyManager.getDashboardCsvText()));
					log.info("Dashboard, Overview, csv: " + checkFileContentCsv(f.getPath(), DASHBOARD));
					log.info("DashboardO txt from prop: " + propertyManager.getDashboardCsvText());		
			        break;    
					
			        
				default: break;
				}   
			}	
   	     }	
	
		
		//for debug
		/*System.out.println(checkFile.size());
		System.out.println(checkFile);
		System.out.println("exit");
		System.exit(0);*/
		
		//delete download dir
		for(File f : new File("src/main/resources").listFiles()) {			
			if(f.getName().contains("downloads")) {				
				try {
					FileUtils.deleteDirectory(f);
				} catch (IOException e) {
					log.error("could not delete downloads directory", e);
					e.printStackTrace();
				}
			}
		}
		
		return checkFile; 
	}
	
	//generate the possible name combination of downloaded files
	private List<String> generatePosibleFileNames() {
		String curentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-d"));	
		String[] fileFirstPart = propertyManager.getDownloadedFileNames().split("\\|");
		
		String[] fileExtension = {XLS, CSV};
		List<String> names = new ArrayList<String>();
		
		for(String f:fileFirstPart) {
			  StringBuilder buff = new StringBuilder();
			  buff.append(f).append(curentDate);
			    for(String ff: fileExtension) {
			    	buff.append(ff);   
			    	names.add(buff.toString());
			    	buff.delete(buff.length()-4,buff.length());
			    }
		  }		
		
		return names;
	}
	
	private String checkFileContentXls(String path, String sheetName, String location) {
		ExcelUtil.setExcelFile(path, sheetName);
		StringBuilder sb = new StringBuilder();

		switch (location) {
		case ALERTS:
			for (int i = 0; i < 6; i++)
				sb.append(ExcelUtil.getRowData(i));			
			break;
			
		case DETAILS:
			if (sheetName.equals(SHEET_OVERVIEW)) {
				for (int i = 0; i < 7; i++) {
					if (i == 3)
						continue;
					sb.append(ExcelUtil.getRowData(i));

				}
			} else {
				sb.append(ExcelUtil.getRowData(0));
				sb.append(ExcelUtil.getRowData(1));
			}

			break;
			
		case MY_FLEET:	
			for (int i = 0; i < 6; i++) {
				if (i == 4)	continue;
				sb.append(ExcelUtil.getRowData(i));
			}
			break;
			
		case MYFLEETDETAILS:
			if (sheetName.equals(SHEET_OVERVIEW)) {
				for (int i = 0; i < 7; i++) {
					if (i == 3)
						continue;
					sb.append(ExcelUtil.getRowData(i));

				}
			} else {
				sb.append(ExcelUtil.getRowData(0));
				sb.append(ExcelUtil.getRowData(1));
			}

			break;	
			
		case DASHBOARD:	
			for (int i = 0; i < 29; i++) {
				if (i == 5 )	continue;
				if(i == 6) {sb.append(ExcelUtil.getCellData(6, 0)); continue;}
				if (i == 11)	continue;
				if(i == 12) {sb.append(ExcelUtil.getCellData(12, 0)); continue;}
				if (i == 24)	continue;
				if(i == 25) {sb.append(ExcelUtil.getCellData(25, 0)); continue;}
				sb.append(ExcelUtil.getRowData(i).replaceAll("[0-9]", ""));
			}
			break;	
			
			
		default:
			sb.append("");
		}

		return sb.toString();
	}
	
	private String checkFileContentCsv(String path, String location) {
		StringBuilder sb = new StringBuilder();

		// it reads the csv file line by line so we skip lines with non static text
		switch (location) {
		
		case DETAILS:
			for (int i = 0; i < CsvUtils.getCsvData(path).size(); i++) {
				if (i == 0 || i == 1 || i == 2 || i == 4 || i == 5 || i == 10 || i == 11 || i == 13 || i == 14
						|| i == 16 || i == 17 || i == 19 || i == 20)
					sb.append(CsvUtils.getCsvData(path).get(i).replaceAll(",", ""));
			}
			break;
			
		case MY_FLEET:
			for (int i = 0; i < CsvUtils.getCsvData(path).size(); i++) {
				if (i == 0 || i == 1 || i == 2 || i == 3)
					sb.append(CsvUtils.getCsvData(path).get(i).replaceAll(",", ""));
			}			
			break;
			
		case MYFLEETDETAILS:
			for (int i = 0; i < CsvUtils.getCsvData(path).size(); i++) {
				if (i == 0 || i == 1 || i == 2 || i == 4 || i == 5 || i == 10 || i == 11 || i == 13 || i == 14
						|| i == 16 || i == 17 || i == 19 || i == 20)
					sb.append(CsvUtils.getCsvData(path).get(i).replaceAll(",", ""));
			}
			break;	
			
		case DASHBOARD:
			for(int i=0; i<CsvUtils.getCsvData(path).size(); i++) {
				if(!(i==3))			
				sb.append(CsvUtils.getCsvData(path).get(i).replaceAll("[0-9,]", ""));
			}			
			break;	
			
		default:
			sb.append("");
		}

		return sb.toString();
	}
}
