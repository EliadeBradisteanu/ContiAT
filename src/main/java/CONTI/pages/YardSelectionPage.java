package CONTI.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YardSelectionPage extends BasePage<YardSelectionPage>{
	
	@FindBy(xpath = "//md-select")
	private WebElement yardSelector;
	
	@FindBy(css = ".md-checkbox-enabled.md-ink-ripple")
	private List<WebElement> allYardReaders;
	
	@FindBy(css = ".fap-table-button")
	private WebElement continueButton;
	
	public YardSelectionPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);		
	}

	public YardSelectionPage selectYardReader(String yardID) {		
		clickElementIfPresent(yardSelector);
		
		WebElement selection = allYardReaders.stream()
                                             .filter(x -> x.getAttribute("value")
                                             .equals(yardID))
                                             .findAny().get();
		clickElementIfPresent(selection);
		clickWebElementToOffset(selection, 50, 50);	
		
		return this;		
	}
	
	public void clickContinue() {
		clickElementIfPresent(continueButton);
	}

}
