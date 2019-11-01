package CONTI.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import CONTI.utils.TimeUtil;

public class MyFleetPage extends BasePage<MyFleetPage> {

	private static final String PRESSION_TRÈS_BASSE = "Pression très basse";

	// ----------- KPIs --------------

	// size 3, V. Monitored, V. Green and Incomplete V.
	@FindBy(css = ".kpi-value")
	private List<WebElement> kpis;
	
	@FindBy(css = ".icon-gen-no-sensor")
	private List<WebElement> allGreyEntries;
	
	@FindBy(css = ".icon-gen-ok-no-sensor")
	private List<WebElement> allGreyEntries1;	
		
	@FindBy(css = ".icon-gen-ok")
	private List<WebElement> allGreenEntries;	
	
	@FindBy(css =  ".icon-gen-warning")
	private List<WebElement> allYellowEntries;
	
	@FindBy(css = ".icon-gen-critical")
	private List<WebElement> allRedEntries;
	
	@FindBy(css = ".vehicle-detail-row td:nth-child(3)")
	private List<WebElement> pressionColumns;
	
	@FindBy(css = ".vehicle-detail-row td:nth-child(6)")
	private List<WebElement> alertsColumns;

	// size 4
	@FindBy(css = ".btn.btn-default")
	private List<WebElement> buttons10_25_50_100;

	// size 11
	@FindBy(css = ".pagination.ng-table-pagination li a")
	private List<WebElement> paginationButtons;

	@FindBy(css = ".pagination.ng-table-pagination li")
	private List<WebElement> nextButtonStatus;
	
	// ----------- Export download --------------
	
	@FindBy(css = ".fap-toolbar-button.fap-print-button")
	private List<WebElement> printButtons;	
	
	@FindBy(css = ".md-primary")
	private List<WebElement> csvExcelButtons;
	
	@FindBy(css = ".fap-back-link")
	private List<WebElement> downloadCancelButtons;
	
	// ----------- Filter search --------------

	@FindBy(css = ".fap-toolbar-button.fap-filter-button")
	private WebElement filterButton;

	@FindBy(css = ".filter-lpn")
	private List<WebElement> filterLPN;

	@FindBy(css = "#filter-customerVehicleId")	
	private List<WebElement> filterCustomerVehicleId;

	@FindBy(css = ".fap-table-button")
	private WebElement detailsButton;	
	
	// ----------- Search Table --------------
	@FindBy(css = ".vehicle-entry tr:nth-child(1)")
	private WebElement firstEntryInTable;
	
	@FindBy(css = ".vehicle-row > td")
	private List<WebElement> vehicleTableCelss;
	

	public MyFleetPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}

	public String getKpiMonitored() {
		
		System.out.println("size"+kpis.size());
		
		String monitoredKpi = "";
		
		if(kpis.size() == 3 || kpis.size() == 2) {
			monitoredKpi = getWebElementText(kpis.get(0));
		} else if (kpis.size() == 8) {
			monitoredKpi = getWebElementText(kpis.get(5));
		}
		
		return monitoredKpi;
	}

	public String getKpiGreen() {
		String greenKpi = "";

		if (kpis.size() == 3 || kpis.size() == 2) {
			greenKpi = getWebElementText(kpis.get(1));
		} else if (kpis.size() == 8) {
			greenKpi = getWebElementText(kpis.get(6));
		}

		return greenKpi;
	}

	public String getKpiIncomplete() {			
		String incompleteKpi = "";

		if (kpis.size() == 3 || kpis.size() == 2) {
			incompleteKpi = getWebElementText(kpis.get(2));
		} else if (kpis.size() == 8) {
			incompleteKpi = getWebElementText(kpis.get(7));
		}

		return incompleteKpi;
	}
	
	/**
	 * counts all grey entries (kpi red), all green entries (kpi green), all green+yellow+red entries (kpi blue)
	 * @return int array [all grey entries (kpi red), all green entries (kpi green), all green+yellow+red entries (kpi blue)]
	 * @throws InterruptedException 
	 */
	public int[] countEntries() {
		int[] counters = { 0, 0, 0 };		     
		
		if (!(nextButtonStatus.size() == 0))
		      scrollIntoView(paginationButtons.get(0));
		
		
		/*int k=1;
		int red=0;
		int green = 0;
		int yellow = 0;
		int grey = 0;
		int grey1 = 0;
		
		while(k <93) {
			
			System.out.println("-------- "+k++ +" --------------");
			
			System.out.println("red: " + (red+=allRedEntries.size()-1));
			System.out.println("ylw: " + (yellow+=allYellowEntries.size()-1));
			System.out.println("grn: " + (green+=allGreenEntries.size()-1));
			System.out.println("gry: " + (grey+=allGreyEntries.size()-1));
			System.out.println("gy1: " + (grey1+=allGreyEntries1.size()));
			
			clickElementIfPresent(paginationButtons.get(10));
			scrollIntoView(paginationButtons.get(0));
		}
		
		
		System.out.println("exit");System.exit(0);*/
		
		if (!(nextButtonStatus.size() == 0)) {

			while (!getAttribute("class", nextButtonStatus.get(10)).equals("disabled")) {

				// one element is shown in filter area, this has to be substracted
				counters[0] += allGreyEntries.size() - 1;
				counters[1] += allGreenEntries.size() - 1;
				counters[2] += allGreenEntries.size() + allRedEntries.size() + allYellowEntries.size() - 3;

				clickElementIfPresent(paginationButtons.get(10));
				scrollIntoView(paginationButtons.get(0));

				TimeUtil.sleep(500, TimeUnit.MILLISECONDS);
			}
		} else {
			counters[0] += allGreyEntries.size() - 1;
			counters[1] += allGreenEntries.size() - 1;
			counters[2] += allGreenEntries.size() + allRedEntries.size() + allYellowEntries.size() - 3;
		}

		return counters;
	}

	public MyFleetPage click100EntriesInTable() {
		clickElementIfPresent(buttons10_25_50_100.get(3));

		return this;
	}
	
	public MyFleetPage click10EntriesInTable() {
		clickElementIfPresent(buttons10_25_50_100.get(0));

		return this;
	}
	
	// ----------- Export download--------------
	public MyFleetPage pressExportButton() {	
		TimeUtil.sleep(2, TimeUnit.SECONDS);		
		
		if (printButtons.size() == 3) {
			clickElementIfPresent(printButtons.get(1));
		}else if  (printButtons.size() == 5){
			clickElementIfPresent(printButtons.get(3));
		}
		
		return this;
	}
	
	public MyFleetPage selectCsv() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(csvExcelButtons.get(0));
		
		return this;
	}
	
	public MyFleetPage selectExcel() {
		clickElementIfPresent(csvExcelButtons.get(1));
		
		return this;
	}
	
	public void pressDownload() {
		clickElementIfPresent(downloadCancelButtons.get(0));
	}
	

	// ----------- Filter search --------------

	public void clickOnFilterButton() {
		clickElementIfPresent(filterButton);
	}

	public void filter(String customerVehicleId, String lpn) {	
		clickOnFilterButton();	
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		
		if(filterLPN.size() == 2) {
			enterKeysIntoWebElement(Optional.of(filterLPN.get(1)), lpn);
			enterKeysIntoWebElement(Optional.of(filterCustomerVehicleId.get(1)), customerVehicleId);
		}else {
			enterKeysIntoWebElement(Optional.of(filterLPN.get(0)), lpn);
			enterKeysIntoWebElement(Optional.of(filterCustomerVehicleId.get(0)), customerVehicleId);
		}			
		
		//wait.until(ExpectedConditions.visibilityOf(detailsButton));
	}

	public GeneralInfoMyFleetPage clickDetailsForFirstAlertInTable() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(detailsButton);
		return new GeneralInfoMyFleetPage(driver, wait);
	}

	public MyFleetPage clickFirstEntryInTable() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(firstEntryInTable);
		
		return this;
	}
	
	public GeneralInfoMyFleetPage clickDetailsForVehicleID(String vehicleId) {				
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		WebElement parent = getParentOfWebElement(getWebElementFromListByText1(vehicleTableCelss, vehicleId));
		clickElementIfPresent(parent.findElement(By.className("fap-table-button")));
			
		return new GeneralInfoMyFleetPage(driver, wait);
	}

	public MyFleetPage filterCustomerVehicleId(String customerVehicleId) {		
		clickOnFilterButton();					
		if(filterCustomerVehicleId.size() == 2) {
			enterKeysIntoWebElement(Optional.of(filterCustomerVehicleId.get(1)), customerVehicleId);
		}else {			
			enterKeysIntoWebElement(Optional.of(filterCustomerVehicleId.get(0)), customerVehicleId);
		}
		
		wait.until(ExpectedConditions.visibilityOf(detailsButton));
		return this;
	}

	public MyFleetPage filterCustomerVehicleIdFr(String customerVehicleId) {
		clickOnFilterButton();
		if(filterCustomerVehicleId.size() == 2) {
			enterKeysIntoWebElement(Optional.of(filterCustomerVehicleId.get(1)), customerVehicleId);
		}else {			
			enterKeysIntoWebElement(Optional.of(filterCustomerVehicleId.get(0)), customerVehicleId);
		}
		wait.until(ExpectedConditions.visibilityOf(detailsButton));
		return this;
	}

	public MyFleetPage checkCriticalIcon() {
		Assert.assertEquals(allRedEntries.size(), 3);
		
		return this;
	}

	public void checkPresureValueAndAlertsMessages() {
		StringBuilder sb = new StringBuilder();
		
		//check values have changed
		pressionColumns.forEach(t -> sb.append(t.getText().replaceAll("\\n", " ")));		
		Assert.assertEquals(sb.toString().split("0,0",-1).length-1, 12);
		
		//check alerts column have message
		alertsColumns.forEach(t -> Assert.assertEquals(t.getText(),PRESSION_TRÈS_BASSE));		
	}
}
