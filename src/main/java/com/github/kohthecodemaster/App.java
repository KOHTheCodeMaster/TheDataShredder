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

    private boolean isAppPropertiesValid;

    public static void main(String[] args) {

        App app = new App();
        app.major();

    }

    private void major() {

        LOGGER.info("major() - Begin.\n");

        SpringApplication.run(App.class);

        LOGGER.info("major() - End.");


    }

    @Override
    public void run(String... args) {

        LOGGER.info("\n\n");
        LOGGER.info("run() - Begin.");

        init();

        validate();
        if (!isAppPropertiesValid) return;

        mainController.start();

        LOGGER.info("run() - End.\n");

    }

    private void init() {

        //  For Any Pre-processing / Initialization

    }

    private void validate() {

        validateApplicationProperties();

    }

    private void validateApplicationProperties() {

        LOGGER.info("validateApplicationProperties() - Begin.");

        String strValidationSuccessMsg = "Validation Successful..!! ^-^";
        String strValidationFailureMsg = "application.properties file is INVALID. PROGRAM TERMINATED..!!";

        isAppPropertiesValid = mainController.validateApplicationProperties();

        if (!isAppPropertiesValid) LOGGER.error("validateApplicationProperties() - " + strValidationFailureMsg);
        else LOGGER.info("validateApplicationProperties() - " + strValidationSuccessMsg);

        LOGGER.info("validateApplicationProperties() - End.");

    }

}

/*
 *  Date Created    :   27th Jul. 2K23 - 12:45 PM..!!
 *  last Modified   :    1st Aug. 2K23 - 12:57 PM..!!
 *
 *  Change Log:
 *
 *  7th Commit - Code Cleanup
 *
 *  6th Commit - Added Total Size Validation
 *
 *  5th Commit - Process Shredding Directory Tree
 *
 *  4th Commit - Shredding Single File
 *
 *  3rd Commit - FileToShredBean Added
 *
 *  2nd Commit - Validate application.properties
 *
 *  Init Commit - Establish Base Using Spring Boot
 *
 *  Code Developed By,
 *  ~K.O.H..!! ^__^
 */