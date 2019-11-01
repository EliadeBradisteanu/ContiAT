package CONTI.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class GeneralInfoMyFleetPage extends BasePage<GeneralInfoMyFleetPage>{
			
	@FindBy(css = ".vehicle-masterdata-list")
	private WebElement generalInformation;
	
	//@FindBy(css = ".bv-tire.status-ok")
	@FindBy(css = ".bv-tire.status-no-sensor-information")
	private List<WebElement> sensors;
	
	@FindBy(css = ".bv-tire.no-sensor")
	private List<WebElement> noSensors;
	
	@FindBy(css = ".fap-table-button")
	private WebElement edit;
	
	//size 4
	@FindBy(css = ".fap-context-menu ul li")
	private List<WebElement> editOptions;
	
	@FindBy(css = ".fap-toolbar-button.fap-print-button")
	private List<WebElement> exportButton;
	
	@FindBy(css = ".md-primary")
	private List<WebElement> csvExcelButtons;
	
	//size 3
	@FindBy(css = ".fap-back-link")
	private List<WebElement> downloadCancelButtons;
	
	@FindBy(css = ".chart-wrapper")
	private WebElement diagramGrid;
	
	public GeneralInfoMyFleetPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	public String getGeneralInformation() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		return getWebElementText(generalInformation);
	}	
	
	public int getSensorAmount() {
		return sensors.size();
	}

	public GeneralInfoMyFleetPage clickEdit() {
		TimeUtil.sleep(2, TimeUnit.SECONDS);
		clickElementIfPresent(edit);
		
		return this;
	}

	public SelectHhtFilePage selectTelecharger() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(editOptions.get(0));		
		
		return new SelectHhtFilePage(driver, wait);
	}
	
	public GeneralInfoMyFleetPage pressExportButton() {
		TimeUtil.sleep(2, TimeUnit.SECONDS); 
		
		if (exportButton.size() == 4) {
			clickElementIfPresent(exportButton.get(3));
		}else if  (exportButton.size() == 6){
			clickElementIfPresent(exportButton.get(5));
		}else if  (exportButton.size() == 2){
			clickElementIfPresent(exportButton.get(0));
		}
		
		return this;		
	}
	
	public GeneralInfoMyFleetPage selectCsv() {
		clickElementIfPresent(csvExcelButtons.get(2));
		
		return this;
	}
	
	public GeneralInfoMyFleetPage selectExcel() {
		clickElementIfPresent(csvExcelButtons.get(3));
		
		return this;
	}
	
	public void pressDownload() {		
		clickElementIfPresent(downloadCancelButtons.get(3));		
	}

	public boolean getHistoryChart() {
		return isWebElementDisplayed(diagramGrid);		
	}

	
}
