package CONTI.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class SelectHhtFilePage extends BasePage<SelectHhtFilePage>{
	
	@FindBy(css = ".lf-ng-md-file-input-tag.md-input")
	private WebElement browse;
	
	@FindBy(xpath = "//input[@type='file']")
	private WebElement hhtInput;
	
	//telecharger button
	@FindBy(css = ".md-raised.pull-right.md-button.md-ink-ripple")
	private List<WebElement> buttons;
	
	public SelectHhtFilePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	public boolean getBrowseButton() {
		return isWebElementDisplayed(browse);
	}

	public SelectHhtFilePage uploadHTT(String path) {
		 hhtInput.sendKeys(path);
		 return this;
	}

	public void clickTelecharger() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		clickElementIfPresent(buttons.get(0));
	}

}
