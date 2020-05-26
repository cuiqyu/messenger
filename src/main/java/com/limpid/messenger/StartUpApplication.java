package com.limpid.messenger;

import com.limpid.messenger.config.SmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartUpApplication implements CommandLineRunner {

    @Autowired
    private SmsConfig smsTemplateCodeConstant;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(StartUpApplication.class);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(smsTemplateCodeConstant);
    }
}
