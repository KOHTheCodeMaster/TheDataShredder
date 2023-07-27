package com.github.kohthecodemaster;

import com.github.kohthecodemaster.controller.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    MainController mainController;

    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        App app = new App();
        app.major();

    }

    private void major() {

        LOGGER.info("major() - Begin.\n");

        SpringApplication.run(App.class);

        LOGGER.info("major() - End.");


    }

    private void init() {

        //  For Any Pre-processing / Initialization

    }

    @Override
    public void run(String... args) {

        init();
        mainController.start();

    }

}

/*
 *  Time Stamp  :   27th Jul. 2K23 - 12:45 PM..!!
 *
 *  Change Log:
 *
 *  Init Commit - Establish Base Using Spring Boot
 *
 *  Code Developed By,
 *  ~K.O.H..!! ^__^
 */