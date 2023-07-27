package com.github.kohthecodemaster.service;

import com.github.kohthecodemaster.bean.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataShredderService {

    @Autowired
    private AppProperties appProperties;

    private final static Logger LOGGER = LoggerFactory.getLogger(DataShredderService.class);

    public boolean validateAppProperties() {

        return appProperties.validateTargetPath();

    }

    public void printAppProperties() {

        LOGGER.info("printAppProperties() - AppProperties: \n" + appProperties);

    }

}
