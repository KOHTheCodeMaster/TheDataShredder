package com.github.kohthecodemaster.service;

import com.github.kohthecodemaster.bean.AppProperties;
import com.github.kohthecodemaster.bean.FileToShredBean;
import com.github.kohthecodemaster.utils.FileToShredBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataShredderService {

    private AppProperties appProperties;

    private FileToShredBean fileToShredBean;

    private final static Logger LOGGER = LoggerFactory.getLogger(DataShredderService.class);

    @Autowired
    public DataShredderService(AppProperties appProperties) {

        this.appProperties = appProperties;
        this.fileToShredBean = FileToShredBeanFactory.getFileToShredBean(appProperties);

    }

    public boolean validateAppProperties() {

        return appProperties.validateTargetPath();

    }

    public void start() {

        printAppProperties();

//        beginShredding();

    }

    public void printAppProperties() {

        LOGGER.info("printAppProperties() - AppProperties: \n" + appProperties);
        LOGGER.info("printAppProperties() - fileToShredBean: \n" + fileToShredBean);

    }


    /*public void beginShredding() {

        File targetFile = new File(appProperties.getTargetPath());

        if (targetFile.isFile()) processSingleFile(targetFile, appProperties.isFlagDeleteFilesAfterShredding());
//        else if (targetFile.isDirectory()) shredTheDirectory();
        else LOGGER.error("beginShredding() - INVALID Target File Path: " + appProperties.getTargetPath());

    }*/

    /*private void processSingleFile(File targetFile, boolean flagDeleteFilesAfterShredding) {

        //  TODO : Abstract Print Logs when Shredding Files

//        this.fileLength = file.length();
        LOGGER.info("Currently Processing: [" + targetFile.getAbsolutePath() + "]");

        try (RandomAccessFile raf = new RandomAccessFile(targetFile, "rwd")) {

            //  DAMAGE Entire File
            long deleteThresholdInBytes = appProperties.getMinFileSizeDeleteThresholdInMB() * ONE_MB;

            if (appProperties.isFlagDamageEntireFile() || (targetFile.length() < deleteThresholdInBytes)) {
//                damageEntireFile(raf);
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
            if (!KOHFilesUtil.deleteFileNow(targetFile))
                LOGGER.error("Unable to Shred File : [" + targetFile.getAbsolutePath() + "]\t|\tShredding Failed..!!\n");
        }

    }*/

    /*private void damageEntireFile(RandomAccessFile raf) throws IOException, InterruptedException {

        byte[] buffer = new byte[appProperties.getBufferSizeInMB() * ONE_MB];

//        System.out.println("\nCurrently Processing : [" + raf.getAbsolutePath() + "]");

        int sharedCurrentFilePointer = 0;
        Runnable runnable = () -> {
            *//*
                Time Stamp : 22nd August 2K19, 12:56 AM..!!
                sharedCurrentFilePointer -> value of i i.e. current Pos.
                        Following Condition :
                (sharedCurrentFilePointer + buffer.length > fileLength) == true
                only when the Main Thread has completed the Processing.
             *//*
            while (sharedCurrentFilePointer + buffer.length < fileLength) {
                System.out.print((sharedCurrentFilePointer * 100 / fileLength) + "%");

                try {
                    Thread.sleep(1);
//                        this.wait(1000);
                    System.out.print("\b\b\b");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("\b\b\b");
            System.out.println("100%\nFile Destroyed Successfully!");

        };
        Thread displayPercentageThread = new Thread(runnable);
        displayPercentageThread.start();

        //  DAMAGE Entire File!
        for (long i = 0; i < fileLength; i += buffer.length) {
            raf.seek(i);
            raf.write(buffer);
            sharedCurrentFilePointer = i;

        }
        Thread.sleep(20);
        sharedCurrentFilePointer += buffer.length * 2;
        displayPercentageThread.join();

    }*/


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
