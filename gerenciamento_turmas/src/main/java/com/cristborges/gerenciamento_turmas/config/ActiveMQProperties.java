package com.cristborges.gerenciamento_turmas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:activemq.properties")
@ConfigurationProperties
public class ActiveMQProperties {

	private String brokerUrl;
	private String user;
	private String password;
	private String schedulingTopic;

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchedulingTopic() {
		return schedulingTopic;
	}

	public void setSchedulingTopic(String schedulingTopic) {
		this.schedulingTopic = schedulingTopic;
	}
}
