package CONTI.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class GeneralInfoAlertsPage extends BasePage<GeneralInfoAlertsPage>{
	
	@FindBy(css = ".chart-wrapper")
	private WebElement diagramGrid;
	
	@FindBy(css = ".fap-toolbar-button.fap-print-button")
	private List<WebElement> exportButton;
	
	@FindBy(css = ".md-primary")
	private List<WebElement> csvExcelButtons;
	
	//size 3
	@FindBy(css = ".fap-back-link")
	private List<WebElement> downloadCancelButtons;
	
	
	public GeneralInfoAlertsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);		
	}
	

	public boolean getHistoryChart() {
		return isWebElementDisplayed(diagramGrid);
	}

	public GeneralInfoAlertsPage pressExportButton() {
		TimeUtil.sleep(1, TimeUnit.SECONDS); 
		
		if(exportButton.size()==2) {
			clickElementIfPresent(exportButton.get(1));
		}else {
			clickElementIfPresent(exportButton.get(2));
		}
		
			
		System.out.println(exportButton.size());
		
		return this;		
	}
	
	public GeneralInfoAlertsPage selectCsv() {
		clickElementIfPresent(csvExcelButtons.get(0));
		
		return this;
	}
	
	public GeneralInfoAlertsPage selectExcel() {
		clickElementIfPresent(csvExcelButtons.get(1));
		
		return this;
	}
	
	public void pressDownload() {
		clickElementIfPresent(downloadCancelButtons.get(1));		
	}

}
