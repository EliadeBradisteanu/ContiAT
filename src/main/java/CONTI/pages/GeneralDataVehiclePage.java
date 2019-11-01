package CONTI.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GeneralDataVehiclePage extends BasePage<GeneralDataVehiclePage>{	
		
	@FindBys({ @FindBy(css = ".ng-pristine.ng-untouched.ng-valid.md-input.ng-not-empty") })
	private List<WebElement> genaralDatas;
	
	@FindBys({ @FindBy(css = ".axle-information tspan") })
	private List<WebElement> axleInformations;	
	
	public GeneralDataVehiclePage(WebDriver driver, WebDriverWait wait) {
		super(driver,wait);
	}
	
	public String getFleetId() {
		return getAttribute("value",genaralDatas.get(0));
	}
	
	public String getFleetName() {
		return getAttribute("value", genaralDatas.get(1));
	}
	
	public String getDepotId() {
		return getAttribute("value", genaralDatas.get(2));
	}
	
	public String getDepotName() {
		return getAttribute("value",genaralDatas.get(3));
	}
	
	public String getStatus() {
		return getAttribute("value",genaralDatas.get(4));
	}
	
	public String getVehicleType() {
		return getAttribute("value", genaralDatas.get(5));
	}

	public String getAxleConfiguration() {
		return getAttribute("value", genaralDatas.get(6));
	}
	
	public String getCustomerVehicleId() {
		return getAttribute("value", genaralDatas.get(7));
	}
	
	public String getPressureUnit(int nrOfTargetPressure) {
		if(nrOfTargetPressure == 2) {
			return getAttribute("value", genaralDatas.get(10));
		}
		return getAttribute("value", genaralDatas.get(10));
	}

	
	public String getTemperatureUnit(int nrOfTargetPressure) {
		if(nrOfTargetPressure == 2) {
			return getAttribute("value", genaralDatas.get(11));
		}
		return getAttribute("value", genaralDatas.get(11));
	}
	
	public String getLPN(int nrOfTargetPressure) {
		if(nrOfTargetPressure == 2) {			
			return getAttribute("value", genaralDatas.get(12));
		}
		return getAttribute("value", genaralDatas.get(12));
	}	
	
	public String getLpnCountry(int nrOfTargetPressure) {
		if(nrOfTargetPressure == 2) {
			return getAttribute("value", genaralDatas.get(13));
		}
		return getAttribute("value", genaralDatas.get(13));
	}

	public String getMake(int nrOfTargetPressure) {
		if(nrOfTargetPressure == 2) {
			return getAttribute("value", genaralDatas.get(14));
		}
		return getAttribute("value", genaralDatas.get(14));
	}


	public String getBilling(int nrOfTargetPressure) {
		if(nrOfTargetPressure == 2) {
			return getAttribute("value", genaralDatas.get(16));
		}
		return getAttribute("value", genaralDatas.get(16));
	}
	
	public List<String> getAxleInformatios() {
		return axleInformations.stream()
				               .filter(x -> x.getText().contains(" "))
				               .map(m -> m.getText())
				               .collect(Collectors.toList());
	}
}
