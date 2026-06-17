package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

// 💡 以下の2つのクラスを除外（exclude）設定に指定します
@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
})
public class ClinicSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicSystemApplication.class, args);
	}

}