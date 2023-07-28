package com.github.kohthecodemaster.service;

import com.github.kohthecodemaster.bean.AppProperties;
import com.github.kohthecodemaster.bean.FileToShredBean;
import com.github.kohthecodemaster.utils.ConstantHelper;
import com.github.kohthecodemaster.utils.FileToShredBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stdlib.utils.KOHFilesUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Service
public class DataShredderService {

    private AppProperties appProperties;
    private final int bufferLength;

    private final static Logger LOGGER = LoggerFactory.getLogger(DataShredderService.class);

    @Autowired
    public DataShredderService(AppProperties appProperties) {

        this.appProperties = appProperties;
        this.bufferLength = appProperties.getBufferSizeInMB() * ConstantHelper.ONE_MB;

    }

    public boolean validateAppProperties() {
        return appProperties.validateTargetPath();
    }

    public void start() {

        printAppProperties();

        beginShredding();

    }

    public void printAppProperties() {

        LOGGER.info("printAppProperties() - AppProperties: \n" + appProperties);
//        LOGGER.info("printAppProperties() - fileToShredBean: \n" + fileToShredBean);

    }


    public void beginShredding() {

        File targetFile = new File(appProperties.getTargetPath());

        if (targetFile.isFile()) {
            FileToShredBean fileToShredBean = FileToShredBeanFactory.getFileToShredBean(appProperties.getTargetPath());
            processSingleFile(fileToShredBean, appProperties.isFlagDeleteFilesAfterShredding());
        }
//        else if (targetFile.isDirectory()) shredTheDirectory();
        else LOGGER.error("beginShredding() - INVALID Target File Path: " + appProperties.getTargetPath());

    }

    private void processSingleFile(FileToShredBean fileToShredBean, boolean flagDeleteFilesAfterShredding) {

        //  TODO : Abstract Print Logs when Shredding Files

        File currentFile = fileToShredBean.getFile();

        LOGGER.info("Currently Processing: [" + currentFile.getAbsolutePath() + "]");

        try (RandomAccessFile raf = new RandomAccessFile(currentFile, "rwd")) {

            //  Shred Entire File when file size is less than deleteThresholdInBytes
            long deleteThresholdInBytes = appProperties.getMinFileSizeDeleteThresholdInMB() * ConstantHelper.ONE_MB;

            if (appProperties.isFlagDamageEntireFile() || (currentFile.length() < deleteThresholdInBytes)) {

                //  When file to shred is Smaller than Buffer length,
                if (fileToShredBean.getFileLength() < bufferLength) {

                    //  Shred Small File by using small temp buffer of current file length
                    byte[] tempSmallBuffer = new byte[(int) fileToShredBean.getFileLength()];
                    raf.seek(0);  //  Initially already at 0
//                    raf.write(tempSmallBuffer);
                    fileToShredBean.setFilePointer(tempSmallBuffer.length);

                } else {

                    shredEntireFile(raf, fileToShredBean);

                }

                LOGGER.info("File [" + currentFile.getAbsolutePath() + "] Shredded Successfully.");


            } else {
                //  Otherwise, Damage DMG_PERCENTAGE % of the File
//                damageFileHeaderAndFooter(raf);
//                damageMajorFileSegment(raf);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("I/O Exception Occurred!\nProgram Terminated...");
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("Interrupted Exception Occurred!\nProgram Terminated...");
        }


        if (flagDeleteFilesAfterShredding) {
            if (!KOHFilesUtil.deleteFileNow(currentFile))
                LOGGER.error("Unable to Shred File : [" + currentFile.getAbsolutePath() + "]\t|\tShredding Failed..!!\n");
        }

    }

    private void shredEntireFile(RandomAccessFile raf, FileToShredBean fileToShredBean) throws IOException, InterruptedException {

        byte[] buffer = new byte[bufferLength];

        System.out.println("\nCurrently Processing : [" + fileToShredBean.getFile().getAbsolutePath() + "]");

        Runnable runnable = () -> {
            /*
                Time Stamp : 22nd August 2K19, 12:56 AM..!!
                sharedCurrentFilePointer -> value of i i.e. current Pos.
                        Following Condition :
                (sharedCurrentFilePointer + buffer.length > fileLength) == true
                only when the Main Thread has completed the Processing.
             */
            while (fileToShredBean.getFilePointer() + buffer.length < fileToShredBean.getFileLength()) {
                System.out.print((fileToShredBean.getFilePointer() * 100 / fileToShredBean.getFileLength()) + "%");

                try {
                    Thread.sleep(100);  //  Busy Waiting for displayPercentageThread
//                        this.wait(1000);
                    System.out.print("\b\b\b");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("\b\b\b");
            System.out.println("100%\nFile Shredded Successfully!");

        };
        Thread displayPercentageThread = new Thread(runnable);
        displayPercentageThread.start();

        //  DAMAGE Entire File!

        while (fileToShredBean.getFilePointer() != fileToShredBean.getFileLength()) {

            //  Length: 210 |   Buffer: 100
            //  raf: 99      |   FP: 100
            //  raf: 199     |   FP: 200
            //  raf: 210     |   FP: 210

            raf.seek(fileToShredBean.getFilePointer());

            //  Write Buffer to File as long as it can accommodate buffer length without exceeding file length
            if (fileToShredBean.getFilePointer() + buffer.length <= fileToShredBean.getFileLength()) {

                raf.write(buffer);
                fileToShredBean.setFilePointer(fileToShredBean.getFilePointer() + buffer.length);

            } else {
                //  Edge case occurs during End Of File when -> (file pointer + buffer) > file length
                //  Handle edge case by writing only the remaining bytes instead of exceeding the file length

                int tempRemainingBytes = (int) (fileToShredBean.getFileLength() - fileToShredBean.getFilePointer());
                byte[] tempBufferForEndOfFile = new byte[tempRemainingBytes];

                raf.write(tempBufferForEndOfFile);
                fileToShredBean.setFilePointer(fileToShredBean.getFilePointer() + tempRemainingBytes);

            }

        }

        Thread.sleep(20);

        long tempFilePointer = fileToShredBean.getFilePointer() + buffer.length * 2L;
        fileToShredBean.setFilePointer(tempFilePointer);

        displayPercentageThread.join();

    }


    /*private void processDirectory() {

        DirFilesCounter dirFilesCounter = new DirFilesCounter();
        Files.walkFileTree(targetFile.toPath(), dirFilesCounter);

        origFileCount = dirFilesCounter.getFileCount();

        TheDataShredder.DirTreeWalker dirTreeWalker = new TheDataShredder.DirTreeWalker();
        Files.walkFileTree(targetFile.toPath(), dirTreeWalker);

        LOGGER.info("\nFiles Destroyed : " + dirTreeWalker.filesCount);
        LOGGER.info("Dirs. Visited : " + dirTreeWalker.dirsCount);
        LOGGER.info("Failed to Destroy : " + dirTreeWalker.failureCount);
        LOGGER.info("Deleted Files Count : " + dirTreeWalker.deletedFilesCount);

        //  Display list of files' absolute path
        if (listCode != 0)
            dirTreeWalker.displayLists(listCode);

    }*/


}
