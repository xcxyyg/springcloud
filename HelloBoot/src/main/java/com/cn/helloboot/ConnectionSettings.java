package com.cn.helloboot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
/*@PropertySource(value={"file:src/main/resources/app.properties"})*/
/*@PropertySource(value={"classpath:app.properties"})*/
@ConfigurationProperties(prefix = "server")
public class ConnectionSettings {

	private String address;
	private String port;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}

}
