package com.github.kohthecodemaster.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AppProperties {

    @Value("${targetPath}")
    private String targetPath;

    @Value("${minFileSizeDeleteThresholdInMB}")
    private Integer minFileSizeDeleteThresholdInMB;

    @Value("${bufferSizeInMB}")
    private Integer bufferSizeInMB;

    @Value("${flagDamageEntireFile}")
    private boolean flagDamageEntireFile;

    @Value("${flagDeleteFilesAfterShredding}")
    private boolean flagDeleteFilesAfterShredding;

    public AppProperties() {
    }

    @Override
    public String toString() {
        return "targetPath: " + targetPath + "\n" +
                "minFileSizeDeleteThresholdInMB: " + minFileSizeDeleteThresholdInMB + "\n" +
                "bufferSizeInMB: " + bufferSizeInMB + "\n" +
                "flagDamageEntireFile: " + flagDamageEntireFile + "\n" +
                "flagDeleteFilesAfterShredding: " + flagDeleteFilesAfterShredding;
    }

    public boolean validateTargetPath() {

        //  Check targetPath is valid File/Dir Path
        return targetPath != null && !targetPath.equals("") && new File(targetPath).exists();

    }

    public String getTargetPath() {
        return targetPath;
    }

    public Integer getMinFileSizeDeleteThresholdInMB() {
        return minFileSizeDeleteThresholdInMB;
    }

    public Integer getBufferSizeInMB() {
        return bufferSizeInMB;
    }

    public boolean isFlagDamageEntireFile() {
        return flagDamageEntireFile;
    }

    public boolean isFlagDeleteFilesAfterShredding() {
        return flagDeleteFilesAfterShredding;
    }
}
