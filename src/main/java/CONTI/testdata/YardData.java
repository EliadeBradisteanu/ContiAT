package CONTI.testdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YardData {

	@SerializedName("id")
	@Expose
	int id;
	@SerializedName("yardID")
	@Expose
	String yardID;
	@SerializedName("depotID")
	@Expose
	String depotID;
	@SerializedName("fleetID")
	@Expose
	String fleetID;
	@SerializedName("status")
	@Expose
	String status;

	public String getYardID() {
		return yardID;
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
}