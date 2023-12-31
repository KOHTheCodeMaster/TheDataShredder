package com.github.kohthecodemaster.controller;

import com.github.kohthecodemaster.service.DataShredderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import stdlib.utils.MyTimer;

@Controller
public class MainController {

    private final DataShredderService dataShredderService;
    private final MyTimer myTimer;

    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController(DataShredderService dataShredderService, MyTimer myTimer) {
        this.dataShredderService = dataShredderService;
        this.myTimer = myTimer;
    }

    public boolean validateApplicationProperties() {
        return dataShredderService.validateAppProperties();
    }

    public void start() {

        LOGGER.info("start() - Begin.");
        myTimer.startTimer();

        dataShredderService.start();

        myTimer.stopTimer(true);
        LOGGER.info("start() - End.");

    }
}
