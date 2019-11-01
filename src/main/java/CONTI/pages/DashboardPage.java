package CONTI.pages;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class DashboardPage extends BasePage<DashboardPage>{
	
	@FindBy(css = ".tree-branch-head:nth-child(1)")
	private WebElement openDepotDropDown;
	
	@FindBy(css = ".tree-collapsed:nth-child(1) div span")
	private WebElement firstDepotInDropDown;
	
	// ----------- Export download --------------
	
	@FindBy(css = ".fap-toolbar-button.fap-print-button")
	private List<WebElement> exportButton;
	
	@FindBy(css = ".md-primary")
	private List<WebElement> csvExcelButtons;
	
	@FindBy(css = ".fap-back-link")
	private List<WebElement> downloadCancelButtons;
	
	public DashboardPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	public void openDepotDropDown() {
		// can take up to 90s to load
		wait.withTimeout(Duration.ofSeconds(90)).until(ExpectedConditions.elementToBeClickable(openDepotDropDown));
		clickElementIfPresent(openDepotDropDown);
	}
	
	public WebElement getFirstDepotFromDropDown() {
		return firstDepotInDropDown;
	}
	
	// ----------- Export download --------------
	
	public DashboardPage pressExportButton() {
        TimeUtil.sleep(2, TimeUnit.SECONDS);  
              
        if (exportButton.size() == 2) {
			clickElementIfPresent(exportButton.get(0));
		}else if  (exportButton.size() == 1){
			clickElementIfPresent(exportButton.get(0));
		}else if  (exportButton.size() == 3){
			clickElementIfPresent(exportButton.get(2));
		}        
        
		return this;
	}
	
	public DashboardPage pressCsv() {
		clickElementIfPresent(csvExcelButtons.get(0));
		
		return this;
	}
	
	public DashboardPage pressExcel() {
		clickElementIfPresent(csvExcelButtons.get(1));
		
		return this;
	}
	
	public void pressDownload() {
		clickElementIfPresent(downloadCancelButtons.get(0));		
	}

}
