package com.example.reportgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class ReportgeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportgeneratorApplication.class, args);
	}

}
