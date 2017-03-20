package com.home;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Main Application class to run the application as standalone web
 */
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MainApplication.class, args);
    }
}
