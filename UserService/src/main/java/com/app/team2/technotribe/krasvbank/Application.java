package com.app.team2.technotribe.krasvbank;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import org.modelmapper.Conditions;

@SpringBootApplication(scanBasePackages = "com.app.team2.technotribe.krasvbank")
@OpenAPIDefinition(info = @Info(title = "Krasv Bank", description = "Backend Rest Apis for Krasv Bank", version = "v1.0", contact = @Contact(name = "Krasv Bank", email = "3001chavanrohan@gmail.com", url = "https://github.com/D4-80594-RohanChavan/Banking-Application"), license = @License(name = "Krasv Bank", url = "https://github.com/D4-80594-RohanChavan/Banking-Application")), externalDocs = @ExternalDocumentation(description = "Krasv Bank Documentation", url = "https://github.com/D4-80594-RohanChavan/Banking-Application "))
@EnableFeignClients
//@EnableEurekaClient

public class Application {

	public static void main(String[] args) {
		System.out.println("version:" + SpringVersion.getVersion());
		SpringApplication.run(Application.class, args);
	}


	@Bean // equivalent to <bean id ..../> in xml file
	public ModelMapper mapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
				.setPropertyCondition(Conditions.isNotNull());
		return modelMapper;

	}
}
