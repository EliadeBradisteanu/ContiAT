package CONTI.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.PropertyManager;
import CONTI.utils.TimeUtil;

public class BasePage<T extends BasePage<T>> {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected JavascriptExecutor js;
	protected Actions actions;
	protected static PropertyManager propertyManager;

	@FindBy(tagName = "body")
	protected WebElement body;

	public BasePage(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
		propertyManager = PropertyManager.getInstance();
		js = (JavascriptExecutor) driver;
		actions = new Actions(driver);
		PageFactory.initElements(driver, this);
	}

	@SuppressWarnings("unchecked")
	public T openUrl(String url) {		
		driver.get(url);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T closeWindow() {
		driver.close();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T refreshWindow() {
		driver.navigate().refresh();
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T back() {
		driver.navigate().back();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T openNewTab() {
		js.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T switchToFirstTab() {
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T switchToSecondTab() {
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));

		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T closeCurrentTab() {
		driver.close();
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));

		return (T) this;
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public WebElement getBody() {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		return body;
	}	
	
	public void clickElementIfPresent(WebElement webElement) {
		wait.ignoring(StaleElementReferenceException.class)
		    .until(ExpectedConditions.elementToBeClickable(webElement));
		
		actions.moveToElement(webElement).click().perform();
	}
	
	//needed for setting the tire pressures
	public void clickWebElementToOffset(WebElement webElement, int x, int y) {	
		wait.ignoring(StaleElementReferenceException.class)
	        .until(ExpectedConditions.elementToBeClickable(webElement));
		
		actions.moveToElement(webElement).moveByOffset(x, y).click().build().perform();
	}
	
	public void enterKeysIntoWebElement(Optional<WebElement> webElement, String keys) {
		  wait.ignoring(StaleElementReferenceException.class)
		      .until(ExpectedConditions.elementToBeClickable(webElement.orElse(null)));
	
	    actions.moveToElement(webElement.orElse(null));
	    webElement.get().clear();
	    actions.click().sendKeys(keys).perform();
	}
	
	public void pressEnter(WebElement webElement) {
		webElement.sendKeys(Keys.ENTER);
	}
	
	public boolean isWebElementDisplayed(WebElement webElement) {
		wait.until(ExpectedConditions.visibilityOf(webElement));
		return webElement.isDisplayed();
	}
	
	@SuppressWarnings("unchecked")
	public T scroll(String pixel) {
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		js.executeScript("window.scrollBy(0,"+pixel+")");
		
		return (T) this;	
	}
	
	@SuppressWarnings("unchecked")
	public T scrollIntoView(WebElement webElement) {
		js.executeScript("arguments[0].scrollIntoView(true);", webElement);		
		return (T) this;	
	}
	
	public WebElement getParentOfWebElement(WebElement webElement) {
		return (WebElement) js.executeScript("return arguments[0].parentNode;", webElement);
	}
	
	public String getAttribute(String attribute, WebElement webElement) {
		wait.until(ExpectedConditions.visibilityOf(webElement));		
		return webElement.getAttribute(attribute).trim();
	}
	
	public String getWebElementText(WebElement webElement) {
		wait.until(ExpectedConditions.visibilityOf(webElement));
		TimeUtil.sleep(1, TimeUnit.SECONDS);
		return webElement.getText();
	}
	
	public String getWebElementText(WebElement webElement, String text) {
		wait.ignoring(StaleElementReferenceException.class)
	      .until(ExpectedConditions.textToBePresentInElement(webElement, text));		
		return webElement.getText();
	}
	
	protected WebElement getWebElementFromListByText(List<WebElement> elements, String text) {
		elements.removeAll(Collections.singleton(null));
		return elements.stream()
				       .filter(e -> e.getText().contains(text))
				       .findAny().orElse(null);				     
	}
	
	protected WebElement getWebElementFromListByText1(List<WebElement> elements, String text) {
		elements.removeAll(Collections.singleton(null));
		return elements.stream()
				       .filter(e -> e.getText().equals(text))
				       .findAny().orElse(null);				     
	}

	protected WebElement getWebElementFromListByAttribute(List<WebElement> elements, String attribute, String text) {
		return elements.stream()
				       .filter(t -> (t.getAttribute(attribute) != null ))
			           .filter(e -> e.getAttribute(attribute).equals(text))
			           .findAny().orElse(null);
	}
}
