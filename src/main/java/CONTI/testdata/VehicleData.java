package CONTI.testdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleData {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("depotName")
	@Expose
	private String depotName;
	@SerializedName("depotID")
	@Expose
	private String depotID;
	@SerializedName("fleetID")
	@Expose
	private String fleetID;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("vehicleType")
	@Expose
	private String vehicleType;
	@SerializedName("axleConfiguration")
	@Expose
	private String axleConfiguration;
	@SerializedName("internalCustomerVehicleId")
	@Expose
	private String internalCustomerVehicleId;
	@SerializedName("lpn")
	@Expose
	private String lpn;
	@SerializedName("pressure")
	@Expose
	private String pressure;
	@SerializedName("make")
	@Expose
	private String make;
	@SerializedName("lpnCountry")
	@Expose
	private String lpnCountry;
	@SerializedName("pressureUnit")
	@Expose
	private String pressureUnit;
	@SerializedName("temperatureUnit")
	@Expose
	private String temperatureUnit;
	@SerializedName("billing")
	@Expose
	private String billing;
	@SerializedName("sensorAmount")
	@Expose
	private int sensorAmount;


	public String getId() {
		return id;
	}

	public String getBilling() {
		return billing;
	}

	public String getTemperatureUnit() {
		return temperatureUnit;
	}

	public String getPressureUnit() {
		return pressureUnit;
	}

	public String getLpnCountry() {
		return lpnCountry;
	}

	public String getPressure() {
		return pressure;
	}

	public String getMake() {
		return make;
	}

	public String getDepotName() {
		return depotName;
	}

	public String getDepotID() {
		return depotID;
	}

	public String getFleetID() {
		return fleetID;
	}

	public String getStatus() {
		return status;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public String getAxleConfiguration() {
		return axleConfiguration;
	}

	public String getInternalCustomerVehicleId() {
		return internalCustomerVehicleId;
	}

	public String getLpn() {
		return lpn;
	}
	
	public int getSensorAmount() {
		return sensorAmount;
	}

	public void incrementLPN() {		
		String lpnStamm = lpn.substring(0, lpn.length()-3);
		String lpnNumber = lpn.substring(lpn.length()-3);

		int oldNumber = Integer.parseInt(lpnNumber);
		int newLpnNumber = oldNumber + 1;;

		this.lpn = lpnStamm+generateStringLength3(newLpnNumber);
	}

	public void incrementInternalCustomerId() {

		String internalCustomerIdStamm = internalCustomerVehicleId.substring(0, 6);
		String internalCustomerIdNumber = internalCustomerVehicleId.substring(6);

		int oldNumber = Integer.parseInt(internalCustomerIdNumber);
		int newinternalCustomerId  = oldNumber + 1;
		
		this.internalCustomerVehicleId = internalCustomerIdStamm+generateStringLength3(newinternalCustomerId);
	}	
	
	public String generateStringLength3(int number) {
		String newNumber = null;
		if (number <= 9) {
			newNumber = "00"+Integer.toString(number);
		} else if (number >=10 && number <= 100) {
			newNumber = "0"+Integer.toString(number);
		} else if (number >=100) {
			newNumber = Integer.toString(number);
		}
		return newNumber;
	}
}
