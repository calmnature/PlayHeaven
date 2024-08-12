package io.spring.playheaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PlayheavenApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlayheavenApplication.class, args);
    }

}
