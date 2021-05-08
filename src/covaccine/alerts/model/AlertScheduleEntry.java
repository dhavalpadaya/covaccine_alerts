package covaccine.alerts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlertScheduleEntry {
	private String userName;
	private String findBasedOn;
	private String findValue;
	private List<String> emailAddresses = new ArrayList<>();
	private String formatedDate;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFindBasedOn() {
		return findBasedOn;
	}
	public void setFindBasedOn(String findBasedOn) {
		this.findBasedOn = findBasedOn;
	}
	public String getFindValue() {
		return findValue;
	}
	public void setFindValue(String findValue) {
		this.findValue = findValue;
	}
	public List<String> getEmailAddresses() {
		return emailAddresses;
	}
	public void setEmailAddresses(List<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}
	public void addEmailAddress(String emailAddress) {
		this.emailAddresses.add(emailAddress);
	}
	@Override
	public String toString() {
		String output = this.getUserName()+"[User] "+ this.getFindBasedOn()+"[findBasedOn] " + this.getFindValue() + "[findValue] " + this.getEmailAddresses().stream().collect(Collectors.joining(",")) + "[emails] ";
		return output;
	}
	
	public boolean setValuesFromString(String str) {
		String[] splittedStr = str.split("-");
		if(splittedStr.length < 3) {
			System.err.println("All values in this entry has not been provided, skipping this entry");
			return false;
		}
		String userName = null;
		if(splittedStr[0].trim() != null) {
			userName = splittedStr[0].trim();
			this.setUserName(userName);
		}else {
			System.err.println("userName is not proper. Provide Proper value, skipping this entry");
			return false;
		}
		
		if(splittedStr[1].trim().toUpperCase().equals("P") || splittedStr[1].trim().toUpperCase().equals("D")) {
			this.setFindBasedOn(splittedStr[1].trim().toUpperCase());
		}else {
			System.err.println("findBasedOn("+splittedStr[1].trim()+")is not either P nor D. Provide Proper value, skipping this entry for user "+userName);
			return false;
		}
		
		if(splittedStr[2].trim() != null) {
			this.setFindValue(splittedStr[2].trim().toLowerCase());
		}else {
			System.err.println("findValue is not proper. Provide Proper value, skipping this entry for user "+userName);
			return false;
		}
		
		if(splittedStr[3].trim() != null) {
			String emailAddressStr = splittedStr[3].trim();
			String[] emailAddressList = emailAddressStr.split(",");
			for(int i=0;i<emailAddressList.length && i<3;i++) {
				this.addEmailAddress(emailAddressList[i]);
			}
		}else {
			System.err.println("emailAddresses are not proper. Provide Proper value, skipping this entry for user"+userName);
			return false;
		}
		return true;
	}
	
	public String getFormatedDate() {
		return formatedDate;
	}
	public void setFormatedDate(String formatedDate) {
		this.formatedDate = formatedDate;
	}
	
	
	
}
