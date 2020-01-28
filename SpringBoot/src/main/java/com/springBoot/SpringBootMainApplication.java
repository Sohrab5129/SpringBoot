package com.springBoot;

import java.util.TimeZone;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = { SpringBootMainApplication.class, Jsr310JpaConverters.class })
public class SpringBootMainApplication extends SpringBootServletInitializer{
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootMainApplication.class);
	
	public static void main(String[] args) {
		log.info("Inside main");
		SpringApplication.run(SpringBootMainApplication.class, args);
	}

	@PostConstruct
	void init() {
		log.info("Inside init, time zone: "+TimeZone.getTimeZone("UTC")+"\n");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootMainApplication.class);
	}
}
