package com.github.kohthecodemaster.bean;

public class FileStatusFlagBean {

    boolean isFile;
    boolean isDirectory;
    boolean filesCountChanged;
    boolean invalidFilePath;
    boolean visitFileFailed;
    boolean failedToDelete;
    boolean failedToRename;

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isFilesCountChanged() {
        return filesCountChanged;
    }

    public void setFilesCountChanged(boolean filesCountChanged) {
        this.filesCountChanged = filesCountChanged;
    }

    public boolean isInvalidFilePath() {
        return invalidFilePath;
    }

    public void setInvalidFilePath(boolean invalidFilePath) {
        this.invalidFilePath = invalidFilePath;
    }

    public boolean isVisitFileFailed() {
        return visitFileFailed;
    }

    public void setVisitFileFailed(boolean visitFileFailed) {
        this.visitFileFailed = visitFileFailed;
    }

    public boolean isFailedToDelete() {
        return failedToDelete;
    }

    public void setFailedToDelete(boolean failedToDelete) {
        this.failedToDelete = failedToDelete;
    }

    public boolean isFailedToRename() {
        return failedToRename;
    }

    public void setFailedToRename(boolean failedToRename) {
        this.failedToRename = failedToRename;
    }
}
