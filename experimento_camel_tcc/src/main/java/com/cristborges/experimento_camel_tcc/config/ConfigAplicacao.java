package com.cristborges.experimento_camel_tcc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.cristborges.experimento_camel_tcc")
public class ConfigAplicacao {

	@Configuration
	@Profile("standard")
	@PropertySource("classpath:experimento_camel_tcc.properties")
	static class StandardProfile {

	}

	@Configuration
	@Profile("test")
	@PropertySource("classpath:experimento_camel_tcc_test.properties")
	static class TestProfile {

	}
}
