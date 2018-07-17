package com.cn.helloboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Example {
//	
//    @Bean
//    @ConfigurationProperties(prefix = "my")
//    public ConnectionSettings connectionSettings(){
//        return new ConnectionSettings();
//
//    }
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Example.class, args);
	}
}
