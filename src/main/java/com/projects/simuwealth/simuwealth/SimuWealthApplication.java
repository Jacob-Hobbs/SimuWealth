package com.projects.simuwealth.simuwealth;

import com.projects.simuwealth.simuwealth.Service.MailService.EmailSenderService;
import com.projects.simuwealth.simuwealth.Service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SimuWealthApplication {

	//@Autowired
	//private EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(SimuWealthApplication.class, args);

	}

	//@EventListener(ApplicationReadyEvent.class)
	//public void sendMail() {
		//senderService.sendEmail("jrh0397@gmail.com", "Reset Password Link", "Please reset your password here: ");
	//}


}
