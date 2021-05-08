package covaccine.alerts.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.google.gson.annotations.SerializedName;

public class Center {
	private int center_id;
	private String name;
	private String state_name;
	private String district_name;
	private String block_name;
	private int pincode;
	private int lat;
	@SerializedName("long")
	private int long1;
	private String from;
	private String to;
	private String fee_type;
	private ArrayList<Session> sessions = new ArrayList<>();
	public int getCenter_id() {
		return center_id;
	}
	public void setCenter_id(int center_id) {
		this.center_id = center_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	public String getBlock_name() {
		return block_name;
	}
	public void setBlock_name(String block_name) {
		this.block_name = block_name;
	}
	public int getPincode() {
		return pincode;
	}
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}
	public int getLat() {
		return lat;
	}
	public void setLat(int lat) {
		this.lat = lat;
	}
	public int getLong1() {
		return long1;
	}
	public void setLong1(int long1) {
		this.long1 = long1;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public ArrayList<Session> getSessions() {
		return sessions;
	}
	public void setSessions(ArrayList<Session> sessions) {
		this.sessions = sessions;
	}
	@Override
	public String toString() {
		return "Center [center_id=" + center_id + ", name=" + name + ", state_name=" + state_name + ", district_name="
				+ district_name + ", block_name=" + block_name + ", pincode=" + pincode + ", lat=" + lat + ", long1="
				+ long1 + ", from=" + from + ", to=" + to + ", fee_type=" + fee_type + ", sessions=" + sessions + "]";
	}
	
	public String getMailContentForOneCenter() {
		StringBuffer mailContent = new StringBuffer();
    	mailContent.append("<table style=\"border: 1px solid black;\">");
		mailContent.append("<tr style=\"border: 1px solid black;\"><td style=\"border: 1px solid black;\" colspan=\"3\">"+"Center Name   : "+this.getName()+"</td></tr>");
    	mailContent.append("<tr style=\"border: 1px solid black;\"><td style=\"border: 1px solid black;\" colspan=\"3\">Fee Type      : "+this.getFee_type()+"</td></tr>");
    	mailContent.append("<tr	><td colspan=\"3\"></td></tr>");
    	mailContent.append("<tr style=\"border: 1px solid black;\"><th style=\"border: 1px solid black;\">Date</th><th style=\"border: 1px solid black;\">Available Capacity</th><th style=\"border: 1px solid black;\">Vaccine Name</th></tr>");
    	for(Session session : this.getSessions()) {
    		session.setAvailable_capacity(ThreadLocalRandom.current().nextInt(0, 25));
    		mailContent.append("<tr>");
    		mailContent.append("<td style=\"border: 1px solid black;\">"+session.getDate()+"</td>");
    		mailContent.append("<td style=\"border: 1px solid black;\">"+session.getAvailable_capacity()+"</td>");
    		mailContent.append("<td style=\"border: 1px solid black;\">"+session.getVaccine()+"</td>");
    		//mailContent.append("<td style=\"border: 1px solid black;\">"+session.getSlots().stream().collect(Collectors.joining ("<br/>"))+"</td>");
    		mailContent.append("</tr>");
    	}
    	mailContent.append("</table>");
    	mailContent.append("<br/>");
    	return mailContent.toString();
	}
	 
}
