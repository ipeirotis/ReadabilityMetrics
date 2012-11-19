package com.ipeirotis.readability.di;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;
import com.ipeirotis.readability.model.Text;
import com.ipeirotis.readability.model.TextMetric;
import com.ipeirotis.readability.model.User;

@Configuration
@ComponentScan(basePackages="com.ipeirotis")
public class ContextConfig {
	@PostConstruct
	public void initObjectify() throws Exception {
		ObjectifyService.register(Text.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(TextMetric.class);
	}
	
	@Bean
	public ObjectMapper getObjectMapper() throws Exception {
		return new ObjectMapper().registerModule(new ObjectifyJacksonModule());
	}
}
