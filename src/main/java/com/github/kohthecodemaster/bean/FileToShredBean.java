package com.github.kohthecodemaster.bean;

import java.io.File;

public class FileToShredBean {

    private final File file;
    private final long fileLength;
    private long filePointer;
    private boolean isSmallerThanBuffer;
    private final FileStatusFlagBean fileStatusFlagBean;

    public FileToShredBean(File file) {
        this.file = file;
        this.fileLength = file.length();
        this.fileStatusFlagBean = new FileStatusFlagBean();
    }

    public FileToShredBean(File file, boolean isFile) {
        this.file = file;
        this.fileLength = file.length();
        this.fileStatusFlagBean = new FileStatusFlagBean();
        this.fileStatusFlagBean.setFile(isFile);
    }


    @Override
    public String toString() {
        return "file: " + file + "\n" +
                "fileLength: " + fileLength + "\n" +
                "filePointer: " + filePointer + "\n";
    }

    public File getFile() {
        return file;
    }

    public long getFilePointer() {
        return filePointer;
    }

    public void setFilePointer(long filePointer) {
        this.filePointer = filePointer;
    }

    public long getFileLength() {
        return fileLength;
    }

    public FileStatusFlagBean getFileStatusFlagBean() {
        return fileStatusFlagBean;
    }
}
