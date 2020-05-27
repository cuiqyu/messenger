package com.limpid.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartUpApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(StartUpApplication.class);
        application.run(args);
    }

}
