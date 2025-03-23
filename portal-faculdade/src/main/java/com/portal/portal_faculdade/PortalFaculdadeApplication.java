package com.portal.portal_faculdade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
	"com.portal.portal_faculdade",
	"com.portal.portal_faculdade.config"
})
public class PortalFaculdadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalFaculdadeApplication.class, args);
	}

}
