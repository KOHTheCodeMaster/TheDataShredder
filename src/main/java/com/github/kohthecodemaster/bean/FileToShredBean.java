package com.github.kohthecodemaster.bean;

import java.io.File;

public class FileToShredBean {

    private final File targetFile;
    private final long fileLength;
    private long filePointer;

    public FileToShredBean(File targetFile) {
        this.targetFile = targetFile;
        this.fileLength = targetFile.length();
    }

    @Override
    public String toString() {
        return "targetFile: " + targetFile + "\n" +
                "fileLength: " + fileLength + "\n" +
                "filePointer: " + filePointer + "\n";
    }


}
