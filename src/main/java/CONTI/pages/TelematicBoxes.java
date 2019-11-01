package CONTI.pages;

import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TelematicBoxes extends BasePage<MainPage>{
	
	@FindBy(className = "cpc-searchfield")
	private WebElement serialNumber;
	
	@FindBy(css = ".md-raised.md-button.md-ink-ripple")
	private WebElement searchButton;
	
	public TelematicBoxes(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);		
	}
	
	private void enterSerialNumber(String serialNumber) {		
		enterKeysIntoWebElement(Optional.of(this.serialNumber), serialNumber);
	}
	
	public void searchTelematicsBoxes(String searchString) {		
		enterSerialNumber(searchString);
		clickElementIfPresent(searchButton);		
	}
}
