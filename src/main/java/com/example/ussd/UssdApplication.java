package com.example.ussd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class UssdApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssdApplication.class, args);
    }
}