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

public class JsonDataHandlerRecipientsData {
	private final String filePath = "src/main/resources/testdata/recipients.json";
	private List<RecipientsData> dataList;
	
	public JsonDataHandlerRecipientsData() throws IOException{
		dataList = getData();
	}
	
	private List<RecipientsData> getData() throws IOException {
		Gson gson = new Gson();
		try(BufferedReader bufferReader = new BufferedReader(new FileReader(filePath))){
			RecipientsData[] data = gson.fromJson(bufferReader, RecipientsData[].class);
			return Arrays.asList(data);
		}
	}
	
		
	public void saveData(RecipientsData data) throws IOException {
		dataList.forEach(vehicleData -> {
			if (vehicleData.getId().equals(data.getId())) {
				vehicleData = data;
			}
		});
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String dataString = gson.toJson(dataList);
		
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))){
			bufferedWriter.write(dataString);
		}
	}
	
	public final RecipientsData getRecipientsData(String id){
		return dataList.stream().filter(x -> x.getId().equalsIgnoreCase(id)).findAny().get();
	}
	
 
}
