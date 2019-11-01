package CONTI.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import CONTI.utils.TimeUtil;

public class PressureSimulatorPage extends BasePage<PressureSimulatorPage>{
	
	@FindBy(id = "temp")
	private List<WebElement> temps;	
	
	@FindBy(id = "pressure")
	private List<WebElement> pressures;	
	
	@FindBy(tagName = "input")
	private List<WebElement> inputs;
	
	public PressureSimulatorPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	}
	
	
	/**
	 * @param temperatureC temerature in Celsius
	 * @param pressurePascal pressure in Pascal
	 */
	public PressureSimulatorPage setTempPressure(String temperatureC, String pressurePascal) {
		temps.stream()
		     .limit(6)
		     .forEach(t -> enterKeysIntoWebElement(Optional.of(t), temperatureC));
		
		pressures.stream()
		         .limit(6)
		         .forEach(p -> enterKeysIntoWebElement(Optional.of(p), pressurePascal));
		
		return this;
	}
	
	public PressureSimulatorPage clickSubmit() {		
		clickElementIfPresent(getWebElementFromListByAttribute(inputs, "type", "submit"));
		TimeUtil.sleep(2, TimeUnit.SECONDS);
		
		return this;
	}
	
	//a slopy way to check that the form was submitted
	public String getTempValues() {
		StringBuilder sb = new StringBuilder();		
		temps.forEach(t -> sb.append(t.getText()));
		
		return sb.toString();
	}

}
