package com.github.kohthecodemaster.utils;

import com.github.kohthecodemaster.bean.AppProperties;
import com.github.kohthecodemaster.bean.FileToShredBean;

import java.io.File;

public class FileToShredBeanFactory {

    public static FileToShredBean getFileToShredBean(AppProperties appProperties) {

        return new FileToShredBean(new File(appProperties.getTargetPath()));

    }

}
