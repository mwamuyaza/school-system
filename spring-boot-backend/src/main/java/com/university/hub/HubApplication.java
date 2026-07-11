package com.university.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HubApplication {
    public static void main(String[] args) {
        SpringApplication.run(HubApplication.class, args);
        System.out.println("====================================================");
        System.out.println("University Hub Backend Server Running on Port 8080!");
        System.out.println("Linked to MySQL Database with Active E-mail Onboarding.");
        System.out.println("====================================================");
    }
}
