package org.zadyraichuk.construction;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zadyraichuk.construction.service.external.AmazonS3Service;

import java.io.*;
import java.net.URL;
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
    void setUpBeforeAll() {
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
        boolean fileExists = aws.isFileExists(BUCKET, "cloud", "jpg");
        assertFalse(fileExists);

        boolean uploaded = aws.uploadFile(BUCKET, "cloud", readFromJPG);
        fileExists = aws.isFileExists(BUCKET, "cloud", "jpg");
        assertTrue(uploaded);
        assertTrue(fileExists);
    }

    @Test
    @Order(3)
    void uploadPNG() {
        boolean fileExists = aws.isFileExists(BUCKET, "planet", "png");
        assertFalse(fileExists);

        boolean uploaded = aws.uploadFile(BUCKET, "planet", readFromPNG);
        fileExists = aws.isFileExists(BUCKET, "planet", "png");
        assertTrue(uploaded);
        assertTrue(fileExists);
    }

    @Test
    @Order(4)
    void loadJPG() {
        boolean fileExists = aws.isFileExists(BUCKET, "cloud", "jpg");
        assertTrue(fileExists);

        URL url = aws.getFileURL(BUCKET, "cloud", "jpg");
        assertNotNull(url);

        quickCopyFile(url, writeToJPG);
    }

    @Test
    @Order(5)
    void loadPNG() {
        boolean fileExists = aws.isFileExists(BUCKET, "planet", "png");
        assertTrue(fileExists);

        URL loaded = aws.getFileURL(BUCKET, "planet", "png");
        assertNotNull(loaded);

        quickCopyFile(loaded, writeToPNG);
    }

    @Test
    @Order(6)
    void getExistedFilesList() {
        List<URL> files = aws.getFileURLs(BUCKET);
        assertNotNull(files);
        assertFalse(files.isEmpty());
        assertEquals(files.size(), 2);
    }

    @Test
    @Order(7)
    void removeFile() {
        boolean fileExists = aws.isFileExists(BUCKET, "cloud", "jpg");
        assertTrue(fileExists);

        aws.removeFile(BUCKET, "cloud", "jpg");
        fileExists = aws.isFileExists(BUCKET, "cloud", "jpg");
        assertFalse(fileExists);
    }

    @Test
    @Order(8)
    void removeBucket() {
        boolean bucketExists = aws.isBucketExists(BUCKET);
        assertTrue(bucketExists);

        aws.removeFile(BUCKET, "planet", "png");
        aws.removeBucket(BUCKET);
        bucketExists = aws.isBucketExists(BUCKET);
        assertFalse(bucketExists);
    }

    @AfterAll
    void setUpAfterAll() {
        if (writeToJPG.exists())
            writeToJPG.delete();
        if (writeToPNG.exists())
            writeToPNG.delete();

        aws.removeFile(BUCKET, readFromJPG.getName(), "jpg");
        aws.removeFile(BUCKET, readFromPNG.getName(), "jpg");
        aws.removeBucket(BUCKET);
    }

    private static void quickCopyFile(URL source, File dest) {
        try (BufferedInputStream bis = new BufferedInputStream(source.openStream());
             FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(bis.readAllBytes());
        } catch (IOException ignored) {}
    }

}
