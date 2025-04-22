package com.repsy.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.repsy.main", "com.repsy.storage.filesystem.service"})  //storageservice bir interface olduğu için bean tanımlayamıyordum. Bu durumda, FileUploadControllerda storageservice beani problem çıkarıyordu. ComponentScan ile paket ekleyince çözüldü.
public class MainApiAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApiAppApplication.class, args);
	}

}
