package com.gomson.gomsonServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class GomsonServerApplication {

//	@PostConstruct
//	public void started() {
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//		System.out.println("현재시각 : " + new Date());
//	}

	public static void main(String[] args) {
		SpringApplication.run(GomsonServerApplication.class, args);
	}

}
