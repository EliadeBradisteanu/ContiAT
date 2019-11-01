package CONTI.testdata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class JsonDataReaderYardID {
	private final String filePath = "src/main/resources/testdata/yardReader.json";
	private List<YardData> dataList;

	public JsonDataReaderYardID() {
		dataList = getData();
	}

	private List<YardData> getData() {
		Gson gson = new Gson();
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(filePath));
			YardData[] data = gson.fromJson(bufferReader, YardData[].class);
			return Arrays.asList(data);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Json file not found at path : " + filePath);
		} finally {
			try {
				if (bufferReader != null)
					bufferReader.close();
			} catch (IOException ignore) {
			}
		}
	}

	public final YardData getYardReader(int id) {
		return dataList.stream().filter(x -> x.id == id).findAny().get();
	}
}
