package org.zadyraichuk.construction;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zadyraichuk.construction.service.aws.AmazonS3Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AmazonS3Test {

    private static final File readFromJPG = new File("src/test/webapp/resources/img/cloud.jpg");
    private static final File readFromPNG = new File("src/test/webapp/resources/img/planet.png");
    private static final File writeToJPG = new File("src/test/webapp/resources/img/cloud_2.jpg");
    private static final File writeToPNG = new File("src/test/webapp/resources/img/planet_2.png");

    private static final String BUCKET = "buildico-aws-test";

    @Autowired
    private AmazonS3Service aws;

    @BeforeAll
    void createEmptyFilesForWriting() {
        if (writeToJPG.exists())
            writeToJPG.delete();
        if (writeToPNG.exists())
            writeToPNG.delete();

        try {
            writeToJPG.createNewFile();
            writeToPNG.createNewFile();
        } catch (IOException ignored) {}
    }

    @Test
    @Order(1)
    void createBucket() {
        boolean bucketExists = aws.isBucketExists(BUCKET);
        assertFalse(bucketExists);

        boolean isCreated = aws.createBucket(BUCKET);
        bucketExists = aws.isBucketExists(BUCKET);
        assertTrue(isCreated);
        assertTrue(bucketExists);

        isCreated = aws.createBucket(BUCKET);
        bucketExists = aws.isBucketExists(BUCKET);
        assertFalse(isCreated);
        assertTrue(bucketExists);
    }

    @Test
    @Order(2)
    void uploadJPG() {
        boolean fileExists = aws.isFileExists(BUCKET, readFromJPG.getName());
        assertFalse(fileExists);

        boolean uploaded = aws.uploadFile(BUCKET, readFromJPG);
        fileExists = aws.isFileExists(BUCKET, readFromJPG.getName());
        assertTrue(uploaded);
        assertTrue(fileExists);
    }

    @Test
    @Order(3)
    void uploadPNG() {
        boolean fileExists = aws.isFileExists(BUCKET, readFromPNG.getName());
        assertFalse(fileExists);

        boolean uploaded = aws.uploadFile(BUCKET, readFromPNG);
        fileExists = aws.isFileExists(BUCKET, readFromPNG.getName());
        assertTrue(uploaded);
        assertTrue(fileExists);
    }

    @Test
    @Order(4)
    void loadJPG() {
        boolean fileExists = aws.isFileExists(BUCKET, readFromJPG.getName());
        assertTrue(fileExists);

        File loaded = aws.loadFile(BUCKET, readFromJPG.getName());
        assertNotNull(loaded);

        quickCopyFile(loaded, writeToJPG);
    }

    @Test
    @Order(5)
    void loadPNG() {
        boolean fileExists = aws.isFileExists(BUCKET, readFromPNG.getName());
        assertTrue(fileExists);

        File loaded = aws.loadFile(BUCKET, readFromPNG.getName());
        assertNotNull(loaded);

        quickCopyFile(loaded, writeToPNG);
    }

    @Test
    @Order(6)
    void getExistedFilesList() {
        List<String> files = aws.getExistedFiles(BUCKET);
        assertNotNull(files);
        assertFalse(files.isEmpty());
        assertTrue(files.contains(readFromPNG.getName()));
        assertTrue(files.contains(readFromJPG.getName()));
    }

    @Test
    @Order(7)
    void removeFile() {
        boolean fileExists = aws.isFileExists(BUCKET, readFromJPG.getName());
        assertTrue(fileExists);

        aws.removeFile(BUCKET, readFromJPG.getName());
        fileExists = aws.isFileExists(BUCKET, readFromJPG.getName());
        assertFalse(fileExists);
    }

    @Test
    @Order(8)
    void removeBucket() {
        boolean bucketExists = aws.isBucketExists(BUCKET);
        assertTrue(bucketExists);

        aws.removeFile(BUCKET, readFromPNG.getName());
        aws.removeBucket(BUCKET);
        bucketExists = aws.isBucketExists(BUCKET);
        assertFalse(bucketExists);
    }

    @AfterAll
    void deleteAllOnLocalAndRemote() {
        if (writeToJPG.exists())
            writeToJPG.delete();
        if (writeToPNG.exists())
            writeToPNG.delete();

        aws.removeFile(BUCKET, readFromJPG.getName());
        aws.removeFile(BUCKET, readFromPNG.getName());
        aws.removeBucket(BUCKET);
    }

    private static void quickCopyFile(File source, File dest) {
        try (FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(fis.readAllBytes());
        } catch (IOException ignored) {}
    }

}
