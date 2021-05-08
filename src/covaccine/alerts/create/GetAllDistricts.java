package covaccine.alerts.create;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

import com.google.gson.Gson;

import covaccine.alerts.model.District;
import covaccine.alerts.model.Districts;
import covaccine.alerts.model.State;
import covaccine.alerts.model.States;
import covaccine.alerts.util.ConnectionUtil;

public class GetAllDistricts {
	private HashMap<Integer,String> allStatesMap = new HashMap<>();
	private HashMap<String,Integer> allDistrictsMap = new HashMap<>();
	Gson gson = new Gson();
	
	public GetAllDistricts(){
		getAllStates();
		for(Integer stateId : allStatesMap.keySet()) {
			try {
				String apiURL = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/"+stateId;
				HttpURLConnection conection = ConnectionUtil.createConnection(apiURL, "GET");
				int responseCode = ConnectionUtil.getResponseCode(conection);
				String readLine = null;
				if (responseCode == HttpURLConnection.HTTP_OK) {
					BufferedReader inputReader = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			        StringBuffer response = new StringBuffer();
			        while ((readLine = inputReader.readLine()) != null) {
			            response.append(readLine);
			        } 
			        inputReader.close();
			        
			        Districts districts = gson.fromJson(response.toString(),Districts.class);
			        for(District district : districts.getDistricts()){
			        	allDistrictsMap.put(district.getDistrict_name().toLowerCase(), district.getDistrict_id());
			        }
			        
				}else {
					System.err.println("Error Fetching States");
				}
				}catch(Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	private void getAllStates() {
		try {
		String apiURL = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
		HttpURLConnection conection = ConnectionUtil.createConnection(apiURL, "GET");
		int responseCode = ConnectionUtil.getResponseCode(conection);
		String readLine = null;
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = inputReader.readLine()) != null) {
	            response.append(readLine);
	        } 
	        inputReader.close();
	        
	        States states = gson.fromJson(response.toString(),States.class);
	        for(State state : states.getStates()){
	        	allStatesMap.put(state.getState_id(),state.getState_name());
	        }
	        
		}else {
			System.err.println("Error Fetching States - URL - "+apiURL);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public HashMap<Integer, String> getAllStatesMap() {
		return allStatesMap;
	}

	public void setAllStatesMap(HashMap<Integer, String> allStatesMap) {
		this.allStatesMap = allStatesMap;
	}

	public HashMap<String, Integer> getAllDistrictsMap() {
		return allDistrictsMap;
	}

	public void setAllDistrictsMap(HashMap<String, Integer> allDistrictsMap) {
		this.allDistrictsMap = allDistrictsMap;
	}

	public int getDistrictId(String districtName) {
		if(this.getAllDistrictsMap() != null)
			return this.getAllDistrictsMap().get(districtName.toLowerCase());
		else
			return 0;
	}
}
