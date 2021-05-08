import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import covaccine.alerts.create.AvailableSlotsAlerts;
import covaccine.alerts.create.GetAllDistricts;
import covaccine.alerts.model.AlertScheduleEntry;
import covaccine.alerts.util.EncodeDecodeUtil;
import covaccine.alerts.util.MailUtil;
import covaccine.alerts.util.PropertyUtils;

public class ExecutionMain implements Runnable {
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	MailUtil mailUtil = null;
	GetAllDistricts getAllDistricts = null;
	
	ExecutionMain(GetAllDistricts getAllDistricts,String fromAddress,String fromAddressPass){
		mailUtil = new MailUtil(fromAddress,fromAddressPass);
		this.getAllDistricts = getAllDistricts;
	}

	@Override
	public void run() {
		System.out.println("AvailableSlotsAlerts Started at "+new Date());
		String formatedDate = formatter.format(new Date().getTime());
		File fileObj = null;
		Scanner fileReader = null;
		try {
			fileObj = new File("alert_schedule_entry.txt");
		    fileReader = new Scanner(fileObj);
		    AvailableSlotsAlerts availableSlotsAlerts = new AvailableSlotsAlerts(this.getAllDistricts);
		      while (fileReader.hasNextLine()) {
		        String oneLine = fileReader.nextLine();
		        if(oneLine.startsWith("#") || oneLine.isEmpty() || oneLine == null || oneLine.equals("")) continue;
		        AlertScheduleEntry alertScheduleEntry = new AlertScheduleEntry();
		        boolean result = alertScheduleEntry.setValuesFromString(oneLine);
		        alertScheduleEntry.setFormatedDate(formatedDate);
		        if(result) {
		        	System.out.println("Processing For : ");
		        	System.out.println(alertScheduleEntry);
		        	
					String mailSubject = "Covid Vaccination Available Slots For User "+alertScheduleEntry.getUserName();
					String mailContent = availableSlotsAlerts.process(alertScheduleEntry);
					if(mailContent != null) {
						alertScheduleEntry.getEmailAddresses().stream().forEach(new Consumer<String>(){

							@Override
							public void accept(String email) {
								System.out.println("Sending mail to email - "+email);
					        	boolean result = mailUtil.sendMail(Arrays.asList(email), mailSubject, mailContent.toString());
						        if(result)
						        	System.out.println("********Mail Sent to "+email+"***********");
						        else
						        	System.err.println("Error Sending mail to "+email);
							}
						});
					}else {
						System.out.println("Mail Sent Skipped for user "+alertScheduleEntry.getUserName()+" as no available slots");
					}
		        }
		      }
		      }catch (Exception e) {
			e.printStackTrace();
		}finally {
			fileReader.close();
		}
		
		System.out.println("AvailableSlotsAlerts Completed at "+new Date());
		System.out.println();
	}
	
	public static void main(String[] args) {
		try {
		GetAllDistricts getAllDistricts = new GetAllDistricts(); 
		Properties alertsConfigProperties = PropertyUtils.readPropertyFile("alerts_config.txt");
		String fromAddress = alertsConfigProperties.getProperty("fromAddress");
		String fromAddressPass = alertsConfigProperties.getProperty("fromAddressPassEncode");
		if(fromAddressPass == null)
			fromAddressPass = alertsConfigProperties.getProperty("fromAddressPass");
		else
			fromAddressPass = EncodeDecodeUtil.decode(fromAddressPass);
		ExecutionMain execution = new ExecutionMain(getAllDistricts,fromAddress,fromAddressPass);
		System.out.println("Scheduler Started");
		System.out.println("Email will be sent from "+fromAddress);
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        int executionPeiodInMinutes = 30;
        try {
        	executionPeiodInMinutes = Integer.parseInt(alertsConfigProperties.getProperty("executionPeiodInMinutes"));
        }catch(NumberFormatException e) {
        	e.printStackTrace();
        	System.err.println("Error occurred while converting property executionPeiodInMinutes, Taking default value as 30 minutes for executionPeriod");
        }
        exec.scheduleAtFixedRate(execution, 0, executionPeiodInMinutes, TimeUnit.MINUTES);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
