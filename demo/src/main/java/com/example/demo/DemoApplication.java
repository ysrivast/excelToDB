package com.example.demo;

import java.util.Calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example"})
public class DemoApplication {

	public static void main(String[] args) {
		long start= Calendar.getInstance().getTimeInMillis();
		
		SpringApplication.run(DemoApplication.class, args);
		
		long end= Calendar.getInstance().getTimeInMillis();
		System.out.println("Time requeired for exection is : "+(end-start));
	}
}
