package com.philip.ss_02_restresult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class Ss02RestResultApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ss02RestResultApplication.class, args);
    }

}
