package com.github.kohthecodemaster.utils;

import com.github.kohthecodemaster.bean.FileToShredBean;

import java.io.File;

public class FileToShredBeanFactory {

    public static FileToShredBean getFileToShredBean(String filePath) {

        return new FileToShredBean(new File(filePath));

    }

    public static FileToShredBean getFileToShredBean(String filePath, boolean isFile) {

        return new FileToShredBean(new File(filePath), isFile);

    }

}
