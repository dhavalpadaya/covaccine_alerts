package covaccine.alerts.create;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import covaccine.alerts.model.AlertScheduleEntry;
import covaccine.alerts.model.Center;
import covaccine.alerts.model.Centers;
import covaccine.alerts.util.ConnectionUtil;

public class AvailableSlotsAlerts {
	
	GetAllDistricts getAllDistricts = null;
	
	public AvailableSlotsAlerts(GetAllDistricts getAllDistricts){
		this.getAllDistricts = getAllDistricts;
	}
	
	public String process(AlertScheduleEntry alertScheduleEntry) {
		HttpURLConnection conection = null;
		try{
			String userName = alertScheduleEntry.getUserName();
			String findBasedOn = alertScheduleEntry.getFindBasedOn();
			String findValue = alertScheduleEntry.getFindValue();
			String fromDate = alertScheduleEntry.getFormatedDate();
			
			String apiURL = null;
			if(findBasedOn.equals("P"))
				apiURL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+findValue+"&date="+fromDate;
			else if(findBasedOn.equals("D")) {
				System.out.println("Getting district Id for value - "+findValue);
				int districtId = this.getAllDistricts.getDistrictId(findValue);
				apiURL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="+districtId+"&date="+fromDate;
			}else {
				System.err.println("findBasedOn value is not proper for user "+userName+" value is "+findBasedOn);
				return null;
			}
		conection = ConnectionUtil.createConnection(apiURL, "GET");
		int responseCode = ConnectionUtil.getResponseCode(conection);
	    String readLine = null;
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	    	BufferedReader inputReader = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = inputReader.readLine()) != null) {
	            response.append(readLine);
	        } 
	        inputReader.close();
	        
	        Gson gson = new Gson();
	        boolean isSentMail = false;
	       
	        Centers centers = gson.fromJson(response.toString(),Centers.class);
	        
	        StringBuffer mailContent = new StringBuffer();
	        mailContent.append("Hello "+userName+",<br/><br/>");
	        mailContent.append("Please find vaccination slots available in your area<br/><br/>	");
	        List<Center> centerFilteredList = centers.getCenters().stream().filter(new Predicate<Center>() {

				@Override
				public boolean test(Center center) {
					long getAvailableSessionCount = center.getSessions().stream()
							//.filter(e -> e.getAvailable_capacity() > 0)
							.count();
					return getAvailableSessionCount > 0 ? true : false;
				}
	        	
	        }).collect(Collectors.toList());
	        int i=0;
	        for(Center center : centerFilteredList) {
	        	mailContent.append(center.getMailContentForOneCenter());
	        	i++;
	        	if(i > 2) break;
				isSentMail = true;
	        }
	        
	        mailContent.append("<br/>Hope this information is useful to you<br/>");
	        mailContent.append("Feel free to mail us at covaccinealerts@gmail.com if you have any complain or suggestion.");
	        mailContent.append("<h3>Covaccine Alerts - Developed By <a href=\"http://dhavalspadaya.000webhostapp.com/\">Dhaval Padaya</a></h3>.");
	        
	        if(isSentMail)
	        	return mailContent.toString();
	    } else {
	    	System.err.println("*******");
	        System.err.println("API call is not successful for User "+userName);
	        System.err.println("Response Code "+responseCode+" URL - "+apiURL);
	        System.err.println("*******");
	    }
	    if(conection != null)
	    	conection.disconnect();
	}catch(Exception e){
		e.printStackTrace();
		}finally {
			if(conection != null)
		    	conection.disconnect();
		}
		return null;
	}
	
	
}
