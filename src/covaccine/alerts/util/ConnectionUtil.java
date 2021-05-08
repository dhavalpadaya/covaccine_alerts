package covaccine.alerts.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionUtil {
	public static HttpURLConnection createConnection(String apiURL,String requestType) {
		try {
		URL urlForGetRequest = new URL(apiURL);
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod(requestType);
	    conection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	    return conection;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	    
	}
	
	public static int getResponseCode(HttpURLConnection connection) {
		try {
			return connection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			return 500;
		}
	}
}
