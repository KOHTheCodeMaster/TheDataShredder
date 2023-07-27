package com.github.kohthecodemaster.controller;

import com.github.kohthecodemaster.service.DataShredderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import stdlib.utils.MyTimer;

@Controller
public class MainController {

    @Autowired
    private DataShredderService dataShredderService;

    @Autowired
    private MyTimer myTimer;

    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    public boolean validateApplicationProperties() {

        return dataShredderService.validateAppProperties();

    }

    public void start() {

        LOGGER.info("start() - Begin.");
        myTimer.startTimer();

        dataShredderService.printAppProperties();


        myTimer.stopTimer(true);
        LOGGER.info("stop() - End.");

    }
}
