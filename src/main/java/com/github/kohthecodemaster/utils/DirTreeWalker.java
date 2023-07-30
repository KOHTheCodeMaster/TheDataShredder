package com.github.kohthecodemaster.utils;

import com.github.kohthecodemaster.bean.FileToShredBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stdlib.pojos.FileSizePojo;
import stdlib.utils.KOHFilesUtil;
import stdlib.utils.KOHStringUtil;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DirTreeWalker extends SimpleFileVisitor<Path> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DirTreeWalker.class);

    long origFileCount;
    long origTotalSize;
    long filesShreddedCount;
    long totalSize;
    long dirsCount;
    long failureCount;
    int deletedFilesCount;
    boolean shouldDeleteFiles;

    //    List<String> listOfFilesFailedToShred = new ArrayList<>();  //  Both Files & Dirs
//    List<String> listOfFilesShredded = new ArrayList<>();
    List<FileToShredBean> fileToShredBeanList;

    //    FileToShredBean fileToShredBean;
    Consumer<FileToShredBean> fileToShredBeanConsumer;

    public DirTreeWalker(Consumer<FileToShredBean> fileToShredBeanConsumer, boolean shouldDeleteFiles, long origFileCount, long origTotalSize) {
        this.fileToShredBeanConsumer = fileToShredBeanConsumer;
        this.shouldDeleteFiles = shouldDeleteFiles;
        this.origFileCount = origFileCount;
        this.origTotalSize = origTotalSize;
        this.fileToShredBeanList = new ArrayList<>();
    }

    public String getFilesShreddedCountSummary() {

        return "Files - " + filesShreddedCount + "\n" +
                "Dirs. - " + dirsCount + "\n" +
                "Failed Files - " + failureCount + "\n" +
                "Total Size - " + FileSizePojo.acquireFileSizePojo(totalSize);

    }

    private boolean validFilesCountIntegrity(Path dir) {

        boolean validFlag = true;

        //  Avoiding Re-traversal of dir tree due to renaming of files
        if (filesShreddedCount > origFileCount || totalSize > origTotalSize) {

            validFlag = false;

            LOGGER.error("validFilesCountIntegrity() - Files Count Changed possibly due to Renaming/Moving files. Program Terminating...");
            LOGGER.error("filesShreddedCount: " + filesShreddedCount + " | origFileCount: " + origFileCount);
            LOGGER.error("totalSize: " + totalSize + " | origTotalSize: " + origTotalSize);

            // TODO: 29-07-2023 - Replace toAbsolutePath with toRealPath along with proper exception handling
            FileToShredBean fileToShredBean = FileToShredBeanFactory.getFileToShredBean(dir.toAbsolutePath().toString());
            fileToShredBean.getFileStatusFlagBean().setFile(dir.toFile().isFile());

            fileToShredBean.getFileStatusFlagBean().setFilesCountChanged(true);
            fileToShredBeanList.add(fileToShredBean);

        }

        return validFlag;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return validFilesCountIntegrity(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

        //  Time Stamp : 22nd August 2K19, 01:57 AM..!!

        LOGGER.info("visitFile() - Starts.");
        LOGGER.info("file: " + file.toAbsolutePath());

        if (!validFilesCountIntegrity(file)) return FileVisitResult.SKIP_SUBTREE;

        try {

            String strCurrentFilePath = file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
            FileToShredBean fileToShredBean = FileToShredBeanFactory.getFileToShredBean(strCurrentFilePath, true);
//            fileToShredBean.getFileStatusFlagBean().setFile(file.toFile().isFile());    //  True
            fileToShredBeanConsumer.accept(fileToShredBean);

            //  Rename File & Delete It!
            handleRenameAndDeleteFile(fileToShredBean);

            filesShreddedCount++;
            totalSize += attrs.size();
//            totalSize += fileToShredBean.getFile().length();

        } catch (NoSuchFileException x) {
            LOGGER.error("No Such File Found. File: " + file.toAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("visitFile() - ERROR: " + e.getMessage() + "\nFailed to Shred File: " + file.toAbsolutePath());
        } finally {

            LOGGER.info("visitFile() - After Exceptions, executing Finally Block.");

            String strCurrentFilePath = file.toAbsolutePath().toString();
            FileToShredBean fileToShredBean = FileToShredBeanFactory.getFileToShredBean(strCurrentFilePath, true);
//            fileToShredBean.getFileStatusFlagBean().setFile(file.toFile().isFile());    //  True
//            fileToShredBean.getFileStatusFlagBean().setInvalidFilePath(true);
            fileToShredBeanList.add(fileToShredBean);

        }

        LOGGER.info("visitFile() - Ends.");
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {

        failureCount++;
        LOGGER.info("visitFileFailed() - FAILED to Visit File. : " + file.toAbsolutePath() + "\n" + exc.getMessage());

        FileToShredBean fileToShredBean = FileToShredBeanFactory.getFileToShredBean(file.toAbsolutePath().toString());
        fileToShredBean.getFileStatusFlagBean().setVisitFileFailed(true);
        fileToShredBeanList.add(fileToShredBean);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

        if (!validFilesCountIntegrity(dir)) return FileVisitResult.SKIP_SUBTREE;

        handleRenameAndDeleteFile(FileToShredBeanFactory.getFileToShredBean(dir.toAbsolutePath().toString()));

        dirsCount++;
        return FileVisitResult.CONTINUE;
    }

    private void handleRenameAndDeleteFile(FileToShredBean fileToShredBean) {

        LOGGER.info("handleRenameAndDeleteFile() - Starts.");

        if (shouldDeleteFiles) deleteShreddedFile(fileToShredBean);
        else renameShreddedFile(fileToShredBean);

        LOGGER.info("handleRenameAndDeleteFile() - Ends.");

    }

    private void deleteShreddedFile(FileToShredBean fileToShredBean) {

        LOGGER.info("deleteShreddedFile() - Starts.");

        boolean hasDeleted = KOHFilesUtil.deleteFileNow(fileToShredBean.getFile());

        if (hasDeleted) {
            deletedFilesCount++;
            fileToShredBeanList.add(fileToShredBean);
        } else {
            failureCount++;
            fileToShredBean.getFileStatusFlagBean().setFailedToDelete(true);
            LOGGER.error("deleteShreddedFile() - Unable to Delete Shredded File : " +
                    "[" + fileToShredBean.getFile().getAbsolutePath() + "]");
        }

        LOGGER.info("deleteShreddedFile() - Ends.");

    }

    private void renameShreddedFile(FileToShredBean fileToShredBean) {

        LOGGER.info("renameShreddedFile() - Starts.");

        String shreddedFileName = fileToShredBean.getFile().getName() + " - " +
                KOHStringUtil.generateCurrentTimeStamp() + " - " +
                System.nanoTime() +
                ConstantHelper.SHREDDED_FILE_EXTENSION;

        boolean hasRenamed = KOHFilesUtil.renameFileNameToStr(fileToShredBean.getFile(), shreddedFileName);

        if (hasRenamed) fileToShredBeanList.add(fileToShredBean);
        else {
            failureCount++;
            fileToShredBean.getFileStatusFlagBean().setFailedToRename(true);
            LOGGER.error("renameShreddedFile() - Unable to Rename Shredded File : " +
                    "[" + fileToShredBean.getFile().getAbsolutePath() + "]");
        }

        LOGGER.info("renameShreddedFile() - Ends.");

    }

    /*private void handleRenameAndDeleteDir(File file) {

        LOGGER.info("handleRenameAndDeleteDir() - Starts.");

        if (shouldDeleteFiles) {

            boolean hasDeleted = KOHFilesUtil.deleteFileNow(file);

            if (hasDeleted) {
                dirsCount++;
                listOfFilesShredded.add(file.getAbsolutePath());
            } else {
                failureCount++;
                listOfFilesFailedToShred.add(file.getAbsolutePath());
                LOGGER.info("Unable to Shred Dir : [" + file.getAbsolutePath() + "]\t|\tShredding Failed..!!\n");
            }

        } else {

            String shreddedFileName = file.getName() + " - " +
                    KOHStringUtil.generateCurrentTimeStamp() + " - " +
                    System.nanoTime() +
                    ConstantHelper.SHREDDED_FILE_EXTENSION;

            boolean hasRenamed = KOHFilesUtil.renameFileNameToStr(file, shreddedFileName);

            if (hasRenamed) listOfFilesShredded.add(file.getAbsolutePath());
            else {
                LOGGER.info("Unable to Shred Item : [" + file.getAbsolutePath() + "]\t|\tShredding Failed..!!\n");
                listOfFilesFailedToShred.add(file.getAbsolutePath());
            }
        }

        LOGGER.info("handleRenameAndDeleteDir() - Ends.");

    }*/

    public long getFilesShreddedCount() {
        return filesShreddedCount;
    }

    public long getTotalSize() {
        return totalSize;
    }
}