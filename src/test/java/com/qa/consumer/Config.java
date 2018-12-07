package com.qa.consumer;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	@Bean
	public Properties myProps() {
		Properties properties = new Properties();
		properties.setProperty("CVNotFound.message", "${CVNotFound.message}");
		properties.setProperty("CVAdded.message", "${CVAdded.message}");
		properties.setProperty("MalformedRequest.message", "${MalformedRequest.message}");
		properties.setProperty("CVUpdated.message", "${CVUpdated.message}");
		properties.setProperty("CVDeleted.message", "${CVDeleted.message}");
		return properties;
	}

}
