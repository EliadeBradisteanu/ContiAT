package CONTI.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class GeneralDataYardReaderPage extends BasePage<GeneralDataYardReaderPage>{
	
	@FindBys({ @FindBy(css = ".ng-pristine.ng-untouched.ng-valid.md-input.ng-not-empty") })
	private List<WebElement> genaralDatas;
	
	@FindBy(name = "fleetCustName")
	private WebElement fleetName;
	
	//size 2
	@FindBy(css = ".md-select-icon")
	private List<WebElement> yardReaderStatus;
	
	@FindBy(css = ".md-text.ng-binding")	
	private List<WebElement> statusActiveInactive;	
	
	
	@FindBy(xpath = "//*[@name='yardrStatus']//div")
	WebElement currentStatus;
	
	@FindBy(xpath = "//md-icon[@aria-label='Save yard reader']")
	private WebElement saveButton;
	
	@FindBy(xpath = "//a[@type='button' and text()='Save']")
	private WebElement saveConfirmation;
	
	public GeneralDataYardReaderPage(WebDriver driver, WebDriverWait wait) {
		super(driver,wait);
	}
	
	public String getFleetId() {
		return getAttribute("value", genaralDatas.get(1));
	}
	
	public String getDepotId() {
		return getAttribute("value", genaralDatas.get(3));
	}
	
	public String getYardReaderId() {	
		return getAttribute("value", genaralDatas.get(4));
	}
	
	public String getStatus() {
		return currentStatus.getText().trim();
	}
	
	public SearchAccountPage openFleetSearch() {
		clickElementIfPresent(fleetName);
		return new SearchAccountPage(driver, wait);
	}
	
	public void clickOnSave() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(saveButton);
		clickElementIfPresent(saveConfirmation);		
	}
	
	public GeneralDataYardReaderPage setYardReaderStatusToActive() {	
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(yardReaderStatus.get(0));
		clickElementIfPresent(getWebElementFromListByText(statusActiveInactive, "Active"));
			
		return this;
	}
	
	public GeneralDataYardReaderPage setYardReaderStatusToInactive() {	
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(yardReaderStatus.get(0));	
		clickElementIfPresent(getWebElementFromListByText(statusActiveInactive, "Inactive"));
			
		return this;
	}
	
}
