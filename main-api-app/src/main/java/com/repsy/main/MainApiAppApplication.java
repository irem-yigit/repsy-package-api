package com.repsy.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
		"com.repsy.main",
		"com.repsy.storage.filesystem.service",
		"com.repsy.storage.object"
})
public class MainApiAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApiAppApplication.class, args);
	}

}
