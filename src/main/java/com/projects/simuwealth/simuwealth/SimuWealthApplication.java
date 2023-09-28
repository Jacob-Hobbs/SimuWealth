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

	public static void main(String[] args) {
		SpringApplication.run(SimuWealthApplication.class, args);

	}

}
