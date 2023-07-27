package com.github.kohthecodemaster.bean;

import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    private String targetPath;

//    @Value("flagDamageEntireFile")
    private boolean flagDamageEntireFile;

//    @Value("flagDeleteFilesAfterShredding")
    private boolean flagDeleteFilesAfterShredding;

    public AppProperties() {
    }

    @Override
    public String toString() {
        return "AppProperties{" +
                "targetFile=" + targetPath +
                ", flagDamageEntireFile=" + flagDamageEntireFile +
                ", flagDeleteFilesAfterShredding=" + flagDeleteFilesAfterShredding +
                '}';
    }

}
