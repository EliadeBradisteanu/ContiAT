package CONTI.testdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonDataHandlerVehicleData {
	
	private static final String FILE_PATH = "src/main/resources/testdata/vehicleInformation.json";
	private List<VehicleData> dataList;
	
	public JsonDataHandlerVehicleData() throws IOException{
		dataList = getData();
	}
	
	private List<VehicleData> getData() throws IOException {
		Gson gson = new Gson();
		try(BufferedReader bufferReader = new BufferedReader(new FileReader(FILE_PATH))){
			VehicleData[] data = gson.fromJson(bufferReader, VehicleData[].class);
			return Arrays.asList(data);
		}
	}
	
		
	public void saveData(VehicleData data) throws IOException {
		dataList.forEach(vehicleData -> {
			if (vehicleData.getId().equals(data.getId())) {
				vehicleData = data;
			}
		});
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String dataString = gson.toJson(dataList);
		
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))){
			bufferedWriter.write(dataString);
		}
	}
	
	public final VehicleData getVehicleData(String id){
		return dataList.stream().filter(x -> x.getId().equalsIgnoreCase(id)).findAny().get();
	} 
}
