package com.github.kohthecodemaster.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AppProperties {

    @Value("${targetPath}")
    private String targetPath;

    @Value("${flagDamageEntireFile}")
    private boolean flagDamageEntireFile;

    @Value("${flagDeleteFilesAfterShredding}")
    private boolean flagDeleteFilesAfterShredding;

    public AppProperties() {
    }

    @Override
    public String toString() {
        return "targetFile: " + targetPath + "\n" +
                "flagDamageEntireFile: " + flagDamageEntireFile + "\n" +
                "flagDeleteFilesAfterShredding: " + flagDeleteFilesAfterShredding;
    }

    public boolean validateTargetPath() {

        //  Check targetPath is valid File/Dir Path
        return targetPath != null && !targetPath.equals("") && new File(targetPath).exists();

    }
}
