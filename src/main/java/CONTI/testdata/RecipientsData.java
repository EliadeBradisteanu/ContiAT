package CONTI.testdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipientsData {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("firstName")
	@Expose
	private String firstName;
	@SerializedName("lastName")
	@Expose
	private String lastName;
	@SerializedName("eMail")
	@Expose
	private String email;
	@SerializedName("telNo")
	@Expose
	private String telNo;
	@SerializedName("country")
	@Expose
	private String country;
	@SerializedName("language")
	@Expose
	private String language;
	

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getTelNo() {
		return telNo;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

}
