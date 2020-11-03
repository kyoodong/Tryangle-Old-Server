package com.gomson.tryangle.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {
		"com.gomson.tryangle"
})
public class TryangleServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TryangleServerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TryangleServerApplication.class);
	}
}
